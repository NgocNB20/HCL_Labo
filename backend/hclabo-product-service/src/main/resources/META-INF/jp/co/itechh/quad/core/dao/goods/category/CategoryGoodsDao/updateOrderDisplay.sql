UPDATE
  categorygoods
SET manualOrderDisplay =
  (SELECT
    COALESCE(MAX(manualOrderDisplay) + 1, 1) AS manualOrderDisplay
   FROM
    categorygoods
   WHERE categorySeq = /*categorySeq*/0)
WHERE
  categorySeq = /*categorySeq*/0
  AND goodsGroupSeq =/*goodsGroupSeq*/0
