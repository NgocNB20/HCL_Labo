/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * ValidateException
 * バリデートエラー発生時にスローされる例外
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class ValidateException extends BaseException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     * <p>
     * 本例外は遅延スロー例外であるため、
     * エラー情報はインスタンス生成後に後からセットする
     */
    public ValidateException() {
        super();
    }

    /**
     * Add message.
     *
     * @param fieldName the field name
     * @param code      the code
     */
    public void addMessage(String fieldName, String code) {
        super.addMessage(fieldName, code);
    }

    /**
     * Add message.
     *
     * @param fieldName the field name
     * @param code      the code
     * @param args      the message arguments
     */
    public void addMessage(String fieldName, String code, String[] args) {
        super.addMessage(fieldName, code, args);
    }
}