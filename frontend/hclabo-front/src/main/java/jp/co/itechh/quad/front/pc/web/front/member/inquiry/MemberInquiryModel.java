/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.inquiry;

import jp.co.itechh.quad.front.pc.web.front.common.inquiry.AbstractInquiryModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * お問い合わせ一覧画面 Model
 *
 * @author kimura
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberInquiryModel extends AbstractInquiryModel {

    /** 問い合わせ一覧画面ページアイテムのリスト */
    private List<MemberInquiryModelItem> memberInquiryModelItems;

    /** 問い合わせ一覧：ページ番号 */
    private String pnum;

    /** 問い合わせ一覧：最大表示件数 */
    private int limit;

    /**
     * 問い合わせ一覧画面ページアイテムのリストの要素が空か否か
     *
     * @return true：リストの要素が空
     */
    public boolean isEmptyMemberInquiryModelItems() {
        if (memberInquiryModelItems == null || memberInquiryModelItems.isEmpty()) {
            return true;
        }
        return false;
    }
}
