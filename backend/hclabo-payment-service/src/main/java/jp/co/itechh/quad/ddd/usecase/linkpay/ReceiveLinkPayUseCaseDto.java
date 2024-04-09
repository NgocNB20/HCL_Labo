/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.linkpay;

import lombok.Data;

/**
 * リンク決済結果を受け取るユースケースDto
 */
@Data
public class ReceiveLinkPayUseCaseDto {

    /** 取引ID */
    private String transactionId;

    /** 処理なしでHMへ戻る 判定 */
    private boolean isNoProcessBack;
}
