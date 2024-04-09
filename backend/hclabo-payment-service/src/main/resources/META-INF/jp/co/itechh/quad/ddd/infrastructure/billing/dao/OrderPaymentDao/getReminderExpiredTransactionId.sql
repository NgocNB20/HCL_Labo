select 1
from orderpayment o
    inner join billingslip b
    on o.billingslipid  = b.billingslipid
where b.transactionid  = /*transactionId*/0
  and date_trunc('day', o.laterdatelimit) < /*today*/0
