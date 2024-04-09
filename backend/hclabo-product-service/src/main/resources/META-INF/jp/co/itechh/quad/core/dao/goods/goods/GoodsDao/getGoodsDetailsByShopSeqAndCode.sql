select
    goods.*,
    goodsgroup.taxrate,
    goodsgroup.goodsgroupcode,
    goodsgroup.goodsprice,
    goodsgroup.whatsnewdate,
    goodsgroup.goodsopenstatuspc,
    goodsgroup.openstarttimepc,
    goodsgroup.openendtimepc,
    goodsgroup.goodsgroupname,
    goodsgroupdisplay.goodstag,
    goodsgroupdisplay.unittitle1,
    goodsgroupdisplay.unittitle2
from
        goods
 left join goodsgroup on( goods.goodsgroupseq = goodsgroup.goodsgroupseq )
 left join goodsgroupdisplay on( goods.goodsgroupseq = goodsgroupdisplay.goodsgroupseq )
where
        goodscode=/*goodsCode*/0
        and goodsgroup.shopseq=/*shopSeq*/0
/*%if goodsOpenStatus != null && siteType != null */
  /*%if siteType.value == "0" || siteType.value == "1" */
    AND (goodsgroup.openstarttimepc <= CURRENT_TIMESTAMP OR goodsgroup.openstarttimepc is null)
    AND (goodsgroup.openendtimepc >= CURRENT_TIMESTAMP OR goodsgroup.openendtimepc is null)
    AND goodsgroup.goodsopenstatuspc = /*goodsOpenStatus.value*/0
  /*%end*/
/*%end*/
