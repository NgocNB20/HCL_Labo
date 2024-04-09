/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSexUnnecessaryAnswer;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会員クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "MemberInfo")
@Data
@Component
@Scope("prototype")
public class MemberInfoEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ（必須） */
    @Column(name = "memberInfoSeq")
    @Id
    private Integer memberInfoSeq;

    /** 会員状態（必須） */
    @Column(name = "memberInfoStatus")
    private HTypeMemberInfoStatus memberInfoStatus = HTypeMemberInfoStatus.ADMISSION;

    /** 会員一意制約用ID */
    @Column(name = "memberInfoUniqueId")
    private String memberInfoUniqueId;

    /** 会員ID */
    @Column(name = "memberInfoId")
    private String memberInfoId;

    /** 会員パスワード */
    @Column(name = "memberInfoPassword")
    private String memberInfoPassword;

    /** 会員氏名(姓)（必須） */
    @Column(name = "memberInfoLastName")
    private String memberInfoLastName;

    /** 会員氏名(名） */
    @Column(name = "memberInfoFirstName")
    private String memberInfoFirstName;

    /** 会員フリガナ(姓)（必須） */
    @Column(name = "memberInfoLastKana")
    private String memberInfoLastKana;

    /** 会員フリガナ(名) */
    @Column(name = "memberInfoFirstKana")
    private String memberInfoFirstKana;

    /** 会員性別 */
    @Column(name = "memberInfoSex")
    private HTypeSexUnnecessaryAnswer memberInfoSex;

    /** 会員生年月日（必須） */
    @Column(name = "memberInfoBirthday")
    private Timestamp memberInfoBirthday;

    /** 会員TEL */
    @Column(name = "memberInfoTel")
    private String memberInfoTel;

    /** 会員住所ID */
    @Column(name = "memberInfoAddressId")
    private String memberInfoAddressId;

    /** 会員メールアドレス */
    @Column(name = "memberInfoMail")
    private String memberInfoMail;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 端末識別情報 */
    @Column(name = "accessUid")
    private String accessUid;

    /** 更新カウンタ（必須） */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo = 0;

    /** 入会日Ymd */
    @Column(name = "admissionYmd")
    private String admissionYmd;

    /** 退会日Ymd */
    @Column(name = "secessionYmd")
    private String secessionYmd;

    /** メモ */
    @Column(name = "memo")
    private String memo;

    /** 会員FAX */
    @Column(name = "memberInfoFax")
    private String memberInfoFax;

    /** 最終ログイン日時 */
    @Column(name = "lastLoginTime")
    private Timestamp lastLoginTime;

    /** 最終ログインユーザーエージェント */
    @Column(name = "lastLoginUserAgent")
    private String lastLoginUserAgent;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
