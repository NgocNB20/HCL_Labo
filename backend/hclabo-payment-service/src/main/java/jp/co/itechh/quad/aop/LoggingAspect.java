package jp.co.itechh.quad.aop;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * ログ アスペクトクラス
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * APIコール ログ
     *
     * @Param joinPoint 実行ポイント
     */
    @Around("execution(* jp.co.itechh.quad..api.*Api.*(..))"
            + "&& !execution(* *..*.getApiClient(..)) && !within(*..*Controller)")
    public Object callApiLog(ProceedingJoinPoint pJoinPoint) throws Throwable {

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        // 対象メソッドのメソッド名を取得
        String methodName = ((MethodSignature) pJoinPoint.getSignature()).getMethod().getName();
        String apiClassName = ((MethodSignature) pJoinPoint.getSignature()).getDeclaringTypeName();

        // API-REQ APIログを出力
        applicationLogUtility.writeApiLog("<API-REQ>", apiClassName, methodName, null);

        Object obj = pJoinPoint.proceed();

        // API-RES APIログを出力
        applicationLogUtility.writeApiLog("<API-RES>", apiClassName, methodName, null);

        return obj;
    }
}