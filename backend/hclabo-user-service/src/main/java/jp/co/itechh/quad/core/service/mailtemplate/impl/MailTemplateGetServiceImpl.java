/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.mailtemplate.impl;

import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mailtemplate.MailTemplateGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MailTemplateGetServiceの実装
 *
 * @author tm27400
 * @version $Revision: 1.3 $
 *
 */
@Service
public class MailTemplateGetServiceImpl extends AbstractShopService implements MailTemplateGetService {

    /** テンプレートを取得するロジック */
    private final MailTemplateGetLogic mailTemplateGetLogic;

    @Autowired
    public MailTemplateGetServiceImpl(MailTemplateGetLogic mailTemplateGetLogic) {
        this.mailTemplateGetLogic = mailTemplateGetLogic;
    }

    /**
     * 実行
     *
     * @param tempSeq   tempSeq
     * @return 結果
     */
    @Override
    public MailTemplateEntity execute(Integer tempSeq) {
        Integer shopSeq = 1001;
        return this.mailTemplateGetLogic.execute(shopSeq, tempSeq);
    }

}