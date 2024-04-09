SELECT
    npCond.noveltyPresentConditionSeq,
    npGoods.goodsseq
    ,stock.realstock
    ,stocksetting.safetystock
    ,stock.orderreservestock
    ,npGoods.priorityorder
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
    npCond.noveltyPresentConditionSeq IN /*noveltyPresentConditionSeqList*/() 

ORDER BY
      npCond.noveltyPresentConditionSeq
    , npGoods.priorityorder
