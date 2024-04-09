update
    orderpayment
set
    gmopaymentcancelstatus = /*gmoPaymentCancelStatus.value*/0
from
    billingslip
where
    billingslip.billingslipid = orderpayment.billingslipid
and
    billingslip.billingstatus = 'CANCEL'
and
    orderpayment.ordercode = /*orderCode*/0
