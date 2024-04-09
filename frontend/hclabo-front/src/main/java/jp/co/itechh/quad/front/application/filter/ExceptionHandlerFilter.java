package jp.co.itechh.quad.front.application.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 意外な例外ハンドリングを行う
 * OncePerRequestFilterの実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    /**
     * 意外な例外ハンドリングを行う
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (!request.getRequestURI().contains("/assets/") && !request.getRequestURI().contains("/common/")
                && !request.getRequestURI().contains("/images/") && !request.getRequestURI()
                                                                            .contains(request.getContextPath()
                                                                                      + "/error/")) {
                response.sendRedirect(request.getContextPath() + "/error/");
            }
            filterChain.doFilter(request, response);
        }
    }
}