/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.stock;

import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫詳細Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class StockDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 商品名 */
    private String goodsGroupName;

    /** 商品コード */
    private String goodsCode;

    /** 商品単価（税込み） */
    private BigDecimal goodsPriceInTax;

    /** 販売状態PC */
    private HTypeGoodsSaleStatus saleStatusPC;

    /** 販売開始日時PC */
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    private Timestamp saleEndTimePC;

    /** 在庫管理 */
    private HTypeStockManagementFlag stockManagementFlag;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** JANコード */
    private String janCode;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** 実在庫数 */
    private BigDecimal realStock;

    /** 注文確保在庫数 */
    private BigDecimal orderReserveStock;

    /** 残少表示在庫数 */
    private BigDecimal remainderFewStock;

    /** 発注点在庫数 */
    private BigDecimal orderPointStock;

    /** 安全在庫数 */
    private BigDecimal safetyStock;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品公開状態PC */
    private HTypeOpenDeleteStatus goodsOpenStatusPC;

    /** 商品公開開始日時PC */
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    private Timestamp openEndTimePC;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格タイトル2 */
    private String unitTitle2;
}