/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品Dao用検索条件（管理機能）Dtoクラス
 *
 * @author DtoGenerator
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Component
@Scope("prototype")
public class GoodsSearchForBackDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** サイト区分 */
    private String site;

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

    /** 商品タグ条件1 */
    private String goodsTagLikeCondition1;

    /** 商品タグ条件2 */
    private String goodsTagLikeCondition2;

    /** 商品タグ条件3 */
    private String goodsTagLikeCondition3;

    /** 商品タグ条件4 */
    private String goodsTagLikeCondition4;

    /** 商品タグ条件5 */
    private String goodsTagLikeCondition5;

    /** 商品タグ条件6 */
    private String goodsTagLikeCondition6;

    /** 商品タグ条件7 */
    private String goodsTagLikeCondition7;

    /** 商品タグ条件8 */
    private String goodsTagLikeCondition8;

    /** 商品タグ条件9 */
    private String goodsTagLikeCondition9;

    /** 商品タグ条件10 */
    private String goodsTagLikeCondition10;

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

    /** 公開状態リスト */
    private List<String> goodsOpenStatusList;

    /** 公開開始日時From */
    private Timestamp goodsOpenStartTimeFrom;

    /** 公開開始日時To */
    private Timestamp goodsOpenStartTimeTo;

    /** 公開終了日時From */
    private Timestamp goodsOpenEndTimeFrom;

    /** 公開終了日時To */
    private Timestamp goodsOpenEndTimeTo;

    /** 削除商品表示フラグ */
    private Boolean deleteStatusDsp;

    /** 販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    /** 販売状態リスト */
    private List<String> saleStatusList;

    /** 販売開始日時From */
    private Timestamp saleStartTimeFrom;

    /** 販売開始日時To */
    private Timestamp saleStartTimeTo;

    /** 販売終了日時From */
    private Timestamp saleEndTimeFrom;

    /** 販売終了日時To */
    private Timestamp saleEndTimeTo;

    /** 登録日時From */
    private Timestamp registTimeFrom;

    /** 登録日時To */
    private Timestamp registTimeTo;

    /** 更新日時From */
    private Timestamp updateTimeFrom;

    /** 更新日時To */
    private Timestamp updateTimeTo;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** SNS連携フラグ */
    private HTypeSnsLinkFlag snsLinkFlag;

    /** 商品管理：商品登録更新：関連商品検索での利用時はtrue */
    private boolean relationGoodsSearchFlag;

    /** 販売可能在庫下限数 */
    private BigDecimal minSalesPossibleStock;

    /** 販売可能在庫上限数 */
    private BigDecimal maxSalesPossibleStock;

    /** ノベルティ商品フラグ */
    private HTypeNoveltyGoodsType noveltyGoodsType;

    /** フロント表示状態リスト */
    private List<String> frontDisplayList;

    /** フロント表示基準日時 */
    private Timestamp frontDisplayReferenceDate;

    /** 検索用商品設定キーワード */
    private List<String> searchSettingKeywordList;

    /** 商品グループコードリスト */
    private List<String> goodsGroupCodeList;

    /** 商品コードリスト */
    private List<String> goodsCodeList;

    /** JANコードリスト */
    private List<String> janCodeList;

    /** 検索コード種類 */
    private String searchCodeType;

    /** カテゴリIDリスト */
    private List<String> categoryIdList;

    /** 商品タグ条件Check */
    public Boolean hasGoodsTagCondition() {
        if (StringUtils.isNotEmpty(this.goodsTagLikeCondition1)) {
            return true;
        }
        return false;
    }
}
