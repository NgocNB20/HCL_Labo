-- Drop table
DROP TABLE IF EXISTS public.favorite CASCADE;
DROP TABLE IF EXISTS public.orderslip CASCADE;
DROP TABLE IF EXISTS public.orderslipforrevision CASCADE;
DROP TABLE IF EXISTS public.footprint CASCADE;
DROP TABLE IF EXISTS public.freearea CASCADE;
DROP TABLE IF EXISTS public.orderitem CASCADE;
DROP TABLE IF EXISTS public.orderitemoriginrevision CASCADE;
DROP TABLE IF EXISTS public.orderitemrevision CASCADE;

-- CREATE TABLE
-- public.favorite definition
CREATE TABLE public.favorite (
	memberinfoseq numeric(10) NOT NULL, -- 会員SEQ
	goodsseq numeric(8) NOT NULL, -- 商品SEQ
	registtime timestamp(0) NOT NULL, -- 登録日時
	updatetime timestamp(0) NOT NULL, -- 更新日時
	CONSTRAINT favorite_pkey PRIMARY KEY (memberinfoseq, goodsseq)
);
COMMENT ON TABLE public.favorite IS 'お気に入り';

-- Column comments

COMMENT ON COLUMN public.favorite.memberinfoseq IS '会員SEQ';
COMMENT ON COLUMN public.favorite.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.favorite.registtime IS '登録日時';
COMMENT ON COLUMN public.favorite.updatetime IS '更新日時';

-- public.orderslip definition
CREATE TABLE public.orderslip (
	orderslipid text NOT NULL, -- 注文票ID
	orderstatus text NULL, -- 注文ステータス
	customerid text NULL, -- 顧客ID
	transactionid text NULL, -- 取引ID
	registdate timestamp NULL, -- 登録日時
	useragent text NULL, -- ユーザーエージェント
	CONSTRAINT orderslip_pk PRIMARY KEY (orderslipid)
);
COMMENT ON TABLE public.orderslip IS '注文票';

-- Column comments

COMMENT ON COLUMN public.orderslip.orderslipid IS '注文票ID';
COMMENT ON COLUMN public.orderslip.orderstatus IS '注文ステータス';
COMMENT ON COLUMN public.orderslip.customerid IS '顧客ID';
COMMENT ON COLUMN public.orderslip.transactionid IS '取引ID';
COMMENT ON COLUMN public.orderslip.registdate IS '登録日時';
COMMENT ON COLUMN public.orderslip.useragent IS 'ユーザーエージェント';


-- public.orderslipforrevision definition
CREATE TABLE public.orderslipforrevision (
	orderslipid text NOT NULL, -- 注文票ID
	orderstatus text NULL, -- 注文ステータス
	customerid text NULL, -- 顧客ID
	transactionid text NULL, -- 取引ID
	registdate timestamp NULL, -- 登録日時
	transactionrevisionid text NULL, -- 改訂用取引ID
	ordersliprevisionid text NOT NULL, -- 改訂用注文票ID
	CONSTRAINT orderslipforrevision_pk PRIMARY KEY (ordersliprevisionid)
);
COMMENT ON TABLE public.orderslipforrevision IS '改訂用注文票';

-- Column comments

COMMENT ON COLUMN public.orderslipforrevision.orderslipid IS '注文票ID';
COMMENT ON COLUMN public.orderslipforrevision.orderstatus IS '注文ステータス';
COMMENT ON COLUMN public.orderslipforrevision.customerid IS '顧客ID';
COMMENT ON COLUMN public.orderslipforrevision.transactionid IS '取引ID';
COMMENT ON COLUMN public.orderslipforrevision.registdate IS '登録日時';
COMMENT ON COLUMN public.orderslipforrevision.ordersliprevisionid IS '改訂用注文票ID';
COMMENT ON COLUMN public.orderslipforrevision.transactionrevisionid IS '改訂用取引ID';

-- public.footprint definition
CREATE TABLE public.footprint (
	accessuid varchar(100) NOT NULL, -- 端末識別情報
	goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
	shopseq numeric(4) NOT NULL, -- ショップSEQ
	registtime timestamp(0) NOT NULL, -- 登録日時
	updatetime timestamp(0) NOT NULL, -- 更新日時
	CONSTRAINT footprint_pkey PRIMARY KEY (accessuid, goodsgroupseq)
);
COMMENT ON TABLE public.footprint IS 'あしあと商品';

-- Column comments

COMMENT ON COLUMN public.footprint.accessuid IS '端末識別情報';
COMMENT ON COLUMN public.footprint.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.footprint.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.footprint.registtime IS '登録日時';
COMMENT ON COLUMN public.footprint.updatetime IS '更新日時';

-- public.freearea definition
CREATE TABLE public.freearea (
	freeareaseq numeric(8) NOT NULL, -- フリーエリアSEQ
	shopseq numeric(4) NOT NULL, -- ショップSEQ
	freeareakey varchar(50) NOT NULL, -- フリーエリアKEY
	openstarttime timestamp(0) NOT NULL, -- 公開開始日時
	freeareatitle varchar(100) NULL, -- フリーエリアタイトル
	freeareabodypc text NULL, -- フリーエリア本文PC
	targetgoods text NULL, -- 対象商品
	registtime timestamp(0) NOT NULL, -- 登録日時
	updatetime timestamp(0) NOT NULL, -- 更新日時
	sitemapflag varchar(1) NOT NULL DEFAULT 0, -- サイトマップ出力フラグ
	CONSTRAINT freearea_freeareakey_key UNIQUE (shopseq, freeareakey, openstarttime),
	CONSTRAINT freearea_pkey PRIMARY KEY (freeareaseq)
);
COMMENT ON TABLE public.freearea IS 'フリーエリア';

-- Column comments

COMMENT ON COLUMN public.freearea.freeareaseq IS 'フリーエリアSEQ';
COMMENT ON COLUMN public.freearea.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.freearea.freeareakey IS 'フリーエリアKEY';
COMMENT ON COLUMN public.freearea.openstarttime IS '公開開始日時';
COMMENT ON COLUMN public.freearea.freeareatitle IS 'フリーエリアタイトル';
COMMENT ON COLUMN public.freearea.freeareabodypc IS 'フリーエリア本文PC';
COMMENT ON COLUMN public.freearea.targetgoods IS '対象商品';
COMMENT ON COLUMN public.freearea.registtime IS '登録日時';
COMMENT ON COLUMN public.freearea.updatetime IS '更新日時';
COMMENT ON COLUMN public.freearea.sitemapflag IS 'サイトマップ出力フラグ';

-- public.orderitem definition
CREATE TABLE public.orderitem (
	id bigserial NOT NULL,
	itemid text NULL, -- 商品ID
	orderitemseq float8 NULL, -- 注文商品連番
	ordercount float8 NULL, -- 注文数量
	itemname text, -- 商品名
	unittitle1 text, -- 規格タイトル1
	unitvalue1 text, -- 規格値1
	unittitle2 text, -- 規格タイトル2
	unitvalue2 text, -- 規格値2
	jancode text, -- JANコード
	orderslipid text NOT NULL, -- 注文票ID
    noveltygoodstype varchar(1) NULL DEFAULT 0, -- ノベルティ商品フラグ
    orderitemid text NOT NULL, -- 注文商品ID
	CONSTRAINT orderitem_orderitemseq_orderslipid_key UNIQUE (orderitemseq, orderslipid),
	CONSTRAINT orderitem_pk PRIMARY KEY (id, orderslipid)
);
COMMENT ON TABLE public.orderitem IS '注文商品';

-- Column comments

COMMENT ON COLUMN public.orderitem.itemid IS '商品ID';
COMMENT ON COLUMN public.orderitem.orderitemseq IS '注文商品連番';
COMMENT ON COLUMN public.orderitem.ordercount IS '注文数量';
COMMENT ON COLUMN public.orderitem.itemname IS '商品名';
COMMENT ON COLUMN public.orderitem.unittitle1 IS '規格タイトル1';
COMMENT ON COLUMN public.orderitem.unitvalue1 IS '規格値1';
COMMENT ON COLUMN public.orderitem.unittitle2 IS '規格タイトル2';
COMMENT ON COLUMN public.orderitem.unitvalue2 IS '規格値2';
COMMENT ON COLUMN public.orderitem.jancode IS 'JANコード';
COMMENT ON COLUMN public.orderitem.orderslipid IS '注文票ID';
COMMENT ON COLUMN public.orderitem.noveltygoodstype IS 'ノベルティ商品フラグ';
COMMENT ON COLUMN public.orderitem.orderitemid IS '注文商品ID';


-- public.orderitem foreign keys

ALTER TABLE public.orderitem ADD CONSTRAINT orderitem_fk FOREIGN KEY (orderslipid) REFERENCES public.orderslip(orderslipid);

-- public.orderitemoriginrevision definition
CREATE TABLE public.orderitemoriginrevision (
	id bigserial NOT NULL,
	itemid text NULL, -- 商品ID
	orderitemseq float8 NULL, -- 注文商品連番
	ordercount float8 NULL, -- 注文数量
	itemname text, -- 商品名
	unittitle1 text, -- 規格タイトル1
	unitvalue1 text, -- 規格値1
	unittitle2 text, -- 規格タイトル2
	unitvalue2 text, -- 規格値2
	jancode text, -- JANコード
	ordersliprevisionid text NOT NULL, -- 改訂用注文票ID
    noveltygoodstype varchar(1) NULL DEFAULT 0, -- ノベルティ商品フラグ
    orderitemid text NOT NULL, -- 注文商品ID
	CONSTRAINT orderitemoriginrevision_orderitemseq_ordersliprevisionid_key UNIQUE (orderitemseq, ordersliprevisionid),
	CONSTRAINT orderitemoriginrevision_pk PRIMARY KEY (id, ordersliprevisionid)
);
COMMENT ON TABLE public.orderitemoriginrevision IS '改訂用元注文商品';

-- Column comments

COMMENT ON COLUMN public.orderitemoriginrevision.itemid IS '商品ID';
COMMENT ON COLUMN public.orderitemoriginrevision.orderitemseq IS '注文商品連番';
COMMENT ON COLUMN public.orderitemoriginrevision.ordercount IS '注文数量';
COMMENT ON COLUMN public.orderitemoriginrevision.itemname IS '商品名';
COMMENT ON COLUMN public.orderitemoriginrevision.unittitle1 IS '規格タイトル1';
COMMENT ON COLUMN public.orderitemoriginrevision.unitvalue1 IS '規格値1';
COMMENT ON COLUMN public.orderitemoriginrevision.unittitle2 IS '規格タイトル2';
COMMENT ON COLUMN public.orderitemoriginrevision.unitvalue2 IS '規格値2';
COMMENT ON COLUMN public.orderitemoriginrevision.jancode IS 'JANコード';
COMMENT ON COLUMN public.orderitemoriginrevision.ordersliprevisionid IS '改訂用注文票ID';
COMMENT ON COLUMN public.orderitemoriginrevision.noveltygoodstype IS 'ノベルティ商品フラグ';
COMMENT ON COLUMN public.orderitemoriginrevision.orderitemid IS '注文商品ID';


-- public.orderitemoriginrevision foreign keys

ALTER TABLE public.orderitemoriginrevision ADD CONSTRAINT orderitemoriginrevision_fk FOREIGN KEY (ordersliprevisionid) REFERENCES public.orderslipforrevision(ordersliprevisionid);

-- public.orderitemrevision definition
CREATE TABLE public.orderitemrevision (
	id bigserial NOT NULL,
	itemid text NULL, -- 商品ID
	orderitemseq float8 NULL, -- 注文商品連番
	ordercount float8 NULL, -- 注文数量
	itemname text, -- 商品名
	unittitle1 text, -- 規格タイトル1
	unitvalue1 text, -- 規格値1
	unittitle2 text, -- 規格タイトル2
	unitvalue2 text, -- 規格値1
	jancode text, -- JANコード
	ordersliprevisionid text NOT NULL, -- 改訂用注文票ID
    noveltygoodstype varchar(1) NULL DEFAULT 0, -- ノベルティ商品フラグ
    orderitemid text NOT NULL, -- 注文商品ID
	CONSTRAINT orderitemrevision_orderitemseq_ordersliprevisionid_key UNIQUE (orderitemseq, ordersliprevisionid),
	CONSTRAINT orderitemrevision_pk PRIMARY KEY (id, ordersliprevisionid)
);
COMMENT ON TABLE public.orderitemrevision IS '改訂用注文商品';

-- Column comments

COMMENT ON COLUMN public.orderitemrevision.itemid IS '商品ID';
COMMENT ON COLUMN public.orderitemrevision.orderitemseq IS '注文商品連番';
COMMENT ON COLUMN public.orderitemrevision.ordercount IS '注文数量';
COMMENT ON COLUMN public.orderitemrevision.itemname IS '商品名';
COMMENT ON COLUMN public.orderitemrevision.unittitle1 IS '規格タイトル1';
COMMENT ON COLUMN public.orderitemrevision.unitvalue1 IS '規格値1';
COMMENT ON COLUMN public.orderitemrevision.unittitle2 IS '規格タイトル2';
COMMENT ON COLUMN public.orderitemrevision.unitvalue2 IS '規格値2';
COMMENT ON COLUMN public.orderitemrevision.jancode IS 'JANコード';
COMMENT ON COLUMN public.orderitemrevision.ordersliprevisionid IS '改訂用注文票ID';
COMMENT ON COLUMN public.orderitemrevision.noveltygoodstype IS 'ノベルティ商品フラグ';
COMMENT ON COLUMN public.orderitemrevision.orderitemid IS '注文商品ID';


-- public.orderitemrevision foreign keys

ALTER TABLE public.orderitemrevision ADD CONSTRAINT orderitemrevision_fk FOREIGN KEY (ordersliprevisionid) REFERENCES public.orderslipforrevision(ordersliprevisionid);

-- DROP SEQUENCE
DROP SEQUENCE IF EXISTS public.freeAreaSeq CASCADE;

-- CREATE SEQUENCE
/* SHO04 フリーエリアFEQ */
CREATE SEQUENCE freeAreaSeq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999 START 10000000;
