SELECT
    goods.goodsCode
    ,goods.saleStatusPC
    ,goods.stockManagementFlag
    ,goodsGroup.goodsOpenStatusPC
    ,goodsGroup.goodsPrice

FROM
    goods
    INNER JOIN
        goodsGroup
    ON
        goodsGroup.goodsGroupSeq = goods.goodsGroupSeq
WHERE
    goods.shopSeq = /*shopSeq*/0
AND goodsCode IN /*goodsCodeList*/(0) 
AND goodsGroup.noveltygoodstype = '1'
