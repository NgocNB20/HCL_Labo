delete
from orderpaymentforrevision
where billingsliprevisionid in
      (select billingsliprevisionid
       from billingslipforrevision
       where registdate <= /*deleteTime*/0);