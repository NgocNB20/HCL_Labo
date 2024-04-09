/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.constant.type.HTypeTaxRateType;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.admin.entity.shop.tax.TaxRateEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 注文情報マスタDto
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderInfoMasterDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品マスタマップ */
    private Map<Integer, GoodsDetailsDto> goodsMaster;

    /** 配送マスタマップ */
    private Map<Integer, DeliveryMethodDetailsDto> deliveryMethodMaster;

    /** 税率マスタマップ */
    private Map<HTypeTaxRateType, TaxRateEntity> taxRateMaster;

    /**
     * @return 標準税率
     */
    public BigDecimal getTaxRateStandard() {
        return taxRateMaster.get(HTypeTaxRateType.STANDARD).getRate();
    }

    /**
     * @return 軽減税率
     */
    public BigDecimal getTaxRateReduced() {
        return taxRateMaster.get(HTypeTaxRateType.REDUCED).getRate();
    }
}