/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.config.thymeleaf;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * 独自Dialect<br/>
 * thymeleafでのカスタムユーティリティオブジェクト作成用
 *
 * @author kaneda
 *
 */
public class CustomDialect extends AbstractDialect implements IExpressionObjectDialect {

    /**
     * コンストラクタ
     */
    public CustomDialect() {
        super("custom");
    }

    /**
     * IExpressionObjectFactory生成
     */
    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new CustomExpressionObjectFactory();
    }
}
