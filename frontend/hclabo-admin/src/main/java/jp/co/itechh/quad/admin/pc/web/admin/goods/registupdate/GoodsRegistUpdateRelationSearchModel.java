/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 商品管理：商品登録更新（関連商品設定検索）ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsRegistUpdateRelationSearchModel extends AbstractGoodsRegistUpdateModel {

    /**
     * 正規表現：SQL文のエスケープ文字は、入力不可とする。<br/>
     */
    public static final String REGULAR_PATTERN_NO_ESCAPE_CHAR = "[^%_\\\\]*$";

    /**
     * limitのバリデータを1度しか行わない為のフラグ<br/>
     */
    private boolean limitValidFlg;

    /* PagerPage継承用ここまで */

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public GoodsRegistUpdateRelationSearchModel() {
        super();
        setResultGoodsRelationFlg(false);
    }

    /************************************
     ** 関連商品リスト項目
     ************************************/
    /**
     * 関連商品グループ情報<br/>
     */
    private List<GoodsRelationEntity> tmpGoodsRelationEntityList;

    /**
     * 関連商品グループ情報（ページ間リダイレクト受け渡し用）<br/>
     */
    private List<GoodsRelationEntity> redirectGoodsRelationEntityList;

    /************************************
     ** セッション保持用検索条件
     ************************************/
    /**
     * 検索条件保持<br/>
     */
    private GoodsSearchForBackDaoConditionDto goodsGroupSearchForDaoConditionDto;

    /************************************
     ** 関連商品検索条件項目
     ************************************/
    /**
     * 検索条件 キーワード<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchRelationGoodsKeyword;

    /**
     * 検索条件 商品グループコード<br/>
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {SearchGroup.class, DisplayChangeGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_GROUP_CODE,
             message = ValidatorConstants.MSGCD_REGEX_GOODS_GROUP_CODE,
             groups = {SearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_GROUP_CODE_MAXIMUM,
            groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HCHankaku
    private String searchGoodsGroupCode;

    /**
     * 検索条件 商品名<br/>
     */
    @Length(min = 0, max = 120, groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchGoodsGroupName;

    /**
     * 検索条件 カテゴリID<br/>
     */
    @HVSpecialCharacter(allowPunctuation = true, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryId;

    /**
     * 表示件数アイテム<br/>
     */
    private Map<String, String> limitItems;

    /************************************
     ** 検索条件コンボボックス
     ************************************/
    /**
     * カテゴリーID<br/>
     */
    private Map<String, String> searchCategoryIdItems;

    /************************************
     ** 商品検索結果リスト項目
     ************************************/
    /**
     * 商品検索結果情報<br/>
     */
    private GoodsRegistUpdateRelationSearchItem result;

    /**
     * 商品検索結果リスト<br/>
     */
    private List<GoodsRegistUpdateRelationSearchItem> resultItems;

    /************************************
     ** 検索結果表示判定
     ************************************/
    /**
     * 検索結果フラグ<br/>
     */
    private boolean resultGoodsRelationFlg;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果表示
     */
    public boolean isResultGoodsReration() {
        return resultGoodsRelationFlg;
    }

    /**
     * 検索結果0件判定<br/>
     *
     * @return true=検索結果=0件
     */
    public boolean isResultNoData() {
        return (resultItems == null || resultItems.size() == 0);
    }

    /************************************
     ** ソート項目
     ************************************/
    /**
     * 商品グループコード<br/>
     */
    private String goodsGroupCodeSort;

    /**
     * 商品名<br/>
     */
    private String goodsGroupGroupNameSort;

    /**
     * 公開状態PC<br/>
     */
    private String goodsOpenStatusPCSort;

    /** 検索条件 カテゴリIDリスト */
    private List<CategoryEntity> searchCategoryIdList;
}