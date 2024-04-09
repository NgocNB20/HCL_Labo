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
 * お問い合わせ分類リスト取得サービス
 *
 * @author wh4200
 * @version $Revision: 1.3 $
 *
 */
public interface InquiryGroupListGetService {

    /**
     * メソッド概要<br/>
     * メソッドの説明・概要<br/>
     * 対象の問い合わせ分類リストを取得する
     * @param なし
     * @return お問い合わせ分類情報ティティリスト
     */
    List<InquiryGroupEntity> execute();

}