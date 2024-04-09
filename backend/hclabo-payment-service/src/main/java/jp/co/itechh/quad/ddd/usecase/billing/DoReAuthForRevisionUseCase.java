/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用請求伝票再オーソリ実行ユースケース
 */
@Service
public class DoReAuthForRevisionUseCase {

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 改訂用注文決済サービス */
    private final OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService;

    /** 改訂用請求伝票確定ユースケース */
    private final OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** コンストラクタ */
    @Autowired
    public DoReAuthForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                      OrderPaymentForRevisionEntityService orderPaymentForRevisionEntityService,
                                      OpenBillingSlipReviseUseCase openBillingSlipReviseUseCase,
                                      HeaderParamsUtility headerParamsUtil) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.orderPaymentForRevisionEntityService = orderPaymentForRevisionEntityService;
        this.openBillingSlipReviseUseCase = openBillingSlipReviseUseCase;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * 再オーソリを実行する
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void doReAuth(String transactionRevisionId, Boolean revisionOpenFlag) {

        // 取引IDに紐づく改訂用請求伝票を取得する
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        this.billingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        // 改訂用請求伝票が取得できない場合、不正な呼び出しとみなしエラーリストをセット
        if (ObjectUtils.isEmpty(billingSlipForRevisionEntity)) {
            // 改訂用請求伝票取得失敗
            throw new DomainException("PAYMENT_EPAR0001-E", new String[] {transactionRevisionId});
        }

        // 再オーソリ実行
        this.orderPaymentForRevisionEntityService.doReAuth(billingSlipForRevisionEntity);

        // 改訂用請求伝票を更新する
        this.billingSlipForRevisionRepository.update(billingSlipForRevisionEntity);

        if (Boolean.TRUE.equals(revisionOpenFlag)) {
            openBillingSlipReviseUseCase.openBillingSlipRevise(
                            transactionRevisionId, Boolean.TRUE, getAdministratorId());
        }
    }

    /**
     * 管理者SEQを取得する
     *
     * @return AdminSeq 管理者ID
     */
    private String getAdministratorId() {
        return this.headerParamsUtil.getAdministratorSeq();
    }

}