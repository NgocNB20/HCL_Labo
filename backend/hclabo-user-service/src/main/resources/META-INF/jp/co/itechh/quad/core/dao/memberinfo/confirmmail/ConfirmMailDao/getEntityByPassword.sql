select
    confirmmail.*
from
    confirmMail
where
    confirmmail.confirmmailpassword = /*password*/0
and
    confirmmail.effectiveTime >= current_timestamp
