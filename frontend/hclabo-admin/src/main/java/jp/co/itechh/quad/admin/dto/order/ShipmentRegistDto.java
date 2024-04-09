/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 出荷登録Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class ShipmentRegistDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注番号 */
    private String orderCode;

    /** 受注履歴連番 */
    private Integer orderVersionNo;

    /** 注文連番 */
    private Integer orderConsecutiveNo;

    /** 出荷日 */
    private Timestamp shipmentDate;

    /** 伝票番号 */
    private String deliveryCode;

    /** メール送信フラグ */
    private Boolean sendMailFlag;

    /**
     * 商品数量フラグ<br/>
     * 商品数量が0でない場合のみ出荷完了メールを送信する
     */
    private boolean goodsCountNotZeroFlag;

}