/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

/**
 * 問い合わせ分類登録処理
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupRegistLogic {

    /**
     * 問い合わせおグループ登録失敗<br/>
     * <code>MSGCD_INQUIRYGROUP_INSERT_FAIL</code>
     */
    public static final String MSGCD_INQUIRYGROUP_INSERT_FAIL = "LSI001001";

    /**
     * 問い合わせ分類登録処理
     *
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 処理件数
     */
    int execute(InquiryGroupEntity inquiryGroupEntity);
}