/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.convenience.presentation.api;

import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceResponse;
import jp.co.itechh.quad.core.entity.conveni.ConvenienceEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * コンビニ　Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class ConvenienceHelper {

    /**
     * コンビニ名称一覧レスポンスに変換<br/>
     *
     * @param convenienceEntityList コンビニ名称リスト
     * @return コンビニ名称一覧レスポンス
     */
    public ConvenienceListResponse toConvenienceListResponse(List<ConvenienceEntity> convenienceEntityList) {

        List<ConvenienceResponse> convenienceResponseList = convenienceEntityList.stream().map(convenienceEntity -> {
            ConvenienceResponse convenienceResponse = new ConvenienceResponse();
            convenienceResponse.setConveniSeq(convenienceEntity.getConveniSeq());
            convenienceResponse.setPayCode(convenienceEntity.getPayCode());
            convenienceResponse.setConveniCode(convenienceEntity.getConveniCode());
            convenienceResponse.setPayName(convenienceEntity.getPayName());
            convenienceResponse.setConveniName(convenienceEntity.getConveniName());
            convenienceResponse.setRegistTime(convenienceEntity.getRegistTime());
            convenienceResponse.setUpdateTime(convenienceEntity.getUpdateTime());
            convenienceResponse.setDisplayOrder(convenienceEntity.getDisplayOrder());
            convenienceResponse.setUseFlag(EnumTypeUtil.getValue(convenienceEntity.getUseFlag()));
            return convenienceResponse;
        }).collect(Collectors.toList());

        ConvenienceListResponse convenienceListResponse = new ConvenienceListResponse();
        convenienceListResponse.setConvenienceList(convenienceResponseList);

        return convenienceListResponse;
    }
}