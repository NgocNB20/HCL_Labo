/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.entity;

import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.Date;
import java.util.List;

/**
 * 検査キットエンティティ
 */
@Getter
public class ExamKitEntity {

    /** 検査キット番号 */
    protected ExamKitCode examKitCode;

    /** 受付日 */
    protected Date receptionDate;

    /** 検体番号 */
    protected String specimenCode;

    /** 検査状態 */
    protected HTypeExamStatus examStatus;

    /** 検体コメント */
    protected String specimenComment;

    /** 検査結果PDF */
    protected String examResultsPdf;

    /** 注文商品ID */
    protected String orderItemId;

    /** 受注番号 */
    protected String orderCode;

    /** 検査結果リスト */
    protected List<ExamResult> examResultList;

    /**
     * コンストラクタ
     * 検査キット登録
     *
     * @param examKitCode 検査キット番号
     * @param orderItemId 注文商品ID
     * @param orderCode   受注番号
     */
    public ExamKitEntity(ExamKitCode examKitCode, String orderItemId, String orderCode) {

        // アサートチェック
        AssertChecker.assertNotNull("examKitCode is null", examKitCode);
        AssertChecker.assertNotEmpty("orderItemId is empty", orderItemId);
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        this.examKitCode = examKitCode;
        this.examStatus = HTypeExamStatus.WAITING_RETURN;
        this.orderItemId = orderItemId;
        this.orderCode = orderCode;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     *
     * @param examKitCode     検査キット番号
     * @param receptionDate   受付日
     * @param specimenCode    検体番号
     * @param examStatus      検査状態
     * @param specimenComment 検体コメント
     * @param examResultsPdf  検査結果PDF
     * @param orderItemId     注文商品ID
     * @param orderCode       受注番号
     * @param examResultList  注文商品リスト
     */
    public ExamKitEntity(ExamKitCode examKitCode, Date receptionDate, String specimenCode, HTypeExamStatus examStatus,
                         String specimenComment, String examResultsPdf, String orderItemId, String orderCode,
                         List<ExamResult> examResultList) {
        this.examKitCode = examKitCode;
        this.receptionDate = receptionDate;
        this.specimenCode = specimenCode;
        this.examStatus = examStatus;
        this.specimenComment = specimenComment;
        this.examResultsPdf = examResultsPdf;
        this.orderItemId = orderItemId;
        this.orderCode = orderCode;
        this.examResultList = examResultList;
    }



    /**
     * 受領済み.
     */
    public void receivedStatus() {
        this.examStatus = HTypeExamStatus.RECEIVED;
    }

    /**
     * 検査状態の更新
     *
     * @param receptionDate   受付日
     * @param specimenCode    検体番号
     * @param completedFlag   検査完了フラグ
     * @param specimenComment 検体コメント
     */
    public void settingExamStatus(Date receptionDate, String specimenCode, String completedFlag, String specimenComment) {

        // アサートチェック
        AssertChecker.assertNotNull("receptionDate is null", receptionDate);
        AssertChecker.assertNotEmpty("completedFlag is empty", completedFlag);
        AssertChecker.assertNotEmpty("specimenCode is empty", specimenCode);

        this.receptionDate = receptionDate;
        this.specimenCode = specimenCode;
        this.examStatus = EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, completedFlag);
        this.specimenComment = specimenComment;
    }

    /**
     * 検査結果PDF名の更新
     *
     * @param pdfFileName PDFのファイル名
     */
    public void updatePdfsFileName(String pdfFileName) {
        // アサートチェック
        AssertChecker.assertNotEmpty("pdfFileName is empty", pdfFileName);

        this.examResultsPdf = pdfFileName;
    }
}