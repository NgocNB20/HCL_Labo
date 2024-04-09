SELECT
    gg.*
FROM
	goodsgroup gg
WHERE
    gg.shopseq = /*shopSeq*/0
AND
	LOWER(gg.goodsGroupCode) = LOWER(/*goodsGroupCode*/0)
