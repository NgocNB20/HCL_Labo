DELETE
FROM
    salesslip
WHERE
    salesslip.salesstatus != 'DRAFT'
AND
    salesslip.transactionid = /*transactionId*/'1001'