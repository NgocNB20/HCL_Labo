SELECT
    *
FROM
    coupon

INNER JOIN
	couponindex
ON
	coupon.couponversionno = couponindex.couponversionno
AND
	coupon.couponseq = couponindex.couponseq

WHERE
	coupon.shopseq = /*conditionDto.shopSeq*/'1001'

/*%if conditionDto.couponName != null*/
AND
    (coupon.couponname LIKE '%' || /*conditionDto.couponName*/'' || '%'
OR
    coupon.coupondisplaynamepc LIKE '%' || /*conditionDto.couponName*/'' || '%')
/*%end*/

/*%if conditionDto.couponId != null*/
AND
    coupon.couponid LIKE /*conditionDto.couponId*/'' || '%'
/*%end*/

/*%if conditionDto.couponCode != null*/
AND
    coupon.couponcode =  /*conditionDto.couponCode*/''
/*%end*/

/*%if conditionDto.couponStartTimeFrom != null*/
AND
    coupon.couponstarttime >= /*conditionDto.couponStartTimeFrom*/0
/*%end*/

/*%if conditionDto.couponStartTimeTo != null*/
AND
    coupon.couponstarttime <= /*conditionDto.couponStartTimeTo*/0
/*%end*/

/*%if conditionDto.couponEndTimeFrom != null*/
AND
    coupon.couponendtime >= /*conditionDto.couponEndTimeFrom*/0
/*%end*/

/*%if conditionDto.couponEndTimeTo != null*/
AND
    coupon.couponendtime <= /*conditionDto.couponEndTimeTo*/0
/*%end*/

/*%if conditionDto.targetGoodsCode != null*/
AND
    coupon.targetGoods LIKE '%' || /*conditionDto.targetGoodsCode*/'' || '%'
/*%end*/

/*************** sort ***************/
ORDER BY
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "couponName"*/
        coupon.couponname /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,coupon.couponid
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "couponId"*/
        coupon.couponid /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "couponStartTime"*/
        coupon.couponstarttime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,coupon.couponid
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "couponEndTime"*/
        coupon.couponendtime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,coupon.couponid
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "couponCode"*/
        coupon.couponcode /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        ,coupon.couponid
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "discountPrice"*/
        coupon.discountprice /*%if conditionDto.pageInfo.orderAsc*/ ASC NULLS FIRST /*%else*/ DESC NULLS LAST /*%end*/
        ,coupon.couponid
    /*%end*/

    /*%if conditionDto.pageInfo.orderField == "discountRate"*/
        coupon.discountrate /*%if conditionDto.pageInfo.orderAsc*/ ASC NULLS FIRST /*%else*/ DESC NULLS LAST /*%end*/
        ,coupon.couponid
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
