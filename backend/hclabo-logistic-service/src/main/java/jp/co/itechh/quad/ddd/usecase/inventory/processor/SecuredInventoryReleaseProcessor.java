package jp.co.itechh.quad.ddd.usecase.inventory.processor;

import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.usecase.inventory.service.SecuredInventoryReleaseSingleExecuter;
import jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 在庫開放 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class SecuredInventoryReleaseProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SecuredInventoryReleaseProcessor.class);

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 在庫解放処理（新規トランザクション指定） */
    private final SecuredInventoryReleaseSingleExecuter securedInventoryReleaseExecuter;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 登録からの経過時間（分） */
    private final Integer elapsedTime;

    /** コンストラクタ */
    @Autowired
    public SecuredInventoryReleaseProcessor(IShippingSlipRepository shippingSlipRepository,
                                            InventoryProxyService inventoryProxyService,
                                            IProductAdapter productAdapter,
                                            INotificationSubAdapter notificationSubAdapter,
                                            SecuredInventoryReleaseSingleExecuter securedInventoryReleaseExecuter,
                                            DateUtility dateUtility) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.productAdapter = productAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
        this.securedInventoryReleaseExecuter = securedInventoryReleaseExecuter;
        this.dateUtility = dateUtility;
        this.elapsedTime = 20;
    }

    /**
     * Consumerメソッド <br/>
     * 在庫確保状態の在庫を解放する
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId(HTypeBatchName.BATCH_ORDER_RESERVE_STOCK_RELEASE.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_ORDER_RESERVE_STOCK_RELEASE.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("在庫開放開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            // (1) バッチ処理(現在日付 - elapsedTime)
            Timestamp thresholdTime = getAmountMinuteTimestamp(this.elapsedTime, false, dateUtility.getCurrentTime());

            // (2) 在庫解放対象の 配送伝票リストを取得する
            // 対象：一定期間を経過した、配送伝票テーブルのステータスが在庫確保状態の配送伝票
            List<ShippingSlipEntity> shippingSlipEntityList =
                            this.shippingSlipRepository.getSecuredInventoryShippingSlipListTargetElapsedPeriod(
                                            thresholdTime);

            // 対象リストが存在する場合
            if (shippingSlipEntityList != null && shippingSlipEntityList.size() > 0) {

                for (ShippingSlipEntity shippingSlipEntity : shippingSlipEntityList) {
                    // 在庫解放を1件ずつコミット
                    boolean resultFlg = this.securedInventoryReleaseExecuter.execute(shippingSlipEntity);
                    if (resultFlg) {
                        // 商品サービス側の在庫データを更新（API呼び出し先でMQに依頼）
                        List<Integer> goodsSeqList = shippingSlipEntity.getSecuredShippingItemList()
                                                                       .stream()
                                                                       .map(id -> Integer.parseInt(id.getItemId()))
                                                                       .collect(Collectors.toList());
                        try {
                            BatchExecuteResponse batchExecuteResponse =
                                            this.productAdapter.syncUpsertGoodsStockDisplay(goodsSeqList);
                            if (batchExecuteResponse.getExecuteCode() == null || batchExecuteResponse.getExecuteCode()
                                                                                                     .equals(HTypeBatchResult.FAILED.getValue())) {
                                writeLogForSyncUpsertGoodsStockDisplay(shippingSlipEntity);
                            }
                        } catch (HttpServerErrorException | HttpClientErrorException e) {
                            writeLogForSyncUpsertGoodsStockDisplay(shippingSlipEntity);
                        }
                    }
                }
                LOGGER.info("[" + SecuredInventoryReleaseProcessor.class + "]" + shippingSlipEntityList.size()
                            + "件の配送伝票の確保済み在庫を開放しました。");
                reportString.append("登録件数[").append(shippingSlipEntityList.size()).append("]で処理が終了しました。");
                batchLogging.setProcessCount(shippingSlipEntityList.size());
            } else {
                LOGGER.info("[" + SecuredInventoryReleaseProcessor.class + "] 対象のデータはありませんでした。");
                reportString.append("登録件数[0]で処理が終了しました。");
                batchLogging.setProcessCount(0);
            }
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {

            // メール送信
            this.notificationSubAdapter.inventoryReleaseError(e.getClass().getName());

            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());
            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("在庫開放終了");
        }
    }

    /**
     * 指定された時間分加算または減算された日時のTimestamp型を返します。
     *
     * @param amountMinute 加算(減算)する分の量
     * @param plus         加算の場合はtrue、減算の場合はfalse
     * @param date         日時
     * @return 指定された時間分加算または減算された日時のTimestamp
     */
    public static Timestamp getAmountMinuteTimestamp(int amountMinute, boolean plus, Timestamp date) {

        // 減算の場合
        if (!plus) {
            amountMinute = -1 * amountMinute;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MINUTE, amountMinute);

        // 基準日数より算出した日付
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 商品サービス側の在庫データを更新に失敗した場合にログを出力
     *
     * @param shippingSlipEntity
     */
    public void writeLogForSyncUpsertGoodsStockDisplay(ShippingSlipEntity shippingSlipEntity) {
        LOGGER.error("[" + SecuredInventoryReleaseProcessor.class + "]" + "取引IDが"
                     + shippingSlipEntity.getTransactionId() + "の配送商品の商品サービス用在庫データの更新に失敗しました");
    }

}