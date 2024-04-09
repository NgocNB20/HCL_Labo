/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 注文履歴詳細請求先 ModelItem
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class HistoryModelBillingAddressItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 請求先先情報
     ************************************/

    /** 氏名(姓) */
    private String billingLastName;

    /** 氏名(名) */
    private String billingFirstName;

    /** フリガナ(セイ) */
    private String billingLastKana;

    /** フリガナ(メイ) */
    private String billingFirstKana;

    /** 電話番号 */
    private String billingTel;

    /** 住所-郵便番号1 */
    private String billingZipCode1;

    /** 住所-郵便番号2 */
    private String billingZipCode2;

    /** 住所-都道府県 */
    private String billingPrefecture;

    /** 住所-市区郡 */
    private String billingAddress1;

    /** 住所-町名 */
    private String billingAddress2;

    /** 住所-番地 */
    private String billingAddress3;

}
