/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.web;

import jp.co.itechh.quad.core.base.application.FacesMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.exception.ControllerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * コントローラ基底クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 * @Transactionalでトランザクション制御
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractController {

    /** エラーリスト */
    private List<AppLevelException> errorList = new ArrayList<>();

    /**
     * エラーメッセージの有無判定
     *
     * @return true..有、false..無
     */
    protected boolean hasErrorMessage() {
        return errorList != null && !errorList.isEmpty();
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * @param controllerException
     */
    protected void addErrorMessage(ControllerException controllerException) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(controllerException);
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * ControllerExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     */
    protected void addErrorMessage(String messageCode) {
        addErrorMessage(messageCode, null);
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * ControllerExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     */
    protected void addErrorMessage(String messageCode, Object[] args) {
        addErrorMessage(messageCode, args, null, null);
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * ControllerExceptionを作成し、errorListに追加
     * メソッドの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param cause       例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, Throwable cause) {
        addErrorMessage(messageCode, args, null, cause);
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * ControllerExceptionを作成し、errorListに追加
     * メソッドの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param componentId コンポーネントID
     * @param cause       例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, String componentId, Throwable cause) {
        addErrorMessage(createControllerException(FacesMessage.SEVERITY_ERROR, messageCode, args, componentId, cause));
    }

    /**
     * 引数 errorCode からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     *
     * @param errorCode エラーコード
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode) throws AppLevelListException {
        throwMessage(errorCode, null);
    }

    /**
     * 引数 errorCode args からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     *
     * @param errorCode エラーコード
     * @param args      エラーメッセージ引数
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args) throws AppLevelListException {
        throwMessage(errorCode, args, null);
    }

    /**
     * 引数 errorCode args cause からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     * エラーレベルは、FacesMessage.SEVERITY_ERROR<br/>
     *
     * @param errorCode エラーコード
     * @param args      エラーメッセージ引数
     * @param cause     例外
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args, Throwable cause) throws AppLevelListException {
        throwMessage(errorCode, args, null, cause);
    }

    /**
     * 引数 errorCode args cause からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     * エラーレベルは、FacesMessage.SEVERITY_ERROR<br/>
     *
     * @param errorCode エラーコード
     * @param args      エラーメッセージ引数
     * @param componentId コンポーネントID
     * @param cause     例外
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args, String componentId, Throwable cause)
                    throws AppLevelListException {
        ControllerException controllerException =
                        createControllerException(FacesMessage.SEVERITY_ERROR, errorCode, args, componentId, cause);
        addErrorMessage(controllerException);
        throwMessage();
    }

    /**
     * エラーを発生させる(以降の処理中断)<br/>
     * 現在溜まっているメッセージからAppLevelListExceptionを作成し、<br/>
     * 例外をスローする<br/>
     * メッセージがない場合は、空のAppLevelListExceptionをスローする<br/>
     *
     * @throws AppLevelListException メッセージがある場合にスロー
     * @throws RuntimeException      メッセージがない場合にスロー
     */
    protected void throwMessage() throws AppLevelListException, RuntimeException {
        throw new AppLevelListException(errorList);
    }

    /**
     * 引数 severity, errorCode, args, causeからControllerExceptionを作成<br/>
     * メソッドの説明・概要<br/>
     *
     * @param severity  エラーレベル
     * @param errorCode メッセージコード
     * @param args      メッセージ引数
     * @param componentId コンポーネントID
     * @param cause     例外
     * @return ControllerExceptionオブジェクト
     */
    protected static ControllerException createControllerException(FacesMessage.Severity severity,
                                                                   String errorCode,
                                                                   Object[] args,
                                                                   String componentId,
                                                                   Throwable cause) {
        return new ControllerException(severity, errorCode, args, componentId, cause);
    }

}