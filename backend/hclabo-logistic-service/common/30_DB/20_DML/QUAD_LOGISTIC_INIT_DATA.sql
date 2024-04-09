-- INSERT DELIVERYMETHOD
INSERT INTO public.deliverymethod(deliverymethodseq,shopseq,deliverymethodname,deliverymethoddisplaynamepc,deliverymethoddisplaynamemb,deliverychaseurl,deliverychaseurldisplayperiod,openstatuspc,openstatusmb,deliverynotepc,deliverynotemb,deliverymethodtype,equalscarriage,largeamountdiscountprice,largeamountdiscountcarriage,shortfalldisplayflag,deliveryleadtime,possibleselectdays,receivertimezone1,receivertimezone2,receivertimezone3,receivertimezone4,receivertimezone5,receivertimezone6,receivertimezone7,receivertimezone8,receivertimezone9,receivertimezone10,orderdisplay,registtime,updatetime) VALUES
    (1001,1001,'郵便局','郵便局',NULL,'https://trackings.post.japanpost.jp/services/srv/search/direct?locale=ja&reqCodeNo1={0}',100,'1','0',NULL,NULL,'1',500,0,0,'0',0,0,'指定なし',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,TIMESTAMP '2022-03-17 10:48:55.000',TIMESTAMP '2024-02-14 15:23:36.000');
-- INSERT DELIVERYMETHODTYPECARRIAGE
INSERT INTO public.deliverymethodtypecarriage(deliverymethodseq,prefecturetype,maxprice,carriage,registtime,updatetime) VALUES
    (1001,'01',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'02',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'03',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'04',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'05',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'06',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'07',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'08',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'09',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'10',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'11',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'12',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'13',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'14',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'15',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'16',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'17',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'18',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'19',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'20',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'21',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'22',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'23',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'24',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'25',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'26',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'27',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'28',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'29',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'30',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'31',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'32',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'33',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'34',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'35',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'36',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'37',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'38',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'39',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'40',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'41',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'42',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'43',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'44',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'45',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'46',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'47',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'98',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000')
  , (1001,'99',0,0,TIMESTAMP '2024-02-14 15:23:36.000',TIMESTAMP '2024-02-14 15:23:36.000');

SELECT SETVAL ('deliverymethodseq', 1002);