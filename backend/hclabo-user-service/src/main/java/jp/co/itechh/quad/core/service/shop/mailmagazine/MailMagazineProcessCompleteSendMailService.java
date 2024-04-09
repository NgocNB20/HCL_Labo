/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;

/**
 * メルマガ登録完了メール送信サービス<br/>
 *
 * @author kimura
 */
public interface MailMagazineProcessCompleteSendMailService {

    /**
     * メルマガ購読者情報取得エラー<br/>
     * <code>MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL</code>
     */
    public static final String MSGCD_MAILMAGAZINEMEMBER_NULL = "SSG000501";

    /**
     * メルマガ登録/解除完了メール送信処理<br/>
     * ※メアド変更前の購読者が、変更後に購読希望しない場合の解除メールの本文にのみ、「変更前メールアドレス」を利用する
     *
     * @param mail メールアドレス
     * @param preMail 変更前メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @param mailTemplateType メールテンプレートタイプ(メルマガ登録完了/メルマガ解除完了)
     * @return boolean 送信結果
     */
    boolean execute(String mail, String preMail, Integer memberInfoSeq, HTypeMailTemplateType mailTemplateType);
}