SELECT deliveryImpossibleDay.*
FROM deliveryImpossibleDay
WHERE
deliverymethodseq = /*dto.deliveryMethodSeq*/0
AND year = /*dto.year*/0
ORDER BY
date ASC
