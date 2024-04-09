package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockStatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品グループ在庫状態更新異常
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class StockStatusProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(StockStatusProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param stockStatusRequest 商品グループ在庫状態更新異常リクエスト
     */
    public void processor(StockStatusRequest stockStatusRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_STOCKSTATUSDISPLAY_UPDATE.getLabel());
            valueMap.put("RESULT", "処理中に" + stockStatusRequest.getExecptionInfo() + "が発生しました。");

            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.STOCK_STATUS_ADMINISTRATOR_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.stock.status.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.stock.status.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject(
                            "【" + systemName + "】" + HTypeBatchName.BATCH_STOCKSTATUSDISPLAY_UPDATE.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("管理者へエラー通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者へのエラー通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}