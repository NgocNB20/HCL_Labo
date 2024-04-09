SELECT
  memberInfo.*,
  customeraddressbook.*
FROM
  memberInfo memberInfo
LEFT JOIN customeraddressbook
  ON memberInfo.memberinfoaddressid = customeraddressbook.addressid
WHERE
  memberInfo.shopseq = /*conditionDto.shopSeq*/0

/*%if conditionDto.memberInfoStatus != null*/
  AND
    memberInfo.memberinfostatus = /*conditionDto.memberInfoStatus.value*/0
/*%end*/

/*%if conditionDto.lastLoginUserAgent != null*/
  AND
    memberInfo.lastLoginUserAgent LIKE '%' || /*conditionDto.lastLoginUserAgent*/0 || '%'
/*%end*/

/*%if conditionDto.memberInfoId != null*/
  AND
    memberInfo.memberinfoid LIKE '%' || /*conditionDto.memberInfoId*/0 || '%'
/*%end*/

/*%if conditionDto.memberInfoSeq != null*/
  AND
    memberInfo.memberInfoSeq = /*conditionDto.memberInfoSeq*/0
/*%end*/

/*%if conditionDto.memberInfoSex != null*/
  AND
    memberInfo.memberInfoSex = /*conditionDto.memberInfoSex.value*/0
/*%end*/

/*%if conditionDto.memberInfoBirthday != null*/
  AND
    memberInfo.memberInfoBirthday = /*conditionDto.memberInfoBirthday*/0
/*%end*/

/*%if conditionDto.memberInfoTel != null*/
  AND
    memberInfo.memberInfoTel LIKE '%' || /*conditionDto.memberInfoTel*/0 || '%'
/*%end*/

/*%if conditionDto.memberInfoZipCode != null*/
  AND
    customeraddressbook.zipcode LIKE '%' || /*conditionDto.memberInfoZipCode*/0 || '%'
/*%end*/

/*%if conditionDto.memberInfoPrefecture != null*/
  AND
    customeraddressbook.prefecture = /*conditionDto.memberInfoPrefecture*/0
/*%end*/

/*%if conditionDto.memberInfoName != null*/
  AND
    (
    memberInfo.memberInfoLastName || memberInfo.memberInfoFirstName LIKE '%' || /*conditionDto.memberInfoName*/0 || '%' or
    memberInfo.memberInfoLastKana || memberInfo.memberInfoFirstKana LIKE '%' || /*conditionDto.memberInfoName*/0 || '%'
    )

/*%end*/

/*%if conditionDto.memberInfoAddress != null*/
  AND
    customeraddressbook.prefecture || customeraddressbook.address1 || customeraddressbook.address2 || coalesce(customeraddressbook.address3,'') LIKE '%' || /*conditionDto.memberInfoAddress*/0 || '%'
/*%end*/

/*%if conditionDto.periodType != null*/
    /*%if conditionDto.periodType == "0"*/
        /*%if conditionDto.startDate != null*/
            AND
                TO_DATE(memberInfo.admissionymd, 'yyyyMMdd') >= TO_DATE(/*conditionDto.startDate*/0, 'yyyy/MM/dd')
        /*%end*/
        /*%if conditionDto.endDate != null*/
            AND
                TO_DATE(memberInfo.admissionymd, 'yyyyMMdd') <= TO_DATE(/*conditionDto.endDate*/0, 'yyyy/MM/dd')
        /*%end*/
    /*%end*/

    /*%if conditionDto.periodType == "1"*/
        /*%if conditionDto.startDate != null*/
            AND
                TO_DATE(memberInfo.secessionymd, 'yyyyMMdd') >= TO_DATE(/*conditionDto.startDate*/0, 'yyyy/MM/dd')
        /*%end*/
        /*%if conditionDto.endDate != null*/
            AND
                TO_DATE(memberInfo.secessionymd, 'yyyyMMdd') <= TO_DATE(/*conditionDto.endDate*/0, 'yyyy/MM/dd')
        /*%end*/
    /*%end*/
/*%end*/

/*%if conditionDto.mailMagazine != null*/
    /*%if conditionDto.mailMagazine.value == "0"*/
      AND
        EXISTS (SELECT mailMagazineMember.* FROM mailMagazineMember WHERE mailmagazinemember.sendstatus = '0' AND mailmagazinemember.mail = memberInfo.memberinfoid AND mailmagazinemember.memberinfoseq = memberInfo.memberinfoseq)
    /*%end*/
/*%end*/

/*%if conditionDto.mainMemberFlag != null*/
    /*%if conditionDto.mainMemberFlag.value == "0"*/
      AND
      NOT (memberInfo.memberInfoUniqueId IS NULL AND memberInfo.memberinfostatus = '0')
    /*%end*/
    /*%if conditionDto.mainMemberFlag.value == "1"*/
      AND
      (memberInfo.memberInfoUniqueId IS NULL AND memberInfo.memberinfostatus = '0')
    /*%end*/
/*%end*/

/*************** sort ***************/

ORDER BY 
/*%if conditionDto.pageInfo.orderField != null*/

/*%if conditionDto.pageInfo.orderField == "memberInfoSeq"*/
memberInfo.memberInfoSeq /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoId"*/
memberInfo.memberInfoId /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoStatus"*/
memberInfo.memberInfoStatus /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "mailMagazine"*/
CASE WHEN (SELECT mailMagazineMember.sendstatus FROM mailMagazineMember WHERE mailmagazinemember.sendstatus = '0' AND mailmagazinemember.mail = memberInfo.memberinfoid AND mailmagazinemember.memberinfoseq = memberInfo.memberinfoseq) != '' THEN '0' ELSE '1' END /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoName"*/
memberInfo.memberInfoLastName /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , memberInfo.memberInfoFirstName
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoTel"*/
memberInfo.memberInfoTel /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoZipCode"*/
customeraddressbook.zipCode /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoAddress"*/
customeraddressbook.prefecture /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
       , customeraddressbook.address1, customeraddressbook.address2, customeraddressbook.address3
       , memberInfo.memberinfoseq
/*%end*/

/*%if conditionDto.pageInfo.orderField == "memberInfoRegistTime"*/
memberInfo.registTime /*%if conditionDto.pageInfo.orderAsc*/ ASC /*%else*/ DESC /*%end*/
/*%end*/

/*%else*/
    1 ASC
/*%end*/
