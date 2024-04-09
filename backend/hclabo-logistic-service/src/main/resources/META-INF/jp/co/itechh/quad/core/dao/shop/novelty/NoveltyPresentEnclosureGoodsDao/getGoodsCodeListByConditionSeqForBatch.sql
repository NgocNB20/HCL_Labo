SELECT
    noveltyPresentEnclosureGoods.goodsseq
FROM
     noveltyPresentEnclosureGoods
    ,stock
    ,stockSetting
WHERE
        noveltyPresentConditionSeq = /*noveltyPresentConditionSeq*/0
    AND noveltyPresentEnclosureGoods.goodsseq = stock.goodsseq
    AND noveltyPresentEnclosureGoods.goodsseq = stockSetting.goodsseq
    AND stock.realstock - stockSetting.safetystock - orderreservestock >= 1

ORDER BY
    priorityorder ASC
