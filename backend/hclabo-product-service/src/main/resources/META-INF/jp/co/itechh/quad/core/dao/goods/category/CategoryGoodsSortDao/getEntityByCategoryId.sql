select
    categoryGoodsSort.*
from
    categoryGoodsSort
inner join category on
    category.categoryseq = categoryGoodsSort.categorySeq
where
    category.categoryId = /*categoryId*/0