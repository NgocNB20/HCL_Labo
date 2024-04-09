/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.shop.settlement;

import jp.co.itechh.quad.admin.entity.multipayment.CardBrandEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 決済DTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class SettlementDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 決済詳細Dto */
    private SettlementDetailsDto settlementDetailsDto;

    /** カードブランドリスト */
    private List<CardBrandEntity> cardBrandEntityList;

    /** 決済手数料 */
    private BigDecimal charge;

    /** 選択区分 */
    private boolean selectClass;
}