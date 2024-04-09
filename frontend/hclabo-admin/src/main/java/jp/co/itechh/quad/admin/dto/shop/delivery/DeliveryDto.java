/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.shop.delivery;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配送DTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class DeliveryDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法詳細DTO */
    private DeliveryDetailsDto deliveryDetailsDto;

    /** 送料 */
    private BigDecimal carriage;

    /** 選択区分 */
    private boolean selectClass;

    /** お届け日Dto */
    private ReceiverDateDto receiverDateDto;

    /** 特別配送エリア対象フラグ */
    private boolean specialChargeAreaFlag;
}
