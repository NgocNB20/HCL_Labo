package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentType;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeWaitingFlag;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.order.OrderMessageDto;
import jp.co.itechh.quad.admin.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OnceOrderCancelGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.ConvenienceUtility;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 受注詳細修正画面Model
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DetailsUpdateModel extends AbstractModel {

    /** 受注番号 */
    public static final String FLASH_ORDERCODE = "orderCode";

    /** 決済方法：銀行振込（バーチャル口座 あおぞら） */
    protected static final String BANK_TRANSFER_AOZORA_TYPE = "36";

    /** 決済方法：Pay-easy（ペイジー） */
    protected static final String PAY_EASY_TYPE = "4";

    /** 決済方法：コンビニ */
    protected static final String CONVENI_TYPE = "3";

    /************************************
     ** 受注概要情報
     ************************************/
    /** 処理日時 */
    private Timestamp processTime;

    /** 督促メール送信済みフラグ */
    private HTypeSend reminderSentFlag;

    /** 期限切れメール送信済みフラグ */
    private HTypeSend expiredSentFlag;

    /** メール送信要否 */
    private String updateMailRequired;

    /** 受注日時（必須） */
    private Timestamp orderTime;

    /** キャンセル日時 */
    private Timestamp cancelTime;

    /** 期限切れメール送信（キャンセル処理）予定日 */
    private Timestamp cancelableDate;

    /** キャンセルフラグ（必須） */
    private HTypeCancelFlag cancelFlag = HTypeCancelFlag.OFF;

    /** 保留中フラグ（受注インデックスから設定） */
    private HTypeWaitingFlag waitingFlag = HTypeWaitingFlag.OFF;

    /** 受注状態（必須） */
    private HTypeOrderStatus orderStatus;

    /** 受注サイト種別（必須） */
    private HTypeSiteType orderSiteType;

    /** キャリア種別（必須） */
    private HTypeCarrierType carrierType;

    /** メモ（入力） */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class, OnceOrderCancelGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class, OnceOrderCancelGroup.class})
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}",
            groups = {ConfirmGroup.class, OnceOrderCancelGroup.class})
    private String memo;

    /************************************
     ** お客様情報
     ************************************/
    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** ご注文主メールアドレス */
    private String orderMail;

    /************************************
     ** 受注配送
     ************************************/
    /** 受注お届け先商品 */
    @Valid
    private OrderReceiverUpdateItem orderReceiverItem;

    /** 商品合計点数 */
    private BigDecimal orderGoodsCountTotal;

    /** 都道府県アイテムリスト */
    private Map<String, String> receiverPrefectureItems;

    /** 納品書添付フラグ（入力）） */
    @NotEmpty(message = "{AOX001006W}", groups = {ConfirmGroup.class})
    private String updateInvoiceAttachmentFlag;

    /** 納品書添付フラグアイテムリスト */
    private Map<String, String> updateInvoiceAttachmentFlagItems;

    /** 納品書添付フラグ（修正なし参照用） */
    private HTypeInvoiceAttachmentFlag invoiceAttachmentFlag = HTypeInvoiceAttachmentFlag.OFF;

    /************************************
     ** 受注請求
     ************************************/
    /** 請求状態（必須） */
    private HTypeBillStatus billStatus = HTypeBillStatus.BILL_NO_CLAIM;

    /** 異常フラグ（必須） */
    private HTypeEmergencyFlag emergencyFlag = HTypeEmergencyFlag.OFF;

    /** GMO連携解除フラグ */
    private HTypeGmoReleaseFlag gmoReleaseFlag = HTypeGmoReleaseFlag.NORMAL;

    /** 支払期限日 */
    private Timestamp paymentTimeLimitDate;

    /************************************
     ** 受注決済
     ************************************/
    /** 決済方法SEQ */
    private Integer settlementMethodSeq;

    /** 決済方法名 */
    private String settlementMethodName;

    /** 決済種別 */
    private HTypeSettlementMethodType settlementMethodType;

    /** 請求種別（必須） */
    private HTypeBillType billType;

    /** 決済タイプ */
    private HTypePaymentLinkType billTypeLink;

    /** 入金状態（受注金額及び入金累計金額により設定） */
    private String paymentStatus;

    /** コンビニ名称 */
    private String conveniName;

    /** 決済方法SEQ（入力） */
    @NotEmpty(message = "{AOX001005W}", groups = {ConfirmGroup.class})
    private String updateSettlementMethodSeq;

    /** 決済方法SEQ（変更前） */
    private String originalSettlementMethodSeq;

    /** 決済方法SEQアイテムリスト */
    private Map<String, String> updateSettlementMethodSeqItems;

    /** クレジット決済の決済IDリスト（カンマ区切り） */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String creditSettlementMethodSeqList;

    /** メール要否設定可能決済リスト（カンマ区切り） */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String mailRequiredSettlementMethodSeqList;

    /************************************
     ** 請求先
     ************************************/
    /** 請求先住所ID */
    private String orderBillingAddressId;

    /** 請求先住所氏名(姓) */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 16, groups = {ConfirmGroup.class})
    @HCZenkaku
    private String orderBillingLastName;

    /** 請求先住所氏名(名) */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 16, groups = {ConfirmGroup.class})
    @HCZenkaku
    private String orderBillingFirstName;

    /** 請求先住所フリガナ(姓) */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 40, groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {ConfirmGroup.class})
    @HCZenkakuKana
    private String orderBillingLastKana;

    /** 請求先住所フリガナ(名) */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 40, groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {ConfirmGroup.class})
    @HCZenkakuKana
    private String orderBillingFirstKana;

    /** 請求先住所-郵便番号 */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZIP_CODE_REGEX, message = "{HZipCodeValidator.INVALID_detail}",
             groups = {ConfirmGroup.class})
    @Length(min = 1, max = 7, groups = {ConfirmGroup.class})
    @HCHankaku
    private String orderBillingZipCode;

    /** 請求先住所-都道府県 */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    private String orderBillingPrefecture;

    /** 請求先住所-都道府県アイテムリスト */
    private Map<String, String> orderBillingPrefectureItems;

    /** 請求先住所-市区郡 */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 50, groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {ConfirmGroup.class})
    @HCZenkaku
    private String orderBillingAddress1;

    /** 請求先住所-町村・番地 */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 100, groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {ConfirmGroup.class})
    @HCZenkaku
    private String orderBillingAddress2;

    /** 請求先住所それ以降の住所 */
    @Length(min = 0, max = 200, groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {ConfirmGroup.class})
    @HCZenkaku
    private String orderBillingAddress3;

    /** 請求先住所電話番号 */
    @NotEmpty(groups = {ConfirmGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}", groups = {ConfirmGroup.class})
    @Length(min = 1, max = 11, groups = {ConfirmGroup.class})
    @HCHankaku
    private String orderBillingTel;

    /************************************
     ** 受注入金
     ************************************/
    /** 入金日時 */
    private Timestamp receiptTime;

    /** 入金金額（必須） */
    private BigDecimal receiptPrice = BigDecimal.ZERO;

    /************************************
     ** マルチペイメント請求情報
     ************************************/
    /** ﾏﾙﾁﾍﾟｲﾒﾝﾄ請求SEQ */
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

    /** GMOトランザクション ID */
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

    /************************************
     ** 金額情報
     ************************************/
    /** 受注金額（必須） */
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 商品金額合計（必須) */
    private BigDecimal goodsPriceTotal = BigDecimal.ZERO;

    /** 送料 */
    private BigDecimal carriage = BigDecimal.ZERO;

    /** 決済手数料（必須） */
    private BigDecimal settlementCommission = BigDecimal.ZERO;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

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

    /** 商品金額合計(税込み)（必須） */
    private BigDecimal postTaxGoodsPriceTotal = BigDecimal.ZERO;

    /** クーポン名 */
    private String couponName;

    /** 適用クーポン名 */
    private String applyCouponName;

    /** 適用クーポンID */
    private String applyCouponId;

    /** 適用クーポンSEQ */
    private Integer couponSeq;

    /** 適用クーポン枝番 */
    private Integer couponVersionNo;

    /** その他金額合計 */
    private BigDecimal othersPriceTotal = BigDecimal.ZERO;

    /** 追加料金リスト */
    private List<OrderAdditionalChargeItem> orderAdditionalChargeItems;

    /************************************
     ** ポップアップ表示用
     ************************************/
    /** 手数料 */
    private String orgCommissionDisp;

    /** 送料 */
    private String orgCarriageDisp;

    /** 送料ダイアログ表示フラグ */
    private boolean dispCarriageDialog;

    /** クーポン */
    private boolean dispCouponCancelDialog;

    /** 商品合計金額 */
    private BigDecimal goodsPriceTotalDisp;

    /** クーポン適用金額 */
    private BigDecimal couponDiscountLowerOrderPriceDisp;

    /** クーポン割引金額 */
    private BigDecimal couponDiscountPriceDisp;

    /** 決済手数料ダイアログ表示フラグ */
    private boolean commissionDialogDisplay = false;

    /** 決済手数料ダイアログ選択フラグ */
    private boolean commissionSelected = false;

    /** 送料ダイアログ表示フラグ */
    private boolean carriageDialogDisplay = false;

    /** 送料ダイアログ選択フラグ */
    private boolean carriageSelected = false;

    /************************************
     ** 制御用
     ************************************/
    /** 保留中フラグ */
    private boolean updateWaitingFlag = false;

    /** GMO連携解除フラグ */
    private boolean cancelOfCooperation = false;

    /**
     * クレジット受注 GMO通信エラーフラグ<br />
     * GMOに取引状態を参照した際、通信エラーが発生した場合に使用するフラグ
     */
    private boolean mulPayBillPaymentExceptionFlag = false;

    /** 決済Dtoマップ */
    private Map<String, SettlementDto> settlementDtoMap;

    /** 出荷済みフラグ ※GMO連携解除チェックボックス表示フラグ(isCancelOfCooperationView)に利用 */
    private boolean shippedFlag;

    @NotEmpty(groups = {ConfirmGroup.class})
    private String updateNoveltyPresentJudgmentStatus;

    /** 出荷状態アイテム */
    private HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus;

    /** 出荷状態アイテム */
    private Map<String, String> noveltyPresentJudgmentStatusItems;

    /**
     * GMO連携解除チェックボックス表示フラグ
     *
     * @return true:表示 false:非表示
     */
    public boolean isCancelOfCooperationView() {
        return shippedFlag;
    }

    /**
     * GMO連結解除状態フラグ
     *
     * @return true:GMO連携解除を表示 :GMO連携解除を表示しない
     */
    public boolean isRelease() {
        return HTypeGmoReleaseFlag.RELEASE.equals(gmoReleaseFlag);
    }

    /**
     * @return the mulPayBillPaymentExceptionFlag
     */
    public boolean isMulPayBillPaymentException() {
        return this.mulPayBillPaymentExceptionFlag;
    }

    /**
     * 決済方法変更が可能か不可能かを制御するコンディションメソッド
     *
     * @return true 変更可能
     * false 変更不可能
     */
    public boolean isSettlementUpdatable() {
        return false;
    }

    /**
     * 画面項目の編集が可能か不可能かを制御<br />
     * <p>
     * 適応箇所：お客様情報<br />
     * お届け先情報<br />
     * 配送情報<br />
     * 受注商品(「商品の追加」、「その他料金の追加」、「商品の削除」ボタン含む)<br />
     * 「確認」ボタン<br />
     *
     * @return true 編集可能
     * false 編集不可能
     */
    public boolean isOrderUpdatable() {
        // emergencyFlag・mulPayBillPaymentExceptionFlagのどれか一つでもTRUEの場合,対象の画面項目は編集不可
        if (isEmergency() || isMulPayBillPaymentException()) {
            return false;
        }
        return true;
    }

    /** マルチペイメント通信結果エラーメッセージ */
    private String mulPayErrMsg;

    /** クーポン対象商品番号 */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String couponTargetGoodsCode;

    /** クーポン対象商品名 */
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String couponTargetGoodsName;

    /** クーポン対象種別 */
    private String couponTargetType;

    /** クーポン対象全商品フラグ */
    private boolean couponTargetGoodsIsAllFlg = false;

    /** クーポンキャンセルダイアログ表示フラグ */
    private boolean couponCancelDialogDisplay = false;

    /** クーポン対象商品キャンセルメッセージフラグ */
    private boolean couponTargetGoodsCancelMessgeFlg = false;

    /** クーポン適用金額メッセージフラグ */
    private boolean couponDiscountLowerOrderPriceMessgeFlg = false;

    /** クーポン利用制限対象種別 value値 */
    private String couponLimitTargetTypeValue;

    /**
     * クーポン割引情報の表示／非表示判定処理
     *
     * @return true..クーポン名が設定されている場合
     */
    public boolean isDisplayCouponDiscount() {
        return StringUtils.isNotEmpty(couponName);
    }

    /**
     * クーポン表示フラグ(js用)<br/>
     *
     * @return "true"..クーポン使用 / "false"..クーポン未使用
     */
    public String getUseCouponFlg() {
        return String.valueOf(StringUtils.isNotEmpty(couponName));
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
     * マルチペイメント通信結果エラー有無
     *
     * @return true 通信結果エラーあり
     */
    public boolean isMulPayErr() {
        return (mulPayErrMsg != null);
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
     * 決済方法がコンビニ
     *
     * @return the settlementMethodType is CONVENIENCE or else
     */
    public boolean isConveni() {
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && payType.equals(CONVENI_TYPE);
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
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && payType.equals(PAY_EASY_TYPE);
    }

    /**
     * 銀行振込（バーチャル口座 あおぞら）決済かどうかを判定します。<br />
     * 画面表示用のConditionで使用。<br />
     *
     * @return true..銀行振込（バーチャル口座 あおぞら）false..銀行振込（バーチャル口座 あおぞら）ではない。
     */
    public boolean isBankTransferAozora() {
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && payType.equals(
                        BANK_TRANSFER_AOZORA_TYPE);
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
     * 最新のキャンセル状態を他画面に渡す為のダミー
     */
    private boolean cancel;

    /**
     * @return the cancelFlag is ON or OFF
     */
    public boolean isCancel() {
        return HTypeCancelFlag.ON.equals(cancelFlag);
    }

    /**
     * @return the waitingFlag is ON or OFF
     */
    public boolean isWaiting() {
        return HTypeWaitingFlag.ON.equals(waitingFlag);
    }

    /**
     * @return the emergencyFlag is ON or OFF
     */
    public boolean isEmergency() {
        return HTypeEmergencyFlag.ON.equals(emergencyFlag);
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
     * @return the mailRequired
     */
    public boolean isMailRequiredOn() {
        return "true".equals(updateMailRequired);
    }

    /**
     * @return the method
     */
    public String getMethodDsp() {
        if (method == null || "".equals(method)
            || EnumTypeUtil.getEnumFromValue(HTypePaymentType.class, method) == null) {
            return "";
        }
        return EnumTypeUtil.getEnumFromValue(HTypePaymentType.class, method).getLabel();
    }

    /** 注文チェックメッセージDTO */
    private OrderMessageDto orderMessageDto;

    /** 異常値の項目の表示スタイル */
    private String errStyleClass;

    /** クーポン割引額 */
    private String diffCouponDiscountPriceClass;

    /**
     * @return the cancelFlag is ON or OFF
     */
    public boolean isStateCancel() {
        if (cancelTime != null && cancelTime.compareTo(processTime) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * キャンセル以降の処理の場合、true を返却する
     *
     * @return the cancelFlag is ON or OFF
     */
    public boolean isStateAfterCancel() {
        if (cancelTime != null && cancelTime.compareTo(processTime) < 0) {
            return true;
        }
        return false;
    }

    /**
     * 決済手数料計算フラグ<br/>
     * true..変更前で計算する<br/>
     */
    private boolean commissionCalcFlag;

    /**
     * 送料計算フラグ<br/>
     * true..変更前で計算する<br/>
     */
    private boolean carriageCalcFlag;

    /**
     * 送料ダイアログを表示するか否か<br/>
     *
     * @return true..表示する
     */
    public boolean isDispCarriageDialog() {

        if (carriageDialogDisplay && !carriageSelected && (!commissionDialogDisplay || commissionSelected)) {
            return true;
        }
        return false;
    }

    /**
     * クーポンダイアログを表示するか否か<br/>
     *
     * @return rue..表示する
     */
    public boolean isDispCouponCancelDialog() {
        if (couponCancelDialogDisplay && !isDispCarriageDialog()) {
            return true;
        }
        return false;
    }

    /**
     * クーポンダイアログを表示するか否か<br/>
     *
     * @return rue..表示する
     */
    public boolean isDispCouponInvalid() {
        if (HTypeCouponLimitTargetType.OFF.getValue().equals(couponLimitTargetTypeValue)) {
            return true;
        }
        return false;
    }

    /** お客様情報エリアの表示制御 */
    private boolean displayMemberInfo;

    /** In case of order cancellation and modification is possible or not */
    private boolean orderCancelModifyPossible;

    /**
     * ご注文主年齢が存在するか判定<br/>
     *
     * @return true:存在する false:存在しない
     */
    public boolean isDispOrderAge() {
        return false;
    }

    /**
     * クーポン割引情報の表示／非表示判定処理を行う。<br />
     * 注文情報エリアにクーポン割引情報を表示するかどうか判定する為に利用する。
     *
     * @return クーポン割引額が１円以上の場合 true
     */
    public boolean isDisplayCouponPriceDiscount() {
        return couponDiscountPrice != null && couponDiscountPrice.compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * 決済方法が督促メール等送信要
     *
     * @return settlementMailRequired
     */
    public boolean isSettlementMailRequiredOn() {
        // if (this.modifiedOrderDetails.getSettlementMethodEntity() != null) {
        //     return (HTypeMailRequired.REQUIRED == this.modifiedOrderDetails.getSettlementMethodEntity().getSettlementMailRequired());
        // }
        return false;
    }

    /**
     * @return 督促メール送信済みフラグ
     */
    public HTypeSend getReminderSentFlag() {
        return isReset() ? HTypeSend.UNSENT : reminderSentFlag;
    }

    /**
     * @return 期限切れメール送信済みフラグ
     */
    public HTypeSend getExpiredSentFlag() {
        return isReset() ? HTypeSend.UNSENT : expiredSentFlag;
    }

    /**
     * 督促/期限切れメール送信状態のリセット有無を検査する
     *
     * @return trueの場合はリセット、それ以外は不要
     */
    private boolean isReset() {
        // if (ObjectUtils.isEmpty(originalOrderDetails) || ObjectUtils.isEmpty(modifiedOrderDetails)) {
        //     return false;
        // }
        // OrderBillEntity original = originalOrderDetails.getOrderBillEntity();
        // OrderBillEntity modified = modifiedOrderDetails.getOrderBillEntity();
        //
        // if (LOGGER.isDebugEnabled()) {
        //     LOGGER.debug(String.format("督促/期限切れメール送信リセット　決済SEQ:修正前[%s]/修正後[%s] 請求金額:修正前[%s]/修正後[%s]", original.getSettlementMethodSeq(), modified.getSettlementMethodSeq(), original.getBillPrice(), modified.getBillPrice()));
        // }
        // return !original.getSettlementMethodSeq().equals(modified.getSettlementMethodSeq()) || original.getBillPrice().compareTo(modified.getBillPrice()) != 0;
        return false;
    }

    /**
     * GMO連携解除状態フラグ
     *
     * @return true:GMO連携解除を表示 :GMO連携解除を表示しない
     */
    public boolean isReleaseConfirm() {
        if (cancelOfCooperation) {
            return false;
        }
        return HTypeGmoReleaseFlag.RELEASE.equals(gmoReleaseFlag);
    }
}