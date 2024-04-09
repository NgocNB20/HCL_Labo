SELECT
    inquiryGroup.*
from inquiryGroup
where
shopseq = /*conditionDto.shopSeq*/0
and openstatus = /*conditionDto.openStatus.value*/0
order by inquiryGroup.orderdisplay
