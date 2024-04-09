/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSexUnnecessaryAnswer;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会員バックエンド用Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.3 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class MemberInfoForBackDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 会員状態 */
    private HTypeMemberInfoStatus memberInfoStatus = HTypeMemberInfoStatus.ADMISSION;

    /** 会員一意制約用ID */
    private String memberInfoUniqueId;

    /** 会員ID */
    private String memberInfoId;

    /** 会員パスワード */
    private String memberInfoPassword;

    /** 会員氏名(姓) */
    private String memberInfoLastName;

    /** 会員氏名(名） */
    private String memberInfoFirstName;

    /** 会員フリガナ(姓) */
    private String memberInfoLastKana;

    /** 会員フリガナ(名) */
    private String memberInfoFirstKana;

    /** 会員性別 */
    private HTypeSexUnnecessaryAnswer memberInfoSex;

    /** 会員生年月日 */
    private Timestamp memberInfoBirthday;

    /** 会員TEL */
    private String memberInfoTel;

    /** 会員住所ID */
    private String memberInfoAddressId;

    /** 会員メールアドレス */
    private String memberInfoMail;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 端末識別情報 */
    private String accessUid;

    /** 更新カウンタ */
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

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    /** ここから住所情報 */

    /** 住所名 */
    private String addressName;

    /** 氏名(姓) */
    private String lastName;

    /** 氏名(名) */
    private String firstName;

    /** フリガナ(姓) */
    private String lastKana;

    /** フリガナ(名) */
    private String firstKana;

    /** 電話番号 */
    private String tel;

    /** 郵便番号 */
    private String zipCode;

    /** 都道府県 */
    private String prefecture;

    /** 住所1 */
    private String address1;

    /** 住所2 */
    private String address2;

    /** 住所3 */
    private String address3;

    /** 配送メモ */
    private String shippingMemo;

    /** ここまでが住所情報 */

}
