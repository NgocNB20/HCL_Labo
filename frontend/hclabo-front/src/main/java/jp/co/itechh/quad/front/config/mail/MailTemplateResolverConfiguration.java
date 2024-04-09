package jp.co.itechh.quad.front.config.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

/**
 * 機能概要：新メール送信サービス
 * 作成日：2021/03/30
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Configuration
public class MailTemplateResolverConfiguration {

    private static final String TEMPLATE_PREFIX = "mailtemplate/";
    private static final String TEMPLATE_SUFFIX = ".html";
    private static final String TEMPLATE_CHARACTER_ENCODING = StandardCharsets.UTF_8.name();

    @Bean
    public ClassLoaderTemplateResolver mailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix(TEMPLATE_PREFIX);
        templateResolver.setSuffix(TEMPLATE_SUFFIX);
        templateResolver.setCharacterEncoding(TEMPLATE_CHARACTER_ENCODING);
        templateResolver.setCacheable(true);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

}
