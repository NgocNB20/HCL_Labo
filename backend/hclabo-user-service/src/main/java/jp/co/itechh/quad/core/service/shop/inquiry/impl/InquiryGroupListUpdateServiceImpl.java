/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupListOrderDisplayUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 問い合わせ分類リスト更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class InquiryGroupListUpdateServiceImpl extends AbstractShopService implements InquiryGroupListUpdateService {

    /** 問い合わせ分類表示順更新ロジック */
    private final InquiryGroupListOrderDisplayUpdateLogic updateLogic;

    @Autowired
    public InquiryGroupListUpdateServiceImpl(InquiryGroupListOrderDisplayUpdateLogic updateLogic) {
        this.updateLogic = updateLogic;
    }

    /**
     * 問い合わせ分類リスト更新
     *
     * @param inquiryGroupEntityList 問い合わせリスト
     * @return 処理件数
     */
    @Override
    public int execute(List<InquiryGroupEntity> inquiryGroupEntityList) {
        // パラメータチェック
        this.checkParam(inquiryGroupEntityList);

        // 問題なければ更新処理
        return updateLogic.execute(inquiryGroupEntityList);
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