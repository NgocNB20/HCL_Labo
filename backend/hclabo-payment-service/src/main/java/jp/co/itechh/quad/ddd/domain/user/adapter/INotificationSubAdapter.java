/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.ddd.domain.billing.entity.TargetSalesOrderPaymentDto;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;

import java.util.List;

/**
 * 通知アダプター
 */
public interface INotificationSubAdapter {

    /**
     * ユーザーマイクロサービス<br/>
     * オーソリ期限間近注文警告メール<br/>
     *
     * @param result      結果レポート文字列
     * @param subjectFlg  件名切り替え用フラグ true:対象受注あり false:対象受注なし
     * @param mailMessage 正常処理メッセージ(処理内容格納）
     */
    void authExpirationApproaching(String result, boolean subjectFlg, String mailMessage);

    /**
     * ユーザーマイクロサービス<br/>
     * オーソリ期限間近注文警告エラーメール<br/>
     *
     * @param errorResultMsg エラー詳細
     */
    void authExpirationApproachingError(String errorResultMsg);

    /**
     * ユーザーマイクロサービス<br/>
     * 与信枠解放メール<br/>
     *
     * @param resultDtoListSize 対象受注結果数
     * @param errorCnt          エラー数
     * @param result            完了メッセージ
     * @param result2           完了メッセージ
     */
    void creditLineRelease(Integer resultDtoListSize, Integer errorCnt, String result, String result2);

    /**
     * ユーザーマイクロサービス<br/>
     * 与信枠解放エラーメール<br/>
     *
     * @param errorResultMsg エラー詳細
     */
    void creditLineReleaseError(String errorResultMsg);

    /**
     * GMO決済キャンセル漏れメール送信要求 <br/>
     *
     * @param targetSalesOrderNumberDtoList 対象受注番号Dtoリスト
     */
    void linkpayCancelReminder(List<TargetSalesOrderPaymentDto> targetSalesOrderNumberDtoList);

    /**
     * 請求不整合報告
     *
     * @param settlementMismatchRequest 請求不整合報告リクエスト
     */
    void settlementMisMatch(SettlementMismatchRequest settlementMismatchRequest);
}
