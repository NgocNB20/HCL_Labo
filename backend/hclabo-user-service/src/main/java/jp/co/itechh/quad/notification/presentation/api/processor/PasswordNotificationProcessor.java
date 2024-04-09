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
import jp.co.itechh.quad.notificationsub.presentation.api.param.PasswordNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * パスワード再設定
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class PasswordNotificationProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordNotificationProcessor.class);

    /**
     * メール送信
     *
     * @param passwordNotificationRequest パスワード再設定メール送信リクエスト
     */
    public void processor(PasswordNotificationRequest passwordNotificationRequest) {

        // 確認メール情報取得
        ConfirmMailDao confirmMailDao = ApplicationContextUtility.getBean(ConfirmMailDao.class);
        ConfirmMailEntity confirmMailEntity = confirmMailDao.getEntity(passwordNotificationRequest.getConfirmMailSeq());

        // 送信に使用するメールテンプレートを取得する

        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
        MailTemplateEntity mailTemplateEntity = mailTemplateGetLogic.execute(confirmMailEntity.getShopSeq(),
                                                                             HTypeMailTemplateType.PASSWORD_NOTIFICATION
                                                                            );

        // テンプレートがない場合
        if (mailTemplateEntity == null) {
            return;
        }

        // 送信先取得
        List<String> toList = Collections.singletonList(confirmMailEntity.getConfirmMail());

        // メール用値マップの作成
        Transformer transformer = ApplicationContextUtility.getBean(StringTransformer.class);
        Map<String, String> mailContentsMap = transformer.toValueMap(confirmMailEntity.getConfirmMailPassword());

        // メールDto作成
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.PASSWORD_NOTIFICATION, mailTemplateEntity,
                                                    toList, mailContentsMap
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