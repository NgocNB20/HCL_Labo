SELECT
        *
    FROM
        mulPayBill m
    WHERE
        m.orderId = /*orderId*/0
        AND m.accessid = /*accessId*/0
        AND m.errcode is null
    ORDER BY
        m.mulpaybillseq DESC OFFSET 0 LIMIT 1
