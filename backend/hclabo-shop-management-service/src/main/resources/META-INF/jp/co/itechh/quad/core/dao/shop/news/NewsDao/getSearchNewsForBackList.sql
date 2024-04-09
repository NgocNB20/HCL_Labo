select
	newsseq,
	shopseq,
	titlepc,
	newsbodypc,
	newsurlpc,
	newsopenstatuspc,
	newsopenstarttimepc,
	newsopenendtimepc,
	newsnotepc,
	newstime,
	registtime,
	updatetime
from
 news
-- /*BEGIN*/
where
/*%if conditionDto.shopSeq != null*/
     shopseq=/*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.newsTimeTo != null*/
 and newstime <= /*conditionDto.newsTimeTo*/0
/*%end*/
/*%if conditionDto.newsTimeFrom != null*/
 and newstime >= /*conditionDto.newsTimeFrom*/0
/*%end*/
/*%if conditionDto.title != null*/
 and ( titlepc like '%' || /*conditionDto.title*/0 || '%' )
/*%end*/

/*%if conditionDto.bodyNote != null*/
 and (newsbodypc like '%' || /*conditionDto.bodyNote*/0 || '%' or newsnotepc like '%' || /*conditionDto.bodyNote*/0 || '%')
/*%end*/
/*%if conditionDto.url != null*/
 and ( newsurlpc like '%' || /*conditionDto.url*/0 || '%' )
/*%end*/

/*%if conditionDto.openStatus != null*/
 and newsopenstatuspc = /*conditionDto.openStatus.value*/0
/*%end*/

-- openStartTime openEndTime START
/*%if conditionDto.openStartTimeFrom != null*/
  and ( newsopenstarttimepc >= /*conditionDto.openStartTimeFrom*/0 )
/*%end*/
/*%if conditionDto.openStartTimeTo != null*/
  and ( newsopenstarttimepc <= /*conditionDto.openStartTimeTo*/0 )
/*%end*/

/*%if conditionDto.openEndTimeFrom != null*/
  and ( newsopenendtimepc >= /*conditionDto.openEndTimeFrom*/0 )
/*%end*/
/*%if conditionDto.openEndTimeTo != null*/
  and ( newsopenendtimepc <= /*conditionDto.openEndTimeTo*/0 )
/*%end*/
-- openStartTime openEndTime END

--%end

/*************** sort ***************/
order by
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "newsSeq"*/
         newsSeq /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "newsTime"*/
         newsTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,newsseq DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "newsTitlePC"*/
         titlePC /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,newsseq DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "newsOpenStatusPC"*/
         newsOpenStatusPC /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,newsseq DESC
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
