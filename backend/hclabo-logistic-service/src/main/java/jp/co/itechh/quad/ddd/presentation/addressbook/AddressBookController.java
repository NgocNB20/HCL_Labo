/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.addressbook;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.addressbook.presentation.api.ShippingsApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCloseRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerExclusionRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookDeleteRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.usecase.addressbook.AddAddressUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.CloseAddressBookUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.CloseAllAddressBookUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.DeleteAddressBookUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.GetAddressListUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.GetAddressUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.RegistAddressRelateCustomerInfoUseCase;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 住所録エンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class AddressBookController extends AbstractController implements ShippingsApi {

    /** 住所取得ユースケース */
    private final GetAddressUseCase getAddressUseCase;

    /** 住所リスト取得ユースケース */
    private final GetAddressListUseCase getAddressListUseCase;

    /** 住所追加ユースケース */
    private final AddAddressUseCase addAddressUseCase;

    /** 住所録削除ユースケース */
    private final DeleteAddressBookUseCase deleteAddressBookUseCase;

    /** 住所録非公開ユースケース */
    private final CloseAddressBookUseCase closeAddressBookUseCase;

    /** 住所録一括非公開ユースケース */
    private final CloseAllAddressBookUseCase closeAllAddressBookUseCase;

    /** 顧客住所登録ユースケース */
    private final RegistAddressRelateCustomerInfoUseCase registAddressRelateCustomerInfoUseCase;

    /** ヘルパー */
    private final AddressBookHelper addressBookHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** コンストラクタ */
    @Autowired
    public AddressBookController(GetAddressUseCase getAddressUseCase,
                                 GetAddressListUseCase getAddressListUseCase,
                                 AddAddressUseCase addAddressUseCase,
                                 DeleteAddressBookUseCase deleteAddressBookUseCase,
                                 CloseAddressBookUseCase closeAddressBookUseCase,
                                 CloseAllAddressBookUseCase closeAllAddressBookUseCase,
                                 RegistAddressRelateCustomerInfoUseCase registAddressRelateCustomerInfoUseCase,
                                 AddressBookHelper addressBookHelper,
                                 HeaderParamsUtility headerParamsUtil) {
        this.getAddressUseCase = getAddressUseCase;
        this.getAddressListUseCase = getAddressListUseCase;
        this.addAddressUseCase = addAddressUseCase;
        this.deleteAddressBookUseCase = deleteAddressBookUseCase;
        this.closeAddressBookUseCase = closeAddressBookUseCase;
        this.closeAllAddressBookUseCase = closeAllAddressBookUseCase;
        this.registAddressRelateCustomerInfoUseCase = registAddressRelateCustomerInfoUseCase;
        this.addressBookHelper = addressBookHelper;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * GET /shippings/addressbooks/{addressId} : 住所取得
     *
     * @param addressId 住所ID (required)
     * @return 住所レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AddressBookAddressResponse> getAddressById(
                    @ApiParam(value = "住所ID", required = true) @PathVariable("addressId") String addressId) {

        AddressBookEntity entity = this.getAddressUseCase.getAddress(addressId);

        return new ResponseEntity<>(this.addressBookHelper.toAddressBookAddressResponse(entity), HttpStatus.OK);
    }

    /**
     * GET /shippings/addressbooks : 住所リスト取得
     *
     * @param addressBookCustomerExclusionRequest 会員住所除外リクエスト (required)
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 住所リストレスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AddressBookAddressListResponse> get(@ApiParam(value = "会員住所除外リクエスト", required = true)
                                                                              AddressBookCustomerExclusionRequest addressBookCustomerExclusionRequest,
                                                              PageInfoRequest pageInfoRequest) {

        AddressModel addressList = this.getAddressListUseCase.getAddressList(
                        addressBookCustomerExclusionRequest.getAddressBookCustomerExclusionFlag(), getCustomerId(),
                        pageInfoRequest
                                                                            );

        return new ResponseEntity<>(
                        this.addressBookHelper.toAddressBookAddressListResponse(addressList), HttpStatus.OK);
    }

    /**
     * POST /shippings/addressbooks : 住所登録
     *
     * @param addressBookAddressRegistRequest 住所登録リクエスト (required)
     * @return 住所登録レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AddressBookAddressRegistResponse> regist(
                    @ApiParam(value = "住所登録リクエスト", required = true) @Valid @RequestBody
                                    AddressBookAddressRegistRequest addressBookAddressRegistRequest) {

        if (addressBookAddressRegistRequest.getHideFlag() == null) {
            addressBookAddressRegistRequest.setHideFlag(false);
        }
        if (addressBookAddressRegistRequest.getDefaultFlag() == null) {
            addressBookAddressRegistRequest.setDefaultFlag(false);
        }
        String addressId = this.addAddressUseCase.addAddress(getCustomerId(),
                                                             addressBookAddressRegistRequest.getAddressName(),
                                                             addressBookAddressRegistRequest.getLastName(),
                                                             addressBookAddressRegistRequest.getFirstName(),
                                                             addressBookAddressRegistRequest.getLastKana(),
                                                             addressBookAddressRegistRequest.getFirstKana(),
                                                             addressBookAddressRegistRequest.getTel(),
                                                             addressBookAddressRegistRequest.getZipCode(),
                                                             addressBookAddressRegistRequest.getPrefecture(),
                                                             addressBookAddressRegistRequest.getAddress1(),
                                                             addressBookAddressRegistRequest.getAddress2(),
                                                             addressBookAddressRegistRequest.getAddress3(),
                                                             addressBookAddressRegistRequest.getShippingMemo(),
                                                             addressBookAddressRegistRequest.getHideFlag(),
                                                             addressBookAddressRegistRequest.getDefaultFlag()
                                                            );

        return new ResponseEntity<>(
                        this.addressBookHelper.toAddressBookAddressRegistResponse(addressId), HttpStatus.OK);
    }

    /**
     * DELETE /shippings/addressbooks : 住所録削除
     *
     * @param addressBookDeleteRequest 住所録削除リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(@NotNull @ApiParam(value = "住所録削除リクエスト", required = true)
                                       @Valid AddressBookDeleteRequest addressBookDeleteRequest) {

        this.deleteAddressBookUseCase.deleteAddressBook(
                        addressBookDeleteRequest.getAddressId(), addressBookDeleteRequest.getPreAddressId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/addressbooks/close : 住所録非公開
     *
     * @param addressBookCloseRequest 住所録非公開リクエスト (required)
     * @param addressId               住所録非公開リクエストヘッダー&lt;br/&gt;暫定対応のため、住所IDのみ定義&lt;br/&gt;フェーズ3では、IDトークンでその他の顧客情報も含まれる想定
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> close(@ApiParam(value = "住所録非公開リクエスト", required = true) @Valid @RequestBody
                                                      AddressBookCloseRequest addressBookCloseRequest,
                                      @ApiParam("住所録非公開リクエストヘッダー<br/>暫定対応のため、住所IDのみ定義<br/>フェーズ3では、IDトークンでその他の顧客情報も含まれる想定")
                                      @RequestHeader(value = "addressId", required = false) String addressId) {

        this.closeAddressBookUseCase.closeAddressBook(addressBookCloseRequest.getAddressId(), addressId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/addressbooks/allclose : 住所録一括非公開
     *
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> allClose() {

        this.closeAllAddressBookUseCase.closeAllAddressBook(getCustomerId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/addressbooks/customers : 顧客住所登録
     *
     * @param addressBookCustomerAddressRegistRequest 顧客住所登録リクエスト (required)
     * @return 住所登録レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AddressBookAddressRegistResponse> registRelateCustomerInfo(
                    @ApiParam(value = "顧客住所登録リクエスト", required = true) @Valid @RequestBody
                                    AddressBookCustomerAddressRegistRequest addressBookCustomerAddressRegistRequest) {

        AddressBookAddressRegistRequest addressBook = addressBookCustomerAddressRegistRequest.getAddressBook();

        String addressId = this.registAddressRelateCustomerInfoUseCase.registAddressRelateCustomerInfo(getCustomerId(),
                                                                                                       addressBook.getAddressName(),
                                                                                                       addressBook.getLastName(),
                                                                                                       addressBook.getFirstName(),
                                                                                                       addressBook.getLastKana(),
                                                                                                       addressBook.getFirstKana(),
                                                                                                       addressBook.getTel(),
                                                                                                       addressBook.getZipCode(),
                                                                                                       addressBook.getPrefecture(),
                                                                                                       addressBook.getAddress1(),
                                                                                                       addressBook.getAddress2(),
                                                                                                       addressBook.getAddress3(),
                                                                                                       addressBook.getShippingMemo(),
                                                                                                       addressBook.getDefaultFlag(),
                                                                                                       addressBookCustomerAddressRegistRequest.getPreAddressId()
                                                                                                      );

        return new ResponseEntity<>(
                        this.addressBookHelper.toAddressBookAddressRegistResponse(addressId), HttpStatus.OK);
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private String getCustomerId() {
        // PR層でチェックはしない。memberSeqがNullの場合、customerIdにNullが設定される
        return this.headerParamsUtil.getMemberSeq();
    }

}
