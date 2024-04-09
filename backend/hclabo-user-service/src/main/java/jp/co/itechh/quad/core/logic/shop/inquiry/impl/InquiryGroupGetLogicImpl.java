/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.inquiry.InquiryGroupDao;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * お問い合わせ分類取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Component
public class InquiryGroupGetLogicImpl extends AbstractShopLogic implements InquiryGroupGetLogic {

    /** お問い合わせ分類Dao */
    private final InquiryGroupDao inquiryGroupDao;

    @Autowired
    public InquiryGroupGetLogicImpl(InquiryGroupDao inquiryGroupDao) {
        this.inquiryGroupDao = inquiryGroupDao;
    }

    /**
     * お問い合わせ分類取得
     *
     * @param inquiryGroupSeq お問い合わせ分類SEQ
     * @param shopSeq ショップSEQ
     * @return お問い合わせ分類
     */
    @Override
    public InquiryGroupEntity execute(Integer inquiryGroupSeq, Integer shopSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("iconSeq", inquiryGroupSeq);
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 取得
        return inquiryGroupDao.getEntityByShopSeq(inquiryGroupSeq, shopSeq);
    }
}