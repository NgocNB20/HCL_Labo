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
	coupon.couponendtime > /*reUseEndTime*/'2010/08/01 00:00:00'
AND
	couponcode = /*couponCode*/'couponCode'
	
