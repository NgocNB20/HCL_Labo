package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品グループ/規格画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class GoodsAsynchronousProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsAsynchronousProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param goodsAsynchronousRequest パスワード再設定メール送信リクエスト
     */
    public void processor(GoodsAsynchronousRequest goodsAsynchronousRequest) {
        // ショップSEQ
        Integer shopSeq = 1001;

        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
            Map<String, Object> mailContentsMap = new HashMap<>();

            mailDto.setMailTemplateType(HTypeMailTemplateType.GOODS_ASYNCHRONOUS_MAIL);

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            // メール本文作成用マップ
            final Map<String, String> valueMap = new HashMap<>();
            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_GOODS_ASYNCHRONOUS.getLabel());
            valueMap.put("SHOP_SEQ", String.valueOf(shopSeq));

            // 総処理件数をメール本文に追加
            valueMap.put("CNT_GOODSGROUP_SUM", Integer.toString(goodsAsynchronousRequest.getGoodsGroupSum()));
            // 成功件数をメール本文に追加
            valueMap.put("CNT_SUCCESS_SUM", Integer.toString(goodsAsynchronousRequest.getSuccessSum()));
            // 失敗件数をメール本文に追加
            valueMap.put("CNT_ERROR_SUM", Integer.toString(goodsAsynchronousRequest.getErrorSum()));
            // エラー結果
            valueMap.put("LIST", goodsAsynchronousRequest.getErrorMessage());

            mailContentsMap.put("admin", valueMap);
            mailDto.setMailContentsMap(mailContentsMap);

            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.from"));
            mailDto.setToList(Arrays.asList(
                            PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_GOODS_ASYNCHRONOUS.getLabel() + "報告");

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("管理者へ通知メールを送信しました。");
        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }

}