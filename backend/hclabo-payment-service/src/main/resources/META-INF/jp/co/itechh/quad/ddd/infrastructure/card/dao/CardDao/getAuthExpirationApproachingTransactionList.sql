SELECT
    orderpayment.ordercode
    , orderpayment.authlimitdate AS authoryLimitDate
FROM
    billingslip INNER JOIN orderpayment
    ON billingslip.billingslipid = orderpayment.billingslipid
WHERE
    -- ①請求伝票ステータス：オーソリ済み
    billingslip.billingstatus = 'AUTHORIZED'
    -- ②後払い請求
    AND billingslip.billingtype = '1'
    -- ③注文決済ステータス：確定
    AND orderpayment.orderpaymentstatus = 'OPEN'
    -- ④決済方法：クレジット
    AND orderpayment.settlementmethodtype = '0'
    -- ⑤バッチ実行日がオーソリ保持期限日（決済日付＋オーソリ保持期間）- メール送信開始期間以降
    AND /*currentDate*/0 >= (orderpayment.authlimitdate - cast(/*mailSendStartPeriod*/0 || ' days' AS interval))
    -- ⑥オーソリ保持期限（決済日付＋オーソリ保持期間）がバッチ実行日以降
    AND /*currentDate*/0 <= orderpayment.authlimitdate
    -- ⑦請求伝票が最新履歴であること
    AND EXISTS (
        SELECT 1
        FROM billingslip latest_billingslip
		    INNER JOIN orderpayment latest_orderpayment
                    ON latest_billingslip.billingslipid =
                       latest_orderpayment.billingslipid
		WHERE
		    -- 同一受注番号内で
            latest_orderpayment.ordercode = orderpayment.ordercode
        GROUP BY
            latest_orderpayment.ordercode
        HAVING
		    -- 登録日時が最新
            MAX(latest_billingslip.registdate) = billingslip.registdate
    )
ORDER BY orderpayment.authlimitdate,orderpayment.ordercode
