/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品規格値DTOクラス
 *
 * @author DtoGenerator
 *
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsUnitDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品コード */
    private String goodsCode;

    /** 販売状態PC */
    private HTypeGoodsSaleStatus saleStatusPC;

    /** 販売開始日時PC */
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    private Timestamp saleEndTimePC;

    /** 在庫管理 */
    private HTypeStockManagementFlag stockManagementFlag;

    /** 規格値1 */
    private String unitValue1;

    /** 規格値2 */
    private String unitValue2;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** 残少表示在庫数 */
    private BigDecimal remainderFewStock;
}
