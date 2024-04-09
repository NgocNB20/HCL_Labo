/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.card;

import com.gmo_pg.g_pay.client.output.SearchCardOutput;
import com.gmo_pg.g_pay.client.output.SearchCardOutput.CardInfo;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.ddd.domain.card.proxy.CardProxyService;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * クレジットカード情報取得ユースケース
 */
@Service
public class GetCardInfoUseCase {

    /** クレジットカードプロキシサービス */
    private final CardProxyService cardProxyService;

    /** コンストラクタ */
    @Autowired
    public GetCardInfoUseCase(CardProxyService cardProxyService) {
        this.cardProxyService = cardProxyService;
    }

    /**
     * クレジットカード情報を取得する
     *
     * @param customerId 取引ID
     * @return 存在する ... ユースケース返却用Dto / 存在しない ... null
     */
    public GetCardInfoUseCaseDto getCardInfo(String customerId) {

        // クレジットカード情報を取得する
        SearchCardOutput searchCardOutput = this.cardProxyService.getCardInfo(customerId);

        // 登録済みカードが存在する場合
        if (searchCardOutput != null && !CollectionUtils.isEmpty(searchCardOutput.getCardList())) {
            // カード情報の照会結果から、カード情報を取得し変換
            return toGetCardInfoUseCaseDto((CardInfo) searchCardOutput.getCardList().get(0));
        }

        return null;
    }

    /**
     * クレジットカード情報をユースケース返却用Dtoへ変換
     *
     * @param cardInfo カード情報
     * @return dto ユースケース返却用Dto
     */
    private GetCardInfoUseCaseDto toGetCardInfoUseCaseDto(CardInfo cardInfo) {

        GetCardInfoUseCaseDto dto = new GetCardInfoUseCaseDto();

        // cardInfoには「年2桁+月2桁」で設定されているため、西暦4桁、月2桁に切り出し
        String expirationYear = null;
        String expirationMonth = null;
        if (cardInfo.getExpire().length() == 4) {
            expirationYear = PropertiesUtil.getSystemPropertiesValue("expiration.date.year") + cardInfo.getExpire()
                                                                                                       .substring(0, 2);
            expirationMonth = cardInfo.getExpire().substring(2);
        } else {
            throw new DomainException("PAYMENT_GCIU0001-E");
        }

        dto.setRegistedCardMaskNo(cardInfo.getCardNo());
        dto.setExpirationMonth(expirationMonth);
        dto.setExpirationYear(expirationYear);

        return dto;
    }

}