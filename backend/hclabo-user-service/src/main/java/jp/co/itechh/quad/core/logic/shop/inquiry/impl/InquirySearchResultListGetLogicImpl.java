/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDao;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquirySearchResultListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 問い合わせ検索結果リスト取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Component
public class InquirySearchResultListGetLogicImpl extends AbstractShopLogic implements InquirySearchResultListGetLogic {

    /** 問い合わせDao */
    private final InquiryDao inquiryDao;

    @Autowired
    public InquirySearchResultListGetLogicImpl(InquiryDao inquiryDao) {
        this.inquiryDao = inquiryDao;
    }

    /**
     * 問い合わせ検索結果リスト取得
     *
     * @param conditionDto 検索条件
     * @return 問い合わせ検索結果Dtoリスト
     */
    @Override
    public List<InquirySearchResultDto> execute(InquirySearchDaoConditionDto conditionDto) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("InquirySearchDaoConditionDto", conditionDto);

        return inquiryDao.getSearchInquiryForBackList(conditionDto, conditionDto.getPageInfo().getSelectOptions());
    }

    /**
     * 問い合わせ検索結果リスト取得（フロント）
     *
     * @param conditionDto 検索条件
     * @return 問い合わせ検索結果Dtoリスト
     */
    @Override
    public List<InquirySearchResultDto> executeFront(InquirySearchDaoConditionDto conditionDto) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        // 検索条件に追加
        conditionDto.setShopSeq(shopSeq);

        return inquiryDao.getSearchInquiryForFrontList(conditionDto, conditionDto.getPageInfo().getSelectOptions());
    }
}