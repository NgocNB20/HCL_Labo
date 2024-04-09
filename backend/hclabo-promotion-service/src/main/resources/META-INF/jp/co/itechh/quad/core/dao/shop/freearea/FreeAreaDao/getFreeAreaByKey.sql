SELECT
    freearea.*
FROM
    freearea
WHERE
    freearea.shopseq = /*shopSeq*/0
AND
    freearea.freeareakey = /*freeAreaKey*/0
AND
    freearea.openstarttime <= current_timestamp
ORDER BY freearea.openstarttime DESC
LIMIT 1
