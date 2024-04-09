SELECT DISTINCT
    goodsgroup.goodsgroupname,
	goods.goodsCode,
	goods.saleStatusPC,
	goods.saleStartTimePC,
	goods.saleEndTimePC,
	goods.unitValue1,
	goods.unitValue2,
	goods.jancode,
	goodsstockdisplay.realStock,
	goodsstockdisplay.orderReserveStock,
	(goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) AS salesPossibleStock,
	goodsstockdisplay.remainderFewStock,
    goodsstockdisplay.orderPointStock,
	goodsstockdisplay.safetyStock,
	goodsgroup.goodsGroupCode,
	goodsgroup.goodsOpenStatusPC,
	goodsgroup.openStartTimePC,
	goodsgroup.openEndTimePC
FROM
    goodsgroup
	inner join
    goodsgroupdisplay on goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq
/*%if conditionDto.categoryId != null*/
	inner join
	categorygoods on goodsgroup.goodsgroupseq = categorygoods.goodsgroupseq
	inner join
	category on categorygoods.categoryseq = category.categoryseq
/*%end*/
	inner join
    goods on goods.goodsgroupseq = goodsgroup.goodsgroupseq
	inner join
    goodsstockdisplay on goods.goodsseq = goodsstockdisplay.goodsseq
WHERE
	goodsgroup.shopseq = /*conditionDto.shopSeq*/0
	AND goods.shopSeq = /*conditionDto.shopSeq*/0
    AND
        goods.stockManagementFlag = '1'
	/*%if conditionDto.keywordLikeCondition1 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition1*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition2 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition2*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition3 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition3*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition4 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition4*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition5 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition5*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition6 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition6*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition7 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition7*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition8 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition8*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition9 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition9*/0 || '%'
	/*%end*/
	/*%if conditionDto.keywordLikeCondition10 != null*/
	AND
		goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition10*/0 || '%'
	/*%end*/
	/*%if conditionDto.categoryId != null*/
    AND
		category.categoryId LIKE /*conditionDto.categoryId*/0 || '%'
    /*%end*/
	/*%if conditionDto.goodsGroupCode != null*/
	AND
		goodsgroup.goodsGroupCode LIKE '%' || /*conditionDto.goodsGroupCode*/0 || '%'
	/*%end*/
	/*%if conditionDto.goodsCode != null*/
	AND
		goods.goodsCode LIKE '%' || /*conditionDto.goodsCode*/0 || '%'
	/*%end*/
    /*%if conditionDto.multiCode == "0" && conditionDto.multiCodeList != null*/
    AND goodsgroup.goodsGroupCode IN /*conditionDto.multiCodeList*/(1,2,3)
    /*%end*/
    /*%if conditionDto.multiCode == "1" && conditionDto.multiCodeList != null*/
    AND goods.goodsCode IN /*conditionDto.multiCodeList*/(1,2,3)
    /*%end*/
    /*%if conditionDto.multiCode == "2" && conditionDto.multiCodeList != null*/
    AND (goods.janCode IN /*conditionDto.multiCodeList*/(1,2,3)
        OR goods.catalogCode IN /*conditionDto.multiCodeList*/(1,2,3))
    /*%end*/
	/*%if conditionDto.multiCode == "3" && conditionDto.multiCodeList != null*/
		AND goods.unitImageCode IN /*conditionDto.multiCodeList*/(1,2,3)
	/*%end*/
    /*%if conditionDto.goodsGroupName != null*/
    AND (goodsgroup.goodsgroupName LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%'
        OR goods.unitValue1 LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%'
        OR goods.unitValue2 LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%')
    /*%end*/
    /*%if conditionDto.individualDeliveryType != null && conditionDto.individualDeliveryType.value != "0"*/
    AND
        goods.individualDeliveryType = /*conditionDto.individualDeliveryType.value*/0
    /*%end*/
	/*%if conditionDto.minPrice != null*/
	AND
		goods.goodsPrice >= /*conditionDto.minPrice*/0
	/*%end*/
	/*%if conditionDto.maxPrice != null*/
	AND
		goods.goodsPrice <= /*conditionDto.maxPrice*/0
	/*%end*/

AND (
/*%if conditionDto.site != "2" */
	(
		/*%if conditionDto.goodsOpenStatus != null*/
			/*%if conditionDto.saleStatus != null*/
				( goodsgroup.goodsOpenStatusPC = /*conditionDto.goodsOpenStatus.value*/0 /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC = /*conditionDto.saleStatus.value*/0
			/*%else*/
				( goodsgroup.goodsOpenStatusPC = /*conditionDto.goodsOpenStatus.value*/0 /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC != '9'
			/*%end*/
		/*%else*/
			/*%if conditionDto.saleStatus != null*/
				( goodsgroup.goodsOpenStatusPC != '9' /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC = /*conditionDto.saleStatus.value*/0
			/*%else*/
				( goodsgroup.goodsOpenStatusPC != '9' /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/)  AND goods.saleStatusPC != '9'
			/*%end*/
		/*%end*/

		/*%if conditionDto.openStartTimeFrom != null*/ AND goodsgroup.openStartTimePC >= /*conditionDto.openStartTimeFrom*/0 /*%end*/
		/*%if conditionDto.openStartTimeTo != null*/ AND goodsgroup.openStartTimePC <= /*conditionDto.openStartTimeTo*/0 /*%end*/
		/*%if conditionDto.openEndTimeFrom != null*/ AND goodsgroup.openEndTimePC >= /*conditionDto.openEndTimeFrom*/0 /*%end*/
		/*%if conditionDto.openEndTimeTo != null*/ AND goodsgroup.openEndTimePC <= /*conditionDto.openEndTimeTo*/0 /*%end*/
		/*%if conditionDto.saleStartTimeFrom != null*/ AND goods.saleStartTimePC >= /*conditionDto.saleStartTimeFrom*/0 /*%end*/
		/*%if conditionDto.saleStartTimeTo != null*/ AND goods.saleStartTimePC <= /*conditionDto.saleStartTimeTo*/0 /*%end*/
		/*%if conditionDto.saleEndTimeFrom != null*/ AND goods.saleEndTimePC >= /*conditionDto.saleEndTimeFrom*/0 /*%end*/
		/*%if conditionDto.saleEndTimeTo != null*/ AND goods.saleEndTimePC <= /*conditionDto.saleEndTimeTo*/0 /*%end*/
	)
/*%end*/
)

	/*%if conditionDto.deliveryMethod != null*/
	AND
		goods.possibledeliverymethod LIKE '%' || /*conditionDto.deliveryMethod*/0 || '%'
	/*%end*/
	/*%if conditionDto.stockTypeFlag != null && conditionDto.minStockCount != null*/
		/*%if conditionDto.stockTypeFlag == "0"*/
	AND	(goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) >= /*conditionDto.minStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "1"*/
	AND	goodsstockdisplay.realStock >= /*conditionDto.minStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "2"*/
	AND	goodsstockdisplay.safetystock >= /*conditionDto.minStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "3"*/
	AND	goodsstockdisplay.remainderfewstock >= /*conditionDto.minStockCount*/0
		/*%end*/
        /*%if conditionDto.stockTypeFlag == "4"*/
    AND	goodsstockdisplay.orderpointstock >= /*conditionDto.minStockCount*/0
        /*%end*/
	/*%end*/
	/*%if conditionDto.stockTypeFlag != null && conditionDto.maxStockCount != null*/
		/*%if conditionDto.stockTypeFlag == "0"*/
	AND	(goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) <= /*conditionDto.maxStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "1"*/
	AND	goodsstockdisplay.realStock <= /*conditionDto.maxStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "2"*/
	AND	goodsstockdisplay.safetystock <= /*conditionDto.maxStockCount*/0
		/*%end*/
		/*%if conditionDto.stockTypeFlag == "3"*/
	AND	goodsstockdisplay.remainderfewstock <= /*conditionDto.maxStockCount*/0
		/*%end*/
        /*%if conditionDto.stockTypeFlag == "4"*/
    AND	goodsstockdisplay.orderpointstock <= /*conditionDto.maxStockCount*/0
        /*%end*/
	/*%end*/

        /*%if conditionDto.orderPointStockBelow*/
    AND goodsstockdisplay.orderpointstock >= (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock)
        /*%end*/

-- /*************** sort ***************/
order by goodsgroup.goodsGroupCode ASC, goods.goodsCode ASC
