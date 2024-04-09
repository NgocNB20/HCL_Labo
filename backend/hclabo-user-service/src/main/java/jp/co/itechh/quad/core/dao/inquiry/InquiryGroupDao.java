/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.inquiry;

import jp.co.itechh.quad.core.dto.inquiry.InquiryGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 問い合わせ分類Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface InquiryGroupDao {

    /**
     * インサート
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(InquiryGroupEntity inquiryGroupEntity);

    /**
     * アップデート
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 更新した数
     */
    @Update
    int update(InquiryGroupEntity inquiryGroupEntity);

    /**
     * デリート
     * @param inquiryGroupEntity 問い合わせ分類
     * @return 削除した数
     */
    @Delete
    int delete(InquiryGroupEntity inquiryGroupEntity);

    /**
     * エンティティ取得
     * @param inquiryGroupSeq 問い合わせ分類SEQ
     * @return 問い合わせ分類
     */
    @Select
    InquiryGroupEntity getEntity(Integer inquiryGroupSeq);

    /*
     * 追加メソッド
     */

    /**
     * 指定されたショップSEQ、公開状態に該当するデータ件数（count）を取得する<br>
     * <br>
     * ＜検索ルール＞<br>
     * 検索条件設定<br>
     *   お問い合わせ分類情報Dao用検索条件Dto
     *      ショップSEQ：パラメータのshopSeqで完全一致検索<br>
     *      公開状態 ：パラメータのinquiryGroupStatusで完全一致検索<br>
     *      ページ情報：NULLの場合、無視<br>
     * 　　　　　　 NULL以外の場合、パラメータのinquiryGroupOrderDisplay<br>
     * 検索結果<br>
     * 　該当データの件数(count)を返す。<br>
     * 　該当データが存在しない場合は０を返す。<br>
     * @param conditionDto お問い合わせ分類情報Dao用検索条件Dto
     * @return 問い合わせ分類エンティティリスト
     */
    @Select
    List<InquiryGroupEntity> getInquiryGroupList(InquiryGroupSearchForDaoConditionDto conditionDto);

    /**
     * 指定ショップに紐付く全問い合わせ分類を表示順昇順で取得する
     *
     * @param shopSeq ショップSEQ
     * @return 問い合わせ分類エンティティリスト
     */
    @Select
    List<InquiryGroupEntity> getInquiryGroupEntityList(Integer shopSeq);

    /**
     * 新規登録(表示順セット)
     *
     * @param inquiryGroupEntity InquiryGroupEntity
     * @return 処理件数
     */
    @Insert(sqlFile = true, excludeNull = true)
    int insertAddOrderDisplay(InquiryGroupEntity inquiryGroupEntity);

    /**
     * エンティティ取得(ショップSEQ指定)
     *
     * @param inquiryGroupSeq 問い合わせ分類SEQ
     * @param shopSeq         ショップSEQ
     * @return 問い合わせ分類エンティティ
     */
    @Select
    InquiryGroupEntity getEntityByShopSeq(Integer inquiryGroupSeq, Integer shopSeq);

    /**
     * 表示順更新
     *
     * @param inquiryGroupSeq 問い合わせ分類SEQ(更新条件)
     * @param shopSeq         ショップSEQ(更新条件)
     * @param orderDisplay    表示順(更新値)
     * @param updateTime      更新日時(更新値)
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateOrderDisplay(Integer inquiryGroupSeq, Integer shopSeq, Integer orderDisplay, Timestamp updateTime);

    /**
     *
     * テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 新規問い合わせ分類SEQ取得
     *
     * @return 問い合わせ分類SEQ
     */
    @Select
    Integer getInquiryGroupSeqNextVal();
}