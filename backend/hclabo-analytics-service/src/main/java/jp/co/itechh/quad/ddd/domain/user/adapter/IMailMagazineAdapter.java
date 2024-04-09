/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.ddd.domain.user.adapter.model.MailMagazineSubscriptionFlag;

/**
 * メルマガアダプター
 */
public interface IMailMagazineAdapter {

    /**
     * メールマガジン購読フラグを取得
     *
     * @param memberInfoSeq  会員SEQ
     * @param memberInfoMail メールアドレス
     * @return メールマガジン購読フラグ
     */
    MailMagazineSubscriptionFlag getMailMagazineSubscriptionFlag(Integer memberInfoSeq, String memberInfoMail);

}
