/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.orderperson;

import jp.co.itechh.quad.admin.constant.type.HTypeAddressType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 受注ご注文主クラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderPersonEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注ご注文主連番（必須） */
    private Integer orderPersonVersionNo;

    /** 会員SEQ（必須） */
    private Integer memberInfoSeq;

    /** ご注文主氏名(姓)（必須） */
    private String orderLastName;

    /** ご注文主氏名(名) */
    private String orderFirstName;

    /** ご注文主フリガナ(姓)（必須） */
    private String orderLastKana;

    /** ご注文主フリガナ(名) */
    private String orderFirstKana;

    /** ご注文主住所-郵便番号（必須） */
    private String orderZipCode;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** ご注文主住所-都道府県（必須） */
    private String orderPrefecture;

    /** ご注文主住所-市区郡（必須） */
    private String orderAddress1;

    /** ご注文主住所-町村・番地（必須） */
    private String orderAddress2;

    /** ご注文主住所-それ以降の住所 */
    private String orderAddress3;

    /** ご注文主電話番号（必須） */
    private String orderTel;

    /** ご注文主連絡先電話番号 */
    private String orderContactTel;

    /** ご注文主メールアドレス（必須） */
    private String orderMail;

    /** ご注文主生年月日 */
    private Timestamp orderBirthday;

    /** 年齢 */
    private Integer orderAge;

    /** ご注文主年代 */
    private HTypeOrderAgeType orderAgeType;

    /** ご注文主性別 */
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** アドレス種別（必須） */
    private HTypeAddressType addressType = HTypeAddressType.PC;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}