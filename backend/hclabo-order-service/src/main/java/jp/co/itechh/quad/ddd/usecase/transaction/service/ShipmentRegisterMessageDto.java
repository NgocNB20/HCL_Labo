/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import lombok.Data;

/**
 * 出荷登録実行メッセージDto<br/>
 */
@Data
public class ShipmentRegisterMessageDto {

    /** エラーメッセージコード */
    private String errCode;

    /** エラーメッセージ内容 */
    private String errMessage;

    /** args */
    private String[] args;

    /** メール通知非同期処理用 */
    private OrderCode mailOrderCode;

    /** 改訂伝票取引改訂の非同期処理用 */
    private OrderReceivedEntity orderReceivedEntity;
}