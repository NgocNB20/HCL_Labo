/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.goods.stock;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class StockDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** 実在庫数 */
    private BigDecimal realStock;

    /** 注文確保在庫数 */
    private BigDecimal orderReserveStock;

    /** 残少表示在庫数 */
    private BigDecimal remainderFewStock;

    /** 入庫数 */
    private BigDecimal supplementCount;

    /** 発注点在庫数 */
    private BigDecimal orderPointStock;

    /** 安全在庫数 */
    private BigDecimal safetyStock;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;
}
