package jp.co.itechh.quad.inquirygroup.presentation.api;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupCheckRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListUpdateRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRegistRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupUpdateRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * お問い合わせ分類 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class InquiryGroupHelper {

    /**
     * レスポンスに変換処理
     *
     * @param inquiryGroupEntity 問い合わせ分類エンティティ
     */
    public InquiryGroupResponse toInquiryGroupResponse(InquiryGroupEntity inquiryGroupEntity) {
        InquiryGroupResponse inquiryGroupResponse = new InquiryGroupResponse();
        if (ObjectUtils.isNotEmpty(inquiryGroupEntity)) {
            inquiryGroupResponse.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());
            inquiryGroupResponse.setInquiryGroupSeq(inquiryGroupEntity.getInquiryGroupSeq());
            inquiryGroupResponse.setOpenStatus(EnumTypeUtil.getValue(inquiryGroupEntity.getOpenStatus()));
            inquiryGroupResponse.setOrderDisplay(inquiryGroupEntity.getOrderDisplay());
        }

        return inquiryGroupResponse;
    }

    /**
     * レスポンスリストに変換処理
     *
     * @param inquiryGroupEntities 検索結果リスト
     */
    public InquiryGroupListResponse toInquiryGroupListResponse(List<InquiryGroupEntity> inquiryGroupEntities) {
        InquiryGroupListResponse inquiryGroupListResponse = new InquiryGroupListResponse();
        List<InquiryGroupResponse> inquiryGroupList = new ArrayList<>();

        for (InquiryGroupEntity inquiryGroupEntity : inquiryGroupEntities) {
            InquiryGroupResponse inquiryGroupResponse = new InquiryGroupResponse();
            if (ObjectUtils.isNotEmpty(inquiryGroupEntity)) {
                inquiryGroupResponse.setInquiryGroupSeq(inquiryGroupEntity.getInquiryGroupSeq());
                inquiryGroupResponse.setInquiryGroupName(inquiryGroupEntity.getInquiryGroupName());
                inquiryGroupResponse.setOrderDisplay(inquiryGroupEntity.getOrderDisplay());
                inquiryGroupResponse.setOpenStatus(EnumTypeUtil.getValue(inquiryGroupEntity.getOpenStatus()));
                inquiryGroupList.add(inquiryGroupResponse);
            }
        }
        inquiryGroupListResponse.setInquiryGroupList(inquiryGroupList);

        return inquiryGroupListResponse;
    }

    /**
     * 登録リクエストからエンティティに変換
     *
     * @param inquiryGroupRegistRequest 問い合わせ分類登録リクエスト
     */
    public InquiryGroupEntity toInquiryGroupEntityFromRegistRequest(InquiryGroupRegistRequest inquiryGroupRegistRequest) {
        InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();
        if (ObjectUtils.isNotEmpty(inquiryGroupRegistRequest)) {
            inquiryGroupEntity.setInquiryGroupName(inquiryGroupRegistRequest.getInquiryGroupName());
            inquiryGroupEntity.setInquiryGroupSeq(inquiryGroupRegistRequest.getInquiryGroupSeq());
            HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   inquiryGroupRegistRequest.getOpenStatus()
                                                                                  );
            inquiryGroupEntity.setOpenStatus(openDeleteStatus);
            inquiryGroupEntity.setOrderDisplay(inquiryGroupRegistRequest.getOrderDisplay());
        }
        return inquiryGroupEntity;
    }

    /**
     * チェックリクエストからエンティティに変換
     *
     * @param inquiryGroupCheckRequest 問い合わせ分類チェックリクエスト
     */
    public InquiryGroupEntity toInquiryGroupEntityFromCheckRequest(InquiryGroupCheckRequest inquiryGroupCheckRequest) {
        InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();
        if (ObjectUtils.isNotEmpty(inquiryGroupCheckRequest)) {
            inquiryGroupEntity.setInquiryGroupName(inquiryGroupCheckRequest.getInquiryGroupName());
            inquiryGroupEntity.setInquiryGroupSeq(inquiryGroupCheckRequest.getInquiryGroupSeq());
            HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   inquiryGroupCheckRequest.getOpenStatus()
                                                                                  );
            inquiryGroupEntity.setOpenStatus(openDeleteStatus);
            inquiryGroupEntity.setOrderDisplay(inquiryGroupCheckRequest.getOrderDisplay());
        }
        return inquiryGroupEntity;
    }

    /**
     * 更新リクエストからエンティティに変換
     *
     * @param inquiryGroupSeq 問い合わせ分類SEQ
     * @param inquiryGroupUpdateRequest 問い合わせ分類更新リクエスト
     */
    public InquiryGroupEntity toInquiryGroupEntityFromUpdateRequest(Integer inquiryGroupSeq,
                                                                    InquiryGroupUpdateRequest inquiryGroupUpdateRequest) {
        InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();
        if (ObjectUtils.isNotEmpty(inquiryGroupUpdateRequest)) {
            inquiryGroupEntity.setInquiryGroupName(inquiryGroupUpdateRequest.getInquiryGroupName());
            inquiryGroupEntity.setInquiryGroupSeq(inquiryGroupSeq);
            HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   inquiryGroupUpdateRequest.getOpenStatus()
                                                                                  );
            inquiryGroupEntity.setOpenStatus(openDeleteStatus);
            inquiryGroupEntity.setOrderDisplay(inquiryGroupUpdateRequest.getOrderDisplay());
            inquiryGroupEntity.setShopSeq(1001);
        }
        return inquiryGroupEntity;
    }

    /**
     * お問い合わせ分類エンティティリストに変換
     *
     * @param inquiryGroupListUpdateRequest 問い合わせ分類一覧更新リクエスト
     */
    public List<InquiryGroupEntity> toInquiryGroupList(InquiryGroupListUpdateRequest inquiryGroupListUpdateRequest) {
        List<InquiryGroupEntity> inquiryGroupList = new ArrayList<>();

        for (InquiryGroupRequest inquiryGroup : inquiryGroupListUpdateRequest.getInquiryGroupListUpdate()) {
            InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();
            if (ObjectUtils.isNotEmpty(inquiryGroup)) {
                inquiryGroupEntity.setShopSeq(1001);
                inquiryGroupEntity.setInquiryGroupSeq(inquiryGroup.getInquiryGroupSeq());
                inquiryGroupEntity.setInquiryGroupName(inquiryGroup.getInquiryGroupName());
                inquiryGroupEntity.setOrderDisplay(inquiryGroup.getOrderDisplay());
                HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       inquiryGroup.getOpenStatus()
                                                                                      );
                inquiryGroupEntity.setOpenStatus(openDeleteStatus);
                inquiryGroupList.add(inquiryGroupEntity);
            }
        }

        return inquiryGroupList;
    }

}