select
    transactionrevisionid
from
    (
    select
      t.transactionid transactionOpen,
      tr.transactionrevisionid,
      tr.registdate
    from
      transactionforrevision tr
    left join "transaction" t
    on
      tr.transactionrevisionid = t.transactionid
    where
      tr.registdate <= /*periodTimeCancelUnopenBatch*/0
    ) as trt
where
    transactionOpen is null
order by registdate asc