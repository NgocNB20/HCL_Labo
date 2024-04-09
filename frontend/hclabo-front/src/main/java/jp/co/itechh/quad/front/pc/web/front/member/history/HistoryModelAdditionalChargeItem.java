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

/**
 * 注文履歴詳細追加料金 ModelItem
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class HistoryModelAdditionalChargeItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 追加料金名 */
    private String additionalDetailsName;

    /** 追加料金金額 */
    private BigDecimal additionalDetailsPrice;
}
