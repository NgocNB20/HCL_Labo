/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.settlement;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 決済方法Dao用検索条件DTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class SettlementSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC;

    /** 決済金額 */
    private BigDecimal settlementCharge;

    /** 消費税計算用: 軽減消費税対象金額 */
    private BigDecimal reducedTaxTargetPrice;

    /** 消費税計算用: 軽減税率 */
    private BigDecimal reducedTaxRate;

    /** 消費税計算用: 標準消費税対象金額 */
    private BigDecimal standardTaxTargetPrice;

    /** 消費税計算用: 標準税率 */
    private BigDecimal standardTaxRate;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

}
