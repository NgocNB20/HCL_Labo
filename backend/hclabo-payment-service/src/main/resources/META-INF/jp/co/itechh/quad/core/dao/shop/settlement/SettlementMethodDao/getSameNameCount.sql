SELECT
	COUNT(settlementmethodname)
FROM
	settlementmethod
WHERE
	shopseq = /*shopSeq*/0
AND
	settlementmethodname = /*settlementMethodName*/0
AND
	settlementmethodseq <> /*settlementMethodSeq*/0
AND
	openstatuspc <> '9'
