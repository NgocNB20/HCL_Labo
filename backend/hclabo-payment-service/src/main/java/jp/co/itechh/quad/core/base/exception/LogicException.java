/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import jp.co.itechh.quad.core.base.application.FacesMessage;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;

/**
 * LogicException
 * Logicで発生する例外クラス
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public class LogicException extends AppLevelException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * 空のLogicExceptionを作成する
     */
    public LogicException() {
        super();
    }

    /**
     * コンストラクタ概要
     * コンストラクタの説明・概要
     *
     * @param messageCode メッセージコード
     */
    public LogicException(String messageCode) {
        this(FacesMessage.SEVERITY_ERROR, messageCode, null, null);
    }

    /**
     * コンストラクタ概要
     * コンストラクタの説明・概要
     *
     * @param severity エラーレベル
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     */
    public LogicException(FacesMessage.Severity severity, String messageCode, Object[] args) {
        this(severity, messageCode, args, null);
    }

    /**
     * コンストラクタ概要
     * コンストラクタの説明・概要
     *
     * @param severity エラーレベル
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     */
    public LogicException(FacesMessage.Severity severity, String messageCode, Object[] args, Throwable cause) {
        super(cause);
        super.setAppLevelFacesMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args));
    }

}
