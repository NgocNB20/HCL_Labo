/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.zipcode;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 郵便番号住所クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "ZipCode")
@Data
@Component
@Scope("prototype")
public class ZipCodeEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 全国地方公共団体コード */
    @Column(name = "localCode")
    @Id
    private String localCode;

    /** (旧)郵便番号 */
    @Column(name = "oldZipCode")
    @Id
    private String oldZipCode;

    /** 郵便番号 */
    @Column(name = "zipCode")
    @Id
    private String zipCode;

    /** 都道府県名(カナ) */
    @Column(name = "prefectureKana")
    private String prefectureKana;

    /** 市区町村名(カナ) */
    @Column(name = "cityKana")
    private String cityKana;

    /** 町域名(カナ) */
    @Column(name = "townKana")
    private String townKana;

    /** 都道府県名 */
    @Column(name = "prefectureName")
    @Id
    private String prefectureName;

    /** 市区町村名 */
    @Column(name = "cityName")
    @Id
    private String cityName;

    /** 町域名 */
    @Column(name = "townName")
    @Id
    private String townName;

    /** 複数郵便番号フラグ */
    @Column(name = "anyZipFlag")
    private String anyZipFlag;

    /** 小字フラグ */
    @Column(name = "numberFlag1")
    private String numberFlag1;

    /** 丁目フラグ */
    @Column(name = "numberFlag2")
    private String numberFlag2;

    /** 複数町域フラグ */
    @Column(name = "numberFlag3")
    private String numberFlag3;

    /** 変更表示種別 */
    @Column(name = "updateVisibleType")
    private String updateVisibleType;

    /** 変更理由種別 */
    @Column(name = "updateNoteType")
    private String updateNoteType;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}