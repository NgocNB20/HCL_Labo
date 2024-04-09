SELECT 
	*
FROM
	coupon

INNER JOIN
	couponindex
ON
	coupon.couponseq = couponindex.couponseq
AND
	coupon.couponversionno = couponindex.couponversionno

WHERE
	coupon.couponcode = /*couponCode*/'couponcode'

-- FIXME 同一クーポンコードで別期間のクーポンが登録可能だが、このソート条件だと取りたいクーポンが取れない
-- 本日～N日後までのクーポンとN+1～N+2日後のクーポンを登録すると、後者が取れてしまう=利用できない
ORDER BY
	coupon.couponstarttime DESC
LIMIT 1
