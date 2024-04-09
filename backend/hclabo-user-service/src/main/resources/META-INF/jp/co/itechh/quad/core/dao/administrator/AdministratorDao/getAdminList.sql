SELECT
	admin.*
FROM
	administrator admin
LEFT JOIN
	adminAuthGroup auth
ON
	admin.adminAuthGroupSeq = auth.adminAuthGroupSeq
WHERE
	admin.shopSeq = /*condition.shopSeq*/1001
ORDER BY
 /*%if condition.orderField == "administratorId"*/
 	admin.administratorId /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "administratorName"*/
 	admin.administratorlastname /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,admin.administratorfirstname /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "administratorKana"*/
 	admin.administratorlastkana /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,admin.administratorfirstkana /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "mail"*/
 	admin.mail /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "administratorStatus"*/
 	admin.administratorstatus /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "useStartDate"*/
 	admin.usestartdate /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "useEndDate"*/
 	admin.useenddate /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
 /*%if condition.orderField == "administratorGroupName"*/
 	auth.authGroupDisplayName /*%if condition.orderAsc*/ ASC /*%else*/ DESC /*%end*/
 	,/*%end*/
admin.administratorSeq
