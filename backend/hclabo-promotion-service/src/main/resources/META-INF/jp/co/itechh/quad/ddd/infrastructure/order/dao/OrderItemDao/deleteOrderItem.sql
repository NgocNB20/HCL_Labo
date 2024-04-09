DELETE
FROM orderitem oi
    USING orderslip os
WHERE
    oi.orderslipid = os.orderslipid
  AND
    os.orderstatus != 'DRAFT'
  AND
    os.transactionid = /*transactionId*/'1001'