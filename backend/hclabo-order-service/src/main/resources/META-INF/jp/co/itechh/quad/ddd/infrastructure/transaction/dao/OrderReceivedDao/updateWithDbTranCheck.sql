update
    orderreceived
set
    ordercode = /*orderReceivedDbEntity.orderCode*/'111'
  , registdate = /*orderReceivedDbEntity.registDate*/'2023/01/19 17:12:26.941'
  , orderreceiveddate = /*orderReceivedDbEntity.orderReceivedDate*/'2023/01/19 17:12:26.941'
  , canceldate = /*orderReceivedDbEntity.cancelDate*/'2023/01/19 17:12:26.941'
  , latesttransactionid = /*orderReceivedDbEntity.latestTransactionId*/'aaaa'
where
    orderreceivedid = /*orderReceivedDbEntity.orderReceivedId*/'0'
/*%if originTransactionId != null*/
and
    latesttransactionid = /*originTransactionId*/'aaaa'
/*%end*/
