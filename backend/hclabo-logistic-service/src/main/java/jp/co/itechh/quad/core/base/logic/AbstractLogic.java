/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.logic;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.exception.LogicException;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic基底クラス<br/>
 *
 */
public class AbstractLogic {

    /**
     * エラー保持リスト
     */
    private List<AppLevelException> errorList;

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
     * @param args メッセージ引数
     * @param cause エラー例外
     * @throws AppLevelListException 例外スロー
     */
    protected void throwMessage(String messageCode, Object[] args, Throwable cause) throws AppLevelListException {
        LogicException logicException = createLogicException(messageCode, args, cause);
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
     * LogicException作成<br/>
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     * @return LogicException
     */
    protected LogicException createLogicException(String messageCode, Object[] args, Throwable cause) {
        return new LogicException(messageCode, args, cause);
    }
}