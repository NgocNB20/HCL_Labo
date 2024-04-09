/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.stock;

import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫状態表示判定用DTO<br/>
 *
 * @author hs32101
 *
 */
@Data
@Component
@Scope("prototype")
public class StockStatusDisplayConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    /** 販売開始日 */
    private Timestamp saleStartTime;

    /** 販売終了日 */
    private Timestamp saleEndTime;

    /** 在庫管理 */
    private HTypeStockManagementFlag stockManagementFlag;

    /** 残少表示在庫数 */
    private BigDecimal remainderFewStock;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** セールID */
    private String saleId;

    /** セール開始日時 */
    private Timestamp saleHoldStartTime;

    /** セール終了日時 */
    private Timestamp saleHoldEndTime;

}