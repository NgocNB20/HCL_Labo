/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistory;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistoryListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

import static jp.co.itechh.quad.admin.pc.web.admin.memberinfo.MemberInfoDetailsOrderItem.CANCEL;
import static jp.co.itechh.quad.admin.pc.web.admin.memberinfo.MemberInfoDetailsOrderItem.ITEM_PREPARING;
import static jp.co.itechh.quad.admin.pc.web.admin.memberinfo.MemberInfoDetailsOrderItem.PAYMENT_CONFIRMING;
import static jp.co.itechh.quad.admin.pc.web.admin.memberinfo.MemberInfoDetailsOrderItem.SHIPMENT_COMPLETION;
import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.PAYMENT_ERROR;

/**
 * 会員詳細HELPER
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class MemberInfoDetailsHelper {

    /** 変換Utility取得 */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     * @param dateUtility       日付関連Utilityクラス
     */
    @Autowired
    public MemberInfoDetailsHelper(ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * ページに反映<br/>
     *
     * @param detailsDto             会員詳細Dto
     * @param memberInfoDetailsModel ページ
     * @param addressResponse        住所録レスポンス
     */
    public void toModelDetailsForLoad(MemberInfoDetailsDto detailsDto,
                                      MemberInfoDetailsModel memberInfoDetailsModel,
                                      AddressBookAddressResponse addressResponse) {
        // 会員状態情報
        setupMemberStatusInfo(detailsDto, memberInfoDetailsModel);
        // お客様情報
        setupMemberInfo(detailsDto, memberInfoDetailsModel, addressResponse);
        // 最終ログイン情報
        setupLastLoginInfo(detailsDto, memberInfoDetailsModel);

        // 受注履歴はこのメソッド内に含めないようにする
        // ※コントローラから別途呼び出す想定

        // 会員エンティティ
        memberInfoDetailsModel.setMemberInfoEntity(detailsDto.getMemberInfoEntity());
    }

    /**
     * 会員詳細Dtoに変換
     *
     * @param customerResponse 会員レスポンス
     * @return MemberInfoDetailsDto 会員詳細Dtoクラス
     */
    public MemberInfoDetailsDto toMemberInfoDetailsDto(CustomerResponse customerResponse) {

        MemberInfoDetailsDto detailsDto = new MemberInfoDetailsDto();
        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();

        memberInfoEntity.setMemberInfoSeq(customerResponse.getMemberInfoSeq());

        if (customerResponse.getMemberInfoStatus() != null) {
            memberInfoEntity.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                               customerResponse.getMemberInfoStatus()
                                                                              ));
        }

        memberInfoEntity.setMemberInfoUniqueId(customerResponse.getMemberInfoUniqueId());
        memberInfoEntity.setMemberInfoId(customerResponse.getMemberInfoId());
        memberInfoEntity.setMemberInfoPassword(customerResponse.getMemberInfoPassword());
        memberInfoEntity.setMemberInfoFirstName(customerResponse.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastName(customerResponse.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoFirstKana(customerResponse.getMemberInfoFirstKana());
        memberInfoEntity.setMemberInfoLastKana(customerResponse.getMemberInfoLastKana());

        if (customerResponse.getMemberInfoSex() != null) {
            memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                            customerResponse.getMemberInfoSex()
                                                                           ));
        }

        memberInfoEntity.setMemberInfoBirthday(conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
        memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
        memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());
        memberInfoEntity.setAccessUid(customerResponse.getAccessUid());
        memberInfoEntity.setVersionNo(customerResponse.getVersionNo());
        memberInfoEntity.setAdmissionYmd(customerResponse.getAdmissionYmd());
        memberInfoEntity.setSecessionYmd(customerResponse.getSecessionYmd());
        memberInfoEntity.setMemo(customerResponse.getMemo());
        memberInfoEntity.setLastLoginTime(conversionUtility.toTimestamp(customerResponse.getLastLoginTime()));
        memberInfoEntity.setLastLoginUserAgent(customerResponse.getLastLoginUserAgent());
        memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(customerResponse.getRegistTime()));
        memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(customerResponse.getUpdateTime()));

        detailsDto.setMemberInfoEntity(memberInfoEntity);

        return detailsDto;
    }

    /**
     * 会員状態情報をページに反映する<br/>
     *
     * @param detailsDto             会員詳細Dto
     * @param memberInfoDetailsModel ページ
     */
    protected void setupMemberStatusInfo(MemberInfoDetailsDto detailsDto,
                                         MemberInfoDetailsModel memberInfoDetailsModel) {

        MemberInfoEntity entity = detailsDto.getMemberInfoEntity();
        // 状態
        memberInfoDetailsModel.setMemberInfoStatus(entity.getMemberInfoStatus());
        // 入会日
        memberInfoDetailsModel.setAdmissionYmd(entity.getAdmissionYmd());
        // 退会日
        memberInfoDetailsModel.setSecessionYmd(entity.getSecessionYmd());
        // 登録日時
        memberInfoDetailsModel.setRegistTime(entity.getRegistTime());
        // 更新日時
        memberInfoDetailsModel.setUpdateTime(entity.getUpdateTime());
    }

    /**
     * お客様情報をページに反映する<br/>
     *
     * @param detailsDto             会員詳細Dto
     * @param memberInfoDetailsModel ページ
     * @param addressResponse        住所録レスポンス
     */
    protected void setupMemberInfo(MemberInfoDetailsDto detailsDto,
                                   MemberInfoDetailsModel memberInfoDetailsModel,
                                   AddressBookAddressResponse addressResponse) {
        MemberInfoEntity entity = detailsDto.getMemberInfoEntity();
        // 会員ID
        memberInfoDetailsModel.setMemberInfoId(entity.getMemberInfoId());
        // 氏名
        memberInfoDetailsModel.setMemberInfoName(
                        entity.getMemberInfoLastName() + "　" + entity.getMemberInfoFirstName());
        // フリガナ
        memberInfoDetailsModel.setMemberInfoKana(
                        entity.getMemberInfoLastKana() + "　" + entity.getMemberInfoFirstKana());
        // 性別
        memberInfoDetailsModel.setMemberInfoSex(entity.getMemberInfoSex());
        // 誕生日
        memberInfoDetailsModel.setMemberInfoBirthday(entity.getMemberInfoBirthday());
        // 電話番号
        memberInfoDetailsModel.setMemberInfoTel(entity.getMemberInfoTel());
        if (addressResponse != null) {
            // 郵便番号
            memberInfoDetailsModel.setMemberInfoZipCode(addressResponse.getZipCode());
            // 住所
            String address = addressResponse.getPrefecture() + addressResponse.getAddress1()
                             + addressResponse.getAddress2();
            if (addressResponse.getAddress3() != null) {
                address = address + addressResponse.getAddress3();
            }
            memberInfoDetailsModel.setMemberInfoAddress(address);
        }
    }

    /**
     * 最終ログイン情報をページに反映する<br/>
     *
     * @param detailsDto             会員詳細Dto
     * @param memberInfoDetailsModel ページ
     */
    protected void setupLastLoginInfo(MemberInfoDetailsDto detailsDto, MemberInfoDetailsModel memberInfoDetailsModel) {
        MemberInfoEntity entity = detailsDto.getMemberInfoEntity(); // .memberInfoEntity;
        // 日時
        if (entity.getLastLoginTime() != null) {
            memberInfoDetailsModel.setLastLoginTime(
                            conversionUtility.toYmd(entity.getLastLoginTime()) + " " + conversionUtility.toHms(
                                            entity.getLastLoginTime()));
        }
        // ユーザーエージェント
        memberInfoDetailsModel.setLastLoginUserAgent(entity.getLastLoginUserAgent());// .lastLoginUserAgent;
    }

    /**
     * 受注履歴をモデルに反映する<br/>
     *
     * @param customerHistoryList    顧客注文履歴一覧レスポンス
     * @param memberInfoDetailsModel 会員詳細モデル
     */
    public void toModelForOrderHistory(CustomerHistoryListResponse customerHistoryList,
                                       MemberInfoDetailsModel memberInfoDetailsModel) {

        if (CollectionUtils.isEmpty(customerHistoryList.getCustomerHistoryList())) {
            return;
        }

        // オフセット + 1をNoにセット
        int index = ((customerHistoryList.getPageInfo().getPage() - 1) * customerHistoryList.getPageInfo()
                                                                                            .getLimit()) + 1;

        // 注文履歴
        memberInfoDetailsModel.setOrderHistoryItems(new ArrayList<>());

        // 注文履歴数分ループ
        for (CustomerHistory customerHistory : customerHistoryList.getCustomerHistoryList()) {
            // 注文履歴情報を作成
            MemberInfoDetailsOrderItem detailsPageOrderItem = createOrderItem(customerHistory);
            // No
            detailsPageOrderItem.setResultNo(index++);
            // 注文履歴リストに設定
            memberInfoDetailsModel.getOrderHistoryItems().add(detailsPageOrderItem);
        }
    }

    /**
     * 注文履歴Itemを作成する<br/>
     *
     * @param customerHistory 顧客注文履歴
     * @return MemberInfoDetailsOrderItem
     */
    protected MemberInfoDetailsOrderItem createOrderItem(CustomerHistory customerHistory) {

        // 会員詳細画面注文履歴情報を作成
        MemberInfoDetailsOrderItem detailsPageOrderItem =
                        ApplicationContextUtility.getBean(MemberInfoDetailsOrderItem.class);

        // 受注番号
        detailsPageOrderItem.setOrderCode(customerHistory.getOrderCode());

        // 受注日
        detailsPageOrderItem.setOrderTime(this.conversionUtility.toTimestamp(customerHistory.getOrderReceivedDate()));

        // 受注金額
        detailsPageOrderItem.setOrderPrice(new BigDecimal(customerHistory.getPaymentPrice()));

        // 注文ステータス
        // 商品準備中
        if (ITEM_PREPARING.equals(customerHistory.getOrderStatus())) {
            detailsPageOrderItem.setOrderStatus(HTypeOrderStatus.GOODS_PREPARING.getLabel());
        }
        // 入金確認中
        else if (PAYMENT_CONFIRMING.equals(customerHistory.getOrderStatus())) {
            detailsPageOrderItem.setOrderStatus(HTypeOrderStatus.PAYMENT_CONFIRMING.getLabel());
        }
        // 出荷完了
        else if (SHIPMENT_COMPLETION.equals(customerHistory.getOrderStatus())) {
            detailsPageOrderItem.setOrderStatus(HTypeOrderStatus.SHIPMENT_COMPLETION.getLabel());
        }
        // キャンセル
        else if (CANCEL.equals(customerHistory.getOrderStatus())) {
            detailsPageOrderItem.setOrderStatus(HTypeCancelFlag.ON.getLabel());
        }
        // 請求決済エラー
        else if (PAYMENT_ERROR.equals(customerHistory.getOrderStatus())) {
            detailsPageOrderItem.setOrderStatus(HTypeOrderStatus.PAYMENT_ERROR.getLabel());
        }

        return detailsPageOrderItem;
    }
}