INSERT INTO ADJUSTMENTAMOUNTFORREVISION
(adjustmentamountseq, adjustname, adjustprice, salessliprevisionid)
VALUES
    ( /*adjustmentAmountForRevisionDbEntity.adjustmentAmountSeq*/0
    , /*adjustmentAmountForRevisionDbEntity.adjustName*/0
    , /*adjustmentAmountForRevisionDbEntity.adjustPrice*/0
    , /*adjustmentAmountForRevisionDbEntity.salesSlipRevisionId*/0)

    ON CONFLICT (adjustmentamountseq, salessliprevisionid)
DO UPDATE SET
    adjustname = /*adjustmentAmountForRevisionDbEntity.adjustName*/0,
    adjustprice = /*adjustmentAmountForRevisionDbEntity.adjustPrice*/0

