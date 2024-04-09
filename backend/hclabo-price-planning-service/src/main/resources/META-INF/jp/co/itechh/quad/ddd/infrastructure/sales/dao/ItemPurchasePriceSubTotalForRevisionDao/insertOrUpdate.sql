INSERT INTO ITEMPURCHASEPRICESUBTOTALFORREVISION
(orderitemseq, itempurchasepricesubtotal, itemid, itemunitprice, itemcount, itemtaxrate, freecarriageitemflag, salessliprevisionid)
VALUES
    ( /*itemPurchasePriceSubTotalForRevisionDbEntity.orderItemSeq*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.itemPurchasePriceSubTotal*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.itemId*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.itemUnitPrice*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.itemCount*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.itemTaxRate*/0
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.freeCarriageItemFlag*/TRUE
    , /*itemPurchasePriceSubTotalForRevisionDbEntity.salesSlipRevisionId*/0)

    ON CONFLICT (orderItemSeq, salessliprevisionid)
DO UPDATE SET
    itemPurchasePriceSubTotal = /*itemPurchasePriceSubTotalForRevisionDbEntity.itemPurchasePriceSubTotal*/0,
    itemUnitPrice = /*itemPurchasePriceSubTotalForRevisionDbEntity.itemUnitPrice*/0,
    itemTaxRate = /*itemPurchasePriceSubTotalForRevisionDbEntity.itemTaxRate*/0,
    itemCount = /*itemPurchasePriceSubTotalForRevisionDbEntity.itemCount*/0,
    freecarriageitemflag = /*itemPurchasePriceSubTotalForRevisionDbEntity.freeCarriageItemFlag*/TRUE
