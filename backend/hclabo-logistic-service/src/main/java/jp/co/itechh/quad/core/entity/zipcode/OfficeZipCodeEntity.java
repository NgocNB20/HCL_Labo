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
 * 事業所の個別郵便番号住所クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "OfficeZipCode")
@Data
@Component
@Scope("prototype")
public class OfficeZipCodeEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 大口事業所の所在地 */
    @Column(name = "officeCode")
    @Id
    private String officeCode;

    /** 大口事業所名（カナ） */
    @Column(name = "officeKana")
    @Id
    private String officeKana;

    /** 大口事業所名（漢字） */
    @Column(name = "officeName")
    @Id
    private String officeName;

    /** 都道府県名（漢字） */
    @Column(name = "prefectureName")
    @Id
    private String prefectureName;

    /** 市区町村名（漢字） */
    @Column(name = "cityName")
    @Id
    private String cityName;

    /** 町域名（漢字） */
    @Column(name = "townName")
    @Id
    private String townName;

    /** 小字名、丁目、番地等（漢字） */
    @Column(name = "numbers")
    @Id
    private String numbers;

    /** 大口事業所個別番号 */
    @Column(name = "zipCode")
    @Id
    private String zipCode;

    /** 旧郵便番号 */
    @Column(name = "oldZipCode")
    @Id
    private String oldZipCode;

    /** 取扱支店名（漢字） */
    @Column(name = "handlingBranchName")
    private String handlingBranchName;

    /** 個別番号の種別 */
    @Column(name = "individualType")
    private String individualType;

    /** 複数番号の有無 */
    @Column(name = "anyZipFlag")
    private String anyZipFlag;

    /** 修正コード */
    @Column(name = "updateCode")
    private String updateCode;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}