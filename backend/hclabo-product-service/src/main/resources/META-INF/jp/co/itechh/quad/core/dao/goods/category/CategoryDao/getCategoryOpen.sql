SELECT *
FROM category
WHERE
        shopseq = /*shopSeq*/0
  AND categoryid = /*categoryId*/0
-- /** frontDisplayReferenceDateが設定されている場合、Dtoを生成するタイミングで公開状態の判定処理を実施 **/
/*%if frontDisplayReferenceDate == null */
    /*%if openStatus != null && openStatus.value != null */
    AND categoryopenstatuspc = /*openStatus.value*/0
    /*%end*/
    AND (categoryopenstarttimepc IS NULL OR categoryopenstarttimepc <= CURRENT_TIMESTAMP)
    AND (categoryopenendtimepc IS NULL OR categoryopenendtimepc >= CURRENT_TIMESTAMP)
/*%end*/