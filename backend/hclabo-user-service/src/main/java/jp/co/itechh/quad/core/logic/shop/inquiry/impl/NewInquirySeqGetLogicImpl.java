/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.dao.inquiry.InquiryDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.NewInquirySeqGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新規問い合わせSEQ取得ロジック実装クラス
 *
 * @author watanabe
 * @version $Revision: 1.2 $
 *
 */
@Component
public class NewInquirySeqGetLogicImpl extends AbstractShopLogic implements NewInquirySeqGetLogic {

    /** 問い合わせDao */
    private final InquiryDao inquiryDao;

    @Autowired
    public NewInquirySeqGetLogicImpl(InquiryDao inquiryDao) {
        this.inquiryDao = inquiryDao;
    }

    /**
     * 実行メソッド
     *
     * @return 問い合わせSEQ
     */
    @Override
    public Integer execute() {
        return inquiryDao.getInquirySeqNextVal();
    }

}