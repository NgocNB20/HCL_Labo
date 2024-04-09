/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.exception;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import lombok.Getter;

/**
 * 例外内容
 */
@Getter
public class ExceptionContent {

    /** メッセージコード */
    private final String code;

    /** メッセージ内容 */
    private final String message;

    /**
     * コンストラクタ
     * @param code メッセージコード
     */
    public ExceptionContent(String code) {
        this(code, null);
    }

    /**
     * コンストラクタ
     * @param code メッセージコード
     * @param args メッセージ引数
     */
    public ExceptionContent(String code, String[] args) {
        this.code = code;
        this.message = AppLevelFacesMessageUtil.getAllMessage(code, args).getMessage();
    }
}
