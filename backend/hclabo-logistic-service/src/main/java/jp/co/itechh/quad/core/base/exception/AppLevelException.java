/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;

/**
 * AppLevelException<br/>
 * アプリでの発生するException
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public class AppLevelException extends FacesException {

    /**
     *  シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * メッセージ<br/>
     */
    private AppLevelFacesMessage appLevelFacesMessage;

    /**
     * 空のAppLevelExceptionを作成<br/>
     */
    public AppLevelException() {
        super();
    }

    /**
     * 引数 cause からAppLevelExceptionを作成<br/>
     *
     * @param cause 例外
     */
    public AppLevelException(Throwable cause) {
        super(cause);
    }

    /**
     * メッセージの取得<br/>
     * 詳細メッセージがある場合は、詳細メッセージを、<br/>
     * ない場合は、概要メッセージを返す<br/>
     *
     * @return String メッセージ
     */
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder();
        if (appLevelFacesMessage != null) {
            if (appLevelFacesMessage.getDetail() != null) {
                message.append(appLevelFacesMessage.getDetail());
            } else {
                message.append(appLevelFacesMessage.getSummary());
            }
        }
        return message.toString();
    }

    /**
     * メッセージコード<br/>
     * このExceptionのメッセージコードを返す<br/>
     *
     * @return String メッセーコード
     */
    public String getMessageCode() {
        return this.appLevelFacesMessage.getMessageCode();
    }

    /**
     * @return the appLevelFacesMessage
     */
    public AppLevelFacesMessage getAppLevelFacesMessage() {
        return appLevelFacesMessage;
    }

    /**
     * @param appLevelFacesMessage the appLevelFacesMessage to set
     */
    public void setAppLevelFacesMessage(AppLevelFacesMessage appLevelFacesMessage) {
        this.appLevelFacesMessage = appLevelFacesMessage;
    }

}
