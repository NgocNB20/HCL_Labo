SELECT
    *
FROM
    ORDERSLIP
WHERE
      CUSTOMERID = /*customerId*/'1001'
  AND ORDERSTATUS = /*orderStatus*/'DRAFT'