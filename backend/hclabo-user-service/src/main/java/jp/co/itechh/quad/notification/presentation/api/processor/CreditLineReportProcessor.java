package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * クレジットラインレポート
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class CreditLineReportProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditLineReportProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param creditLineReportRequest クレジットラインレポートリクエスト
     */
    public void processor(CreditLineReportRequest creditLineReportRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_ORDER_CREDITLINE_REPORT.getLabel());

            valueMap.put("SIZE", String.valueOf(creditLineReportRequest.getResultDtoListSize()));
            // メール本文の処理結果詳細を構成する
            String mailBody = creditLineReportRequest.getResult();
            valueMap.put("RESULT", mailBody);

            int errorCnt = creditLineReportRequest.getErrorCnt();
            if (errorCnt == 0) {
                // 取引取消エラーなしの場合、検出通知メール用テンプレートを指定
                mailDto.setMailTemplateType(HTypeMailTemplateType.CREDIT_LINE_REPORT_MAIL);
            } else {
                // 取引取消エラーありの場合、取引取消エラー通知メール用テンプレートを指定
                mailDto.setMailTemplateType(HTypeMailTemplateType.CREDIT_LINE_OPERATION_REPORT_MAIL);

                valueMap.put("SIZE2", String.valueOf(errorCnt));
                String mailBody2 = creditLineReportRequest.getResult2();
                valueMap.put("RESULT2", mailBody2);
            }

            mailContentsMap.put("admin", valueMap);

            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.creditline.report.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.creditline.report.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_ORDER_CREDITLINE_REPORT.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}