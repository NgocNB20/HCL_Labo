SELECT
	COUNT(*) AS CNT
FROM
(
SELECT DISTINCT
	goods.*
FROM
	goods
	LEFT OUTER JOIN
		goodsGroup
	ON
		goods.goodsGroupSeq = goodsGroup.goodsGroupSeq

	LEFT OUTER JOIN
		categoryGoods
	ON
		categoryGoods.goodsGroupSeq = goodsGroup.goodsGroupSeq

	LEFT OUTER JOIN
		goodsGroupDisplay
	ON
		goodsGroupDisplay.goodsGroupSeq = goodsGroup.goodsGroupSeq
WHERE
1 = 1
/*%if conditionDto.goodsGroupCodeList != null && conditionDto.goodsGroupCodeList.size() > 0 */
AND goodsGroup.goodsGroupCode IN /*conditionDto.goodsGroupCodeList*/(0)
/*%end*/
/*%if conditionDto.goodsCodeList != null && conditionDto.goodsCodeList.size() > 0*/
AND goods.goodsCode IN /*conditionDto.goodsCodeList*/(0)
/*%end*/
/*%if conditionDto.categorySeqList != null && conditionDto.categorySeqList.size() > 0*/
AND categoryGoods.categorySeq IN /*conditionDto.categorySeqList*/(1,2,3)
/*%end*/

/*%if conditionDto.iconSeqList != null && conditionDto.iconSeqList.size() > 0*/
    AND ( goodsGroupDisplay.informationIconPc IN (
    /*%for item : conditionDto.iconSeqList*/
    /* item */'0'
    /*%if item_has_next */ , /*%end*/
    /*%end*/
    ))
/*%end*/

/*%if conditionDto.goodsNameList != null && conditionDto.goodsNameList.size() > 0*/
    AND (
    /*%for item : conditionDto.goodsNameList*/
            goodsGroup.goodsGroupName LIKE /*item*/0 || '%'
        /*%if item_has_next */ OR /*%end*/
    /*%end*/
    )
/*%end*/

/*%if conditionDto.searchKeywordList != null  && conditionDto.searchKeywordList.size() > 0*/
    AND (
    /*%for item : conditionDto.searchKeywordList*/
            goodsGroupDisplay.searchKeyword LIKE '%' || /*item*/'' || '%'
        OR goodsGroupDisplay.searchKeywordEmUc LIKE '%' || /*item*/'' || '%'
        /*%if item_has_next */ OR /*%end*/
    /*%end*/
    )
/*%end*/

) SUBTABLE
