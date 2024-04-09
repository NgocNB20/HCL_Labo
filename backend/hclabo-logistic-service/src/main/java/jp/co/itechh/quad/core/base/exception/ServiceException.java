/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;

/**
 * ServiceException<br/>
 * Serviceで発生する例外クラス<br/>
 *
 */
public class ServiceException extends AppLevelException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * 引数 messageCode, args, cause からServiceExceptionを作成する<br/>
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     */
    public ServiceException(String messageCode, Object[] args, Throwable cause) {
        super(cause);
        this.setAppLevelFacesMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args));
    }

}
