INSERT INTO ADJUSTMENTAMOUNT
(adjustmentamountseq, adjustname, adjustprice, salesslipid)
VALUES
    ( /*adjustmentAmountDbEntity.adjustmentAmountSeq*/0
    , /*adjustmentAmountDbEntity.adjustName*/0
    , /*adjustmentAmountDbEntity.adjustPrice*/0
    , /*adjustmentAmountDbEntity.salesSlipId*/0)

    ON CONFLICT (adjustmentamountseq, salesslipid)
DO UPDATE SET
    adjustname = /*adjustmentAmountDbEntity.adjustName*/0,
    adjustprice = /*adjustmentAmountDbEntity.adjustPrice*/0

