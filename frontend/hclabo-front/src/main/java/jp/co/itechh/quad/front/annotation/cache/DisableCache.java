/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.annotation.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ヘッダにてキャッシュ設定のアノテーション
 * Cache-Control：private, no-store, no-cache, max-age=0, must-revalidate
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableCache {
}
