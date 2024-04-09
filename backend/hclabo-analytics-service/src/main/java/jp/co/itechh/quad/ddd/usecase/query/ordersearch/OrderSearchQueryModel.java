package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 受注検索クエリーモデル
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderSearchQueryModel {

    /**
     * 受注番号
     */
    @Id
    private String orderCode;

    /**
     * 受注商品リスト
     */
    private List<OrderProduct> orderProductList;

    /**
     * 商品金額合計
     */
    private Integer goodsPriceTotal;

    /**
     * 受注日時
     */
    private Date orderTime;

    /**
     * 受注状態
     */
    private String orderStatus;

    /**
     * 出荷日
     */
    private Date shipmentDate;

    /**
     * 出荷状態
     */
    private String shipmentStatus;

    /**
     * キャンセル日時
     */
    private Date cancelTime;

    /**
     * 受注金額
     */
    private Integer orderPrice;

    /**
     * 入金状態(詳細)   未入金:1 入金済み:2 入金不足:3 入金超過:4
     */
    private String paymentStatus;

    /**
     * 入金日時
     */
    private Date paymentDateAndTime;

    /**
     * 手数料
     */
    private Integer settlementCommission;

    /**
     * 配送料
     */
    private Integer orderDeliveryCarriage;

    /**
     * 調整金額合計
     */
    private Integer totalAdjustmentAmount;

    /**
     * 消費税額
     */
    private Integer taxPrice;

    /**
     * 標準税率対象金額
     */
    private Integer standardTaxTargetPrice;

    /**
     * 標準税率消費税
     */
    private Integer standardTaxPrice;

    /**
     * 軽減税率対象金額
     */
    private Integer reducedTaxTargetPrice;

    /**
     * 軽減税率消費税
     */
    private Integer reducedTaxPrice;

    /**
     * クーポンID
     */
    private String couponId;

    /**
     * クーポン名
     */
    private String couponName;

    /**
     * クーポン支払額
     */
    private Integer couponPaymentAmount;

    /**
     * 請求先姓
     */
    private String billingLastName;

    /**
     * 請求先名
     */
    private String billingFirstName;

    /**
     * 請求先セイ
     */
    private String billingLastKana;

    /**
     * 請求先メイ
     */
    private String billingFirstKana;

    /**
     * 請求先郵便番号
     */
    private String billingZipCode;

    /**
     * 請求先都道府県
     */
    private String billingPrefecture;

    /**
     * 請求先市区郡
     */
    private String billingAddress1;

    /**
     * 請求先町村・番地
     */
    private String billingAddress2;

    /**
     * 請求先マンション・建物名
     */
    private String billingAddress3;

    /**
     * 請求先電話番号
     */
    private String billingTel;

    /**
     * お客様姓
     */
    private String customerLastName;

    /**
     * お客様名
     */
    private String customerFirstName;

    /**
     * お客様セイ
     */
    private String customerLastKana;

    /**
     * お客様メイ
     */
    private String customerFirstKana;

    /**
     * お客様電話番号
     */
    private String customerTel;

    /**
     * お客様メールアドレス
     */
    private String customerMail;

    /**
     * お客様生年月日
     */
    private Date customerBirthday;

    /**
     * お客様年代
     */
    private String customerAgeType;

    /**
     * お客様性別
     */
    private String customerSex;

    /**
     * リピート種別
     */
    private Integer repeatPurchaseType;

    /**
     * お届け先姓
     */
    private String shippingLastName;

    /**
     * お届け先名
     */
    private String shippingFirstName;

    /**
     * お届け先セイ
     */
    private String shippingLastKana;

    /**
     * お届け先メイ
     */
    private String shippingFirstKana;

    /**
     * お届け先郵便番号
     */
    private String shippingZipCode;

    /**
     * お届け先都道府県
     */
    private String shippingPrefecture;

    /**
     * お届け先市区郡
     */
    private String shippingAddress1;

    /**
     * お届け先町村・番地
     */
    private String shippingAddress2;

    /**
     * お届け先マンション・建物名
     */
    private String shippingAddress3;

    /**
     * お届け先電話番号
     */
    private String shippingTel;

    /**
     * 配送方法
     */
    private String shippingMethodName;

    /**
     * お届け希望日
     */
    private Date receiverDate;

    /**
     * お届け時間指定
     */
    private String receiverTimeZone;

    /**
     * 納品書
     */
    private String invoiceAttachmentFlag;

    /**
     * 配送メモ
     */
    private String deliveryNote;

    /**
     * 配送状況確認番号
     */
    private String deliveryStatusConfirmationNo;

    /**
     * 決済方法
     */
    private String paymentMethodName;

    /**
     * リンク決済手段
     */
    private String linkPaymentMethodName;

    /**
     * 請求状態
     */
    private String billStatus;

    /**
     * 異常フラグ
     */
    private String emergencyFlag;

    /**
     * 支払期限日
     */
    private Date paymentTimeLimitDate;

    /**
     * メモ
     */
    private String memo;

    /**
     * 顧客ID
     */
    private Integer customerId;

    /**
     * 処理日時
     */
    private Date processTime;

    /**
     * 配送方法ID
     */
    private Integer shippingMethodId;

    /**
     * 決済方法ID
     */
    private Integer paymentMethodId;

    /**
     * ノベルティ判定状態
     */
    private String noveltyPresentJudgmentStatus;

    /**
     * リンク決済手段
     */
    private String linkPaymentMethod;

    /**
     * 検索用氏名
     */
    private String searchNameEmUc;

    /**
     * 検索用電話番号
     */
    private String searchTelEn;

    /**
     * ゆうプリR用郵送種別
     */
    private Integer youPackType;
}
