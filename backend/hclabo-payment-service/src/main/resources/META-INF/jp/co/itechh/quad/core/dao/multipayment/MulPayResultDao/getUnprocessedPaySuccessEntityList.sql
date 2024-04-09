SELECT
        *
    FROM
        mulPayResult
    WHERE
        mulPayResult.shopSeq = /*shopSeq*/0
        AND (mulPayResult.processedFlag = '0'
        OR   (mulPayResult.processedFlag = '9' AND mulPayResult.payType = '38'))
    ORDER BY
        mulPayResult.orderseq ASC
