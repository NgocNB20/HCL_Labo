/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 住所録削除ユースケース
 */
@Service
public class DeleteAddressBookUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAddressBookUseCase.class);

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public DeleteAddressBookUseCase(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 住所録を削除する<br/>
     * ※新規会員登録や会員情報変更に失敗したときのみ呼ばれる想定で実装
     * <pre>
     *   削除前住所IDが指定された場合、該当する住所録のデフォルト指定フラグを復元
     * </pre>
     *
     * @param addressId 住所ID
     * @param preAddressId 削除前住所ID
     */
    public void deleteAddressBook(String addressId, String preAddressId) {

        // 住所IDが設定されていない場合はエラー
        AssertChecker.assertNotEmpty("addressId is empty", addressId);

        // 削除前の住所録のデフォルト指定フラグを復元
        int result;
        if (StringUtils.isNotBlank(preAddressId)) {
            result = addressBookRepository.updateDefault(new AddressId(preAddressId));
            if (result != 1) {
                LOGGER.error("住所録のデフォルト表示フラグの復元に失敗しました。住所ID：" + preAddressId);
                // 万が一失敗しても、再度会員情報修正すればデフォルト指定フラグは戻るので、処理は続行する
                // 会員の住所録が2件になったまま放置する方が問題が大きい
            }
        }

        // 住所録を削除
        result = addressBookRepository.delete(new AddressId(addressId));

        if (result != 1) {
            // TODO このメッセージ（住所を削除できません。）を表示しても、お客様は何のことかわからないのでは？
            //   会員の新規登録、もしくは、会員情報修正の失敗時にここを通る
            //   単なるdeleteで失敗する可能性はまずないと思われるため、タスクコメントのみで留めておく
            throw new DomainException("LOGISTIC-DEAB0001-E", new String[] {addressId});
        }
    }

}
