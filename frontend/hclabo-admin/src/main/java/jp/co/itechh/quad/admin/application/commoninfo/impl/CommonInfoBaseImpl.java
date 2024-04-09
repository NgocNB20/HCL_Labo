/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.application.commoninfo.impl;

import jp.co.itechh.quad.admin.application.commoninfo.CommonInfoBase;
import jp.co.itechh.quad.admin.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 基本情報（共通クラス）<br/>
 *
 * @author natume
 *
 */
@Component
public class CommonInfoBaseImpl implements CommonInfoBase {

    /** シリアルID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** サイト区分 */
    private HTypeSiteType siteType;

    /** User-Agent */
    private String userAgent;

    /** デバイス種別 */
    private HTypeDeviceType deviceType;

    /** セッションID */
    private String sessionId;

    /** URL */
    private String url;

    /** ページID */
    private String pageId;

    /** 端末識別番号 */
    private String accessUid;

    /**
     * 商品合計点数<br/>
     */
    private BigDecimal cartGoodsSumCount;

    /**
     * 問合せ認証の認証済みお問い合わせ番号のリスト
     */
    private List<String> inquiryCodeAttestationList;

    /**
     * @return the shopSeq
     */
    @Override
    public Integer getShopSeq() {
        return shopSeq;
    }

    /**
     * @param shopSeq the shopSeq to set
     */
    public void setShopSeq(Integer shopSeq) {
        this.shopSeq = shopSeq;
    }

    /**
     * @return the siteType
     */
    @Override
    public HTypeSiteType getSiteType() {
        return siteType;
    }

    /**
     * @param siteType the siteType to set
     */
    public void setSiteType(HTypeSiteType siteType) {
        this.siteType = siteType;
    }

    /**
     * @return the userAgent
     */
    @Override
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the deviceType
     */
    @Override
    public HTypeDeviceType getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(HTypeDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return the sessionId
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the pageId
     */
    @Override
    public String getPageId() {
        return pageId;
    }

    /**
     * @param pageId the pageId to set
     */
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    /**
     * @return the accessUid
     */
    @Override
    public String getAccessUid() {
        return accessUid;
    }

    /**
     * @param accessUid the accessUid to set
     */
    public void setAccessUid(String accessUid) {
        this.accessUid = accessUid;
    }

    @Override
    public BigDecimal getCartGoodsSumCount() {
        return cartGoodsSumCount;
    }

    @Override
    public void setCartGoodsSumCount(BigDecimal cartGoodsSumCount) {
        this.cartGoodsSumCount = cartGoodsSumCount;
    }

    @Override
    public List<String> getInquiryCodeAttestationList() {
        return inquiryCodeAttestationList;
    }

    @Override
    public void setInquiryCodeAttestationList(List<String> inquiryCodeAttestationList) {
        this.inquiryCodeAttestationList = inquiryCodeAttestationList;
    }
}