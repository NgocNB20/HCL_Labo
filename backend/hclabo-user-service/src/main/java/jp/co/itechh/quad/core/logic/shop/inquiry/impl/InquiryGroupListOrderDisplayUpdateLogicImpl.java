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
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupListOrderDisplayUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 問い合わせ分類表示順更新
 *
 * @author shibuya
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class InquiryGroupListOrderDisplayUpdateLogicImpl extends AbstractShopLogic
                implements InquiryGroupListOrderDisplayUpdateLogic {

    /** 問い合わせ分類Dao */
    private final InquiryGroupDao inquiryGroupDao;

    @Autowired
    public InquiryGroupListOrderDisplayUpdateLogicImpl(InquiryGroupDao inquiryGroupDao) {
        this.inquiryGroupDao = inquiryGroupDao;
    }

    /**
     * 問い合わせ分類表示順更新
     *
     * @param inquiryGroupEntityList 問い合わせ分類リスト
     * @return 処理件数
     */
    @Override
    public int execute(List<InquiryGroupEntity> inquiryGroupEntityList) {
        // パラメータチェック
        this.checkParam(inquiryGroupEntityList);

        // テーブルロック
        inquiryGroupDao.updateLockTableShareModeNowait();

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // リスト分更新
        int retSum = 0;
        for (InquiryGroupEntity inquiryGroupEntity : inquiryGroupEntityList) {
            Integer inquiryGroupSeq = inquiryGroupEntity.getInquiryGroupSeq();
            Integer shopSeq = inquiryGroupEntity.getShopSeq();
            Integer orderDisplay = inquiryGroupEntity.getOrderDisplay();
            Timestamp updateTime = dateUtility.getCurrentTime();

            int ret = inquiryGroupDao.updateOrderDisplay(inquiryGroupSeq, shopSeq, orderDisplay, updateTime);
            // 0件更新時
            if (ret == 0) {
                throwMessage(MSGCD_INQUIRYGROUP_UPDATE_FAIL);
            }

            retSum = retSum + ret;
        }

        return retSum;
    }

    /**
     * パラメータチェック
     * (更新時の必須項目がnullでないかチェック)
     *
     * @param inquiryGroupEntityList 問い合わせ分類リスト
     */
    protected void checkParam(List<InquiryGroupEntity> inquiryGroupEntityList) {
        ArgumentCheckUtil.assertNotNull("inquiryGroupEntityList", inquiryGroupEntityList);

        for (InquiryGroupEntity inquiryGroupEntity : inquiryGroupEntityList) {
            ArgumentCheckUtil.assertGreaterThanZero("inquiryGroupSeq", inquiryGroupEntity.getInquiryGroupSeq());
            ArgumentCheckUtil.assertGreaterThanZero("shopSeq", inquiryGroupEntity.getShopSeq());
            ArgumentCheckUtil.assertNotEmpty("inquiryGroupName", inquiryGroupEntity.getInquiryGroupName());
            ArgumentCheckUtil.assertNotNull("openStatus", inquiryGroupEntity.getOpenStatus());
        }
    }

}