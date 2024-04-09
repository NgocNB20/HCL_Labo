package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.admin.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * InquiryHelper Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class InquiryHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public InquiryHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件を画面に反映
     * 再検索用
     *
     * @param conditionDto 検索条件Dto
     * @param inquiryModel 検索ページ
     */
    public void toPageForLoad(InquirySearchDaoConditionDto conditionDto, InquiryModel inquiryModel) {

        /* 各検索条件を画面に反映する */

        // 検索条件：問い合わせ分類SEQ
        inquiryModel.setSearchInquiryGroupSeq(conditionDto.getInquiryGroupSeq());

        // 検索条件：問い合わせ状態
        inquiryModel.setSearchInquiryStatus(EnumTypeUtil.getValue(conditionDto.getInquiryStatus()));

        // 検索条件：問い合わせコード
        inquiryModel.setSearchInquiryCode(conditionDto.getInquiryCode());

        // 検索条件：問い合わせ日時(FROM)
        inquiryModel.setSearchInquiryTimeFrom(conversionUtility.toYmd(conditionDto.getInquiryTimeFrom()));

        // 検索条件：問い合わせ日時(TO)
        inquiryModel.setSearchInquiryTimeTo(conversionUtility.toYmd(conditionDto.getInquiryTimeTo()));

        // 検索条件：問い合わせ者(氏名)
        inquiryModel.setSearchInquiryName(conditionDto.getInquiryName());

        // 検索条件：問い合わせ者(E-Mail)
        inquiryModel.setSearchInquiryMail(conditionDto.getInquiryMail());

        // 検索条件：問い合わせ種別
        inquiryModel.setSearchInquiryType(conditionDto.getInquiryType());

        // 検索条件：注文番号
        inquiryModel.setSearchOrderCode(conditionDto.getOrderCode());

        // 検索条件：電話番号
        inquiryModel.setSearchInquiryTel(conditionDto.getInquiryTel());

        // 検索条件：担当者（最終担当者）
        inquiryModel.setSearchLastRepresentative(conditionDto.getLastRepresentative());

        // 検索条件：連携メモ
        inquiryModel.setSearchCooperationMemo(conditionDto.getCooperationMemo());

        // 検索条件：会員ID(メールアドレス)
        inquiryModel.setSearchMemberInfoMail(conditionDto.getMemberInfoMail());

    }

    /**
     * 問い合わせ情報一覧取得リクエストに反映
     *
     * @param inquiryModel 検索画面のページ情報
     * @return 問い合わせ情報一覧取得リクエスト
     */
    public InquiryListGetRequest toInquiryListGetRequestForSearch(InquiryModel inquiryModel) {

        InquiryListGetRequest inquiryListGetRequest = new InquiryListGetRequest();

        // 問い合わせ分類
        inquiryListGetRequest.setInquiryGroupSeq(inquiryModel.getSearchInquiryGroupSeq());
        // 問い合わせ状態
        inquiryListGetRequest.setInquiryStatus(inquiryModel.getSearchInquiryStatus());
        // 問い合わせコード
        inquiryListGetRequest.setInquiryCode(inquiryModel.getSearchInquiryCode());
        // 問い合わせ日時(FROM)
        inquiryListGetRequest.setInquiryTimeFrom(
                        conversionUtility.toTimeStamp(inquiryModel.getSearchInquiryTimeFrom()));
        // 問い合わせ日時(To)
        if (inquiryModel.getSearchInquiryTimeTo() != null) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

            inquiryListGetRequest.setInquiryTimeTo(dateUtility.getEndOfDate(
                            conversionUtility.toTimeStamp(inquiryModel.getSearchInquiryTimeTo())));
        }
        // メールアドレス
        inquiryListGetRequest.setInquiryMail(inquiryModel.getSearchInquiryMail());
        // 氏名
        String inquiryName = null;
        if (inquiryModel.getSearchInquiryName() != null) {
            inquiryName = inquiryModel.getSearchInquiryName().replace(" ", "").replace("　", "");
        }
        inquiryListGetRequest.setInquiryName(inquiryName);

        // 問い合わせ種別
        inquiryListGetRequest.setInquiryType(inquiryModel.getSearchInquiryType());
        // 注文番号
        inquiryListGetRequest.setOrderCode(inquiryModel.getSearchOrderCode());
        // 電話番号
        inquiryListGetRequest.setInquiryTel(inquiryModel.getSearchInquiryTel());
        // 担当者（最終担当者）
        inquiryListGetRequest.setLastRepresentative(inquiryModel.getSearchLastRepresentative());
        // 連携メモ
        inquiryListGetRequest.setCooperationMemo(inquiryModel.getSearchCooperationMemo());
        // 会員ID(メールアドレス)
        inquiryListGetRequest.setMemberInfoMail(inquiryModel.getSearchMemberInfoMail());

        return inquiryListGetRequest;
    }

    /**
     * 検索結果をページに反映<br/>
     *
     * @param inquiryListResponse 問い合わせ情報一覧レスポンス
     * @param resultList          問い合わせDao用検索結果Dtoクラスリスト
     * @param inquiryModel        問い合わせ検索ページ
     */
    public void toPageForSearch(InquiryListResponse inquiryListResponse,
                                List<InquirySearchResultDto> resultList,
                                InquiryModel inquiryModel) {
        List<InquiryModelItem> resultItemList = new ArrayList<>();

        // オフセット + 1をNoにセット
        int index = ((inquiryListResponse.getPageInfo().getPage() - 1) * inquiryListResponse.getPageInfo()
                                                                                            .getLimit()) + 1;

        for (InquirySearchResultDto inquirySearchResultDto : resultList) {
            InquiryModelItem inquiryModelItem = ApplicationContextUtility.getBean(InquiryModelItem.class);

            inquiryModelItem.setResultNo(index++);
            inquiryModelItem.setInquirySeq(inquirySearchResultDto.getInquirySeq());

            if (inquirySearchResultDto.getInquiryStatus() != null) {
                inquiryModelItem.setInquiryStatus(inquirySearchResultDto.getInquiryStatus().getValue());
            }
            inquiryModelItem.setInquiryCode(inquirySearchResultDto.getInquiryCode());
            inquiryModelItem.setInquiryGroupName(inquirySearchResultDto.getInquiryGroupName());
            inquiryModelItem.setInquiryLastName(inquirySearchResultDto.getInquiryLastName());
            inquiryModelItem.setInquiryFirstName(inquirySearchResultDto.getInquiryFirstName());
            inquiryModelItem.setInquiryTime(inquirySearchResultDto.getInquiryTime());
            inquiryModelItem.setAnswerTime(inquirySearchResultDto.getAnswerTime());

            // 検索条件：お問い合わせ種別
            inquiryModelItem.setResultInquiryType(inquirySearchResultDto.getInquiryType());

            // 検索条件：初回お問い合わせ日時
            inquiryModelItem.setResultFirstInquiryTime(inquirySearchResultDto.getFirstInquiryTime());

            // 検索条件：最終お客様お問い合わせ日時
            inquiryModelItem.setResultLastUserInquiryTime(inquirySearchResultDto.getLastUserInquiryTime());

            // 検索条件：注文番号
            inquiryModelItem.setOrderCode(inquirySearchResultDto.getOrderCode());

            // 検索条件：担当者（最終担当者）
            inquiryModelItem.setResultLastRepresentative(inquirySearchResultDto.getLastRepresentative());

            // 検索条件：問い合わせ者氏名
            inquiryModelItem.setResultInquiryName(inquirySearchResultDto.getInquiryLastName());

            // 検索条件：連携メモ
            inquiryModelItem.setResultCooperationMemo(inquirySearchResultDto.getCooperationMemo());

            resultItemList.add(inquiryModelItem);
        }
        inquiryModel.setResultItems(resultItemList);
    }

    /**
     * 問い合わせDao用検索結果Dtoクラスのリストに反映
     *
     * @param inquiryDetailsResponseList 問い合わせ情報レスポンスのリスト
     * @return 問い合わせDao用検索結果Dtoクラスのリスト
     */
    public List<InquirySearchResultDto> toInquirySearchResultDtoList(List<InquiryDetailsResponse> inquiryDetailsResponseList) {
        List<InquirySearchResultDto> inquirySearchResultDtoList = new ArrayList<>();

        Integer shopSeq = 1001;

        inquiryDetailsResponseList.forEach(item -> {
            InquirySearchResultDto inquirySearchResultDto = new InquirySearchResultDto();

            inquirySearchResultDto.setShopSeq(shopSeq);
            inquirySearchResultDto.setInquirySeq(item.getInquirySeq());
            inquirySearchResultDto.setInquiryStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class, item.getInquiryStatus()));
            inquirySearchResultDto.setInquiryCode(item.getInquiryCode());
            inquirySearchResultDto.setInquiryGroupSeq(String.valueOf(item.getInquirySeq()));
            inquirySearchResultDto.setInquiryGroupName(item.getInquiryGroupName());
            inquirySearchResultDto.setInquiryLastName(item.getInquiryLastName());
            inquirySearchResultDto.setInquiryFirstName(item.getInquiryFirstName());
            inquirySearchResultDto.setInquiryTime(conversionUtility.toTimestamp(item.getInquiryTime()));
            inquirySearchResultDto.setAnswerTime(conversionUtility.toTimestamp(item.getAnswerTime()));
            inquirySearchResultDto.setInquiryType(item.getInquiryType());
            inquirySearchResultDto.setOrderCode(item.getOrderCode());
            inquirySearchResultDto.setInquiryTel(item.getInquiryTel());
            inquirySearchResultDto.setFirstInquiryTime(conversionUtility.toTimestamp(item.getFirstInquiryTime()));
            inquirySearchResultDto.setLastUserInquiryTime(conversionUtility.toTimestamp(item.getLastUserInquiryTime()));
            inquirySearchResultDto.setLastRepresentative(item.getLastRepresentative());
            inquirySearchResultDto.setCooperationMemo(item.getMemo());
            inquirySearchResultDto.setMemberInfoMail(item.getMemberInfoMail());

            inquirySearchResultDtoList.add(inquirySearchResultDto);
        });

        return inquirySearchResultDtoList;
    }

    /**
     * 問い合わせ分類クラスのリストに反映
     *
     * @param inquiryGroupResponseList 問い合わせ分類レスポンス
     * @return 問い合わせ分類クラスのリスト
     */
    public List<InquiryGroupEntity> toInquiryGroupEntityList(List<InquiryGroupResponse> inquiryGroupResponseList) {
        List<InquiryGroupEntity> inquiryGroupEntityList = new ArrayList<>();

        Integer shopSeq = 1001;

        inquiryGroupResponseList.forEach(item -> {
            InquiryGroupEntity inquiryGroupEntity = new InquiryGroupEntity();

            inquiryGroupEntity.setInquiryGroupSeq(item.getInquiryGroupSeq());
            inquiryGroupEntity.setShopSeq(shopSeq);
            inquiryGroupEntity.setInquiryGroupName(item.getInquiryGroupName());
            inquiryGroupEntity.setOpenStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class, item.getOpenStatus()));
            inquiryGroupEntity.setOrderDisplay(item.getOrderDisplay());
            inquiryGroupEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            inquiryGroupEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            inquiryGroupEntityList.add(inquiryGroupEntity);
        });

        return inquiryGroupEntityList;
    }

}