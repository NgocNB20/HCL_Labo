package jp.co.itechh.quad.front.config.twofactorauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

import java.util.List;

/**
 * 二要素認証クラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class TwoFactorAuthentication extends AbstractAuthenticationToken {

    private final Authentication primary;

    /**
     * 二要素認証
     *
     * @param primary :1段階認証
     */
    public TwoFactorAuthentication(Authentication primary) {
        super(List.of());
        this.primary = primary;
    }

    public Object getPrincipal() {
        return this.primary.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return this.primary.getCredentials();
    }

    @Override
    public void eraseCredentials() {
        if (this.primary instanceof CredentialsContainer) {
            ((CredentialsContainer) this.primary).eraseCredentials();
        }
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * 1段階認証取得
     *
     * @return primary：1段階認証
     */
    public Authentication getPrimary() {
        return this.primary;
    }

}

