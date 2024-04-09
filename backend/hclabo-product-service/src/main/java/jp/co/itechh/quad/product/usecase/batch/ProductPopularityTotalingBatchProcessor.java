package jp.co.itechh.quad.product.usecase.batch;

import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupPopularityDao;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupPopularityDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupPopularityEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.ordersearch.presentation.api.param.CountOrderProductRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.CountOrderProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 人気商品ランキング集計バッチ processor
 */
@Component
@Scope("prototype")
public class ProductPopularityTotalingBatchProcessor {

    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPopularityTotalingBatchProcessor.class);

    /** 商品グループ人気Dao */
    private final GoodsGroupPopularityDao goodsGroupPopularityDao;

    /** 受注検索API */
    private final OrderSearchApi orderSearchApi;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 処理時間計測用 開始 */
    private long processStartTime = 0;

    /**
     * コンストラクタ
     */
    public ProductPopularityTotalingBatchProcessor(GoodsGroupPopularityDao goodsGroupPopularityDao,
                                                   OrderSearchApi orderSearchApi,
                                                   DateUtility dateUtility) {
        this.goodsGroupPopularityDao = goodsGroupPopularityDao;
        this.orderSearchApi = orderSearchApi;
        this.dateUtility = dateUtility;
    }

    /**
     * 商品グループ人気の集計、更新を行う
     *
     * @param message メッセージ
     * @throws Exception 例外
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage message) throws Exception {

        LOGGER.info("人気商品ランキング集計開始");

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_PRODUCT_POPULARITY_TOTALING");
        batchLogging.setBatchName("人気商品ランキング集計");
        batchLogging.setStartType(Objects.requireNonNull(
                        EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, message.getStartType())).getLabel());
        batchLogging.setInputData(message);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        AtomicInteger count = new AtomicInteger(0);

        /** properties read 人気商品ランキング集計期間 */
        String totalingPeriodFrom = PropertiesUtil.getSystemPropertiesValue("product.popularity.period.from.month");
        // 取得できなかった場合はエラー
        if (StringUtil.isEmpty(totalingPeriodFrom)) {

            LOGGER.error("人気商品ランキング集計期間を取得できませんでした。 properites : product.popularity.period.from.month");

            reportString.append("人気商品ランキング集計期間を取得できませんでした。").append("\n");
            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(null);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            throw new IOException(reportString.toString());
        }

        // 商品グループ人気の集計、更新処理
        try {

            // 公開状態でない商品の人気カウントを0へ更新
            this.startProcess(); // 処理時間計測
            int updateCount = goodsGroupPopularityDao.updatePopularityOfNonOpenToZero();
            LOGGER.info(GoodsGroupPopularityDao.class.getSimpleName() + ".updatePopularityOfNonOpenToZero"
                        + " processCount:" + updateCount + " " + this.stopProcessAndGetProcessTime() + "ms");

            // 更新対象商品一覧を取得し、分析サービスに依頼して人気カウントを取得して更新する
            this.startProcess();
            try (Stream<GoodsGroupPopularityDetailsDto> popularityDetailsDtoStream = goodsGroupPopularityDao.getOpenGoodsGroupPopularityList()) {

                popularityDetailsDtoStream.forEach(popularityDetailsDto -> {

                    CountOrderProductRequest countOrderProductRequest = new CountOrderProductRequest();
                    countOrderProductRequest.setGoodsGroupCode(popularityDetailsDto.getGoodsGroupCode());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MONTH, Integer.parseInt(totalingPeriodFrom));
                    countOrderProductRequest.setTimeFrom(calendar.getTime());

                    // 商品売上件数を取得
                    CountOrderProductResponse countOrderProductResponse =
                                    orderSearchApi.countOrderProduct(countOrderProductRequest);

                    // 商品売上件数(人気カウント)の更新
                    if (countOrderProductResponse != null) {
                        popularityDetailsDto.setPopularityCount(countOrderProductResponse.getOrderGoodsCount());

                        // 更新用エンティティ作成
                        GoodsGroupPopularityEntity popularityEntity =
                                        ApplicationContextUtility.getBean(GoodsGroupPopularityEntity.class);
                        popularityEntity.setGoodsGroupSeq(popularityDetailsDto.getGoodsGroupSeq());
                        popularityEntity.setPopularityCount(countOrderProductResponse.getOrderGoodsCount());
                        popularityEntity.setRegistTime(popularityDetailsDto.getRegistTime());
                        popularityEntity.setUpdateTime(dateUtility.getCurrentTime());

                        goodsGroupPopularityDao.update(popularityEntity);
                    }

                    count.incrementAndGet();
                });
            }

            // レポート処理
            reportString.append("人気商品ランキング集計終了").append("\n");
            batchLogging.setProcessCount(count.get());
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info(GoodsGroupPopularityDao.class.getSimpleName()
                        + ".updatePopularityOrder - 人気カウント更新 - 正常終了 - 処理時間：" + this.stopProcessAndGetProcessTime()
                        + "ms");

        } catch (Exception e) {

            reportString.append(new Timestamp(System.currentTimeMillis()) + " 例外が発生しました。ロールバックし、処理を終了します。")
                        .append("\n");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.error("例外が発生しました。ロールバックし、処理を終了します。");
            LOGGER.error("エラー情報 ", e);

            throw e;

        } finally {

            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
        }
    }

    /**
     * 計測用 処理開始時間をセット
     */
    private void startProcess() {
        processStartTime = System.currentTimeMillis();
    }

    /**
     * 計測用 処理時間を取得
     *
     * @return 処理時間(ミリ秒)  開始時間がセットされていない場合は -1
     */
    private long stopProcessAndGetProcessTime() {
        if (processStartTime == 0) {
            return -1;
        }
        long processTime = System.currentTimeMillis() - processStartTime;
        processStartTime = 0; // 初期化

        return processTime;
    }
}