/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVRNumberGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * ノベルティプレゼント検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "searchNoveltyPresentStartTimeTo", comparison = "searchNoveltyPresentStartTimeFrom")
@HVRDateGreaterEqual(target = "searchNoveltyPresentEndTimeTo", comparison = "searchNoveltyPresentEndTimeFrom")
@HVRNumberGreaterEqual(target = "searchNoveltyPresentGoodsCountTo", comparison = "searchNoveltyPresentGoodsCountFrom")
public class NoveltyModel extends AbstractModel {

    /**
     * ページ番号<br/>
     */
    private String pageNumber;

    /**
     * 最大表示件数<br/>
     */
    private int limit;

    /**
     * ソート項目<br/>
     */
    private String orderField;

    /**
     * ソート条件<br/>
     */
    private boolean orderAsc;

    /**
     * 検索結果総件数<br/>
     */
    private int totalCount;

    /**
     * 検索一覧<br/>
     */
    private List<NoveltyModelItem> resultItems;

    /**
     * limit動的Validator target<br/>
     *
     * @return target
     */
    protected String getLimitValidatorTarget() {
        return "doNoveltySearch,doDisplayChange";
    }

    /************************************
     **  検索条件
     ************************************/
    /** ノベルティプレゼント条件名 */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 120)
    public String searchNoveltyPresentName;

    /** ノベルティプレゼント条件状態 */
    public String[] noveltyPresentState;

    /** ノベルティプレゼント条件開始日-From */
    @HCDate
    @HVDate
    public String searchNoveltyPresentStartTimeFrom;

    /** ノベルティプレゼント条件開始日-To */
    @HCDate
    @HVDate
    public String searchNoveltyPresentStartTimeTo;

    /** ノベルティプレゼント条件終了日-From */
    @HCDate
    @HVDate
    public String searchNoveltyPresentEndTimeFrom;

    /** ノベルティプレゼント条件終了日-To */
    @HCDate
    @HVDate
    public String searchNoveltyPresentEndTimeTo;

    /** ノベルティ商品番号 */
    @Length(min = 0, max = 20)
    @Pattern(regexp = ValidatorConstants.REGEX_GOODS_GROUP_CODE,
             message = ValidatorConstants.MSGCD_NOVELTY_PRESENT_GOODS_CODE)
    public String searchNoveltyPresentGoodsCode;

    /** ノベルティ商品在庫数-From */
    @HVNumber(minus = false)
    @Digits(integer = 4, fraction = 0)
    @HCNumber
    public String searchNoveltyPresentGoodsCountFrom;

    /** ノベルティ商品在庫数-To */
    @HVNumber(minus = false)
    @Digits(integer = 4, fraction = 0)
    @HCNumber
    public String searchNoveltyPresentGoodsCountTo;

    /************************************
     **  選択肢
     ************************************/
    /** ノベルティプレゼント条件状態 */
    public Map<String, String> noveltyPresentStateItems;

    /************************************
     **  ソート項目
     ************************************/
    /** ソート：ノベルティプレゼント条件開始日 */
    public String noveltyPresentStartTimeSort;

    /** ソート：ノベルティプレゼント条件終了日 */
    public String noveltyPresentEndTimeSort;

    /** ソート：ノベルティプレゼント条件名 */
    public String noveltyPresentNameSort;

    /** ソート：ノベルティプレゼント条件状態 */
    public String noveltyPresentStateSort;

    /** ソート：ノベルティ商品 */
    public String noveltyPresentGoodsSort;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }
}