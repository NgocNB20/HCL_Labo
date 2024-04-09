package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeMagazineSubscribeType;
import jp.co.itechh.quad.admin.constant.type.HTypeMainMemberType;
import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSex;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoAddressDto;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.admin.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.OptionDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerForBackResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListCsvGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.OptionContent;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会員検索HELPER
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class MemberInfoHelper {

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * Instantiates a new Member info helper.
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public MemberInfoHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件Dtoの作成<br/>
     *
     * @param memberInfoModel 会員検索ページ
     * @return 会員検索条件Dto
     */
    public MemberInfoSearchForDaoConditionDto toConditionDtoForSearch(MemberInfoModel memberInfoModel) {
        // 検索条件Dto取得
        MemberInfoSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(MemberInfoSearchForDaoConditionDto.class);

        /* 画面条件 */
        // 会員ID
        conditionDto.setMemberInfoId(memberInfoModel.getMemberInfoId());
        // 会員SEQ
        conditionDto.setMemberInfoSeq(conversionUtility.toInteger(memberInfoModel.getSearchMemberInfoSeq()));
        // 氏名
        String memberInfoName = null;
        if (memberInfoModel.getMemberInfoName() != null) {
            memberInfoName = memberInfoModel.getMemberInfoName().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoName(memberInfoName);
        // 性別
        conditionDto.setMemberInfoSex(
                        EnumTypeUtil.getEnumFromValue(HTypeSex.class, memberInfoModel.getMemberInfoSex()));
        // 生年月日
        conditionDto.setMemberInfoBirthday(conversionUtility.toTimeStamp(memberInfoModel.getMemberInfoBirthday()));
        // 電話番号
        conditionDto.setMemberInfoTel(memberInfoModel.getMemberInfoTel());
        // 状態
        conditionDto.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                       memberInfoModel.getMemberInfoStatus()
                                                                      ));
        // 郵便番号
        conditionDto.setMemberInfoZipCode(memberInfoModel.getMemberInfoZipCode());
        // 都道府県
        conditionDto.setMemberInfoPrefecture(memberInfoModel.getMemberInfoPrefecture());
        // 住所
        String memberInfoAddress = null;
        if (memberInfoModel.getMemberInfoAddress() != null) {
            memberInfoAddress = memberInfoModel.getMemberInfoAddress().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoAddress(memberInfoAddress);
        // 期間種別
        conditionDto.setPeriodType(memberInfoModel.getPeriodType());
        // 期間（FROM）
        conditionDto.setStartDate(memberInfoModel.getStartDate());
        // 期間（TO）
        conditionDto.setEndDate(memberInfoModel.getEndDate());
        // 最終ログインユーザーエージェント
        conditionDto.setLastLoginUserAgent(memberInfoModel.getLastLoginUserAgent());

        // メルマガ購読フラグ
        if (memberInfoModel.isMailMagazine()) {
            conditionDto.setMailMagazine(HTypeMagazineSubscribeType.SUBSCRIBE);
        }

        // 本会員フラグ
        if (memberInfoModel.isMainMemberFlag()) {
            conditionDto.setMainMemberFlag(HTypeMainMemberType.MAIN_MENBER);
        }

        return conditionDto;
    }

    /**
     * 検索結果をページに反映<br/>
     *
     * @param memberInfoDetailsDtoList 会員詳細Dtoリスト
     * @param memberInforModel        会員検索ページ
     * @param conditionDto            検索Dto
     */
    public void toPageForResultList(List<MemberInfoDetailsDto> memberInfoDetailsDtoList,
                                    MemberInfoModel memberInforModel,
                                    MemberInfoSearchForDaoConditionDto conditionDto) {

        // オフセット + 1をNoにセット
        int index = memberInforModel.getPageInfo().getOffset() + 1;
        memberInforModel.setResultItems(new ArrayList<>());
        for (MemberInfoDetailsDto memberInfoDetailsDto : memberInfoDetailsDtoList) {
            MemberInfoResultItem resultItem = createResultItem(memberInfoDetailsDto);
            resultItem.setResultNo(index++);
            memberInforModel.getResultItems().add(resultItem);
        }
    }

    /**
     * Create result item member info result item.
     *
     * @param memberInfoDetailsDto 会員詳細Dto
     * @return 会員検索結果画面情報
     */
    protected MemberInfoResultItem createResultItem(MemberInfoDetailsDto memberInfoDetailsDto) {
        MemberInfoResultItem resultItem = ApplicationContextUtility.getBean(MemberInfoResultItem.class);
        // 会員SEQ
        resultItem.setMemberInfoSeq(memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoSeq());
        // 郵便番号
        resultItem.setResultMemberInfoZipCode(memberInfoDetailsDto.getMemberInfoAddressDto().getZipCode());
        // 都道府県
        resultItem.setResultMemberInfoPrefecture(memberInfoDetailsDto.getMemberInfoAddressDto().getPrefecture());
        // 住所1
        resultItem.setResultMemberInfoAddress1(memberInfoDetailsDto.getMemberInfoAddressDto().getAddress1());
        // 住所2
        resultItem.setResultMemberInfoAddress2(memberInfoDetailsDto.getMemberInfoAddressDto().getAddress2());
        // 住所3
        resultItem.setResultMemberInfoAddress3(memberInfoDetailsDto.getMemberInfoAddressDto().getAddress3());
        // 会員ID
        resultItem.setResultMemberInfoId(memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoId());
        // 氏名：姓
        resultItem.setResultMemberInfoLastName(memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoLastName());
        // 氏名：名
        resultItem.setResultMemberInfoFirstName(memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoFirstName());
        // 会員状態
        resultItem.setResultMemberInfoStatus(
                        memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoStatus().getValue());
        // 電話番号
        resultItem.setResultMemberInfoTel(memberInfoDetailsDto.getMemberInfoEntity().getMemberInfoTel());

        return resultItem;
    }

    /**
     * 会員エンティティリストに変換
     *
     * @param customerListResponse 会員一覧レスポンス
     * @return 会員クラスリスト
     */
    public List<MemberInfoDetailsDto> toMemberInfoEntityList(CustomerListResponse customerListResponse) {
        List<MemberInfoDetailsDto> resultList = new ArrayList<>();

        if (customerListResponse != null) {

            for (CustomerForBackResponse customerResponse : customerListResponse.getCustomerList()) {

                MemberInfoDetailsDto memberInfoDetailsDto = new MemberInfoDetailsDto();
                MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
                MemberInfoAddressDto memberInfoAddressDto = new MemberInfoAddressDto();

                memberInfoEntity.setMemberInfoSeq(customerResponse.getMemberInfoSeq());
                if (customerResponse.getMemberInfoStatus() != null) {
                    memberInfoEntity.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                                       customerResponse.getMemberInfoStatus()
                                                                                      ));
                }
                memberInfoEntity.setMemberInfoUniqueId(customerResponse.getMemberInfoUniqueId());
                memberInfoEntity.setMemberInfoId(customerResponse.getMemberInfoId());
                memberInfoEntity.setMemberInfoPassword(customerResponse.getMemberInfoPassword());
                memberInfoEntity.setMemberInfoLastName(customerResponse.getMemberInfoLastName());
                memberInfoEntity.setMemberInfoFirstName(customerResponse.getMemberInfoFirstName());
                memberInfoEntity.setMemberInfoLastKana(customerResponse.getMemberInfoLastKana());
                memberInfoEntity.setMemberInfoFirstKana(customerResponse.getMemberInfoFirstKana());
                if (customerResponse.getMemberInfoSex() != null) {
                    memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                                    customerResponse.getMemberInfoSex()
                                                                                   ));
                }
                if (customerResponse.getMemberInfoBirthday() != null) {
                    memberInfoEntity.setMemberInfoBirthday(
                                    conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
                }
                memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
                memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());
                memberInfoEntity.setAccessUid(customerResponse.getAccessUid());
                memberInfoEntity.setVersionNo(customerResponse.getVersionNo());
                memberInfoEntity.setAdmissionYmd(customerResponse.getAdmissionYmd());
                memberInfoEntity.setSecessionYmd(customerResponse.getSecessionYmd());
                memberInfoEntity.setMemo(customerResponse.getMemo());
                if (customerResponse.getLastLoginTime() != null) {
                    memberInfoEntity.setLastLoginTime(
                                    conversionUtility.toTimestamp(customerResponse.getLastLoginTime()));
                }
                memberInfoEntity.setLastLoginUserAgent(customerResponse.getLastLoginUserAgent());
                if (customerResponse.getRegistTime() != null) {
                    memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(customerResponse.getRegistTime()));
                }
                if (customerResponse.getUpdateTime() != null) {
                    memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(customerResponse.getUpdateTime()));
                }

                // 住所情報をセット
                memberInfoAddressDto.setZipCode(customerResponse.getZipCode());
                memberInfoAddressDto.setPrefecture(customerResponse.getPrefecture());
                memberInfoAddressDto.setAddress1(customerResponse.getAddress1());
                memberInfoAddressDto.setAddress2(customerResponse.getAddress2());
                memberInfoAddressDto.setAddress3(customerResponse.getAddress3());

                memberInfoDetailsDto.setMemberInfoEntity(memberInfoEntity);
                memberInfoDetailsDto.setMemberInfoAddressDto(memberInfoAddressDto);

                resultList.add(memberInfoDetailsDto);
            }
        }

        return resultList;
    }

    /**
     * メルマガ購読者クラスリストに変換
     *
     * @param mailmagazineMemberListResponse メルマガ会員一覧レスポンス
     * @return メルマガ購読者クラスリスト
     */
    public List<MailMagazineMemberEntity> toMailMagazineMemberEntityList(MailmagazineMemberListResponse mailmagazineMemberListResponse) {
        List<MailMagazineMemberEntity> resultList = new ArrayList<>();

        if (mailmagazineMemberListResponse != null) {

            for (MailmagazineMemberResponse mailmagazineMemberResponse : mailmagazineMemberListResponse.getMailmagazineMemberList()) {
                MailMagazineMemberEntity mailMagazineMemberEntity = new MailMagazineMemberEntity();

                mailMagazineMemberEntity.setMemberinfoSeq(mailmagazineMemberResponse.getMemberinfoSeq());
                mailMagazineMemberEntity.setUniqueMail(mailmagazineMemberResponse.getUniqueMail());
                mailMagazineMemberEntity.setMail(mailmagazineMemberResponse.getMail());
                if (mailmagazineMemberResponse.getSendStatus() != null) {
                    mailMagazineMemberEntity.setSendStatus(EnumTypeUtil.getEnumFromValue(HTypeSendStatus.class,
                                                                                         mailmagazineMemberResponse.getSendStatus()
                                                                                        ));
                }
                if (mailmagazineMemberResponse.getRegistTime() != null) {
                    mailMagazineMemberEntity.setRegistTime(
                                    conversionUtility.toTimestamp(mailmagazineMemberResponse.getRegistTime()));
                }
                if (mailmagazineMemberResponse.getUpdateTime() != null) {
                    mailMagazineMemberEntity.setUpdateTime(
                                    conversionUtility.toTimestamp(mailmagazineMemberResponse.getUpdateTime()));
                }

                resultList.add(mailMagazineMemberEntity);
            }
        }
        return resultList;
    }

    /**
     * 会員一覧取得リクエストに変換
     *
     * @param memberInfoConditionDto 会員Dao用検索条件Dtoクラス
     * @return 会員一覧取得リクエスト
     */
    public CustomerListGetRequest toCustomerListGetRequest(MemberInfoSearchForDaoConditionDto memberInfoConditionDto) {

        CustomerListGetRequest customerListGetRequest = new CustomerListGetRequest();

        if (memberInfoConditionDto != null) {

            customerListGetRequest.setMemberInfoId(memberInfoConditionDto.getMemberInfoId());
            customerListGetRequest.setMemberInfoSeq(memberInfoConditionDto.getMemberInfoSeq());
            customerListGetRequest.setMemberInfoName(memberInfoConditionDto.getMemberInfoName());
            if (memberInfoConditionDto.getMemberInfoSex() != null) {
                customerListGetRequest.setMemberInfoSex(memberInfoConditionDto.getMemberInfoSex().getValue());
            }
            customerListGetRequest.setMemberInfoBirthday(memberInfoConditionDto.getMemberInfoBirthday());
            customerListGetRequest.setMemberInfoTel(memberInfoConditionDto.getMemberInfoTel());
            customerListGetRequest.setMemberInfoZipCode(memberInfoConditionDto.getMemberInfoZipCode());
            customerListGetRequest.setMemberInfoPrefecture(memberInfoConditionDto.getMemberInfoPrefecture());
            customerListGetRequest.setMemberInfoAddress(memberInfoConditionDto.getMemberInfoAddress());
            if (memberInfoConditionDto.getMemberInfoStatus() != null) {
                customerListGetRequest.setMemberInfoStatus(memberInfoConditionDto.getMemberInfoStatus().getValue());
            }
            customerListGetRequest.setStartDate(memberInfoConditionDto.getStartDate());
            customerListGetRequest.setEndDate(memberInfoConditionDto.getEndDate());
            customerListGetRequest.setPeriodType(memberInfoConditionDto.getPeriodType());
            if (memberInfoConditionDto.getMemberInfoSendMailPermitFlag() != null) {
                customerListGetRequest.setMemberInfoSendMailPermitFlag(
                                memberInfoConditionDto.getMemberInfoSendMailPermitFlag().getValue());
            }
            customerListGetRequest.setLastLoginUserAgent(memberInfoConditionDto.getLastLoginUserAgent());
            if (memberInfoConditionDto.getMailMagazine() != null) {
                customerListGetRequest.setMailMagazine(memberInfoConditionDto.getMailMagazine().getValue().equals("0"));
            }
            if (memberInfoConditionDto.getMainMemberFlag() != null) {
                customerListGetRequest.setMainMemberFlag(
                                memberInfoConditionDto.getMainMemberFlag().getValue().equals("0"));
            }
        }

        return customerListGetRequest;
    }

    /**
     * To member info seq list list.
     *
     * @param memberInfoModel 会員検索モデル
     * @return リスト
     */
    public List<Integer> toMemberInfoSeqList(MemberInfoModel memberInfoModel) {
        List<Integer> memberInfoSeqList = new ArrayList<>();
        for (MemberInfoResultItem resultItem : memberInfoModel.getResultItems()) {
            if (resultItem.isResultMemberInfoCheck()) {
                memberInfoSeqList.add(resultItem.getMemberInfoSeq());
            }
        }

        return memberInfoSeqList;
    }

    /**
     * To customer list csv get request customer list csv get request.
     *
     * @param memberInfoConditionDto 会員Dao用検索条件Dtoクラス
     * @return 会員一覧取CSVDLリクエスト
     */
    public CustomerListCsvGetRequest toCustomerListCsvGetRequest(MemberInfoSearchForDaoConditionDto memberInfoConditionDto) {
        CustomerListCsvGetRequest customerListCsvGetRequest = new CustomerListCsvGetRequest();

        if (memberInfoConditionDto != null) {
            customerListCsvGetRequest.setMemberInfoId(memberInfoConditionDto.getMemberInfoId());
            customerListCsvGetRequest.setMemberInfoSeq(memberInfoConditionDto.getMemberInfoSeq());
            customerListCsvGetRequest.setMemberInfoName(memberInfoConditionDto.getMemberInfoName());
            if (memberInfoConditionDto.getMemberInfoSex() != null) {
                customerListCsvGetRequest.setMemberInfoSex(memberInfoConditionDto.getMemberInfoSex().getValue());
            }
            customerListCsvGetRequest.setMemberInfoBirthday(memberInfoConditionDto.getMemberInfoBirthday());
            customerListCsvGetRequest.setMemberInfoTel(memberInfoConditionDto.getMemberInfoTel());
            customerListCsvGetRequest.setMemberInfoZipCode(memberInfoConditionDto.getMemberInfoZipCode());
            customerListCsvGetRequest.setMemberInfoPrefecture(memberInfoConditionDto.getMemberInfoPrefecture());
            customerListCsvGetRequest.setMemberInfoAddress(memberInfoConditionDto.getMemberInfoAddress());
            if (memberInfoConditionDto.getMemberInfoStatus() != null) {
                customerListCsvGetRequest.setMemberInfoStatus(memberInfoConditionDto.getMemberInfoStatus().getValue());
            }
            customerListCsvGetRequest.setStartDate(memberInfoConditionDto.getStartDate());
            customerListCsvGetRequest.setEndDate(memberInfoConditionDto.getEndDate());
            customerListCsvGetRequest.setPeriodType(memberInfoConditionDto.getPeriodType());
            if (memberInfoConditionDto.getMemberInfoSendMailPermitFlag() != null) {
                customerListCsvGetRequest.setMemberInfoSendMailPermitFlag(
                                memberInfoConditionDto.getMemberInfoSendMailPermitFlag().getValue());
            }
            customerListCsvGetRequest.setLastLoginUserAgent(memberInfoConditionDto.getLastLoginUserAgent());
            if (memberInfoConditionDto.getMailMagazine() != null) {
                customerListCsvGetRequest.setMailMagazine(
                                memberInfoConditionDto.getMailMagazine().getValue().equals("0"));
            }
            if (memberInfoConditionDto.getMainMemberFlag() != null) {
                customerListCsvGetRequest.setMainMemberFlag(
                                memberInfoConditionDto.getMainMemberFlag().getValue().equals("0"));
            }
        }

        return customerListCsvGetRequest;
    }

    /**
     * To customer list csv get request customer list csv get request.
     *
     * @param memberInfoSeqList 整数リスト
     * @return 会員一覧取CSVDLリクエスト
     */
    public CustomerListCsvGetRequest toCustomerListCsvGetRequest(List<Integer> memberInfoSeqList) {
        CustomerListCsvGetRequest customerListCsvGetRequest = new CustomerListCsvGetRequest();

        if (memberInfoSeqList != null) {
            customerListCsvGetRequest.setMemberInfoSeqList(memberInfoSeqList);
        }

        return customerListCsvGetRequest;
    }

    public List<CsvDownloadOptionDto> toCsvDownloadOptionDtoList(CustomerCsvOptionListResponse customerCsvOptionListResponse) {

        if (customerCsvOptionListResponse == null || CollectionUtils.isEmpty(
                        customerCsvOptionListResponse.getCsvDownloadOptionList())) {
            return new ArrayList<>();
        }

        return customerCsvOptionListResponse.getCsvDownloadOptionList().stream().map(csvOptionResponse -> {
            CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();

            csvDownloadOptionDto.setOptionId(csvOptionResponse.getOptionId());
            csvDownloadOptionDto.setDefaultOptionName(csvOptionResponse.getDefaultOptionName());
            csvDownloadOptionDto.setOptionName(csvOptionResponse.getOptionName());
            csvDownloadOptionDto.setOutHeader(csvOptionResponse.getOutHeader());

            return csvDownloadOptionDto;
        }).collect(Collectors.toList());
    }

    public CustomerCsvOptionUpdateRequest toCustomerCsvOptionUpdateRequest(CsvDownloadOptionDto csvDownloadOptionDto) {

        if (csvDownloadOptionDto == null) {
            return null;
        }

        CustomerCsvOptionUpdateRequest customerCsvOptionUpdateRequest = new CustomerCsvOptionUpdateRequest();

        customerCsvOptionUpdateRequest.setOptionId(csvDownloadOptionDto.getOptionId());
        customerCsvOptionUpdateRequest.setOptionName(csvDownloadOptionDto.getOptionName());
        customerCsvOptionUpdateRequest.setDefaultOptionName(csvDownloadOptionDto.getDefaultOptionName());
        customerCsvOptionUpdateRequest.setOutHeader(csvDownloadOptionDto.getOutHeader());

        List<OptionContent> optionContentList = toOptionContentList(csvDownloadOptionDto.getOptionContent());
        customerCsvOptionUpdateRequest.setOptionContent(optionContentList);

        return customerCsvOptionUpdateRequest;
    }

    private List<OptionContent> toOptionContentList(List<OptionDto> optionDtoList) {

        if (CollectionUtils.isEmpty(optionDtoList)) {
            return new ArrayList<>();
        }

        return optionDtoList.stream().map(optionDto -> {
            OptionContent optionContent = new OptionContent();

            optionContent.setItemName(optionDto.getItemName());
            optionContent.setDefaultOrder(optionDto.getDefaultOrder());
            optionContent.setOrder(optionDto.getOrder());
            optionContent.setDefaultColumnLabel(optionDto.getDefaultColumnLabel());
            optionContent.setColumnLabel(optionDto.getColumnLabel());
            optionContent.setOutFlag(optionDto.getOutFlag());

            return optionContent;
        }).collect(Collectors.toList());
    }
}