/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;

/**
 * 問い合わせ取得
 *
 * @author shibuya
 * @author hasimoto (itec) 2017/4/19 チケット #3720 対応
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGetLogic {

    /**
     * 指定の問い合わせSEQに紐付く問い合わせを取得する
     *
     * @param inquirySeq 問い合わせSEQ
     * @param shopSeq ショップSEQ
     * @return 問い合わせエンティティ
     */
    InquiryEntity execute(Integer inquirySeq, Integer shopSeq);

    /**
     * 指定の問い合わせSEQに紐付く問い合わせを取得する（取得できない場合でもｴﾗｰは出しません）
     *　問い合わせ情報の取得
     * @param inquiryCode ご連絡番号
     * @return 問い合わせ詳細Dto
     */
    InquiryDetailsDto execute(String inquiryCode);

    /**
     * 指定の問い合わせSEQに紐付く問い合わせを取得する（取得できない場合でもｴﾗｰは出しません）
     *　問い合わせ情報の取得
     * @param inquirySeq お問い合わせSEQ
     * @return 問い合わせ詳細Dto
     */
    InquiryDetailsDto execute(Integer inquirySeq);

    /**
     * 指定の問い合わせSEQ、問い合わせ者TELに紐付く問い合わせを取得する<br>
     * （取得できない場合でもエラーは出しません）<br>
     * 問い合わせ情報の取得
     *
     * @param inquiryCode ご連絡番号
     * @param inquiryTel 問い合わせ者TEL
     * @return 問い合わせEntity
     */
    InquiryEntity execute(String inquiryCode, String inquiryTel);

}