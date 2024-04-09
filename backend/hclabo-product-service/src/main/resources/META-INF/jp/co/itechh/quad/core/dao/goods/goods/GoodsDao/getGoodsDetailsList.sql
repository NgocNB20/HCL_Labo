SELECT
    goods.*
     , goodsgroup.*
     , goodsgroup.goodsprice
     , goodsgroup.whatsnewdate
     , goodsgroup.goodsopenstatuspc
     , goodsgroup.openstarttimepc
     , goodsgroup.openendtimepc
     , goodsgroup.goodsgroupname
     , goodsgroup.snsLinkFlag
     , goodsgroup.goodstaxtype
     , goodsgroup.taxrate
     , goodsgroup.alcoholFlag
     , goodsgroupdisplay.deliverytype
     , goodsgroupdisplay.goodstag
     , goodsgroupdisplay.goodsNote1
     , goodsgroupdisplay.goodsNote2
     , goodsgroupdisplay.goodsNote3
     , goodsgroupdisplay.goodsNote4
     , goodsgroupdisplay.goodsNote5
     , goodsgroupdisplay.goodsNote6
     , goodsgroupdisplay.goodsNote7
     , goodsgroupdisplay.goodsNote8
     , goodsgroupdisplay.goodsNote9
     , goodsgroupdisplay.goodsNote10
     , goodsgroupdisplay.orderSetting1
     , goodsgroupdisplay.orderSetting2
     , goodsgroupdisplay.orderSetting3
     , goodsgroupdisplay.orderSetting4
     , goodsgroupdisplay.orderSetting5
     , goodsgroupdisplay.orderSetting6
     , goodsgroupdisplay.orderSetting7
     , goodsgroupdisplay.orderSetting8
     , goodsgroupdisplay.orderSetting9
     , goodsgroupdisplay.orderSetting10
     , goodsgroupdisplay.unittitle1
     , goodsgroupdisplay.unittitle2
     , goodsgroupdisplay.metaDescription
     , goodsstockdisplay.realstock
     , goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock as salesPossibleStock
     , goodsstockdisplay.orderreservestock
     , goodsstockdisplay.remainderfewstock
     , goodsstockdisplay.orderpointstock
     , goodsstockdisplay.safetystock
     , stockStatusDisplay.stockStatusPc

FROM
    goods
        INNER JOIN
    goodsgroup ON( goods.goodsgroupseq = goodsgroup.goodsgroupseq )
        INNER JOIN
    goodsgroupdisplay ON( goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq )
        INNER JOIN
    goodsstockdisplay ON( goods.goodsseq = goodsstockdisplay.goodsseq )
        LEFT OUTER JOIN
    stockStatusDisplay ON( stockStatusDisplay.goodsGroupSeq = goods.goodsGroupSeq )

WHERE
        goods.goodsSeq IN /*goodsSeqList*/(0);
