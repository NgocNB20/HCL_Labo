package jp.co.itechh.quad.hclabo.ddd.usecase.examination.service;

import jp.co.itechh.quad.hclabo.core.constant.OrderStatus;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamResultsRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dto.ExamResultsCsvDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 検査結果CSV登録処理.
 */
@Service
public class ExamResultsCsvSingleExecuter {

    /** 検査キットリポジトリー */
    private final IExamKitRepository examKitRepository;

    /** 検査結果トリポジトリー */
    private final IExamResultsRepository examResultsRepository;

    /** 受注アダプター */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /** コンストラクタ */
    @Autowired
    public ExamResultsCsvSingleExecuter(IExamKitRepository examKitRepository,
                                        IExamResultsRepository examResultsRepository,
                                        IOrderReceivedAdapter orderReceivedAdapter) {
        this.examKitRepository = examKitRepository;
        this.examResultsRepository = examResultsRepository;
        this.orderReceivedAdapter = orderReceivedAdapter;
    }

    /**
     * 検査結果CSV登録処理みにするProcessor
     *
     * @param examResultsCsvDtoList
     * @param messageDto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void execute(List<ExamResultsCsvDto> examResultsCsvDtoList, ExamResultsEntryMessageDto messageDto) {
        // チェック
        AssertChecker.assertNotEmpty("examResultsCsvDtoList is empty", examResultsCsvDtoList);

        // 検査を重ねるごとに検体コメントが変更されるため（最終レコードが正しい検体コメントとなる）
        ExamResultsCsvDto examResultsCsvDto = examResultsCsvDtoList.get(examResultsCsvDtoList.size() - 1);
        ExamKitEntity examKitEntity = examKitRepository.getByExamKitCode(examResultsCsvDto.getExamKitCode());

        // 検査キット番号に該当する検査キットが存在しない場合
        if (examKitEntity == null) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0001-E", new String[]{examResultsCsvDto.getExamKitCode()});
        }

        // 検査状態が「返送待ち」の場合
        if (HTypeExamStatus.WAITING_RETURN == examKitEntity.getExamStatus()) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0002-E",
                    new String[]{examResultsCsvDto.getExamKitCode(),
                            EnumTypeUtil.getLabelValue(examKitEntity.getExamStatus())});
        }

        OrderReceived orderReceived = orderReceivedAdapter.getByOrderCode(examKitEntity.getOrderCode());
        // 受注番号に該当する受注が存在しない場合
        if (orderReceived == null) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0003-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), examKitEntity.getOrderCode()});
        }

        String orderStatus = orderReceived.getOrderStatus();
        // 注文状態が「出荷完了」、「キャンセル」以外の場合
        if (!OrderStatus.SHIPMENT_COMPLETION.name().equals(orderStatus)
            && !OrderStatus.CANCEL.name().equals(orderStatus)) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0004-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), orderReceived.getOrderStatus()});
        }

        // 検査状態の更新
        examKitEntity.settingExamStatus(examResultsCsvDto.getReceptionDate(),
                examResultsCsvDto.getSpecimenCode(),
                EnumTypeUtil.getValue(examResultsCsvDto.getExamCompletedFlag()),
                examResultsCsvDto.getSpecimenComment());
        try {
            examKitRepository.update(examKitEntity);
        } catch (Exception e) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0005-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), e.getMessage()});
        }

        List<ExamResult> examResultListByExamCode = examResultsRepository.getByExamKitCode(
                examResultsCsvDto.getExamKitCode());
        if (CollectionUtils.isNotEmpty(examResultListByExamCode)) {
            examResultsRepository.deleteByExamKitCode(examResultsCsvDto.getExamKitCode());
        }

        // 検査結果リストに変換
        List<ExamResult> examResultList = examResultsCsvDtoList.stream().map(csvDto ->
                new ExamResult(
                        new ExamKitCode(csvDto.getExamKitCode()),
                        csvDto.getExamItemCode(),
                        csvDto.getExamItemName(),
                        csvDto.getAbnormalValueType(),
                        csvDto.getExamResultValue(),
                        csvDto.getUnit(),
                        csvDto.getStandardValue(),
                        csvDto.getComment1(),
                        csvDto.getComment2(),
                        csvDto.getExamCompletedFlag(),
                        csvDto.getExamCompletedDate()
                )).collect(Collectors.toList());

        try {
            examResultsRepository.save(examResultList);
        } catch (Exception e) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0005-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), e.getMessage()});
        }

        if (messageDto.getOrderReceivedList() == null) {
            messageDto.setOrderReceivedList(new ArrayList<>());
        }

        // 重複をチェックする
        Boolean isDuplicateOrderReceived = messageDto.getOrderReceivedList()
                .stream().anyMatch(item -> item.getOrderCode().equals(orderReceived.getOrderCode()));
        if (Boolean.FALSE.equals(isDuplicateOrderReceived)) {
            messageDto.getOrderReceivedList().add(orderReceived);
        }
    }
}
