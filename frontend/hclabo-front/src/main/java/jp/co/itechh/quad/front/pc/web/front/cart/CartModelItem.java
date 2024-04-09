/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.cart;

import jp.co.itechh.quad.front.annotation.converter.HCNumber;
import jp.co.itechh.quad.front.annotation.validator.HVNumber;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * カートModel カート商品Item<br/>
 * @author kaneda
 */
@Data
@Component
@Scope("prototype")
public class CartModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カート商品No */
    private Integer cartNo;

    /** カートSEQ */
    private Integer cartSeq;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品コード */
    private String gcd;

    /** 商品名 */
    private String goodsGroupName;

    /**規格タイトル1*/
    private String unitTitle1;

    /**規格値1*/
    private String unitValue1;

    /**規格タイトル2*/
    private String unitTitle2;

    /**規格値2*/
    private String unitValue2;

    /** 商品画像アイテム */
    private List<String> goodsImageItems;

    /** 商品単価(税抜き) */
    private BigDecimal goodsPrice;

    /** 商品単価(税込み) */
    private BigDecimal goodsPriceInTax;

    /** 商品数量 */
    @NotEmpty
    @HVNumber
    @Range(min = 1, max = 9999)
    @Digits(integer = 4, fraction = 0)
    @HCNumber
    private String gcnt;

    /** 商品金額（税抜き） */
    private BigDecimal goodsTotalPrice;

    /** 商品金額（税込み） */
    private BigDecimal goodsTotalPriceInTax;

    // HTMLへの追加のみで表示できるよう保持
    /** 商品納期 */
    private String deliveryType;

    /** 商品お知らせ情報 */
    private String goodsInfomation;

    /** お気に入りボタン表示フラグ */
    private boolean wishlistButtonView;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 商品グループコード */
    private String ggcd;

    /** 商品グループ画像 */
    private String goodsGroupImageFileName;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    /** 商品アイコンリスト*/
    private List<CartModelItem> goodsIconItems;

    /** 商品アイコン名*/
    private String iconName;

    /** 商品アイコンカラーコード*/
    private String iconColorCode;

    /** 公開状態 */
    private HTypeOpenDeleteStatus goodsOpenStatus;

    /** 公開開始日時 */
    private Timestamp openStartTime;

    /** 公開終了日時 */
    private Timestamp openEndTime;

    /** 販売状態 */
    private HTypeGoodsSaleStatus saleStatus;

    /** 販売開始日時 */
    private Timestamp saleStartTime;

    /** 販売終了日時 */
    private Timestamp saleEndTime;

    /** 商品消費税種別 */
    private HTypeGoodsTaxType goodsTaxType;

    /** 税率 */
    private BigDecimal taxRate;

    /** 酒類フラグ */
    private HTypeAlcoholFlag alcoholFlag;

    /** 一覧用在庫状況表示 */
    private String listStockStatusPc;

    /** 個別配送フラグ */
    private String individualDeliveryType;

    /**
     * お気に入り表示判定<br/>
     * お気に入り登録済みなら非表示<br/>
     * true：表示、false：非表示
     */
    private boolean wishlistView = true;

    /**
     * カートに入れるボタンの表示判定
     * <br/>
     * 以下の場合のみカートに入れるボタンを表示する<br/>
     * ・公開状態：公開<br/>
     * ・販売状態：販売<br/>
     * ・在庫状況：在庫なし以外<br/>
     *
     * @return true=表示する、false=表示しない
     */
    public boolean isViewCartIn() {
        return isGoodsOpen() && isGoodsSales() && !isNoStockIconDisp();
    }

    /**
     * 納期が設定されているか確認<br/>
     * true:設定されている<br/>
     *
     * @return true:納期が設定されている
     */
    public boolean isViewDeliveryType() {
        return StringUtils.isNotEmpty(deliveryType);
    }

    /**
     * 商品グループ画像をセット（サムネイル画像）
     * @return goodsName
     */
    public String getGoodsGroupImageThumbnailAlt() {
        String name = null;
        if (StringUtils.isNotEmpty(goodsGroupName)) {
            name = goodsGroupName;
        }
        return name;
    }

    /**
     * 在庫状態:在庫なし
     * （一覧アイコン表示用）
     * @return true：在庫なし
     */
    public boolean isNoStockIconDisp() {
        return HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:在庫あり
     * （一覧アイコン表示用）
     * @return true：販売期間終了
     */
    public boolean isSellOutIconDisp() {
        return HTypeStockStatusType.SOLDOUT.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:販売前
     * （一覧アイコン表示用）
     * @return true：販売前
     */
    public boolean isBeforeSaleIconDisp() {
        return HTypeStockStatusType.BEFORE_SALE.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:在庫あり
     * （一覧アイコン表示用）
     * @return true：在庫あり
     */
    public boolean isStockPossibleSalesIconDisp() {
        return HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:残りわずか
     * （一覧アイコン表示用）
     * @return true：残りわずか
     */
    public boolean isStockFewIconDisp() {
        return HTypeStockStatusType.STOCK_FEW.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:非販売
     * （一覧アイコン表示用）
     * @return true：非販売
     */
    public boolean isNoSaleIconDisp() {
        return HTypeStockStatusType.NO_SALE.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:公開前
     * （一覧アイコン表示用）
     * @return true：公開前
     */
    public boolean isStockBeforeOpenIconDisp() {
        return HTypeStockStatusType.BEFORE_OPEN.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:公開終了
     * （一覧アイコン表示用）
     * @return true：公開終了
     */
    public boolean isStockOpenEndIconDisp() {
        return HTypeStockStatusType.OPEN_END.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:非公開
     * （一覧アイコン表示用）
     * @return true：非公開
     */
    public boolean isStockNoOpenIconDisp() {
        return HTypeStockStatusType.NO_OPEN.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:予約受付前
     * （一覧アイコン表示用）
     * @return true：予約受付前
     */
    public boolean isStockBeforeReservationDisp() {
        return HTypeStockStatusType.BEFORE_RESERVATIONS.getValue().equals(listStockStatusPc);
    }

    /**
     * 在庫状態:予約受付中
     * （一覧アイコン表示用）
     * @return true：予約受付中
     */
    public boolean isStockOnReservationDisp() {
        return HTypeStockStatusType.ON_RESERVATIONS.getValue().equals(listStockStatusPc);
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
     * 規格1の有無判定<br/>
     * @return true=有、false=無
     */
    public boolean isUnit1() {
        return StringUtils.isNotEmpty(unitTitle1);
    }

    /**
     * 規格2の有無判定<br/>
     * @return true=有、false=無
     */
    public boolean isUnit2() {
        return StringUtils.isNotEmpty(unitTitle2);
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
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        return whatsnewDate.compareTo(dateUtility.getCurrentDate()) >= 0;
    }

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isAlcoholOn() {
        if (alcoholFlag == null || HTypeAlcoholFlag.ALCOHOL != alcoholFlag) {
            return false;
        }
        return true;
    }

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isIndividualDeliveryOn() {
        if (StringUtils.isEmpty(individualDeliveryType) || !HTypeIndividualDeliveryType.ON.getLabel()
                                                                                          .equals(individualDeliveryType)) {
            return false;
        }
        return true;
    }

}