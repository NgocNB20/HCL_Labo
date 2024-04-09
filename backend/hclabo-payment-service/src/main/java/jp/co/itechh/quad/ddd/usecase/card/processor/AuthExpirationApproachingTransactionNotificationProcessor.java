package jp.co.itechh.quad.ddd.usecase.card.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.usecase.card.query.ICardQuery;
import jp.co.itechh.quad.ddd.usecase.card.query.model.AuthExpirationApproachingTransactionQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * オーソリ期限切れ間近取引警告通知 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class AuthExpirationApproachingTransactionNotificationProcessor {

    /** ロガー */
    protected static final Logger LOGGER =
                    LoggerFactory.getLogger(AuthExpirationApproachingTransactionNotificationProcessor.class);

    /** クレジットカードクエリー */
    private final ICardQuery cardQuery;

    /** 正常処理メッセージ(処理内容格納） */
    private final StringBuilder mailMessage;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** コンストラクタ */
    @Autowired
    public AuthExpirationApproachingTransactionNotificationProcessor(Environment environment) {
        // バッチタスクテーブルに情報を残す
        this.mailMessage = new StringBuilder();
        this.cardQuery = ApplicationContextUtility.getBean(ICardQuery.class);
        this.notificationSubAdapter = ApplicationContextUtility.getBean(INotificationSubAdapter.class);
    }

    /**
     * Consumerメソッド <br/>
     * オーソリ期限間近の取引に警告通知
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {
        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId(HTypeBatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info(" オーソリ期限切れ間近取引警告通知バッチ処理を開始します。");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            // 処理日の取得
            Timestamp currentDate = dateUtility.getCurrentDate();

            // 警告メール送信開始期間の取得
            String mailSendStartPeriod = PropertiesUtil.getSystemPropertiesValue("mail.send.start.period");

            // オーソリ期限切れ間近取引一覧取得
            List<AuthExpirationApproachingTransactionQueryModel> transactionList =
                            this.cardQuery.getAuthExpirationApproachingTransactionList(currentDate,
                                                                                       mailSendStartPeriod
                                                                                      );

            // 対象受注のチェック
            // 完了通知メールSubject切り替え用フラグ
            boolean subjectFlg = true;

            if (transactionList.isEmpty()) {
                subjectFlg = false;
                LOGGER.info("オーソリ期限切れ間近の受注が0件でした。");
                sendAdministratorMail("オーソリ期限切れ間近の受注はありません。", subjectFlg);

                reportString.append("オーソリ期限切れ間近の受注が0件でした。").append("\n");
                batchLogging.setProcessCount(transactionList.size());
                batchLogging.setReport(reportString);
                batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

                return;
            }

            // 対象の受注がある場合は、通知用に該当の受注リスト情報を作成
            createMailMessage(transactionList, currentDate);

            // 管理者宛に作成した受注リスト情報を送信する
            sendAdministratorMail("下記受注のオーソリ期限が近付いています。", subjectFlg);

            reportString.append("対象受注件数[")
                        .append(transactionList.size())
                        .append("]で処理が終了しました。詳細は通知メールをご確認ください。")
                        .append("\n");
            batchLogging.setProcessCount(transactionList.size());
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {
            // 取得に失敗した場合、管理者宛にエラーメールを送信する
            LOGGER.warn(" オーソリ期限切れ間近取引警告通知バッチ処理中に予期せぬ異常が発生しました。", e);

            // エラーメッセージを成形する
            String errorResultMsg = null;
            if (StringUtils.isNotBlank(e.getMessage())) {
                errorResultMsg = e.getMessage();
            } else {
                errorResultMsg = e.getClass().getName() + "が発生";
            }

            sendAdministratorErrorMail(errorResultMsg);
            reportString.append("オーソリ期限切れ間近取引警告通知バッチ処理中に予期せぬ異常が発生しました。").append("\n");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");
            LOGGER.info(reportString.toString());
            LOGGER.info(" オーソリ期限切れ間近取引警告通知バッチ処理を終了します。");
        }
    }

    /**
     * メール送信用にオーソリ期限切れ間近の取引情報を作成する
     *
     * @param transactionList オーソリ期限切れ間近取引リスト
     * @param currentDate     現在日
     */
    protected void createMailMessage(List<AuthExpirationApproachingTransactionQueryModel> transactionList,
                                     Timestamp currentDate) {

        // 処理対象になった、処理連番、各受注コード、オーソリ保持期限日を表示する。
        int n = 0;
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        for (AuthExpirationApproachingTransactionQueryModel target : transactionList) {
            LOGGER.info("受注番号=" + target.getOrderCode() + "　オーソリ保持期限日：" + dateUtility.formatYmdWithSlash(
                            target.getAuthoryLimitDate()));
            n++;
            mailMessage.append(n);
            mailMessage.append("件目　");
            mailMessage.append("受注番号=");
            mailMessage.append(target.getOrderCode());
            mailMessage.append("　オーソリ保持期限日：");
            mailMessage.append(dateUtility.formatYmdWithSlash(target.getAuthoryLimitDate()));
            // バッチ実行日 = オーソリ保持期限日の場合、「【注意】本日で期限が切れます！」のメッセージを表示する。
            if (dateUtility.compareDate(currentDate, target.getAuthoryLimitDate())) {
                mailMessage.append("　　【注意】本日で期限が切れます！");
            }
            mailMessage.append("\r\n");
        }
    }

    /**
     * 処理が失敗した旨の管理者向けメールを送信する。
     *
     * @param errorResultMsg エラー結果メッセージ
     * @return true:成功、false:失敗
     */
    protected boolean sendAdministratorErrorMail(final String errorResultMsg) {

        try {
            // ユーザーサービスの通知処理を実行
            this.notificationSubAdapter.authExpirationApproachingError(errorResultMsg);

            LOGGER.info("管理者へ通知メールを送信しました。");

            return true;

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);

            return false;
        }
    }

    /**
     * 管理者向け処理完了メールを送信する。
     *
     * @param result     結果レポート文字列
     * @param subjectFlg 件名切り替え用フラグ true:対象受注あり false:対象受注なし
     * @return 成否
     */
    protected boolean sendAdministratorMail(final String result, final boolean subjectFlg) {

        try {
            // ユーザーサービスの通知処理を実行
            this.notificationSubAdapter.authExpirationApproaching(result, subjectFlg, this.mailMessage.toString());

            LOGGER.info("管理者へ通知メールを送信しました。");

            return true;

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);

            return false;
        }
    }

}