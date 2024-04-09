/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * ApplicationException<br/>
 * 妥当性チェックエラー発生時にスローされる例外<br/>
 *
 * @author yt23807
 */
public class ApplicationException extends BaseException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ<br/>
     * <br/>
     * 本例外は遅延スロー例外であるため、<br/>
     * エラー情報はインスタンス生成後に後からセットする<br/>
     */
    public ApplicationException() {
        super();
    }

    /**
     * Add message.
     *
     * @param code      the code
     */
    public void addMessage(String code) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code);
    }

    /**
     * Add message.
     *
     * @param code      the code
     * @param args      the message arguments
     */
    public void addMessage(String code, String[] args) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code, args);
    }
}