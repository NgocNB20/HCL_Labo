package jp.co.itechh.quad.hclabo.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.hclabo.core.process.AsyncService;
import jp.co.itechh.quad.hclabo.core.web.HeaderParamsUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsEntryRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsNoticeRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamkitReceivedEntryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通知アダプター実装クラス
 */
@Component
public class NotificationSubAdapterImpl implements INotificationSubAdapter {

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** コンストラクタ */
    @Autowired
    public NotificationSubAdapterImpl(NotificationSubApi notificationSubApi,
                                      AsyncService asyncService,
                                      HeaderParamsUtil headerParamsUtil) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.notificationSubApi.getApiClient());
    }

    @Override
    public void examkitReceivedEntry(String message) {
        ExamkitReceivedEntryRequest request = new ExamkitReceivedEntryRequest();
        request.setErrorMailMessage(message);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {ExamkitReceivedEntryRequest.class};

        asyncService.asyncService(notificationSubApi, "examkitReceivedEntry", args, argsClass);
    }

    @Override
    public void examResultsEntry(String message) {
        ExamResultsEntryRequest request = new ExamResultsEntryRequest();
        request.setErrorMailMessage(message);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {ExamResultsEntryRequest.class};

        asyncService.asyncService(notificationSubApi, "examResultsEntry", args, argsClass);
    }

    @Override
    public void examResultsNotice(List<String> orderCodeList) {
        ExamResultsNoticeRequest request = new ExamResultsNoticeRequest();
        request.setOrderCodeList(orderCodeList);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {ExamResultsNoticeRequest.class};

        asyncService.asyncService(notificationSubApi, "examResultsNotice", args, argsClass);
    }
}
