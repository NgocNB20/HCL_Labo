package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.shop.mail.MailTemplateDao;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.OrderTransformer;
import jp.co.itechh.quad.core.logic.order.OrderReceivedGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 受注決済督促
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class SettlementReminderProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentNotificationProcessor.class);

    /**
     * メール送信
     *
     * @param request 受注決済督促リクエスト
     */
    public void processor(SettlementReminderRequest request) {

        // 引数チェック
        checkParameter(request.getOrderCodeList());

        // 管理画面からのテスト送信のみかつ、受注番号が1つのみの場合
        if (request.getIsSendTestOnly() && StringUtils.isNotBlank(request.getTestMailAddress())
            && request.getOrderCodeList().size() == 1) {
            sendMailForTest(request.getTestMailAddress(), request.getOrderCodeList().get(0));
        }

        // 管理画面からの注文主＋管理者への送信かつ、受注番号が1つのみの場合
        else if (request.getIsSendAdmin() && StringUtils.isNotBlank(request.getTestMailAddress())
                 && request.getOrderCodeList().size() == 1) {
            sendMailForTargetAndAdmin(request.getTestMailAddress(), request.getOrderCodeList().get(0));
        }

        // バッチ用
        else {
            OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
            OrderTransformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            for (String target : request.getOrderCodeList()) {

                // 受注取得
                OrderReceivedDto dto = logic.execute(target);

                MailDto mailDto = createMailDtoCommon();

                Map<String, Object> mailContentsMap = new HashMap<>();

                mailDto.setToList(Collections.singletonList(
                                dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoMail()));

                // メール用値マップの作成
                mailContentsMap.put("mailContentsMap", transformer.toValueMap(dto));

                mailDto.setMailContentsMap(mailContentsMap);

                try {
                    mailSendService.execute(mailDto);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    throw e;
                }

            }
        }
    }

    /**
     * 管理画面からのテスト送信のみの実行メソッド
     *
     * @param testMailAddress テスト用送信先メールアドレス
     * @param orderCode       受注番号
     */
    private void sendMailForTest(String testMailAddress, String orderCode) {

        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        OrderTransformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

        // 受注取得
        OrderReceivedDto dto = logic.execute(orderCode);

        MailDto mailDto = createMailDtoCommon();

        Map<String, Object> mailContentsMap = new HashMap<>();

        mailDto.setToList(Collections.singletonList(testMailAddress));

        // メール用値マップの作成
        Map<String, String> tmp = transformer.toValueMap(dto);
        mailContentsMap.put("mailContentsMap", tmp);

        mailDto.setMailContentsMap(mailContentsMap);

        try {
            // メール送信リストに設定
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 管理画面からの注文主＋管理者への送信実行メソッド
     *
     * @param adminMailAddress 管理者用送信先メールアドレス
     * @param orderCode        受注番号
     */
    private void sendMailForTargetAndAdmin(String adminMailAddress, String orderCode) {

        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        OrderTransformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

        // 受注取得
        OrderReceivedDto dto = logic.execute(orderCode);

        MailDto mailDto = createMailDtoCommon();

        Map<String, Object> mailContentsMap = new HashMap<>();

        mailDto.setToList(Collections.singletonList(
                        dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoMail()));

        // メール用値マップの作成
        Map<String, String> tmp = transformer.toValueMap(dto);
        mailContentsMap.put("mailContentsMap", tmp);

        mailDto.setMailContentsMap(mailContentsMap);

        try {
            // メール送信リストに設定
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        // 管理者へのテストメールを送信
        MailDto mailDtoTest = new MailDto();
        mailDtoTest.setMailTemplateType(mailDto.getMailTemplateType());
        mailDtoTest.setSubject(mailDto.getSubject());
        mailDtoTest.setFrom(mailDto.getFrom());
        mailDtoTest.setToList(Collections.singletonList(adminMailAddress));
        mailDtoTest.setMailContentsMap(mailDto.getMailContentsMap());

        try {
            mailSendService.execute(mailDtoTest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 督促メール受信者DTO を生成する
     *
     * @return メール受信者 DTO
     */
    protected MailDto createMailDtoCommon() {

        // 注文者情報のメールアドレスを使用する
        MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
        mailDto.setMailTemplateType(HTypeMailTemplateType.SETTLEMENT_REMINDER);

        MailTemplateDao mailTemplateDao = ApplicationContextUtility.getBean(MailTemplateDao.class);
        MailTemplateEntity entity = mailTemplateDao.getEntityByType(1001, HTypeMailTemplateType.SETTLEMENT_REMINDER);
        mailDto.setSubject(entity.getMailTemplateSubject());
        mailDto.setFrom(entity.getMailTemplateFromAddress());
        if (entity.getMailTemplateCcAddress() != null) {
            mailDto.setCcList(Collections.singletonList(entity.getMailTemplateCcAddress()));
        }
        if (entity.getMailTemplateBccAddress() != null) {
            mailDto.setBccList(Collections.singletonList(entity.getMailTemplateBccAddress()));
        }

        return mailDto;
    }

    /**
     * 引数チェック
     *
     * @param orderCodeList 受注番号リスト
     */
    protected void checkParameter(List<String> orderCodeList) {
        ArgumentCheckUtil.assertNotNull("OrderCodeList", orderCodeList);
    }

}