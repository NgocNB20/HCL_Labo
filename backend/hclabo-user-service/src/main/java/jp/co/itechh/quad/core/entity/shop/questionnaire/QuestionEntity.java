/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.questionnaire;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeReplyRequiredFlag;
import jp.co.itechh.quad.core.constant.type.HTypeReplyType;
import jp.co.itechh.quad.core.constant.type.HTypeReplyValidatePattern;
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
 * アンケート設問エンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "Question")
@Data
@Component
@Scope("prototype")
public class QuestionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケート設問SEQ（PK） */
    @Column(name = "questionSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "questionSeq")
    private Integer questionSeq;

    /** アンケートSEQ (FK) */
    @Column(name = "questionnaireSeq")
    private Integer questionnaireSeq;

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 質問内容 */
    @Column(name = "question")
    private String question;

    /** 公開状態 */
    @Column(name = "openStatus")
    private HTypeOpenDeleteStatus openStatus = HTypeOpenDeleteStatus.NO_OPEN;

    /** 回答必須フラグ */
    @Column(name = "replyRequiredFlag")
    private HTypeReplyRequiredFlag replyRequiredFlag = HTypeReplyRequiredFlag.OPTIONAL;

    /** 回答形式種別 */
    @Column(name = "replyType")
    private HTypeReplyType replyType = HTypeReplyType.TEXTBOX;

    /** 回答文字種別 */
    @Column(name = "replyValidatePattern")
    private HTypeReplyValidatePattern replyValidatePattern = HTypeReplyValidatePattern.NO_LIMIT;

    /** 回答桁数 */
    @Column(name = "replyMaxLength")
    private Integer replyMaxLength;

    /** 選択肢 */
    @Column(name = "choices")
    private String choices;

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

}