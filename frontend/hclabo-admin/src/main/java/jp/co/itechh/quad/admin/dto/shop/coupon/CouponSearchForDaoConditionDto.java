/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.shop.coupon;

import jp.co.itechh.quad.admin.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * クーポン検索条件用DTOクラス。<br />
 * <pre>
 * 検索条件を保持するためのDTOクラス。
 * </pre>
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class CouponSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** クーポン名 */
    private String couponName;

    /** クーポンID */
    private String couponId;

    /** クーポンコード */
    private String couponCode;

    /** クーポン開始日時-From */
    private Timestamp couponStartTimeFrom;

    /** クーポン開始日時-To */
    private Timestamp couponStartTimeTo;

    /** クーポン開始日時-From */
    private Timestamp couponEndTimeFrom;

    /** 検索条件：クーポン開始日時-To */
    private Timestamp couponEndTimeTo;

    /** 対象商品コード */
    private String targetGoodsCode;
}