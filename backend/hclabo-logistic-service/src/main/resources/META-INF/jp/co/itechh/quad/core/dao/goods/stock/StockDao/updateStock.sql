UPDATE stock
SET orderreservestock = orderreservestock + /*itemCount*/0,
    updatetime = CURRENT_TIMESTAMP
    FROM stockSetting
WHERE stock.goodsseq = /*itemId*/0
  AND stock.goodsseq = stockSetting.goodsseq
  AND
    (
    (
    stockSetting.stockmanagementflag = '1'
  AND stock.realstock - stockSetting.safetystock - (stock.orderreservestock + /*itemCount*/0) >= 0
    )
   OR stockSetting.stockmanagementflag = '0'
    )