/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.search;

import jp.co.itechh.quad.front.annotation.converter.HCNumber;
import jp.co.itechh.quad.front.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.front.annotation.validator.HVNumber;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import java.util.List;
import java.util.Map;

/**
 * 商品検索画面 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchModel extends AbstractModel {

    /** 共通ヘッダからの検索 */
    public static final String FROM_VIEW_HEADER_KEY = "header";

    /** 検索画面からの検索 */
    public static final String FROM_VIEW_SEARCH_KEY = "search";

    /** 価格帯From */
    @HCNumber
    @HVNumber
    @Range(min = 0, max = 99999999)
    @Digits(integer = 8, fraction = 0)
    private String ll;

    /** 価格帯To */
    @HCNumber
    @HVNumber
    @Range(min = 0, max = 99999999)
    @Digits(integer = 8, fraction = 0)
    private String ul;

    /** キーワード */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    private String keyword;

    /** カテゴリID */
    @HVSpecialCharacter(allowPunctuation = true)
    private String condCid;

    /** 階層付き通番 例）1-1-1 */
    @HVSpecialCharacter(allowPunctuation = true)
    private String hsn;

    /** エンコード後キーワード */
    @HVSpecialCharacter(allowPunctuation = true)
    private String q;

    /** 商品グループリスト */
    private List<GoodsGroupItem> goodsGroupListItems;

    /** サムネイル用ループリスト */
    private List<List<GoodsGroupItem>> goodsGroupThumbnailItemsItems;

    /** 総件数 */
    private int totalCount;

    /** カテゴリID(ヘッダで選択した情報) */
    private String headerSelectcategory;

    /** カテゴリプルダウン */
    private List<Map<String, String>> condCidItems;

    /** 在庫ありフラグ */
    private boolean st;

    /** ページ番号 */
    private String pnum;

    /** 表示形式 */
    private String vtype;

    /** ソート形式 */
    private String stype;

    /** 昇順/降順フラグ */
    private boolean asc;

    /** リスト最大表示件数 */
    private int listLimit;

    /** サムネイル最大表示件数 */
    private int thumbnailLimit;

    /** サムネイル表示：改行位置*/
    private int col;

    /**
     * 検索結果０件チェック
     *
     * @return true:結果０件
     */
    public boolean isNoGoods() {
        return CollectionUtil.isEmpty(goodsGroupListItems);
    }

}