select
    goodsgroup.*
from
  goodsgroup
where
  goodsGroupSeq in /*goodsGroupSeqList*/(0)
  AND versionNo = /*versionNo*/0
for update nowait
