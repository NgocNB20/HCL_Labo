/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup.registupdate;

import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRegistRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupUpdateRequest;
import org.springframework.stereotype.Component;

/**
 * 問い合わせ分類更新ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class InquiryGroupRegistUpdateHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility
     */
    public InquiryGroupRegistUpdateHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理時の画面反映
     *
     * @param inquiryGroupRegistUpdateModel 問い合わせ分類登録更新画面
     * @param inquiryGroupEntity            問い合わせ分類エンティティ
     */
    public void toPageForLoad(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel,
                              InquiryGroupEntity inquiryGroupEntity) {
        inquiryGroupRegistUpdateModel.setNormality(true);
        // 指定時
        if (inquiryGroupEntity != null) {
            // 画面へ反映
            inquiryGroupRegistUpdateModel.setInquiryGroupSeq(inquiryGroupEntity.getInquiryGroupSeq());
            inquiryGroupRegistUpdateModel.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());
            inquiryGroupRegistUpdateModel.setOpenStatus(inquiryGroupEntity.getOpenStatus().getValue());
            inquiryGroupRegistUpdateModel.setInquiryGroupEntity(inquiryGroupEntity);
        }
    }

    /**
     * 問い合わせ分類登録更新時の処理
     *
     * @param inquiryGroupResponse 問い合わせ分類レスポンス
     * @return 問い合わせ分類エンティティ
     */
    public InquiryGroupEntity toInquiryGroupEntityFromInquiryGroupResponse(InquiryGroupResponse inquiryGroupResponse) {

        Integer shopSeq = 1001;
        InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();

        inquiryGroupEntity.setShopSeq(shopSeq);
        inquiryGroupEntity.setInquiryGroupSeq(inquiryGroupResponse.getInquiryGroupSeq());
        inquiryGroupEntity.setInquiryGroupName(inquiryGroupResponse.getInquiryGroupName());
        inquiryGroupEntity.setOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                       inquiryGroupResponse.getOpenStatus()
                                                                      ));
        inquiryGroupEntity.setOrderDisplay(inquiryGroupResponse.getOrderDisplay());
        inquiryGroupEntity.setRegistTime(conversionUtility.toTimestamp(inquiryGroupResponse.getRegistTime()));
        inquiryGroupEntity.setUpdateTime(conversionUtility.toTimestamp(inquiryGroupResponse.getUpdateTime()));

        return inquiryGroupEntity;

    }

    /**
     * 問い合わせ分類登録更新時の処理
     *
     * @param inquiryGroupEntity 問い合わせ分類エンティティ
     * @return 問い合わせ分類登録リクエスト
     */
    public InquiryGroupRegistRequest toInquiryGroupRegistRequestFromInquiryGroupEntity(InquiryGroupEntity inquiryGroupEntity) {

        InquiryGroupRegistRequest inquiryGroupRegistRequest = new InquiryGroupRegistRequest();

        inquiryGroupRegistRequest.setInquiryGroupSeq(inquiryGroupEntity.getInquiryGroupSeq());
        inquiryGroupRegistRequest.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());
        inquiryGroupRegistRequest.setOpenStatus(inquiryGroupEntity.getOpenStatus().getValue());
        inquiryGroupRegistRequest.setOrderDisplay(inquiryGroupEntity.getOrderDisplay());

        return inquiryGroupRegistRequest;

    }

    /**
     * 問い合わせ分類登録更新時の処理
     *
     * @param inquiryGroupEntity 問い合わせ分類エンティティ
     * @return 問い合わせ分類更新リクエスト
     */
    public InquiryGroupUpdateRequest toInquiryGroupUpdateRequestFromInquiryGroupEntity(InquiryGroupEntity inquiryGroupEntity) {

        InquiryGroupUpdateRequest inquiryGroupUpdateRequest = new InquiryGroupUpdateRequest();

        inquiryGroupUpdateRequest.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());
        inquiryGroupUpdateRequest.setOpenStatus(inquiryGroupEntity.getOpenStatus().getValue());
        inquiryGroupUpdateRequest.setOrderDisplay(inquiryGroupEntity.getOrderDisplay());

        return inquiryGroupUpdateRequest;

    }

    /**
     * 問い合わせ分類登録更新時の処理
     *
     * @param inquiryGroupRegistUpdateModel 問い合わせ分類登録更新確認画面
     * @return 問い合わせ分類エンティティ
     */
    public InquiryGroupEntity toInquiryGroupEntityForInquiryGroupRegist(InquiryGroupRegistUpdateModel inquiryGroupRegistUpdateModel) {

        InquiryGroupEntity inquiryGroupEntity = inquiryGroupRegistUpdateModel.getInquiryGroupEntity();

        inquiryGroupEntity.setInquiryGroupName(inquiryGroupRegistUpdateModel.getInquiryGroupName());
        inquiryGroupEntity.setOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                       inquiryGroupRegistUpdateModel.getOpenStatus()
                                                                      ));

        return inquiryGroupEntity;

    }

}