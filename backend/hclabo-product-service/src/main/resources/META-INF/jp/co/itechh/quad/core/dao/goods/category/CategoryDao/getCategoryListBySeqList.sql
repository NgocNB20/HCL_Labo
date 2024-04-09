SELECT *
FROM category
WHERE 1=1
/*%if categorySeqList != null*/
AND categoryseq in /*categorySeqList*/(1,2,3)
/*%end*/
 order by
 categoryseq
