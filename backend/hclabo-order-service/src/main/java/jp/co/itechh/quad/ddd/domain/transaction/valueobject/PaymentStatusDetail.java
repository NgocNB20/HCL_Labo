/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import lombok.Getter;

/**
 * 入金状態詳細 値オブジェクト
 */
@Getter
public class PaymentStatusDetail {

    /** 入金不足フラグ */
    private final boolean insufficientMoneyFlag;

    /** 入金超過フラグ */
    private final boolean overMoneyFlag;

    // --- 入金状態詳細 定数定義 ---
    /** 入金状態詳細：未入金（0円） */
    public static final String STATUS_NOTHING = "NOTHING_MONEY";

    /** 入金状態詳細：入金済み（ちょうど） */
    public static final String STATUS_JUST = "JUST_MONEY";

    /** 入金状態詳細：入金不足 */
    public static final String STATUS_INSUFFICIENT = "INSUFFICIENT_MONEY";

    /** 入金状態詳細：入金超過 */
    public static final String STATUS_OVER = "OVER_MONEY";

    /**
     * コンストラクタ
     * ※ファクトリ、DB取得値設定用
     *
     * @param insufficientMoneyFlag
     * @param overMoney
     */
    public PaymentStatusDetail(boolean insufficientMoneyFlag, boolean overMoney) {
        this.insufficientMoneyFlag = insufficientMoneyFlag;
        this.overMoneyFlag = overMoney;
    }

    /**
     * 入金状態詳細のステータス値取得
     *
     * @param orderPaidFlag 受注入金済みフラグ
     * @return
     */
    public String getStatusValue(boolean orderPaidFlag) {

        if (!orderPaidFlag) {
            // 受注未入金の場合
            if (insufficientMoneyFlag) {
                // 入金不足
                return STATUS_INSUFFICIENT;
            } else {
                // 未入金（0円）
                return STATUS_NOTHING;
            }
        } else {
            // 受注入金済みの場合
            if (overMoneyFlag) {
                // 入金超過
                return STATUS_OVER;
            } else {
                // 入金済み（ちょうど）
                return STATUS_JUST;
            }
        }
    }
}