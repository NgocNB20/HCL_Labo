package jp.co.itechh.quad.filter;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.core.config.logging.NotOutputProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * アクセスログフィルタ
 */
@Component
public class HmLoggingFilter implements Filter {

    /**
     * オブジェクト初期化処理
     *
     * @param arg0  引数
     */
    @Override
    public void init(FilterConfig arg0) {
        // do nothing
    }

    /**
     * オブジェクト破棄処理
     */
    @Override
    public void destroy() {
        // do nothing
    }

    /**
     * フィルタリング処理
     *
     * @param rq    リクエスト
     * @param rs    レスポンス
     * @param nextFilter    次のフィルタを呼ぶためのオブジェクト
     * @throws IOException  発生した I/O 例外
     * @throws ServletException 発生したサーブレット例外
     */
    @Override
    public void doFilter(final ServletRequest rq, final ServletResponse rs, final FilterChain nextFilter)
                    throws IOException, ServletException {

        // オブジェクト変換
        final HttpServletRequest request = (HttpServletRequest) rq;

        // -------------------------------------------------------------------------------------------------
        // 管理画面でCSVダウンロードを行った際、LogFilter処理の奥でExceptionが発生し、CSVダウンロードに失敗してしまうため、
        // CSVダウンロード時にLogFilterを通さないようにする
        // -------------------------------------------------------------------------------------------------
        String responseType = ((HttpServletResponse) rs).getHeader("responseType");
        if (!StringUtils.isEmpty(responseType) && "Async".equals(responseType)) {
            nextFilter.doFilter(rq, rs);
            return;
        }

        // User-Agent が、Log出力対象外の時にLogFilterを通さないようにする
        if (!isLogUA(request)) {
            nextFilter.doFilter(rq, rs);
            return;
        }

        final LoggingServletResponse response = new LoggingServletResponse(rs);

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        // スレッドにリクエストを登録
        applicationLogUtility.setHttpServletRequest(request);

        try {

            // 処理開始時刻
            final long accepted = System.currentTimeMillis();

            // アクセス開始 APIログを出力
            applicationLogUtility.writeApiLog("【ACCESS-START】", request.getRequestURI(), "", null);

            // 業務処理
            nextFilter.doFilter(rq, response);

            // 処理にかかった時間
            final long elapsed = System.currentTimeMillis() - accepted;

            // アクセス終了 APIログを出力
            applicationLogUtility.writeApiLog(
                            "【ACCESS-END】", request.getRequestURI(), "process-time:" + elapsed + " ms", null);

        } finally {

            // スレッドからリクエストを除去
            applicationLogUtility.remveHttpServletRequest();
        }

    }

    /**
     * User-Agent が、Log出力対象か否かを判定する。
     *
     * @param request HttpServletRequest
     * @return true:Log出力対象
     *         false:Log出力対象外
     */
    private boolean isLogUA(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        NotOutputProperties prop = ApplicationContextUtility.getBean(NotOutputProperties.class);
        List<String> userAgentsList = prop.getUserAgents();

        if (StringUtils.isNotEmpty(userAgent)) {
            // User-Agentが設定されているものだとLog出力対象外
            for (String s : userAgentsList) {
                if (userAgent.contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }

}
