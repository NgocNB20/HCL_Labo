package jp.co.itechh.quad.front.aop;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * コントローラー系アスペクトクラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Aspect
@Component
public class FrontControllerAspect {

    /** カート合計数量を計算除外対象(部分一致) */
    private static final String[] EXCLUSION_CART_SUMCOUNT =
                    {"org.springframework.boot", "jp.co.hankyuhanshin.itec.hitmall.web"};

    /**
     * コントローラー開始ログ出力メソッド<br/>
     * 指定したアノテーションが付与されているメソッドの前に呼び出される
     * @Param joinPoint 実行ポイント
     *
     */
    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerStartLog(JoinPoint joinPoint) {

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        // 対象メソッドのメソッド名を取得
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        // アクションログを出力
        applicationLogUtility.writeActionLog(methodName);

    }

    /**
     * カート合計数量を計算してセッションに格納するメソッド<br/>
     * @Param joinPoint 実行ポイント
     */
    @After("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
           + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
           + "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void sessionCartSumCount(JoinPoint joinPoint) {

        // カート合計数量計算対象外
        for (String exclusion : EXCLUSION_CART_SUMCOUNT) {
            if (joinPoint.getSignature().toString().contains(exclusion)) {
                return;
            }
        }

        // ajaxリクエスト（CartAdd以外）の場合は注文商品数量の取得が不要なので処理なし
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWithHeader)) {
            return;
        }

        // カート商品数量（初期値ゼロ）
        BigDecimal cartGoodsSumCount = BigDecimal.ZERO;

        // カート商品数量の取得要否を判定
        if (needGetOrderSlip()) {
            // -------------------------------------------------------------
            // 取得要の場合、プロモーションサービスより注文票を検索
            // 【前提】
            // 「取得要」と判定される場合は、Apiのヘッダーにすでに顧客IDが設定済である
            // （そうなるように適切に制御されていることが前提となる）
            // -------------------------------------------------------------
            OrderSlipApi orderSlipApi = ApplicationContextUtility.getBean(OrderSlipApi.class);
            // 下書き注文票取得
            OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(null);
            if (!ObjectUtils.isEmpty(orderSlipResponse)) {
                // 注文商品数量を取得
                cartGoodsSumCount = new BigDecimal(orderSlipResponse.getTotalItemCount());
            }
        }
        // カート合計数量をセット
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        commonInfo.getCommonInfoBase().setCartGoodsSumCount(cartGoodsSumCount);
    }

    /**
     * 注文票取得要否を判定
     *
     * @return true...取得要
     */
    private boolean needGetOrderSlip() {
        CommonInfoUtility commonInfoUtility = ApplicationContextUtility.getBean(CommonInfoUtility.class);
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        if (commonInfoUtility.isLogin(commonInfo)) {
            // ログイン済の場合、取得要
            return true;
        }

        HmFrontUserDetailsServiceImpl userDetailsService =
                        ApplicationContextUtility.getBean(HmFrontUserDetailsServiceImpl.class);
        if (StringUtils.isNotEmpty(userDetailsService.getCustomerId())) {
            // 未ログインでも顧客IDが取得できる場合、取得要
            return true;
        }

        // 上記以外は取得不要
        return false;
    }
}