insert into goodsinformationicon
(iconseq, shopseq, iconname, colorcode, orderdisplay, registtime, updatetime)
values(
 /*goodsInformationIconEntity.iconSeq*/0,
 /*goodsInformationIconEntity.shopSeq*/0,
 /*goodsInformationIconEntity.iconName*/0,
 /*goodsInformationIconEntity.colorCode*/0,
 (select coalesce(max(orderdisplay) + 1, 1) from goodsinformationicon where shopSeq = /*goodsInformationIconEntity.shopSeq*/0),
 /*goodsInformationIconEntity.registTime*/0,
 /*goodsInformationIconEntity.updateTime*/0
 );
