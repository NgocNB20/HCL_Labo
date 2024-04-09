/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.batch.offline.creditline.dto;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 与信枠解放バッチ結果Dto
 *
 * @author kk32102
 */
@Data
@Component
@Scope("prototype")
public class OrderCreditLineReportBatchResultDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** オーダーID */
    private String orderId;

    /** infoメッセージ */
    private List<String> infoMessages = new ArrayList<>();

    /** errorメッセージ */
    private List<String> errorMessages = new ArrayList<>();

    /**
     * infoメッセージ追加
     *
     * @param message メッセージ
     */
    public void addInfoMessage(String message) {
        infoMessages.add(message);
    }

    /**
     * errorメッセージ追加
     *
     * @param message メッセージ
     */
    public void addErrorMessage(String message) {
        errorMessages.add(message);
    }

    /**
     * メッセージが設定されているか？
     *
     * @return true メッセージあり
     */
    public boolean hasMessage() {
        if (hasInfoMessage()) {
            return true;
        }
        return hasErrorMessage();
    }

    /**
     * infoメッセージが設定されているか？
     *
     * @return true infoメッセージあり
     */
    public boolean hasInfoMessage() {
        return !CollectionUtil.isEmpty(infoMessages);
    }

    /**
     * errorメッセージが設定されているか？
     *
     * @return true errorメッセージあり
     */
    public boolean hasErrorMessage() {
        return !CollectionUtil.isEmpty(errorMessages);
    }

}
