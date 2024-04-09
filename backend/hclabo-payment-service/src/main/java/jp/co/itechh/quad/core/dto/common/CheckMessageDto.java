/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * チェックメッセージDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.5 $
 */
@Component
@Scope("prototype")
public class CheckMessageDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** メッセージID */
    private String messageId;

    /** args */
    private Object[] args;

    /** メッセージ */
    private String message;

    /** 注文連番 */
    private Integer orderConsecutiveNo;

    /** 複数配送固定文言：プレフィックス */
    private static final String PREFIX = "XX";

    /** 複数配送固定文言 */
    private static final String MSG_CONSECUTIVE = "お届け先" + PREFIX + "件目：";

    /** エラーフラグ */
    private boolean error = false;

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        String outPutMsg = "";
        if (orderConsecutiveNo != null) {
            outPutMsg = MSG_CONSECUTIVE.replace(PREFIX, orderConsecutiveNo.toString());
        }
        return outPutMsg + message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * @return the orderConsecutiveNo
     */
    public Integer getOrderConsecutiveNo() {
        return orderConsecutiveNo;
    }

    /**
     * @param orderConsecutiveNo the orderConsecutiveNo to set
     */
    public void setOrderConsecutiveNo(Integer orderConsecutiveNo) {
        this.orderConsecutiveNo = orderConsecutiveNo;
    }
}
