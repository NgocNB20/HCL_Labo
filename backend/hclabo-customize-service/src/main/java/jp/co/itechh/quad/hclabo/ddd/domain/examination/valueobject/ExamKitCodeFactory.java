/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 検査キット番号 値オブジェクト ファクトリ
 */
@Component
public class ExamKitCodeFactory {

    /** 検査キットリポジトリー */
    private final IExamKitRepository examKitRepository;

    /**
     * コンストラクタ
     */
    @Autowired
    public ExamKitCodeFactory(IExamKitRepository examKitRepository) {
        this.examKitRepository = examKitRepository;
    }

    /**
     * 検査キット番号 値オブジェクト生成
     *
     * @param orderCode 受注番号
     * @return 検査キット番号
     */
    public ExamKitCode constructExamKitCode(String orderCode) {
        //アサートチェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        // 検査キット番号用連番取得（ランダムな数値6桁）
        Random random = new Random();
        String examCodeSuffix = String.format("%06d", random.nextInt(1000000));

        return new ExamKitCode(orderCode + examCodeSuffix);
    }

}
