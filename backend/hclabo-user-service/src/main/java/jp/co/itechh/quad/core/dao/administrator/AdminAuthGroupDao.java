/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.administrator;

import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 権限グループ Dao
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface AdminAuthGroupDao {
    /**
     * 権限グループを登録する。<br />
     * 権限グループ詳細は登録されない。
     * @param group 権限グループ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(AdminAuthGroupEntity group);

    /**
     * 権限グループを更新する。<br />
     * 権限グループ詳細は更新されない。
     * @param group 権限グループ
     * @return 処理件数
     */
    @Update
    int update(AdminAuthGroupEntity group);

    /**
     * 権限グループを削除する。<br />
     * 権限グループ詳細は削除されない。
     * @param group 権限グループ
     * @return 処理件数
     */
    @Delete
    int delete(AdminAuthGroupEntity group);

    /**
     * 権限グループSEQを使用してエンティティを取得する
     * @param adminAuthGroupSeq 権限グループSEQ
     * @return エンティティ
     */
    @Select
    AdminAuthGroupEntity getEntityBySeq(Integer adminAuthGroupSeq);

    /**
     * ショップSEQと権限グループ表示名称を使用してエンティティを取得する
     * @param shopSeq ショップSEQ
     * @param authGroupDisplayName 権限グループ表示名用
     * @return エンティティ
     */
    @Select
    AdminAuthGroupEntity getEntityByName(Integer shopSeq, String authGroupDisplayName);

    /**
     * ショップ下の権限グループエンティティリストを取得する
     * @param shopSeq ショップSEQ
     * @return エンティティリスト
     */
    @Select
    List<AdminAuthGroupEntity> getEntityList(Integer shopSeq);
}