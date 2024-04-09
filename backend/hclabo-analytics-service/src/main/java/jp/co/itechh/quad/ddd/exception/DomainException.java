/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

/**
 * DomainException
 * 業務エラー発生時にスローされる例外
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class DomainException extends BaseException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ
     * <p>
     * 本例外は即時スロー例外であるため、
     * エラー情報はコンストラクタでセット
     * ※インスタンス生成した後はセット不可
     *
     * @param code メッセージコード
     */
    public DomainException(String code) {
        this.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code);
    }

    /**
     * コンストラクタ
     * <p>
     * 本例外は即時スロー例外であるため、
     * エラー情報はコンストラクタでセット
     * ※インスタンス生成した後はセット不可
     *
     * @param code メッセージコード
     * @param args メッセージ引数
     */
    public DomainException(String code, String[] args) {
        this.addMessage(GLOBAL_MESSAGE_FIELD_NAME, code, args);
    }
}