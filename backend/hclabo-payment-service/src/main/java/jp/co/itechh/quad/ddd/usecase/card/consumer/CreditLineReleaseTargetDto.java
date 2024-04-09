/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.usecase.card.consumer;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 与信枠解放対象Dto
 *
 * @author kimura
 */
@Entity
@Data
@Component
@Scope("prototype")
public class CreditLineReleaseTargetDto {

    /** オーダーID（注文決済.受注番号） */
    private String orderId;

    /** 注文決済.注文決済ID */
    private String orderPaymentId;

    /** 処理区分 */
    private String jobCd;

    /** ACS 呼出判定 */
    private String acs;

    /** トランザクション種別 */
    private String tranExec;

}
