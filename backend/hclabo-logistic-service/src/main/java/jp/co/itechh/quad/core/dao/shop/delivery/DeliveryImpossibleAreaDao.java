/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 配送不可能エリアDao
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface DeliveryImpossibleAreaDao {

    /**
     * インサート
     *
     * @param deliveryImpossibleAreaEntity 配送不可能エリアエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(DeliveryImpossibleAreaEntity deliveryImpossibleAreaEntity);

    /**
     * デリート
     *
     * @param deliveryImpossibleAreaEntity 配送不可能エリアエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(DeliveryImpossibleAreaEntity deliveryImpossibleAreaEntity);

    /**
     * エンティティ取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @param zipCode 郵便番号
     * @return 配送不可能エリアエンティティ
     */
    @Select
    DeliveryImpossibleAreaEntity getEntity(Integer deliveryMethodSeq, String zipCode);

    /**
     * 件数取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 件数
     */
    @Select
    int getCount(Integer deliveryMethodSeq);

    /**
     * 配送不可能エリアDTOリスト取得<br/>
     * 配送方法設定：配送不可能エリア設定画面で利用されることを想定した検索メソッド<br/>
     * 配送不可能エリアとして登録されている住所の一覧を取得する。<br/>
     * 検索条件に都道府県名が指定されている場合には、指定された都道府県名の住所のみを取得する<br/>
     *
     * @param conditionDto 検索条件Dto
     * @param selectOptions 検索オプション
     * @return 配送不可能エリアDTOリスト
     */
    @Select
    List<DeliveryImpossibleAreaResultDto> getDeliveryImpossibleAreaListInAddress(DeliveryImpossibleAreaConditionDto conditionDto,
                                                                                 SelectOptions selectOptions);

    /**
     * 配送不可能エリアエンティティリスト取得
     *
     * @param deliveryMethodSeqList 配送方法SEQリスト
     * @param zipCode 郵便番号
     * @return 配送不可能エリアエンティティリスト
     */
    @Select
    List<DeliveryImpossibleAreaEntity> getDeliveryImpossibleAreaList(List<Integer> deliveryMethodSeqList,
                                                                     String zipCode);

}