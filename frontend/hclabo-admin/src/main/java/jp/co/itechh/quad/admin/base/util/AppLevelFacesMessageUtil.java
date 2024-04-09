/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.util;

import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.application.FacesMessage;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;

import java.util.Arrays;

/**
 * AppLevelFacesMessageのUtil
 *
 * @author natume
 *
 */
public class AppLevelFacesMessageUtil {

    /**
     * コンストラクタ<br/>
     */
    private AppLevelFacesMessageUtil() {
    }

    /**
     * FacesMessageを取得し、AppLevelFacesMessageを作成
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param componentId  コンポーネントID
     * @return AppLevelFacesMessage
     */
    protected static AppLevelFacesMessage getMessage(String messageCode, Object[] args, String componentId) {
        // 取得できればAppLevelFacesMessageを作成
        AppLevelFacesMessage appLevelFacesMessage = null;
        FacesMessage facesMessage = FacesMessageUtil.getMessage(messageCode, args);
        if (AppLevelFacesMessageUtil.hasMessage(facesMessage)) {
            appLevelFacesMessage = new AppLevelFacesMessage(facesMessage, messageCode, args, componentId);

            // デバッグ用にエラーコードをエラーメッセージに含める
            boolean isDebug = PropertiesUtil.getSystemPropertiesValueToBool("debug");
            if (isDebug) {
                String msgCd = "[" + appLevelFacesMessage.getMessageCode() + "]";
                appLevelFacesMessage.setDetail(msgCd + appLevelFacesMessage.getDetail());
                appLevelFacesMessage.setSummary(msgCd + appLevelFacesMessage.getSummary());
            }
        }

        return appLevelFacesMessage;
    }

    /**
     * メッセージを取得し、AppLevelMessageを作成する<br/>
     * メッセージコードには、メッセージタイプをつけて取得する<br/>
     * エラータイプ取得順序
     * <ul>
     *  <li>・Fatal</li>
     *  <li>・Error</li>
     *  <li>・Warn</li>
     *  <li>・Info</li>
     * </ul>
     *
     * @param messageCode  メッセージコード
     * @param args         メッセージ引数
     * @return AppLevelFacesMessage
     */
    public static AppLevelFacesMessage getAllMessage(String messageCode, Object[] args) {
        return getAllMessage(messageCode, args, null);
    }

    /**
     * メッセージを取得し、AppLevelMessageを作成する<br/>
     * メッセージコードには、メッセージタイプをつけて取得する<br/>
     * エラータイプ取得順序
     * <ul>
     *  <li>・Fatal</li>
     *  <li>・Error</li>
     *  <li>・Warn</li>
     *  <li>・Info</li>
     * </ul>
     *
     * @param messageCode  メッセージコード
     * @param args         メッセージ引数
     * @param componentId  コンポーネントID
     * @return AppLevelFacesMessage
     */
    public static AppLevelFacesMessage getAllMessage(String messageCode, Object[] args, String componentId) {

        // そのままのコードで取得
        AppLevelFacesMessage appLevelFacesMessage = null;
        appLevelFacesMessage = getMessage(messageCode, copyArgs(args), componentId);
        if (appLevelFacesMessage != null) {
            return appLevelFacesMessage;
        }

        // Fatal
        appLevelFacesMessage =
                        getMessage(getCode(messageCode, AppLevelFacesMessage.TYPE_FATAL), copyArgs(args), componentId);
        if (appLevelFacesMessage != null) {
            return appLevelFacesMessage;
        }

        // Error
        appLevelFacesMessage =
                        getMessage(getCode(messageCode, AppLevelFacesMessage.TYPE_ERROR), copyArgs(args), componentId);
        if (appLevelFacesMessage != null) {
            return appLevelFacesMessage;
        }

        // Warn
        appLevelFacesMessage =
                        getMessage(getCode(messageCode, AppLevelFacesMessage.TYPE_WARN), copyArgs(args), componentId);
        if (appLevelFacesMessage != null) {
            return appLevelFacesMessage;
        }

        // Info
        appLevelFacesMessage =
                        getMessage(getCode(messageCode, AppLevelFacesMessage.TYPE_INFO), copyArgs(args), componentId);
        if (appLevelFacesMessage != null) {
            return appLevelFacesMessage;
        }

        // 全てない場合は、空のAppLevelFacesMessage
        return new AppLevelFacesMessage();
    }

    /**
     * メッセージ引数のコピーを渡す<br/>
     * FacesMessageUtil#getMessageの処理でメッセージが取得できない場合も<br/>
     * 引数の文字列は、HTMLエスケープされてしまう為<br/>
     *
     * @param args メッセージ引数
     * @return コピーしたメッセージ引数
     */
    private static Object[] copyArgs(Object[] args) {
        if (args == null) {
            return null;
        }
        return Arrays.copyOf(args, args.length);
    }

    /**
     * エラーメッセージコードに整形
     *
     * @param messageCode メッセージコード
     * @param messageType メッセージタイプ
     * @return メッセージコード + メッセージタイプ
     */
    public static String getCode(String messageCode, String messageType) {
        return messageCode + messageType;
    }

    /**
     * メッセージの有無
     *
     * @param facesMessage FacesMessage
     * @return メッセージ　有=true, 無=false
     */
    public static boolean hasMessage(FacesMessage facesMessage) {
        if (facesMessage == null) {
            return false;
        }
        if (facesMessage.getSummary() == null && facesMessage.getDetail() == null) {
            return false;
        }
        return true;
    }

    /**
     * メッセージコードが同じかの判定
     *
     * @param appLevelFacesMessage AppLevelFacesMessage
     * @param messageCode メッセージコード
     * @return メッセージ判定 同じ=true, 違う=false
     */
    public static boolean isSameCode(AppLevelFacesMessage appLevelFacesMessage, String messageCode) {

        if (appLevelFacesMessage == null || appLevelFacesMessage.getMessageCode() == null) {
            return false;
        }
        return isSameCode(appLevelFacesMessage.getMessageCode(), messageCode);
    }

    /**
     * メッセージコードが同じかの判定
     *
     * @param appLevelFacesMessageCode AppLevelFacesMessageのメッセージコード
     * @param messageCode メッセージコード
     * @return メッセージ判定 同じ=true, 違う=false
     */
    public static boolean isSameCode(String appLevelFacesMessageCode, String messageCode) {
        return appLevelFacesMessageCode.startsWith(messageCode);
    }

    /**
     * メッセージタイプが「システムエラー」かを判断
     *
     * @param appLevelFacesMessage AppLevelFacesMessage
     * @return システムエラー=true , それ以外=false
     */
    public static boolean isSystemError(AppLevelFacesMessage appLevelFacesMessage) {
        return appLevelFacesMessage != null && appLevelFacesMessage.getMessageCode() != null
               && appLevelFacesMessage.getMessageCode().endsWith(AppLevelFacesMessage.TYPE_FATAL);
    }

    /**
     * メッセージの再作成を行う<br/>
     * メッセージコードと引数でメッセージを取得し、<br/>
     * 元のエラーレベルでメッセージの再作成を行う<br/>
     *
     * @param appLevelFacesMessage メッセージ
     * @return 再作成後のメッセージ
     */
    public static AppLevelFacesMessage remakeAppLevelFacesMessage(AppLevelFacesMessage appLevelFacesMessage) {

        // メッセージの取得
        AppLevelFacesMessage reAppLevelFacesMessage =
                        getAllMessage(appLevelFacesMessage.getMessageCode(), appLevelFacesMessage.getArgs(),
                                      appLevelFacesMessage.getComponentId()
                                     );

        // メッセージレベルの再設定
        FacesMessage.Severity severity = null;
        switch (appLevelFacesMessage.getSeverity().getOrdinal()) {
            case 1:
                severity = FacesMessage.SEVERITY_INFO;
                break;
            case 2:
                severity = FacesMessage.SEVERITY_WARN;
                break;
            case 3:
                severity = FacesMessage.SEVERITY_ERROR;
                break;
            case 4:
                severity = FacesMessage.SEVERITY_FATAL;
                break;
            default:
                severity = FacesMessage.SEVERITY_ERROR;
        }
        reAppLevelFacesMessage.setSeverity(severity);
        return reAppLevelFacesMessage;
    }

}