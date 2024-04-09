/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.exception;

import java.util.List;

/**
 * AppLevelListException
 * AppLevelExceptionのリストを保持するException
 *
 * @author natume
 * @version $Revision: 1.1 $
 */
public class AppLevelListException extends RuntimeException {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 引数 errorList からAppLevelListExceptionを作成する
     *
     * @param errorList AppLevelExceptionリスト
     */
    public AppLevelListException(List<AppLevelException> errorList) {
        this.errorList = errorList;
    }

    /**
     * エラーリスト
     */
    private List<AppLevelException> errorList;

    /**
     * @return the errorList
     */
    public List<AppLevelException> getErrorList() {
        return errorList;
    }

    /**
     * @param errorList the errorList to set
     */
    public void setErrorList(List<AppLevelException> errorList) {
        this.errorList = errorList;
    }

}
