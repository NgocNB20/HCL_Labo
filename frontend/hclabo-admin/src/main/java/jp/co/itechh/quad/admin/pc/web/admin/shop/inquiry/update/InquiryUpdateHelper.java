package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update;

import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryRequestType;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.admin.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CommonInfoUtility;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquirySubResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * InquiryUpdateHelper Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class InquiryUpdateHelper {

    /**
     * 問い合わせ者表示用スペース
     */
    private static final String SPACE = "　";

    /**
     * 問合せ者表示用敬称
     */
    private static final String HONORIFIC = "様";

    /**
     * お客様スタイル
     */
    private static final String BGCOLOR_CONSUMER = "left user";

    /**
     * 運用者スタイル
     */
    private static final String BGCOLOR_OPERATOR = "left operator";

    /**
     * 変更スタイル
     */
    private static final String CHANGE_STYLE_LEFT = "left diff";

    /**
     * 変更スタイル
     */
    private static final String CHANGE_STYLE = "diff";

    /**
     * 日付関連Utility
     */
    private final DateUtility dateUtility;

    /**
     * 共通情報関連ユーティリティ
     */
    private final CommonInfoUtility commonInfoUtility;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility       日付関連Utility
     * @param commonInfoUtility 共通情報関連ユーティリティ
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public InquiryUpdateHelper(DateUtility dateUtility,
                               CommonInfoUtility commonInfoUtility,
                               ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.commonInfoUtility = commonInfoUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 問い合わせ内容リストを画面に反映
     *
     * @param inquiryDetailsDto  問い合わせ詳細Dto
     * @param inquiryUpdateModel 問い合わせ詳細画面
     */
    public void toPageForInquiryItem(InquiryDetailsDto inquiryDetailsDto, InquiryUpdateModel inquiryUpdateModel) {

        // 問い合わせ詳細DTO設定
        inquiryUpdateModel.setInquiryDetailsDto(inquiryDetailsDto);
        // 問い合わせ分類名の設定
        inquiryUpdateModel.setInquiryGroupName(inquiryDetailsDto.getInquiryGroupName());
        // 問い合わせ情報の設定
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        // 問い合わせSEQ
        inquiryUpdateModel.setInquirySeq(inquiryEntity.getInquirySeq());
        // ご連絡番号
        inquiryUpdateModel.setInquiryCode(inquiryEntity.getInquiryCode());
        // 問い合わせ種別
        inquiryUpdateModel.setInquiryType(inquiryEntity.getInquiryType().getValue());
        // 問い合わせ状態
        inquiryUpdateModel.setInquiryStatus(inquiryEntity.getInquiryStatus().getValue());
        // 問い合わせ者氏名（姓）
        inquiryUpdateModel.setInquiryLastName(inquiryEntity.getInquiryLastName());
        // 問い合わせ者氏名（名）
        inquiryUpdateModel.setInquiryFirstName(inquiryEntity.getInquiryFirstName());
        // 問い合わせ者氏名フリガナ（姓）
        inquiryUpdateModel.setInquiryLastKana(inquiryEntity.getInquiryLastKana());
        // 問い合わせ者フリガナ（名）
        inquiryUpdateModel.setInquiryFirstKana(inquiryEntity.getInquiryFirstKana());
        // 問い合わせ者電話番号
        inquiryUpdateModel.setInquiryTel(inquiryEntity.getInquiryTel());
        // 問い合わせ者メールアドレス
        inquiryUpdateModel.setInquiryMail(inquiryEntity.getInquiryMail());
        // 会員SEQ
        Integer memberInfoSeq = inquiryEntity.getMemberInfoSeq();
        inquiryUpdateModel.setMemberInfoSeq(memberInfoSeq == 0 ? null : memberInfoSeq.toString());
        // 会員ID
        if (inquiryDetailsDto.getMemberInfoEntity() != null) {
            inquiryUpdateModel.setMemberInfoId(inquiryDetailsDto.getMemberInfoEntity().getMemberInfoId());
        } else if (!inquiryUpdateModel.isResetMemberInfoId()) {
            inquiryUpdateModel.setMemberInfoId(null);
        }
        // 初回問い合わせ日時
        inquiryUpdateModel.setFirstInquiryTime(inquiryEntity.getFirstInquiryTime());
        // 連携メモ
        inquiryUpdateModel.setCooperationMemo(inquiryEntity.getCooperationMemo());
        // 管理メモ
        inquiryUpdateModel.setMemo(inquiryEntity.getMemo());

        // 問い合わせ内容情報リストの設定
        List<InquiryDetailItem> inquiryDetailItemList = new ArrayList<>();

        for (InquiryDetailEntity inquiryDetail : inquiryDetailsDto.getInquiryDetailEntityList()) {
            InquiryDetailItem inquiryDetailItem = ApplicationContextUtility.getBean(InquiryDetailItem.class);
            // 連番
            inquiryDetailItem.setInquiryVersionNo(inquiryDetail.getInquiryVersionNo());
            // 発信者種別
            inquiryDetailItem.setRequestType(inquiryDetail.getRequestType().getValue());
            // 問い合わせ日時
            inquiryDetailItem.setInquiryTime(inquiryDetail.getInquiryTime());
            // 問い合わせ内容
            inquiryDetailItem.setInquiryBody(inquiryDetail.getInquiryBody());
            // 部署名
            inquiryDetailItem.setDivisionName(inquiryDetail.getDivisionName());
            // 担当者
            inquiryDetailItem.setRepresentative(inquiryDetail.getRepresentative());
            // 処理担当者
            inquiryDetailItem.setOperator(inquiryDetail.getOperator());
            // 問い合わせ発信者種別
            if (inquiryDetail.getRequestType() == HTypeInquiryRequestType.CONSUMER) {
                // お客様の場合
                // 問い合わせ者氏名
                inquiryDetailItem.setInquiryMan(inquiryUpdateModel.getInquiryLastName() + SPACE
                                                + inquiryUpdateModel.getInquiryFirstName() + SPACE + HONORIFIC);
                // スタイルの設定
                inquiryDetailItem.setBgColorInquiryDetailManClass(BGCOLOR_CONSUMER);
                inquiryDetailItem.setBgColorInquiryDetailTimeClass(BGCOLOR_CONSUMER);
                inquiryDetailItem.setBgColorInquiryDetailBodyClass(BGCOLOR_CONSUMER);

            } else {
                // 運用者の場合
                // 部署名 + 担当者
                inquiryDetailItem.setInquiryMan(
                                inquiryDetail.getDivisionName() + SPACE + inquiryDetail.getRepresentative());
                // 連絡先TEL
                inquiryDetailItem.setContactTel(inquiryDetail.getContactTel());
                // スタイルの設定
                inquiryDetailItem.setBgColorInquiryDetailManClass(BGCOLOR_OPERATOR);
                inquiryDetailItem.setBgColorInquiryDetailTimeClass(BGCOLOR_OPERATOR);
                inquiryDetailItem.setBgColorInquiryDetailBodyClass(BGCOLOR_OPERATOR);
            }
            // 問い合わせ内容リストに追加
            inquiryDetailItemList.add(inquiryDetailItem);
        }

        // 再検索時入力項目を削除
        // 問い合わせ内容
        inquiryUpdateModel.setInquiryBody(null);
        // 問い合わせ内容(完了)
        inquiryUpdateModel.setInputCompletionReport(null);

        // 問い合わせ内容の設定
        inquiryUpdateModel.setInquiryDetailItems(inquiryDetailItemList);
    }

    /**
     * 会員IDの設定
     *
     * @param inquiryUpdateModel 問い合わせ詳細画面
     * @return 問い合わせクラス
     */
    public InquiryEntity toPageForInquiryEntityMember(InquiryUpdateModel inquiryUpdateModel) {

        inquiryUpdateModel.getInquiryDetailsDto()
                          .getInquiryEntity()
                          .setMemberInfoId(inquiryUpdateModel.getMemberInfoId());

        return inquiryUpdateModel.getInquiryDetailsDto().getInquiryEntity();
    }

    /**
     * 会員SEQの削除
     *
     * @param inquiryUpdateModel 問い合わせ詳細画面
     * @return inquiryEntity 問い合わせクラス
     */
    public InquiryEntity toPageForInquiryEntityMemberRelease(InquiryUpdateModel inquiryUpdateModel) {

        // 会員SEQ削除
        inquiryUpdateModel.getInquiryDetailsDto().getInquiryEntity().setMemberInfoSeq(0);
        return inquiryUpdateModel.getInquiryDetailsDto().getInquiryEntity();
    }

    /**
     * 管理メモの設定
     *
     * @param inquiryUpdateModel 問い合わせ詳細画面
     * @return 問い合わせクラス
     */
    public InquiryEntity toPageForInquiryEntityMemo(InquiryUpdateModel inquiryUpdateModel) {

        InquiryEntity inquiryEntity = inquiryUpdateModel.getInquiryDetailsDto().getInquiryEntity();

        // 連携メモ
        inquiryEntity.setCooperationMemo(inquiryUpdateModel.getCooperationMemo());
        // 管理メモ
        inquiryEntity.setMemo(inquiryUpdateModel.getMemo());

        return inquiryEntity;
    }

    /**
     * 初期表示時処理
     *
     * @param inquiryUpdateModel 問い合わせ更新確認画面
     */
    public void toPageForLoad(InquiryUpdateModel inquiryUpdateModel) {
        // 管理者ユーザー（苗字 + 名前）
        String operator = commonInfoUtility.getAdministratorName(inquiryUpdateModel.getCommonInfo());
        // 確認画面用問い合わせ情報の取得
        InquiryEntity inquiryEntity = inquiryUpdateModel.getConfirmInquiryDetailsDto().getInquiryEntity();

        // 最終運用者問い合わせ日時の設定
        inquiryEntity.setLastOperatorInquiryTime(dateUtility.getCurrentTime());
        // 最終担当者の設定
        inquiryEntity.setLastRepresentative(operator);
        // 問い合わせ状態の設定
        inquiryUpdateModel.setInquiryStatus(inquiryUpdateModel.getInquiryStatusFlg());
        // 初回問い合わせ日時の設定
        inquiryUpdateModel.setFirstInquiryTime(inquiryEntity.getFirstInquiryTime());
        // 問い合わせ内容リストの取得
        List<InquiryDetailItem> registInquiryDetailItems = inquiryUpdateModel.getConfirmInquiryDetailItems();
        // 問い合わせ内容の最下行に新規問い合わせ情報を追加する。
        InquiryDetailItem newInquiryDetailItem = ApplicationContextUtility.getBean(InquiryDetailItem.class);
        // 連番
        newInquiryDetailItem.setInquiryVersionNo(registInquiryDetailItems.size() + 1);
        // 問い合わせ日時
        newInquiryDetailItem.setInquiryTime(dateUtility.getCurrentTime());
        // 担当者
        newInquiryDetailItem.setOperator(operator);
        // 問い合わせ状態が完了の場合
        if (HTypeInquiryStatus.COMPLETION.getValue().equals(inquiryUpdateModel.getInquiryStatus())) {
            // リストに完了報告を追加
            newInquiryDetailItem.setInquiryBody(inquiryUpdateModel.getInputCompletionReport());
            // 問い合わせ状態が連絡案内受付中の場合
        } else {
            // リストに 返信問い合わせ内容を追加
            newInquiryDetailItem.setInquiryBody(inquiryUpdateModel.getInputInquiryBody());
        }
        // 問い合わせ状態スタイル設定 変更あれば背景色を設定
        if (StringUtil.isNotEmpty(inquiryUpdateModel.getInquiryStatus()) && !inquiryUpdateModel.getInquiryStatus()
                                                                                               .equals(inquiryUpdateModel.getPreInquiryStatus())) {
            inquiryUpdateModel.setBgColorConfirmInquiryStatusClass(CHANGE_STYLE);
        }

        // 発信者種別
        newInquiryDetailItem.setRequestType(HTypeInquiryRequestType.OPERATOR.getValue());
        // 運用者の場合
        // スタイルの設定
        newInquiryDetailItem.setBgColorInquiryDetailManClass(CHANGE_STYLE_LEFT);
        newInquiryDetailItem.setBgColorInquiryDetailTimeClass(CHANGE_STYLE_LEFT);
        newInquiryDetailItem.setBgColorInquiryDetailBodyClass(CHANGE_STYLE_LEFT);
        // 新規行を追加
        registInquiryDetailItems.add(newInquiryDetailItem);

    }

    /**
     * 登録後、内容初期化
     *
     * @param inquiryUpdateModel ページクラス
     */
    public void initData(InquiryUpdateModel inquiryUpdateModel) {

        inquiryUpdateModel.setFromConfirm(false);

        // 問い合わせ報告、完了報告初期化
        inquiryUpdateModel.setInputInquiryBody(null);
        inquiryUpdateModel.setInputCompletionReport(null);

        // メール送信フラグ 初期化
        inquiryUpdateModel.setSendMail(true);
        inquiryUpdateModel.setCompletionReportFlag(false);
    }

    /**
     * 登録時処理(問い合わせ内容エンティティ反映)
     *
     * @param inquiryUpdateModel 問い合わせ更新確認画面
     * @return InquiryDetailsDto 問い合わせ内容Dto
     */
    public InquiryUpdateRequest toInquiryUpdateRequest(InquiryUpdateModel inquiryUpdateModel) {

        InquiryDetailsDto inquiryDetailsDto = CopyUtil.deepCopy(inquiryUpdateModel.getConfirmInquiryDetailsDto());

        // 新規登録する問い合わせ内容エンティティの作成
        InquiryDetailEntity inquiryDetail = ApplicationContextUtility.getBean(InquiryDetailEntity.class);

        // 最下行の問い合わせ内容情報を取得
        InquiryDetailItem inquiryDetailItem = inquiryUpdateModel.getConfirmInquiryDetailItems()
                                                                .get(inquiryUpdateModel.getConfirmInquiryDetailItems()
                                                                                       .size() - 1);
        // 発信者種別:運用者
        inquiryDetail.setRequestType(HTypeInquiryRequestType.OPERATOR);
        // 問い合わせSEQ
        inquiryDetail.setInquirySeq(inquiryUpdateModel.getInquirySeq());
        // 連番
        inquiryDetail.setInquiryVersionNo(inquiryDetailItem.getInquiryVersionNo());
        // 問い合わせ日時
        inquiryDetail.setInquiryTime(inquiryDetailItem.getInquiryTime());
        // 問い合わせ内容
        inquiryDetail.setInquiryBody(inquiryDetailItem.getInquiryBody());
        // 部署名
        inquiryDetail.setDivisionName(inquiryDetailItem.getDivisionName());
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

        InquiryUpdateRequest inquiryUpdateRequest = new InquiryUpdateRequest();
        inquiryUpdateRequest.setInquirySeq(inquiryUpdateModel.getInquirySeq());
        inquiryUpdateRequest.setInquiryStatus(inquiryUpdateModel.getInquiryStatus());
        inquiryUpdateRequest.setSendMailFlag(inquiryUpdateModel.isSendMailFlag());

        List<InquiryUpdateDetailRequest> inquiryDetailList = new ArrayList<>();

        list.forEach(item -> {
            InquiryUpdateDetailRequest inquiryUpdateDetailRequest = new InquiryUpdateDetailRequest();

            inquiryUpdateDetailRequest.setInquirySeq(item.getInquirySeq());
            inquiryUpdateDetailRequest.setInquiryVersionNo(item.getInquiryVersionNo());
            inquiryUpdateDetailRequest.setRequestType(item.getRequestType().getValue());
            inquiryUpdateDetailRequest.setInquiryTime(item.getInquiryTime());
            inquiryUpdateDetailRequest.setInquiryBody(item.getInquiryBody());
            inquiryUpdateDetailRequest.setDivisionName(item.getDivisionName());
            inquiryUpdateDetailRequest.setRepresentative(item.getRepresentative());
            inquiryUpdateDetailRequest.setContactTel(item.getContactTel());
            inquiryUpdateDetailRequest.setOperator(item.getOperator());

            inquiryDetailList.add(inquiryUpdateDetailRequest);
        });

        inquiryUpdateRequest.setInquiryDetailList(inquiryDetailList);

        return inquiryUpdateRequest;
    }

    /**
     * 問い合わせ詳細Dtoクラスに反映
     *
     * @param inquiryResponse 問い合わせ情報レスポンス
     * @return 問い合わせ詳細Dtoクラス
     */
    public InquiryDetailsDto toInquiryDetailsDto(InquiryResponse inquiryResponse) {
        if (inquiryResponse.getInquirySubResponse() == null
            || inquiryResponse.getInquiryUpdateDetailsResponse() == null) {
            return null;
        }

        InquiryDetailsDto inquiryDetailsDto = new InquiryDetailsDto();

        InquiryEntity inquiryEntity = toInquiryEntity(inquiryResponse.getInquirySubResponse());
        List<InquiryDetailEntity> inquiryDetailEntityList =
                        toInquiryDetailEntityList(inquiryResponse.getInquiryUpdateDetailsResponse());

        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
        if (inquiryResponse.getInquiryCustomerDetailsResponse() != null
            && inquiryResponse.getInquiryCustomerDetailsResponse().getMemberInfoId() != null) {
            memberInfoEntity.setMemberInfoId(inquiryResponse.getInquiryCustomerDetailsResponse().getMemberInfoId());
        }

        inquiryDetailsDto.setMemberInfoEntity(memberInfoEntity);

        inquiryDetailsDto.setInquiryEntity(inquiryEntity);

        inquiryDetailsDto.setInquiryDetailEntityList(inquiryDetailEntityList);

        inquiryDetailsDto.setInquiryGroupName(inquiryResponse.getInquiryGroupName());

        return inquiryDetailsDto;
    }

    /**
     * 問い合わせクラスに反映
     *
     * @param inquirySubResponse 問い合わせ情報レスポンス
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
        inquiryEntity.setInquiryTime(conversionUtility.toTimestamp(inquirySubResponse.getInquiryTime()));
        inquiryEntity.setInquiryStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class, inquirySubResponse.getInquiryStatus()));
        inquiryEntity.setAnswerTime(conversionUtility.toTimestamp(inquirySubResponse.getAnswerTime()));
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
        inquiryEntity.setRegistTime(conversionUtility.toTimestamp(inquirySubResponse.getRegistTime()));
        inquiryEntity.setUpdateTime(conversionUtility.toTimestamp(inquirySubResponse.getUpdateTime()));
        inquiryEntity.setInquiryType(
                        EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class, inquirySubResponse.getInquiryType()));
        inquiryEntity.setMemberInfoSeq(inquirySubResponse.getMemberInfoSeq());
        inquiryEntity.setOrderCode(inquirySubResponse.getOrderCode());
        inquiryEntity.setFirstInquiryTime(conversionUtility.toTimestamp(inquirySubResponse.getFirstInquiryTime()));
        inquiryEntity.setLastUserInquiryTime(
                        conversionUtility.toTimestamp(inquirySubResponse.getLastUserInquiryTime()));
        inquiryEntity.setLastOperatorInquiryTime(
                        conversionUtility.toTimestamp(inquirySubResponse.getLastOperatorInquiryTime()));
        inquiryEntity.setLastRepresentative(inquirySubResponse.getLastRepresentative());
        inquiryEntity.setMemo(inquirySubResponse.getMemo());
        inquiryEntity.setCooperationMemo(inquirySubResponse.getCooperationMemo());
        inquiryEntity.setMemberInfoId(inquirySubResponse.getMemberInfoId());

        return inquiryEntity;
    }

    /**
     * 問い合わせ内容エンティティクラスのリストに反映
     *
     * @param responseList 問い合わせ情報レスポンスのリスト
     * @return 問い合わせ内容エンティティクラスのリスト
     */
    public List<InquiryDetailEntity> toInquiryDetailEntityList(List<InquiryUpdateDetailsResponse> responseList) {
        List<InquiryDetailEntity> inquiryDetailEntityList = new ArrayList<>();

        responseList.forEach(item -> {
            InquiryDetailEntity inquiryDetailEntity = new InquiryDetailEntity();

            inquiryDetailEntity.setInquirySeq(item.getInquirySeq());
            inquiryDetailEntity.setInquiryVersionNo(item.getInquiryVersionNo());
            inquiryDetailEntity.setRequestType(
                            EnumTypeUtil.getEnumFromValue(HTypeInquiryRequestType.class, item.getRequestType()));
            inquiryDetailEntity.setInquiryTime(conversionUtility.toTimestamp(item.getInquiryTime()));
            inquiryDetailEntity.setInquiryBody(item.getInquiryBody());
            inquiryDetailEntity.setDivisionName(item.getDivisionName());
            inquiryDetailEntity.setRepresentative(item.getRepresentative());
            inquiryDetailEntity.setContactTel(item.getContactTel());
            inquiryDetailEntity.setOperator(item.getOperator());
            inquiryDetailEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            inquiryDetailEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            inquiryDetailEntityList.add(inquiryDetailEntity);
        });

        return inquiryDetailEntityList;
    }
}
