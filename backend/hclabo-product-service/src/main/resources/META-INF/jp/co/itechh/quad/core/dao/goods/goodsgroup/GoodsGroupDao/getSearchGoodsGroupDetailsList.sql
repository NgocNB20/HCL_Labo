SELECT * FROM (
	SELECT DISTINCT
		  goodsgroup.goodsgroupseq
		, goodsgroup.goodsgroupcode
		, goodsgroup.goodsprice
		/*%if conditionDto.getTaxRoundingModeStr() == "UP"*/
			, (CEIL(goodsgroup.goodsprice * goodsgroup.taxRate / 100) + goodsgroup.goodsprice) AS goodspriceincludedtax
		/*%else*/
			, (FLOOR(goodsgroup.goodsprice * goodsgroup.taxRate / 100) + goodsgroup.goodsprice) AS goodspriceincludedtax
		/*%end*/
		, goodsgroup.whatsnewdate
		, goodsgroup.goodsopenstatuspc
		, goodsgroup.shopseq
		, goodsgroup.goodsgroupname
		, goodsgroup.taxrate
		, goodsgroup.goodstaxtype
		, goodsgroupdisplay.goodstag
        , CASE
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
		, goodsgroupdisplay.goodsnote1
		, goodsgroupdisplay.goodsnote2
		, goodsgroupdisplay.goodsnote3
		, goodsgroupdisplay.goodsnote4
		, goodsgroupdisplay.goodsnote5
		, goodsgroupdisplay.goodsnote6
		, goodsgroupdisplay.goodsnote7
		, goodsgroupdisplay.goodsnote8
		, goodsgroupdisplay.goodsnote9
		, goodsgroupdisplay.goodsnote10
		, goodsgroupdisplay.orderSetting1
		, goodsgroupdisplay.orderSetting2
		, goodsgroupdisplay.orderSetting3
		, goodsgroupdisplay.orderSetting4
		, goodsgroupdisplay.orderSetting5
		, goodsgroupdisplay.orderSetting6
		, goodsgroupdisplay.orderSetting7
		, goodsgroupdisplay.orderSetting8
		, goodsgroupdisplay.orderSetting9
		, goodsgroupdisplay.orderSetting10
		, goodsgroupdisplay.informationiconpc
		, goodsgrouppopularity.popularitycount
		, stockStatusDisplay.stockstatuspc
		/*%if conditionDto.pageInfo.orderField == "normal"*/
			, categorygoods.manualorderdisplay
		/*%end*/
	FROM
		goodsgroup
		INNER JOIN goodsgroupdisplay
			ON goodsgroupdisplay.goodsgroupseq = goodsgroup.goodsgroupseq
		INNER JOIN goodsgrouppopularity
			ON goodsgrouppopularity.goodsgroupseq = goodsgroup.goodsgroupseq
		LEFT OUTER JOIN stockStatusDisplay
			ON stockStatusDisplay.goodsgroupseq = goodsgroup.goodsgroupseq

		/*%if conditionDto.pageInfo.orderField == "normal"*/
			INNER JOIN categorygoods
				ON categorygoods.goodsgroupseq = goodsgroup.goodsgroupseq
			INNER JOIN category
				ON category.categoryseq = categorygoods.categoryseq
                /*%if conditionDto.categoryId != null && conditionDto.categoryId != "" */
				    AND category.categoryid = /*conditionDto.categoryId*/0
                /*%end*/
		/*%end*/
	WHERE 1 = 1
		/*%if conditionDto.categoryId != null*/
			AND goodsgroup.goodsgroupseq IN (
				SELECT
					categorygoods.goodsgroupseq
				FROM
					category
					INNER JOIN categorygoods
						ON categorygoods.categoryseq = category.categoryseq
				WHERE
				    /*%if conditionDto.frontDisplayReferenceDate == null && conditionDto.openStatus.value == "1"*/
					    (category.categoryopenstarttimepc <= CURRENT_TIMESTAMP OR category.categoryopenstarttimepc IS NULL)
					    AND (category.categoryopenendtimepc >= CURRENT_TIMESTAMP OR category.categoryopenendtimepc IS NULL)
					/*%end*/
				    /*%if conditionDto.frontDisplayReferenceDate != null && conditionDto.openStatus.value == "1"*/
					    (category.categoryopenstarttimepc <= /*conditionDto.frontDisplayReferenceDate*/0 OR category.categoryopenstarttimepc IS NULL)
					    AND (category.categoryopenendtimepc >= /*conditionDto.frontDisplayReferenceDate*/0 OR category.categoryopenendtimepc IS NULL)
					/*%end*/
					AND category.categoryopenstatuspc = '1'
                    /*%if conditionDto.categoryId != null && conditionDto.categoryId != "" */
                        AND category.categoryid = /*conditionDto.categoryId*/0
                    /*%end*/
			)
		/*%end*/
		/*%if conditionDto.goodsGroupSeqList != null*/
			AND goodsgroup.goodsgroupseq IN /*conditionDto.goodsGroupSeqList*/(0)
		/*%end*/
		/*%if conditionDto.shopSeq != null*/
			AND goodsgroup.shopSeq = /*conditionDto.shopSeq*/0
		/*%end*/
		/*%if conditionDto.openStatus != null*/
		    /*%if conditionDto.frontDisplayReferenceDate == null && conditionDto.openStatus.value == "1"*/
    		    AND (openstarttimepc <= CURRENT_TIMESTAMP OR openstarttimepc IS NULL)
                AND (openendtimepc >= CURRENT_TIMESTAMP OR openendtimepc IS NULL)
            /*%end*/
            /*%if conditionDto.frontDisplayReferenceDate != null && conditionDto.openStatus.value == "1"*/
    		    AND (openstarttimepc <= /*conditionDto.frontDisplayReferenceDate*/0 OR openstarttimepc IS NULL)
                AND (openendtimepc >= /*conditionDto.frontDisplayReferenceDate*/0 OR openendtimepc IS NULL)
            /*%end*/
		  AND goodsopenstatuspc = /*conditionDto.openStatus.value*/0
		/*%end*/
		/*%if conditionDto.keywordLikeCondition1 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition1*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition2 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition2*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition3 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition3*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition4 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition4*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition5 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition5*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition6 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition6*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition7 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition7*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition8 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition8*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition9 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition9*/0 || '%'/*%end*/
		/*%if conditionDto.keywordLikeCondition10 != null*/AND goodsgroupdisplay.searchkeywordemuc LIKE '%' || /*conditionDto.keywordLikeCondition10*/0 || '%'/*%end*/
		/*%if conditionDto.stcockExistStatus != null*/
			AND stockStatusDisplay.stockstatuspc IN /*conditionDto.stcockExistStatus*/(0)
		/*%end*/
) goodsgroupdetail
WHERE 1 = 1
	/*%if conditionDto.minPrice != null*/
		AND goodsgroupdetail.goodsprice >= /*conditionDto.minPrice*/0
	/*%end*/
	/*%if conditionDto.maxPrice != null*/
		AND goodsgroupdetail.goodsprice <= /*conditionDto.maxPrice*/0
	/*%end*/
/*************** sort ***************/
ORDER BY
/*%if conditionDto.pageInfo.orderField == "whatsnewdate"*/
    /*%if conditionDto.pageInfo.orderAsc*/
        goodsgroupdetail.whatsnewdate ASC, goodsgroupdetail.goodsgroupseq ASC
    /*%else*/
        goodsgroupdetail.whatsnewdate DESC, goodsgroupdetail.goodsgroupseq DESC
    /*%end*/
/*%elseif conditionDto.pageInfo.orderField == "goodsGroupMinPrice"*/
    /*%if conditionDto.pageInfo.orderAsc*/
        goodsgroupdetail.goodsprice ASC, goodsgroupdetail.whatsnewdate DESC, goodsgroupdetail.goodsgroupseq DESC
    /*%else*/
        goodsgroupdetail.goodsprice DESC, goodsgroupdetail.whatsnewdate ASC, goodsgroupdetail.goodsgroupseq ASC
    /*%end*/
/*%elseif conditionDto.pageInfo.orderField == "normal"*/
    /*%if conditionDto.pageInfo.orderAsc*/
        goodsgroupdetail.manualorderdisplay ASC
    /*%else*/
        goodsgroupdetail.manualorderdisplay DESC
    /*%end*/
/*%elseif conditionDto.pageInfo.orderField == "popularityCount"*/
    /*%if conditionDto.pageInfo.orderAsc*/
        goodsgroupdetail.popularitycount ASC, goodsgroupdetail.whatsnewdate ASC, goodsgroupdetail.goodsgroupseq ASC
    /*%else*/
        goodsgroupdetail.popularitycount DESC, goodsgroupdetail.whatsnewdate DESC, goodsgroupdetail.goodsgroupseq DESC
    /*%end*/
/*%elseif conditionDto.pageInfo.orderField == "goodsGroupCode"*/
    /*%if conditionDto.pageInfo.orderAsc*/
    goodsgroupdetail.goodsGroupCode ASC
    /*%else*/
    goodsgroupdetail.goodsGroupCode DESC
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
