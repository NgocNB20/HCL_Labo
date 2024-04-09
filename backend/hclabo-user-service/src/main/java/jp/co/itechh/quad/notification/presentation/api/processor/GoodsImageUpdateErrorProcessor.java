package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品画像更新エラー
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class GoodsImageUpdateErrorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsImageUpdateErrorProcessor.class);

    /** 改行(キャリッジリターン) */
    private static final String LINE_FEED_CR = "\r\n";

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param goodsImageUpdateErrorRequest 商品画像更新エラーリクエスト
     */
    public void processor(GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest) {
        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            final Map<String, String> valueMap = new HashMap<>();
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_GOODSIMAGE_UPDATE.getLabel());
            valueMap.put("SHOP_SEQ", "1001");
            if (goodsImageUpdateErrorRequest.getExceptionName() != null) {
                String resultMsg = "処理中に" + goodsImageUpdateErrorRequest.getExceptionName() + "が発生しました。" + LINE_FEED_CR
                                   + LINE_FEED_CR;
                valueMap.put("RESULT", resultMsg);
            } else {
                String resultMsg = "キャンセル要求があったため処理は中断されました。" + LINE_FEED_CR + LINE_FEED_CR;
                valueMap.put("RESULT", resultMsg);
            }

            if (goodsImageUpdateErrorRequest.getMsg() != null) {
                valueMap.put("LIST", goodsImageUpdateErrorRequest.getMsg());
            } else {
                valueMap.put("LIST", "");
            }

            if (LOGGER.isDebugEnabled()) {
                valueMap.put("DEBUG", "1");
            } else {
                valueMap.put("DEBUG", "0");
            }

            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.GOODSIMAGE_UPDATE_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.goodsimage.upload.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_GOODSIMAGE_UPDATE.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }
}