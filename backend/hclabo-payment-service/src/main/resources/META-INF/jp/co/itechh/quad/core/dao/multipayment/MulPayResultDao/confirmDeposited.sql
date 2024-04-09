select
        *
    from
        mulPayResult
    where
        mulPayResult.orderId = /*orderId*/'' and mulPayResult.processedflag = '0'
    ORDER BY
        mulPayResult.mulpayresultseq DESC OFFSET 0 LIMIT 1
