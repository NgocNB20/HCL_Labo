/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 配送方法設定画面モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DeliveryModel extends AbstractModel {

    /** 配送方法リスト */
    private List<DeliveryResultItem> deliveryMethodItems;

    /** ラジオボタンのPOSTされたvalue */
    @NotEmpty(message = "移動する配送方法 を選択してください。")
    private String deliveryMethodRadio;

    /** 各ラジオボタンのvalue */
    private Integer deliveryMethodRadioValue;

    /** 表示順 */
    private Integer orderDisplay;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 配送方法名 */
    private String deliveryMethodName;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC;

    /** 公開状態携帯 */
    private HTypeOpenDeleteStatus openStatusMB;

    /** 配送方法種別 */
    private HTypeDeliveryMethodType deliveryMethodType;

    /**
     * コンディション<br/>
     * 配送方法があるかどうか
     *
     * @return true..ある / false..ない
     */
    public boolean isExist() {
        return !CollectionUtil.isEmpty(deliveryMethodItems);
    }

    /**
     * @return the deliveryMethodSeq
     */
    public Integer getDmcd() {
        return deliveryMethodSeq;
    }
}
