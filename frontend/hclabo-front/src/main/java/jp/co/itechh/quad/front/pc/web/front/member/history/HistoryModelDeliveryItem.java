/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import jp.co.itechh.quad.front.constant.type.HTypeShipmentStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 注文履歴詳細配送 ModelItem
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class HistoryModelDeliveryItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品合計 */
    private BigDecimal goodsPriceTotal;

    /************************************
     ** お届け先情報
     ************************************/

    /** 氏名(姓) */
    private String receiverLastName;

    /** 氏名(名) */
    private String receiverFirstName;

    /** フリガナ(セイ) */
    private String receiverLastKana;

    /** フリガナ(メイ) */
    private String receiverFirstKana;

    /** メールアドレス */
    private String receiverMail;

    /** 電話番号 */
    private String receiverTel;

    /** 住所-郵便番号1 */
    private String receiverZipCode1;

    /** 住所-郵便番号2 */
    private String receiverZipCode2;

    /** 住所-都道府県 */
    private String receiverPrefecture;

    /** 住所-市区郡 */
    private String receiverAddress1;

    /** 住所-町名 */
    private String receiverAddress2;

    /** 住所-番地 */
    private String receiverAddress3;

    /************************************
     ** 商品情報
     ************************************/
    /** 商品情報 */
    private List<HistoryModelGoodsItem> goodsListItems;

    /************************************
     ** 配送情報
     ************************************/
    /** 配送方法名 */
    private String deliveryMethodName;

    /** お届け希望日 **/
    private String receiverDate;

    /** 希望時間帯 */
    private String receiverTimeZone;

    /** 伝票番号 */
    private String deliveryCode;

    /** 配送備考 */
    private String deliveryNote;

    /** 出荷状態 */
    private HTypeShipmentStatus shipmentStatus;

    /** 配送追跡URL */
    private String deliveryChaseURL;

    /**
     * コンディション<br />
     * お届け希望日が指定されているかどうか
     *
     * @return true..指定 / false..未指定
     */
    public boolean isExistsReceiverDate() {
        return receiverDate != null;
    }

    /**
     * コンディション<br />
     * お届け時間帯が指定されているかどうか
     *
     * @return true..指定 / false..未指定
     */
    public boolean isExistsReceiverTimeZone() {
        return receiverTimeZone != null;
    }

    /**
     * 配送追跡URLを表示するか判定する
     *
     * @return 表示 /true　非表示 /false
     */
    public boolean isDeliveryChaseURLOpen() {
        return StringUtils.isNotEmpty(deliveryChaseURL);
    }
}
