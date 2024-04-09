DELETE
FROM
    orderslip
WHERE
    orderstatus != 'DRAFT'
AND
    orderslip.transactionid = /*transactionId*/'1001'