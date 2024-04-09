SELECT
    DeliverySpecialChargeArea.*
FROM
    DeliverySpecialChargeArea
WHERE
    DeliverySpecialChargeArea.deliveryMethodSeq in /*deliveryMethodSeqList*/(0)
AND
    DeliverySpecialChargeArea.zipCode = /*zipCode*/0
