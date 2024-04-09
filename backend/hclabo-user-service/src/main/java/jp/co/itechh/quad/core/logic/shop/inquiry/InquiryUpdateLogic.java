/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;

/**
 * 問い合わせ更新ロジック
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryUpdateLogic {

    /**
     * 問い合わせ更新ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    int execute(InquiryEntity inquiryEntity);

}
