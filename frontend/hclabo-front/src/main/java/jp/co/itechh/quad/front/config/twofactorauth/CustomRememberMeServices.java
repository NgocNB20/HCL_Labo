package jp.co.itechh.quad.front.config.twofactorauth;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.constant.SecurityName;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * リメンバーミーサービスのカスタマイズクラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private static final String REMEMBER_ME_ON = "true";

    /**
     * Persistentトークンリポジトリ
     */
    private final PersistentTokenRepository tokenRepository;

    /**
     *  コンストラクタ
     */
    public CustomRememberMeServices(String key,
                                    UserDetailsService userDetailsService,
                                    PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    /**
     * 新しいpersistentログイントークンを作成し、Persistentトークンリポジトリに保存し、対応するクッキーをレスポンスに追加する。
     *
     */
    @Override
    protected void onLoginSuccess(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Authentication successfulAuthentication) {
        String username = successfulAuthentication.getName();
        this.logger.debug(LogMessage.format("Creating new persistent login for user %s", username));
        PersistentRememberMeToken persistentToken =
                        new PersistentRememberMeToken(username, this.generateSeriesData(), this.generateTokenData(),
                                                      new Date()
                        );

        try {
            boolean isTwoFactorAuthentication =
                            PropertiesUtil.getSystemPropertiesValueToBool("two-factor-authentication.switch");
            if (!isTwoFactorAuthentication) {
                if (REMEMBER_ME_ON.equals(request.getSession().getAttribute(SecurityName.SESSION_REMEMBER_ME))) {
                    this.tokenRepository.createNewToken(persistentToken);
                    this.setCookie(new String[] {persistentToken.getSeries(), persistentToken.getTokenValue()},
                                   this.getTokenValiditySeconds(), request, response
                                  );
                } else {
                    super.cancelCookie(request, response);
                }
            } else {
                if (Boolean.TRUE.equals(
                                request.getSession().getAttribute(SecurityName.SESSION_VERIFY_USERNAME_PASSWORD))) {
                    if (REMEMBER_ME_ON.equals(request.getSession().getAttribute(SecurityName.SESSION_REMEMBER_ME))) {
                        this.tokenRepository.createNewToken(persistentToken);
                        this.setCookie(new String[] {persistentToken.getSeries(), persistentToken.getTokenValue()},
                                       this.getTokenValiditySeconds(), request, response
                                      );
                    } else {
                        super.cancelCookie(request, response);
                    }
                }
            }
        } catch (Exception var7) {
            this.logger.error("Failed to save persistent token ", var7);
        }
    }
}
