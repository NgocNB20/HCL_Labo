SELECT
category.*,
categorydisplay.categorynamepc,
categorydisplay.categorynotepc,
categorydisplay.freetextpc,
categorydisplay.metadescription,
categorydisplay.categoryimagepc,
categorydisplay.registtime as displayregisttime,
categorydisplay.updatetime as displayupdatetime,
categorygoodssort.goodsSortColumn,
categorygoodssort.goodsSortOrder
FROM category,categorydisplay, categorygoodssort
WHERE
category.categoryseq = categorydisplay.categoryseq
AND category.categoryseq = categorygoodssort.categoryseq
AND category.shopseq = /*shopSeq*/0
/*%if categoryIdList != null*/
AND categoryId in /*categoryIdList*/(1,2,3)
/*%end*/
/*%if openStatus != null && siteType != null */
  /*%if siteType.value == "0" || siteType.value == "1" */
    AND (category.categoryopenstarttimepc <= CURRENT_TIMESTAMP OR category.categoryopenstarttimepc is null)
    AND (category.categoryopenendtimepc >= CURRENT_TIMESTAMP OR category.categoryopenendtimepc is null)
    AND category.categoryopenstatuspc = /*openStatus.value*/0
  /*%end*/
/*%end*/
 order by
 categoryId
