package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品グループ/規格画像更新エラー Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class GoodsAsynchronousErrorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsAsynchronousErrorProcessor.class);

    /** 改行(キャリッジリターン) */
    private static final String LINE_FEED = "\r\n";

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param goodsAsynchronousErrorRequest 商品グループ規格画像更新（商品一括アップロード）異常リクエスト
     */
    public void processor(GoodsAsynchronousErrorRequest goodsAsynchronousErrorRequest) {
        // ショップSEQ
        Integer shopSeq = 1001;

        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            // メール本文作成用マップ
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_GOODS_ASYNCHRONOUS.getLabel());
            valueMap.put("SHOP_SEQ", String.valueOf(shopSeq));

            // エラーメール本文の内容作成
            final Map<String, String> includeMailMessageMap =
                            addErrorMailMessage(valueMap, goodsAsynchronousErrorRequest.getErrorMessage());

            mailContentsMap.put("error", includeMailMessageMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.GOODS_ASYNCHRONOUS_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.goodsimage.upload.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_GOODS_ASYNCHRONOUS.getLabel() + "報告");
            mailDto.setMailContentsMap(mailContentsMap);

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }

    /**
     * エラーメール本文を作成する
     * @param valueMap メール本文作成用マップ
     * @param errorResultMsg エラー結果
     * @return valueMap メール本文作成用マップ
     */
    public Map<String, String> addErrorMailMessage(Map<String, String> valueMap, final String errorResultMsg) {
        String resultMsg = "処理中に" + errorResultMsg + "が発生しました。" + LINE_FEED + LINE_FEED;
        valueMap.put("RESULT", resultMsg);
        if (LOGGER.isDebugEnabled()) {
            valueMap.put("DEBUG", "1");
        } else {
            valueMap.put("DEBUG", "0");
        }

        return valueMap;
    }
}