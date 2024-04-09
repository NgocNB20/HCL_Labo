/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook.query.model;

import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoResponse;
import lombok.Data;

import java.util.List;

/**
 * 住所情報モデル<br/>
 */
@Data
public class AddressModel {

    /** 住所リスト */
    private List<AddressQueryModel> addressList;

    /** ページング情報 */
    private PageInfoResponse pageInfoResponse;

}
