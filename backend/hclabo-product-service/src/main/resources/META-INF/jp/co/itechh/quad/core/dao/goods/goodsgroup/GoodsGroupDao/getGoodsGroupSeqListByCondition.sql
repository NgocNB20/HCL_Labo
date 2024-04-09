select goodsgroup.goodsgroupseq
from goodsgroup
         inner join goodsgroupdisplay on goodsgroup.goodsgroupseq = goodsgroupdisplay.goodsgroupseq
         inner join goods on goodsgroup.goodsgroupseq = goods.goodsgroupseq
         inner join goodsstockdisplay on goods.goodsseq = goodsstockdisplay.goodsseq
where
/*%if categoryConditionDetailEntityList != null */
(
    /*%for entity : categoryConditionDetailEntityList */
        /*%if entity.conditionColumn != null */
            /*%if entity.conditionColumn == "1" */
                /*%if entity.conditionOperator == "0" */goodsgroup.goodsgroupcode = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */goodsgroup.goodsgroupcode != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */goodsgroup.goodsgroupcode LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */goodsgroup.goodsgroupcode LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */goodsgroup.goodsgroupcode LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */goodsgroup.goodsgroupcode NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "2" */
                /*%if entity.conditionOperator == "0" */goodsgroup.goodsgroupname = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */goodsgroup.goodsgroupname != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */goodsgroup.goodsgroupname LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */goodsgroup.goodsgroupname LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */goodsgroup.goodsgroupname LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */goodsgroup.goodsgroupname NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "3" */
                /*%if entity.conditionOperator == "0" *//*entity.conditionValue*/0 = any(goodsgroupdisplay.goodstag)/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "4" */
                /*%if entity.conditionOperator == "0" */
                    /*%if entity.conditionValues != null */
                        (
                        /*%for element : entity.conditionValues */
                            goodsgroupdisplay.informationiconpc LIKE '%' || /*element*/0 || '%'
                            /*%if element_has_next */
                                /*# "or" */
                            /*%end*/
                        /*%end*/
                        )
                    /*%else*/ false
                    /*%end*/
                /*%end*/
                /*%if entity.conditionOperator == "8" */goodsgroupdisplay.informationiconpc IS NOT NULL/*%end*/
                /*%if entity.conditionOperator == "9" */goodsgroupdisplay.informationiconpc IS NULL/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "5" */
                /*%if entity.conditionOperator == "0" */goodsgroupdisplay.deliverytype = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */goodsgroupdisplay.deliverytype != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */goodsgroupdisplay.deliverytype LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */goodsgroupdisplay.deliverytype LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */goodsgroupdisplay.deliverytype LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */goodsgroupdisplay.deliverytype NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "6" */
                /*%if entity.conditionOperator == "0" */goodsgroup.goodsprice = /*entity.conditionValue*/0 ::DECIMAL/*%end*/
                /*%if entity.conditionOperator == "1" */goodsgroup.goodsprice != /*entity.conditionValue*/0 ::DECIMAL/*%end*/
                /*%if entity.conditionOperator == "2" */goodsgroup.goodsprice > /*entity.conditionValue*/0 ::DECIMAL/*%end*/
                /*%if entity.conditionOperator == "3" */goodsgroup.goodsprice < /*entity.conditionValue*/0 ::DECIMAL/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "7" */
                /*%if entity.conditionOperator == "0" */goodsgroup.whatsnewdate = to_timestamp(/*entity.conditionValue*/0,'YYYY-MM-DD HH:MI:SS') /*%end*/
                /*%if entity.conditionOperator == "1" */goodsgroup.whatsnewdate != to_timestamp(/*entity.conditionValue*/0,'YYYY-MM-DD HH:MI:SS') /*%end*/
                /*%if entity.conditionOperator == "2" */goodsgroup.whatsnewdate > to_timestamp(/*entity.conditionValue*/0,'YYYY-MM-DD HH:MI:SS') /*%end*/
                /*%if entity.conditionOperator == "3" */goodsgroup.whatsnewdate < to_timestamp(/*entity.conditionValue*/0,'YYYY-MM-DD HH:MI:SS') /*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "8" */
                /*%if entity.conditionOperator == "0" */ (goodsstockdisplay.realstock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) = /*entity.conditionValue*/0 ::DECIMAL/*%end*/
                /*%if entity.conditionOperator == "2" */ (goodsstockdisplay.realstock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) > /*entity.conditionValue*/0 ::DECIMAL/*%end*/
                /*%if entity.conditionOperator == "3" */ (goodsstockdisplay.realstock - goodsstockdisplay.orderreservestock - goodsstockdisplay.safetystock) < /*entity.conditionValue*/0 ::DECIMAL/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "9" */
                /*%if entity.conditionOperator == "0" */ goodsgroupdisplay.unittitle1 = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */ goodsgroupdisplay.unittitle1 != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */ goodsgroupdisplay.unittitle1 LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */ goodsgroupdisplay.unittitle1 LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */ goodsgroupdisplay.unittitle1 LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */ goodsgroupdisplay.unittitle1 NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "10" */
                /*%if entity.conditionOperator == "0" */ goods.unitvalue1 = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */ goods.unitvalue1 != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */ goods.unitvalue1 LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */ goods.unitvalue1 LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */ goods.unitvalue1 LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */ goods.unitvalue1 NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "11" */
                /*%if entity.conditionOperator == "0" */ goodsgroupdisplay.unittitle2 = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */ goodsgroupdisplay.unittitle2 != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */ goodsgroupdisplay.unittitle2 LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */ goodsgroupdisplay.unittitle2 LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */ goodsgroupdisplay.unittitle2 LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */ goodsgroupdisplay.unittitle2 NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
            /*%if entity.conditionColumn == "12" */
                /*%if entity.conditionOperator == "0" */ goods.unitvalue2 = /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "1" */ goods.unitvalue2 != /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "4" */ goods.unitvalue2 LIKE /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "5" */ goods.unitvalue2 LIKE '%' || /*entity.conditionValue*/0/*%end*/
                /*%if entity.conditionOperator == "6" */ goods.unitvalue2 LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
                /*%if entity.conditionOperator == "7" */ goods.unitvalue2 NOT LIKE '%' || /*entity.conditionValue*/0 || '%'/*%end*/
            /*%end*/
        /*%end*/
        /*%if entity_has_next */
            /*%if conditionType == "0"*/
                /*# "and" */
            /*%end*/
            /*%if conditionType == "1"*/
              /*# "or" */
            /*%end*/
        /*%end*/
    /*%end*/
)
/*%end*/
/*%if goodsGroupSeq != null */
  AND goodsgroup.goodsGroupSeq = /*goodsGroupSeq*/0
/*%end*/