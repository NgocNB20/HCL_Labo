UPDATE stock
SET realstock = realstock - stockManagementCount.realUpGoodsCont,
    orderreservestock = orderreservestock - /*itemCount*/0,
    updatetime = CURRENT_TIMESTAMP
    FROM (  SELECT stockSetting.goodsseq, CASE WHEN stockSetting.stockmanagementflag = '1' then /*itemCount*/0 ELSE 0 END AS realUpGoodsCont
        FROM stockSetting
        WHERE stockSetting.goodsseq = /*itemId*/0
     ) AS stockManagementCount
WHERE stock.goodsseq = /*itemId*/0
  AND stock.goodsseq = stockManagementCount.goodsseq