package jp.co.itechh.quad.front.config.twofactorauth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 二要素認証管理クラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class TwoFactorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    /**
     * REDIRECT_TOP_SCREENの定義
     */
    public static final String REDIRECT_TOP_SCREEN = "/" ;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        // 二要素認証成功ならトップページへの遷移
        if (authentication.get() instanceof UsernamePasswordAuthenticationToken) {
            object.getRequest().getSession().setAttribute("redirect", REDIRECT_TOP_SCREEN);
        }
        return new AuthorizationDecision(authentication.get() instanceof TwoFactorAuthentication);
    }

}

