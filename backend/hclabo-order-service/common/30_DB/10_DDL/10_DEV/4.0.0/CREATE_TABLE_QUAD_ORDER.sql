-- DROP TABLE
DROP TABLE IF EXISTS ORDERRECEIVED CASCADE;
DROP TABLE IF EXISTS TRANSACTION CASCADE;
DROP TABLE IF EXISTS TRANSACTIONFORREVISION CASCADE;

-- CREATE TABLE
-- CREATE ORDERRECEIVED
CREATE TABLE public.ORDERRECEIVED (
                                      orderreceivedid text NOT NULL, -- 受注ID
                                      ordercode text NULL, -- 受注番号
                                      registdate timestamp NULL, -- 登録日時
                                      orderreceiveddate timestamp NULL, -- 受注日時
                                      canceldate timestamp NULL, -- 取消日時
                                      latesttransactionid text NULL -- 最新取引ID
);
-- COMMENT TABLE ORDERRECEIVED
COMMENT ON COLUMN public.orderreceived.orderreceivedid IS '受注ID';
COMMENT ON COLUMN public.orderreceived.ordercode IS '受注番号';
COMMENT ON COLUMN public.orderreceived.registdate IS '登録日時';
COMMENT ON COLUMN public.orderreceived.orderreceiveddate IS '受注日時';
COMMENT ON COLUMN public.orderreceived.canceldate IS '取消日時';
COMMENT ON COLUMN public.orderreceived.latesttransactionid IS '最新取引ID';
COMMENT ON TABLE public.orderreceived IS '受注';

-- CREATE TRANSACTION
CREATE TABLE public.TRANSACTION (
                                      transactionid text NOT NULL, -- 取引ID
                                      transactionstatus text NULL, -- 取引ステータス
                                      paidflag bool NULL, -- 入金済みフラグ
                                      insufficientMoneyFlag bool NULL, -- 入金不足フラグ(入金状態詳細)
                                      overMoneyFlag bool NULL, -- 入金超過フラグ(入金状態詳細)
                                      shippedflag bool NULL, -- 出荷済みフラグ
                                      notificationflag bool NULL, -- 入金関連通知実施フラグ
                                      remindersentflag bool NULL, -- 入金督促通知済みフラグ
                                      expiredsentflag bool NULL, -- 入金期限切れ通知済みフラグ
                                      billpaymenterrorflag bool NULL, -- 請求決済エラーフラグ
                                      registdate timestamp NULL, -- 登録日時
                                      customerid text NULL, -- 顧客ID
                                      adminmemo text NULL, -- 管理メモ
                                      orderreceivedid text NULL, -- 受注ID
                                      processtime timestamp NULL, -- 処理日時
                                      processtype text NULL, -- 処理種別
                                      processpersonname text NULL, -- 処理担当者名
                                      preclaimflag bool NULL, -- 前請求フラグ
                                      noveltypresentjudgmentstatus text NULL, -- ノベルティプレゼント判定状態
                                      CONSTRAINT transaction_pk PRIMARY KEY (transactionid)
);
-- COMMENT TABLE TRANSACTION
COMMENT ON COLUMN public."transaction".transactionid IS '取引ID';
COMMENT ON COLUMN public."transaction".transactionstatus IS '取引ステータス';
COMMENT ON COLUMN public."transaction".paidflag IS '入金済みフラグ';
COMMENT ON COLUMN public."transaction".insufficientMoneyFlag IS '入金不足フラグ(入金状態詳細)';
COMMENT ON COLUMN public."transaction".overMoneyFlag IS '入金超過フラグ(入金状態詳細)';
COMMENT ON COLUMN public."transaction".shippedflag IS '出荷済みフラグ';
COMMENT ON COLUMN public."transaction".notificationflag IS '入金関連通知実施フラグ';
COMMENT ON COLUMN public."transaction".remindersentflag IS '入金督促通知済みフラグ';
COMMENT ON COLUMN public."transaction".expiredsentflag IS '入金期限切れ通知済みフラグ';
COMMENT ON COLUMN public."transaction".billpaymenterrorflag IS '請求決済エラーフラグ';
COMMENT ON COLUMN public."transaction".registdate IS '登録日時';
COMMENT ON COLUMN public."transaction".customerid IS '顧客ID';
COMMENT ON COLUMN public."transaction".adminmemo IS '管理メモ';
COMMENT ON COLUMN public."transaction".orderreceivedid IS '受注ID';
COMMENT ON COLUMN public."transaction".processtime IS '処理日時';
COMMENT ON COLUMN public."transaction".processtype IS '処理種別';
COMMENT ON COLUMN public."transaction".processpersonname IS '処理担当者名';
COMMENT ON COLUMN public."transaction".preclaimflag IS '前請求フラグ ';
COMMENT ON COLUMN public."transaction".noveltypresentjudgmentstatus IS 'ノベルティプレゼント判定状態';
COMMENT ON TABLE public.transaction IS '取引';

-- CREATE TRANSACTION FOR REVISION
CREATE TABLE TRANSACTIONFORREVISION (
                                               transactionrevisionid text NOT NULL, -- 改訂用取引ID
                                               transactionid text NOT NULL, -- 改訂元取引ID
                                               transactionstatus text NULL, -- 取引ステータス
                                               paidflag bool NULL, -- 入金済みフラグ
                                               insufficientMoneyFlag bool NULL, -- 入金不足フラグ(入金状態詳細)
                                               overMoneyFlag bool NULL, -- 入金超過フラグ(入金状態詳細)
                                               shippedflag bool NULL, -- 出荷済みフラグ
                                               notificationflag bool NULL, -- 入金関連通知実施フラグ
                                               remindersentflag bool NULL, -- 入金督促通知済みフラグ
                                               expiredsentflag bool NULL, -- 入金期限切れ通知済みフラグ
                                               billpaymenterrorflag bool NULL, -- 請求決済エラーフラグ
                                               registdate timestamp NULL, -- 登録日時
                                               customerid text NULL, -- 顧客ID
                                               adminmemo text NULL, -- 管理メモ
                                               orderreceivedid text NULL, -- 受注ID
                                               processtime timestamp NULL, -- 処理日時
                                               processtype text NULL, -- 処理種別
                                               processpersonname text NULL, -- 処理担当者名
                                               preclaimflag bool NULL, -- 前請求フラグ
                                               noveltypresentjudgmentstatus text NULL, -- ノベルティプレゼント判定状態
                                               CONSTRAINT transactionforrevision_pk PRIMARY KEY (transactionrevisionid)
);
-- COMMENT TABLE TRANSACTION FOR REVISION
COMMENT ON COLUMN public.transactionforrevision.transactionrevisionid IS '改訂用取引ID';
COMMENT ON COLUMN public.transactionforrevision.transactionid IS '改訂元取引ID';
COMMENT ON COLUMN public.transactionforrevision.transactionstatus IS '取引ステータス';
COMMENT ON COLUMN public.transactionforrevision.paidflag IS '入金済みフラグ';
COMMENT ON COLUMN public.transactionforrevision.insufficientMoneyFlag IS '入金不足フラグ(入金状態詳細)';
COMMENT ON COLUMN public.transactionforrevision.overMoneyFlag IS '入金超過フラグ(入金状態詳細)';
COMMENT ON COLUMN public.transactionforrevision.shippedflag IS '出荷済みフラグ';
COMMENT ON COLUMN public.transactionforrevision.notificationflag IS '入金関連通知実施フラグ';
COMMENT ON COLUMN public.transactionforrevision.remindersentflag IS '入金督促通知済みフラグ';
COMMENT ON COLUMN public.transactionforrevision.expiredsentflag IS '入金期限切れ通知済みフラグ';
COMMENT ON COLUMN public.transactionforrevision.billpaymenterrorflag IS '請求決済エラーフラグ';
COMMENT ON COLUMN public.transactionforrevision.registdate IS '登録日時';
COMMENT ON COLUMN public.transactionforrevision.customerid IS '顧客ID';
COMMENT ON COLUMN public.transactionforrevision.adminmemo IS '管理メモ';
COMMENT ON COLUMN public.transactionforrevision.orderreceivedid IS '受注ID';
COMMENT ON COLUMN public.transactionforrevision.processtime IS '処理日時';
COMMENT ON COLUMN public.transactionforrevision.processtype IS '処理種別';
COMMENT ON COLUMN public.transactionforrevision.processpersonname IS '処理担当者名';
COMMENT ON COLUMN public.transactionforrevision.preclaimflag IS '前請求フラグ ';
COMMENT ON COLUMN public.transactionforrevision.noveltypresentjudgmentstatus IS 'ノベルティプレゼント判定状態';
COMMENT ON TABLE public.transactionforrevision IS '改訂用取引';

-- CREATE SEQUENCE
DROP SEQUENCE IF EXISTS "public"."ordercodeseq";
CREATE SEQUENCE ordercodeseq INCREMENT 1 MINVALUE 100000 MAXVALUE 999999 START 100000 CACHE 1 CYCLE;
