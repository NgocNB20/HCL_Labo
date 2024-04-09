/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.logic;

import jp.co.itechh.quad.core.base.application.FacesMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.exception.LogicException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic基底クラス
 *
 * @author natume
 * @author Nishigaki (Itec) 2011/02/07 #2642 対応
 */
public class AbstractLogic {

    /**
     * エラー保持リスト
     */
    private List<AppLevelException> errorList;

    /**
     * getComponent()
     *
     * @param <T>          コンポーネントの型
     * @param componentKey コンポーネントクラス
     * @return コンポーネント
     */
    protected <T> T getComponent(final Class<T> componentKey) {
        return ApplicationContextUtility.getBean(componentKey);
    }

    /**
     * 例外をスロー
     *
     * @param messageCode メッセージコード
     * @throws AppLevelListException 例外スロー
     */
    protected void throwMessage(String messageCode) throws AppLevelListException {
        throwMessage(messageCode, null, null);
    }

    /**
     * 例外をスロー
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @throws AppLevelListException 例外スロー
     */
    protected void throwMessage(String messageCode, Object[] args) throws AppLevelListException {
        throwMessage(messageCode, args, null);
    }

    /**
     * 例外をスロー
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param cause       エラー例外
     * @throws AppLevelListException 例外スロー
     */
    protected void throwMessage(String messageCode, Object[] args, Throwable cause) throws AppLevelListException {
        LogicException logicException = createLogicException(FacesMessage.SEVERITY_ERROR, messageCode, args, cause);
        addErrorMessage(logicException);
        throwMessage();
    }

    /**
     * LogicExceptionを作成し、errorListに追加
     *
     * @param logicException ロジック例外
     */
    protected void addErrorMessage(LogicException logicException) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(logicException);
    }

    /**
     * LogicExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     */
    protected void addErrorMessage(String messageCode) {
        addErrorMessage(messageCode, null, null);
    }

    /**
     * LogicExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     */
    protected void addErrorMessage(String messageCode, Object[] args) {
        addErrorMessage(messageCode, args, null);
    }

    /**
     * LogicExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param cause       エラー例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, Throwable cause) {
        addErrorMessage(createLogicException(FacesMessage.SEVERITY_ERROR, messageCode, args, cause));
    }

    /**
     * 例外をスロー
     *
     * @throws AppLevelListException 例外スロー
     */
    protected void throwMessage() throws AppLevelListException {
        if (!hasErrorList()) {
            throw new RuntimeException("エラーメッセージはありません");
        }
        throw new AppLevelListException(errorList);
    }

    /**
     * エラーリストの有無判定
     *
     * @return エラー 有=true, 無=false
     */
    protected boolean hasErrorList() {
        return errorList != null && !errorList.isEmpty();
    }

    /**
     * LogicException作成
     *
     * @param severity    エラーレベル
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param cause       例外
     * @return LogicException
     */
    protected LogicException createLogicException(FacesMessage.Severity severity,
                                                  String messageCode,
                                                  Object[] args,
                                                  Throwable cause) {
        return new LogicException(severity, messageCode, args, cause);
    }

}