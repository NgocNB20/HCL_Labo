/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipModel;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static jp.co.itechh.quad.front.constant.type.HTypePaymentType.INSTALLMENT;
import static jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType.CREDIT;
import static jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType.LINK_PAYMENT;

/**
 * 注文内容確認 Model
 *
 * @author Pham Quang Dieu
 */
@Data
public class ConfirmModel extends AbstractModel {

    public ConfirmModel() {
        // 販売伝票Model初期化
        this.salesSlip = new SalesSlipModel();
    }

    /** 注文確認画面用：コンビニインクルードページ取得キー */
    protected static final String PREFFIX_CONFIRM = "conveni.payment.procedure.confirm.";

    /** 注文完了画面用：コンビニインクルードページ取得キー */
    protected static final String PREFFIX_COMPLETE = "conveni.payment.procedure.complete.";

    /** 決済方法：銀行振込（バーチャル口座 あおぞら） */
    protected static final String BANK_TRANSFER_AOZORA_TYPE = "36";

    /** 決済方法：コンビニ */
    protected static final String CONVENIENCE_TYPE = "3";

    /** 決済方法：ペイジー */
    protected static final String PAYEASY_TYPE = "4";

    // << お届け先情報
    /** お届け先情報-氏名 **/
    private String addressName;

    /** お届け先情報-フリガナ **/
    private String addressKana;

    /** お届け先情報-郵便番号 **/
    private String addressZipCode;

    /** お届け先情報-住所 **/
    private String address;

    /** お届け先情報-電話番号 **/
    private String addressTel;

    // << 配送方法
    /** 配送方法 **/
    private String deliveryMethodName;

    /** お届け希望日選択値 */
    private String deliveryDate;

    /** 配送時間選択値 */
    private String deliveryTime;

    /** 納品書要否フラグ */
    private boolean invoiceNecessaryFlag;

    // << 決済方法

    /** 決済方法名 **/
    private String settlementMethodName;

    /** 決済方法説明文 **/
    private String paymentNote;

    /** コンビニ名 */
    private String convenienceName;

    /** 決済方法エンティティ */
    private SettlementMethodEntity settlementMethodEntity;

    /** 登録済みカード番号 */
    private String registedCardMaskNo;

    /** 新規入力カード番号 */
    private String newCardMaskNo;

    /** カード有効期限（月） */
    private String expirationDateMonth;

    /** カード有効期限（年） */
    private String expirationDateYear;

    /** トークン */
    private String token;

    /** セキュリティコード */
    @HCHankaku
    private String securityCode;

    /** 支払い区分 */
    private String paymentType;

    /** 分割回数 */
    private Integer paymentFrequency;

    /** コンビニ決済支払い手順表示 */
    private boolean paymentProcedureDisplay;

    /** コンビニ決済支払い手順表示パス(確認画面用) */
    private String paymentProcedureConfirmDisplaySrc;

    /** コンビニ決済支払い手順表示パス(完了画面用) */
    private String paymentProcedureCompleteDisplaySrc;

    // << お客様情報
    /** お客様情報-氏名 **/
    private String billingName;

    /** お客様情報-フリガナ **/
    private String billingKana;

    /** お客様情報-郵便番号 **/
    private String billingZipCode;

    /** お客様情報-住所 **/
    private String billingAddress;

    /** 住所を確認する **/
    private boolean isEqualAddress;

    /** 請求メモ **/
    private String billingShippingMemo;

    /** 住所発送メモ **/
    private String addressShippingMemo;

    /** お客様情報-電話番号 **/
    private String billingTel;

    /** お客様情報-連絡先電話番号 **/
    // TODO 項目存在してない？
    private String billingContactTel;

    /** お客様情報-氏名（姓） **/
    private String billingLastName;

    /** お客様情報-氏名（名） **/
    private String billingFirstName;

    /** 商品詳細Dtoリスト */
    private List<GoodsDetailsDto> goodsDetailDtoList;

    /** 販売伝票Model */
    SalesSlipModel salesSlip;

    /**
     * 注文コード<br/>
     * 注文完了画面に表示
     */
    private String orderCode;

    /** Pay-Easy 暗号化決済番号 */
    private String encryptReceiptNo;

    // << 利用規約画面へ渡す情報
    /** 受付番号 */
    private String receiptNo;

    /** 請求金額 */
    private BigDecimal billPrice;

    /** 注文者の姓 */
    private String orderLastName;

    /** 注文者の名 */
    private String orderFirstName;

    /** 受信支払期限 */
    private String paymentTerm;

    /** 電話番号 */
    private String tel;

    /** 収納機関番号 */
    private String bkCode;

    /** お客様番号 */
    private String custId;

    /** 確認番号 */
    private String confNo;

    /** 払込期限 */
    private Timestamp paymentTimeLimitDate;

    /** Pay-Easy 暗号化決済番号 (通信用) */
    private String code;

    /** Facebookを利用するか？ */
    private boolean useFacebook;

    /** Twitterを利用するか？ */
    private boolean useTwitter;

    /** Lineを利用するか？ */
    private boolean useLine;

    /** Twitter アカウント名 */
    private String twitterVia;

    /** ペイメントURL */
    private String paymentUrl;

    /** コンビニコード */
    private String convenience;

    /** 決済方法 */
    private String payType;

    /** 決済手段名 */
    private String payTypeName;

    /** 決済手段識別子 */
    private String payMethod;

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

    /** 3Dセキュア用モデル */
    private SecureModel secureModel;

    /** 配送先住所を確認する */
    private boolean isAddressSelected = false;

    /** 請求先住所を確認してください */
    private boolean isBillingSelected = false;

    /** 軽減税率対象商品があるか */
    private boolean isReducedTaxRate = false;

    /**
     * PayEasy決済かどうかを判定します。<br />
     * 画面表示用のConditionで使用。<br />
     *
     * @return true..Pay-Easy。false..Pay-Easyではない。
     */
    public boolean isPayEasy() {
        return PAYEASY_TYPE.equals(this.payType);
    }

    /**
     * 銀行振込（バーチャル口座 あおぞら）決済かどうかを判定します。<br />
     * 画面表示用のConditionで使用。<br />
     *
     * @return true..銀行振込（バーチャル口座 あおぞら）false..銀行振込（バーチャル口座 あおぞら）ではない。
     */
    public boolean isBankTransferAozora() {
        return BANK_TRANSFER_AOZORA_TYPE.equals(this.payType);
    }

    /**
     * コンディション<br/>
     * 選択されているクレジットの支払い区分が分割かどうか
     *
     * @return true..分割 / false..分割以外
     */
    public boolean isDividedPaymentType() {
        if (this.paymentFrequency == null) {
            return false;
        }
        return this.paymentType.equals(INSTALLMENT.getValue());
    }

    /**
     * トークン決済かどうか<br/>
     *
     * @return boolean true:トークン決済 / false:トークン決済ではない
     */
    public boolean isToken() {
        return StringUtils.isNotEmpty(this.token);
    }

    /**
     * コンディション<br />
     * お届け時間帯が指定されているかどうか
     *
     * @return true..指定 / false..未指定
     */
    public boolean isExistsDeliveryTime() {
        return deliveryTime != null;
    }

    /**
     * コンディション<br />
     * お届け希望日が指定されているかどうか
     *
     * @return true..指定 / false..未指定
     */
    public boolean isExistsDeliveryDate() {
        return deliveryDate != null;
    }

    /**
     * コンディション<br />
     * 選択されている決済方法がコンビニかどうか
     *
     * @return true..コンビニ / false..コンビニ以外
     */
    public boolean isConvenienceType() {
        if (this.settlementMethodEntity == null) {
            return false;
        }
        return LINK_PAYMENT.equals(this.settlementMethodEntity.getSettlementMethodType()) && CONVENIENCE_TYPE.equals(
                        this.payType);
    }

    /**
     * コンディション<br />
     * 選択されている決済方法がクレジットかどうか
     *
     * @return true..クレジット / false..クレジット以外
     */
    public boolean isCreditType() {
        if (this.settlementMethodEntity == null) {
            return false;
        }
        return CREDIT.equals(this.settlementMethodEntity.getSettlementMethodType());
    }

    /**
     * 完了画面への遷移有無判定<br/>
     * 注文チェックでエラーが発生した場合、完了画面へ遷移させないための条件制御
     *
     * @return true...遷移許可 / false...遷移不可
     */
    public boolean isNextComplete() {
        return true;
    }

    /**
     * ログインしてるかどうかを判定します。<br />
     * 画面表示のConditionとして使用。<br />
     *
     * @return true..ログインしてる。false..ログインしてない。
     */
    public boolean isLogin() {
        // 共通情報Helper取得
        CommonInfoUtility commonInfoUtility = ApplicationContextUtility.getBean(CommonInfoUtility.class);
        return commonInfoUtility.isLogin(getCommonInfo());
    }

    /**
     * コンディション<br />
     * 選択されている決済方法がクレジット決済、かつ3Dセキュア有効かどうか
     *
     * @return true..3Dセキュア有効 / false..3Dセキュア無効
     */
    public boolean isSecureValid() {
        if (this.settlementMethodEntity == null || !CREDIT.equals(
                        this.settlementMethodEntity.getSettlementMethodType())) {
            return false;
        }
        return this.settlementMethodEntity.getEnable3dSecure().equals(HTypeEffectiveFlag.VALID);
    }

    /**
     * @return true：軽減税率対象商品がある
     */
    public boolean isReducedTaxRate() {
        return isReducedTaxRate;
    }
}
