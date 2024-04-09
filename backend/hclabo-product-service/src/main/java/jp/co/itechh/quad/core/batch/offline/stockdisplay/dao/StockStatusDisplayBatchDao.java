/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.batch.offline.stockdisplay.dao;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 商品グループ在庫表示Daoクラス
 * 前提：バッチでのみ使用するメソッドを定義
 *
 * @author tsumura
 */
@Dao
@ConfigAutowireable
public interface StockStatusDisplayBatchDao {
    /**
     * 商品グループ在庫表示に含まれていない商品を登録します。
     *
     * @param today 現在日付
     * @return 処理件数
     */
    @Insert(sqlFile = true, excludeNull = true)
    int insertNotExists(Timestamp today);

    /**
     * 商品グループ在庫表示テーブルを最新に更新します。
     *
     * @param today 現在日付
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateAll(Timestamp today);

}
