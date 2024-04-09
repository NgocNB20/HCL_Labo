package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メルマガ登録完了
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MailMagazineProcessCompleteProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailMagazineProcessCompleteProcessor.class);

    /** 送信アドレス（設定値） */
    protected static final String MAIL_FROM_ADDRESS = "mail.from.address";

    /**
     * メール送信
     *
     * @param mailMagazineProcessCompleteRequest メルマガ登録完了リクエスト
     */
    public void processor(MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("mail", mailMagazineProcessCompleteRequest.getMail());
        ArgumentCheckUtil.assertNotNull("mailTemplateType", mailMagazineProcessCompleteRequest.getMailTemplateType());

        // メルマガ購読者取得
        MailMagazineMemberGetLogic mailMagazineMemberGetLogic =
                        ApplicationContextUtility.getBean(MailMagazineMemberGetLogic.class);
        MailMagazineMemberEntity mailMagazineMemberEntity =
                        mailMagazineMemberGetLogic.execute(mailMagazineProcessCompleteRequest.getMemberInfoSeq());

        // 送信に使用するメールテンプレートを取得する
        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
        MailTemplateEntity entity = mailTemplateGetLogic.execute(mailMagazineMemberEntity.getShopSeq(),
                                                                 EnumTypeUtil.getEnumFromValue(
                                                                                 HTypeMailTemplateType.class,
                                                                                 mailMagazineProcessCompleteRequest.getMailTemplateType()
                                                                                              )
                                                                );

        // 送信先取得
        List<String> toList = Collections.singletonList(mailMagazineProcessCompleteRequest.getMail());

        // メール送信
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(
                        EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                      mailMagazineProcessCompleteRequest.getMailTemplateType()
                                                     ), entity, toList, null);

        // メール作成
        createMail(mailDto, mailMagazineProcessCompleteRequest.getMail(),
                   mailMagazineProcessCompleteRequest.getPreMail()
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

    /**
     * 送信メール作成
     *
     * @param mailDto
     * @param mail
     * @param preMail
     */
    private void createMail(MailDto mailDto, String mail, String preMail) {

        // 送信者メールアドレス作成
        String from = PropertiesUtil.getSystemPropertiesValue(MAIL_FROM_ADDRESS);

        // プレースホルダ作成
        Map<String, Object> ph = new HashMap<>();

        if (StringUtils.isEmpty(preMail)) {
            // 対象メールアドレスをセット
            ph.put("MA_MAIL", mail);
        } else {
            // 変更前のメールアドレスを本文内容セット（解除送信用）
            ph.put("MA_MAIL", preMail);
        }

        mailDto.initializeMailDto(mailDto.getMailTemplateType(), mailDto.getSubject(), from, mailDto.getToList(), null,
                                  null, ph, null
                                 );
    }
}