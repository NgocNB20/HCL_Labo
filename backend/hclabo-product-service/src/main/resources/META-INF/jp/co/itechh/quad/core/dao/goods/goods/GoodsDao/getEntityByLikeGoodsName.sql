SELECT
    *
FROM
    goods
LEFT OUTER JOIN
    goodsGroup
ON
    goods.goodsGroupSeq = goodsGroup.goodsGroupSeq
WHERE
    goods.shopSeq = /*shopSeq*/0
AND goodsGroup.goodsGroupName Like '%' || /*goodsName*/0 || '%' 
