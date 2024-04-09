UPDATE
    ORDERSLIP
SET
    ORDERSTATUS = /*orderSlipDbEntity.orderStatus*/'OPEN',
    USERAGENT = /*orderSlipDbEntity.userAgent*/'0'
WHERE
        ORDERSLIPID = /*orderSlipDbEntity.orderSlipId*/'1001'
 AND    TRANSACTIONID = /*transactionId*/'1001'
