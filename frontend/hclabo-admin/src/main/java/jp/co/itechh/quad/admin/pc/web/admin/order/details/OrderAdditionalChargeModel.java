/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

/**
 * 追加料金ページ
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderAdditionalChargeModel {

    /** 追加項目名（入力欄） */
    @Length(min = 1, max = 60)
    @NotEmpty
    private String inputAdditionalDetailsName;

    /** 追加金額（入力欄） */
    @HVNumber(minus = true)
    @Digits(integer = 8, fraction = 0, message = "{HNumberLengthValidator.FRACTION_MAX_ZERO_detail}")
    @NotEmpty
    @HCNumber
    private String inputAdditionalDetailsPrice;

}
