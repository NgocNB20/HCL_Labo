package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.constant.type.HTypeDeviceType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * アクセスデバイスの解析用Utility
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
        // スマートフォン、タブレット判定
        SmartPhoneUtility smartPhoneUtility = ApplicationContextUtility.getBean(SmartPhoneUtility.class);
        if (smartPhoneUtility.isDeviceTypeSp(userAgent)) {
            return HTypeDeviceType.SP;
        } else if (smartPhoneUtility.isDeviceTypeTab(userAgent)) {
            return HTypeDeviceType.TAB;
        }

        return HTypeDeviceType.PC;
    }
}