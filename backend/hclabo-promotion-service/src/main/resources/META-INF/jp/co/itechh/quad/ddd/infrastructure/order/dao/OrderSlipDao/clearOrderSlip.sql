delete
from orderslip
where orderstatus = 'DRAFT'
  and registdate <= /*deleteTime*/0