/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.entity.shop.FreeAreaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 特集ページHelper
 *
 * @author Pham Quang Dieu
 */
@Component
public class SpecialHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    @Autowired
    public SpecialHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面表示・再表示<br/>
     *
     * @param freeAreaEntity
     * @param specialModel
     */
    public void toPageForLoad(FreeAreaEntity freeAreaEntity, SpecialModel specialModel) {
        specialModel.setFkey(freeAreaEntity.getFreeAreaKey());
        specialModel.setFreeAreaTitle(freeAreaEntity.getFreeAreaTitle());
        specialModel.setFreeAreaBodyPc(freeAreaEntity.getFreeAreaBodyPc());
    }

    /**
     * フリーエリアレスポンス更新時の処理
     *
     * @param freeAreaResponse フリーエリアレスポンス
     * @return フリーエリアクラス
     */
    public FreeAreaEntity toFreeAreaEntity(FreeAreaResponse freeAreaResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(freeAreaResponse)) {
            return null;
        }

        FreeAreaEntity freeAreaEntity = new FreeAreaEntity();

        freeAreaEntity.setFreeAreaKey(freeAreaResponse.getFreeAreaKey());
        freeAreaEntity.setFreeAreaSeq(freeAreaResponse.getFreeAreaSeq());
        freeAreaEntity.setOpenStartTime(conversionUtility.toTimestamp(freeAreaResponse.getOpenStartTime()));
        freeAreaEntity.setFreeAreaTitle(freeAreaResponse.getFreeAreaTitle());
        freeAreaEntity.setFreeAreaBodyPc(freeAreaResponse.getFreeAreaBodyPc());
        freeAreaEntity.setRegistTime(conversionUtility.toTimestamp(freeAreaResponse.getRegistTime()));
        freeAreaEntity.setUpdateTime(conversionUtility.toTimestamp(freeAreaResponse.getUpdateTime()));

        return freeAreaEntity;
    }
}
