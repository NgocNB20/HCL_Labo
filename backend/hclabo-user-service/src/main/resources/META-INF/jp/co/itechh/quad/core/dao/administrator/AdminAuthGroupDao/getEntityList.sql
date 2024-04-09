SELECT
	authGroup.*
FROM
	AdminAuthGroup authGroup
WHERE
	authGroup.shopSeq = /*shopSeq*/1001
ORDER BY
	authGroup.adminAuthGroupSeq
