SELECT
	mulPayResult.processedflag
FROM
	mulPayResult
WHERE
	orderId = /*orderId*/'0'
	AND status = /*status*/'0'
	AND (
		(paytype = '36' AND ganbTotalTransferAmount = /*ganbTotalTransferAmount*/0)
		OR paytype <> '36'
	)
	/*%if errCode != null*/
		AND errCode = /*errCode*/'0'
	/*%else*/
		AND errCode is null
	/*%end*/
	/*%if errInfo != null*/
		AND errInfo = /*errInfo*/'0'
	/*%else*/
		AND errInfo is null
	/*%end*/
ORDER BY
    mulPayResult.mulpayresultseq DESC OFFSET 0 LIMIT 1
