delete from footprint
where
 accessUid = /*accessUid*/0
/*%if shopSeq != null*/
    and shopSeq = /*shopSeq*/0
/*%end*/
