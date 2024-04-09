/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.linkpay;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.LinkPaymentService;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.ReceiveLinkPaymentResultDto;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * リンク決済結果を受取るユースケース
 */
@Service
public class ReceiveLinkPaymentResultUseCase {

    /** LinkPay決済バリューエントリードメインサービス */
    private final LinkPaymentService linkPaymentService;

    /** 注文決済ドメインサービス */
    private final OrderPaymentEntityService orderPaymentEntityService;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public ReceiveLinkPaymentResultUseCase(LinkPaymentService linkPaymentService,
                                           OrderPaymentEntityService orderPaymentEntityService,
                                           IBillingSlipRepository billingSlipRepository) {
        this.linkPaymentService = linkPaymentService;
        this.orderPaymentEntityService = orderPaymentEntityService;
        this.billingSlipRepository = billingSlipRepository;
    }

    /**
     * リンク決済結果を受け取る
     *
     * @param linkPayJsonText リンク決済jsonテキスト
     * @return 取引ID
     */
    public ReceiveLinkPayUseCaseDto receiveLinkPayResult(String linkPayJsonText) {

        // 戻り値用インスタンス
        ReceiveLinkPayUseCaseDto receiveLinkPayUseCaseDto = new ReceiveLinkPayUseCaseDto();

        // リンク決済結果を受け取る
        ReceiveLinkPaymentResultDto receiveLinkPaymentResultDto =
                        linkPaymentService.receiveLinkPaymentResult(linkPayJsonText);
        // リンク決済結果より取得した取引IDを設定
        receiveLinkPayUseCaseDto.setTransactionId(receiveLinkPaymentResultDto.getTransactionId());
        if (receiveLinkPaymentResultDto.isNoProcessBack()) {
            receiveLinkPayUseCaseDto.setNoProcessBack(true);
            return receiveLinkPayUseCaseDto;
        }

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity =
                        billingSlipRepository.getByTransactionId(receiveLinkPaymentResultDto.getTransactionId());
        // 請求伝票が取得できない場合、不正な呼び出しとみなしエラーリストをセット
        if (billingSlipEntity == null) {
            // 請求伝票取得失敗
            throw new DomainException(
                            "PAYMENT_EPAR0002-E", new String[] {receiveLinkPaymentResultDto.getTransactionId()});
        }

        // 注文決済へリンク決済結果を設定する
        orderPaymentEntityService.setLinkPayResToOrderPayment(
                        receiveLinkPaymentResultDto.getMulPayBillEntity(), billingSlipEntity);

        // 請求伝票を更新する
        billingSlipRepository.update(billingSlipEntity);

        return receiveLinkPayUseCaseDto;
    }
}