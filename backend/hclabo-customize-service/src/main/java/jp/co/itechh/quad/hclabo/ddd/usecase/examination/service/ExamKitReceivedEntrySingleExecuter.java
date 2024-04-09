package jp.co.itechh.quad.hclabo.ddd.usecase.examination.service;

import jp.co.itechh.quad.hclabo.core.constant.OrderStatus;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeOrderStatusDdd;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ExamKitReceivedEntrySingleExecuter.
 */
@Service
public class ExamKitReceivedEntrySingleExecuter {

    /** 検査キットリポジトリー */
    private final IExamKitRepository examKitRepository;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** コンストラクタ */
    @Autowired
    public ExamKitReceivedEntrySingleExecuter(IExamKitRepository examKitRepository, OrderReceivedApi orderReceivedApi) {
        this.examKitRepository = examKitRepository;
        this.orderReceivedApi = orderReceivedApi;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public String execute(String examKitCode) {

        // 1.1. 検査キット情報取得
        ExamKitEntity examKitEntity = examKitRepository.getByExamKitCode(examKitCode);
        if (examKitEntity == null) {
            throw new DomainException("ORDER-EXAMKIT01-E", new String[]{examKitCode});
        }
        if (!HTypeExamStatus.WAITING_RETURN.equals(examKitEntity.getExamStatus())) {
            throw new DomainException("ORDER-EXAMKIT02-E", new String[]{examKitCode,
                examKitEntity.getExamStatus().getLabel()});
        }
        // 1.2. 受注情報取得
        OrderReceivedResponse orderReceivedResponse =
            orderReceivedApi.getByOrderCode(examKitEntity.getOrderCode());
        if (orderReceivedResponse == null) {
            throw new DomainException("ORDER-EXAMKIT03-E",
                new String[]{examKitCode, examKitEntity.getOrderCode()});
        }
        // 1.3. 注文状況チェック
        String orderStatus = orderReceivedResponse.getOrderStatus();
        if (!OrderStatus.SHIPMENT_COMPLETION.name().equals(orderStatus)
            && !OrderStatus.CANCEL.name().equals(orderStatus)) {
            HTypeOrderStatusDdd orderStatusDdd
                = EnumTypeUtil.getEnumFromName(HTypeOrderStatusDdd.class, orderStatus);
            String labelOrderStatus = orderStatusDdd != null ? orderStatusDdd.getLabel() : orderStatus;
            throw new DomainException("ORDER-EXAMKIT04-E",
                new String[]{examKitCode, examKitEntity.getOrderCode(), labelOrderStatus});
        }
        // 1.4. 検査状態の更新
        examKitEntity.receivedStatus();
        examKitRepository.update(examKitEntity);

        return orderReceivedResponse.getOrderReceivedId();
    }
}
