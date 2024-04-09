/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReleaseAdministratorErrorMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /** コンストラクタ */
    @Autowired
    public NotificationSubAdapterImpl(NotificationSubApi notificationSubApi,
                                      AsyncService asyncService,
                                      HeaderParamsUtility headerParamsUtil) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.notificationSubApi.getApiClient());
    }

    /**
     * ユーザーマイクロサービス<br/>
     * 在庫解放エラーメール<br/>
     *
     * @param exceptionName 例外エラー名
     */
    @Override
    public void inventoryReleaseError(String exceptionName) {

        StockReleaseAdministratorErrorMailRequest request = new StockReleaseAdministratorErrorMailRequest();
        request.setExceptionName(exceptionName);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {StockReleaseAdministratorErrorMailRequest.class};

        asyncService.asyncService(notificationSubApi, "stockRelease", args, argsClass);
    }

}