select
 *
from
 freearea
where
    true
    /*%if freeAreaEntity.shopSeq != null*/
        and shopseq = /*freeAreaEntity.shopSeq*/0
    /*%end*/
    /*%if freeAreaEntity.freeAreaSeq != null*/
        and freeAreaseq <> /*freeAreaEntity.freeAreaSeq*/0
    /*%end*/
    /*%if freeAreaEntity.freeAreaKey != null*/
        and freeAreaKey = /*freeAreaEntity.freeAreaKey*/0
    /*%end*/
    /*%if freeAreaEntity.openStartTime != null*/
        and openstarttime = /*freeAreaEntity.openStartTime*/0
    /*%end*/
