DELETE
FROM
     transactionforrevision tr
     USING "transaction" t
WHERE
    tr.transactionrevisionid = t.transactionid
AND  tr.registdate <= /*periodTimeCancelUnopenBatch*/0