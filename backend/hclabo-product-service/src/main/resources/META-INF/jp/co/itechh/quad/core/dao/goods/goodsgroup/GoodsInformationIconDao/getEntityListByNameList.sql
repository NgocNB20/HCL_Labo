SELECT
    *
FROM
    goodsInformationIcon
WHERE 1=1
/*%if iconNameList != null*/
    AND iconName IN /*iconNameList*/(1,2,3)
/*%end*/
