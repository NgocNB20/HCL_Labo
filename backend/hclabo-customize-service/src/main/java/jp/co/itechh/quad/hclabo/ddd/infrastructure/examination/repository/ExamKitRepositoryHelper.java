package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.OrderDisplay;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamKitDbEntity;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamResultsDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  検査キットリポジトリHelperクラス<br/>
 *
 */
@Component
public class ExamKitRepositoryHelper {

    /**
     * 検査キットDbエンティティに変換
     *
     * @param examKitEntity 検査キットエンティティ
     * @return 検査キットDbエンティティ
     */
    public ExamKitDbEntity toExamKitDbEntity(ExamKitEntity examKitEntity) {

        if (examKitEntity == null) {
            return null;
        }

        ExamKitDbEntity examKitDbEntity = new ExamKitDbEntity();

        examKitDbEntity.setExamKitCode(examKitEntity.getExamKitCode().getValue());
        examKitDbEntity.setReceptionDate(examKitEntity.getReceptionDate());
        examKitDbEntity.setSpecimenCode(examKitEntity.getSpecimenCode());
        examKitDbEntity.setExamStatus(examKitEntity.getExamStatus());
        examKitDbEntity.setSpecimenComment(examKitEntity.getSpecimenComment());
        examKitDbEntity.setExamResultsPdf(examKitEntity.getExamResultsPdf());
        examKitDbEntity.setOrderItemId(examKitEntity.getOrderItemId());
        examKitDbEntity.setOrderCode(examKitEntity.getOrderCode());

        return examKitDbEntity;
    }

    /**
     * 検査結果リストに変換
     *
     * @param examResultsDbEntityList 検査結果Dbエンティティリスト
     * @return 注文商品リスト
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
