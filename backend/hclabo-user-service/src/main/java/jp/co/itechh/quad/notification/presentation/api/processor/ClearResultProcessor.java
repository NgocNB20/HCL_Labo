package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ClearResultRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クリア通知
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ClearResultProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClearResultProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param clearResultRequest クリア通知リクエスト
     */
    public void processor(ClearResultRequest clearResultRequest) {

        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            // ショップSEQ
            Integer shopSeq = 1001;

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            final Map<String, String> valueMap = putMapValue(clearResultRequest, shopSeq, systemName);

            mailContentsMap.put("admin", valueMap);

            // 削除失敗ファイルの一覧
            List<String> deleteFailFiles = clearResultRequest.getDeleteFailFiles();

            mailContentsMap.put("deleteFailFiles", deleteFailFiles);

            mailDto.setMailTemplateType(HTypeMailTemplateType.CLEAR_RESULT_ADMINISTRATOR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.clear.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue("mail.setting.clear.mail.receivers")
                                                          .split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_CLEAR.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            // メール送信
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");
        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }

    /**
     * メール本文作成用マップ
     *
     * @param clearResultRequest clear result request
     * @param shopSeq            ショップSEQ
     * @param systemName         システム名
     */
    private Map<String, String> putMapValue(ClearResultRequest clearResultRequest, Integer shopSeq, String systemName) {

        Map<String, String> valueMap = new HashMap<>();

        // プレースホルダーへ結果セット
        valueMap.put("SYSTEM", systemName);
        valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_CLEAR.getLabel());
        valueMap.put("SHOP_SEQ", String.valueOf(shopSeq));
        valueMap.put("FILE_CLEAR_STATUS", clearResultRequest.getResultData().getStatusMessage());
        valueMap.put("FILE_CLEAR_COUNT", clearResultRequest.getResultData().getFileClearCount());
        valueMap.put(
                        "CONFIRMMAIL_RECORD_CLEAR_STATUS",
                        clearResultRequest.getResultData().getConfirmMailStatusMessage()
                    );
        valueMap.put(
                        "CONFIRMMAIL_RECORD_CLEAR_COUNT",
                        clearResultRequest.getResultData().getConfirmMailRecordClearCount()
                    );
        valueMap.put("GOODSCART_RECORD_CLEAR_STATUS", clearResultRequest.getResultData().getGoodsCartStatusMessage());
        valueMap.put("GOODSCART_RECORD_CLEAR_COUNT", clearResultRequest.getResultData().getGoodsCartRecordClearCount());
        valueMap.put(
                        "PREVIEWACCESS_RECORD_CLEAR_STATUS",
                        clearResultRequest.getResultData().getPreviewAccessStatusMessage()
                    );
        valueMap.put(
                        "PREVIEWACCESS_RECORD_CLEAR_COUNT",
                        clearResultRequest.getResultData().getPreviewAccessRecordClearCount()
                    );
        valueMap.put("FOOTPRINT_RECORD_CLEAR_STATUS", clearResultRequest.getResultData().getFootPrintStatusMessage());
        valueMap.put("FOOTPRINT_RECORD_CLEAR_COUNT", clearResultRequest.getResultData().getFootPrintRecordClearCount());
        valueMap.put(
                        "ACCESSINFO_RECORD_REPORT_STATUS",
                        clearResultRequest.getResultData().getAccessInfoStatusMessage()
                    );
        valueMap.put("ACCESSINFO_ALL_RECORD_COUNT", clearResultRequest.getResultData().getAccessInfoRecordCount());
        valueMap.put(
                        "ACCESSINFO_RECORD_COUNT",
                        clearResultRequest.getResultData().getAccessInfoSpecifieddaysRecordCount()
                    );
        valueMap.put(
                        "ACCESSSEARCHKEYWORD_RECORD_REPORT_STATUS",
                        clearResultRequest.getResultData().getAccessSearchKeywordStatusMessage()
                    );
        valueMap.put(
                        "ACCESSSEARCHKEYWORD_ALL_RECORD_COUNT",
                        clearResultRequest.getResultData().getAccessSearchKeywordRecordCount()
                    );
        valueMap.put(
                        "ACCESSSEARCHKEYWORD_RECORD_COUNT",
                        clearResultRequest.getResultData().getAccessSearchKeywordSpecifieddaysRecordCount()
                    );
        valueMap.put(
                        "ADMINCONFIRMMAIL_RECORD_CLEAR_STATUS",
                        clearResultRequest.getResultData().getAdminConfirmMailStatusMessage()
                    );
        valueMap.put(
                        "ADMINCONFIRMMAIL_RECORD_CLEAR_COUNT",
                        clearResultRequest.getResultData().getAdminConfirmMailRecordClearCount()
                    );

        return valueMap;
    }
}