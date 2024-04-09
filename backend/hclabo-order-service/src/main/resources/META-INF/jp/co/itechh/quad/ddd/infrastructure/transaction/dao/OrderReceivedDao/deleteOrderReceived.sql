DELETE
FROM orderreceived o
    USING transaction t
WHERE
    o.orderreceivedid = t.orderreceivedid
  AND
    o.latesttransactionid is null
  AND
    t.transactionid = /*transactionId*/'1001'