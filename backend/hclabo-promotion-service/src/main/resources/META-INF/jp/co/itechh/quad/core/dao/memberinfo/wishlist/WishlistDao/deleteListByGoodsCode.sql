delete
from
  favorite
where
  favorite.memberInfoSeq = /*memberInfoSeq*/0
  and favorite.goodsSeq in (
    select
      goodsSeq
    from
      goods
    where
      goodsCode in /*goodsCodes*/(1,2,3)
  )
