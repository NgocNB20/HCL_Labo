SELECT
    orderdisplay
FROM
    (
    SELECT shopseq,goodsgroupseq,orderdisplay,COUNT(*) AS duplicateCount
    FROM goods
    WHERE shopseq = /*shopSeq*/0
    AND goodsgroupseq = /*goodsGroupSeq*/0
    AND salestatuspc != '9'
    GROUP BY shopseq,goodsgroupseq,orderdisplay
    ) as duplicateOrderDisplay
WHERE
    duplicateOrderDisplay.duplicateCount > 1
