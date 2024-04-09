select
    count(*)
from
    ( select
        * ,
        row_number() over ( partition by ORDERCODE order by REGISTDATE desc) as HISTORY
      from
        SALESSLIP
      where
        SALESSLIP.CUSTOMERID = /*customerId*/'1001'
        and SALESSLIP.COUPONSEQ = /*couponSeq*/'1001'
        and SALESSLIP.SALESSTATUS != 'DRAFT'
     ) SSLIP_HISTORY
inner join
    COUPON
    on
        COUPON.COUPONSEQ = SSLIP_HISTORY.COUPONSEQ
    and
        COUPON.COUPONVERSIONNO = SSLIP_HISTORY.COUPONVERSIONNO
    and
        COUPON.COUPONSTARTTIME = /*couponStartTime*/0
where
    SSLIP_HISTORY.HISTORY = 1
    and
        SSLIP_HISTORY.COUPONUSEFLAG = true
