/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CategoryGoodsRegistUpdateErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * カテゴリ商品更新バッチ異常
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class CategoryGoodsRegistUpdateErrorProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryGoodsRegistUpdateErrorProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param categoryGoodsRegistUpdateErrorRequest カテゴリ商品更新バッチ異常リクエスト
     */
    public void processor(CategoryGoodsRegistUpdateErrorRequest categoryGoodsRegistUpdateErrorRequest) {

        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();
            final Map<String, String> valueMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            valueMap.put("SYSTEM", systemName);
            valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_CATEGORY_GOODS_REGISTER_UPDATE.getLabel());
            valueMap.put("SUB_WORD", "エラー");
            valueMap.put("RESULT", "処理中に " + categoryGoodsRegistUpdateErrorRequest.getErrorResultMsg());
            mailContentsMap.put("error", valueMap);

            mailDto.setMailTemplateType(HTypeMailTemplateType.CATEGORY_GOODS_REGISTER_UPDATE_ERROR_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.categorygoods.registupdate.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.categorygoods.registupdate.mail.receivers").split(COMMA_DELIMITER)));
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_CATEGORY_GOODS_REGISTER_UPDATE.getLabel()
                               + "報告");
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
}