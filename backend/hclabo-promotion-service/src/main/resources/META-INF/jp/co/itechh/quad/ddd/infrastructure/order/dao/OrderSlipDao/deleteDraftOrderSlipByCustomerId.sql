DELETE
FROM
    orderslip
WHERE
    orderstatus = 'DRAFT'
AND
    orderslip.customerid = /*customerId*/'0'