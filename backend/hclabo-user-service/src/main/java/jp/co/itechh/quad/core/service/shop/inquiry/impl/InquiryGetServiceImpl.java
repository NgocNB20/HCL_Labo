/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDetailDao;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGetLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGetService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 問い合わせ取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class InquiryGetServiceImpl extends AbstractShopService implements InquiryGetService {

    /** 問い合わせ取得 */
    private final InquiryGetLogic inquiryGetLogic;

    /** 問い合わせ分類取得 */
    private final InquiryGroupGetLogic inquiryGroupGetLogic;

    /** 問い合わせ詳細DAO */
    private final InquiryDetailDao inquiryDetailDao;

    @Autowired
    public InquiryGetServiceImpl(InquiryGetLogic inquiryGetLogic,
                                 InquiryGroupGetLogic inquiryGroupGetLogic,
                                 InquiryDetailDao inquiryDetailDao) {

        this.inquiryGetLogic = inquiryGetLogic;
        this.inquiryGroupGetLogic = inquiryGroupGetLogic;
        this.inquiryDetailDao = inquiryDetailDao;
    }

    /**
     * 問い合わせ取得
     *
     * @param inquirySeq 問い合わせSEQ
     * @return 問い合わせ詳細Dto
     */
    @Override
    public InquiryDetailsDto execute(Integer inquirySeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("inquirySeq", inquirySeq);
        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問題なければ取得・作成
        return this.createInquiryDetailsDto(inquirySeq, shopSeq);
    }

    /**
     * 問い合わせ詳細Dto作成
     *
     * @param inquirySeq 問い合わせSEQ
     * @param shopSeq    ショップSEQ
     * @return 問い合わせ詳細Dto
     */
    protected InquiryDetailsDto createInquiryDetailsDto(Integer inquirySeq, Integer shopSeq) {
        // 問い合わせ取得
        InquiryEntity inquiryEntity = inquiryGetLogic.execute(inquirySeq, shopSeq);
        if (ObjectUtils.isEmpty(inquiryEntity)) {
            throwMessage(MSGCD_INQUIRY_GET_FAIL);
        }

        // 問い合わせ分類取得
        InquiryGroupEntity inquiryGroupEntity =
                        inquiryGroupGetLogic.execute(inquiryEntity.getInquiryGroupSeq(), shopSeq);
        if (ObjectUtils.isEmpty(inquiryGroupEntity)) {
            throwMessage(MSGCD_INQUIRYGROUP_GET_FAIL);
        }

        // 問い合わせ詳細Dto作成
        InquiryDetailsDto inquiryDetailsDto = getComponent(InquiryDetailsDto.class);

        inquiryDetailsDto.setInquiryEntity(inquiryEntity);
        inquiryDetailsDto.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());

        // 問い合わせ内容取得
        List<InquiryDetailEntity> list = inquiryDetailDao.getInquiryDetailsListByInquirySeq(
                        inquiryDetailsDto.getInquiryEntity().getInquirySeq());
        if (list == null) {
            return null;
        }
        inquiryDetailsDto.setInquiryDetailEntityList(list);

        return inquiryDetailsDto;
    }
}