SELECT
    goodsgroup.*
FROM
    goodsgroup INNER JOIN goods
ON goodsgroup.goodsgroupseq = goods.goodsgroupseq
WHERE
    goodsgroup.shopseq = /*shopSeq*/0
AND goodsGroupCode IN /*goodsGroupCodeList*/(0) 
