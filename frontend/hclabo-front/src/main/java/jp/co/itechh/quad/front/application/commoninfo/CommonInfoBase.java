/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.application.commoninfo;

import jp.co.itechh.quad.front.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.front.constant.type.HTypeSiteType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 基本情報(共通情報)
 *
 * @author natume
 *
 */
public interface CommonInfoBase extends Serializable {

    /**
     * @return the shopSeq
     */
    Integer getShopSeq();

    /**
     * @return the hTypeSiteType
     */
    HTypeSiteType getSiteType();

    /**
     * @return the userAgent
     */
    String getUserAgent();

    /**
     * @return the hTypeDeviceType
     */
    HTypeDeviceType getDeviceType();

    /**
     * @return the carrierType
     */
    // Carrier getCarrierType();

    /**
     * @return the sessionId
     */
    String getSessionId();

    /**
     * @return the url
     */
    String getUrl();

    /**
     * @return the pageId
     */
    String getPageId();

    /**
     * @return the accessUid
     */
    String getAccessUid();

    /**
     * カート合計数量取得
     * @return the cartGoodsSumCount
     */
    BigDecimal getCartGoodsSumCount();

    /**
     * カート合計数量設定
     * @param cartGoodsSumCount
     */
    void setCartGoodsSumCount(BigDecimal cartGoodsSumCount);

    /**
     * 問合せ認証の認証済みお問い合わせ番号のリスト設定
     * @return the inquiryCodeAttestationList
     */
    List<String> getInquiryCodeAttestationList();

    /**
     * 問合せ認証の認証済みお問い合わせ番号のリスト設定
     * @param inquiryCodeAttestationList
     */
    void setInquiryCodeAttestationList(List<String> inquiryCodeAttestationList);
}