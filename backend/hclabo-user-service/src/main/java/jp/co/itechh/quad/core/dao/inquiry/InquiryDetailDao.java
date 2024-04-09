/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.inquiry;

import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 問い合わせ内容Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface InquiryDetailDao {

    /**
     * インサート
     * @param inquiryDetailsEntity 問い合わせ内容
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(InquiryDetailEntity inquiryDetailsEntity);

    /**
     * アップデート
     * @param inquiryDetailsEntity 問い合わせ内容
     * @return 更新した数
     */
    @Update
    int update(InquiryDetailEntity inquiryDetailsEntity);

    /**
     * デリート
     * @param inquiryDetailsEntity 問い合わせ内容
     * @return 削除した数
     */
    @Delete
    int delete(InquiryDetailEntity inquiryDetailsEntity);

    /**
     * エンティティ取得
     * @param inquirySeq 問い合わせSEQ
     * @return 問い合わせ情報
     */
    @Select
    InquiryDetailEntity getEntity(Integer inquirySeq);

    /**
     * 問い合わせ内容の取得
     * @param inquirySeq 問い合わせSEQ
     * @return 問い合わせ内容リスト
     */
    @Select
    List<InquiryDetailEntity> getInquiryDetailsListByInquirySeq(Integer inquirySeq);
}