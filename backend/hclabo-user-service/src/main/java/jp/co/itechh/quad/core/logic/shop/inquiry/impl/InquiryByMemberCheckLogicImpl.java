/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.dao.inquiry.InquiryDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryByMemberCheckLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会員に紐付く問い合わせの存在チェック<br/>
 *
 * @author kawakami
 *
 */
@Component
public class InquiryByMemberCheckLogicImpl extends AbstractShopLogic implements InquiryByMemberCheckLogic {

    /** 問い合わせDao */
    private final InquiryDao inquiryDao;

    @Autowired
    public InquiryByMemberCheckLogicImpl(InquiryDao inquiryDao) {
        this.inquiryDao = inquiryDao;
    }

    /**
     * 会員SEQに紐付く問い合わせの存在をチェックする<br/>
     *
     * @param memberInfoSeq 会員SEQ(メンバー管理番号)
     * @return true:問い合わせあり、false:問い合わせなし
     */
    @Override
    public boolean execute(Integer memberInfoSeq) {

        // 共通情報よりショップSEQを取得
        Integer shopSeq = 1001;

        // 指定された会員SEQの問い合わせがある場合、「true」を返す
        if (inquiryDao.getInquiryCountByMemberInfoSeq(shopSeq, memberInfoSeq) > 0) {
            return true;
        }
        return false;
    }

}