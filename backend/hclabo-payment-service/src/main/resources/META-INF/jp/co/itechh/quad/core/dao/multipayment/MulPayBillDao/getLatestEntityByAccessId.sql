SELECT
        *
    FROM
        mulPayBill
    WHERE
        mulPayBill.accessId = /*accessId*/0
    ORDER BY
        mulPayBill.mulpaybillseq DESC OFFSET 0 LIMIT 1