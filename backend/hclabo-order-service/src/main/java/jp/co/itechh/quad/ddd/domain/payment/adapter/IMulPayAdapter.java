/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmDepositedResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;

/**
 * マルチペイメントエンドポイントアダプター
 * 決済マイクロサービス
 */
public interface IMulPayAdapter {

    /**
     * マルチペイメント決済結果が要入金反映処理か確認する
     *
     * @param orderId 受注番号
     * @return MulPayResultConfirmDepositedResponse マルチペイメント決済結果レスポンス
     */
    MulPayResultConfirmDepositedResponse doConfirmDeposited(String orderId);

    /**
     * 決済代行へ入金を確認する
     *
     * @param orderId 受注番号
     * @return 要入金反映処理フラグ
     */
    Boolean confirmPaymentAgencyResult(String orderId);

    /**
     * マルチペイメント決済結果登録取得
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト
     * @return 要入金反映処理フラグ
     */
    Boolean mulPayRegistResult(MulPayResultRequest mulPayResultRequest);

    /**
     * マルチペイメント決済結果へ入金処理済みを反映
     *
     * @param mulPayResultSeq マルチペイメント決済結果SEQ
     */
    void updateDeposited(Integer mulPayResultSeq);
}
