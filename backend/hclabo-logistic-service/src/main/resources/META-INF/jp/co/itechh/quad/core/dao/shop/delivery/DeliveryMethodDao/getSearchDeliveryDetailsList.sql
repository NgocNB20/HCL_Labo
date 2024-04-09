SELECT
    deliveryMethod.*,
    deliveryMethodTypeCarriage.prefectureType,
    deliveryMethodTypeCarriage.maxPrice,
    deliveryMethodTypeCarriage.carriage
FROM
    deliveryMethod
LEFT OUTER JOIN
    deliveryMethodTypeCarriage
ON
    deliveryMethod.deliveryMethodSeq = deliveryMethodTypeCarriage.deliveryMethodSeq
    AND
    (
        (
        deliveryMethod.deliveryMethodType = '1'
        /*%if conditionDto.prefectureType != null*/
            AND deliveryMethodTypeCarriage.prefectureType = /*conditionDto.prefectureType.value*/0
        /*%end*/
        )
        OR
        (
        deliveryMethod.deliveryMethodType = '2'
        AND deliveryMethodTypeCarriage.maxPrice >= /*conditionDto.totalGoodsPrice*/0
        )
    )

WHERE 1 = 1
/*%if conditionDto.shopSeq != null*/
    AND deliveryMethod.shopSeq = /*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.openStatusPC != null*/
    AND deliveryMethod.openStatusPC = /*conditionDto.openStatusPC.value*/0
/*%end*/
/*%if conditionDto.openStatusPC == null*/
and
    deliverymethod.openstatuspc <> '9'
/*%end*/
/*%if conditionDto.prefectureType == null*/
and
    deliverymethod.deliveryMethodType = '0'
/*%end*/
ORDER BY
    deliveryMethod.orderDisplay,
    deliveryMethod.deliveryMethodSeq,
    deliveryMethodTypeCarriage.maxPrice
