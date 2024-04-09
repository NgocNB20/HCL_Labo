/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.aop.gmo;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import org.apache.commons.lang.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * 通信内容のログ出力用Interceptor
 *
 * @author yt23807
 *
 */
@Order(4)
@Aspect
@Component
@Scope("prototype")
public class PaymentClientLogInterceptor {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentClientLogInterceptor.class);

    /** true=ログ出力時にマスキングする */
    private boolean maskFlag = true;

    /** ログ出力時にマスキングするパラメータ */
    private List<String> maskParams;

    /** ログ出力から除外するパラメータ */
    private List<String> excludeParams;

    /**
     * コンストラクタ
     */
    public PaymentClientLogInterceptor() {
        // フィールド初期化 ※現行[paymentclient.dicon]から移植
        this.maskFlag = PropertiesUtil.getSystemPropertiesValueToBool("paymentclient.log.mask");
        this.maskParams = Arrays.asList("sitePass", "shopPass", "accessPass", "cardPass", "cardNo", "securityCode",
                                        "expire", "holderName", "memberName", "customerName", "customerKana", "telNo",
                                        "mailAddress"
                                       );
        this.excludeParams = Arrays.asList("orderSeq", "orderVersionNo", "paRes", "entryTranInput", "execTranInput");
    }

    @Around("execution(* com.gmo_pg.g_pay.client.PaymentClient.*(..))")
    public Object invoke(ProceedingJoinPoint invocation) throws Throwable {
        try {
            Object[] args = invocation.getArgs();
            execute(args);
        } catch (Throwable th) {
            LOGGER.error("ログ出力処理失敗", th);
        }

        return invocation.proceed();
    }

    /**
     * 送信内容のログ出力
     * <pre>
     * 引数のオブジェクトのフィールドをログに出力する
     *
     * ▽ログ出力例（タブはスペースで表現）
     * 「 arg1=[ param1=value1 param2=value2 ] arg2=[ param1=value1 param2=value2 ]」
     * </pre>
     *
     * @param args 実行時の引数
     */
    protected void execute(Object[] args) {
        if (args == null) {
            return;
        }

        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        StringJoiner argsParams = new StringJoiner("\t", "\t", "");

        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            StringJoiner argParams = new StringJoiner("\t", "[\t", "\t]");
            BeanWrapper beanDesc = new BeanWrapperImpl(arg);
            PropertyDescriptor[] propertyDescriptors = beanDesc.getPropertyDescriptors();
            if (propertyDescriptors == null) {
                return;
            }
            for (PropertyDescriptor propertyDesc : propertyDescriptors) {
                if (propertyDesc.getReadMethod() == null) {
                    continue;
                }
                String name = propertyDesc.getName();
                // ログ出力対象外のパラメータは処理しない
                if (excludeParams.contains(name)) {
                    continue;
                }
                String value = ObjectUtils.toString(beanDesc.getPropertyValue(name));
                // マスキング対象のパラメータはマスキング
                if (maskFlag) {
                    if (maskParams.contains(name)) {
                        // ログ出力対象外（全文字をマスキング）
                        value = value.replaceAll(".", "*");
                    }
                }
                argParams.add(name + "=" + value);
            }
            String className = arg.getClass().getSimpleName();
            argsParams.add(className + "=" + argParams.toString());
        }

        applicationLogUtility.writeFreeLog("PAYMENT_CLIENT", argsParams.toString());
    }

}