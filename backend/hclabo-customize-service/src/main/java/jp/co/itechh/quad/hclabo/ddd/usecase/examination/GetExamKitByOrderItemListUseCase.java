/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.usecase.examination;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文商品IDリストをもとに検査キットを取得するユースケース
 */
@Service
public class GetExamKitByOrderItemListUseCase {

    /** 検査キットリポジトリー */
    private final IExamKitRepository examKitRepository;

    /**
     * コンストラクタ
     */
    @Autowired
    public GetExamKitByOrderItemListUseCase(IExamKitRepository examKitRepository) {
        this.examKitRepository = examKitRepository;
    }

    /**
     * 注文商品IDリストをもとに検査キットを取得する
     */
    public List<ExamKitEntity> getExamKitByOrderItemList(List<String> orderItemIdList) {
        // アサートチェック
        AssertChecker.assertNotEmpty("orderItemIdList is empty", orderItemIdList);

        return examKitRepository.getExamKitEntityList(orderItemIdList);
    }
}
