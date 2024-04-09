SELECT
  *
FROM
  news
WHERE
  shopseq=/*conditionDto.shopSeq*/0
/*%if conditionDto.openStatus != null && conditionDto.siteType != null */
  /*%if conditionDto.siteType.value == "0" || conditionDto.siteType.value == "1"*/
AND
  (newsopenstarttimepc is null OR newsopenstarttimepc <= CURRENT_TIMESTAMP)
AND
  (newsopenendtimepc is null OR newsopenendtimepc >= CURRENT_TIMESTAMP)
AND
  newsopenstatuspc = /*conditionDto.openStatus.value*/0
  /*%end*/
/*%end*/
ORDER BY
	/*%if conditionDto.pageInfo.orderField == "newstime"*/
	 NEWS.NEWSTIME DESC, NEWS.NEWSSEQ DESC
	/*%else*/
	 1 ASC
	/*%end*/
