delete
from orderreceived
where latesttransactionid is null
  and registdate <= /*deleteTime*/0