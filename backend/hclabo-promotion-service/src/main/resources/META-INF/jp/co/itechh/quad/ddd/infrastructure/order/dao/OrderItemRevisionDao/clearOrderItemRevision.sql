delete
from orderitemrevision
where ordersliprevisionid in
      (select ordersliprevisionid
          from orderslipforrevision
          where registdate <= /*deleteTime*/0);