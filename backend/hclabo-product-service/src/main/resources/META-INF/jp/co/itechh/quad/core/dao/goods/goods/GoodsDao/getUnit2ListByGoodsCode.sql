SELECT
    g1.goodscode,
    g1.salestatuspc,
    g1.salestarttimepc,
    g1.saleendtimepc,
    g1.stockmanagementflag,
    g1.unitvalue1,
    g1.unitvalue2,
    g1.orderdisplay,
    s1.realStock - s1.orderreservestock - s1.safetystock AS salespossiblestock,
    s1.remainderfewstock
FROM
    goods AS g1
        INNER JOIN
    goodsstockdisplay AS s1 ON g1.goodsseq = s1.goodsseq
WHERE
        unitmanagementflag = '1'
  AND
    EXISTS (
            SELECT
                *
            FROM
                goodsgroup AS gg1
            WHERE
                    gg1.goodsgroupcode = /*ggcd*/0
              AND
                    gg1.goodsgroupseq = g1.goodsgroupseq
        )
/*%if gcd != null && gcd != "" */
  AND
        unitvalue1 = (
        SELECT
            unitvalue1
        FROM
            goods AS g2
        WHERE
                g2.goodscode = /*gcd*/0
          AND
            EXISTS (
                    SELECT
                        *
                    FROM
                        goodsgroup AS gg2
                    WHERE
                            gg2.goodsgroupcode = /*ggcd*/0
                      AND
                            gg2.goodsgroupseq = g2.goodsgroupseq
                )
    )
/*%end*/
ORDER BY
    g1.orderdisplay ASC
