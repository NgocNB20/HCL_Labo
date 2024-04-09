select
    COUNT(*)
from
    orderReceived
inner join
    transaction
on
    transaction.transactionid  = orderReceived.latesttransactionid
and
    transaction.customerid = /*customerId*/'10000000'
