/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

/**
 * 問い合わせ分類登録
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupRegistService {

    /* メッセージ SSI0009 */
    /**
     * 問い合わせ分類登録失敗エラー<br/>
     * <code>MSGCD_INQUIRYGROUP_REGIST_FAIL</code>
     */
    public static final String MSGCD_INQUIRYGROUP_REGIST_FAIL = "SSI000901";

    /**
     * 問い合わせ分類登録
     *
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 処理件数
     */
    int execute(InquiryGroupEntity inquiryGroupEntity);

}