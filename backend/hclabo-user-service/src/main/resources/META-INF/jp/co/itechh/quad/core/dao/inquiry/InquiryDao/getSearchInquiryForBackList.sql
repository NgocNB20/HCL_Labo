select
 inquiry.inquiryseq,
 inquiry.shopseq,
 inquiry.inquirycode,
 inquiry.inquirylastname,
 inquiry.inquiryfirstname,
 inquiry.inquirytime,
 inquiry.inquirystatus,
 inquiry.answertime,
 inquiry.inquirygroupseq,
 inquiry.inquirytel,
 inquiryGroup.inquiryGroupName,
 inquiry.inquiryType,
 inquiry.firstinquirytime,
 inquiry.cooperationmemo,
 inquiry.lastrepresentative,
 inquiry.lastuserinquirytime

from
 inquiry as inquiry
 , inquiryGroup
where
 inquiry.inquiryGroupSeq = inquiryGroup.inquiryGroupSeq
/*%if conditionDto.shopSeq != null*/
 and inquiry.shopseq=/*conditionDto.shopSeq*/0
 and inquiryGroup.shopseq=/*conditionDto.shopSeq*/0
/*%end*/
/*%if conditionDto.inquiryGroupSeq != null*/
 and inquiry.inquirygroupseq = /*conditionDto.inquiryGroupSeq*/0
/*%end*/
/*%if conditionDto.inquiryStatus != null*/
 and inquiry.inquirystatus = /*conditionDto.inquiryStatus.value*/0
/*%end*/
/*%if conditionDto.inquiryCode != null*/
 and inquiry.inquirycode like '%' || /*conditionDto.inquiryCode*/0 || '%'
/*%end*/
/*%if conditionDto.inquiryTimeFrom != null*/
 and inquiry.inquirytime >= /*conditionDto.inquiryTimeFrom*/0
/*%end*/
/*%if conditionDto.inquiryTimeTo != null*/
 and inquiry.inquirytime <= /*conditionDto.inquiryTimeTo*/0
/*%end*/
/*%if conditionDto.inquiryName != null*/
 and
 (
 	inquiry.inquirylastname || coalesce(inquiry.inquiryfirstname,'') like '%' || /*conditionDto.inquiryName*/0 || '%' or
 	inquiry.inquirylastkana || coalesce(inquiry.inquiryfirstkana,'') like '%' || /*conditionDto.inquiryName*/0 || '%'
 )
/*%end*/
/*%if conditionDto.inquiryMail != null*/
 and inquiry.inquirymail like '%' || /*conditionDto.inquiryMail*/0 || '%'
/*%end*/

/*%if conditionDto.memberInfoMail != null*/
 and inquiry.memberinfoseq in (select memberinfoseq from memberinfo where memberinfoid like '%' || /*conditionDto.memberInfoMail*/0 || '%')
/*%end*/
/*%if conditionDto.inquiryType != null && conditionDto.inquiryType != "2"*/
 and inquiry.inquirytype = /*conditionDto.inquiryType*/0
/*%end*/

/*%if conditionDto.inquiryTel != null*/
 and inquiry.inquiryTel like '%' || /*conditionDto.inquiryTel*/0 || '%'
/*%end*/

/*%if conditionDto.lastRepresentative != null*/
 and inquiry.lastrepresentative like '%' || /*conditionDto.lastRepresentative*/0 || '%'
/*%end*/
/*%if conditionDto.cooperationMemo != null*/
 and inquiry.cooperationMemo like '%' || /*conditionDto.cooperationMemo*/0 || '%'
/*%end*/

/*************** sort ***************/
order by
/*%if conditionDto.pageInfo.orderField != null*/
    /*%if conditionDto.pageInfo.orderField == "inquiryStatus"*/
        inquiry.inquiryStatus /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "inquiryCode"*/
        inquiry.inquiryCode /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "inquiryName"*/
        inquiry.inquirylastname /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
        , inquiry.inquiryfirstname
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "firstInquiryTime"*/
        inquiry.firstInquiryTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "lastUserInquiryTime"*/
        inquiry.lastUserInquiryTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "inquiryGroupName"*/
        inquiryGroup.inquiryGroupName /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "lastRepresentative"*/
        inquiry.lastRepresentative /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
    /*%if conditionDto.pageInfo.orderField == "cooperationMemo"*/
        inquiry.cooperationMemo /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
    /*%end*/
/*%else*/
    1 ASC
/*%end*/
