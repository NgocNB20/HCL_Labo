/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

import java.util.List;

/**
 * 問い合わせ分類リスト更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupListUpdateService {

    /**
     * 問い合わせ分類リスト更新
     *
     * @param  inquiryGroupEntityList 問い合わせ分類リスト
     * @return 処理件数
     */
    int execute(List<InquiryGroupEntity> inquiryGroupEntityList);

}