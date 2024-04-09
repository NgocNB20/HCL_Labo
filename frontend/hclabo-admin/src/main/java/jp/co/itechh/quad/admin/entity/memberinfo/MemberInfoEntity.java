/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.memberinfo;

import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class MemberInfoEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ（必須） */
    private Integer memberInfoSeq;

    /** 会員状態（必須） */
    private HTypeMemberInfoStatus memberInfoStatus = HTypeMemberInfoStatus.ADMISSION;

    /** 会員一意制約用ID */
    private String memberInfoUniqueId;

    /** 会員ID */
    private String memberInfoId;

    /** 会員パスワード */
    private String memberInfoPassword;

    /** 会員氏名(姓)（必須） */
    private String memberInfoLastName;

    /** 会員氏名(名） */
    private String memberInfoFirstName;

    /** 会員フリガナ(姓)（必須） */
    private String memberInfoLastKana;

    /** 会員フリガナ(名) */
    private String memberInfoFirstKana;

    /** 会員性別 */
    private HTypeSexUnnecessaryAnswer memberInfoSex;

    /** 会員生年月日（必須） */
    private Timestamp memberInfoBirthday;

    /** 会員TEL */
    private String memberInfoTel;

    /** 会員住所ID */
    private String memberInfoAddressId;

    /** 会員メールアドレス */
    private String memberInfoMail;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 端末識別情報 */
    private String accessUid;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 入会日Ymd */
    private String admissionYmd;

    /** 退会日Ymd */
    private String secessionYmd;

    /** メモ */
    private String memo;

    /** 最終ログイン日時 */
    private Timestamp lastLoginTime;

    /** 最終ログインユーザーエージェント */
    private String lastLoginUserAgent;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}
