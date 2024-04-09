delete
from transaction
where transactionStatus = 'DRAFT'
  and registdate <= /*deleteTime*/0