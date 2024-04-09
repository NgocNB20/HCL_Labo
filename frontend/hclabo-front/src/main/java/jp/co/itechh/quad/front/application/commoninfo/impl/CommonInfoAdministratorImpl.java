/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.front.application.commoninfo.impl;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfoAdministrator;
import jp.co.itechh.quad.front.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.front.entity.administrator.AdminAuthGroupEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 管理者情報（共通情報）<br/>
 *
 * @author thang
 *
 */
@Component
public class CommonInfoAdministratorImpl implements CommonInfoAdministrator {

    /** シリアルID */
    private static final long serialVersionUID = 1L;

    /** 管理者SEQ */
    private Integer administratorSeq;

    /** 管理者ID */
    private String administratorId;

    /** 管理者氏名(姓) */
    private String administratorLastName;

    /** 管理者氏名(名) */
    private String administratorFirstName;

    /** 権限設定 */
    private Map<String, Integer> authMap;

    /**
     * @return the administratorSeq
     */
    @Override
    public Integer getAdministratorSeq() {
        return administratorSeq;
    }

    /**
     * @param administratorSeq the administratorSeq to set
     */
    public void setAdministratorSeq(Integer administratorSeq) {
        this.administratorSeq = administratorSeq;
    }

    /**
     * @return the administratorId
     */
    @Override
    public String getAdministratorId() {
        return administratorId;
    }

    /**
     * @param administratorId the administratorId to set
     */
    public void setAdministratorId(String administratorId) {
        this.administratorId = administratorId;
    }

    /**
     * @return the administratorLastName
     */
    @Override
    public String getAdministratorLastName() {
        return administratorLastName;
    }

    /**
     * @param administratorLastName the administratorLastName to set
     */
    public void setAdministratorLastName(String administratorLastName) {
        this.administratorLastName = administratorLastName;
    }

    /**
     * @return the administratorFirstName
     */
    @Override
    public String getAdministratorFirstName() {
        return administratorFirstName;
    }

    /**
     * @param administratorFirstName the administratorFirstName to set
     */
    public void setAdministratorFirstName(String administratorFirstName) {
        this.administratorFirstName = administratorFirstName;
    }

    /**
     * 指定された権限種別に対する権限レベルを返す
     * @param authTypeCode 権限種別コード
     * @return 権限レベル
     */
    @Override
    public Integer getAuthLevel(String authTypeCode) {

        if (this.authMap == null) {
            return Integer.valueOf(0);
        }

        Integer level = this.authMap.get(authTypeCode);

        if (level == null) {
            return Integer.valueOf(0);
        }

        return level;
    }

    /**
     * 権限グループ情報を設定する
     * @param group 権限グループ
     */
    public void setAdminAuthgroup(AdminAuthGroupEntity group) {

        if (group == null || group.getAdminAuthGroupDetailList() == null) {
            this.authMap = Collections.emptyMap();
            return;
        }

        Map<String, Integer> map = new LinkedHashMap<>();
        for (AdminAuthGroupDetailEntity detail : group.getAdminAuthGroupDetailList()) {
            map.put(detail.getAuthTypeCode(), detail.getAuthLevel());
        }

        this.authMap = Collections.unmodifiableMap(map);
    }
}