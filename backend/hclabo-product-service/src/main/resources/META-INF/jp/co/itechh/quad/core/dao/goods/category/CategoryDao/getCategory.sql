SELECT *
FROM category
WHERE
shopseq = /*shopSeq*/0
AND categoryid = /*categoryId*/0
-- /** frontDisplayReferenceDateが設定されている場合、Dtoを生成するタイミングで公開状態の判定処理を実施 **/
/*%if frontDisplayReferenceDate == null && openStatus != null && openStatus.value != null */
    AND categoryopenstatuspc = /*openStatus.value*/0

    -- 公開条件がOpenの時は公開期間も判定する
    /*%if openStatus.value == "1"*/
        AND (category.categoryopenstarttimepc <= CURRENT_TIMESTAMP OR category.categoryopenstarttimepc is null)
        AND (category.categoryopenendtimepc >= CURRENT_TIMESTAMP OR category.categoryopenendtimepc is null)
    /*%end*/
/*%end*/
