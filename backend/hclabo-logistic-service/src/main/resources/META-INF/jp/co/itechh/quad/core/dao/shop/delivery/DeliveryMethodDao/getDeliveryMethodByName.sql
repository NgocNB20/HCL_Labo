SELECT
  *
FROM
  deliveryMethod
WHERE 1 = 1
/*%if shopSeq != null*/
  AND shopSeq = /*shopSeq*/0
/*%end*/
/*%if deliveryMethodName != null*/
  AND deliveryMethodName = /*deliveryMethodName*/0
/*%end*/
  AND openstatuspc != '9'
