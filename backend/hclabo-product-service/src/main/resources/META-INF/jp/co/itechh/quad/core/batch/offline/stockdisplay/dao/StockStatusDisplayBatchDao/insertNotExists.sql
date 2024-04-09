insert into stockstatusdisplay
(
"goodsgroupseq",
"stockstatuspc",
"registtime",
"updatetime"
)
-- 在庫表示テーブルに未存在のデータを登録対象とする
select
goodsgroupseq,
'0' as stockstatuspc,
/*today*/'2010-01-01' as registtime,
/*today*/'2010-01-01' as updatetime
from
-- 商品グループテーブルに存在し、在庫表示テーブルに未存在のデータを抜粋
goodsgroup as gg
where not exists
(
select goodsgroupseq
from
stockstatusdisplay as sd
where gg.goodsgroupseq = sd.goodsgroupseq
)
