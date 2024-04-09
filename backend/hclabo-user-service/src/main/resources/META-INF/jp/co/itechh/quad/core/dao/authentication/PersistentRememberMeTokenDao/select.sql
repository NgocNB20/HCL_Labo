select
    username
     ,series
     ,token
     ,last_used
from persistent_logins
where series = /*series*/0
