/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 住所録非公開ユースケース
 */
@Service
public class CloseAddressBookUseCase {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public CloseAddressBookUseCase(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 住所録を非公開にする
     *
     * @param addressId           住所ID
     * @param addressIdByCustomer 顧客に紐づく住所ID
     */
    public void closeAddressBook(String addressId, String addressIdByCustomer) {

        // 住所録を取得する
        AddressBookEntity addressBookEntity = this.addressBookRepository.get(new AddressId(addressId));

        // バリデーションチェック
        ApplicationException appException = new ApplicationException();

        // 住所録が取得できない場合はエラー
        if (addressBookEntity == null) {
            // 住所録取得失敗
            throw new DomainException("LOGISTIC-ADDB0001-E", new String[] {addressId});
        }

        // 渡された住所IDが顧客に紐づく住所IDに一致する場合はエラー
        if (StringUtils.isNotEmpty(addressId) && addressId.equals(addressIdByCustomer)) {
            appException.addMessage("LOGISTIC-ADID0001-E");
        }

        if (appException.hasMessage()) {
            throw appException;
        }

        // 住所録を非公開にする
        addressBookEntity.close();

        // 住所録を更新する
        this.addressBookRepository.update(addressBookEntity);
    }

}