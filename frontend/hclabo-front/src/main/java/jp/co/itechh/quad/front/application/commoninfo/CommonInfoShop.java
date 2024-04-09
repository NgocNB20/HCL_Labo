/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.application.commoninfo;

import java.io.Serializable;

/**
 * ショップ情報（共通情報）<br/>
 *
 * @author thang
 *
 */
public interface CommonInfoShop extends Serializable {

    /**
     * @return the shopNamePC
     */
    String getShopNamePC();

    /**
     * @return the urlPC
     */
    String getUrlPC();

    /**
     * @return the shopMetaKeyword
     */
    String getShopMetaKeyword();

    /**
     * @return the shopMetaDescription
     */
    String getShopMetaDescription();

}
