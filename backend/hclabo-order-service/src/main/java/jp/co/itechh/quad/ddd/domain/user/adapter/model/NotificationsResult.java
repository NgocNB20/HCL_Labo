/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 通知結果<br/>
 */
@Data
@Component
@Scope("prototype")
public class NotificationsResult {

    /** 結果コード（0:SUCCESSED, 1:FAILED） */
    private String executeCode;

    /** 結果メッセージ */
    private String executeMessage;

}