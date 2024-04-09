SELECT
    *
FROM
    category AS ca
        JOIN categorygoods AS cg ON cg.categoryseq = ca.categoryseq
WHERE
        cg.goodsgroupseq IN ( SELECT goodsgroupseq FROM goodsgroup WHERE goodsgroupcode = /*goodsGroupCode*/0)