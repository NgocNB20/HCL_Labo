/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.config.async;

import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * 非同期処理設定クラス
 *
 * @author yt23807
 * @version $Revision: 1.0 $
 *
 */
@Configuration
public class CoreAsyncConfiguration extends AsyncConfigurerSupport {

    //    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) {
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }

    /**
     * ペイメントクライアントの定義
     * @return PaymentClient
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();

        // スレッドプール設定
        // 
        // ※スレッド設定の流れはcoreSystem.properties参照
        executor.setCorePoolSize(PropertiesUtil.getSystemPropertiesValueToInt("async.executor.core.pool.size"));
        executor.setQueueCapacity(PropertiesUtil.getSystemPropertiesValueToInt("async.executor.queue.capacity"));
        executor.setMaxPoolSize(PropertiesUtil.getSystemPropertiesValueToInt("async.executor.max.pool.size"));
        executor.setThreadNamePrefix(PropertiesUtil.getSystemPropertiesValue("async.executor.thread.name.prefix"));
        executor.initialize();
        return executor;
    }

    /**
     * 非同期処理内でSpringSecurity管理情報を扱えるようにするためのExecutor
     * @author yt23807
     */
    public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
        private static final long serialVersionUID = 1L;

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            // DelegatingSecurityContextCallableでラップすることで、SpringSecurity管理情報も参照可能とする
            return super.submit(new DelegatingSecurityContextCallable(task));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            // DelegatingSecurityContextCallableでラップすることで、SpringSecurity管理情報も参照可能とする
            return super.submitListenable(new DelegatingSecurityContextCallable(task));
        }
    }
}