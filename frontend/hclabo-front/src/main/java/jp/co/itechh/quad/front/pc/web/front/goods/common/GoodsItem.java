/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods.common;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
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
 * 商品Item
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsItem {

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品コード */
    private String gcd;

    /** 商品画像アイテム */
    private List<String> goodsImageItems;

    /** 商品名 */
    private String goodsGroupName;

    /** 商品説明 */
    private String goodsDetailsNote;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格値1 */
    private String unitValue1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 規格値2 */
    private String unitValue2;

    // HTMLへの追加のみで表示できるよう保持
    /** 納期 */
    private String deliveryType;

    /** 商品単価(税抜) */
    private BigDecimal goodsPrice;

    /** 商品単価(税込) */
    private BigDecimal goodsPriceInTax;

    /** 税率 */
    private BigDecimal taxRate;

    /** 商品消費税種別（必須） */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 商品公開状態 */
    private HTypeOpenDeleteStatus goodsOpenStatus;

    /** 商品公開開始時間 */
    private Timestamp openStartTime;

    /** 商品公開終了時間 */
    private Timestamp openEndTime;

    /** 商品販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    /** 商品販売開始時間 */
    private Timestamp saleStartTime;

    /** 商品販売終了時間 */
    private Timestamp saleEndTime;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    // アイコン
    /** 商品アイコンリスト*/
    private List<GoodsIconItem> goodsIconItems;

    /** 商品グループ在庫表示Pc */
    private String stockStatusPc;
    /** 商品グループ在庫表示フラグ */
    private boolean stockStatusDisplay;

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isNewDate() {
        if (whatsnewDate == null) {
            return false;
        }
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        return whatsnewDate.compareTo(dateUtility.getCurrentDate()) >= 0;
    }

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

    /**
     * 商品販売判定<br/>
     *
     * @return true=公開、false=公開でない
     */
    public boolean isGoodsSales() {
        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
        return goodsUtility.isGoodsSales(saleStatus, saleStartTime, saleEndTime);
    }

    /**
     * 商品説明の有無判定<br/>
     *
     * @return true=有、false=無
     */
    public boolean isDetailsNote() {
        return StringUtils.isNotEmpty(goodsDetailsNote);
    }

    /**
     * 規格1の有無判定<br/>
     *
     * @return true=有、false=無
     */
    public boolean isUnit1() {
        return StringUtils.isNotEmpty(unitTitle1);
    }

    /**
     * 規格2の有無判定<br/>
     *
     * @return true=有、false=無
     */
    public boolean isUnit2() {
        return StringUtils.isNotEmpty(unitTitle2);
    }

    /**
     *
     * 納期が設定されているか確認<br/>
     * true:設定されている<br/>
     *
     * @return true:納期が設定されている
     */
    public boolean isViewDeliveryType() {
        return deliveryType != null;
    }

    /**
     * カートに入れるボタンの表示判定<br/>
     * <br/>
     * 以下の場合のみカートに入れるボタンを表示する<br/>
     * ・公開状態：公開<br/>
     * ・販売状態：販売<br/>
     * ・在庫状態：在庫なし以外<br/>
     *
     * @return true=表示する、false=表示しない
     */
    public boolean isViewCartIn() {
        return isGoodsOpen() && isGoodsSales() && !isStockNoStockIconDisp();
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
     * 在庫状態:残りわずか
     * （アイコン表示用）
     * @return true：残りわずか
     */
    public boolean isStockFewIconDisp() {
        return HTypeStockStatusType.STOCK_FEW.getValue().equals(stockStatusPc);
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
     * 在庫状態:非販売
     * （アイコン表示用）
     * @return true：非販売
     */
    public boolean isStockNoSaleIconDisp() {
        return HTypeStockStatusType.NO_SALE.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:公開前
     * （アイコン表示用）
     * @return true：公開前
     */
    public boolean isStockBeforeOpenIconDisp() {
        return HTypeStockStatusType.BEFORE_OPEN.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:公開終了
     * （アイコン表示用）
     * @return true：公開終了
     */
    public boolean isStockOpenEndIconDisp() {
        return HTypeStockStatusType.OPEN_END.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:非公開
     * （アイコン表示用）
     * @return true：非公開
     */
    public boolean isStockNoOpenIconDisp() {
        return HTypeStockStatusType.NO_OPEN.getValue().equals(stockStatusPc);
    }

}