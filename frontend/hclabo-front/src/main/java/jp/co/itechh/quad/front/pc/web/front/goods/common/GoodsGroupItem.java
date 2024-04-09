/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods.common;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品グループItem
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupItem {

    /** カテゴリID */
    private String cid;

    /** 階層付き通番 例）1-1-1 */
    private String hsn;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 商品グループコード */
    private String ggcd;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    /** 商品画像アイテム */
    private List<String> goodsImageItems;

    /** 商品表示単価(税抜) */
    private BigDecimal goodsPrice;

    /** 商品表示単価(税込) */
    private BigDecimal goodsPriceInTax;

    /** 税率 */
    private BigDecimal taxRate;

    /** 商品消費税種別（必須） */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    // アイコン
    /** 商品アイコンリスト */
    private List<GoodsIconItem> goodsIconItems;

    /** 商品グループ在庫表示Pc */
    private String stockStatusPc;

    /** 商品グループ在庫表示フラグ */
    private boolean stockStatusDisplay;

    /** 商品説明1 */
    private String goodsNote1;

    /** 商品説明2 */
    private String goodsNote2;

    /** 商品説明3 */
    private String goodsNote3;

    /** 商品説明4 */
    private String goodsNote4;

    /** 商品説明5 */
    private String goodsNote5;

    /** 商品説明6 */
    private String goodsNote6;

    /** 商品説明7 */
    private String goodsNote7;

    /** 商品説明8 */
    private String goodsNote8;

    /** 商品説明9 */
    private String goodsNote9;

    /** 商品説明10 */
    private String goodsNote10;

    // あしあと商品のみで使用 >>>>>>>>>>>>>>>>>>>>>>>>>
    /** 公開状態 */
    private HTypeOpenDeleteStatus goodsOpenStatus;

    /** 公開開始日時 */
    private Timestamp openStartTime;

    /** 公開終了日時 */
    private Timestamp openEndTime;
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /** ここからプレビュー画面用項目 ※販売状況のステータスはバッチ情報を参照するため、商品一覧プレビュー画面では未対応 **/

    /** フロント表示　※プレビュー日時を加味して判定したステータス */
    private String frontDisplay;

    /** フロント表示基準日時 */
    private Timestamp frontDisplayReferenceDate;

    /** ここまでプレビュー画面用項目 **/

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isNewDate() {

        if (whatsnewDate == null && frontDisplayReferenceDate == null) {
            return false;
        }

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // プレビュー画面の場合
        if (frontDisplayReferenceDate != null) {

            // プロパティ値設定前の新着日付
            Timestamp originWhatsnewDate = whatsnewDate;
            String whatsnewViewDays = PropertiesUtil.getSystemPropertiesValue("whatsnew.view.days");
            if (StringUtils.isNotBlank(whatsnewViewDays)) {
                originWhatsnewDate = dateUtility.getAmountDayTimestamp(Integer.parseInt(whatsnewViewDays), false,
                                                                       whatsnewDate
                                                                      );
            }
            // プレビュー日時が新着日付 - プロパティ値より過去の場合は表示させない
            if (frontDisplayReferenceDate.before(originWhatsnewDate)) {
                return false;
            }
            return whatsnewDate.compareTo(frontDisplayReferenceDate) >= 0;
        }
        return whatsnewDate.compareTo(dateUtility.getCurrentDate()) >= 0;
    }

    /**
     * 在庫状態:非販売
     * （アイコン表示用）
     * @return true：非販売
     */
    public boolean isStockNoSaleIconDisp() {
        return HTypeStockStatusType.NO_SALE.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:販売期間終了
     * （アイコン表示用）
     * @return true：販売期間終了
     */
    public boolean isStockSoldOutIconDisp() {
        return HTypeStockStatusType.SOLDOUT.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:販売前
     * （アイコン表示用）
     * @return true：販売前
     */
    public boolean isStockBeforeSaleIconDisp() {
        return HTypeStockStatusType.BEFORE_SALE.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:在庫なし
     * （アイコン表示用）
     * @return true：在庫なし
     */
    public boolean isStockNoStockIconDisp() {
        return HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:残りわずか
     * （アイコン表示用）
     * @return true：残りわずか
     */
    public boolean isStockFewIconDisp() {
        return HTypeStockStatusType.STOCK_FEW.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:在庫あり
     * （アイコン表示用）
     * @return true：在庫あり
     */
    public boolean isStockPossibleSalesIconDisp() {
        return HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue().equals(stockStatusPc);
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

    // あしあと商品のみで使用 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * 商品公開判定<br/>
     *
     * @return true=公開、false=公開でない
     */
    public boolean isGoodsOpen() {
        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
        return goodsUtility.isGoodsOpen(goodsOpenStatus, openStartTime, openEndTime);
    }
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

}
