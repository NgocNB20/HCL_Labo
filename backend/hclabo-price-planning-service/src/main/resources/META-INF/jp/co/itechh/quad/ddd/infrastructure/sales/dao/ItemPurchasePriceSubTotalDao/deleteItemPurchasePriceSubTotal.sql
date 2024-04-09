DELETE
FROM itempurchasepricesubtotal ippst
    USING salesslip ss
WHERE
    ippst.salesslipid = ss.salesslipid
  AND
    ss.salesstatus != 'DRAFT'
  AND
    ss.transactionid = /*transactionId*/'1001'