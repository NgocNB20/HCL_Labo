DELETE
FROM orderpayment o
    USING billingslip sl
WHERE
    o.billingslipid = sl.billingslipid
  AND
    o.orderpaymentstatus != 'DRAFT'
  AND
    sl.transactionid = /*transactionId*/'1001'