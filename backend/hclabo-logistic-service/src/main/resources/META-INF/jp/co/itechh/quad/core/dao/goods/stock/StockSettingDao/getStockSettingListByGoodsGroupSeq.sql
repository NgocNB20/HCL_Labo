SELECT
  stocksetting.*
FROM
  stocksetting
WHERE
  stocksetting.goodsSeq IN /*goodsSeq*/(0)
ORDER BY stocksetting.goodsseq ASC
