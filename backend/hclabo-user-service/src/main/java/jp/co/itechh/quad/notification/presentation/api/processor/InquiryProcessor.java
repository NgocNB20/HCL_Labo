package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.InquiryTransformer;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGetService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.InquiryRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 問い合わせメール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class InquiryProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryProcessor.class);

    /**
     * メール送信
     *
     * @param inquiryRequest 問い合わせメール送信リクエスト
     */
    public void processor(InquiryRequest inquiryRequest) throws Exception {
        LOGGER.info("【Subscribe】ルーティングキー： user-inquiry-routing");

        InquiryDetailsDto inquiryDetailsDto = null;
        HTypeMailTemplateType hTypeMailTemplateType = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                                                    inquiryRequest.getMailTemplateType()
                                                                                   );

        if (StringUtils.isNotEmpty(inquiryRequest.getInquiryCode())) {

            // 問い合わせ情報取得
            InquiryGetLogic inquiryGetLogic = ApplicationContextUtility.getBean(InquiryGetLogic.class);
            inquiryDetailsDto = inquiryGetLogic.execute(inquiryRequest.getInquiryCode());

        } else if (inquiryRequest.getInquirySeq() != null) {

            // 問い合わせ情報取得
            InquiryGetService inquiryGetService = ApplicationContextUtility.getBean(InquiryGetService.class);
            inquiryDetailsDto = inquiryGetService.execute(inquiryRequest.getInquirySeq());

        }

        if (!ObjectUtils.isEmpty(inquiryDetailsDto)) {
            executeSendMail(hTypeMailTemplateType, inquiryDetailsDto);
        }

        LOGGER.info("問い合わせメールを送信しました。");
    }

    /**
     *
     * @param mailTemplateType メールテンプレートタイプ
     * @param inquiryDetailsDto 問い合わせ詳細Dto
     */
    private void executeSendMail(HTypeMailTemplateType mailTemplateType, InquiryDetailsDto inquiryDetailsDto) {

        // 送信に使用するメールテンプレートを取得する
        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
        MailTemplateEntity entity = mailTemplateGetLogic.execute(1001, mailTemplateType);

        // テンプレートがない場合
        if (entity == null) {
            return;
        }

        // 送信先取得
        List<String> toList = Collections.singletonList(inquiryDetailsDto.getInquiryEntity().getInquiryMail());

        // メール用値マップの作成
        Transformer transformer = ApplicationContextUtility.getBean(InquiryTransformer.class);
        Map<String, String> mailContentsMap = transformer.toValueMap(inquiryDetailsDto);

        // メールDto作成
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(mailTemplateType, entity, toList, mailContentsMap);

        // メール送信
        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

        try {
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}