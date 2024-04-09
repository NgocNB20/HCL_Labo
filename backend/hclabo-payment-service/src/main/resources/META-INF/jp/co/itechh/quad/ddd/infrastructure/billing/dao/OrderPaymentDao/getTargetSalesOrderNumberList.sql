select distinct
    o.ordercode,
    o.cancellimit,
    o.paytype
from
    billingslip b
    join orderpayment o
      on b.billingslipid = o.billingslipid
where
    b.billingstatus = 'CANCEL'
    and o.linkPaymentType = '0' -- 即時払い
    and o.cancellimit notnull
    and o.cancellimit > now()
	and o.gmopaymentcancelstatus in ('0', '1') -- 未判定 or 未キャンセル
