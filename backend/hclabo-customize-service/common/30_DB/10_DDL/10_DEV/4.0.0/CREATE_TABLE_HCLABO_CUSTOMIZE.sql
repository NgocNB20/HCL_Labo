-- hclabo-customize-service

-- Drop table
DROP TABLE IF EXISTS public.examkit CASCADE;
DROP TABLE IF EXISTS public.examresults CASCADE;

-- CREATE TABLE
-- public.examkit definition
CREATE TABLE public.examkit (
                                examkitcode text NOT NULL, -- 検査キット番号
                                receptiondate timestamp NULL, -- 受付日
                                specimencode text NULL, -- 検体番号
                                examstatus text NULL, -- 検査状態
                                specimencomment text NULL, -- 検体コメント
                                examresultspdf text NULL, -- 検査結果PDF
                                orderitemid text NOT NULL, -- 注文商品ID
                                ordercode text NOT NULL, -- 受注番号
                                CONSTRAINT examkit_pk PRIMARY KEY (examkitcode)
);
COMMENT ON TABLE public.examkit IS '検査キット';

-- Column comments
COMMENT ON COLUMN public.examkit.examkitcode IS '検査キット番号';
COMMENT ON COLUMN public.examkit.receptiondate IS '受付日';
COMMENT ON COLUMN public.examkit.specimencode IS '検体番号';
COMMENT ON COLUMN public.examkit.examstatus IS '検査状態';
COMMENT ON COLUMN public.examkit.specimencomment IS '検体コメント';
COMMENT ON COLUMN public.examkit.examresultspdf IS '検査結果PDF';
COMMENT ON COLUMN public.examkit.orderitemid IS '注文商品ID';
COMMENT ON COLUMN public.examkit.ordercode IS '受注番号';


-- public.examresults definition
CREATE TABLE public.examresults (
                                    examkitcode text NOT NULL, -- 検査キット番号
                                    examitemcode text NOT NULL, -- 検査項目コード
                                    examitemname text NULL, -- 検査項目名称
                                    abnormalvaluetype text NULL, -- 異常値区分
                                    examresultvalue text NULL, -- 検査結果値
                                    unit text NULL, -- 単位
                                    standardvalue text NULL, -- 表示基準値
                                    comment1 text NULL, -- 結果補助コメント１内容
                                    comment2 text NULL, -- 結果補助コメント２内容
                                    examcompletedflag text NULL, -- 検査完了フラグ
                                    examcompleteddate timestamp NULL, -- 検査完了日
                                    orderdisplay float8 NOT NULL, -- 表示順
                                    CONSTRAINT examresults_pk PRIMARY KEY (examkitcode, examitemcode)
);
COMMENT ON TABLE public.examresults IS '検査結果';

-- Column comments
COMMENT ON COLUMN public.examresults.examkitcode IS '検査キット番号';
COMMENT ON COLUMN public.examresults.examitemcode IS '検査項目コード';
COMMENT ON COLUMN public.examresults.examitemname IS '検査項目名称';
COMMENT ON COLUMN public.examresults.abnormalvaluetype IS '異常値区分';
COMMENT ON COLUMN public.examresults.examresultvalue IS '検査結果値';
COMMENT ON COLUMN public.examresults.unit IS '単位';
COMMENT ON COLUMN public.examresults.standardvalue IS '表示基準値';
COMMENT ON COLUMN public.examresults.comment1 IS '結果補助コメント１内容';
COMMENT ON COLUMN public.examresults.comment2 IS '結果補助コメント２内容';
COMMENT ON COLUMN public.examresults.examcompletedflag IS '検査完了フラグ';
COMMENT ON COLUMN public.examresults.examcompleteddate IS '検査完了日';
COMMENT ON COLUMN public.examresults.orderdisplay IS '表示順';

-- CREATE SEQUENCE
DROP SEQUENCE IF EXISTS "public"."examkitcodeseq";
CREATE SEQUENCE examkitcodeseq INCREMENT 1 MINVALUE 100000 MAXVALUE 999999 START 100000 CACHE 1 CYCLE;
