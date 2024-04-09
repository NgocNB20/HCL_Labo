SELECT
    *
FROM
    goodsGroupDisplay display
    INNER JOIN
    	goodsGroup goodsGroup
    ON
    	goodsGroup.goodsGroupSeq = display.goodsGroupSeq
WHERE
    goodsgroup.shopseq = /*shopSeq*/0
AND searchKeyword LIKE '%' || /*searchKeyword*/0 || '%' 
