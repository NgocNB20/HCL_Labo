UPDATE memberInfo
SET
  lastLoginTime = CURRENT_TIMESTAMP
  , lastLoginUserAgent = /*userAgent*/0
WHERE
  memberInfoSeq = /*memberInfoSeq*/0
