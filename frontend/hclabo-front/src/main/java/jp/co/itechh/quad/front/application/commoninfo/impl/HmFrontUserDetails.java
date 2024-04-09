/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.application.commoninfo.impl;

import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Spring-Security Front用ユーザ情報クラス
 * commonInfoに設定する項目用にカスタマイズ
 *
 * @author kaneda
 */
@Getter
public class HmFrontUserDetails extends User {

    private static final long serialVersionUID = 1L;

    /** 会員クラス */
    private final MemberInfoEntity memberInfoEntity;

    /**
     * コンストラクタ
     *
     * @param memberInfoEntity
     */
    public HmFrontUserDetails(MemberInfoEntity memberInfoEntity) {

        // パラメータ設定 (認証用+commonInfo用)
        super(memberInfoEntity.getMemberInfoId(), memberInfoEntity.getMemberInfoPassword(),
              AuthorityUtils.createAuthorityList("ROLE_USER")
             );

        this.memberInfoEntity = memberInfoEntity;
    }
}