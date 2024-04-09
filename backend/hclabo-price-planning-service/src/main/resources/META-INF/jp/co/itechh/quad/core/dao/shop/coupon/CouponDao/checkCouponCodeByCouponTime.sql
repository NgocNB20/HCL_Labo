SELECT
    count(*)
FROM
    coupon
INNER JOIN
	couponindex
ON
	coupon.couponversionno = couponindex.couponversionno
AND
	coupon.couponseq = couponindex.couponseq
WHERE
	coupon.shopseq = /*shopSeq*/'1001'
AND
	couponcode = /*couponCode*/'ASWXMx7kE0'
AND 
	coupon.couponendtime >= /*reUsePastTime*/'2012/12/31 23:59:59'
AND 
	coupon.couponstarttime <= /*reUseFutureTime*/'2013/01/9 00:00:00'
/*%if couponSeq != null*/
AND
	coupon.couponSeq <> /*couponSeq*/1
/*%end*/

