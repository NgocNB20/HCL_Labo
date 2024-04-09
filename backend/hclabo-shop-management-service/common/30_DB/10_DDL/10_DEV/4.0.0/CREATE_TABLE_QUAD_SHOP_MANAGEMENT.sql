-- DROP TABLE
DROP TABLE IF EXISTS NEWS CASCADE;
DROP TABLE IF EXISTS SHOP CASCADE;

-- CREATE TABLE
-- NEWS
CREATE TABLE PUBLIC.NEWS (
                             NEWSSEQ NUMERIC(8) NOT NULL,
                             SHOPSEQ NUMERIC(4) NOT NULL,
                             TITLEPC VARCHAR(200) NULL,
                             TITLESP VARCHAR(200) NULL,
                             TITLEMB VARCHAR(200) NULL,
                             NEWSBODYPC TEXT NULL,
                             NEWSBODYSP TEXT NULL,
                             NEWSBODYMB TEXT NULL,
                             NEWSURLPC VARCHAR(200) NULL,
                             NEWSURLSP VARCHAR(200) NULL,
                             NEWSURLMB VARCHAR(200) NULL,
                             NEWSOPENSTATUSPC VARCHAR(1) NOT NULL DEFAULT 0,
                             NEWSOPENSTATUSMB VARCHAR(1) NULL DEFAULT 0,
                             NEWSOPENSTARTTIMEPC TIMESTAMP(0) NULL,
                             NEWSOPENENDTIMEPC TIMESTAMP(0) NULL,
                             NEWSOPENSTARTTIMEMB TIMESTAMP(0) NULL,
                             NEWSOPENENDTIMEMB TIMESTAMP(0) NULL,
                             NEWSTIME TIMESTAMP(0) NOT NULL,
                             REGISTTIME TIMESTAMP(0) NOT NULL,
                             UPDATETIME TIMESTAMP(0) NOT NULL,
                             NEWSNOTEPC TEXT NULL,
                             NEWSNOTESP TEXT NULL,
                             NEWSNOTEMB TEXT NULL,
                             CONSTRAINT NEWS_PKEY PRIMARY KEY (NEWSSEQ)
);
-- COMMENT
COMMENT ON COLUMN "public"."news"."newsseq" IS 'ニュースSEQ';
COMMENT ON COLUMN "public"."news"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."news"."titlepc" IS 'ニュースタイトル-PC';
COMMENT ON COLUMN "public"."news"."titlesp" IS 'ニュースタイトル-スマートフォン';
COMMENT ON COLUMN "public"."news"."titlemb" IS 'ニュースタイトル-携帯';
COMMENT ON COLUMN "public"."news"."newsbodypc" IS 'ニュース本文-PC';
COMMENT ON COLUMN "public"."news"."newsbodysp" IS 'ニュース本文-スマートフォン';
COMMENT ON COLUMN "public"."news"."newsbodymb" IS 'ニュース本文-携帯';
COMMENT ON COLUMN "public"."news"."newsurlpc" IS 'ニュースURL-PC';
COMMENT ON COLUMN "public"."news"."newsurlsp" IS 'ニュースURL-スマートフォン';
COMMENT ON COLUMN "public"."news"."newsurlmb" IS 'ニュースURL-携帯';
COMMENT ON COLUMN "public"."news"."newsopenstatuspc" IS 'ニュース公開状態PC';
COMMENT ON COLUMN "public"."news"."newsopenstatusmb" IS 'ニュース公開状態携帯';
COMMENT ON COLUMN "public"."news"."newsopenstarttimepc" IS 'ニュース公開開始日時PC';
COMMENT ON COLUMN "public"."news"."newsopenendtimepc" IS 'ニュース公開終了日時PC';
COMMENT ON COLUMN "public"."news"."newsopenstarttimemb" IS 'ニュース公開開始日時携帯';
COMMENT ON COLUMN "public"."news"."newsopenendtimemb" IS 'ニュース公開終了日時携帯';
COMMENT ON COLUMN "public"."news"."newstime" IS 'ニュース日時';
COMMENT ON COLUMN "public"."news"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."news"."updatetime" IS '更新日時';
COMMENT ON COLUMN "public"."news"."newsnotepc" IS 'ニュース詳細PC';
COMMENT ON COLUMN "public"."news"."newsnotesp" IS 'ニュース詳細スマートフォン';
COMMENT ON COLUMN "public"."news"."newsnotemb" IS 'ニュース詳細モバイル';
COMMENT ON TABLE "public"."news" IS 'ニュース';

-- SHOP
CREATE TABLE PUBLIC.SHOP (
                      SHOPSEQ NUMERIC(4) NOT NULL,
                      SHOPID VARCHAR(20) NOT NULL,
                      SHOPNAMEPC VARCHAR(80) NOT NULL,
                      SHOPNAMEMB VARCHAR(80) NULL,
                      URLPC VARCHAR(200) NULL,
                      METADESCRIPTION VARCHAR(400) NULL,
                      METAKEYWORD VARCHAR(400) NULL,
                      VERSIONNO NUMERIC(6) NOT NULL DEFAULT 0,
                      REGISTTIME TIMESTAMP(0) NOT NULL,
                      UPDATETIME TIMESTAMP(0) NOT NULL,
                      AUTOAPPROVALFLAG VARCHAR(1) NOT NULL DEFAULT 0,
                      REVIEWDEFAULTPOINT NUMERIC(8) NULL,
                      CONSTRAINT SHOP_PKEY PRIMARY KEY (SHOPSEQ),
                      CONSTRAINT SHOP_SHOPID_KEY UNIQUE (SHOPID)
);

-- COMMENT
COMMENT ON COLUMN "public"."shop"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."shop"."shopid" IS 'ショップID';
COMMENT ON COLUMN "public"."shop"."shopnamepc" IS 'ショップ名PC';
COMMENT ON COLUMN "public"."shop"."shopnamemb" IS 'ショップ名携帯';
COMMENT ON COLUMN "public"."shop"."urlpc" IS 'ショップURL-PC';
COMMENT ON COLUMN "public"."shop"."metadescription" IS 'META-DESCRIPTION';
COMMENT ON COLUMN "public"."shop"."metakeyword" IS 'META-KEYWORD';
COMMENT ON COLUMN "public"."shop"."versionno" IS '更新カウンタ';
COMMENT ON COLUMN "public"."shop"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."shop"."updatetime" IS '更新日時';
COMMENT ON COLUMN "public"."shop"."autoapprovalflag" IS 'オート承認フラグ';
COMMENT ON COLUMN "public"."shop"."reviewdefaultpoint" IS 'レビューデフォルトポイント付与数';
COMMENT ON TABLE "public"."shop" IS 'ショップ';

-- SEQUENCE
DROP SEQUENCE IF EXISTS public.newsseq;
/* SHO05 ニュースSEQ */
CREATE SEQUENCE newsseq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999 START 10000000;
