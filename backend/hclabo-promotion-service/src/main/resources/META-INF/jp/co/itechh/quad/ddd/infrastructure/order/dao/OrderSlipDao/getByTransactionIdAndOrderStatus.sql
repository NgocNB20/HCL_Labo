SELECT
    *
FROM
    ORDERSLIP
WHERE
        TRANSACTIONID = /*transactionId*/'1001'
    AND ORDERSTATUS = /*orderStatus*/'DRAFT'