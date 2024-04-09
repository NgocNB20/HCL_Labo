UPDATE stock
SET realStock = realStock + /*itemCount*/0,
    updatetime = CURRENT_TIMESTAMP
WHERE
    goodsseq = /*goodsSeq*/0