DELETE
FROM billingslip
WHERE
    billingstatus = 'DRAFT'
  AND
    registdate <= /*deleteTime*/0