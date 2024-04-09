package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.OrderTransformer;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.order.OrderReceivedGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderConfirmationRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 注文確認
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class OrderConfirmationProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConfirmationProcessor.class);

    /**
     * メール送信
     *
     * @param request 注文確認リクエスト
     */
    public void processor(OrderConfirmationRequest request) {

        // 引数チェック
        checkParameter(request.getOrderCode());

        // 受注取得
        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        OrderReceivedDto dto = logic.execute(request.getOrderCode());

        // 送信先アドレスを設定
        String targetMailAddress = null;
        // 管理画面からのテスト送信のみの場合
        if (request.getIsSendTestOnly() && StringUtils.isNotBlank(request.getTestMailAddress())) {
            targetMailAddress = request.getTestMailAddress();
        } else {
            targetMailAddress = dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoMail();
        }

        // 送信に使用するメールテンプレートを取得する
        MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
        MailTemplateEntity entity = mailTemplateGetLogic.execute(1001, HTypeMailTemplateType.ORDER_CONFIRMATION);

        // 送信先取得
        List<String> toList = Collections.singletonList(targetMailAddress);

        // メール用値マップの作成
        Transformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
        Map<String, String> mailContentsMap = transformer.toValueMap(dto);

        // メールDto作成
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);
        MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.ORDER_CONFIRMATION, entity, toList,
                                                    mailContentsMap
                                                   );

        // メール送信
        try {
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            // 管理者にも送信する場合
            if (request.getIsSendAdmin() && StringUtils.isNotBlank(request.getTestMailAddress())) {
                MailDto mailDtoTest = new MailDto();
                mailDtoTest.setMailTemplateType(mailDto.getMailTemplateType());
                mailDtoTest.setSubject(mailDto.getSubject());
                mailDtoTest.setFrom(mailDto.getFrom());
                mailDtoTest.setToList(Collections.singletonList(request.getTestMailAddress()));
                mailDtoTest.setMailContentsMap(mailDto.getMailContentsMap());
                mailSendService.execute(mailDtoTest);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 引数チェック
     *
     * @param orderCode 受注コード
     */
    protected void checkParameter(String orderCode) {
        ArgumentCheckUtil.assertNotNull("orderCode", orderCode);
    }

}