SELECT
    *
FROM
    settlementmethod
WHERE
    shopseq = /*shopSeq*/0
AND
    settlementmethodtype = /*settlementMethodType.value*/0
AND
    openstatuspc = /*openStatus.value*/0
ORDER BY
    orderdisplay,
    settlementmethodseq
LIMIT 1
