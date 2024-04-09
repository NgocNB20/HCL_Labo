package jp.co.itechh.quad.front.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.lang.reflect.Method;

/**
 * ヘッダパラメーターヘルパー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class HeaderParamsHelper {
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderParamsHelper.class);
    /* パッケージ名 */
    private static final String API_PACKAGE_NAME = "presentation.api";

    /* API最後名 */
    private static final String API_END_NAME = "Api";

    /* APIクライアント取得メソッド */
    public static final String METHOD_GET_API_CLIENT = "getApiClient";

    /* 認証取得メソッド */
    public static final String METHOD_GET_AUTHENTICATION = "getAuthentication";

    /* APIキー設定メソッド */
    public static final String METHOD_SET_API_KEY = "setApiKey";

    /* APIキー */
    public static final String API_KEY_AUTHENTICATION = "XLoggedInMemberSeq";

    /* Beanファクトリー */
    private final ConfigurableListableBeanFactory beanFactory;

    /**
     * コンストラクタ
     *
     * @param beanFactory ConfigurableListableBeanFactory
     */
    @Autowired
    public HeaderParamsHelper(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * ヘッダに会員Seqを設定する
     *
     * @param memberSeq 会員Seq
     */
    public void setMemberSeq(String memberSeq) {
        // APIクライアントコンポーネント一覧取得
        String[] apiComponentList = beanFactory.getBeanNamesForAnnotation(SessionScope.class);
        for (String apiComponent : apiComponentList) {
            // APIクライアントコンポーネントかどうかチェック
            if (beanFactory.getBean(apiComponent).getClass().getName().contains(API_PACKAGE_NAME)
                && beanFactory.getBean(apiComponent).getClass().getName().endsWith(API_END_NAME)) {
                try {
                    // APIクライアントコンポーネント取得
                    Method getApiClient = beanFactory.getBean(apiComponent)
                                                     .getClass()
                                                     .getDeclaredMethod(METHOD_GET_API_CLIENT);
                    Object apiClient = getApiClient.invoke(beanFactory.getBean(apiComponent));

                    // 認証取得
                    Method getAuthentication =
                                    apiClient.getClass().getDeclaredMethod(METHOD_GET_AUTHENTICATION, String.class);
                    Object apiKeyAuth = getAuthentication.invoke(apiClient, API_KEY_AUTHENTICATION);

                    // APIキー設定
                    Method setApiKey = apiKeyAuth.getClass().getDeclaredMethod(METHOD_SET_API_KEY, String.class);
                    setApiKey.invoke(apiKeyAuth, memberSeq);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }
}