SELECT
	auth.*
FROM
	adminAuthGroup auth
WHERE
		auth.shopSeq = /*shopSeq*/1001
	AND auth.authGroupDisplayName = /*authGroupDisplayName*/'フルアクセス'
