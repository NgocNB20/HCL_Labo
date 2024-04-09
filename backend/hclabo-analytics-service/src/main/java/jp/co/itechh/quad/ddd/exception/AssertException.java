/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * AssertException
 * アサートエラー発生時にスローされる例外
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class AssertException extends BaseException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * アサートエラーメッセージコード
     */
    private static final String ASSERT_ERROR_MESSAGE_CODE = "ANALYTICS-ASSERT";

    /**
     * コンストラクタ
     * <p>
     * 本例外は即時スロー例外であるため、
     * エラー情報はコンストラクタでセット
     * ※インスタンス生成した後はセット不可
     *
     * @param message アサートメッセージ文言
     */
    public AssertException(String message) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, ASSERT_ERROR_MESSAGE_CODE, new String[] {message});
    }
}