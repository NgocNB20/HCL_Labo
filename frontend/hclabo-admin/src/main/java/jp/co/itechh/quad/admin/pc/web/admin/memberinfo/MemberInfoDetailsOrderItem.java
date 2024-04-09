/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 会員詳細画面注文履歴情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class MemberInfoDetailsOrderItem implements Serializable {

    /************************************
     ** 注文ステータス 値オブジェクト(enum)の一覧<br//>
     ** jp.co.itechh.quad.ddd.domain.transaction.valueobject
     ************************************/

    /** 商品準備中 */
    public static final String ITEM_PREPARING = "ITEM_PREPARING";

    /** 入金確認中 */
    public static final String PAYMENT_CONFIRMING = "PAYMENT_CONFIRMING";

    /** 出荷完了 */
    public static final String SHIPMENT_COMPLETION = "SHIPMENT_COMPLETION";

    /** 取消 */
    public static final String CANCEL = "CANCEL";

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** No */
    public Integer resultNo;

    /** 受注履歴連番 */
    public Integer orderVersionNo;

    /** 受注番号 */
    public String orderCode;

    /** 受注日時 */
    public Timestamp orderTime;

    /** 受注金額 */
    public BigDecimal orderPrice;

    /** 受注状態 */
    public String orderStatus;

}
