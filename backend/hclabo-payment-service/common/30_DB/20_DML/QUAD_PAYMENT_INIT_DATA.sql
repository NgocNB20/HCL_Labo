-- INSERT TABLE public.settlementmethod
INSERT INTO public.settlementmethod(settlementmethodseq,shopseq,settlementmethodname,settlementmethoddisplaynamepc,settlementmethoddisplaynamemb,openstatuspc,openstatusmb,settlementnotepc,settlementnotemb,settlementmethodtype,settlementmethodcommissiontype,billtype,deliverymethodseq,equalscommission,settlementmethodpricecommissionflag,largeamountdiscountprice,largeamountdiscountcommission,orderdisplay,maxpurchasedprice,minpurchasedprice,settlementmailrequired,enablecardnoholding,enablesecuritycode,enable3dsecure,enableinstallment,enablebonussinglepayment,enablebonusinstallment,enablerevolving,registtime,updatetime) VALUES
    (1001,1001,'クレジット（後決済）','クレジット（後決済）',NULL,'1','0','VISAカード、マスターカード、DCカード、UFJニコス、JCB、AMEX、Dinersがご利用いただけます。<br>お支払い回数は一括払いとさせていただきます。',NULL,'0','0','1',NULL,0,'0',NULL,NULL,1,999999,1,'0','1','0','1','1','0','0','1',TIMESTAMP '2022-03-17 10:48:55.000',TIMESTAMP '2024-02-14 15:50:45.000')
  , (1002,1001,'リンク決済','リンク決済',NULL,'1','0','下記の決済方法をご利用いただけます。<br>ご注文内容確定後、「PGマルチペインメントサービス決済ページ」に遷移しますので、決済方法を選択しお手続きをしてください。',NULL,'5','0','0',NULL,0,'0',NULL,NULL,6,0,0,'1','0','0','0','0','0','0','0',TIMESTAMP '2023-01-11 18:16:13.000',TIMESTAMP '2024-02-14 15:51:23.000');
SELECT SETVAL ('settlementmethodseq', 1003);

-- INSERT TABLE public.convenience
INSERT INTO public.convenience (conveniseq,paycode,convenicode,payname,conveniname,shopseq,useflag,displayorder,registtime,updatetime) VALUES
(1,'999','00007','セブン-イレブン決済','セブン-イレブン',1001,'1',1,'2022-03-17 10:48:55.000','2022-03-17 10:48:55.000'),
(9,'999','10001','ローソンLoppi決済','ローソン',1001,'1',2,'2022-03-17 10:48:55.000','2022-03-17 10:48:55.000'),
(10,'999','10002','ファミリーマート決済','ファミリーマート',1001,'1',3,'2022-03-17 10:48:55.000','2022-03-17 10:48:55.000'),
(12,'999','10005','ミニストップ決済','ミニストップ',1001,'1',5,'2022-03-17 10:48:55.000','2022-03-17 10:48:55.000'),
(13,'999','10008','セイコーマート決済','セイコーマート',1001,'1',7,'2022-03-17 10:48:55.000','2022-03-17 10:48:55.000');

-- INSERT TABLE public.CARDBRAND
INSERT INTO public.cardbrand (cardbrandseq,cardbrandcode,cardbrandname,cardbranddisplaypc,cardbranddisplaymb,orderdisplay,installment,bounussingle,bounusinstallment,revolving,installmentcounts,frontdisplayflag) VALUES
(1,'2a99662','DC','DC','',1,'0','0','0','0','3,5,6,10,12,15,18,20,24','1'),
(2,'2a99664','UFJ-NICOS','UFJニコス','',2,'0','0','0','0','3,5,6,10,12,15,18,20,24','1'),
(3,'2a99661','JCB','JCB','',3,'0','0','0','0','3,5,6,10,12,15,18,20,24','1'),
(4,'137','AMEX','AMEX','',4,'0','0','0','0','3,5,6,10,12,15,18,20,24','1'),
(5,'2a99660','Diners','Diners','',5,'0','0','0','0',NULL,'1');

-- INSERT TABLE public.MULPAYSITE
INSERT INTO public.mulpaysite (siteid,sitepassword,siteaccessurl) VALUES
('tsite00053587','a7hgssr6','https://kt01.mul-pay.jp/mulpayconsole/site/tsite00053587/');

-- INSERT TABLE public.MULPAYSHOP
INSERT INTO public.mulpayshop (shopseq,shopid,shoppass,shopaccessurl,tdtenantname,httpaccept,httpuseragent,clientfield1,clientfield2,clientfield3,clientfieldflag,shopmailaddress,registerdisp1,registerdisp2,registerdisp3,registerdisp4,registerdisp5,registerdisp6,registerdisp7,registerdisp8,receiptsdisp1,receiptsdisp2,receiptsdisp3,receiptsdisp4,receiptsdisp5,receiptsdisp6,receiptsdisp7,receiptsdisp8,receiptsdisp9,receiptsdisp10,receiptsdisp11,receiptsdisp12,receiptsdisp13,itemname) VALUES
(1001,'tshop00065388','5s1zrsff','https://kt01.mul-pay.jp/mulpayconsole/shop/tshop00065388/','HIT-MALL4','','','','','',' ','','ヒットモール４','','','','','','','','毎度ご利用ありがとうございます。','','','','','','','','','','ヒットモール４','06-1234-5678','09:00-18:00','');

-- INSERT TABLE public.SETTLEMENTMETHODLINK
insert  into public.settlementmethodlink (paymethod, paytype, paytypename, cancellimitday, cancellimitmonth) values ('cvs', 3, 'コンビニ', null ,null);
insert  into public.settlementmethodlink (paymethod, paytype, paytypename, cancellimitday, cancellimitmonth) values ('paypay', 45,'PayPay', 180, null);
