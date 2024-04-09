/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 住所録一括非公開ユースケース
 */
@Service
public class CloseAllAddressBookUseCase {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public CloseAllAddressBookUseCase(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 住所録を一括非公開にする
     *
     * @param customerId 顧客ID
     */
    public void closeAllAddressBook(String customerId) {

        // 顧客IDが存在しない場合はエラー
        AssertChecker.assertNotEmpty("customerId is empty", customerId);

        // 住所録を一括非公開にする
        int result = this.addressBookRepository.updateAllClose(customerId);

        if (result == 0) {
            throw new DomainException("LOGISTIC-DEAB0002-E", new String[] {customerId});
        }
    }

}