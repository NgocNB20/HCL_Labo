select
    transaction.transactionstatus,
    transaction.paidflag,
    transaction.shippedflag,
    transaction.billpaymenterrorflag,
    orderReceived.orderreceiveddate,
    orderReceived.ordercode,
    transaction.transactionid
from
    orderReceived
inner join
    transaction
on
    transaction.transactionid  = orderReceived.latesttransactionid
and
    transaction.transactionstatus != 'DRAFT'
and
    transaction.customerid = /*customerId*/'10000000'
order by
    orderReceived.orderreceiveddate desc
