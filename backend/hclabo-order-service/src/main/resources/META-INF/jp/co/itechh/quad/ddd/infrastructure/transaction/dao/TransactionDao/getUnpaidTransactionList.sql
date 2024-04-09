select *
from transaction
    inner join orderreceived
    on orderreceived.latesttransactionid = transaction.transactionid
where transaction.transactionstatus = 'OPEN'
  and transaction.paidflag = FALSE
  and transaction.preclaimflag = TRUE
