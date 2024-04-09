/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.questionnaire;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * アンケート設問回答エンティティクラス。<br />
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class QuestionReplyEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** アンケート回答SEQ (FK) */
    private Integer questionnaireReplySeq;

    /** アンケート設問SEQ (FK) */
    private Integer questionSeq;

    /** 回答内容 */
    private String reply;

    /** 登録日時 */
    private Timestamp registTime;

}