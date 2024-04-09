/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.mail;

import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateDefaultFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class MailTemplateEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** メールテンプレートSEQ（必須） */
    private Integer mailTemplateSeq;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** メールテンプレート名（必須） */
    private String mailTemplateName;

    /** メールテンプレート種別（必須） */
    private HTypeMailTemplateType mailTemplateType;

    /** メールテンプレート標準フラグ（必須） */
    private HTypeMailTemplateDefaultFlag mailTemplateDefaultFlag;

    /** メールテンプレート件名 */
    private String mailTemplateSubject;

    /** メールテンプレートFROM */
    private String mailTemplateFromAddress;

    /** メールテンプレートTO */
    private String mailTemplateToAddress;

    /** メールテンプレートCC */
    private String mailTemplateCcAddress;

    /** メールテンプレートBCC */
    private String mailTemplateBccAddress;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

}