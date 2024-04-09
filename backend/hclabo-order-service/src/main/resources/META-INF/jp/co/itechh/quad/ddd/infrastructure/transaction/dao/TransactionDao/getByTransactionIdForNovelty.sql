select
    *
from
    transaction
where
    TRANSACTIONID = /*transactionId*/'1001'
and noveltypresentjudgmentstatus = '0'
and transactionstatus = 'OPEN'
