SELECT holiday.*
FROM holiday
WHERE 1 = 1
/*%if holidaySearchForDaoConditionDto.deliveryMethodSeq != null*/
AND deliverymethodseq = /*holidaySearchForDaoConditionDto.deliveryMethodSeq*/0
/*%end*/
/*%if holidaySearchForDaoConditionDto.year != null*/
AND holiday.year = /*holidaySearchForDaoConditionDto.year*/0
/*%end*/
ORDER BY
date ASC
