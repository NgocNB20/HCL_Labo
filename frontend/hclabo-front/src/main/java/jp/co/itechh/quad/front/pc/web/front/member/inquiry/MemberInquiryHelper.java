/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.inquiry;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSiteType;
import jp.co.itechh.quad.front.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.front.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.front.pc.web.front.common.inquiry.AbstractInquiryHelper;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * お問い合わせ一覧画面 Helper
 *
 * @author Pham Quang Dieu
 */

@Component
public class MemberInquiryHelper extends AbstractInquiryHelper {

    /**
     * 問い合わせDao用検索条件Dtoの作成<br/>
     *
     * @param memberInquiryModel 問い合わせ一覧画面Model
     * @return 問い合わせDao用検索条件Dto
     */
    public InquirySearchDaoConditionDto toInquirySearchDaoConditionDtoForLoad(MemberInquiryModel memberInquiryModel) {

        InquirySearchDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(InquirySearchDaoConditionDto.class);

        conditionDto.setMemberInfoSeq(memberInquiryModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq());

        return conditionDto;
    }

    /**
     * Model変換、初期表示<br/>
     *
     * @param resultList         問い合わせDao用検索結果Dto
     * @param conditionDto       問い合わせDao用検索条件Dto
     * @param memberInquiryModel 問い合わせ一覧画面Model
     */
    public void toPageForLoad(List<InquirySearchResultDto> resultList,
                              InquirySearchDaoConditionDto conditionDto,
                              MemberInquiryModel memberInquiryModel) {
        if (resultList != null) {

            List<MemberInquiryModelItem> itemlist = new ArrayList<>();

            for (InquirySearchResultDto resultDto : resultList) {

                MemberInquiryModelItem item = ApplicationContextUtility.getBean(MemberInquiryModelItem.class);

                item.setInquiryCode(resultDto.getInquiryCode());
                item.setFirstInquiryTime(resultDto.getFirstInquiryTime());
                if (resultDto.getInquiryStatus() != null) {
                    item.setInquiryStatus(resultDto.getInquiryStatus().getLabel());
                    item.setInquiryStatusValue(resultDto.getInquiryStatus().getValue());
                }
                item.setInquiryType(resultDto.getInquiryType());
                item.setInquiryGroupName(resultDto.getInquiryGroupName());

                itemlist.add(item);
            }

            memberInquiryModel.setMemberInquiryModelItems(itemlist);
        }
    }

    /**
     * 問い合わせ情報一覧取得リクエストに変換
     *
     * @param inquirySearchDaoConditionDto 問い合わせDao用検索条件Dto
     * @return inquiryListGetRequest 問い合わせ情報一覧取得リクエスト
     */
    public InquiryListGetRequest toInquiryListGetRequest(InquirySearchDaoConditionDto inquirySearchDaoConditionDto) {
        InquiryListGetRequest inquiryListGetRequest = new InquiryListGetRequest();

        if (inquirySearchDaoConditionDto != null) {
            inquiryListGetRequest.setInquiryGroupSeq(inquirySearchDaoConditionDto.getInquiryGroupSeq());
            if (inquirySearchDaoConditionDto.getInquiryStatus() != null) {
                inquiryListGetRequest.setInquiryStatus(inquirySearchDaoConditionDto.getInquiryStatus().getValue());
            }
            inquiryListGetRequest.setInquiryCode(inquirySearchDaoConditionDto.getInquiryCode());
            inquiryListGetRequest.setInquiryName(inquirySearchDaoConditionDto.getInquiryName());
            inquiryListGetRequest.setInquiryMail(inquirySearchDaoConditionDto.getInquiryMail());
            inquiryListGetRequest.setInquiryTimeFrom(inquirySearchDaoConditionDto.getInquiryTimeFrom());
            inquiryListGetRequest.setInquiryTimeTo(inquirySearchDaoConditionDto.getInquiryTimeTo());
            inquiryListGetRequest.setInquiryType(inquirySearchDaoConditionDto.getInquiryType());
            inquiryListGetRequest.setOrderCode(inquirySearchDaoConditionDto.getOrderCode());
            inquiryListGetRequest.setInquiryTel(inquirySearchDaoConditionDto.getInquiryTel());
            inquiryListGetRequest.setLastRepresentative(inquirySearchDaoConditionDto.getLastRepresentative());
            inquiryListGetRequest.setMemberInfoMail(inquirySearchDaoConditionDto.getMemberInfoMail());
            inquiryListGetRequest.setMemberInfoSeq(inquirySearchDaoConditionDto.getMemberInfoSeq());
            inquiryListGetRequest.setSiteType(HTypeSiteType.FRONT_PC.getValue());
        }

        return inquiryListGetRequest;
    }

    /**
     * 問い合わせDao用検索結果Dto一覧に変換
     *
     * @param inquiryDetailsResponseList 問い合わせ情報レスポンス一覧
     * @return inquirySearchResultDtos 問い合わせDao用検索結果Dto一覧
     */
    public List<InquirySearchResultDto> toInquirySearchResultDtos(List<InquiryDetailsResponse> inquiryDetailsResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(inquiryDetailsResponseList)) {
            return null;
        }

        List<InquirySearchResultDto> inquirySearchResultDtos = new ArrayList<>();
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        for (InquiryDetailsResponse inquiryDetailsResponse : inquiryDetailsResponseList) {

            InquirySearchResultDto inquirySearchResultDto = new InquirySearchResultDto();
            inquirySearchResultDto.setInquirySeq(inquiryDetailsResponse.getInquirySeq());
            if (inquiryDetailsResponse.getInquiryStatus() != null) {
                inquirySearchResultDto.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                                      inquiryDetailsResponse.getInquiryStatus()
                                                                                     ));
            }
            inquirySearchResultDto.setInquiryCode(inquiryDetailsResponse.getInquiryCode());
            inquirySearchResultDto.setInquiryGroupName(inquiryDetailsResponse.getInquiryGroupName());
            inquirySearchResultDto.setInquiryLastName(inquiryDetailsResponse.getInquiryLastName());
            inquirySearchResultDto.setInquiryFirstName(inquiryDetailsResponse.getInquiryFirstName());
            inquirySearchResultDto.setInquiryTime(
                            conversionUtility.toTimestamp(inquiryDetailsResponse.getInquiryTime()));
            inquirySearchResultDto.setAnswerTime(conversionUtility.toTimestamp(inquiryDetailsResponse.getAnswerTime()));
            inquirySearchResultDto.setInquiryType(inquiryDetailsResponse.getInquiryType());
            inquirySearchResultDto.setFirstInquiryTime(
                            conversionUtility.toTimestamp(inquiryDetailsResponse.getFirstInquiryTime()));
            inquirySearchResultDto.setLastUserInquiryTime(
                            conversionUtility.toTimestamp(inquiryDetailsResponse.getLastUserInquiryTime()));
            inquirySearchResultDto.setOrderCode(inquiryDetailsResponse.getOrderCode());
            inquirySearchResultDto.setLastRepresentative(inquiryDetailsResponse.getLastRepresentative());
            inquirySearchResultDto.setInquiryLastName(inquiryDetailsResponse.getResultInquiryName());

            inquirySearchResultDtos.add(inquirySearchResultDto);
        }

        return inquirySearchResultDtos;
    }

}