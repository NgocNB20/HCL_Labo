SELECT
    DeliveryImpossibleArea.*
FROM
    DeliveryImpossibleArea
WHERE
        DeliveryImpossibleArea.deliveryMethodSeq = /*deliveryMethodSeq*/0
  AND
        DeliveryImpossibleArea.zipCode = /*zipCode*/0
