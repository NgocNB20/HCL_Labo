INSERT
INTO
    ADDRESSBOOK
(addressid,
 customerid,
 addressname,
 lastname,
 firstname,
 lastkana,
 firstkana,
 tel,
 zipcode,
 prefecture,
 address1,
 address2,
 address3,
 shippingmemo,
 registdate,
 hideflag,
 defaultflag)
VALUES(
    /* addressBookDbEntity.addressId */'67a18853-30a1-41c1-ba31-32d05818c1ac',
    /* addressBookDbEntity.customerId */'10000006',
    /* addressBookDbEntity.addressName */'田中花子',
    /* addressBookDbEntity.lastName */'田中',
    /* addressBookDbEntity.firstName */'花子',
    /* addressBookDbEntity.lastKana */'タナカ',
    /* addressBookDbEntity.firstKana */'ハナコ',
    /* addressBookDbEntity.tel */'11122223333',
    /* addressBookDbEntity.zipCode */'5560013',
    /* addressBookDbEntity.prefecture */'大阪府',
    /* addressBookDbEntity.address1 */'旭川市',
    /* addressBookDbEntity.address2 */'１－４７',
    /* addressBookDbEntity.address3 */'ＨＩＴ－ＭＡＬＬマンション',
    /* addressBookDbEntity.shippingMemo */'不在時宅配ボックス',
    /* addressBookDbEntity.registDate */'2022-07-06 14:20:29.390',
    /* addressBookDbEntity.hideFlag */false,
    /* addressBookDbEntity.defaultFlag */false)
    ON CONFLICT(ADDRESSID) DO NOTHING
