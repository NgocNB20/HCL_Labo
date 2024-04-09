SELECT
    stock.goodsseq,
    stock.shopseq,
    CASE WHEN stockSetting.stockManagementFlag = '1' THEN (stock.realStock - stock.orderreservestock - stockSetting.safetystock) ELSE 0 END AS salesPossibleStock,
    stock.realstock,
    stocksetting.remainderfewstock,
    stocksetting.safetystock,
    stocksetting.orderpointstock,
    stocksetting.registtime,
    stocksetting.updatetime
FROM
    Stock stock,
    StockSetting stocksetting
WHERE
        stock.goodsseq = stocksetting.goodsseq
  AND stock.goodsseq = /*goodsSeq*/0
