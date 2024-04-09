/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 問い合わせ更新サービス
 *
 * @author shibuya
 *
 */
public interface InquiryUpdateService {

    /**
     * 指定された会員が不在エラー<br/>
     * <code>MSGCD_MEMBER_NOEXIST_ERROR</code>
     */
    String MSGCD_MEMBER_NOEXIST_ERROR = "PKG-3720-010-S-";

    /**
     * 問い合わせ更新失敗エラー<br/>
     * <code>MSGCD_INQUIRY_UPDATE_RETRY_FAIL</code>
     */
    String MSGCD_INQUIRY_UPDATE_RETRY_FAIL = "PKG-3720-011-S-";

    /**
     * 問い合わせ会員紐づけロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    MemberInfoEntity executeMemberRegist(InquiryEntity inquiryEntity);

    /**
     * 問い合わせ会員紐づけ解除ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    int executeMemberRegistRelease(InquiryEntity inquiryEntity);

    /**
     * 問い合わせ管理メモ登録ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    int executeMemoRegist(InquiryEntity inquiryEntity);

    /**
     * 問い合わせ状態登録ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    int executeStatusRegist(InquiryEntity inquiryEntity);

}
