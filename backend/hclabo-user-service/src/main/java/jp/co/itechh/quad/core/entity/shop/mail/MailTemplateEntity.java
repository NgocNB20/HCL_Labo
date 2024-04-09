/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.mail;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateDefaultFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * メールテンプレートクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "MailTemplate")
@Data
@Component
@Scope("prototype")
public class MailTemplateEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** メールテンプレートSEQ（必須） */
    @Column(name = "mailTemplateSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "mailTemplateSeq")
    private Integer mailTemplateSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** メールテンプレート名（必須） */
    @Column(name = "mailTemplateName")
    private String mailTemplateName;

    /** メールテンプレート種別（必須） */
    @Column(name = "mailTemplateType")
    private HTypeMailTemplateType mailTemplateType;

    /** メールテンプレート標準フラグ（必須） */
    @Column(name = "mailTemplateDefaultFlag")
    private HTypeMailTemplateDefaultFlag mailTemplateDefaultFlag;

    /** メールテンプレート件名 */
    @Column(name = "mailTemplateSubject")
    private String mailTemplateSubject;

    /** メールテンプレートFROM */
    @Column(name = "mailTemplateFromAddress")
    private String mailTemplateFromAddress;

    /** メールテンプレートTO */
    @Column(name = "mailTemplateToAddress")
    private String mailTemplateToAddress;

    /** メールテンプレートCC */
    @Column(name = "mailTemplateCcAddress")
    private String mailTemplateCcAddress;

    /** メールテンプレートBCC */
    @Column(name = "mailTemplateBccAddress")
    private String mailTemplateBccAddress;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}