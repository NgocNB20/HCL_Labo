select
    goods.goodsCode,
    goods.unitValue1,
    goods.unitValue2,
    array_to_string(ARRAY[goodsgroupdisplay.goodstag], '/') as goodsTagSetting,
    goodsgroup.goodsGroupCode,
    goodsgroupdisplay.unitTitle1,
    goodsgroupdisplay.unitTitle2
from
    goodsgroup
        inner join
    goodsgroupdisplay on goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq
        inner join
    goods on goodsgroup.goodsgroupseq = goods.goodsgroupseq
where
        goods.goodsseq = /*goodsSeq*/0
  and goods.stockManagementFlag = '1'
