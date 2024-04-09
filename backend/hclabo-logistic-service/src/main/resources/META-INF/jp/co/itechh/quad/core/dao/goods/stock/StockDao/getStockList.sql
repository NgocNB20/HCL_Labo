SELECT
    stock.goodsseq,
    stock.shopseq,
	CASE WHEN stocksetting.stockManagementFlag = '1' THEN (stock.realStock - stock.orderreservestock - stockSetting.safetystock) ELSE 0 END AS salesPossibleStock,
    stock.realstock,
    CASE WHEN stocksetting.stockManagementFlag = '1' THEN stock.orderreservestock ELSE 0 END AS orderreservestock,
    stocksetting.remainderfewstock,
    stocksetting.safetystock,
    stocksetting.orderpointstock,
    stocksetting.registtime,
    stocksetting.updatetime
FROM
    stock,
    stocksetting
WHERE
    stock.goodsseq = stocksetting.goodsseq
    AND stock.goodsseq in /*goodsSeqList*/(1,2,3)
ORDER BY
    stock.goodsseq