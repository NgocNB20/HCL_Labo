select
    transaction.transactionid ,
    transaction.processtime ,
    transaction.processtype ,
    transaction.processPersonName
from
    orderReceived
inner join
    transaction
on
    transaction.orderreceivedid = orderReceived.orderreceivedid
and
    transaction.transactionstatus != 'DRAFT'
where
    orderreceived.ordercode  = /*orderCode*/'M4W2290ZN393YO'
order by
    transaction.processtime asc
