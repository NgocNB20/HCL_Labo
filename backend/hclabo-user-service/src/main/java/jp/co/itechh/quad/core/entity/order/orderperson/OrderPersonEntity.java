/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.orderperson;

import jp.co.itechh.quad.core.constant.type.HTypeAddressType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 受注ご注文主クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderPerson")
@Data
@Component
@Scope("prototype")
public class OrderPersonEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注ご注文主連番（必須） */
    @Column(name = "orderPersonVersionNo")
    @Id
    private Integer orderPersonVersionNo;

    /** 会員SEQ（必須） */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq;

    /** ご注文主氏名(姓)（必須） */
    @Column(name = "orderLastName")
    private String orderLastName;

    /** ご注文主氏名(名) */
    @Column(name = "orderFirstName")
    private String orderFirstName;

    /** ご注文主フリガナ(姓)（必須） */
    @Column(name = "orderLastKana")
    private String orderLastKana;

    /** ご注文主フリガナ(名) */
    @Column(name = "orderFirstKana")
    private String orderFirstKana;

    /** ご注文主住所-郵便番号（必須） */
    @Column(name = "orderZipCode")
    private String orderZipCode;

    /** 都道府県種別 */
    @Column(name = "prefectureType")
    private HTypePrefectureType prefectureType;

    /** ご注文主住所-都道府県（必須） */
    @Column(name = "orderPrefecture")
    private String orderPrefecture;

    /** ご注文主住所-市区郡（必須） */
    @Column(name = "orderAddress1")
    private String orderAddress1;

    /** ご注文主住所-町村・番地（必須） */
    @Column(name = "orderAddress2")
    private String orderAddress2;

    /** ご注文主住所-それ以降の住所 */
    @Column(name = "orderAddress3")
    private String orderAddress3;

    /** ご注文主電話番号（必須） */
    @Column(name = "orderTel")
    private String orderTel;

    /** ご注文主連絡先電話番号 */
    @Column(name = "orderContactTel")
    private String orderContactTel;

    /** ご注文主メールアドレス（必須） */
    @Column(name = "orderMail")
    private String orderMail;

    /** ご注文主生年月日 */
    @Column(name = "orderBirthday")
    private Timestamp orderBirthday;

    /** 年齢 */
    @Column(name = "orderAge")
    private Integer orderAge;

    /** ご注文主年代 */
    @Column(name = "orderAgeType")
    private HTypeOrderAgeType orderAgeType;

    /** ご注文主性別 */
    @Column(name = "orderSex")
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** アドレス種別（必須） */
    @Column(name = "addressType")
    private HTypeAddressType addressType = HTypeAddressType.PC;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}