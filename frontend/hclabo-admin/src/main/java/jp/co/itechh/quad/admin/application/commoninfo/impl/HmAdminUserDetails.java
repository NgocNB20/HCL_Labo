/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.application.commoninfo.impl;

import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Spring-Security Admin用管理者情報クラス
 * DB認証用カスタマイズ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Getter
public class HmAdminUserDetails extends User {

    private static final long serialVersionUID = 1L;

    /** 管理者クラス */
    private final AdministratorEntity administratorEntity;

    /**
     * コンストラクタ
     *
     * @param administratorEntity
     * @param authorityList
     */
    public HmAdminUserDetails(AdministratorEntity administratorEntity, String[] authorityList) {

        // 認証用ユーザ情報設定
        super(administratorEntity.getAdministratorId(), administratorEntity.getAdministratorPassword(),
              AuthorityUtils.createAuthorityList(authorityList)
             );

        // DI設定
        this.administratorEntity = administratorEntity;
    }

}