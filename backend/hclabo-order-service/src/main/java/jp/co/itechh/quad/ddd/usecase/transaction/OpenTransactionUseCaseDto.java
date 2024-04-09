/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 取引確定ユースケースDto<br/>
 */
@Data
@Component
@Scope("prototype")
public class OpenTransactionUseCaseDto {

    /**
     * 本人認証パスワード入力画面URL<br/>
     */
    private String termUrl;

    /**
     * 本人認証サービスの要求電文<br/>
     * ※マルチペイメントには頭文字を大文字で渡す必要がある。レスポンス受取用モデルでは大文字で再定義すること
     */
    private String paReq;

    /**
     * 決済代行の取引ID<br/>
     * ※マルチペイメントには頭文字を大文字で渡す必要がある。レスポンス受取用モデルでは大文字で再定義すること
     */
    private String md;

}