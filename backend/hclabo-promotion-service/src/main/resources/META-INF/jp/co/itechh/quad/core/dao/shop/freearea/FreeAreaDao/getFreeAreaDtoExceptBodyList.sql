SELECT
    status_fa.*
FROM
    (
        SELECT
            *
            , CASE
                WHEN CURRENT_TIMESTAMP < openStartTime
                    THEN '2'   --公開予定
                WHEN open_fa.openTime > openStartTime
                    THEN '0'   --公開終了
                ELSE '1'   --公開中
                END AS freeareaopenstatus
        FROM
            (
                SELECT
                    *
                    , MAX(openStartTime) FILTER(WHERE openStartTime <= CURRENT_TIMESTAMP) OVER (PARTITION BY freeAreaKey) AS openTime
                FROM
                    freearea
            ) open_fa
    ) status_fa


-- /*BEGIN*/
where
/*%if conditionDto.shopSeq != null*/
     shopseq = /*conditionDto.shopSeq*/1
/*%end*/
/*%if conditionDto.freeAreaKey != null*/
 and status_fa.freeareakey like '%' || /*conditionDto.freeAreaKey*/0 || '%'
/*%end*/
/*%if conditionDto.freeAreaTitle != null*/
 and freeareatitle like '%' || /*conditionDto.freeAreaTitle*/0 || '%'
/*%end*/
/*%if conditionDto.openStartTimeTo != null*/
 and openstarttime <= /*conditionDto.openStartTimeTo*/0
/*%end*/
/*%if conditionDto.openStartTimeFrom != null*/
 and openstarttime >= /*conditionDto.openStartTimeFrom*/0
/*%end*/
/*%if conditionDto.openStatusList != null*/
 and freeAreaOpenStatus in /*conditionDto.openStatusList*/(0)
/*%end*/
/*%if conditionDto.siteMapFlag != null*/
 and sitemapflag = /*conditionDto.siteMapFlag*/0
/*%end*/

-- /*%end*/

/*************** sort ***************/
order by
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "freeAreaKey"*/
        status_fa.freeareakey /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "freeAreaTitle"*/
        freeareatitle /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "openStartTime"*/
        openstarttime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "siteMapFlag"*/
        siteMapFlag /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
