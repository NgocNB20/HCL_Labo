package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品アダプタークラスHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ProductAdapterHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public ProductAdapterHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 商品詳細へDtoリスト
     *
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @return 商品詳細へDtoリスト
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
            if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
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
            if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsIconList())) {
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
            return null;
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
            return null;
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
}