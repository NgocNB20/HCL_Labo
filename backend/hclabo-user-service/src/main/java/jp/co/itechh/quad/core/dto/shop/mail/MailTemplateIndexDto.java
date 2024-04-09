/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.mail;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateDefaultFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * メールテンプレート見出しDTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class MailTemplateIndexDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** メールテンプレートSEQ */
    private Integer mailTemplateSeq;

    /** メールテンプレート種別 */
    private HTypeMailTemplateType mailTemplateType;

    /** メールテンプレート名 */
    private String mailTemplateName;

    /** メールテンプレート標準フラグ */
    private HTypeMailTemplateDefaultFlag mailTemplateDefaultFlag;
}