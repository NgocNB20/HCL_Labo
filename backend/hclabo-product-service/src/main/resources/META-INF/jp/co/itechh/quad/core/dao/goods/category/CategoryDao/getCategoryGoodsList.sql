SELECT
    categorygoods.categoryseq,
    categorygoods.goodsgroupseq,
    categorygoods.manualorderdisplay,
    categorygoods.registtime,
    categorygoods.updatetime,
    goodsgroup.goodsgroupcode,
    goodsgroup.goodsprice,
    goodsgroup.whatsnewdate,
    goodsgroup.goodsopenstatuspc,
    goodsgroup.openstarttimepc,
    goodsgroup.openendtimepc,
    goodsgroup.shopseq,
    goodsgroup.goodsgroupname,
    goodsgrouppopularity.popularitycount,
    CASE
        WHEN goodsgroup.goodsOpenStatusPC = '1' THEN
            CASE
            /*%if conditionDto.frontDisplayReferenceDate != null*/
                WHEN goodsgroup.openStartTimePC > /*conditionDto.frontDisplayReferenceDate*/0 OR goodsgroup.openEndTimePC < /*conditionDto.frontDisplayReferenceDate*/0 THEN '0'
            /*%end*/
            /*%if conditionDto.frontDisplayReferenceDate == null*/
                WHEN goodsgroup.openStartTimePC > CURRENT_TIMESTAMP OR goodsgroup.openEndTimePC < CURRENT_TIMESTAMP THEN '0'
            /*%end*/
            ELSE '1'
            END
        ELSE '0'
    END AS frontDisplay
FROM
    categorygoods INNER JOIN goodsgroup ON
    categorygoods.goodsgroupseq = goodsgroup.goodsgroupseq
INNER JOIN category ON
    categorygoods.categoryseq = category.categoryseq
INNER JOIN goodsgrouppopularity ON
    categorygoods.goodsgroupseq = goodsgrouppopularity.goodsgroupseq
WHERE
        category.categoryId = /*conditionDto.categoryId*/0

ORDER BY
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "popularityCount"*/
        /*%if conditionDto.pageInfo.orderAsc*/
            goodsgrouppopularity.popularityCount ASC
        /*%else*/
            goodsgrouppopularity.popularityCount DESC
        /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "normal"*/
        /*%if conditionDto.pageInfo.orderAsc*/
            categorygoods.manualorderdisplay ASC
        /*%else*/
            categorygoods.manualorderdisplay DESC
        /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "whatsnewdate"*/
        /*%if conditionDto.pageInfo.orderAsc*/
            goodsgroup.whatsnewdate ASC
        /*%else*/
            goodsgroup.whatsnewdate DESC
        /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "goodsGroupMinPrice"*/
        /*%if conditionDto.pageInfo.orderAsc*/
            goodsgroup.goodsprice ASC
        /*%else*/
            goodsgroup.goodsprice DESC
        /*%end*/
    /*%end*/
/*%else*/
    1 ASC
/*%end*/