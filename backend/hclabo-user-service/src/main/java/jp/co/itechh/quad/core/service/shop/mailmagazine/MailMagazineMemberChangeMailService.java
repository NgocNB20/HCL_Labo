/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine;

/**
 * メルマガ購読者メールアドレス変更サービス<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberChangeMailService {

    /**
     * メルマガ購読者情報更新エラー<br/>
     * <code>MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL</code>
     */
    public static final String MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL = "SSG000202";

    /**
     * メルマガ購読者メールアドレス変更処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param mail メールアドレス
     * @param memberinfoSeq 会員SEQ
     * @return 処理件数
     */
    int execute(Integer shopSeq, String mail, Integer memberinfoSeq);
}