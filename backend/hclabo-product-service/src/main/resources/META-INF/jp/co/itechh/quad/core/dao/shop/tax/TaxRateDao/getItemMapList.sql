select
  rate || '%' as taxrate
  , rate
from
  tax
  inner join taxrate
    on tax.taxseq = taxrate.taxseq
where
  tax.startTime <= CURRENT_TIMESTAMP
  and tax.endTime >= CURRENT_TIMESTAMP
order by
  orderdisplay asc;
