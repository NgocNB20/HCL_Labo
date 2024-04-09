/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.application.commoninfo;

import java.io.Serializable;

/**
 * 管理者情報(共通情報)
 *
 * @author thang
 *
 */
public interface CommonInfoAdministrator extends Serializable {

    /**
     * @return the administratorSeq
     */
    Integer getAdministratorSeq();

    /**
     * @return the adminId
     */
    String getAdministratorId();

    /**
     * @return the administratorLastName
     */
    String getAdministratorLastName();

    /**
     * @return the administratorFirstName
     */
    String getAdministratorFirstName();

    /**
     * 指定された権限種別に対する権限レベルを返す
     * @param authTypeCode 権限種別コード
     * @return 権限レベル
     */
    Integer getAuthLevel(String authTypeCode);

}
