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
 * ServiceException
 * Serviceで発生する例外クラス
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public class ServiceException extends AppLevelException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * 空のServiceExceptionを作成する
     */
    public ServiceException() {
        super();
    }

    /**
     * 引数 messageCode からServiceExceptionを作成する
     *
     * @param messageCode メッセージコード
     */
    public ServiceException(String messageCode) {
        this(FacesMessage.SEVERITY_ERROR, messageCode, null, null);
    }

    /**
     * 引数 messageCode, messageCode, args からServiceExceptionを作成する
     *
     * @param severity エラーレベル
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     */
    public ServiceException(FacesMessage.Severity severity, String messageCode, Object[] args) {
        this(severity, messageCode, args, null);
    }

    /**
     * 引数 messageCode, messageCode, args, cause からServiceExceptionを作成する
     *
     * @param severity エラーレベル
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     */
    public ServiceException(FacesMessage.Severity severity, String messageCode, Object[] args, Throwable cause) {
        super(cause);
        this.setAppLevelFacesMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args));
    }

}
