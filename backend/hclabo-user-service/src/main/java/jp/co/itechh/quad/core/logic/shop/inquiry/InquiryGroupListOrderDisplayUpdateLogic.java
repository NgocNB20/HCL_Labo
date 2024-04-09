/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;

import java.util.List;

/**
 * 問い合わせ分類表示順更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface InquiryGroupListOrderDisplayUpdateLogic {

    /* メッセージ LSI0007 */
    /**
     * 問い合わせ分類更新失敗エラー<br/>
     * <code>MSGCD_INQUIRYGROUP_UPDATE_FAIL</code>
     */
    public static final String MSGCD_INQUIRYGROUP_UPDATE_FAIL = "LSI000701";

    /**
     * リスト分、問い合わせ分類の表示順を指定値で更新します。<br />
     * 更新項目
     * <ul>
     *  <li>表示順</li>
     *  <li>更新日付</li>
     * </ul>
     *
     * @param inquiryGroupEntityList 問い合わせ分類エンティティリスト
     * @return 処理件数
     */
    int execute(List<InquiryGroupEntity> inquiryGroupEntityList);

}