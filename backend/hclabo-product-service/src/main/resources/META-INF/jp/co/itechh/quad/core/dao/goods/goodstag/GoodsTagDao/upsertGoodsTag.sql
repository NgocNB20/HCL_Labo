INSERT INTO GOODSTAG
(tag, count, registtime, updatetime)
VALUES
    ( /*tag*/0
    , /*addCount*/0
    , now()
    , now())
    
    ON CONFLICT (tag)
DO UPDATE SET
   count =	(select gt.count from goodstag gt where gt.tag = /*tag*/0) + /*addCount*/0,
   updatetime = now()