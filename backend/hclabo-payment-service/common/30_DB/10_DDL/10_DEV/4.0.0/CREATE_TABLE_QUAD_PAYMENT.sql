-- DROP TABLE
DROP TABLE IF EXISTS billingslip CASCADE;
DROP TABLE IF EXISTS orderpayment CASCADE;
DROP TABLE IF EXISTS billingslipforrevision CASCADE;
DROP TABLE IF EXISTS orderpaymentforrevision CASCADE;
DROP TABLE IF EXISTS settlementmethod CASCADE;
DROP TABLE IF EXISTS settlementmethodpricecommission CASCADE;
DROP TABLE IF EXISTS convenience CASCADE;
DROP TABLE IF EXISTS cardbrand CASCADE;
DROP TABLE IF EXISTS mulpaybill CASCADE;
DROP TABLE IF EXISTS mulpayresult CASCADE;
DROP TABLE IF EXISTS mulpaysite CASCADE;
DROP TABLE IF EXISTS mulpayshop CASCADE;
DROP TABLE IF EXISTS creditlinereport CASCADE;
DROP TABLE IF EXISTS settlementmethodlink CASCADE;

-- CREATE TABLE
-- public.billingslip definition
CREATE TABLE public.billingslip (
                                    billingslipid text NOT NULL, -- 請求伝票ID
                                    billingstatus text NULL, -- 請求ステータス
                                    billedprice float8 NULL, -- 請求済金額
                                    billingtype text NULL, -- 請求種別
                                    moneyreceipttime timestamp NULL, -- 入金日時
                                    moneyreceiptamounttotal float8 NULL, -- 累計入金額
                                    registdate timestamp NULL, -- 登録日時
                                    transactionid text NULL, -- 取引ID
                                    billingaddressid text NULL, -- 請求先住所ID
                                    CONSTRAINT billingslip_pk PRIMARY KEY (billingslipid)
);
COMMENT ON TABLE public.billingslip IS '請求伝票';
-- Column comments

COMMENT ON COLUMN public.billingslip.billingslipid IS '請求伝票ID';
COMMENT ON COLUMN public.billingslip.billingstatus IS '請求ステータス';
COMMENT ON COLUMN public.billingslip.billedprice IS '請求済金額';
COMMENT ON COLUMN public.billingslip.billingtype IS '請求種別';
COMMENT ON COLUMN public.billingslip.moneyreceipttime IS '入金日時';
COMMENT ON COLUMN public.billingslip.moneyreceiptamounttotal IS '累計入金額';
COMMENT ON COLUMN public.billingslip.registdate IS '登録日時';
COMMENT ON COLUMN public.billingslip.transactionid IS '取引ID';
COMMENT ON COLUMN public.billingslip.billingaddressid IS '請求先住所ID';

-- public.orderpayment definition
CREATE TABLE public.orderpayment (
                                     orderpaymentid text NOT NULL, -- 注文決済ID
                                     orderpaymentstatus text NULL, -- 注文決済ステータス
                                     settlementmethodtype text NULL, -- 決済種別
                                     paymentmethodid text NULL, -- 決済方法ID(マスタ)
                                     paymentmethodname text NULL, -- 決済方法名(マスタ)
                                     ordercode text NULL, -- 受注番号
                                     paymentlimitdate timestamp NULL, -- 入金期限日時
                                     cancelabledate timestamp NULL, -- 取消可能日時
                                     expirationyear text NULL, -- 有効期限(年) ※後ろ2桁
                                     expirationmonth text NULL, -- 有効期限(月) ※2桁
                                     paymenttoken text NULL, -- 決済トークン
                                     maskedcardno text NULL, -- マスク済みカード番号
                                     paymenttype text NULL, -- 支払区分（1：一括, 2：分割, 5：リボ）
                                     dividednumber text NULL, -- 分割回数
                                     enable3dsecureflag bool NULL, -- 3Dセキュア有効フラグ
                                     registcardflag bool NULL, -- カード保存フラグ（保存時true）
                                     useregistedcardflag bool NULL, -- 登録済カード使用フラグ（登録済みtrue）
                                     authlimitdate timestamp NULL, -- オーソリ期限日時
                                     gmoreleaseflag bool NULL, -- GMO連携解除フラグ（解除時true）
                                     billingslipid text NULL, -- 請求伝票ID
                                     gmoPaymentCancelStatus text NULL, -- GMO決済キャンセル状態
                                     paymethod varchar(32) NULL, -- 決済手段識別子
                                     paytype varchar(32) NULL, -- 決済方法(GMO)
                                     linkPaymentType varchar(32) NULL, -- リンク決済タイプ
                                     paytypename varchar(60) NULL, -- 決済手段名
                                     cancellimit  timestamp(0) NULL, -- GMOキャンセル期限日
                                     laterdatelimit  timestamp(0) NULL, -- GMO後日払い支払期限日時
                                     CONSTRAINT orderpayment_pk PRIMARY KEY (orderpaymentid)
);
COMMENT ON TABLE public.orderpayment IS '注文決済';
-- Column comments

COMMENT ON COLUMN public.orderpayment.orderpaymentid IS '注文決済ID';
COMMENT ON COLUMN public.orderpayment.orderpaymentstatus IS '注文決済ステータス';
COMMENT ON COLUMN public.orderpayment.settlementmethodtype IS '決済種別';
COMMENT ON COLUMN public.orderpayment.paymentmethodid IS '決済方法ID(マスタ)';
COMMENT ON COLUMN public.orderpayment.paymentmethodname IS '決済方法名(マスタ)';
COMMENT ON COLUMN public.orderpayment.ordercode IS '受注番号';
COMMENT ON COLUMN public.orderpayment.paymentlimitdate IS '入金期限日時';
COMMENT ON COLUMN public.orderpayment.cancelabledate IS '取消可能日時';
COMMENT ON COLUMN public.orderpayment.expirationyear IS '有効期限(年) ※後ろ2桁';
COMMENT ON COLUMN public.orderpayment.expirationmonth IS '有効期限(月) ※2桁';
COMMENT ON COLUMN public.orderpayment.paymenttoken IS '決済トークン';
COMMENT ON COLUMN public.orderpayment.maskedcardno IS 'マスク済みカード番号';
COMMENT ON COLUMN public.orderpayment.paymenttype IS '支払区分（1：一括, 2：分割, 5：リボ）';
COMMENT ON COLUMN public.orderpayment.dividednumber IS '分割回数';
COMMENT ON COLUMN public.orderpayment.enable3dsecureflag IS '3Dセキュア有効フラグ';
COMMENT ON COLUMN public.orderpayment.registcardflag IS 'カード保存フラグ（保存時true）';
COMMENT ON COLUMN public.orderpayment.useregistedcardflag IS '登録済カード使用フラグ（登録済みtrue）';
COMMENT ON COLUMN public.orderpayment.authlimitdate IS 'オーソリ期限日時';
COMMENT ON COLUMN public.orderpayment.gmoreleaseflag IS 'GMO連携解除フラグ（解除時true）';
COMMENT ON COLUMN public.orderpayment.billingslipid IS '請求伝票ID';
COMMENT ON COLUMN public.orderpayment.gmoPaymentCancelStatus IS 'GMO決済キャンセル状態';
COMMENT ON COLUMN public.orderpayment.paymethod IS '決済手段識別子';
COMMENT ON COLUMN public.orderpayment.paytype IS '決済方法(GMO)';
COMMENT ON COLUMN public.orderpayment.linkPaymentType IS 'リンク決済タイプ';
COMMENT ON COLUMN public.orderpayment.paytypename IS '決済手段名';
COMMENT ON COLUMN public.orderpayment.cancellimit IS 'GMO即日払いキャンセル期限日';
COMMENT ON COLUMN public.orderpayment.laterdatelimit IS 'GMO後日払い支払期限日時';

-- public.billingslipforrevision definition
CREATE TABLE public.billingslipforrevision (
                                               billingsliprevisionid text NOT NULL, -- 改訂用請求伝票ID
                                               transactionrevisionid text NULL, -- 改訂用取引ID
                                               orderpaymentrevisionid text NULL, -- 注文決済ID
                                               billingslipid text NOT NULL, -- 請求伝票ID
                                               billingstatus text NULL, -- 請求ステータス
                                               billedprice float8 NULL, -- 請求済金額
                                               billingtype text NULL, -- 請求種別
                                               moneyreceipttime timestamp NULL, -- 入金日時
                                               moneyreceiptamounttotal float8 NULL, -- 累計入金額
                                               registdate timestamp NULL, -- 登録日時
                                               transactionid text NULL, -- 取引ID
                                               billingaddressid text NULL, -- 請求先住所ID
                                               CONSTRAINT billingslipforrevision_pk PRIMARY KEY (billingsliprevisionid)
);
COMMENT ON TABLE public.billingslipforrevision IS '改訂用請求伝票';
-- Column comments

COMMENT ON COLUMN public.billingslipforrevision.billingsliprevisionid IS '改訂用請求伝票ID';
COMMENT ON COLUMN public.billingslipforrevision.transactionrevisionid IS '改訂用取引ID';
COMMENT ON COLUMN public.billingslipforrevision.orderpaymentrevisionid IS '注文決済ID';
COMMENT ON COLUMN public.billingslipforrevision.billingslipid IS '請求伝票ID';
COMMENT ON COLUMN public.billingslipforrevision.billingstatus IS '請求ステータス';
COMMENT ON COLUMN public.billingslipforrevision.billedprice IS '請求済金額';
COMMENT ON COLUMN public.billingslipforrevision.billingtype IS '請求種別';
COMMENT ON COLUMN public.billingslipforrevision.moneyreceipttime IS '入金日時';
COMMENT ON COLUMN public.billingslipforrevision.moneyreceiptamounttotal IS '累計入金額';
COMMENT ON COLUMN public.billingslipforrevision.registdate IS '登録日時';
COMMENT ON COLUMN public.billingslipforrevision.transactionid IS '取引ID';
COMMENT ON COLUMN public.billingslipforrevision.billingaddressid IS '請求先住所ID';

-- public.orderpaymentforrevision definition
CREATE TABLE public.orderpaymentforrevision (
                                                orderpaymentrevisionid text NOT NULL, -- 改訂用注文決済ID
                                                orderpaymentid text NOT NULL, -- 注文決済ID
                                                orderpaymentstatus text NULL, -- 注文決済ステータス
                                                settlementmethodtype text NULL, -- 決済種別
                                                paymentmethodid text NULL, -- 決済方法ID(マスタ)
                                                paymentmethodname text NULL, -- 決済方法名(マスタ)
                                                ordercode text NULL, -- 受注番号
                                                paymentlimitdate timestamp NULL, -- 入金期限日時
                                                cancelabledate timestamp NULL, -- 取消可能日時
                                                expirationyear text NULL, -- 有効期限(年) ※後ろ2桁
                                                expirationmonth text NULL, -- 有効期限(月) ※2桁
                                                paymenttoken text NULL, -- 決済トークン
                                                maskedcardno text NULL, -- マスク済みカード番号
                                                paymenttype text NULL, -- 支払区分（1：一括, 2：分割, 5：リボ）
                                                dividednumber text NULL, -- 分割回数
                                                enable3dsecureflag bool NULL, -- 3Dセキュア有効フラグ
                                                registcardflag bool NULL, -- カード保存フラグ（保存時true）
                                                useregistedcardflag bool NULL, -- 登録済カード使用フラグ（登録済みtrue）
                                                authlimitdate timestamp NULL, -- オーソリ期限日時
                                                gmoreleaseflag bool NULL, -- GMO連携解除フラグ（解除時true）
                                                billingsliprevisionid text NULL, -- 改訂用請求伝票ID
                                                gmoPaymentCancelStatus text NULL, -- GMO決済キャンセル状態
                                                paymethod varchar(32) NULL, -- 決済手段識別子
                                                paytype varchar(32) NULL, -- 決済方法(GMO)
                                                linkPaymentType varchar(32) NULL, -- リンク決済タイプ
                                                paytypename varchar(60) NULL, -- 決済手段名
                                                cancellimit  timestamp(0) NULL, -- GMOキャンセル期限日
                                                laterdatelimit  timestamp(0) NULL, -- GMO後日払い支払期限日時
                                                CONSTRAINT orderpaymentforrevision_pk PRIMARY KEY (orderpaymentrevisionid)
);
COMMENT ON TABLE public.orderpaymentforrevision IS '改訂用注文決済';
-- Column comments

COMMENT ON COLUMN public.orderpaymentforrevision.orderpaymentrevisionid IS '改訂用注文決済ID';
COMMENT ON COLUMN public.orderpaymentforrevision.orderpaymentid IS '注文決済ID';
COMMENT ON COLUMN public.orderpaymentforrevision.orderpaymentstatus IS '注文決済ステータス';
COMMENT ON COLUMN public.orderpaymentforrevision.settlementmethodtype IS '決済種別';
COMMENT ON COLUMN public.orderpaymentforrevision.paymentmethodid IS '決済方法ID(マスタ)';
COMMENT ON COLUMN public.orderpaymentforrevision.paymentmethodname IS '決済方法名(マスタ)';
COMMENT ON COLUMN public.orderpaymentforrevision.ordercode IS '受注番号';
COMMENT ON COLUMN public.orderpaymentforrevision.paymentlimitdate IS '入金期限日時';
COMMENT ON COLUMN public.orderpaymentforrevision.cancelabledate IS '取消可能日時';
COMMENT ON COLUMN public.orderpaymentforrevision.expirationyear IS '有効期限(年) ※後ろ2桁';
COMMENT ON COLUMN public.orderpaymentforrevision.expirationmonth IS '有効期限(月) ※2桁';
COMMENT ON COLUMN public.orderpaymentforrevision.paymenttoken IS '決済トークン';
COMMENT ON COLUMN public.orderpaymentforrevision.maskedcardno IS 'マスク済みカード番号';
COMMENT ON COLUMN public.orderpaymentforrevision.paymenttype IS '支払区分（1：一括, 2：分割, 5：リボ）';
COMMENT ON COLUMN public.orderpaymentforrevision.dividednumber IS '分割回数';
COMMENT ON COLUMN public.orderpaymentforrevision.enable3dsecureflag IS '3Dセキュア有効フラグ';
COMMENT ON COLUMN public.orderpaymentforrevision.registcardflag IS 'カード保存フラグ（保存時true）';
COMMENT ON COLUMN public.orderpaymentforrevision.useregistedcardflag IS '登録済カード使用フラグ（登録済みtrue）';
COMMENT ON COLUMN public.orderpaymentforrevision.authlimitdate IS 'オーソリ期限日時';
COMMENT ON COLUMN public.orderpaymentforrevision.gmoreleaseflag IS 'GMO連携解除フラグ（解除時true）';
COMMENT ON COLUMN public.orderpaymentforrevision.billingsliprevisionid IS '改訂用請求伝票ID';
COMMENT ON COLUMN public.orderpaymentforrevision.gmoPaymentCancelStatus IS 'GMO決済キャンセル状態';
COMMENT ON COLUMN public.orderpaymentforrevision.paymethod IS '決済手段識別子';
COMMENT ON COLUMN public.orderpaymentforrevision.paytype IS '決済方法(GMO)';
COMMENT ON COLUMN public.orderpaymentforrevision.linkPaymentType IS 'リンク決済タイプ';
COMMENT ON COLUMN public.orderpaymentforrevision.paytypename IS '決済手段名';
COMMENT ON COLUMN public.orderpaymentforrevision.cancellimit IS 'GMOキャンセル期限日';
COMMENT ON COLUMN public.orderpaymentforrevision.laterdatelimit IS 'GMOキャンセル期限日';

-- public.settlementmethod definition
CREATE TABLE public.settlementmethod (
                                         settlementmethodseq numeric(4) NOT NULL, -- 決済方法SEQ
                                         shopseq numeric(4) NOT NULL, -- ショップSEQ
                                         settlementmethodname varchar(60) NOT NULL, -- 決済方法名
                                         settlementmethoddisplaynamepc varchar(60) NULL, -- 決済方法表示名PC
                                         settlementmethoddisplaynamemb varchar(60) NULL, -- 決済方法表示名携帯
                                         openstatuspc varchar(1) NOT NULL DEFAULT 0, -- 公開状態PC
                                         openstatusmb varchar(1) NULL DEFAULT 0, -- 公開状態携帯
                                         settlementnotepc varchar(2000) NULL, -- 決済方法説明文PC
                                         settlementnotemb varchar(2000) NULL, -- 決済方法説明文携帯
                                         settlementmethodtype varchar(2) NOT NULL, -- 決済方法種別
                                         settlementmethodcommissiontype varchar(1) NOT NULL, -- 決済方法手数料種別
                                         billtype varchar(1) NOT NULL, -- 請求種別
                                         deliverymethodseq numeric(4) NULL, -- 配送方法SEQ
                                         equalscommission numeric(8) NULL DEFAULT 0, -- 一律手数料
                                         settlementmethodpricecommissionflag varchar(1) NOT NULL, -- 決済方法金額別手数料フラグ
                                         largeamountdiscountprice numeric(8) NULL DEFAULT 0, -- 高額割引下限金額
                                         largeamountdiscountcommission numeric(8) NULL DEFAULT 0, -- 高額割引手数料
                                         orderdisplay numeric(4) NULL, -- 表示順
                                         maxpurchasedprice numeric(8) NULL DEFAULT 0, -- 最大購入金額
                                         minpurchasedprice numeric(8) NULL DEFAULT 0, -- 最小購入金額
                                         settlementmailrequired bpchar(1) NOT NULL, -- 決済関連メール要否フラグ
                                         enablecardnoholding bpchar(1) NOT NULL DEFAULT 0, -- クレジットカード登録機能有効化フラグ
                                         enablesecuritycode bpchar(1) NOT NULL DEFAULT 0, -- クレジットセキュリティコード有効化フラグ（予約項目。現在未使用）
                                         enable3dsecure bpchar(1) NOT NULL DEFAULT 0, -- クレジット3Dセキュア有効化フラグ
                                         enableinstallment bpchar(1) NOT NULL DEFAULT 0, -- クレジット分割支払有効化フラグ
                                         enablebonussinglepayment bpchar(1) NOT NULL DEFAULT 0, -- クレジットボーナス一括支払有効化フラグ（予約項目。現在未使用）
                                         enablebonusinstallment bpchar(1) NOT NULL DEFAULT 0, -- クレジットボーナス分割支払有効化フラグ（予約項目。現在未使用）
                                         enablerevolving bpchar(1) NOT NULL DEFAULT 0, -- クレジットリボ有効化フラグ
                                         registtime timestamp(0) NOT NULL, -- 登録日時
                                         updatetime timestamp(0) NOT NULL, -- 更新日時
                                         CONSTRAINT settlementmethod_pkey PRIMARY KEY (settlementmethodseq)
);
COMMENT ON TABLE public.settlementmethod IS '決済方法';

-- Column comments

COMMENT ON COLUMN public.settlementmethod.settlementmethodseq IS '決済方法SEQ';
COMMENT ON COLUMN public.settlementmethod.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.settlementmethod.settlementmethodname IS '決済方法名';
COMMENT ON COLUMN public.settlementmethod.settlementmethoddisplaynamepc IS '決済方法表示名PC';
COMMENT ON COLUMN public.settlementmethod.settlementmethoddisplaynamemb IS '決済方法表示名携帯';
COMMENT ON COLUMN public.settlementmethod.openstatuspc IS '公開状態PC';
COMMENT ON COLUMN public.settlementmethod.openstatusmb IS '公開状態携帯';
COMMENT ON COLUMN public.settlementmethod.settlementnotepc IS '決済方法説明文PC';
COMMENT ON COLUMN public.settlementmethod.settlementnotemb IS '決済方法説明文携帯';
COMMENT ON COLUMN public.settlementmethod.settlementmethodtype IS '決済方法種別';
COMMENT ON COLUMN public.settlementmethod.settlementmethodcommissiontype IS '決済方法手数料種別';
COMMENT ON COLUMN public.settlementmethod.billtype IS '請求種別';
COMMENT ON COLUMN public.settlementmethod.deliverymethodseq IS '配送方法SEQ';
COMMENT ON COLUMN public.settlementmethod.equalscommission IS '一律手数料';
COMMENT ON COLUMN public.settlementmethod.settlementmethodpricecommissionflag IS '決済方法金額別手数料フラグ';
COMMENT ON COLUMN public.settlementmethod.largeamountdiscountprice IS '高額割引下限金額';
COMMENT ON COLUMN public.settlementmethod.largeamountdiscountcommission IS '高額割引手数料';
COMMENT ON COLUMN public.settlementmethod.orderdisplay IS '表示順';
COMMENT ON COLUMN public.settlementmethod.maxpurchasedprice IS '最大購入金額';
COMMENT ON COLUMN public.settlementmethod.minpurchasedprice IS '最小購入金額';
COMMENT ON COLUMN public.settlementmethod.settlementmailrequired IS '決済関連メール要否フラグ';
COMMENT ON COLUMN public.settlementmethod.enablecardnoholding IS 'クレジットカード登録機能有効化フラグ';
COMMENT ON COLUMN public.settlementmethod.enablesecuritycode IS 'クレジットセキュリティコード有効化フラグ（予約項目。現在未使用）';
COMMENT ON COLUMN public.settlementmethod.enable3dsecure IS 'クレジット3Dセキュア有効化フラグ';
COMMENT ON COLUMN public.settlementmethod.enableinstallment IS 'クレジット分割支払有効化フラグ';
COMMENT ON COLUMN public.settlementmethod.enablebonussinglepayment IS 'クレジットボーナス一括支払有効化フラグ（予約項目。現在未使用）';
COMMENT ON COLUMN public.settlementmethod.enablebonusinstallment IS 'クレジットボーナス分割支払有効化フラグ（予約項目。現在未使用）';
COMMENT ON COLUMN public.settlementmethod.enablerevolving IS 'クレジットリボ有効化フラグ';
COMMENT ON COLUMN public.settlementmethod.registtime IS '登録日時';
COMMENT ON COLUMN public.settlementmethod.updatetime IS '更新日時';

-- public.settlementmethodpricecommission definition
CREATE TABLE public.settlementmethodpricecommission (
                                                        settlementmethodseq numeric(4) NOT NULL, -- 決済方法SEQ
                                                        maxprice numeric(8) NOT NULL DEFAULT 0, -- 上限金額
                                                        commission numeric(8) NULL DEFAULT 0, -- 手数料
                                                        registtime timestamp(0) NOT NULL, -- 登録日時
                                                        updatetime timestamp(0) NOT NULL, -- 更新日時
                                                        CONSTRAINT settlementmethodpricecommission_pkey PRIMARY KEY (settlementmethodseq, maxprice)
);
COMMENT ON TABLE public.settlementmethodpricecommission IS '決済方法金額別手数料';

-- Column comments

COMMENT ON COLUMN public.settlementmethodpricecommission.settlementmethodseq IS '決済方法SEQ';
COMMENT ON COLUMN public.settlementmethodpricecommission.maxprice IS '上限金額';
COMMENT ON COLUMN public.settlementmethodpricecommission.commission IS '手数料';
COMMENT ON COLUMN public.settlementmethodpricecommission.registtime IS '登録日時';
COMMENT ON COLUMN public.settlementmethodpricecommission.updatetime IS '更新日時';

-- public.convenience definition
CREATE TABLE public.convenience (
                                    conveniseq numeric(2) NOT NULL, -- コンビニSEQ
                                    paycode varchar(3) NOT NULL, -- 支払コード
                                    convenicode varchar(5) NOT NULL, -- コンビニコード
                                    payname varchar(100) NOT NULL, -- 支払名称
                                    conveniname varchar(100) NOT NULL, -- コンビニ名称
                                    shopseq numeric(4) NOT NULL, -- ショップSEQ
                                    useflag varchar(1) NOT NULL DEFAULT 1, -- コンビニ利用フラグ(0:利用しない,1:利用する)
                                    displayorder numeric(4) NOT NULL DEFAULT 1, -- コンビニ表示順
                                    registtime timestamp(0) NOT NULL, -- 登録日時
                                    updatetime timestamp(0) NOT NULL, -- 更新日時
                                    CONSTRAINT convenience_pkey PRIMARY KEY (conveniseq)
);
COMMENT ON TABLE public.convenience IS 'コンビニ名称';

-- Column comments

COMMENT ON COLUMN public.convenience.conveniseq IS 'コンビニSEQ';
COMMENT ON COLUMN public.convenience.paycode IS '支払コード';
COMMENT ON COLUMN public.convenience.convenicode IS 'コンビニコード';
COMMENT ON COLUMN public.convenience.payname IS '支払名称';
COMMENT ON COLUMN public.convenience.conveniname IS 'コンビニ名称';
COMMENT ON COLUMN public.convenience.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.convenience.useflag IS 'コンビニ利用フラグ(0:利用しない,1:利用する)';
COMMENT ON COLUMN public.convenience.displayorder IS 'コンビニ表示順';
COMMENT ON COLUMN public.convenience.registtime IS '登録日時';
COMMENT ON COLUMN public.convenience.updatetime IS '更新日時';

-- public.cardbrand definition
CREATE TABLE public.cardbrand (
                                  cardbrandseq numeric(3) NOT NULL, -- カードブランドSEQ
                                  cardbrandcode text NOT NULL, -- クレジットカード会社コード
                                  cardbrandname varchar(32) NOT NULL, -- カードブランド名
                                  cardbranddisplaypc varchar(32) NOT NULL, -- カードブランド表示名PC
                                  cardbranddisplaymb varchar(32) NULL, -- カードブランド表示名携帯
                                  orderdisplay numeric(3) NULL DEFAULT 0, -- 表示順
                                  installment bpchar(1) NOT NULL DEFAULT 0, -- 分割支払契約（予約項目。現在未使用）
                                  bounussingle bpchar(1) NOT NULL DEFAULT 0, -- ボーナス一括契約（予約項目。現在未使用）
                                  bounusinstallment bpchar(1) NOT NULL DEFAULT 0, -- ボーナス分割契約（予約項目。現在未使用）
                                  revolving bpchar(1) NOT NULL DEFAULT 0, -- リボ契約（予約項目。現在未使用）
                                  installmentcounts varchar(64) NULL, -- 選択可能分割回数
                                  frontdisplayflag bpchar(1) NOT NULL DEFAULT 1, -- Front表示フラグ
                                  CONSTRAINT cardbrand_cardbrandcode_key UNIQUE (cardbrandcode),
                                  CONSTRAINT cardbrand_pkey PRIMARY KEY (cardbrandseq)
);
COMMENT ON TABLE public.cardbrand IS 'カードブランド';

-- Column comments

COMMENT ON COLUMN public.cardbrand.cardbrandseq IS 'カードブランドSEQ';
COMMENT ON COLUMN public.cardbrand.cardbrandcode IS 'クレジットカード会社コード';
COMMENT ON COLUMN public.cardbrand.cardbrandname IS 'カードブランド名';
COMMENT ON COLUMN public.cardbrand.cardbranddisplaypc IS 'カードブランド表示名PC';
COMMENT ON COLUMN public.cardbrand.cardbranddisplaymb IS 'カードブランド表示名携帯';
COMMENT ON COLUMN public.cardbrand.orderdisplay IS '表示順';
COMMENT ON COLUMN public.cardbrand.installment IS '分割支払契約（予約項目。現在未使用）';
COMMENT ON COLUMN public.cardbrand.bounussingle IS 'ボーナス一括契約（予約項目。現在未使用）';
COMMENT ON COLUMN public.cardbrand.bounusinstallment IS 'ボーナス分割契約（予約項目。現在未使用）';
COMMENT ON COLUMN public.cardbrand.revolving IS 'リボ契約（予約項目。現在未使用）';
COMMENT ON COLUMN public.cardbrand.installmentcounts IS '選択可能分割回数';
COMMENT ON COLUMN public.cardbrand.frontdisplayflag IS 'Front表示フラグ';

-- public.mulpaybill definition
CREATE TABLE public.mulpaybill (
                                   mulpaybillseq numeric(8) NOT NULL, -- マルチペイメント請求SEQ
                                   paytype varchar(2) NOT NULL, -- 決済方法
                                   paymethod varchar(32) NULL, -- 決済手段識別子
                                   trantype varchar(32) NULL, -- トランザクション種別
                                   orderpaymentid text NULL, -- HM注文決済ID
                                   orderid varchar(27) NULL, -- オーダーID
                                   accessid varchar(32) NULL, -- 取引ID
                                   accesspass varchar(32) NULL, -- 取引パスワード
                                   "result" varchar(32) NULL, --リンクタイプPlus処理結果
                                   jobcd varchar(32) NULL, -- 処理区分
                                   "method" varchar(1) NULL, -- 支払方法
                                   paytimes numeric(2) NULL, -- 支払回数
                                   seqmode varchar(1) NULL, -- カード登録連番モード
                                   cardseq numeric(4) NULL, -- カード登録連番
                                   amount numeric(7) NULL, -- 利用金額
                                   tax numeric(7) NULL, -- 税送料
                                   tdflag varchar(1) NULL, -- 3Dセキュア使用フラグ
                                   acs varchar(1) NULL, -- ACS 呼出判定
                                   "forward" text NULL, -- クレジットカード会社コード
                                   approve varchar(7) NULL, -- 承認番号
                                   tranid varchar(28) NULL, -- トランザクション ID
                                   trandate varchar(14) NULL, -- 決済日付
                                   convenience varchar(5) NULL, -- 支払先コンビニコード
                                   confno varchar(20) NULL, -- 確認番号
                                   receiptno varchar(32) NULL, -- 受付番
                                   paymentterm varchar(14) NULL, -- 支払期限日時
                                   custid varchar(11) NULL, -- お客様番号
                                   bkcode varchar(5) NULL, -- 収納機関番号
                                   encryptreceiptno varchar(128) NULL, -- 暗号化決済番号
                                   expiredate varchar(8) NULL, -- 取引有効期限
                                   tradereason varchar(64) NULL,-- 振込事由
                                   tradeclientname varchar(64) NULL, -- 振込依頼者氏名
                                   tradeclientmailaddress varchar(256) NULL, -- 振込依頼者メールアドレス
                                   bankcode varchar(4) NULL, -- 銀行コード
                                   bankname varchar(45) NULL, -- 銀行名
                                   branchcode varchar(3) NULL, -- 支店コード
                                   branchname varchar(45) NULL, -- 支店名
                                   accounttype varchar(1) NULL, -- 振込先口座種別
                                   accountnumber varchar(7) NULL, -- 振込先口座番号
                                   errinfo text NULL, -- エラー情報
                                   errcode text NULL, -- エラーコード
                                   paymenturl varchar(300) NULL, -- 金融機関選択画面URL
                                   registtime timestamp(0) NOT NULL, -- 登録日時
                                   updatetime timestamp(0) NOT NULL, -- 更新日時
                                   CONSTRAINT mulpaybill_pkey PRIMARY KEY (mulpaybillseq)
);
CREATE INDEX idx_mulpaybill_accessid ON public.mulpaybill USING btree (accessid);
CREATE INDEX idx_mulpaybill_orderid ON public.mulpaybill USING btree (orderid);
COMMENT ON TABLE public.mulpaybill IS 'マルチペイメント請求';

-- Column comments

COMMENT ON COLUMN public.mulpaybill.mulpaybillseq IS 'マルチペイメント請求SEQ';
COMMENT ON COLUMN public.mulpaybill.paytype IS '決済方法';
COMMENT ON COLUMN public.mulpaybill.paymethod IS '決済手段識別子';
COMMENT ON COLUMN public.mulpaybill.trantype IS 'トランザクション種別';
COMMENT ON COLUMN public.mulpaybill.orderpaymentid IS 'HM注文決済ID';
COMMENT ON COLUMN public.mulpaybill.orderid IS 'オーダーID';
COMMENT ON COLUMN public.mulpaybill.accessid IS '取引ID';
COMMENT ON COLUMN public.mulpaybill.accesspass IS '取引パスワード';
COMMENT ON COLUMN public.mulpaybill."result" IS 'リンクタイプPlus処理結果';
COMMENT ON COLUMN public.mulpaybill.jobcd IS '処理区分';
COMMENT ON COLUMN public.mulpaybill."method" IS 'カード支払方法';
COMMENT ON COLUMN public.mulpaybill.paytimes IS 'カード支払回数';
COMMENT ON COLUMN public.mulpaybill.seqmode IS 'カード登録連番モード';
COMMENT ON COLUMN public.mulpaybill.cardseq IS 'カード登録連番';
COMMENT ON COLUMN public.mulpaybill.amount IS '利用金額';
COMMENT ON COLUMN public.mulpaybill.tax IS '税送料';
COMMENT ON COLUMN public.mulpaybill.tdflag IS '3Dセキュア使用フラグ';
COMMENT ON COLUMN public.mulpaybill.acs IS 'ACS 呼出判定';
COMMENT ON COLUMN public.mulpaybill."forward" IS 'クレジットカード会社コード';
COMMENT ON COLUMN public.mulpaybill.approve IS '承認番号';
COMMENT ON COLUMN public.mulpaybill.tranid IS 'トランザクション ID';
COMMENT ON COLUMN public.mulpaybill.trandate IS '決済日付';
COMMENT ON COLUMN public.mulpaybill.convenience IS '支払先コンビニコード';
COMMENT ON COLUMN public.mulpaybill.confno IS '確認番号';
COMMENT ON COLUMN public.mulpaybill.receiptno IS '受付番';
COMMENT ON COLUMN public.mulpaybill.paymentterm IS '支払期限日時';
COMMENT ON COLUMN public.mulpaybill.custid IS 'お客様番号';
COMMENT ON COLUMN public.mulpaybill.bkcode IS '収納機関番号';
COMMENT ON COLUMN public.mulpaybill.encryptreceiptno IS '暗号化決済番号';
COMMENT ON COLUMN public.mulpaybill.expiredate IS '取引有効期限';
COMMENT ON COLUMN public.mulpaybill.tradereason IS '振込事由';
COMMENT ON COLUMN public.mulpaybill.tradeclientname IS '振込依頼者氏名';
COMMENT ON COLUMN public.mulpaybill.tradeclientmailaddress IS '振込依頼者メールアドレス';
COMMENT ON COLUMN public.mulpaybill.bankcode IS '銀行コード';
COMMENT ON COLUMN public.mulpaybill.bankname IS '銀行名';
COMMENT ON COLUMN public.mulpaybill.branchcode IS '支店コード';
COMMENT ON COLUMN public.mulpaybill.branchname IS '支店名';
COMMENT ON COLUMN public.mulpaybill.accounttype IS '振込先口座種別';
COMMENT ON COLUMN public.mulpaybill.accountnumber IS '振込先口座番号';
COMMENT ON COLUMN public.mulpaybill.errinfo IS 'エラー情報';
COMMENT ON COLUMN public.mulpaybill.errcode IS 'エラーコード';
COMMENT ON COLUMN public.mulpaybill.paymenturl IS 'ペイジー金融機関選択画面URL';
COMMENT ON COLUMN public.mulpaybill.registtime IS '登録日時';
COMMENT ON COLUMN public.mulpaybill.updatetime IS '更新日時';

-- public.mulpayresult definition
CREATE TABLE public.mulpayresult (
                                     mulpayresultseq numeric(8) NOT NULL, -- マルチペイメント決済結果SEQ
                                     receivemode varchar(10) NOT NULL, -- 受信方法
                                     processedflag varchar(1) NOT NULL DEFAULT 0, -- 入金処理済みフラグ
                                     orderpaymentid text NULL, -- HM注文決済ID
                                     shopseq numeric(4) NOT NULL, -- ショップSEQ
                                     orderid varchar(27) NOT NULL, -- オーダーID
                                     status varchar(32) NULL, -- 現状態
                                     jobcd varchar(32) NULL, -- 処理区分
                                     processdate varchar(14) NULL, -- 処理日時
                                     itemcode varchar(7) NULL, -- 商品コード
                                     amount numeric(7) NULL, -- 利用金額
                                     tax numeric(7) NULL, -- 税送料
                                     siteid varchar(13) NULL, -- サイトID
                                     memberid varchar(60) NULL, -- 会員ID
                                     cardno varchar(16) NULL, -- カード番号（下４桁）
                                     expire varchar(4) NULL, -- カード有効期限
                                     currency varchar(3) NULL, -- 通貨コード
                                     "forward" text NULL, -- クレジットカード会社コード
                                     "method" varchar(1) NULL, -- 支払方法
                                     paytimes numeric(2) NULL, -- 支払回数
                                     tranid varchar(28) NULL, -- トランザクションID
                                     approve varchar(7) NULL, -- 承認番号
                                     trandate varchar(14) NULL, -- 処理日付
                                     errcode text NULL, -- エラーコード
                                     errinfo text NULL, -- エラー詳細コード
                                     clientfield1 varchar(100) NULL, -- 加盟店自由項目1
                                     clientfield2 varchar(100) NULL, -- 加盟店自由項目2
                                     clientfield3 varchar(100) NULL, -- 加盟店自由項目3
                                     paytype varchar(2) NULL, -- 決済方法
                                     paymethod varchar(32) NULL, -- 決済手段識別子
                                     cvscode varchar(5) NULL, -- 支払先コンビニコード
                                     cvsconfno varchar(20) NULL, -- CVS確認番号
                                     cvsreceiptno varchar(32) NULL, -- CVS受付番号
                                     custid varchar(11) NULL, -- お客様番号
                                     bkcode varchar(5) NULL, -- 収納機関番号
                                     confno varchar(20) NULL, -- 確認番号
                                     paymentterm varchar(14) NULL, -- 支払期限日時
                                     encryptreceiptno varchar(128) NULL, -- 暗号化決済番号
                                     finishdate varchar(14) NULL, -- 入金確定日時
                                     receiptdate varchar(14) NULL, -- 受付日時
                                     requestamount numeric(10) NULL, -- 振込依頼金額
                                     expiredate varchar(8) NULL, -- 振込有効期限
                                     tradereason varchar(64) NULL, -- 振込事由
                                     clientname varchar(64) NULL, -- 振込依頼先氏名
                                     clientmailaddress varchar(256) NULL, -- 振込依頼先メールアドレス
                                     bankcode varchar(4) NULL, -- 銀行コード
                                     bankname varchar(45) NULL, -- 銀行名
                                     branchcode varchar(3) NULL, -- 支店コード
                                     branchname varchar(45) NULL, -- 支店名
                                     accounttype varchar(1) NULL, -- 預金種別
                                     accountnumber varchar(7) NULL, -- 口座番号
                                     insettlementdate varchar(8) NULL, -- 勘定日
                                     inamount numeric(13) NULL, -- 入金金額
                                     inclientname varchar(144) NULL, -- 振込依頼人名
                                     ganbprocesstype varchar(15) NULL, -- 処理種別
                                     ganbaccountholdername varchar(40) NULL, -- 口座名義
                                     ganbinremittingbankname varchar(45) NULL, -- 仕向銀行名
                                     ganbinremittingbranchname varchar(45) NULL, -- 仕向支店名
                                     ganbtotaltransferamount numeric(15) NULL, -- 累計入金額
                                     ganbtotaltransfercount numeric(15) NULL, -- 入金回数
                                     registtime timestamp(0) NOT NULL, -- 登録日時
                                     updatetime timestamp(0) NOT NULL, -- 更新日時
                                     CONSTRAINT mulpayresult_pkey PRIMARY KEY (mulpayresultseq)
);
CREATE INDEX idx_mulpayresult_orderid ON public.mulpayresult USING btree (orderid);
COMMENT ON TABLE public.mulpayresult IS 'マルチペイメント決済結果';

-- Column comments

COMMENT ON COLUMN public.mulpayresult.mulpayresultseq IS 'マルチペイメント決済結果SEQ';
COMMENT ON COLUMN public.mulpayresult.receivemode IS '受信方法';
COMMENT ON COLUMN public.mulpayresult.processedflag IS '入金処理済みフラグ';
COMMENT ON COLUMN public.mulpayresult.orderpaymentid IS 'HM注文決済ID';
COMMENT ON COLUMN public.mulpayresult.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.mulpayresult.orderid IS 'オーダーID';
COMMENT ON COLUMN public.mulpayresult.status IS '現状態';
COMMENT ON COLUMN public.mulpayresult.jobcd IS '処理区分';
COMMENT ON COLUMN public.mulpayresult.processdate IS '処理日時';
COMMENT ON COLUMN public.mulpayresult.itemcode IS '商品コード';
COMMENT ON COLUMN public.mulpayresult.amount IS '利用金額';
COMMENT ON COLUMN public.mulpayresult.tax IS '税送料';
COMMENT ON COLUMN public.mulpayresult.siteid IS 'サイトID';
COMMENT ON COLUMN public.mulpayresult.memberid IS '会員ID';
COMMENT ON COLUMN public.mulpayresult.cardno IS 'カード番号（下４桁）';
COMMENT ON COLUMN public.mulpayresult.expire IS 'カード有効期限';
COMMENT ON COLUMN public.mulpayresult.currency IS '通貨コード';
COMMENT ON COLUMN public.mulpayresult."forward" IS 'クレジットカード会社コード';
COMMENT ON COLUMN public.mulpayresult."method" IS '支払方法';
COMMENT ON COLUMN public.mulpayresult.paytimes IS '支払回数';
COMMENT ON COLUMN public.mulpayresult.tranid IS 'トランザクションID';
COMMENT ON COLUMN public.mulpayresult.approve IS '承認番号';
COMMENT ON COLUMN public.mulpayresult.trandate IS '処理日付';
COMMENT ON COLUMN public.mulpayresult.errcode IS 'エラーコード';
COMMENT ON COLUMN public.mulpayresult.errinfo IS 'エラー詳細コード';
COMMENT ON COLUMN public.mulpayresult.clientfield1 IS '加盟店自由項目1';
COMMENT ON COLUMN public.mulpayresult.clientfield2 IS '加盟店自由項目2';
COMMENT ON COLUMN public.mulpayresult.clientfield3 IS '加盟店自由項目3';
COMMENT ON COLUMN public.mulpayresult.paytype IS '決済方法';
COMMENT ON COLUMN public.mulpayresult.paymethod IS '決済手段識別子';
COMMENT ON COLUMN public.mulpayresult.cvscode IS '支払先コンビニコード';
COMMENT ON COLUMN public.mulpayresult.cvsconfno IS 'CVS確認番号';
COMMENT ON COLUMN public.mulpayresult.cvsreceiptno IS 'CVS受付番号';
COMMENT ON COLUMN public.mulpayresult.custid IS 'お客様番号';
COMMENT ON COLUMN public.mulpayresult.bkcode IS '収納機関番号';
COMMENT ON COLUMN public.mulpayresult.confno IS '確認番号';
COMMENT ON COLUMN public.mulpayresult.paymentterm IS '支払期限日時';
COMMENT ON COLUMN public.mulpayresult.encryptreceiptno IS '暗号化決済番号';
COMMENT ON COLUMN public.mulpayresult.finishdate IS '入金確定日時';
COMMENT ON COLUMN public.mulpayresult.receiptdate IS '受付日時';
COMMENT ON COLUMN public.mulpayresult.requestamount IS '振込依頼金額';
COMMENT ON COLUMN public.mulpayresult.expiredate IS '振込有効期限';
COMMENT ON COLUMN public.mulpayresult.tradereason IS '振込事由';
COMMENT ON COLUMN public.mulpayresult.clientname IS '振込依頼先氏名';
COMMENT ON COLUMN public.mulpayresult.clientmailaddress IS '振込依頼先メールアドレス';
COMMENT ON COLUMN public.mulpayresult.bankcode IS '銀行コード';
COMMENT ON COLUMN public.mulpayresult.bankname IS '銀行名';
COMMENT ON COLUMN public.mulpayresult.branchcode IS '支店コード';
COMMENT ON COLUMN public.mulpayresult.branchname IS '支店名';
COMMENT ON COLUMN public.mulpayresult.accounttype IS '預金種別';
COMMENT ON COLUMN public.mulpayresult.accountnumber IS '口座番号';
COMMENT ON COLUMN public.mulpayresult.insettlementdate IS '勘定日';
COMMENT ON COLUMN public.mulpayresult.inamount IS '入金金額';
COMMENT ON COLUMN public.mulpayresult.inclientname IS '振込依頼人名';
COMMENT ON COLUMN public.mulpayresult.ganbprocesstype IS '処理種別';
COMMENT ON COLUMN public.mulpayresult.ganbaccountholdername IS '口座名義';
COMMENT ON COLUMN public.mulpayresult.ganbinremittingbankname IS '仕向銀行名';
COMMENT ON COLUMN public.mulpayresult.ganbinremittingbranchname IS '仕向支店名';
COMMENT ON COLUMN public.mulpayresult.ganbtotaltransferamount IS '累計入金額';
COMMENT ON COLUMN public.mulpayresult.ganbtotaltransfercount IS '入金回数';
COMMENT ON COLUMN public.mulpayresult.registtime IS '登録日時';
COMMENT ON COLUMN public.mulpayresult.updatetime IS '更新日時';

-- public.mulpaysite definition
CREATE TABLE public.mulpaysite (
                                   siteid varchar(32) NOT NULL, -- サイトID
                                   sitepassword varchar(32) NOT NULL, -- サイトパスワード
                                   siteaccessurl varchar(512) NOT NULL, -- アクセスURL
                                   CONSTRAINT mulpaysite_pkey PRIMARY KEY (siteid)
);
COMMENT ON TABLE public.mulpaysite IS 'マルチペイメントサイト設定';

-- Column comments

COMMENT ON COLUMN public.mulpaysite.siteid IS 'サイトID';
COMMENT ON COLUMN public.mulpaysite.sitepassword IS 'サイトパスワード';
COMMENT ON COLUMN public.mulpaysite.siteaccessurl IS 'アクセスURL';

-- public.mulpayshop definition
CREATE TABLE public.mulpayshop (
                                   shopseq numeric(4) NOT NULL, -- ショップSEQ
                                   shopid varchar(23) NOT NULL, -- ショップID
                                   shoppass varchar(32) NOT NULL, -- ショップパスワード
                                   shopaccessurl varchar(512) NULL, -- アクセスURL
                                   tdtenantname varchar(25) NULL, -- 3Dセキュア表示店舗名(BASE64 エンコーディング後の長さが25BYTE 以内の文字列)
                                   httpaccept varchar(256) NULL, -- HTTP_ACCEPT
                                   httpuseragent varchar(256) NULL, -- HTTP_USER_AGENT
                                   clientfield1 varchar(100) NULL, -- 加盟店舗自由項目１
                                   clientfield2 varchar(100) NULL, -- 加盟店舗自由項目２
                                   clientfield3 varchar(100) NULL, -- 加盟店舗自由項目３
                                   clientfieldflag bpchar(1) NOT NULL, -- 加盟店自由項目返却フラグ
                                   shopmailaddress varchar(256) NULL, -- 加盟店メールアドレス
                                   registerdisp1 varchar(32) NOT NULL, -- POS レジ表示欄-ショップ名
                                   registerdisp2 varchar(32) NULL, -- POS レジ表示欄2
                                   registerdisp3 varchar(32) NULL, -- POS レジ表示欄3
                                   registerdisp4 varchar(32) NULL, -- POS レジ表示欄4
                                   registerdisp5 varchar(32) NULL, -- POS レジ表示欄5
                                   registerdisp6 varchar(32) NULL, -- POS レジ表示欄6
                                   registerdisp7 varchar(32) NULL, -- POS レジ表示欄7
                                   registerdisp8 varchar(32) NULL, -- POS レジ表示欄8
                                   receiptsdisp1 varchar(60) NULL, -- レシート表示欄1
                                   receiptsdisp2 varchar(60) NULL, -- レシート表示欄2
                                   receiptsdisp3 varchar(60) NULL, -- レシート表示欄3
                                   receiptsdisp4 varchar(60) NULL, -- レシート表示欄4
                                   receiptsdisp5 varchar(60) NULL, -- レシート表示欄5
                                   receiptsdisp6 varchar(60) NULL, -- レシート表示欄6
                                   receiptsdisp7 varchar(60) NULL, -- レシート表示欄7
                                   receiptsdisp8 varchar(60) NULL, -- レシート表示欄8
                                   receiptsdisp9 varchar(60) NULL, -- レシート表示欄9
                                   receiptsdisp10 varchar(60) NULL, -- レシート表示欄10
                                   receiptsdisp11 varchar(42) NOT NULL, -- レシート表示欄-お問合せ先
                                   receiptsdisp12 varchar(12) NOT NULL, -- レシート表示欄-お問合せ電話番号
                                   receiptsdisp13 varchar(11) NOT NULL, -- レシート表示欄-お問合せ受付時間
                                   itemname varchar(256) NOT NULL, -- 商品・サービス名
                                   CONSTRAINT mulpayshop_pkey PRIMARY KEY (shopseq)
);
COMMENT ON TABLE public.mulpayshop IS 'マルチペイメントショップ設定';

-- Column comments

COMMENT ON COLUMN public.mulpayshop.shopseq IS 'ショップSEQ';
COMMENT ON COLUMN public.mulpayshop.shopid IS 'ショップID';
COMMENT ON COLUMN public.mulpayshop.shoppass IS 'ショップパスワード';
COMMENT ON COLUMN public.mulpayshop.shopaccessurl IS 'アクセスURL';
COMMENT ON COLUMN public.mulpayshop.tdtenantname IS '3Dセキュア表示店舗名(BASE64 エンコーディング後の長さが25BYTE 以内の文字列)';
COMMENT ON COLUMN public.mulpayshop.httpaccept IS 'HTTP_ACCEPT';
COMMENT ON COLUMN public.mulpayshop.httpuseragent IS 'HTTP_USER_AGENT';
COMMENT ON COLUMN public.mulpayshop.clientfield1 IS '加盟店舗自由項目１';
COMMENT ON COLUMN public.mulpayshop.clientfield2 IS '加盟店舗自由項目２';
COMMENT ON COLUMN public.mulpayshop.clientfield3 IS '加盟店舗自由項目３';
COMMENT ON COLUMN public.mulpayshop.clientfieldflag IS '加盟店自由項目返却フラグ';
COMMENT ON COLUMN public.mulpayshop.shopmailaddress IS '加盟店メールアドレス';
COMMENT ON COLUMN public.mulpayshop.registerdisp1 IS 'POS レジ表示欄-ショップ名';
COMMENT ON COLUMN public.mulpayshop.registerdisp2 IS 'POS レジ表示欄2';
COMMENT ON COLUMN public.mulpayshop.registerdisp3 IS 'POS レジ表示欄3';
COMMENT ON COLUMN public.mulpayshop.registerdisp4 IS 'POS レジ表示欄4';
COMMENT ON COLUMN public.mulpayshop.registerdisp5 IS 'POS レジ表示欄5';
COMMENT ON COLUMN public.mulpayshop.registerdisp6 IS 'POS レジ表示欄6';
COMMENT ON COLUMN public.mulpayshop.registerdisp7 IS 'POS レジ表示欄7';
COMMENT ON COLUMN public.mulpayshop.registerdisp8 IS 'POS レジ表示欄8';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp1 IS 'レシート表示欄1';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp2 IS 'レシート表示欄2';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp3 IS 'レシート表示欄3';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp4 IS 'レシート表示欄4';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp5 IS 'レシート表示欄5';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp6 IS 'レシート表示欄6';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp7 IS 'レシート表示欄7';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp8 IS 'レシート表示欄8';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp9 IS 'レシート表示欄9';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp10 IS 'レシート表示欄10';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp11 IS 'レシート表示欄-お問合せ先';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp12 IS 'レシート表示欄-お問合せ電話番号';
COMMENT ON COLUMN public.mulpayshop.receiptsdisp13 IS 'レシート表示欄-お問合せ受付時間';
COMMENT ON COLUMN public.mulpayshop.itemname IS '商品・サービス名';

-- public.creditlinereport definition
CREATE TABLE public.creditlinereport (
                                         orderid varchar(27) NOT NULL, -- 受注ID
                                         registtime timestamp(0) NOT NULL, -- 登録日時
                                         updatetime timestamp(0) NOT NULL, -- 更新日時
                                         CONSTRAINT creditlinereport_pkey PRIMARY KEY (orderid)
);
COMMENT ON TABLE public.creditlinereport IS '与信枠解放';

-- Column comments

COMMENT ON COLUMN public.creditlinereport.orderid IS '受注ID';
COMMENT ON COLUMN public.creditlinereport.registtime IS '登録日時';
COMMENT ON COLUMN public.creditlinereport.updatetime IS '更新日時';

-- DROP SEQUENCE
DROP SEQUENCE IF EXISTS public.mulpaybillseq CASCADE;
DROP SEQUENCE IF EXISTS public.settlementmethodseq CASCADE;
DROP SEQUENCE IF EXISTS public.mulpayresultseq CASCADE;

-- public.settlementmethodlink definition
CREATE TABLE public.settlementmethodlink (
                                         paymethod varchar(32) NOT NULL, -- 決済手段識別子
                                         paytype varchar(32) NOT NULL, -- 決済方法(GMO)
                                         paytypename varchar(60) NULL, -- 決済手段名
                                         cancellimitday numeric (3) NULL, -- キャンセル期限(日数)
                                         cancellimitmonth numeric (1) NULL, -- キャンセル期限(月数)
                                         CONSTRAINT settlementmethodlink_pkey PRIMARY KEY (paymethod)
);
COMMENT ON TABLE public.settlementmethodlink IS 'リンク決済個別決済手段';

-- Column comments

COMMENT ON COLUMN public.settlementmethodlink.paymethod IS '決済手段識別子';
COMMENT ON COLUMN public.settlementmethodlink.paytype IS '決済方法(GMO)';
COMMENT ON COLUMN public.settlementmethodlink.paytypename IS '決済手段名';
COMMENT ON COLUMN public.settlementmethodlink.cancellimitday IS 'キャンセル期限(日数)';
COMMENT ON COLUMN public.settlementmethodlink.cancellimitmonth IS 'キャンセル期限(月数)';


-- CREATE SEQUENCE
/* ORD12 マルチペイメント請求SEQ */
CREATE SEQUENCE mulpaybillseq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999 START 10000000;

/* SYS15 決済方法SEQ */
CREATE SEQUENCE settlementmethodseq   INCREMENT 1 MINVALUE 1000 MAXVALUE 9999 START 1000;

/* ORD13 ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果SEQ */
CREATE SEQUENCE mulpayresultseq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999 START 10000000;
