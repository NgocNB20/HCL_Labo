/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.front.pc.web.front.goods.common.CategoryItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 商品一覧画面 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
public class GoodsListModel extends AbstractModel {

    /**
     * カテゴリIDが指定されていない<br/>
     * <code>MSGCD_CATEGORY_NOT_FOUND_ERROR</code>
     */
    public static final String MSGCD_CATEGORY_NOT_SELECT_ERROR = "AGX000101";

    /**
     * カテゴリが取得できない<br/>
     * <code>MSGCD_CATEGORY_NOT_FOUND_ERROR</code>
     */
    public static final String MSGCD_CATEGORY_NOT_FOUND_ERROR = "AGX000102";

    /** カテゴリID */
    private String cid;

    /** 階層付き通番 例）1-1-1 */
    private String hsn;

    /** カテゴリ画像PC */
    private String categoryImagePC;

    /** meta-description */
    private String metaDescription;

    /** カテゴリ表示名PC */
    private String categoryName;

    /** 手動表示フラグ */
    private HTypeManualDisplayFlag manualDisplayFlag;

    /** フリーテキストPC */
    private String freeTextPC;

    /** カテゴリパスリスト（パンくず情報） */
    private List<CategoryItem> categoryPassItems;

    /** 商品グループ（リスト） */
    private List<GoodsGroupItem> goodsGroupListItems;

    /** サムネイル用ループリスト */
    private List<List<GoodsGroupItem>> goodsGroupThumbnailItemsItems;

    /** 画面初期表示処理フラグ */
    private Boolean initialDisplayFlag;

    /** ここからプレビュー画面用項目 **/

    /** プレビューキー(パラメータ値) */
    private String preKey;

    /** プレビュー日時(パラメータ値) */
    private String preTime;

    /** プレビュー日付 */
    private String previewDate;

    /** プレビュー時間 */
    private String previewTime;

    /** フロント表示　※プレビュー日時を加味して判定したステータス */
    private String frontDisplay;

    /** ワーニングメッセージ */
    private String warningMessage;

    /** ここまでプレビュー画面用項目 **/

    /**
     * 検索結果０件チェック
     *
     * @return true:結果０件
     */
    public boolean isNoGoods() {
        return CollectionUtil.isEmpty(goodsGroupListItems);
    }

    /**
     *
     * 手動並び順指定可能フラグ<br/>
     *
     * @return true:手動並び順指定可能
     */
    public boolean isManualDisplay() {
        return HTypeManualDisplayFlag.ON.equals(manualDisplayFlag);
    }

    /**
     * カテゴリ画像（PC）存在チェック<br/>
     *
     * @return true:存在する
     */
    public boolean isViewCategoryImagePC() {
        return StringUtils.isNotEmpty(categoryImagePC);
    }

    /**
     * メニュー表示名取得
     *
     * @return パンくずリストがあれば、リストの末端の名称を返却する, パンくずリストがなければnullを返却する
     */
    public String getMenuDisplayName() {
        // パンくずリストがあれば、リストの末端の名称を返却する, パンくずリストがなければnullを返却する
        if (!CollectionUtil.isEmpty(categoryPassItems)) {
            return categoryPassItems.get(categoryPassItems.size() - 1).getCategoryName();
        }
        return null;
    }

    /**
     * カテゴリー表示名取得
     *
     * @return カテゴリー表示名
     */
    public String getCategoryDisplayName() {
        String menuDisplayName = getMenuDisplayName();
        if (menuDisplayName != null) {
            return menuDisplayName;
        }
        return categoryName;
    }

}