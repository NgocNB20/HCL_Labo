/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 注文履歴 ModelItem
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class MemberHistoryModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 注文日時 */
    private Timestamp orderTime;

    /** 注文番号 */
    private String orderCode;

    /** 合計金額 */
    private BigDecimal orderPrice;

    /**
     * 注文状況
     * 受注状態(入金確認中、商品準備中、出荷完了) 、 キャンセル 、 保留
     */
    private String status;

    /**
     * 注文状況.値
     * 受注状態(入金確認中、商品準備中、出荷完了) 、 キャンセル 、 保留
     */
    private String statusValue;

    /**
     * @return 受注コード(パラメータ用)
     */
    public String getOcd() {
        return orderCode;
    }
}
