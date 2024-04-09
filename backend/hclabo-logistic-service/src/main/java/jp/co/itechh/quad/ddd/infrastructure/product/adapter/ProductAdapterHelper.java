package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsGroupDisplayDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ProductAdapterHelper {

    /**
     * 変換Utility
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Utility
     */
    @Autowired
    public ProductAdapterHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 文字型に変換
     *
     * @param stringList
     * @return
     */
    public List<Integer> toIntegerList(List<String> stringList) {

        if (stringList == null) {
            return null;
        }

        return stringList.stream().map(conversionUtility::toInteger).collect(Collectors.toList());
    }

    /**
     * 商品詳細Dtoに変換
     *
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @return 商品詳細Dto
     */
    public List<GoodsDetailsDto> toGoodsDetailsDtoList(ProductDetailListResponse productDetailListResponse) {

        if (productDetailListResponse == null || CollectionUtil.isEmpty(
                        productDetailListResponse.getGoodsDetailsList())) {
            return null;
        }

        List<GoodsDetailsResponse> goodsDetailsResponseList = productDetailListResponse.getGoodsDetailsList();

        List<GoodsDetailsDto> goodsDetailsDtoList = new ArrayList<>();
        for (GoodsDetailsResponse goodsDetailsResponse : goodsDetailsResponseList) {

            GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

            goodsDetailsDto.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setVersionNo(goodsDetailsResponse.getVersionNo());
            goodsDetailsDto.setGoodsCode(goodsDetailsResponse.getGoodsCode());
            if (goodsDetailsResponse.getGoodsTaxType() != null) {
                goodsDetailsDto.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                              goodsDetailsResponse.getGoodsTaxType()
                                                                             ));
            }
            goodsDetailsDto.setTaxRate(goodsDetailsResponse.getTaxRate());
            goodsDetailsDto.setGoodsPriceInTax(goodsDetailsResponse.getGoodsPriceInTax());
            goodsDetailsDto.setGoodsPrice(goodsDetailsResponse.getGoodsPrice());
            if (goodsDetailsResponse.getAlcoholFlag() != null) {
                goodsDetailsDto.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                             goodsDetailsResponse.getAlcoholFlag()
                                                                            ));
            }
            goodsDetailsDto.setDeliveryType(goodsDetailsResponse.getDeliveryType());
            if (goodsDetailsResponse.getSaleStatus() != null) {
                goodsDetailsDto.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                              goodsDetailsResponse.getSaleStatus()
                                                                             ));
            }
            goodsDetailsDto.setSaleStartTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getSaleStartTime()));
            goodsDetailsDto.setSaleEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getSaleEndTime()));
            if (goodsDetailsResponse.getUnitManagementFlag() != null) {
                goodsDetailsDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                    goodsDetailsResponse.getUnitManagementFlag()
                                                                                   ));
            }
            if (goodsDetailsResponse.getStockManagementFlag() != null) {
                goodsDetailsDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                     goodsDetailsResponse.getStockManagementFlag()
                                                                                    ));
            }
            if (goodsDetailsResponse.getIndividualDeliveryType() != null) {
                goodsDetailsDto.setIndividualDeliveryType(
                                EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                              goodsDetailsResponse.getIndividualDeliveryType()
                                                             ));
            }
            goodsDetailsDto.setPurchasedMax(goodsDetailsResponse.getPurchasedMax());
            if (goodsDetailsResponse.getFreeDeliveryFlag() != null) {
                goodsDetailsDto.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                                  goodsDetailsResponse.getFreeDeliveryFlag()
                                                                                 ));
            }
            goodsDetailsDto.setOrderDisplay(goodsDetailsResponse.getOrderDisplay());
            goodsDetailsDto.setUnitValue1(goodsDetailsResponse.getUnitValue1());
            goodsDetailsDto.setUnitValue2(goodsDetailsResponse.getUnitValue2());
            goodsDetailsDto.setJanCode(goodsDetailsResponse.getJanCode());
            goodsDetailsDto.setSalesPossibleStock(goodsDetailsResponse.getSalesPossibleStock());
            goodsDetailsDto.setRealStock(goodsDetailsResponse.getRealStock());
            goodsDetailsDto.setOrderReserveStock(goodsDetailsResponse.getOrderReserveStock());
            goodsDetailsDto.setRemainderFewStock(goodsDetailsResponse.getRemainderFewStock());
            goodsDetailsDto.setOrderPointStock(goodsDetailsResponse.getOrderPointStock());
            goodsDetailsDto.setSafetyStock(goodsDetailsResponse.getSafetyStock());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setWhatsnewDate(conversionUtility.toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
            if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   goodsDetailsResponse.getGoodsOpenStatus()
                                                                                  ));
            }
            goodsDetailsDto.setOpenStartTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getOpenStartTime()));
            goodsDetailsDto.setOpenEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getOpenEndTime()));
            goodsDetailsDto.setGoodsGroupName(goodsDetailsResponse.getGoodsGroupName());
            goodsDetailsDto.setUnitTitle1(goodsDetailsResponse.getUnitTitle1());
            goodsDetailsDto.setUnitTitle2(goodsDetailsResponse.getUnitTitle2());
            if (!CollectionUtils.isEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                goodsDetailsDto.setGoodsGroupImageEntityList(
                                toGoodsGroupImageEntityList(goodsDetailsResponse.getGoodsGroupImageList()));
            }
            if (goodsDetailsResponse.getSnsLinkFlag() != null) {
                goodsDetailsDto.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                             goodsDetailsResponse.getSnsLinkFlag()
                                                                            ));
            }
            goodsDetailsDto.setMetaDescription(goodsDetailsResponse.getMetaDescription());
            if (goodsDetailsResponse.getStockStatus() != null) {
                goodsDetailsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                               goodsDetailsResponse.getStockStatus()
                                                                              ));
            }
            if (!CollectionUtils.isEmpty(goodsDetailsResponse.getGoodsIconList())) {
                goodsDetailsDto.setGoodsIconList(
                                toGoodsInformationIconDetailsDtoList(goodsDetailsResponse.getGoodsIconList()));
            }
            goodsDetailsDto.setGoodsNote1(goodsDetailsResponse.getGoodsNote1());
            goodsDetailsDto.setGoodsNote2(goodsDetailsResponse.getGoodsNote2());
            goodsDetailsDto.setGoodsNote3(goodsDetailsResponse.getGoodsNote3());
            goodsDetailsDto.setGoodsNote4(goodsDetailsResponse.getGoodsNote4());
            goodsDetailsDto.setGoodsNote5(goodsDetailsResponse.getGoodsNote5());
            goodsDetailsDto.setGoodsNote6(goodsDetailsResponse.getGoodsNote6());
            goodsDetailsDto.setGoodsNote7(goodsDetailsResponse.getGoodsNote7());
            goodsDetailsDto.setGoodsNote8(goodsDetailsResponse.getGoodsNote8());
            goodsDetailsDto.setGoodsNote9(goodsDetailsResponse.getGoodsNote9());
            goodsDetailsDto.setGoodsNote10(goodsDetailsResponse.getGoodsNote10());
            goodsDetailsDto.setOrderSetting1(goodsDetailsResponse.getOrderSetting1());
            goodsDetailsDto.setOrderSetting2(goodsDetailsResponse.getOrderSetting2());
            goodsDetailsDto.setOrderSetting3(goodsDetailsResponse.getOrderSetting3());
            goodsDetailsDto.setOrderSetting4(goodsDetailsResponse.getOrderSetting4());
            goodsDetailsDto.setOrderSetting5(goodsDetailsResponse.getOrderSetting5());
            goodsDetailsDto.setOrderSetting6(goodsDetailsResponse.getOrderSetting6());
            goodsDetailsDto.setOrderSetting7(goodsDetailsResponse.getOrderSetting7());
            goodsDetailsDto.setOrderSetting8(goodsDetailsResponse.getOrderSetting8());
            goodsDetailsDto.setOrderSetting9(goodsDetailsResponse.getOrderSetting9());
            goodsDetailsDto.setOrderSetting10(goodsDetailsResponse.getOrderSetting10());
            HTypeNoveltyGoodsType noveltyGoodsType = EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                                   goodsDetailsResponse.getNoveltyGoodsType()
                                                                                  );
            goodsDetailsDto.setNoveltyGoodsType(noveltyGoodsType);

            goodsDetailsDtoList.add(goodsDetailsDto);
        }

        return goodsDetailsDtoList;
    }

    /**
     * 商品グループ画像エンティティに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像レスポンスリスト
     * @return 商品グループ画像エンティティ
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageEntityList(List<GoodsGroupImageResponse> goodsGroupImageResponseList) {

        if (CollectionUtil.isEmpty(goodsGroupImageResponseList)) {
            return new ArrayList<>();
        }

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
        goodsGroupImageResponseList.forEach(item -> {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
            goodsGroupImageEntity.setImageFileName(item.getImageFileName());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsGroupImageEntityList.add(goodsGroupImageEntity);
        });

        return goodsGroupImageEntityList;
    }

    /**
     * アイコン詳細DTOに変換
     *
     * @param goodsIconList アイコン詳細レスポンスリスト
     * @return アイコン詳細DTO
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsResponse> goodsIconList) {

        if (CollectionUtil.isEmpty(goodsIconList)) {
            return new ArrayList<>();
        }

        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
        goodsIconList.forEach(goodsIcon -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setIconSeq(goodsIcon.getIconSeq());
            goodsInformationIconDetailsDto.setGoodsGroupSeq(goodsIcon.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconName(goodsIcon.getIconName());
            goodsInformationIconDetailsDto.setColorCode(goodsIcon.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(goodsIcon.getOrderDisplay());

            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品詳細DTOに変換
     *
     * @param productDetailByGoodCodeResponse 商品詳細レスポンスリスト
     * @return 商品詳細DTO
     */
    public GoodsDetailsDto productDetailGetByGoodCode(GoodsDetailsResponse productDetailByGoodCodeResponse) {

        if (productDetailByGoodCodeResponse == null) {
            return null;
        }

        GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

        goodsDetailsDto.setGoodsSeq(productDetailByGoodCodeResponse.getGoodsSeq());
        goodsDetailsDto.setGoodsGroupSeq(productDetailByGoodCodeResponse.getGoodsGroupSeq());

        return goodsDetailsDto;
    }

    /**
     * 商品グループ表示DTOに変換
     *
     * @param response 商品表示レスポンスリスト
     * @return 商品グループ表示DTO
     */
    public GoodsGroupDisplayDto toGoodsGroupDisplayDto(ProductDisplayResponse response) {
        if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getGoodsGroupDisplay())) {
            return null;
        }

        GoodsGroupDisplayDto displayDto = new GoodsGroupDisplayDto();

        displayDto.setGoodsGroupSeq(response.getGoodsGroupDisplay().getGoodsGroupSeq());
        displayDto.setInformationIconPC(response.getGoodsGroupDisplay().getInformationIcon());
        displayDto.setSearchKeyword(response.getGoodsGroupDisplay().getSearchKeyword());
        displayDto.setSearchKeywordEm(response.getGoodsGroupDisplay().getSearchKeywordEmUc());
        displayDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                       response.getGoodsGroupDisplay()
                                                                               .getUnitManagementFlag()
                                                                      ));
        displayDto.setUnitTitle1(response.getGoodsGroupDisplay().getUnitTitle1());
        displayDto.setUnitTitle2(response.getGoodsGroupDisplay().getUnitTitle2());
        displayDto.setMetaDescription(response.getGoodsGroupDisplay().getMetaDescription());
        displayDto.setMetaKeyword(response.getGoodsGroupDisplay().getMetaKeyword());
        displayDto.setDeliveryType(response.getGoodsGroupDisplay().getDeliveryType());
        displayDto.setGoodsNote1(response.getGoodsGroupDisplay().getGoodsNote1());
        displayDto.setGoodsNote2(response.getGoodsGroupDisplay().getGoodsNote2());
        displayDto.setGoodsNote3(response.getGoodsGroupDisplay().getGoodsNote3());
        displayDto.setGoodsNote4(response.getGoodsGroupDisplay().getGoodsNote4());
        displayDto.setGoodsNote5(response.getGoodsGroupDisplay().getGoodsNote5());
        displayDto.setGoodsNote6(response.getGoodsGroupDisplay().getGoodsNote6());
        displayDto.setGoodsNote7(response.getGoodsGroupDisplay().getGoodsNote7());
        displayDto.setGoodsNote8(response.getGoodsGroupDisplay().getGoodsNote8());
        displayDto.setGoodsNote9(response.getGoodsGroupDisplay().getGoodsNote9());
        displayDto.setGoodsNote10(response.getGoodsGroupDisplay().getGoodsNote10());
        displayDto.setOrderSetting1(response.getGoodsGroupDisplay().getOrderSetting1());
        displayDto.setOrderSetting2(response.getGoodsGroupDisplay().getOrderSetting2());
        displayDto.setOrderSetting3(response.getGoodsGroupDisplay().getOrderSetting3());
        displayDto.setOrderSetting4(response.getGoodsGroupDisplay().getOrderSetting4());
        displayDto.setOrderSetting5(response.getGoodsGroupDisplay().getOrderSetting5());
        displayDto.setOrderSetting6(response.getGoodsGroupDisplay().getOrderSetting6());
        displayDto.setOrderSetting7(response.getGoodsGroupDisplay().getOrderSetting7());
        displayDto.setOrderSetting8(response.getGoodsGroupDisplay().getOrderSetting8());
        displayDto.setOrderSetting9(response.getGoodsGroupDisplay().getOrderSetting9());
        displayDto.setOrderSetting10(response.getGoodsGroupDisplay().getOrderSetting10());
        displayDto.setRegistTime(conversionUtility.toTimestamp(response.getGoodsGroupDisplay().getRegistTime()));
        displayDto.setUpdateTime(conversionUtility.toTimestamp(response.getGoodsGroupDisplay().getUpdateTime()));

        return displayDto;
    }
}