/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 配送方法詳細Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class DeliveryMethodDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法エンティティ */
    private DeliveryMethodEntity deliveryMethodEntity;

    /** 配送区分別送料エンティティリスト */
    private List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList;

    /** 特別料金エリア件数 */
    private int deliverySpecialChargeAreaCount;

    /** 配送不可能エリア件数 */
    private int deliveryImpossibleAreaCount;

    /** 配送不可能エリア詳細リスト */
    private List<DeliveryImpossibleAreaResultDto> deliveryImpossibleAreaResultDtoList;

    /** 配送特別料金エリア詳細リスト */
    private List<DeliverySpecialChargeAreaResultDto> deliverySpecialChargeAreaResultDtoList;
}