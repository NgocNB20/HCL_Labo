package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.AttachFileDto;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 在庫状況を管理者にメール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class StockReportProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(StockReportProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param stockReportRequest 在庫状況を管理者にメール送信リクエスト
     * @throws Exception Exception
     */
    public void processor(StockReportRequest stockReportRequest) throws Exception {
        MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

        mailDto.setMailTemplateType(HTypeMailTemplateType.BATCH_MAIL_STOCK_REPORT);

        Map<String, String> valueMap = new LinkedHashMap<>();
        Map<String, Object> mailContentsMap = new HashMap<>();

        // システム名を取得
        String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

        valueMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        valueMap.put("server", systemName);
        mailContentsMap.put("report", valueMap);

        mailDto.setMailContentsMap(mailContentsMap);

        //
        // 件名
        //
        LOGGER.info("メール件名を作成");
        String subject = "【" + systemName + "】在庫状況 " + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        mailDto.setSubject(subject);

        // 「To」を設定
        mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                        "mail.setting.stock.report.mail.receivers").split(COMMA_DELIMITER)));

        LOGGER.info("添付ファイルを作成");

        mailDto.setAttachFileFlag(true);
        List<AttachFileDto> attachFileList = new ArrayList<>();

        stockReportRequest.getFileList().forEach(file -> {
            AttachFileDto attachFileDto = new AttachFileDto();
            attachFileDto.setAttachFileName(file.getFileName());
            attachFileDto.setAttachFilePath(file.getFilePath());
            attachFileList.add(attachFileDto);
        });

        // 添付ファイル一覧を設定
        mailDto.setAttachFileList(attachFileList);

        mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.stock.report.mail.from"));

        LOGGER.info("CSVファイルが添付されたメールを送信します。");
        LOGGER.info("送信先：" + Collections.singletonList(
                        PropertiesUtil.getSystemPropertiesValue("mail.setting.stock.report.mail.receivers")));
        LOGGER.info("送信しました。");

        MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

        try {
            if (!mailSendService.execute(mailDto)) {
                throw new Exception("次のメールアドレスに" + mailDto.getMailTemplateType().getLabel() + "メールの送信を失敗しました。"
                                    + mailDto.getToList());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}