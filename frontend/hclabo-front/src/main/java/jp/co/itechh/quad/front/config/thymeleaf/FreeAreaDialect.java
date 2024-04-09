/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.config.thymeleaf;

import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

/**
 * フリーエリアDialect<br/>
 * thymeleafでのカスタムタグオブジェクト作成用
 *
 * @author yt23807
 *
 */
public class FreeAreaDialect extends AbstractProcessorDialect {

    /** フリーエリアAPI */
    private FreeareaApi freeareaApi;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** Dialect名 */
    private static final String DIALECT_NAME = "FreeArea Dialect";
    /** 処理対象タグプレフィックス */
    private static final String DIALECT_PREFIX = "hm";

    public FreeAreaDialect(FreeareaApi freeareaApi, ConversionUtility conversionUtility) {
        //    第1引数にDialect名、第2に引数に処理対象のタグプレフィックスを引き渡す
        super(DIALECT_NAME, DIALECT_PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
        this.freeareaApi = freeareaApi;
        this.conversionUtility = conversionUtility;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        //    FreeAreaProcessorをProcessorに登録する
        processors.add(new FreeAreaProcessor(dialectPrefix, freeareaApi, conversionUtility));
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

}