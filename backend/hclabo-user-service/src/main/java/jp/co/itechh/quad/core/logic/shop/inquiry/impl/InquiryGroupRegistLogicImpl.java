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
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupRegistLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 問い合わせ分類登録
 *
 * @author shibuya
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class InquiryGroupRegistLogicImpl extends AbstractShopLogic implements InquiryGroupRegistLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryGroupRegistLogicImpl.class);

    /** 問い合わせ分類Dao */
    private final InquiryGroupDao inquiryGroupDao;

    @Autowired
    public InquiryGroupRegistLogicImpl(InquiryGroupDao inquiryGroupDao) {
        this.inquiryGroupDao = inquiryGroupDao;
    }

    /**
     * 問い合わせ分類登録
     *
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 処理件数
     */
    @Override
    public int execute(InquiryGroupEntity inquiryGroupEntity) {
        // パラメータチェック
        checkParam(inquiryGroupEntity);
        Integer inquiryGroupSeq = inquiryGroupDao.getInquiryGroupSeqNextVal();
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録日時、更新日時に現在日付をセット
        Timestamp currentTime = dateUtility.getCurrentTime();
        inquiryGroupEntity.setInquiryGroupSeq(inquiryGroupSeq);
        inquiryGroupEntity.setRegistTime(currentTime);
        inquiryGroupEntity.setUpdateTime(currentTime);

        int count = 0;
        try {
            count = inquiryGroupDao.insertAddOrderDisplay(inquiryGroupEntity);
        } catch (DataAccessException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MSGCD_INQUIRYGROUP_INSERT_FAIL);
        }

        return count;
    }

    /**
     * パラメータチェック
     * (登録時の必須項目がnullでないかチェック)
     *
     * @param inquiryGroupEntity 問い合わせ分類
     */
    protected void checkParam(InquiryGroupEntity inquiryGroupEntity) {
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", inquiryGroupEntity.getShopSeq());
        ArgumentCheckUtil.assertNotEmpty("inquiryGroupName", inquiryGroupEntity.getInquiryGroupName());
        ArgumentCheckUtil.assertNotNull("openStatus", inquiryGroupEntity.getOpenStatus());
    }

}