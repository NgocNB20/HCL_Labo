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
    private static final String ASSERT_ERROR_MESSAGE_CODE = "PROMOTION-ASSERT";

    /**
     * コンストラクタ<br/>
     * <br/>
     * 本例外は即時スロー例外であるため、<br/>
     * エラー情報はコンストラクタでセット<br/>
     * ※インスタンス生成した後はセット不可
     *
     * @param message アサートメッセージ文言
     */
    public AssertException(String message) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, ASSERT_ERROR_MESSAGE_CODE, new String[] {message});
    }
}