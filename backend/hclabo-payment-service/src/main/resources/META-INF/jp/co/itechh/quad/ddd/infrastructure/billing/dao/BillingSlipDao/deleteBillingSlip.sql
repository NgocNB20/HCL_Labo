DELETE
FROM
    billingslip
WHERE
    billingslip.billingstatus != 'DRAFT'
AND
    billingslip.transactionid = /*transactionId*/'1001'