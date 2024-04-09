package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate;

import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.EnumType;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeDispDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryImpossibleAreaResultResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodRequest;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodTypeCarriageRequest;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodTypeCarriageResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliverySpecialChargeAreaResultResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法登録・更新入力画面Helper
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
@Component
public class DeliveryRegistUpdateHelper {
    /**
     * 都道府県別送料表示アイテムリスト作成
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新Model
     * @param deliveryMethodDetailsDto 配送方法詳細DTO
     */
    public void toPageForLoadIndex(DeliveryRegistUpdateModel deliveryRegistUpdateModel,
                                   DeliveryMethodDetailsDto deliveryMethodDetailsDto) {

        boolean registMode = false;
        DeliveryMethodEntity deliveryMethodEntity = null;
        List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList = null;

        // 登録モード
        if (deliveryMethodDetailsDto.getDeliveryMethodEntity() == null) {
            registMode = true;
        } else {
            deliveryMethodEntity = deliveryMethodDetailsDto.getDeliveryMethodEntity();
            deliveryMethodTypeCarriageEntityList = deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList();
        }

        deliveryRegistUpdateModel.setDeliveryMethodTypeItems(
                        EnumTypeUtil.getEnumMap(HTypeDispDeliveryMethodType.class));
        deliveryRegistUpdateModel.setOpenStatusPCItems(EnumTypeUtil.getEnumMap(HTypeOpenDeleteStatus.class));

        // 登録モード
        if (registMode) {
            deliveryRegistUpdateModel.setDeliveryMethodType(HTypeDeliveryMethodType.FLAT.getValue());
            deliveryRegistUpdateModel.setDeliverySpecialChargeAreaCount(0);
            deliveryRegistUpdateModel.setDeliveryImpossibleAreaCount(0);
            deliveryRegistUpdateModel.setEqualsCarriage("");
            deliveryRegistUpdateModel.setLargeAmountDiscountPrice("");
            deliveryRegistUpdateModel.setLargeAmountDiscountCarriage("");

        } else {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            deliveryRegistUpdateModel.setDeliveryMethodId(deliveryMethodEntity.getDeliveryMethodSeq().toString());
            deliveryRegistUpdateModel.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
            deliveryRegistUpdateModel.setDeliveryMethodType(deliveryMethodEntity.getDeliveryMethodType().getValue());
            deliveryRegistUpdateModel.setDeliveryMethodTypeName(
                            deliveryMethodEntity.getDeliveryMethodType().getLabel());

            // 配送追跡URL、配送追跡URLの表示期間に値をセット
            deliveryRegistUpdateModel.setDeliveryChaseURL("");
            deliveryRegistUpdateModel.setDeliveryChaseURLDisplayPeriod("");
            if (StringUtil.isNotEmpty(deliveryMethodEntity.getDeliveryChaseURL())) {
                deliveryRegistUpdateModel.setDeliveryChaseURL(deliveryMethodEntity.getDeliveryChaseURL());
            }
            if (deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod() != null) {
                deliveryRegistUpdateModel.setDeliveryChaseURLDisplayPeriod(
                                conversionUtility.toString(deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod()));
            }

            deliveryRegistUpdateModel.setOpenStatusPC(deliveryMethodEntity.getOpenStatusPC().getValue());
            deliveryRegistUpdateModel.setDeliveryMethodDisplayNamePC(deliveryMethodEntity.getDeliveryMethodName());
            deliveryRegistUpdateModel.setDeliveryNotePC(deliveryMethodEntity.getDeliveryNotePC());
            deliveryRegistUpdateModel.setDeliveryLeadTime(
                            conversionUtility.toString(deliveryMethodEntity.getDeliveryLeadTime()));
            deliveryRegistUpdateModel.setPossibleSelectDays(
                            conversionUtility.toString(deliveryMethodEntity.getPossibleSelectDays()));
            deliveryRegistUpdateModel.setReceiverTimeZone1(deliveryMethodEntity.getReceiverTimeZone1());
            deliveryRegistUpdateModel.setReceiverTimeZone2(deliveryMethodEntity.getReceiverTimeZone2());
            deliveryRegistUpdateModel.setReceiverTimeZone3(deliveryMethodEntity.getReceiverTimeZone3());
            deliveryRegistUpdateModel.setReceiverTimeZone4(deliveryMethodEntity.getReceiverTimeZone4());
            deliveryRegistUpdateModel.setReceiverTimeZone5(deliveryMethodEntity.getReceiverTimeZone5());
            deliveryRegistUpdateModel.setReceiverTimeZone6(deliveryMethodEntity.getReceiverTimeZone6());
            deliveryRegistUpdateModel.setReceiverTimeZone7(deliveryMethodEntity.getReceiverTimeZone7());
            deliveryRegistUpdateModel.setReceiverTimeZone8(deliveryMethodEntity.getReceiverTimeZone8());
            deliveryRegistUpdateModel.setReceiverTimeZone9(deliveryMethodEntity.getReceiverTimeZone9());
            deliveryRegistUpdateModel.setReceiverTimeZone10(deliveryMethodEntity.getReceiverTimeZone10());

            if (deliveryMethodEntity.getDeliveryMethodType().equals(HTypeDeliveryMethodType.FLAT)) {
                deliveryRegistUpdateModel.setEqualsCarriage(deliveryMethodEntity.getEqualsCarriage().toString());
            }

            if (!deliveryMethodEntity.getDeliveryMethodType().equals(HTypeDeliveryMethodType.AMOUNT)
                && deliveryMethodEntity.getLargeAmountDiscountPrice() != null) {
                // 高額割引送料,高額割引手数料の両方に「0」が設定されている場合
                // 入力エリアには値を出力しない
                // 【理由】
                // 高額割引設定を未設定とし、配送方法の登録を行った場合、「高額割引下限金額,高額割引手数料」ともに「0」でDBに登録される。
                // そのため、配送方法を更新した際に入力エリアに「0」が出力される。
                // この場合、「[0]円以上購入すると送料[0]円」と画面に出力され
                // 高額割引を設定していないはずが、0円以上だと送料が無料になる設定になると誤解を受ける可能性が高い。
                // また、「決済方法設定」画面では高額割引が未設定の場合は入力エリアが「未入力」となる。
                // 高額割引が設定されていない場合は入力エリアを「未入力」とし、仕様の統一を行う
                if (!deliveryMethodEntity.getLargeAmountDiscountPrice().equals(BigDecimal.ZERO)
                    || !deliveryMethodEntity.getLargeAmountDiscountCarriage().equals(BigDecimal.ZERO)) {
                    deliveryRegistUpdateModel.setLargeAmountDiscountPrice(
                                    deliveryMethodEntity.getLargeAmountDiscountPrice().toString());
                }
            }

            if (!deliveryMethodEntity.getDeliveryMethodType().equals(HTypeDeliveryMethodType.AMOUNT)
                && deliveryMethodEntity.getLargeAmountDiscountCarriage() != null) {
                // 高額割引送料,高額割引手数料の両方に「0」が設定されている場合
                // 入力エリアには値を出力しない。
                // 【理由】
                // ↑上記に記載
                if (!deliveryMethodEntity.getLargeAmountDiscountPrice().equals(BigDecimal.ZERO)
                    || !deliveryMethodEntity.getLargeAmountDiscountCarriage().equals(BigDecimal.ZERO)) {
                    deliveryRegistUpdateModel.setLargeAmountDiscountCarriage(
                                    deliveryMethodEntity.getLargeAmountDiscountCarriage().toString());
                }
            }

            if (HTypeShortfallDisplayFlag.ON.equals(deliveryMethodEntity.getShortfallDisplayFlag())) {
                deliveryRegistUpdateModel.setShortfallDisplayFlag(true);
            } else {
                deliveryRegistUpdateModel.setShortfallDisplayFlag(false);
            }

            deliveryRegistUpdateModel.setDeliverySpecialChargeAreaCount(
                            deliveryMethodDetailsDto.getDeliverySpecialChargeAreaCount());
            deliveryRegistUpdateModel.setDeliveryImpossibleAreaCount(
                            deliveryMethodDetailsDto.getDeliveryImpossibleAreaCount());

        }

        /* 都道府県別送料 start */
        DeliveryPrefectureCarriageItem prefectureCarriageItem = null;
        List<DeliveryPrefectureCarriageItem> prefectureCarriageItems = new ArrayList<>();
        List<DeliveryPrefectureCarriageItem> prefectureCarriageList = new ArrayList<>();

        // 都道府県の数分作る
        EnumType[] values = HTypePrefectureType.class.getEnumConstants();
        for (EnumType pref : values) {

            HTypePrefectureType prefectureType = (HTypePrefectureType) pref;

            prefectureCarriageItem = ApplicationContextUtility.getBean(DeliveryPrefectureCarriageItem.class);
            // 登録モード
            if (registMode) {
                // 有効フラグ
                prefectureCarriageItem.setActiveFlag(false);
                // 送料
                prefectureCarriageItem.setPrefectureCarriage("");

                // 更新モード
            } else {
                if (HTypeDeliveryMethodType.PREFECTURE.equals(deliveryMethodEntity.getDeliveryMethodType())) {
                    for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryMethodTypeCarriageEntityList) {
                        if (deliveryMethodTypeCarriageEntity.getPrefectureType().equals(prefectureType)) {
                            // 有効フラグ
                            prefectureCarriageItem.setActiveFlag(true);
                            // 送料
                            prefectureCarriageItem.setPrefectureCarriage(
                                            deliveryMethodTypeCarriageEntity.getCarriage().toString());
                            break;
                        }
                    }

                } else {
                    // 有効フラグ
                    prefectureCarriageItem.setActiveFlag(false);
                    // 送料
                    prefectureCarriageItem.setPrefectureCarriage("");
                }
            }

            // 都道府県
            prefectureCarriageItem.setPrefectureName(prefectureType.getLabel());
            // 都道府県種別
            prefectureCarriageItem.setPrefectureType(prefectureType);
            // リストに貯める
            prefectureCarriageItems.add(prefectureCarriageItem);
            prefectureCarriageList.add(CopyUtil.deepCopy(prefectureCarriageItem));
        }

        // 作ったリストをページに設定
        deliveryRegistUpdateModel.setDeliveryPrefectureCarriageItems(prefectureCarriageItems);
        deliveryRegistUpdateModel.setDeliveryPrefectureCarriageList(prefectureCarriageList);
        /* 都道府県別送料 end */

        /* 金額別送料 start */
        DeliveryAmountCarriageItem amountCarriageItem = null;
        List<DeliveryAmountCarriageItem> amountCarriageItemList = new ArrayList<>();

        // 10個作る
        for (int i = 0; i < 10; i++) {
            amountCarriageItem = ApplicationContextUtility.getBean(DeliveryAmountCarriageItem.class);
            // 上限金額
            amountCarriageItem.setMaxPrice("");
            // 送料
            amountCarriageItem.setAmountCarriage("");

            // リストに貯める
            amountCarriageItemList.add(amountCarriageItem);
        }

        // 更新モード
        // ※金額別送料は現在未使用
        if (!registMode && HTypeDeliveryMethodType.AMOUNT.equals(deliveryMethodEntity.getDeliveryMethodType())) {
            for (int i = 0; i < deliveryMethodTypeCarriageEntityList.size(); i++) {
                // 上限金額
                amountCarriageItemList.get(i)
                                      .setMaxPrice(deliveryMethodTypeCarriageEntityList.get(i)
                                                                                       .getMaxPrice()
                                                                                       .toString());
                // 送料
                amountCarriageItemList.get(i)
                                      .setAmountCarriage(deliveryMethodTypeCarriageEntityList.get(i)
                                                                                             .getCarriage()
                                                                                             .toString());
            }

        }

        // 作ったリストをページに設定
        deliveryRegistUpdateModel.setDeliveryAmountCarriageItems(amountCarriageItemList);
        /* 金額別送料 end */

        deliveryRegistUpdateModel.setDeliveryMethodDetailsDto(deliveryMethodDetailsDto);
        deliveryRegistUpdateModel.setNormality(true);
    }

    /**
     * ページへの変換処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新画面ページ
     */
    public void toPageForConfirmIndex(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // ***** 配送方法エンティティの設定。*****
        DeliveryMethodEntity deliveryMethodEntity = CopyUtil.deepCopy(
                        deliveryRegistUpdateModel.getDeliveryMethodDetailsDto().getDeliveryMethodEntity());
        if (deliveryMethodEntity == null) {
            deliveryMethodEntity = deliveryRegistUpdateModel.getDeliveryMethodEntity();
            if (deliveryMethodEntity == null) {
                deliveryMethodEntity = new DeliveryMethodEntity();
            }
        }

        deliveryMethodEntity.setDeliveryMethodName(deliveryRegistUpdateModel.getDeliveryMethodName());
        deliveryMethodEntity.setDeliveryChaseURL(deliveryRegistUpdateModel.getDeliveryChaseURL());
        deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                        conversionUtility.toBigDecimal(deliveryRegistUpdateModel.getDeliveryChaseURLDisplayPeriod()));
        deliveryMethodEntity.setDeliveryMethodDisplayNamePC(deliveryRegistUpdateModel.getDeliveryMethodName());
        // 公開状態PCをEnumに変換
        HTypeOpenDeleteStatus openStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                         deliveryRegistUpdateModel.getOpenStatusPC()
                                                                        );
        deliveryMethodEntity.setOpenStatusPC(openStatus);
        // 公開状態携帯をEnumに変換
        deliveryMethodEntity.setDeliveryNotePC(deliveryRegistUpdateModel.getDeliveryNotePC());

        HTypeDeliveryMethodType deliveryMethodType = EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                   deliveryRegistUpdateModel.getDeliveryMethodType()
                                                                                  );
        deliveryMethodEntity.setDeliveryMethodType(deliveryMethodType);
        // 全国一律なら一律送料を設定
        if (HTypeDeliveryMethodType.FLAT.equals(deliveryMethodType)) {
            deliveryMethodEntity.setEqualsCarriage(
                            BigDecimalConversionUtil.toBigDecimal(deliveryRegistUpdateModel.getEqualsCarriage()));
        }
        // 金額別以外なら高額割引送料を設定
        if (!ObjectUtils.isEmpty(deliveryMethodType) && !HTypeDeliveryMethodType.AMOUNT.equals(deliveryMethodType)) {
            if (StringUtil.isEmpty(deliveryRegistUpdateModel.getLargeAmountDiscountPrice())) {
                deliveryMethodEntity.setLargeAmountDiscountPrice(BigDecimal.ZERO);
            } else {
                deliveryMethodEntity.setLargeAmountDiscountPrice(BigDecimalConversionUtil.toBigDecimal(
                                deliveryRegistUpdateModel.getLargeAmountDiscountPrice()));
            }

            if (StringUtil.isEmpty(deliveryRegistUpdateModel.getLargeAmountDiscountCarriage())) {
                deliveryMethodEntity.setLargeAmountDiscountCarriage(BigDecimal.ZERO);
            } else {
                deliveryMethodEntity.setLargeAmountDiscountCarriage(BigDecimalConversionUtil.toBigDecimal(
                                deliveryRegistUpdateModel.getLargeAmountDiscountCarriage()));
            }
        }
        if (StringUtil.isEmpty(deliveryRegistUpdateModel.getDeliveryLeadTime())) {
            // 入力がない場合は、0をセット
            deliveryRegistUpdateModel.setDeliveryLeadTime("0");
        }
        deliveryMethodEntity.setDeliveryLeadTime(
                        conversionUtility.toInteger(deliveryRegistUpdateModel.getDeliveryLeadTime()));
        if (StringUtil.isEmpty(deliveryRegistUpdateModel.getPossibleSelectDays())) {
            // 入力がない場合は、0をセット
            deliveryRegistUpdateModel.setPossibleSelectDays("0");
        }
        deliveryMethodEntity.setPossibleSelectDays(
                        conversionUtility.toInteger(deliveryRegistUpdateModel.getPossibleSelectDays()));
        deliveryMethodEntity.setReceiverTimeZone1(deliveryRegistUpdateModel.getReceiverTimeZone1());
        deliveryMethodEntity.setReceiverTimeZone2(deliveryRegistUpdateModel.getReceiverTimeZone2());
        deliveryMethodEntity.setReceiverTimeZone3(deliveryRegistUpdateModel.getReceiverTimeZone3());
        deliveryMethodEntity.setReceiverTimeZone4(deliveryRegistUpdateModel.getReceiverTimeZone4());
        deliveryMethodEntity.setReceiverTimeZone5(deliveryRegistUpdateModel.getReceiverTimeZone5());
        deliveryMethodEntity.setReceiverTimeZone6(deliveryRegistUpdateModel.getReceiverTimeZone6());
        deliveryMethodEntity.setReceiverTimeZone7(deliveryRegistUpdateModel.getReceiverTimeZone7());
        deliveryMethodEntity.setReceiverTimeZone8(deliveryRegistUpdateModel.getReceiverTimeZone8());
        deliveryMethodEntity.setReceiverTimeZone9(deliveryRegistUpdateModel.getReceiverTimeZone9());
        deliveryMethodEntity.setReceiverTimeZone10(deliveryRegistUpdateModel.getReceiverTimeZone10());

        // ***** 配送区分別送料エンティティの設定 *****
        List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList = new ArrayList<>();
        DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity = null;
        if (HTypeDeliveryMethodType.PREFECTURE.equals(deliveryMethodType)) {
            for (DeliveryPrefectureCarriageItem prefectureCarriageItem : deliveryRegistUpdateModel.getDeliveryPrefectureCarriageItems()) {
                if (prefectureCarriageItem.isActiveFlag()) {
                    // 配送区分別送料エンティティの作成
                    deliveryMethodTypeCarriageEntity =
                                    ApplicationContextUtility.getBean(DeliveryMethodTypeCarriageEntity.class);

                    deliveryMethodTypeCarriageEntity.setPrefectureType(prefectureCarriageItem.getPrefectureType());
                    deliveryMethodTypeCarriageEntity.setMaxPrice(BigDecimal.ZERO);
                    deliveryMethodTypeCarriageEntity.setCarriage(BigDecimalConversionUtil.toBigDecimal(
                                    prefectureCarriageItem.getPrefectureCarriage()));

                    // リストに貯める
                    deliveryMethodTypeCarriageEntityList.add(deliveryMethodTypeCarriageEntity);
                }
            }

        } else if (HTypeDeliveryMethodType.AMOUNT.equals(deliveryMethodType)) {
            // ※金額別送料は現在未使用
            for (DeliveryAmountCarriageItem amountCarriageItem : deliveryRegistUpdateModel.getDeliveryAmountCarriageItems()) {
                if (!StringUtil.isEmpty(amountCarriageItem.getMaxPrice())) {
                    // 配送区分別送料エンティティの作成
                    deliveryMethodTypeCarriageEntity =
                                    ApplicationContextUtility.getBean(DeliveryMethodTypeCarriageEntity.class);

                    deliveryMethodTypeCarriageEntity.setPrefectureType(HTypePrefectureType.HOKKAIDO);
                    deliveryMethodTypeCarriageEntity.setMaxPrice(
                                    BigDecimalConversionUtil.toBigDecimal(amountCarriageItem.getMaxPrice()));
                    deliveryMethodTypeCarriageEntity.setCarriage(
                                    BigDecimalConversionUtil.toBigDecimal(amountCarriageItem.getAmountCarriage()));

                    deliveryMethodTypeCarriageEntityList.add(deliveryMethodTypeCarriageEntity);
                }
            }
        }
        if (deliveryRegistUpdateModel.isShortfallDisplayFlag()) {
            deliveryMethodEntity.setShortfallDisplayFlag(HTypeShortfallDisplayFlag.ON);
        } else {
            deliveryMethodEntity.setShortfallDisplayFlag(HTypeShortfallDisplayFlag.OFF);
        }

        // ページに設定
        deliveryRegistUpdateModel.setDeliveryMethodEntity(deliveryMethodEntity);
        deliveryRegistUpdateModel.setDeliveryMethodTypeCarriageEntityList(deliveryMethodTypeCarriageEntityList);

    }

    /**
     * ページへの変換処理
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新確認画面ページ
     */
    public void toPageForLoadConfirm(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        deliveryRegistUpdateModel.setShortfallDisplayFlag(deliveryRegistUpdateModel.getDeliveryMethodEntity()
                                                                                   .getShortfallDisplayFlag()
                                                                                   .equals(HTypeShortfallDisplayFlag.ON));

        if (!deliveryRegistUpdateModel.isRegistMode()) {
            // 修正箇所の検出
            DeliveryMethodEntity original = CopyUtil.deepCopy(
                            deliveryRegistUpdateModel.getDeliveryMethodDetailsDto().getDeliveryMethodEntity());
            DeliveryMethodEntity modified = CopyUtil.deepCopy(deliveryRegistUpdateModel.getDeliveryMethodEntity());

            // テキストエリアの項目の改行コードを統一
            if (original.getDeliveryNotePC() != null) {
                original.setDeliveryNotePC(original.getDeliveryNotePC().replaceAll("\r\n", "\n"));
            }

            if (modified.getDeliveryNotePC() != null) {
                modified.setDeliveryNotePC(modified.getDeliveryNotePC().replaceAll("\r\n", "\n"));
            }

            deliveryRegistUpdateModel.setModifiedList(DiffUtil.diff(original, modified));

            List<List<String>> tmpDiffList = new ArrayList<>();
            // 変更前の都道府県別
            for (int i = 0; i < deliveryRegistUpdateModel.getDeliveryPrefectureCarriageList().size(); i++) {
                // 変更後の都道府県別
                tmpDiffList.add(DiffUtil.diff(
                                deliveryRegistUpdateModel.getDeliveryPrefectureCarriageList().get(i),
                                deliveryRegistUpdateModel.getDeliveryPrefectureCarriageItems().get(i)
                                             ));
            }
            deliveryRegistUpdateModel.setModifiedPrefectureList(tmpDiffList);
        }
    }

    /**
     * 配送方法登録リクエストに変換
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新確認画面ページ
     * @return 配送方法登録リクエスト
     */
    public ShippingMethodRegistRequest toShippingMethodRegistRequest(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {

        ShippingMethodRegistRequest shippingMethodRegistRequest = new ShippingMethodRegistRequest();
        List<DeliveryMethodTypeCarriageRequest> deliveryMethodTypeCarriageRequestList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(deliveryRegistUpdateModel.getDeliveryMethodEntity())) {
            DeliveryMethodRequest deliveryMethodRequest = new DeliveryMethodRequest();
            DeliveryMethodEntity deliveryMethodEntity = deliveryRegistUpdateModel.getDeliveryMethodEntity();

            deliveryMethodRequest.setDeliveryMethodSeq(deliveryMethodEntity.getDeliveryMethodSeq());
            deliveryMethodRequest.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
            deliveryMethodRequest.setDeliveryMethodDisplayNamePC(deliveryMethodEntity.getDeliveryMethodDisplayNamePC());
            deliveryMethodRequest.setOpenStatusPC(deliveryMethodEntity.getOpenStatusPC().getValue());
            deliveryMethodRequest.setDeliveryNotePC(deliveryMethodEntity.getDeliveryNotePC());
            deliveryMethodRequest.setDeliveryMethodType(deliveryMethodEntity.getDeliveryMethodType().getValue());
            deliveryMethodRequest.setEqualsCarriage(deliveryMethodEntity.getEqualsCarriage());
            deliveryMethodRequest.setLargeAmountDiscountPrice(deliveryMethodEntity.getLargeAmountDiscountPrice());
            deliveryMethodRequest.setLargeAmountDiscountCarriage(deliveryMethodEntity.getLargeAmountDiscountCarriage());
            deliveryMethodRequest.setShortfallDisplayFlag(deliveryMethodEntity.getShortfallDisplayFlag().getValue());
            deliveryMethodRequest.setDeliveryLeadTime(deliveryMethodEntity.getDeliveryLeadTime());
            deliveryMethodRequest.setDeliveryChaseURL(deliveryMethodEntity.getDeliveryChaseURL());
            deliveryMethodRequest.setDeliveryChaseURLDisplayPeriod(
                            deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod());
            deliveryMethodRequest.setPossibleSelectDays(deliveryMethodEntity.getPossibleSelectDays());
            deliveryMethodRequest.setReceiverTimeZone1(deliveryMethodEntity.getReceiverTimeZone1());
            deliveryMethodRequest.setReceiverTimeZone2(deliveryMethodEntity.getReceiverTimeZone2());
            deliveryMethodRequest.setReceiverTimeZone3(deliveryMethodEntity.getReceiverTimeZone3());
            deliveryMethodRequest.setReceiverTimeZone4(deliveryMethodEntity.getReceiverTimeZone4());
            deliveryMethodRequest.setReceiverTimeZone5(deliveryMethodEntity.getReceiverTimeZone5());
            deliveryMethodRequest.setReceiverTimeZone6(deliveryMethodEntity.getReceiverTimeZone6());
            deliveryMethodRequest.setReceiverTimeZone7(deliveryMethodEntity.getReceiverTimeZone7());
            deliveryMethodRequest.setReceiverTimeZone8(deliveryMethodEntity.getReceiverTimeZone8());
            deliveryMethodRequest.setReceiverTimeZone9(deliveryMethodEntity.getReceiverTimeZone9());
            deliveryMethodRequest.setReceiverTimeZone10(deliveryMethodEntity.getReceiverTimeZone10());
            deliveryMethodRequest.setOrderDisplay(deliveryMethodEntity.getOrderDisplay());

            shippingMethodRegistRequest.setDeliveryMethodRequest(deliveryMethodRequest);
        }

        if (deliveryRegistUpdateModel.getDeliveryMethodTypeCarriageEntityList() != null) {

            for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryRegistUpdateModel.getDeliveryMethodTypeCarriageEntityList()) {
                DeliveryMethodTypeCarriageRequest deliveryMethodTypeCarriageRequest =
                                new DeliveryMethodTypeCarriageRequest();

                deliveryMethodTypeCarriageRequest.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageEntity.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageRequest.setPrefectureType(
                                deliveryMethodTypeCarriageEntity.getPrefectureType().getValue());
                deliveryMethodTypeCarriageRequest.setMaxPrice(deliveryMethodTypeCarriageEntity.getMaxPrice());
                deliveryMethodTypeCarriageRequest.setCarriage(deliveryMethodTypeCarriageEntity.getCarriage());

                deliveryMethodTypeCarriageRequestList.add(deliveryMethodTypeCarriageRequest);
            }
        }

        shippingMethodRegistRequest.setDeliveryMethodTypeCarriageRequestList(deliveryMethodTypeCarriageRequestList);

        return shippingMethodRegistRequest;
    }

    /**
     * 配送方法登録リクエストに変換
     *
     * @param deliveryRegistUpdateModel 配送方法登録・更新確認画面ページ
     * @return shippingMethodUpdateRequest 配送方法更新リクエスト
     */
    public ShippingMethodUpdateRequest toShippingMethodUpdateRequest(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {

        ShippingMethodUpdateRequest shippingMethodUpdateRequest = new ShippingMethodUpdateRequest();
        List<DeliveryMethodTypeCarriageRequest> deliveryMethodTypeCarriageRequestList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(deliveryRegistUpdateModel.getDeliveryMethodEntity())) {

            DeliveryMethodRequest deliveryMethodRequest = new DeliveryMethodRequest();

            deliveryMethodRequest.setDeliveryMethodSeq(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryMethodSeq());
            deliveryMethodRequest.setDeliveryMethodName(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryMethodName());
            deliveryMethodRequest.setDeliveryMethodDisplayNamePC(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryMethodDisplayNamePC());
            deliveryMethodRequest.setOpenStatusPC(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getOpenStatusPC().getValue());
            deliveryMethodRequest.setDeliveryNotePC(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryNotePC());
            deliveryMethodRequest.setDeliveryMethodType(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryMethodType().getValue());
            deliveryMethodRequest.setEqualsCarriage(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getEqualsCarriage());
            deliveryMethodRequest.setLargeAmountDiscountPrice(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getLargeAmountDiscountPrice());
            deliveryMethodRequest.setLargeAmountDiscountCarriage(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getLargeAmountDiscountCarriage());
            deliveryMethodRequest.setShortfallDisplayFlag(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getShortfallDisplayFlag().getValue());
            deliveryMethodRequest.setDeliveryLeadTime(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryLeadTime());
            deliveryMethodRequest.setDeliveryChaseURL(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryChaseURL());
            deliveryMethodRequest.setDeliveryChaseURLDisplayPeriod(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getDeliveryChaseURLDisplayPeriod());
            deliveryMethodRequest.setPossibleSelectDays(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getPossibleSelectDays());
            deliveryMethodRequest.setReceiverTimeZone1(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone1());
            deliveryMethodRequest.setReceiverTimeZone2(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone2());
            deliveryMethodRequest.setReceiverTimeZone3(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone3());
            deliveryMethodRequest.setReceiverTimeZone4(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone4());
            deliveryMethodRequest.setReceiverTimeZone5(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone5());
            deliveryMethodRequest.setReceiverTimeZone6(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone6());
            deliveryMethodRequest.setReceiverTimeZone7(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone7());
            deliveryMethodRequest.setReceiverTimeZone8(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone8());
            deliveryMethodRequest.setReceiverTimeZone9(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone9());
            deliveryMethodRequest.setReceiverTimeZone10(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getReceiverTimeZone10());
            deliveryMethodRequest.setOrderDisplay(
                            deliveryRegistUpdateModel.getDeliveryMethodEntity().getOrderDisplay());

            shippingMethodUpdateRequest.setDeliveryMethodRequest(deliveryMethodRequest);
        }

        if (deliveryRegistUpdateModel.getDeliveryMethodTypeCarriageEntityList() != null) {

            for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryRegistUpdateModel.getDeliveryMethodTypeCarriageEntityList()) {

                DeliveryMethodTypeCarriageRequest deliveryMethodTypeCarriageRequest =
                                new DeliveryMethodTypeCarriageRequest();

                deliveryMethodTypeCarriageRequest.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageEntity.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageRequest.setPrefectureType(
                                deliveryMethodTypeCarriageEntity.getPrefectureType().getValue());
                deliveryMethodTypeCarriageRequest.setMaxPrice(deliveryMethodTypeCarriageEntity.getMaxPrice());
                deliveryMethodTypeCarriageRequest.setCarriage(deliveryMethodTypeCarriageEntity.getCarriage());

                deliveryMethodTypeCarriageRequestList.add(deliveryMethodTypeCarriageRequest);
            }
        }
        shippingMethodUpdateRequest.setDeliveryMethodTypeCarriageRequestList(deliveryMethodTypeCarriageRequestList);

        return shippingMethodUpdateRequest;
    }

    /**
     * 配送方法登録リクエストに変換
     *
     * @param deliveryMethodEntity 配送方法クラス
     * @return shippingMethodUpdateRequest 配送方法更新リクエスト
     */
    public ShippingMethodCheckRequest toShippingMethodCheckRequest(DeliveryMethodEntity deliveryMethodEntity) {

        ShippingMethodCheckRequest shippingMethodCheckRequest = new ShippingMethodCheckRequest();

        if (!ObjectUtils.isEmpty(deliveryMethodEntity)) {

            shippingMethodCheckRequest.setDeliveryMethodSeq(deliveryMethodEntity.getDeliveryMethodSeq());
            shippingMethodCheckRequest.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
            shippingMethodCheckRequest.setDeliveryMethodDisplayNamePC(
                            deliveryMethodEntity.getDeliveryMethodDisplayNamePC());
            shippingMethodCheckRequest.setOpenStatusPC(deliveryMethodEntity.getOpenStatusPC().getValue());
            shippingMethodCheckRequest.setDeliveryNotePC(deliveryMethodEntity.getDeliveryNotePC());
            shippingMethodCheckRequest.setDeliveryMethodType(deliveryMethodEntity.getDeliveryMethodType().getValue());
            shippingMethodCheckRequest.setEqualsCarriage(deliveryMethodEntity.getEqualsCarriage());
            shippingMethodCheckRequest.setLargeAmountDiscountPrice(deliveryMethodEntity.getLargeAmountDiscountPrice());
            shippingMethodCheckRequest.setLargeAmountDiscountCarriage(
                            deliveryMethodEntity.getLargeAmountDiscountCarriage());
            shippingMethodCheckRequest.setShortfallDisplayFlag(
                            deliveryMethodEntity.getShortfallDisplayFlag().getValue());
            shippingMethodCheckRequest.setDeliveryLeadTime(deliveryMethodEntity.getDeliveryLeadTime());
            shippingMethodCheckRequest.setDeliveryChaseURL(deliveryMethodEntity.getDeliveryChaseURL());
            shippingMethodCheckRequest.setDeliveryChaseURLDisplayPeriod(
                            deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod());
            shippingMethodCheckRequest.setPossibleSelectDays(deliveryMethodEntity.getPossibleSelectDays());
            shippingMethodCheckRequest.setReceiverTimeZone1(deliveryMethodEntity.getReceiverTimeZone1());
            shippingMethodCheckRequest.setReceiverTimeZone2(deliveryMethodEntity.getReceiverTimeZone2());
            shippingMethodCheckRequest.setReceiverTimeZone3(deliveryMethodEntity.getReceiverTimeZone3());
            shippingMethodCheckRequest.setReceiverTimeZone4(deliveryMethodEntity.getReceiverTimeZone4());
            shippingMethodCheckRequest.setReceiverTimeZone5(deliveryMethodEntity.getReceiverTimeZone5());
            shippingMethodCheckRequest.setReceiverTimeZone6(deliveryMethodEntity.getReceiverTimeZone6());
            shippingMethodCheckRequest.setReceiverTimeZone7(deliveryMethodEntity.getReceiverTimeZone7());
            shippingMethodCheckRequest.setReceiverTimeZone8(deliveryMethodEntity.getReceiverTimeZone8());
            shippingMethodCheckRequest.setReceiverTimeZone9(deliveryMethodEntity.getReceiverTimeZone9());
            shippingMethodCheckRequest.setReceiverTimeZone10(deliveryMethodEntity.getReceiverTimeZone10());
            shippingMethodCheckRequest.setOrderDisplay(deliveryMethodEntity.getOrderDisplay());
        }

        return shippingMethodCheckRequest;
    }

    /**
     * 配送方法登録リクエストに変換
     *
     * @param shippingMethodResponse 配送方法レスポンス
     * @return deliveryMethodDetailsDto 配送方法詳細Dto
     */
    public DeliveryMethodDetailsDto toDeliveryMethodDetailsDto(ShippingMethodResponse shippingMethodResponse) {

        DeliveryMethodDetailsDto deliveryMethodDetailsDtoUpdate = new DeliveryMethodDetailsDto();
        deliveryMethodDetailsDtoUpdate.setDeliveryMethodTypeCarriageEntityList(new ArrayList<>());
        deliveryMethodDetailsDtoUpdate.setDeliveryImpossibleAreaResultDtoList(new ArrayList<>());
        deliveryMethodDetailsDtoUpdate.setDeliverySpecialChargeAreaResultDtoList(new ArrayList<>());

        if (!ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())) {

            DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

            deliveryMethodEntity.setDeliveryMethodSeq(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodSeq());
            deliveryMethodEntity.setDeliveryMethodName(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodDisplayNamePC());
            deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               shippingMethodResponse.getDeliveryMethodResponse()
                                                                                                     .getOpenStatusPC()
                                                                              ));
            deliveryMethodEntity.setDeliveryNotePC(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryNotePC());
            deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                     shippingMethodResponse.getDeliveryMethodResponse()
                                                                                                           .getDeliveryMethodType()
                                                                                    ));
            deliveryMethodEntity.setEqualsCarriage(
                            shippingMethodResponse.getDeliveryMethodResponse().getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(
                            shippingMethodResponse.getDeliveryMethodResponse().getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            shippingMethodResponse.getDeliveryMethodResponse().getLargeAmountDiscountCarriage());
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                       shippingMethodResponse.getDeliveryMethodResponse()
                                                                                                             .getShortfallDisplayFlag()
                                                                                      ));
            if (shippingMethodResponse.getDeliveryMethodResponse().getDeliveryLeadTime() != null) {
                deliveryMethodEntity.setDeliveryLeadTime(
                                shippingMethodResponse.getDeliveryMethodResponse().getDeliveryLeadTime());
            }
            deliveryMethodEntity.setDeliveryChaseURL(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURLDisplayPeriod());
            if (shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays() != null) {
                deliveryMethodEntity.setPossibleSelectDays(
                                shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays());
            }
            deliveryMethodEntity.setReceiverTimeZone1(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(shippingMethodResponse.getDeliveryMethodResponse().getOrderDisplay());

            deliveryMethodDetailsDtoUpdate.setDeliveryMethodEntity(deliveryMethodEntity);
        }

        if (shippingMethodResponse.getDeliveryMethodTypeCarriageResponseList() != null) {

            for (DeliveryMethodTypeCarriageResponse deliveryMethodTypeCarriageResponse : shippingMethodResponse.getDeliveryMethodTypeCarriageResponseList()) {
                DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity =
                                new DeliveryMethodTypeCarriageEntity();

                deliveryMethodTypeCarriageEntity.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageResponse.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageEntity.setPrefectureType(
                                EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                              deliveryMethodTypeCarriageResponse.getPrefectureType()
                                                             ));
                deliveryMethodTypeCarriageEntity.setMaxPrice(deliveryMethodTypeCarriageResponse.getMaxPrice());
                deliveryMethodTypeCarriageEntity.setCarriage(deliveryMethodTypeCarriageResponse.getCarriage());

                deliveryMethodDetailsDtoUpdate.getDeliveryMethodTypeCarriageEntityList()
                                              .add(deliveryMethodTypeCarriageEntity);
            }
        }

        if (shippingMethodResponse.getDeliverySpecialChargeAreaCount() != null) {
            deliveryMethodDetailsDtoUpdate.setDeliverySpecialChargeAreaCount(
                            shippingMethodResponse.getDeliverySpecialChargeAreaCount());
        }

        if (shippingMethodResponse.getDeliveryImpossibleAreaCount() != null) {
            deliveryMethodDetailsDtoUpdate.setDeliveryImpossibleAreaCount(
                            shippingMethodResponse.getDeliveryImpossibleAreaCount());
        }

        if (shippingMethodResponse.getDeliveryImpossibleAreaResultResponseList() != null) {

            for (DeliveryImpossibleAreaResultResponse deliveryImpossibleAreaResultResponse : shippingMethodResponse.getDeliveryImpossibleAreaResultResponseList()) {
                DeliveryImpossibleAreaResultDto deliveryImpossibleAreaResultDto = new DeliveryImpossibleAreaResultDto();

                deliveryImpossibleAreaResultDto.setDeliveryMethodSeq(
                                deliveryImpossibleAreaResultResponse.getDeliveryMethodSeq());
                deliveryImpossibleAreaResultDto.setZipcode(deliveryImpossibleAreaResultResponse.getZipcode());
                deliveryImpossibleAreaResultDto.setPrefecture(deliveryImpossibleAreaResultResponse.getPrefecture());
                deliveryImpossibleAreaResultDto.setCity(deliveryImpossibleAreaResultResponse.getCity());
                deliveryImpossibleAreaResultDto.setTown(deliveryImpossibleAreaResultResponse.getTown());
                deliveryImpossibleAreaResultDto.setNumbers(deliveryImpossibleAreaResultResponse.getNumbers());
                deliveryImpossibleAreaResultDto.setAddressList(deliveryImpossibleAreaResultResponse.getAddressList());

                deliveryMethodDetailsDtoUpdate.getDeliveryImpossibleAreaResultDtoList()
                                              .add(deliveryImpossibleAreaResultDto);
            }
        }

        if (shippingMethodResponse.getDeliverySpecialChargeAreaResultResponseList() != null) {

            for (DeliverySpecialChargeAreaResultResponse deliverySpecialChargeAreaResultResponse : shippingMethodResponse.getDeliverySpecialChargeAreaResultResponseList()) {
                DeliverySpecialChargeAreaResultDto deliverySpecialChargeAreaResultDto =
                                new DeliverySpecialChargeAreaResultDto();

                deliverySpecialChargeAreaResultDto.setDeliveryMethodSeq(
                                deliverySpecialChargeAreaResultResponse.getDeliveryMethodSeq());
                deliverySpecialChargeAreaResultDto.setZipcode(deliverySpecialChargeAreaResultResponse.getZipcode());
                deliverySpecialChargeAreaResultDto.setPrefecture(
                                deliverySpecialChargeAreaResultResponse.getPrefecture());
                deliverySpecialChargeAreaResultDto.setCity(deliverySpecialChargeAreaResultResponse.getCity());
                deliverySpecialChargeAreaResultDto.setTown(deliverySpecialChargeAreaResultResponse.getTown());
                deliverySpecialChargeAreaResultDto.setNumbers(deliverySpecialChargeAreaResultResponse.getNumbers());
                deliverySpecialChargeAreaResultDto.setAddressList(
                                deliverySpecialChargeAreaResultResponse.getAddressList());

                deliveryMethodDetailsDtoUpdate.getDeliverySpecialChargeAreaResultDtoList()
                                              .add(deliverySpecialChargeAreaResultDto);
            }
        }

        return deliveryMethodDetailsDtoUpdate;
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param deliveryRegistUpdateModel
     */
    public void setDefaultValueForLoad(DeliveryRegistUpdateModel deliveryRegistUpdateModel) {
        // 公開状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(deliveryRegistUpdateModel.getOpenStatusPC())) {
            deliveryRegistUpdateModel.setOpenStatusPC(HTypeOpenDeleteStatus.NO_OPEN.getValue());
        }
    }
}