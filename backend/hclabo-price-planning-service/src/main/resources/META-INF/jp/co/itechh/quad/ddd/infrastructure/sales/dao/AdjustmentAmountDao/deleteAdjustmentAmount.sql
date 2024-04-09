DELETE
FROM adjustmentamount aa
    USING salesslip ss
WHERE
    aa.salesslipid = ss.salesslipid
  AND
    ss.salesstatus != 'DRAFT'
  AND
    ss.transactionid = /*transactionId*/'1001'