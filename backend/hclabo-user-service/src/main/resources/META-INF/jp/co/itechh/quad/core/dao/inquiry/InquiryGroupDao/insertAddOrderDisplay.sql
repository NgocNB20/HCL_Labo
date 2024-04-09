insert into inquirygroup
(inquirygroupseq, shopseq, inquirygroupname, openstatus, orderdisplay, registtime, updatetime)
values(
 /*inquiryGroupEntity.inquiryGroupSeq*/0,
 /*inquiryGroupEntity.shopSeq*/0,
 /*inquiryGroupEntity.inquiryGroupName*/0,
 /*inquiryGroupEntity.openStatus.value*/0,
 (select coalesce(max(orderdisplay) + 1, 1) from inquirygroup where shopSeq = /*inquiryGroupEntity.shopSeq*/0),
 /*inquiryGroupEntity.registTime*/0,
 /*inquiryGroupEntity.updateTime*/0
 );
