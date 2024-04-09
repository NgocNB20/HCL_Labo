package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メールUtility<br/>
 *
 * @author Doan Thang (VTI Japan Co. Ltd)
 *
 */
@Component
public class MailUtility {

    /**
     * MailDto作成
     *
     * @param mailTemplateType メールテンプレートタイプ
     * @param entity メールテンプレートEntity
     * @param toList 送信宛先リスト
     * @param mailContents メールコンテンツ
     * @return MailDto
     */
    public MailDto createMailDto(HTypeMailTemplateType mailTemplateType,
                                 MailTemplateEntity entity,
                                 List<String> toList,
                                 Map<String, String> mailContents) {
        MailDto mailDto = new MailDto();

        mailDto.setMailTemplateType(mailTemplateType);
        mailDto.setSubject(entity.getMailTemplateSubject());
        mailDto.setFrom(entity.getMailTemplateFromAddress());

        // デフォルト BCC の取得
        if (entity.getMailTemplateBccAddress() != null) {
            mailDto.setBccList(Collections.singletonList(entity.getMailTemplateBccAddress()));
        }

        // デフォルト CC の取得
        if (entity.getMailTemplateCcAddress() != null) {
            mailDto.setCcList(Collections.singletonList(entity.getMailTemplateCcAddress()));
        }

        mailDto.setToList(toList);

        // メール用値マップの作成
        Map<String, Object> mailContentsMap = new HashMap<>();
        mailContentsMap.put("mailContentsMap", mailContents);

        mailDto.setMailContentsMap(mailContentsMap);

        return mailDto;
    }
}