/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.stock;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 在庫Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class StockSearchForDaoConditionDto extends AbstractConditionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** キーワード条件1 */
    private String keywordLikeCondition1;

    /** キーワード条件2 */
    private String keywordLikeCondition2;

    /** キーワード条件3 */
    private String keywordLikeCondition3;

    /** キーワード条件4 */
    private String keywordLikeCondition4;

    /** キーワード条件5 */
    private String keywordLikeCondition5;

    /** キーワード条件6 */
    private String keywordLikeCondition6;

    /** キーワード条件7 */
    private String keywordLikeCondition7;

    /** キーワード条件8 */
    private String keywordLikeCondition8;

    /** キーワード条件9 */
    private String keywordLikeCondition9;

    /** キーワード条件10 */
    private String keywordLikeCondition10;

    /** カテゴリID */
    private String categoryId;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品コード */
    private String goodsCode;

    /** 商品名 */
    private String goodsGroupName;

    /** JANコード */
    private String janCode;

    /** 複数番号検索コード */
    private String multiCode;

    /** 複数番号 */
    private String searchMultiCode;

    /** 複数番号リスト */
    private List<String> multiCodeList;

    /** 個別配送のみ */
    private HTypeIndividualDeliveryType individualDeliveryType;

    /** 下限金額 */
    private BigDecimal minPrice;

    /** 上限金額 */
    private BigDecimal maxPrice;

    /** 公開状態 */
    private HTypeOpenDeleteStatus goodsOpenStatus;

    /** 公開開始日時From */
    private Timestamp openStartTimeFrom;

    /** 公開開始日時To */
    private Timestamp openStartTimeTo;

    /** 公開終了日時From */
    private Timestamp openEndTimeFrom;

    /** 公開終了日時To */
    private Timestamp openEndTimeTo;

    /** 削除商品表示フラグ */
    private Boolean deleteStatusDsp;

    /** 販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    /** 販売開始日時From */
    private Timestamp saleStartTimeFrom;

    /** 販売開始日時To */
    private Timestamp saleStartTimeTo;

    /** 販売終了日時From */
    private Timestamp saleEndTimeFrom;

    /** 販売終了日時To */
    private Timestamp saleEndTimeTo;

    /** 配送 */
    private String deliveryMethod;

    /** 在庫種別フラグ */
    private String stockTypeFlag;

    /** 発注点割れ商品のみ */
    private Boolean orderPointStockBelow;

    /** 下限在庫数 */
    private BigDecimal minStockCount;

    /** 上限在庫数 */
    private BigDecimal maxStockCount;

    /** 入庫日時From */
    private Timestamp supplementTimeFrom;

    /** 入庫日時To */
    private Timestamp supplementTimeTo;

    /** 下限入荷待ちメール数 */
    private BigDecimal minMail;

    /** 上限入荷待ちメール数 */
    private BigDecimal maxMail;

    /** 検索種別 */
    private Integer searchType;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** サイト区分 */
    private String site;

    /** カテゴリーパス */
    private String categoryPath;
}
