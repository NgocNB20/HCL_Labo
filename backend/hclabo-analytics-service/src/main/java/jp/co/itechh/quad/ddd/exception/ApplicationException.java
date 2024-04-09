/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * ApplicationException
 * 妥当性チェックエラー発生時にスローされる例外
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class ApplicationException extends BaseException {

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
    public ApplicationException() {
        super();
    }

    /**
     * Add message.
     *
     * @param code the code
     */
    public void addMessage(String code) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code);
    }

    /**
     * Add message.
     *
     * @param code the code
     * @param args the message arguments
     */
    public void addMessage(String code, String[] args) {
        super.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code, args);
    }

    /**
     * Add message.
     *
     * @param goodsId the goodsId
     * @param code    the code
     */
    public void addMessage(String goodsId, String code) {
        super.addMessage(goodsId, code);
    }
}