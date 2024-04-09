/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.common.CheckMessageDto;
import org.springframework.stereotype.Component;

/**
 * CheckMessageDto生成Utility
 *
 * @author Faizan Momin
 */
@Component
public class CheckMessageUtility {

    /**
     * メッセージ取得
     *
     * @param messageId メッセージID
     * @param error エラーフラグ
     * @param args メッセージ引数
     * @return チェックメッセージDto
     */
    public CheckMessageDto createCheckMessageDto(String messageId, boolean error, Object... args) {
        CheckMessageDto checkMessageDto = ApplicationContextUtility.getBean(CheckMessageDto.class);
        checkMessageDto.setMessageId(messageId);
        String message = getMessage(messageId, args);
        checkMessageDto.setMessage(message);
        checkMessageDto.setArgs(args);
        checkMessageDto.setError(error);
        return checkMessageDto;
    }

    /**
     * メッセージ取得
     *
     * @param messageId メッセージID
     * @param args メッセージ引数
     * @return メッセージ
     */
    public String getMessage(String messageId, Object... args) {
        AppLevelFacesMessage facesMessage = AppLevelFacesMessageUtil.getAllMessage(messageId, args);
        return facesMessage.getMessage();
    }

}