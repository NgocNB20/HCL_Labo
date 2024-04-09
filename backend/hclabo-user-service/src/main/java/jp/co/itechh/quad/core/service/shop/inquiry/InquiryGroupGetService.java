/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

/**
 * お問い合わせ分類取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupGetService {

    /* メッセージ SSI0008 */

    /**
     * 問い合わせ取得失敗エラー<br/>
     * <code>MSGCD_INQUIRYGROUP_GET_FAIL</code>
     */
    public static final String MSGCD_INQUIRYGROUP_GET_FAIL = "SSI000801";

    /**
     * お問い合わせ分類取得
     *
     * @param inquiryGroupSeq お問い合わせ分類SEQ
     * @return お問い合わせ分類
     */
    InquiryGroupEntity execute(Integer inquiryGroupSeq);
}