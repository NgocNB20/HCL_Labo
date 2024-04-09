package jp.co.itechh.quad.front.config.twofactorauth;

import jp.co.itechh.quad.front.constant.SecurityName;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ログインフィルタークラス
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.getSession().setAttribute(SecurityName.SESSION_VERIFY_USERNAME_PASSWORD, false);
        request.getSession().setAttribute(SecurityName.SESSION_REMEMBER_ME, request.getParameter(SecurityName.REMEMBER_ME_PARAMETER));
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/signin");
    }
}
