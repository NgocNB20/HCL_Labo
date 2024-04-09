/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.questionnaire;

import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * アンケートエンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class QuestionnaireEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケートSEQ（PK） */
    private Integer questionnaireSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** アンケートキー */
    private String questionnaireKey;

    /** アンケート名 */
    private String name;

    /** アンケート表示名PC */
    private String namePc;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPc = HTypeOpenDeleteStatus.NO_OPEN;

    /** 公開開始日時 */
    private Timestamp openStartTime;

    /** 公開終了日時 */
    private Timestamp openEndTime;

    /** 説明文PC */
    private String freeTextPc;

    /** 完了文PC */
    private String completeTextPc;

    /** メモ */
    private String memo;

    /** 更新カウンタ */
    private Integer versionNo = 0;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    /** サイトマップ出力 */
    private HTypeSiteMapFlag siteMapFlag = HTypeSiteMapFlag.OFF;
}