INSERT INTO CategoryGoods
(categorySeq, goodsGroupSeq, manualOrderDisplay, registTime, updateTime)
VALUES
    ( /*categorySeq*/0																
    ,/*goodsGroupSeq*/0																		
    , (SELECT COALESCE((SELECT MAX(manualOrderDisplay) + 1 FROM categorygoods WHERE categorySeq = /*categorySeq*/0),0))
    , now()																		
    , now())
ON CONFLICT (categorySeq, goodsGroupSeq)
DO UPDATE SET
    updatetime = now()