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

WHERE
    npCond.noveltypresentstate='0'
/*%if conditionDto.noveltyPresentEndTimeTo != null*/
AND npCond.noveltyPresentStartTime <= /*conditionDto.noveltyPresentEndTimeTo*/''
/*%end*/
/*%if conditionDto.noveltyPresentStartTimeFrom != null*/
AND (npCond.noveltyPresentEndTime IS NULL
    OR npCond.noveltyPresentEndTime >= /*conditionDto.noveltyPresentStartTimeFrom*/''
    )
/*%end*/

/*%if conditionDto.noveltyPresentGoodsCountFrom != null*/
AND (stock.realstock - stocksetting.safetystock - stock.orderreservestock) >= /*conditionDto.noveltyPresentGoodsCountFrom*/0
/*%end*/

ORDER BY npCond.noveltyPresentStartTime ASC
