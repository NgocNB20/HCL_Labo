/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentType;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.admin.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.ConvenienceUtility;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 受注詳細抽象モデル<br/>
 *
 * @author kimura
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AbstractOrderDetailsModel extends AbstractModel {

    /************************************
     ** 注文ステータス 値オブジェクト(enum)の一覧<br//>
     ** jp.co.itechh.quad.ddd.domain.transaction.valueobject
     ************************************/

    /** 商品準備中 */
    public static final String ITEM_PREPARING = "ITEM_PREPARING";

    /** 入金確認中 */
    public static final String PAYMENT_CONFIRMING = "PAYMENT_CONFIRMING";

    /** 出荷完了 */
    public static final String SHIPMENT_COMPLETION = "SHIPMENT_COMPLETION";

    /** 取消 */
    public static final String CANCEL = "CANCEL";

    /** 請求決済エラー */
    public static final String PAYMENT_ERROR = "PAYMENT_ERROR";

    /** 決済方法：銀行振込（バーチャル口座 あおぞら） */
    protected static final String BANK_TRANSFER_AOZORA_TYPE = "36";

    /** 決済方法：Pay-easy（ペイジー） */
    protected static final String PAY_EASY_TYPE = "4";

    /** 決済方法：コンビニ */
    protected static final String CONVENI_TYPE = "3";

    /************************************
     ** 受注情報（ID、ステータス、フラグ、その他）
     ************************************/

    /** 受注番号 ※orderSeq(受注Seq) */
    private String orderCode;

    /** 取引ID ※orderCode(受注コード) */
    private String transactionId;

    /** 決済関連メール要否フラグ */
    private HTypeMailRequired settlementMailRequired = HTypeMailRequired.NO_NEED;

    /** 督促メール送信済みフラグ */
    private HTypeSend reminderSentFlag;

    /** 期限切れメール送信済みフラグ */
    private HTypeSend expiredSentFlag;

    /** 受注日時 */
    private Timestamp orderTime;

    /** 受注状態 */
    private HTypeOrderStatus orderStatus;

    /** 請求状態 */
    private HTypeBillStatus billStatus = HTypeBillStatus.BILL_NO_CLAIM;

    /** 異常フラグ */
    protected HTypeEmergencyFlag emergencyFlag = HTypeEmergencyFlag.OFF;

    /** GMO連携解除フラグ */
    private HTypeGmoReleaseFlag gmoReleaseFlag = HTypeGmoReleaseFlag.NORMAL;

    /** 入金状態 */
    private String paymentStatus;

    /** キャンセル日時 */
    protected Timestamp cancelTime;

    /** 期限切れメール送信（キャンセル処理）予定日 */
    private Timestamp cancelableDate;

    /** キャンセルフラグ */
    private HTypeCancelFlag cancelFlag = HTypeCancelFlag.OFF;

    /** メモ */
    private String memo;

    /** 処理日時 */
    private Timestamp processTime;

    /** リピート購入種別 */
    private HTypeRepeatPurchaseType repeatPurchaseType = HTypeRepeatPurchaseType.GUEST;

    /** 受注サイト種別 */
    private HTypeSiteType orderSiteType;

    /** キャリア種別 */
    private HTypeCarrierType carrierType;

    /************************************
     ** お客様情報
     ************************************/

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** ご注文主氏名(姓) */
    private String orderLastName;

    /** ご注文主氏名(名) */
    private String orderFirstName;

    /** ご注文主フリガナ(姓) */
    private String orderLastKana;

    /** ご注文主フリガナ(名) */
    private String orderFirstKana;

    /** ご注文主住所-郵便番号 */
    private String orderZipCode;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** ご注文主住所-都道府県 */
    private String orderPrefecture;

    /** ご注文主住所-市区郡 */
    private String orderAddress1;

    /** ご注文主住所-町村・番地 */
    private String orderAddress2;

    /** ご注文主住所-それ以降の住所 */
    private String orderAddress3;

    /** ご注文主電話番号 */
    private String orderTel;

    /** ご注文主メールアドレス */
    private String orderMail;

    /** ご注文主生年月日 */
    private Timestamp orderBirthday;

    /** ご注文主年齢 */
    private String orderAge;

    /** ご注文主年代 */
    private HTypeOrderAgeType orderAgeType;

    /** ご注文主性別 */
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /************************************
     ** 配送情報
     ************************************/

    /** 納品書添付フラグ */
    private HTypeInvoiceAttachmentFlag invoiceAttachmentFlag = HTypeInvoiceAttachmentFlag.OFF;

    /** 受注お届け先アイテム */
    @Valid
    private OrderReceiverUpdateItem orderReceiverItem;

    /** お届け希望日指定フラグ **/
    private HTypeReceiverDateDesignationFlag receiverDateDesignationFlag = HTypeReceiverDateDesignationFlag.ON;

    /************************************
     ** 商品情報
     ************************************/

    /** 商品消費税種別 */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 商品金額(税抜き)（表示の為のダミー） */
    private BigDecimal orderGoodsPrice = BigDecimal.ZERO;

    /** 商品金額(税込み)（表示の為のダミー） */
    private BigDecimal postTaxOrderGoodsPrice = BigDecimal.ZERO;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 商品合計点数 */
    private BigDecimal orderGoodsCountTotal;

    /************************************
     ** 金額情報
     ************************************/

    /** 受注金額 */
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 商品金額合計(税抜き) */
    private BigDecimal goodsPriceTotal = BigDecimal.ZERO;

    /** 商品金額合計（税込み） */
    private BigDecimal postTaxGoodsPriceTotal = BigDecimal.ZERO;

    /** 決済手数料 */
    private BigDecimal settlementCommission = BigDecimal.ZERO;

    /** 送料 */
    private BigDecimal carriage = BigDecimal.ZERO;

    /** 消費税 */
    private BigDecimal taxPrice = BigDecimal.ZERO;

    /** 標準税率対象金額 */
    private BigDecimal standardTaxTargetPrice = BigDecimal.ZERO;

    /** 標準税率消費税 */
    private BigDecimal standardTaxPrice = BigDecimal.ZERO;

    /** 軽減税率対象金額 */
    private BigDecimal reducedTaxTargetPrice = BigDecimal.ZERO;

    /** 軽減税率消費税 */
    private BigDecimal reducedTaxPrice = BigDecimal.ZERO;

    /************************************
     ** 追加料金情報
     ************************************/

    /** 追加料金 */
    @HCNumber
    private OrderAdditionalChargeItem orderAdditionalChargeItem;

    /** 追加料金リスト */
    @Valid
    private List<OrderAdditionalChargeItem> orderAdditionalChargeItems;

    /************************************
     ** 検査キット情報
     ************************************/

    /** 検査結果PDFの格納場所 */
    private String examresultsPdfStoragePath;

    /**「診療・診察時のお願い」のPDFのファイル名（フルパス） */
    private String examinationRulePdfPath;

    /** 検査キットリスト */
    private List<OrderExamKitItem> orderExamKitItemList;

    /** 検査結果を表示するかどうかを判断する */
    private boolean isExamResultsListNotEmpty = false;

    /** ダウンロード用検査結果PDF  */
    private String examResultsDownloadFileName;
    /************************************
     ** クーポン情報
     ************************************/

    /** クーポン名 */
    private String couponName;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

    /** 適用クーポン名 */
    private String applyCouponName;

    /** 適用クーポンID */
    private String applyCouponId;

    /** クーポン利用フラグ */
    private HTypeCouponLimitTargetType couponLimitTargetType;

    /************************************
     ** 決済情報
     ************************************/

    /** 決済方法SEQ */
    private Integer settlementMethodSeq;

    /** 決済方法名 */
    private String settlementMethodName;

    /** 決済種別 */
    protected HTypeSettlementMethodType settlementMethodType;

    /** 請求種別 */
    private HTypeBillType billType;

    /** 決済タイプ */
    private HTypePaymentLinkType billTypeLink;

    /** オーソリ期限日（決済日付＋オーソリ保持期間 ※時間無し） */
    private Timestamp authoryLimitDate;

    /************************************
     ** 請求情報
     ************************************/

    /** 支払期限日 */
    private Timestamp paymentTimeLimitDate;

    /************************************
     ** 請求先住所情報
     ************************************/

    /** 請求先住所氏名(姓) */
    private String orderBillingLastName;

    /** 請求先住所氏名(名) */
    private String orderBillingFirstName;

    /** 請求先住所フリガナ(姓) */
    private String orderBillingLastKana;

    /** 請求先住所フリガナ(名) */
    private String orderBillingFirstKana;

    /** 請求先住所-郵便番号 */
    private String orderBillingZipCode;

    /** 請求先住所-都道府県種別 */
    private HTypePrefectureType billingPrefectureType;

    /** 請求先住所-都道府県 */
    private String orderBillingPrefecture;

    /** 請求先住所-市区郡 */
    private String orderBillingAddress1;

    /** 請求先住所-町村・番地 */
    private String orderBillingAddress2;

    /** 請求先住所それ以降の住所 */
    private String orderBillingAddress3;

    /** 請求先住所電話番号 */
    private String orderBillingTel;

    /************************************
     ** 入金情報
     ************************************/

    /** 入金日時 */
    private Timestamp receiptTime;

    /************************************
     ** 売上情報
     ************************************/

    /** 売上日時 */
    private Timestamp salesTime;

    /** 売上フラグ */
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /************************************
     ** マルチペイメント請求情報
     ************************************/

    /** マルチペイメント通信結果エラーメッセージ */
    private String mulPayErrMsg;

    /** マルチペイメント請求SEQ */
    private Integer mulPayBillSeq;

    /** 決済方法 */
    private String payType;

    /** トランザクション種別 */
    private String tranType;

    /** オーダーID */
    private String orderId;

    /** 取引ID */
    private String accessId;

    /** 取引パスワード */
    private String accessPass;

    /** 処理区分 */
    private String jobCd;

    /** 支払方法 */
    private String method;

    /** 支払回数 */
    private Integer payTimes;

    /** カード登録連番モード */
    private String seqMode;

    /** カード登録連番 */
    private Integer cardSeq;

    /** 利用金額 */
    private BigDecimal amount;

    /** 税送料 */
    private BigDecimal tax;

    /** 3Dセキュア使用フラグ */
    private String tdFlag;

    /** ACS 呼出判定 */
    private String acs;

    /** 仕向先コード */
    private String forward;

    /** 承認番号 */
    private String approve;

    /** トランザクション ID */
    private String tranId;

    /** 決済日付 */
    private String tranDate;

    /** 支払先コンビニコード */
    private String convenience;

    /** 決済手段識別子 */
    private String payMethod;

    /** 決済手段名 */
    private String payTypeName;

    /** GMOキャンセル期限 */
    private String cancelLimit;

    /** 銀行名 */
    private String bankName;

    /** 支店名 */
    private String branchName;

    /** 振込先口座種別 */
    private String accountType;

    /** 振込先口座番号 */
    private String accountNumber;

    /** 取引有効期限 */
    private String exprireDate;

    /** 確認番号 */
    private String confNo;

    /** 受付番号 */
    private String receiptNo;

    /** 支払期限日時 */
    private String paymentTerm;

    /** お客様番号 */
    private String custId;

    /** 収納機関番号 */
    private String bkCode;

    /** 暗号化決済番号 */
    private String encryptReceiptNo;

    /** メールアドレス */
    private String mailAddress;

    /** エラーコード */
    private String errCode;

    /** エラー情報 */
    private String errInfo;

    /** コンビニ名称 */
    private String conveniName;

    /** ノベルティプレゼント判定状態 */
    private HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus =
                    HTypeNoveltyPresentJudgmentStatus.UNJUDGMENT;

    /**
     * クーポン割引情報の表示／非表示判定処理。<br />
     *
     * <pre>
     * 注文情報エリアにクーポン割引情報を表示するかどうか判定する為に利用する。
     * </pre>
     *
     * @return true..クーポン名が設定されている場合
     */
    public boolean isDisplayCouponDiscount() {
        return StringUtils.isNotEmpty(couponName);
    }

    /**
     * 適用クーポン情報の表示／非表示判定処理。<br />
     *
     * <pre>
     * 適用クーポン情報を表示するかどうか判定する為に利用する。
     * </pre>
     *
     * @return クーポンIDがnullでない場合 true
     */
    public boolean isDisplayApplyCoupon() {
        return applyCouponId != null;
    }

    /**
     * @return 追加料金エンティティ数
     */
    public BigDecimal getOrderAdditionalChargeItemsCount() {
        if (orderAdditionalChargeItems == null || orderAdditionalChargeItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(orderAdditionalChargeItems.size());
    }

    /**
     * @return 追加料金あり
     */
    public boolean isOrderAdditionalCharge() {
        if (orderAdditionalChargeItems == null || orderAdditionalChargeItems.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * @return 有効期限有無
     */
    public boolean isPaymentTimeLimitDateFlag() {
        return paymentTimeLimitDate != null;
    }

    /**
     * @return 有効期限（画面表示用）
     */
    public String getPaymentTermDsp() {
        String paymentTermDsp = "";
        if (paymentTerm != null && paymentTerm.length() >= 8) {
            paymentTermDsp = paymentTerm.substring(0, 4) + "/" + paymentTerm.substring(4, 6) + "/"
                             + paymentTerm.substring(6, 8);
        }
        return paymentTermDsp;
    }

    /**
     * コンビニ受付番号（4桁-7桁）
     *
     * @return コンビニ受付番号（4桁-7桁）
     */
    public String getReceiptNoWithHyphen() {
        String ret = "";
        if (receiptNo != null && receiptNo.length() >= 11) {
            return receiptNo.substring(0, 4) + "-" + receiptNo.substring(4, 11);
        }
        return ret;
    }

    /**
     * コンビニ受付番号なし
     *
     * @return true コンビニ受付番号がnullまたは空
     */
    public boolean isNullReceiptNo() {
        return (receiptNo == null || "".equals(receiptNo));
    }

    /**
     * 支払回数指定あり
     *
     * @return true 支払回数指定あり
     */
    public boolean isPayTimesSetting() {
        if (payTimes != null && payTimes > 0) {
            return true;
        }
        return false;
    }

    /**
     * お支払種別取得
     *
     * @return the method
     */
    public String getMethodDsp() {
        if (method == null || "".equals(method)
            || EnumTypeUtil.getEnumFromValue(HTypePaymentType.class, method) == null) {
            return "";
        }
        return EnumTypeUtil.getEnumFromValue(HTypePaymentType.class, method).getLabel();
    }

    /**
     * 決済方法がコンビニ
     *
     * @return the settlementMethodType is CONVENIENCE or else
     */
    public boolean isConveni() {
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && CONVENI_TYPE.equals(this.payType);
    }

    /**
     * 決済方法がコンビニの表示パターン１（ローソン・ファミリーマート・サークルＫサンクス・ミニストップ）
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni1() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni1(convenience);
    }

    /**
     * 決済方法がコンビニの表示パターン2（デイリーヤマザキ・セイコーマート・スリーエフ）
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni2() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni2(convenience);
    }

    /**
     * 決済方法がコンビニの表示パターン3（セブンイレブン）
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni3() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni3(convenience);
    }

    /**
     * 決済方法がコンビニの表示パターン４
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni4() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni4(convenience);
    }

    /**
     * 決済方法がコンビニの表示パターン５
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni5() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni5(convenience);
    }

    /**
     * 決済方法がクレジット
     *
     * @return the settlementMethodType is CREDIT or else
     */
    public boolean isCredit() {
        return HTypeSettlementMethodType.CREDIT.equals(settlementMethodType);
    }

    /**
     * 決済方法がLINK-PAY
     *
     * @return the settlementMethodType is LINK-PAY or else
     */
    public boolean isLinkPay() {
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType);
    }

    /**
     * 決済方法がPay-easy
     *
     * @return the settlementMethodType is PAY-EASY or else
     */
    public boolean isPayEasy() {
        return (HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && PAY_EASY_TYPE.equals(
                        this.payType));
    }

    /**
     * 銀行振込（バーチャル口座 あおぞら）決済かどうかを判定します。<br />
     * 画面表示用のConditionで使用。<br />
     *
     * @return true..銀行振込（バーチャル口座 あおぞら）false..銀行振込（バーチャル口座 あおぞら）ではない。
     */
    public boolean isBankTransferAozora() {
        return (HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && BANK_TRANSFER_AOZORA_TYPE.equals(
                        this.payType));
    }

    /**
     * 決済方法がその他
     *
     * @return the settlementMethodType is else
     */
    public boolean isElse() {
        if (!HTypeSettlementMethodType.CREDIT.equals(settlementMethodType)
            && !HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType)) {
            return true;
        }
        return false;
    }

    /**
     * @return the cancelFlag is ON or OFF
     */
    public boolean isCancel() {
        return HTypeCancelFlag.ON.equals(cancelFlag);
    }

    /**
     * @return the emergencyFlag is ON or OFF
     */
    public boolean isEmergency() {
        return HTypeEmergencyFlag.ON.equals(emergencyFlag);
    }

    /**
     * マルチペイメント通信結果エラー有無
     *
     * @return true 通信結果エラーあり
     */
    public boolean isMulPayErr() {
        return (mulPayErrMsg != null);
    }

    /**
     * GMO連携解除状態フラグ
     *
     * @return true:GMO連携解除を表示 :GMO連携解除を表示しない
     */
    public boolean isRelease() {
        return HTypeGmoReleaseFlag.RELEASE.equals(gmoReleaseFlag);
    }

    /**
     * @return tranId is not null?
     */
    public boolean isNullTranId() {
        if (tranId == null || tranId.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return orderId is not null?
     */
    public boolean isNullOrderId() {
        if (orderId == null || orderId.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 決済関連メール要否フラグkコンディション
     *
     * @return the mailRequired
     */
    public boolean isMailRequiredOn() {
        return HTypeMailRequired.REQUIRED == settlementMailRequired;
    }

    /**
     * 再オーソリ期限関連項目を表示するか否かを判定する<br/>
     *
     * <pre>
     * クレジット仮売上有効期限切れが近い受注を対象に表示。
     * オーソリ期限日 - オーソリ期限メール送信開始期間 <= 処理日 <= オーソリ期限日
     * </pre>
     *
     * @return true:表示する false:表示しない
     */
    public boolean isAuthoryLimit() {
        if (authoryLimitDate == null) {
            return false;
        }

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日の取得
        Timestamp currentDate = dateUtility.getCurrentDate();
        // メール送信開始期間（日）
        int mailSendStartPeriod = IntegerConversionUtil.toInteger(
                        PropertiesUtil.getSystemPropertiesValue("mail.send.start.period"));
        return dateUtility.isOpen(dateUtility.getAmountDayTimestamp(mailSendStartPeriod, false, authoryLimitDate),
                                  authoryLimitDate, currentDate
                                 );
    }

    /**
     * ご注文主年齢が存在するか判定<br/>
     *
     * @return true:存在する false:存在しない
     */
    public boolean isDispOrderAge() {
        return StringUtil.isNotEmpty(orderAge);
    }

    /**
     * 会員詳細ボタン制御用コンディション
     *
     * @return the memberFlg
     */
    public boolean isMemberFlg() {
        return memberInfoSeq != 0;
    }

}