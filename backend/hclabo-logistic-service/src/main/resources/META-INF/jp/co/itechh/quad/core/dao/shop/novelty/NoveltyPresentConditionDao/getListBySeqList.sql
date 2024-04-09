SELECT DISTINCT
    noveltyPresentConditionSeq
    ,noveltypresentstarttime
    ,noveltypresentendtime
    ,noveltypresentname
    ,noveltypresentstate
FROM
    noveltyPresentCondition
WHERE
/*%if noveltyPresentConditionSeqList != null*/
noveltyPresentConditionSeq in /*noveltyPresentConditionSeqList*/(0)
/*%end*/
