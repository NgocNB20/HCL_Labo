select
    confirmmail.*
from
    confirmMail
where
    confirmmail.confirmmailpassword = /*password*/0
/*%if(confirmMailType != null)*/
and
    confirmmailtype = /*confirmMailType.value*/'type'
/*%end*/
and
    confirmmail.effectiveTime >= current_timestamp
