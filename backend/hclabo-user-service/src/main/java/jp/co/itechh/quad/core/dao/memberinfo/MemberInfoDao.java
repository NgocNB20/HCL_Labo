/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.dto.memberinfo.MemberCsvDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;
import java.util.stream.Stream;

/**
 * 会員情報DAOクラス<br/>
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface MemberInfoDao {

    /**
     * インサート
     *
     * @param memberInfoEntity 会員情報
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(MemberInfoEntity memberInfoEntity);

    /**
     * アップデート
     *
     * @param memberInfoEntity 会員情報
     * @return 更新件数
     */
    @Update
    int update(MemberInfoEntity memberInfoEntity);

    /**
     * デリート
     *
     * @param memberInfoEntity 会員情報
     * @return 削除件数
     */
    @Delete
    int delete(MemberInfoEntity memberInfoEntity);

    /**
     * エンティティ取得
     *
     * @param memberInfoSeq 会員SEQ
     * @return 会員エンティティ
     */
    @Select
    MemberInfoEntity getEntity(Integer memberInfoSeq);

    /**
     * ショップ会員ユニークIDで会員情報を取得する<br/>
     *
     * @param memberInfoUniqueId 会員ユニークID
     * @return 会員エンティティ
     */
    @Select
    MemberInfoEntity getEntityByMemberInfoUniqueId(String memberInfoUniqueId);

    /**
     * ショップSEQ・会員ID・会員状態から会員情報の取得<br/>
     *
     * @param shopSeq          ショップSEQ
     * @param memberInfoId     会員ID
     * @param memberInfoStatus 会員状態
     * @return 会員エンティティ
     */
    @Select
    MemberInfoEntity getEntityByIdStatus(Integer shopSeq, String memberInfoId, HTypeMemberInfoStatus memberInfoStatus);

    /**
     * 会員SEQと会員状態から会員情報を取得<br/>
     *
     * @param memberInfoSeq    会員SEQ
     * @param memberInfoStatus 会員状態
     * @return 会員エンティティ
     */
    @Select
    MemberInfoEntity getEntityByStatus(Integer memberInfoSeq, HTypeMemberInfoStatus memberInfoStatus);

    /**
     * ログイン日時の更新を行う<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param userAgent     最終ログインユーザーエージェント
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateLogin(Integer memberInfoSeq, String userAgent);

    /**
     * 会員検索一覧取得
     *
     * @param conditionDto  会員検索条件DTO
     * @param selectOptions 検索オプション
     * @return 会員情報管理サイト用リスト
     */
    @Select
    List<MemberInfoForBackDto> getMemberInfoConditionDtoList(MemberInfoSearchForDaoConditionDto conditionDto,
                                                             SelectOptions selectOptions);

    /**
     * CSV出力する。
     *
     * @param memberInfoSeqList 会員SEQリスト
     * @return 出力件数
     */
    @Select
    Stream<MemberCsvDto> exportCsvByMemberInfoSeqList(List<Integer> memberInfoSeqList);

    /**
     * 会員検索全件CSV出力
     *
     * @param conditionDto 会員検索条件DTO
     * @return 出力件数
     */
    @Select
    Stream<MemberCsvDto> exportCsvByMemberInfoSearchForDaoConditionDtoStream(MemberInfoSearchForDaoConditionDto conditionDto);

    /**
     * メールアドレスと会員ステータスで会員情報を取得する<br/>
     *
     * @param memberInfoMail   メールアドレス
     * @param memberInfoStatus 会員ステータス
     * @return 会員エンティティ
     */
    @Select
    MemberInfoEntity getEntityByMailStatus(String memberInfoMail, HTypeMemberInfoStatus memberInfoStatus);

    /**
     * 新規会員SEQ取得<br/>
     * 新たに登録する会員情報の会員SEQを取得する。<br/>
     *
     * @return 新規会員SEQ
     */
    @Select
    int getMemberInfoSeqNextVal();

    /**
     * 会員情報リスト取得（会員ID）
     *
     * @param memberInfoIdList 会員IDリスト
     * @return 会員情報エンティティリスト
     */
    @Select
    List<String> getEntityListByMemberInfoIdList(List<String> memberInfoIdList);
}