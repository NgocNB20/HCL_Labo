/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry;

import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;

/**
 * 問い合わせ取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGetService {

    /* メッセージ SSI0004 */

    /**
     * 問い合わせ取得失敗エラー<br/>
     * <code>MSGCD_INQUIRY_GET_FAIL</code>
     */
    public static final String MSGCD_INQUIRY_GET_FAIL = "SSI000401";
    /**
     * 問い合わせ分類取得失敗エラー<br/>
     * <code>MSGCD_INQUIRYGROUP_GET_FAIL</code>
     */
    public static final String MSGCD_INQUIRYGROUP_GET_FAIL = "SSI000402";

    /**
     * 問い合わせ取得
     *
     * @param inquirySeq 問い合わせSEQ
     * @return 問い合わせ詳細Dto
     */
    InquiryDetailsDto execute(Integer inquirySeq);
}
