SELECT *
FROM categorygoods
WHERE
    categorygoods.categoryseq = /*categorySeq*/0
/*%if goodsGroupSeq != null */
    AND categorygoods.goodsgroupseq = /*goodsGroupSeq*/0
/*%end*/
/*%if fromOrderDisplay != null */
    AND categorygoods.manualorderdisplay >= /*fromOrderDisplay*/0
/*%end*/
/*%if toOrderDisplay != null */
    AND categorygoods.manualorderdisplay <= /*toOrderDisplay*/0
/*%end*/

ORDER BY orderDisplay

/*%if orderBy*/
ASC
/*%end*/

/*%if !orderBy*/
DESC
/*%end*/
