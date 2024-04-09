/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.config;

import jp.co.itechh.quad.admin.application.filter.CacheControlFilter;
import jp.co.itechh.quad.admin.application.filter.CommonProcessFilter;
import jp.co.itechh.quad.admin.application.filter.ExceptionHandlerFilter;
import jp.co.itechh.quad.admin.application.filter.LogFilter;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring-Security セキュリティ設定Configクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminSecurityConfiguration {

    /** Adminアカウント情報取得サービス */
    private final UserDetailsService userDetailsService;

    /** Adminログイン成功ハンドラ */
    private final AuthenticationSuccessHandler successHandler;

    /** 受注のロールの階層的な関係 */
    private static final String HIERARCHY_ORDER = "ORDER:8 > ORDER:4 > ORDER:0";

    /** 会員のロールの階層的な関係 */
    private static final String HIERARCHY_MEMBER = "MEMBER:8 > MEMBER:4 > MEMBER:0";

    /** 商品のロールの階層的な関係 */
    private static final String HIERARCHY_GOODS = "GOODS:8 > GOODS:4 > GOODS:0";

    /** 店舗のロールの階層的な関係 */
    private static final String HIERARCHY_SHOP = "SHOP:8 > SHOP:4 > SHOP:0";

    /** レポートのロールの階層的な関係 */
    private static final String HIERARCHY_REPORT = "REPORT:8 > REPORT:4 > REPORT:0";

    /** バッチのロールの階層的な関係 */
    private static final String HIERARCHY_BATCH = "BATCH:8 > BATCH:4 > BATCH:0";

    /** 管理者のロールの階層的な関係 */
    private static final String HIERARCHY_ADMIN = "ADMIN:8 > ADMIN:4 > ADMIN:0";

    /** 設定のロールの階層的な関係 */
    private static final String HIERARCHY_SETTING = "SETTING:8 > SETTING:4 > SETTING:0";

    /** 改行記号: RoleHierarchyImplクラスで "\n" に基づいて階層的な権限リストをsplitしているため */
    private static final String LINE_SEPARATOR_SYMBOL = "\n";

    /**
     * コンストラクタ
     *
     * @param userDetailsService
     * @param successHandler
     */
    @Autowired
    public AdminSecurityConfiguration(@Qualifier("hmAdminUserDetailsServiceImpl") UserDetailsService userDetailsService,
                                      AuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    /**
     * パスワードエンコーダー Bean
     * ※設定値あり
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    /**
     * DaoAuthenticationProvider Bean
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    /**
     * Cookie設定 Bean
     * ※設定値あり
     */
    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setName("hm4-session");
            servletContext.getSessionCookieConfig().setSecure(true);
        };
    }

    /**
     * ロールの階層的な関係の定義
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        // -----------------------------------------------
        // ロールの階層的な関係をビルド
        // -----------------------------------------------
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HIERARCHY_ORDER);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_MEMBER);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_GOODS);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_SHOP);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_REPORT);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_BATCH);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_ADMIN);
        stringBuilder.append(LINE_SEPARATOR_SYMBOL);
        stringBuilder.append(HIERARCHY_SETTING);

        // -----------------------------------------------
        // RoleHierarchyのBeanを定義
        // -----------------------------------------------
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(stringBuilder.toString());
        return roleHierarchy;
    }

    /**
     * ロール投票の設定
     */
    @Bean
    public RoleVoter roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    /**
     * 認証方法の設定
     */
    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 静的なresourcesはSecurityチェック対象外とする
     */
    @Bean
    protected WebSecurityCustomizer webSecurityCustomizer() {
        String[] staticResource = PropertiesUtil.getSystemPropertiesValue("quad.static.resource").split(",");
        return web -> web.ignoring().antMatchers(staticResource);
    }

    /**
     * ページのアクセス制限の設定
     * ※設定値あり
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 認可に関する設定
        http.authorizeRequests()
            .antMatchers("/login/**", "/logout/**", "/assets/**", "/common/**", "/images/**", "/error/**",
                         "/mulPayNotification"
                        )
            .permitAll()
            .anyRequest()
            .authenticated();

        // ログインに関する設定
        http.formLogin().loginPage("/login/") // ログインページ
            .loginProcessingUrl("/login/") // ログイン処理
            .successHandler(successHandler) // ログイン後遷移先
            .failureUrl("/login/?error"); // ログインエラー遷移先

        // アクセス拒否されたセッションの宛先
        http.exceptionHandling().accessDeniedPage("/accessdenied/");

        // Referrer-Policy HTTP ヘッダーの設定：same-originリクエストに対して、 origin、パス、クエリ文字列を送信する。
        http.headers().referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN);

        // ログアウトに関する設定
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/**/logout")) // ここにリクエストがあればログアウト
            .clearAuthentication(true) // 認証のクリア
            .invalidateHttpSession(true) // セッションを無効にする
            .deleteCookies("hm4-session") // Cookieからhm4-sessionを削除
            .logoutSuccessUrl("/login/");

        //        セキュリティ担保するため、管理画面の方では一旦利用しない方針
        //        // RememberMe認証に関する設定
        //        http.rememberMe().key("hm4-remember-me-key") // key
        //                .tokenValiditySeconds(10 * 24 * 60 * 60) // Tokenの有効期限を設定（10日間）
        //                .userDetailsService(userDetailsService) // アカウント情報取得サービス
        //                .tokenRepository(this.persistentTokenRepository()) // PersistentToken
        //                .useSecureCookie(true); // secure利用

        // バッチ管理画面での詳細レポートがポップアップの形式で表示できるように、こちらの設定を追加する
        // htmlのキャッシュを有効化（Spring-Securityによるデフォルトの制御では無効化となっている）
        http.headers()
            .frameOptions()
            .sameOrigin()
            .cacheControl()
            .disable()
            .addHeaderWriter(new StaticHeadersWriter(HttpHeaders.CACHE_CONTROL, "no-cache"));

        // SpringSequrityのfilterChainの最初にセットされる、
        // ChannelProcessingFilterの前にLogFilterをセット
        http.addFilterBefore(new LogFilter(), ChannelProcessingFilter.class);
        http.addFilterBefore(new CommonProcessFilter(), ChannelProcessingFilter.class);

        // ------------------------------------------------------------------------------------------------------
        // Spring Securityで @SessionAttributes を利用するとCache-Controlヘッダが上書きされるため、
        // 上記「Cache-Control: no-cache」を設定していても、各Controllerで「@SessionAttributes」を宣言している場合は、
        // HTTPレスポンスヘッダーに「Cache-Control: no-store」が勝手に上書きされてしまう
        // 従って、以下のフィルターを追加することで、「Cache-Control: no-cache」を改めて設定する
        // ------------------------------------------------------------------------------------------------------
        http.addFilterBefore(new CacheControlFilter(), ChannelProcessingFilter.class);

        // 意外な例外ハンドリングを行う処理をセット
        http.addFilterBefore(new ExceptionHandlerFilter(), ChannelProcessingFilter.class);

        return http.build();
    }
}