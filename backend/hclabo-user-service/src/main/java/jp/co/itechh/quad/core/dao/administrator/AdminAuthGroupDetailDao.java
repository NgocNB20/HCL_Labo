/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.administrator;

import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 権限グループ詳細 Dao
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface AdminAuthGroupDetailDao {

    /**
     * 権限グループ詳細を登録する
     * @param detail 権限グループ詳細
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(AdminAuthGroupDetailEntity detail);

    /**
     * 指定された権限グループに所属する権限グループ詳細を全て削除する
     * @param adminAuthGroupSeq 権限グループSEQ
     */
    @Delete(sqlFile = true)
    int deleteAll(Integer adminAuthGroupSeq);

    /**
     * 指定された権限グループに所属する権限グループ詳細を全て取得する
     * @param adminAuthGroupSeq 権限グループSEQ
     * @return 権限グループ詳細リスト
     */
    @Select
    List<AdminAuthGroupDetailEntity> getDetailList(Integer adminAuthGroupSeq);

}