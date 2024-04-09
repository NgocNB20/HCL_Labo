/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.config.thymeleaf;

import jp.co.itechh.quad.core.thymeleaf.DateConverterViewUtil;
import jp.co.itechh.quad.core.thymeleaf.DiffStyleClassViewUtil;
import jp.co.itechh.quad.core.thymeleaf.EnumTypeViewUtil;
import jp.co.itechh.quad.core.thymeleaf.EscapeViewUtil;
import jp.co.itechh.quad.core.thymeleaf.ImageConverterViewUtil;
import jp.co.itechh.quad.core.thymeleaf.MaskConverterViewUtil;
import jp.co.itechh.quad.core.thymeleaf.NumberConverterViewUtil;
import jp.co.itechh.quad.core.thymeleaf.PreConverterViewUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * ユーティリティオブジェクトFactoryクラス<br />
 * thymeleafでのカスタムユーティリティオブジェクト作成用
 *
 */
public class CustomExpressionObjectFactory implements IExpressionObjectFactory {

    /** DATEユーティリティオブジェクト名 */
    private static final String DATE_EXPRESSION_NAME = "date";

    /** DIFFSTYLECLASSユーティリティオブジェクト名 */
    private static final String DIFFSTYLECLASS_EXPRESSION_NAME = "style";

    /** ENUMTYPEユーティリティオブジェクト名 */
    private static final String ENUMTYPE_EXPRESSION_NAME = "enumtype";

    /** IMAGEユーティリティオブジェクト名 */
    private static final String IMAGE_EXPRESSION_NAME = "image";

    /** MASKユーティリティオブジェクト名 */
    private static final String MASK_EXPRESSION_NAME = "mask";

    /** NUMBERユーティリティオブジェクト名 */
    private static final String NUMBER_EXPRESSION_NAME = "number";

    /** PREユーティリティオブジェクト名 */
    private static final String PRE_EXPRESSION_NAME = "pre";

    /** ESCAPEユーティリティオブジェクト名 */
    private static final String ESCAPE_EXPRESSION_NAME = "escape";

    /**
     * ユーティリティオブジェクト名一覧
     *
     * @return 名称セット
     */
    @Override
    public Set<String> getAllExpressionObjectNames() {
        Set<String> nameSet = new HashSet<>();
        nameSet.add(DATE_EXPRESSION_NAME);
        nameSet.add(DIFFSTYLECLASS_EXPRESSION_NAME);
        nameSet.add(ENUMTYPE_EXPRESSION_NAME);
        nameSet.add(IMAGE_EXPRESSION_NAME);
        nameSet.add(MASK_EXPRESSION_NAME);
        nameSet.add(NUMBER_EXPRESSION_NAME);
        nameSet.add(PRE_EXPRESSION_NAME);
        nameSet.add(ESCAPE_EXPRESSION_NAME);
        return nameSet;
    }

    /**
     * ユーティリティオブジェクト登録
     *
     * @return ユーティリティオブジェクト
     */
    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (DATE_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new DateConverterViewUtil();
        } else if (DIFFSTYLECLASS_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new DiffStyleClassViewUtil();
        } else if (ENUMTYPE_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new EnumTypeViewUtil();
        } else if (IMAGE_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new ImageConverterViewUtil();
        } else if (MASK_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new MaskConverterViewUtil();
        } else if (NUMBER_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new NumberConverterViewUtil();
        } else if (PRE_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new PreConverterViewUtil();
        } else if (ESCAPE_EXPRESSION_NAME.equals(expressionObjectName)) {
            return new EscapeViewUtil();
        }
        return null;
    }

    /**
     * キャッシュ可否
     *
     * @return boolean
     */
    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}