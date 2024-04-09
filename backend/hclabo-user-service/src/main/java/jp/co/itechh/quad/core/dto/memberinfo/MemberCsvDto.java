/*
 * Project Name : HIT-MALL
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.dto.memberinfo;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSubscribeType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSex;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会員情報CSVDTO
 *
 * @author DtoGenerator
 */
@Entity
@Data
@Component
@Scope("prototype")
public class MemberCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員SEQ */
    @CsvColumn(order = 10, columnLabel = "会員SEQ")
    private Integer memberInfoSeq;

    /** 会員状態 */
    @CsvColumn(order = 20, columnLabel = "会員状態", enumOutputType = "value")
    private HTypeMemberInfoStatus memberInfoStatus;

    /** 会員ID */
    @CsvColumn(order = 30, columnLabel = "会員ID")
    private String memberInfoId;

    /** 最終ログイン日時 */
    @CsvColumn(order = 40, columnLabel = "最終ログイン日時", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp lastLoginTime;

    /** 最終ログインユーザーエージェント */
    @CsvColumn(order = 50, columnLabel = "最終ログインユーザーエージェント")
    private String lastLoginUserAgent;

    /** 会員氏名(姓) */
    @CsvColumn(order = 60, columnLabel = "会員姓")
    private String memberInfoLastName;

    /** 会員氏名(名） */
    @CsvColumn(order = 70, columnLabel = "会員名")
    private String memberInfoFirstName;

    /** 会員フリガナ(姓) */
    @CsvColumn(order = 80, columnLabel = "会員フリガナ姓")
    private String memberInfoLastKana;

    /** 会員フリガナ(名) */
    @CsvColumn(order = 90, columnLabel = "会員フリガナ名")
    private String memberInfoFirstKana;

    /** 会員性別 */
    @CsvColumn(order = 100, columnLabel = "性別", enumOutputType = "value")
    private HTypeSex memberInfoSex;

    /** 会員生年月日 */
    @CsvColumn(order = 110, columnLabel = "生年月日", dateFormat = "yyyy/MM/dd")
    private Timestamp memberInfoBirthday;

    /** 会員TEL */
    @CsvColumn(order = 120, columnLabel = "電話番号")
    private String memberInfoTel;

    /** 会員住所-郵便番号 */
    @CsvColumn(order = 130, columnLabel = "郵便番号")
    private String zipCode;

    /** 会員住所-都道府県 */
    @CsvColumn(order = 140, columnLabel = "都道府県")
    private String prefecture;

    /** 会員住所-市区郡 */
    @CsvColumn(order = 150, columnLabel = "市区郡")
    private String address1;

    /** 会員住所-町村・番地 */
    @CsvColumn(order = 160, columnLabel = "町村_番地")
    private String address2;

    /** 会員住所-マンション・建物名 */
    @CsvColumn(order = 170, columnLabel = "マンション_建物名")
    private String address3;

    /** 入会日Ymd */
    @CsvColumn(order = 180, columnLabel = "入会日")
    private String admissionYmd;

    /** 退会日Ymd */
    @CsvColumn(order = 190, columnLabel = "退会日")
    private String secessionYmd;

    /** 会員パスワード */
    @CsvColumn(order = 200, columnLabel = "パスワード")
    private String memberInfoPassword;

    /** メルマガ会員購読 */
    @CsvColumn(order = 210, columnLabel = "メルマガ会員購読", enumOutputType = "value")
    private HTypeMagazineSubscribeType mailMagazine;

    /** 登録日時 */
    @CsvColumn(order = 220, columnLabel = "登録日時", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp registTime;

    /** 更新日時 */
    @CsvColumn(order = 230, columnLabel = "更新日時", dateFormat = "yyyy/MM/dd HH:mm:ss")
    private Timestamp updateTime;
}
