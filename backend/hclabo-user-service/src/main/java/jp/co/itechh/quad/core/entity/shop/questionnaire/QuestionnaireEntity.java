/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.questionnaire;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * アンケートエンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "Questionnaire")
@Data
@Component
@Scope("prototype")
public class QuestionnaireEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケートSEQ（PK） */
    @Column(name = "questionnaireSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "questionnaireSeq")
    private Integer questionnaireSeq;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** アンケートキー */
    @Column(name = "questionnaireKey")
    private String questionnaireKey;

    /** アンケート名 */
    @Column(name = "name")
    private String name;

    /** アンケート表示名PC */
    @Column(name = "namePc")
    private String namePc;

    /** 公開状態PC */
    @Column(name = "openStatusPc")
    private HTypeOpenDeleteStatus openStatusPc = HTypeOpenDeleteStatus.NO_OPEN;

    /** 公開開始日時 */
    @Column(name = "openStartTime")
    private Timestamp openStartTime;

    /** 公開終了日時 */
    @Column(name = "openEndTime")
    private Timestamp openEndTime;

    /** 説明文PC */
    @Column(name = "freeTextPc")
    private String freeTextPc;

    /** 完了文PC */
    @Column(name = "completeTextPc")
    private String completeTextPc;

    /** メモ */
    @Column(name = "memo")
    private String memo;

    /** 更新カウンタ */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo = 0;

    /** 登録日時 */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** サイトマップ出力 */
    @Column(name = "siteMapFlag")
    private HTypeSiteMapFlag siteMapFlag = HTypeSiteMapFlag.OFF;
}