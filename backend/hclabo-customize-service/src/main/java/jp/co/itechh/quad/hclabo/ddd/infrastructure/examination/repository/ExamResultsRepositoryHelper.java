package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.OrderDisplay;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamResultsDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  検査結果リポジトリHelperクラス<br/>
 *
 */
@Component
public class ExamResultsRepositoryHelper {

    /**
     * 検査結果Dbエンティティリストに変換
     *
     * @param examResultList 検査結果リスト
     * @return 検査結果Dbエンティティリスト
     */
    public List<ExamResultsDbEntity> toExamResultsDbEntityListForRegist(List<ExamResult> examResultList) {

        if (CollectionUtils.isEmpty(examResultList)) {
            return new ArrayList<>();
        }
        List<ExamResultsDbEntity> examResultsDbEntityList = new ArrayList<>();

        for (int i = 0; i < examResultList.size(); i++) {
            ExamResultsDbEntity examResultsDbEntity = new ExamResultsDbEntity();
            examResultsDbEntity.setExamKitCode(examResultList.get(i).getExamKitCode().getValue());
            examResultsDbEntity.setExamItemCode(examResultList.get(i).getExamItemCode());
            examResultsDbEntity.setExamItemName(examResultList.get(i).getExamItemName());
            examResultsDbEntity.setAbnormalValueType(examResultList.get(i).getAbnormalValueType());
            examResultsDbEntity.setExamResultValue(examResultList.get(i).getExamResultValue());
            examResultsDbEntity.setUnit(examResultList.get(i).getUnit());
            examResultsDbEntity.setStandardValue(examResultList.get(i).getStandardValue());
            examResultsDbEntity.setComment1(examResultList.get(i).getComment1());
            examResultsDbEntity.setComment2(examResultList.get(i).getComment2());
            examResultsDbEntity.setExamCompletedFlag(examResultList.get(i).getExamCompletedFlag());
            examResultsDbEntity.setExamCompletedDate(examResultList.get(i).getExamCompletedDate());
            examResultsDbEntity.setOrderDisplay(i + 1);

            examResultsDbEntityList.add(examResultsDbEntity);
        }

        return examResultsDbEntityList;
    }


    /**
     * 検査結果リスト
     *
     * @param examResultsDbEntityList 検査結果Dbエンティティリスト
     * @return 検査結果リスト
     */
    public List<ExamResult> toExamResultList(List<ExamResultsDbEntity> examResultsDbEntityList) {

        if (CollectionUtils.isEmpty(examResultsDbEntityList)) {
            return null;
        }

        return examResultsDbEntityList.stream().map(examDbResult ->
                new ExamResult(new ExamKitCode(examDbResult.getExamKitCode()),
                        examDbResult.getExamItemCode(),
                        examDbResult.getExamItemName(),
                        examDbResult.getAbnormalValueType(),
                        examDbResult.getExamResultValue(),
                        examDbResult.getUnit(),
                        examDbResult.getStandardValue(),
                        examDbResult.getComment1(),
                        examDbResult.getComment2(),
                        examDbResult.getExamCompletedFlag(),
                        examDbResult.getExamCompletedDate(),
                        new OrderDisplay((int) examDbResult.getOrderDisplay())
                )).collect(Collectors.toList());
    }

}
