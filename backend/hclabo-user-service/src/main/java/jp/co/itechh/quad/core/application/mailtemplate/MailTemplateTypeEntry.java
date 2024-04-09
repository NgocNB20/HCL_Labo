/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.application.mailtemplate;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 使用可能なメールテンプレートタイプ登録
 *
 * @author tm27400
 * @version $Revision: 1.1 $
 *
 */
@Data
@Component
@Scope("prototype")
public class MailTemplateTypeEntry implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** テンプレートタイプ */
    private HTypeMailTemplateType templateType;

    /** オブジェクトから値マップへの変換に使用するトランスフォーマ */
    private Transformer transformer;

    /** 並び順　*/
    private Integer order;
}