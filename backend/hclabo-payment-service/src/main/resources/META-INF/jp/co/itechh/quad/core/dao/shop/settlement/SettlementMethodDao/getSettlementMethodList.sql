SELECT
    *
FROM
    SettlementMethod
WHERE
    shopSeq = /*shopSeq*/0
AND
	openstatuspc <> '9'
ORDER BY
    orderdisplay,
    settlementmethodseq
