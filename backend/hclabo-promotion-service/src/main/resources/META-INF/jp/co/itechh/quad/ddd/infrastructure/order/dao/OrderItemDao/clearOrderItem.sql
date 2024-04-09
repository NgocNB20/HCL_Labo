delete
from orderitem
where orderslipid in
      (select orderslipid
       from orderslip
       where orderstatus = 'DRAFT'
         and registdate <= /*deleteTime*/0);