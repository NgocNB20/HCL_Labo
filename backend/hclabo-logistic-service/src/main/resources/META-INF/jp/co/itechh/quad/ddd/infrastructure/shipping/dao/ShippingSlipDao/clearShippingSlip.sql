delete
from shippingslip
where shippingstatus = 'DRAFT'
  and registdate <= /*deleteTime*/0