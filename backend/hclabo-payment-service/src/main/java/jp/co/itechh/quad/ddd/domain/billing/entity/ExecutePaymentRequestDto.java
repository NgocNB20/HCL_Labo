/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import lombok.Data;

/**
 * 決済情報 Dto </br>
 * 3Dセキュア本人認証URL、リンク決済URL
 */
@Data
public class ExecutePaymentRequestDto {

    /** 3DSサーバーへのリダイレクトURL */
    private String secureRedirectUrl;

    /** リンク決済ダイレクトURL */
    private String linkPayRedirectUrl;
}
