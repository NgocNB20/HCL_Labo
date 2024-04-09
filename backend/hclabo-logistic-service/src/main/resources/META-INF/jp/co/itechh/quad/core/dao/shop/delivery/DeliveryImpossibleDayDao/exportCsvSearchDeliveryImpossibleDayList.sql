SELECT deliveryImpossibleDay.*
FROM deliveryImpossibleDay
WHERE
deliverymethodseq = /*deliveryMethodSeq*/0
AND year = /*year*/0
ORDER BY
date ASC
