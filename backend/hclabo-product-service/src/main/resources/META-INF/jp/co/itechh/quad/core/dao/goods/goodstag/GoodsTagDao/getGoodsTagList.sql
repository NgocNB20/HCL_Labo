SELECT
    *
FROM
    goodstag
WHERE 1=1
/*%if dto.tag != null*/
    AND tag	LIKE '%' || /*dto.tag*/0 || '%'
/*%end*/
order by
/*%if dto.pageInfo.orderField != null*/
    /*%if dto.pageInfo.orderField == "count"*/
         count DESC
    /*%end*/
    /*%if dto.pageInfo.orderField == "tag"*/
         tag ASC
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
