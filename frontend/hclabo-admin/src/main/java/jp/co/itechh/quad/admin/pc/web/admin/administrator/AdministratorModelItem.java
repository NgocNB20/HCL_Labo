/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 運営者検索結果画面情報
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 */
@Data
@Component
@Scope("prototype")
public class AdministratorModelItem implements Serializable {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * seq
     */
    private Integer administratorSeq;

    /**
     * No
     */
    private Integer resultNo;
    /**
     * ID
     */
    private String resultAdministratorId;
    /**
     * 姓
     */
    private String resultAdministratorLastName;
    /**
     * 名
     */
    private String resultAdministratorFirstName;
    /**
     * 姓カナ
     */
    private String resultAdministratorLastKana;
    /**
     * 名カナ
     */
    private String resultAdministratorFirstKana;
    /**
     * メールアドレス
     */
    private String resultMail;
    /**
     * 状態
     */
    private String resultAdministratorStatus;
    /**
     * 利用開始日
     */
    private Timestamp resultUseStartDate;
    /**
     * 利用終了日
     */
    private Timestamp resultUseEndDate;
    /**
     * 管理者グループ名
     */
    private String resultAdministratorGroupName;

}
