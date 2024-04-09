/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import com.gmo_pg.g_pay.client.output.ExecTranOutput;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.multipayment.MulPayShopDao;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.ExecutePaymentRequestDto;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreateGmoPaymentUrlDto;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPaymentService;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPaymentService;
import jp.co.itechh.quad.ddd.domain.card.proxy.CardProxyService;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 決済代行請求委託（注文決済確定）ユースケース
 */
@Service
public class EntrustPaymentAgencyUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 注文決済ドメインサービス */
    private final CreditPaymentService creditPaymentService;

    /** LinkPay決済バリューエントリードメインサービス */
    private final LinkPaymentService linkPaymentService;

    /** カードプロキシサービス */
    private final CardProxyService cardProxyService;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulpayProxyService;

    /** 販売企画 アダプター */
    private final ISalesAdapter salesAdapter;

    /** マルチペイメントショップDao */
    private final MulPayShopDao mulPayShopDao;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** コンストラクタ */
    @Autowired
    public EntrustPaymentAgencyUseCase(IBillingSlipRepository billingSlipRepository,
                                       CreditPaymentService creditPaymentService,
                                       LinkPaymentService linkPaymentService,
                                       CardProxyService cardProxyService,
                                       MulPayProxyService mulpayProxyService,
                                       ISalesAdapter salesAdapter,
                                       MulPayShopDao mulPayShopDao,
                                       MulPayProxyService mulPayProxyService) {
        this.billingSlipRepository = billingSlipRepository;
        this.creditPaymentService = creditPaymentService;
        this.linkPaymentService = linkPaymentService;
        this.cardProxyService = cardProxyService;
        this.mulpayProxyService = mulpayProxyService;
        this.salesAdapter = salesAdapter;
        this.mulPayShopDao = mulPayShopDao;
        this.mulPayProxyService = mulPayProxyService;
    }

    /**
     * 決済代行に請求委託する（注文決済確定）
     *
     * @param customerId               顧客ID
     * @param transactionId            取引ID
     * @param callbackType             コールバック方法
     * @param creditTdResultReceiveUrl 加盟店戻りURL
     * @param securityCode             セキュリティコード
     * @param returnUrl                リンク決済戻り先URL
     * @return ExecutePaymentRequestDto
     */
    public ExecutePaymentRequestDto entrustPaymentAgency(String customerId,
                                                         String transactionId,
                                                         String callbackType,
                                                         String creditTdResultReceiveUrl,
                                                         String securityCode,
                                                         String returnUrl) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が存在しない場合は処理をスキップする
        if (billingSlipEntity == null) {
            return null;
        }

        // 全額クーポン決済の場合は処理をスキップする
        if (HTypeSettlementMethodType.DISCOUNT.equals(
                        billingSlipEntity.getOrderPaymentEntity().getSettlementMethodType())) {
            // 注文決済を確定状態にする
            billingSlipEntity.getOrderPaymentEntity().openStatus();
            // 請求伝票を更新する
            billingSlipRepository.update(billingSlipEntity);
            // 処理終了
            return null;
        }

        // 販売伝票を取得する
        SalesSlip salesSlip = salesAdapter.getSalesSlip(transactionId);
        if (salesSlip == null) {
            throw new DomainException("PAYMENT_EPAU0001-E", new String[] {transactionId});
        }

        // クレジットカード決済の実行
        if (billingSlipEntity.getOrderPaymentEntity().getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {

            return executeForCredit(salesSlip, customerId, billingSlipEntity, callbackType, creditTdResultReceiveUrl,
                                    securityCode
                                   );

            // リンク決済実行
        } else if (billingSlipEntity.getOrderPaymentEntity().getSettlementMethodType()
                   == HTypeSettlementMethodType.LINK_PAYMENT) {

            return executeForLinkPayment(billingSlipEntity, salesSlip, returnUrl);

        } else {
            // その他決済方法はエラー
            throw new DomainException("PAYMENT_ODPS0001-E");
        }

    }

    /**
     * クレジットカード決済の実行
     * ※3Dセキュア認証が必要な場合、注文決済は途中決済になり、認証後に再度本ユースケースを呼び出す必要がある
     *
     * @param salesSlip                販売伝票
     * @param customerId               顧客ID
     * @param billingSlipEntity        請求伝票
     * @param callbackType             コールバック方法
     * @param creditTdResultReceiveUrl 加盟店戻りURL
     * @param securityCode             セキュリティコード
     * @return ExecutePaymentRequestDto ※3Dセキュア認証が必要な場合のみ返却
     */
    protected ExecutePaymentRequestDto executeForCredit(SalesSlip salesSlip,
                                                        String customerId,
                                                        BillingSlipEntity billingSlipEntity,
                                                        String callbackType,
                                                        String creditTdResultReceiveUrl,
                                                        String securityCode) {

        ExecutePaymentRequestDto executePaymentRequestSecureDto = null;

        // 本人認証後2回目呼出しかで分岐
        if (!checkSecondTimeForSecure(billingSlipEntity.getOrderPaymentEntity().getOrderPaymentId().getValue())) {
            // 1回目呼出しの場合

            // 注文決済エンティティを取得
            OrderPaymentEntity orderPaymentEntity = billingSlipEntity.getOrderPaymentEntity();

            // GMO決済依頼実行
            ExecTranOutput execTranOutput =
                            creditPaymentService.executeGmoPaymentRequest(orderPaymentEntity.getOrderCode(),
                                                                          orderPaymentEntity.getOrderPaymentId(),
                                                                          customerId,
                                                                          orderPaymentEntity.getPaymentMethodId(),
                                                                          salesSlip.getBillingAmount(), securityCode,
                                                                          callbackType, creditTdResultReceiveUrl,
                                                                          (CreditPayment) orderPaymentEntity.getPaymentRequest()
                                                                         );

            if (StringUtils.equals(execTranOutput.getAcs(), "2")) {
                executePaymentRequestSecureDto = new ExecutePaymentRequestDto();
                // 3Dセキュア本人認証が必要な場合
                executePaymentRequestSecureDto.setSecureRedirectUrl(execTranOutput.getRedirectUrl());
            }

            // 「3Dセキュア認証パラメータ」がnullの場合、3Dセキュア認証が不要なので、決済依頼完了
            if (executePaymentRequestSecureDto == null) {

                // 注文決済を確定状態にする
                billingSlipEntity.getOrderPaymentEntity().openStatus();
                // 請求伝票を更新する
                billingSlipRepository.update(billingSlipEntity);

                // 前請求の場合、マルチペイメント決済結果 テーブルへ入金登録
                if (HTypeBillType.PRE_CLAIM == billingSlipEntity.getBillingType()) {
                    // MulPayBillエンティティの取得
                    MulPayBillEntity mulPayBillEntity = mulpayProxyService.getLatestEntityByOrderPaymentId(
                                    billingSlipEntity.getOrderPaymentEntity().getOrderPaymentId().getValue());
                    MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(1001);
                    mulPayProxyService.registMulpayResult(
                                    createMulPayResult4CreditRequest(mulPayBillEntity, mulPayShopEntity.getShopId(),
                                                                     mulPayShopEntity.getShopPass(),
                                                                     billingSlipEntity.getTransactionId()
                                                                    ), "DIRECT");
                }

                /* 決済完了後の追加処理（カード登録処理） */
                CreditPayment creditPayment =
                                (CreditPayment) billingSlipEntity.getOrderPaymentEntity().getPaymentRequest();
                if (checkRegistCardFlag(billingSlipEntity, creditPayment)) {

                    // カード情報を登録/更新する
                    cardProxyService.saveTradedCardInfo(customerId, creditPayment.getExpirationDate(),
                                                        creditPayment.getPaymentToken(),
                                                        creditPayment.isRegistCardFlag(),
                                                        creditPayment.isUseRegistedCardFlag(),
                                                        billingSlipEntity.getOrderPaymentEntity().getOrderCode()
                                                       );
                }

                return null;

            } else {
                // 3Dセキュア認証が必要な場合

                // 注文決済のステータスを途中決済にする
                billingSlipEntity.getOrderPaymentEntity().underSettlementStatus();
                // 請求伝票を更新する
                billingSlipRepository.update(billingSlipEntity);

                return executePaymentRequestSecureDto;
            }

        } else {
            // 本人認証後2回目呼出しの場合

            CreditPayment creditPayment = (CreditPayment) billingSlipEntity.getOrderPaymentEntity().getPaymentRequest();

            /* 決済完了後の追加処理（カード登録処理） */
            if (checkRegistCardFlag(billingSlipEntity, creditPayment)) {
                // カード情報を登録/更新する
                cardProxyService.saveTradedCardInfo(customerId, creditPayment.getExpirationDate(),
                                                    creditPayment.getPaymentToken(), creditPayment.isRegistCardFlag(),
                                                    creditPayment.isUseRegistedCardFlag(),
                                                    billingSlipEntity.getOrderPaymentEntity().getOrderCode()
                                                   );
            }

            // 注文決済を確定状態にする
            billingSlipEntity.getOrderPaymentEntity().openStatusAfterSecure();

            // 請求伝票を更新する
            billingSlipRepository.update(billingSlipEntity);

            // 前請求の場合、マルチペイメント決済結果 テーブルへ入金登録
            if (HTypeBillType.PRE_CLAIM == billingSlipEntity.getBillingType()) {
                // MulPayBillエンティティの取得
                MulPayBillEntity mulPayBillEntity = mulpayProxyService.getLatestEntityByOrderPaymentId(
                                billingSlipEntity.getOrderPaymentEntity().getOrderPaymentId().getValue());
                MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(1001);
                mulPayProxyService.registMulpayResult(
                                createMulPayResult4CreditRequest(mulPayBillEntity, mulPayShopEntity.getShopId(),
                                                                 mulPayShopEntity.getShopPass(),
                                                                 billingSlipEntity.getTransactionId()
                                                                ), "DIRECT");
            }

            return null;
        }

    }

    /**
     * リンク決済実行
     *
     * @param billingSlipEntity
     * @param salesSlip
     * @param returnUrl
     * @return ExecutePaymentRequestDto
     */
    protected ExecutePaymentRequestDto executeForLinkPayment(BillingSlipEntity billingSlipEntity,
                                                             SalesSlip salesSlip,
                                                             String returnUrl) {

        ExecutePaymentRequestDto executePaymentRequestSecureDto = null;

        // リンク決済2回目呼出しと判断する
        if (!checkSecondTimeForLinkPay(billingSlipEntity.getOrderPaymentEntity())) {

            // 1回目呼出しの場合
            CreateGmoPaymentUrlDto linkPaymentUrlResponse = linkPaymentService.createGmoPaymentUrl(
                            billingSlipEntity.getOrderPaymentEntity().getOrderCode(),
                            billingSlipEntity.getBillingAddressId().getValue(), salesSlip.getBillingAmount(), returnUrl
                                                                                                  );

            // リンク決済URL発行できない場合はエラー
            if (linkPaymentUrlResponse.getLinkUrl() == null) {
                throw new DomainException("PAYMENT_LINK0001-E");
            } else {
                executePaymentRequestSecureDto = new ExecutePaymentRequestDto();
                executePaymentRequestSecureDto.setLinkPayRedirectUrl(linkPaymentUrlResponse.getLinkUrl());

                // 注文決済のステータスを途中決済にする
                billingSlipEntity.getOrderPaymentEntity().underSettlementStatusLinkPay();
                // 請求伝票を更新する
                billingSlipRepository.update(billingSlipEntity);

                return executePaymentRequestSecureDto;
            }

        } else {
            // 2回目呼出しの場合

            // 注文決済を確定状態にする
            billingSlipEntity.getOrderPaymentEntity().openStatus();

            // 請求伝票を更新する
            billingSlipRepository.update(billingSlipEntity);

            return null;
        }

    }

    /**
     * 本ユースケースは、3Dセキュアの本人認証後に2回目が呼ばれるため、本人認証後2回目呼出しかの判定を行う
     *
     * @param orderPaymentId 注文決済ID
     * @return True：「トランザクションタイプ」が”SecureTran2"であること且つ「エラーコード」に値が設定されていないこと
     * False：上記以外はfalseを返却する
     */
    private boolean checkSecondTimeForSecure(String orderPaymentId) {

        // MulPayBillエンティティの取得
        MulPayBillEntity mulPayBillEntity = mulpayProxyService.getLatestEntityByOrderPaymentId(orderPaymentId);

        // 本人認証後2回目呼出しフラグ
        if (mulPayBillEntity != null) {
            if ("SecureTran2".equals(mulPayBillEntity.getTranType()) && StringUtils.isBlank(
                            mulPayBillEntity.getErrCode())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 本ユースケースは、以下のマルチペイメント請求データがあるとき、リンク決済2回回目呼出しと判断する
     *
     * @param orderPaymentEntity 注文決済
     * @return True：マルチペイメント請求.決済方法=リンク決済(1) , マルチペイメント請求.エラー情報　= 空
     * 注文決済.決済タイプ=後日払い & マルチペイメント請求.リンクタイプPlus処理結果=REQSUCCESS
     * OR 注文決済.決済タイプ=即時払い & マルチペイメント請求.リンクタイプPlus処理結果=PAYSUCCESS
     * False：上記以外はfalseを返却する
     */
    private boolean checkSecondTimeForLinkPay(OrderPaymentEntity orderPaymentEntity) {

        // MulPayBillエンティティの取得
        MulPayBillEntity mulPayBillEntity = mulpayProxyService.getLatestEntityByOrderPaymentId(
                        orderPaymentEntity.getOrderPaymentId().getValue());
        if (mulPayBillEntity == null) {
            return false;
        }

        // リンク決済 2回目呼出し判定
        if (mulPayBillEntity != null) {
            HTypePaymentLinkType hTypePaymentLinkType =
                            ((LinkPayment) orderPaymentEntity.getPaymentRequest()).getLinkPaymentType();
            if ((hTypePaymentLinkType == HTypePaymentLinkType.IMMEDIATEPAYMENT && "PAYSUCCESS".equals(
                            mulPayBillEntity.getResult()))
                || (hTypePaymentLinkType == HTypePaymentLinkType.LATERDATEPAYMENT) && "REQSUCCESS".equals(
                            mulPayBillEntity.getResult())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 決済完了後の追加処理（カード登録処理）
     *
     * @param billingSlipEntity 請求伝票エンティティ
     * @param creditPayment     クレジットカード決済 値オブジェクト
     * @return True：請求種別が「クレジット」とクレジット決済．カード保存フラグがON
     * False：上記以外はfalseを返却する
     */
    private boolean checkRegistCardFlag(BillingSlipEntity billingSlipEntity, CreditPayment creditPayment) {

        // 注文決済の決済依頼した決済方法がクレジットカードの場合
        if (billingSlipEntity.getOrderPaymentEntity().getSettlementMethodType() == HTypeSettlementMethodType.CREDIT) {

            // カード保存フラグがONの場合
            if (creditPayment.isRegistCardFlag()) {
                return true;
            }
        }

        return false;
    }

    /**
     * クレジット前請求用
     * マルチペイメント決済結果通知受付リクエスト モデルを生成
     *
     * @param *             @param jsonObj
     * @param shopId
     * @param shopPass
     * @param transactionId
     * @return マルチペイメント決済結果通知受付リクエストモデル
     */
    private MulPayResultRequest createMulPayResult4CreditRequest(MulPayBillEntity mulPayBillEntity,
                                                                 String shopId,
                                                                 String shopPass,
                                                                 String transactionId) {

        MulPayResultRequest mulPayResultRequest = new MulPayResultRequest();

        // オーダーID
        mulPayResultRequest.setOrderId(mulPayBillEntity.getOrderId());
        // 現状態
        mulPayResultRequest.setStatus("CAPTURE");
        // 利用金額
        mulPayResultRequest.setAmount(mulPayBillEntity.getAmount());
        // 処理日付
        mulPayResultRequest.setTranDate(mulPayBillEntity.getTranDate());
        // エラーコード
        mulPayResultRequest.setErrCode(mulPayBillEntity.getErrCode());
        // エラー詳細コード
        mulPayResultRequest.setErrInfo(mulPayBillEntity.getErrInfo());
        // 決済方法
        mulPayResultRequest.setPayType(mulPayBillEntity.getPayType());

        return mulPayResultRequest;
    }
}