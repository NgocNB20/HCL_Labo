delete
from orderpayment
where billingslipid in
      (select billingslipid
       from billingslip
       where billingstatus = 'DRAFT'
         and registdate <= /*deleteTime*/0);