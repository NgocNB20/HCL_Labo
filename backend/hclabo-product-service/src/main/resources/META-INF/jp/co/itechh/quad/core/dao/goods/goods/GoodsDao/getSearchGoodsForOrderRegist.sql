SELECT DISTINCT
    goods.goodsSeq,
    goods.goodsCode,
    goods.janCode,
    goodsgroup.goodsPrice,
    goods.freeDeliveryFlag,
    goods.unitValue1,
    goods.unitValue2,
    goodsgroup.taxRate,
    goodsgroup.goodsTaxType,
    goodsgroup.goodsGroupCode,
    goodsgroup.goodsGroupName,
    goods.individualDeliveryType,
    goods.stockManagementFlag,
    CASE
        WHEN goods.stockManagementFlag = '0' THEN
            0
        ELSE
            (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock)
        END AS salesPossibleStock,
    goods.unitManagementFlag,
    goodsgroupdisplay.deliveryType,
    goodsgroupdisplay.orderSetting1,
    goodsgroupdisplay.orderSetting2,
    goodsgroupdisplay.orderSetting3,
    goodsgroupdisplay.orderSetting4,
    goodsgroupdisplay.orderSetting5,
    goodsgroupdisplay.orderSetting6,
    goodsgroupdisplay.orderSetting7,
    goodsgroupdisplay.orderSetting8,
    goodsgroupdisplay.orderSetting9,
    goodsgroupdisplay.orderSetting10
FROM
    goodsgroup
   , goodsgroupdisplay
   , goods
   , goodsstockdisplay
/*%if conditionDto.categoryId != null*/
   , categorygoods
   , category
/*%end*/
WHERE
        goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq
  AND goodsgroup.goodsgroupseq = goods.goodsgroupseq
  AND goods.goodsseq = goodsstockdisplay.goodsseq
  AND (goods.stockManagementFlag = '0' OR (goodsstockdisplay.realStock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) > 0)
/*%if conditionDto.categoryId != null*/
  AND goodsgroup.goodsGroupSeq = categorygoods.goodsGroupSeq
  AND categorygoods.categorySeq = category.categorySeq
/*%end*/
/*%if conditionDto.shopSeq != null*/
  AND goodsgroup.shopSeq = /*conditionDto.shopSeq*/0
  AND goods.shopSeq = /*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.keywordLikeCondition1 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition1*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition2 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition2*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition3 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition3*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition4 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition4*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition5 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition5*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition6 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition6*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition7 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition7*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition8 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition8*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition9 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition9*/0 || '%'
/*%end*/
/*%if conditionDto.keywordLikeCondition10 != null*/
  AND goodsgroupdisplay.searchKeyword LIKE '%' || /*conditionDto.keywordLikeCondition10*/0 || '%'
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
    /*%if conditionDto.goodsOpenStatus != null*/
    /*%if conditionDto.saleStatus != null*/
        ( goodsgroup.goodsOpenStatusPC = /*conditionDto.goodsOpenStatus.value*/0 /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC = /*conditionDto.saleStatus.value*/0
    /*%else*/
    (goodsgroup.goodsOpenStatusPC = /*conditionDto.goodsOpenStatus.value*/0 /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC != '9'
    /*%end*/
    /*%else*/
    /*%if conditionDto.saleStatus != null*/
    ( goodsgroup.goodsOpenStatusPC != '9' /*%if conditionDto.deleteStatusDsp*/ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC = /*conditionDto.saleStatus.value*/0
    /*%else*/
    (goodsgroup.goodsOpenStatusPC != '9' /*%if conditionDto.deleteStatusDsp */ OR goodsgroup.goodsOpenStatusPC = '9' /*%end*/) AND goods.saleStatusPC != '9'
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


/*%if !conditionDto.deleteStatusDsp*/
  AND (goods.saleStatusPC != '9' AND goodsgroup.goodsOpenStatusPC != '9')
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

/*************** sort ***************/
ORDER BY
/*%if conditionDto.pageInfo.orderField != null*/

/*%if conditionDto.pageInfo.orderField == "goodsGroupCode"*/
    goodsgroup.goodsGroupCode /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/,
    goods.goodsCode /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
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

/*%if conditionDto.pageInfo.orderField == "goodsPrice"*/
    goodsgroup.goodsPrice /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "individualDeliveryType"*/
    goods.individualDeliveryType /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "janCode"*/
    goods.janCode /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "salesPossibleStock"*/
    (goodsstockdisplay.realstock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) /*%if conditionDto.pageInfo.orderAsc */ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%else*/
    1 ASC
/*%end*/
