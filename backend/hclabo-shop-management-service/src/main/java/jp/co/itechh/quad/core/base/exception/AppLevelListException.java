/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import java.util.List;

/**
 * AppLevelListException<br/>
 * AppLevelExceptionのリストを保持するException
 *
 */
public class AppLevelListException extends RuntimeException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /**
     * 引数 errorList からAppLevelListExceptionを作成する<br/>
     *
     * @param errorList AppLevelExceptionリスト
     */
    public AppLevelListException(List<AppLevelException> errorList) {
        this.errorList = errorList;
    }

    /**
     * エラーリスト<br/>
     */
    private List<AppLevelException> errorList;

    /**
     * @return the errorList
     */
    public List<AppLevelException> getErrorList() {
        return errorList;
    }
}
