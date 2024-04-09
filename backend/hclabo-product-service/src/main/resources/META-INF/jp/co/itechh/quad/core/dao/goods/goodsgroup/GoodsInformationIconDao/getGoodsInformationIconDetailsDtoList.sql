select
     icon.*
from
    goodsinformationicon as icon
where
    icon.shopseq = /*shopSeq*/0
order by icon.orderdisplay asc
