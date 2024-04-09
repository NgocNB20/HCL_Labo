/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.TargetSalesOrderPaymentDto;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportErrorRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.LinkpayCancelReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通知アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class NotificationSubAdapterImpl implements INotificationSubAdapter {

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    private final NotificationSubAdapterHelper notificationSubAdapterHelper;

    /** コンストラクタ */
    @Autowired
    public NotificationSubAdapterImpl(NotificationSubApi notificationSubApi,
                                      AsyncService asyncService,
                                      NotificationSubAdapterHelper notificationSubAdapterHelper,
                                      HeaderParamsUtility headerParamsUtil) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
        this.notificationSubAdapterHelper = notificationSubAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.notificationSubApi.getApiClient());
    }

    /**
     * ユーザーマイクロサービス<br/>
     * オーソリ期限間近注文警告メール<br/>
     *
     * @param result      結果レポート文字列
     * @param subjectFlg  件名切り替え用フラグ true:対象受注あり false:対象受注なし
     * @param mailMessage 正常処理メッセージ(処理内容格納）
     */
    @Override
    public void authExpirationApproaching(String result, boolean subjectFlg, String mailMessage) {

        AuthTimeLimitRequest request = new AuthTimeLimitRequest();
        request.setResult(result);
        request.setSubjectFlg(subjectFlg);
        request.setMailMessage(mailMessage);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {AuthTimeLimitRequest.class};
        asyncService.asyncService(notificationSubApi, "authTimeLimit", args, argsClass);
    }

    /**
     * ユーザーマイクロサービス<br/>
     * オーソリ期限間近注文警告エラーメール<br/>
     *
     * @param errorResultMsg エラー詳細
     */
    @Override
    public void authExpirationApproachingError(String errorResultMsg) {

        AuthTimeLimitErrorRequest request = new AuthTimeLimitErrorRequest();
        request.setErrorResultMsg(errorResultMsg);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {AuthTimeLimitErrorRequest.class};
        asyncService.asyncService(notificationSubApi, "authTimeLimitError", args, argsClass);
    }

    /**
     * ユーザーマイクロサービス<br/>
     * 与信枠解放メール<br/>
     *
     * @param resultDtoListSize 対象受注結果数
     * @param errorCnt          エラー数
     * @param result            完了メッセージ
     * @param result2           完了メッセージ
     */
    @Override
    public void creditLineRelease(Integer resultDtoListSize, Integer errorCnt, String result, String result2) {

        CreditLineReportRequest request = new CreditLineReportRequest();
        request.setResultDtoListSize(resultDtoListSize);
        request.setErrorCnt(errorCnt);
        request.setResult(result);
        request.setResult2(result2);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {CreditLineReportRequest.class};
        asyncService.asyncService(notificationSubApi, "creditLineReport", args, argsClass);
    }

    /**
     * ユーザーマイクロサービス<br/>
     * 与信枠解放エラーメール<br/>
     *
     * @param errorResultMsg エラー詳細
     */
    @Override
    public void creditLineReleaseError(String errorResultMsg) {

        CreditLineReportErrorRequest request = new CreditLineReportErrorRequest();
        request.setExceptionInfo(errorResultMsg);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {CreditLineReportErrorRequest.class};
        asyncService.asyncService(notificationSubApi, "creditLineReportError", args, argsClass);
    }

    /**
     * GMO決済キャンセル漏れメール送信要求 <br/>
     * <p>
     * targetSalesOrderNumberDtoList
     */
    @Override
    public void linkpayCancelReminder(List<TargetSalesOrderPaymentDto> targetSalesOrderNumberDtoList) {
        LinkpayCancelReminderRequest request =
                        notificationSubAdapterHelper.toLinkpayCancelReminderRequest(targetSalesOrderNumberDtoList);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {LinkpayCancelReminderRequest.class};
        asyncService.asyncService(notificationSubApi, "linkpayCancelReminder", args, argsClass);
    }

    /**
     * 請求不整合報告
     *
     * @param settlementMismatchRequest 請求不整合報告リクエスト
     */
    @Override
    public void settlementMisMatch(SettlementMismatchRequest settlementMismatchRequest) {

        Object[] args = new Object[] {settlementMismatchRequest};
        Class<?>[] argsClass = new Class<?>[] {SettlementMismatchRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementMismatch", args, argsClass);
    }
}