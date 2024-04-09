/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.commoninfo.impl;

import jp.co.itechh.quad.admin.application.commoninfo.CommonInfoUser;

/**
 * ユーザー情報（共通情報）<br/>
 *
 * @author natume
 *
 */
public class CommonInfoUserImpl implements CommonInfoUser {

    /** シリアルID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 会員ID */
    private String memberInfoId;

    /** 会員氏名(姓) */
    private String memberInfoLastName;

    /** 会員氏名(名) */
    private String memberInfoFirstName;

    /**
     * @return the memberInfoSeq
     */
    @Override
    public Integer getMemberInfoSeq() {
        return memberInfoSeq;
    }

    /**
     * @param memberInfoSeq the memberInfoSeq to set
     */
    public void setMemberInfoSeq(Integer memberInfoSeq) {
        this.memberInfoSeq = memberInfoSeq;
    }

    /**
     * @return the memberInfoLastName
     */
    @Override
    public String getMemberInfoLastName() {
        return memberInfoLastName;
    }

    /**
     * @param memberInfoLastName the memberInfoLastName to set
     */
    public void setMemberInfoLastName(String memberInfoLastName) {
        this.memberInfoLastName = memberInfoLastName;
    }

    /**
     * @return the memberInfoFirstName
     */
    @Override
    public String getMemberInfoFirstName() {
        return memberInfoFirstName;
    }

    /**
     * @param memberInfoFirstName the memberInfoFirstName to set
     */
    public void setMemberInfoFirstName(String memberInfoFirstName) {
        this.memberInfoFirstName = memberInfoFirstName;
    }

    /**
     * @return the memberInfoId
     */
    @Override
    public String getMemberInfoId() {
        return memberInfoId;
    }

    /**
     * @param memberInfoId the memberInfoId to set
     */
    public void setMemberInfoId(String memberInfoId) {
        this.memberInfoId = memberInfoId;
    }
}
