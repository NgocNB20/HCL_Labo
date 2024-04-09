-- 金額別手数料の対象を抽出
-- 該当する上限金額の内、最安値の上限金額のレコードを抽出する
WITH target AS (
    SELECT
        s.settlementmethodseq, MIN(p.maxprice) AS maxprice
    FROM
        settlementmethod AS s
            INNER JOIN settlementmethodpricecommission AS p
                       ON p.settlementmethodseq = s.settlementmethodseq
    WHERE
        s.settlementmethodseq = /*settlementMethodSeq*/1001
        -- 手数料抜きの受注金額でチェック
        AND p.maxprice  >=  /*judgePrice*/0
    GROUP BY
        s.settlementmethodseq
)

SELECT
    s.*,
    p.maxPrice,
    p.commission
FROM
    settlementmethod AS s
        -- 金額別手数料のレコードを１つに絞る為に target とJOIN
        LEFT OUTER JOIN target
                        ON target.settlementmethodseq = s.settlementmethodseq
        LEFT OUTER JOIN settlementmethodpricecommission AS p
                        ON p.settlementmethodseq = s.settlementmethodseq
                            AND p.maxprice = target.maxprice
WHERE
    s.settlementmethodseq = /*settlementMethodSeq*/1001
