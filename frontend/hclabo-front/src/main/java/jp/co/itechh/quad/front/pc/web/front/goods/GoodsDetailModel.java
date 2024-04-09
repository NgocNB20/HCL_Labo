/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.annotation.converter.HCNumber;
import jp.co.itechh.quad.front.annotation.validator.HVNumber;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.pc.web.front.goods.common.CategoryItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsIconItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsItem;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 商品詳細画面 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@Data
public class GoodsDetailModel extends AbstractModel {

    /**
     * 商品が存在しない
     */
    public static final String MSGCD_GOODS_NOT_FOUND_ERROR = "AGX000201";

    /**
     * カートからの遷移
     */
    private boolean fromCart;

    /**
     * カテゴリID
     */
    @HVSpecialCharacter(allowPunctuation = true)
    private String cid;

    /**
     * 階層付き通番 例）1-1-1
     */
    private String hsn;

    /**
     * カテゴリ名
     */
    private String categoryName;
    /**
     * タイトル画像PC（カテゴリ）
     */
    private String categoryImagePC;
    /**
     * カテゴリパスリスト（パンくず情報）
     */
    private List<CategoryItem> categoryPassItems;

    /**
     * 商品数量
     */
    @NotEmpty
    @HVNumber
    @Range(min = 1, max = 9999)
    @Digits(integer = 4, fraction = 0)
    @HCNumber
    private String gcnt;

    // パンくず情報（商品一覧画面から引き継いできた。）
    /**
     * 商品グループSEQ
     */
    private Integer goodsGroupSeq;
    /**
     * 商品グループコード
     */
    @HVSpecialCharacter(allowPunctuation = true)
    private String ggcd;
    /**
     * 商品名
     */
    private String goodsGroupName;

    /**
     * 商品説明01
     */
    private String goodsNote1;
    /**
     * 商品説明02
     */
    private String goodsNote2;
    /**
     * 商品説明03
     */
    private String goodsNote3;
    /**
     * 商品説明04
     */
    private String goodsNote4;
    /**
     * 商品説明05
     */
    private String goodsNote5;
    /**
     * 商品説明06
     */
    private String goodsNote6;
    /**
     * 商品説明07
     */
    private String goodsNote7;
    /**
     * 商品説明08
     */
    private String goodsNote8;
    /**
     * 商品説明09
     */
    private String goodsNote9;
    /**
     * 商品説明10
     */
    private String goodsNote10;

    /**
     * 受注連携設定01
     */
    private String orderSetting1;
    /**
     * 受注連携設定02
     */
    private String orderSetting2;
    /**
     * 受注連携設定03
     */
    private String orderSetting3;
    /**
     * 受注連携設定04
     */
    private String orderSetting4;
    /**
     * 受注連携設定05
     */
    private String orderSetting5;
    /**
     * 受注連携設定06
     */
    private String orderSetting6;
    /**
     * 受注連携設定07
     */
    private String orderSetting7;
    /**
     * 受注連携設定08
     */
    private String orderSetting8;
    /**
     * 受注連携設定09
     */
    private String orderSetting9;
    /**
     * 受注連携設定10
     */
    private String orderSetting10;

    /**
     * 規格管理フラグ
     */
    private String unitManagementFlag;
    /**
     * 規格タイトル１（在庫表示用）
     */
    private String unitTitle1;
    /**
     * 規格タイトル１（在庫表示用）
     */
    private String unitTitle2;
    /**
     * meta-description
     */
    private String metaDescription;
    /**
     * meta-keyword
     */
    private String metaKeyword;
    /**
     * エラー表示用規格表示名１
     */
    @HVSpecialCharacter(allowPunctuation = true)
    private String errorUnitTitle1;
    /**
     * エラー表示用規格表示名２
     */
    @HVSpecialCharacter(allowPunctuation = true)
    private String errorUnitTitle2;
    /**
     * 納期
     */
    private String deliveryType;

    /**
     * 商品インフォメーションアイコン
     */
    private List<GoodsIconItem> informationIconItems;
    /**
     * 商品在庫一覧
     */
    private List<GoodsStockItem> goodsStockItems;
    /**
     * あしあと情報リスト
     */
    private List<GoodsGroupItem> browsingHistoryGoodsItems;
    /**
     * お気に入り情報リスト
     */
    private List<GoodsItem> wishlistGoodsItems;
    /**
     * 関連商品情報リスト
     */
    private List<GoodsGroupItem> relatedGoodsItems;

    /**
     * 商品グループ在庫表示Pc
     */
    private String stockStatusPc;
    /**
     * 商品グループ在庫表示フラグ
     */
    private boolean stockStatusDisplay;

    /**
     * 販売状態=販売の商品ありフラグ
     */
    private boolean existsSaleStatusGoods;

    /**
     * 商品画像アイテム
     */
    private List<String> goodsImageItems;

    /**
     * 商品選択プルダウン１リスト
     */
    // private List<Map<String, String>> unitSelect1Items;
    private Map<String, String> unitSelect1Items;
    /**
     * 商品選択プルダウン２リスト
     */
    // private List<Map<String, String>> unitSelect2Items;
    private Map<String, String> unitSelect2Items;

    // 選択値
    /**
     * 選択商品コード
     */
    @HVSpecialCharacter(allowCharacters = {'-'})
    private String gcd;

    /**
     * 規格値１（選択プルダウン用 ※Ajaxで値を変更しているので、「@HVItems」は使用不可）
     */
    @NotEmpty
    @HVSpecialCharacter(allowPunctuation = true)
    private String unitSelect1;

    /**
     * 規格値２（選択プルダウン用 ※Ajaxで値を変更しているので、「@HVItems」は使用不可）
     */
    @NotEmpty
    @HVSpecialCharacter(allowPunctuation = true)
    private String unitSelect2;

    /**
     * 規格1
     */
    private String redirectU1Lbl;

    /**
     * お気に入り商品リスト(":"区切り)
     */
    @HVSpecialCharacter(allowPunctuation = true)
    private String wishlistGoodsCodeList;

    /**
     * SNS連携のボタンを表示するか？
     */
    private boolean snsLinkDisplay;

    /**
     * Facebookを利用するか？
     */
    private boolean useFacebook;

    /**
     * Twitterを利用するか？
     */
    private boolean useTwitter;
    /**
     * Twitter アカウント名
     */
    private String twitterVia;

    /**
     * Lineを利用するか？
     */
    private boolean useLine;

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

    /** フロント表示在庫状態　※プレビュー日時を加味して判定したステータス */
    private String frontDisplayStockStatus;

    /** ワーニングメッセージ */
    private String warningMessage;

    /** ここまでプレビュー画面用項目 **/

    /**
     * カテゴリ画像存在チェック
     *
     * @return true:カテゴリ画像が存在する
     */
    public boolean isViewCategoryImagePC() {
        return StringUtils.isNotEmpty(categoryImagePC);
    }

    /**
     * 商品存在チェック
     *
     * @return true:商品が存在しない
     */
    public boolean isNoGoods() {
        return this.goodsGroupSeq == null;
    }

    /**
     * 商品アイコン存在チェック
     *
     * @return true:商品アイコンあり
     */
    public boolean isInformationIconView() {
        boolean flg = true;
        if (CollectionUtil.isEmpty(informationIconItems)) {
            if (!isNewDate() && !stockStatusDisplay) {
                // 新着でない、かつ、商品グループ在庫表示をしない場合
                flg = false;
            }
        }
        return flg;
    }

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isNewDate() {
        if (whatsnewDate == null) {
            return false;
        }
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp targetTime = dateUtility.getCurrentTime();

        if (StringUtils.isNotBlank(preKey) && StringUtils.isNotBlank(preTime)) {
            // プレビュー画面の場合、プレビュー日時で比較
            targetTime = dateUtility.toTimestampValue(preTime, dateUtility.YMD_HMS);

            // プロパティ値設定前の新着日付
            Timestamp originWhatsnewDate = whatsnewDate;
            String whatsnewViewDays = PropertiesUtil.getSystemPropertiesValue("whatsnew.view.days");
            if (StringUtils.isNotBlank(whatsnewViewDays)) {
                originWhatsnewDate = dateUtility.getAmountDayTimestamp(Integer.parseInt(whatsnewViewDays), false,
                                                                       whatsnewDate
                                                                      );
            }
            // プレビュー日時が新着日付 - プロパティ値より過去の場合は表示させない
            if (targetTime.before(originWhatsnewDate)) {
                return false;
            }
        }
        return whatsnewDate.compareTo(targetTime) >= 0;
    }

    /**
     * 在庫表示チェック
     *
     * @return true:表示する
     */
    public boolean isViewStock() {
        return CollectionUtil.isNotEmpty(goodsStockItems);
    }

    /**
     * 商品説明01がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote1() {
        return StringUtils.isNotEmpty(goodsNote1);
    }

    /**
     * 商品説明02がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote2() {
        return StringUtils.isNotEmpty(goodsNote2);
    }

    /**
     * 商品説明03がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote3() {
        return StringUtils.isNotEmpty(goodsNote3);
    }

    /**
     * 商品説明04がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote4() {
        return StringUtils.isNotEmpty(goodsNote4);
    }

    /**
     * 商品説明05がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote5() {
        return StringUtils.isNotEmpty(goodsNote5);
    }

    /**
     * 商品説明06がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote6() {
        return StringUtils.isNotEmpty(goodsNote6);
    }

    /**
     * 商品説明07がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote7() {
        return StringUtils.isNotEmpty(goodsNote7);
    }

    /**
     * 商品説明08がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote8() {
        return StringUtils.isNotEmpty(goodsNote8);
    }

    /**
     * 商品説明09がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote9() {
        return StringUtils.isNotEmpty(goodsNote9);
    }

    /**
     * 商品説明10がセットされてるか確認する<br/>
     *
     * @return true;セットされている。
     */
    public boolean isGoodsNote10() {
        return StringUtils.isNotEmpty(goodsNote10);
    }

    /**
     * 非販売の判定<br/>
     * <pre>
     * 販売可能チェック、及び、在庫チェックを実行し、非販売であるかの判定を行う。
     * 「販売可能でない」、且つ、「在庫切れでない」場合は「true」、そうでない場合は「false」とする。
     * </pre>
     *
     * @return true:カテゴリ内商品一覧は空でない / false:空である
     */
    public boolean isNoSale() {
        return HTypeStockStatusType.NO_SALE.getValue()
                                           .equals(StringUtils.isNotEmpty(preKey) ?
                                                                   frontDisplayStockStatus :
                                                                   stockStatusPc);
    }

    /**
     * 納期
     *
     * @return true:納期あり
     */
    public boolean isDeliveryTypeDisplay() {
        return StringUtils.isNotEmpty(deliveryType);
    }

    /**
     * 販売可能チェック<br/>
     *
     * @return true:販売可能
     */
    public boolean isPossibleBuy() {
        boolean flg = false;
        if (unitSelect1Items != null && unitSelect1Items.size() > 0) {
            // 規格がプルダウンにセットされていたら
            if (CollectionUtil.isEmpty(goodsStockItems)) {
                // 在庫管理しない場合はtrue
                flg = true;
            } else {
                for (GoodsStockItem stockItem : goodsStockItems) {
                    if (!HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(stockItem.getStockTextType())) {
                        // 在庫切れでなければtrue
                        flg = true;
                        break;
                    }
                }
            }
        } else if (unitSelect1Items == null && this.gcd != null) {
            if (CollectionUtil.isEmpty(goodsStockItems)) {
                // 在庫管理しない場合はtrue
                flg = true;
            } else {
                GoodsStockItem stockItem = goodsStockItems.get(0);
                if (!HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(stockItem.getStockTextType())) {
                    // 在庫切れでなければtrue
                    flg = true;
                }
            }
        }
        if (flg) {
            flg = existsSaleStatusGoods;
        }
        return flg;
    }

    /**
     * 在庫チェック（全商品total)
     * StockFew「残りわずか」
     *
     * @return true：残りわずか
     */
    public boolean isStockFewDisp() {
        return HTypeStockStatusType.STOCK_FEW.getValue()
                                             .equals(StringUtils.isNotEmpty(preKey) ?
                                                                     frontDisplayStockStatus :
                                                                     stockStatusPc);
    }

    /**
     * 在庫チェック（全商品total)
     * StockPossibleSalesDisp「在庫あり」
     *
     * @return true：在庫あり
     */
    public boolean isStockPossibleSalesDisp() {
        return HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue()
                                                        .equals(StringUtils.isNotEmpty(preKey) ?
                                                                                frontDisplayStockStatus :
                                                                                stockStatusPc);
    }

    /**
     * 在庫チェック（全商品total）
     * NoStoc「在庫切れ」
     *
     * @return true:在庫切れ
     */
    public boolean isNoStockDisp() {
        return HTypeStockStatusType.STOCK_NOSTOCK.getValue()
                                                 .equals(StringUtils.isNotEmpty(preKey) ?
                                                                         frontDisplayStockStatus :
                                                                         stockStatusPc);
    }

    /**
     * 在庫チェック（全商品total）
     * SellOut 「販売期間終了」
     *
     * @return true:販売期間終了
     */
    public boolean isSellOutDisp() {
        return HTypeStockStatusType.SOLDOUT.getValue()
                                           .equals(StringUtils.isNotEmpty(preKey) ?
                                                                   frontDisplayStockStatus :
                                                                   stockStatusPc);
    }

    /**
     * 在庫チェック（全商品total）
     * BeforeSale 「販売前」
     *
     * @return true:販売前
     */
    public boolean isBeforeSaleDisp() {
        return HTypeStockStatusType.BEFORE_SALE.getValue()
                                               .equals(StringUtils.isNotEmpty(preKey) ?
                                                                       frontDisplayStockStatus :
                                                                       stockStatusPc);
    }

    /**
     * 在庫チェック（全商品total）
     * NoSale「非販売」
     *
     * @return true:非販売
     */
    public boolean isNoSaleDisp() {
        return HTypeStockStatusType.NO_SALE.getValue()
                                           .equals(StringUtils.isNotEmpty(preKey) ?
                                                                   frontDisplayStockStatus :
                                                                   stockStatusPc);
    }

    /**
     * 規格管理チェック
     *
     * @return true:規格管理する
     */
    public boolean isUnitManage() {
        return HTypeUnitManagementFlag.ON.getValue().equals(unitManagementFlag);
    }

    /**
     * 商品規格２の存在チェック
     *
     * @return true:あり
     */
    public boolean isUseUnit2() {
        return StringUtils.isNotEmpty(unitTitle2);
    }

    /**
     * お気に入り情報表示チェック<br/>
     * プレビュー画面では未表示
     *
     * @return true:表示
     */
    public boolean isViewWishlistGoods() {
        if (wishlistGoodsItems == null || StringUtils.isNotEmpty(preKey)) {
            return false;
        }
        return true;
    }

    /**
     * 商品単価(税抜)
     */
    private BigDecimal goodsPrice;
    /**
     * 商品単価(税込)
     */
    private BigDecimal goodsPriceInTax;
    /**
     * 新着日付
     */
    private Timestamp whatsnewDate;

}