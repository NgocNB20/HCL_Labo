/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine;

/**
 * メルマガ購読者登録サービス<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberRegistService {

    /**
     * メルマガ購読者情報登録エラー<br/>
     * <code>MSGCD_MAILMAGAZINEMEMBER_REGIST_FAIL</code>
     */
    String MSGCD_MAILMAGAZINEMEMBER_REGIST_FAIL = "SSG000201";

    /**
     * メルマガ購読者情報更新エラー<br/>
     * <code>MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL</code>
     */
    String MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL = "SSG000202";

    /**
     * メルマガ購読者登録処理<br/>
     *
     * @param mail メールアドレス
     * @param memberinfoSeq 会員SEQ
     * @return 処理件数
     */
    int execute(String mail, Integer memberinfoSeq);
}