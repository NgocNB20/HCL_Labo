/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.config;

import jp.co.itechh.quad.front.application.filter.CacheControlFilter;
import jp.co.itechh.quad.front.application.filter.CommonProcessFilter;
import jp.co.itechh.quad.front.application.filter.ExceptionHandlerFilter;
import jp.co.itechh.quad.front.application.filter.LogFilter;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.config.twofactorauth.CustomRememberMeServices;
import jp.co.itechh.quad.front.config.twofactorauth.FullyAuthenticatedAuthorizationManager;
import jp.co.itechh.quad.front.config.twofactorauth.LoginFilter;
import jp.co.itechh.quad.front.config.twofactorauth.TwoFactorAuthenticationSuccessHandler;
import jp.co.itechh.quad.front.config.twofactorauth.TwoFactorAuthorizationManager;
import jp.co.itechh.quad.front.constant.SecurityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Spring-Security　セキュリティ設定Configクラス
 *
 * @author kaneda
 */
@Configuration
@EnableWebSecurity
public class FrontSecurityConfiguration {

    /** Frontアカウント情報取得サービス */
    private final UserDetailsService userDetailsService;

    /** Frontログイン成功ハンドラ */
    private final AuthenticationSuccessHandler successHandler;

    /**
     * コンストラクタ
     *
     * @param userDetailsService
     * @param successHandler
     */
    @Autowired
    public FrontSecurityConfiguration(@Qualifier("hmFrontUserDetailsServiceImpl") UserDetailsService userDetailsService,
                                      @Qualifier("hmAuthenticationSuccessHandler")
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
     * DaoAuthenticationProvider　Bean
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

    /***
     * PersistentTokenRepository Bean
     * RememberMe認証：Persistent Token方式を利用する場合のトークンリポジトリ
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new PersistentTokenAdapterImpl();
    }

    /***
     * PersistentTokenBasedRememberMeServices Bean
     * RememberMe認証：Persistent TokenBase方式を利用する場合のトークンリポジトリ
     */
    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        CustomRememberMeServices rememberMeTokenService =
            new CustomRememberMeServices(
                "hm4-remember-me-key", // key
                userDetailsService, // アカウント情報取得サービス
                this.persistentTokenRepository() // PersistentToken
            );
        rememberMeTokenService.setAlwaysRemember(true);
        rememberMeTokenService.setUseSecureCookie(true); // secure利用
        rememberMeTokenService.setTokenValiditySeconds(10 * 24 * 60 * 60); // Tokenの有効期限を設定（10日間）
        rememberMeTokenService.setParameter(SecurityName.REMEMBER_ME_PARAMETER); // パラメータ名
        return rememberMeTokenService;
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
    protected SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler primarySuccessHandler) throws Exception {

        // 認可に関する設定
        http.authorizeHttpRequests(
            authorize -> authorize.antMatchers("/member/**", "/order/**") // 会員ページ && 注文ページ
                .access(new FullyAuthenticatedAuthorizationManager())
                .antMatchers("/certification/")
                .access(new TwoFactorAuthorizationManager())
                .antMatchers("/order/linkpay-callback")
                .permitAll()
                .anyRequest()
                .permitAll()); // 他

        // ログインに関する設定
        http.formLogin().loginPage("/login/") // ログインページURL
            .loginProcessingUrl("/signin") // ログイン処理URL
            .successHandler(new TwoFactorAuthenticationSuccessHandler("/certification/", primarySuccessHandler)) // ログイン後遷移先
            .failureUrl("/login/?error") // ログインエラー遷移先
            .usernameParameter("memberInfoId") // ログインID
            .passwordParameter("memberInfoPassWord"); // ログインパスワード

        http.securityContext(securityContext -> securityContext.requireExplicitSave(false));

        // アクセス拒否されたセッションの宛先
        http.exceptionHandling().accessDeniedPage("/accessdenied/");

        // Referrer-Policy HTTP ヘッダーの設定：same-originリクエストに対して、 origin、パス、クエリ文字列を送信する。
        http.headers().referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN);

        // ログアウトに関する設定
        http.logout().clearAuthentication(true) // 認証のクリア
            .invalidateHttpSession(true) // セッションを無効にする
            .deleteCookies("hm4-session") // Cookieからhm4-sessionを削除
            .logoutSuccessUrl("/logout-completed"); // ログアウト完了処理

        // 会員情報破棄はSpringSecurityにお任せ（ここでは何もしない）

        // RememberMe認証に関する設定
        http.rememberMe()
            .rememberMeServices(getPersistentTokenBasedRememberMeServices());

        // 3Dセキュア遷移時のcsrfチェックを除外
        http.csrf().ignoringAntMatchers("/order/secureredirect", "/order/linkpay-callback", "/logout");

        // htmlのキャッシュを有効化（Spring-Securityによるデフォルトの制御では無効化となっている）
        http.headers()
            .cacheControl()
            .disable()
            .addHeaderWriter(new StaticHeadersWriter(HttpHeaders.CACHE_CONTROL, "no-cache"));

        // SpringSecurityのFilterChainProxyの最初にセットされる、
        // ChannelProcessingFilterの前にHMのfilterをセット
        // 「LogFilter → CommonProcessFilter → SpringSecurityのFilter」の順で起動
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
        http.addFilterBefore(new LoginFilter(), ChannelProcessingFilter.class);

        return http.build();
    }
}