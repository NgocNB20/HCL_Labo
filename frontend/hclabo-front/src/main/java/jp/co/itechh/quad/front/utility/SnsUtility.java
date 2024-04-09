/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import org.springframework.stereotype.Component;

/**
 * SNS連携のUtil
 *
 * @author kimura
 */
@Component
public class SnsUtility {

    /**
     * Facebookを利用するか？
     *
     * @return true = 利用する
     */
    public boolean isUseFacebook() {
        String value = PropertiesUtil.getSystemPropertiesValue("facebook.use");
        return Boolean.valueOf(value);
    }

    /**
     * Facebook のアプリケーションIDを取得
     *
     * @return Facebook のアプリケーションID
     */
    public String getFacebookAppId() {
        return PropertiesUtil.getSystemPropertiesValue("facebook.app.id");
    }

    /**
     * Twitterを利用するか？
     *
     * @return true = 利用する
     */
    public boolean isUseTwitter() {
        String value = PropertiesUtil.getSystemPropertiesValue("twitter.use");
        return Boolean.valueOf(value);
    }

    /**
     * Twitter のアカウント名を取得
     *
     * @return Twitter のアカウント名
     */
    public String getTwitterVia() {
        return PropertiesUtil.getSystemPropertiesValue("twitter.via");
    }

    /**
     * Lineを利用するか？
     *
     * @return true = 利用する
     */
    public boolean isUseLine() {
        String value = PropertiesUtil.getSystemPropertiesValue("line.use");
        return Boolean.valueOf(value);
    }
}