UPDATE stock
SET orderreservestock = orderreservestock + /*itemCount*/0,
    updatetime = CURRENT_TIMESTAMP
WHERE stock.goodsseq = /*itemId*/0
