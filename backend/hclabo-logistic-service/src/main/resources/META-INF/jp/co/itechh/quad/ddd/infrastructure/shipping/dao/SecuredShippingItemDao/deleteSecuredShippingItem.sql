DELETE
FROM securedshippingitem ssi
    USING shippingslip sl
WHERE
    ssi.shippingslipid = sl.shippingslipid
  AND
    sl.shippingstatus != 'DRAFT'
  AND
    sl.shippingstatus != 'SECURED_INVENTORY'
  AND
    sl.transactionid = /*transactionId*/'1001'