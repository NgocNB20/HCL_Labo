SELECT *
FROM category
WHERE 1=1
/*%if conditionDto.categorySeqList != null*/
AND categoryseq in /*conditionDto.categorySeqList*/(1,2,3)
/*%end*/
/*%if conditionDto.shopSeq != null*/
AND shopseq = /*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.openStatus != null && conditionDto.siteType != null */
  /*%if conditionDto.siteType.value == "0" || conditionDto.siteType.value == "1"  */
    AND (category.categoryopenstarttimepc <= CURRENT_TIMESTAMP OR category.categoryopenstarttimepc is null)
    AND (category.categoryopenendtimepc >= CURRENT_TIMESTAMP OR category.categoryopenendtimepc is null)
    AND categoryopenstatuspc = /*conditionDto.openStatus.value*/0
  /*%end*/
/*%end*/

order by
/*%if conditionDto.orderField != null*/
  /*%if conditionDto.orderField == "categorypath"*/
    category.categorypath
  /*%end*/
  /*%if conditionDto.orderAsc */
    asc
  /*%else*/
    desc
  /*%end*/
/*%end*/

