/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter.model;

/**
 * 通知アダプター
 */
public interface IAdministratorAdapter {

    /**
     * 運営者取得
     *
     * @param administratorSeq
     * @return AdministratorDto
     */
    AdministratorDto get(Integer administratorSeq);

}
