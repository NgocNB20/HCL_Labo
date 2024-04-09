select
    goodsgroup.goodsgroupcode,
    goodsgrouppopularity.*
from
    goodsgrouppopularity
inner join
    goodsgroup
on
	goodsgroup.goodsgroupseq = goodsgrouppopularity.goodsgroupseq
where
	goodsgroup.goodsopenstatuspc = '1'
and
	( goodsgroup.openendtimepc is null
	 or (goodsgroup.openendtimepc > CURRENT_TIMESTAMP and ( openstarttimepc is null or openstarttimepc <= CURRENT_TIMESTAMP )));
