/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.usecase.examination;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCodeFactory;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 検査キット登録ユースケース
 */
@Service
public class ExamKitRegistrationUseCase {

    /** 検査キットリポジトリー */
    private final IExamKitRepository examKitRepository;

    /** 検査キット番号ファクトリ */
    private final ExamKitCodeFactory examKitCodeFactory;

    @Autowired
    public ExamKitRegistrationUseCase(IExamKitRepository examKitRepository, ExamKitCodeFactory examKitCodeFactory) {
        this.examKitRepository = examKitRepository;
        this.examKitCodeFactory = examKitCodeFactory;
    }

    /**
     * 検査キット登録
     */
    @Transactional(rollbackFor = Exception.class)
    public void registExamKit(String orderCode, List<String> orderItemIdList) {

        AssertChecker.assertNotEmpty("orderItemIdList is empty", orderItemIdList);
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        orderItemIdList.forEach(orderItemId -> {

            ExamKitCode examKitCode = examKitCodeFactory.constructExamKitCode(orderCode);

            ExamKitEntity examKitEntity = examKitRepository.getByExamKitCode(examKitCode.getValue());
            // 検査キット番号が検査キットテーブルに存在する場合はエラーとする
            if (examKitEntity != null) {
                throw new DomainException("HCLABO-CUSTOMIZE-EXN0001-E", new String[]{examKitCode.getValue()});
            }

            examKitEntity = examKitRepository.getByOrderItemId(orderItemId);
            // 注文商品IDが検査キットテーブルに存在する場合はエラーとする
            if (examKitEntity != null) {
                throw new DomainException("HCLABO-CUSTOMIZE-EXN0002-E", new String[]{orderItemId});
            }

            examKitEntity = new ExamKitEntity(examKitCode, orderItemId, orderCode);

            examKitRepository.save(examKitEntity);
        });
    }
}
