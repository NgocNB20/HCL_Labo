package jp.co.itechh.quad.ddd.usecase.novelty;

import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.INoveltyAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentMemberRegistParam;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.NoveltyPresentParam;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.transaction.AddOrderItemToTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.CheckTransactionForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.service.OpenTransactionReviseUseCaseExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.StartTransactionReviseUseCaseExecuter;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ノベルティバッチ Processor
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class NoveltyProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(NoveltyProcessor.class);

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    private final INoveltyAdapter noveltyAdapter;

    private final ISalesSlipAdapter salesSlipAdapter;

    /** 取引改訂開始ユースケース */
    private final StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter;

    /** 改訂用取引に注文商品を追加 ユースケース */
    private final AddOrderItemToTransactionForRevisionUseCase addOrderItemToTransactionForRevisionUseCase;

    /** 改訂取引全体チェックユースケース */
    private final CheckTransactionForRevisionUseCase checkTransactionForRevisionUseCase;

    /** 取引改訂を確定するユースケース 内部ロジック */
    private final OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /**
     * コンストラクタ
     *
     * @param transactionRepository
     * @param transactionForRevisionRepository
     * @param noveltyAdapter
     * @param salesSlipAdapter
     * @param startTransactionReviseUseCaseExecuter
     * @param addOrderItemToTransactionForRevisionUseCase
     * @param checkTransactionForRevisionUseCase
     * @param openTransactionReviseUseCaseExecuter
     * @param orderReceivedRepository
     */
    @Autowired
    public NoveltyProcessor(ITransactionRepository transactionRepository,
                            ITransactionForRevisionRepository transactionForRevisionRepository,
                            INoveltyAdapter noveltyAdapter,
                            ISalesSlipAdapter salesSlipAdapter,
                            StartTransactionReviseUseCaseExecuter startTransactionReviseUseCaseExecuter,
                            AddOrderItemToTransactionForRevisionUseCase addOrderItemToTransactionForRevisionUseCase,
                            CheckTransactionForRevisionUseCase checkTransactionForRevisionUseCase,
                            OpenTransactionReviseUseCaseExecuter openTransactionReviseUseCaseExecuter,
                            IOrderReceivedRepository orderReceivedRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.noveltyAdapter = noveltyAdapter;
        this.salesSlipAdapter = salesSlipAdapter;
        this.startTransactionReviseUseCaseExecuter = startTransactionReviseUseCaseExecuter;
        this.addOrderItemToTransactionForRevisionUseCase = addOrderItemToTransactionForRevisionUseCase;
        this.checkTransactionForRevisionUseCase = checkTransactionForRevisionUseCase;
        this.openTransactionReviseUseCaseExecuter = openTransactionReviseUseCaseExecuter;
        this.orderReceivedRepository = orderReceivedRepository;
    }

    /**
     * Processorメソッド
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public OrderReceivedEntity processor(BatchQueueMessage batchQueueMessage) throws Exception {

        LOGGER.info("ノベルティ商品自動付与を開始します");

        // 戻り値
        OrderReceivedEntity orderReceivedEntityAsync = null;

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {

            // 最新取引IDを取得
            OrderReceivedEntity orderReceivedEntity =
                            orderReceivedRepository.getByOrderCode(new OrderCode(batchQueueMessage.getOrderCode()));
            // 対象受注が存在しない場合はエラー
            if (orderReceivedEntity == null) {
                throw new DomainException(
                                "ORDER-ODER0002-E", new String[] {batchQueueMessage.getOrderCode().toString()});
            }

            // ノベルティに関しての取引取得
            TransactionEntity transactionEntity =
                            transactionRepository.getForNovelty(orderReceivedEntity.getLatestTransactionId());

            if (ObjectUtils.isNotEmpty(transactionEntity)) {

                SalesSlip salesSlip = salesSlipAdapter.getSaleSlip(orderReceivedEntity.getLatestTransactionId());
                List<NoveltyPresentParam> noveltyPresentParamList =
                                noveltyAdapter.noveltyPresentConditionJudgment(transactionEntity,
                                                                               salesSlip.getItemSalesPriceTotal()
                                                                              );

                // 取引改訂を開始する ユースケース呼出し
                String transactionRevisionId = startTransactionReviseUseCaseExecuter.startTransactionReviseInnerLogic(
                                orderReceivedEntity.getLatestTransactionId().getValue());

                // 追加ノベルティ商品件数
                int addNoveltyGoodsCount = 0;
                if (ObjectUtils.isNotEmpty(noveltyPresentParamList)) {
                    addNoveltyGoodsCount = noveltyPresentParamList.size();
                    // 改訂用取引に注文商品を追加
                    for (NoveltyPresentParam noveltyPresentParam : noveltyPresentParamList) {
                        addOrderItemToTransactionForRevisionUseCase.addOrderItemToTransactionForRevision(
                                        transactionRevisionId, noveltyPresentParam.getItemId(),
                                        noveltyPresentParam.getItemCount()
                                                                                                        );
                    }
                }

                // 改訂用取引エンティティの「ノベルティプレゼント判定済フラグ=判定済み」に更新
                TransactionForRevisionEntity transactionForRevisionEntity =
                                transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));

                // 取引が取得できない場合エラー
                if (transactionForRevisionEntity == null) {
                    // 取引取得失敗
                    throw new DomainException("ORDER-TREV0009-E", new String[] {transactionRevisionId});
                }

                // 手数料と送料の再計算フラグをOFFにする
                salesSlipAdapter.updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(
                                new TransactionRevisionId(transactionRevisionId), true, true);

                // 改訂用取引を更新する
                transactionForRevisionEntity.settingNoveltyPresentJudgmentStatus(
                                HTypeNoveltyPresentJudgmentStatus.UDGMENT);
                transactionForRevisionRepository.update(transactionForRevisionEntity);

                // 改訂用取引全体をチェックする
                checkTransactionForRevisionUseCase.checkTransaction(transactionRevisionId, false);

                // 取引改訂を確定する（DB楽観ロックチェックあり）
                orderReceivedEntityAsync = openTransactionReviseUseCaseExecuter.openTransactionReviseInnerLogic(
                                transactionRevisionId, null, HTypeProcessType.NOVELTY_BATCH, false, false, false);

                // 取引改訂楽観ロックエラーを考慮してここで処理
                // ノベルティプレゼント対象会員登録
                if (ObjectUtils.isNotEmpty(noveltyPresentParamList)) {
                    registNoveltyPresentMember(noveltyPresentParamList, transactionEntity,
                                               orderReceivedEntityAsync.getOrderReceivedId().getValue()
                                              );
                }

                // ログ出力設定
                reportString.append("判定済み受注番号：").append(orderReceivedEntity.getOrderCode().getValue()).append("\n");
                reportString.append("ノベルティ付与済み受注件数：").append(addNoveltyGoodsCount).append("件");
            } else {
                reportString.append("受注情報が取得できない").append("\n");
                reportString.append("受注番号：").append(orderReceivedEntity.getOrderCode().getValue());
            }

            LOGGER.info(String.valueOf(reportString));

        } catch (Exception e) {

            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);
            // バッチ異常終了
            LOGGER.error("ロールバックします。");

            throw e;
        } finally {
            LOGGER.info("ノベルティ商品自動付与が終了しました");
        }

        return orderReceivedEntityAsync;
    }

    /**
     * ノベルティプレゼント対象会員登録
     *
     * @param noveltyPresentParamList ノベルティプレゼントパラメーター一覧
     * @param transactionEntity       取引エンティティ
     * @param orderreceivedId         受注ID
     */
    private void registNoveltyPresentMember(List<NoveltyPresentParam> noveltyPresentParamList,
                                            TransactionEntity transactionEntity,
                                            String orderreceivedId) {
        List<NoveltyPresentMemberRegistParam> paramList = new ArrayList<>();
        for (NoveltyPresentParam noveltyPresentParam : noveltyPresentParamList) {
            NoveltyPresentMemberRegistParam param = new NoveltyPresentMemberRegistParam();
            param.setNoveltyPresentConditionId(noveltyPresentParam.getNoveltyPresentConditionId());
            param.setOrderreceivedId(orderreceivedId);
            param.setItemId(noveltyPresentParam.getItemId());
            param.setMemberInfoSeq(Integer.valueOf(transactionEntity.getCustomerId()));
            paramList.add(param);
        }
        noveltyAdapter.noveltyPresentMemberRegist(paramList);
    }

    /**
     * 処理実行後非同期処理
     *
     * @param orderReceivedEntity
     */
    public void asyncAfterProcess(OrderReceivedEntity orderReceivedEntity) {
        if (orderReceivedEntity != null) {
            openTransactionReviseUseCaseExecuter.asyncAfterProcessInnerLogic(orderReceivedEntity);
        }
    }
}