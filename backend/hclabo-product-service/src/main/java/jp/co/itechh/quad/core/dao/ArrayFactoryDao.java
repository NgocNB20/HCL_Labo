/*
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao;

import org.seasar.doma.ArrayFactory;
import org.seasar.doma.Dao;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Array;

/**
 * ArrayFactoryDaoクラス
 *
 * @author Thang Doan (VJP)
 */
@Dao
@ConfigAutowireable
public interface ArrayFactoryDao {

    /**
     * データベースのArray作成
     *
     * @param elements 文字列配列
     * @return 配列
     */
    @ArrayFactory(typeName = "varchar")
    Array createStringArray(String[] elements);
}
