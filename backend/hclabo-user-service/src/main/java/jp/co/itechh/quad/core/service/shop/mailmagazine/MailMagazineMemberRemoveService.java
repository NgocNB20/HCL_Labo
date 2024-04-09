/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine;

/**
 * メルマガ購読者解除サービス<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberRemoveService {

    /**
     * メルマガ購読者情報更新エラー<br/>
     * <code>MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL</code>
     */
    String MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL = "SSG000401";

    /**
     * サービス実行<br/>
     *
     * @param mail メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @return メルマガ解除結果
     */
    boolean execute(String mail, Integer memberInfoSeq);
}