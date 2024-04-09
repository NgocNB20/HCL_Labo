/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.mail.impl;

import jp.co.itechh.quad.core.config.thymeleaf.CustomDialect;
import jp.co.itechh.quad.core.dto.mail.AttachFileDto;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 機能概要：新メール送信サービス（同期送信）
 * 作成日：2021/03/30
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Service
public class MailSendServiceImpl implements MailSendService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendServiceImpl.class);

    /** 例外ログ */
    private static final Logger EXCEPT_LOG = LoggerFactory.getLogger("EXCEPT");

    private final JavaMailSender javaMailSender;
    private final ClassLoaderTemplateResolver mailTemplateResolver;
    private final Environment environment;
    private static final String hostUrl = "hostUrl";
    private static final String shopName = "shopName";
    private static final String shopInfoMail = "shopInfoMail";

    @Autowired
    public MailSendServiceImpl(JavaMailSender javaMailSender,
                               ClassLoaderTemplateResolver mailTemplateResolver,
                               Environment environment) {
        this.javaMailSender = javaMailSender;
        this.mailTemplateResolver = mailTemplateResolver;
        this.environment = environment;
    }

    /**
     * 単一MailDtoを依頼する場合
     *
     * @param mailDto
     * @return　成功時...true 失敗時...false
     */
    @Override
    public boolean execute(MailDto mailDto) {
        try {
            this.sendSingleMailDto(mailDto);
            return true;
        } catch (Exception e) {
            // sendSingleMailDtoの処理で発生したthrowは全てここでキャッチし、ログ出力
            List<String> errorAddress = mailDto.getToList();
            LOGGER.error("次のメールアドレスに" + mailDto.getMailTemplateType().getLabel() + "メールの送信を失敗しました。" + errorAddress, e);

            EXCEPT_LOG.error("次のメールアドレスに" + mailDto.getMailTemplateType().getLabel() + "メールの送信を失敗しました。" + errorAddress,
                             e
                            );
            return false;
        }
    }

    /**
     * 複数MailDtoを依頼する場合
     *
     * @param mailDtoList メールDTOリスト
     * @return 全て成功...ture 失敗時...false
     */
    @Override
    public boolean execute(List<MailDto> mailDtoList) {

        if (mailDtoList.size() >= 10) {
            LOGGER.warn("同期メール送信の最大件数が10件までとなるため、超過される場合は、非同期メール送信バッチに依頼してください。");

            EXCEPT_LOG.warn("同期メール送信の最大件数が10件までとなるため、超過される場合は、非同期メール送信バッチに依頼してください。");

            return false;
        }

        int successCnt = 0;

        for (MailDto mailDto : mailDtoList) {
            try {
                this.sendSingleMailDto(mailDto);
                successCnt++;
            } catch (Exception e) {
                // sendSingleMailDtoの処理で発生したthrowは全てここでキャッチし、ログ出力
                List<String> errorAddress = mailDto.getToList();

                LOGGER.error(
                                "次のメールアドレスに" + mailDto.getMailTemplateType().getLabel() + "メールの送信を失敗しました。"
                                + errorAddress, e);

                EXCEPT_LOG.error("次のメールアドレスに" + mailDto.getMailTemplateType().getLabel() + "メールの送信を失敗しました。"
                                 + errorAddress, e);
            }
        }

        // 結果チェック
        if (successCnt == mailDtoList.size()) {
            return true;
        } else {
            // 失敗件数をログに出力
            int errorCnt = mailDtoList.size() - successCnt;

            LOGGER.error("対象送信数" + mailDtoList.size() + "件中のうち" + String.valueOf(errorCnt) + "件送信失敗");

            EXCEPT_LOG.error("対象送信数" + mailDtoList.size() + "件中のうち" + String.valueOf(errorCnt) + "件送信失敗");

            return false;
        }
    }

    /**
     * 単一MailDtoを送信するロジック
     *
     * @param mailDto
     * @throws MessagingException
     */
    private void sendSingleMailDto(MailDto mailDto) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper =
                        new MimeMessageHelper(mimeMessage, mailDto.getAttachFileFlag(), StandardCharsets.UTF_8.name());

        // メール本文用のThymeleafコンテキスト準備
        Context context = new Context();
        for (Map.Entry<String, Object> entry : mailDto.getMailContentsMap().entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        // サイトURLを設定
        context.setVariable(hostUrl, environment.getProperty("web.site.url"));

        // ショップ名を設定
        context.setVariable(shopName, environment.getProperty("shop.name"));

        // ショップInfoメールアドレスを設定
        context.setVariable(shopInfoMail, environment.getProperty("shop.info.mail"));

        helper.setSubject(mailDto.getSubject());
        helper.setFrom(mailDto.getFrom());
        helper.setTo(mailDto.getToList().toArray(String[]::new));
        if (mailDto.getCcList() != null) {
            helper.setCc(mailDto.getCcList().toArray(String[]::new));
        }
        if (mailDto.getBccList() != null) {
            helper.setBcc(mailDto.getBccList().toArray(String[]::new));
        }

        // ファイル添付する場合はMimeMessageHelperにリソースパスを渡す
        if (mailDto.getAttachFileFlag()) {
            InputStreamSource fileResource;
            for (AttachFileDto attachFileDto : mailDto.getAttachFileList()) {
                if (!StringUtils.isEmpty(attachFileDto.getAttachFilePath())) {
                    fileResource = new FileSystemResource(attachFileDto.getAttachFilePath());
                } else {
                    fileResource = new ByteArrayResource(attachFileDto.getAttachFileByte());
                }
                try {
                    if (!StringUtils.isEmpty(attachFileDto.getAttachFileName())) {
                        helper.addAttachment(
                                        MimeUtility.encodeText(attachFileDto.getAttachFileName(),
                                                               StandardCharsets.UTF_8.name(), null
                                                              ), fileResource);
                    } else {
                        helper.addAttachment(
                                        MimeUtility.encodeText("添付ファイル", StandardCharsets.UTF_8.name(), null),
                                        fileResource
                                            );
                    }
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    e.printStackTrace();
                }
            }
        }

        // メール内容を準備
        String mailTemplateName = this.environment.getProperty(mailDto.getMailTemplateType().name());
        helper.setText(this.createMailBody(mailTemplateName, context), true);

        // 準備したメールを送信
        javaMailSender.send(mimeMessage);
    }

    /**
     * メール内容を準備
     *
     * @param mailTemplateName
     * @param context
     * @return
     */
    private String createMailBody(String mailTemplateName, Context context) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(this.mailTemplateResolver);
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(new CustomDialect());
        return templateEngine.process(mailTemplateName, context);
    }

}