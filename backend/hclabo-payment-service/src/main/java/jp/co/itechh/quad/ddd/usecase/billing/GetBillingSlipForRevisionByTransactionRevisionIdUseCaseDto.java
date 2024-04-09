/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentRevisionId;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 取引に紐づく請求伝票取得ユースケースDto
 */
@Data
public class GetBillingSlipForRevisionByTransactionRevisionIdUseCaseDto {

    /** 改訂用請求伝票ID */
    private BillingSlipRevisionId billingSlipRevisionId;

    /** 改訂用注文決済ID */
    private OrderPaymentRevisionId orderPaymentRevisionId;

    /** 請求伝票ID */
    private BillingSlipId billingSlipId;

    /** 請求先住所ID */
    private BillingAddressId billingAddressId;

    /** 注文決済 */
    private OrderPaymentId orderPaymentId;

    /** 決済方法ID */
    private String paymentMethodId;

    /** 決済方法名 */
    private String paymentMethodName;

    /** 決済トークン */
    private String paymentToken;

    /** 有効期限(月) */
    private String expirationMonth;

    /** 有効期限(年) */
    private String expirationYear;

    /** 支払区分（1：一括, 2：分割, 5：リボ） */
    private String paymentType;

    /** 分割回数 */
    private String dividedNumber;

    /** カード保存フラグ（保存時true） */
    private boolean registCardFlag;

    /** 登録済カード使用フラグ（登録済みtrue） */
    private boolean useRegistedCardFlag;

    /** カード番号のマスク値 */
    private String cardMaskNo;

    /** オーソリ期限日 */
    private Date authLimitDate;

    /** 決済代行連携解除フラグ */
    private boolean paymentAgencyReleaseFlag;

    /** GMO決済キャンセル状態 */
    private HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus;

    /** 決済手段識別子 */
    private String paymethod;

    /** 決済手段名 */
    private String payTypeName;

    /** 決済方法 */
    private String payType;

    /** 入金日 */
    private Date moneyReceiptTime;

    /** リンク決済タイプ */
    private HTypePaymentLinkType linkPayType;

    /** GMOキャンセル期限日 */
    private Timestamp cancelLimit;

    /** GMO後日払い支払期限日時 */
    private Timestamp laterDateLimit;

    /** 支払期限切れ処理予定日 */
    private Date cancelExpiredDate;
}
