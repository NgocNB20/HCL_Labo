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
 * お問い合わせ分類リスト取得サービス(管理者用)
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupListGetForBackService {

    /**
     * お問い合わせ分類リスト取得サービス(管理者用)
     *
     * @return お問い合わせ分類リスト
     */
    List<InquiryGroupEntity> execute();

}