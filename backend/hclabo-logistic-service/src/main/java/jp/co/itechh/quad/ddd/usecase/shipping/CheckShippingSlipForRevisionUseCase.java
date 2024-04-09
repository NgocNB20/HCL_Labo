/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.shippingslip.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 改訂用配送伝票チェックユースケース
 */
@Service
public class CheckShippingSlipForRevisionUseCase {

    /** 改訂用配送伝票リポジトリ */
    private final IShippingSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 改訂用配送伝票ドメインサービス */
    private final ShippingSlipForRevisionEntityService shippingSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public CheckShippingSlipForRevisionUseCase(IShippingSlipForRevisionRepository shippingSlipForRevisionRepository,
                                               ShippingSlipForRevisionEntityService shippingSlipForRevisionEntityService) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.shippingSlipForRevisionEntityService = shippingSlipForRevisionEntityService;
    }

    /**
     * 改訂用配送伝票をチェックする
     *
     * @param transactionRevisionId
     * @return 警告メッセージマップ
     */
    public Map<String, List<WarningContent>> checkShippingSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用配送伝票を取得する
        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 配送伝票が存在しない場合は処理をスキップする
        if (shippingSlipForRevisionEntity == null) {
            return null;
        }

        // 改訂用配送伝票チェック
        return shippingSlipForRevisionEntityService.checkShippingSlipForRevision(shippingSlipForRevisionEntity);
    }

}