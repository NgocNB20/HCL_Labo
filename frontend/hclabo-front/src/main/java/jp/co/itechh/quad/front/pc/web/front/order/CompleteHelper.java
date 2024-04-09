/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfoShop;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.util.seasar.TimestampConversionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipModel;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.SnsUtility;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 注文完了画面 Helper
 *
 * @author Pham Quang Dieu
 */
@Component
public class CompleteHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 預金種別ラベル */
    private static final String ACCOUNTTYPE_LABEL = "普通預金";

    /** コンストラクタ */
    @Autowired
    public CompleteHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面表示・再表示<br/>
     *
     * @param confirmModel              モデル
     * @param orderReceivedResponse     受注レスポンス
     * @param orderSlipResponse         注文票レスポンス
     * @param productDetailListResponse 商品詳細レスポンス
     * @param salesSlipResponse         販売伝票レスポンス
     */
    public void toPageForLoad(ConfirmModel confirmModel,
                              OrderReceivedResponse orderReceivedResponse,
                              OrderSlipResponse orderSlipResponse,
                              ProductDetailListResponse productDetailListResponse,
                              SalesSlipResponse salesSlipResponse,
                              MulPayBillResponse mulPayBillResponse,
                              BillingSlipResponse billingSlipResponse,
                              boolean isLinkPayment) {

        if (!ObjectUtils.isEmpty(orderReceivedResponse)) {
            // 受注番号をセット
            confirmModel.setOrderCode(orderReceivedResponse.getOrderCode());
        }

        if (isLinkPayment && !ObjectUtils.isEmpty(billingSlipResponse.getPaymentLinkResponse())) {
            PaymentLinkResponse paymentLinkResponse = billingSlipResponse.getPaymentLinkResponse();

            //決済手段識別子
            confirmModel.setPayMethod(paymentLinkResponse.getPaymethod());
            //決済手段名
            confirmModel.setPayTypeName(paymentLinkResponse.getPayTypeName());
            //決済方法
            confirmModel.setPayType(paymentLinkResponse.getPayType());

            // マルチペイメント請求レスポンスが存在する場合
            if (!ObjectUtils.isEmpty(mulPayBillResponse)) {
                if ("36".equals(paymentLinkResponse.getPayType())) {
                    toBankTransferInfo(confirmModel, mulPayBillResponse);
                } else if ("4".equals(paymentLinkResponse.getPayType())) {
                    //ペイジーの場合
                    toPayEasyInfo(confirmModel, mulPayBillResponse, paymentLinkResponse);
                } else if ("3".equals(billingSlipResponse.getPaymentLinkResponse().getPayType())) {
                    toConveniInfo(confirmModel, mulPayBillResponse, salesSlipResponse);
                }
            }
        }

        // GA用＋画面表示用の商品詳細Dtoリストをセット
        confirmModel.setGoodsDetailDtoList(
                        toProductDetailList(productDetailListResponse, orderSlipResponse, salesSlipResponse));

        // GA用の販売情報をセット
        confirmModel.setSalesSlip(toSalesSlipModel(salesSlipResponse));

        // 決済方法がコンビニの場合、注文確認画面でインクルードするページのパスを設定
        setPaymentProcedureConfirmDisplaySrc(confirmModel, mulPayBillResponse);

        // 商品情報のSNS情報をセット
        setGoodsDetailForSns(confirmModel);

    }

    /**
     * 商品情報のSNS設定
     *
     * @param confirmModel モデル
     */
    private void setGoodsDetailForSns(ConfirmModel confirmModel) {

        List<GoodsDetailsDto> goodsDetailDtoList = confirmModel.getGoodsDetailDtoList();

        // 商品詳細を取得
        if (goodsDetailDtoList != null) {
            for (GoodsDetailsDto goodsDetailDto : goodsDetailDtoList) {
                if (goodsDetailDto.getSnsLinkFlag() == HTypeSnsLinkFlag.ON) {
                    // SNS連携情報の設定
                    setSnsInfo(confirmModel);
                    goodsDetailDto.setSnsLinkFlg(true);
                }
            }
        }
    }

    /**
     * SNS連携の設定<br/>
     *
     * @param confirmModel 注文確認Model
     */
    private void setSnsInfo(ConfirmModel confirmModel) {

        SnsUtility snsUtil = ApplicationContextUtility.getBean(SnsUtility.class);
        // システム設定値を取得し、各種SNS連携可能かチェック
        confirmModel.setUseFacebook(snsUtil.isUseFacebook());
        confirmModel.setUseTwitter(snsUtil.isUseTwitter());
        confirmModel.setUseLine(snsUtil.isUseLine());

        if (confirmModel.isUseTwitter()) {
            // Twitter用メンションを取得
            confirmModel.setTwitterVia(snsUtil.getTwitterVia());
        }
    }

    /**
     * 商品詳細Dtoリストに変換<br/>
     * GAと画面表示項目用のため、必要な項目にセット
     *
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @param orderSlipResponse         注文票レスポンス（注文時の商品マスタ情報を取得するため）
     * @param salesSlipResponse         販売伝票レスポンス（注文時の商品マスタの金額情報を取得するため）
     * @return 商品詳細Dtoリスト
     */
    private List<GoodsDetailsDto> toProductDetailList(ProductDetailListResponse productDetailListResponse,
                                                      OrderSlipResponse orderSlipResponse,
                                                      SalesSlipResponse salesSlipResponse) {

        if (ObjectUtils.isEmpty(productDetailListResponse) || CollectionUtils.isEmpty(
                        productDetailListResponse.getGoodsDetailsList()) || ObjectUtils.isEmpty(orderSlipResponse)
            || ObjectUtils.isEmpty(salesSlipResponse)) {
            return null;
        }

        List<GoodsDetailsDto> goodsDetailDtoList = new ArrayList<>();

        List<OrderSlipResponseItemList> orderItemList = orderSlipResponse.getItemList();

        if (CollectionUtil.isNotEmpty(orderItemList)) {
            for (OrderSlipResponseItemList orderSlipItem : orderItemList) {

                // 対象の商品マスタ情報の探索
                GoodsDetailsResponse goodsDetailsResponse = productDetailListResponse.getGoodsDetailsList()
                                                                                     .stream()
                                                                                     .filter(goodsDetails -> goodsDetails.getGoodsSeq()
                                                                                                             != null
                                                                                                             && goodsDetails.getGoodsSeq()
                                                                                                                            .toString()
                                                                                                                            .equals(orderSlipItem.getItemId()))
                                                                                     .findFirst()
                                                                                     .orElse(null);
                if (ObjectUtils.isEmpty(goodsDetailsResponse)) {
                    continue;
                }
                // 対象の販売伝票情報の探索
                ItemPriceSubTotal targetItemPrice = null;

                if (CollectionUtil.isNotEmpty(salesSlipResponse.getItemPriceSubTotalList())) {
                    targetItemPrice = salesSlipResponse.getItemPriceSubTotalList()
                                                       .stream()
                                                       .filter(itemPrice -> StringUtils.isNotEmpty(
                                                                       itemPrice.getItemId()) && itemPrice.getItemId()
                                                                                                          .equals(orderSlipItem.getItemId()))
                                                       .findFirst()
                                                       .orElse(null);
                }
                if (ObjectUtils.isEmpty(targetItemPrice)) {
                    continue;
                }

                // 対象の商品画像を探索
                List<String> goodsImageItems = new ArrayList<>();
                if (!CollectionUtils.isEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                    for (GoodsGroupImageResponse goodsGroupImageResponse : goodsDetailsResponse.getGoodsGroupImageList()) {
                        goodsImageItems.add(goodsGroupImageResponse.getImageFileName());
                    }
                }

                // 戻り値用 商品詳細Dto
                GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

                // 商品マスタ情報設定
                goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
                goodsDetailsDto.setGoodsCode(goodsDetailsResponse.getGoodsCode());
                goodsDetailsDto.setGoodsImageItems(goodsImageItems);
                if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                    goodsDetailsDto.setGoodsGroupImageEntityList(
                                    toGoodsGroupImageList(goodsDetailsResponse.getGoodsGroupImageList()));
                }
                if (CollectionUtil.isNotEmpty(goodsDetailsDto.getGoodsIconList())) {
                    goodsDetailsDto.setGoodsIconList(toGoodsGroupImage(goodsDetailsResponse.getGoodsIconList()));
                }
                if (goodsDetailsResponse.getSnsLinkFlag() != null) {
                    goodsDetailsDto.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                                 goodsDetailsResponse.getSnsLinkFlag()
                                                                                ));
                }

                // 注文票情報設定
                goodsDetailsDto.setGoodsGroupName(orderSlipItem.getItemName());
                goodsDetailsDto.setUnitTitle1(orderSlipItem.getUnitTitle1());
                goodsDetailsDto.setUnitValue1(orderSlipItem.getUnitValue1());
                goodsDetailsDto.setUnitTitle2(orderSlipItem.getUnitTitle2());
                goodsDetailsDto.setUnitValue2(orderSlipItem.getUnitValue2());

                // 注文票から数量を設定
                goodsDetailsDto.setOrderGoodsCount(this.conversionUtility.toBigDecimal(orderSlipItem.getItemCount()));

                // 販売伝票の商品金額から設定
                goodsDetailsDto.setGoodsPrice(this.conversionUtility.toBigDecimal(targetItemPrice.getItemUnitPrice()));

                // 戻り値へ設定
                goodsDetailDtoList.add(goodsDetailsDto);
            }
        }

        return goodsDetailDtoList;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    private List<GoodsGroupImageEntity> toGoodsGroupImageList(List<GoodsGroupImageResponse> goodsGroupImageResponseList) {

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsGroupImageResponseList)) {
            goodsGroupImageResponseList.forEach(item -> {
                GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

                goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageEntity.setImageFileName(item.getImageFileName());
                goodsGroupImageEntity.setRegistTime(this.conversionUtility.toTimestamp(item.getRegistTime()));
                goodsGroupImageEntity.setUpdateTime(this.conversionUtility.toTimestamp(item.getUpdateTime()));

                goodsGroupImageEntityList.add(goodsGroupImageEntity);
            });
        }
        return goodsGroupImageEntityList;
    }

    /**
     * アイコン詳細レスポンスに変換
     *
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスクラスリスト
     * @return アイコン詳細DTOクラス
     */
    private List<GoodsInformationIconDetailsDto> toGoodsGroupImage(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList) {

        List<GoodsInformationIconDetailsDto> GoodsInformationIconDetailsDtoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(goodsInformationIconDetailsResponseList)) {
            goodsInformationIconDetailsResponseList.forEach(item -> {

                GoodsInformationIconDetailsDto goodsInformationIconDetailsDto =
                                ApplicationContextUtility.getBean(GoodsInformationIconDetailsDto.class);
                goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
                goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsInformationIconDetailsDto.setIconName(item.getIconName());
                goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
                goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());

                GoodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
            });
        }

        return GoodsInformationIconDetailsDtoList;
    }

    /**
     * 販売伝票モデルに変換
     *
     * @param salesSlipResponse 販売伝票レスポンス
     * @return SalesSlipModel
     */
    private SalesSlipModel toSalesSlipModel(SalesSlipResponse salesSlipResponse) {

        SalesSlipModel model = new SalesSlipModel();
        model.setBillingAmount(this.conversionUtility.toBigDecimal(salesSlipResponse.getBillingAmount()));
        model.setTaxPrice(this.conversionUtility.toBigDecimal(
                        (salesSlipResponse.getStandardTax() != null ? salesSlipResponse.getStandardTax() : 0) + (
                                        salesSlipResponse.getReducedTax() != null ?
                                                        salesSlipResponse.getReducedTax() :
                                                        0)));
        model.setCarriage(this.conversionUtility.toBigDecimal(
                        salesSlipResponse.getCarriage() != null ? salesSlipResponse.getCarriage() : 0));
        return model;
    }

    /**
     * Pay-Easyの暗号化決済番号をモデルに変換
     *
     * @param confirmModel        モデル
     * @param mulpayBillResponse  マルチペイメント請求レスポンス
     * @param paymentLinkResponse リンク決済URLレスポンス
     */
    private void toPayEasyInfo(ConfirmModel confirmModel,
                               MulPayBillResponse mulpayBillResponse,
                               PaymentLinkResponse paymentLinkResponse) {

        confirmModel.setEncryptReceiptNo(mulpayBillResponse.getEncryptReceiptNo());
        confirmModel.setBkCode(mulpayBillResponse.getBkCode());
        confirmModel.setCustId(mulpayBillResponse.getCustId());
        confirmModel.setConfNo(mulpayBillResponse.getConfNo());
        confirmModel.setPaymentTimeLimitDate(conversionUtility.toTimestamp(paymentLinkResponse.getLaterDateLimit()));
        confirmModel.setPaymentUrl(mulpayBillResponse.getPaymentURL());
        // ペイジーの場合は、金融機関選択画面遷移用のボタンを表示する。
        confirmModel.setCode(mulpayBillResponse.getEncryptReceiptNo());
    }

    /**
     * コンビニ請求情報をモデルに変換
     *
     * @param confirmModel       　モデル
     * @param mulpayBillResponse マルチペイメント請求レスポンス
     */
    private void toConveniInfo(ConfirmModel confirmModel,
                               MulPayBillResponse mulpayBillResponse,
                               SalesSlipResponse salesSlipResponse) {

        confirmModel.setConvenience(mulpayBillResponse.getConvenience());
        // 受付番号
        confirmModel.setReceiptNo(mulpayBillResponse.getReceiptNo());
        // 確認番号
        confirmModel.setConfNo(mulpayBillResponse.getConfNo());
        // 請求金額
        confirmModel.setBillPrice(conversionUtility.toBigDecimal(salesSlipResponse.getBillingAmount()));
        // 電話番号
        confirmModel.setTel(confirmModel.getBillingTel());
        // 受信支払期限
        String paymentTerm = mulpayBillResponse.getPaymentTerm();
        if (!StringUtils.isEmpty(paymentTerm)) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            paymentTerm = dateUtility.format(TimestampConversionUtil.toTimestamp(paymentTerm, "yyyyMMddhhmmss"),
                                             "yyyy/MM/dd"
                                            );
        }
        // 受信支払期限
        confirmModel.setPaymentTerm(paymentTerm);
        // 注文完了画面でインクルードするページのパスを設定
        setPaymentProcedureCompleteDisplaySrc(confirmModel, mulpayBillResponse);
    }

    /**
     * 銀行振込請求情報をモデルに変換
     *
     * @param confirmModel       　モデル
     * @param mulpayBillResponse マルチペイメント請求レスポンス
     */
    private void toBankTransferInfo(ConfirmModel confirmModel, MulPayBillResponse mulpayBillResponse) {

        //銀行名
        confirmModel.setBankName(mulpayBillResponse.getBankName() + "（" + mulpayBillResponse.getBankCode() + "）");
        //支店名
        confirmModel.setBranchName(mulpayBillResponse.getBranchName() + "（" + mulpayBillResponse.getBranchCode() + "）");
        //振込先口座種別
        confirmModel.setAccountType(this.getAccountTypeLabel(mulpayBillResponse.getAccountType()));
        //振込先口座番号
        confirmModel.setAccountNumber(mulpayBillResponse.getAccountNumber());
        //取引有効期限
        String exprireDate = mulpayBillResponse.getExprireDate();
        if (!StringUtils.isEmpty(exprireDate)) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            exprireDate = dateUtility.format(TimestampConversionUtil.toTimestamp(exprireDate, "yyyyMMdd"),
                                             "yyyy/MM/dd"
                                            );
        }
        confirmModel.setExprireDate(exprireDate);
    }

    /**
     * 決済方法がコンビニの場合、注文完了画面でインクルードするページのパスを設定
     *
     * @param confirmModel       モデル
     * @param mulPayBillResponse マルチペイメント請求レスポンス
     * @return コンビニ決済手順HTMLパス
     */
    private void setPaymentProcedureCompleteDisplaySrc(ConfirmModel confirmModel, MulPayBillResponse mulPayBillResponse) {

        // コンビニ決済でない場合
        if (mulPayBillResponse == null || StringUtils.isEmpty(mulPayBillResponse.getConvenience())) {
            return;
        }

        // 選択されたコンビニ用のインクルードページパスを取得する
        String key = confirmModel.PREFFIX_COMPLETE + mulPayBillResponse.getConvenience();
        String includePath = PropertiesUtil.getSystemPropertiesValue(key);

        if (StringUtils.isEmpty(includePath)) {
            confirmModel.setPaymentProcedureDisplay(false);
            return;
        }

        confirmModel.setPaymentProcedureDisplay(true);
        confirmModel.setPaymentProcedureCompleteDisplaySrc(includePath);
    }

    /**
     * 決済方法がコンビニの場合、注文確認画面でインクルードするページのパスを設定
     *
     * @param confirmModel 確認Model
     * @param mulPayBillResponse マルチペイメント請求レスポンス
     */
    public void setPaymentProcedureConfirmDisplaySrc(ConfirmModel confirmModel, MulPayBillResponse mulPayBillResponse) {

        // コンビニ決済でない場合
        if (mulPayBillResponse == null || StringUtils.isEmpty(mulPayBillResponse.getConvenience())) {
            return;
        }

        // 選択されたコンビニ用のインクルードページパスを取得する
        String key = confirmModel.PREFFIX_CONFIRM + mulPayBillResponse.getConvenience();
        String includePath = PropertiesUtil.getSystemPropertiesValue(key);

        if (StringUtils.isEmpty(includePath)) {
            confirmModel.setPaymentProcedureDisplay(false);
            return;
        }

        confirmModel.setPaymentProcedureDisplay(true);
        confirmModel.setPaymentProcedureConfirmDisplaySrc(includePath);
    }

    /**
     * 振込先口座種別ラベル取得
     *
     * @param accountType 振込先口座種別
     * @return 振込先口座種別ラベル
     */
    public String getAccountTypeLabel(String accountType) {
        return StringUtils.isNotEmpty(accountType) && "1".equals(accountType) ? ACCOUNTTYPE_LABEL : StringUtils.EMPTY;
    }

    /**
     * eコマース用データ送信準備を行う
     *
     * @param confirmModel モデル
     * @param commonInfoShop ショップ情報（共通情報）
     * @return eコマース用データ送信
     */
    public GoogleAnalyticsInfo toGoogleAnalyticsInfo(ConfirmModel confirmModel, CommonInfoShop commonInfoShop) {
        GoogleAnalyticsInfo googleAnalyticsInfo = new GoogleAnalyticsInfo();
        SalesSlipModel salesSlipModel = confirmModel.getSalesSlip();

        googleAnalyticsInfo.setShopNamePC(commonInfoShop.getShopNamePC());
        googleAnalyticsInfo.setOrderCode(confirmModel.getOrderCode());
        googleAnalyticsInfo.setBillingAmount(salesSlipModel.getBillingAmount());
        googleAnalyticsInfo.setTaxPrice(salesSlipModel.getTaxPrice());
        googleAnalyticsInfo.setCarriage(salesSlipModel.getCarriage());
        googleAnalyticsInfo.setGoodsItemList(new ArrayList<>());

        for (GoodsDetailsDto goodsDetailsDto : confirmModel.getGoodsDetailDtoList()) {
            GoogleAnalyticsSalesItem gaModelItems = new GoogleAnalyticsSalesItem();
            gaModelItems.setGoodsCode(goodsDetailsDto.getGoodsCode());
            gaModelItems.setGoodsGroupName(goodsDetailsDto.getGoodsGroupName());
            gaModelItems.setUnitValue1(goodsDetailsDto.getUnitValue1());
            gaModelItems.setUnitValue2(goodsDetailsDto.getUnitValue2());
            gaModelItems.setGoodsPrice(goodsDetailsDto.getGoodsPrice());
            gaModelItems.setOrderGoodsCount(goodsDetailsDto.getOrderGoodsCount());

            googleAnalyticsInfo.getGoodsItemList().add(gaModelItems);
        }

        return googleAnalyticsInfo;
    }

}