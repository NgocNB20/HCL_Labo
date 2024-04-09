/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import jp.co.itechh.quad.billingslip.presentation.api.param.WarningContent;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PostClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPaymentService;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 改訂用注文決済サービス
 */
@Service
public class OrderPaymentForRevisionEntityService {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPaymentForRevisionEntityService.class);

    /** クレジットカード決済 値オブジェクト ドメインサービス */
    private final CreditPaymentService creditPaymentService;

    /**
     * 処理件数マップ　ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    public static final String WARNING_MESSAGE = "WarningMessage";

    /** コンストラクタ */
    @Autowired
    public OrderPaymentForRevisionEntityService(CreditPaymentService creditPaymentService) {
        this.creditPaymentService = creditPaymentService;
    }

    /**
     * 注文決済を取消する<br/>
     * ※クレジット決済の場合のみGMO決済依頼取消実行を行う<br/>
     * ※GMO連携が解除されている場合は処理をスキップ
     *
     * @param orderPaymentForRevisionEntity          注文決済
     * @param warningMessage                         警告メッセージ
     * @return GMOキャンセル実行
     */
    public boolean cancelOrderPayment(OrderPaymentForRevisionEntity orderPaymentForRevisionEntity,
                                      Map<String, List<WarningContent>> warningMessage) {

        // チェック
        // 確定状態でない場合はエラー
        if (orderPaymentForRevisionEntity.getOrderPaymentStatus() != OrderPaymentStatus.OPEN) {
            throw new DomainException("PAYMENT_ORDP0001-E");
        }

        // クレジット決済の場合
        if (orderPaymentForRevisionEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT
            && orderPaymentForRevisionEntity.getPaymentRequest() instanceof CreditPayment) {

            CreditPayment creditPayment = (CreditPayment) orderPaymentForRevisionEntity.getPaymentRequest();
            // GMO連携解除フラグがtrueの場合は処理をスキップ
            if (creditPayment.isGmoReleaseFlag()) {
                return false;
            }

            try {
                // GMO決済依頼取消実行
                creditPaymentService.cancelGmoPaymentRequest(orderPaymentForRevisionEntity.getOrderCode(),
                                                             orderPaymentForRevisionEntity.getOrderPaymentRevisionId()
                                                            );
            } catch (ApplicationException ae) {
                // nullチェック
                if (!ae.hasMessage()) {
                    LOGGER.error("受注キャンセルにてGMO通信失敗：" + "オーターID：" + orderPaymentForRevisionEntity.getOrderCode()
                                 + "エラー内容：予期せぬエラーが発生しました。", ae);
                } else {
                    // ApplicationExceptionのログ出力を行う
                    ae.getMessageMap().forEach((fieldName, exceptionContentList) -> {
                        for (ExceptionContent exceptionContent : exceptionContentList) {
                            // ログに出力
                            LOGGER.error("受注キャンセルにてGMO通信失敗：" + "オーターID：" + orderPaymentForRevisionEntity.getOrderCode()
                                         + "エラー内容：" + exceptionContent.getMessage());
                        }
                    });

                    this.settingWarningMessage(warningMessage);
                }

            }

            // 注文決済を取消する
            orderPaymentForRevisionEntity.setOrderPaymentStatus(OrderPaymentStatus.CANCEL);

            return true;
        } else {
            // 注文決済を取消する
            orderPaymentForRevisionEntity.setOrderPaymentStatus(OrderPaymentStatus.CANCEL);

            return false;
        }
    }

    /**
     * 注文決済を売上計上する<br/>
     * ※クレジット決済の場合のみ、GMO売上計上依頼実行を呼び出す
     *
     * @param orderPaymentForRevisionEntity 改訂用注文決済
     */
    public boolean recordSales(OrderPaymentForRevisionEntity orderPaymentForRevisionEntity) {

        boolean isCreditSuccess = false;

        AssertChecker.assertNotNull("orderPaymentForRevisionEntity is null", orderPaymentForRevisionEntity);

        // クレジット決済の場合
        if (orderPaymentForRevisionEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT
            && orderPaymentForRevisionEntity.getPaymentRequest() instanceof CreditPayment) {

            // GMO売上計上依頼実行
            AlterTranOutput alterTranOutput =
                            creditPaymentService.recordSales(orderPaymentForRevisionEntity.getOrderCode(),
                                                             orderPaymentForRevisionEntity.getOrderPaymentRevisionId()
                                                            );

            isCreditSuccess = true;
        }
        return isCreditSuccess;
    }

    /**
     * 金額変更依頼<br/>
     * ※GMO連携が解除されている場合は処理をスキップ
     *
     * @param orderPaymentForRevisionEntity 改訂用注文決済
     * @param modifiedBillingPrice          改訂後請求金額
     * @param originalBilledPrice           改訂元請求金額
     * @return GMO実行
     */
    public boolean changeOrderPayment(OrderPaymentForRevisionEntity orderPaymentForRevisionEntity,
                                      int modifiedBillingPrice,
                                      int originalBilledPrice) {

        // クレジット決済の場合
        if (orderPaymentForRevisionEntity.getSettlementMethodType() == HTypeSettlementMethodType.CREDIT
            && orderPaymentForRevisionEntity.getPaymentRequest() instanceof CreditPayment) {
            // 金額変更チェック
            if (modifiedBillingPrice != originalBilledPrice) {

                CreditPayment creditPayment = (CreditPayment) orderPaymentForRevisionEntity.getPaymentRequest();

                // GMO連携解除フラグがtrueの場合は処理をスキップ
                if (creditPayment.isGmoReleaseFlag()) {
                    return false;
                }

                // オーソリ期限が設定されている場合
                if (ObjectUtils.isNotEmpty(creditPayment.getAuthLimitDate())) {
                    // オーソリ期限を再設定
                    orderPaymentForRevisionEntity.resetAuthLimitDate();
                }

                // 金額変更通信実行
                creditPaymentService.changePrice(orderPaymentForRevisionEntity.getOrderCode(), modifiedBillingPrice,
                                                 orderPaymentForRevisionEntity.getOrderPaymentRevisionId()
                                                );
                return true;
            }
        }

        return false;
    }

    /**
     * 再オーソリ依頼<br>
     * 金額変更依頼(CreditChangeTranAdapter#doChangeTran)を呼び出す。金額などの設定値は前回情報をそのまま引き渡す<br/>
     * ※GMO連携が解除されている場合は処理をスキップ
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     */
    public void doReAuth(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        // チェック
        // 決済方法がクレジット以外の場合はエラー
        if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getSettlementMethodType()
            != HTypeSettlementMethodType.CREDIT) {
            throw new DomainException("PAYMENT_ODPS0003-E");
        }
        // 後払いかつ、オーソリ済み以外の場合はエラー
        if (billingSlipForRevisionEntity.getBillingType() != HTypeBillType.POST_CLAIM
            && billingSlipForRevisionEntity.getBillingStatus() != PostClaimBillingStatus.AUTHORIZED) {
            throw new DomainException("PAYMENT_BLSE0005-E");
        }

        // 変更前のクレジットカード決済情報を取得
        CreditPayment originalCreditPayment =
                        (CreditPayment) billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                    .getPaymentRequest();

        // GMO連携解除フラグがtrueの場合は処理をスキップ
        if (originalCreditPayment.isGmoReleaseFlag()) {
            return;
        }

        // オーソリ期限を再設定
        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().resetAuthLimitDate();

        // 金額変更通信実行
        creditPaymentService.changePrice(billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getOrderCode(),
                                         billingSlipForRevisionEntity.getBilledPrice(),
                                         billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                     .getOrderPaymentRevisionId()
                                        );
    }

    /**
     * 決済代行連携解除設定
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     * @param paymentAgencyReleaseFlag     決済代行連携解除フラグ
     */
    public void settingPaymentAgencyRelease(BillingSlipForRevisionEntity billingSlipForRevisionEntity,
                                            boolean paymentAgencyReleaseFlag) {

        // チェック
        // ステータスが一時停止以外の場合はエラー
        if (!billingSlipForRevisionEntity.isSuspended()) {
            throw new DomainException("PAYMENT_ODPS0004-E",
                                      new String[] {billingSlipForRevisionEntity.getTransactionRevisionId()}
            );
        }
        // クレジット決済以外の場合はエラー
        if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getSettlementMethodType()
            != HTypeSettlementMethodType.CREDIT || !(billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                                 .getPaymentRequest() instanceof CreditPayment)) {
            throw new DomainException("PAYMENT_ODPS0003-E");
        }

        // 連携解除フラグを設定
        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                    .settingPaymentAgencyRelease(paymentAgencyReleaseFlag);
    }

    /**
     * オーソリ期限日削除
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     */
    public void removeAuthLimitDate(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        // チェック
        // ステータスが売上確定以外の場合はエラー
        if (!billingSlipForRevisionEntity.isOpenSales()) {
            throw new DomainException("PAYMENT_ODPS0005-E",
                                      new String[] {billingSlipForRevisionEntity.getTransactionRevisionId()}
            );
        }
        // クレジット決済以外の場合はエラー
        if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().getSettlementMethodType()
            != HTypeSettlementMethodType.CREDIT || !(billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                                 .getPaymentRequest() instanceof CreditPayment)) {
            throw new DomainException("PAYMENT_ODPS0003-E");
        }

        // オーソリ期限日を削除
        billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity().removeAuthLimitDate();
    }

    /**
     * 警告メッセージの設定
     *
     * @param messageMapForResponse 警告メッセージ
     */
    public void settingWarningMessage(Map<String, List<WarningContent>> messageMapForResponse) {

        WarningContent warningContent = new WarningContent();
        warningContent.setCode("LOX000318");
        warningContent.setMessage(AppLevelFacesMessageUtil.getAllMessage("LOX000318", null).getMessage());

        messageMapForResponse.put(WARNING_MESSAGE, Arrays.asList(warningContent));

    }

}