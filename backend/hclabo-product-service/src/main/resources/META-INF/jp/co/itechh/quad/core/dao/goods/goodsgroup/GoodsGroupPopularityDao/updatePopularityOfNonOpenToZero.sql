update
    goodsgrouppopularity
set
    popularitycount = 0
FROM
    goodsgroup
WHERE
	goodsgrouppopularity.goodsgroupseq = goodsgroup.goodsgroupseq
AND
	( goodsgroup.goodsopenstatuspc != '1'
		OR
		( goodsgroup.goodsopenstatuspc = '1' AND goodsgroup.openendtimepc < CURRENT_TIMESTAMP )
	);
