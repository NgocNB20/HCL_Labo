package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.memberinfo.confirmmail.ConfirmMailDao;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.StringTransformer;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberPreregistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 仮会員登録
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MemberPreregistrationProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberPreregistrationProcessor.class);

    /**
     * メール送信
     *
     * @param memberPreregistrationRequest 仮会員登録リクエスト
     */
    public void processor(MemberPreregistrationRequest memberPreregistrationRequest) {
        // 確認メール情報取得
        ConfirmMailDao confirmMailDao = ApplicationContextUtility.getBean(ConfirmMailDao.class);
        ConfirmMailEntity confirmMailEntity =
                        confirmMailDao.getEntity(memberPreregistrationRequest.getConfirmMailSeq());
        // URL
        String url = confirmMailEntity.getConfirmMailPassword();

        // 送信に使用するメールテンプレートを取得する
        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
        MailTemplateEntity entity = mailTemplateGetLogic.execute(confirmMailEntity.getShopSeq(),
                                                                 HTypeMailTemplateType.MEMBER_PREREGISTRATION
                                                                );

        // 送信先取得
        List<String> toList = Collections.singletonList(confirmMailEntity.getConfirmMail());

        // メール用値マップの作成
        Transformer transformer = ApplicationContextUtility.getBean(StringTransformer.class);
        Map<String, String> mailContentsMap = transformer.toValueMap(url);

        // メールDto作成
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.MEMBER_PREREGISTRATION, entity, toList,
                                                    mailContentsMap
                                                   );

        // メール送信
        try {
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

    }
}