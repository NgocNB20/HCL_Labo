select
    stockresult.*
from
    stockresult
where
    stockresult.goodsSeq = /*conditionDto.goodsSeq*/0
    and stockresult.supplementcount != 0

-- /*************** sort ***************/
order by
/*%if conditionDto.pageInfo.orderField == "stockResultSeq"*/
 stockresult.stockresultseq /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
/*%else*/
    1 ASC
/*%end*/
