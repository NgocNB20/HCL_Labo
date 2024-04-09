SELECT
    coupon.*
FROM
    coupon
INNER JOIN
	couponindex
ON
	coupon.couponversionno = couponindex.couponversionno
AND
	coupon.couponseq = couponindex.couponseq
WHERE
	coupon.couponseq = /*couponSeq*/'10000001'
AND
	coupon.shopseq = /*shopSeq*/'1001'
