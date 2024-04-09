package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
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
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
     * 文字型に変換
     *
     * @param stringList
     * @return
     */
    public List<Integer> toIntegerList(List<String> stringList) {
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
            if (goodsDetailsResponse.getNoveltyGoodsType() != null) {
                goodsDetailsDto.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                                  goodsDetailsResponse.getNoveltyGoodsType()
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
     * 商品詳細Dtoに変換
     *
     * @param goodsDetailsResponse 商品詳細レスポ
     * @return 商品詳細Dtoクラス
     */
    public GoodsDetailsDto productDetailGetByGoodCode(GoodsDetailsResponse goodsDetailsResponse) {

        if (goodsDetailsResponse == null) {
            return null;
        }
        GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

        goodsDetailsDto.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
        goodsDetailsDto.setGoodsGroupSeq(goodsDetailsResponse.getGoodsGroupSeq());

        return goodsDetailsDto;
    }

    /**
     * 商品グループ在庫表示エンティティDTOクラスに変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 商品グループ在庫表示エンティティDTO
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayResponse stockStatusDisplayResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(stockStatusDisplayResponse)) {
            return null;
        }

        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(stockStatusDisplayResponse.getGoodsGroupSeq());
        stockStatusDisplayEntity.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                stockStatusDisplayResponse.getStockStatus()
                                                                               ));
        stockStatusDisplayEntity.setRegistTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getUpdateTime()));

        return stockStatusDisplayEntity;
    }

    /**
     * 商品グループ表示クラスに変換
     *
     * @param goodsGroupDisplayResponse 商品グループ検索条件
     * @return 商品グループ表示クラス
     */
    public GoodsGroupDisplayEntity toGoodsGroupDisplayEntity(GoodsGroupDisplayResponse goodsGroupDisplayResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(goodsGroupDisplayResponse)) {
            return null;
        }

        GoodsGroupDisplayEntity goodsGroupDisplayEntity = new GoodsGroupDisplayEntity();

        goodsGroupDisplayEntity.setGoodsGroupSeq(goodsGroupDisplayResponse.getGoodsGroupSeq());
        goodsGroupDisplayEntity.setInformationIconPC(goodsGroupDisplayResponse.getInformationIcon());
        goodsGroupDisplayEntity.setSearchKeyword(goodsGroupDisplayResponse.getSearchKeyword());
        goodsGroupDisplayEntity.setSearchKeywordEm(goodsGroupDisplayResponse.getSearchKeywordEmUc());
        goodsGroupDisplayEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                    goodsGroupDisplayResponse.getUnitManagementFlag()
                                                                                   ));
        goodsGroupDisplayEntity.setUnitTitle1(goodsGroupDisplayResponse.getUnitTitle1());
        goodsGroupDisplayEntity.setUnitTitle2(goodsGroupDisplayResponse.getUnitTitle2());
        goodsGroupDisplayEntity.setMetaDescription(goodsGroupDisplayResponse.getMetaDescription());
        goodsGroupDisplayEntity.setMetaKeyword(goodsGroupDisplayResponse.getMetaKeyword());
        goodsGroupDisplayEntity.setDeliveryType(goodsGroupDisplayResponse.getDeliveryType());
        goodsGroupDisplayEntity.setGoodsNote1(goodsGroupDisplayResponse.getGoodsNote1());
        goodsGroupDisplayEntity.setGoodsNote2(goodsGroupDisplayResponse.getGoodsNote2());
        goodsGroupDisplayEntity.setGoodsNote3(goodsGroupDisplayResponse.getGoodsNote3());
        goodsGroupDisplayEntity.setGoodsNote4(goodsGroupDisplayResponse.getGoodsNote4());
        goodsGroupDisplayEntity.setGoodsNote5(goodsGroupDisplayResponse.getGoodsNote5());
        goodsGroupDisplayEntity.setGoodsNote6(goodsGroupDisplayResponse.getGoodsNote6());
        goodsGroupDisplayEntity.setGoodsNote7(goodsGroupDisplayResponse.getGoodsNote7());
        goodsGroupDisplayEntity.setGoodsNote8(goodsGroupDisplayResponse.getGoodsNote8());
        goodsGroupDisplayEntity.setGoodsNote9(goodsGroupDisplayResponse.getGoodsNote9());
        goodsGroupDisplayEntity.setGoodsNote10(goodsGroupDisplayResponse.getGoodsNote10());
        goodsGroupDisplayEntity.setOrderSetting1(goodsGroupDisplayResponse.getOrderSetting1());
        goodsGroupDisplayEntity.setOrderSetting2(goodsGroupDisplayResponse.getOrderSetting2());
        goodsGroupDisplayEntity.setOrderSetting3(goodsGroupDisplayResponse.getOrderSetting3());
        goodsGroupDisplayEntity.setOrderSetting4(goodsGroupDisplayResponse.getOrderSetting4());
        goodsGroupDisplayEntity.setOrderSetting5(goodsGroupDisplayResponse.getOrderSetting5());
        goodsGroupDisplayEntity.setOrderSetting6(goodsGroupDisplayResponse.getOrderSetting6());
        goodsGroupDisplayEntity.setOrderSetting7(goodsGroupDisplayResponse.getOrderSetting7());
        goodsGroupDisplayEntity.setOrderSetting8(goodsGroupDisplayResponse.getOrderSetting8());
        goodsGroupDisplayEntity.setOrderSetting9(goodsGroupDisplayResponse.getOrderSetting9());
        goodsGroupDisplayEntity.setOrderSetting10(goodsGroupDisplayResponse.getOrderSetting10());
        goodsGroupDisplayEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupDisplayResponse.getRegistTime()));
        goodsGroupDisplayEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupDisplayResponse.getUpdateTime()));

        return goodsGroupDisplayEntity;
    }

    /**
     * 在庫Dtoクラスに変換
     *
     * @param stockResponse 商品グループ検索条件
     * @return 在庫Dtoクラス
     */
    public StockDto toStockDto(StockResponse stockResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(stockResponse)) {
            return null;
        }

        StockDto stockDto = new StockDto();

        stockDto.setGoodsSeq(stockResponse.getGoodsSeq());
        stockDto.setShopSeq(1001);
        stockDto.setSalesPossibleStock(stockResponse.getSalesPossibleStock());
        stockDto.setRealStock(stockResponse.getRealStock());
        stockDto.setOrderReserveStock(stockResponse.getOrderReserveStock());
        stockDto.setRemainderFewStock(stockResponse.getRemainderFewStock());
        stockDto.setSupplementCount(stockResponse.getSupplementCount());
        stockDto.setOrderPointStock(stockResponse.getOrderPointStock());
        stockDto.setSafetyStock(stockResponse.getSafetyStock());
        stockDto.setRegistTime(conversionUtility.toTimestamp(stockResponse.getRegistTime()));
        stockDto.setUpdateTime(conversionUtility.toTimestamp(stockResponse.getUpdateTime()));

        return stockDto;
    }

    /**
     * 商品クラスに変換
     *
     * @param goodsSubResponse 商品クラス
     * @return 商品エンティティ
     */
    public GoodsEntity toGoodsEntity(GoodsSubResponse goodsSubResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(goodsSubResponse)) {
            return null;
        }

        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsSeq(goodsSubResponse.getGoodsSeq());
        goodsEntity.setGoodsGroupSeq(goodsSubResponse.getGoodsGroupSeq());
        goodsEntity.setGoodsCode(goodsSubResponse.getGoodsCode());
        goodsEntity.setJanCode(goodsSubResponse.getJanCode());
        if (goodsSubResponse.getSaleStatus() != null) {
            goodsEntity.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                      goodsSubResponse.getSaleStatus()
                                                                     ));
        }
        goodsEntity.setSaleStartTimePC(conversionUtility.toTimestamp(goodsSubResponse.getSaleStartTime()));
        goodsEntity.setSaleEndTimePC(conversionUtility.toTimestamp(goodsSubResponse.getSaleEndTime()));
        if (goodsSubResponse.getIndividualDeliveryType() != null) {
            goodsEntity.setIndividualDeliveryType(EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                                goodsSubResponse.getIndividualDeliveryType()
                                                                               ));
        }
        if (goodsSubResponse.getFreeDeliveryFlag() != null) {
            goodsEntity.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                          goodsSubResponse.getFreeDeliveryFlag()
                                                                         ));
        }
        if (goodsSubResponse.getUnitManagementFlag() != null) {
            goodsEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                            goodsSubResponse.getUnitManagementFlag()
                                                                           ));
        }
        goodsEntity.setUnitValue1(goodsSubResponse.getUnitValue1());
        goodsEntity.setUnitValue2(goodsSubResponse.getUnitValue2());
        if (goodsSubResponse.getStockManagementFlag() != null) {
            goodsEntity.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                             goodsSubResponse.getStockManagementFlag()
                                                                            ));
        }
        goodsEntity.setPurchasedMax(goodsSubResponse.getPurchasedMax());
        goodsEntity.setOrderDisplay(goodsSubResponse.getOrderDisplay());
        goodsEntity.setVersionNo(goodsSubResponse.getVersionNo());
        goodsEntity.setShopSeq(1001);
        goodsEntity.setRegistTime(conversionUtility.toTimestamp(goodsSubResponse.getRegistTime()));
        goodsEntity.setUpdateTime(conversionUtility.toTimestamp(goodsSubResponse.getUpdateTime()));

        return goodsEntity;
    }

    /**
     * 商品DTOリストに変換
     *
     * @param goodsRequestList 商品レスポンスクラスリスト
     * @return 商品DTOリスト
     */
    public List<GoodsDto> toGoodsDtoList(List<GoodsResponse> goodsRequestList) {
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsRequestList.forEach(item -> {
            GoodsDto goodsDto = new GoodsDto();
            GoodsEntity goodsEntity = toGoodsEntity(item.getGoodsSub());
            StockDto stockDto = toStockDto(item.getStock());
            goodsDto.setGoodsEntity(goodsEntity);
            goodsDto.setStockDto(stockDto);
            goodsDto.setDeleteFlg(item.getDeleteFlg());
            goodsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, item.getStockStatus()));
            goodsDtoList.add(goodsDto);
        });

        return goodsDtoList;
    }

    /**
     * カテゴリ登録商品クラスリストに変換
     *
     * @param categoryGoodsResponseList 商品レスポンスクラスリスト
     * @return カテゴリ登録商品クラスリスト
     */
    public List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsResponse> categoryGoodsResponseList) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        categoryGoodsResponseList.forEach(item -> {
            CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();
            categoryGoodsEntity.setCategorySeq(item.getCategorySeq());
            categoryGoodsEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            categoryGoodsEntity.setOrderDisplay(item.getManualOrderDisplay());
            categoryGoodsEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            categoryGoodsEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
            categoryGoodsEntityList.add(categoryGoodsEntity);
        });

        return categoryGoodsEntityList;
    }

    /**
     * 商品グループ一覧DTOクラスに変換<br/>
     *
     * @param productListResponse 商品グループ一覧レスポンス
     * @return 商品グループ一覧DTO
     */
    public List<GoodsGroupDto> toGoodsGroupDtos(ProductListResponse productListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(productListResponse)) {
            return null;
        }

        List<GoodsGroupDto> goodsGroupDtoList = new ArrayList<>();
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        if (!CollectionUtils.isEmpty(productListResponse.getGoodsGroupList())) {

            for (GoodsGroupResponse goodsGroupResponse : productListResponse.getGoodsGroupList()) {
                GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

                if (goodsGroupResponse.getGoodsGroupSubResponse() != null) {
                    GoodsGroupSubResponse goodsGroupSubResponse = goodsGroupResponse.getGoodsGroupSubResponse();
                    GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

                    goodsGroupEntity.setGoodsGroupSeq(goodsGroupSubResponse.getGoodsGroupSeq());
                    goodsGroupEntity.setGoodsGroupCode(goodsGroupSubResponse.getGoodsGroupCode());
                    goodsGroupEntity.setGoodsGroupName(goodsGroupSubResponse.getGoodsGroupName());
                    goodsGroupEntity.setGoodsPrice(goodsGroupSubResponse.getGoodsPrice());
                    goodsGroupEntity.setGoodsPriceInTax(goodsGroupSubResponse.getGoodsPriceInTax());
                    goodsGroupEntity.setWhatsnewDate(
                                    conversionUtility.toTimestamp(goodsGroupSubResponse.getWhatsnewDate()));
                    if (goodsGroupSubResponse.getGoodsOpenStatus() != null) {
                        goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                            goodsGroupSubResponse.getGoodsOpenStatus()
                                                                                           ));
                    }
                    goodsGroupEntity.setOpenStartTimePC(
                                    conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenStartTime()));
                    goodsGroupEntity.setOpenEndTimePC(
                                    conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenEndTime()));
                    if (goodsGroupSubResponse.getGoodsTaxType() != null) {
                        goodsGroupEntity.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                                       goodsGroupSubResponse.getGoodsTaxType()
                                                                                      ));
                    }
                    goodsGroupEntity.setTaxRate(goodsGroupSubResponse.getTaxRate());
                    if (goodsGroupSubResponse.getAlcoholFlag() != null) {
                        goodsGroupEntity.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                                      goodsGroupSubResponse.getAlcoholFlag()
                                                                                     ));
                    }
                    if (goodsGroupSubResponse.getSnsLinkFlag() != null) {
                        goodsGroupEntity.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                                      goodsGroupSubResponse.getSnsLinkFlag()
                                                                                     ));
                    }

                    goodsGroupEntity.setVersionNo(goodsGroupSubResponse.getVersionNo());
                    goodsGroupEntity.setRegistTime(
                                    conversionUtility.toTimestamp(goodsGroupSubResponse.getRegistTime()));
                    goodsGroupEntity.setUpdateTime(
                                    conversionUtility.toTimestamp(goodsGroupSubResponse.getUpdateTime()));

                    goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
                }

                if (goodsGroupResponse.getBatchUpdateStockStatus() != null) {
                    StockStatusDisplayEntity stockStatusDisplayEntity =
                                    toStockStatusDisplayEntity(goodsGroupResponse.getBatchUpdateStockStatus());
                    goodsGroupDto.setBatchUpdateStockStatus(stockStatusDisplayEntity);
                }
                if (goodsGroupResponse.getRealTimeStockStatus() != null) {
                    StockStatusDisplayEntity stockStatusDisplayEntity =
                                    toStockStatusDisplayEntity(goodsGroupResponse.getRealTimeStockStatus());
                    goodsGroupDto.setRealTimeStockStatus(stockStatusDisplayEntity);
                }
                if (goodsGroupResponse.getGoodsGroupDisplay() != null) {
                    GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                                    toGoodsGroupDisplayEntity(goodsGroupResponse.getGoodsGroupDisplay());
                    goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
                }
                if (goodsGroupResponse.getGoodsGroupImageResponseList() != null
                    && goodsGroupResponse.getGoodsGroupImageResponseList().size() > 0) {
                    List<GoodsGroupImageEntity> goodsGroupImageEntityList =
                                    toGoodsGroupImageEntityList(goodsGroupResponse.getGoodsGroupImageResponseList());
                    goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);
                }
                if (goodsGroupResponse.getGoodsResponseList() != null
                    && goodsGroupResponse.getGoodsResponseList().size() > 0) {
                    List<GoodsDto> goodsDtoList = toGoodsDtoList(goodsGroupResponse.getGoodsResponseList());
                    goodsGroupDto.setGoodsDtoList(goodsDtoList);
                }
                if (goodsGroupResponse.getCategoryGoodsResponseList() != null
                    && goodsGroupResponse.getCategoryGoodsResponseList().size() > 0) {
                    List<CategoryGoodsEntity> categoryGoodsEntityList =
                                    toCategoryGoodsEntityList(goodsGroupResponse.getCategoryGoodsResponseList());
                    goodsGroupDto.setCategoryGoodsEntityList(categoryGoodsEntityList);
                }
                if (goodsGroupResponse.getGoodsInformationIconDetailsResponseList() != null
                    && goodsGroupResponse.getGoodsInformationIconDetailsResponseList().size() > 0) {
                    List<GoodsInformationIconDetailsDto> categoryGoodsEntityList = toGoodsInformationIconDetailsDtoList(
                                    goodsGroupResponse.getGoodsInformationIconDetailsResponseList());
                    goodsGroupDto.setGoodsInformationIconDetailsDtoList(categoryGoodsEntityList);
                }
                goodsGroupDto.setTaxRate(goodsGroupResponse.getTaxRate());

                goodsGroupDtoList.add(goodsGroupDto);
            }
        }

        return goodsGroupDtoList;
    }

}