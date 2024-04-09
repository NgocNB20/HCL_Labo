/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.coupon;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * クーポンエンティティクラス。
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "Coupon")
@Data
@Component
@Scope("prototype")
public class CouponEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** クーポンSEQ（FK） */
    @Column(name = "couponSeq")
    @Id
    private Integer couponSeq;

    /** クーポン枝番 */
    @Column(name = "couponVersionNo")
    @Id
    private Integer couponVersionNo;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** クーポンID */
    @Column(name = "couponId")
    private String couponId;

    /** クーポン名 */
    @Column(name = "couponName")
    private String couponName;

    /** クーポン表示名PC */
    @Column(name = "couponDisplayNamePC")
    private String couponDisplayNamePC;

    /** クーポンコード */
    @Column(name = "couponCode")
    private String couponCode;

    /** クーポン開始日時 */
    @Column(name = "couponStartTime")
    private Timestamp couponStartTime;

    /** クーポン終了日時 */
    @Column(name = "couponEndTime")
    private Timestamp couponEndTime;

    /** 割引金額 */
    @Column(name = "discountPrice")
    private BigDecimal discountPrice;

    /** クーポン適用金額 */
    @Column(name = "discountLowerOrderPrice")
    private BigDecimal discountLowerOrderPrice;

    /** 利用回数 */
    @Column(name = "useCountLimit")
    private Integer useCountLimit = 0;

    /** 対象商品 */
    @Column(name = "targetGoods")
    private String targetGoods;

    /** 対象会員 */
    @Column(name = "targetMembers")
    private String targetMembers;

    /** 管理用メモ */
    @Column(name = "memo")
    private String memo;

    /** 管理者ID */
    @Column(name = "administratorId")
    private String administratorId;

    /** 登録日時 */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}