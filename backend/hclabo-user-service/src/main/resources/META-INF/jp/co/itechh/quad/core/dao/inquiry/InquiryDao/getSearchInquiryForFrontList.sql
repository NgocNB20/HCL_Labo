SELECT
	inquiry.*,
	inquirygroup.inquiryGroupName
FROM
	inquiry AS inquiry
	INNER JOIN inquiryGroup AS inquiryGroup
	ON inquiry.inquiryGroupSeq = inquiryGroup.inquiryGroupSeq
WHERE
	inquiry.shopseq = /*conditionDto.shopSeq*/0
	-- 管理機能でmemberInfoSeqの曖昧検索を行う為、DtoではStringで定義されているので、数値にキャストして使用する
	AND inquiry.memberinfoseq = CAST(/*conditionDto.memberInfoSeq*/0 AS NUMERIC)
ORDER BY
	inquiry.firstInquiryTime DESC,
	inquiry.inquirySeq DESC
