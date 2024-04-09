SELECT
    zipCode,
    prefectureName,
    prefectureKana prefectureNameKana ,
    cityName,
    cityKana cityNameKana,
    townName,
    townKana townNameKana,
    '' as numbers,
    '0' as zipCodeType
from
    zipcode
where
    zipCode like (/*zipCode*/0 || '%')
    and updatenotetype <> '6' -- 住所の郵便番号廃止コード
union
SELECT
    zipCode,
    prefectureName,
    '' as prefectureKana,
    cityName,
    '' as cityKana,
    townName,
    '' as townKana,
    numbers,
    '1' as zipCodeType
from
    officezipcode
where
    zipCode like (/*zipCode*/0 || '%')
    and updatecode <> '5' -- 事業所の個別郵便番号廃止コード
order by
    zipCode,

    -- 住所の郵便番号ソート用
    prefectureNameKana,
    cityNameKana,
    townNameKana,

    -- 以下の条件は事業所住所用
    prefectureName,
    cityName,
    townName,
    numbers
