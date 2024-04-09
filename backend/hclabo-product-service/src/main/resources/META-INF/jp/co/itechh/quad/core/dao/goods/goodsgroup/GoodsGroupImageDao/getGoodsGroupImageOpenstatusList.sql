SELECT
	  goodsgroup.goodsgroupseq
	, goodsgroup.goodsgroupcode
	, goodsgroup.goodsopenstatuspc
	, goodsgroupimage.*
FROM
	goodsgroupimage
	INNER JOIN
		goodsgroup
	ON
		goodsgroup.goodsgroupseq = goodsgroupimage.goodsgroupseq
ORDER BY
	goodsgroup.goodsgroupseq
offset /*offset*/0 limit /*limit*/0
;
