/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;

/**
 * LogicException<br/>
 * Logicで発生する例外クラス<br/>
 *
 *
 */
public class LogicException extends AppLevelException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ概要<br/>
     * コンストラクタの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     */
    public LogicException(String messageCode, Object[] args, Throwable cause) {
        super(cause);
        super.setAppLevelFacesMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args));
    }

}
