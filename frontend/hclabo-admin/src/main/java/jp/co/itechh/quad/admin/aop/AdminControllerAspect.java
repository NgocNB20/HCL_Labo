package jp.co.itechh.quad.admin.aop;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * コントローラー系アスペクトクラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Aspect
@Component
public class AdminControllerAspect {

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

}