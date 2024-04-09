/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.config.hitmall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 商品詳細で商品画像アップロード時に「/tmp & /g_images」パスから
 * 物理ファイルが保存されるディレクトリへ振り分ける
 * ※ローカル環境でWebサーバ（Nginx）が未設定の場合のみに必要となる
 *
 * @author doanthang
 */
@Configuration
public class ResourceHandlerConfiguration implements WebMvcConfigurer {

    // 環境定数
    private final Environment environment;

    @Autowired
    public ResourceHandlerConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 商品画像の相当パスが/tmp」のパターン（アップロードボタンを押下後のパス）
        String imgTmpResourceLocations = "file:///" + environment.getProperty("real.tmp.path") + "/";
        registry.addResourceHandler("/tmp/**").addResourceLocations(imgTmpResourceLocations);

        // 商品画像の相当パスが「/g_images」のパターン（DBに登録済みのパス）
        String imgRealResourceLocations = "file:///" + environment.getProperty("real.images.path.goods") + "/";
        registry.addResourceHandler("/g_images/**").addResourceLocations(imgRealResourceLocations);

        // 商品画像の相当パスが「/g_images」のパターン（DBに登録済みのパス）
        String categoryImgRealResourceLocations =
                        "file:///" + environment.getProperty("real.images.path.category") + "/";
        registry.addResourceHandler("/d_images/category/***").addResourceLocations(categoryImgRealResourceLocations);
    }

}