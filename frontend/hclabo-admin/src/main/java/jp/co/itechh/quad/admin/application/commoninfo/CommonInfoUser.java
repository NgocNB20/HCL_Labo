/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.application.commoninfo;

import java.io.Serializable;

/**
 * ユーザー情報(共通情報)<br/>
 *
 * @author thang
 *
 */
public interface CommonInfoUser extends Serializable {

    /**
     * @return the memberInfoSeq
     */
    Integer getMemberInfoSeq();

    /**
     * @return the memberInfo
     */
    String getMemberInfoId();

    /**
     * @return the memberInfoLastName
     */
    String getMemberInfoLastName();

    /**
     * @return the memberInfoFirstName
     */
    String getMemberInfoFirstName();

}
