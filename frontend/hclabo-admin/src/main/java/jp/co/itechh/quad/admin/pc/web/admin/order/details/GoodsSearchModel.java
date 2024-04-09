/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.goodssearch.group.GoodsSearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsSearchModel extends AbstractModel {

    /** コンストラクタ */
    public GoodsSearchModel() {
        super();
    }

    /** サイト */
    private String site = "0";

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** ソート項目 */
    private String orderField;

    /** ソート条件 */
    private boolean orderAsc;

    /** 検索結果総件数 */
    private int totalCount;

    /** 検索条件 */
    private OrderSearchConditionDto conditionDto;

    /** 検索一覧インデックス */
    private Integer resultIndex;

    /**
     * 検索一覧<br/>
     */
    @Valid
    private List<GoodsSearchModelItem> resultItems;

    /**
     * 検索条件保持<br/>
     */
    private GoodsSearchForBackDaoConditionDto goodsSearchForBackDaoConditionDto;

    /************************************
     ** ソート項目
     ************************************/

    /** 商品グループコード・商品コード（デフォルト） */
    private String goodsGroupCodeAndGoodsCodeSort;

    /** 商品グループコード */
    private String goodsGroupCodeSort;

    /** 商品コード */
    private String goodsCodeSort;

    /** 商品名 */
    private String goodsGroupNameSort;

    /** 規格1 */
    private String unitValue1Sort;

    /** 規格2 */
    private String unitValue2Sort;

    /** 単価 */
    private String goodsPriceSort;

    /** 個別配送 */
    private String individualDeliveryTypeSort;

    /** JANコード */
    private String janCodeSort;

    /** 販売可能在庫数 */
    private String salesPossibleStockSort;

    /************************************
     ** 検索条件
     ************************************/
    /**
     * キーワード<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    private String searchKeyword;

    /**
     * カテゴリーID<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryId;

    /**
     * 商品グループコード<br/>
     */
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_GROUP_CODE_MAXIMUM,
            groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_GROUP_CODE,
             message = ValidatorConstants.MSGCD_REGEX_GOODS_GROUP_CODE,
             groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchGoodsGroupCode;

    /**
     * 商品コード<br/>
     */
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_CODE_MAXIMUM,
            groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_CODE, message = ValidatorConstants.MSGCD_REGEX_GOODS_CODE,
             groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchGoodsCode;

    /**
     * JANコード<br/>
     */
    @Length(min = 0, max = ValidatorConstants.LENGTH_JAN_CODE_MAXIMUM,
            groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_CATALOG_CODE, message = ValidatorConstants.MSGCD_REGEX_JAN_CATALOG_CODE,
             groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchJanCode;

    /**
     * 商品名<br/>
     */
    @Length(min = 0, max = 120, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    private String searchGoodsGroupName;

    /**
     * 商品個別配送種別<br/>
     */
    private Boolean searchIndividualDeliveryType;

    /**
     * 下限価格<br/>
     */
    @HVNumber(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCNumber
    private String searchMinPrice;

    /**
     * 上限価格<br/>
     */
    @HVNumber(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCNumber
    private String searchMaxPrice;

    /**
     * 公開状態<br/>
     */
    @HVItems(target = HTypeOpenStatus.class, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    private String goodsOpenStatus;

    /**
     * 公開状態開始日[From]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchOpenStartTimeFrom;

    /**
     * 公開状態開始日[to]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchOpenStartTimeTo;

    /**
     * 公開状態終了日[from]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchOpenEndTimeFrom;

    /**
     * 公開状態終了日[To]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchOpenEndTimeTo;

    /**
     * 商品販売状態<br/>
     */
    @HVItems(target = HTypeGoodsSaleStatus.class, groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    private String goodsSaleStatus;

    /**
     * 販売状態開始日[from]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchSaleStartTimeFrom;

    /**
     * 販売状態開始日[to]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchSaleStartTimeTo;

    /**
     * 販売状態終了日[from]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchSaleEndTimeFrom;

    /**
     * 販売状態開始日[to]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchSaleEndTimeTo;

    /**
     * 登録日／更新日選択フラグ
     */
    private String selectRegitOrUpdate;

    /**
     * 登録／更新日[from]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchRegOrUpTimeFrom;

    /**
     * 登録／更新日[to]<br/>
     */
    @HVDate(groups = {GoodsSearchGroup.class, DisplayChangeGroup.class})
    @HCDate
    private String searchRegOrUpTimeTo;

    /************************************
     ** 検索条件コンボボックス
     ************************************/
    /**
     * カテゴリーID<br/>
     */
    private Map<String, String> searchCategoryIdItems;

    /**
     * 商品公開状態<br/>
     */
    private Map<String, String> goodsOpenStatusItems;

    /**
     * 商品販売状態<br/>
     */
    private Map<String, String> goodsSaleStatusItems;

    /**
     * 配送方法<br/>
     */
    private Map<String, String> searchDeliveryMethodItems;

    /**
     * 決済方法<br/>
     */
    private Map<String, String> searchSettlementMethodItems;

    /************************************
     ** 検索結果表示項目
     ************************************/

    /* 画面表示判定 */
    public boolean isResult() {
        return getResultItems() != null;
    }
}