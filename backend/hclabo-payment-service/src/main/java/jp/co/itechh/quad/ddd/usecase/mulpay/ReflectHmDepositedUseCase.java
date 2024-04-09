/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.mulpay;

import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HM入金済みをマルペイ決済結果へ反映
 */
@Service
public class ReflectHmDepositedUseCase {

    /* マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** コンストラクタ */
    @Autowired
    public ReflectHmDepositedUseCase(MulPayProxyService mulPayProxyService) {
        this.mulPayProxyService = mulPayProxyService;
    }

    /**
     * HM入金済みをマルペイ決済結果へ反映
     *
     * @param mulPayResultSeq
     */
    public void reflectHmDeposited(Integer mulPayResultSeq) {

        // マルチペイメント決済結果.入金登録済みフラグ更新
        MulPayResultEntity mulPayResultEntity = mulPayProxyService.getEntityByMulPayResultSeq(mulPayResultSeq);
        if (mulPayResultEntity == null) {
            return;
        }

        mulPayProxyService.updateMulPayResultAfterDeposit(mulPayResultEntity);
    }
}
