delete
from itempurchasepricesubtotal
where salesslipid in
      (select salesslipid
       from salesslip
       where salesstatus = 'DRAFT'
         and registdate <= /*deleteTime*/0);