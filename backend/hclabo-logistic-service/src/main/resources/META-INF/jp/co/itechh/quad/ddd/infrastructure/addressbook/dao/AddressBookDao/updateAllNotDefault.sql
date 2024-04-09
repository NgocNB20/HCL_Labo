UPDATE
    addressbook
SET
    defaultflag = false
WHERE
    addressbook.customerid = /*customerId*/'0'
    AND addressbook.defaultflag = true
