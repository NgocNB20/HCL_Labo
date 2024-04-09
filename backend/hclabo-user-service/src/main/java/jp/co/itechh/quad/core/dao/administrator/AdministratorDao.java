/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.administrator;

import jp.co.itechh.quad.core.dto.administrator.AdministratorSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 運用者Dao
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface AdministratorDao {

    /**
     * インサート
     * @param administratorEntity 運用者情報
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(AdministratorEntity administratorEntity);

    /**
     * アップデート
     * @param administratorEntity 運用者情報
     * @return 更新した数
     */
    @Update
    int update(AdministratorEntity administratorEntity);

    /**
     * デリート
     * @param administratorEntity 運用者情報
     * @return 削除した数
     */
    @Delete
    int delete(AdministratorEntity administratorEntity);

    /**
     * エンティティ取得
     *
     * @param administratorSeq 運用者SEQ
     * @return 運用者エンティティ
     */
    @Select
    AdministratorEntity getEntity(Integer administratorSeq);

    /**
     * id、passwordから運用者情報取得
     *
     * @param shopSeq ショップSEQ
     * @param administratorId 運用者ID
     * @return 運用者エンティティ
     */
    @Select
    AdministratorEntity getEntityById(Integer shopSeq, String administratorId);

    /**
     * 運営者一覧を取得する
     * @param condition 検索条件
     * @return 運営者エンティティリスト
     */
    @Select
    List<AdministratorEntity> getAdminList(AdministratorSearchForDaoConditionDto condition);

    /**
     *
     * 新規管理者SEQ取得<br/>
     * 新たに登録する管理者情報の管理者SEQを取得する。<br/>
     *
     * @return 新規会員SEQ
     */
    @Select
    Integer getAdministratorSeqNextVal();

}