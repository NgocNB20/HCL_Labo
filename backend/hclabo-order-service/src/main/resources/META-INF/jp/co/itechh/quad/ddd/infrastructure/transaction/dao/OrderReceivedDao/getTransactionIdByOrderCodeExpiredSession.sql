select
    transaction.transactionid
from
    orderReceived
inner join
    transaction
    on orderReceived.orderreceivedid = transaction.orderreceivedid
where
    orderreceived.ordercode = /*orderCode*/'R1KKJ7PEY58GG1'
order by
    transaction.registdate desc
limit 1
