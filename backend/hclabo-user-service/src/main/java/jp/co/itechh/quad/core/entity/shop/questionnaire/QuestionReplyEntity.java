/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.questionnaire;

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
 * アンケート設問回答エンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "QuestionReply")
@Data
@Component
@Scope("prototype")
public class QuestionReplyEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケート回答SEQ (FK) */
    @Column(name = "questionnaireReplySeq")
    @Id
    private Integer questionnaireReplySeq;

    /** アンケート設問SEQ (FK) */
    @Column(name = "questionSeq")
    @Id
    private Integer questionSeq;

    /** 回答内容 */
    @Column(name = "reply")
    private String reply;

    /** 登録日時 */
    @Column(name = "registTime")
    private Timestamp registTime;

}