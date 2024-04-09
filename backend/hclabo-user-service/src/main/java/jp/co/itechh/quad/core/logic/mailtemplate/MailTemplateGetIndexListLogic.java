/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.mailtemplate;

import jp.co.itechh.quad.core.dto.shop.mail.MailTemplateIndexDto;

import java.util.List;

/**
 * テンプレート見出し一覧を取得するロジック
 *
 * @author tm27400
 * @version $Revision: 1.2 $
 *
 */
public interface MailTemplateGetIndexListLogic {

    /**
     * 実行
     *
     * @param shopSeq   shopSeq
     * @return 結果
     */
    List<MailTemplateIndexDto> execute(Integer shopSeq);

}