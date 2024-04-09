/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 決済方法金額別手数料Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface SettlementMethodPriceCommissionDao {

    /**
     * インサート
     *
     * @param settlementMethodPriceCommissionEntity 決済方法金額別手数料エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity);

    /**
     * アップデート
     *
     * @param settlementMethodPriceCommissionEntity 決済方法金額別手数料エンティティ
     * @return 処理件数
     */
    @Update
    int update(SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity);

    /**
     * デリート
     *
     * @param settlementMethodPriceCommissionEntity 決済方法金額別手数料エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity);

    /**
     * エンティティ取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法金額別手数料エンティティリスト
     */
    @Select
    List<SettlementMethodPriceCommissionEntity> getListBySettlementMethodSeq(Integer settlementMethodSeq);

    /**
     * リストデリート
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteListBySettlementMethodSeq(Integer settlementMethodSeq);
}
