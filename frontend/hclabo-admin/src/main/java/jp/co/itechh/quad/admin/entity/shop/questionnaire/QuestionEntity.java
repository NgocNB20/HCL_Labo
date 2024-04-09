/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.questionnaire;

import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeReplyRequiredFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeReplyType;
import jp.co.itechh.quad.admin.constant.type.HTypeReplyValidatePattern;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * アンケート設問エンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class QuestionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケート設問SEQ（PK） */
    private Integer questionSeq;

    /** アンケートSEQ (FK) */
    private Integer questionnaireSeq;

    /** 表示順 */
    private Integer orderDisplay;

    /** 質問内容 */
    private String question;

    /** 公開状態 */
    private HTypeOpenDeleteStatus openStatus = HTypeOpenDeleteStatus.NO_OPEN;

    /** 回答必須フラグ */
    private HTypeReplyRequiredFlag replyRequiredFlag = HTypeReplyRequiredFlag.OPTIONAL;

    /** 回答形式種別 */
    private HTypeReplyType replyType = HTypeReplyType.TEXTBOX;

    /** 回答文字種別 */
    private HTypeReplyValidatePattern replyValidatePattern = HTypeReplyValidatePattern.NO_LIMIT;

    /** 回答桁数 */
    private Integer replyMaxLength;

    /** 選択肢 */
    private String choices;

    /** 更新カウンタ */
    private Integer versionNo = 0;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

}