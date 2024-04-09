SELECT COUNT(*)
FROM
goods INNER JOIN goodsgroup ON
goods.goodsgroupseq = goodsgroup.goodsgroupseq
WHERE
goodsgroup.goodsOpenStatusPC != '9'
AND goods.salestatuspc != '9'
AND goods.shopseq = /*shopSeq*/0
AND goods.goodscode = /*goodsCode*/0
