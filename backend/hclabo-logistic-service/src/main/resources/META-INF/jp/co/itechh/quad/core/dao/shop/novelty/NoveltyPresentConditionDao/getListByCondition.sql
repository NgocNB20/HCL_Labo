SELECT DISTINCT
    npCond.*
FROM
    noveltyPresentCondition npCond
    LEFT OUTER JOIN
        (
        noveltyPresentEnclosureGoods npGoods
        INNER JOIN
            stock stock
        ON
            npGoods.goodsSeq = stock.goodsSeq
        INNER JOIN
            stocksetting stocksetting
        ON
            npGoods.goodsSeq = stocksetting.goodsSeq
        )
    ON
        npCond.noveltyPresentConditionSeq = npGoods.noveltyPresentConditionSeq

-- /*BEGIN*/
WHERE

/*%if conditionDto.noveltyPresentName != null*/
AND npCond.noveltyPresentName LIKE '%' || /*conditionDto.noveltyPresentName*/'' || '%'
/*%end*/
/*%if conditionDto.noveltyPresentState != null*/
AND npCond.noveltyPresentState in /*conditionDto.noveltyPresentState*/()
/*%end*/

/*%if conditionDto.noveltyPresentStartTimeFrom != null*/
AND npCond.noveltyPresentStartTime >= /*conditionDto.noveltyPresentStartTimeFrom*/''
/*%end*/
/*%if conditionDto.noveltyPresentStartTimeTo != null*/
AND npCond.noveltyPresentStartTime <= /*conditionDto.noveltyPresentStartTimeTo*/''
/*%end*/

/*%if conditionDto.noveltyPresentEndTimeFrom != null*/
AND npCond.noveltyPresentEndTime >= /*conditionDto.noveltyPresentEndTimeFrom*/''
/*%end*/
/*%if conditionDto.noveltyPresentEndTimeTo != null*/
AND npCond.noveltyPresentEndTime <= /*conditionDto.noveltyPresentEndTimeTo*/''
/*%end*/

/*%if conditionDto.noveltyPresentGoodsCode != null*/
AND npGoods.goodsSeq = /*conditionDto.noveltyGoodsSeq*/0
/*%end*/

/*%if conditionDto.noveltyPresentGoodsCountFrom != null*/
AND (stock.realstock - stocksetting.safetystock - stock.orderreservestock) >= /*conditionDto.noveltyPresentGoodsCountFrom*/0
/*%end*/
/*%if conditionDto.noveltyPresentGoodsCountTo != null*/
AND (stock.realstock - stocksetting.safetystock - stock.orderreservestock) <= /*conditionDto.noveltyPresentGoodsCountTo*/0
/*%end*/
/*%if conditionDto.goodsSeqList != null*/
AND npGoods.goodsSeq in /*conditionDto.goodsSeqList*/()
/*%end*/
-- /*%end*/

ORDER BY
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "noveltyPresentStartTime"*/
    npCond.noveltyPresentStartTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    ,npCond.noveltyPresentConditionSeq DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "noveltyPresentEndTime"*/
    npCond.noveltyPresentEndTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    ,npCond.noveltyPresentConditionSeq DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "noveltyPresentName"*/
    npCond.noveltyPresentName /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    ,npCond.noveltyPresentConditionSeq DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "noveltyPresentState"*/
    npCond.noveltyPresentState /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    ,npCond.noveltyPresentConditionSeq DESC
    /*%end*/
/*%else*/
    1 ASC
/*%end*/