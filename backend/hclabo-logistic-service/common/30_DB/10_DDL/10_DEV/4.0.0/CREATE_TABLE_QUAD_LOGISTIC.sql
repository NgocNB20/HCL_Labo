-- DROP TABLE
DROP TABLE IF EXISTS shippingslip CASCADE;
DROP TABLE IF EXISTS securedshippingitem CASCADE;
DROP TABLE IF EXISTS shippingslipforrevision CASCADE;
DROP TABLE IF EXISTS securedshippingitemforrevision CASCADE;
DROP TABLE IF EXISTS addressbook CASCADE;
DROP TABLE IF EXISTS stocksetting CASCADE;
DROP TABLE IF EXISTS stock CASCADE;
DROP TABLE IF EXISTS stockresult CASCADE;
DROP TABLE IF EXISTS deliverymethod CASCADE;
DROP TABLE IF EXISTS deliverymethodtypecarriage CASCADE;
DROP TABLE IF EXISTS deliveryspecialchargearea CASCADE;
DROP TABLE IF EXISTS deliveryimpossiblearea CASCADE;
DROP TABLE IF EXISTS holiday CASCADE;
DROP TABLE IF EXISTS deliveryimpossibleday CASCADE;
DROP TABLE IF EXISTS zipcode CASCADE;
DROP TABLE IF EXISTS officezipcode CASCADE;
DROP TABLE IF EXISTS noveltypresentcondition CASCADE;
DROP TABLE IF EXISTS noveltypresentenclosuregoods CASCADE;
DROP TABLE IF EXISTS noveltypresentmember CASCADE;

-- CREATE TABLE
-- CREATE SHIPPINGSLIP
CREATE TABLE public.shippingslip (
                                     shippingslipid text NOT NULL, -- 配送伝票ID
                                     shippingstatus text NULL, -- 配送ステータス
                                     shipmentstatusconfirmcode text NULL, -- 配送状況確認番号
                                     shippingmethodid text NULL, -- 配送方法ID
                                     shippingmethodname text NULL, -- 配送方法名
                                     shippingaddressid text NULL, -- 配送先住所ID
                                     transactionid text NULL, -- 取引ID
                                     invoicenecessaryflag bool NULL, -- 納品書要否フラグ
                                     receiverdate timestamp NULL, -- お届け希望日
                                     receivertimezone text NULL, -- お届け希望時間帯
                                     completeshipmentdate timestamp NULL, -- 出荷完了日時
                                     registdate timestamp NULL, -- 登録日時
                                     CONSTRAINT shippingslip_pk PRIMARY KEY (shippingslipid)
);
COMMENT ON TABLE public.shippingslip IS '配送伝票';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.shippingslip.shippingslipid IS '配送伝票ID';
COMMENT ON COLUMN public.shippingslip.shippingstatus IS '配送ステータス';
COMMENT ON COLUMN public.shippingslip.shipmentstatusconfirmcode IS '配送状況確認番号';
COMMENT ON COLUMN public.shippingslip.shippingmethodid IS '配送方法ID';
COMMENT ON COLUMN public.shippingslip.shippingmethodname IS '配送方法名';
COMMENT ON COLUMN public.shippingslip.shippingaddressid IS '配送先住所ID';
COMMENT ON COLUMN public.shippingslip.transactionid IS '取引ID';
COMMENT ON COLUMN public.shippingslip.invoicenecessaryflag IS '納品書要否フラグ';
COMMENT ON COLUMN public.shippingslip.receiverdate IS 'お届け希望日';
COMMENT ON COLUMN public.shippingslip.receivertimezone IS 'お届け希望時間帯';
COMMENT ON COLUMN public.shippingslip.completeshipmentdate IS '出荷完了日時';
COMMENT ON COLUMN public.shippingslip.registdate IS '登録日時';

-- CREATE SECUREDSHIPPINGITEM
CREATE TABLE public.securedshippingitem (
                                            id bigserial NOT NULL,
                                            shippingitemseq float8 NULL, -- 配送商品連番（注文商品連番）
                                            itemid text NULL, -- 商品ID（商品サービスの商品SEQ）
                                            itemname text NULL, -- 商品名
                                            unittitle1 text NULL, -- 規格タイトル1
                                            unitvalue1 text NULL, -- 規格値1
                                            unittitle2 text NULL, -- 規格タイトル2
                                            unitvalue2 text NULL, -- 規格値2
                                            shippingcount float8 NULL, -- 配送数量
                                            shippingslipid text NOT NULL, -- 配送伝票ID
                                            CONSTRAINT securedshippingitem_shippingitemseq_shippingslipid_key UNIQUE (shippingitemseq, shippingslipid),
                                            CONSTRAINT shippingitem_pk PRIMARY KEY (id, shippingslipid)
);
COMMENT ON TABLE public.securedshippingitem IS '確保済み配送商品';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.securedshippingitem.shippingitemseq IS '配送商品連番（注文商品連番）';
COMMENT ON COLUMN public.securedshippingitem.itemid IS '商品ID（商品サービスの商品SEQ）';
COMMENT ON COLUMN public.securedshippingitem.itemname IS '商品名';
COMMENT ON COLUMN public.securedshippingitem.unittitle1 IS '規格タイトル1';
COMMENT ON COLUMN public.securedshippingitem.unitvalue1 IS '規格値1';
COMMENT ON COLUMN public.securedshippingitem.unittitle2 IS '規格タイトル2';
COMMENT ON COLUMN public.securedshippingitem.unitvalue2 IS '規格値2';
COMMENT ON COLUMN public.securedshippingitem.shippingcount IS '配送数量';
COMMENT ON COLUMN public.securedshippingitem.shippingslipid IS '配送伝票ID';

-- public.securedshippingitem foreign keys
ALTER TABLE public.securedshippingitem ADD CONSTRAINT shippingitem_fk FOREIGN KEY (shippingslipid) REFERENCES public.shippingslip(shippingslipid);

-- CREATE SHIPPINGSLIPFORREVISION
CREATE TABLE public.shippingslipforrevision (
                                                shippingsliprevisionid text NOT NULL, -- 改訂用配送伝票ID
                                                shippingslipid text NULL, -- 配送伝票ID
                                                shippingstatus text NULL, -- 配送ステータス
                                                shipmentstatusconfirmcode text NULL, -- 配送状況確認番号
                                                shippingmethodid text NULL, -- 配送方法ID
                                                shippingmethodname text NULL, -- 配送方法名
                                                shippingaddressid text NULL, -- 配送先住所ID
                                                transactionid text NULL, -- 取引ID
                                                transactionrevisionid text NULL, -- 改訂用取引ID
                                                invoicenecessaryflag bool NULL, -- 納品書要否フラグ
                                                receiverdate timestamp NULL, -- お届け希望日
                                                receivertimezone text NULL, -- お届け希望時間帯
                                                completeshipmentdate timestamp NULL, -- 出荷完了日時
                                                registdate timestamp NULL, -- 登録日時
                                                CONSTRAINT shippingslipforrevision_pk PRIMARY KEY (shippingsliprevisionid)
);
COMMENT ON TABLE public.shippingslipforrevision IS '改訂用配送伝票';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.shippingslipforrevision.shippingsliprevisionid IS '改訂用配送伝票ID';
COMMENT ON COLUMN public.shippingslipforrevision.shippingslipid IS '配送伝票ID';
COMMENT ON COLUMN public.shippingslipforrevision.shippingstatus IS '配送ステータス';
COMMENT ON COLUMN public.shippingslipforrevision.shipmentstatusconfirmcode IS '配送状況確認番号';
COMMENT ON COLUMN public.shippingslipforrevision.shippingmethodid IS '配送方法ID';
COMMENT ON COLUMN public.shippingslipforrevision.shippingmethodname IS '配送方法名';
COMMENT ON COLUMN public.shippingslipforrevision.shippingaddressid IS '配送先住所ID';
COMMENT ON COLUMN public.shippingslipforrevision.transactionid IS '取引ID';
COMMENT ON COLUMN public.shippingslipforrevision.transactionrevisionid IS '改訂用取引ID';
COMMENT ON COLUMN public.shippingslipforrevision.invoicenecessaryflag IS '納品書要否フラグ';
COMMENT ON COLUMN public.shippingslipforrevision.receiverdate IS 'お届け希望日';
COMMENT ON COLUMN public.shippingslipforrevision.receivertimezone IS 'お届け希望時間帯';
COMMENT ON COLUMN public.shippingslipforrevision.completeshipmentdate IS '出荷完了日時';
COMMENT ON COLUMN public.shippingslipforrevision.registdate IS '登録日時';

-- CREATE SECUREDSHIPPINGITEMFORREVISION
CREATE TABLE public.securedshippingitemforrevision (
                                                       id bigserial NOT NULL,
                                                       shippingitemseq float8 NULL, -- 配送商品連番（注文商品連番）
                                                       itemid text NULL, -- 商品ID（商品サービスの商品SEQ）
                                                       itemname text NULL, -- 商品名
                                                       unittitle1 text NULL, -- 規格タイトル1
                                                       unitvalue1 text NULL, -- 規格値1
                                                       unittitle2 text NULL, -- 規格タイトル2
                                                       unitvalue2 text NULL, -- 規格値2
                                                       shippingcount float8 NULL, -- 配送数量
                                                       shippingsliprevisionid text NOT NULL, -- 改訂用配送伝票ID
                                                       CONSTRAINT securedshippingitemforrevisio_shippingitemseq_shippingslipr_key UNIQUE (shippingitemseq, shippingsliprevisionid),
                                                       CONSTRAINT shippingitemforrevision_pk PRIMARY KEY (id, shippingsliprevisionid)
);
COMMENT ON TABLE public.securedshippingitemforrevision IS '改訂用確保済み配送商品';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.securedshippingitemforrevision.shippingitemseq IS '配送商品連番（注文商品連番）';
COMMENT ON COLUMN public.securedshippingitemforrevision.itemid IS '商品ID（商品サービスの商品SEQ）';
COMMENT ON COLUMN public.securedshippingitemforrevision.itemname IS '商品名';
COMMENT ON COLUMN public.securedshippingitemforrevision.unittitle1 IS '規格タイトル1';
COMMENT ON COLUMN public.securedshippingitemforrevision.unitvalue1 IS '規格値1';
COMMENT ON COLUMN public.securedshippingitemforrevision.unittitle2 IS '規格タイトル2';
COMMENT ON COLUMN public.securedshippingitemforrevision.unitvalue2 IS '規格値2';
COMMENT ON COLUMN public.securedshippingitemforrevision.shippingcount IS '配送数量';
COMMENT ON COLUMN public.securedshippingitemforrevision.shippingsliprevisionid IS '改訂用配送伝票ID';


-- public.securedshippingitemforrevision foreign keys
ALTER TABLE public.securedshippingitemforrevision ADD CONSTRAINT shippingitemforrevision_fk FOREIGN KEY (shippingsliprevisionid) REFERENCES public.shippingslipforrevision(shippingsliprevisionid);

-- CREATE ADDRESSBOOK
CREATE TABLE public.addressbook (
                                    addressid text NOT NULL, -- 住所ID
                                    customerid text NULL, -- 顧客ID
                                    addressname text NULL, -- 住所名
                                    lastname text NULL, -- 氏名(姓)
                                    firstname text NULL, -- 氏名(名)
                                    lastkana text NULL, -- フリガナ(姓)
                                    firstkana text NULL, -- フリガナ(名)
                                    tel text NULL, -- 電話番号
                                    zipcode text NULL, -- 郵便番号
                                    prefecture text NULL, -- 都道府県
                                    address1 text NULL, -- 住所1
                                    address2 text NULL, -- 住所2
                                    address3 text NULL, -- 住所3
                                    shippingmemo text NULL, -- 配送メモ
                                    registdate timestamp NULL, -- 登録日時
                                    hideflag bool NULL, -- 非表示フラグ
                                    defaultflag bool NOT NULL DEFAULT FALSE, -- デフォルト指定フラグ
                                    CONSTRAINT addressbook_pk PRIMARY KEY (addressid)
);
COMMENT ON TABLE public.addressbook IS '住所録';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.addressbook.addressid IS '住所ID';
COMMENT ON COLUMN public.addressbook.customerid IS '顧客ID';
COMMENT ON COLUMN public.addressbook.addressname IS '住所名';
COMMENT ON COLUMN public.addressbook.lastname IS '氏名(姓)';
COMMENT ON COLUMN public.addressbook.firstname IS '氏名(名)';
COMMENT ON COLUMN public.addressbook.lastkana IS 'フリガナ(姓)';
COMMENT ON COLUMN public.addressbook.firstkana IS 'フリガナ(名)';
COMMENT ON COLUMN public.addressbook.tel IS '電話番号';
COMMENT ON COLUMN public.addressbook.zipcode IS '郵便番号';
COMMENT ON COLUMN public.addressbook.prefecture IS '都道府県';
COMMENT ON COLUMN public.addressbook.address1 IS '住所1';
COMMENT ON COLUMN public.addressbook.address2 IS '住所2';
COMMENT ON COLUMN public.addressbook.address3 IS '住所3';
COMMENT ON COLUMN public.addressbook.shippingmemo IS '配送メモ';
COMMENT ON COLUMN public.addressbook.registdate IS '登録日時';
COMMENT ON COLUMN public.addressbook.hideflag IS '非表示フラグ';
COMMENT ON COLUMN public.addressbook.defaultflag IS 'デフォルト指定フラグ';

-- CREATE STOCKSETTING
CREATE TABLE public.stocksetting (
                                     goodsseq numeric(8) NOT NULL, -- 商品SEQ
                                     shopseq numeric(4) NOT NULL, -- ショップSEQ
                                     remainderfewstock numeric(6) NOT NULL, -- 残少表示在庫数
                                     orderpointstock numeric(6) NOT NULL, -- 発注点在庫数
                                     safetystock numeric(6) NOT NULL, -- 安全在庫数
                                     registtime timestamp(0) NOT NULL, -- 登録日時
                                     stockmanagementflag varchar(1) NOT NULL DEFAULT 0, -- 在庫管理フラグ
                                     updatetime timestamp(0) NOT NULL, -- 更新日時
                                     CONSTRAINT stocksetting_pkey PRIMARY KEY (goodsseq)
);
COMMENT ON TABLE public.stocksetting IS '在庫設定';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.stocksetting.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.stocksetting.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.stocksetting.remainderfewstock IS '残少表示在庫数';
COMMENT ON COLUMN public.stocksetting.orderpointstock IS '発注点在庫数';
COMMENT ON COLUMN public.stocksetting.safetystock IS '安全在庫数';
COMMENT ON COLUMN public.stocksetting.registtime IS '登録日時';
COMMENT ON COLUMN public.stocksetting.updatetime IS '更新日時';

-- CREATE STOCK
CREATE TABLE public.stock (
                              goodsseq numeric(8) NOT NULL, -- 商品SEQ
                              shopseq numeric(4) NOT NULL, -- ショップSEQ
                              realstock numeric(6) NOT NULL, -- 実在庫数
                              orderreservestock numeric(6) NOT NULL, -- 注文確保在庫数
                              registtime timestamp(0) NOT NULL, -- 登録日時
                              updatetime timestamp(0) NOT NULL, -- 更新日時
                              pregoodscount numeric(4) NULL, -- 前回商品数量
                              goodscount numeric(4) NULL, -- 商品数量
                              CONSTRAINT stock_pkey PRIMARY KEY (goodsseq)
);
COMMENT ON TABLE public.stock IS '在庫';

-- Column comments

COMMENT ON COLUMN public.stock.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.stock.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.stock.realstock IS '実在庫数';
COMMENT ON COLUMN public.stock.orderreservestock IS '注文確保在庫数';
COMMENT ON COLUMN public.stock.registtime IS '登録日時';
COMMENT ON COLUMN public.stock.updatetime IS '更新日時';
COMMENT ON COLUMN public.stock.pregoodscount IS '前回商品数量';
COMMENT ON COLUMN public.stock.goodscount IS '商品数量';

-- CREATE STOCKRESULT
CREATE TABLE public.stockresult (
                                    stockresultseq numeric(8) NOT NULL, -- 入庫実績SEQ
                                    goodsseq numeric(8) NOT NULL, -- 商品SEQ
                                    supplementtime timestamp(0) NOT NULL, -- 入庫日時
                                    supplementcount numeric(6) NOT NULL, -- 入庫数
                                    realstock numeric(6) NOT NULL, -- 実在庫数
                                    processpersonname varchar(40) NULL, -- 処理担当者名
                                    note varchar(200) NULL, -- 備考
                                    stockmanagementflag varchar(1) NOT NULL DEFAULT 1, -- 在庫管理フラグ
                                    registtime timestamp(0) NOT NULL, -- 登録日時
                                    updatetime timestamp(0) NOT NULL, -- 更新日時
                                    CONSTRAINT stockresult_pkey PRIMARY KEY (stockresultseq)
);
CREATE INDEX idx_stockresult_goodsseq ON public.stockresult USING btree (goodsseq);
CREATE INDEX idx_stockresult_supplementtime ON public.stockresult USING btree (supplementtime);
COMMENT ON TABLE public.stockresult IS '入庫実績';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.stockresult.stockresultseq IS '入庫実績SEQ';
COMMENT ON COLUMN public.stockresult.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.stockresult.supplementtime IS '入庫日時';
COMMENT ON COLUMN public.stockresult.supplementcount IS '入庫数';
COMMENT ON COLUMN public.stockresult.realstock IS '実在庫数';
COMMENT ON COLUMN public.stockresult.processpersonname IS '処理担当者名';
COMMENT ON COLUMN public.stockresult.note IS '備考';
COMMENT ON COLUMN public.stockresult.stockmanagementflag IS '在庫管理フラグ';
COMMENT ON COLUMN public.stockresult.registtime IS '登録日時';
COMMENT ON COLUMN public.stockresult.updatetime IS '更新日時';

-- CREATE DELIVERYMETHOD
CREATE TABLE public.deliverymethod (
                                       deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                       shopseq numeric(4) NOT NULL, -- ショップSEQ
                                       deliverymethodname varchar(60) NOT NULL, -- 配送方法名
                                       deliverymethoddisplaynamepc varchar(60) NULL, -- 配送方法表示名PC
                                       deliverymethoddisplaynamemb varchar(60) NULL, -- 配送方法表示名携帯
                                       deliverychaseurl text NULL, -- 配送追跡URL
                                       deliverychaseurldisplayperiod numeric(3) NULL, -- 配送追跡URL表示期間
                                       openstatuspc varchar(1) NOT NULL DEFAULT 0, -- 公開状態PC
                                       openstatusmb varchar(1) NULL DEFAULT 0, -- 公開状態携帯
                                       deliverynotepc varchar(2000) NULL, -- 配送方法説明文PC
                                       deliverynotemb varchar(2000) NULL, -- 配送方法説明文携帯
                                       deliverymethodtype varchar(1) NOT NULL, -- 配送方法種別
                                       equalscarriage numeric(8) NULL DEFAULT 0, -- 一律送料
                                       largeamountdiscountprice numeric(8) NULL DEFAULT 0, -- 高額割引下限金額
                                       largeamountdiscountcarriage numeric(8) NULL DEFAULT 0, -- 高額割引送料
                                       shortfalldisplayflag varchar(1) NOT NULL DEFAULT 0, -- 不足金額表示フラグ
                                       deliveryleadtime numeric(3) NOT NULL DEFAULT 0, -- リードタイム
                                       possibleselectdays numeric(3) NOT NULL DEFAULT 0, -- 選択可能日数
                                       receivertimezone1 varchar(100) NULL, -- お届け時間帯1
                                       receivertimezone2 varchar(100) NULL, -- お届け時間帯2
                                       receivertimezone3 varchar(100) NULL, -- お届け時間帯3
                                       receivertimezone4 varchar(100) NULL, -- お届け時間帯4
                                       receivertimezone5 varchar(100) NULL, -- お届け時間帯5
                                       receivertimezone6 varchar(100) NULL, -- お届け時間帯6
                                       receivertimezone7 varchar(100) NULL, -- お届け時間帯7
                                       receivertimezone8 varchar(100) NULL, -- お届け時間帯8
                                       receivertimezone9 varchar(100) NULL, -- お届け時間帯9
                                       receivertimezone10 varchar(100) NULL, -- お届け時間帯10
                                       orderdisplay numeric(4) NULL, -- 表示順
                                       registtime timestamp(0) NOT NULL, -- 登録日時
                                       updatetime timestamp(0) NOT NULL, -- 更新日時
                                       CONSTRAINT deliverymethod_pkey PRIMARY KEY (deliverymethodseq)
);
COMMENT ON TABLE public.deliverymethod IS '配送方法';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.deliverymethod.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.deliverymethod.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.deliverymethod.deliverymethodname IS '配送方法名';
COMMENT ON COLUMN public.deliverymethod.deliverymethoddisplaynamepc IS '配送方法表示名PC';
COMMENT ON COLUMN public.deliverymethod.deliverymethoddisplaynamemb IS '配送方法表示名携帯';
COMMENT ON COLUMN public.deliverymethod.deliverychaseurl IS '配送追跡URL';
COMMENT ON COLUMN public.deliverymethod.deliverychaseurldisplayperiod IS '配送追跡URL表示期間';
COMMENT ON COLUMN public.deliverymethod.openstatuspc IS '公開状態PC';
COMMENT ON COLUMN public.deliverymethod.openstatusmb IS '公開状態携帯';
COMMENT ON COLUMN public.deliverymethod.deliverynotepc IS '配送方法説明文PC';
COMMENT ON COLUMN public.deliverymethod.deliverynotemb IS '配送方法説明文携帯';
COMMENT ON COLUMN public.deliverymethod.deliverymethodtype IS '配送方法種別';
COMMENT ON COLUMN public.deliverymethod.equalscarriage IS '一律送料';
COMMENT ON COLUMN public.deliverymethod.largeamountdiscountprice IS '高額割引下限金額';
COMMENT ON COLUMN public.deliverymethod.largeamountdiscountcarriage IS '高額割引送料';
COMMENT ON COLUMN public.deliverymethod.shortfalldisplayflag IS '不足金額表示フラグ';
COMMENT ON COLUMN public.deliverymethod.deliveryleadtime IS 'リードタイム';
COMMENT ON COLUMN public.deliverymethod.possibleselectdays IS '選択可能日数';
COMMENT ON COLUMN public.deliverymethod.receivertimezone1 IS 'お届け時間帯1';
COMMENT ON COLUMN public.deliverymethod.receivertimezone2 IS 'お届け時間帯2';
COMMENT ON COLUMN public.deliverymethod.receivertimezone3 IS 'お届け時間帯3';
COMMENT ON COLUMN public.deliverymethod.receivertimezone4 IS 'お届け時間帯4';
COMMENT ON COLUMN public.deliverymethod.receivertimezone5 IS 'お届け時間帯5';
COMMENT ON COLUMN public.deliverymethod.receivertimezone6 IS 'お届け時間帯6';
COMMENT ON COLUMN public.deliverymethod.receivertimezone7 IS 'お届け時間帯7';
COMMENT ON COLUMN public.deliverymethod.receivertimezone8 IS 'お届け時間帯8';
COMMENT ON COLUMN public.deliverymethod.receivertimezone9 IS 'お届け時間帯9';
COMMENT ON COLUMN public.deliverymethod.receivertimezone10 IS 'お届け時間帯10';
COMMENT ON COLUMN public.deliverymethod.orderdisplay IS '表示順';
COMMENT ON COLUMN public.deliverymethod.registtime IS '登録日時';
COMMENT ON COLUMN public.deliverymethod.updatetime IS '更新日時';

-- CREATE DELIVERYMETHODTYPECARRIAGE
CREATE TABLE public.deliverymethodtypecarriage (
                                                   deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                                   prefecturetype varchar(2) NOT NULL DEFAULT '01'::character varying, -- 都道府県種別
                                                   maxprice numeric(8) NOT NULL DEFAULT 0, -- 上限金額
                                                   carriage numeric(8) NULL DEFAULT 0, -- 送料
                                                   registtime timestamp(0) NOT NULL, -- 登録日時
                                                   updatetime timestamp(0) NOT NULL, -- 更新日時
                                                   CONSTRAINT deliverymethodtypecarriage_pkey PRIMARY KEY (deliverymethodseq, prefecturetype, maxprice)
);
COMMENT ON TABLE public.deliverymethodtypecarriage IS '配送区分別送料';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.deliverymethodtypecarriage.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.deliverymethodtypecarriage.prefecturetype IS '都道府県種別';
COMMENT ON COLUMN public.deliverymethodtypecarriage.maxprice IS '上限金額';
COMMENT ON COLUMN public.deliverymethodtypecarriage.carriage IS '送料';
COMMENT ON COLUMN public.deliverymethodtypecarriage.registtime IS '登録日時';
COMMENT ON COLUMN public.deliverymethodtypecarriage.updatetime IS '更新日時';

-- CREATE DELIVERYSPECIALCHARGEAREA
CREATE TABLE public.deliveryspecialchargearea (
                                                  deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                                  zipcode varchar(7) NOT NULL, -- 郵便番号
                                                  carriage numeric(8) NULL DEFAULT 0, -- 送料
                                                  registtime timestamp(0) NOT NULL, -- 登録日時
                                                  updatetime timestamp(0) NOT NULL, -- 更新日時
                                                  CONSTRAINT deliveryspecialchargearea_pkey PRIMARY KEY (deliverymethodseq, zipcode)
);
COMMENT ON TABLE public.deliveryspecialchargearea IS '配送特別料金エリア';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.deliveryspecialchargearea.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.deliveryspecialchargearea.zipcode IS '郵便番号';
COMMENT ON COLUMN public.deliveryspecialchargearea.carriage IS '送料';
COMMENT ON COLUMN public.deliveryspecialchargearea.registtime IS '登録日時';
COMMENT ON COLUMN public.deliveryspecialchargearea.updatetime IS '更新日時';

-- CREATE DELIVERYIMPOSSIBLEAREA
CREATE TABLE public.deliveryimpossiblearea (
                                               deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                               zipcode varchar(7) NOT NULL, -- 郵便番号
                                               registtime timestamp(0) NOT NULL, -- 登録日時
                                               updatetime timestamp(0) NOT NULL, -- 更新日時
                                               CONSTRAINT deliveryimpossiblearea_pkey PRIMARY KEY (deliverymethodseq, zipcode)
);
COMMENT ON TABLE public.deliveryimpossiblearea IS '配送不可能エリア';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.deliveryimpossiblearea.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.deliveryimpossiblearea.zipcode IS '郵便番号';
COMMENT ON COLUMN public.deliveryimpossiblearea.registtime IS '登録日時';
COMMENT ON COLUMN public.deliveryimpossiblearea.updatetime IS '更新日時';

-- CREATE HOLIDAY
CREATE TABLE public.holiday (
                                deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                "date" date NOT NULL, -- 年月日
                                "year" numeric(4) NOT NULL, -- 年
                                "name" varchar(20) NOT NULL, -- 名前
                                registtime timestamp NOT NULL, -- 登録日時
                                CONSTRAINT holiday_pkey PRIMARY KEY (deliverymethodseq, date)
);
COMMENT ON TABLE public.holiday IS '休日';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.holiday.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.holiday."date" IS '年月日';
COMMENT ON COLUMN public.holiday."year" IS '年';
COMMENT ON COLUMN public.holiday."name" IS '名前';
COMMENT ON COLUMN public.holiday.registtime IS '登録日時';

-- CREATE DELIVERYIMPOSSIBLEDAY
CREATE TABLE public.deliveryimpossibleday (
                                              deliverymethodseq numeric(4) NOT NULL, -- 配送方法SEQ
                                              "date" date NOT NULL, -- 年月日
                                              "year" numeric(4) NOT NULL, -- 年
                                              reason varchar(20) NOT NULL, -- 理由
                                              registtime timestamp(0) NOT NULL, -- 登録日時
                                              CONSTRAINT deliveryimpossibleday_pkey PRIMARY KEY (deliverymethodseq, date)
);
COMMENT ON TABLE public.deliveryimpossibleday IS 'お届け不可日';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.deliveryimpossibleday.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.deliveryimpossibleday."date" IS '年月日';
COMMENT ON COLUMN public.deliveryimpossibleday."year" IS '年';
COMMENT ON COLUMN public.deliveryimpossibleday.reason IS '理由';
COMMENT ON COLUMN public.deliveryimpossibleday.registtime IS '登録日時';

-- CREATE ZIPCODE
CREATE TABLE public.zipcode (
                                localcode varchar(6) NOT NULL, -- 全国地方公共団体コード
                                oldzipcode varchar(5) NOT NULL, -- (旧)郵便番号
                                zipcode varchar(7) NOT NULL, -- 郵便番号
                                prefecturekana varchar(10) NOT NULL, -- 都道府県名(カナ)
                                citykana varchar(30) NOT NULL, -- 市区町村名(カナ)
                                townkana varchar(100) NOT NULL, -- 町域名(カナ)
                                prefecturename varchar(10) NOT NULL, -- 都道府県名
                                cityname varchar(20) NOT NULL, -- 市区町村名
                                townname varchar(100) NOT NULL, -- 町域名
                                anyzipflag varchar(1) NULL, -- 複数郵便番号フラグ
                                numberflag1 varchar(1) NULL, -- 小字フラグ
                                numberflag2 varchar(1) NULL, -- 丁目フラグ
                                numberflag3 varchar(1) NULL, -- 複数町域フラグ
                                updatevisibletype varchar(1) NULL, -- 変更表示種別
                                updatenotetype varchar(1) NOT NULL, -- 変更理由種別
                                registtime timestamp(0) NOT NULL, -- 登録日時
                                updatetime timestamp(0) NOT NULL, -- 更新日時
                                CONSTRAINT zipcode_pkey PRIMARY KEY (localcode, oldzipcode, zipcode, prefecturename, cityname, townname)
);
CREATE INDEX idx_zipcode_zipcode ON public.zipcode USING btree (zipcode);
COMMENT ON TABLE public.zipcode IS '郵便番号住所';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.zipcode.localcode IS '全国地方公共団体コード';
COMMENT ON COLUMN public.zipcode.oldzipcode IS '(旧)郵便番号';
COMMENT ON COLUMN public.zipcode.zipcode IS '郵便番号';
COMMENT ON COLUMN public.zipcode.prefecturekana IS '都道府県名(カナ)';
COMMENT ON COLUMN public.zipcode.citykana IS '市区町村名(カナ)';
COMMENT ON COLUMN public.zipcode.townkana IS '町域名(カナ)';
COMMENT ON COLUMN public.zipcode.prefecturename IS '都道府県名';
COMMENT ON COLUMN public.zipcode.cityname IS '市区町村名';
COMMENT ON COLUMN public.zipcode.townname IS '町域名';
COMMENT ON COLUMN public.zipcode.anyzipflag IS '複数郵便番号フラグ';
COMMENT ON COLUMN public.zipcode.numberflag1 IS '小字フラグ';
COMMENT ON COLUMN public.zipcode.numberflag2 IS '丁目フラグ';
COMMENT ON COLUMN public.zipcode.numberflag3 IS '複数町域フラグ';
COMMENT ON COLUMN public.zipcode.updatevisibletype IS '変更表示種別';
COMMENT ON COLUMN public.zipcode.updatenotetype IS '変更理由種別';
COMMENT ON COLUMN public.zipcode.registtime IS '登録日時';
COMMENT ON COLUMN public.zipcode.updatetime IS '更新日時';

-- CREATE OFFICEZIPCODE
CREATE TABLE public.officezipcode (
                                      officecode varchar(5) NOT NULL, -- 大口事業所の所在地
                                      officekana varchar(100) NOT NULL, -- 大口事業所名（カナ）
                                      officename varchar(80) NOT NULL, -- 大口事業所名（漢字）
                                      prefecturename varchar(4) NOT NULL, -- 都道府県名（漢字）
                                      cityname varchar(12) NOT NULL, -- 市区町村名（漢字）
                                      townname varchar(12) NOT NULL, -- 町域名（漢字）
                                      numbers varchar(62) NOT NULL, -- 小字名、丁目、番地等（漢字）
                                      zipcode varchar(7) NOT NULL, -- 大口事業所個別番号
                                      oldzipcode varchar(5) NOT NULL, -- 旧郵便番号
                                      handlingbranchname varchar(20) NULL, -- 取扱支店名（漢字）
                                      individualtype varchar(1) NULL, -- 個別番号の種別
                                      anyzipflag varchar(1) NULL, -- 複数番号の有無
                                      updatecode varchar(1) NOT NULL, -- 修正コード
                                      registtime timestamp(0) NOT NULL, -- 登録日時
                                      updatetime timestamp(0) NOT NULL, -- 更新日時
                                      CONSTRAINT officezipcode_pkey PRIMARY KEY (officecode, officekana, officename, prefecturename, cityname, townname, numbers, zipcode, oldzipcode)
);
CREATE INDEX idx_officezipcode_zipcode ON public.officezipcode USING btree (zipcode);
COMMENT ON TABLE public.officezipcode IS '事業所郵便番号';

-- COLUMN COMMENTS
COMMENT ON COLUMN public.officezipcode.officecode IS '大口事業所の所在地';
COMMENT ON COLUMN public.officezipcode.officekana IS '大口事業所名（カナ）';
COMMENT ON COLUMN public.officezipcode.officename IS '大口事業所名（漢字）';
COMMENT ON COLUMN public.officezipcode.prefecturename IS '都道府県名（漢字）';
COMMENT ON COLUMN public.officezipcode.cityname IS '市区町村名（漢字）';
COMMENT ON COLUMN public.officezipcode.townname IS '町域名（漢字）';
COMMENT ON COLUMN public.officezipcode.numbers IS '小字名、丁目、番地等（漢字）';
COMMENT ON COLUMN public.officezipcode.zipcode IS '大口事業所個別番号';
COMMENT ON COLUMN public.officezipcode.oldzipcode IS '旧郵便番号';
COMMENT ON COLUMN public.officezipcode.handlingbranchname IS '取扱支店名（漢字）';
COMMENT ON COLUMN public.officezipcode.individualtype IS '個別番号の種別';
COMMENT ON COLUMN public.officezipcode.anyzipflag IS '複数番号の有無';
COMMENT ON COLUMN public.officezipcode.updatecode IS '修正コード';
COMMENT ON COLUMN public.officezipcode.registtime IS '登録日時';
COMMENT ON COLUMN public.officezipcode.updatetime IS '更新日時';

/******************************
 * ノベルティプレゼント条件
 *   テーブル作成
 ******************************/
CREATE TABLE noveltypresentcondition
(
    noveltypresentconditionseq numeric(8,0) NOT NULL, -- ノベルティプレゼント条件SEQ
    noveltypresentname character varying(120) NOT NULL, -- ノベルティプレゼント条件名
    enclosureunittype character varying(1) NOT NULL DEFAULT '0'::character varying, -- 同梱単位区分
    noveltypresentstate character varying(1) NOT NULL DEFAULT '0'::character varying, -- ノベルティプレゼント条件状態
    noveltypresentstarttime timestamp without time zone NOT NULL, -- ノベルティプレゼント条件開始日時
    noveltypresentendtime timestamp without time zone, -- ノベルティプレゼント条件終了日時
    exclusionnoveltypresentseq text, -- 除外条件SEQ
    magazinesendflag character varying(1) NOT NULL, -- メールマガジン配信条件フラグ
    admissionstarttime timestamp without time zone, -- 入会開始日時
    admissionendtime timestamp without time zone, -- 入会終了日時
    goodsgroupcode character varying(10000), -- 商品管理番号
    goodscode character varying(60000), -- 商品番号
    categoryid character varying(1000), -- カテゴリーID
    iconid character varying(160), -- アイコンID
    goodsname character varying(2400), -- 商品名
    searchkeyword character varying(1000), -- 検索キーワード
    goodspricetotal numeric(8,0), -- 商品金額合計
    goodspricetotalflag character varying(1) NOT NULL, -- 商品金額条件フラグ
    prizegoodslimit numeric(6,0), -- プレゼント数制限
    memo character varying(2000), -- 管理メモ
    registtime timestamp without time zone NOT NULL, -- 登録日時
    updatetime timestamp without time zone NOT NULL, -- 更新日時
    CONSTRAINT noveltypresentcondition_pkey PRIMARY KEY (noveltypresentconditionseq)
);
COMMENT ON TABLE noveltypresentcondition
  IS 'ノベルティプレゼント条件';
COMMENT ON COLUMN noveltypresentcondition.noveltypresentconditionseq IS 'ノベルティプレゼント条件SEQ';
COMMENT ON COLUMN noveltypresentcondition.noveltypresentname IS 'ノベルティプレゼント条件名';
COMMENT ON COLUMN noveltypresentcondition.enclosureunittype IS '同梱単位区分';
COMMENT ON COLUMN noveltypresentcondition.noveltypresentstate IS 'ノベルティプレゼント条件状態';
COMMENT ON COLUMN noveltypresentcondition.noveltypresentstarttime IS 'ノベルティプレゼント条件開始日時';
COMMENT ON COLUMN noveltypresentcondition.noveltypresentendtime IS 'ノベルティプレゼント条件終了日時';
COMMENT ON COLUMN noveltypresentcondition.exclusionnoveltypresentseq IS '除外条件SEQ';
COMMENT ON COLUMN noveltypresentcondition.magazinesendflag IS 'メールマガジン配信条件フラグ';
COMMENT ON COLUMN noveltypresentcondition.admissionstarttime IS '入会開始日時';
COMMENT ON COLUMN noveltypresentcondition.admissionendtime IS '入会終了日時';
COMMENT ON COLUMN noveltypresentcondition.goodsgroupcode IS '商品管理番号';
COMMENT ON COLUMN noveltypresentcondition.goodscode IS '商品番号';
COMMENT ON COLUMN noveltypresentcondition.categoryid IS 'カテゴリーID';
COMMENT ON COLUMN noveltypresentcondition.iconid IS 'アイコンID';
COMMENT ON COLUMN noveltypresentcondition.goodsname IS '商品名';
COMMENT ON COLUMN noveltypresentcondition.searchkeyword IS '検索キーワード';
COMMENT ON COLUMN noveltypresentcondition.goodspricetotal IS '商品金額合計';
COMMENT ON COLUMN noveltypresentcondition.goodspricetotalflag IS '商品金額条件フラグ';
COMMENT ON COLUMN noveltypresentcondition.prizegoodslimit IS 'プレゼント数制限';
COMMENT ON COLUMN noveltypresentcondition.memo IS '管理メモ';
COMMENT ON COLUMN noveltypresentcondition.registtime IS '登録日時';
COMMENT ON COLUMN noveltypresentcondition.updatetime IS '更新日時';

-- シーケンス作成（ノベルティプレゼント条件SEQ）
DROP SEQUENCE IF EXISTS noveltypresentconditionseq CASCADE;

CREATE SEQUENCE noveltypresentconditionseq
    INCREMENT 1
  MINVALUE 1
  MAXVALUE 99999999
  START 10000000;
COMMENT ON SEQUENCE noveltypresentconditionseq
  IS 'ノベルティプレゼント条件SEQ';

/******************************
 * ノベルティプレゼント同梱商品
 *   テーブル作成
 ******************************/
CREATE TABLE noveltypresentenclosuregoods
(
    noveltypresentconditionseq numeric(8,0) NOT NULL, -- ノベルティプレゼント条件SEQ
    goodsseq numeric(8,0) NOT NULL, -- 商品SEQ
    priorityorder numeric(4,0), -- 優先順
    registtime timestamp without time zone, -- 登録日時
    updatetime timestamp without time zone, -- 更新日時
    CONSTRAINT noveltypresentenclosuregoods_pkey PRIMARY KEY (noveltypresentconditionseq, goodsseq)
);
COMMENT ON TABLE noveltypresentenclosuregoods
  IS 'ノベルティプレゼント同梱商品';
COMMENT ON COLUMN noveltypresentenclosuregoods.noveltypresentconditionseq IS 'ノベルティプレゼント条件SEQ';
COMMENT ON COLUMN noveltypresentenclosuregoods.goodsseq IS '商品SEQ';
COMMENT ON COLUMN noveltypresentenclosuregoods.priorityorder IS '優先順';
COMMENT ON COLUMN noveltypresentenclosuregoods.registtime IS '登録日時';
COMMENT ON COLUMN noveltypresentenclosuregoods.updatetime IS '更新日時';

/******************************
 * ノベルティプレゼント対象会員
 *   テーブル作成
 ******************************/
CREATE TABLE noveltypresentmember
(
    noveltypresentconditionseq numeric(8,0) NOT NULL, -- ノベルティプレゼント条件SEQ
    memberinfoseq numeric(10,0) NOT NULL, -- 会員SEQ
    orderreceivedid text, -- 受注ID
    goodsseq numeric(8,0) NOT NULL, -- 商品SEQ
    registtime timestamp without time zone, -- 登録日時
    updatetime timestamp without time zone, -- 更新日時
    CONSTRAINT noveltypresentmember_pkey PRIMARY KEY (noveltypresentconditionseq, memberinfoseq, orderreceivedid, goodsseq)
);
COMMENT ON TABLE noveltypresentmember
  IS 'ノベルティプレゼント対象会員';
COMMENT ON COLUMN noveltypresentmember.noveltypresentconditionseq IS 'ノベルティプレゼント条件SEQ';
COMMENT ON COLUMN noveltypresentmember.memberinfoseq IS '会員SEQ';
COMMENT ON COLUMN noveltypresentmember.orderreceivedid IS '受注ID';
COMMENT ON COLUMN noveltypresentmember.goodsseq IS '商品SEQ';
COMMENT ON COLUMN noveltypresentmember.registtime IS '登録日時';
COMMENT ON COLUMN noveltypresentmember.updatetime IS '更新日時';


-- DROP SEQUENCE
DROP SEQUENCE IF EXISTS stockresultseq CASCADE;
DROP SEQUENCE IF EXISTS deliverymethodseq CASCADE;

-- CREATE SEQUENCES
-- CREATE STOCKRESULTSEQ
CREATE SEQUENCE public.stockresultseq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999
    START 10000000
	CACHE 1
	NO CYCLE;

-- CREATE DELIVERYMETHODSEQ
CREATE SEQUENCE public.deliverymethodseq
    INCREMENT BY 1
    MINVALUE 1000
    MAXVALUE 9999
    START 1000
	CACHE 1
	NO CYCLE;
