/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.additional;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 受注追加料金クラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderAdditionalChargeEntity implements Serializable {

    /** 追加明細項目名（必須） */
    private String additionalDetailsName;

    /** 追加明細金額（必須） */
    private BigDecimal additionalDetailsPrice;

}
