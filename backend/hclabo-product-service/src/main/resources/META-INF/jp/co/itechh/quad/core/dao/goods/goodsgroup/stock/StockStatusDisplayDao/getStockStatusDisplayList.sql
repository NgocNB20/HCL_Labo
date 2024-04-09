select
    stockstatusdisplay.*
from stockstatusdisplay
where
    goodsGroupSeq in /*goodsGroupSeqList*/(1,2,3);
