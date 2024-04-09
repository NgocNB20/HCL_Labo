/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.settlement;

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
 * 決済方法金額別手数料クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "SettlementMethodPriceCommission")
@Data
@Component
@Scope("prototype")
public class SettlementMethodPriceCommissionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 決済方法SEQ (FK)（必須） */
    @Column(name = "settlementMethodSeq")
    @Id
    private Integer settlementMethodSeq;

    /** 上限金額（必須） */
    @Column(name = "maxPrice")
    @Id
    private BigDecimal maxPrice = new BigDecimal(0);

    /** 手数料 */
    @Column(name = "commission")
    private BigDecimal commission = new BigDecimal(0);

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
