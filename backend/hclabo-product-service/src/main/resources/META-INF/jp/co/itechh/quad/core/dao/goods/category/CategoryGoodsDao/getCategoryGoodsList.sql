SELECT
    cg.*
     , c.categoryId
	, c.categoryName
	, c.categoryType
FROM
    categorygoods cg
    INNER JOIN Category c ON cg.categoryseq = c.categoryseq
WHERE
    cg.goodsGroupSeq in /*goodsGroupSeqList*/(1,2,3)
ORDER BY
    cg.goodsGroupSeq
        , c.categoryType ASC
        , c.categoryName ASC
