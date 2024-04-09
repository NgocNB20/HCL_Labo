SELECT
    goodsstockdisplay.goodsseq,
    CASE WHEN goods.stockManagementFlag = '1' THEN (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) ELSE 0 END AS salesPossibleStock,
    goodsstockdisplay.realstock,
    CASE WHEN goods.stockManagementFlag = '1' THEN goodsstockdisplay.orderreservestock ELSE 0 END AS orderreservestock,
    goodsstockdisplay.remainderfewstock,
    goodsstockdisplay.safetystock,
    goodsstockdisplay.orderpointstock,
    goodsstockdisplay.registtime,
    goodsstockdisplay.updatetime
FROM
    Goodsstockdisplay goodsstockdisplay,
    Goods goods
WHERE
        goodsstockdisplay.goodsseq in /*goodsSeqList*/(1,2,3)
  AND goodsstockdisplay.goodsseq = goods.goodsseq