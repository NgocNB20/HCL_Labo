insert into stockresult
(stockresultseq, goodsseq, supplementtime, supplementcount, realstock, processpersonname, note, registtime, updatetime, stockManagementFlag)
values(
 /*stockResultEntity.stockResultSeq*/0,
 /* goodsSeq*/0,
 CURRENT_TIMESTAMP,
 /*stockResultEntity.supplementCount*/0,
 (select realstock
     from stock
     where
         shopSeq = /*shopSeq*/0
     AND
         goodsseq = /* goodsSeq*/0),
 /*stockResultEntity.processPersonName*/0,
 /*stockResultEntity.note*/0,
 CURRENT_TIMESTAMP,
 CURRENT_TIMESTAMP,
 /*stockResultEntity.stockManagementFlag.value*/0
 );
