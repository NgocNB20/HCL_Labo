package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.MemberInfoTransformer;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberinfoProcessCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 会員処理完了メール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class MemberInfoProcessCompleteProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoProcessCompleteProcessor.class);

    /**
     * メール送信
     *
     * @param memberinfoProcessCompleteRequest 会員処理完了メール送信リクエスト
     */
    public void processor(MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest) {
        LOGGER.info("【Subscribe】ルーティングキー： user-memberinfo-process-complete-routing");

        // 会員情報取得
        MemberInfoGetLogic memberInfoGetLogic = ApplicationContextUtility.getBean(MemberInfoGetLogic.class);
        MemberInfoEntity memberInfoEntity =
                        memberInfoGetLogic.execute(memberinfoProcessCompleteRequest.getMemberInfoSeq());
        if (memberInfoEntity == null) {
            return;
        }

        // 送信に使用するメールテンプレートを取得する
        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);

        HTypeMailTemplateType mailTemplateType = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                                               memberinfoProcessCompleteRequest.getMailTemplateType()
                                                                              );
        MailTemplateEntity entity = mailTemplateGetLogic.execute(1001, mailTemplateType);

        // テンプレートがない場合
        if (entity == null) {
            return;
        }

        // 送信先取得
        List<String> toList = Collections.singletonList(memberInfoEntity.getMemberInfoMail());

        // メール用値マップの作成
        Transformer transformer = ApplicationContextUtility.getBean(MemberInfoTransformer.class);
        Map<String, String> mailContentsMap = transformer.toValueMap(memberInfoEntity);

        // メールDto作成
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(mailTemplateType, entity, toList, mailContentsMap);

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