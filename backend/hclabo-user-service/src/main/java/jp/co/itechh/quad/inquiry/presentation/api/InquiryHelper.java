package jp.co.itechh.quad.inquiry.presentation.api;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryRequestType;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryCustomerDetailResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryMemoUpdateRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquirySubResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 問い合わせHelper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class InquiryHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryHelper.class);

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility 日付関連Utilityクラス
     */
    public InquiryHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * お問い合わせへの対応
     *
     * @param inquiryDetailsDto 問い合わせ詳細Dtoクラス
     * @return 問い合わせ情報レスポンス
     */
    public InquiryResponse toInquiryResponse(InquiryDetailsDto inquiryDetailsDto) {

        InquiryResponse inquiryResponse = new InquiryResponse();

        if (inquiryDetailsDto.getInquiryEntity() != null) {
            inquiryResponse.setInquirySubResponse(toInquirySubResponse(inquiryDetailsDto.getInquiryEntity()));
        }

        inquiryResponse.setInquiryGroupName(inquiryDetailsDto.getInquiryGroupName());

        inquiryResponse.setInquiryUpdateDetailsResponse(
                        toInquiryUpdateDetailsResponseList(inquiryDetailsDto.getInquiryDetailEntityList()));

        inquiryResponse.setInquiryCustomerDetailsResponse(
                        toInquiryCustomerDetailResponse(inquiryDetailsDto.getMemberInfoEntity()));

        return inquiryResponse;
    }

    /**
     * お問い合わせ検索への回答
     *
     * @param inquiryListGetRequest 問い合わせ情報一覧取得リクエスト
     * @return 問い合わせDao用検索条件Dtoクラス
     */
    public InquirySearchDaoConditionDto toInquiryListResponse(InquiryListGetRequest inquiryListGetRequest) {
        // 変換Helper取得
        InquirySearchDaoConditionDto inquiryReq = ApplicationContextUtility.getBean(InquirySearchDaoConditionDto.class);

        // 問い合わせ分類
        inquiryReq.setInquiryGroupSeq(inquiryListGetRequest.getInquiryGroupSeq());
        inquiryReq.setMemberInfoSeq(inquiryListGetRequest.getMemberInfoSeq());
        // 問い合わせ状態
        inquiryReq.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                  inquiryListGetRequest.getInquiryStatus()
                                                                 ));
        // 問い合わせコード
        inquiryReq.setInquiryCode(inquiryListGetRequest.getInquiryCode());

        inquiryReq.setInquiryMail(inquiryListGetRequest.getInquiryMail());
        // 問い合わせ日時(FROM)
        inquiryReq.setInquiryTimeFrom(dateUtility.convertDateToTimestamp(inquiryListGetRequest.getInquiryTimeFrom()));
        // 問い合わせ日時(To)
        if (inquiryListGetRequest.getInquiryTimeTo() != null) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

            inquiryReq.setInquiryTimeTo(dateUtility.getEndOfDate(
                            dateUtility.convertDateToTimestamp(inquiryListGetRequest.getInquiryTimeTo())));
        }
        // メールアドレス
        inquiryReq.setInquiryMail(inquiryListGetRequest.getInquiryMail());
        // 氏名
        String inquiryName = null;
        if (inquiryListGetRequest.getInquiryName() != null) {
            inquiryName = inquiryListGetRequest.getInquiryName().replace(" ", "").replace("　", "");
        }
        inquiryReq.setInquiryName(inquiryName);

        // 問い合わせ種別
        inquiryReq.setInquiryType(inquiryListGetRequest.getInquiryType());
        // 注文番号
        inquiryReq.setOrderCode(inquiryListGetRequest.getOrderCode());
        // 電話番号
        inquiryReq.setInquiryTel(inquiryListGetRequest.getInquiryTel());
        // 担当者（最終担当者）
        inquiryReq.setLastRepresentative(inquiryListGetRequest.getLastRepresentative());
        // 会員ID(メールアドレス)
        inquiryReq.setMemberInfoMail(inquiryListGetRequest.getMemberInfoMail());

        return inquiryReq;
    }

    /**
     * お問い合わせ検索への回答
     *
     * @param inquirySearchResultDto 問い合わせDao用検索結果Dto
     * @return 問い合わせ情報レスポンス list
     */
    public List<InquiryDetailsResponse> toInquiryResponseList(List<InquirySearchResultDto> inquirySearchResultDto) {
        List<InquiryDetailsResponse> listInquiryResponse = new ArrayList<>();

        if (inquirySearchResultDto.size() > 0) {
            for (InquirySearchResultDto inquirySearchResult : inquirySearchResultDto) {
                InquiryDetailsResponse inquiryResponse = new InquiryDetailsResponse();
                inquiryResponse.setInquirySeq(inquirySearchResult.getInquirySeq());
                inquiryResponse.setInquiryStatus(EnumTypeUtil.getValue(inquirySearchResult.getInquiryStatus()));
                inquiryResponse.setInquiryCode(inquirySearchResult.getInquiryCode());
                inquiryResponse.setInquiryGroupName(inquirySearchResult.getInquiryGroupName());
                inquiryResponse.setInquiryLastName(inquirySearchResult.getInquiryLastName());
                inquiryResponse.setInquiryFirstName(inquirySearchResult.getInquiryFirstName());
                inquiryResponse.setInquiryTime(inquirySearchResult.getInquiryTime());
                inquiryResponse.setAnswerTime(inquirySearchResult.getAnswerTime());

                // 検索条件：お問い合わせ種別
                inquiryResponse.setInquiryType(inquirySearchResult.getInquiryType());

                // 検索条件：初回お問い合わせ日時
                inquiryResponse.setFirstInquiryTime(inquirySearchResult.getFirstInquiryTime());

                // 検索条件：最終お客様お問い合わせ日時
                inquiryResponse.setLastUserInquiryTime(inquirySearchResult.getLastUserInquiryTime());

                // 検索条件：注文番号
                inquiryResponse.setOrderCode(inquirySearchResult.getOrderCode());

                // 検索条件：担当者（最終担当者
                inquiryResponse.setLastRepresentative(inquirySearchResult.getLastRepresentative());

                // 検索条件：問い合わせ者氏名
                inquiryResponse.setResultInquiryName(inquirySearchResult.getInquiryLastName());

                inquiryResponse.setInquiryTel(inquirySearchResult.getInquiryTel());

                inquiryResponse.setMemberInfoMail(inquirySearchResult.getMemberInfoMail());

                listInquiryResponse.add(inquiryResponse);
            }
        }
        return listInquiryResponse;
    }

    /**
     * お問い合わせ内容へ
     *
     * @param memberInfoSeq 会員SEQ
     * @param inquiryRegistRequest 問い合わせ登録リクエスト
     * @return 問い合わせ詳細Dtoクラス
     */
    public InquiryDetailsDto toInquiryDetailsDtoFromRequest(String memberInfoSeq,
                                                            InquiryRegistRequest inquiryRegistRequest) {
        InquiryDetailsDto detailsDto = ApplicationContextUtility.getBean(InquiryDetailsDto.class);

        InquiryEntity inquiryEntity = ApplicationContextUtility.getBean(InquiryEntity.class);

        if (ObjectUtils.isNotEmpty(inquiryRegistRequest)) {
            inquiryEntity.setInquirySeq(inquiryRegistRequest.getInquirySeq());
            inquiryEntity.setInquiryCode(inquiryRegistRequest.getInquiryCode());
            inquiryEntity.setInquiryLastName(inquiryRegistRequest.getInquiryLastName());
            inquiryEntity.setInquiryFirstName(inquiryRegistRequest.getInquiryFirstName());
            inquiryEntity.setInquiryMail(inquiryRegistRequest.getInquiryMail());
            inquiryEntity.setInquiryBody(inquiryRegistRequest.getInquiryBody());
            inquiryEntity.setInquiryTime(
                            dateUtility.convertDateToTimestamp(inquiryRegistRequest.getFirstInquiryTime()));
            inquiryEntity.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                         inquiryRegistRequest.getInquiryStatus()
                                                                        ));
            inquiryEntity.setAnswerTime(dateUtility.convertDateToTimestamp(inquiryRegistRequest.getAnswerTime()));
            inquiryEntity.setAnswerTitle(inquiryRegistRequest.getAnswerTitle());
            inquiryEntity.setAnswerBody(inquiryRegistRequest.getAnswerBody());
            inquiryEntity.setAnswerFrom(inquiryRegistRequest.getAnswerFrom());
            inquiryEntity.setAnswerTo(inquiryRegistRequest.getAnswerTo());
            inquiryEntity.setAnswerBcc(inquiryRegistRequest.getAnswerBcc());
            inquiryEntity.setInquiryGroupSeq(inquiryRegistRequest.getInquiryGroupSeq());
            inquiryEntity.setInquiryLastKana(inquiryRegistRequest.getInquiryLastKana());
            inquiryEntity.setInquiryFirstKana(inquiryRegistRequest.getInquiryFirstKana());
            inquiryEntity.setInquiryZipCode(inquiryRegistRequest.getInquiryZipCode());
            inquiryEntity.setInquiryPrefecture(inquiryRegistRequest.getInquiryPrefecture());
            inquiryEntity.setInquiryAddress1(inquiryRegistRequest.getInquiryAddress1());
            inquiryEntity.setInquiryAddress2(inquiryRegistRequest.getInquiryAddress2());
            inquiryEntity.setInquiryAddress3(inquiryRegistRequest.getInquiryAddress3());
            inquiryEntity.setInquiryTel(inquiryRegistRequest.getInquiryTel());
            inquiryEntity.setInquiryMobileTel(inquiryRegistRequest.getInquiryMobileTel());
            inquiryEntity.setProcessPersonName(inquiryRegistRequest.getProcessPersonName());
            inquiryEntity.setVersionNo(inquiryRegistRequest.getVersionNo());
            inquiryEntity.setRegistTime(dateUtility.convertDateToTimestamp(inquiryRegistRequest.getRegistTime()));
            inquiryEntity.setUpdateTime(dateUtility.convertDateToTimestamp(inquiryRegistRequest.getUpdateTime()));
            inquiryEntity.setInquiryType(EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class,
                                                                       inquiryRegistRequest.getInquiryType()
                                                                      ));
            inquiryEntity.setOrderCode(inquiryRegistRequest.getOrderCode());
            inquiryEntity.setFirstInquiryTime(
                            dateUtility.convertDateToTimestamp(inquiryRegistRequest.getFirstInquiryTime()));
            inquiryEntity.setLastUserInquiryTime(
                            dateUtility.convertDateToTimestamp(inquiryRegistRequest.getLastUserInquiryTime()));
            inquiryEntity.setLastOperatorInquiryTime(
                            dateUtility.convertDateToTimestamp(inquiryRegistRequest.getLastOperatorInquiryTime()));
            inquiryEntity.setLastRepresentative(inquiryRegistRequest.getLastRepresentative());
            inquiryEntity.setMemo(inquiryRegistRequest.getMemo());
            inquiryEntity.setCooperationMemo(inquiryRegistRequest.getCooperationMemo());
            inquiryEntity.setMemberInfoId(inquiryRegistRequest.getMemberInfoId());
            detailsDto.setInquiryGroupName(inquiryRegistRequest.getInquiryGroupName());
        }
        if (memberInfoSeq != null) {
            inquiryEntity.setMemberInfoSeq(Integer.valueOf(memberInfoSeq));
        }
        detailsDto.setInquiryEntity(inquiryEntity);

        if (ObjectUtils.isNotEmpty(inquiryRegistRequest) && !CollectionUtils.isEmpty(
                        inquiryRegistRequest.getInquiryDetailList())) {
            List<InquiryDetailEntity> inquiryDetailEntityList = new ArrayList<>();
            for (InquiryDetailRequest inquiryDetailRequest : inquiryRegistRequest.getInquiryDetailList()) {
                InquiryDetailEntity inquiryDetailEntity = new InquiryDetailEntity();

                inquiryDetailEntity.setInquiryVersionNo(inquiryDetailRequest.getInquiryVersionNo());
                inquiryDetailEntity.setRequestType(EnumTypeUtil.getEnumFromValue(HTypeInquiryRequestType.class,
                                                                                 inquiryDetailRequest.getRequestType()
                                                                                ));
                inquiryDetailEntity.setInquiryTime(
                                dateUtility.convertDateToTimestamp(inquiryDetailRequest.getInquiryTime()));
                inquiryDetailEntity.setInquiryBody(inquiryDetailRequest.getInquiryBody());
                inquiryDetailEntity.setDivisionName(inquiryDetailRequest.getDivisionName());
                inquiryDetailEntity.setRepresentative(inquiryDetailRequest.getRepresentative());
                inquiryDetailEntity.setOperator(inquiryDetailRequest.getOperator());
                inquiryDetailEntity.setDivisionName(inquiryDetailRequest.getDivisionName());
                inquiryDetailEntity.setInquirySeq(inquiryDetailRequest.getInquirySeq());

                inquiryDetailEntityList.add(inquiryDetailEntity);
            }
            detailsDto.setInquiryDetailEntityList(inquiryDetailEntityList);
        }

        return detailsDto;
    }

    /**
     * お問い合わせ内容へ
     *
     * @param inquiryUpdateRequest 問い合わせ更新リクエスト
     * @param inquiryDetailsDto    問い合わせ詳細Dtoクラス
     * @return 問い合わせ詳細Dtoクラス
     */
    public InquiryDetailsDto toInquiryDetailsDto(InquiryUpdateRequest inquiryUpdateRequest,
                                                 InquiryDetailsDto inquiryDetailsDto) {

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日時の取得
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 新規登録する問い合わせ内容エンティティの作成
        InquiryDetailEntity inquiryDetail = ApplicationContextUtility.getBean(InquiryDetailEntity.class);

        // 最下行の問い合わせ内容情報を取得
        InquiryUpdateDetailRequest inquiryDetailItem = inquiryUpdateRequest.getInquiryDetailList()
                                                                           .get(inquiryUpdateRequest.getInquiryDetailList()
                                                                                                    .size() - 1);
        // 発信者種別:運用者
        inquiryDetail.setRequestType(HTypeInquiryRequestType.OPERATOR);
        // 問い合わせSEQ
        inquiryDetail.setInquirySeq(inquiryUpdateRequest.getInquirySeq());
        // 連番
        inquiryDetail.setInquiryVersionNo(inquiryDetailItem.getInquiryVersionNo());
        // 問い合わせ内容
        inquiryDetail.setInquiryBody(inquiryDetailItem.getInquiryBody());
        // 部署名
        inquiryDetail.setDivisionName(inquiryDetailItem.getDivisionName());
        // 問い合わせ日時
        inquiryDetail.setInquiryTime(currentTime);
        // 担当者
        inquiryDetail.setRepresentative(inquiryDetailItem.getRepresentative());
        // 連絡先TEL
        inquiryDetail.setContactTel(inquiryDetailItem.getContactTel());
        // 処理担当者
        inquiryDetail.setOperator(inquiryDetailItem.getOperator());
        // 問い合わせ内容の追加
        List<InquiryDetailEntity> list = inquiryDetailsDto.getInquiryDetailEntityList();
        list.add(inquiryDetail);
        inquiryDetailsDto.setInquiryDetailEntityList(list);

        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        inquiryEntity.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                     inquiryUpdateRequest.getInquiryStatus()
                                                                    ));
        // 最終担当者
        inquiryEntity.setLastRepresentative(inquiryDetailItem.getOperator());
        // 最終運用者問い合わせ日時
        inquiryEntity.setLastOperatorInquiryTime(currentTime);

        return inquiryDetailsDto;
    }

    /**
     * 登録時処理(問い合わせ内容エンティティ反映)
     *
     * @param inquiryEntityRequest     問い合わせクラス
     * @param inquiryMemoUpdateRequest 問い合わせメモ更新リクエスト
     * @return 問い合わせクラス
     */
    public InquiryEntity toInquiryMemoEntity(InquiryEntity inquiryEntityRequest,
                                             InquiryMemoUpdateRequest inquiryMemoUpdateRequest) {
        InquiryEntity inquiryEntity;

        try {
            inquiryEntity = CopyUtil.deepCopy(inquiryEntityRequest);
            // 管理メモ
            inquiryEntity.setMemo(inquiryMemoUpdateRequest.getMemo());
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }

        return inquiryEntity;
    }

    /**
     * 問い合わせクラスに反映
     *
     * @param inquirySubResponse
     * @return 問い合わせクラス
     */
    public InquiryEntity toInquiryEntity(InquirySubResponse inquirySubResponse) {
        InquiryEntity inquiryEntity = new InquiryEntity();

        inquiryEntity.setInquirySeq(inquirySubResponse.getInquirySeq());
        inquiryEntity.setShopSeq(inquirySubResponse.getShopSeq());
        inquiryEntity.setInquiryCode(inquirySubResponse.getInquiryCode());
        inquiryEntity.setInquiryLastName(inquirySubResponse.getInquiryLastName());
        inquiryEntity.setInquiryFirstName(inquirySubResponse.getInquiryFirstName());
        inquiryEntity.setInquiryMail(inquirySubResponse.getInquiryMail());
        inquiryEntity.setInquiryTitle(inquirySubResponse.getInquiryTitle());
        inquiryEntity.setInquiryBody(inquirySubResponse.getInquiryBody());
        inquiryEntity.setInquiryTime(dateUtility.convertDateToTimestamp(inquirySubResponse.getInquiryTime()));
        inquiryEntity.setInquiryStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class, inquirySubResponse.getInquiryStatus()));
        inquiryEntity.setAnswerTime(dateUtility.convertDateToTimestamp(inquirySubResponse.getAnswerTime()));
        inquiryEntity.setAnswerTitle(inquirySubResponse.getAnswerTitle());
        inquiryEntity.setAnswerBody(inquirySubResponse.getAnswerBody());
        inquiryEntity.setAnswerFrom(inquirySubResponse.getAnswerFrom());
        inquiryEntity.setAnswerTo(inquirySubResponse.getAnswerTo());
        inquiryEntity.setAnswerBcc(inquirySubResponse.getAnswerBcc());
        inquiryEntity.setInquiryGroupSeq(inquirySubResponse.getInquiryGroupSeq());
        inquiryEntity.setInquiryLastKana(inquirySubResponse.getInquiryLastKana());
        inquiryEntity.setInquiryFirstKana(inquirySubResponse.getInquiryFirstKana());
        inquiryEntity.setInquiryZipCode(inquirySubResponse.getInquiryZipCode());
        inquiryEntity.setInquiryPrefecture(inquirySubResponse.getInquiryPrefecture());
        inquiryEntity.setInquiryAddress1(inquirySubResponse.getInquiryAddress1());
        inquiryEntity.setInquiryAddress2(inquirySubResponse.getInquiryAddress2());
        inquiryEntity.setInquiryAddress3(inquirySubResponse.getInquiryAddress3());
        inquiryEntity.setInquiryTel(inquirySubResponse.getInquiryTel());
        inquiryEntity.setInquiryMobileTel(inquirySubResponse.getInquiryMobileTel());
        inquiryEntity.setProcessPersonName(inquirySubResponse.getProcessPersonName());
        inquiryEntity.setVersionNo(inquirySubResponse.getVersionNo());
        inquiryEntity.setRegistTime(dateUtility.convertDateToTimestamp(inquirySubResponse.getRegistTime()));
        inquiryEntity.setUpdateTime(dateUtility.convertDateToTimestamp(inquirySubResponse.getUpdateTime()));
        inquiryEntity.setInquiryType(
                        EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class, inquirySubResponse.getInquiryType()));
        inquiryEntity.setMemberInfoSeq(inquirySubResponse.getMemberInfoSeq());
        inquiryEntity.setOrderCode(inquirySubResponse.getOrderCode());
        inquiryEntity.setFirstInquiryTime(dateUtility.convertDateToTimestamp(inquirySubResponse.getFirstInquiryTime()));
        inquiryEntity.setLastUserInquiryTime(
                        dateUtility.convertDateToTimestamp(inquirySubResponse.getLastUserInquiryTime()));
        inquiryEntity.setLastOperatorInquiryTime(
                        dateUtility.convertDateToTimestamp(inquirySubResponse.getLastOperatorInquiryTime()));
        inquiryEntity.setLastRepresentative(inquirySubResponse.getLastRepresentative());
        inquiryEntity.setMemo(inquirySubResponse.getMemo());
        inquiryEntity.setCooperationMemo(inquirySubResponse.getCooperationMemo());
        inquiryEntity.setMemberInfoId(inquirySubResponse.getMemberInfoId());

        return inquiryEntity;
    }

    /**
     * inquirySubResponse
     *
     * @param inquiryEntity 問い合わせクラス
     * @return inquirySubResponse
     */
    public InquirySubResponse toInquirySubResponse(InquiryEntity inquiryEntity) {
        InquirySubResponse inquirySubResponse = new InquirySubResponse();

        inquirySubResponse.setInquirySeq(inquiryEntity.getInquirySeq());
        inquirySubResponse.setShopSeq(inquiryEntity.getShopSeq());
        inquirySubResponse.setInquiryCode(inquiryEntity.getInquiryCode());
        inquirySubResponse.setInquiryLastName(inquiryEntity.getInquiryLastName());
        inquirySubResponse.setInquiryFirstName(inquiryEntity.getInquiryFirstName());
        inquirySubResponse.setInquiryMail(inquiryEntity.getInquiryMail());
        inquirySubResponse.setInquiryTitle(inquiryEntity.getInquiryTitle());
        inquirySubResponse.setInquiryBody(inquiryEntity.getInquiryBody());
        inquirySubResponse.setInquiryTime(inquiryEntity.getInquiryTime());
        inquirySubResponse.setInquiryStatus(EnumTypeUtil.getValue(inquiryEntity.getInquiryStatus()));
        inquirySubResponse.setAnswerTime(inquiryEntity.getAnswerTime());
        inquirySubResponse.setAnswerTitle(inquiryEntity.getAnswerTitle());
        inquirySubResponse.setAnswerBody(inquiryEntity.getAnswerBody());
        inquirySubResponse.setAnswerFrom(inquiryEntity.getAnswerFrom());
        inquirySubResponse.setAnswerTo(inquiryEntity.getAnswerTo());
        inquirySubResponse.setAnswerBcc(inquiryEntity.getAnswerBcc());
        inquirySubResponse.setInquiryGroupSeq(inquiryEntity.getInquiryGroupSeq());
        inquirySubResponse.setInquiryLastKana(inquiryEntity.getInquiryLastKana());
        inquirySubResponse.setInquiryFirstKana(inquiryEntity.getInquiryFirstKana());
        inquirySubResponse.setInquiryZipCode(inquiryEntity.getInquiryZipCode());
        inquirySubResponse.setInquiryPrefecture(inquiryEntity.getInquiryPrefecture());
        inquirySubResponse.setInquiryAddress1(inquiryEntity.getInquiryAddress1());
        inquirySubResponse.setInquiryAddress2(inquiryEntity.getInquiryAddress2());
        inquirySubResponse.setInquiryAddress3(inquiryEntity.getInquiryAddress3());
        inquirySubResponse.setInquiryTel(inquiryEntity.getInquiryTel());
        inquirySubResponse.setInquiryMobileTel(inquiryEntity.getInquiryMobileTel());
        inquirySubResponse.setProcessPersonName(inquiryEntity.getProcessPersonName());
        inquirySubResponse.setVersionNo(inquiryEntity.getVersionNo());
        inquirySubResponse.setRegistTime(inquiryEntity.getRegistTime());
        inquirySubResponse.setUpdateTime(inquiryEntity.getUpdateTime());
        inquirySubResponse.setInquiryType(EnumTypeUtil.getValue(inquiryEntity.getInquiryType()));
        inquirySubResponse.setMemberInfoSeq(inquiryEntity.getMemberInfoSeq());
        inquirySubResponse.setOrderCode(inquiryEntity.getOrderCode());
        inquirySubResponse.setFirstInquiryTime(inquiryEntity.getFirstInquiryTime());
        inquirySubResponse.setLastUserInquiryTime(inquiryEntity.getLastUserInquiryTime());
        inquirySubResponse.setLastOperatorInquiryTime(inquiryEntity.getLastOperatorInquiryTime());
        inquirySubResponse.setLastRepresentative(inquiryEntity.getLastRepresentative());
        inquirySubResponse.setMemo(inquiryEntity.getMemo());
        inquirySubResponse.setCooperationMemo(inquiryEntity.getCooperationMemo());
        inquirySubResponse.setMemberInfoId(inquiryEntity.getMemberInfoId());

        return inquirySubResponse;
    }

    /**
     * 問い合わせ情報レスポンスのリストに反映
     *
     * @param inquiryDetailEntityList 問い合わせ内容エンティティクラスのリスト
     * @return 問い合わせ情報レスポンスのリスト
     */
    public List<InquiryUpdateDetailsResponse> toInquiryUpdateDetailsResponseList(List<InquiryDetailEntity> inquiryDetailEntityList) {
        List<InquiryUpdateDetailsResponse> inquiryUpdateDetailsResponseList = new ArrayList<>();

        if (!CollectionUtil.isEmpty(inquiryDetailEntityList)) {
            inquiryDetailEntityList.forEach(item -> {
                InquiryUpdateDetailsResponse inquiryUpdateDetailsResponse = new InquiryUpdateDetailsResponse();

                inquiryUpdateDetailsResponse.setInquirySeq(item.getInquirySeq());
                inquiryUpdateDetailsResponse.setInquiryVersionNo(item.getInquiryVersionNo());
                inquiryUpdateDetailsResponse.setRequestType(EnumTypeUtil.getValue(item.getRequestType()));
                inquiryUpdateDetailsResponse.setInquiryTime(item.getInquiryTime());
                inquiryUpdateDetailsResponse.setInquiryBody(item.getInquiryBody());
                inquiryUpdateDetailsResponse.setDivisionName(item.getDivisionName());
                inquiryUpdateDetailsResponse.setRepresentative(item.getRepresentative());
                inquiryUpdateDetailsResponse.setContactTel(item.getContactTel());
                inquiryUpdateDetailsResponse.setOperator(item.getOperator());
                inquiryUpdateDetailsResponse.setRegistTime(item.getRegistTime());
                inquiryUpdateDetailsResponse.setUpdateTime(item.getUpdateTime());

                inquiryUpdateDetailsResponseList.add(inquiryUpdateDetailsResponse);
            });
        }

        return inquiryUpdateDetailsResponseList;
    }

    /**
     * 会員レスポンスに反映
     *
     * @param memberInfoEntity 会員クラス
     * @return 会員レスポンス
     */
    public InquiryCustomerDetailResponse toInquiryCustomerDetailResponse(MemberInfoEntity memberInfoEntity) {

        if (memberInfoEntity == null) {
            return null;
        }

        InquiryCustomerDetailResponse inquiryCustomerDetailResponse = new InquiryCustomerDetailResponse();

        inquiryCustomerDetailResponse.setMemberInforSeq(memberInfoEntity.getMemberInfoSeq());
        inquiryCustomerDetailResponse.setMemberInfoId(memberInfoEntity.getMemberInfoId());
        inquiryCustomerDetailResponse.setMemberInfoLastName(memberInfoEntity.getMemberInfoLastName());

        return inquiryCustomerDetailResponse;
    }
}