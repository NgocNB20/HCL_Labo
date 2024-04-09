delete
from securedshippingitemforrevision
where shippingsliprevisionid in
      (select shippingsliprevisionid
       from shippingslipforrevision
       where registdate <= /*deleteTime*/0);