/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import jp.co.itechh.quad.front.constant.type.HTypeDeviceType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * アクセスデバイスの解析用Utility
 *
 * @author kk32102
 *
 */
@Component
public class AccessDeviceUtility {

    /**
     * User-Agent を取得
     *
     * @param request リクエスト
     * @return User-Agent
     */
    public String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }

    /**
     * Accept を取得
     *
     * @param request リクエスト
     * @return Accept
     */
    public String getAccept(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept;
    }

    /**
     * デバイス種別を返却
     *
     * @param request リクエスト
     * @return デバイス種別
     */
    public HTypeDeviceType getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceType(userAgent);
    }

    /**
     * デバイス種別を返却
     *
     * @param userAgent User-Agent
     * @return デバイス種別
     */
    public HTypeDeviceType getDeviceType(String userAgent) {
        return HTypeDeviceType.PC;
    }

}