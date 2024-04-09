/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * リンク決済
 */
@Getter
public class LinkPayment implements IPaymentRequest {

    /** GMOキャンセル漏れバッチ用フラグ：GMO決済キャンセル状態 */
    private final HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus;

    /** リンク決済結果：決済手段識別子 */
    private final String payMethod;

    /** リンク決済結果：決済方法(GMO) */
    private final String payType;

    /** リンク決済結果：決済手段名 */
    private final String payTypeName;

    /** リンク決済結果：リンク決済タイプ */
    private final HTypePaymentLinkType linkPaymentType;

    /** リンク決済結果：GMO即日払いキャンセル期限日 */
    private final Timestamp cancelLimit;

    /** リンク決済結果：GMO後日払い支払期限日時 */
    private final Timestamp laterDateLimit;

    /**
     * コンストラクタ
     *
     * @param gmoPaymentCancelStatus GMO決済キャンセル状態
     * @param payMethod              決済手段識別子
     * @param payType                決済方法(GMO)
     * @param payTypeName            決済手段名
     * @param linkPaymentType        リンク決済タイプ
     * @param cancelLimit            GMO即日払いキャンセル期限日
     * @param laterDateLimit         GMO後日払い支払期限日時
     */
    public LinkPayment(HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus,
                       String payMethod,
                       String payType,
                       String payTypeName,
                       HTypePaymentLinkType linkPaymentType,
                       Timestamp cancelLimit,
                       Timestamp laterDateLimit) {
        this.gmoPaymentCancelStatus = gmoPaymentCancelStatus;
        this.payMethod = payMethod;
        this.payType = payType;
        this.payTypeName = payTypeName;
        this.linkPaymentType = linkPaymentType;
        this.cancelLimit = cancelLimit;
        this.laterDateLimit = laterDateLimit;
    }
}