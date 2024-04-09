INSERT INTO SECUREDSHIPPINGITEMFORREVISION
(itemid, shippingitemseq, itemname, unittitle1, unitvalue1, unittitle2, unitvalue2, shippingcount, shippingSlipRevisionId)
VALUES
    ( /*securedShippingItemForRevisionDbEntity.itemId*/0
    , /*securedShippingItemForRevisionDbEntity.shippingItemSeq*/0
    , /*securedShippingItemForRevisionDbEntity.itemName*/0
    , /*securedShippingItemForRevisionDbEntity.unitTitle1*/0
    , /*securedShippingItemForRevisionDbEntity.unitValue1*/0
    , /*securedShippingItemForRevisionDbEntity.unitTitle2*/0
    , /*securedShippingItemForRevisionDbEntity.unitValue2*/0
    , /*securedShippingItemForRevisionDbEntity.shippingCount*/0
    , /*securedShippingItemForRevisionDbEntity.shippingSlipRevisionId*/0)

    ON CONFLICT (shippingItemSeq, shippingSlipRevisionId)
DO UPDATE SET
    shippingcount = /*securedShippingItemForRevisionDbEntity.shippingCount*/0
