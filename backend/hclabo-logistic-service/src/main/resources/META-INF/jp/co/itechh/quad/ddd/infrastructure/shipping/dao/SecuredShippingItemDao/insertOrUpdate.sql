INSERT INTO SECUREDSHIPPINGITEM
(itemid, shippingitemseq, itemname, unittitle1, unitvalue1, unittitle2, unitvalue2, shippingcount, shippingslipid)
VALUES
    ( /*securedShippingItemDbEntity.itemId*/0
    , /*securedShippingItemDbEntity.shippingItemSeq*/0
    , /*securedShippingItemDbEntity.itemName*/0
    , /*securedShippingItemDbEntity.unitTitle1*/0
    , /*securedShippingItemDbEntity.unitValue1*/0
    , /*securedShippingItemDbEntity.unitTitle2*/0
    , /*securedShippingItemDbEntity.unitValue2*/0
    , /*securedShippingItemDbEntity.shippingCount*/0
    , /*securedShippingItemDbEntity.shippingSlipId*/0)

    ON CONFLICT (shippingitemseq, shippingslipid)
DO UPDATE SET
    shippingcount = /*securedShippingItemDbEntity.shippingCount*/0,
    itemname = /*securedShippingItemDbEntity.itemName*/0,
    unittitle1 = /*securedShippingItemDbEntity.unitTitle1*/0,
    unitvalue1 = /*securedShippingItemDbEntity.unitValue1*/0,
    unittitle2 = /*securedShippingItemDbEntity.unitTitle2*/0,
    unitvalue2 = /*securedShippingItemDbEntity.unitValue2*/0
