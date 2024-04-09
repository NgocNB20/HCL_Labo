/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.customer.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSubscribeType;
import jp.co.itechh.quad.core.constant.type.HTypeMainMemberType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSex;
import jp.co.itechh.quad.core.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.core.dto.csv.CsvOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerForBackResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListCsvGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.OptionContent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会員スエンドポイント Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CustomerHelper {

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility
     * @param conversionUtility
     */
    public CustomerHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 顧客の応答に変換
     *
     * @param memberInfoEntity メンバー情報エンティティ
     * @return customerResponse 会員レスポンス
     */
    public CustomerResponse toCustomerResponse(MemberInfoEntity memberInfoEntity) {
        CustomerResponse customerResponse = new CustomerResponse();

        setResponseFromEntity(customerResponse, memberInfoEntity);

        return customerResponse;
    }

    /**
     * ページに反映<br/>
     *
     * @param detailsDto 会員詳細Dto
     * @return customerResponse 会員レスポンス
     */
    public CustomerResponse convertDetailsDtoToCustomerResponse(MemberInfoDetailsDto detailsDto) {
        CustomerResponse customerResponse = new CustomerResponse();

        if (detailsDto.getMemberInfoEntity() != null) {
            // 会員状態情報
            setupMemberStatusInfo(detailsDto, customerResponse);
            // お客様情報
            setupMemberInfo(detailsDto, customerResponse);
            // 最終ログイン情報
            setupLastLoginInfo(detailsDto, customerResponse);
        }

        return customerResponse;
    }

    /**
     * 検索条件Dtoの作成<br/>
     *
     * @param customerListGetRequest the customer list get request
     * @return 会員検索条件Dto member info search for dao condition dto
     */
    public MemberInfoSearchForDaoConditionDto toConditionDtoForSearch(CustomerListGetRequest customerListGetRequest) {
        // 検索条件Dto取得
        MemberInfoSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(MemberInfoSearchForDaoConditionDto.class);

        /* 画面条件 */
        // 会員ID
        conditionDto.setMemberInfoId(customerListGetRequest.getMemberInfoId());
        // 会員SEQ
        conditionDto.setMemberInfoSeq(conversionUtility.toInteger(customerListGetRequest.getMemberInfoSeq()));
        // 氏名
        String memberInfoName = null;
        if (customerListGetRequest.getMemberInfoName() != null) {
            memberInfoName = customerListGetRequest.getMemberInfoName().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoName(memberInfoName);
        // 性別
        conditionDto.setMemberInfoSex(
                        EnumTypeUtil.getEnumFromValue(HTypeSex.class, customerListGetRequest.getMemberInfoSex()));
        // 生年月日
        conditionDto.setMemberInfoBirthday(
                        dateUtility.convertDateToTimestamp(customerListGetRequest.getMemberInfoBirthday()));
        // 電話番号
        conditionDto.setMemberInfoTel(customerListGetRequest.getMemberInfoTel());
        // 状態
        conditionDto.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                       customerListGetRequest.getMemberInfoStatus()
                                                                      ));
        // 郵便番号
        conditionDto.setMemberInfoZipCode(customerListGetRequest.getMemberInfoZipCode());
        // 都道府県
        conditionDto.setMemberInfoPrefecture(customerListGetRequest.getMemberInfoPrefecture());
        // 住所
        String memberInfoAddress = null;
        if (customerListGetRequest.getMemberInfoAddress() != null) {
            memberInfoAddress = customerListGetRequest.getMemberInfoAddress().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoAddress(memberInfoAddress);
        // 期間種別
        conditionDto.setPeriodType(customerListGetRequest.getPeriodType());
        // 期間（FROM）
        conditionDto.setStartDate(customerListGetRequest.getStartDate());
        // 期間（TO）
        conditionDto.setEndDate(customerListGetRequest.getEndDate());
        // 最終ログインユーザーエージェント
        conditionDto.setLastLoginUserAgent(customerListGetRequest.getLastLoginUserAgent());

        // メルマガ購読フラグ
        if (Boolean.TRUE.equals(customerListGetRequest.getMailMagazine())) {
            conditionDto.setMailMagazine(HTypeMagazineSubscribeType.SUBSCRIBE);
        }

        // 本会員フラグ
        // 本会員
        if (Boolean.TRUE.equals(customerListGetRequest.getMainMemberFlag())) {
            conditionDto.setMainMemberFlag(HTypeMainMemberType.MAIN_MENBER);
        }
        // 暫定会員
        if (Boolean.FALSE.equals(customerListGetRequest.getMainMemberFlag())) {
            conditionDto.setMainMemberFlag(HTypeMainMemberType.PROVISIONAL_MEMBER);
        }

        return conditionDto;
    }

    /**
     * 顧客リストの応答に変換
     *
     * @param memberInfoForBackDtoList 会員情報管理サイト用リスト
     * @return customerListResponse 会員一覧レスポンス
     */
    public CustomerListResponse toCustomerListResponse(List<MemberInfoForBackDto> memberInfoForBackDtoList) {
        CustomerListResponse customerListResponse = new CustomerListResponse();
        List<CustomerForBackResponse> customerList = new ArrayList<>();

        for (MemberInfoForBackDto dto : memberInfoForBackDtoList) {
            CustomerForBackResponse customerResponse = new CustomerForBackResponse();

            setResponseFromDtoForBack(customerResponse, dto);

            customerList.add(customerResponse);
        }
        customerListResponse.setCustomerList(customerList);

        return customerListResponse;
    }

    /**
     * メンバー情報に変換
     *
     * @param customerRegistRequest 会員登録リクエスト
     * @return memberInfoEntity 会員クラス
     */
    public MemberInfoEntity toCustomerEntity(CustomerRegistRequest customerRegistRequest) {
        MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);

        // ID・メールアドレス
        memberInfoEntity.setMemberInfoId(customerRegistRequest.getMemberInfoMail());
        memberInfoEntity.setMemberInfoMail(customerRegistRequest.getMemberInfoMail());

        // 画面入力値で上書き
        memberInfoEntity.setMemberInfoFirstKana(customerRegistRequest.getMemberInfoFirstKana());
        memberInfoEntity.setMemberInfoFirstName(customerRegistRequest.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastKana(customerRegistRequest.getMemberInfoLastKana());
        memberInfoEntity.setMemberInfoLastName(customerRegistRequest.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoAddressId(customerRegistRequest.getMemberInfoAddressId());
        memberInfoEntity.setMemberInfoTel(customerRegistRequest.getMemberInfoTel());
        memberInfoEntity.setMemberInfoPassword(customerRegistRequest.getMemberInfoPassword());

        // 性別
        memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                        customerRegistRequest.getMemberInfoSex()
                                                                       ));

        // 生年月日
        if (StringUtils.isNotEmpty(customerRegistRequest.getMemberInfoBirthdayYear()) && StringUtils.isNotEmpty(
                        customerRegistRequest.getMemberInfoBirthdayMonth()) && StringUtils.isNotEmpty(
                        customerRegistRequest.getMemberInfoBirthdayDay())) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            memberInfoEntity.setMemberInfoBirthday(
                            conversionUtility.toTimeStamp(customerRegistRequest.getMemberInfoBirthdayYear(),
                                                          customerRegistRequest.getMemberInfoBirthdayMonth(),
                                                          customerRegistRequest.getMemberInfoBirthdayDay()
                                                         ));
        }

        return memberInfoEntity;
    }

    /**
     * 顧客更新リクエストをメンバー情報に変換
     *
     * @param customerUpdateRequest 会員更新リクエストt
     * @param memberInfoEntity      会員クラス
     * @return MemberInfoEntity 会員クラス
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MemberInfoEntity toCustomerEntity(CustomerUpdateRequest customerUpdateRequest,
                                             MemberInfoEntity memberInfoEntity)
                    throws InvocationTargetException, IllegalAccessException {
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 画面入力情報で上書き
        memberInfoEntity.setMemberInfoFirstKana(customerUpdateRequest.getMemberInfoFirstKana());
        memberInfoEntity.setMemberInfoFirstName(customerUpdateRequest.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastKana(customerUpdateRequest.getMemberInfoLastKana());
        memberInfoEntity.setMemberInfoLastName(customerUpdateRequest.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoAddressId(customerUpdateRequest.getMemberInfoAddressId());
        memberInfoEntity.setMemberInfoTel(customerUpdateRequest.getMemberInfoTel());

        // 性別
        memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                        customerUpdateRequest.getMemberInfoSex()
                                                                       ));

        // 生年月日
        if (customerUpdateRequest.getMemberInfoBirthdayYear() != null) {
            memberInfoEntity.setMemberInfoBirthday(
                            conversionUtility.toTimeStamp(customerUpdateRequest.getMemberInfoBirthdayYear(),
                                                          customerUpdateRequest.getMemberInfoBirthdayMonth(),
                                                          customerUpdateRequest.getMemberInfoBirthdayDay()
                                                         ));
        }

        return memberInfoEntity;
    }

    /**
     * 会員状態情報をページに反映する<br/>
     *
     * @param detailsDto       会員詳細Dto
     * @param customerResponse 顧客の応答
     */
    protected void setupMemberStatusInfo(MemberInfoDetailsDto detailsDto, CustomerResponse customerResponse) {
        MemberInfoEntity entity = detailsDto.getMemberInfoEntity();
        // 状態
        customerResponse.setMemberInfoStatus(EnumTypeUtil.getValue(entity.getMemberInfoStatus()));
        // 入会日
        customerResponse.setAdmissionYmd(
                        dateUtility.getYmdFormatValue(entity.getAdmissionYmd(), DateUtility.YMD_SLASH));
        // 退会日
        customerResponse.setSecessionYmd(
                        dateUtility.getYmdFormatValue(entity.getSecessionYmd(), DateUtility.YMD_SLASH));
        // 登録日時
        customerResponse.setRegistTime(entity.getRegistTime());
        // 更新日時
        customerResponse.setUpdateTime(entity.getUpdateTime());

    }

    /**
     * お客様情報をページに反映する<br/>
     *
     * @param detailsDto       会員詳細Dto
     * @param customerResponse 顧客の応答
     */
    protected void setupMemberInfo(MemberInfoDetailsDto detailsDto, CustomerResponse customerResponse) {
        MemberInfoEntity entity = detailsDto.getMemberInfoEntity();
        // 会員ID
        customerResponse.setMemberInfoId(entity.getMemberInfoId());
        // 氏名
        customerResponse.setMemberInfoFirstName(entity.getMemberInfoFirstName());
        customerResponse.setMemberInfoLastName(entity.getMemberInfoLastName());
        // フリガナ
        customerResponse.setMemberInfoFirstKana(entity.getMemberInfoFirstKana());
        customerResponse.setMemberInfoLastKana(entity.getMemberInfoLastKana());
        // 性別
        customerResponse.setMemberInfoSex(EnumTypeUtil.getValue(entity.getMemberInfoSex()));
        // 誕生日
        customerResponse.setMemberInfoBirthday(entity.getMemberInfoBirthday());
        // 電話番号
        customerResponse.setMemberInfoTel(entity.getMemberInfoTel());

        customerResponse.setMemberInfoAddressId(entity.getMemberInfoAddressId());
        customerResponse.setMemberInfoMail(entity.getMemberInfoMail());

        customerResponse.setMemberInfoSeq(detailsDto.getMemberInfoEntity().getMemberInfoSeq());
        customerResponse.setMemberInfoUniqueId(detailsDto.getMemberInfoEntity().getMemberInfoUniqueId());

    }

    /**
     * 最終ログイン情報をページに反映する<br/>
     *
     * @param detailsDto       会員詳細Dto
     * @param customerResponse 顧客の応答
     */
    protected void setupLastLoginInfo(MemberInfoDetailsDto detailsDto, CustomerResponse customerResponse) {
        MemberInfoEntity entity = detailsDto.getMemberInfoEntity(); // .memberInfoEntity;
        // 日時
        if (entity.getLastLoginTime() != null) {
            customerResponse.setLastLoginTime(entity.getLastLoginTime());
        }
        // ユーザーエージェント
        customerResponse.setLastLoginUserAgent(entity.getLastLoginUserAgent());// .lastLoginUserAgent;
    }

    protected void setResponseFromEntity(CustomerResponse customerResponse, MemberInfoEntity memberInfoEntity) {
        if (!ObjectUtils.isEmpty(memberInfoEntity)) {
            customerResponse.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
            customerResponse.setMemberInfoStatus(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoStatus()));
            customerResponse.setMemberInfoUniqueId(memberInfoEntity.getMemberInfoUniqueId());
            customerResponse.setMemberInfoId(memberInfoEntity.getMemberInfoId());
            customerResponse.setMemberInfoPassword(memberInfoEntity.getMemberInfoPassword());
            customerResponse.setMemberInfoLastName(memberInfoEntity.getMemberInfoLastName());
            customerResponse.setMemberInfoFirstName(memberInfoEntity.getMemberInfoFirstName());
            customerResponse.setMemberInfoLastKana(memberInfoEntity.getMemberInfoLastKana());
            customerResponse.setMemberInfoFirstKana(memberInfoEntity.getMemberInfoFirstKana());
            customerResponse.setMemberInfoSex(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoSex()));
            customerResponse.setMemberInfoBirthday(memberInfoEntity.getMemberInfoBirthday());
            customerResponse.setMemberInfoTel(memberInfoEntity.getMemberInfoTel());
            customerResponse.setMemberInfoAddressId(memberInfoEntity.getMemberInfoAddressId());
            customerResponse.setMemberInfoMail(memberInfoEntity.getMemberInfoMail());
            customerResponse.setAccessUid(memberInfoEntity.getAccessUid());
            customerResponse.setVersionNo(memberInfoEntity.getVersionNo());
            customerResponse.setAdmissionYmd(memberInfoEntity.getAdmissionYmd());
            customerResponse.setSecessionYmd(memberInfoEntity.getSecessionYmd());
            customerResponse.setMemo(memberInfoEntity.getMemo());
            customerResponse.setLastLoginTime(memberInfoEntity.getLastLoginTime());
            customerResponse.setLastLoginUserAgent(memberInfoEntity.getLastLoginUserAgent());
            customerResponse.setRegistTime(memberInfoEntity.getRegistTime());
            customerResponse.setUpdateTime(memberInfoEntity.getUpdateTime());
        }
    }

    protected void setResponseFromDtoForBack(CustomerForBackResponse customerResponse, MemberInfoForBackDto dto) {
        if (!ObjectUtils.isEmpty(dto)) {
            customerResponse.setMemberInfoSeq(dto.getMemberInfoSeq());
            customerResponse.setMemberInfoStatus(EnumTypeUtil.getValue(dto.getMemberInfoStatus()));
            customerResponse.setMemberInfoUniqueId(dto.getMemberInfoUniqueId());
            customerResponse.setMemberInfoId(dto.getMemberInfoId());
            customerResponse.setMemberInfoPassword(dto.getMemberInfoPassword());
            customerResponse.setMemberInfoLastName(dto.getMemberInfoLastName());
            customerResponse.setMemberInfoFirstName(dto.getMemberInfoFirstName());
            customerResponse.setMemberInfoLastKana(dto.getMemberInfoLastKana());
            customerResponse.setMemberInfoFirstKana(dto.getMemberInfoFirstKana());
            customerResponse.setMemberInfoSex(EnumTypeUtil.getValue(dto.getMemberInfoSex()));
            customerResponse.setMemberInfoBirthday(dto.getMemberInfoBirthday());
            customerResponse.setMemberInfoTel(dto.getMemberInfoTel());
            customerResponse.setMemberInfoAddressId(dto.getMemberInfoAddressId());
            customerResponse.setMemberInfoMail(dto.getMemberInfoMail());
            customerResponse.setAccessUid(dto.getAccessUid());
            customerResponse.setVersionNo(dto.getVersionNo());
            customerResponse.setAdmissionYmd(dto.getAdmissionYmd());
            customerResponse.setSecessionYmd(dto.getSecessionYmd());
            customerResponse.setMemo(dto.getMemo());
            customerResponse.setLastLoginTime(dto.getLastLoginTime());
            customerResponse.setLastLoginUserAgent(dto.getLastLoginUserAgent());
            customerResponse.setRegistTime(dto.getRegistTime());
            customerResponse.setUpdateTime(dto.getUpdateTime());

            // 住所情報
            customerResponse.setAddressName(dto.getAddressName());
            customerResponse.setZipCode(dto.getZipCode());
            customerResponse.setPrefecture(dto.getPrefecture());
            customerResponse.setAddress1(dto.getAddress1());
            customerResponse.setAddress2(dto.getAddress2());
            customerResponse.setAddress3(dto.getAddress3());
            customerResponse.setShippingMemo(dto.getShippingMemo());
        }
    }

    /**
     * 検索条件Dtoの作成<br/>
     *
     * @param customerListCsvGetRequest 会員一覧取CSVDLリクエスト
     * @return 会員検索条件Dto
     */
    public MemberInfoSearchForDaoConditionDto toConditionDtoForSearch(CustomerListCsvGetRequest customerListCsvGetRequest) {
        // 検索条件Dto取得
        MemberInfoSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(MemberInfoSearchForDaoConditionDto.class);

        /* 画面条件 */
        // 会員ID
        conditionDto.setMemberInfoId(customerListCsvGetRequest.getMemberInfoId());
        // 会員SEQ
        conditionDto.setMemberInfoSeq(conversionUtility.toInteger(customerListCsvGetRequest.getMemberInfoSeq()));
        // 氏名
        String memberInfoName = null;
        if (customerListCsvGetRequest.getMemberInfoName() != null) {
            memberInfoName = customerListCsvGetRequest.getMemberInfoName().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoName(memberInfoName);
        // 性別
        conditionDto.setMemberInfoSex(
                        EnumTypeUtil.getEnumFromValue(HTypeSex.class, customerListCsvGetRequest.getMemberInfoSex()));
        // 生年月日
        conditionDto.setMemberInfoBirthday(
                        dateUtility.convertDateToTimestamp(customerListCsvGetRequest.getMemberInfoBirthday()));
        // 電話番号
        conditionDto.setMemberInfoTel(customerListCsvGetRequest.getMemberInfoTel());
        // 状態
        conditionDto.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                       customerListCsvGetRequest.getMemberInfoStatus()
                                                                      ));
        // 郵便番号
        conditionDto.setMemberInfoZipCode(customerListCsvGetRequest.getMemberInfoZipCode());
        // 都道府県
        conditionDto.setMemberInfoPrefecture(customerListCsvGetRequest.getMemberInfoPrefecture());
        // 住所
        String memberInfoAddress = null;
        if (customerListCsvGetRequest.getMemberInfoAddress() != null) {
            memberInfoAddress = customerListCsvGetRequest.getMemberInfoAddress().replace(" ", "").replace("　", "");
        }
        conditionDto.setMemberInfoAddress(memberInfoAddress);
        // 期間種別
        conditionDto.setPeriodType(customerListCsvGetRequest.getPeriodType());
        // 期間（FROM）
        conditionDto.setStartDate(customerListCsvGetRequest.getStartDate());
        // 期間（TO）
        conditionDto.setEndDate(customerListCsvGetRequest.getEndDate());
        // 最終ログインユーザーエージェント
        conditionDto.setLastLoginUserAgent(customerListCsvGetRequest.getLastLoginUserAgent());

        // メルマガ購読フラグ
        if (Boolean.TRUE.equals(customerListCsvGetRequest.getMailMagazine())) {
            conditionDto.setMailMagazine(HTypeMagazineSubscribeType.SUBSCRIBE);
        }

        // 本会員フラグ
        // 本会員
        if (Boolean.TRUE.equals(customerListCsvGetRequest.getMainMemberFlag())) {
            conditionDto.setMainMemberFlag(HTypeMainMemberType.MAIN_MENBER);
        }
        // 暫定会員
        if (Boolean.FALSE.equals(customerListCsvGetRequest.getMainMemberFlag())) {
            conditionDto.setMainMemberFlag(HTypeMainMemberType.PROVISIONAL_MEMBER);
        }

        return conditionDto;
    }

    /**
     * 顧客住所エンティティの作成<br/>
     * TODO 顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）
     *  各項目のチェックは、物流サービスの住所録登録時にチェックされているため、ここでは対応しない
     *
     * @param addressId  住所ID
     * @param customerId 顧客ID
     * @param address    顧客住所登録リクエスト
     * @return 顧客住所エンティティ
     */
    public CustomerAddressBookEntity toCustomerAddressBookEntity(String addressId,
                                                                 String customerId,
                                                                 CustomerAddressRegistRequest address) {

        CustomerAddressBookEntity entity = ApplicationContextUtility.getBean(CustomerAddressBookEntity.class);

        entity.setAddressId(addressId);
        entity.setCustomerId(customerId);
        entity.setAddressName(address.getAddressName());
        entity.setLastName(address.getLastName());
        entity.setFirstName(address.getFirstName());
        entity.setLastKana(address.getLastKana());
        entity.setFirstKana(address.getFirstKana());
        entity.setTel(address.getTel());
        entity.setZipCode(address.getZipCode());
        entity.setPrefecture(address.getPrefecture());
        entity.setAddress1(address.getAddress1());
        entity.setAddress2(address.getAddress2());
        entity.setAddress3(address.getAddress3());
        entity.setShippingMemo(address.getShippingMemo());

        return entity;
    }

    /**
     * 受注検索CSVDLオプションレスポンスに変換.
     *
     * @param csvOptionDto
     * @return 受注検索CSVDLオプションレスポンス
     */
    public CustomerCsvOptionResponse toCustomerCsvOptionResponse(CsvOptionDto csvOptionDto) {

        CustomerCsvOptionResponse customerCsvOptionResponse = new CustomerCsvOptionResponse();

        customerCsvOptionResponse.setOptionId(conversionUtility.toString(csvOptionDto.getOptionId()));
        customerCsvOptionResponse.setOptionName(csvOptionDto.getOptionName());
        customerCsvOptionResponse.setOutHeader(csvOptionDto.isOutHeader());
        customerCsvOptionResponse.setOptionContent(toOptionContent(csvOptionDto.getOptionContent()));

        return customerCsvOptionResponse;
    }

    /**
     * 受注検索CSVDLオプションリストレスポンスに変換.
     *
     * @param csvOptionEntityList
     * @return 受注検索CSVDLオプションリストレスポンス
     */
    public CustomerCsvOptionListResponse toCustomerCsvOptionListResponse(List<CsvOptionEntity> csvOptionEntityList) {

        if (CollectionUtils.isEmpty(csvOptionEntityList)) {
            return null;
        }

        List<CustomerCsvOptionResponse> customerCsvOptionResponses = new ArrayList<>();

        csvOptionEntityList.forEach(csvOptionEntity -> {

            CsvOptionDto csvOptionInfoDto =
                            conversionUtility.toObject(csvOptionEntity.getOptionInfo(), CsvOptionDto.class);

            CustomerCsvOptionResponse customerCsvOptionResponse = new CustomerCsvOptionResponse();

            customerCsvOptionResponse.setOptionId(conversionUtility.toString(csvOptionEntity.getOptionId()));
            customerCsvOptionResponse.setDefaultOptionName(csvOptionInfoDto.getDefaultOptionName());
            customerCsvOptionResponse.setOptionName(csvOptionInfoDto.getOptionName());
            customerCsvOptionResponse.setOutHeader(csvOptionInfoDto.isOutHeader());

            List<OptionContent> optionContentList = toOptionContent(csvOptionInfoDto.getOptionContent());
            customerCsvOptionResponse.setOptionContent(optionContentList);

            customerCsvOptionResponses.add(customerCsvOptionResponse);

        });

        CustomerCsvOptionListResponse customerCsvOptionListResponse = new CustomerCsvOptionListResponse();
        customerCsvOptionListResponse.setCsvDownloadOptionList(customerCsvOptionResponses);

        return customerCsvOptionListResponse;
    }

    /**
     * CSVオプションDtoに変換.
     *
     * @param csvOptionEntity 受注検索CSVDLオプションの更新リクエスト
     * @return CSVオプションDto
     */
    public CsvOptionDto toCsvOptionDto(CsvOptionEntity csvOptionEntity) {

        if (csvOptionEntity == null) {
            return null;
        }

        CsvOptionDto csvOptionInfoDto = conversionUtility.toObject(csvOptionEntity.getOptionInfo(), CsvOptionDto.class);

        CsvOptionDto csvOptionDto = new CsvOptionDto();
        csvOptionDto.setOptionId(csvOptionEntity.getOptionId());
        csvOptionDto.setOptionName(csvOptionInfoDto.getOptionName());
        csvOptionDto.setOutHeader(csvOptionInfoDto.isOutHeader());
        csvOptionDto.setOptionContent(csvOptionInfoDto.getOptionContent());

        return csvOptionDto;
    }

    /**
     * To csv option entity csv option entity.
     *
     * @param request the request
     * @return the csv option entity
     */
    public CsvOptionEntity toCsvOptionEntity(CustomerCsvOptionUpdateRequest request) {

        CsvOptionDto csvOptionInfoDto = new CsvOptionDto();
        csvOptionInfoDto.setOptionId(conversionUtility.toInteger(request.getOptionId()));
        csvOptionInfoDto.setDefaultOptionName(request.getDefaultOptionName());
        csvOptionInfoDto.setOptionName(request.getOptionName());
        csvOptionInfoDto.setOutHeader(request.getOutHeader());
        csvOptionInfoDto.setOptionContent(toOptionContentDtoList(request.getOptionContent()));

        CsvOptionEntity csvOptionEntity = new CsvOptionEntity();
        csvOptionEntity.setOptionId(conversionUtility.toInteger(request.getOptionId()));
        csvOptionEntity.setOptionInfo(conversionUtility.toStringJson(csvOptionInfoDto));

        return csvOptionEntity;
    }

    /**
     * オプションコンテンツリストに変換.
     *
     * @param optionContentDtoList
     * @return オプションコンテンツリスト
     */
    private List<OptionContent> toOptionContent(List<OptionContentDto> optionContentDtoList) {

        if (CollectionUtils.isEmpty(optionContentDtoList)) {
            return new ArrayList<>();
        }

        return optionContentDtoList.stream().map(optionContentDto -> {
            OptionContent optionContent = new OptionContent();

            optionContent.setItemName(optionContentDto.getItemName());
            optionContent.setDefaultOrder(optionContentDto.getDefaultOrder());
            optionContent.setOrder(optionContentDto.getOrder());
            optionContent.setDefaultColumnLabel(optionContentDto.getDefaultColumnLabel());
            optionContent.setColumnLabel(optionContentDto.getColumnLabel());
            optionContent.setOutFlag(optionContentDto.isOutFlag());

            return optionContent;
        }).collect(Collectors.toList());
    }

    /**
     * オプションコンテンツDTOリストに変換.
     *
     * @param optionContentList
     * @return オプションコンテンツDTOリスト
     */
    private List<OptionContentDto> toOptionContentDtoList(List<OptionContent> optionContentList) {

        if (CollectionUtils.isEmpty(optionContentList)) {
            return new ArrayList<>();
        }

        return optionContentList.stream().map(optionContent -> {
            OptionContentDto optionContentDto = new OptionContentDto();

            optionContentDto.setItemName(optionContent.getItemName());
            optionContentDto.setDefaultOrder(optionContent.getDefaultOrder());
            optionContentDto.setOrder(optionContent.getOrder());
            optionContentDto.setDefaultColumnLabel(optionContent.getDefaultColumnLabel());
            optionContentDto.setColumnLabel(optionContent.getColumnLabel());
            optionContentDto.setOutFlag(optionContent.getOutFlag());

            return optionContentDto;
        }).collect(Collectors.toList());
    }
}