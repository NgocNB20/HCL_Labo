package jp.co.itechh.quad.front.config.twofactorauth;

import jp.co.itechh.quad.authentication.presentation.api.AuthenticationApi;
import jp.co.itechh.quad.authentication.presentation.api.param.CertificationCodeIssueRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.CertificationCodeIssueResponse;
import jp.co.itechh.quad.front.application.commoninfo.impl.HmFrontUserDetails;
import jp.co.itechh.quad.front.application.listener.AuthenticationEventListeners;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.SecurityName;
import jp.co.itechh.quad.front.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.front.util.async.AsyncUtil;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CertificationCodeRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * 二要素認証成功ハンドラークラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler primarySuccessHandler;

    private final AuthenticationSuccessHandler secondarySuccessHandler;

    /**
     * 二要素認証成功ハンドラー
     *
     * @param secondAuthUrl         2段階認証URL
     * @param primarySuccessHandler 1段階成功ハンドラー
     */
    public TwoFactorAuthenticationSuccessHandler(String secondAuthUrl,
                                                 AuthenticationSuccessHandler primarySuccessHandler) {
        this.primarySuccessHandler = primarySuccessHandler;
        this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondAuthUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        boolean isTwoFactorAuthentication =
                        PropertiesUtil.getSystemPropertiesValueToBool("two-factor-authentication.switch");
        if (isTwoFactorAuthentication) {
            SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
            this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
            HmFrontUserDetails userDetails = (HmFrontUserDetails) authentication.getPrincipal();
            // 1段階成功（メールとパスワード）
            request.getSession().setAttribute(SecurityName.SESSION_VERIFY_USERNAME_PASSWORD, true);

            // 認証コード発行APIの呼び出し
            AuthenticationApi authenticationApi = ApplicationContextUtility.getBean(AuthenticationApi.class);
            CertificationCodeIssueRequest codeIssueRequest = new CertificationCodeIssueRequest();
            codeIssueRequest.setMemberInfoUniqueId(userDetails.getMemberInfoEntity().getMemberInfoUniqueId());
            CertificationCodeIssueResponse certificationCodeIssueResponse =
                            authenticationApi.issueCertificationCode(codeIssueRequest);

            // セッションに認証コードを保存
            long expired = Instant.now().getEpochSecond();
            if (certificationCodeIssueResponse.getCertificationCodeExpiry() != null) {
                expired += certificationCodeIssueResponse.getCertificationCodeExpiry();
            }
            request.getSession().setAttribute(
                            SecurityName.SESSION_CERTIFICATION_CODE,
                            certificationCodeIssueResponse.getCertificationCode() + "_" + expired
                                             );

            // 認証コードメール送信APIの呼び出し
            CertificationCodeRequest certificationCodeRequest = new CertificationCodeRequest();
            certificationCodeRequest.setCertificationCode(certificationCodeIssueResponse.getCertificationCode());
            certificationCodeRequest.setMailTemplateType(HTypeMailTemplateType.CERTIFICATION_CODE.getValue());
            certificationCodeRequest.setMemberInfoSeq(userDetails.getMemberInfoEntity().getMemberInfoSeq());

            Object[] args = new Object[] {certificationCodeRequest};
            Class<?>[] argsClass = new Class<?>[] {CertificationCodeRequest.class};

            AsyncUtil.asyncService(NotificationSubApi.class, "certificationCode", args, argsClass);
        } else {
            this.primarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
            AuthenticationEventListeners eventListeners =
                            ApplicationContextUtility.getBean(AuthenticationEventListeners.class);
            // ヘッダに会員SEQを設定する
            eventListeners.setHeaderParameter(authentication);
            // ゲスト⇒会員カート移行処理
            eventListeners.replaceGuestCartToMemberCart();
        }

    }
}
