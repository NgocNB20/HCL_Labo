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
ORDER BY
    priorityorder ASC
