package jp.co.itechh.quad.front.config;

import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeDeleteRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeGetRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeGetResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeRegistRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeUpdateRequest;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Persistent login token repository implementation
 *
 * @author thangdoan
 */
@Component
public class PersistentTokenAdapterImpl implements PersistentTokenRepository {

    /** ログ */
    private static final Logger LOG = LoggerFactory.getLogger(PersistentTokenAdapterImpl.class);

    /** 認証クライアントAPI */
    private final AuthenticationApi authenticationApi;

    /**
     * コンストラクタ
     */
    @Autowired
    public PersistentTokenAdapterImpl() {
        this.authenticationApi = ApplicationContextUtility.getBean(AuthenticationApi.class);
    }

    /**
     *
     * @param token RememberMe認証token
     */
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        RememberMeRegistRequest request = new RememberMeRegistRequest();
        request.setUsername(token.getUsername());
        request.setSeriesId(token.getSeries());
        request.setToken(token.getTokenValue());
        request.setLastUsed(token.getDate());

        authenticationApi.post(request);
    }

    /**
     *
     * @param series
     * @param tokenValue
     * @param lastUsed
     */
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        RememberMeUpdateRequest request = new RememberMeUpdateRequest();
        request.setSeriesId(series);
        request.setToken(tokenValue);
        request.setLastUsed(lastUsed);

        authenticationApi.put(request);
    }

    /**
     *
     * @param seriesId
     * @return
     */
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        RememberMeGetRequest request = new RememberMeGetRequest();
        request.setSeriesId(seriesId);
        try {
            RememberMeGetResponse response = authenticationApi.get(request);
            if (response == null) {
                return null;
            } else {
                return new PersistentRememberMeToken(response.getUsername(), response.getSeriesId(),
                                                     response.getToken(), response.getLastUsed()
                );
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param username
     */
    @Override
    public void removeUserTokens(String username) {
        RememberMeDeleteRequest request = new RememberMeDeleteRequest();
        request.setUsername(username);
        authenticationApi.delete(request);
    }
}