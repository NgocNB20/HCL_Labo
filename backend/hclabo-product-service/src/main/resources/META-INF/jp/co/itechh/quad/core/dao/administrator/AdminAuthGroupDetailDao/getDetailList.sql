SELECT
	detail.*
FROM
	adminAuthGroupDetail detail
WHERE
	detail.adminAuthGroupSeq = /*adminAuthGroupSeq*/1001
ORDER BY
	detail.authTypeCode
