/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.application.commoninfo;

import java.io.Serializable;

/**
 * 共通情報 <br/>
 *
 * <ul>
 * 各共通情報を保持
 * <li>基本情報</li>
 * <li>ユーザー情報</li>
 * <li>管理者情報</li>
 * <li>ショップ情報</li>
 * </ul>
 *
 * @author thang
 *
 */
public interface CommonInfo extends Serializable {

    /**
     * 共通情報クラス 定数<br/>
     */
    String COMMONINFO = CommonInfo.class.getName();

    /**
     * @return the commonBaseInfo
     */
    CommonInfoBase getCommonInfoBase();

    /**
     * @return the commonUserInfo
     */
    CommonInfoUser getCommonInfoUser();

    /**
     * @return the commonAdministratorInfo
     */
    CommonInfoAdministrator getCommonInfoAdministrator();
}
