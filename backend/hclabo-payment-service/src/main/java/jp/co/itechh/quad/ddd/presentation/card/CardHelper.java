/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.card;

import jp.co.itechh.quad.card.presentation.api.param.CardInfoResponse;
import jp.co.itechh.quad.ddd.usecase.card.GetCardInfoUseCaseDto;
import org.springframework.stereotype.Component;

/**
 * クレジットカードHelperクラス
 */
@Component
public class CardHelper {

    /**
     * カード情報からカード情報レスポンスに変換
     *
     * @param dto カード情報
     * @return response カード情報レスポンス
     */
    public CardInfoResponse toCardInfoResponse(GetCardInfoUseCaseDto dto) {

        if (dto == null) {
            return null;
        }

        CardInfoResponse response = new CardInfoResponse();
        response.setRegistedCardMaskNo(dto.getRegistedCardMaskNo());
        response.setExpireMonth(dto.getExpirationMonth());
        response.setExpireYear(dto.getExpirationYear());

        return response;
    }

}