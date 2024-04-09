UPDATE
  goods
SET orderdisplay =
  (SELECT
    COALESCE(MAX(orderdisplay) + 1, 1) AS orderdisplay
   FROM
    goods
   WHERE goodsGroupSeq = /*goodsGroupSeq*/0)
WHERE
  goodsSeq =/*goodsSeq*/0
