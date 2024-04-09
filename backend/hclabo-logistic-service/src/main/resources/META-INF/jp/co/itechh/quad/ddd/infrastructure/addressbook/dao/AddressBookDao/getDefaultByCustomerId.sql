SELECT
    *
FROM
    addressbook
WHERE
    addressbook.customerid = /*customerId*/'0'
    AND addressbook.defaultflag = true
-- Just in case
ORDER BY
    addressbook.registDate DESC
LIMIT
    1
