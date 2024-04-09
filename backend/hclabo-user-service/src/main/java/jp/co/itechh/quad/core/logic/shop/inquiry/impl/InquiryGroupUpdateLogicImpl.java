/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.inquiry.InquiryGroupDao;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 問い合わせ分類更新
 *
 * @author shibuya
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class InquiryGroupUpdateLogicImpl extends AbstractShopLogic implements InquiryGroupUpdateLogic {

    /** 問い合わせ分類Dao */
    private final InquiryGroupDao inquiryGroupDao;

    @Autowired
    public InquiryGroupUpdateLogicImpl(InquiryGroupDao inquiryGroupDao) {
        this.inquiryGroupDao = inquiryGroupDao;
    }

    /**
     * 問い合わせ分類更新
     *
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 処理件数
     */
    @Override
    public int execute(InquiryGroupEntity inquiryGroupEntity) {
        // パラメータチェック
        checkParam(inquiryGroupEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 更新日時に現在日付をセット
        inquiryGroupEntity.setUpdateTime(dateUtility.getCurrentTime());

        return inquiryGroupDao.update(inquiryGroupEntity);
    }

    /**
     * パラメータチェック
     * (更新時の必須項目がnullでないかチェック)
     *
     * @param inquiryGroupEntity 問い合わせ分類
     */
    protected void checkParam(InquiryGroupEntity inquiryGroupEntity) {
        ArgumentCheckUtil.assertGreaterThanZero("inquiryGroupSeq", inquiryGroupEntity.getInquiryGroupSeq());
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", inquiryGroupEntity.getShopSeq());
        ArgumentCheckUtil.assertNotEmpty("inquiryGroupName", inquiryGroupEntity.getInquiryGroupName());
        ArgumentCheckUtil.assertNotNull("openStatus", inquiryGroupEntity.getOpenStatus());
    }

}