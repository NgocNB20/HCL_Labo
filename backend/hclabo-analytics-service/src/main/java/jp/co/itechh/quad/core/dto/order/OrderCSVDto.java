/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.order;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.util.Date;

import static jp.co.itechh.quad.ddd.presentation.ordersearch.api.OrderSearchHelper.DEFAULT_VALUE_INTEGER;

/**
 * 受注CSV出力用Dto
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderCSVDto {

    /**
     * 受注番号
     */
    @Id
    @CsvColumn(order = 10, columnLabel = "受注番号")
    private String orderCode;

    /**
     * 受注日時
     */
    @CsvColumn(order = 20, columnLabel = "受注日時", dateFormat = DateUtility.YMD_SLASH_HMS)
    private Date orderTime;

    /**
     * 受注状態
     */
    @CsvColumn(order = 30, columnLabel = "受注状態")
    private String orderStatus;

    /**
     * 出荷日
     */
    @CsvColumn(order = 40, columnLabel = "出荷日", dateFormat = DateUtility.YMD_SLASH)
    private Date shipmentDate;

    /**
     * 出荷状態
     */
    @CsvColumn(order = 50, columnLabel = "出荷状態")
    private String shipmentStatus;

    /**
     * キャンセル日時
     */
    @CsvColumn(order = 60, columnLabel = "キャンセル日時", dateFormat = DateUtility.YMD_SLASH_HMS)
    private Date cancelTime;

    /**
     * 受注金額
     */
    @CsvColumn(order = 70, columnLabel = "受注金額")
    @JsonSetter(nulls = Nulls.SKIP)
    private String orderPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 入金状態
     */
    @CsvColumn(order = 80, columnLabel = "入金状態")
    private String paymentStatus;

    /**
     * 入金日時
     */
    @CsvColumn(order = 90, columnLabel = "入金日時", dateFormat = DateUtility.YMD_SLASH_HMS)
    private Date paymentDateAndTime;

    /**
     * 商品管理番号
     */
    @CsvColumn(order = 100, columnLabel = "商品管理番号")
    @Field("orderProductList.goodsGroupCode")
    private String goodsGroupCode;

    /**
     * 商品番号
     */
    @CsvColumn(order = 110, columnLabel = "商品番号")
    @Field("orderProductList.goodsCode")
    private String goodsCode;

    /**
     * JANコード
     */
    @CsvColumn(order = 120, columnLabel = "JANコード")
    @Field("orderProductList.janCode")
    private String janCode;

    /**
     * 商品名
     */
    @CsvColumn(order = 130, columnLabel = "商品名")
    @Field("orderProductList.goodsGroupName")
    private String goodsGroupName;

    /**
     * 規格1
     */
    @CsvColumn(order = 140, columnLabel = "規格1")
    @Field("orderProductList.unitValue1")
    private String unitValue1;

    /**
     * 規格2
     */
    @CsvColumn(order = 150, columnLabel = "規格2")
    @Field("orderProductList.unitValue2")
    private String unitValue2;

    /**
     * 税率
     */
    @CsvColumn(order = 160, columnLabel = "税率")
    @Field("orderProductList.taxRate")
    private String taxRate;

    /**
     * 価格
     */
    @CsvColumn(order = 170, columnLabel = "価格")
    @Field("orderProductList.goodsPrice")
    @JsonSetter(nulls = Nulls.SKIP)
    private String goodsPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 数量
     */
    @CsvColumn(order = 180, columnLabel = "数量")
    @Field("orderProductList.goodsCount")
    @JsonSetter(nulls = Nulls.SKIP)
    private String goodsCount = DEFAULT_VALUE_INTEGER;

    /**
     * 小計
     */
    @CsvColumn(order = 190, columnLabel = "小計")
    @Field("orderProductList.summaryPrice")
    @JsonSetter(nulls = Nulls.SKIP)
    private String summaryPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 販売開始日時
     */
    @CsvColumn(order = 200, columnLabel = "販売開始日時", dateFormat = DateUtility.YMD_SLASH_HMS)
    @Field("orderProductList.saleStartTime")
    private Date saleStartTime;

    /**
     * 商品金額合計
     */
    @CsvColumn(order = 210, columnLabel = "商品金額合計")
    @JsonSetter(nulls = Nulls.SKIP)
    private String goodsPriceTotal = DEFAULT_VALUE_INTEGER;

    /**
     * 手数料
     */
    @CsvColumn(order = 220, columnLabel = "手数料")
    @JsonSetter(nulls = Nulls.SKIP)
    private String settlementCommission = DEFAULT_VALUE_INTEGER;

    /**
     * 配送料
     */
    @CsvColumn(order = 230, columnLabel = "配送料")
    @JsonSetter(nulls = Nulls.SKIP)
    private String orderDeliveryCarriage = DEFAULT_VALUE_INTEGER;

    /**
     * 調整金額合計
     */
    @CsvColumn(order = 240, columnLabel = "調整金額合計")
    @JsonSetter(nulls = Nulls.SKIP)
    private String totalAdjustmentAmount = DEFAULT_VALUE_INTEGER;

    /**
     * 消費税額
     */
    @CsvColumn(order = 250, columnLabel = "消費税額")
    @JsonSetter(nulls = Nulls.SKIP)
    private String taxPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 標準税率対象金額
     */
    @CsvColumn(order = 260, columnLabel = "標準税率対象金額")
    @JsonSetter(nulls = Nulls.SKIP)
    private String standardTaxTargetPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 標準税率消費税
     */
    @CsvColumn(order = 270, columnLabel = "標準税率消費税")
    @JsonSetter(nulls = Nulls.SKIP)
    private String standardTaxPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 軽減税率対象金額
     */
    @CsvColumn(order = 280, columnLabel = "軽減税率対象金額")
    @JsonSetter(nulls = Nulls.SKIP)
    private String reducedTaxTargetPrice = DEFAULT_VALUE_INTEGER;

    /**
     * 軽減税率消費税
     */
    @CsvColumn(order = 290, columnLabel = "軽減税率消費税")
    private String reducedTaxPrice;

    /**
     * クーポンID
     */
    @CsvColumn(order = 300, columnLabel = "クーポンID")
    private String couponId;

    /**
     * クーポン名
     */
    @CsvColumn(order = 310, columnLabel = "クーポン名")
    private String couponName;

    /**
     * クーポン支払額
     */
    @CsvColumn(order = 320, columnLabel = "クーポン支払額")
    @JsonSetter(nulls = Nulls.SKIP)
    private String couponPaymentAmount = DEFAULT_VALUE_INTEGER;

    /**
     * 請求先姓
     */
    @CsvColumn(order = 330, columnLabel = "請求先姓")
    private String billingLastName;

    /**
     * 請求先名
     */
    @CsvColumn(order = 340, columnLabel = "請求先名")
    private String billingFirstName;

    /**
     * 請求先セイ
     */
    @CsvColumn(order = 350, columnLabel = "請求先セイ")
    private String billingLastKana;

    /**
     * 請求先メイ
     */
    @CsvColumn(order = 360, columnLabel = "請求先メイ")
    private String billingFirstKana;

    /**
     * 請求先郵便番号
     */
    @CsvColumn(order = 370, columnLabel = "請求先郵便番号")
    private String billingZipCode;

    /**
     * 請求先都道府県
     */
    @CsvColumn(order = 380, columnLabel = "請求先都道府県")
    private String billingPrefecture;

    /**
     * 請求先市区郡
     */
    @CsvColumn(order = 390, columnLabel = "請求先市区郡")
    private String billingAddress1;

    /**
     * 請求先町村・番地
     */
    @CsvColumn(order = 400, columnLabel = "請求先町村・番地")
    private String billingAddress2;

    /**
     * 請求先マンション・建物名
     */
    @CsvColumn(order = 410, columnLabel = "請求先マンション・建物名")
    private String billingAddress3;

    /**
     * 請求先電話番号
     */
    @CsvColumn(order = 420, columnLabel = "請求先電話番号")
    private String billingTel;

    /**
     * お客様姓
     */
    @CsvColumn(order = 430, columnLabel = "お客様姓")
    private String customerLastName;

    /**
     * お客様名
     */
    @CsvColumn(order = 440, columnLabel = "お客様名")
    private String customerFirstName;

    /**
     * お客様セイ
     */
    @CsvColumn(order = 450, columnLabel = "お客様セイ")
    private String customerLastKana;

    /**
     * お客様メイ
     */
    @CsvColumn(order = 460, columnLabel = "お客様メイ")
    private String customerFirstKana;

    /**
     * お客様電話番号
     */
    @CsvColumn(order = 470, columnLabel = "お客様電話番号")
    private String customerTel;

    /**
     * お客様メールアドレス
     */
    @CsvColumn(order = 480, columnLabel = "お客様メールアドレス")
    private String customerMail;

    /**
     * お客様生年月日
     */
    @CsvColumn(order = 490, columnLabel = "お客様生年月日", dateFormat = DateUtility.YMD_SLASH)
    private Date customerBirthday;

    /**
     * お客様年代
     */
    @CsvColumn(order = 500, columnLabel = "お客様年代")
    private String customerAgeType;

    /**
     * お客様性別
     */
    @CsvColumn(order = 510, columnLabel = "お客様性別")
    private String customerSex;

    /**
     * リピート種別
     */
    @CsvColumn(order = 520, columnLabel = "リピート種別")
    @JsonSetter(nulls = Nulls.SKIP)
    private String repeatPurchaseType = DEFAULT_VALUE_INTEGER;

    /**
     * お届け先姓
     */
    @CsvColumn(order = 530, columnLabel = "お届け先姓")
    private String shippingLastName;

    /**
     * お届け先名
     */
    @CsvColumn(order = 540, columnLabel = "お届け先名")
    private String shippingFirstName;

    /**
     * お届け先セイ
     */
    @CsvColumn(order = 550, columnLabel = "お届け先セイ")
    private String shippingLastKana;

    /**
     * お届け先メイ
     */
    @CsvColumn(order = 560, columnLabel = "お届け先メイ")
    private String shippingFirstKana;

    /**
     * お届け先郵便番号
     */
    @CsvColumn(order = 570, columnLabel = "お届け先郵便番号")
    private String shippingZipCode;

    /**
     * お届け先都道府県
     */
    @CsvColumn(order = 580, columnLabel = "お届け先都道府県")
    private String shippingPrefecture;

    /**
     * お届け先市区郡
     */
    @CsvColumn(order = 590, columnLabel = "お届け先市区郡")
    private String shippingAddress1;

    /**
     * お届け先町村・番地
     */
    @CsvColumn(order = 600, columnLabel = "お届け先町村・番地")
    private String shippingAddress2;

    /**
     * お届け先マンション・建物名
     */
    @CsvColumn(order = 610, columnLabel = "お届け先マンション・建物名")
    private String shippingAddress3;

    /**
     * お届け先電話番号
     */
    @CsvColumn(order = 620, columnLabel = "お届け先電話番号")
    private String shippingTel;

    /**
     * 配送方法
     */
    @CsvColumn(order = 630, columnLabel = "配送方法")
    private String shippingMethodName;

    /**
     * お届け希望日
     */
    @CsvColumn(order = 640, columnLabel = "お届け希望日", dateFormat = DateUtility.YMD_SLASH)
    private Date receiverDate;

    /**
     * お届け時間指定
     */
    @CsvColumn(order = 650, columnLabel = "お届け時間指定")
    private String receiverTimeZone;

    /**
     * 納品書
     */
    @CsvColumn(order = 660, columnLabel = "納品書")
    private String invoiceAttachmentFlag;

    /**
     * 配送メモ
     */
    @CsvColumn(order = 670, columnLabel = "配送メモ")
    private String deliveryNote;

    /**
     * 配送状況確認番号
     */
    @CsvColumn(order = 680, columnLabel = "配送状況確認番号")
    private String deliveryStatusConfirmationNo;

    /**
     * 決済方法
     */
    @CsvColumn(order = 690, columnLabel = "決済方法")
    private String paymentMethodName;

    /**
     * リンク決済手段
     */
    @CsvColumn(order = 695, columnLabel = "リンク決済手段")
    private String linkPaymentMethod;

    /**
     * 請求状態
     */
    @CsvColumn(order = 700, columnLabel = "請求状態")
    private String billStatus;

    /**
     * 異常フラグ
     */
    @CsvColumn(order = 710, columnLabel = "異常フラグ")
    private String emergencyFlag;

    /**
     * 支払期限日
     */
    @CsvColumn(order = 720, columnLabel = "支払期限日", dateFormat = DateUtility.YMD_SLASH)
    private Date paymentTimeLimitDate;

    /**
     * メモ
     */
    @CsvColumn(order = 730, columnLabel = "メモ")
    private String memo;

    /**
     * 顧客ID
     */
    @CsvColumn(order = 740, columnLabel = "顧客ID")
    @JsonSetter(nulls = Nulls.SKIP)
    private String customerId = DEFAULT_VALUE_INTEGER;

    /**
     * 受注連携設定01
     */
    @CsvColumn(order = 750, columnLabel = "受注連携設定01")
    @Field("orderProductList.orderSetting1")
    private String orderSetting1;

    /**
     * 受注連携設定02
     */
    @CsvColumn(order = 760, columnLabel = "受注連携設定02")
    @Field("orderProductList.orderSetting2")
    private String orderSetting2;

    /**
     * 受注連携設定03
     */
    @CsvColumn(order = 770, columnLabel = "受注連携設定03")
    @Field("orderProductList.orderSetting3")
    private String orderSetting3;

    /**
     * 受注連携設定04
     */
    @CsvColumn(order = 780, columnLabel = "受注連携設定04")
    @Field("orderProductList.orderSetting4")
    private String orderSetting4;

    /**
     * 受注連携設定05
     */
    @CsvColumn(order = 790, columnLabel = "受注連携設定05")
    @Field("orderProductList.orderSetting5")
    private String orderSetting5;

    /**
     * 受注連携設定06
     */
    @CsvColumn(order = 800, columnLabel = "受注連携設定06")
    @Field("orderProductList.orderSetting6")
    private String orderSetting6;

    /**
     * 受注連携設定07
     */
    @CsvColumn(order = 810, columnLabel = "受注連携設定07")
    @Field("orderProductList.orderSetting7")
    private String orderSetting7;

    /**
     * 受注連携設定08
     */
    @CsvColumn(order = 820, columnLabel = "受注連携設定08")
    @Field("orderProductList.orderSetting8")
    private String orderSetting8;

    /**
     * 受注連携設定09
     */
    @CsvColumn(order = 830, columnLabel = "受注連携設定09")
    @Field("orderProductList.orderSetting9")
    private String orderSetting9;

    /**
     * 受注連携設定10
     */
    @CsvColumn(order = 840, columnLabel = "受注連携設定10")
    @Field("orderProductList.orderSetting10")
    private String orderSetting10;

    /**
     * ノベルティ判定状態
     */
    @CsvColumn(order = 850, columnLabel = "ノベルティ判定状態")
    private String noveltyPresentJudgmentStatus;

    /**
     * ノベルティ商品フラグ
     */
    @CsvColumn(order = 860, columnLabel = "ノベルティ商品フラグ")
    @Field("orderProductList.noveltyGoodsType")
    private String noveltyGoodsType;

    /**
     * 検査キット番号
     */
    @CsvColumn(order = 870, columnLabel = "検査キット番号")
    @Field("orderProductList.examKitCode")
    private String examKitCode;

    /**
     * 受付日
     */
    @CsvColumn(order = 880, columnLabel = "受付日", dateFormat = DateUtility.YMD_SLASH)
    @Field("orderProductList.receptionDate")
    private Date receptionDate;

    /**
     * 検体番号
     */
    @CsvColumn(order = 890, columnLabel = "検体番号")
    @Field("orderProductList.specimenCode")
    private String specimenCode;

    /**
     * 検査状態
     */
    @CsvColumn(order = 900, columnLabel = "検査状態")
    @Field("orderProductList.examStatus")
    private String examStatus;

    /**
     * 検査結果PDF
     */
    @CsvColumn(order = 910, columnLabel = "検査結果PDF")
    @Field("orderProductList.examResultsPdf")
    private String examResultsPdf;

    /**
     * ゆうプリR用郵送種別
     */
    @CsvColumn(order = 920, columnLabel = "ゆうプリR用郵送種別")
    private String youPackType;

}