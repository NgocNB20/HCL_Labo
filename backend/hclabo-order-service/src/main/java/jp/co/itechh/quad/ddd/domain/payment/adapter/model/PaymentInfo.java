/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 決済用情報<br/>
 * 3Dセキュア認証用情報またはリンク決済URL情報</br>
 * ※【ステータスコード】202:3Dセキュア認証用情報、201:リンク決済情報
 */
@Data
@Component
@Scope("prototype")
public class PaymentInfo {

    /** 3DSサーバーへのリダイレクトURL */
    private String redirectUrl;

    /** HTTP ステータス コード */
    private int statusCode;

}
