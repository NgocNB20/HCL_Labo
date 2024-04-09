/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

/**
 * お問い合わせ分類取得ロジック
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupGetLogic {
    /**
     * お問い合わせ分類取得
     *
     * @param inquiryGroupSeq お問い合わせ分類SEQ
     * @param shopSeq ショップSEQ
     * @return お問い合わせ分類
     */
    InquiryGroupEntity execute(Integer inquiryGroupSeq, Integer shopSeq);
}
