/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.exception;

import jp.co.itechh.quad.front.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.front.base.application.FacesMessage;
import jp.co.itechh.quad.front.base.exception.AppLevelException;
import jp.co.itechh.quad.front.base.util.AppLevelFacesMessageUtil;

/**
 * ControllerException<br/>
 * Controllerで発生する例外クラス<br/>
 *
 * @author kaneda
 *
 */
public class ControllerException extends AppLevelException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 空のControllerExceptionを作成<br/>
     */
    public ControllerException() {
        super();
    }

    /**
     * FacesMessage.SEVERITY_ERRORと引数messageCodeからControllerExceptionを作成<br/>
     *
     * @param messageCode   メッセージコード
     */
    public ControllerException(String messageCode) {
        this(FacesMessage.SEVERITY_ERROR, messageCode, null);
    }

    /**
     * 引数 severity, messageCode, args からControllerExceptionを作成<br/>
     *
     * @param severity      エラーレベル
     * @param messageCode   メッセージコード
     * @param args          メッセージ引数
     */
    public ControllerException(FacesMessage.Severity severity, String messageCode, Object[] args) {
        this(severity, messageCode, args, null, null);
    }

    /**
     * 引数 severity, messageCode, args, cause からControllerExceptionを作成<br/>
     *
     * @param severity      エラーレベル
     * @param messageCode   メッセージコード
     * @param args          メッセージ引数
     * @param componentId   コンポーネントID
     * @param cause         例外
     */
    public ControllerException(FacesMessage.Severity severity,
                               String messageCode,
                               Object[] args,
                               String componentId,
                               Throwable cause) {
        super(cause);
        this.setAppLevelFacesMessage(AppLevelFacesMessageUtil.getAllMessage(messageCode, args, componentId));
    }

    /**
     * 引数 severity, message
     *
     * @param severity リハーサル
     * @param message メッセージ
     */
    public ControllerException(FacesMessage.Severity severity, String message) {

        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSeverity(severity);
        facesMessage.setDetail(message);
        facesMessage.setSummary(message);

        // そのままのコードで取得
        AppLevelFacesMessage appLevelFacesMessage = new AppLevelFacesMessage(facesMessage);

        this.setAppLevelFacesMessage(appLevelFacesMessage);
    }
}