SELECT
    *
FROM
    cardbrand
WHERE
    /*%if isFront*/
        frontDisplayFlag = '1'
    /*%else*/
    true
    /*%end*/
ORDER BY
    orderdisplay,
    cardbrandSeq
