SELECT
  GoodsInformationIcon.*
FROM
  GoodsInformationIcon
WHERE
  GoodsInformationIcon.iconSeq in /*informationIconSeqList*/(0)
ORDER BY
  GoodsInformationIcon.orderDisplay ASC
