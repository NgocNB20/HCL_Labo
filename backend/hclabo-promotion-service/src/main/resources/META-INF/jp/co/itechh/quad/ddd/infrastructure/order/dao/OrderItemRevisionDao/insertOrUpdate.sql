INSERT INTO ORDERITEMREVISION
(itemid, orderItemSeq, ordercount, itemname, unittitle1, unitvalue1, unittitle2, unitvalue2, jancode, ordersliprevisionid, noveltygoodstype, orderitemid)
VALUES
    ( /*orderItemRevisionDbEntity.itemId*/0
    , /*orderItemRevisionDbEntity.orderItemSeq*/0
    , /*orderItemRevisionDbEntity.orderCount*/0
    , /*orderItemRevisionDbEntity.itemName*/0
    , /*orderItemRevisionDbEntity.unitTitle1*/0
    , /*orderItemRevisionDbEntity.unitValue1*/0
    , /*orderItemRevisionDbEntity.unitTitle2*/0
    , /*orderItemRevisionDbEntity.unitValue2*/0
    , /*orderItemRevisionDbEntity.janCode*/0
    , /*orderItemRevisionDbEntity.orderSlipRevisionId*/0
    , /*orderItemRevisionDbEntity.noveltyGoodsType*/0
    , /*orderItemRevisionDbEntity.orderItemId*/0)

    ON CONFLICT (orderItemSeq, orderSlipRevisionId)
DO UPDATE SET
    ordercount = /*orderItemRevisionDbEntity.orderCount*/0,
    itemname = /*orderItemRevisionDbEntity.itemName*/0,
    unittitle1 = /*orderItemRevisionDbEntity.unitTitle1*/0,
    unitvalue1 = /*orderItemRevisionDbEntity.unitValue1*/0,
    unittitle2 = /*orderItemRevisionDbEntity.unitTitle2*/0,
    unitvalue2 = /*orderItemRevisionDbEntity.unitValue2*/0,
    jancode = /*orderItemRevisionDbEntity.janCode*/0,
    noveltygoodstype = /*orderItemRevisionDbEntity.noveltyGoodsType*/0,
    orderItemId = /*orderItemRevisionDbEntity.orderItemId*/0