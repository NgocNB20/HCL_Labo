-- Drop table
DROP TABLE IF EXISTS public.itempurchasepricesubtotal CASCADE;
DROP TABLE IF EXISTS public.adjustmentamount CASCADE;
DROP TABLE IF EXISTS public.salesslip CASCADE;
DROP TABLE IF EXISTS public.itempurchasepricesubtotalforrevision CASCADE;
DROP TABLE IF EXISTS public.adjustmentamountforrevision CASCADE;
DROP TABLE IF EXISTS public.salesslipforrevision CASCADE;
DROP TABLE IF EXISTS public.coupon CASCADE;
DROP TABLE IF EXISTS public.couponindex CASCADE;

-- CREATE TABLE
-- public.salesslip definition
CREATE TABLE public.salesslip (
	salesslipid text NOT NULL, -- 販売伝票ID
	salesstatus text NULL, -- 販売ステータス
	couponcode text NULL, -- クーポンコード
	couponseq float8 NULL, -- クーポンSEQ
	couponversionno float8 NULL, -- クーポン連番
	couponname text NULL, -- クーポン名
	couponpaymentprice float8 NULL, -- クーポン支払い額
	couponuseflag bool NULL, -- クーポン利用フラグ
	billingamount float8 NULL, -- 請求金額
	itempurchasepricetotal float8 NULL, -- 商品購入価格合計
	carriage float8 NULL, -- 送料
	commission float8 NULL, -- 手数料
	standardtaxtargetprice float8 NULL, -- 標準税率対象金額
	reducedtaxtargetprice float8 NULL, -- 軽減税率対象金額
	standardtax float8 NULL, -- 標準税額
	reducedtax float8 NULL, -- 軽減税額
	transactionid text NULL, -- 取引ID
	customerid text NULL, -- 顧客ID
    ordercode text NULL, -- 受注番号
	registdate timestamp NULL, -- 登録日時
	salesopendate timestamp NULL, -- 販売確定日時
	CONSTRAINT salesslip_pk PRIMARY KEY (salesslipid)
);

COMMENT ON TABLE public.salesslip IS '販売伝票';

-- Column comments

COMMENT ON COLUMN public.salesslip.salesslipid IS '販売伝票ID';
COMMENT ON COLUMN public.salesslip.salesstatus IS '販売ステータス';
COMMENT ON COLUMN public.salesslip.couponcode IS 'クーポンコード';
COMMENT ON COLUMN public.salesslip.couponseq IS 'クーポンSEQ';
COMMENT ON COLUMN public.salesslip.couponversionno IS 'クーポン連番';
COMMENT ON COLUMN public.salesslip.couponname IS 'クーポン名';
COMMENT ON COLUMN public.salesslip.couponpaymentprice IS 'クーポン支払い額';
COMMENT ON COLUMN public.salesslip.couponuseflag IS 'クーポン利用フラグ';
COMMENT ON COLUMN public.salesslip.billingamount IS '請求金額';
COMMENT ON COLUMN public.salesslip.itempurchasepricetotal IS '商品購入価格合計';
COMMENT ON COLUMN public.salesslip.carriage IS '送料';
COMMENT ON COLUMN public.salesslip.commission IS '手数料';
COMMENT ON COLUMN public.salesslip.standardtaxtargetprice IS '標準税率対象金額';
COMMENT ON COLUMN public.salesslip.reducedtaxtargetprice IS '軽減税率対象金額';
COMMENT ON COLUMN public.salesslip.standardtax IS '標準税額';
COMMENT ON COLUMN public.salesslip.reducedtax IS '軽減税額';
COMMENT ON COLUMN public.salesslip.transactionid IS '取引ID';
COMMENT ON COLUMN public.salesslip.customerid IS '顧客ID';
COMMENT ON COLUMN public.salesslip.ordercode IS '受注番号';
COMMENT ON COLUMN public.salesslip.registdate IS '登録日時';
COMMENT ON COLUMN public.salesslip.salesopendate IS '販売確定日時';


-- public.itempurchasepricesubtotal definition
CREATE TABLE public.itempurchasepricesubtotal (
	orderitemseq float8 NULL, -- 注文商品連番
	itempurchasepricesubtotal float8 NULL, -- 商品購入価格小計
	itemid text NULL, -- 商品ID
	itemunitprice float8 NULL, -- 商品単価
	itemcount float8 NULL, -- 商品数量
	itemtaxrate NUMERIC(2,0) NULL, -- 商品税率
	salesslipid text NOT NULL, -- 販売伝票ID
	freecarriageitemflag bool NULL, -- 送料無料商品フラグ
	id bigserial NOT NULL,
	CONSTRAINT itempurchasepricesubtotal_orderitemseq_salesslipid_key UNIQUE (orderitemseq, salesslipid),
	CONSTRAINT itempurchasepricesubtotal_pk PRIMARY KEY (id, salesslipid)
);

COMMENT ON TABLE public.itempurchasepricesubtotal IS '商品購入価格小計';

-- Column comments

COMMENT ON COLUMN public.itempurchasepricesubtotal.orderitemseq IS '注文商品連番';
COMMENT ON COLUMN public.itempurchasepricesubtotal.itempurchasepricesubtotal IS '商品購入価格小計';
COMMENT ON COLUMN public.itempurchasepricesubtotal.itemid IS '商品ID';
COMMENT ON COLUMN public.itempurchasepricesubtotal.itemunitprice IS '商品単価';
COMMENT ON COLUMN public.itempurchasepricesubtotal.itemcount IS '商品数量';
COMMENT ON COLUMN public.itempurchasepricesubtotal.itemtaxrate IS '商品税率';
COMMENT ON COLUMN public.itempurchasepricesubtotal.freecarriageitemflag IS '送料無料商品フラグ';
COMMENT ON COLUMN public.itempurchasepricesubtotal.salesslipid IS '販売伝票ID';


-- public.itempurchasepricesubtotal foreign keys

ALTER TABLE public.itempurchasepricesubtotal ADD CONSTRAINT itempurchasepricesubtotal_fk FOREIGN KEY (salesslipid) REFERENCES public.salesslip(salesslipid);


-- public.adjustmentamount definition
CREATE TABLE public.adjustmentamount (
	id bigserial NOT NULL,
	adjustmentamountseq float8 NULL, -- 調整金額連番
	adjustname text NULL, -- 調整項目名
	adjustprice float8 NULL, -- 調整金額
	salesslipid text NOT NULL, -- 販売伝票ID
	CONSTRAINT adjustmentamount_adjustmentamountseq_salesslipid_key UNIQUE (adjustmentamountseq, salesslipid),
	CONSTRAINT adjustmentamount_pk PRIMARY KEY (id, salesslipid)
);

COMMENT ON TABLE public.adjustmentamount IS '調整金額';

-- Column comments

COMMENT ON COLUMN public.adjustmentamount.adjustmentamountseq IS '調整金額連番';
COMMENT ON COLUMN public.adjustmentamount.adjustname IS '調整項目名';
COMMENT ON COLUMN public.adjustmentamount.adjustprice IS '調整金額';
COMMENT ON COLUMN public.adjustmentamount.salesslipid IS '販売伝票ID';


-- public.adjustmentamount foreign keys

ALTER TABLE public.adjustmentamount ADD CONSTRAINT adjustmentamount_fk FOREIGN KEY (salesslipid) REFERENCES public.salesslip(salesslipid);

-- public.salesslipforrevision definition
CREATE TABLE public.salesslipforrevision (
	salessliprevisionid text NOT NULL, -- 改訂用販売伝票ID
	salesslipid text NULL, -- 販売伝票ID
	salesstatus text NULL, -- 販売ステータス
	couponcode text NULL, -- クーポンコード
	couponseq float8 NULL, -- クーポンSEQ
	couponversionno float8 NULL, -- クーポン連番
	couponname text NULL, -- クーポン名
	couponpaymentprice float8 NULL, -- クーポン支払い額
	couponuseflag bool NULL, -- クーポン利用フラグ
	billingamount float8 NULL, -- 請求金額
	itempurchasepricetotal float8 NULL, -- 商品購入価格合計
	carriage float8 NULL, -- 送料
	commission float8 NULL, -- 手数料
	standardtaxtargetprice float8 NULL, -- 標準税率対象金額
	reducedtaxtargetprice float8 NULL, -- 軽減税率対象金額
	standardtax float8 NULL, -- 標準税額
	reducedtax float8 NULL, -- 軽減税額
	transactionid text NULL, -- 取引ID
	transactionrevisionid text NULL, -- 改訂用取引ID
	customerid text NULL, -- 顧客ID
	ordercode text NULL, -- 受注番号
	registdate timestamp NULL, -- 登録日時
	salesopendate timestamp NULL, -- 販売確定日時
	origincommissionapplyflag bool NULL, -- 改訂前手数料適用フラグ
	origincarriageapplyflag bool NULL, -- 改訂前送料適用フラグ
	CONSTRAINT salesslipforrevision_pk PRIMARY KEY (salessliprevisionid)
);

COMMENT ON TABLE public.salesslipforrevision IS '改訂用販売伝票';

-- Column comments

COMMENT ON COLUMN public.salesslipforrevision.salessliprevisionid IS '改訂用販売伝票ID';
COMMENT ON COLUMN public.salesslipforrevision.salesslipid IS '販売伝票ID';
COMMENT ON COLUMN public.salesslipforrevision.salesstatus IS '販売ステータス';
COMMENT ON COLUMN public.salesslipforrevision.couponcode IS 'クーポンコード';
COMMENT ON COLUMN public.salesslipforrevision.couponseq IS 'クーポンSEQ';
COMMENT ON COLUMN public.salesslipforrevision.couponversionno IS 'クーポン連番';
COMMENT ON COLUMN public.salesslipforrevision.couponname IS 'クーポン名';
COMMENT ON COLUMN public.salesslipforrevision.couponpaymentprice IS 'クーポン支払い額';
COMMENT ON COLUMN public.salesslipforrevision.couponuseflag IS 'クーポン利用フラグ';
COMMENT ON COLUMN public.salesslipforrevision.billingamount IS '請求金額';
COMMENT ON COLUMN public.salesslipforrevision.itempurchasepricetotal IS '商品購入価格合計';
COMMENT ON COLUMN public.salesslipforrevision.carriage IS '送料';
COMMENT ON COLUMN public.salesslipforrevision.commission IS '手数料';
COMMENT ON COLUMN public.salesslipforrevision.standardtaxtargetprice IS '標準税率対象金額';
COMMENT ON COLUMN public.salesslipforrevision.reducedtaxtargetprice IS '軽減税率対象金額';
COMMENT ON COLUMN public.salesslipforrevision.standardtax IS '標準税額';
COMMENT ON COLUMN public.salesslipforrevision.reducedtax IS '軽減税額';
COMMENT ON COLUMN public.salesslipforrevision.transactionid IS '取引ID';
COMMENT ON COLUMN public.salesslipforrevision.transactionrevisionid IS '改訂用取引ID';
COMMENT ON COLUMN public.salesslipforrevision.customerid IS '顧客ID';
COMMENT ON COLUMN public.salesslipforrevision.ordercode IS '受注番号';
COMMENT ON COLUMN public.salesslipforrevision.registdate IS '登録日時';
COMMENT ON COLUMN public.salesslipforrevision.salesopendate IS '販売確定日時';
COMMENT ON COLUMN public.salesslipforrevision.origincommissionapplyflag IS '改訂前手数料適用フラグ';
COMMENT ON COLUMN public.salesslipforrevision.origincarriageapplyflag IS '改訂前送料適用フラグ';

-- public.itempurchasepricesubtotalforrevision definition
CREATE TABLE public.itempurchasepricesubtotalforrevision (
	orderitemseq float8 NULL, -- 注文商品連番
	itempurchasepricesubtotal float8 NULL, -- 商品購入価格小計
	itemid text NULL, -- 商品ID
	itemunitprice float8 NULL, -- 商品単価
	itemcount float8 NULL, -- 商品数量
	itemtaxrate NUMERIC(2,0) NULL, -- 商品税率
	freecarriageitemflag bool NULL, -- 送料無料商品フラグ
	salessliprevisionid text NOT NULL, -- 販売伝票ID
	id bigserial NOT NULL,
	CONSTRAINT itempurchasepricesubtotalforr_orderitemseq_salessliprevisio_key UNIQUE (orderitemseq, salessliprevisionid),
	CONSTRAINT itempurchasepricesubtotalforrevision_pk PRIMARY KEY (id, salessliprevisionid)
);

COMMENT ON TABLE public.itempurchasepricesubtotalforrevision IS '改訂用商品購入価格小計';

-- Column comments

COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.orderitemseq IS '注文商品連番';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.itempurchasepricesubtotal IS '商品購入価格小計';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.itemid IS '商品ID';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.itemunitprice IS '商品単価';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.itemcount IS '商品数量';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.itemtaxrate IS '商品税率';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.freecarriageitemflag IS '送料無料商品フラグ';
COMMENT ON COLUMN public.itempurchasepricesubtotalforrevision.salessliprevisionid IS '販売伝票ID';


-- public.itempurchasepricesubtotalforrevision foreign keys

ALTER TABLE public.itempurchasepricesubtotalforrevision ADD CONSTRAINT itempurchasepricesubtotalforrevision_fk FOREIGN KEY (salessliprevisionid) REFERENCES public.salesslipforrevision(salessliprevisionid);

-- public.adjustmentamountforrevision definition
CREATE TABLE public.adjustmentamountforrevision (
	id bigserial NOT NULL,
	adjustmentamountseq float8 NULL, -- 調整金額連番
	adjustname text NULL, -- 調整項目名
	adjustprice float8 NULL, -- 調整金額
	salessliprevisionid text NOT NULL, -- 販売伝票ID
	CONSTRAINT adjustmentamountrev_adjustseq_salessliprevid_key UNIQUE (adjustmentamountseq, salessliprevisionid),
	CONSTRAINT adjustmentamountforrevision_pk PRIMARY KEY (id, salessliprevisionid)
);
COMMENT ON TABLE public.adjustmentamountforrevision IS '改訂用調整金額';

-- Column comments

COMMENT ON COLUMN public.adjustmentamountforrevision.adjustmentamountseq IS '調整金額連番';
COMMENT ON COLUMN public.adjustmentamountforrevision.adjustname IS '調整項目名';
COMMENT ON COLUMN public.adjustmentamountforrevision.adjustprice IS '調整金額';
COMMENT ON COLUMN public.adjustmentamountforrevision.salessliprevisionid IS '販売伝票ID';

-- public.adjustmentamountforrevision foreign keys

ALTER TABLE public.adjustmentamountforrevision ADD CONSTRAINT adjustmentamountforrevision_fk FOREIGN KEY (salessliprevisionid) REFERENCES public.salesslipforrevision(salessliprevisionid);

-- public.coupon definition
CREATE TABLE public.coupon (
	couponseq numeric(8) NOT NULL, -- クーポンSEQ
	couponversionno numeric(2) NOT NULL, -- クーポン枝番
	shopseq numeric(4) NOT NULL, -- ショップSEQ
	couponid varchar(12) NOT NULL, -- クーポンID
	couponname varchar(80) NOT NULL, -- クーポン名
	coupondisplaynamepc varchar(80) NOT NULL, -- クーポン表示名PC
	couponcode varchar(20) NOT NULL, -- クーポンコード
	couponstarttime timestamp(0) NOT NULL, -- クーポン開始日時
	couponendtime timestamp(0) NOT NULL, -- クーポン終了日時
	discounttype varchar(1) NOT NULL, -- 割引種別
	discountrate numeric(2,0), -- 割引率
	discountprice numeric(8), -- 割引金額
	discountlowerorderprice numeric(8) NULL, -- クーポン適用金額
	usecountlimit numeric(4) NOT NULL, -- 利用回数
    targetgoodstype varchar(1) NOT NULL , --対象商品制限種別,
	targetgoods text NULL, -- 対象商品
    targetmemberstype varchar(1) NOT NULL , --対象会員制限種別,
	targetmembers text NULL, -- 対象会員
	memo varchar(2000) NULL, -- 管理用メモ
	administratorid varchar(20) NOT NULL, -- 管理者ID
	registtime timestamp(0) NOT NULL, -- 登録日時
	updatetime timestamp(0) NOT NULL, -- 更新日時
	CONSTRAINT coupon_pkey PRIMARY KEY (couponseq, couponversionno)
);
COMMENT ON TABLE public.coupon IS 'クーポン';

-- Column comments

COMMENT ON COLUMN public.coupon.couponseq IS 'クーポンSEQ';
COMMENT ON COLUMN public.coupon.couponversionno IS 'クーポン枝番';
COMMENT ON COLUMN public.coupon.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.coupon.couponid IS 'クーポンID';
COMMENT ON COLUMN public.coupon.couponname IS 'クーポン名';
COMMENT ON COLUMN public.coupon.coupondisplaynamepc IS 'クーポン表示名PC';
COMMENT ON COLUMN public.coupon.couponcode IS 'クーポンコード';
COMMENT ON COLUMN public.coupon.couponstarttime IS 'クーポン開始日時';
COMMENT ON COLUMN public.coupon.couponendtime IS 'クーポン終了日時';
COMMENT ON COLUMN public.coupon.discounttype IS '割引種別';
COMMENT ON COLUMN public.coupon.discountrate IS '割引率';
COMMENT ON COLUMN public.coupon.discountprice IS '割引金額';
COMMENT ON COLUMN public.coupon.discountlowerorderprice IS 'クーポン適用金額';
COMMENT ON COLUMN public.coupon.usecountlimit IS '利用回数';
COMMENT ON COLUMN public.coupon.targetgoodstype IS '対象商品制限種別';
COMMENT ON COLUMN public.coupon.targetgoods IS '対象商品';
COMMENT ON COLUMN public.coupon.targetmemberstype IS '対象会員制限種別';
COMMENT ON COLUMN public.coupon.targetmembers IS '対象会員';
COMMENT ON COLUMN public.coupon.memo IS '管理用メモ';
COMMENT ON COLUMN public.coupon.administratorid IS '管理者ID';
COMMENT ON COLUMN public.coupon.registtime IS '登録日時';
COMMENT ON COLUMN public.coupon.updatetime IS '更新日時';


-- public.couponindex definition
CREATE TABLE public.couponindex (
	couponseq numeric(8) NOT NULL, -- クーポンSEQ
	couponversionno numeric(2) NOT NULL, -- クーポン枝番
	shopseq numeric(4) NOT NULL, -- ショップSEQ
	registtime timestamp(0) NOT NULL, -- 登録日時
	updatetime timestamp(0) NOT NULL, -- 更新日時
	CONSTRAINT couponindex_pkey PRIMARY KEY (couponseq, couponversionno)
);
COMMENT ON TABLE public.couponindex IS 'クーポンインデックス';

-- Column comments

COMMENT ON COLUMN public.couponindex.couponseq IS 'クーポンSEQ';
COMMENT ON COLUMN public.couponindex.couponversionno IS 'クーポン枝番';
COMMENT ON COLUMN public.couponindex.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.couponindex.registtime IS '登録日時';
COMMENT ON COLUMN public.couponindex.updatetime IS '更新日時';

--SEQ

-- DROP SEQUENCE
DROP SEQUENCE IF EXISTS public.couponseq CASCADE;

--CREATE SEQUENCE
CREATE SEQUENCE public.couponseq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 99999999
	START 10000000
	CACHE 1
	NO CYCLE;
