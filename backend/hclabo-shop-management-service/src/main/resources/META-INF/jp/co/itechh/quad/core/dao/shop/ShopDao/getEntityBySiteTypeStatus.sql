SELECT
    *
FROM shop
WHERE
/*%if shopSeq != null*/
    shopseq = /*shopSeq*/0 AND
/*%end*/
    1 = 1
