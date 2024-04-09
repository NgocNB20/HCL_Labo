/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.ddd.usecase.billing.service;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.offline.creditline.dao.CreditLineReportBatchDao;
import jp.co.itechh.quad.core.batch.offline.creditline.dto.OrderCreditLineReportBatchResultDto;
import jp.co.itechh.quad.core.batch.offline.creditline.entity.CreditLineReport;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.card.consumer.CreditLineReleaseTargetDto;
import jp.co.itechh.quad.ddd.usecase.card.processor.CreditLineReleaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 与信枠解放サービス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
@Scope("prototype")
public class CreditLineReleaseExecuter {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CreditLineReleaseExecuter.class);

    /** 通信処理中エラー発生時 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000061";

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 与信枠解放Dao ※フェーズ1のものをそのまま流用 */
    private final CreditLineReportBatchDao creditLineReportDao;

    /** 与信枠解放 Consumer */
    private final CreditLineReleaseProcessor creditLineReleaseProcessor;

    /**
     * コンストラクタ
     *
     * @param mulPayProxyService         マルチペイメントプロキシサービス
     * @param dateUtility                日付関連Utility
     * @param creditLineReportDao        与信枠解放Dao ※フェーズ1のものをそのまま流用
     * @param creditLineReleaseProcessor 与信枠解放 Consumer
     */
    @Autowired
    public CreditLineReleaseExecuter(MulPayProxyService mulPayProxyService,
                                     DateUtility dateUtility,
                                     CreditLineReportBatchDao creditLineReportDao,
                                     CreditLineReleaseProcessor creditLineReleaseProcessor) {
        this.mulPayProxyService = mulPayProxyService;
        this.dateUtility = dateUtility;
        this.creditLineReportDao = creditLineReportDao;
        this.creditLineReleaseProcessor = creditLineReleaseProcessor;
    }

    /**
     * クレジット決済の与信枠を解放
     *
     * @param orderCode 受注番号
     * @return 実行対象か否か
     */
    public boolean creditLineRelease(String orderCode) {

        // 与信枠開放対象データ取得
        CreditLineReleaseTargetDto creditLineReleaseTarget = mulPayProxyService.getCreditLineReleaseTarget(orderCode);
        if (creditLineReleaseTarget != null) {

            OrderCreditLineReportBatchResultDto batchResultDto = new OrderCreditLineReportBatchResultDto();
            batchResultDto.setOrderId(creditLineReleaseTarget.getOrderId());
            // GMO取引状態参照、取引取消を実行する
            creditLineReleaseProcessor.execute(creditLineReleaseTarget, batchResultDto);
            if (!CollectionUtil.isEmpty(batchResultDto.getErrorMessages())) {
                throw new DomainException("PAYMENT_EPAR0007-E", new String[] {orderCode});
            }

            // 与信枠解放済みを登録
            CreditLineReport creditLineReport = new CreditLineReport();
            creditLineReport.setOrderId(creditLineReleaseTarget.getOrderId());
            creditLineReport.setRegistTime(dateUtility.getCurrentTime());
            creditLineReport.setUpdateTime(creditLineReport.getRegistTime());
            creditLineReportDao.insert(creditLineReport);

            return true;
        }
        return false;
    }
}