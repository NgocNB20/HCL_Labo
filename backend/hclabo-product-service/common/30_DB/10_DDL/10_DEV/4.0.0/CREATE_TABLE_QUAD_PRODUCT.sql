-- public.goodsgroup definition

-- Drop table
DROP TABLE IF EXISTS goodsgroup CASCADE;

-- DROP TABLE public.goodsgroup;

CREATE TABLE public.goodsgroup (
                                   goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                   goodsgroupcode varchar(20) NOT NULL, -- 商品グループコード
                                   goodsgroupname varchar(120) NULL, -- 商品グループ名
                                   whatsnewdate timestamp(0) NOT NULL, -- 新着日付
                                   goodsopenstatuspc varchar(1) NOT NULL DEFAULT 0, -- 商品公開状態PC
                                   openstarttimepc timestamp(0) NULL, -- 商品公開開始日時PC
                                   openendtimepc timestamp(0) NULL, -- 商品公開終了日時PC
                                   goodstaxtype varchar(1) NOT NULL DEFAULT '1'::character varying, -- 商品消費税種別
                                   taxrate numeric(2) NOT NULL, -- 税率
                                   goodsprice numeric(8) NOT NULL DEFAULT 0, -- 商品単価
                                   alcoholflag varchar(1) NOT NULL DEFAULT '0'::character varying, -- 酒類フラグ
                                   noveltygoodstype varchar(1) NOT NULL DEFAULT '0'::character varying, -- ノベルティ商品フラグ
                                   snslinkflag varchar(1) NOT NULL DEFAULT 0, -- SNS連携フラグ
                                   shopseq numeric(4) NOT NULL, -- ショップSEQ
                                   versionno numeric(6) NOT NULL DEFAULT 0, -- 更新カウンタ
                                   registtime timestamp(0) NOT NULL, -- 登録日時
                                   updatetime timestamp(0) NOT NULL, -- 更新日時
                                   CONSTRAINT goodsgroup_pkey PRIMARY KEY (goodsgroupseq)
);
CREATE INDEX idx_goodsgroup_goodsgroupcode ON public.goodsgroup USING btree (goodsgroupcode);
COMMENT ON TABLE public.goodsgroup IS '商品グループ';
-- Column comments

COMMENT ON COLUMN public.goodsgroup.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goodsgroup.goodsgroupcode IS '商品グループコード';
COMMENT ON COLUMN public.goodsgroup.goodsgroupname IS '商品グループ名';
COMMENT ON COLUMN public.goodsgroup.whatsnewdate IS '新着日付';
COMMENT ON COLUMN public.goodsgroup.goodsopenstatuspc IS '商品公開状態PC';
COMMENT ON COLUMN public.goodsgroup.openstarttimepc IS '商品公開開始日時PC';
COMMENT ON COLUMN public.goodsgroup.openendtimepc IS '商品公開終了日時PC';
COMMENT ON COLUMN public.goodsgroup.goodstaxtype IS '商品消費税種別';
COMMENT ON COLUMN public.goodsgroup.taxrate IS '税率';
COMMENT ON COLUMN public.goodsgroup.goodsprice IS '商品単価';
COMMENT ON COLUMN public.goodsgroup.alcoholflag IS '酒類フラグ';
COMMENT ON COLUMN public.goodsgroup.noveltygoodstype IS 'ノベルティ商品フラグ';
COMMENT ON COLUMN public.goodsgroup.snslinkflag IS 'SNS連携フラグ';
COMMENT ON COLUMN public.goodsgroup.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.goodsgroup.versionno IS '更新カウンタ';
COMMENT ON COLUMN public.goodsgroup.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsgroup.updatetime IS '更新日時';

-- public.goodsgroupdisplay definition

-- Drop table
DROP TABLE IF EXISTS goodsgroupdisplay CASCADE;

-- DROP TABLE public.goodsgroupdisplay;

CREATE TABLE public.goodsgroupdisplay (
                                          goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                          goodstag text[] NULL, -- 商品タグ
                                          deliverytype varchar(50) NULL, -- 商品納期
                                          goodsnote1 varchar(4000) NULL, -- 商品説明１
                                          goodsnote2 varchar(4000) NULL, -- 商品説明２
                                          goodsnote3 varchar(4000) NULL, -- 商品説明３
                                          goodsnote4 varchar(4000) NULL, -- 商品説明４
                                          goodsnote5 varchar(4000) NULL, -- 商品説明５
                                          goodsnote6 varchar(4000) NULL, -- 商品説明６
                                          goodsnote7 varchar(4000) NULL, -- 商品説明７
                                          goodsnote8 varchar(4000) NULL, -- 商品説明８
                                          goodsnote9 varchar(4000) NULL, -- 商品説明９
                                          goodsnote10 varchar(4000) NULL, -- 商品説明１０
                                          ordersetting1 varchar(4000) NULL, -- 受注連携設定１
                                          ordersetting2 varchar(4000) NULL, -- 受注連携設定２
                                          ordersetting3 varchar(4000) NULL, -- 受注連携設定３
                                          ordersetting4 varchar(4000) NULL, -- 受注連携設定４
                                          ordersetting5 varchar(4000) NULL, -- 受注連携設定５
                                          ordersetting6 varchar(4000) NULL, -- 受注連携設定６
                                          ordersetting7 varchar(4000) NULL, -- 受注連携設定７
                                          ordersetting8 varchar(4000) NULL, -- 受注連携設定８
                                          ordersetting9 varchar(4000) NULL, -- 受注連携設定９
                                          ordersetting10 varchar(4000) NULL, -- 受注連携設定１０
                                          informationiconpc varchar(150) NULL, -- インフォメーションアイコンPC
                                          unitmanagementflag varchar(1) NOT NULL DEFAULT 0, -- 規格管理フラグ
                                          unittitle1 varchar(100) NULL, -- 規格タイトル１
                                          unittitle2 varchar(100) NULL, -- 規格タイトル２
                                          searchkeyword varchar(1000) NULL, -- 商品検索キーワード
                                          searchkeywordemuc text NULL, -- 商品検索キーワード全角大文字
                                          searchsettingkeywordsemuc text NULL, -- 検索用商品設定キーワード全角
                                          metadescription varchar(400) NULL, -- meta-description
                                          metakeyword varchar(200) NULL, -- meta-keyword
                                          registtime timestamp(0) NOT NULL, -- 登録日時
                                          updatetime timestamp(0) NOT NULL, -- 更新日時
                                          CONSTRAINT goodsgroupdisplay_pkey PRIMARY KEY (goodsgroupseq)
);
COMMENT ON TABLE public.goodsgroupdisplay IS '商品グループ表示';

-- Column comments
COMMENT ON COLUMN public.goodsgroupdisplay.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goodsgroupdisplay.goodstag IS '商品タグ';
COMMENT ON COLUMN public.goodsgroupdisplay.deliverytype IS '商品納期';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote1 IS '商品説明１';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote2 IS '商品説明２';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote3 IS '商品説明３';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote4 IS '商品説明４';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote5 IS '商品説明５';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote6 IS '商品説明６';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote7 IS '商品説明７';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote8 IS '商品説明８';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote9 IS '商品説明９';
COMMENT ON COLUMN public.goodsgroupdisplay.goodsnote10 IS '商品説明１０';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting1 IS '受注連携設定１';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting2 IS '受注連携設定２';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting3 IS '受注連携設定３';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting4 IS '受注連携設定４';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting5 IS '受注連携設定５';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting6 IS '受注連携設定６';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting7 IS '受注連携設定７';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting8 IS '受注連携設定８';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting9 IS '受注連携設定９';
COMMENT ON COLUMN public.goodsgroupdisplay.ordersetting10 IS '受注連携設定１０';
COMMENT ON COLUMN public.goodsgroupdisplay.informationiconpc IS 'インフォメーションアイコンPC';
COMMENT ON COLUMN public.goodsgroupdisplay.unitmanagementflag IS '規格管理フラグ';
COMMENT ON COLUMN public.goodsgroupdisplay.unittitle1 IS '規格タイトル１';
COMMENT ON COLUMN public.goodsgroupdisplay.unittitle2 IS '規格タイトル２';
COMMENT ON COLUMN public.goodsgroupdisplay.searchkeyword IS '商品検索キーワード';
COMMENT ON COLUMN public.goodsgroupdisplay.searchkeywordemuc IS '商品検索キーワード全角大文字';
COMMENT ON COLUMN public.goodsgroupdisplay.searchsettingkeywordsemuc IS '検索用商品設定キーワード全角';
COMMENT ON COLUMN public.goodsgroupdisplay.metadescription IS 'meta-description';
COMMENT ON COLUMN public.goodsgroupdisplay.metakeyword IS 'meta-keyword';
COMMENT ON COLUMN public.goodsgroupdisplay.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsgroupdisplay.updatetime IS '更新日時';

-- ----------------------------
-- Indexes structure for table goodsgroupdisplay
-- ----------------------------
CREATE INDEX idx_goodsgroupdisplay_goodstag ON goodsgroupdisplay USING GIN (goodstag);

-- public.goodsgroupimage definition

-- Drop table
DROP TABLE IF EXISTS goodsgroupimage CASCADE;

-- DROP TABLE public.goodsgroupimage;

CREATE TABLE public.goodsgroupimage (
                                        goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                        imagetypeversionno numeric(4) NOT NULL DEFAULT 1, -- 画像種別内連番
                                        imagefilename varchar(100) NULL, -- 画像ファイル名
                                        registtime timestamp(0) NOT NULL, -- 登録日時
                                        updatetime timestamp(0) NOT NULL, -- 更新日時
                                        CONSTRAINT goodsgroupimage_pkey PRIMARY KEY (goodsgroupseq, imagetypeversionno)
);
COMMENT ON TABLE public.goodsgroupimage IS '商品グループ画像';

-- Column comments

COMMENT ON COLUMN public.goodsgroupimage.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goodsgroupimage.imagetypeversionno IS '画像種別内連番';
COMMENT ON COLUMN public.goodsgroupimage.imagefilename IS '画像ファイル名';
COMMENT ON COLUMN public.goodsgroupimage.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsgroupimage.updatetime IS '更新日時';

-- public.goodsinformationicon definition

-- Drop table
DROP TABLE IF EXISTS goodsinformationicon CASCADE;

-- DROP TABLE public.goodsinformationicon;

CREATE TABLE public.goodsinformationicon (
                                             iconseq numeric(8) NOT NULL, -- アイコンSEQ
                                             shopseq numeric(4) NOT NULL, -- ショップSEQ
                                             iconname varchar(60) NOT NULL, -- アイコン名
                                             colorcode varchar(20) NOT NULL, -- カラーコード
                                             orderdisplay numeric(4) NULL, -- 表示順
                                             registtime timestamp(0) NOT NULL, -- 登録日時
                                             updatetime timestamp(0) NOT NULL, -- 更新日時
                                             CONSTRAINT goodsinformationicon_pkey PRIMARY KEY (iconseq)
);
COMMENT ON TABLE public.goodsinformationicon IS '商品インフォメーションアイコン';

-- Column comments

COMMENT ON COLUMN public.goodsinformationicon.iconseq IS 'アイコンSEQ';
COMMENT ON COLUMN public.goodsinformationicon.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.goodsinformationicon.iconname IS 'アイコン名';
COMMENT ON COLUMN public.goodsinformationicon.colorcode IS 'カラーコード';
COMMENT ON COLUMN public.goodsinformationicon.orderdisplay IS '表示順';
COMMENT ON COLUMN public.goodsinformationicon.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsinformationicon.updatetime IS '更新日時';

-- public.goodsgrouppopularity definition

-- Drop table
DROP TABLE IF EXISTS goodsgrouppopularity CASCADE;

-- DROP TABLE public.goodsgrouppopularity;

CREATE TABLE public.goodsgrouppopularity (
                                             goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                             popularitycount numeric(8) NOT NULL DEFAULT 0, -- 人気カウント
                                             registtime timestamp(0) NOT NULL, -- 登録日時
                                             updatetime timestamp(0) NOT NULL, -- 更新日時
                                             CONSTRAINT goodsgrouppopularity_pkey PRIMARY KEY (goodsgroupseq)
);
COMMENT ON TABLE public.goodsgrouppopularity IS '商品グループ人気';

-- Column comments

COMMENT ON COLUMN public.goodsgrouppopularity.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goodsgrouppopularity.popularitycount IS '人気カウント';
COMMENT ON COLUMN public.goodsgrouppopularity.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsgrouppopularity.updatetime IS '更新日時';

-- public.goodsranking definition

-- Drop table
DROP TABLE IF EXISTS goodsranking CASCADE;

-- DROP TABLE public.goodsranking;

CREATE TABLE public.goodsranking (
                                     shopseq numeric(4) NOT NULL,
                                     goodsrankingtype varchar(1) NOT NULL DEFAULT 0,
                                     goodsgroupseq numeric(8) NOT NULL,
                                     totalvalue numeric(8) NOT NULL DEFAULT 0,
                                     totaltargetname varchar(120) NULL,
                                     registtime timestamp(0) NOT NULL,
                                     updatetime timestamp(0) NOT NULL,
                                     CONSTRAINT goodsranking_pkey PRIMARY KEY (shopseq, goodsrankingtype, goodsgroupseq)
);

-- public.goodsrelation definition

-- Drop table
DROP TABLE IF EXISTS goodsrelation CASCADE;

-- DROP TABLE public.goodsrelation;

CREATE TABLE public.goodsrelation (
                                      goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                      goodsrelationgroupseq numeric(8) NOT NULL, -- 関連商品グループSEQ
                                      orderdisplay numeric(4) NULL, -- 表示順
                                      registtime timestamp(0) NOT NULL, -- 登録日時
                                      updatetime timestamp(0) NOT NULL, -- 更新日時
                                      CONSTRAINT goodsrelation_pkey PRIMARY KEY (goodsrelationgroupseq, goodsgroupseq)
);
CREATE INDEX idx_goodsrelation_goodsgroupseq ON public.goodsrelation USING btree (goodsgroupseq);
COMMENT ON TABLE public.goodsrelation IS '関連商品';

-- Column comments

COMMENT ON COLUMN public.goodsrelation.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goodsrelation.goodsrelationgroupseq IS '関連商品グループSEQ';
COMMENT ON COLUMN public.goodsrelation.orderdisplay IS '表示順';
COMMENT ON COLUMN public.goodsrelation.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsrelation.updatetime IS '更新日時';

-- public.goods definition

-- Drop table
DROP TABLE IF EXISTS goods CASCADE;

-- DROP TABLE public.goods;

CREATE TABLE public.goods (
                              goodsseq numeric(8) NOT NULL, -- 商品SEQ
                              goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                              goodscode varchar(20) NOT NULL, -- 商品コード
                              jancode varchar(16) NULL, -- JANコード
                              salestatuspc varchar(1) NOT NULL DEFAULT 0, -- 販売状態PC
                              salestarttimepc timestamp(0) NULL, -- 販売開始日時PC
                              saleendtimepc timestamp(0) NULL, -- 販売終了日時PC
                              individualdeliverytype varchar(1) NOT NULL DEFAULT 0, -- 商品個別配送種別
                              freedeliveryflag varchar(1) NOT NULL DEFAULT 0, -- 無料配送フラグ
                              unitmanagementflag varchar(1) NOT NULL DEFAULT 0, -- 規格管理フラグ
                              unitvalue1 varchar(100) NULL, -- 規格値１
                              unitvalue2 varchar(100) NULL, -- 規格値２
                              stockmanagementflag varchar(1) NOT NULL DEFAULT 0, -- 在庫管理フラグ
                              purchasedmax numeric(4) NOT NULL DEFAULT 0, -- 商品最大購入可能数
                              orderdisplay numeric(4) NULL, -- 表示順
                              shopseq numeric(4) NOT NULL, -- ショップSEQ
                              versionno numeric(6) NOT NULL DEFAULT 0, -- 更新カウンタ
                              registtime timestamp(0) NOT NULL, -- 登録日時
                              updatetime timestamp(0) NOT NULL, -- 更新日時
                              CONSTRAINT goods_pkey PRIMARY KEY (goodsseq)
);
CREATE UNIQUE INDEX goods_goodscode_key ON public.goods USING btree (shopseq, goodscode);
CREATE INDEX idx_goods_goodsgroupseq ON public.goods USING btree (goodsgroupseq);
COMMENT ON TABLE public.goods IS '商品';

-- Column comments

COMMENT ON COLUMN public.goods.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.goods.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.goods.goodscode IS '商品コード';
COMMENT ON COLUMN public.goods.jancode IS 'JANコード';
COMMENT ON COLUMN public.goods.salestatuspc IS '販売状態PC';
COMMENT ON COLUMN public.goods.salestarttimepc IS '販売開始日時PC';
COMMENT ON COLUMN public.goods.saleendtimepc IS '販売終了日時PC';
COMMENT ON COLUMN public.goods.individualdeliverytype IS '商品個別配送種別';
COMMENT ON COLUMN public.goods.freedeliveryflag IS '無料配送フラグ';
COMMENT ON COLUMN public.goods.unitmanagementflag IS '規格管理フラグ';
COMMENT ON COLUMN public.goods.unitvalue1 IS '規格値１';
COMMENT ON COLUMN public.goods.unitvalue2 IS '規格値２';
COMMENT ON COLUMN public.goods.stockmanagementflag IS '在庫管理フラグ';
COMMENT ON COLUMN public.goods.purchasedmax IS '商品最大購入可能数';
COMMENT ON COLUMN public.goods.orderdisplay IS '表示順';
COMMENT ON COLUMN public.goods.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.goods.versionno IS '更新カウンタ';
COMMENT ON COLUMN public.goods.registtime IS '登録日時';
COMMENT ON COLUMN public.goods.updatetime IS '更新日時';

-- public.category definition

-- Drop table
DROP TABLE IF EXISTS category CASCADE;

-- DROP TABLE public.category;

CREATE TABLE public.category (
                                 categoryseq numeric(8) NOT NULL, -- カテゴリSEQ
                                 shopseq numeric(4) NOT NULL, -- ショップSEQ
                                 categoryid varchar(20) NOT NULL, -- カテゴリID
                                 categoryname varchar(120) NOT NULL, -- カテゴリ名
                                 categoryopenstatuspc varchar(1) NOT NULL DEFAULT 0, -- カテゴリ公開状態PC
                                 categoryopenstarttimepc timestamp(0) NULL, -- カテゴリ公開開始日時PC
                                 categoryopenendtimepc timestamp(0) NULL, -- カテゴリ公開終了日時PC
                                 categorytype varchar(1) NOT NULL DEFAULT 0, -- カテゴリ種別
                                 registtime timestamp(0) NOT NULL, -- 登録日時
                                 updatetime timestamp(0) NOT NULL, -- 更新日時
                                 CONSTRAINT category_pkey PRIMARY KEY (categoryseq)
);
COMMENT ON TABLE public.category IS 'カテゴリ';

-- Column comments

COMMENT ON COLUMN public.category.categoryseq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.category.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.category.categoryid IS 'カテゴリID';
COMMENT ON COLUMN public.category.categoryname IS 'カテゴリ名';
COMMENT ON COLUMN public.category.categoryopenstatuspc IS 'カテゴリ公開状態PC';
COMMENT ON COLUMN public.category.categoryopenstarttimepc IS 'カテゴリ公開開始日時PC';
COMMENT ON COLUMN public.category.categoryopenendtimepc IS 'カテゴリ公開終了日時PC';
COMMENT ON COLUMN public.category.categorytype IS 'カテゴリ種別';
COMMENT ON COLUMN public.category.registtime IS '登録日時';
COMMENT ON COLUMN public.category.updatetime IS '更新日時';

-- public.categorydisplay definition

-- Drop table
DROP TABLE IF EXISTS categorydisplay CASCADE;

-- DROP TABLE public.categorydisplay;

CREATE TABLE public.categorydisplay (
                                        categoryseq numeric(8) NOT NULL, -- カテゴリSEQ
                                        categorynamepc varchar(120) NULL, -- カテゴリ表示名PC
                                        categorynotepc varchar(2000) NULL, -- カテゴリについての説明文PC
                                        freetextpc text NULL,
                                        metadescription varchar(400) NULL, -- meta-description
                                        categoryimagepc varchar(100) NULL, -- カテゴリ画像PC
                                        registtime timestamp(0) NOT NULL, -- 登録日時
                                        updatetime timestamp(0) NOT NULL, -- 更新日時
                                        CONSTRAINT categorydisplay_pkey PRIMARY KEY (categoryseq)
);
COMMENT ON TABLE public.categorydisplay IS 'カテゴリ';

-- Column comments

COMMENT ON COLUMN public.categorydisplay.categoryseq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.categorydisplay.categorynamepc IS 'カテゴリ表示名PC';
COMMENT ON COLUMN public.categorydisplay.categorynotepc IS 'カテゴリについての説明文PC';
COMMENT ON COLUMN public.categorydisplay.freeTextPC IS 'フリーテキストPC';
COMMENT ON COLUMN public.categorydisplay.metadescription IS 'meta-description';
COMMENT ON COLUMN public.categorydisplay.categoryimagepc IS 'カテゴリ画像PC';
COMMENT ON COLUMN public.categorydisplay.registtime IS '登録日時';
COMMENT ON COLUMN public.categorydisplay.updatetime IS '更新日時';

-- public.categorygoods definition

-- Drop table
DROP TABLE IF EXISTS categorygoods CASCADE;

-- DROP TABLE public.categorygoods;

CREATE TABLE public.categorygoods (
                                      categoryseq numeric(8) NOT NULL, -- カテゴリSEQ
                                      goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                      manualOrderDisplay numeric(4) NULL, -- 手動並び替え表示順
                                      registtime timestamp(0) NOT NULL, -- 登録日時
                                      updatetime timestamp(0) NOT NULL, -- 更新日時
                                      CONSTRAINT categorygoods_pkey PRIMARY KEY (categoryseq, goodsgroupseq)
);
CREATE INDEX idx_categorygoods_goodsgroupseq ON public.categorygoods USING btree (goodsgroupseq);
COMMENT ON TABLE public.categorygoods IS 'カテゴリ登録商品';

-- Column comments

COMMENT ON COLUMN public.categorygoods.categoryseq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.categorygoods.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.categorygoods.manualOrderDisplay IS '手動並び替え表示順';
COMMENT ON COLUMN public.categorygoods.registtime IS '登録日時';
COMMENT ON COLUMN public.categorygoods.updatetime IS '更新日時';

-- public.stockstatusdisplay definition

-- Drop table
DROP TABLE IF EXISTS stockstatusdisplay CASCADE;

-- DROP TABLE public.stockstatusdisplay;

CREATE TABLE public.stockstatusdisplay (
                                           goodsgroupseq numeric(8) NOT NULL, -- 商品グループSEQ
                                           stockstatuspc varchar(2) NOT NULL, -- 在庫状態PC
                                           registtime timestamp(0) NOT NULL, -- 登録日時
                                           updatetime timestamp(0) NOT NULL, -- 更新日時
                                           CONSTRAINT stockstatusdisplay_pkey PRIMARY KEY (goodsgroupseq)
);
COMMENT ON TABLE public.stockstatusdisplay IS '商品グループ在庫表示';

-- Column comments

COMMENT ON COLUMN public.stockstatusdisplay.goodsgroupseq IS '商品グループSEQ';
COMMENT ON COLUMN public.stockstatusdisplay.stockstatuspc IS '在庫状態PC';
COMMENT ON COLUMN public.stockstatusdisplay.registtime IS '登録日時';
COMMENT ON COLUMN public.stockstatusdisplay.updatetime IS '更新日時';

-- public.goodsstockdisplay definition

-- Drop table
DROP TABLE IF EXISTS goodsstockdisplay CASCADE;

-- DROP TABLE public.goodsstockdisplay;

CREATE TABLE public.goodsstockdisplay (
                                          goodsseq numeric(8) NOT NULL, -- 商品SEQ
                                          remainderfewstock numeric(6) NOT NULL, -- 残少表示在庫数
                                          orderpointstock numeric(6) NOT NULL, -- 発注点在庫数
                                          safetystock numeric(6) NOT NULL, -- 安全在庫数
                                          realstock numeric(6) NOT NULL, -- 実在庫数
                                          orderreservestock numeric(6) NOT NULL, -- 注文確保在庫数
                                          registtime timestamp(0) NOT NULL, -- 登録日時
                                          updatetime timestamp(0) NOT NULL -- 更新日時
);
ALTER TABLE public.goodsstockdisplay ADD CONSTRAINT goodsstockdisplay_pk PRIMARY KEY (goodsseq);
COMMENT ON TABLE public.goodsstockdisplay IS '商品在庫表示';

-- Column comments

COMMENT ON COLUMN public.goodsstockdisplay.goodsseq IS '商品SEQ';
COMMENT ON COLUMN public.goodsstockdisplay.remainderfewstock IS '残少表示在庫数';
COMMENT ON COLUMN public.goodsstockdisplay.orderpointstock IS '発注点在庫数';
COMMENT ON COLUMN public.goodsstockdisplay.safetystock IS '安全在庫数';
COMMENT ON COLUMN public.goodsstockdisplay.realstock IS '実在庫数';
COMMENT ON COLUMN public.goodsstockdisplay.orderreservestock IS '注文確保在庫数';
COMMENT ON COLUMN public.goodsstockdisplay.registtime IS '登録日時';
COMMENT ON COLUMN public.goodsstockdisplay.updatetime IS '更新日時';

-- public.tax definition

-- Drop table
DROP TABLE IF EXISTS tax CASCADE;

-- DROP TABLE public.tax;

CREATE TABLE public.tax (
                            taxseq numeric(4) NOT NULL, -- 消費税SEQ
                            starttime timestamp(0) NOT NULL, -- 開始日時
                            endtime timestamp(0) NOT NULL, -- 終了日時
                            registtime timestamp(0) NOT NULL, -- 登録日時
                            updatetime timestamp(0) NOT NULL, -- 更新日時
                            CONSTRAINT tax_pkey PRIMARY KEY (taxseq)
);
COMMENT ON TABLE public.tax IS '消費税';

-- Column comments

COMMENT ON COLUMN public.tax.taxseq IS '消費税SEQ';
COMMENT ON COLUMN public.tax.starttime IS '開始日時';
COMMENT ON COLUMN public.tax.endtime IS '終了日時';
COMMENT ON COLUMN public.tax.registtime IS '登録日時';
COMMENT ON COLUMN public.tax.updatetime IS '更新日時';

-- public.taxrate definition

-- Drop table
DROP TABLE IF EXISTS taxrate CASCADE;

-- DROP TABLE public.taxrate;

CREATE TABLE public.taxrate (
                                taxseq numeric(4) NOT NULL, -- 消費税SEQ
                                rate numeric(2) NOT NULL, -- 税率
                                ratetype varchar(1) NOT NULL, -- 区分
                                orderdisplay numeric(4) NOT NULL, -- 表示順
                                registtime timestamp(0) NOT NULL, -- 登録日時
                                updatetime timestamp(0) NOT NULL, -- 更新日時
                                CONSTRAINT taxrate_pkey PRIMARY KEY (taxseq, rate)
);
COMMENT ON TABLE public.taxrate IS '税率';

-- Column comments

COMMENT ON COLUMN public.taxrate.taxseq IS '消費税SEQ';
COMMENT ON COLUMN public.taxrate.rate IS '税率';
COMMENT ON COLUMN public.taxrate.ratetype IS '区分';
COMMENT ON COLUMN public.taxrate.orderdisplay IS '表示順';
COMMENT ON COLUMN public.taxrate.registtime IS '登録日時';
COMMENT ON COLUMN public.taxrate.updatetime IS '更新日時';

-- public.csvoption definition

DROP TABLE IF EXISTS csvoption CASCADE;

CREATE TABLE public.csvoption (
                                  optionid numeric(4,0) NOT NULL,
                                  optioninfo text,
                                  CONSTRAINT csvoption_pk PRIMARY KEY (optionid)
);
COMMENT ON TABLE public.csvoption IS 'CSVオプション';

-- Column comments
COMMENT ON COLUMN public.csvoption.optionid IS 'オプションID';
COMMENT ON COLUMN public.csvoption.optioninfo IS 'CSVDL オプション情報';

-- public.categorygoodssort definition

-- Drop table
DROP TABLE IF EXISTS categorygoodssort CASCADE;

-- DROP TABLE public.categorygoodssort;

CREATE TABLE public.categorygoodssort (
                                          categorySeq numeric(8) NOT NULL, -- カテゴリSEQ
                                          goodsSortColumn varchar(50) NOT NULL, -- 商品並び順項目
                                          goodsSortOrder bool NOT NULL, -- 商品並び順
                                          registtime timestamp(0) NOT NULL, -- 登録日時
                                          updatetime timestamp(0) NOT NULL, -- 更新日時
                                          CONSTRAINT categorygoodssort_pkey PRIMARY KEY (categorySeq)
);
COMMENT ON TABLE public.categorygoodssort IS 'カテゴリ登録商品並び順';
-- Column comments

COMMENT ON COLUMN public.categorygoodssort.categorySeq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.categorygoodssort.goodsSortColumn IS '商品並び順項目';
COMMENT ON COLUMN public.categorygoodssort.goodsSortOrder IS '商品並び順';
COMMENT ON COLUMN public.categorygoodssort.registtime IS '登録日時';
COMMENT ON COLUMN public.categorygoodssort.updatetime IS '更新日時';

-- public.categorycondition definition

-- Drop table
DROP TABLE IF EXISTS categorycondition CASCADE;

-- DROP TABLE public.categorycondition;

CREATE TABLE public.categorycondition (
                                          categorySeq numeric(8) NOT NULL, -- カテゴリSEQ
                                          conditionType varchar(1) NOT NULL, -- 条件種別
                                          registtime timestamp(0) NOT NULL, -- 登録日時
                                          updatetime timestamp(0) NOT NULL, -- 更新日時
                                          CONSTRAINT categorycondition_pkey PRIMARY KEY (categorySeq)
);
COMMENT ON TABLE public.categorycondition IS 'カテゴリ条件';
-- Column comments

COMMENT ON COLUMN public.categorycondition.categorySeq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.categorycondition.conditionType IS '条件種別';
COMMENT ON COLUMN public.categorycondition.registtime IS '登録日時';
COMMENT ON COLUMN public.categorycondition.updatetime IS '更新日時';

-- public.categoryconditiondetail definition

-- Drop table
DROP TABLE IF EXISTS categoryconditiondetail CASCADE;

-- DROP TABLE public.categoryconditiondetail;

CREATE TABLE public.categoryconditiondetail (
                                                categorySeq numeric(8) NOT NULL, -- カテゴリSEQ
                                                conditionNo numeric(4) NOT NULL, -- 条件No
                                                conditionColumn varchar(50) NOT NULL, -- 条件項目
                                                conditionOperator varchar(2) NOT NULL, -- 条件演算子
                                                conditionValue text NOT NULL, -- 条件値
                                                registtime timestamp(0) NOT NULL, -- 登録日時
                                                updatetime timestamp(0) NOT NULL, -- 更新日時
                                                CONSTRAINT categoryconditiondetail_pkey PRIMARY KEY (categorySeq, conditionNo)
);
COMMENT ON TABLE public.categoryconditiondetail IS 'カテゴリ条件詳細';
-- Column comments

COMMENT ON COLUMN public.categoryconditiondetail.categorySeq IS 'カテゴリSEQ';
COMMENT ON COLUMN public.categoryconditiondetail.conditionNo IS '条件No';
COMMENT ON COLUMN public.categoryconditiondetail.conditionColumn IS '条件項目';
COMMENT ON COLUMN public.categoryconditiondetail.conditionOperator IS '条件演算子';
COMMENT ON COLUMN public.categoryconditiondetail.conditionValue IS '条件値';
COMMENT ON COLUMN public.categoryconditiondetail.registtime IS '登録日時';
COMMENT ON COLUMN public.categoryconditiondetail.updatetime IS '更新日時';

-- public.menu definition

-- Drop table
DROP TABLE IF EXISTS menu CASCADE;

-- DROP TABLE public.menu;

CREATE TABLE public.menu (
                             menuId numeric(4) NOT NULL, -- メニューID
                             categoryTree jsonb NULL, -- カテゴリーツリー
                             registtime timestamp(0) NOT NULL, -- 登録日時
                             updatetime timestamp(0) NOT NULL, -- 更新日時
                             CONSTRAINT menu_pkey PRIMARY KEY (menuId)
);
COMMENT ON TABLE public.menu IS 'メニュー';
-- Column comments

COMMENT ON COLUMN public.menu.menuId IS 'メニューID';
COMMENT ON COLUMN public.menu.categoryTree IS 'カテゴリーツリー';
COMMENT ON COLUMN public.menu.registtime IS '登録日時';
COMMENT ON COLUMN public.menu.updatetime IS '更新日時';


-- public.goodstag definition

-- Drop table
DROP TABLE IF EXISTS goodstag CASCADE;

-- DROP TABLE public.goodstag;

CREATE TABLE public.goodstag (
                                tag text NOT NULL, -- タグ
                                count numeric(8) NOT NULL, -- 参照数
                                registtime timestamp(0) NOT NULL, -- 登録日時
                                updatetime timestamp(0) NOT NULL, -- 更新日時
                                CONSTRAINT goodstag_pkey PRIMARY KEY (tag)
);
COMMENT ON TABLE public.goodstag IS '商品タグ';
-- Column comments

COMMENT ON COLUMN public.goodstag.tag IS 'タグ';
COMMENT ON COLUMN public.goodstag.count IS '参照数';
COMMENT ON COLUMN public.goodstag.registtime IS '登録日時';
COMMENT ON COLUMN public.goodstag.updatetime IS '更新日時';

-- CREATE SEQUENCES

-- public.iconseq definition

-- DROP SEQUENCE public.iconseq;
DROP SEQUENCE IF EXISTS iconseq;
-- CREATE iconseq
CREATE SEQUENCE public.iconseq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999
    START 10000000
	CACHE 1
	NO CYCLE;

-- public.goodsseq definition

-- DROP SEQUENCE public.goodsseq;
DROP SEQUENCE IF EXISTS goodsseq;
-- CREATE goodsseq
CREATE SEQUENCE public.goodsseq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999
    START 10000000
	CACHE 1
	NO CYCLE;

-- public.goodsgroupseq definition

-- DROP SEQUENCE public.goodsgroupseq;
DROP SEQUENCE IF EXISTS goodsgroupseq;
-- CREATE goodsgroupseq
CREATE SEQUENCE public.goodsgroupseq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999
    START 10000000
	CACHE 1
	NO CYCLE;

-- public.categoryseq definition

-- DROP SEQUENCE public.categoryseq;
DROP SEQUENCE IF EXISTS categoryseq;
-- CREATE categoryseq
CREATE SEQUENCE public.categoryseq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999
    START 10000000
	CACHE 1
	NO CYCLE;

-- DROP SEQUENCE public.categoryseq;
DROP SEQUENCE IF EXISTS optionid_seq;
-- ----------------------------
-- Sequence structure for optionid
-- ----------------------------
CREATE SEQUENCE "public"."optionid_seq"
    INCREMENT 1
MINVALUE 1
MAXVALUE 9999
START 1000
CACHE 1;