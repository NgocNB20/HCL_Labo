SELECT
    memberInfo.memberinfoseq,
    memberInfo.memberInfoMail AS mail,
    memberInfo.memberinfostatus,
    memberInfo.memberinfoid,
    memberInfo.memberinfolastname,
    memberInfo.memberinfofirstname,
    memberInfo.memberinfolastkana,
    memberInfo.memberinfofirstkana,
    memberInfo.memberinfosex,
    memberInfo.memberinfobirthday,
    memberInfo.memberinfotel,
    memberInfo.admissionymd,
    memberInfo.secessionymd,
    memberInfo.lastlogintime,
    memberInfo.lastloginuseragent,
    CASE WHEN (SELECT mailMagazineMember.sendstatus FROM mailMagazineMember WHERE mailmagazinemember.sendstatus = '0' AND  mailmagazinemember.mail = memberInfo.memberinfoid AND mailmagazinemember.memberinfoseq = memberInfo.memberinfoseq) != '' THEN '0' ELSE '1' END AS mailMagazine,
    memberInfo.registtime,
    memberInfo.updatetime,
    customeraddressbook.zipcode,
    customeraddressbook.prefecture,
    customeraddressbook.address1,
    customeraddressbook.address2,
    customeraddressbook.address3
FROM
    (SELECT * FROM memberInfo WHERE memberInfoSeq IN /*memberInfoSeqList*/(1,2,3)) AS memberInfo

LEFT JOIN customeraddressbook ON memberInfo.memberinfoaddressid = customeraddressbook.addressid

ORDER BY
    memberInfo.memberInfoSeq
