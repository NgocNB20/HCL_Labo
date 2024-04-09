/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.mailtemplate;

import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;

/**
 * メールテンプレートを取得するサービス
 *
 * @author tm27400
 * @version $Revision: 1.2 $
 *
 */
public interface MailTemplateGetService {

    /**
     * 実行
     *
     * @param tempSeq   tempSeq
     * @return 結果
     */
    MailTemplateEntity execute(Integer tempSeq);

}