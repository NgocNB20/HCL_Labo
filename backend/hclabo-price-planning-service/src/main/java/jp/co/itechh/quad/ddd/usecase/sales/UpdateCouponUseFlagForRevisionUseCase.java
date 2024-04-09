/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用販売伝票のクーポン利用フラグ更新 ユースケース
 */
@Service
public class UpdateCouponUseFlagForRevisionUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public UpdateCouponUseFlagForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
    }

    /**
     * 改訂用販売伝票のクーポン利用フラグ更新
     *
     * @param transactionRevisionId
     * @param useCouponFlag
     */
    public void updateCouponUseFlagOfSalesSlipForRevision(String transactionRevisionId, boolean useCouponFlag) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            throw new DomainException("PRICE-PLANNING-SEVE0001-E", new String[] {transactionRevisionId});
        }

        // クーポン利用フラグ更新
        salesSlipForRevisionEntity.settingCouponUseFlag(useCouponFlag);

        // 販売伝票を更新する
        salesSlipForRevisionRepository.update(salesSlipForRevisionEntity);
    }
}