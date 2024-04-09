/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeDividedNumber;
import jp.co.itechh.quad.core.constant.type.HTypePaymentType;
import jp.co.itechh.quad.ddd.domain.card.valueobject.CreditExpirationDate;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * クレジットカード決済 値オブジェクト
 */
@Getter
public class CreditPayment implements IPaymentRequest {

    /** 決済トークン */
    private final String paymentToken;

    /** マスク済みカード番号 */
    private final String maskedCardNo;

    /** 有効期限 */
    private final CreditExpirationDate expirationDate;

    /** 支払区分（1：一括, 2：分割, 5：リボ） */
    private final HTypePaymentType paymentType;

    /** 分割回数 */
    private final HTypeDividedNumber dividedNumber;

    /** 3Dセキュア有効フラグ */
    private final boolean enable3dSecureFlag;

    /** カード保存フラグ（保存時true） */
    private final boolean registCardFlag;

    /** 登録済カード使用フラグ（登録済みtrue） */
    private final boolean useRegistedCardFlag;

    /** オーソリ期限日 */
    private final Date authLimitDate;

    /** GMO連携解除フラグ */
    private final boolean gmoReleaseFlag;

    /**
     * コンストラクタ<br/>
     *
     * @param paymentToken        決済トークン
     * @param maskedCardNo        マスク済みカード番号
     * @param expirationDate      有効期限
     * @param paymentType         支払区分（1：一括, 2：分割, 5：リボ）
     * @param billType            請求種別
     * @param dividedNumber       分割回数
     * @param enable3dSecureFlag  ３Dセキュア有効フラグ
     * @param registCardFlag      カード保存フラグ（保存時true）
     * @param useRegistedCardFlag 登録済カード使用フラグ（登録済みtrue）
     */
    public CreditPayment(String paymentToken,
                         String maskedCardNo,
                         CreditExpirationDate expirationDate,
                         HTypePaymentType paymentType,
                         HTypeBillType billType,
                         HTypeDividedNumber dividedNumber,
                         boolean enable3dSecureFlag,
                         boolean registCardFlag,
                         boolean useRegistedCardFlag) {

        // チェック
        // 登録済カード不使用の場合
        if (!useRegistedCardFlag) {
            // 決済トークンが設定されていない場合はエラー
            AssertChecker.assertNotEmpty("paymentToken is empty", paymentToken);
        } else {
            // マスク済みカード番号が設定されていない場合はエラー
            AssertChecker.assertNotEmpty("maskedCardNo is empty", maskedCardNo);
        }
        AssertChecker.assertNotNull("expirationDate is null", expirationDate);
        AssertChecker.assertNotNull("paymentType is null", paymentType);
        // 支払区分が分割かつ、分割回数が設定されていない場合はエラー
        if (paymentType == HTypePaymentType.INSTALLMENT && dividedNumber == null) {
            throw new DomainException("PAYMENT_CRPA0001-E");
        }

        // 日付Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        this.paymentToken = paymentToken;
        this.maskedCardNo = maskedCardNo;
        this.expirationDate = expirationDate;
        this.paymentType = paymentType;
        this.dividedNumber = dividedNumber;
        this.enable3dSecureFlag = enable3dSecureFlag;
        this.registCardFlag = registCardFlag;
        this.useRegistedCardFlag = useRegistedCardFlag;

        // オーソリ保持期限の取得
        if (HTypeBillType.POST_CLAIM == billType) {
            String authoryHoldPeriod = PropertiesUtil.getSystemPropertiesValue("authory.hold.period");
            // 現在日＋オーソリ保持期限の日付を取得する
            Timestamp authLimitDate =
                            dateUtility.getAmountDayTimestamp(conversionUtility.toInteger(authoryHoldPeriod), true,
                                                              dateUtility.getCurrentDate()
                                                             );
            this.authLimitDate = conversionUtility.toDate(authLimitDate);
        } else {
            this.authLimitDate = null;
        }

        this.gmoReleaseFlag = false;
    }

    /**
     * コンストラクタ
     *
     * @param paymentToken        決済トークン
     * @param maskedCardNo        マスク済みカード番号
     * @param expirationDate      有効期限
     * @param paymentType         支払区分（1：一括, 2：分割, 5：リボ）
     * @param dividedNumber       分割回数
     * @param enable3dSecureFlag  ３Dセキュア有効フラグ
     * @param registCardFlag      カード保存フラグ（保存時true）
     * @param useRegistedCardFlag 登録済カード使用フラグ（登録済みtrue）
     * @param authLimitDate       オーソリ期限日
     * @param gmoReleaseFlag      GMO連携解除フラグ
     * @param customParams        案件用引数
     */
    public CreditPayment(String paymentToken,
                         String maskedCardNo,
                         CreditExpirationDate expirationDate,
                         HTypePaymentType paymentType,
                         HTypeDividedNumber dividedNumber,
                         boolean enable3dSecureFlag,
                         boolean registCardFlag,
                         boolean useRegistedCardFlag,
                         Date authLimitDate,
                         boolean gmoReleaseFlag,
                         Object... customParams) {
        this.paymentToken = paymentToken;
        this.maskedCardNo = maskedCardNo;
        this.expirationDate = expirationDate;
        this.paymentType = paymentType;
        this.dividedNumber = dividedNumber;
        this.enable3dSecureFlag = enable3dSecureFlag;
        this.registCardFlag = registCardFlag;
        this.useRegistedCardFlag = useRegistedCardFlag;
        this.authLimitDate = authLimitDate;
        this.gmoReleaseFlag = gmoReleaseFlag;
    }
}