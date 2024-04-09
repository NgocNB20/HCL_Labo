/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dto.inquiry.InquiryGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 問い合わせ分類リスト取得サービス
 *
 * @author kimura
 * @version $Revision: 1.2 $
 *
 */
@Service
public class InquiryGroupListGetServiceImpl extends AbstractShopService implements InquiryGroupListGetService {

    /** お問い合わせ分類リスト取得処理ロジッククラス */
    private final InquiryGroupListGetLogic inquiryGroupListGetLogic;

    @Autowired
    public InquiryGroupListGetServiceImpl(InquiryGroupListGetLogic inquiryGroupListGetLogic) {
        this.inquiryGroupListGetLogic = inquiryGroupListGetLogic;
    }

    /**
     * サービス実行
     *
     * @return お問い合わせ情報リスト
     */
    @Override
    public List<InquiryGroupEntity> execute() {

        // 問い合わせ分類情報DAO用検索条件DTO作成
        InquiryGroupSearchForDaoConditionDto inquiryGroupSearchForDaoConditionDto =
                        getComponent(InquiryGroupSearchForDaoConditionDto.class);
        inquiryGroupSearchForDaoConditionDto.setShopSeq(1001);
        inquiryGroupSearchForDaoConditionDto.setOpenStatus(HTypeOpenDeleteStatus.OPEN);

        // 問い合わせ分類情報リスト取得サービス実行
        return inquiryGroupListGetLogic.execute(inquiryGroupSearchForDaoConditionDto);

    }

}