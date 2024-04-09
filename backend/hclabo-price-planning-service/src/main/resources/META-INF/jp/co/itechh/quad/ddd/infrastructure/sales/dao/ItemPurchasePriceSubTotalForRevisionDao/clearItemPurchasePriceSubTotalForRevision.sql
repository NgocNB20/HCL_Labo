delete
from itempurchasepricesubtotalforrevision
where salessliprevisionid in
      (select salessliprevisionid
       from salesslipforrevision
       where registdate <= /*deleteTime*/0);