/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest;

import com.gmo_pg.g_pay.client.input.SearchTradeInput;
import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import com.gmo_pg.g_pay.client.output.BaseOutput;
import com.gmo_pg.g_pay.client.output.ChangeTranOutput;
import com.gmo_pg.g_pay.client.output.EntryTranOutput;
import com.gmo_pg.g_pay.client.output.ExecTranOutput;
import com.gmo_pg.g_pay.client.output.SearchTradeOutput;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeJobCode;
import jp.co.itechh.quad.core.dto.common.CheckMessageDto;
import jp.co.itechh.quad.core.dto.multipayment.HmAlterTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmChangeTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmEntryTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmExecTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmPaymentClientInput;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPaySiteEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CommunicateUtility;
import jp.co.itechh.quad.core.utility.MulPayUtility;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentRevisionId;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditAlterTranAdapter;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditChangeTranAdapter;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditEntryExecTranAdapter;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditSearchTradeAdapter;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * クレジットカード決済 値オブジェクト ドメインサービス
 */
@Service
public class CreditPaymentService {

    /** セキュリティコードの正規表現（数値のみかどうか） */
    protected static final String SECURITY_CODE_NUMBER_REGEX = "[\\d]*$";

    /** デフォルト最小入力文字数 */
    public static final int DEFAULT_MIN_LIMIT = 3;

    /** デフォルト最大入力文字数 */
    public static final int DEFAULT_MAX_LIMIT = 4;

    /** 最小入力文字数 */
    protected int minLimit = DEFAULT_MIN_LIMIT;

    /** 最大入力文字数 */
    protected int maxLimit = DEFAULT_MAX_LIMIT;

    /** 決済方法取得ロジック */
    private final SettlementMethodGetLogic settlementMethodGetLogic;

    /** GMOクレジット取引登録決済アダプター */
    private final ICreditEntryExecTranAdapter creditEntryExecTranAdapter;

    /** GMOクレジット取引通信（取消取消・取引再開・売上実行）アダプター */
    private final ICreditAlterTranAdapter creditAlterTranAdapter;

    /** GMOクレジット取引通信（金額変更）アダプター */
    private final ICreditChangeTranAdapter creditChangeTranAdapter;

    /** GMOクレジット取引状態参照 アダプター */
    private final ICreditSearchTradeAdapter creditSearchTradeAdapter;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** マルチペイメントヘルパークラス */
    private final MulPayUtility mulPayUtility;

    /** 通信ヘルパークラス */
    private final CommunicateUtility communicateUtility;

    /** コンストラクタ */
    public CreditPaymentService(SettlementMethodGetLogic settlementMethodGetLogic,
                                ICreditEntryExecTranAdapter creditEntryExecTranAdapter,
                                ICreditAlterTranAdapter creditAlterTranAdapter,
                                ICreditChangeTranAdapter creditChangeTranAdapter,
                                ICreditSearchTradeAdapter creditSearchTradeAdapter,
                                MulPayProxyService mulPayProxyService,
                                MulPayUtility mulPayUtility,
                                CommunicateUtility communicateUtility) {
        this.settlementMethodGetLogic = settlementMethodGetLogic;
        this.creditEntryExecTranAdapter = creditEntryExecTranAdapter;
        this.creditAlterTranAdapter = creditAlterTranAdapter;
        this.creditChangeTranAdapter = creditChangeTranAdapter;
        this.creditSearchTradeAdapter = creditSearchTradeAdapter;
        this.mulPayProxyService = mulPayProxyService;
        this.mulPayUtility = mulPayUtility;
        this.communicateUtility = communicateUtility;
    }

    /**
     * GMO決済依頼実行
     *
     * @param orderCode
     * @param orderPaymentId
     * @param customerId
     * @param paymentMethodId
     * @param paymentPrice
     * @param securityCode
     * @param callbackType
     * @param creditTdResultReceiveUrl
     * @param creditPayment
     */
    public ExecTranOutput executeGmoPaymentRequest(String orderCode,
                                                   OrderPaymentId orderPaymentId,
                                                   String customerId,
                                                   String paymentMethodId,
                                                   int paymentPrice,
                                                   String securityCode,
                                                   String callbackType,
                                                   String creditTdResultReceiveUrl,
                                                   CreditPayment creditPayment) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);
        AssertChecker.assertNotNull("orderPaymentId is null", orderPaymentId);
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        AssertChecker.assertNotEmpty("paymentMethodId is empty", paymentMethodId);
        AssertChecker.assertIntegerPositive("paymentPrice is zero or negative", paymentPrice);
        AssertChecker.assertNotNull("creditPayment is null", creditPayment);
        // セキュリティコードチェック
        if (!checkSecurityCode(securityCode)) {
            throw new DomainException("PAYMENT_CRPA0005-E");
        }

        // 決済方法マスタ情報を取得する
        SettlementMethodEntity settlementMethodDbEntity =
                        settlementMethodGetLogic.execute(Integer.parseInt(paymentMethodId));
        AssertChecker.assertNotNull("settlementMethodDbEntity is null", settlementMethodDbEntity);

        // 取引登録通信処理
        HmEntryTranInput hmEntryTranInput = createGmoEntryTranInputParam(orderCode, orderPaymentId, paymentPrice,
                                                                         settlementMethodDbEntity.getBillType(),
                                                                         creditPayment
                                                                        );
        EntryTranOutput entryOutput = creditEntryExecTranAdapter.creditEntryTran(hmEntryTranInput);
        checkGmoOutput(entryOutput);

        // 決済実行通信処理
        HmExecTranInput hmExecTranInput =
                        createGmoExecTranInputParam(entryOutput, hmEntryTranInput.getJobCd(), orderCode, orderPaymentId,
                                                    paymentPrice, customerId, securityCode, callbackType,
                                                    creditTdResultReceiveUrl, creditPayment
                                                   );
        ExecTranOutput execTranOutput = creditEntryExecTranAdapter.creditEntryExecTran(hmExecTranInput);
        checkGmoOutput(execTranOutput);

        return execTranOutput;
    }

    /**
     * GMO決済依頼取消実行
     *
     * @param orderCode              受注番号
     * @param orderPaymentRevisionId
     */
    public AlterTranOutput cancelGmoPaymentRequest(String orderCode, OrderPaymentRevisionId orderPaymentRevisionId) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);
        AssertChecker.assertNotNull("orderPaymentRevisionId is null", orderPaymentRevisionId);

        // キャンセル用の GMOクレジット決済変更通信 パラメータ作成
        HmAlterTranInput hmAlterTranInput = createGmoAlterTranInputParamCancel(orderCode, orderPaymentRevisionId);

        // GMOクレジット取引取消実行通信アダプターを呼び出す
        AlterTranOutput alterTranOutput = creditAlterTranAdapter.doAlterTran(hmAlterTranInput);
        checkGmoOutput(alterTranOutput);

        return alterTranOutput;
    }

    /**
     * GMO決済依頼取消実行 ※与信枠解放用
     *
     * @param mulPayBillEntity MulPayBillエンティティ
     */
    public AlterTranOutput cancelGmoPaymentRequest(MulPayBillEntity mulPayBillEntity) {

        // チェック
        AssertChecker.assertNotNull("mulPayBillEntity is null", mulPayBillEntity);

        // キャンセル用の GMOクレジット決済変更通信 パラメータ作成
        HmAlterTranInput hmAlterTranInput = createGmoAlterTranInputParamCancel(mulPayBillEntity, null);

        // GMOクレジット取引取消実行通信アダプターを呼び出す
        AlterTranOutput alterTranOutput = creditAlterTranAdapter.doAlterTran(hmAlterTranInput);
        checkGmoOutput(alterTranOutput);

        return alterTranOutput;
    }

    /**
     * GMO売上計上依頼実行
     *
     * @param orderCode              受注番号
     * @param orderPaymentRevisionId
     */
    public AlterTranOutput recordSales(String orderCode, OrderPaymentRevisionId orderPaymentRevisionId) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);
        AssertChecker.assertNotNull("orderPaymentRevisionId is null", orderPaymentRevisionId);

        // 売上計上用の GMOクレジット決済変更通信 パラメータ作成
        HmAlterTranInput hmAlterTranInput = createGmoAlterTranInputParamSale(orderCode, orderPaymentRevisionId);

        // GMOクレジット売上実行通信アダプターを呼び出す
        AlterTranOutput alterTranOutput = creditAlterTranAdapter.doAlterTran(hmAlterTranInput);

        // 通信で決済エラーが帰ってきた場合
        if (alterTranOutput.isErrorOccurred()) {

            // トランザクション情報がないか、180日以上経過していた場合
            if (communicateUtility.isOutdatedTran(alterTranOutput)) {
                throw new DomainException("SOO002214W", new String[] {orderCode});
            }
            // 仮売上の有効期限が切れていた場合
            if (communicateUtility.isAuthExpired(alterTranOutput)) {
                throw new DomainException("SOO002215W", new String[] {orderCode});
            }
        }

        checkGmoOutput(alterTranOutput);

        return alterTranOutput;
    }

    /**
     * 金額変更用の GMOクレジット決済変更通信
     *
     * @param orderCode              受注番号
     * @param modifiedBillingPrice   改訂後請求金額
     * @param orderPaymentRevisionId
     */
    public void changePrice(String orderCode, int modifiedBillingPrice, OrderPaymentRevisionId orderPaymentRevisionId) {
        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);
        AssertChecker.assertNotNull("orderPaymentRevisionId is null", orderPaymentRevisionId);

        // 売上計上用の GMOクレジット決済変更通信 パラメータ作成
        HmChangeTranInput hmChangeTranInput =
                        createGmoChangeTranInputParam(orderCode, modifiedBillingPrice, orderPaymentRevisionId);

        // GMOクレジット取引取消実行通信アダプターを呼び出す
        ChangeTranOutput changeTranOutput = creditChangeTranAdapter.doChangeTran(hmChangeTranInput);
        checkGmoOutput(changeTranOutput);
    }

    /**
     * GMO取引状態参照実行 ※与信枠解放用
     *
     * @param orderCode 受注番号
     */
    public SearchTradeOutput searchTrade(String orderCode) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        // 売上計上用の GMOクレジット決済変更通信 パラメータ作成
        SearchTradeInput hmSearchTradeInput = createGmoSearchTradeInputParam(orderCode);

        // GMOクレジット取引取消実行通信アダプターを呼び出す
        SearchTradeOutput searchTradeOutput = this.creditSearchTradeAdapter.doSearchTrade(hmSearchTradeInput);
        checkGmoOutput(searchTradeOutput);

        return searchTradeOutput;
    }

    /**
     * クレジット決済 取引登録パラメータ作成
     *
     * @param orderCode
     * @param orderPaymentId
     * @param paymentPrice
     * @param billType
     * @param creditPayment
     * @return HmEntryTranInput
     */
    private HmEntryTranInput createGmoEntryTranInputParam(String orderCode,
                                                          OrderPaymentId orderPaymentId,
                                                          int paymentPrice,
                                                          HTypeBillType billType,
                                                          CreditPayment creditPayment) {

        HmEntryTranInput input = new HmEntryTranInput();

        HTypeJobCode jobCode = HTypeBillType.PRE_CLAIM == billType ? HTypeJobCode.CAPTURE : HTypeJobCode.AUTH;
        input.setJobCd(EnumTypeUtil.getValue(jobCode));
        input.setOrderPaymentId(orderPaymentId.getValue());
        input.setOrderId(orderCode);
        input.setAmount(paymentPrice);

        if (creditPayment.isEnable3dSecureFlag()) {
            input.setTdFlag("2");
            input.setTds2Type(PropertiesUtil.getSystemPropertiesValue("gmo.3dsecure20.unsupport.treatment"));
        } else {
            input.setTdFlag("0");
        }

        return input;
    }

    /**
     * クレジット決済 決済実行パラメータ作成
     *
     * @param entryTranOutput
     * @param jobCd
     * @param orderCode
     * @param orderPaymentId
     * @param paymentPrice
     * @param customerId
     * @param securityCode
     * @param callbackType
     * @param creditTdResultReceiveUrl
     * @param creditPayment
     * @return HmExecTranInput
     */
    private HmExecTranInput createGmoExecTranInputParam(EntryTranOutput entryTranOutput,
                                                        String jobCd,
                                                        String orderCode,
                                                        OrderPaymentId orderPaymentId,
                                                        int paymentPrice,
                                                        String customerId,
                                                        String securityCode,
                                                        String callbackType,
                                                        String creditTdResultReceiveUrl,
                                                        CreditPayment creditPayment) {

        HmExecTranInput input = new HmExecTranInput();

        input.setAccessId(entryTranOutput.getAccessId());
        input.setAccessPass(entryTranOutput.getAccessPass());
        input.setJobCd(jobCd);

        input.setOrderPaymentId(orderPaymentId.getValue());
        input.setOrderId(orderCode);
        input.setAmount(paymentPrice);
        input.setMethod(EnumTypeUtil.getValue(creditPayment.getPaymentType()));
        if (creditPayment.getDividedNumber() != null) {
            input.setPayTimes(Integer.parseInt(EnumTypeUtil.getValue(creditPayment.getDividedNumber())));
        }
        input.setSecurityCode(securityCode);

        if (creditPayment.isUseRegistedCardFlag()) {
            // 登録済みカードを利用する場合

            MulPaySiteEntity mulPaySiteEntity = this.mulPayProxyService.getMulPaySiteEntity();
            input.setSiteId(mulPaySiteEntity.getSiteId());
            input.setSitePass(mulPaySiteEntity.getSitePassword());
            input.setMemberId(mulPayUtility.createPaymentMemberId(Integer.parseInt(customerId)));
            input.setSeqMode(HmPaymentClientInput.SEQ_MODE_LOGICAL);
            input.setCardSeq(HmPaymentClientInput.CARD_SEQ);
            input.setToken(null);
        } else {
            // 登録済みカードを利用しない場合

            input.setToken(creditPayment.getPaymentToken());
        }

        // 3Dセキュア有効の場合
        if (creditPayment.isEnable3dSecureFlag()) {
            input.setCallbackType(callbackType);
            input.setRetUrl(creditTdResultReceiveUrl);
        }

        return input;
    }

    /**
     * キャンセル用の GMOクレジット決済変更通信 パラメータ作成
     *
     * @param orderCode
     * @param orderPaymentRevisionId
     * @return GMO決済変更通信パラメータ
     */
    private HmAlterTranInput createGmoAlterTranInputParamCancel(String orderCode,
                                                                OrderPaymentRevisionId orderPaymentRevisionId) {

        // MulPayBillエンティティの取得
        MulPayBillEntity mulPayBillEntity = mulPayProxyService.getLatestNoErrorEntityByOrderId(orderCode);
        AssertChecker.assertNotNull("mulPayBillEntity is null", mulPayBillEntity);
        return createGmoAlterTranInputParamCancel(mulPayBillEntity, orderPaymentRevisionId);
    }

    /**
     * キャンセル用の GMOクレジット決済変更通信 渡されたMulPayBillエンティテでパラメータ作成
     *
     * @param mulPayBillEntity       MulPayBillエンティティ
     * @param orderPaymentRevisionId
     * @return GMO決済変更通信パラメータ
     */
    private HmAlterTranInput createGmoAlterTranInputParamCancel(MulPayBillEntity mulPayBillEntity,
                                                                OrderPaymentRevisionId orderPaymentRevisionId) {

        HmAlterTranInput input = new HmAlterTranInput();

        if (orderPaymentRevisionId == null) {
            input.setOrderPaymentId(mulPayBillEntity.getOrderPaymentId());
        } else {
            input.setOrderPaymentId(orderPaymentRevisionId.getValue());
        }
        input.setAccessId(mulPayBillEntity.getAccessId());
        input.setAccessPass(mulPayBillEntity.getAccessPass());
        input.setMethod(mulPayBillEntity.getMethod());
        input.setPayTimes(mulPayBillEntity.getPayTimes());

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        // JOBCODE を判定する
        HTypeJobCode jobCode = EnumTypeUtil.getEnumFromValue(HTypeJobCode.class, mulPayBillEntity.getJobCd());
        Timestamp currentDate = dateUtility.getCurrentTime();
        Timestamp tranDate = dateUtility.toTimestampValue(mulPayBillEntity.getTranDate(), "yyyyMMddHHmmss");
        if (dateUtility.compareDate(tranDate, currentDate)) {
            // 前回処理日と現在日が同日
            jobCode = HTypeJobCode.VOID;
        } else if (dateUtility.compareMonth(tranDate, currentDate)) {
            // 前回処理月と現在月が同月
            jobCode = HTypeJobCode.RETURN;
        } else {
            // 前回処理月と現在月が異なる
            if (HTypeJobCode.AUTH == jobCode) {
                // 前回JOBCODEがAUTH
                jobCode = HTypeJobCode.RETURN;
            } else {
                jobCode = HTypeJobCode.RETURNX;
            }
        }
        input.setJobCd(EnumTypeUtil.getValue(jobCode));

        return input;
    }

    /**
     * 売上計上用の GMOクレジット決済変更通信 パラメータ作成
     *
     * @param orderCode              受注番号
     * @param orderPaymentRevisionId
     * @return GMO決済変更通信パラメータ
     */
    private HmAlterTranInput createGmoAlterTranInputParamSale(String orderCode,
                                                              OrderPaymentRevisionId orderPaymentRevisionId) {

        // MulPayBillエンティティの取得
        MulPayBillEntity mulPayBillEntity = this.mulPayProxyService.getLatestNoErrorEntityByOrderId(orderCode);
        AssertChecker.assertNotNull("mulPayBillEntity is null", mulPayBillEntity);

        HmAlterTranInput input = new HmAlterTranInput();

        input.setOrderPaymentId(orderPaymentRevisionId.getValue());
        input.setAccessId(mulPayBillEntity.getAccessId());
        input.setAccessPass(mulPayBillEntity.getAccessPass());
        input.setMethod(mulPayBillEntity.getMethod());
        input.setPayTimes(mulPayBillEntity.getPayTimes());
        if (mulPayBillEntity.getAmount() != null) {
            input.setAmount(mulPayBillEntity.getAmount().intValue());
        }
        input.setJobCd(HTypeJobCode.SALES.getValue());

        return input;
    }

    /**
     * 金額変更用の GMOクレジット決済変更通信 パラメータ作成
     *
     * @param orderCode              受注番号
     * @param modifiedBillingPrice
     * @param orderPaymentRevisionId
     * @return GMO決済変更通信パラメータ
     */
    private HmChangeTranInput createGmoChangeTranInputParam(String orderCode,
                                                            int modifiedBillingPrice,
                                                            OrderPaymentRevisionId orderPaymentRevisionId) {

        // MulPayBillエンティティの取得
        MulPayBillEntity mulPayBillEntity = this.mulPayProxyService.getLatestNoErrorEntityByOrderId(orderCode);
        AssertChecker.assertNotNull("mulPayBillEntity is null", mulPayBillEntity);
        // 最終のJOBCODE
        HTypeJobCode jobcode = EnumTypeUtil.getEnumFromValue(HTypeJobCode.class, mulPayBillEntity.getJobCd());

        // 最終JOBCODEチェック
        if (modifiedBillingPrice != 0) {
            if (HTypeJobCode.AUTH == jobcode) {
                ;
            } else if (HTypeJobCode.CAPTURE == jobcode || HTypeJobCode.SALES == jobcode) {
                jobcode = HTypeJobCode.CAPTURE;
            } else {
                throw new DomainException("PAYMENT_CRPA0004-E");
            }
        }

        HmChangeTranInput input = new HmChangeTranInput();

        input.setOrderPaymentId(orderPaymentRevisionId.getValue());
        input.setAccessId(mulPayBillEntity.getAccessId());
        input.setAccessPass(mulPayBillEntity.getAccessPass());
        input.setMethod(mulPayBillEntity.getMethod());
        input.setPayTimes(mulPayBillEntity.getPayTimes());
        input.setAmount(modifiedBillingPrice);
        input.setJobCd(EnumTypeUtil.getValue(jobcode));

        return input;
    }

    /**
     * GMOクレジット取引状態参照通信 パラメータ作成
     *
     * @param orderCode 受注番号
     * @return GMO取引状態参照通信パラメータ
     */
    private SearchTradeInput createGmoSearchTradeInputParam(String orderCode) {

        SearchTradeInput input = new SearchTradeInput();
        input.setOrderId(orderCode);

        return input;
    }

    /**
     * 取引登録出力用エラーチェック
     *
     * @param output 取引登録出力パラメータ
     */
    private void checkGmoOutput(BaseOutput output) {

        List<CheckMessageDto> errList = null;

        AssertChecker.assertNotNull("output is null", output);

        if (output.isErrorOccurred()) {
            errList = communicateUtility.checkOutput(output);
        }

        if (!CollectionUtils.isEmpty(errList)) {
            // GMOからは複数メッセージが返却されるためApplicationExceptionにセット
            ApplicationException appException = new ApplicationException();
            for (CheckMessageDto err : errList) {
                appException.addMessage(
                                err.getMessageId(), err.getArgs() != null ?
                                                Arrays.stream(err.getArgs()).toArray(String[]::new) :
                                                null);
            }
            throw appException;
        }
    }

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    private boolean checkSecurityCode(String value) {

        // null or 空の場合未実施
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        // 数値かどうか
        if (!Pattern.matches(SECURITY_CODE_NUMBER_REGEX, value)) {
            return false;
        }

        // 数値だったけど、桁数が許容範囲か
        return isPermittedLength(value);
    }

    /**
     * セキュリティコードとして有り得る桁数かどうか
     *
     * @param value 入力値
     * @return true..OK / false..NG
     */
    private boolean isPermittedLength(Object value) {
        int length = value.toString().length();
        return length >= minLimit && length <= maxLimit;
    }
}