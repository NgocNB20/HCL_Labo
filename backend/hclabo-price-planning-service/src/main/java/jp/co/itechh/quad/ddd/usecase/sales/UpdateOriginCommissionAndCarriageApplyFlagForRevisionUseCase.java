/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用販売伝票の改訂前手数料/送料適用フラグ更新 ユースケース
 */
@Service
public class UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** 販売伝票エンティティ ドメインサービス */
    private final SalesSlipForRevisionEntityService salesSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository,
                                                                        SalesSlipForRevisionEntityService salesSlipForRevisionEntityService) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
        this.salesSlipForRevisionEntityService = salesSlipForRevisionEntityService;
    }

    /**
     * 改訂前手数料/送料適用フラグを更新
     *
     * @param transactionRevisionId
     * @param originCommissionApplyFlag
     * @param originCarriageApplyFlag
     */
    public void updateOriginCommissionAndCarriageApplyFlagForRevision(String transactionRevisionId,
                                                                      boolean originCommissionApplyFlag,
                                                                      boolean originCarriageApplyFlag) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            throw new DomainException("PRICE-PLANNING-SEVE0001-E", new String[] {transactionRevisionId});
        }

        // 改訂前手数料/送料適用フラグ設定
        salesSlipForRevisionEntityService.settingOriginCommissionAndCarriageApplyFlag(
                        originCommissionApplyFlag, originCarriageApplyFlag, salesSlipForRevisionEntity);

        // 販売伝票を更新する
        salesSlipForRevisionRepository.update(salesSlipForRevisionEntity);
    }
}