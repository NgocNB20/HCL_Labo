delete
from securedshippingitem
where shippingslipid in
      (select shippingslipid
       from shippingslip
       where shippingstatus = 'DRAFT'
         and registdate <= /*deleteTime*/0);