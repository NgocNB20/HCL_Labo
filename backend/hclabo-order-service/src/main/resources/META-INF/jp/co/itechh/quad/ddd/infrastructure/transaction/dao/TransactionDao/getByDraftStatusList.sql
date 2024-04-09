select
    t.transactionid,
    o.orderCode
from
    transaction t
left join
    orderreceived o
on
    t.orderreceivedid = o.orderreceivedid
where
    t.transactionstatus = 'DRAFT'
and t.registdate <= /*periodTimeCancelUnopenBatch*/0
order by t.registdate asc