SELECT *
FROM ADDRESSBOOK
WHERE CUSTOMERID = /*customerId*/'1001'
/*%if exclusionAddressId != null*/
  AND ADDRESSID != /*exclusionAddressId*/'1001'
/*%end*/
  AND HIDEFLAG = false
ORDER BY REGISTDATE DESC