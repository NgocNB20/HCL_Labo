/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;

/**
 * 配送不可能エリア登録Serviceインターフェース
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliveryImpossibleAreaRegistService {

    /**
     * 配送特別料金エリア登録処理を行います
     *
     * @param entity DeliverySpecialChargeAreaEntity ME!
     *
     * @return int 処理結果
     */
    int execute(DeliveryImpossibleAreaEntity entity);

}