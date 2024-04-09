INSERT INTO ORDERITEM
(itemid, orderItemSeq, ordercount, itemname, unittitle1, unitvalue1, unittitle2, unitvalue2, jancode, orderslipid, noveltygoodstype, orderitemid)
VALUES
    ( /*orderItemDbEntity.itemId*/0
    , /*orderItemDbEntity.orderItemSeq*/0
    , /*orderItemDbEntity.orderCount*/0
    , /*orderItemDbEntity.itemName*/0
    , /*orderItemDbEntity.unitTitle1*/0
    , /*orderItemDbEntity.unitValue1*/0
    , /*orderItemDbEntity.unitTitle2*/0
    , /*orderItemDbEntity.unitValue2*/0
    , /*orderItemDbEntity.janCode*/0
    , /*orderItemDbEntity.orderSlipId*/0
    , /*orderItemDbEntity.noveltyGoodsType*/0
    , /*orderItemDbEntity.orderItemId*/0)

    ON CONFLICT (orderItemSeq, orderslipid)
DO UPDATE SET
    ordercount = /*orderItemDbEntity.orderCount*/0,
    itemname = /*orderItemDbEntity.itemName*/0,
    unittitle1 = /*orderItemDbEntity.unitTitle1*/0,
    unitvalue1 = /*orderItemDbEntity.unitValue1*/0,
    unittitle2 = /*orderItemDbEntity.unitTitle2*/0,
    unitvalue2 = /*orderItemDbEntity.unitValue2*/0,
    jancode = /*orderItemDbEntity.janCode*/0,
    noveltygoodstype = /*orderItemDbEntity.noveltyGoodsType*/0,
    orderItemId = /*orderItemDbEntity.orderItemId*/0
