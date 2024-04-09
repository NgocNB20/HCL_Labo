/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 出荷実績登録ユースケースパラメータ
 */
@Data
@Component
@Scope("prototype")
public class RegistShipmentResultUseCaseParam {

    /** 受注番号 */
    private String orderCode;

    /** 配送状況確認番号 */
    private String shipmentStatusConfirmCode;

    /** 出荷完了日時 */
    private Date completeShipmentDate;

}
