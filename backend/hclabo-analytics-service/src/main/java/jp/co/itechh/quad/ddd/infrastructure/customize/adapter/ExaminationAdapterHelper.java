package jp.co.itechh.quad.ddd.infrastructure.customize.adapter;

import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamKit;
import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamResults;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 検査アダプターHelperクラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Component
public class ExaminationAdapterHelper {

    /**
     * 検査結果リストに変換
     *
     * @param examKitResponseList 検査キットレスポンスリスト
     * @return 検査キットリスト
     */
    public List<ExamKit> toExamKitList(List<ExamKitResponse> examKitResponseList) {

        if (CollectionUtils.isEmpty(examKitResponseList)) {
            return new ArrayList<>();
        }

        return examKitResponseList.stream().map(examKitResponse -> {
            ExamKit examKit = new ExamKit();

            examKit.setExamKitCode(examKitResponse.getExamKitCode());
            examKit.setReceptionDate(examKitResponse.getReceptionDate());
            examKit.setSpecimenCode(examKitResponse.getSpecimenCode());
            examKit.setExamStatus(examKitResponse.getExamStatus());
            examKit.setSpecimenComment(examKitResponse.getSpecimenComment());
            examKit.setExamResultsPdf(examKitResponse.getExamResultsPdf());
            examKit.setOrderItemId(examKitResponse.getOrderItemId());
            examKit.setOrderCode(examKitResponse.getOrderCode());

            List<ExamResults> examResultsList = this.toExamResultList(examKitResponse.getExamResultList());
            examKit.setExamResultList(examResultsList);

            return examKit;
        }).collect(Collectors.toList());
    }

    /**
     * 検査結果リストに変換
     *
     * @param examResultsResponseList 検査結果レスポンスリスト
     * @return 検査結果リスト
     */
    public List<ExamResults> toExamResultList(List<ExamResultsResponse> examResultsResponseList) {

        if (CollectionUtils.isEmpty(examResultsResponseList)) {
            return new ArrayList<>();
        }

        return examResultsResponseList.stream().map(examResultsResponse -> {
            ExamResults examResults = new ExamResults();

            examResults.setExamItemCode(examResultsResponse.getExamItemCode());
            examResults.setExamItemName(examResultsResponse.getExamItemName());
            examResults.setAbnormalValueType(examResultsResponse.getAbnormalValueType());
            examResults.setExamResultValue(examResultsResponse.getExamResultValue());
            examResults.setUnit(examResultsResponse.getUnit());
            examResults.setStandardvalue(examResultsResponse.getStandardvalue());
            examResults.setComment1(examResultsResponse.getComment1());
            examResults.setComment2(examResultsResponse.getComment2());
            examResults.setExamcompletedflag(examResultsResponse.getExamcompletedflag());
            examResults.setExamCompletedDate(examResultsResponse.getExamCompletedDate());

            return examResults;
        }).collect(Collectors.toList());
    }
}
