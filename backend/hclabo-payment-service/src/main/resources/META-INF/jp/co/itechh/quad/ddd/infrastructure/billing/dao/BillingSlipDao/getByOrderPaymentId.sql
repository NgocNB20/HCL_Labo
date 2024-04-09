SELECT billingslip.*
FROM    billingslip INNER JOIN orderpayment
                    ON billingslip.billingslipid = orderpayment.billingslipid
WHERE orderpayment.orderpaymentid = /*orderPaymentId*/'1001'
