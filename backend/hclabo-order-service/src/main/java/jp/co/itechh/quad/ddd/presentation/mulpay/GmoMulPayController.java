/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.mulpay;

import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.usecase.transaction.PaymentResultReceiveUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * MulPayエンドポイント Controller
 */
@RestController
public class GmoMulPayController extends AbstractController {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GmoMulPayController.class);

    /** 決済代行から入金結果受取 */
    private final PaymentResultReceiveUseCase paymentResultReceiveUseCase;

    /** コンストラクタ */
    @Autowired
    public GmoMulPayController(PaymentResultReceiveUseCase paymentResultReceiveUseCase) {
        this.paymentResultReceiveUseCase = paymentResultReceiveUseCase;
    }

    /**
     * POST /mulPayNotification : 決済代行入金結果受取
     *
     * @param request HTTPリクエストには
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @PostMapping(value = "/mulPayNotification", produces = "text/plain")
    @ResponseStatus(HttpStatus.OK)
    public String paymentResultReceive(HttpServletRequest request) {

        boolean requiredReflectionProcessingFlag = paymentResultReceiveUseCase.receivePaymentResult(request);
        // 処理実行後非同期処理
        paymentResultReceiveUseCase.asyncAfterProcess(requiredReflectionProcessingFlag, request);

        return "0";
    }
}
