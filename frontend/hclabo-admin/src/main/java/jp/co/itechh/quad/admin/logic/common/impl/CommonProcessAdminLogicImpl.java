/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.logic.common.impl;

import jp.co.itechh.quad.admin.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.admin.application.commoninfo.impl.CommonInfoBaseImpl;
import jp.co.itechh.quad.admin.application.commoninfo.impl.CommonInfoImpl;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.logic.common.AbstractCommonProcessLogic;
import jp.co.itechh.quad.admin.logic.common.CommonProcessAdminLogic;
import jp.co.itechh.quad.admin.utility.AccessDeviceUtility;
import jp.co.itechh.quad.admin.utility.CommonInfoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 共通処理：バック<br/>
 *
 * @author natume
 *
 */
@Component
public class CommonProcessAdminLogicImpl extends AbstractCommonProcessLogic implements CommonProcessAdminLogic {

    /** ログ */
    private static final Logger LOG = LoggerFactory.getLogger(CommonProcessAdminLogicImpl.class);

    @Autowired
    public CommonProcessAdminLogicImpl(CommonInfoUtility commonInfoUtility,
                                       ApplicationLogUtility applicationLogUtility,
                                       AccessDeviceUtility accessDeviceUtility) {
        super(commonInfoUtility, applicationLogUtility, accessDeviceUtility);
    }

    /**
     * 共通処理<br/>
     *
     * @param request リクエスト
     * @param response レスポンス
     * @return 処理続行フラグ true=処理中止, false=処理続行
     */
    @Override
    public CommonInfo execute(HttpServletRequest request, HttpServletResponse response) {

        // 各種共通情報の作成
        CommonInfo commonInfo = createCommonInfo(request);

        /* 認証チェック */
        // SpringSecurityで実施するため不要

        return commonInfo;
    }

    /**
     * 共通情報の作成<br/>
     * <ul>
     * <li>基本情報の作成</li>
     * <li>ショップ情報の作成</li>
     * <li>管理者情報の作成</li>
     * </ul>
     *
     * @param request リクエスト
     * @return 共通情報
     */
    protected CommonInfo createCommonInfo(HttpServletRequest request) {

        // 共通情報の取得・作成
        CommonInfo commonInfo = new CommonInfoImpl();

        /* 基本情報の作成・セット */
        createCommonInfoBase(request, (CommonInfoImpl) commonInfo);

        /* 管理者情報の作成 */
        // Spring Security管理とするため、ここでは何も行わない

        return commonInfo;
    }

    /**
     * 共通基本情報の作成<br/>
     * <ul>
     * <li>共通基本情報の各情報をセット</li>
     * <li>共通情報にセット</li>
     * </ul>
     *
     * @param request リクエスト
     * @param commonInfoImpl 共通情報
     */
    protected void createCommonInfoBase(HttpServletRequest request, CommonInfoImpl commonInfoImpl) {

        // 基本情報作成
        CommonInfoBaseImpl commonBaseInfoImpl = new CommonInfoBaseImpl();

        // ショップSEQ
        commonBaseInfoImpl.setShopSeq(super.getShopSeq());

        // URL
        String url = request.getRequestURL().toString();
        commonBaseInfoImpl.setUrl(url);

        // サイト区分
        commonBaseInfoImpl.setSiteType(HTypeSiteType.getSiteType(url));

        // User-Agent
        commonBaseInfoImpl.setUserAgent(super.getAccessDeviceUtility().getUserAgent(request));

        // デバイス種別
        commonBaseInfoImpl.setDeviceType(super.getAccessDeviceUtility().getDeviceType(request));

        // セッションID
        commonBaseInfoImpl.setSessionId(request.getSession().getId());

        // 画面ID
        commonBaseInfoImpl.setPageId(request.getServletPath());

        // 共通情報にセット
        commonInfoImpl.setCommonInfoBase(commonBaseInfoImpl);
    }
}