/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.presentation.examination;

import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsResponse;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 検査Helperクラス
 *
 */
@Component
public class ExaminationHelper {

    /**
     * 検査キットレスポンスに変換
     *
     * @param examKitEntityList 検査キットエンティティリスト
     * @return 検査キットレスポンス
     */
    public ExamKitListResponse toExamKitListResponse(List<ExamKitEntity> examKitEntityList) {

        if (CollectionUtils.isEmpty(examKitEntityList)) {
            return null;
        }

        ExamKitListResponse examKitListResponse = new ExamKitListResponse();
        List<ExamKitResponse> examKitList = new ArrayList<>();

        examKitEntityList.forEach(examKitEntity -> {
            ExamKitResponse examKitResponse = new ExamKitResponse();
            examKitResponse.setExamKitCode(examKitEntity.getExamKitCode().getValue());
            examKitResponse.setReceptionDate(examKitEntity.getReceptionDate());
            examKitResponse.setSpecimenCode(examKitEntity.getSpecimenCode());
            examKitResponse.setExamStatus(EnumTypeUtil.getValue(examKitEntity.getExamStatus()));
            examKitResponse.setSpecimenComment(examKitEntity.getSpecimenComment());
            examKitResponse.setExamResultsPdf(examKitEntity.getExamResultsPdf());
            examKitResponse.setOrderItemId(examKitEntity.getOrderItemId());
            examKitResponse.setOrderCode(examKitEntity.getOrderCode());
            examKitResponse.setExamResultList(
                    this.toExamKitListResponseExamResultList(examKitEntity.getExamResultList()));

            examKitList.add(examKitResponse);
        });

        examKitListResponse.setExamKitList(examKitList);

        return examKitListResponse;
    }
    /**
     * 検査結果レスポンスに変換
     *
     * @param examResultList 検査結果リスト
     * @return 検査結果レスポンス
     */
    private List<ExamResultsResponse> toExamKitListResponseExamResultList(List<ExamResult> examResultList) {
        if (CollectionUtils.isEmpty(examResultList)) {
            return null;
        }
        List<ExamResultsResponse> examKitListResponseExamResultList = new ArrayList<>();

        examResultList.forEach(examResult -> {
            ExamResultsResponse examResultResponse = new ExamResultsResponse();
            examResultResponse.setExamItemCode(examResult.getExamItemCode());
            examResultResponse.setExamItemName(examResult.getExamItemName());
            examResultResponse.setAbnormalValueType(EnumTypeUtil.getValue(examResult.getAbnormalValueType()));
            examResultResponse.setExamResultValue(examResult.getExamResultValue());
            examResultResponse.setUnit(examResult.getUnit());
            examResultResponse.setStandardvalue(examResult.getStandardValue());
            examResultResponse.setComment1(examResult.getComment1());
            examResultResponse.setComment2(examResult.getComment2());
            examResultResponse.setExamcompletedflag(EnumTypeUtil.getValue(examResult.getExamCompletedFlag()));
            examResultResponse.setExamCompletedDate(examResult.getExamCompletedDate());

            examKitListResponseExamResultList.add(examResultResponse);
        });

        return examKitListResponseExamResultList;
    }

}