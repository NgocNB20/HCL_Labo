SELECT
	fp.*
FROM
	footprint AS fp
WHERE
	fp.shopSeq = /*conditionDto.shopSeq*/0
	AND fp.accessUid = /*conditionDto.accessUid*/0
	/*%if conditionDto.goodsGroupSeqList != null*/
		AND fp.goodsGroupSeq NOT IN /*conditionDto.goodsGroupSeqList*/(1,2,3)
	/*%end*/

ORDER BY

/*%if conditionDto.pageInfo.orderField != null*/
	/*%if conditionDto.pageInfo.orderField == "updateTime"*/
		fp.updateTime DESC
	/*%end*/
/*%else*/
	1 ASC
/*%end*/
