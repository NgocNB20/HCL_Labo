/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.usecase.examination.service;

import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;
import lombok.Data;

import java.util.List;

/**
 * 検査結果登録実行メッセージDto<br/>
 */
@Data
public class ExamResultsEntryMessageDto {

    /** エラーメッセージ内容 */
    private StringBuilder errMessage;

    /** メール通知非同期処理用 */
    private List<OrderReceived> orderReceivedList;

    /** 実行件数 */
    private int resultCount;

    /** エラー件数 */
    private int errCount;

}