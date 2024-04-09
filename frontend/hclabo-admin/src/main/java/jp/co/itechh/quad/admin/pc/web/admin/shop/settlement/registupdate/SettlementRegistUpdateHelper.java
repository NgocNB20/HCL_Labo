package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate;

import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodPriceCommissionResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentRegistUpdateModelItemRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SettlementRegistUpdateHelper {

    /**
     * 詳細内容画面反映
     * @param settlementRegistUpdateModel 決済方法詳細設定画面
     * @param settlementMethodDto 決済方法DTO
     */
    public void toPage(SettlementRegistUpdateModel settlementRegistUpdateModel,
                       SettlementMethodDto settlementMethodDto) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        SettlementMethodEntity settlementMethodEntity = settlementMethodDto.getSettlementMethodEntity();
        // 決済方法情報設定
        HTypeSettlementMethodCommissionType commissionType = settlementMethodEntity.getSettlementMethodCommissionType();
        BigDecimal largeAmountDiscountPrice = settlementMethodEntity.getLargeAmountDiscountPrice();
        settlementRegistUpdateModel.setCommissionType(commissionType);

        // 請求種別
        settlementRegistUpdateModel.setBillType(EnumTypeUtil.getValue(settlementMethodEntity.getBillType()));
        // 請求種別表示用
        settlementRegistUpdateModel.setBillTypeValue(EnumTypeUtil.getValue(settlementMethodEntity.getBillType()));
        // 配送方法SEQ
        settlementRegistUpdateModel.setDeliveryMethodSeq(settlementMethodEntity.getDeliveryMethodSeq());
        // 公開状態PC
        settlementRegistUpdateModel.setOpenStatusPC(EnumTypeUtil.getValue(settlementMethodEntity.getOpenStatusPC()));
        // 表示順
        settlementRegistUpdateModel.setOrderDisplay(settlementMethodEntity.getOrderDisplay());

        // 最小購入金額
        settlementRegistUpdateModel.setMinPurchasedPrice(
                        conversionUtility.toString(settlementMethodEntity.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        settlementRegistUpdateModel.setSettlementMailRequired(
                        EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMailRequired()));
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        if (HTypeEffectiveFlag.VALID.equals(settlementMethodEntity.getEnableCardNoHolding())) {
            settlementRegistUpdateModel.setEnableCardNoHolding(true);
        }
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        if (HTypeEffectiveFlag.VALID.equals(settlementMethodEntity.getEnable3dSecure())) {
            settlementRegistUpdateModel.setEnable3dSecure(true);
        }
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        if (HTypeEffectiveFlag.VALID.equals(settlementMethodEntity.getEnableInstallment())) {
            settlementRegistUpdateModel.setEnableInstallment(true);
        }
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        if (HTypeEffectiveFlag.VALID.equals(settlementMethodEntity.getEnableRevolving())) {
            settlementRegistUpdateModel.setEnableRevolving(true);
        }

        // 登録日時
        settlementRegistUpdateModel.setRegistTime(settlementMethodEntity.getRegistTime());
        // 決済方法手数料種別
        settlementRegistUpdateModel.setSettlementMethodCommissionType(
                        EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMethodCommissionType()));
        // 決済方法表示名PC
        settlementRegistUpdateModel.setSettlementMethodDisplayNamePC(
                        settlementMethodEntity.getSettlementMethodDisplayNamePC());
        // 決済方法名
        settlementRegistUpdateModel.setSettlementMethodName(settlementMethodEntity.getSettlementMethodName());
        // 決済方法SEQ
        settlementRegistUpdateModel.setSettlementMethodSeq(settlementMethodEntity.getSettlementMethodSeq());
        // 決済方法SEQ
        settlementRegistUpdateModel.setSettlementMethodId(settlementMethodEntity.getSettlementMethodSeq());
        // 決済方法種別
        settlementRegistUpdateModel.setSettlementMethodType(
                        EnumTypeUtil.getValue(settlementMethodEntity.getSettlementMethodType()));
        // 決済方法種別表示用
        settlementRegistUpdateModel.setSettlementMethodTypeValue(
                        EnumTypeUtil.getLabelValue(settlementMethodEntity.getSettlementMethodType()));
        // 決済方法説明文PC
        settlementRegistUpdateModel.setSettlementNotePC(settlementMethodEntity.getSettlementNotePC());
        // ショップSEQ
        settlementRegistUpdateModel.setShopSeq(settlementMethodEntity.getShopSeq());
        // 更新日時
        settlementRegistUpdateModel.setUpdateTime(settlementMethodEntity.getUpdateTime());

        // 配送方法名
        settlementRegistUpdateModel.setDeliveryMethodName(settlementMethodDto.getDeliveryMethodName());

        // 決済方法金額別手数料設定
        HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag =
                        settlementMethodEntity.getSettlementMethodPriceCommissionFlag();
        settlementRegistUpdateModel.setSettlementMethodPriceCommissionFlag(settlementMethodPriceCommissionFlag);

        if (settlementMethodPriceCommissionFlag != null
            && HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT == settlementMethodPriceCommissionFlag) {

            if (ListUtils.isEmpty(settlementRegistUpdateModel.getPriceCommissionYen())) {
                List<SettlementMethodPriceCommissionEntity> commissonList =
                                settlementMethodDto.getSettlementMethodPriceCommissionEntityList();
                List<SettlementRegistUpdateModelItem> items = new ArrayList<>();

                int count = 0;
                for (SettlementMethodPriceCommissionEntity commisson : commissonList) {
                    SettlementRegistUpdateModelItem item =
                                    ApplicationContextUtility.getBean(SettlementRegistUpdateModelItem.class);
                    item.setCommission(conversionUtility.toString(commisson.getCommission()));
                    item.setMaxPrice((conversionUtility.toString(commisson.getMaxPrice())));
                    item.setRegisttime(commisson.getRegistTime());
                    item.setSettlementmethodseq(commisson.getSettlementMethodSeq());
                    item.setUpdatetime(commisson.getUpdateTime());
                    items.add(item);
                    count++;
                }
                for (; count < 10; count++) {
                    SettlementRegistUpdateModelItem item =
                                    ApplicationContextUtility.getBean(SettlementRegistUpdateModelItem.class);
                    items.add(item);
                }

                // if
                // (HTypeSettlementMethodCommissionType.EACH_AMOUNT_PERCENTAGE
                // ==
                // commissionType) {
                // page.setPriceCommissionPercentage(items);
                // } else {
                settlementRegistUpdateModel.setPriceCommissionYen(items);
                // }
            }
        } else {
            // if (HTypeSettlementMethodCommissionType.FLAT_PERCENTAGE ==
            // commissionType) {
            // // 一律手数料
            // page.setEqualsCommissionPercentage(conversionUtility.toString(settlementMethodEntity.equalsCommission));
            // if (largeAmountDiscountPrice != null &&
            // largeAmountDiscountPrice.compareTo(BigDecimal.ZERO) > 0) {
            // // 高額割引手数料
            // page.setLargeAmountDiscountCommissionPercentage(conversionUtility.toString(settlementMethodEntity.largeAmountDiscountCommission));
            // // 高額割引下限金額
            // page.setLargeAmountDiscountPricePercentage(conversionUtility.toString(largeAmountDiscountPrice));
            // }
            // // 最大購入金額
            // page.setMaxPurchasedPricePercentage(conversionUtility.toString(settlementMethodEntity.maxPurchasedPrice));
            // } else {
            // 一律手数料
            settlementRegistUpdateModel.setEqualsCommissionYen(
                            conversionUtility.toString(settlementMethodEntity.getEqualsCommission()));
            if (largeAmountDiscountPrice != null && largeAmountDiscountPrice.compareTo(BigDecimal.ZERO) > 0) {
                // 高額割引手数料
                settlementRegistUpdateModel.setLargeAmountDiscountCommissionYen(
                                conversionUtility.toString(settlementMethodEntity.getLargeAmountDiscountCommission()));
                // 高額割引下限金額
                settlementRegistUpdateModel.setLargeAmountDiscountPriceYen(
                                conversionUtility.toString(largeAmountDiscountPrice));
            }
            // 最大購入金額
            settlementRegistUpdateModel.setMaxPurchasedPriceYen(
                            conversionUtility.toString(settlementMethodEntity.getMaxPurchasedPrice()));
            // }
        }

        settlementRegistUpdateModel.setSettlementMethodDto(settlementMethodDto);
    }

    /**
     * 決済方法金額別手数料コンポーネント作成<br/>
     * @return 決済方法金額別手数料リスト
     */
    public List<SettlementRegistUpdateModelItem> makePriceCommissionComponent() {

        List<SettlementRegistUpdateModelItem> items = new ArrayList<>();
        for (int count = 0; count < 10; count++) {
            SettlementRegistUpdateModelItem item =
                            ApplicationContextUtility.getBean(SettlementRegistUpdateModelItem.class);
            items.add(item);
        }
        return items;

    }

    /**
     * 決済方法金額別手数料フラグ変更<br/>
     * @param settlementRegistUpdateModel 決済方法詳細設定ページ
     */
    public void changeComissonFlag(SettlementRegistUpdateModel settlementRegistUpdateModel) {
        if (settlementRegistUpdateModel.getSettlementMethodCommissionType() == null) {
            settlementRegistUpdateModel.setSettlementMethodPriceCommissionFlag(null);
            return;
        }
        HTypeSettlementMethodCommissionType commissionType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      settlementRegistUpdateModel.getSettlementMethodCommissionType()
                                                     );
        settlementRegistUpdateModel.setCommissionType(commissionType);

        // 決済方法金額別手数料フラグ設定
        // if (HTypeSettlementMethodCommissionType.EACH_AMOUNT_PERCENTAGE ==
        // commissionType) {
        // page.setSettlementMethodPriceCommissionFlag(HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT);
        // if (page.getPriceCommissionPercentage() != null) {
        // return;
        // }
        // page.setPriceCommissionPercentage(makePriceCommissionComponent());
        // } else
        if (HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN == commissionType) {
            settlementRegistUpdateModel.setSettlementMethodPriceCommissionFlag(
                            HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT);
            if (settlementRegistUpdateModel.getPriceCommissionYen() != null) {
                return;
            }
            settlementRegistUpdateModel.setPriceCommissionYen(makePriceCommissionComponent());
        } else {
            settlementRegistUpdateModel.setSettlementMethodPriceCommissionFlag(
                            HTypeSettlementMethodPriceCommissionFlag.FLAT);
        }
    }

    /**
     * 選択された配送方法名称を設定
     * @param settlementRegistUpdateModel 決済方法詳細設定ページ
     */
    public void setDeliveryMethodName(SettlementRegistUpdateModel settlementRegistUpdateModel) {
        Integer deliveryMethodSeq = settlementRegistUpdateModel.getDeliveryMethodSeq();
        if (deliveryMethodSeq == null) {
            settlementRegistUpdateModel.setDeliveryMethodName(null);
            return;
        }

        Map<String, String> deliveryMethodSeqItems = settlementRegistUpdateModel.getDeliveryMethodSeqItems();
        if (deliveryMethodSeqItems.containsKey(deliveryMethodSeq.toString())) {
            settlementRegistUpdateModel.setDeliveryMethodName(deliveryMethodSeqItems.get(deliveryMethodSeq.toString()));
        }
    }

    /**
     * コピー用に半角変換した値を返す
     *
     * @param valuePC PC値
     * @return 携帯値
     */
    protected String convertToMB(String valuePC) {

        String ret = null;
        if (valuePC != null) {
            // 全角、半角の変換Helper取得
            ZenHanConversionUtility zenHanConversionUtility =
                            ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

            ret = zenHanConversionUtility.toHankaku(valuePC, new Character[] {'＆', '＜', '＞', '”', '’', '￥'});
        }

        return ret;
    }

    /**
     * 決済方法リクエスト作成 <br/>
     *
     * @param settlementRegistUpdateModel 決済方法詳細設定：確認ページ
     * @return 決済方法DTO
     */
    public PaymentMethodCheckRequest pagePaymentMethodCheckRequest(SettlementRegistUpdateModel settlementRegistUpdateModel) {

        changeComissonFlag(settlementRegistUpdateModel);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        settlementRegistUpdateModel.getSettlementMethodPriceCommissionFlag();

        PaymentMethodCheckRequest paymentMethodCheckRequest = new PaymentMethodCheckRequest();

        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest();

        // 決済方法SEQ
        paymentMethodCheckRequest.setSettlementMethodSeq(settlementRegistUpdateModel.getSettlementMethodSeq());

        // 決済方法名
        paymentMethodRequest.setSettlementMethodName(settlementRegistUpdateModel.getSettlementMethodName());
        // 決済方法表示名PC
        paymentMethodRequest.setSettlementMethodDisplayNamePC(
                        settlementRegistUpdateModel.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        paymentMethodRequest.setOpenStatusPC(settlementRegistUpdateModel.getOpenStatusPC());
        // 決済方法説明文PC
        paymentMethodRequest.setSettlementNotePC(settlementRegistUpdateModel.getSettlementNotePC());
        // 決済方法種別
        paymentMethodRequest.setSettlementMethodType(settlementRegistUpdateModel.getSettlementMethodType());
        // 決済方法手数料種別
        paymentMethodRequest.setSettlementMethodCommissionType(
                        settlementRegistUpdateModel.getSettlementMethodCommissionType());
        // 請求種別
        paymentMethodRequest.setBillType(settlementRegistUpdateModel.getBillType());
        // 配送方法SEQ
        paymentMethodRequest.setDeliveryMethodSeq(settlementRegistUpdateModel.getDeliveryMethodSeq());
        // 一律手数料
        paymentMethodRequest.setEqualsCommission(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        paymentMethodRequest.setSettlementMethodPriceCommissionFlag(priceCommissionFlag.getValue());
        // 高額割引下限金額
        paymentMethodRequest.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        paymentMethodRequest.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getLargeAmountDiscountCommission()));
        // 表示順
        if (!ObjectUtils.isEmpty(settlementRegistUpdateModel.getOrderDisplay())) {
            paymentMethodRequest.setOrderDisplay(settlementRegistUpdateModel.getOrderDisplay());
        }
        // 最大購入金額
        paymentMethodRequest.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getMaxPurchasedPrice()));
        // 最小購入金額
        paymentMethodRequest.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        paymentMethodRequest.setSettlementMailRequired(settlementRegistUpdateModel.getSettlementMailRequired());
        // クレジット用設定値
        paymentMethodRequest.setEnable3dSecure(false);
        paymentMethodRequest.setEnableCardNoHolding(true);
        paymentMethodRequest.setEnableInstallment(false);
        paymentMethodRequest.setEnableRevolving(false);
        // 決済方法種別がクレジットの場合は画面の入力値で再設定する
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodRequest.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnable3dSecure()) {
                paymentMethodRequest.setEnable3dSecure(true);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnableCardNoHolding()) {
                paymentMethodRequest.setEnableCardNoHolding(true);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (settlementRegistUpdateModel.isEnableInstallment()) {
                paymentMethodRequest.setEnableInstallment(true);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnableRevolving()) {
                paymentMethodRequest.setEnableRevolving(true);
            }
        }

        paymentMethodCheckRequest.setPaymentMethodRequest(paymentMethodRequest);

        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return paymentMethodCheckRequest;
        }

        // 金額別手数料設定
        List<SettlementRegistUpdateModelItem> priceCommissonItemList =
                        settlementRegistUpdateModel.getPriceCommissionItemList();
        List<PaymentRegistUpdateModelItemRequest> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        for (SettlementRegistUpdateModelItem item : priceCommissonItemList) {
            PaymentRegistUpdateModelItemRequest paymentRegistUpdateModelItemRequest =
                            new PaymentRegistUpdateModelItemRequest();
            BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
            BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
            if (maxPrice != null && commission != null) {
                // 上限金額
                paymentRegistUpdateModelItemRequest.setMaxPrice(item.getMaxPrice());
                // 手数料
                paymentRegistUpdateModelItemRequest.setCommission(item.getCommission());
                priceCommissonList.add(paymentRegistUpdateModelItemRequest);

                if (maxPurchasedPrice.compareTo(
                                conversionUtility.toBigDecimal(paymentRegistUpdateModelItemRequest.getMaxPrice()))
                    < 0) {
                    maxPurchasedPrice =
                                    conversionUtility.toBigDecimal(paymentRegistUpdateModelItemRequest.getMaxPrice());
                }
            }
        }
        paymentMethodRequest.setPriceCommissionItemList(priceCommissonList);
        paymentMethodRequest.setMaxPurchasedPrice(maxPurchasedPrice);

        return paymentMethodCheckRequest;
    }

    /**
     * 決済方法リクエスト作成 <br/>
     *
     * @param settlementRegistUpdateModel 決済方法詳細設定：確認ページ
     * @return SettlementMethodDto 決済方法DTO
     */
    public SettlementMethodDto pageToSettlementMethodDto(SettlementRegistUpdateModel settlementRegistUpdateModel) {

        changeComissonFlag(settlementRegistUpdateModel);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        settlementRegistUpdateModel.getSettlementMethodPriceCommissionFlag();

        SettlementMethodDto settlementMethodDto = ApplicationContextUtility.getBean(SettlementMethodDto.class);

        SettlementMethodEntity settlementMethod = ApplicationContextUtility.getBean(SettlementMethodEntity.class);

        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(settlementRegistUpdateModel.getSettlementMethodSeq());

        // 決済方法名
        settlementMethod.setSettlementMethodName(settlementRegistUpdateModel.getSettlementMethodName());
        // 決済方法表示名PC
        settlementMethod.setSettlementMethodDisplayNamePC(
                        settlementRegistUpdateModel.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        settlementMethod.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                       settlementRegistUpdateModel.getOpenStatusPC()
                                                                      ));
        // 決済方法説明文PC
        settlementMethod.setSettlementNotePC(settlementRegistUpdateModel.getSettlementNotePC());
        // 決済方法種別
        settlementMethod.setSettlementMethodType(EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                               settlementRegistUpdateModel.getSettlementMethodType()
                                                                              ));
        // 決済方法手数料種別
        settlementMethod.setSettlementMethodCommissionType(
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      settlementRegistUpdateModel.getSettlementMethodCommissionType()
                                                     ));
        // 請求種別
        settlementMethod.setBillType(
                        EnumTypeUtil.getEnumFromValue(HTypeBillType.class, settlementRegistUpdateModel.getBillType()));
        // 配送方法SEQ
        settlementMethod.setDeliveryMethodSeq(settlementRegistUpdateModel.getDeliveryMethodSeq());
        // 一律手数料
        settlementMethod.setEqualsCommission(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        settlementMethod.setSettlementMethodPriceCommissionFlag(priceCommissionFlag);
        // 高額割引下限金額
        settlementMethod.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        settlementMethod.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getLargeAmountDiscountCommission()));
        // 最大購入金額
        settlementMethod.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getMaxPurchasedPrice()));
        // 最小購入金額
        settlementMethod.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(settlementRegistUpdateModel.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        settlementMethod.setSettlementMailRequired(EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                                 settlementRegistUpdateModel.getSettlementMailRequired()
                                                                                ));
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        settlementMethod.setEnableInstallment(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableRevolving(HTypeEffectiveFlag.INVALID);
        // 決済方法種別がクレジットの場合は画面の入力値を設定する
        if (HTypeSettlementMethodType.CREDIT.equals(settlementMethod.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnable3dSecure()) {
                settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnableCardNoHolding()) {
                settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (settlementRegistUpdateModel.isEnableInstallment()) {
                settlementMethod.setEnableInstallment(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (settlementRegistUpdateModel.isEnableRevolving()) {
                settlementMethod.setEnableRevolving(HTypeEffectiveFlag.VALID);
            }
        }

        settlementMethodDto.setSettlementMethodEntity(settlementMethod);

        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return settlementMethodDto;
        }

        // 金額別手数料設定
        List<SettlementRegistUpdateModelItem> priceCommissonItemList =
                        settlementRegistUpdateModel.getPriceCommissionItemList();
        List<SettlementMethodPriceCommissionEntity> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        for (SettlementRegistUpdateModelItem item : priceCommissonItemList) {
            SettlementMethodPriceCommissionEntity priceCommission =
                            ApplicationContextUtility.getBean(SettlementMethodPriceCommissionEntity.class);
            BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
            BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
            if (maxPrice != null && commission != null) {
                // 上限金額
                priceCommission.setMaxPrice(conversionUtility.toBigDecimal(item.getMaxPrice()));
                // 手数料
                priceCommission.setCommission(conversionUtility.toBigDecimal(item.getCommission()));
                priceCommissonList.add(priceCommission);
                if (maxPurchasedPrice.compareTo(priceCommission.getMaxPrice()) < 0) {
                    maxPurchasedPrice = priceCommission.getMaxPrice();
                }
            }
        }
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(priceCommissonList);
        settlementMethod.setMaxPurchasedPrice(maxPurchasedPrice);

        return settlementMethodDto;
    }

    /**
     * 決済方法リクエスト作成<br/>
     *
     * @param settlementMethodDto 決済方法Dto
     * @return PaymentMethodRegistRequest お支払い方法登録リクエスト
     */
    public PaymentMethodRegistRequest pageToPaymentMethodRegistRequest(SettlementRegistUpdateModel settlementMethodDto) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        settlementMethodDto.getSettlementMethodPriceCommissionFlag();

        PaymentMethodRegistRequest paymentMethodRegistRequest = new PaymentMethodRegistRequest();

        SettlementMethodEntity settlementMethod = new SettlementMethodEntity();
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest();
        paymentMethodRegistRequest.setPaymentMethodRequest(paymentMethodRequest);
        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(settlementMethodDto.getSettlementMethodSeq());

        // 決済方法名
        Objects.requireNonNull(paymentMethodRegistRequest.getPaymentMethodRequest())
               .setSettlementMethodName(settlementMethodDto.getSettlementMethodName());
        // 決済方法表示名PC
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setSettlementMethodDisplayNamePC(settlementMethodDto.getSettlementMethodName());
        // 公開状態PC
        paymentMethodRegistRequest.getPaymentMethodRequest().setOpenStatusPC(settlementMethodDto.getOpenStatusPC());
        // 決済方法説明文PC
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setSettlementNotePC(settlementMethodDto.getSettlementNotePC());
        // 決済方法種別
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setSettlementMethodType(settlementMethodDto.getSettlementMethodType());
        // 決済方法手数料種別
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setSettlementMethodCommissionType(
                                                  settlementMethodDto.getSettlementMethodCommissionType());
        // 請求種別
        paymentMethodRegistRequest.getPaymentMethodRequest().setBillType(settlementMethodDto.getBillType());
        // 配送方法SEQ
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setDeliveryMethodSeq(settlementMethodDto.getDeliveryMethodSeq());
        // 一律手数料
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setEqualsCommission(conversionUtility.toBigDecimal(
                                                  settlementMethodDto.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setSettlementMethodPriceCommissionFlag(priceCommissionFlag.getValue());
        // 高額割引下限金額
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setLargeAmountDiscountPrice(conversionUtility.toBigDecimal(
                                                  settlementMethodDto.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setLargeAmountDiscountCommission(conversionUtility.toBigDecimal(
                                                  settlementMethodDto.getLargeAmountDiscountCommission()));
        // 最大購入金額
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setMaxPurchasedPrice(conversionUtility.toBigDecimal(
                                                  settlementMethodDto.getMaxPurchasedPrice()));
        // 最小購入金額
        paymentMethodRegistRequest.getPaymentMethodRequest()
                                  .setMinPurchasedPrice(conversionUtility.toBigDecimal(
                                                  settlementMethodDto.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        Objects.requireNonNull(paymentMethodRegistRequest.getPaymentMethodRequest())
               .setSettlementMailRequired(settlementMethodDto.getSettlementMailRequired());
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        paymentMethodRegistRequest.getPaymentMethodRequest().setEnable3dSecure(false);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        paymentMethodRegistRequest.getPaymentMethodRequest().setEnableCardNoHolding(true);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        paymentMethodRegistRequest.getPaymentMethodRequest().setEnableInstallment(false);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        paymentMethodRegistRequest.getPaymentMethodRequest().setEnableRevolving(false);
        // 決済方法種別がクレジットの場合は画面の入力値を設定する
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(settlementMethodDto.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (settlementMethodDto.isEnable3dSecure()) {
                paymentMethodRegistRequest.getPaymentMethodRequest().setEnable3dSecure(true);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (settlementMethodDto.isEnableCardNoHolding()) {
                paymentMethodRegistRequest.getPaymentMethodRequest().setEnableCardNoHolding(true);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (settlementMethodDto.isEnableInstallment()) {
                paymentMethodRegistRequest.getPaymentMethodRequest().setEnableInstallment(true);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (settlementMethodDto.isEnableRevolving()) {
                paymentMethodRegistRequest.getPaymentMethodRequest().setEnableRevolving(true);
            }
        }

        settlementMethodDto.setSettlementMethodEntity(settlementMethod);

        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return paymentMethodRegistRequest;
        }

        // 金額別手数料設定
        List<SettlementRegistUpdateModelItem> priceCommissonItemList = settlementMethodDto.getPriceCommissionItemList();
        List<PaymentRegistUpdateModelItemRequest> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        for (SettlementRegistUpdateModelItem item : priceCommissonItemList) {
            PaymentRegistUpdateModelItemRequest priceCommission = new PaymentRegistUpdateModelItemRequest();
            BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
            BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
            if (maxPrice != null && commission != null) {
                // 上限金額
                priceCommission.setMaxPrice(item.getMaxPrice());
                // 手数料
                priceCommission.setCommission(conversionUtility.toString(item.getCommission()));
                priceCommissonList.add(priceCommission);
                if (maxPurchasedPrice.compareTo(conversionUtility.toBigDecimal(priceCommission.getMaxPrice())) < 0) {
                    maxPurchasedPrice = conversionUtility.toBigDecimal(priceCommission.getMaxPrice());
                }
            }
        }
        settlementMethod.setMaxPurchasedPrice(maxPurchasedPrice);
        paymentMethodRegistRequest.getPaymentMethodRequest().setPriceCommissionItemList(priceCommissonList);
        return paymentMethodRegistRequest;
    }

    /**
     * 決済方法リクエスト作成<br/>
     *
     * @param settlementMethodDto 決済方法Dto
     * @return 決済方法DTO
     */
    public PaymentMethodUpdateRequest toPaymentMethodUpdateRequest(SettlementRegistUpdateModel settlementMethodDto) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        settlementMethodDto.getSettlementMethodPriceCommissionFlag();

        PaymentMethodUpdateRequest paymentMethodUpdateRequest = new PaymentMethodUpdateRequest();

        SettlementMethodEntity settlementMethod = new SettlementMethodEntity();

        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(settlementMethodDto.getSettlementMethodSeq());
        paymentMethodUpdateRequest.setPaymentMethodRequest(new PaymentMethodRequest());
        // 決済方法名
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMethodName(settlementMethodDto.getSettlementMethodName());
        // 決済方法表示名PC
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMethodDisplayNamePC(settlementMethodDto.getSettlementMethodName());
        // 公開状態PC
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setOpenStatusPC(settlementMethodDto.getOpenStatusPC());
        // 決済方法説明文PC
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementNotePC(settlementMethodDto.getSettlementNotePC());
        // 決済方法種別
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMethodType(settlementMethodDto.getSettlementMethodType());
        // 決済方法手数料種別
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMethodCommissionType(settlementMethodDto.getSettlementMethodCommissionType());
        // 請求種別
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setBillType(settlementMethodDto.getBillType());
        // 配送方法SEQ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setDeliveryMethodSeq(settlementMethodDto.getDeliveryMethodSeq());
        // 一律手数料
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setEqualsCommission(conversionUtility.toBigDecimal(settlementMethodDto.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMethodPriceCommissionFlag(priceCommissionFlag.getValue());
        // 高額割引下限金額
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setLargeAmountDiscountPrice(
                               conversionUtility.toBigDecimal(settlementMethodDto.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setLargeAmountDiscountCommission(
                               conversionUtility.toBigDecimal(settlementMethodDto.getLargeAmountDiscountCommission()));
        // 表示順
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setOrderDisplay(settlementMethodDto.getOrderDisplay());
        // 最大購入金額
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setMaxPurchasedPrice(conversionUtility.toBigDecimal(settlementMethodDto.getMaxPurchasedPrice()));
        // 最小購入金額
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setMinPurchasedPrice(conversionUtility.toBigDecimal(settlementMethodDto.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setSettlementMailRequired(settlementMethodDto.getSettlementMailRequired());
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnable3dSecure(false);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnableCardNoHolding(true);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnableInstallment(false);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnableRevolving(false);
        // 決済方法種別がクレジットの場合は画面の入力値を設定する
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(settlementMethodDto.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (settlementMethodDto.isEnable3dSecure()) {
                Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnable3dSecure(true);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ

            if (settlementMethodDto.isEnableCardNoHolding()) {
                Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
                       .setEnableCardNoHolding(true);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (settlementMethodDto.isEnableInstallment()) {
                Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnableInstallment(true);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (settlementMethodDto.isEnableRevolving()) {
                Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest()).setEnableRevolving(true);
            }
        }

        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return paymentMethodUpdateRequest;
        }

        // 金額別手数料設定
        List<SettlementRegistUpdateModelItem> priceCommissonItemList = settlementMethodDto.getPriceCommissionItemList();
        List<PaymentRegistUpdateModelItemRequest> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        if (priceCommissonItemList != null) {
            for (SettlementRegistUpdateModelItem item : priceCommissonItemList) {
                PaymentRegistUpdateModelItemRequest priceCommission = new PaymentRegistUpdateModelItemRequest();
                BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
                BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
                if (maxPrice != null && commission != null) {
                    // 上限金額
                    priceCommission.setMaxPrice(conversionUtility.toString(item.getMaxPrice()));
                    // 手数料
                    priceCommission.setCommission(conversionUtility.toString(item.getCommission()));
                    priceCommissonList.add(priceCommission);
                    if (maxPurchasedPrice.compareTo(conversionUtility.toBigDecimal(priceCommission.getMaxPrice()))
                        < 0) {
                        maxPurchasedPrice = conversionUtility.toBigDecimal(priceCommission.getMaxPrice());
                    }
                }
            }
        }
        paymentMethodUpdateRequest.getPaymentMethodRequest().setPriceCommissionItemList(priceCommissonList);
        Objects.requireNonNull(paymentMethodUpdateRequest.getPaymentMethodRequest())
               .setMaxPurchasedPrice(maxPurchasedPrice);

        return paymentMethodUpdateRequest;
    }

    /**
     * 決済方法リクエスト作成<br/>
     *
     * @param paymentMethodResponse お支払い方法の対応
     * @return 決済方法DTO
     */
    public SettlementMethodDto toSettlementMethodDto(PaymentMethodResponse paymentMethodResponse) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 決済方法金額別手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag priceCommissionFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                      paymentMethodResponse.getSettlementMethodPriceCommissionFlag()
                                                     );

        SettlementMethodDto settlementMethodDto = ApplicationContextUtility.getBean(SettlementMethodDto.class);

        SettlementMethodEntity settlementMethod = ApplicationContextUtility.getBean(SettlementMethodEntity.class);

        // 決済方法SEQ
        settlementMethod.setSettlementMethodSeq(paymentMethodResponse.getSettlementMethodSeq());

        // 決済方法名
        settlementMethod.setSettlementMethodName(paymentMethodResponse.getSettlementMethodName());
        // 決済方法表示名PC
        settlementMethod.setSettlementMethodDisplayNamePC(paymentMethodResponse.getSettlementMethodDisplayNamePC());
        // 公開状態PC
        settlementMethod.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                       paymentMethodResponse.getOpenStatusPC()
                                                                      ));
        // 決済方法説明文PC
        settlementMethod.setSettlementNotePC(paymentMethodResponse.getSettlementNotePC());
        // 決済方法種別
        settlementMethod.setSettlementMethodType(EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                               paymentMethodResponse.getSettlementMethodType()
                                                                              ));
        // 決済方法手数料種別
        settlementMethod.setSettlementMethodCommissionType(
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                      paymentMethodResponse.getSettlementMethodCommissionType()
                                                     ));
        // 請求種別
        settlementMethod.setBillType(
                        EnumTypeUtil.getEnumFromValue(HTypeBillType.class, paymentMethodResponse.getBillType()));
        // 配送方法SEQ
        settlementMethod.setDeliveryMethodSeq(paymentMethodResponse.getDeliveryMethodSeq());
        // 一律手数料
        settlementMethod.setEqualsCommission(
                        conversionUtility.toBigDecimal(paymentMethodResponse.getEqualsCommission()));
        // 決済方法金額別手数料フラグ
        settlementMethod.setSettlementMethodPriceCommissionFlag(priceCommissionFlag);
        // 高額割引下限金額
        settlementMethod.setLargeAmountDiscountPrice(
                        conversionUtility.toBigDecimal(paymentMethodResponse.getLargeAmountDiscountPrice()));
        // 高額割引手数料
        settlementMethod.setLargeAmountDiscountCommission(
                        conversionUtility.toBigDecimal(paymentMethodResponse.getLargeAmountDiscountCommission()));
        // 表示順
        if (!ObjectUtils.isEmpty(paymentMethodResponse.getOrderDisplay())) {
            settlementMethod.setOrderDisplay(paymentMethodResponse.getOrderDisplay());
        }
        // 最大購入金額
        settlementMethod.setMaxPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodResponse.getMaxPurchasedPrice()));
        // 最小購入金額
        settlementMethod.setMinPurchasedPrice(
                        conversionUtility.toBigDecimal(paymentMethodResponse.getMinPurchasedPrice()));
        // 決済関連メール要否フラグ
        settlementMethod.setSettlementMailRequired(EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                                 paymentMethodResponse.getSettlementMailRequired()
                                                                                ));
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        settlementMethod.setEnableInstallment(HTypeEffectiveFlag.INVALID);
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        settlementMethod.setEnableRevolving(HTypeEffectiveFlag.INVALID);
        // 決済方法種別がクレジットの場合は画面の入力値を設定する
        if (HTypeSettlementMethodType.CREDIT.equals(settlementMethod.getSettlementMethodType())) {
            // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
            if (Objects.equals(paymentMethodResponse.getEnable3dSecure(), "1")) {
                settlementMethod.setEnable3dSecure(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
            if (Objects.equals(paymentMethodResponse.getEnableCardNoHolding(), "1")) {
                settlementMethod.setEnableCardNoHolding(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸ
            if (Objects.equals(paymentMethodResponse.getEnableInstallment(), "1")) {
                settlementMethod.setEnableInstallment(HTypeEffectiveFlag.VALID);
            }
            // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
            if (Objects.equals(paymentMethodResponse.getEnableRevolving(), "1")) {
                settlementMethod.setEnableRevolving(HTypeEffectiveFlag.VALID);
            }
        }

        settlementMethodDto.setSettlementMethodEntity(settlementMethod);

        settlementMethodDto.setDeliveryMethodName(paymentMethodResponse.getDeliveryMethodName());

        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT != priceCommissionFlag) {
            return settlementMethodDto;
        }

        // 金額別手数料設定
        List<PaymentMethodPriceCommissionResponse> priceCommissonItemList =
                        paymentMethodResponse.getPaymentMethodPriceCommissionList();
        List<SettlementMethodPriceCommissionEntity> priceCommissonList = new ArrayList<>();
        BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
        for (PaymentMethodPriceCommissionResponse item : priceCommissonItemList) {
            SettlementMethodPriceCommissionEntity priceCommission =
                            ApplicationContextUtility.getBean(SettlementMethodPriceCommissionEntity.class);
            BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
            BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
            if (maxPrice != null && commission != null) {
                // 上限金額
                priceCommission.setMaxPrice(conversionUtility.toBigDecimal(item.getMaxPrice()));
                // 手数料
                priceCommission.setCommission(conversionUtility.toBigDecimal(item.getCommission()));
                priceCommissonList.add(priceCommission);
                if (maxPurchasedPrice.compareTo(priceCommission.getMaxPrice()) < 0) {
                    maxPurchasedPrice = priceCommission.getMaxPrice();
                }
            }
        }
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(priceCommissonList);
        settlementMethod.setMaxPurchasedPrice(maxPurchasedPrice);

        return settlementMethodDto;
    }

    /**
     * 決済種別リストを再作成する。<br />
     * <pre>
     * 標準PKGの決済種別のリストから、全額割引決済を除去したリストを作成。
     * </pre>
     *
     * @param settlementRegistUpdateModel 決済方法詳細設定画面
     */
    public void toSettlementMethodTypeItems(SettlementRegistUpdateModel settlementRegistUpdateModel) {
        // 決済種別リストを取得
        Map<String, String> settlementMethodTypeList = settlementRegistUpdateModel.getSettlementMethodTypeItems();

        if (!MapUtils.isEmpty(settlementMethodTypeList) && settlementMethodTypeList.containsValue(
                        HTypeSettlementMethodType.DISCOUNT.getValue())) {
            // 決済種別リストから「全額割引」を削除する
            settlementMethodTypeList.remove(HTypeSettlementMethodType.DISCOUNT.getValue());
        }

    }

    /**
     * ページへの変換処理
     *
     * @param settlementRegistUpdateModel 決済方法登録・更新確認画面ページ
     */
    public void toPageForLoad(SettlementRegistUpdateModel settlementRegistUpdateModel) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 表示用にクレジット情報を設定する
        // ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
        if (settlementRegistUpdateModel.isEnableCardNoHolding()) {
            settlementRegistUpdateModel.setEnableCardNoHoldingForDisp(HTypeEffectiveFlag.VALID.getLabel());
        } else {
            settlementRegistUpdateModel.setEnableCardNoHoldingForDisp(HTypeEffectiveFlag.INVALID.getLabel());
        }
        // ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
        if (settlementRegistUpdateModel.isEnable3dSecure()) {
            settlementRegistUpdateModel.setEnable3dSecureForDisp(HTypeEffectiveFlag.VALID.getLabel());
        } else {
            settlementRegistUpdateModel.setEnable3dSecureForDisp(HTypeEffectiveFlag.INVALID.getLabel());
        }
        // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
        if (settlementRegistUpdateModel.isEnableInstallment()) {
            settlementRegistUpdateModel.setEnableInstallmentForDisp(HTypeEffectiveFlag.VALID.getLabel());
        } else {
            settlementRegistUpdateModel.setEnableInstallmentForDisp(HTypeEffectiveFlag.INVALID.getLabel());
        }
        // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
        if (settlementRegistUpdateModel.isEnableRevolving()) {
            settlementRegistUpdateModel.setEnableRevolvingForDisp(HTypeEffectiveFlag.VALID.getLabel());
        } else {
            settlementRegistUpdateModel.setEnableRevolvingForDisp(HTypeEffectiveFlag.INVALID.getLabel());
        }

        // 画面表示用に利用可能最大金額をセットする
        List<SettlementRegistUpdateModelItem> priceCommissonItemList =
                        settlementRegistUpdateModel.getPriceCommissionItemList();
        if (priceCommissonItemList != null) {
            BigDecimal maxPurchasedPrice = BigDecimal.ZERO;
            for (SettlementRegistUpdateModelItem item : priceCommissonItemList) {
                BigDecimal maxPrice = conversionUtility.toBigDecimal(item.getMaxPrice());
                BigDecimal commission = conversionUtility.toBigDecimal(item.getCommission());
                if (maxPrice != null && commission != null) {
                    if (maxPurchasedPrice.compareTo(maxPrice) < 0) {
                        maxPurchasedPrice = maxPrice;
                    }
                }
            }
            settlementRegistUpdateModel.setMaxPurchasedPrice(maxPurchasedPrice.toString());
        }

        if (settlementRegistUpdateModel.getConfigMode() == 2) {
            // 修正箇所の検出
            SettlementMethodEntity original =
                            CopyUtil.deepCopy(settlementRegistUpdateModel.getSettlementMethodEntity());
            SettlementMethodEntity modified = CopyUtil.deepCopy(
                            settlementRegistUpdateModel.getSettlementMethodDto().getSettlementMethodEntity());

            // テキストエリアの項目の改行コードを統一
            if (original.getSettlementNotePC() != null) {
                original.setSettlementNotePC(original.getSettlementNotePC().replaceAll("\r\n", "\n"));
            }

            if (modified.getSettlementNotePC() != null) {
                modified.setSettlementNotePC(modified.getSettlementNotePC().replaceAll("\r\n", "\n"));
            }

            settlementRegistUpdateModel.setModifiedList(DiffUtil.diff(original, modified));

            // 金額別手数料の差分取得
            if (!settlementRegistUpdateModel.isFlat()) {
                List<List<String>> tmpDiffList = new ArrayList<>();
                int index = 0;

                // 変更後の金額別手数料
                for (SettlementMethodPriceCommissionEntity modifiedPriceCommissionYen : settlementRegistUpdateModel.getSettlementMethodDto()
                                                                                                                   .getSettlementMethodPriceCommissionEntityList()) {

                    if (CollectionUtils.isEmpty(
                                    settlementRegistUpdateModel.getSettlementMethodPriceCommissionEntityList())) {
                        // 変更前が金額別手数料未設定の場合
                        tmpDiffList.add(DiffUtil.diff(
                                        new SettlementMethodPriceCommissionEntity(), modifiedPriceCommissionYen));
                    } else {
                        // 変更前の金額別手数料
                        if (settlementRegistUpdateModel.getSettlementMethodPriceCommissionEntityList().size() > index) {
                            // 金額別手数料を更新した場合
                            SettlementMethodPriceCommissionEntity originalPriceCommissionYen =
                                            settlementRegistUpdateModel.getSettlementMethodPriceCommissionEntityList()
                                                                       .get(index);
                            tmpDiffList.add(DiffUtil.diff(originalPriceCommissionYen, modifiedPriceCommissionYen));
                        } else {
                            // 金額別手数料を新たに追加した場合
                            tmpDiffList.add(DiffUtil.diff(
                                            new SettlementMethodPriceCommissionEntity(), modifiedPriceCommissionYen));
                        }
                    }
                    index++;
                }
                settlementRegistUpdateModel.setModifiedPriceCommissionYenList(tmpDiffList);
            }
        }
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param settlementRegistUpdateModel
     */
    public void setDefaultValueForLoad(SettlementRegistUpdateModel settlementRegistUpdateModel) {
        // 公開状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(settlementRegistUpdateModel.getOpenStatusPC())) {
            settlementRegistUpdateModel.setOpenStatusPC(HTypeOpenDeleteStatus.NO_OPEN.getValue());
        }
    }

}