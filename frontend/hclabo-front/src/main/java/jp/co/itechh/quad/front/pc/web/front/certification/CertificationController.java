package jp.co.itechh.quad.front.pc.web.front.certification;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.listener.AuthenticationEventListeners;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.config.twofactorauth.TwoFactorAuthentication;
import jp.co.itechh.quad.front.constant.SecurityName;
import jp.co.itechh.quad.front.exception.CertificationCodeException;
import jp.co.itechh.quad.front.web.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * 認証コントローラー
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Controller
@RequestMapping("/certification/")
@SessionAttributes(value = "certificationModel")
public class CertificationController extends AbstractController {

    private static final String MSGCD_ILLEGAL_CERTIFICATION_CODE = "CODE-001-002-A-E";

    private static final String MSGCD_EXPIRED_CERTIFICATION_CODE = "CODE-001-003-A-E";

    private static final String FILED_NAME_CERTIFICATION_CODE = "certificationCode";

    /** 認証成功ハンドラー */
    private final AuthenticationSuccessHandler successHandler;

    /** 認証用イベントリスナ */
    private final AuthenticationEventListeners eventListeners;

    /** Persistent Token方式を利用する場合のトークンリポジトリ */
    private final PersistentTokenBasedRememberMeServices rememberMeTokenService;

    /**
     * コンストラクタ
     *
     * @param successHandler
     * @param eventListeners
     * @param rememberMeTokenService
     */
    public CertificationController(AuthenticationSuccessHandler successHandler,
                                   AuthenticationEventListeners eventListeners,
                                   PersistentTokenBasedRememberMeServices rememberMeTokenService) {
        this.successHandler = successHandler;
        this.eventListeners = eventListeners;
        this.rememberMeTokenService = rememberMeTokenService;
    }


    /**
     * 画面初期化
     *
     * @param certificationModel
     * @param redirectAttributes
     * @param model
     * @return string
     */
    @GetMapping
    public String doLoadIndex(CertificationModel certificationModel, RedirectAttributes redirectAttributes,
                              Model model) {
        clearModel(CertificationModel.class, certificationModel, model);
        return "certification/index";
    }

    /**
     * 二要素認証処理
     *
     * @param certificationModel
     * @param error
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @param authentication
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @PostMapping
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/login/")
    @HEHandler(exception = CertificationCodeException.class, returnView = "certification/index")
    public void doVerify(@Validated CertificationModel certificationModel,
                         BindingResult error, RedirectAttributes redirectAttributes, SessionStatus sessionStatus,
                         Model model, TwoFactorAuthentication authentication, HttpServletRequest request,
                         HttpServletResponse response)
        throws ServletException, IOException {

        if (error.hasErrors()) {
            throw new CertificationCodeException(model);
        }

        this.verifyCertificationCode(request, certificationModel.getCertificationCode(), error, model);

        Authentication primaryAuthentication = authentication.getPrimary();

        // Remember-Meトークンを更新
        rememberMeTokenService.loginSuccess(request, response, primaryAuthentication);

        SecurityContextHolder.getContext().setAuthentication(primaryAuthentication);
        this.successHandler.onAuthenticationSuccess(request, response, primaryAuthentication);

        // ヘッダに会員SEQを設定する
        this.eventListeners.setHeaderParameter(primaryAuthentication);
        // ゲスト⇒会員カート移行処理
        this.eventListeners.replaceGuestCartToMemberCart();

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // Clear certificationCode-session
        request.getSession().removeAttribute(SecurityName.SESSION_CERTIFICATION_CODE);
    }

    /**
     * 認証コードチェック処理
     *
     * @param request
     * @param certificationCodeInput
     * @param error
     * @param model
     */
    private void verifyCertificationCode(HttpServletRequest request, String certificationCodeInput, BindingResult error, Model model) {
        String certificationCodeSession = (String) request.getSession().getAttribute(SecurityName.SESSION_CERTIFICATION_CODE);
        if (StringUtils.isEmpty(certificationCodeSession)) {
            throwMessage(MSGCD_EXPIRED_CERTIFICATION_CODE);
        }
        String[] certificationCodeSessionAttr = certificationCodeSession.split("_");
        String certificationCode = certificationCodeSessionAttr[0];
        long expired = Long.parseLong(certificationCodeSessionAttr[1]);
        if (expired < Instant.now().getEpochSecond()) {
            throwMessage(MSGCD_EXPIRED_CERTIFICATION_CODE);
        }

        if (!certificationCode.equals(certificationCodeInput)) {
            error.rejectValue(FILED_NAME_CERTIFICATION_CODE, MSGCD_ILLEGAL_CERTIFICATION_CODE);
            throw new CertificationCodeException(model);
        }
    }

}
