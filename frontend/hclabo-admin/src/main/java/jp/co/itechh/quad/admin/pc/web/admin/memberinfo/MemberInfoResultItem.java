/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 会員検索結果画面情報<br/>
 *
 * @author natume
 *
 */
@Data
@Component
@Scope("prototype")
public class MemberInfoResultItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /* 検索結果 */
    /**
     * resultMemberInfoCheck<br/>
     */
    private boolean resultMemberInfoCheck;

    /**
     * memberInfoSeq<br/>
     */
    private Integer memberInfoSeq;

    /**
     * No<br/>
     */
    private Integer resultNo;

    /**
     * ID<br/>
     */
    private String resultMemberInfoId;

    /**
     * 会員状態<br/>
     */
    private String resultMemberInfoStatus;

    /**
     * 会員氏名(姓)<br/>
     */
    private String resultMemberInfoLastName;

    /**
     * 会員氏名(名)<br/>
     */
    private String resultMemberInfoFirstName;

    /**
     * Tel<br/>
     */
    private String resultMemberInfoTel;

    /**
     * 郵便番号<br/>
     */
    private String resultMemberInfoZipCode;

    /**
     * 都道府県<br/>
     */
    private String resultMemberInfoPrefecture;

    /**
     * 市区郡<br/>
     */
    private String resultMemberInfoAddress1;

    /**
     * 町村<br/>
     */
    private String resultMemberInfoAddress2;

    /**
     * 番地<br/>
     */
    private String resultMemberInfoAddress3;

    /**
     * メルマガ購読フラグ<br/>
     */
    private boolean resultMailMagazine;

}
