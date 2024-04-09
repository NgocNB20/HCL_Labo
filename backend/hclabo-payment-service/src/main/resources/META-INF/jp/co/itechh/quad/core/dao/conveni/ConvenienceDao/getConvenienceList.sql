SELECT *
FROM convenience
WHERE shopseq = /*shopSeq*/0
/*%if limitToUseConveni*/
  AND useFlag = '1'
/*%end*/
ORDER BY displayorder,conveniseq
