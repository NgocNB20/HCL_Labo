SELECT DISTINCT
    goodsgroup.goodsGroupseq
              , goodsgroup.goodsGroupCode
              , goodsgroup.goodsOpenStatusPC
              , goodsgroup.openStartTimePC
              , goodsgroup.openEndTimePC
              , goodsgroup.goodsGroupName
              , goodsgroup.goodsPrice
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
    /*%if !conditionDto.relationGoodsSearchFlag*/
              , goods.goodsSeq
              , goods.goodsCode
              , goods.unitValue1
              , goods.unitValue2
              , goods.saleStatusPC
              , goods.saleStartTimePC
              , goods.saleEndTimePC
              , goods.individualDeliveryType
              , goods.stockmanagementflag
              , CASE WHEN goods.stockManagementFlag = '1' THEN (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) ELSE 0 END AS salesPossibleStock
              , goodsstockdisplay.realStock
    /*%end*/
FROM
    GoodsGroup goodsgroup
   , GoodsGroupDisplay goodsgroupdisplay
   , Goods goods
/*%if conditionDto.categoryIdList != null*/
   , CategoryGoods categorygoods
   , Category category
/*%end*/
   , GoodsStockDisplay goodsstockdisplay
where
      goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq
  AND goodsgroup.goodsgroupseq = goods.goodsgroupseq
  AND goods.goodsSeq = goodsstockdisplay.goodsSeq
/*%if conditionDto.searchSettingKeywordList != null*/
/*%for searchSettingKeyword : conditionDto.searchSettingKeywordList */
  AND goodsgroupdisplay.searchsettingkeywordsemuc LIKE '%' || /* searchSettingKeyword */0 || '%'
/*%end*/
/*%end*/
/*%if conditionDto.goodsGroupCodeList != null*/
  AND goodsgroup.goodsGroupCode IN /*conditionDto.goodsGroupCodeList*/(1,2,3)
/*%end*/
/*%if conditionDto.goodsCodeList != null*/
  AND goods.goodsCode IN /*conditionDto.goodsCodeList*/(1,2,3)
/*%end*/
/*%if conditionDto.janCodeList != null*/
  AND goods.janCode IN /*conditionDto.janCodeList*/(1,2,3)
/*%end*/
/*%if conditionDto.categoryIdList != null*/
  AND goodsgroup.goodsGroupSeq = categorygoods.goodsGroupSeq
  AND categorygoods.categorySeq = category.categorySeq
/*%end*/
/*%if conditionDto.categoryIdList != null*/
  AND category.categoryId IN /*conditionDto.categoryIdList*/(1,2,3)
/*%end*/
/*%if conditionDto.shopSeq != null*/
  AND goodsgroup.shopSeq = /*conditionDto.shopSeq*/0
  AND goods.shopSeq = /*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.keywordLikeCondition1 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition1*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition2 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition2*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition3 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition3*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition4 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition4*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition5 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition5*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition6 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition6*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition7 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition7*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition8 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition8*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition9 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition9*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition10 != null*/
  AND goodsgroupdisplay.searchKeyword Like '%' || /*conditionDto.keywordLikeCondition10*/0 || '%'
/*%end*/
/*%if conditionDto.hasGoodsTagCondition()*/
  AND (
    /*%if conditionDto.goodsTagLikeCondition1 != null*/
      array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition1*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition2 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition2*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition3 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition3*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition4 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition4*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition5 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition5*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition6 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition6*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition7 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition7*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition8 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition8*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition9 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition9*/0 || '%'
    /*%end*/
    /*%if conditionDto.goodsTagLikeCondition10 != null*/
    OR array_to_string(ARRAY[goodsgroupdisplay.goodstag], ' ') Like '%' || /*conditionDto.goodsTagLikeCondition10*/0 || '%'
    /*%end*/
  )
/*%end*/
/*%if conditionDto.categoryId != null*/
  AND category.categoryId LIKE /*conditionDto.categoryId*/0 || '%'
/*%end*/
/*%if conditionDto.goodsGroupCode != null*/
  AND goodsgroup.goodsGroupCode LIKE '%' || /*conditionDto.goodsGroupCode*/0 || '%'
/*%end*/
/*%if conditionDto.goodsCode != null*/
  AND goods.goodsCode LIKE '%' || /*conditionDto.goodsCode*/0 || '%'
/*%end*/
/*%if conditionDto.janCode != null*/
  AND goods.janCode LIKE '%' || /*conditionDto.janCode*/0 || '%'
/*%end*/
/*%if conditionDto.multiCode == "0" && conditionDto.multiCodeList != null*/
  AND goodsgroup.goodsGroupCode IN /*conditionDto.multiCodeList*/(0)
/*%end*/
/*%if conditionDto.multiCode == "1" && conditionDto.multiCodeList != null*/
  AND goods.goodsCode IN /*conditionDto.multiCodeList*/(0)
/*%end*/
/*%if conditionDto.multiCode == "2" && conditionDto.multiCodeList != null*/
  AND goods.janCode IN /*conditionDto.multiCodeList*/(0)
/*%end*/
/*%if conditionDto.goodsGroupName != null*/
  AND (goodsgroup.goodsgroupName LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%'
    OR goods.unitValue1 LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%'
    OR goods.unitValue2 LIKE '%' || /*conditionDto.goodsGroupName*/0 || '%')
/*%end*/
/*%if conditionDto.individualDeliveryType != null && conditionDto.individualDeliveryType.value != "0"*/
  AND goods.individualDeliveryType = /*conditionDto.individualDeliveryType.value*/0
/*%end*/
/*%if conditionDto.minPrice != null*/
  AND goodsgroup.goodsPrice >= /*conditionDto.minPrice*/0
/*%end*/
/*%if conditionDto.maxPrice != null*/
  AND goodsgroup.goodsPrice <= /*conditionDto.maxPrice*/0
/*%end*/

/*%if conditionDto.site != "2" */
  AND (
    /*%if conditionDto.goodsOpenStatusList != null*/
        /*%if conditionDto.saleStatusList != null*/
            goodsgroup.goodsOpenStatusPC IN /*conditionDto.goodsOpenStatusList*/(0) AND goods.saleStatusPC IN /*conditionDto.saleStatusList*/(0)
        /*%else*/
        goodsgroup.goodsOpenStatusPC IN /*conditionDto.goodsOpenStatusList*/(0) AND goods.saleStatusPC != '9'
		/*%end*/
	/*%else*/
		/*%if conditionDto.saleStatusList != null*/
			goodsgroup.goodsOpenStatusPC != '9' AND goods.saleStatusPC IN /*conditionDto.saleStatusList*/(0)
		/*%else*/
			goodsgroup.goodsOpenStatusPC != '9' AND goods.saleStatusPC != '9'
		/*%end*/
	/*%end*/

		/*%if conditionDto.goodsOpenStartTimeFrom != null*/ AND goodsgroup.openStartTimePC >= /*conditionDto.goodsOpenStartTimeFrom*/0 /*%end*/
		/*%if conditionDto.goodsOpenStartTimeTo != null*/ AND goodsgroup.openStartTimePC <= /*conditionDto.goodsOpenStartTimeTo*/0 /*%end*/
		/*%if conditionDto.goodsOpenEndTimeFrom != null*/ AND goodsgroup.openEndTimePC >= /*conditionDto.goodsOpenEndTimeFrom*/0 /*%end*/
		/*%if conditionDto.goodsOpenEndTimeTo != null*/ AND goodsgroup.openEndTimePC <= /*conditionDto.goodsOpenEndTimeTo*/0 /*%end*/
		/*%if conditionDto.saleStartTimeFrom != null*/ AND goods.saleStartTimePC >= /*conditionDto.saleStartTimeFrom*/0 /*%end*/
		/*%if conditionDto.saleStartTimeTo != null*/ AND goods.saleStartTimePC <= /*conditionDto.saleStartTimeTo*/0 /*%end*/
		/*%if conditionDto.saleEndTimeFrom != null*/ AND goods.saleEndTimePC >= /*conditionDto.saleEndTimeFrom*/0 /*%end*/
		/*%if conditionDto.saleEndTimeTo != null*/ AND goods.saleEndTimePC <= /*conditionDto.saleEndTimeTo*/0 /*%end*/
    )
/*%end*/

-- /** フロント表示判定 **/
/*%if conditionDto.frontDisplayList != null*/
  AND (CASE
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
        END) IN /*conditionDto.frontDisplayList*/(0)
/*%end*/

/*%if conditionDto.registTimeFrom != null*/
  AND goodsgroup.registTime >= /*conditionDto.registTimeFrom*/0
/*%end*/
/*%if conditionDto.registTimeTo != null*/
  AND goodsgroup.registTime <= /*conditionDto.registTimeTo*/0
/*%end*/
/*%if conditionDto.updateTimeFrom != null*/
  AND goodsgroup.updateTime >= /*conditionDto.updateTimeFrom*/0
/*%end*/
/*%if conditionDto.updateTimeTo != null*/
  AND goodsgroup.updateTime <= /*conditionDto.updateTimeTo*/0
/*%end*/
/*%if conditionDto.snsLinkFlag != null*/
  AND goodsgroup.snslinkflag = /*conditionDto.snsLinkFlag.value*/0
/*%end*/
/*%if conditionDto.minSalesPossibleStock != null*/
  AND CASE
          WHEN goods.stockManagementFlag = '1' THEN (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock)  >= /*conditionDto.minSalesPossibleStock*/0
          ELSE FALSE
    END
/*%end*/
/*%if conditionDto.maxSalesPossibleStock != null*/
  AND CASE
          WHEN goods.stockManagementFlag = '1' THEN (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) <= /*conditionDto.maxSalesPossibleStock*/0
          ELSE FALSE
    END
/*%end*/
/*%if conditionDto.noveltyGoodsType != null && conditionDto.noveltyGoodsType.value != "0"*/
  AND goodsgroup.noveltyGoodsType = /*conditionDto.noveltyGoodsType.value*/'0'
/*%end*/

/*************** sort ***************/
ORDER BY
/*%if conditionDto.pageInfo.orderField != null*/

/*%if conditionDto.pageInfo.orderField == "goodsGroupCode"*/
    goodsgroup.goodsGroupCode /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
	/*%if !conditionDto.relationGoodsSearchFlag*/
		, goods.goodsCode /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
	/*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsCode"*/
goods.goodsCode /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsGroupName"*/
goodsgroup.goodsgroupName /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "unitValue1"*/
goods.unitValue1 /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "unitValue2"*/
goods.unitValue2 /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsOpenStatusPC"*/
goodsgroup.goodsOpenStatusPC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsOpenStartTimePC"*/
goodsgroup.openStartTimePC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsOpenEndTimePC"*/
goodsgroup.openEndTimePC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "saleStatusPC"*/
goods.saleStatusPC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "saleStartTimePC"*/
goods.saleStartTimePC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "saleEndTimePC"*/
goods.saleEndTimePC /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "goodsPrice"*/
goodsgroup.goodsPrice /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "salesPossibleStock"*/
salesPossibleStock /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "realStock"*/
goodsstockdisplay.realStock /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "individualDeliveryType"*/
goods.individualDeliveryType /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%else*/
    1 ASC
/*%end*/
