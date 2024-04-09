/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.application.commoninfo.impl;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfoShop;

/**
 * ショップ情報（共通情報）<br/>
 *
 * @author thang
 *
 */
public class CommonInfoShopImpl implements CommonInfoShop {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** ショップ名PC */
    private String shopNamePC;

    /** ショップURL-PC */
    private String urlPC;

    /** ショップ META Keyword */
    private String shopMetaKeyword;

    /** ショップ META Description */
    private String shopMetaDescription;

    /**
     * @return the shopNamePC
     */
    @Override
    public String getShopNamePC() {
        return shopNamePC;
    }

    /**
     * @param shopNamePC the shopNamePC to set
     */
    public void setShopNamePC(String shopNamePC) {
        this.shopNamePC = shopNamePC;
    }

    /**
     * @return the urlPC
     */
    @Override
    public String getUrlPC() {
        return urlPC;
    }

    /**
     * @param urlPC the urlPC to set
     */
    public void setUrlPC(String urlPC) {
        this.urlPC = urlPC;
    }

    /**
     * @return the shopMetaKeyword
     */
    @Override
    public String getShopMetaKeyword() {
        return shopMetaKeyword;
    }

    /**
     * @param shopMetaKeyword the shopMetaKeyword to set
     */
    public void setShopMetaKeyword(String shopMetaKeyword) {
        this.shopMetaKeyword = shopMetaKeyword;
    }

    /**
     * @return the shopMetaDescription
     */
    @Override
    public String getShopMetaDescription() {
        return shopMetaDescription;
    }

    /**
     * @param shopMetaDescription the shopMetaDescription to set
     */
    public void setShopMetaDescription(String shopMetaDescription) {
        this.shopMetaDescription = shopMetaDescription;
    }
}
