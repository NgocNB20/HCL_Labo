/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.card;

import jp.co.itechh.quad.ddd.domain.card.proxy.CardProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * クレジットカード情報削除ユースケース
 */
@Service
public class DeleteCardInfoUseCase {

    /** クレジットカードプロキシサービス */
    private final CardProxyService cardProxyService;

    /** コンストラクタ */
    @Autowired
    public DeleteCardInfoUseCase(CardProxyService cardProxyService) {
        this.cardProxyService = cardProxyService;
    }

    /**
     * クレジットカード情報を削除する
     *
     * @param customerId 顧客ID
     */
    public void deleteCardInfo(String customerId) {

        // クレジットカード情報を削除する
        this.cardProxyService.deleteCardInfo(customerId);
    }

}
