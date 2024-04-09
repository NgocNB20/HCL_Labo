/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.entity;

import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 住所録ドメインサービス
 */
@Service
public class AddressBookEntityService {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public AddressBookEntityService(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 顧客情報に紐づく新規住所録を生成する
     *
     * @param customerId   顧客ID
     * @param addressName  住所名
     * @param address      住所
     * @param shippingMemo 配送メモ
     * @param preAddressId 住所ID ※変更前の住所ID
     * @return 新規住所録
     */
    public AddressBookEntity createAddressBookRelateCustomerInfo(String customerId,
                                                                 String addressName,
                                                                 Address address,
                                                                 String shippingMemo,
                                                                 AddressId preAddressId) {

        // 変更前の住所録を取得する
        AddressBookEntity preAddressBookEntity = this.addressBookRepository.get(preAddressId);

        // 変更前の住所録が取得できない場合はエラー
        if (preAddressBookEntity == null) {
            throw new DomainException("LOGISTIC-CPAB0001-E", new String[] {preAddressId.getValue()});
        }

        // 変更前の住所情報と新規登録の住所情報の差分をチェックする
        // 差分がない場合は処理をスキップする
        if (isSameAddress(preAddressBookEntity, address)) {
            return null;
        }

        // 変更後の住所情報で住所録を生成する
        return new AddressBookEntity(customerId, addressName, address, shippingMemo, new Date());
    }

    /**
     * 変更前の住所情報と、新規登録の住所情報の各項目が一致しているかチェック
     *
     * @param preAddressBookEntity 変更前の住所録
     * @param address              変更前の住所
     * @return 差分なし ... true / 差分あり ... false
     */
    public boolean isSameAddress(AddressBookEntity preAddressBookEntity, Address address) {

        // 住所の各項目をチェック
        // 氏名(性)
        if (!compare(
                        address.getFullName().getLastName(),
                        preAddressBookEntity.getAddress().getFullName().getLastName()
                    )) {
            return false;
        }
        // 氏名(名)
        if (!compare(
                        address.getFullName().getFirstName(),
                        preAddressBookEntity.getAddress().getFullName().getFirstName()
                    )) {
            return false;
        }
        // フリガナ(性)
        if (!compare(
                        address.getFurigana().getLastKana(),
                        preAddressBookEntity.getAddress().getFurigana().getLastKana()
                    )) {
            return false;
        }
        // フリガナ(名)
        if (!compare(
                        address.getFurigana().getFirstKana(),
                        preAddressBookEntity.getAddress().getFurigana().getFirstKana()
                    )) {
            return false;
        }
        // 電話番号
        if (!compare(address.getTel(), preAddressBookEntity.getAddress().getTel())) {
            return false;
        }
        // 郵便番号
        if (!compare(address.getZipCode(), preAddressBookEntity.getAddress().getZipCode())) {
            return false;
        }
        // 都道府県
        if (!compare(address.getPrefecture(), preAddressBookEntity.getAddress().getPrefecture())) {
            return false;
        }
        // 住所1
        if (!compare(address.getAddress1(), preAddressBookEntity.getAddress().getAddress1())) {
            return false;
        }
        // 住所2
        if (!compare(address.getAddress2(), preAddressBookEntity.getAddress().getAddress2())) {
            return false;
        }
        // 住所1
        if (!compare(address.getAddress3(), preAddressBookEntity.getAddress().getAddress3())) {
            return false;
        }

        return true;
    }

    /**
     * 渡された項目の値を比較する
     *
     * @param target  対象
     * @param compare 比較対象
     * @return 一致する場合true
     */
    private boolean compare(Object target, Object compare) {

        // Stringの検証
        if (target instanceof String && compare instanceof String) {

            String targetStr = (String) target;
            String compareStr = (String) compare;

            // どちらも未設定
            if (StringUtils.isBlank(targetStr) && StringUtils.isBlank(compareStr)) {
                return true;
            }

            // 一致する場合
            if (StringUtils.isNotBlank(targetStr) && targetStr.equals(compareStr)) {
                return true;
            }
            return false;
        }

        // booleanの検証
        if (target instanceof Boolean && compare instanceof Boolean) {

            boolean targetStr = (Boolean) target;
            boolean compareStr = (Boolean) compare;

            // 一致する場合
            if (targetStr == compareStr) {
                return true;
            }

            return false;
        }

        // Objectの検証
        // どちらも未設定
        if (target == null && compare == null) {
            return true;
        }

        // 一致する場合
        if (target != null && compare != null && target.equals(compare)) {
            return true;
        }

        // 検証対象外の項目の場合
        return false;
    }

}