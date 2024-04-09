select
    goodsgroup.*
from goodsgroup
where
    goodsgroup.shopseq = /*shopSeq*/0
    AND goodsgroup.goodsGroupCode in /*goodsGroupCodeList*/(0)
