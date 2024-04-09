SELECT
        *
    FROM
        mulPayBill
    WHERE
        mulPayBill.accessId = /*accessId*/0
        AND mulPayBill.accessPass = /*accessPass*/0
        AND errcode is null
    ORDER BY
        mulPayBill.mulpaybillseq DESC OFFSET 0 LIMIT 1
