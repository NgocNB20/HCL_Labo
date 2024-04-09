/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.service;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.exception.ServiceException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Service基底クラス<br/>
 *
 */
public class AbstractService {

    /**
     * エラー保持リスト
     */
    private List<AppLevelException> errorList;

    /**
     * getComponent()<br/>
     *
     * @param <T> コンポーネントの型
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
     * @throws AppLevelListException スロー例外
     */
    protected void throwMessage(String messageCode) throws AppLevelListException {
        throwMessage(messageCode, null, null);
    }

    /**
     * 例外をスロー
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 例外
     * @throws AppLevelListException スロー例外
     */
    protected void throwMessage(String messageCode, Object[] args, Throwable cause) throws AppLevelListException {
        ServiceException serviceException = createServiceException(messageCode, args, cause);
        addErrorMessage(serviceException);
        throwMessage();
    }

    /**
     * ServiceExceptionを作成し、errorListに追加
     *
     * @param serviceException サービス例外
     */
    protected void addErrorMessage(ServiceException serviceException) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(serviceException);
    }

    /**
     * ServiceExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     */
    protected void addErrorMessage(String messageCode) {
        addErrorMessage(messageCode, null, null);
    }

    /**
     * ServiceExceptionを作成し、errorListに追加
     * メソッドの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause エラー例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, Throwable cause) {
        addErrorMessage(createServiceException(messageCode, args, cause));
    }

    /**
     * エラーリストがある場合に例外をスロー
     *
     * @throws AppLevelListException メッセージがある場合にスロー
     * @throws RuntimeException      メッセージがない場合にスロー
     */
    protected void throwMessage() throws AppLevelListException, RuntimeException {

        if (!hasErrorMessage()) {
            throw new RuntimeException("エラーメッセージはありません");
        }
        throw new AppLevelListException(errorList);
    }

    /**
     * エラーリストの有無判定
     *
     * @return エラー 有=true 無=false
     */
    protected boolean hasErrorMessage() {
        return errorList != null && !errorList.isEmpty();
    }

    /**
     * ServiceException作成<br/>
     *
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause エラー例外
     * @return サービス例外
     */
    protected ServiceException createServiceException(String messageCode, Object[] args, Throwable cause) {
        return new ServiceException(messageCode, args, cause);
    }
}
