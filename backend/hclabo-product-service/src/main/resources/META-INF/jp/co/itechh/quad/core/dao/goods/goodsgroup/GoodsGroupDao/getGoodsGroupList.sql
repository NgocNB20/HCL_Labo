SELECT
	goodsgroup.*
	, goods.goodscode
FROM
	goods
	INNER JOIN
	goodsgroup ON( goods.goodsgroupseq = goodsgroup.goodsgroupseq )
WHERE
	goodsgroup.goodsGroupSeq IN /*goodsGroupSeqList*/(0);
