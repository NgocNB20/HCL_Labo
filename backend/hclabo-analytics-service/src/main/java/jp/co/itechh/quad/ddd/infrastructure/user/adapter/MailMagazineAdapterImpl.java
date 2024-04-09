/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.user.adapter.IMailMagazineAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.MailMagazineSubscriptionFlag;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * メルマガアダプター実装クラス
 */
@Component
public class MailMagazineAdapterImpl implements IMailMagazineAdapter {

    /**
     * メルマガアAPI
     **/
    private final MailMagazineApi mailMagazineApi;

    /**
     * メルマガアダプターHelperクラス
     **/
    private final MailMagazineAdapterHelper mailMagazineAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param mailMagazineApi           メルマガア員API
     * @param mailMagazineAdapterHelper メルマガアダプタHelperクラス
     * @param headerParamsUtil          ヘッダパラメーターユーティル
     */
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
     * メールマガジン購読フラグを取得
     *
     * @param memberInfoSeq  会員SEQ
     * @param memberInfoMail メールアドレス
     * @return メールマガジン購読フラグ
     */
    @Override
    public MailMagazineSubscriptionFlag getMailMagazineSubscriptionFlag(Integer memberInfoSeq, String memberInfoMail) {
        MailmagazineMemberResponse mailmagazineMemberResponse = mailMagazineApi.getByMemberinfoSeq(memberInfoSeq);

        return mailMagazineAdapterHelper.toMailMagazineSubscriptionFlag(
                        memberInfoSeq, memberInfoMail, mailmagazineMemberResponse);
    }
}