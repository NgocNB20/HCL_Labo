UPDATE stock
SET realStock = realStock + /*supplementCount*/0,
    updatetime = CURRENT_TIMESTAMP
WHERE
        shopSeq = /*shopSeq*/0
  AND
        goodsseq = /*goodsSeq*/0
  AND
            realStock + /*supplementCount*/0 >= orderreservestock
