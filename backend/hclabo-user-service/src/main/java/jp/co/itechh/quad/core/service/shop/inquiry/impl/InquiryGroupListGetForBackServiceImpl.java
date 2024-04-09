/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListGetForBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * お問い合わせ分類リスト取得サービス(管理者用)
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class InquiryGroupListGetForBackServiceImpl extends AbstractShopService
                implements InquiryGroupListGetForBackService {

    /** お問い合わせ分類リスト取得ロジック */
    private final InquiryGroupListGetLogic inquiryGroupListGetLogic;

    @Autowired
    public InquiryGroupListGetForBackServiceImpl(InquiryGroupListGetLogic inquiryGroupListGetLogic) {
        this.inquiryGroupListGetLogic = inquiryGroupListGetLogic;
    }

    /**
     * お問い合わせ分類リスト取得サービス(管理者用)
     *
     * @return お問い合わせ分類リスト
     */
    @Override
    public List<InquiryGroupEntity> execute() {

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問題なければ取得
        return inquiryGroupListGetLogic.execute(shopSeq);
    }

}