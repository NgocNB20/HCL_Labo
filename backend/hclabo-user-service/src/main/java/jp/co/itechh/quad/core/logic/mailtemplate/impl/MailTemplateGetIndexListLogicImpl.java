/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.mailtemplate.impl;

import jp.co.itechh.quad.core.application.mailtemplate.MailTemplateTypeEntry;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateDefaultFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.shop.mail.MailTemplateDao;
import jp.co.itechh.quad.core.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetIndexListLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * メールテンプレート見出し一覧を取得するロジック
 *
 * @author tm27400
 * @version $Revision: 1.4 $
 *
 */
@Component
public class MailTemplateGetIndexListLogicImpl extends AbstractShopLogic implements MailTemplateGetIndexListLogic {

    /** dao */
    private final MailTemplateDao mailTemplateDao;

    @Autowired
    public MailTemplateGetIndexListLogicImpl(MailTemplateDao mailTemplateDao) {
        this.mailTemplateDao = mailTemplateDao;
    }

    /**
     * 実行
     *
     * @param shopSeq   shopSeq
     * @return 結果
     */
    @Override
    public List<MailTemplateIndexDto> execute(Integer shopSeq) {
        List<MailTemplateIndexDto> returnList = new ArrayList<>();

        // DBに登録されたメールテンプレート
        List<MailTemplateIndexDto> indexList = this.mailTemplateDao.selectIndexList(shopSeq);

        // ソート用マップ
        final Map<HTypeMailTemplateType, Integer> sortMap = new LinkedHashMap<>();

        // mailTemplateType でフィルタリングする
        List<MailTemplateTypeEntry> mailTemplateTypeList =
                        (List<MailTemplateTypeEntry>) ApplicationContextUtility.getApplicationContext()
                                                                               .getBean("mailTemplateTypeEntryList");
        for (MailTemplateTypeEntry entry : mailTemplateTypeList) {

            HTypeMailTemplateType type = entry.getTemplateType();
            sortMap.put(type, entry.getOrder());

            boolean found = false;

            // mailTemplateTypeEntryList にも DB にもある場合
            for (MailTemplateIndexDto dto : indexList) {
                if (dto.getMailTemplateType() == type) {
                    found = true;
                    returnList.add(dto);
                }
            }

            // mailTemplateTypeEntryList にはあるが、DBにはない場合
            if (!found) {
                MailTemplateIndexDto dto = ApplicationContextUtility.getBean(MailTemplateIndexDto.class);
                dto.setMailTemplateType(type);
                dto.setMailTemplateName("");
                dto.setMailTemplateDefaultFlag(HTypeMailTemplateDefaultFlag.ON);
                dto.setMailTemplateSeq(-1);
                returnList.add(dto);
            }

        }

        //
        // ソートする
        //

        returnList.sort(new Comparator<>() {

            /**
             * 比較する
             *
             * @param o1 MailTemplateIndexDto
             * @param o2 MailTemplateIndexDto
             * @return 比較結果
             */
            @Override
            public int compare(MailTemplateIndexDto o1, MailTemplateIndexDto o2) {

                //
                // テンプレート種別で判定
                //

                Integer order1 = sortMap.get(o1.getMailTemplateType());
                Integer order2 = sortMap.get(o2.getMailTemplateType());

                // mailTemplateTypeEntryList で設定された表示順
                if (!order1.equals(order2)) {
                    return order1.compareTo(order2);
                }

                //
                // 標準フラグで判定
                //

                HTypeMailTemplateDefaultFlag flag1 = o1.getMailTemplateDefaultFlag();
                HTypeMailTemplateDefaultFlag flag2 = o2.getMailTemplateDefaultFlag();

                if (flag1 != flag2) {
                    if (flag1 == HTypeMailTemplateDefaultFlag.ON) {
                        return -1;
                    }

                    return 1;
                }

                //
                // シーケンスで判定
                //

                return o1.getMailTemplateSeq().compareTo(o2.getMailTemplateSeq());

            }
        });

        return returnList;
    }
}