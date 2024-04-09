SELECT DISTINCT
    noveltyPresentConditionSeq
    ,noveltypresentstarttime
    ,noveltypresentendtime
    ,noveltypresentname
    ,noveltypresentstate
FROM
    noveltyPresentCondition
WHERE
	noveltyPresentState = '0'

/*%if noveltyPresentConditionSeq != null*/
AND noveltyPresentConditionSeq <> /*noveltyPresentConditionSeq*/null
/*%end*/
