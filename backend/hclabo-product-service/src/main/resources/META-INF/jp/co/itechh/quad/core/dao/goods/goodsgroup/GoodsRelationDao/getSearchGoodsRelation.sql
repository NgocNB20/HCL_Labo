SELECT
	gr.*
FROM
	goodsRelation AS gr
	INNER JOIN goodsGroup AS gg ON gg.goodsGroupSeq = gr.goodsRelationGroupSeq
WHERE
	gr.goodsGroupSeq = /*conditionDto.goodsGroupSeq*/0
	/*%if conditionDto.frontDisplayReferenceDate == null && conditionDto.openStatus != null && conditionDto.openStatus.value == "1" */
		AND (gg.openStartTimePC <= CURRENT_TIMESTAMP OR gg.openStartTimePC IS NULL)
		AND (gg.openEndTimePC >= CURRENT_TIMESTAMP OR gg.openEndTimePC IS NULL)
		AND gg.goodsOpenStatusPC = /*conditionDto.openStatus.value*/0
	/*%end*/
    -- /** フロント表示基準日時が設定されているかつ公開状態の場合、基準日時で公開状態を判定（プレビュー画面で利用） **/
	/*%if conditionDto.frontDisplayReferenceDate != null && conditionDto.openStatus != null && conditionDto.openStatus.value == "1"*/
		AND (gg.openStartTimePC <= /*conditionDto.frontDisplayReferenceDate*/0 OR gg.openStartTimePC IS NULL)
		AND (gg.openEndTimePC >= /*conditionDto.frontDisplayReferenceDate*/0 OR gg.openEndTimePC IS NULL)
		AND gg.goodsOpenStatusPC = /*conditionDto.openStatus.value*/0
    /*%end*/
	/*%if conditionDto.goodsGroupSeqList != null*/
		AND gr.goodsRelationGroupSeq NOT IN /*conditionDto.goodsGroupSeqList*/(0)
	/*%end*/
ORDER BY gr.orderDisplay ASC
