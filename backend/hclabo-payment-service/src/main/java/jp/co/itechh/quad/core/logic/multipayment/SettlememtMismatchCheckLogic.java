/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment;

/**
 * 請求情報の不整合チェックLogic
 * <pre>
 * 以下の場合に管理者宛にメールを送信する
 *   ・決済代行会社での処理成功後、HIT-MALLの処理でエラーが発生した場合
 *   ・決済代行会社での取消処理でエラーが発生したが、HIT-MALLの処理は正常終了した場合
 * </pre>
 *
 * @author kk32102
 */
public interface SettlememtMismatchCheckLogic {

    /** HIT-MALLの処理区分：受注修正 */
    String PROC_TYPE_ORDER_UPDATE = "orderupdate";

    /** HIT-MALLの処理区分：再オーソリ */
    String PROC_TYPE_RE_AUTH = "reauth";

    /** HIT-MALLの処理区分：受注キャンセル */
    String PROC_TYPE_ORDER_CANCEL = "ordercancel";

    /** HIT-MALLの処理区分：決済方法変更 */
    String PROC_TYPE_SETTLEMENT_METHOD_CHANGE = "settlementmethodchange";

    /** HIT-MALLの処理区分：出荷登録 */
    String PROC_TYPE_SHIPMENT_REGIST = "shipmentregist";

    /**
     * 初期処理（受注修正）
     *
     * @param orderCode 受注番号
     */
    void initOrderUpdate(String orderCode);

    /**
     * 初期処理（再オーソリ）
     *
     * @param orderCode 受注番号
     */
    void initReAuth(String orderCode);

    /**
     * 初期処理（受注キャンセル）
     *
     * @param orderCode 受注番号
     */
    void initOrderCancel(String orderCode);

    /**
     * 初期処理（決済方法変更）
     *
     * @param orderCode 受注番号
     */
    void initSettlementMethodChange(String orderCode);

    /**
     * 初期処理（出荷登録）
     *
     * @param orderCode 受注番号
     */
    void initShipmentRegist(String orderCode);

    /**
     * チェック処理（正常終了時）
     */
    void execute();

    /**
     * チェック処理（異常終了時）
     *
     * @param error 発生したエラー
     */
    void execute(Throwable error);

}
