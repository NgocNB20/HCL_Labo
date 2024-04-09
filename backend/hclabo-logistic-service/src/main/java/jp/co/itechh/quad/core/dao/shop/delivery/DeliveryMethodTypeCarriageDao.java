/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 配送区分別送料Dao
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface DeliveryMethodTypeCarriageDao {

    /**
     * インサート
     *
     * @param deliveryMethodTypeCarriageEntity 配送区分別送料
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity);

    /**
     * デリート
     *
     * @param deliveryMethodTypeCarriageEntity 配送区分別送料
     * @return 処理件数
     */
    @Delete
    int delete(DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity);

    /**
     * 一括削除
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteList(Integer deliveryMethodSeq);

    /**
     * エンティティリスト取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return エンティティリスト
     */
    @Select
    List<DeliveryMethodTypeCarriageEntity> getEntityList(Integer deliveryMethodSeq);

}
