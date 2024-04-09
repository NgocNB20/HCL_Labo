/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * お問い合わせ分類取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class InquiryGroupGetServiceImpl extends AbstractShopService implements InquiryGroupGetService {

    /** 問い合わせ分類取得ロジック */
    private final InquiryGroupGetLogic inquiryGroupGetLogic;

    @Autowired
    public InquiryGroupGetServiceImpl(InquiryGroupGetLogic inquiryGroupGetLogic) {
        this.inquiryGroupGetLogic = inquiryGroupGetLogic;
    }

    /**
     * お問い合わせ分類取得
     *
     * @param inquiryGroupSeq 問い合わせ分類SEQ
     * @return お問い合わせ分類エンティティ
     */
    @Override
    public InquiryGroupEntity execute(Integer inquiryGroupSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("inquiryGroupSeq", inquiryGroupSeq);

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問題なければ取得
        InquiryGroupEntity entity = inquiryGroupGetLogic.execute(inquiryGroupSeq, shopSeq);
        if (entity == null) {
            throwMessage(MSGCD_INQUIRYGROUP_GET_FAIL);
        }

        return entity;
    }

}