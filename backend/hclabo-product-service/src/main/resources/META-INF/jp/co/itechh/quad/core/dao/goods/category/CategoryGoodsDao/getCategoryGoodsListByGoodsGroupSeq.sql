SELECT cg.*
        , c.categoryId
        , c.categoryName
        , c.categoryType
FROM
    categorygoods cg
        INNER JOIN Category c ON cg.categoryseq = c.categoryseq
WHERE goodsGroupSeq = /*goodsGroupSeq*/0
ORDER BY categorySeq ASC
