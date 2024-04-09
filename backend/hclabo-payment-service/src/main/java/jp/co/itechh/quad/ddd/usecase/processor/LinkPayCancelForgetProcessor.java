package jp.co.itechh.quad.ddd.usecase.processor;

import com.gmo_pg.g_pay.client.input.SearchTradeMultiInput;
import com.gmo_pg.g_pay.client.output.SearchTradeMultiOutput;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntityService;
import jp.co.itechh.quad.ddd.domain.billing.entity.TargetSalesOrderPaymentDto;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IMultiTradeSearchAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * GMO決済キャンセル漏れ検知Processor
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class LinkPayCancelForgetProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkPayCancelForgetProcessor.class);

    /** GMO決済キャンセル状態 */
    private static final String GMO_PAYMENT_STATUS_CANCEL = "CANCEL";

    /** GMOマルチ決済取引検索アダプター */
    private final IMultiTradeSearchAdapter multiTradeSearchAdapter;

    /** 注文決済ドメインサービス */
    private final OrderPaymentEntityService orderPaymentEntityService;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** コンストラクタ */
    public LinkPayCancelForgetProcessor(OrderPaymentEntityService orderPaymentEntityService,
                                        IMultiTradeSearchAdapter multiTradeSearchAdapter,
                                        INotificationSubAdapter notificationSubAdapter) {
        this.orderPaymentEntityService = orderPaymentEntityService;
        this.multiTradeSearchAdapter = multiTradeSearchAdapter;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * GMO決済キャンセル漏れ検知
     */
    public void processor() throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId(HTypeBatchName.BATCH_LINK_PAY_CANCEL_REMINDER.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_LINK_PAY_CANCEL_REMINDER.getLabel());
        batchLogging.setStartType(HTypeBatchStartType.SCHEDULER.getLabel());
        batchLogging.setInputData(new BatchQueueMessage());
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("GMO決済キャンセル漏れ検知バッチ処理を開始します。");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            // 【１．検索】 対象受注番号一覧を取得
            List<TargetSalesOrderPaymentDto> targetSalesOrderNumberDtoList =
                            orderPaymentEntityService.getTargetSalesOrderNumberList();

            // 対象が０件の場合
            if (targetSalesOrderNumberDtoList.isEmpty()) {
                LOGGER.info("処理対象となる受注はありません。");
            }

            List<TargetSalesOrderPaymentDto> orderNumberCancelReminder = new ArrayList<>();
            int updateCnt = 0;

            for (TargetSalesOrderPaymentDto targetSalesOrderNumberDto : targetSalesOrderNumberDtoList) {
                String orderCode = targetSalesOrderNumberDto.getOrderCode();

                try {

                    // GMOマルチペイメントより決済状況を取得する
                    SearchTradeMultiInput multiInput = new SearchTradeMultiInput();
                    multiInput.setOrderId(orderCode);
                    multiInput.setPayType(targetSalesOrderNumberDto.getPayType());

                    SearchTradeMultiOutput multiOutput = multiTradeSearchAdapter.doSearchTradeMulti(multiInput);
                    if (StringUtils.isEmpty(multiOutput.getStatus())) {
                        LOGGER.error("GMOマルチペイメントより決済状況データを取得できませんでした。");
                        continue;
                    }

                    // 【2.問合せ】
                    HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus = null;

                    if ("CANCEL".equals(multiOutput.getStatus()) || "RETURN".equals(multiOutput.getStatus())
                        || "VOID".equals(multiOutput.getStatus())) {
                        // GMO問合せ結果が「キャンセル」されている場合
                        gmoPaymentCancelStatus = HTypeGmoPaymentCancelStatus.CANCELLED;

                    } else {
                        // GMO問合せ結果が「キャンセル」されていない場合
                        gmoPaymentCancelStatus = HTypeGmoPaymentCancelStatus.UNCANCELLED;
                        // 通知用データに設定
                        orderNumberCancelReminder.add(targetSalesOrderNumberDto);
                    }

                    // 【3.結果】 対象受注の注文決済に結果反映
                    updateCnt += orderPaymentEntityService.updateGmoPaymentCancelStatus(orderCode,
                                                                                        gmoPaymentCancelStatus
                                                                                       );

                } catch (Throwable e) {
                    LOGGER.error("予期せぬエラーが発生しました。", e);
                }
            }

            // 未キャンセル通知用データが存在する場合、メール送信
            if (CollectionUtils.isNotEmpty(orderNumberCancelReminder)) {
                notificationSubAdapter.linkpayCancelReminder(orderNumberCancelReminder);
                reportString.append("GMO決済キャンセル漏れ受注 [" + orderNumberCancelReminder.size() + "]");
            }

            reportString.append("更新件数[").append(updateCnt).append("]で処理が終了しました。");

            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(orderNumberCancelReminder.size());
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {
            reportString.append("処理中に予期せぬエラーが発生したため異常終了しています。");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            LOGGER.warn("処理中に予期せぬエラーが発生したため異常終了しています。", e);
            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("GMO決済キャンセル漏れ検知バッチ処理が終了しました。");
        }
    }
}