package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CertificationCodeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 認証コードメール
 */
@Component
@Scope("prototype")
public class CertificationCodeProcessor {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationCodeProcessor.class);

    /**
     * メール送信
     *
     * @param certificationCodeRequest 認証コードメール送信
     */
    public void processor(CertificationCodeRequest certificationCodeRequest) {
        // メール送信
        try {

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();
            MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);

            // 会員情報取得
            MemberInfoDao memberInfoDao = ApplicationContextUtility.getBean(MemberInfoDao.class);
            MemberInfoEntity memberInfoEntity = memberInfoDao.getEntity(certificationCodeRequest.getMemberInfoSeq());

            HTypeMailTemplateType mailTemplateType = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                certificationCodeRequest.getMailTemplateType());

            // メールテンプレートを取得
            MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
            MailTemplateEntity mailTemplateEntity =
                mailTemplateGetLogic.execute(memberInfoEntity.getShopSeq(), mailTemplateType);

            // 送信先取得
            List<String> toList = Collections.singletonList(memberInfoEntity.getMemberInfoMail());

            valueMap.put("O_FIRST_NAME", memberInfoEntity.getMemberInfoFirstName());
            valueMap.put("O_LAST_NAME", memberInfoEntity.getMemberInfoLastName());
            valueMap.put("CERTIFICATION_CODE", certificationCodeRequest.getCertificationCode());
            mailContentsMap.put("mailContentsMap", valueMap);

            // １メール分の送信情報
            MailDto mailDto = mailUtility.createMailDto(mailTemplateType, mailTemplateEntity, toList, valueMap);

            // メール送信
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

    }
}