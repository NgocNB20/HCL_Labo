/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.config.thymeleaf;

import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.front.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * フリーエリアカスタムタグプロセッサー<br/>
 *
 * フリーエリアカスタムタグで指定されたkey属性を取得し、
 * フリーエリアデータを取得し、フリーエリア本文をレンダリングする
 * @author yt23807
 *
 */
public class FreeAreaProcessor extends AbstractElementTagProcessor {

    /** フリーエリアAPI */
    private FreeareaApi freeareaApi;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    //    第3引数に処理対象のタグ名「freearea」を渡す
    public FreeAreaProcessor(String dialectPrefix, FreeareaApi freeareaApi, ConversionUtility conversionUtility) {
        super(TemplateMode.HTML, dialectPrefix, "freeArea", true, null, true, 1);
        this.freeareaApi = freeareaApi;
        this.conversionUtility = conversionUtility;
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             IElementTagStructureHandler structureHandler) {
        String freeAreaBody = "";
        try {
            //    key属性に関する情報を取得する
            IAttribute iAttribute = tag.getAttribute("key");
            if (iAttribute != null) {
                //    key属性の値を取得する
                String key = iAttribute.getValue();
                if (key != null && !key.isEmpty()) {
                    FreeAreaGetRequest freeAreaGetRequest = new FreeAreaGetRequest();
                    freeAreaGetRequest.setFreeAreaKey(key);

                    FreeAreaResponse freeAreaResponse = freeareaApi.getByFreeAreaKey(freeAreaGetRequest);
                    FreeAreaEntity freeAreaEntity = toFreeAreaResponse(freeAreaResponse);
                    if (freeAreaEntity != null) {
                        // フリーエリア本文PCを取得
                        // ※nullの場合は""に変換　⇒　こうしないと後のカスタムタグリプレース時にExcepitonが発生してしまうため
                        freeAreaBody = StringUtils.trimToEmpty(freeAreaEntity.getFreeAreaBodyPc());
                    }
                }
            }
            // カスタムタグリプレース実行
            IModelFactory modelFactory = context.getModelFactory();
            IModel model = modelFactory.createModel();
            model.add(modelFactory.createText(freeAreaBody));
            structureHandler.replaceWith(model, false);

        } catch (Exception e) {
            //例外が発生した場合、ログ出力のみ行う
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(e);
        }
    }

    /*
     * フリーエリアクラススに変換
     *
     * @param freeAreaResponse フリーエリアレスポンス
     */
    private FreeAreaEntity toFreeAreaResponse(FreeAreaResponse freeAreaResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(freeAreaResponse)) {
            return null;
        }

        FreeAreaEntity freeAreaEntity = new FreeAreaEntity();

        freeAreaEntity.setShopSeq(1001);
        freeAreaEntity.setFreeAreaSeq(freeAreaResponse.getFreeAreaSeq());
        freeAreaEntity.setFreeAreaKey(freeAreaResponse.getFreeAreaKey());
        freeAreaEntity.setOpenStartTime(conversionUtility.toTimestamp(freeAreaResponse.getOpenStartTime()));
        freeAreaEntity.setFreeAreaTitle(freeAreaResponse.getFreeAreaTitle());
        freeAreaEntity.setFreeAreaBodyPc(freeAreaResponse.getFreeAreaBodyPc());
        freeAreaEntity.setFreeAreaOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeFreeAreaOpenStatus.class,
                                                                           freeAreaResponse.getFreeAreaOpenStatus()
                                                                          ));
        freeAreaEntity.setSiteMapFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, freeAreaResponse.getSiteMapFlag()));

        return freeAreaEntity;
    }
}