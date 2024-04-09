/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;

@SpringBootApplication
@EnableAsync
public class HclaboCustomizeApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HclaboCustomizeApplication.class);
        app.addInitializers(context -> {
            context.addBeanFactoryPostProcessor(beanFactory -> {
                final DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory) beanFactory;
                dlbf.setAutowireCandidateResolver(new AutowireCandidateResolver(dlbf));
            });
        });
        app.run(args);
    }

    // Lazy-loadはアノテーションにしか対応していないためResolverクラスをOverrideする
    private static class AutowireCandidateResolver extends ContextAnnotationAutowireCandidateResolver {

        private final DefaultListableBeanFactory beanFactory;

        private AutowireCandidateResolver(DefaultListableBeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        protected boolean isLazy(DependencyDescriptor descriptor) {

            MethodParameter methodParam = descriptor.getMethodParameter();
            if (methodParam != null) {
                // ControllerでAutowiredしているクラスをLazy-loadする
                for (Annotation ann : methodParam.getContainingClass().getAnnotations()) {
                    RestController restController = AnnotationUtils.getAnnotation(ann, RestController.class);
                    if (restController != null) {
                        return true;
                    }
                }
            }

            return super.isLazy(descriptor);
        }
    }
}

