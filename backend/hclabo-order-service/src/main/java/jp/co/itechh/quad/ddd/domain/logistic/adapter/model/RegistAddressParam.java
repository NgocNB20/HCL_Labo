/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * 住所録登録パラメータ
 */
@Data
public class RegistAddressParam {

    /** 顧客ID */
    private String customerId;

    /** 住所ID */
    private String addressId;

    /** 氏名(姓) */
    private String lastName;

    /** 氏名(名) */
    private String firstName;

    /** フリガナ(姓) */
    private String lastKana;

    /** フリガナ(名) */
    private String firstKana;

    /** 電話番号 */
    private String tel;

    /** 郵便番号 */
    private String zipCode;

    /** 都道府県 */
    private String prefecture;

    /** 住所1 */
    private String address1;

    /** 住所2 */
    private String address2;

    /** 住所3 */
    private String address3;

    /** 配送メモ */
    private String shippingMemo;
}
