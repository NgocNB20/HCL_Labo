/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.inquiry;

import jp.co.itechh.quad.core.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 問い合わせ情報Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface InquiryDao {

    /**
     * インサート
     * @param inquiryEntity 問い合わせ情報
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(InquiryEntity inquiryEntity);

    /**
     * アップデート
     * @param inquiryEntity 問い合わせ情報
     * @return 更新した数
     */
    @Update
    int update(InquiryEntity inquiryEntity);

    /**
     * デリート
     * @param inquiryEntity 問い合わせ情報
     * @return 削除した数
     */
    @Delete
    int delete(InquiryEntity inquiryEntity);

    /**
     * エンティティ取得
     * @param inquirySeq 問い合わせSEQ
     * @return 問い合わせ情報
     */
    @Select
    InquiryEntity getEntity(Integer inquirySeq);

    /**
     * 問い合わせSEQ採番<br/>
     *
     * @return 問い合わせSEQ
     */
    @Select
    int getInquirySeqNextVal();

    /**
     * 問い合わせ検索結果一覧取得(管理画面用)
     *
     * @param conditionDto 問い合わせ検索条件DTO
     * @param selectOptions 検索オプション
     * @return 問い合わせ検索結果DTOリスト
     */
    @Select
    List<InquirySearchResultDto> getSearchInquiryForBackList(InquirySearchDaoConditionDto conditionDto,
                                                             SelectOptions selectOptions);

    /**
     * エンティティ取得
     * @param inquirySeq 問い合わせSEQ
     * @param shopSeq ショップSEQ
     * @return 問い合わせ情報
     */
    @Select
    InquiryEntity getEntityByShopSeq(Integer inquirySeq, Integer shopSeq);

    /**
     * エンティティ取得(注文番号)
     * @param orderCode 注文番号
     * @param shopSeq ショップSEQ
     * @param asc 昇順/降順フラグ
     * @return 問い合わせ情報
     */
    @Select
    List<InquiryEntity> getEntityListByOrderCode(String orderCode, Integer shopSeq, boolean asc);

    /**
     * エンティティ取得(問い合わせコード)
     * @param inquiryCode 問い合わせコード
     * @param shopSeq ショップSEQ
     * @return 問い合わせ情報
     */
    @Select
    InquiryEntity getEntityByInquiryCode(String inquiryCode, Integer shopSeq);

    /**
     * 会員に紐付く問い合わせ件数を取得
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoSeq 会員SEQ(メンバー管理番号)
     * @return 件数
     */
    @Select
    int getInquiryCountByMemberInfoSeq(Integer shopSeq, Integer memberInfoSeq);

    /**
     * エンティティ取得(ご連絡番号、問い合わせ者TEL)
     *
     * @param inquiryCode ご連絡番号
     * @param inquiryTel 問い合わせ者TEL
     * @param shopSeq ショップSEQ
     * @return 問い合わせEntity
     */
    @Select
    InquiryEntity getEntityForInquiryLogin(String inquiryCode, String inquiryTel, Integer shopSeq);

    /**
     * 問い合わせ検索結果一覧取得(フロント画面用)
     *
     * @param conditionDto 問い合わせ検索条件DTO
     * @param selectOptions 検索オプション
     * @return 問い合わせ検索結果DTOリスト
     */
    @Select
    List<InquirySearchResultDto> getSearchInquiryForFrontList(InquirySearchDaoConditionDto conditionDto,
                                                              SelectOptions selectOptions);

}