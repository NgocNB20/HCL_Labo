package jp.co.itechh.quad.ddd.usecase.card.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import com.gmo_pg.g_pay.client.output.BaseOutput;
import com.gmo_pg.g_pay.client.output.ErrHolder;
import com.gmo_pg.g_pay.client.output.SearchTradeOutput;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.offline.creditline.dao.CreditLineReportBatchDao;
import jp.co.itechh.quad.core.batch.offline.creditline.dto.OrderCreditLineReportBatchResultDto;
import jp.co.itechh.quad.core.batch.offline.creditline.entity.CreditLineReport;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.constant.type.HTypeJobCode;
import jp.co.itechh.quad.core.dto.common.CheckMessageDto;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CommunicateUtility;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPaymentService;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.card.consumer.CreditLineReleaseConsumer;
import jp.co.itechh.quad.ddd.usecase.card.consumer.CreditLineReleaseTargetDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * 与信枠解放 Consumer<br/>
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class CreditLineReleaseProcessor {
    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CreditLineReleaseConsumer.class);

    /** 通信処理中エラー発生時 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000061";

    /** 通信中に例外発生 */
    public static final String ALTERTRAN_COM_ERR_MSG_ID = "LMC000021";

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** クレジットカード決済 値オブジェクト ドメインサービス */
    private final CreditPaymentService creditPaymentService;

    /** 与信枠解放Dao ※フェーズ1のものをそのまま流用 */
    private final CreditLineReportBatchDao creditLineReportDao;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 通信関連Utility */
    private final CommunicateUtility communicateUtility;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /**
     * メール本文に記載するメッセージのフォーマット
     * <pre>
     * {0} = 受注件数
     * {1} = オーダーID
     * {2} = メッセージ
     * </pre>
     */
    protected static final String MAIL_MSG_FMT = "{0}件目 {1} {2}\r\n";

    /**
     * GMOのエラーコードから生成するメッセージのフォーマット
     * <pre>
     * {0} = エラーコード（GMOのレスポンス）
     * {1} = エラー詳細コード（GMOのレスポンス）
     * {2} = メッセージ（エラー詳細コードに対応するメッセージをHIT-MALLで定義）
     * </pre>
     */
    protected static final String GMO_MSG_FMT = "{0}:{1}:{2}";

    /** 処理結果 */
    protected List<OrderCreditLineReportBatchResultDto> batchResultDtoList = new ArrayList<>();

    /** コンストラクタ */
    @Autowired
    public CreditLineReleaseProcessor(Environment environment) {
        this.mulPayProxyService = ApplicationContextUtility.getBean(MulPayProxyService.class);
        this.creditPaymentService = ApplicationContextUtility.getBean(CreditPaymentService.class);
        this.creditLineReportDao = ApplicationContextUtility.getBean(CreditLineReportBatchDao.class);
        this.dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        this.communicateUtility = ApplicationContextUtility.getBean(CommunicateUtility.class);
        this.notificationSubAdapter = ApplicationContextUtility.getBean(INotificationSubAdapter.class);
    }

    /**
     * Consumerメソッド <br/>
     * クレジット決済の与信枠を解放
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {
        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId(HTypeBatchName.BATCH_ORDER_CREDITLINE_REPORT.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_ORDER_CREDITLINE_REPORT.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        this.LOGGER.info("与信枠解放バッチ処理を開始します。");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            // (１) 与信枠解放対象の 受注請求リストを取得する

            // データ抽出対象の登録時間を設定 「現在日時 + elapsedTime」より前の受注が抽出対象
            Timestamp thresholdTime = getAmountMinuteTimestamp(
                            Integer.parseInt(PropertiesUtil.getSystemPropertiesValue(
                                            "batch.credit.limit.release.elapsedTime")) * -1,
                            dateUtility.getCurrentTime()
                                                              );
            // データ抽出対象の登録時間を設定 「現在日付 + specifiedDays(マイナス日付の設定を想定)」より後の受注が抽出対象
            Timestamp specifiedDay = dateUtility.getAdditionalDate(Integer.parseInt(
                            PropertiesUtil.getSystemPropertiesValue("batch.credit.limit.release.specifiedDays")));

            // 与信枠解放対象リストを取得する
            List<CreditLineReleaseTargetDto> creditLineReleaseTargetDtoList =
                            this.mulPayProxyService.getCreditLineReleaseTargetList(thresholdTime, specifiedDay);

            // （２） 受注単位に取引状態参照、取引取消を実行する
            for (CreditLineReleaseTargetDto resultDto : creditLineReleaseTargetDtoList) {
                AssertChecker.assertNotNull("resultDto is null", resultDto);

                LOGGER.info(resultDto.getOrderId() + "の処理開始");

                OrderCreditLineReportBatchResultDto batchResultDto =
                                ApplicationContextUtility.getBean(OrderCreditLineReportBatchResultDto.class);
                batchResultDto.setOrderId(resultDto.getOrderId());

                AssertChecker.assertNotNull("batchResultDto is null", batchResultDto);

                execute(resultDto, batchResultDto);
                // 与信枠解放を登録
                try {
                    registCreditLineReport(resultDto.getOrderId());
                } catch (Throwable th) {
                    reportString.append("与信枠解放登録に失敗しました。").append("\n");
                    batchLogging.setProcessCount(null);
                    batchLogging.setReport(reportString);
                    batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

                    LOGGER.error("与信枠解放登録に失敗しました。", th);
                    batchResultDto.addInfoMessage("与信枠解放登録に失敗しました。サーバログを参照してください。");
                } finally {
                    LOGGER.info(resultDto.getOrderId() + "の処理終了");
                }

                if (batchResultDto.hasMessage()) {
                    batchResultDtoList.add(batchResultDto);
                }
            }

            if (CollectionUtil.isEmpty(batchResultDtoList)) {
                // 処理対象が0件 or 通知対象が0件の場合、管理者宛メールの送信は行わない
                LOGGER.info("対象のデータはありませんでした。");
                reportString.append("対象のデータはありませんでした。").append("\n");
            } else {
                // 管理者に通知メールを送信
                sendAdministratorMail();
                LOGGER.info(batchResultDtoList.size() + "件の与信枠解放受注を検出しました。");
                reportString.append(batchResultDtoList.size() + "件の与信枠解放受注を検出しました。詳細は通知メールをご確認ください。");
            }

            batchLogging.setProcessCount(CollectionUtil.isEmpty(batchResultDtoList) ? 0 : batchResultDtoList.size());
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            // バッチ正常終了
            LOGGER.info("与信枠解放バッチ処理が終了しました。");

        } catch (Throwable th) {

            // エラーがあった場合は管理者にメール送信
            sendAdministratorErrorMail(th.getClass().getName());
            reportString.append("処理中に予期せぬエラーが発生したため異常終了しています。").append("\n").append("ロールバックします。");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", th);
            // バッチ異常終了
            LOGGER.error("ロールバックします。");
            throw th;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("与信枠解放終了");

        }
    }

    /**
     * 取引状態参照、取引取消を実行する
     *
     * @param resultDto      与信枠解放対象Dto
     * @param batchResultDto 与信枠解放バッチ結果Dto
     */
    public void execute(CreditLineReleaseTargetDto resultDto, OrderCreditLineReportBatchResultDto batchResultDto) {

        boolean cancelFlag = false;

        // GMOマルチペイメントより請求状況を取得する
        SearchTradeOutput searchTradeOutput = null;
        try {
            searchTradeOutput = this.creditPaymentService.searchTrade(resultDto.getOrderId());
            if (searchTradeOutput.isErrorOccurred()) {
                // GMOから決済情報が取得できなかった場合(エラーが返却される)、注意メッセージを追加する。(GMO受注データ保持期間を超過した受注など)
                // GMOに登録されていない場合のメッセージが優先。
                if (communicateUtility.isNotFound(searchTradeOutput)) {
                    LOGGER.info("GMOに取引は登録されていません。");
                    batchResultDto.addInfoMessage("GMOに取引は登録されていません。");
                } else {
                    // 想定外のエラーコードが含まれている場合
                    String message = createErrorMessage(searchTradeOutput);
                    LOGGER.error("取引の参照に失敗しました。" + message);
                    batchResultDto.addInfoMessage("取引の参照に失敗しました。");
                    batchResultDto.addErrorMessage(message);
                }
            } else {
                // 与信枠が確保されているかどうかを確認する
                if (checkCreditLineReserve(resultDto, searchTradeOutput)) {
                    cancelFlag = true;
                } else {
                    // 与信枠が確保されていない場合は通知しない
                    LOGGER.info("GMOに与信枠は確保されていません。");
                }
            }
        } catch (Throwable th) {
            batchResultDto.addInfoMessage("取引の参照に失敗しました。");
            if (isCommunicateError(th, MSGCD_PAYMENT_COM_FAIL)) {
                LOGGER.error("取引の参照に失敗しました。通信エラーが発生しました。", th);
                batchResultDto.addErrorMessage("通信エラーが発生しました。サーバログを参照してください。");
            } else {
                LOGGER.error("取引の参照に失敗しました。予期せぬエラーが発生しました。", th);
                batchResultDto.addErrorMessage("予期せぬエラーが発生しました。サーバログを参照してください。");
            }
        }

        if (cancelFlag) {
            // 取引取消を実行する
            try {
                MulPayBillEntity mulPayBillEntity =
                                this.mulPayProxyService.getLatestEntityByOrderPaymentId(resultDto.getOrderPaymentId());
                if (StringUtils.isEmpty(mulPayBillEntity.getTranDate())) {
                    // 決済日付が保存されていない場合（EntryTranのレコードのみ登録されている場合など）、参照結果の処理日時を補完
                    mulPayBillEntity.setTranDate(searchTradeOutput.getProcessDate());
                }
                AlterTranOutput output = this.creditPaymentService.cancelGmoPaymentRequest(mulPayBillEntity);
                if (output.isErrorOccurred()) {
                    String message = createErrorMessage(output);
                    LOGGER.error("取引の取り消しに失敗しました。" + message);
                    batchResultDto.addInfoMessage("取引の取り消しに失敗しました。");
                    batchResultDto.addErrorMessage(message);
                } else {
                    LOGGER.info("取引を取り消しました。");
                    batchResultDto.addInfoMessage("取引を取り消しました。");
                }
            } catch (Throwable th) {
                batchResultDto.addInfoMessage("取引の取り消しに失敗しました。");
                if (isCommunicateError(th, ALTERTRAN_COM_ERR_MSG_ID)) {
                    LOGGER.error("取引の取り消しに失敗しました。通信エラーが発生しました。", th);
                    batchResultDto.addErrorMessage("通信エラーが発生しました。サーバログを参照してください。");
                } else {
                    LOGGER.error("取引の取り消しに失敗しました。予期せぬエラーが発生しました。", th);
                    batchResultDto.addErrorMessage("予期せぬエラーが発生しました。サーバログを参照してください。");
                }
            }
        }
    }

    /**
     * 通信エラーかを判定
     *
     * @param th   例外
     * @param code 通信エラー時のメッセージコード
     * @return true = 通信エラー
     */
    protected boolean isCommunicateError(Throwable th, String code) {
        if (th instanceof AppLevelListException) {
            List<AppLevelException> errors = ((AppLevelListException) th).getErrorList();
            for (AppLevelException error : errors) {
                if (error.getMessageCode().startsWith(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * GMOが返却するエラーからメッセージを作成する
     * <pre>
     * 複数ある場合は先頭の１件のみ返却
     * </pre>
     *
     * @param output 出力パラメータ
     * @return エラーメッセージ
     */
    protected String createErrorMessage(BaseOutput output) {
        ErrHolder holder = (ErrHolder) output.getErrList().get(0);
        List<CheckMessageDto> checkMessageDtoList = communicateUtility.checkOutput(output);
        return MessageFormat.format(
                        GMO_MSG_FMT, holder.getErrCode(), holder.getErrInfo(), checkMessageDtoList.get(0).getMessage());
    }

    /**
     * 登録用の与信枠解放を作成する
     *
     * @param orderId オーダーID
     */
    protected void registCreditLineReport(String orderId) {
        CreditLineReport creditLineReport = ApplicationContextUtility.getBean(CreditLineReport.class);

        creditLineReport.setOrderId(orderId);
        creditLineReport.setRegistTime(dateUtility.getCurrentTime());
        creditLineReport.setUpdateTime(creditLineReport.getRegistTime());

        creditLineReportDao.insert(creditLineReport);
    }

    /**
     * メール本文の処理結果詳細を構成する
     *
     * @param errorFlag true エラーメッセージを生成
     * @return ret 処理結果詳細
     */
    protected String constituteMailText(boolean errorFlag) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < batchResultDtoList.size(); i++) {
            OrderCreditLineReportBatchResultDto batchResultDto = batchResultDtoList.get(i);
            List<String> messages;
            if (errorFlag) {
                messages = batchResultDto.getErrorMessages();
            } else {
                messages = batchResultDto.getInfoMessages();
            }
            for (String message : messages) {
                result.append(MessageFormat.format(MAIL_MSG_FMT, i + 1, batchResultDto.getOrderId(), message));
            }
        }
        return result.toString();
    }

    /**
     * 指定された分数　減算された日時のTimestamp型を返します。
     *
     * @param amountMinute 減算する分の量
     * @param date         日時
     * @return 指定された時間分加算または減算された日時のTimestamp
     */
    protected Timestamp getAmountMinuteTimestamp(int amountMinute, Timestamp date) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MINUTE, amountMinute);

        // 基準日数より算出した日付
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 管理者に与信枠解放メールを送信する。
     *
     * @return 成否
     */
    protected boolean sendAdministratorMail() {

        try {
            // メール本文の処理結果詳細を構成する
            String mailBody = constituteMailText(false);

            int errorCnt = getErrorResultCount();
            String mailBody2 = null;

            if (errorCnt != 0) {
                // 取引取消エラーありの場合
                mailBody2 = constituteMailText(true);
            }

            // ユーザーサービスの通知処理を実行
            this.notificationSubAdapter.creditLineRelease(
                            this.batchResultDtoList.size(), errorCnt, mailBody, mailBody2);

            LOGGER.info("管理者へ通知メールを送信しました。");

            return true;

        } catch (Exception e) {

            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);

            return false;
        }
    }

    /**
     * 取引取消エラーが発生した受注件数を取得
     *
     * @return 取引取消エラーが発生した受注件数
     */
    protected int getErrorResultCount() {
        int i = 0;
        for (OrderCreditLineReportBatchResultDto batchResultDto : batchResultDtoList) {
            if (batchResultDto.hasErrorMessage()) {
                i++;
            }
        }
        return i;
    }

    /**
     * 管理者向けエラー通知メールを送信する。
     *
     * @param execptionInfo エラー詳細
     * @return 成否
     */
    protected boolean sendAdministratorErrorMail(final String execptionInfo) {

        try {
            // ユーザーサービスの通知処理を実行
            this.notificationSubAdapter.creditLineReleaseError(execptionInfo);

            LOGGER.info("管理者へエラー通知メールを送信しました。");

            return true;

        } catch (Exception e) {
            LOGGER.warn("管理者へのエラー通知メール送信に失敗しました。", e);

            return false;
        }
    }

    /**
     * GMOの状態より、与信枠が確保されているかどうかを確認する
     *
     * @param resultDto 与信枠解放対象Dto
     * @param outPut    GMO取引参照結果
     * @return false ... 確保なし / true ... 確保あり
     */
    protected boolean checkCreditLineReserve(CreditLineReleaseTargetDto resultDto, SearchTradeOutput outPut) {
        HTypeJobCode jobCd = EnumTypeUtil.getEnumFromValue(HTypeJobCode.class, outPut.getStatus());

        // ExecTranのレコードが存在していない場合 かつ
        // GMOの状態が「未決済」の場合、与信枠は確保されていない
        if (resultDto.getTranExec() == null && HTypeJobCode.UNPROCESSED == jobCd) {
            return false;
        }

        String acs = resultDto.getAcs();

        // acs が「2」の受注は3Dセキュア
        // 3Dセキュアを使用 かつ GMOの状態が「未決済(3D 登録済)」の場合、与信枠は確保されていない
        if ((acs == null || StringUtils.equals(acs, "2")) && HTypeJobCode.AUTHENTICATED == jobCd) {
            return false;
        }

        // GMOの現状態が「VOID」、「RETURN」、「RETURNX」であれば、取消済みである為処理しない
        if (HTypeJobCode.VOID == jobCd || HTypeJobCode.RETURN == jobCd || HTypeJobCode.RETURNX == jobCd) {
            return false;
        }

        // その他は確保されていると判断
        return true;
    }

}