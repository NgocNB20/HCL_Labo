/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery;

import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 配送方法設定画面表示アイテム
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class DeliveryResultItem implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法エンティティ */
    private DeliveryMethodEntity deliveryMethodEntity;

    /** ラジオボタン */
    private Integer deliveryMethodRadioValue;

    /** 表示順 */
    private Integer orderDisplay;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 配送方法名 */
    private String deliveryMethodName;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC;

    /** 配送方法種別 */
    private HTypeDeliveryMethodType deliveryMethodType;
}
