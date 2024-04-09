/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

/**
 * 会員に紐付く問い合わせの存在チェック<br/>
 *
 * @author kawakami
 *
 */
public interface InquiryByMemberCheckLogic {

    /**
     * 会員SEQに紐付く問い合わせの存在をチェックする<br/>
     *
     * @param memberInfoSeq 会員SEQ(メンバー管理番号)
     * @return true:問い合わせあり、false:問い合わせなし
     */
    boolean execute(Integer memberInfoSeq);

}
