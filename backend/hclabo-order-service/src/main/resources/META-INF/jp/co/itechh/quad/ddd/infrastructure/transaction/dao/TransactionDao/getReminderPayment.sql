SELECT t.transactionId, od.ordercode
FROM "transaction" t
    join orderreceived od
      ON t.transactionid = od.latesttransactionid
WHERE t.transactionstatus = 'OPEN'
  AND t.preclaimflag = true
  AND t.paidflag = false
  and t.notificationflag = true
  and t.remindersentflag = false
  AND t.billpaymenterrorflag = false
