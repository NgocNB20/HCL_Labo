INSERT INTO ITEMPURCHASEPRICESUBTOTAL
(orderitemseq, itempurchasepricesubtotal, itemid, itemunitprice, itemcount, itemtaxrate, freecarriageitemflag, salesslipid)
VALUES
    ( /*itemPurchasePriceSubTotalDbEntity.orderItemSeq*/0
    , /*itemPurchasePriceSubTotalDbEntity.itemPurchasePriceSubTotal*/0
    , /*itemPurchasePriceSubTotalDbEntity.itemId*/0
    , /*itemPurchasePriceSubTotalDbEntity.itemUnitPrice*/0
    , /*itemPurchasePriceSubTotalDbEntity.itemCount*/0
    , /*itemPurchasePriceSubTotalDbEntity.itemTaxRate*/0
    , /*itemPurchasePriceSubTotalDbEntity.freeCarriageItemFlag*/TRUE
    , /*itemPurchasePriceSubTotalDbEntity.salesSlipId*/0)
    ON CONFLICT (orderItemSeq, salesslipid)
DO UPDATE SET
    itemPurchasePriceSubTotal = /*itemPurchasePriceSubTotalDbEntity.itemPurchasePriceSubTotal*/0,
    itemUnitPrice = /*itemPurchasePriceSubTotalDbEntity.itemUnitPrice*/0,
    itemTaxRate = /*itemPurchasePriceSubTotalDbEntity.itemTaxRate*/0,
    itemCount = /*itemPurchasePriceSubTotalDbEntity.itemCount*/0,
    freecarriageitemflag = /*itemPurchasePriceSubTotalDbEntity.freeCarriageItemFlag*/TRUE
