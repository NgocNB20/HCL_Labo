/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDao;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDetailDao;
import jp.co.itechh.quad.core.dao.inquiry.InquiryGroupDao;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 問い合わせ取得
 *
 * @author shibuya
 * @author hashimoto (itec) 2017/4/19 チケット #3720 対応
 * @version $Revision: 1.1 $
 *
 */
@Component
public class InquiryGetLogicImpl extends AbstractShopLogic implements InquiryGetLogic {

    /** 問い合わせDao */
    private final InquiryDao inquiryDao;

    /** 問い合わせ内容Dao */
    private final InquiryDetailDao inquiryDetailDao;

    /** 問い合わせ分類Dao */
    private final InquiryGroupDao inquiryGroupDao;

    /** 会員情報取得Logic */
    private final MemberInfoGetLogic memberInfoGetLogic;

    @Autowired
    public InquiryGetLogicImpl(InquiryDao inquiryDao,
                               InquiryDetailDao inquiryDetailDao,
                               InquiryGroupDao inquiryGroupDao,
                               MemberInfoGetLogic memberInfoGetLogic) {

        this.inquiryDao = inquiryDao;
        this.inquiryDetailDao = inquiryDetailDao;
        this.inquiryGroupDao = inquiryGroupDao;
        this.memberInfoGetLogic = memberInfoGetLogic;
    }

    /**
     *
     * 指定の問い合わせSEQに紐付く問い合わせを取得する
     *
     * @param inquirySeq 問い合わせコード
     * @param shopSeq ショップSEQ
     * @return 問い合わせエンティティ
     */
    @Override
    public InquiryEntity execute(Integer inquirySeq, Integer shopSeq) {

        // パラメータチェック
        this.checkParam(inquirySeq, shopSeq);

        // 問い合わせ情報
        return inquiryDao.getEntityByShopSeq(inquirySeq, shopSeq);
    }

    /**
     * パラメータチェック
     *
     * @param inquirySeq 問い合わせSEQ
     * @param shopSeq ショップSEQ
     */
    protected void checkParam(Integer inquirySeq, Integer shopSeq) {
        ArgumentCheckUtil.assertGreaterThanZero("inquirySeq", inquirySeq);
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
    }

    /**
     *
     * 指定の問い合わせSEQに紐付く問い合わせを取得する（取得できない場合でもｴﾗｰは出しません）
     *　問い合わせ情報の取得
     *
     * @param inquiryCode ご連絡番号
     * @return 問い合わせエンティティDto
     */
    @Override
    public InquiryDetailsDto execute(String inquiryCode) {

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問い合わせ取得
        InquiryEntity inquiryEntity = inquiryDao.getEntityByInquiryCode(inquiryCode, shopSeq);

        InquiryDetailsDto inquiryDetailsDto = getInquiryDetailEntity(inquiryEntity, shopSeq);

        // 会員情報取得
        if (inquiryDetailsDto != null) {
            inquiryDetailsDto.setMemberInfoEntity(getMemberInfoEntity(inquiryEntity.getMemberInfoSeq()));
        }

        return inquiryDetailsDto;

    }

    /**
     *
     * 指定の問い合わせSEQに紐付く問い合わせを取得する（取得できない場合でもｴﾗｰは出しません）
     *　問い合わせ情報の取得
     *
     * @param inquirySeq お問い合わせSEQ
     * @return 問い合わせエンティティDto
     */
    @Override
    public InquiryDetailsDto execute(Integer inquirySeq) {

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問い合わせ取得
        InquiryEntity inquiryEntity = inquiryDao.getEntityByShopSeq(inquirySeq, shopSeq);

        InquiryDetailsDto inquiryDetailsDto = getInquiryDetailEntity(inquiryEntity, shopSeq);

        // 会員情報取得
        if (inquiryDetailsDto != null) {
            inquiryDetailsDto.setMemberInfoEntity(getMemberInfoEntity(inquiryEntity.getMemberInfoSeq()));
        }

        return inquiryDetailsDto;
    }

    /**
     * 問い合わせエンティティから問い合わせ内容を取得
     *
     * @param inquiryEntity お問い合わせエンティティ
     * @param shopSeq ショップSEQ
     * @return 問い合わせエンティティDto
     */
    protected InquiryDetailsDto getInquiryDetailEntity(InquiryEntity inquiryEntity, Integer shopSeq) {

        if (inquiryEntity == null) {
            return null;
        }
        // 問い合わせ内容取得
        List<InquiryDetailEntity> list =
                        inquiryDetailDao.getInquiryDetailsListByInquirySeq(inquiryEntity.getInquirySeq());
        if (list == null) {
            return null;
        }
        // 問い合わせ分類取得
        InquiryGroupEntity inquiryGroupEntity =
                        inquiryGroupDao.getEntityByShopSeq(inquiryEntity.getInquiryGroupSeq(), shopSeq);

        // 戻り値のDto作成
        InquiryDetailsDto inquiryDetailsDto = ApplicationContextUtility.getBean(InquiryDetailsDto.class);

        // 問い合わせ情報を設定
        inquiryDetailsDto.setInquiryEntity(inquiryEntity);
        // 問い合わせ詳細リストを設定
        inquiryDetailsDto.setInquiryDetailEntityList(list);
        // 問い合わせ分類名を設定
        inquiryDetailsDto.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());

        return inquiryDetailsDto;
    }

    /**
     * 会員情報を取得する<br/>
     * 会員情報SEQが 0 の場合はnull を返す。
     *
     * @param memberInfoSeq 会員情報SEQ
     * @return 会員情報Entity
     */
    protected MemberInfoEntity getMemberInfoEntity(Integer memberInfoSeq) {
        if (memberInfoSeq == 0) {
            return null;
        }

        return memberInfoGetLogic.execute(memberInfoSeq);
    }

    /**
     * 指定の問い合わせSEQ、問い合わせ者TELに紐付く問い合わせを取得する<br>
     * （取得できない場合でもエラーは出しません）<br>
     * 問い合わせ情報の取得
     *
     * @param inquiryCode ご連絡番号
     * @param inquiryTel 問い合わせ者TEL
     * @return 問い合わせEntity
     */
    @Override
    public InquiryEntity execute(String inquiryCode, String inquiryTel) {
        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 問い合わせ取得
        InquiryEntity inquiryEntity = inquiryDao.getEntityForInquiryLogin(inquiryCode, inquiryTel, shopSeq);

        return inquiryEntity;

    }

}