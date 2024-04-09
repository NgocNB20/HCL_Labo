select
  *
from
  inquiry
where
     shopseq = /*shopSeq*/0
 and ordercode = /*orderCode*/0
 order by inquiryseq /*%if asc*/ ASC /*%else*/ DESC /*%end*/
