/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.delivery;

import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 配送区分別送料クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class DeliveryMethodTypeCarriageEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ (FK)（必須） */
    private Integer deliveryMethodSeq;

    /** 都道府県種別（必須） */
    private HTypePrefectureType prefectureType;

    /** 上限金額（必須） */
    private BigDecimal maxPrice = new BigDecimal(0);

    /** 送料 */
    private BigDecimal carriage = new BigDecimal(0);

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

}