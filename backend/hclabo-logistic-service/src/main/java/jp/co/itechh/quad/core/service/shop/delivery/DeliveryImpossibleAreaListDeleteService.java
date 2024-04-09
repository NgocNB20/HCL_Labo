/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;

import java.util.List;

/**
 * 配送不可能エリア削除Serviceインターフェース
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliveryImpossibleAreaListDeleteService {

    /**
     * 配送特別料金エリア情報を削除します
     *
     * @param entityList List&lt;DeliverySpecialChargeAreaEntity&gt;
     *
     * @return int 処理結果
     */
    int execute(List<DeliveryImpossibleAreaEntity> entityList);

}