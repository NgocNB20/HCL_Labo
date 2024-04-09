INSERT INTO categoryGoods
(categorySeq, goodsGroupSeq, manualOrderDisplay, registTime, updateTime)
VALUES
(/*categoryGoodsEntity.categorySeq*/0
,/*categoryGoodsEntity.goodsGroupSeq*/0
,/*categoryGoodsEntity.manualOrderDisplay*/0
,/*categoryGoodsEntity.registTime*/0
,/*categoryGoodsEntity.updateTime*/0
)
ON CONFLICT (categorySeq, goodsGroupSeq)
DO UPDATE SET
     manualOrderDisplay = /*categoryGoodsEntity.manualOrderDisplay*/0
    ,updatetime = /*categoryGoodsEntity.updateTime*/0
