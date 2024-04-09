select
	 tax.*
	,taxrate.rate
from
	tax
	inner join taxrate
 	on tax.taxseq = taxrate.taxseq
where
	taxrate.ratetype = '1'
	and tax.startTime <= /*currentTime*/0
	and tax.endTime >= /*currentTime*/0
;
