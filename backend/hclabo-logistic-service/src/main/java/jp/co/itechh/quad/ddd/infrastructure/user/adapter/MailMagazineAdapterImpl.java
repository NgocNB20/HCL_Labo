/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.user.adapter.IMailMagazineAdapter;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 顧客アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class MailMagazineAdapterImpl implements IMailMagazineAdapter {

    /** メールマガAPI **/
    private final MailMagazineApi mailMagazineApi;

    /** メールマガアダプタHelperクラス **/
    private final MailMagazineAdapterHelper mailMagazineAdapterHelper;

    /** コンストラクタ */
    @Autowired
    public MailMagazineAdapterImpl(MailMagazineApi mailMagazineApi,
                                   MailMagazineAdapterHelper mailMagazineAdapterHelper,
                                   HeaderParamsUtility headerParamsUtil) {
        this.mailMagazineApi = mailMagazineApi;
        this.mailMagazineAdapterHelper = mailMagazineAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.mailMagazineApi.getApiClient());
    }

    /**
     * ユーザーマイクロサービス<br/>
     * メールマガ配信状態一覧取得
     *
     * @param memberId 会員Id
     * @return メールマガ一覧
     */
    @Override
    public List<String> getMailMagazineSendStatus(String memberId) {
        MailmagazineMemberListResponse response = mailMagazineApi.getByMemberInfoMail(memberId);
        return mailMagazineAdapterHelper.toSendStatusList(response);
    }
}