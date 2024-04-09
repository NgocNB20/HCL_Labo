SELECT
    category.*
    ,categorydisplay.categorynamepc
    ,categorydisplay.categorynotepc
    ,categorydisplay.freetextpc
    ,categorydisplay.metadescription
    ,categorydisplay.categoryimagepc
    ,categorydisplay.registtime as displayRegistTime
    ,categorydisplay.updatetime as displayUpdateTime
    ,categorygoodssort.goodssortcolumn
    ,categorygoodssort.goodssortorder
    ,categorycondition.conditiontype
    -- 検索条件
    ,array_to_string(
        ARRAY(
            SELECT 		ccd.conditionColumn  || ' '  ||  ccd.conditionOperator  || ' '  ||  ccd.conditionValue
			FROM		categoryconditiondetail ccd
			WHERE		ccd.categoryseq = category.categoryseq
            order by    ccd.conditionno Asc
        )
    , '|') as conditionDetailList
    -- 公開商品数
    ,(		SELECT		COUNT(*)
            FROM		categorygoods cg
                        INNER JOIN goodsgroup gg								ON
                    cg.goodsgroupseq = gg.goodsgroupseq							AND
                    gg.goodsOpenStatusPC = '1'									AND
                    /*%if conditionDto.frontDisplayReferenceDate == null*/
                        (gg.openstarttimepc <= CURRENT_TIMESTAMP OR gg.openstarttimepc is null)				AND
                        (gg.openendtimepc >= CURRENT_TIMESTAMP OR gg.openendtimepc is null)
                    /*%end*/
                    /*%if conditionDto.frontDisplayReferenceDate != null*/
                        (gg.openstarttimepc <= /*conditionDto.frontDisplayReferenceDate*/0 OR gg.openstarttimepc is null)				AND
                        (gg.openendtimepc >= /*conditionDto.frontDisplayReferenceDate*/0 OR gg.openendtimepc is null)
                    /*%end*/
			WHERE
					category.categoryseq = cg.categoryseq
	) as openGoodsCount
	-- フロント表示
	,CASE WHEN category.categoryopenstatuspc = '1' THEN
        CASE
        /*%if conditionDto.frontDisplayReferenceDate != null*/
            WHEN category.categoryopenstarttimepc > /*conditionDto.frontDisplayReferenceDate*/0 OR category.categoryopenendtimepc < /*conditionDto.frontDisplayReferenceDate*/0 THEN '0'
        /*%end*/
        /*%if conditionDto.frontDisplayReferenceDate == null*/
            WHEN category.categoryopenstarttimepc > CURRENT_TIMESTAMP OR category.categoryopenendtimepc < CURRENT_TIMESTAMP THEN '0'
        /*%end*/
        ELSE '1'
        END
    ELSE '0'
    END AS frontDisplay
FROM
    category
        INNER JOIN			categorydisplay					ON	category.categoryseq = categorydisplay.categoryseq
        INNER JOIN			categorygoodssort				ON	category.categoryseq = categorygoodssort.categoryseq
        LEFT JOIN			categorycondition				ON	category.categoryseq = categorycondition.categoryseq

WHERE
    category.shopseq = /*conditionDto.shopSeq*/0
    /*%if conditionDto.categorySearchKeyword != null*/
    AND
        (category.categoryId LIKE '%' || /*conditionDto.categorySearchKeyword*/0 || '%'  OR category.categoryName LIKE '%' || /*conditionDto.categorySearchKeyword*/0 || '%' )
    /*%end*/
    /*%if conditionDto.categoryCondition1 != null && (conditionDto.categoryCondition1.conditionColumn != "0" || (conditionDto.categoryCondition1.conditionColumn == "0" && conditionDto.categoryCondition1.conditionValue != null)) */
    AND EXISTS(
    SELECT 1
        FROM categoryconditiondetail ccd
        WHERE
            ccd.categoryseq = category.categoryseq
            -- カテゴリ条件明細の「条件項目」がALL以外
            /*%if conditionDto.categoryCondition1.conditionColumn != null && conditionDto.categoryCondition1.conditionColumn != "0" */
            AND	ccd.conditionColumn = /*conditionDto.categoryCondition1.conditionColumn*/0
            /*%end*/
            /*%if conditionDto.categoryCondition1.conditionValue != null */
            AND	ccd.conditionValue = /*conditionDto.categoryCondition1.conditionValue*/0
            /*%end*/
    )
    /*%end*/
    /*%if conditionDto.categoryCondition2 != null && (conditionDto.categoryCondition2.conditionColumn != "0" || (conditionDto.categoryCondition2.conditionColumn == "0" && conditionDto.categoryCondition2.conditionValue !=null))  */
    AND EXISTS(
        SELECT 2
        FROM categoryconditiondetail ccd
        WHERE
            ccd.categoryseq = category.categoryseq
          -- カテゴリ条件明細の「条件項目」がALL以外
            /*%if conditionDto.categoryCondition2.conditionColumn != null && conditionDto.categoryCondition2.conditionColumn != "0" */
            AND	ccd.conditionColumn = /*conditionDto.categoryCondition2.conditionColumn*/0
            /*%end*/
            /*%if conditionDto.categoryCondition2.conditionValue != null */
            AND	ccd.conditionValue = /*conditionDto.categoryCondition2.conditionValue*/0
            /*%end*/
    )
    /*%end*/
    /*%if conditionDto.categoryCondition3 != null && (conditionDto.categoryCondition3.conditionColumn != "0" || (conditionDto.categoryCondition3.conditionColumn == "0" && conditionDto.categoryCondition3.conditionValue !=null))  */
    AND EXISTS(
        SELECT 3
        FROM categoryconditiondetail ccd
        WHERE
            ccd.categoryseq = category.categoryseq
          -- カテゴリ条件明細の「条件項目」がALL以外
            /*%if conditionDto.categoryCondition3.conditionColumn != null && conditionDto.categoryCondition3.conditionColumn != "0" */
            AND	ccd.conditionColumn = /*conditionDto.categoryCondition3.conditionColumn*/0
            /*%end*/
            /*%if conditionDto.categoryCondition3.conditionValue != null */
            AND	ccd.conditionValue = /*conditionDto.categoryCondition3.conditionValue*/0
            /*%end*/
    )
    /*%end*/
    /*%if conditionDto.categoryCondition4 != null && (conditionDto.categoryCondition4.conditionColumn != "0" || (conditionDto.categoryCondition4.conditionColumn == "0" && conditionDto.categoryCondition4.conditionValue !=null)) */
    AND EXISTS(
        SELECT 4
        FROM categoryconditiondetail ccd
        WHERE
            ccd.categoryseq = category.categoryseq
          -- カテゴリ条件明細の「条件項目」がALL以外
            /*%if conditionDto.categoryCondition4.conditionColumn != null && conditionDto.categoryCondition4.conditionColumn != "0" */
            AND	ccd.conditionColumn = /*conditionDto.categoryCondition4.conditionColumn*/0
            /*%end*/
            /*%if conditionDto.categoryCondition4.conditionValue != null */
            AND	ccd.conditionValue = /*conditionDto.categoryCondition4.conditionValue*/0
             /*%end*/
    )
    /*%end*/
    /*%if conditionDto.categoryCondition5 != null && (conditionDto.categoryCondition5.conditionColumn != "0" || (conditionDto.categoryCondition5.conditionColumn == "0" && conditionDto.categoryCondition5.conditionValue !=null)) */
    AND EXISTS(
        SELECT 5
        FROM categoryconditiondetail ccd
        WHERE
            ccd.categoryseq = category.categoryseq
          -- カテゴリ条件明細の「条件項目」がALL以外
            /*%if conditionDto.categoryCondition5.conditionColumn != null && conditionDto.categoryCondition5.conditionColumn != "0" */
            AND	ccd.conditionColumn = /*conditionDto.categoryCondition5.conditionColumn*/0
            /*%end*/
            /*%if conditionDto.categoryCondition5.conditionValue != null */
            AND	ccd.conditionValue = /*conditionDto.categoryCondition5.conditionValue*/0
            /*%end*/
    )
    /*%end*/
    -- openStartTime openEndTime START
    /*%if conditionDto.openStartTimeFrom != null*/
    AND ( categoryopenstarttimepc >= /*conditionDto.openStartTimeFrom*/0 )
    /*%end*/
    /*%if conditionDto.openStartTimeTo != null*/
    AND ( categoryopenstarttimepc <= /*conditionDto.openStartTimeTo*/0 )
    /*%end*/

    /*%if conditionDto.openEndTimeFrom != null*/
    AND ( categoryopenendtimepc >= /*conditionDto.openEndTimeFrom*/0 )
    /*%end*/
    /*%if conditionDto.openEndTimeTo != null*/
    AND ( categoryopenendtimepc <= /*conditionDto.openEndTimeTo*/0 )
    /*%end*/
    -- openStartTime openEndTime END
    /*%if conditionDto.openStatus != null*/
    AND ( categoryopenstatuspc = /*conditionDto.openStatus.value*/0 )
    /*%end*/

    -- /** フロント表示判定 **/
    /*%if conditionDto.frontDisplayList != null && conditionDto.frontDisplayList.size() > 0*/
      AND (CASE
            WHEN category.categoryopenstatuspc = '1' THEN
                CASE
                    /*%if conditionDto.frontDisplayReferenceDate != null*/
                    WHEN category.categoryopenstarttimepc > /*conditionDto.frontDisplayReferenceDate*/0 OR category.categoryopenendtimepc < /*conditionDto.frontDisplayReferenceDate*/0 THEN '0'
                    /*%end*/
                    /*%if conditionDto.frontDisplayReferenceDate == null*/
                    WHEN category.categoryopenstarttimepc > CURRENT_TIMESTAMP OR category.categoryopenendtimepc < CURRENT_TIMESTAMP THEN '0'
                 /*%end*/
                ELSE '1'
                END
            ELSE '0'
            END) IN /*conditionDto.frontDisplayList*/(0)
    /*%end*/

    /*%if conditionDto.categoryTypeList != null && conditionDto.categoryTypeList.size() > 0 */
    AND ( categorytype IN /*conditionDto.categoryTypeList*/(0) )
    /*%end*/

/*************** sort ***************/
order by
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "categoryId"*/
    categoryId /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "categoryName"*/
         categoryName /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,categoryId DESC
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "openGoodsCount"*/
         openGoodsCount /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,categoryId DESC
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
