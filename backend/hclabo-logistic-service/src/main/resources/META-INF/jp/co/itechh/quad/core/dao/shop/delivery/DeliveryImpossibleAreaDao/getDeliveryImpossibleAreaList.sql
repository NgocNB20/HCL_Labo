SELECT
    DeliveryImpossibleArea.*
FROM
    DeliveryImpossibleArea
WHERE
    DeliveryImpossibleArea.deliveryMethodSeq in /*deliveryMethodSeqList*/(0)
AND
    DeliveryImpossibleArea.zipCode = /*zipCode*/0
