package jp.co.itechh.quad.front.config.twofactorauth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 二要素認証機能のON/OFFにより、権限管理クラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class FullyAuthenticatedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        boolean isRemember = authentication.get() instanceof RememberMeAuthenticationToken;
        boolean isAnonymous = authentication.get() instanceof AnonymousAuthenticationToken;
        boolean isTwoFactor = authentication.get() instanceof TwoFactorAuthentication;
        if (isTwoFactor) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(!isAnonymous && !isRemember);
    }
}

