delete from
    favorite
where
    favorite.memberInfoSeq = /*memberInfoSeq*/0
and
    favorite.goodsSeq in /*goodsSeqs*/(1,2,3)
