package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipMethodUpdateRequest;
import jp.co.itechh.quad.card.presentation.api.param.CardInfoResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeBillType;
import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypePaymentType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethod;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponApplyRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponCancelRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * お支払い方法選択画面 Helper
 *
 * @author Pham Quang Dieu
 */
@Component
public class PaySelectHelper {

    /** 変換Utility取得 */
    private final ConversionUtility conversionUtility;

    /** 区切り文字：カンマ */
    protected static final String SEPARATOR_COMMA = ",";

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換Utility取得
     */
    public PaySelectHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 「別のカードを使う」押下時Modelのカード情報をクリアする<br/>
     *
     * @param paySelectModel Model
     */
    public void toPageForChangeOtherCard(PaySelectModel paySelectModel) {
        // 受注仮Dtoのカード情報登録状態フラグをfalseにする
        paySelectModel.setUseRegistCardFlg(false);
        paySelectModel.setDisplayCredit(false);
        paySelectModel.setPreCreditInformationFlag(true);
        for (PaySelectModelItem item : paySelectModel.getPaySelectModelItems()) {
            if (HTypeSettlementMethodType.CREDIT.equals(item.getSettlementMethodType())) {
                // カード番号
                item.setCardNumber(null);
                // 有効期限：月
                item.setExpirationDateMonth(null);
                // 有効期限：年
                item.setExpirationDateYear(null);
                // セキュアコード
                item.setSecurityCode(null);
            }
        }
    }

    /**
     * お預かりカード情報をModelに反映する<br/>
     *
     * @param paySelectModel お支払い方法選択Model
     * @param resultCard     お預かりカード結果
     */
    protected void setupRegistedCardInfo(PaySelectModel paySelectModel, SearchCardOutput resultCard) {

        if (paySelectModel.getResultCard() == null || CollectionUtil.isEmpty(
                        paySelectModel.getResultCard().getCardList())) {
            paySelectModel.setUseRegistCardFlg(false);
            paySelectModel.setDisplayCredit(false);
            return;
        }

        CardInfo cardInfo = (CardInfo) paySelectModel.getResultCard().getCardList().get(0);

        for (PaySelectModelItem modelItem : paySelectModel.getPaySelectModelItems()) {
            if (HTypeSettlementMethodType.CREDIT.equals(modelItem.getSettlementMethodType())) {
                // カード番号
                modelItem.setCardNumber(cardInfo.getCardNo());
                // 有効期限：月
                modelItem.setExpirationDateMonth(cardInfo.getExpire().substring(0, 2));
                // 有効期限：年
                modelItem.setExpirationDateYear(cardInfo.getExpire().substring(4));
            }
        }
    }

    /**
     * 退避した決済方法リストが有る場合、決済方法SEQが一致する情報に入力情報を設定する
     *
     * @param newItems 新規生成した決済方法Item
     * @param oldItems 保持していた決済方法Item
     */
    public void restorePaymentPageItem(List<PaySelectModelItem> newItems, List<PaySelectModelItem> oldItems) {
        if (CollectionUtil.isEmpty(newItems) || CollectionUtil.isEmpty(oldItems)) {
            return;
        }

        for (PaySelectModelItem newItem : newItems) {
            for (PaySelectModelItem oldItem : oldItems) {
                // 退避情報の決済方法SEQと再取得した情報の決済方法SEQを比較
                if (StringUtils.isNotEmpty(newItem.getSettlementMethodValue()) && newItem.getSettlementMethodValue()
                                                                                         .equals(oldItem.getSettlementMethodValue())) {
                    restorePaymentPageItem(newItem, oldItem);
                }
            }
        }
    }

    /**
     * 決済方法の設定内容を復元する
     *
     * @param newItem 新規生成した決済方法Item
     * @param oldItem 保持していた決済方法Item
     */
    protected void restorePaymentPageItem(PaySelectModelItem newItem, PaySelectModelItem oldItem) {
        if (oldItem != null) {
            // 決済方法別に設定
            if (HTypeSettlementMethodType.CREDIT == newItem.getSettlementMethodType()) {
                // クレジット決済
                // カード番号設定
                newItem.setCardNumber(oldItem.getCardNumber());
                // 分割回数設定
                // 分割が有効の場合に設定
                if (HTypeEffectiveFlag.VALID == newItem.getEnableInstallment()) {
                    newItem.setDividedNumber(oldItem.getDividedNumber());
                }
                // 有効期限（月）設定
                newItem.setExpirationDateMonth(oldItem.getExpirationDateMonth());
                // 有効期限（年）設定
                newItem.setExpirationDateYear(oldItem.getExpirationDateYear());
                // 支払区分設定
                // 選択が分割で有効状態、選択がリボで有効状態の場合
                if ((HTypePaymentType.INSTALLMENT.getValue().equals(oldItem.getPaymentType())
                     && HTypeEffectiveFlag.VALID == newItem.getEnableInstallment()) || (
                                    HTypePaymentType.REVOLVING.getValue().equals(oldItem.getPaymentType())
                                    && HTypeEffectiveFlag.VALID == newItem.getEnableRevolving())) {
                    // 選択した支払区分を設定
                    newItem.setPaymentType(oldItem.getPaymentType());
                } else {
                    // 選択した支払区分が無効状態または選択が一括の場合に、一括を設定
                    newItem.setPaymentType(HTypePaymentType.SINGLE.getValue());
                }
                // セキュリティコード設定
                newItem.setSecurityCode(oldItem.getSecurityCode());

            }
        }
    }

    /**
     * 「前回利用したカードを使う」押下時Modelのカード情報をModelに反映する<br/>
     *
     * @param paySelectModel お支払い方法選択Model
     */
    public void toPageForChangeRegistedCard(PaySelectModel paySelectModel) {
        // 受注仮Dtoのカード情報登録状態フラグをtrueにする
        paySelectModel.setUseRegistCardFlg(true);
        paySelectModel.setDisplayCredit(true);
        // カード情報をModelに反映
        setupRegistedCardInfo(paySelectModel, paySelectModel.getResultCard());
    }

    /**
     * 受注情報を画面に反映する。<br />
     * 追加したクーポン関連の項目を画面に反映する。
     *
     * @param paySelectModel    お支払い方法選択Model
     * @param couponResponse    クーポンレスポンス
     * @param salesSlipResponse 販売伝票レスポンス
     */
    public void toPageForLoadForCoupon(PaySelectModel paySelectModel,
                                       CouponResponse couponResponse,
                                       SalesSlipResponse salesSlipResponse) {

        // クーポン情報が取得できたら、クーポンが適用されているので、クーポン名と割引金額を取得する
        if (couponResponse != null) {
            paySelectModel.setCouponName(salesSlipResponse.getCouponName());
            paySelectModel.setCouponDiscountPrice((salesSlipResponse.getCouponPaymentPrice() != null) ?
                                                                  BigDecimal.valueOf(
                                                                                  salesSlipResponse.getCouponPaymentPrice()) :
                                                                  BigDecimal.ZERO);
        } else {
            paySelectModel.setCouponDiscountPrice(BigDecimal.ZERO);
        }

        // クーポン適用後に決済方法を選択する場合に、クーポンコードが内部に保持されていることによる、バリデーションエラーの発生を防ぐためクリアする
        paySelectModel.setCouponCode("");
    }

    /**
     * 選択可能決済方法一覧取得リクエストに変換
     *
     * @param transactionId 取引ID
     * @return 選択可能決済方法一覧取得リクエスト
     */
    public SelectablePaymentMethodListGetRequest toSelectPayMethodListGetRequest(String transactionId) {

        SelectablePaymentMethodListGetRequest selectablePaymentMethodListGetRequest =
                        new SelectablePaymentMethodListGetRequest();
        selectablePaymentMethodListGetRequest.setTransactionId(transactionId);

        return selectablePaymentMethodListGetRequest;
    }

    /**
     * 会員クラスに変換
     *
     * @param customerResponse 会員レスポンス
     * @return 会員クラス
     */
    public MemberInfoEntity toMemberInfoEntity(CustomerResponse customerResponse) {
        // 処理前は存在しないためnullを返す
        if (customerResponse == null) {
            return null;
        }

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
        memberInfoEntity.setMemberInfoLastName(customerResponse.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoFirstName(customerResponse.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastKana(customerResponse.getMemberInfoLastKana());
        memberInfoEntity.setMemberInfoFirstKana(customerResponse.getMemberInfoFirstKana());
        if (customerResponse.getMemberInfoSex() != null) {
            memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                            customerResponse.getMemberInfoSex()
                                                                           ));
        }
        memberInfoEntity.setMemberInfoBirthday(conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
        memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
        memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());
        memberInfoEntity.setMemberInfoAddressId(customerResponse.getMemberInfoAddressId());
        memberInfoEntity.setAccessUid(customerResponse.getAccessUid());
        memberInfoEntity.setVersionNo(customerResponse.getVersionNo());
        memberInfoEntity.setAdmissionYmd(customerResponse.getAdmissionYmd());
        memberInfoEntity.setSecessionYmd(customerResponse.getSecessionYmd());
        memberInfoEntity.setMemo(customerResponse.getMemo());
        memberInfoEntity.setLastLoginTime(conversionUtility.toTimestamp(customerResponse.getLastLoginTime()));
        memberInfoEntity.setLastLoginUserAgent(customerResponse.getLastLoginUserAgent());
        memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(customerResponse.getRegistTime()));
        memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(customerResponse.getUpdateTime()));

        return memberInfoEntity;
    }

    /**
     * カード参照出力パラメータに変換
     *
     * @param cardInfoResponse クレジットカード情報レスポンス
     * @return カード参照出力パラメータ
     */
    public SearchCardOutput toSearchCardOutput(CardInfoResponse cardInfoResponse) {
        // 処理前は存在しないためnullを返す
        if (cardInfoResponse == null) {
            return null;
        }

        SearchCardOutput searchCardOutput = new SearchCardOutput();

        List cartList = new ArrayList();

        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNo(cardInfoResponse.getRegistedCardMaskNo());
        String expire = cardInfoResponse.getExpireMonth() + cardInfoResponse.getExpireYear();
        cardInfo.setExpire(expire);

        cartList.add(cardInfo);
        searchCardOutput.setCardList(cartList);

        return searchCardOutput;
    }

    /**
     * 決済Dtoリストに変換
     *
     * @param selectable     選択可能決済方法一覧レスポンス
     * @param paySelectModel お支払い方法選択Model
     * @return 決済Dtoリスト
     */
    public List<SettlementDto> toSettlementDto(SelectablePaymentMethodListResponse selectable,
                                               PaySelectModel paySelectModel) {

        List<SettlementDto> settlementDtoList = new ArrayList<>();

        if (selectable != null && selectable.getSelectablePaymentMethodList() != null) {

            for (SelectablePaymentMethod selectablePaymentMethod : selectable.getSelectablePaymentMethodList()) {

                SettlementDto settlementDto = new SettlementDto();

                SettlementDetailsDto settlementDetailsDto = new SettlementDetailsDto();
                settlementDetailsDto.setSettlementMethodSeq(
                                conversionUtility.toInteger(selectablePaymentMethod.getPaymentMethodId()));
                if (selectablePaymentMethod.getSettlementMethodType() != null) {
                    settlementDetailsDto.setSettlementMethodType(
                                    EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                  selectablePaymentMethod.getSettlementMethodType()
                                                                 ));
                }
                settlementDetailsDto.setSettlementMethodName(selectablePaymentMethod.getPaymentMethodName());
                if (selectablePaymentMethod.getBillingType() != null) {
                    settlementDetailsDto.setBillType(EnumTypeUtil.getEnumFromValue(HTypeBillType.class,
                                                                                   selectablePaymentMethod.getBillingType()
                                                                                  ));
                }
                settlementDetailsDto.setSettlementNotePC(selectablePaymentMethod.getPaymentMethodNote());
                settlementDetailsDto.setEnableInstallment(
                                HTypeEffectiveFlag.valueByBool(selectablePaymentMethod.getEnableInstallmentFlag()));
                settlementDetailsDto.setEnableRevolving(
                                HTypeEffectiveFlag.valueByBool(selectablePaymentMethod.getEnableRevolvingFlag()));

                if (HTypeSettlementMethodType.CREDIT.equals(settlementDetailsDto.getSettlementMethodType())
                    && StringUtils.isNotEmpty(selectablePaymentMethod.getInstallmentCounts())) {

                    Map<String, String> dividedNumberItems = new LinkedHashMap<>();
                    for (String installmentCount : selectablePaymentMethod.getInstallmentCounts()
                                                                          .split(SEPARATOR_COMMA)) {
                        dividedNumberItems.put(installmentCount, installmentCount);
                    }

                    paySelectModel.setDividedNumberItems(dividedNumberItems);
                }

                settlementDto.setSettlementDetailsDto(settlementDetailsDto);

                settlementDtoList.add(settlementDto);
            }
        }

        return settlementDtoList;
    }

    /**
     * クーポン適用リクエストに変換
     *
     * @param transactionId 取引ID
     * @param couponCode    クーポンコード
     * @return クーポン適用リクエスト
     */
    public SalesSlipCouponApplyRequest toSalesSlipCouponApplyRequest(String transactionId, String couponCode) {

        SalesSlipCouponApplyRequest salesSlipCouponApplyRequest = new SalesSlipCouponApplyRequest();
        salesSlipCouponApplyRequest.setTransactionId(transactionId);
        salesSlipCouponApplyRequest.setCouponCode(couponCode);

        return salesSlipCouponApplyRequest;
    }

    /**
     * クーポン取消リクエストに変換
     *
     * @param transactionId 取引ID
     * @return クーポン取消リクエスト
     */
    public SalesSlipCouponCancelRequest toSalesSlipCouponCancelRequest(String transactionId) {

        SalesSlipCouponCancelRequest salesSlipCouponCancelRequest = new SalesSlipCouponCancelRequest();
        salesSlipCouponCancelRequest.setTransactionId(transactionId);

        return salesSlipCouponCancelRequest;

    }

    /**
     * 販売伝票取得リクエストに変換
     *
     * @param transactionId 取引ID
     * @return 販売伝票取得リクエスト
     */
    public BillingSlipGetRequest toBillingSlipGetRequest(String transactionId) {

        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(transactionId);

        return billingSlipGetRequest;

    }

    /**
     * 販売伝票取得リクエストに変換
     *
     * @param transactionId  取引ID
     * @param paySelectModel お支払い方法選択Model
     * @return 販売伝票取得リクエスト
     */
    public BillingSlipMethodUpdateRequest toBillingSlipMethodUpdateRequest(String transactionId,
                                                                           PaySelectModel paySelectModel) {

        BillingSlipMethodUpdateRequest billingSlipMethodUpdateRequest = new BillingSlipMethodUpdateRequest();
        billingSlipMethodUpdateRequest.setTransactionId(transactionId);
        billingSlipMethodUpdateRequest.setPaymentMethodId(paySelectModel.getSettlementMethod());
        if (paySelectModel.getPaySelectModelItems() != null) {
            for (PaySelectModelItem payselectModelItem : paySelectModel.getPaySelectModelItems()) {

                if (HTypeSettlementMethodType.CREDIT.equals(payselectModelItem.getSettlementMethodType())
                    && payselectModelItem.getSettlementMethodValue().equals(paySelectModel.getSettlementMethod())) {

                    // セキュリティコードはこちらでは設定しない。注文内容確認画面のモデルに別の処理で引き渡す
                    billingSlipMethodUpdateRequest.setPaymentToken(paySelectModel.getToken());
                    billingSlipMethodUpdateRequest.setMaskedCardNo(payselectModelItem.getCardNumber());
                    billingSlipMethodUpdateRequest.setExpirationMonth(payselectModelItem.getExpirationDateMonth());
                    billingSlipMethodUpdateRequest.setExpirationYear(paySelectModel.getExpirationDateYearItems()
                                                                                   .get(payselectModelItem.getExpirationDateYear()));
                    billingSlipMethodUpdateRequest.setPaymentType(payselectModelItem.getPaymentType());
                    billingSlipMethodUpdateRequest.setDividedNumber(payselectModelItem.getDividedNumber());
                    billingSlipMethodUpdateRequest.setRegistCardFlag(payselectModelItem.isSaveFlg());
                    billingSlipMethodUpdateRequest.setUseRegistedCardFlag(paySelectModel.isUseRegistCardFlg());
                }

            }
        }

        return billingSlipMethodUpdateRequest;
    }

}
