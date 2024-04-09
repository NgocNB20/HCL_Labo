/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;

/**
 * 取引確定可能確認ユースケースDto
 */
@Data
public class ConfirmOpenOfTransactionUseCaseDto {

    /**
     * 3DSサーバーへのリダイレクトURL<br/>
     */
    private String redirectUrl;

    /** HTTP ステータス コード */
    private int statusCode;

}
