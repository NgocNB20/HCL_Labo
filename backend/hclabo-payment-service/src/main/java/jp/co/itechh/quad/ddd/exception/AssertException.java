/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * AssertException<br/>
 * アサートエラー発生時にスローされる例外<br/>
 *
 * @author yt23807
 */
public class AssertException extends BaseException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** アサートエラーメッセージコード */
    private static final String ASSERT_ERROR_MESSAGE_CODE = "PAYMENT_ASSERT_001";

    /**
     * コンストラクタ
     * @param message アサートメッセージ文言
     */
    public AssertException(String message) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, ASSERT_ERROR_MESSAGE_CODE, new String[] {message});
    }
}