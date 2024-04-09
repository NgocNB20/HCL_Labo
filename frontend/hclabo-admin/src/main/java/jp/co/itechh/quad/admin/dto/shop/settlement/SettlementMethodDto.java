/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.shop.settlement;

import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 決済方法DTO<br/>
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
@Data
@Component
@Scope("prototype")
public class SettlementMethodDto implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /**
     * 決済方法エンティティ
     */
    private SettlementMethodEntity settlementMethodEntity;

    /**
     * 決済方法金額別手数料エンティティリスト
     */
    private List<SettlementMethodPriceCommissionEntity> settlementMethodPriceCommissionEntityList;

    /**
     * 配送方法名
     */
    private String deliveryMethodName;
}