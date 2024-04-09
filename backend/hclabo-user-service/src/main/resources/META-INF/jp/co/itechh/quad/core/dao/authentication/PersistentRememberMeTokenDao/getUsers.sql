select
    username
     ,series
     ,token
     ,last_used
from persistent_logins
where username = /*username*/'demo@itec.hankyu-hanshin.co.jp'
