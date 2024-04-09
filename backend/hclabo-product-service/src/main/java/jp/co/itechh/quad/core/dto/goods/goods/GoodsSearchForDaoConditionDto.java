/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsSearchForDaoConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** キーワード条件 */
    private String keywordLikeCondition;

    /** カテゴリID */
    private String categoryId;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 下限金額 */
    private BigDecimal minPrice;

    /** 上限金額 */
    private BigDecimal maxPrice;

    /** 販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    // ※本Dtoをつかった検索ではページング制御不要のため、PageInfoは使わない（AbstractConditionDtoの継承はしない）
    /** 並替項目 */
    private String orderField;

    /** 並替昇順フラグ */
    private boolean orderAsc;
}
