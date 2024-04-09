package jp.co.itechh.quad.core.base.utility;

import org.springframework.stereotype.Component;

/**
 * スマートフォンユーティリティクラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更
 */

@Component
public class SmartPhoneUtility {

    /**
     * 対象ユーザエージェントキーワード（スマートフォン）
     */
    public static final String[] keywordsDeviceSp = new String[] {"ANDROID+MOBILE", "IPHONE+MOBILE", "IPOD+MOBILE"};

    /**
     * 対象ユーザエージェントキーワード（タブレット）
     */
    public static final String[] keywordsDeviceTab = new String[] {"ANDROID", "IPAD"};

    /**
     * スマートフォンのUser-Agentであるかを判定
     *
     * @param userAgent User-Agent
     * @return true = スマートフォン
     */
    public boolean isDeviceTypeSp(String userAgent) {
        return isTargetUserAgent(keywordsDeviceSp, userAgent);
    }

    /**
     * タブレットのUser-Agentであるかを判定
     *
     * @param userAgent User-Agent
     * @return true = タブレット
     */
    public boolean isDeviceTypeTab(String userAgent) {
        return isTargetUserAgent(keywordsDeviceTab, userAgent);
    }

    /**
     * User-Agentにキーワードが含まれている場合に true を返す
     *
     * @param keywords  キーワード
     * @param userAgent User-Agent
     * @return true = キーワードが含まれる
     */
    protected boolean isTargetUserAgent(String[] keywords, String userAgent) {
        if (keywords == null) {
            return false;
        }

        String upperCaseUserAgent = userAgent.toUpperCase();
        for (String keyword : keywords) {
            boolean equiv = true;
            for (String part : keyword.split("\\+")) {
                if (!upperCaseUserAgent.contains(part)) {
                    equiv = false;
                    break;
                }
            }
            if (equiv) {
                return true;
            }
        }
        return false;
    }

}