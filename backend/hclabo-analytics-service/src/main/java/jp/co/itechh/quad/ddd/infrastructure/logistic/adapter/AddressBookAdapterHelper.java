package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import org.springframework.stereotype.Component;

/**
 * 住所録アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AddressBookAdapterHelper {

    /**
     * 住所録に変換
     *
     * @param addressBookAddressResponse 住所レスポンス
     */
    public AddressBook toAddressBook(AddressBookAddressResponse addressBookAddressResponse) {

        if (addressBookAddressResponse == null) {
            return null;
        }

        AddressBook addressBook = new AddressBook();

        addressBook.setLastName(addressBookAddressResponse.getLastName());
        addressBook.setFirstName(addressBookAddressResponse.getFirstName());
        addressBook.setLastKana(addressBookAddressResponse.getLastKana());
        addressBook.setFirstKana(addressBookAddressResponse.getFirstKana());
        addressBook.setTel(addressBookAddressResponse.getTel());
        addressBook.setZipCode(addressBookAddressResponse.getZipCode());
        addressBook.setPrefecture(addressBookAddressResponse.getPrefecture());
        addressBook.setAddress1(addressBookAddressResponse.getAddress1());
        addressBook.setAddress2(addressBookAddressResponse.getAddress2());
        addressBook.setAddress3(addressBookAddressResponse.getAddress3());
        addressBook.setShippingMemo(addressBookAddressResponse.getShippingMemo());

        return addressBook;
    }
}
