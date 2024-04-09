SELECT
    *
FROM
    shippingslip
WHERE
    shippingslip.shippingstatus = 'SECURED_INVENTORY'
AND
    shippingslip.registdate <= /*thresholdTime*/0
ORDER BY
    shippingslip.registdate
