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
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * クーポンインデックスエンティティクラス。
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "CouponIndex")
@Data
@Component
@Scope("prototype")
public class CouponIndexEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** クーポンSEQ（FK） */
    @Column(name = "couponSeq")
    @Id
    private Integer couponSeq;

    /** クーポン枝番 */
    @Version
    @Column(name = "couponVersionNo")
    private Integer couponVersionNo;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}