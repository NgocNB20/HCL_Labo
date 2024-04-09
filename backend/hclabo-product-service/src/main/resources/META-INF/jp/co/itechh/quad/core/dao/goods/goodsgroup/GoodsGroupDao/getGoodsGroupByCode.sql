SELECT
	goodsgroup.*
FROM
	goodsgroup
WHERE
	goodsgroup.shopseq = /*shopSeq*/0
	/*%if goodsGroupCode != null && goodsGroupCode != ""*/
		AND goodsgroup.goodsGroupCode = /*goodsGroupCode*/0
	/*%end*/
	/*%if goodsCode != null && goodsCode != ""*/
		AND EXISTS (
			SELECT * FROM goods
			WHERE goodsgroupseq = goodsgroup.goodsgroupseq AND goodscode = /*goodsCode*/0
		)
	/*%end*/
-- /** frontDisplayReferenceDateが設定されている場合、Dtoを生成するタイミングで公開状態の判定処理を実施 **/
	/*%if frontDisplayReferenceDate==null && openStatus!=null && siteType != null*/
		/*%if siteType.value == "0" */
			AND (goodsgroup.openstarttimepc <= CURRENT_TIMESTAMP OR goodsgroup.openstarttimepc is null)
			AND (goodsgroup.openendtimepc >= CURRENT_TIMESTAMP OR goodsgroup.openendtimepc is null)
			AND goodsgroup.goodsopenstatuspc = /*openStatus.value*/0
		/*%end*/
	/*%end*/