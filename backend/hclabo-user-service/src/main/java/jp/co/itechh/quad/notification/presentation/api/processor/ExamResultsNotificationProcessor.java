/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.order.OrderReceivedGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.transformer.mailtemplate.OrderTransformer;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsNoticeRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 検査結果通知メール Processor
 *
 */
@Component
@Scope("prototype")
public class ExamResultsNotificationProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsNotificationProcessor.class);

    /**
     * メール送信
     *
     * @param examResultsNoticeRequest 検査結果通知メール送信リクエスト
     */
    public void processor(ExamResultsNoticeRequest examResultsNoticeRequest) {

        try {

            // ショップSEQ取得
            final Integer shopSeq = 1001;

            // メールテンプレートを取得
            MailTemplateGetLogic mailTemplateGetLogic = ApplicationContextUtility.getBean(MailTemplateGetLogic.class);
            MailTemplateEntity entity =
                            mailTemplateGetLogic.execute(shopSeq, HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION);

            if (entity == null) {
                LOGGER.error("検査結果通知のメールテンプレートが取得できません。");
                LOGGER.error("検査結果通知メールの送信は行いません。");
            }

            // 検査結果通知メール送信
            sendMail(examResultsNoticeRequest, entity);
        } catch (Exception e) {
            LOGGER.error("エラー -- 検査結果通知メールの送信に失敗しました。");
            LOGGER.error("情報 -- " + e.getMessage() + " -- ", e);
            throw e;
        }
    }

    /**
     * メールDTOリスト設定<br/>
     *
     * @param request 受注情報を取得メール送信リクエスト
     * @param entity  メールテンプレート
     */
    protected void sendMail(ExamResultsNoticeRequest request, MailTemplateEntity entity) {

        // 引数チェック
        checkParameter(request.getOrderCodeList());

        // 管理画面からのテスト送信のみかつ、受注番号が1つのみの場合
        if (request.getIsSendTestOnly() && StringUtils.isNotBlank(request.getTestMailAddress())
            && request.getOrderCodeList().size() == 1) {
            sendMailForTest(request.getTestMailAddress(), request.getOrderCodeList().get(0), entity);
        }

        // 管理画面からの注文主＋管理者への送信かつ、受注番号が1つのみの場合
        else if (request.getIsSendAdmin() && StringUtils.isNotBlank(request.getTestMailAddress())
                 && request.getOrderCodeList().size() == 1) {
            sendMailForTargetAndAdmin(request.getTestMailAddress(), request.getOrderCodeList().get(0), entity);
        }

        // バッチ用
        else {
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
            Transformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
            MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);

            for (String orderCode : request.getOrderCodeList()) {

                if (StringUtil.isEmpty(orderCode)) {
                    continue;
                }

                // 受注情報を取得
                OrderReceivedDto dto = logic.execute(orderCode);

                if (ObjectUtils.isEmpty(dto)) {
                    LOGGER.error("受注番号：" + orderCode + " の受注情報が取得できません。");
                    LOGGER.error("受注番号：" + orderCode + " の受注情報を取得メールの送信は行いません。");
                    continue;
                }

                // 注文主情報を取得
                MemberInfoEntity memberInfoEntity = dto.getMemberInfoDetailsDto().getMemberInfoEntity();

                if (ObjectUtils.isEmpty(memberInfoEntity)) {
                    LOGGER.error("受注番号：" + orderCode + " の注文主情報が取得できません。");
                    LOGGER.error("受注番号：" + orderCode + " の受注情報を取得メールの送信は行いません。");
                    continue;
                }

                // 送信先を設定
                entity.setMailTemplateToAddress(memberInfoEntity.getMemberInfoMail());
                List<String> toList = Collections.singletonList(memberInfoEntity.getMemberInfoMail());
                // メール用値マップの作成
                Map<String, String> mailContents = transformer.toValueMap(dto);

                // １メール分の送信情報
                MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION, entity, toList,
                                                            mailContents
                                                           );

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
     * @param entity          メールテンプレート
     */
    private void sendMailForTest(String testMailAddress, String orderCode, MailTemplateEntity entity) {

        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        Transformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);

        // 受注情報を取得
        OrderReceivedDto dto = logic.execute(orderCode);

        if (ObjectUtils.isEmpty(dto)) {
            LOGGER.error("受注番号：" + orderCode + " の受注情報が取得できません。");
            LOGGER.error("受注番号：" + orderCode + " の受注情報を取得メールの送信は行いません。");
        }

        // 送信先を設定
        entity.setMailTemplateToAddress(testMailAddress);
        List<String> toList = Collections.singletonList(testMailAddress);
        // メール用値マップの作成
        Map<String, String> mailContents = transformer.toValueMap(dto);

        // １メール分の送信情報
        MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION, entity, toList,
                                                    mailContents
                                                   );

        try {
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
     * @param entity           メールテンプレート
     */
    private void sendMailForTargetAndAdmin(String adminMailAddress, String orderCode, MailTemplateEntity entity) {

        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        Transformer transformer = ApplicationContextUtility.getBean(OrderTransformer.class);
        MailUtility mailUtility = ApplicationContextUtility.getBean(MailUtility.class);

        // 受注情報を取得
        OrderReceivedDto dto = logic.execute(orderCode);

        if (ObjectUtils.isEmpty(dto)) {
            LOGGER.error("受注番号：" + orderCode + " の受注情報が取得できません。");
            LOGGER.error("受注番号：" + orderCode + " の受注情報を取得メールの送信は行いません。");
        }

        // 注文主情報を取得
        MemberInfoEntity memberInfoEntity = dto.getMemberInfoDetailsDto().getMemberInfoEntity();

        if (ObjectUtils.isEmpty(memberInfoEntity)) {
            LOGGER.error("受注番号：" + orderCode + " の注文主情報が取得できません。");
            LOGGER.error("受注番号：" + orderCode + " の受注情報を取得メールの送信は行いません。");
        }

        // 送信先を設定
        entity.setMailTemplateToAddress(memberInfoEntity.getMemberInfoMail());
        List<String> toList = Collections.singletonList(memberInfoEntity.getMemberInfoMail());
        // メール用値マップの作成
        Map<String, String> mailContents = transformer.toValueMap(dto);

        // １メール分の送信情報
        MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION, entity, toList,
                                                    mailContents
                                                   );

        try {
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
     * 引数チェック
     *
     * @param orderCodeList 受注番号リスト
     */
    protected void checkParameter(List<String> orderCodeList) {
        ArgumentCheckUtil.assertNotNull("OrderCodeList", orderCodeList);
    }

}