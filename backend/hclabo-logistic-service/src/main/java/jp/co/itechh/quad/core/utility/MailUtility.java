package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ZipcodeUpdateRequest;
import org.springframework.stereotype.Component;

/**
 * メールUtility<br/>
 *
 * @author Doan Thang (VTI Japan Co. Ltd)
 *
 */
@Component
public class MailUtility {

    /** Notification Sub Api */
    private final NotificationSubApi notificationSubApi;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * コンストラクタ<br/>
     *
     * @param notificationSubApi
     * @param headerParamsUtil
     * @param asyncService
     */
    public MailUtility(NotificationSubApi notificationSubApi,
                       HeaderParamsUtility headerParamsUtil,
                       AsyncService asyncService) {
        this.notificationSubApi = notificationSubApi;
        this.headerParamsUtil = headerParamsUtil;
        this.asyncService = asyncService;
    }

    /**
     * メール送信
     *
     * @param batchName
     * @param msg
     * @param administratorSeq 運営者Seq
     * @param result
     */
    public void sendMail(String batchName, String msg, Boolean result, Integer administratorSeq) {

        // 管理者SEQにパラメーターを設定する
        headerParamsUtil.setAdministratorSeq(notificationSubApi.getApiClient(), String.valueOf(administratorSeq));

        ZipcodeUpdateRequest zipcodeUpdateRequest = new ZipcodeUpdateRequest();

        zipcodeUpdateRequest.setBatchName(batchName);
        zipcodeUpdateRequest.setMsg(msg);
        zipcodeUpdateRequest.setResult(result);

        Object[] args = new Object[] {zipcodeUpdateRequest};
        Class<?>[] argsClass = new Class<?>[] {ZipcodeUpdateRequest.class};

        asyncService.asyncService(notificationSubApi, "zipcodeUpdate", args, argsClass);
    }
}