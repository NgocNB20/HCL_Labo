/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.common.inquiry;

import jp.co.itechh.quad.front.base.util.common.CopyUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryRequestType;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.front.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.front.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.front.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryCustomerDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateDetailsResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 問合せヘルパー基底クラス<br/>
 *
 * @author Pham Quang Dieu
 *
 */
public abstract class AbstractInquiryHelper {

    /**
     * ページ変換、初期表示<br/>
     *
     * @param abstractInquiryModel 問合せ詳細履歴Model
     * @param dto 問い合わせ詳細Dto
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void toPageForLoad(AbstractInquiryModel abstractInquiryModel, InquiryDetailsDto dto)
                    throws IllegalAccessException, InvocationTargetException {
        // 問い合わせ情報を設定
        InquiryEntity inquiryEntity = dto.getInquiryEntity();
        BeanUtils.copyProperties(abstractInquiryModel, inquiryEntity);
        abstractInquiryModel.setInquiryStatus(inquiryEntity.getInquiryStatus().getLabel());
        abstractInquiryModel.setInquiryStatusValue(inquiryEntity.getInquiryStatus().getValue());
        abstractInquiryModel.setInquiryName(
                        inquiryEntity.getInquiryLastName() + " " + inquiryEntity.getInquiryFirstName());
        abstractInquiryModel.setInquiryKana(
                        inquiryEntity.getInquiryLastKana() + " " + inquiryEntity.getInquiryFirstKana());
        abstractInquiryModel.setReedOnlyDto(dto);

        // 問い合わせ分類情報設定 */
        abstractInquiryModel.setInquiryGroupName(dto.getInquiryGroupName());

        // お問い合わせ詳細画面アイテムの設定 */
        abstractInquiryModel.setInquiryModelItems(createModelItemList(dto));

        // 保持用
        abstractInquiryModel.setIcd(inquiryEntity.getInquiryCode());
        abstractInquiryModel.setSaveIcd(inquiryEntity.getInquiryCode());
    }

    /**
     * お問い合わせ詳細画面アイテムのリストを作成
     *
     * @param dto 問い合わせ詳細Dto
     * @return お問い合わせ詳細画面アイテムのリスト
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private List<InquiryModelItem> createModelItemList(InquiryDetailsDto dto)
                    throws IllegalAccessException, InvocationTargetException {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(dto.getInquiryDetailEntityList())) {
            return null;
        }

        List<InquiryModelItem> itemList = new ArrayList<>();
        for (InquiryDetailEntity inquiryDetailEntity : dto.getInquiryDetailEntityList()) {
            InquiryModelItem item = createModelItem(inquiryDetailEntity);
            itemList.add(item);
        }
        return itemList;
    }

    /**
     * お問い合わせ詳細画面アイテムを作成
     *
     * @param inquiryDetail 問い合わせ内容Entity
     * @return お問い合わせ詳細画面アイテム
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private InquiryModelItem createModelItem(InquiryDetailEntity inquiryDetail)
                    throws IllegalAccessException, InvocationTargetException {
        InquiryModelItem item = ApplicationContextUtility.getBean(InquiryModelItem.class);
        BeanUtils.copyProperties(item, inquiryDetail);

        return item;
    }

    /**
     * 登録時処理(問い合わせ内容エンティティ反映)
     *
     * @param abstractInquiryModel 問合せ詳細履歴Model
     * @return 問い合わせ詳細Dto
     */
    public InquiryDetailsDto toInquiryDetailsDtoForPage(AbstractInquiryModel abstractInquiryModel) {
        InquiryDetailsDto inquiryDetailsDto = CopyUtil.deepCopy(abstractInquiryModel.getReedOnlyDto());

        // 問合せを設定
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        // 画面入力項目以外を設定
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp time = dateUtility.getCurrentTime();
        inquiryEntity.setLastUserInquiryTime(time);
        inquiryEntity.setInquiryStatus(HTypeInquiryStatus.RECEIVING);

        // 問合せ内容を設定
        InquiryDetailEntity inquiryDetailEntity = ApplicationContextUtility.getBean(InquiryDetailEntity.class);
        // 画面入力項目を設定
        inquiryDetailEntity.setInquiryBody(abstractInquiryModel.getInputInquiryBody());
        // 画面入力項目以外を設定
        inquiryDetailEntity.setInquirySeq(inquiryEntity.getInquirySeq());
        if (!CollectionUtils.isEmpty(inquiryDetailsDto.getInquiryDetailEntityList())) {
            inquiryDetailEntity.setInquiryVersionNo(inquiryDetailsDto.getInquiryDetailEntityList().size() + 1);
        }
        inquiryDetailEntity.setRequestType(HTypeInquiryRequestType.CONSUMER);
        inquiryDetailEntity.setInquiryTime(time);

        // 新規登録する問い合わせ内容を追加する
        inquiryDetailsDto.getInquiryDetailEntityList().add(inquiryDetailEntity);

        return inquiryDetailsDto;
    }

    /**
     * 画面の状態を受付完了にする
     *
     * @param abstractInquiryModel 問合せ詳細履歴Model
     */
    public void changeAccepted(AbstractInquiryModel abstractInquiryModel) {
        abstractInquiryModel.setInputInquiryBody(null);
    }

    /**
     * 問い合わせ詳細Dtoに変換
     *
     * @param inquiryResponse 問い合わせ情報レスポンス
     * @return inquiryDetailsDto 問い合わせ詳細Dto
     */
    public InquiryDetailsDto toInquiryDetailsDto(InquiryResponse inquiryResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(inquiryResponse)) {
            return null;
        }

        InquiryDetailsDto inquiryDetailsDto = new InquiryDetailsDto();

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        if (inquiryResponse.getInquirySubResponse() != null) {
            InquiryEntity inquiryEntity = new InquiryEntity();

            inquiryEntity.setInquirySeq(inquiryResponse.getInquirySubResponse().getInquirySeq());
            inquiryEntity.setInquiryCode(inquiryResponse.getInquirySubResponse().getInquiryCode());
            inquiryEntity.setInquiryLastName(inquiryResponse.getInquirySubResponse().getInquiryLastName());
            inquiryEntity.setInquiryFirstName(inquiryResponse.getInquirySubResponse().getInquiryFirstName());
            inquiryEntity.setInquiryMail(inquiryResponse.getInquirySubResponse().getInquiryMail());
            inquiryEntity.setInquiryBody(inquiryResponse.getInquirySubResponse().getInquiryBody());
            inquiryEntity.setInquiryTime(conversionUtility.toTimestamp(
                            inquiryResponse.getInquirySubResponse().getFirstInquiryTime()));
            if (inquiryResponse.getInquirySubResponse().getInquiryStatus() != null) {
                inquiryEntity.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                             inquiryResponse.getInquirySubResponse()
                                                                                            .getInquiryStatus()
                                                                            ));
            }
            inquiryEntity.setAnswerTime(
                            conversionUtility.toTimestamp(inquiryResponse.getInquirySubResponse().getAnswerTime()));
            inquiryEntity.setAnswerTitle(inquiryResponse.getInquirySubResponse().getAnswerTitle());
            inquiryEntity.setAnswerBody(inquiryResponse.getInquirySubResponse().getAnswerBody());
            inquiryEntity.setAnswerFrom(inquiryResponse.getInquirySubResponse().getAnswerFrom());
            inquiryEntity.setAnswerTo(inquiryResponse.getInquirySubResponse().getAnswerTo());
            inquiryEntity.setAnswerBcc(inquiryResponse.getInquirySubResponse().getAnswerBcc());
            inquiryEntity.setInquiryGroupSeq(inquiryResponse.getInquirySubResponse().getInquiryGroupSeq());
            inquiryEntity.setInquiryLastKana(inquiryResponse.getInquirySubResponse().getInquiryLastKana());
            inquiryEntity.setInquiryFirstKana(inquiryResponse.getInquirySubResponse().getInquiryFirstKana());
            inquiryEntity.setInquiryZipCode(inquiryResponse.getInquirySubResponse().getInquiryZipCode());
            inquiryEntity.setInquiryPrefecture(inquiryResponse.getInquirySubResponse().getInquiryPrefecture());
            inquiryEntity.setInquiryAddress1(inquiryResponse.getInquirySubResponse().getInquiryAddress1());
            inquiryEntity.setInquiryAddress2(inquiryResponse.getInquirySubResponse().getInquiryAddress2());
            inquiryEntity.setInquiryAddress3(inquiryResponse.getInquirySubResponse().getInquiryAddress3());
            inquiryEntity.setInquiryTel(inquiryResponse.getInquirySubResponse().getInquiryTel());
            inquiryEntity.setInquiryMobileTel(inquiryResponse.getInquirySubResponse().getInquiryMobileTel());
            inquiryEntity.setProcessPersonName(inquiryResponse.getInquirySubResponse().getProcessPersonName());
            inquiryEntity.setVersionNo(inquiryResponse.getInquirySubResponse().getVersionNo());
            inquiryEntity.setRegistTime(
                            conversionUtility.toTimestamp(inquiryResponse.getInquirySubResponse().getRegistTime()));
            inquiryEntity.setUpdateTime(
                            conversionUtility.toTimestamp(inquiryResponse.getInquirySubResponse().getUpdateTime()));
            if (inquiryResponse.getInquirySubResponse().getInquiryType() != null) {
                inquiryEntity.setInquiryType(EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class,
                                                                           inquiryResponse.getInquirySubResponse()
                                                                                          .getInquiryType()
                                                                          ));
            }
            inquiryEntity.setMemberInfoSeq(inquiryResponse.getInquirySubResponse().getMemberInfoSeq());
            inquiryEntity.setOrderCode(inquiryResponse.getInquirySubResponse().getOrderCode());
            inquiryEntity.setFirstInquiryTime(conversionUtility.toTimestamp(
                            inquiryResponse.getInquirySubResponse().getFirstInquiryTime()));
            inquiryEntity.setLastUserInquiryTime(conversionUtility.toTimestamp(
                            inquiryResponse.getInquirySubResponse().getLastUserInquiryTime()));
            inquiryEntity.setLastOperatorInquiryTime(conversionUtility.toTimestamp(
                            inquiryResponse.getInquirySubResponse().getLastOperatorInquiryTime()));
            inquiryEntity.setLastRepresentative(inquiryResponse.getInquirySubResponse().getLastRepresentative());
            inquiryEntity.setMemo(inquiryResponse.getInquirySubResponse().getMemo());
            inquiryEntity.setCooperationMemo(inquiryResponse.getInquirySubResponse().getCooperationMemo());
            inquiryEntity.setMemberInfoId(inquiryResponse.getInquirySubResponse().getMemberInfoId());

            inquiryDetailsDto.setInquiryEntity(inquiryEntity);
        }
        inquiryDetailsDto.setInquiryGroupName(inquiryResponse.getInquiryGroupName());

        List<InquiryDetailEntity> inquiryDetailEntityList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(inquiryResponse.getInquiryUpdateDetailsResponse())) {
            for (InquiryUpdateDetailsResponse inquiryUpdateDetailRequest : inquiryResponse.getInquiryUpdateDetailsResponse()) {
                InquiryDetailEntity inquiryDetailEntity = new InquiryDetailEntity();
                // 連番
                inquiryDetailEntity.setInquiryVersionNo(inquiryUpdateDetailRequest.getInquiryVersionNo());
                if (inquiryUpdateDetailRequest.getRequestType() != null) {
                    // 発信者種別
                    inquiryDetailEntity.setRequestType(EnumTypeUtil.getEnumFromValue(HTypeInquiryRequestType.class,
                                                                                     inquiryUpdateDetailRequest.getRequestType()
                                                                                    ));
                }
                // 問い合わせ日時
                inquiryDetailEntity.setInquiryTime(
                                conversionUtility.toTimestamp(inquiryUpdateDetailRequest.getInquiryTime()));
                // 問い合わせ内容
                inquiryDetailEntity.setInquiryBody(inquiryUpdateDetailRequest.getInquiryBody());
                // 部署名
                inquiryDetailEntity.setDivisionName(inquiryUpdateDetailRequest.getDivisionName());
                // 担当者
                inquiryDetailEntity.setRepresentative(inquiryUpdateDetailRequest.getRepresentative());
                // 処理担当者
                inquiryDetailEntity.setOperator(inquiryUpdateDetailRequest.getOperator());
                inquiryDetailEntity.setDivisionName(inquiryUpdateDetailRequest.getDivisionName());
                inquiryDetailEntity.setInquirySeq(inquiryUpdateDetailRequest.getInquirySeq());

                inquiryDetailEntityList.add(inquiryDetailEntity);
            }
        }
        inquiryDetailsDto.setInquiryDetailEntityList(inquiryDetailEntityList);

        return inquiryDetailsDto;
    }

    /**
     * 問い合わせ登録リクエストに変換
     *
     * @param inquiryDetailsDto 問い合わせ詳細Dto
     * @return inquiryRegistRequest 問い合わせ登録リクエスト
     */
    public InquiryRegistRequest toInquiryRegistRequest(InquiryDetailsDto inquiryDetailsDto) {
        InquiryRegistRequest inquiryRegistRequest = new InquiryRegistRequest();
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        if (!ObjectUtils.isEmpty(inquiryDetailsDto.getInquiryEntity())) {
            inquiryRegistRequest.setInquirySeq(inquiryDetailsDto.getInquiryEntity().getInquirySeq());
            inquiryRegistRequest.setInquiryCode(inquiryDetailsDto.getInquiryEntity().getInquiryCode());
            inquiryRegistRequest.setInquiryLastName(inquiryDetailsDto.getInquiryEntity().getInquiryLastName());
            inquiryRegistRequest.setInquiryFirstName(inquiryDetailsDto.getInquiryEntity().getInquiryFirstName());
            inquiryRegistRequest.setInquiryMail(inquiryDetailsDto.getInquiryEntity().getInquiryMail());
            inquiryRegistRequest.setInquiryBody(inquiryDetailsDto.getInquiryEntity().getInquiryBody());
            inquiryRegistRequest.setInquiryTime(
                            conversionUtility.toTimestamp(inquiryDetailsDto.getInquiryEntity().getFirstInquiryTime()));
            inquiryRegistRequest.setInquiryStatus(inquiryDetailsDto.getInquiryEntity().getInquiryStatus().getValue());
            inquiryRegistRequest.setAnswerTime(
                            conversionUtility.toTimestamp(inquiryDetailsDto.getInquiryEntity().getAnswerTime()));
            inquiryRegistRequest.setAnswerTitle(inquiryDetailsDto.getInquiryEntity().getAnswerTitle());
            inquiryRegistRequest.setAnswerBody(inquiryDetailsDto.getInquiryEntity().getAnswerBody());
            inquiryRegistRequest.setAnswerFrom(inquiryDetailsDto.getInquiryEntity().getAnswerFrom());
            inquiryRegistRequest.setAnswerTo(inquiryDetailsDto.getInquiryEntity().getAnswerTo());
            inquiryRegistRequest.setAnswerBcc(inquiryDetailsDto.getInquiryEntity().getAnswerBcc());
            inquiryRegistRequest.setInquiryGroupSeq(inquiryDetailsDto.getInquiryEntity().getInquiryGroupSeq());
            inquiryRegistRequest.setInquiryLastKana(inquiryDetailsDto.getInquiryEntity().getInquiryLastKana());
            inquiryRegistRequest.setInquiryFirstKana(inquiryDetailsDto.getInquiryEntity().getInquiryFirstKana());
            inquiryRegistRequest.setInquiryZipCode(inquiryDetailsDto.getInquiryEntity().getInquiryZipCode());
            inquiryRegistRequest.setInquiryPrefecture(inquiryDetailsDto.getInquiryEntity().getInquiryPrefecture());
            inquiryRegistRequest.setInquiryAddress1(inquiryDetailsDto.getInquiryEntity().getInquiryAddress1());
            inquiryRegistRequest.setInquiryAddress2(inquiryDetailsDto.getInquiryEntity().getInquiryAddress2());
            inquiryRegistRequest.setInquiryAddress3(inquiryDetailsDto.getInquiryEntity().getInquiryAddress3());
            inquiryRegistRequest.setInquiryTel(inquiryDetailsDto.getInquiryEntity().getInquiryTel());
            inquiryRegistRequest.setInquiryMobileTel(inquiryDetailsDto.getInquiryEntity().getInquiryMobileTel());
            inquiryRegistRequest.setProcessPersonName(inquiryDetailsDto.getInquiryEntity().getProcessPersonName());
            inquiryRegistRequest.setVersionNo(inquiryDetailsDto.getInquiryEntity().getVersionNo());
            inquiryRegistRequest.setRegistTime(
                            conversionUtility.toTimestamp(inquiryDetailsDto.getInquiryEntity().getRegistTime()));
            inquiryRegistRequest.setUpdateTime(
                            conversionUtility.toTimestamp(inquiryDetailsDto.getInquiryEntity().getUpdateTime()));
            inquiryRegistRequest.setInquiryType(inquiryDetailsDto.getInquiryEntity().getInquiryType().getValue());
            inquiryRegistRequest.setOrderCode(inquiryDetailsDto.getInquiryEntity().getOrderCode());
            inquiryRegistRequest.setFirstInquiryTime(
                            conversionUtility.toTimestamp(inquiryDetailsDto.getInquiryEntity().getFirstInquiryTime()));
            inquiryRegistRequest.setLastUserInquiryTime(conversionUtility.toTimestamp(
                            inquiryDetailsDto.getInquiryEntity().getLastUserInquiryTime()));
            inquiryRegistRequest.setLastOperatorInquiryTime(conversionUtility.toTimestamp(
                            inquiryDetailsDto.getInquiryEntity().getLastOperatorInquiryTime()));
            inquiryRegistRequest.setLastRepresentative(inquiryDetailsDto.getInquiryEntity().getLastRepresentative());
            inquiryRegistRequest.setMemo(inquiryDetailsDto.getInquiryEntity().getMemo());
            inquiryRegistRequest.setCooperationMemo(inquiryDetailsDto.getInquiryEntity().getCooperationMemo());
            inquiryRegistRequest.setMemberInfoId(inquiryDetailsDto.getInquiryEntity().getMemberInfoId());
        }

        if (!ObjectUtils.isEmpty(inquiryDetailsDto.getInquiryEntity())) {
            if (inquiryRegistRequest.getInquiryCustomerDetail() == null) {
                inquiryRegistRequest.setInquiryCustomerDetail(new InquiryCustomerDetailRequest());
            }
            inquiryRegistRequest.getInquiryCustomerDetail()
                                .setMemberInforSeq(inquiryDetailsDto.getInquiryEntity().getMemberInfoSeq());
        }
        inquiryRegistRequest.setInquiryGroupName(inquiryDetailsDto.getInquiryGroupName());
        if (!CollectionUtils.isEmpty(inquiryDetailsDto.getInquiryDetailEntityList())) {
            List<InquiryDetailRequest> inquiryDetailRequests = new ArrayList<>();
            for (InquiryDetailEntity inquiryDetailEntity : inquiryDetailsDto.getInquiryDetailEntityList()) {
                InquiryDetailRequest inquiryDetailRequest = new InquiryDetailRequest();

                inquiryDetailRequest.setInquiryVersionNo(inquiryDetailEntity.getInquiryVersionNo());
                if (inquiryDetailEntity.getRequestType() != null) {
                    inquiryDetailRequest.setRequestType(inquiryDetailEntity.getRequestType().getValue());
                }
                inquiryDetailRequest.setInquiryTime(inquiryDetailEntity.getInquiryTime());
                inquiryDetailRequest.setInquiryBody(inquiryDetailEntity.getInquiryBody());
                inquiryDetailRequest.setDivisionName(inquiryDetailEntity.getDivisionName());
                inquiryDetailRequest.setRepresentative(inquiryDetailEntity.getRepresentative());
                inquiryDetailRequest.setOperator(inquiryDetailEntity.getOperator());
                inquiryDetailRequest.setDivisionName(inquiryDetailEntity.getDivisionName());
                inquiryDetailRequest.setInquirySeq(inquiryDetailEntity.getInquirySeq());

                inquiryDetailRequests.add(inquiryDetailRequest);
            }
            inquiryRegistRequest.setInquiryDetailList(inquiryDetailRequests);
        }

        return inquiryRegistRequest;
    }

}
