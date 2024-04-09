/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.front.constant.type.HTypePaymentType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.utility.ConvenienceUtility;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 注文履歴 Model
 *
 * @author kimura
 */
@Data
public class MemberHistoryModel extends AbstractModel {

    /************************************
     ** 注文状況.値　画面スタイル用の定数一覧
     ************************************/

    /** 商品準備中 */
    public static final String GOODS_PREPARING_STATUS = "0";

    /** 入金確認中 */
    public static final String PAYMENT_CONFIRMING_STATUS = "1";

    /** 出荷完了 */
    public static final String SHIPMENT_COMPLETION_STATUS = "2";

    /** キャンセル */
    public static final String CANCEL_STATUS = "3";

    /** 決済方法：銀行振込（バーチャル口座 あおぞら） */
    protected static final String BANK_TRANSFER_AOZORA_TYPE = "36";

    /** 決済方法：コンビニ */
    protected static final String CONVENIENCE_TYPE = "3";

    /** 決済方法：Pay-easy（ペイジー） */
    protected static final String PAYEASY_TYPE = "4";

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

    /************************************
     ** 配送ステータス 値オブジェクト(enum)の一覧<br//>
     ** jp.co.itechh.quad.ddd.domain.shipping.valueobject
     ************************************/

    /** 取消 */
    public static final String SHIPPING_CANCEL = "CANCEL";

    /** 返品 */
    public static final String SHIPPING_RETURN = "RETURN";

    /************************************
     ** 一覧画面
     ************************************/

    /** 注文履歴一覧情報 */
    private List<MemberHistoryModelItem> orderHistoryItems;

    /** 注文履歴一覧：ページ番号 */
    private String pnum;

    /** 注文履歴一覧：最大表示件数 */
    private int limit;

    /**
     * 注文履歴の有無<br/>
     *
     * @return true..無 false..有
     */
    public boolean isOrderHistoryEmpty() {

        if (orderHistoryItems == null) {
            return true;
        }
        return orderHistoryItems.isEmpty();
    }

    /************************************
     ** 詳細画面
     ************************************/

    /** 受注SQL（URLパラメタ） */
    private String ocd;

    /**
     * ocd保管用変数
     * <pre>
     * ocdがRedirectScopeだと、画面をリロードした時に保存されていないため
     * 再度認証画面に飛ばされる。
     * しかし、ocdは認証画面に引き継ぎたい値のためRedirectScopeである必要がある。
     * </pre>
     */
    private String saveOcd;

    /************************************
     ** 注文情報
     ************************************/
    /** 受注番号 */
    private String orderCode;

    /** 受注日時 */
    private Timestamp orderTime;

    /**
     * 注文状況
     * 受注状態(入金確認中、商品準備中、出荷完了) 、 キャンセル 、 保留
     */
    private String status;

    /**
     * 注文状況.値
     * 受注状態(入金確認中、商品準備中、出荷完了) 、 キャンセル 、 保留
     */
    private String statusValue;

    /** 決済方法 */
    private String settlementMethodName;

    /** お支払い金額 */
    private BigDecimal orderPrice;

    /************************************
     ** 合計情報
     ************************************/

    /** 商品合計金額 */
    private BigDecimal goodsPriceTotal;

    /** 送料 */
    private BigDecimal carriage;

    /** 決済手数料 */
    private BigDecimal settlementCommission;

    /** クーポン名 */
    private String couponName;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

    /** 消費税 */
    private BigDecimal taxPrice;

    /** 標準税率消費税 */
    private BigDecimal standardTaxPrice;

    /** 軽減税率消費税 */
    private BigDecimal reducedTaxPrice;

    /** 標準税率対象金額 */
    private BigDecimal standardTaxTargetPrice;

    /** 軽減税率対象金額 */
    private BigDecimal reducedTaxTargetPrice;

    /************************************
     ** お届け先
     ************************************/
    /** お届け先アイテム */
    private HistoryModelDeliveryItem orderDeliveryItem;

    /************************************
     ** 請求先
     ************************************/
    /** 請求先アイテム */
    private HistoryModelBillingAddressItem orderBillingAddressItem;

    /************************************
     ** 決済情報
     ************************************/
    /** 決済区分 */
    private HTypeSettlementMethodType settlementMethodType;

    /** お支払い方法 */
    private String paymentTypeDisplay;

    /** 分割回数 */
    private String paytimes;

    /** コンビニコード */
    private String convenienceCode;

    /** コンビニ名称 */
    private String convenienceName;

    /** 決済方法 */
    private String payType;

    /** 決済タイプ */
    private String billType;

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

    /** 受付番号 */
    private String receiptNo;

    /** 確認番号 */
    private String confNo;

    /** 払込期限 */
    private Timestamp paymentTimeLimitDate;

    /** 収納機関番号 */
    private String bkCode;

    /** お客様番号 */
    private String custId;

    /** ペイメントURL */
    private String paymentUrl;

    /** その他備考 */
    private String othersNote;

    /** Pay-Easy 暗号化決済番号 */
    private String code;

    /** 軽減税率対象商品があるか */
    private boolean isReducedTaxRate = false;

    /************************************
     ** 受注追加料金情報
     ************************************/

    /** 追加料金リスト */
    private List<HistoryModelAdditionalChargeItem> orderAdditionalChargeItems;

    /************************************
     ** 検査キット情報
     ************************************/

    /** 検査キットリスト */
    private List<HistoryModelExamKitItem> examKitItemList;

    /** 検査結果を表示するかどうかを判断する */
    private boolean isExamResultsListNotEmpty = false;

    /** 検査結果PDFの格納場所 */
    private String examresultsPdfStoragePath;

    /** 「診療・診察時のお願い」のPDFのファイル名（フルパス） */
    private String examinationRulePdfPath;

    /**
     * クーポンを利用しているかを判定する。<br />
     * <pre>
     * クーポン割引額が0でない場合は、利用していると判定。
     * 0の場合は、利用していないと判定。
     * </pre>
     *
     * @return 利用している場合：true、利用していない場合：false
     */
    public boolean isUseCoupon() {
        return couponDiscountPrice != null && couponDiscountPrice.compareTo(BigDecimal.ZERO) != 0;
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
     * 決済方法がクレジット
     * ※すっきり定義できないのでべた書き
     *
     * @return クレジットの場合true
     */
    public boolean isCredit() {
        return HTypeSettlementMethodType.CREDIT.equals(this.settlementMethodType);
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
     * お支払い方法が分割
     * ※すっきり定義できないのでべた書き
     *
     * @return 分割の場合true
     */
    public boolean isInstallment() {
        return HTypePaymentType.INSTALLMENT.getLabel().equals(this.paymentTypeDisplay);
    }

    /**
     * 銀行振込（バーチャル口座 あおぞら）決済かどうかを判定します。<br />
     * 画面表示用のConditionで使用。<br />
     *
     * @return true..銀行振込（バーチャル口座 あおぞら）false..銀行振込（バーチャル口座 あおぞら）ではない。
     */
    public boolean isBankTransferAozora() {

        if (payType == null) {
            return false;
        }
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(settlementMethodType) && this.payType.equals(
                        BANK_TRANSFER_AOZORA_TYPE);
    }

    /**
     * 決済方法がコンビニ
     * ※すっきり定義できないのでべた書き
     *
     * @return コンビニの場合true
     */
    public boolean isConveni() {

        if (payType == null) {
            return false;
        }
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(this.settlementMethodType) && this.payType.equals(
                        CONVENIENCE_TYPE);
    }

    /**
     * 決済方法がコンビニの表示パターン１
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni1() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni1(convenienceCode);
    }

    /**
     * 決済方法がコンビニの表示パターン２
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni2() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni2(convenienceCode);
    }

    /**
     * 決済方法がコンビニの表示パターン３
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni3() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni3(convenienceCode);
    }

    /**
     * 決済方法がコンビニの表示パターン４
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni4() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni4(convenienceCode);
    }

    /**
     * 決済方法がコンビニの表示パターン５
     *
     * @return コンビニ決済判定の結果
     */
    public boolean isConveni5() {
        ConvenienceUtility convenienceUtility = ApplicationContextUtility.getBean(ConvenienceUtility.class);
        return convenienceUtility.isConveni5(convenienceCode);
    }

    /**
     * 決済方法がペイジー
     * ※すっきり定義できないのでべた書き
     *
     * @return ペイジーの場合true
     */
    public boolean isPayeasy() {

        if (payType == null) {
            return false;
        }
        return HTypeSettlementMethodType.LINK_PAYMENT.equals(this.settlementMethodType) && this.payType.equals(
                        PAYEASY_TYPE);
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
     * PayEasyの金融機関選択ボタン表示の判断を行なう。<br/>
     * 以下の場合は表示しない<br/>
     * ・払込期限が、システム日付より大きい場合<br/>
     * ・入金済みの場合<br/>
     * ・キャンセルの場合<br/>
     *
     * @return true：表示する場合
     */
    public boolean isViewJlpbnkSelectButton() {

        // ペイジーのみ
        if (!isPayeasy()) {
            return false;
        }

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp now = dateUtility.getCurrentDate();
        // 払込期限が、システム日付より大きい場合は、表示しない。
        if (paymentTimeLimitDate != null && now.after(paymentTimeLimitDate)) {
            return false;
        }

        // 入金済みの場合は表示しない
        if (orderPrice.compareTo(BigDecimal.ZERO) > 0 && !PAYMENT_CONFIRMING_STATUS.equals(statusValue)) {
            return false;
        }

        // キャンセルの場合は表示しない
        if (status.equals(HTypeCancelFlag.ON.getLabel())) {
            return false;
        }
        return true;
    }

    /**
     * @return true：軽減税率対象商品がある
     */
    public boolean isReducedTaxRate() {
        return isReducedTaxRate;
    }

    /**
     * 受注状態：「入金確認中」or「商品準備中」の場合 ⇒ 検査状態は非表示
     */
    public boolean isDisplayExamStatus() {
        return !GOODS_PREPARING_STATUS.equals(statusValue) && !PAYMENT_CONFIRMING_STATUS.equals(statusValue);
    }
}