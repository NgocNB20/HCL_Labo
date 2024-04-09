select
 goodsrelation.*,
 goodsgroup.goodsgroupname,
 goodsgroup.goodsgroupcode,
 goodsgroup.goodsopenstatuspc
from
 goodsrelation,
 goodsgroup
where
 goodsrelation.goodsgroupseq = /*goodsGroupSeq*/0
 AND goodsrelation.goodsrelationgroupseq = goodsgroup.goodsgroupseq
order by goodsrelation.orderdisplay asc
