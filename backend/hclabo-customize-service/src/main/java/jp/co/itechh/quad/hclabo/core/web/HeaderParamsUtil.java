package jp.co.itechh.quad.hclabo.core.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * ヘッダパラメーターユーティル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class HeaderParamsUtil {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderParamsUtil.class);

    /* 認証取得メソッド */
    private static final String METHOD_GET_AUTHENTICATION = "getAuthentication";

    /* APIキー設定メソッド */
    private static final String METHOD_SET_API_KEY = "setApiKey";

    /* 会員APIキー */
    private static final String MEMBER_API_KEY_AUTHENTICATION = "XLoggedInMemberSeq";

    /* 管理者APIキー */
    private static final String ADMIN_API_KEY_AUTHENTICATION = "XLoggedInAdminSeq";

    /**
     * コンストラクタ
     *
     */
    @Autowired
    public HeaderParamsUtil() {
    }

    /**
     * ヘッダーにパラメーターを設定する
     *
     * @param apiClient Object
     */
    public void setHeader(Object apiClient) {
        try {
            if (StringUtils.isNotEmpty(getMemberSeq())) {
                // 会員SEQ
                // 認証取得
                Method getAuthentication =
                                apiClient.getClass().getDeclaredMethod(METHOD_GET_AUTHENTICATION, String.class);
                Object apiKeyAuth = getAuthentication.invoke(apiClient, MEMBER_API_KEY_AUTHENTICATION);

                // APIキー設定
                Method setApiKey = apiKeyAuth.getClass().getDeclaredMethod(METHOD_SET_API_KEY, String.class);
                setApiKey.invoke(apiKeyAuth, getMemberSeq());
            } else if (StringUtils.isNotEmpty(getAdministratorSeq())) {
                // 管理者SEQ
                // 認証取得
                Method getAuthentication =
                                apiClient.getClass().getDeclaredMethod(METHOD_GET_AUTHENTICATION, String.class);
                Object apiKeyAuth = getAuthentication.invoke(apiClient, ADMIN_API_KEY_AUTHENTICATION);

                // APIキー設定
                Method setApiKey = apiKeyAuth.getClass().getDeclaredMethod(METHOD_SET_API_KEY, String.class);
                setApiKey.invoke(apiKeyAuth, getAdministratorSeq());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 管理者SEQ取得
     *
     * @return 管理者SEQ
     */
    public String getAdministratorSeq() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request.getHeader("X-LOGGED-IN-ADMIN-SEQ") == null) {
                return null;
            } else {
                return request.getHeader("X-LOGGED-IN-ADMIN-SEQ");
            }
        }
    }

    /**
     * 会員SEQ取得
     *
     * @return 会員SEQ
     */
    public String getMemberSeq() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request.getHeader("X-LOGGED-IN-MEMBER-SEQ") == null) {
                return null;
            } else {
                return request.getHeader("X-LOGGED-IN-MEMBER-SEQ");
            }
        }
    }
}