/*
 hclabo-user
*/

-- DROP TABLE
DROP TABLE IF EXISTS "public"."adminauthgroup" CASCADE;
DROP TABLE IF EXISTS "public"."adminauthgroupdetail" CASCADE;
DROP TABLE IF EXISTS "public"."administrator" CASCADE;
DROP TABLE IF EXISTS "public"."confirmmail" CASCADE;
DROP TABLE IF EXISTS "public"."customeraddressbook" CASCADE;
DROP TABLE IF EXISTS "public"."inquiry" CASCADE;
DROP TABLE IF EXISTS "public"."inquirydetail" CASCADE;
DROP TABLE IF EXISTS "public"."inquirygroup" CASCADE;
DROP TABLE IF EXISTS "public"."mailmagazinemember" CASCADE;
DROP TABLE IF EXISTS "public"."mailtemplate" CASCADE;
DROP TABLE IF EXISTS "public"."memberinfo" CASCADE;
DROP TABLE IF EXISTS "public"."persistent_logins" CASCADE;
DROP TABLE IF EXISTS "public"."csvoption" CASCADE;
DROP TABLE IF EXISTS "public"."previewaccesscontrol" CASCADE;

-- ----------------------------
-- Table structure for adminauthgroup
-- ----------------------------
CREATE TABLE "public"."adminauthgroup" (
  "adminauthgroupseq" numeric(4,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "authgroupdisplayname" varchar(40) NOT NULL,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."adminauthgroup"."adminauthgroupseq" IS '運営者権限グループSEQ';
COMMENT ON COLUMN "public"."adminauthgroup"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."adminauthgroup"."authgroupdisplayname" IS 'グループ表示名';
COMMENT ON COLUMN "public"."adminauthgroup"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."adminauthgroup"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."adminauthgroup" IS '運営者権限グループ';

-- ----------------------------
-- Table structure for adminauthgroupdetail
-- ----------------------------
CREATE TABLE "public"."adminauthgroupdetail" (
  "adminauthgroupseq" numeric(4,0) NOT NULL,
  "authtypecode" varchar(32) NOT NULL,
  "authlevel" numeric(4,0) NOT NULL,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."adminauthgroupdetail"."adminauthgroupseq" IS '運営者権限グループSEQ(FK)';
COMMENT ON COLUMN "public"."adminauthgroupdetail"."authtypecode" IS '権限種別';
COMMENT ON COLUMN "public"."adminauthgroupdetail"."authlevel" IS '権限レベル';
COMMENT ON COLUMN "public"."adminauthgroupdetail"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."adminauthgroupdetail"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."adminauthgroupdetail" IS '運営者権限グループ詳細';

-- ----------------------------
-- Table structure for administrator
-- ----------------------------
CREATE TABLE "public"."administrator" (
  "administratorseq" numeric(8,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "administratorstatus" varchar(1) NOT NULL DEFAULT 0,
  "administratorid" varchar(20) NOT NULL,
  "administratorpassword" varchar(64) NOT NULL,
  "mail" varchar(160) NOT NULL,
  "administratorlastname" varchar(15) NOT NULL,
  "administratorfirstname" varchar(15) NOT NULL,
  "administratorlastkana" varchar(15) NOT NULL,
  "administratorfirstkana" varchar(15) NOT NULL,
  "usestartdate" timestamp(0),
  "useenddate" timestamp(0),
  "adminauthgroupseq" numeric(4,0) NOT NULL,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL,
  "passwordchangetime" timestamp(0),
  "passwordexpirydate" timestamp(0),
  "loginfailurecount" numeric(4,0) NOT NULL DEFAULT 0,
  "accountlocktime" timestamp(0),
  "passwordneedchangeflag" varchar(1),
  "passwordsha256encryptedflag" varchar(1) NOT NULL DEFAULT 1
)
;
COMMENT ON COLUMN "public"."administrator"."administratorseq" IS '管理者SEQ';
COMMENT ON COLUMN "public"."administrator"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."administrator"."administratorstatus" IS '管理者状態';
COMMENT ON COLUMN "public"."administrator"."administratorid" IS '管理者ID';
COMMENT ON COLUMN "public"."administrator"."administratorpassword" IS '管理者パスワード';
COMMENT ON COLUMN "public"."administrator"."mail" IS '管理者メールアドレス';
COMMENT ON COLUMN "public"."administrator"."administratorlastname" IS '管理者氏名(姓)';
COMMENT ON COLUMN "public"."administrator"."administratorfirstname" IS '管理者氏名(名）';
COMMENT ON COLUMN "public"."administrator"."administratorlastkana" IS '管理者フリガナ(姓)';
COMMENT ON COLUMN "public"."administrator"."administratorfirstkana" IS '管理者フリガナ(名)';
COMMENT ON COLUMN "public"."administrator"."usestartdate" IS '利用開始日';
COMMENT ON COLUMN "public"."administrator"."useenddate" IS '利用終了日';
COMMENT ON COLUMN "public"."administrator"."adminauthgroupseq" IS '権限グループSEQ(FK)';
COMMENT ON COLUMN "public"."administrator"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."administrator"."updatetime" IS '更新日時';
COMMENT ON COLUMN "public"."administrator"."passwordchangetime" IS 'パスワード変更日時';
COMMENT ON COLUMN "public"."administrator"."passwordexpirydate" IS 'パスワード期限切れ日';
COMMENT ON COLUMN "public"."administrator"."loginfailurecount" IS 'ログイン失敗回数';
COMMENT ON COLUMN "public"."administrator"."accountlocktime" IS 'アカウントロック日時';
COMMENT ON COLUMN "public"."administrator"."passwordneedchangeflag" IS 'パスワード変更要求フラグ';
COMMENT ON COLUMN "public"."administrator"."passwordsha256encryptedflag" IS 'パスワードSHA256暗号化済みフラグ';
COMMENT ON TABLE "public"."administrator" IS '管理者';

-- ----------------------------
-- Table structure for confirmmail
-- ----------------------------
CREATE TABLE "public"."confirmmail" (
  "confirmmailseq" numeric(8,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "memberinfoseq" numeric(10,0) DEFAULT 0,
  "orderseq" numeric(8,0) DEFAULT 0,
  "confirmmail" varchar(160),
  "confirmmailtype" varchar(2) NOT NULL,
  "confirmmailpassword" varchar(128) NOT NULL,
  "effectivetime" timestamp(0) NOT NULL,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."confirmmail"."confirmmailseq" IS '確認メールSEQ';
COMMENT ON COLUMN "public"."confirmmail"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."confirmmail"."memberinfoseq" IS '会員SEQ';
COMMENT ON COLUMN "public"."confirmmail"."orderseq" IS '受注SEQ';
COMMENT ON COLUMN "public"."confirmmail"."confirmmail" IS '確認メールアドレス';
COMMENT ON COLUMN "public"."confirmmail"."confirmmailtype" IS '確認メール種別';
COMMENT ON COLUMN "public"."confirmmail"."confirmmailpassword" IS '確認メールパスワード';
COMMENT ON COLUMN "public"."confirmmail"."effectivetime" IS '有効期限';
COMMENT ON COLUMN "public"."confirmmail"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."confirmmail"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."confirmmail" IS '確認メール';

-- ----------------------------
-- Table structure for customeraddressbook
-- ----------------------------
CREATE TABLE "public"."customeraddressbook" (
  "addressid" text NOT NULL,
  "customerid" text NOT NULL,
  "addressname" text,
  "lastname" text,
  "firstname" text,
  "lastkana" text,
  "firstkana" text,
  "tel" text,
  "zipcode" text,
  "prefecture" text,
  "address1" text,
  "address2" text,
  "address3" text,
  "shippingmemo" text
)
;

COMMENT ON COLUMN "public"."customeraddressbook"."customerid" IS '顧客ID';
COMMENT ON COLUMN "public"."customeraddressbook"."addressid" IS '住所ID';
COMMENT ON COLUMN "public"."customeraddressbook"."addressname" IS '住所名';
COMMENT ON COLUMN "public"."customeraddressbook"."lastname" IS '氏名(姓)';
COMMENT ON COLUMN "public"."customeraddressbook"."firstname" IS '氏名(名)';
COMMENT ON COLUMN "public"."customeraddressbook"."lastkana" IS 'フリガナ(姓)';
COMMENT ON COLUMN "public"."customeraddressbook"."firstkana" IS 'フリガナ(名)';
COMMENT ON COLUMN "public"."customeraddressbook"."tel" IS '電話番号';
COMMENT ON COLUMN "public"."customeraddressbook"."zipcode" IS '郵便番号';
COMMENT ON COLUMN "public"."customeraddressbook"."prefecture" IS '都道府県';
COMMENT ON COLUMN "public"."customeraddressbook"."address1" IS '住所1';
COMMENT ON COLUMN "public"."customeraddressbook"."address2" IS '住所2';
COMMENT ON COLUMN "public"."customeraddressbook"."address3" IS '住所3';
COMMENT ON COLUMN "public"."customeraddressbook"."shippingmemo" IS '配送メモ';
COMMENT ON TABLE "public"."customeraddressbook" IS '会員住所';


-- ----------------------------
-- Table structure for inquiry
-- ----------------------------
CREATE TABLE "public"."inquiry" (
  "inquiryseq" numeric(8,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "inquirycode" varchar(12) NOT NULL,
  "inquirylastname" varchar(40) NOT NULL,
  "inquiryfirstname" varchar(40),
  "inquirymail" varchar(160),
  "inquirytitle" varchar(4000),
  "inquirybody" varchar(4000),
  "inquirytime" timestamp(0) NOT NULL,
  "inquirystatus" varchar(1) NOT NULL DEFAULT 0,
  "answertime" timestamp(0),
  "answertitle" varchar(200),
  "answerbody" text,
  "answerfrom" varchar(200),
  "answerto" varchar(200),
  "answerbcc" varchar(200),
  "inquirygroupseq" numeric(4,0) NOT NULL,
  "inquirylastkana" varchar(50),
  "inquiryfirstkana" varchar(50),
  "inquiryzipcode" varchar(7),
  "inquiryprefecture" varchar(10),
  "inquiryaddress1" varchar(60),
  "inquiryaddress2" varchar(100),
  "inquiryaddress3" varchar(200),
  "inquirytel" varchar(20),
  "inquirymobiletel" varchar(20),
  "processpersonname" varchar(40),
  "versionno" numeric(6,0) NOT NULL DEFAULT 0,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL,
  "inquirytype" varchar(1) NOT NULL,
  "memberinfoseq" numeric(10,0) NOT NULL,
  "ordercode" varchar(12),
  "firstinquirytime" timestamp(6) NOT NULL,
  "lastuserinquirytime" timestamp(6),
  "lastoperatorinquirytime" timestamp(6),
  "lastrepresentative" varchar(18),
  "memo" varchar(2000),
  "cooperationmemo" varchar(100)
)
;
COMMENT ON COLUMN "public"."inquiry"."inquiryseq" IS '問い合わせSEQ';
COMMENT ON COLUMN "public"."inquiry"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."inquiry"."inquirycode" IS '問い合わせコード';
COMMENT ON COLUMN "public"."inquiry"."inquirylastname" IS '問い合わせ者氏名(姓)';
COMMENT ON COLUMN "public"."inquiry"."inquiryfirstname" IS '問い合わせ者氏名(名）';
COMMENT ON COLUMN "public"."inquiry"."inquirymail" IS '問い合わせ者メールアドレス';
COMMENT ON COLUMN "public"."inquiry"."inquirytitle" IS '問い合わせタイトル';
COMMENT ON COLUMN "public"."inquiry"."inquirybody" IS '問い合わせ内容';
COMMENT ON COLUMN "public"."inquiry"."inquirytime" IS '問い合わせ日時';
COMMENT ON COLUMN "public"."inquiry"."inquirystatus" IS '問い合わせ状態';
COMMENT ON COLUMN "public"."inquiry"."answertime" IS '回答日時';
COMMENT ON COLUMN "public"."inquiry"."answertitle" IS '回答タイトル';
COMMENT ON COLUMN "public"."inquiry"."answerbody" IS '回答内容';
COMMENT ON COLUMN "public"."inquiry"."answerfrom" IS '回答FROM';
COMMENT ON COLUMN "public"."inquiry"."answerto" IS '回答TO';
COMMENT ON COLUMN "public"."inquiry"."answerbcc" IS '回答BCC';
COMMENT ON COLUMN "public"."inquiry"."inquirygroupseq" IS '問い合わせ分類SEQ';
COMMENT ON COLUMN "public"."inquiry"."inquirylastkana" IS '問い合わせ者氏名フリガナ(姓)';
COMMENT ON COLUMN "public"."inquiry"."inquiryfirstkana" IS '問い合わせ者氏名フリガナ(名)';
COMMENT ON COLUMN "public"."inquiry"."inquiryzipcode" IS '問い合わせ者住所-郵便番号';
COMMENT ON COLUMN "public"."inquiry"."inquiryprefecture" IS '問い合わせ者住所-都道府県';
COMMENT ON COLUMN "public"."inquiry"."inquiryaddress1" IS '問い合わせ者住所-市区郡';
COMMENT ON COLUMN "public"."inquiry"."inquiryaddress2" IS '問い合わせ者住所-町村･番地';
COMMENT ON COLUMN "public"."inquiry"."inquiryaddress3" IS '問い合わせ者住所-それ以降の住所';
COMMENT ON COLUMN "public"."inquiry"."inquirytel" IS '問い合わせ者TEL';
COMMENT ON COLUMN "public"."inquiry"."inquirymobiletel" IS '問い合わせ者携帯電話番号';
COMMENT ON COLUMN "public"."inquiry"."processpersonname" IS '処理担当者名';
COMMENT ON COLUMN "public"."inquiry"."versionno" IS '更新カウンタ';
COMMENT ON COLUMN "public"."inquiry"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."inquiry"."updatetime" IS '更新日時';
COMMENT ON COLUMN "public"."inquiry"."inquirytype" IS '問い合わせ種別';
COMMENT ON COLUMN "public"."inquiry"."memberinfoseq" IS '会員SEQ';
COMMENT ON COLUMN "public"."inquiry"."ordercode" IS '注文番号';
COMMENT ON COLUMN "public"."inquiry"."firstinquirytime" IS '初回問い合わせ日時';
COMMENT ON COLUMN "public"."inquiry"."lastuserinquirytime" IS '最終お客様問い合わせ日時';
COMMENT ON COLUMN "public"."inquiry"."lastoperatorinquirytime" IS '最終運用者問い合わせ日時';
COMMENT ON COLUMN "public"."inquiry"."lastrepresentative" IS '最終担当者';
COMMENT ON COLUMN "public"."inquiry"."memo" IS '管理メモ';
COMMENT ON COLUMN "public"."inquiry"."cooperationmemo" IS '連携メモ';
COMMENT ON TABLE "public"."inquiry" IS '問い合わせ';

-- ----------------------------
-- Table structure for inquirydetail
-- ----------------------------
CREATE TABLE "public"."inquirydetail" (
  "inquiryseq" numeric(8,0) NOT NULL,
  "inquiryversionno" numeric(2,0) NOT NULL,
  "requesttype" varchar(1) NOT NULL,
  "inquirytime" timestamp(6) NOT NULL,
  "inquirybody" varchar(4000),
  "divisionname" varchar(20),
  "representative" varchar(18),
  "contacttel" varchar(20),
  "operator" varchar(20),
  "registtime" timestamp(6) NOT NULL,
  "updatetime" timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."inquirydetail"."inquiryseq" IS '問い合わせSEQ';
COMMENT ON COLUMN "public"."inquirydetail"."inquiryversionno" IS '連番';
COMMENT ON COLUMN "public"."inquirydetail"."requesttype" IS '発信者種別';
COMMENT ON COLUMN "public"."inquirydetail"."inquirytime" IS '問い合わせ日時';
COMMENT ON COLUMN "public"."inquirydetail"."inquirybody" IS '問い合わせ内容';
COMMENT ON COLUMN "public"."inquirydetail"."divisionname" IS '部署名';
COMMENT ON COLUMN "public"."inquirydetail"."representative" IS '担当者';
COMMENT ON COLUMN "public"."inquirydetail"."contacttel" IS '連絡先TEL';
COMMENT ON COLUMN "public"."inquirydetail"."operator" IS '処理担当者';
COMMENT ON COLUMN "public"."inquirydetail"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."inquirydetail"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."inquirydetail" IS '問い合わせ内容';

-- ----------------------------
-- Table structure for inquirygroup
-- ----------------------------
CREATE TABLE "public"."inquirygroup" (
  "inquirygroupseq" numeric(4,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "inquirygroupname" varchar(40) NOT NULL,
  "openstatus" varchar(1) NOT NULL DEFAULT 0,
  "orderdisplay" numeric(4,0),
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."inquirygroup"."inquirygroupseq" IS '問い合わせ分類SEQ';
COMMENT ON COLUMN "public"."inquirygroup"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."inquirygroup"."inquirygroupname" IS '問い合わせ分類名';
COMMENT ON COLUMN "public"."inquirygroup"."openstatus" IS '公開状態';
COMMENT ON COLUMN "public"."inquirygroup"."orderdisplay" IS '表示順';
COMMENT ON COLUMN "public"."inquirygroup"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."inquirygroup"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."inquirygroup" IS '問い合わせ分類';

-- ----------------------------
-- Table structure for mailmagazinemember
-- ----------------------------
CREATE TABLE "public"."mailmagazinemember" (
  "shopseq" numeric(4,0) NOT NULL,
  "memberinfoseq" numeric(10,0) NOT NULL,
  "uniquemail" varchar(259) NOT NULL,
  "mail" varchar(160) NOT NULL,
  "sendstatus" varchar(1) NOT NULL DEFAULT 0,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."mailmagazinemember"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."mailmagazinemember"."memberinfoseq" IS '会員SEQ';
COMMENT ON COLUMN "public"."mailmagazinemember"."uniquemail" IS '一意制約用メールアドレス';
COMMENT ON COLUMN "public"."mailmagazinemember"."mail" IS 'メールアドレス';
COMMENT ON COLUMN "public"."mailmagazinemember"."sendstatus" IS '配信状態';
COMMENT ON COLUMN "public"."mailmagazinemember"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."mailmagazinemember"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."mailmagazinemember" IS 'メルマガ購読者';

-- ----------------------------
-- Table structure for mailtemplate
-- ----------------------------
CREATE TABLE "public"."mailtemplate" (
  "mailtemplateseq" numeric(4,0) NOT NULL,
  "shopseq" numeric(4,0) NOT NULL,
  "mailtemplatename" varchar(80) NOT NULL,
  "mailtemplatetype" varchar(2) NOT NULL DEFAULT 0,
  "mailtemplatedefaultflag" varchar(1) NOT NULL DEFAULT 0,
  "mailtemplatesubject" varchar(200) NOT NULL,
  "mailtemplatefromaddress" varchar(200),
  "mailtemplatetoaddress" varchar(200),
  "mailtemplateccaddress" varchar(200),
  "mailtemplatebccaddress" varchar(200),
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
)
;
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplateseq" IS 'メールテンプレートSEQ';
COMMENT ON COLUMN "public"."mailtemplate"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatename" IS 'メールテンプレート名';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatetype" IS 'メールテンプレート種別';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatedefaultflag" IS 'メールテンプレート標準フラグ';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatesubject" IS 'メールテンプレート件名';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatefromaddress" IS 'メールテンプレートFROM';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatetoaddress" IS 'メールテンプレートTO';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplateccaddress" IS 'メールテンプレートCC';
COMMENT ON COLUMN "public"."mailtemplate"."mailtemplatebccaddress" IS 'メールテンプレートBCC';
COMMENT ON COLUMN "public"."mailtemplate"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."mailtemplate"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."mailtemplate" IS 'メールテンプレート';

-- ----------------------------
-- Table structure for memberinfo
-- ----------------------------
CREATE TABLE "public"."memberinfo" (
  "memberinfoseq" numeric(10,0) NOT NULL,
  "memberinfostatus" varchar(1) NOT NULL DEFAULT 0,
  "memberinfouniqueid" varchar(259),
  "memberinfoid" varchar(255),
  "memberinfopassword" varchar(64),
  "memberinfolastname" varchar(40),
  "memberinfofirstname" varchar(40),
  "memberinfolastkana" varchar(40),
  "memberinfofirstkana" varchar(40),
  "memberinfosex" varchar(1),
  "memberinfobirthday" timestamp(0),
  "memberinfotel" varchar(20),
  "memberinfomail" varchar(160),
  "shopseq" numeric(4,0) NOT NULL,
  "accessuid" varchar(100),
  "versionno" numeric(6,0) NOT NULL DEFAULT 0,
  "admissionymd" varchar(8),
  "secessionymd" varchar(8),
  "memo" varchar(2000),
  "lastlogintime" timestamp(0),
  "lastloginuseragent" text,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL,
  "memberinfoaddressid" varchar(36)
)
;
COMMENT ON COLUMN "public"."memberinfo"."memberinfoseq" IS '会員SEQ';
COMMENT ON COLUMN "public"."memberinfo"."memberinfostatus" IS '会員状態';
COMMENT ON COLUMN "public"."memberinfo"."memberinfouniqueid" IS '会員一意制約用ID';
COMMENT ON COLUMN "public"."memberinfo"."memberinfoid" IS '会員ID';
COMMENT ON COLUMN "public"."memberinfo"."memberinfopassword" IS '会員パスワード';
COMMENT ON COLUMN "public"."memberinfo"."memberinfolastname" IS '会員氏名(姓)';
COMMENT ON COLUMN "public"."memberinfo"."memberinfofirstname" IS '会員氏名(名）';
COMMENT ON COLUMN "public"."memberinfo"."memberinfolastkana" IS '会員フリガナ(姓)';
COMMENT ON COLUMN "public"."memberinfo"."memberinfofirstkana" IS '会員フリガナ(名)';
COMMENT ON COLUMN "public"."memberinfo"."memberinfosex" IS '会員性別';
COMMENT ON COLUMN "public"."memberinfo"."memberinfobirthday" IS '会員生年月日';
COMMENT ON COLUMN "public"."memberinfo"."memberinfotel" IS '会員TEL';
COMMENT ON COLUMN "public"."memberinfo"."memberinfomail" IS '会員メールアドレス';
COMMENT ON COLUMN "public"."memberinfo"."shopseq" IS 'ショップSEQ';
COMMENT ON COLUMN "public"."memberinfo"."accessuid" IS '端末識別情報';
COMMENT ON COLUMN "public"."memberinfo"."versionno" IS '更新カウンタ';
COMMENT ON COLUMN "public"."memberinfo"."admissionymd" IS '入会日YMD';
COMMENT ON COLUMN "public"."memberinfo"."secessionymd" IS '退会日YMD';
COMMENT ON COLUMN "public"."memberinfo"."memo" IS 'メモ';
COMMENT ON COLUMN "public"."memberinfo"."lastlogintime" IS '最終ログイン日時';
COMMENT ON COLUMN "public"."memberinfo"."lastloginuseragent" IS '最終ログインユーザーエージェント';
COMMENT ON COLUMN "public"."memberinfo"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."memberinfo"."updatetime" IS '更新日時';
COMMENT ON COLUMN "public"."memberinfo"."memberinfoaddressid" IS '会員住所ID';
COMMENT ON TABLE "public"."memberinfo" IS '会員';

------------------------------
-- Table structure for persistent_logins
------------------------------
CREATE TABLE "public"."persistent_logins"
(
    "username"  varchar(64)  NOT NULL,
    "series"    varchar(64)  NOT NULL,
    "token"     varchar(64)  NOT NULL,
    "last_used" timestamp(6) NOT NULL
);
ALTER TABLE "public"."persistent_logins" ADD CONSTRAINT "persistent_logins_pkey" PRIMARY KEY ("series");

COMMENT ON COLUMN "public"."persistent_logins"."username" IS 'ユーザー名';
COMMENT ON COLUMN "public"."persistent_logins"."series" IS 'シリーズ';
COMMENT ON COLUMN "public"."persistent_logins"."token" IS 'トークン';
COMMENT ON COLUMN "public"."persistent_logins"."last_used" IS '最後使用';
COMMENT ON TABLE "public"."persistent_logins" IS 'RememberMe情報';

------------------------------
-- Table structure for csvoption
------------------------------
CREATE TABLE public.csvoption (
    optionid numeric(4,0) NOT NULL,
    optioninfo text,
    CONSTRAINT csvoption_pk PRIMARY KEY (optionid)
);
COMMENT ON TABLE public.csvoption IS 'CSVオプション';

-- Column comments
COMMENT ON COLUMN public.csvoption.optionid IS 'オプションID';
COMMENT ON COLUMN public.csvoption.optioninfo IS 'CSVDL オプション情報';

-- ----------------------------
-- Table structure for previewaccesscontrol
-- ----------------------------
CREATE TABLE "public"."previewaccesscontrol" (
  "previewaccesscontrolseq" numeric(8,0) NOT NULL,
  "previewaccesskey" varchar(120) NOT NULL,
  "effectivetime" timestamp(0) NOT NULL,
  "administratorseq" numeric(8,0) NOT NULL,
  "registtime" timestamp(0) NOT NULL,
  "updatetime" timestamp(0) NOT NULL
);

COMMENT ON COLUMN "public"."previewaccesscontrol"."previewaccesscontrolseq" IS 'プレビューアクセス制御SEQ';
COMMENT ON COLUMN "public"."previewaccesscontrol"."previewaccesskey" IS 'プレビューアクセスキー';
COMMENT ON COLUMN "public"."previewaccesscontrol"."effectivetime" IS 'プレビューアクセス有効期限';
COMMENT ON COLUMN "public"."previewaccesscontrol"."administratorseq" IS '管理者SEQ';
COMMENT ON COLUMN "public"."previewaccesscontrol"."registtime" IS '登録日時';
COMMENT ON COLUMN "public"."previewaccesscontrol"."updatetime" IS '更新日時';
COMMENT ON TABLE "public"."previewaccesscontrol" IS 'プレビューアクセス制御エンティティ';

-- ----------------------------
-- DROP SEQUENCE
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."adminauthgroupseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."administratorseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."confirmmailseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."inquirygroupseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."inquiryseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."mailtemplateseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."memberinfoseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."accessuidseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."csvoptionseq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."optionid_seq" CASCADE;
DROP SEQUENCE IF EXISTS "public"."previewaccesscontrolseq" CASCADE;

-- ----------------------------
-- Sequence structure for optionid
-- ----------------------------
CREATE SEQUENCE "public"."optionid_seq"
INCREMENT 1
MINVALUE 1
MAXVALUE 9999
START 1000
CACHE 1;

-- ----------------------------
-- Sequence structure for adminauthgroupseq
-- ----------------------------
CREATE SEQUENCE "public"."adminauthgroupseq" 
INCREMENT 1
MINVALUE  1000
MAXVALUE 9999
START 1003
CACHE 1;

-- ----------------------------
-- Sequence structure for administratorseq
-- ----------------------------
CREATE SEQUENCE "public"."administratorseq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 99999999
START 10000
CACHE 1;

-- ----------------------------
-- Sequence structure for confirmmailseq
-- ----------------------------
CREATE SEQUENCE "public"."confirmmailseq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 99999999
START 10000000
CACHE 1
CYCLE ;

-- ----------------------------
-- Sequence structure for inquirygroupseq
-- ----------------------------
CREATE SEQUENCE "public"."inquirygroupseq" 
INCREMENT 1
MINVALUE  1000
MAXVALUE 9999
START 1000
CACHE 1;

-- ----------------------------
-- Sequence structure for inquiryseq
-- ----------------------------
CREATE SEQUENCE "public"."inquiryseq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 99999999
START 10000000
CACHE 1;

-- ----------------------------
-- Sequence structure for mailtemplateseq
-- ----------------------------
CREATE SEQUENCE "public"."mailtemplateseq" 
INCREMENT 1
MINVALUE  1000
MAXVALUE 9999
START 1000
CACHE 1;

-- ----------------------------
-- Sequence structure for memberinfoseq
-- ----------------------------
CREATE SEQUENCE "public"."memberinfoseq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1000000000
CACHE 1;

/* レポート */
/* REP01 端末識別情報SEQ */
CREATE SEQUENCE "public"."accessuidseq" INCREMENT 1 MINVALUE 1 MAXVALUE 9999 START 1000 CYCLE;

-- ----------------------------
-- Sequence structure for csvoptionseq
-- ----------------------------
CREATE SEQUENCE "public"."csvoptionseq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9999
START 1000
CACHE 1;

-- ----------------------------
-- Sequence structure for previewaccesscontrolseq
-- ----------------------------
CREATE SEQUENCE "public"."previewaccesscontrolseq"
INCREMENT 1
MINVALUE  1
MAXVALUE 99999999
START 10000000
CACHE 1;

-- ----------------------------
-- Uniques structure for table adminauthgroup
-- ----------------------------
ALTER TABLE "public"."adminauthgroup" ADD CONSTRAINT "adminauthgroup_key" UNIQUE ("shopseq", "authgroupdisplayname");

-- ----------------------------
-- Primary Key structure for table adminauthgroup
-- ----------------------------
ALTER TABLE "public"."adminauthgroup" ADD CONSTRAINT "adminauthgroup_pkey" PRIMARY KEY ("adminauthgroupseq");

-- ----------------------------
-- Primary Key structure for table adminauthgroupdetail
-- ----------------------------
ALTER TABLE "public"."adminauthgroupdetail" ADD CONSTRAINT "adminauthgroupdetail_pkey" PRIMARY KEY ("adminauthgroupseq", "authtypecode");

-- ----------------------------
-- Primary Key structure for table administrator
-- ----------------------------
ALTER TABLE "public"."administrator" ADD CONSTRAINT "administrator_pkey" PRIMARY KEY ("administratorseq");

-- ----------------------------
-- Uniques structure for table confirmmail
-- ----------------------------
ALTER TABLE "public"."confirmmail" ADD CONSTRAINT "confirmmail_confirmmailpassword_key" UNIQUE ("confirmmailpassword");

-- ----------------------------
-- Primary Key structure for table confirmmail
-- ----------------------------
ALTER TABLE "public"."confirmmail" ADD CONSTRAINT "confirmmail_pkey" PRIMARY KEY ("confirmmailseq");

-- ----------------------------
-- Uniques structure for table customeraddressbook
-- ----------------------------
ALTER TABLE "public"."customeraddressbook" ADD CONSTRAINT "customeraddressbook_addressid_customerid_key" UNIQUE ("addressid", "customerid");

-- ----------------------------
-- Primary Key structure for table customeraddressbook
-- ----------------------------
ALTER TABLE "public"."customeraddressbook" ADD CONSTRAINT "customeraddressbook_pk" PRIMARY KEY ("customerid");

-- ----------------------------
-- Primary Key structure for table inquiry
-- ----------------------------
ALTER TABLE "public"."inquiry" ADD CONSTRAINT "inquiry_pkey" PRIMARY KEY ("inquiryseq");

-- ----------------------------
-- Primary Key structure for table inquirydetail
-- ----------------------------
ALTER TABLE "public"."inquirydetail" ADD CONSTRAINT "inquirydetail_pkey" PRIMARY KEY ("inquiryseq", "inquiryversionno");

-- ----------------------------
-- Primary Key structure for table inquirygroup
-- ----------------------------
ALTER TABLE "public"."inquirygroup" ADD CONSTRAINT "inquirygroup_pkey" PRIMARY KEY ("inquirygroupseq");

-- ----------------------------
-- Primary Key structure for table mailmagazinemember
-- ----------------------------
ALTER TABLE "public"."mailmagazinemember" ADD CONSTRAINT "mailmagazinemember_pkey" PRIMARY KEY ("memberinfoseq");

-- ----------------------------
-- Primary Key structure for table mailtemplate
-- ----------------------------
ALTER TABLE "public"."mailtemplate" ADD CONSTRAINT "mailtemplate_pkey" PRIMARY KEY ("mailtemplateseq", "shopseq");

-- ----------------------------
-- Uniques structure for table memberinfo
-- ----------------------------
ALTER TABLE "public"."memberinfo" ADD CONSTRAINT "memberinfo_memberinfouniqueid_key" UNIQUE ("memberinfouniqueid");

-- ----------------------------
-- Primary Key structure for table memberinfo
-- ----------------------------
ALTER TABLE "public"."memberinfo" ADD CONSTRAINT "memberinfo_pkey" PRIMARY KEY ("memberinfoseq");

-- ----------------------------
-- Primary Key structure for table previewaccesscontrol
-- ----------------------------
ALTER TABLE "public"."previewaccesscontrol" ADD CONSTRAINT "previewaccesscontrol_pkey" PRIMARY KEY ("previewaccesscontrolseq");

-- ----------------------------
-- Foreign Keys structure for table adminauthgroupdetail
-- ----------------------------
ALTER TABLE "public"."adminauthgroupdetail" ADD CONSTRAINT "adminauthgroupdetail_fkey" FOREIGN KEY ("adminauthgroupseq") REFERENCES "public"."adminauthgroup" ("adminauthgroupseq") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table administrator
-- ----------------------------
ALTER TABLE "public"."administrator" ADD CONSTRAINT "administrator_fkey" FOREIGN KEY ("adminauthgroupseq") REFERENCES "public"."adminauthgroup" ("adminauthgroupseq") ON DELETE NO ACTION ON UPDATE NO ACTION;