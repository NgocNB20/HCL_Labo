/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.mailtemplate;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;

/**
 * メールテンプレート取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.2 $
 */
public interface MailTemplateGetLogic {

    // LIM0008

    /**
     * 指定されたメールテンプレート SEQ のエンティティを取得する。<br />
     *
     * @param shopSeq           ショップSEQ
     * @param mailTemplateSeq   メールテンプレートSEQ
     * @return 結果
     */
    MailTemplateEntity execute(Integer shopSeq, Integer mailTemplateSeq);

    /**
     * 指定されたメールテンプレート種別のエンティティを取得する。<br />
     * 標準のエンティティを優先で取得するが、
     * 標準が見つからない場合は、一般のエンティティを取得する。
     *
     * @param shopSeq           ショップSEQ
     * @param mailTemplateType  メールテンプレート種別
     * @return 結果
     */
    MailTemplateEntity execute(Integer shopSeq, HTypeMailTemplateType mailTemplateType);
}
