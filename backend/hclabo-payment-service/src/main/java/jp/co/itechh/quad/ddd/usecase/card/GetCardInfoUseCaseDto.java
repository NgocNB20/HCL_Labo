/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.card;

import lombok.Data;

/**
 * クレジットカード情報取得ユースケースDto
 */
@Data
public class GetCardInfoUseCaseDto {

    /** カード番号のマスク値 */
    private String registedCardMaskNo;

    /** 有効期限(月) */
    private String expirationMonth;

    /** 有効期限(年) */
    private String expirationYear;

}
