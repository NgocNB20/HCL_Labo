INSERT INTO goodsstockdisplay
(goodsseq, remainderfewstock, orderpointstock, safetystock, realstock, orderreservestock, registtime, updatetime)
VALUES
    ( /*goodsStockDisplayEntity.goodsSeq*/0
    , /*goodsStockDisplayEntity.remainderFewStock*/0
    , /*goodsStockDisplayEntity.orderPointStock*/0
    , /*goodsStockDisplayEntity.safetyStock*/0
    , /*goodsStockDisplayEntity.realStock*/0
    , /*goodsStockDisplayEntity.orderReserveStock*/0
    , /*goodsStockDisplayEntity.registTime*/0
    , /*goodsStockDisplayEntity.updateTime*/0)

    ON CONFLICT (goodsseq)
DO UPDATE SET
    remainderfewstock = /*goodsStockDisplayEntity.remainderFewStock*/0,
    orderpointstock = /*goodsStockDisplayEntity.orderPointStock*/0,
    safetystock = /*goodsStockDisplayEntity.safetyStock*/0,
    realstock = /*goodsStockDisplayEntity.realStock*/0,
    orderreservestock = /*goodsStockDisplayEntity.orderReserveStock*/0,
    updatetime = /*goodsStockDisplayEntity.updateTime*/0
