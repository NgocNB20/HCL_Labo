/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.memberinfo;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSubscribeType;
import jp.co.itechh.quad.core.constant.type.HTypeMainMemberType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSendMailPermitFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSex;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 会員Dao用検索条件Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class MemberInfoSearchForDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 会員ID */
    private String memberInfoId;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 氏名 */
    private String memberInfoName;

    /** 性別 */
    private HTypeSex memberInfoSex;

    /** 生年月日 */
    private Timestamp memberInfoBirthday;

    /** 電話番号 */
    private String memberInfoTel;

    /** 郵便番号 */
    private String memberInfoZipCode;

    /** 都道府県 */
    private String memberInfoPrefecture;

    /** 住所 */
    private String memberInfoAddress;

    /** 会員状態 */
    private HTypeMemberInfoStatus memberInfoStatus;

    /** 期間（FROM） */
    private String startDate;

    /** 期間（TO） */
    private String endDate;

    /** 期間種別 */
    private String periodType;

    /** 案内メール希望 */
    private HTypeSendMailPermitFlag memberInfoSendMailPermitFlag;

    /** 最終ログインユーザーエージェント */
    private String lastLoginUserAgent;

    /** メールマガジン購読フラグ */
    private HTypeMagazineSubscribeType mailMagazine;

    /** 本会員フラグ */
    private HTypeMainMemberType mainMemberFlag;
}