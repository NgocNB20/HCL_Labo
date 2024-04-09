/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 配送方法Daoクラス<br/>
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface DeliveryMethodDao {

    /**
     * インサート<br/>
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(DeliveryMethodEntity deliveryMethodEntity);

    /**
     * アップデート<br/>
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return 処理件数
     */
    @Update
    int update(DeliveryMethodEntity deliveryMethodEntity);

    /**
     * エンティティ取得<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法エンティティ
     */
    @Select
    DeliveryMethodEntity getEntity(Integer deliveryMethodSeq);

    /**
     * 配送方法詳細DTOリスト取得
     *
     * @param conditionDto 配送方法Dao用検索条件DTO
     * @return 配送方法詳細DTOリスト
     */
    @Select
    List<DeliveryDetailsDto> getSearchDeliveryDetailsList(DeliverySearchForDaoConditionDto conditionDto);

    /**
     * 配送方法エンティティリスト取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティリスト(表示順昇順)
     */
    @Select
    List<DeliveryMethodEntity> getAllDeliveryMethodList(Integer shopSeq);

    /**
     *
     * 選択項目用リスト取得<br/>
     *
     * @param deliveryMethodName 配送方法名
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティ
     */
    @Select
    DeliveryMethodEntity getDeliveryMethodByName(String deliveryMethodName, Integer shopSeq);

    /**
     * 表示順の最大値を取得
     *
     * @param shopSeq ショップSEQ
     * @return 表示順の最大値
     */
    @Select
    Integer getMaxOrderDisplay(Integer shopSeq);

    /**
     * 配送方法SEQ採番<br/>
     *
     * @return 配送方法SEQ
     */
    @Select
    int getDeliveryMethodSeqNextVal();

}
