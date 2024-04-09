/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.dao.linkpay;

import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * リンク決済個別決済手段 マスタDao
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Dao
@ConfigAutowireable
public interface SettlementMethodLinkDao {

    /**
     * 個別決済手段情報取得
     *
     * @param payMethod 決済手段識別子
     * @return 個別決済手段情報取得
     */
    @Select
    SettlementMethodLinkEntity getSettlementMethodLinkByPayMethod(String payMethod);

    /**
     * 個別決済手段情報リスト取得
     * ※paytype降順
     *
     * @return 個別決済手段情報取得リスト
     */
    @Select
    List<SettlementMethodLinkEntity> getList();
}
