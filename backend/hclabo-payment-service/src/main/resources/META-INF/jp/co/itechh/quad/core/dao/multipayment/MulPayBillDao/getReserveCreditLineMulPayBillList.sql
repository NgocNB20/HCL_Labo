 --
 --マルチペイメント請求内に残っている与信枠を確保したままの可能性がある受注を検出する。
 --チケット#2725対応
select
        mulpaybill_output.orderid
        ,mulpaybill_output.orderpaymentid
        ,mulpaybill_output.jobcd
        ,mulpaybill_output.acs
        ,mulpaybill_output.tran_exec as tranexec
    from
        -- 結合してできたデータからTrantypeが'EntryTran'かつ与信枠解放に登録済みの受注以外のレコードのみを抽出
        (
            select
                    mulpaybill.orderid
                    ,mulpaybill.orderpaymentid
                    ,mulpaybill.trantype
                    ,mulpaybill.paytype
                    ,mulbill_exec.tran_exec
                    ,mulbill_exec.err_exec
                    ,mulbill_exec.acs
                    ,mulbill_secure.tran_secure
                    ,mulbill_secure.err_secure
                    ,mulpaybill.jobcd
                    ,mulpaybill.registtime
                from
                    mulpaybill mulpaybill
                    -- マルチペイメント請求のTrantypeが'ExecTran'のレコードをマルチペイメント請求のTrantypeが'EntryTran'のレコードに結合
                    left outer join (
                        select
                                mulbill_exec.orderid
                                ,mulbill_exec.orderpaymentid
                                ,mulbill_exec.trantype as tran_exec
                                ,mulbill_exec.errcode as err_exec
                                ,mulbill_exec.acs
                                ,mulbill_exec.jobcd
                            from
                                mulpaybill mulbill_exec
                            where
                                mulbill_exec.trantype = 'ExecTran'
                    ) mulbill_exec
                        on mulpaybill.orderid = mulbill_exec.orderid
                    -- マルチペイメント請求のTrantypeが'SecureTran2'のレコードをマルチペイメント請求のTrantypeが'EntryTran'のレコードに結合
                    left outer join (
                        select
                                mulbill_secure.orderid
                                ,mulbill_secure.orderpaymentid
                                ,mulbill_secure.trantype as tran_secure
                                ,mulbill_secure.errcode as err_secure
                                ,mulbill_secure.jobcd
                            from
                                mulpaybill mulbill_secure
                            where
                                mulbill_secure.trantype = 'SecureTran2'
                    ) mulbill_secure
                        on mulpaybill.orderid = mulbill_secure.orderid
                where
                    mulpaybill.orderid not in (
                        select
                                creditlinereport.orderid
                            from
                                creditlinereport
                    )
                    and mulpaybill.trantype = 'EntryTran'
                    -- 対象の取引をdiconファイルで設定した日数、時間で絞り込む
                    and mulpaybill.registtime between /*specifiedDay*/0 and /*thresholdTime*/0
                    and mulpaybill.errcode is null
                order by mulpaybill.orderid
        ) as mulpaybill_output
    where
        -- エラーコードがＮＵＬＬのレコードのみを抽出
        mulpaybill_output.err_exec is null
        and mulpaybill_output.err_secure is null
        -- 除外対象の注文決済IDを抽出
        and mulpaybill_output.orderpaymentid not in (
            select
                    orderpayment.orderpaymentid
                from
                    orderpayment, billingslip
                where
                    mulpaybill_output.orderpaymentid = orderpayment.orderpaymentid
                and
                    orderpayment.billingslipid = billingslip.billingslipid
                -- 請求伝票のステータスがオーソリ済
                and
                    ( billingslip.billingstatus = 'AUTHORIZED'
                      or
                      billingslip.billingstatus = 'DEPOSIT_WAITING'
                      or
                      billingslip.billingstatus = 'DEPOSITED')
        )
