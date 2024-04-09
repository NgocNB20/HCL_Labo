WITH target AS (
	SELECT * FROM deliveryspecialchargearea WHERE deliverymethodseq = /*conditionDto.deliveryMethodSeq*/0
),
target_zipcode AS (
	SELECT
		zipcode
		, prefecturename || cityname || ' ' || townname AS address
		, 0 AS ziptype -- 郵便番号
		, CASE updatenotetype WHEN '6' THEN 1 ELSE 0 END AS deletetype -- 廃止フラグ
	FROM zipcode
	WHERE zipcode IN (SELECT zipcode FROM target)
	UNION
	SELECT
		zipcode
		, prefecturename || cityname || ' ' || townname || ' ' || numbers AS address
		, 1 AS ziptype -- 事業所郵便番号
		, CASE updatecode WHEN '5' THEN 1 ELSE 0 END AS deletetype -- 廃止フラグ
	FROM officezipcode
	WHERE zipcode IN (SELECT zipcode FROM target)
)

SELECT
	target.*,
	zipcode.addresslist
FROM
	target
	LEFT OUTER JOIN
	(
		SELECT
			zipcode,
			string_agg(CASE deletetype WHEN 1 THEN '【廃止】' ELSE '' END || address,'\r\n' ORDER BY deletetype, ziptype) AS addresslist
		FROM
			target_zipcode
		GROUP BY
			zipcode
	) zipcode
	ON
		target.zipcode = zipcode.zipcode
WHERE
    true
	/*%if conditionDto.prefecture != null && conditionDto.prefecture != ""*/
	and zipcode.addresslist LIKE '%' || /*conditionDto.prefecture*/0 || '%'
	/*%end*/
ORDER BY
	zipcode
