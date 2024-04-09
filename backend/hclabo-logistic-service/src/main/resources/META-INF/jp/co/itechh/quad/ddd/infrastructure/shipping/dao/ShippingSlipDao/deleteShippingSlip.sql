DELETE
FROM
    shippingslip
WHERE
    shippingslip.shippingstatus != 'DRAFT'
AND
    shippingslip.shippingstatus != 'SECURED_INVENTORY'
AND
    shippingslip.transactionid = /*transactionId*/'1001'