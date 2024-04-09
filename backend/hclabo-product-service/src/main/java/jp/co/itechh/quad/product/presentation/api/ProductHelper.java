package jp.co.itechh.quad.product.presentation.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.base.service.AbstractService;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupImageDao;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.csv.ProductCsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsUnitDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.module.AccessSearchKeywordLoggingModule;
import jp.co.itechh.quad.core.utility.CalculatePriceUtility;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsRequest;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsCodeListResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsImageItemRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsRelationRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSearchResultForOrderRegistResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.OptionContent;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvDownloadGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockRequest;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayRequest;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.MapUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品 Helper
 */
@Component
public class ProductHelper extends AbstractService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductHelper.class);

    /**
     * 在庫切れメッセージ
     */
    protected static final String NO_STOCK_MESSAGE = " [在庫なし]";

    /** 検索キーワードの最大文字数（超過部分は切り捨てます） */
    protected static final int KEYWORD_MAX_LENGTH = 100;

    /** 配列工場ユーティリティクラス */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /**
     * DateUtility
     */
    private final DateUtility dateUtility;

    /**
     * ConversionUtility
     */
    private final ConversionUtility conversionUtility;

    /**
     * 金額計算のUtilityクラス
     */
    private final CalculatePriceUtility calculatePriceUtility;

    /** GoodsUtility */
    private final GoodsUtility goodsUtility;

    /** スペース */
    private static final String SPACE = "　";

    /**
     * コンストラクター
     *  @param arrayFactoryUtility   配列工場ユーティリティクラス
     * @param dateUtility           日付Utility
     * @param conversionUtility     変換ユーティリティクラス
     * @param goodsUtility          商品系ヘルパークラス
     * @param calculatePriceUtility 金額計算のUtilityクラス
     */
    public ProductHelper(ArrayFactoryUtility arrayFactoryUtility,
                         DateUtility dateUtility,
                         ConversionUtility conversionUtility,
                         GoodsUtility goodsUtility,
                         CalculatePriceUtility calculatePriceUtility) {
        this.arrayFactoryUtility = arrayFactoryUtility;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
        this.goodsUtility = goodsUtility;
        this.calculatePriceUtility = calculatePriceUtility;
    }

    /**
     * 商品グループ画像クラスに変換
     *
     * @param goodsGroupImageEntity 商品グループ画像
     * @return 商品グループ画像クラス
     */
    public GoodsGroupImageRequest toGoodsGroupImageRequest(GoodsGroupImageEntity goodsGroupImageEntity) {
        GoodsGroupImageRequest goodsGroupImageRequest = new GoodsGroupImageRequest();

        goodsGroupImageRequest.setGoodsGroupSeq(goodsGroupImageEntity.getGoodsGroupSeq());
        goodsGroupImageRequest.setImageTypeVersionNo(goodsGroupImageEntity.getImageTypeVersionNo());
        goodsGroupImageRequest.setImageFileName(goodsGroupImageEntity.getImageFileName());
        goodsGroupImageRequest.setRegistTime(dateUtility.convertDateToTimestamp(goodsGroupImageEntity.getRegistTime()));
        goodsGroupImageRequest.setUpdateTime(dateUtility.convertDateToTimestamp(goodsGroupImageEntity.getUpdateTime()));

        return goodsGroupImageRequest;
    }

    /**
     * レスポンスに変換
     *
     * @param stockDto 在庫DTO
     * @return 商品グループ検索条件
     */
    public StockResponse toStockResponse(StockDto stockDto) {
        StockResponse stockResponse = new StockResponse();

        stockResponse.setGoodsSeq(stockDto.getGoodsSeq());
        stockResponse.setSalesPossibleStock(stockDto.getSalesPossibleStock());
        stockResponse.setRealStock(stockDto.getRealStock());
        stockResponse.setOrderReserveStock(stockDto.getOrderReserveStock());
        stockResponse.setRemainderFewStock(stockDto.getRemainderFewStock());
        stockResponse.setSupplementCount(stockDto.getSupplementCount());
        stockResponse.setOrderPointStock(stockDto.getOrderPointStock());
        stockResponse.setSafetyStock(stockDto.getSafetyStock());
        stockResponse.setRegistTime(stockDto.getRegistTime());
        stockResponse.setUpdateTime(stockDto.getUpdateTime());

        return stockResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param goodsEntity 商品エンティティ
     * @return 商品クラス
     */
    public GoodsSubResponse toGoodsSubResponse(GoodsEntity goodsEntity) {
        GoodsSubResponse goodsSubResponse = new GoodsSubResponse();

        goodsSubResponse.setGoodsSeq(goodsEntity.getGoodsSeq());
        goodsSubResponse.setGoodsGroupSeq(goodsEntity.getGoodsGroupSeq());
        goodsSubResponse.setGoodsCode(goodsEntity.getGoodsCode());
        goodsSubResponse.setJanCode(goodsEntity.getJanCode());
        goodsSubResponse.setSaleStatus(EnumTypeUtil.getValue(goodsEntity.getSaleStatusPC()));
        goodsSubResponse.setSaleStartTime(goodsEntity.getSaleStartTimePC());
        goodsSubResponse.setSaleEndTime(goodsEntity.getSaleEndTimePC());
        goodsSubResponse.setIndividualDeliveryType(EnumTypeUtil.getValue(goodsEntity.getIndividualDeliveryType()));
        goodsSubResponse.setFreeDeliveryFlag(EnumTypeUtil.getValue(goodsEntity.getFreeDeliveryFlag()));
        goodsSubResponse.setUnitManagementFlag(EnumTypeUtil.getValue(goodsEntity.getUnitManagementFlag()));
        goodsSubResponse.setUnitValue1(goodsEntity.getUnitValue1());
        goodsSubResponse.setUnitValue2(goodsEntity.getUnitValue2());
        goodsSubResponse.setStockManagementFlag(EnumTypeUtil.getValue(goodsEntity.getStockManagementFlag()));
        goodsSubResponse.setPurchasedMax(goodsEntity.getPurchasedMax());
        goodsSubResponse.setOrderDisplay(goodsEntity.getOrderDisplay());
        goodsSubResponse.setVersionNo(goodsEntity.getVersionNo());
        goodsSubResponse.setRegistTime(goodsEntity.getRegistTime());
        goodsSubResponse.setUpdateTime(goodsEntity.getUpdateTime());

        return goodsSubResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @return 商品詳細レスポンスクラス
     */
    public GoodsGroupSubResponse toGoodsGroupResponse(GoodsGroupEntity goodsGroupEntity) {

        GoodsGroupSubResponse goodsGroupResponse = new GoodsGroupSubResponse();
        goodsGroupResponse.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
        goodsGroupResponse.setGoodsGroupCode(goodsGroupEntity.getGoodsGroupCode());
        goodsGroupResponse.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
        goodsGroupResponse.setGoodsPrice(goodsGroupEntity.getGoodsPrice());
        goodsGroupResponse.setGoodsPriceInTax(
                        calculatePriceUtility.getTaxIncludedPrice(goodsGroupEntity.getGoodsPrice(),
                                                                  goodsGroupEntity.getTaxRate()
                                                                 ));
        goodsGroupResponse.setWhatsnewDate(goodsGroupEntity.getWhatsnewDate());
        goodsGroupResponse.setGoodsOpenStatus(EnumTypeUtil.getValue(goodsGroupEntity.getGoodsOpenStatusPC()));
        goodsGroupResponse.setOpenStartTime(goodsGroupEntity.getOpenStartTimePC());
        goodsGroupResponse.setOpenEndTime(goodsGroupEntity.getOpenEndTimePC());
        goodsGroupResponse.setGoodsTaxType(EnumTypeUtil.getValue(goodsGroupEntity.getGoodsTaxType()));
        goodsGroupResponse.setTaxRate(goodsGroupEntity.getTaxRate());
        goodsGroupResponse.setAlcoholFlag(EnumTypeUtil.getValue(goodsGroupEntity.getAlcoholFlag()));
        goodsGroupResponse.setNoveltyGoodsType(EnumTypeUtil.getValue(goodsGroupEntity.getNoveltyGoodsType()));
        goodsGroupResponse.setSnsLinkFlag(EnumTypeUtil.getValue(goodsGroupEntity.getSnsLinkFlag()));
        goodsGroupResponse.setVersionNo(goodsGroupEntity.getVersionNo());
        goodsGroupResponse.setRegistTime(goodsGroupEntity.getRegistTime());
        goodsGroupResponse.setUpdateTime(goodsGroupEntity.getUpdateTime());

        return goodsGroupResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param stockStatusDisplayEntity 在庫状態表示
     * @param frontDisplayStockStatus  フロント表示在庫状態　※プレビュー日時を加味して判定したステータス
     * @return 商品グループ在庫表示クラス
     */
    public StockStatusDisplayResponse toStockStatusDisplayResponse(StockStatusDisplayEntity stockStatusDisplayEntity,
                                                                   HTypeStockStatusType frontDisplayStockStatus) {

        StockStatusDisplayResponse stockStatusDisplayResponse = new StockStatusDisplayResponse();

        stockStatusDisplayResponse.setGoodsGroupSeq(stockStatusDisplayEntity.getGoodsGroupSeq());
        stockStatusDisplayResponse.setStockStatus(EnumTypeUtil.getValue(stockStatusDisplayEntity.getStockStatusPc()));
        stockStatusDisplayResponse.setFrontDisplayStockStatus(EnumTypeUtil.getValue(frontDisplayStockStatus));
        stockStatusDisplayResponse.setRegistTime(stockStatusDisplayEntity.getRegistTime());
        stockStatusDisplayResponse.setUpdateTime(stockStatusDisplayEntity.getUpdateTime());

        return stockStatusDisplayResponse;
    }

    /**
     * レスポンスに変換<br/>
     *
     * @param goodsSearchResultDtoList 商品検索結果Dtoリスト
     * @return 商品詳細一覧取得レスポンス
     */
    public ProductItemListResponse toProductItemListResponse(List<GoodsSearchResultDto> goodsSearchResultDtoList)
                    throws Exception {

        ProductItemListResponse productItemListResponse = new ProductItemListResponse();

        List<GoodsDetailsResponse> goodsDetailsResponseList = new ArrayList<>();
        for (GoodsSearchResultDto goodsSearchResultDto : goodsSearchResultDtoList) {
            GoodsDetailsResponse goodsDetailsResponse = new GoodsDetailsResponse();

            goodsDetailsResponse.setGoodsSeq(goodsSearchResultDto.getGoodsSeq());
            goodsDetailsResponse.setGoodsGroupSeq(goodsSearchResultDto.getGoodsGroupSeq());
            goodsDetailsResponse.setGoodsGroupCode(goodsSearchResultDto.getGoodsGroupCode());
            goodsDetailsResponse.setGoodsCode(goodsSearchResultDto.getGoodsCode());
            goodsDetailsResponse.setGoodsGroupName(goodsSearchResultDto.getGoodsGroupName());
            goodsDetailsResponse.setUnitValue1(goodsSearchResultDto.getUnitValue1());
            goodsDetailsResponse.setUnitValue2(goodsSearchResultDto.getUnitValue2());
            goodsDetailsResponse.setGoodsOpenStatus(EnumTypeUtil.getValue(goodsSearchResultDto.getGoodsOpenStatusPC()));
            goodsDetailsResponse.setOpenStartTime(goodsSearchResultDto.getOpenStartTimePC());
            goodsDetailsResponse.setOpenEndTime(goodsSearchResultDto.getOpenEndTimePC());
            goodsDetailsResponse.setSaleStatus(EnumTypeUtil.getValue(goodsSearchResultDto.getSaleStatusPC()));
            goodsDetailsResponse.setSaleStartTime(goodsSearchResultDto.getSaleStartTimePC());
            goodsDetailsResponse.setSaleEndTime(goodsSearchResultDto.getSaleEndTimePC());
            goodsDetailsResponse.setGoodsPrice(goodsSearchResultDto.getGoodsPrice());
            goodsDetailsResponse.setStockManagementFlag(
                            EnumTypeUtil.getValue(goodsSearchResultDto.getStockmanagementflag()));
            goodsDetailsResponse.setSalesPossibleStock(goodsSearchResultDto.getSalesPossibleStock());
            goodsDetailsResponse.setRealStock(goodsSearchResultDto.getRealStock());
            goodsDetailsResponse.setIndividualDeliveryType(
                            EnumTypeUtil.getValue(goodsSearchResultDto.getIndividualDeliveryType()));
            if (ObjectUtils.isNotEmpty(goodsSearchResultDto.getGoodsTag())) {
                goodsDetailsResponse.setGoodsTag(arrayFactoryUtility.arrayToList(goodsSearchResultDto.getGoodsTag()));
            }
            goodsDetailsResponse.setFrontDisplay(EnumTypeUtil.getValue(goodsSearchResultDto.getFrontDisplay()));

            goodsDetailsResponseList.add(goodsDetailsResponse);
        }
        productItemListResponse.setGoodsDetailsList(goodsDetailsResponseList);

        return productItemListResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param goodsGroupDisplayEntity 商品グループ表示DTO
     * @return 商品グループ検索条件
     */
    public GoodsGroupDisplayResponse toGoodsGroupDisplayResponse(GoodsGroupDisplayEntity goodsGroupDisplayEntity)
                    throws Exception {
        GoodsGroupDisplayResponse goodsGroupDisplayResponse = new GoodsGroupDisplayResponse();

        goodsGroupDisplayResponse.setGoodsGroupSeq(goodsGroupDisplayEntity.getGoodsGroupSeq());
        if (ObjectUtils.isNotEmpty(goodsGroupDisplayEntity.getGoodsTag())) {
            goodsGroupDisplayResponse.setGoodsTag(
                            arrayFactoryUtility.arrayToList(goodsGroupDisplayEntity.getGoodsTag()));
        }
        goodsGroupDisplayResponse.setInformationIcon(goodsGroupDisplayEntity.getInformationIconPC());
        goodsGroupDisplayResponse.setSearchKeyword(goodsGroupDisplayEntity.getSearchKeyword());
        goodsGroupDisplayResponse.setSearchKeywordEmUc(goodsGroupDisplayEntity.getSearchKeywordEmUc());
        goodsGroupDisplayResponse.settingKeywords(goodsGroupDisplayEntity.getSearchSettingKeywordsEmUc());
        goodsGroupDisplayResponse.setUnitManagementFlag(
                        EnumTypeUtil.getValue(goodsGroupDisplayEntity.getUnitManagementFlag()));
        goodsGroupDisplayResponse.setUnitTitle1(goodsGroupDisplayEntity.getUnitTitle1());
        goodsGroupDisplayResponse.setUnitTitle2(goodsGroupDisplayEntity.getUnitTitle2());
        goodsGroupDisplayResponse.setMetaDescription(goodsGroupDisplayEntity.getMetaDescription());
        goodsGroupDisplayResponse.setMetaKeyword(goodsGroupDisplayEntity.getMetaKeyword());
        goodsGroupDisplayResponse.setDeliveryType(goodsGroupDisplayEntity.getDeliveryType());
        goodsGroupDisplayResponse.setGoodsNote1(goodsGroupDisplayEntity.getGoodsNote1());
        goodsGroupDisplayResponse.setGoodsNote2(goodsGroupDisplayEntity.getGoodsNote2());
        goodsGroupDisplayResponse.setGoodsNote3(goodsGroupDisplayEntity.getGoodsNote3());
        goodsGroupDisplayResponse.setGoodsNote4(goodsGroupDisplayEntity.getGoodsNote4());
        goodsGroupDisplayResponse.setGoodsNote5(goodsGroupDisplayEntity.getGoodsNote5());
        goodsGroupDisplayResponse.setGoodsNote6(goodsGroupDisplayEntity.getGoodsNote6());
        goodsGroupDisplayResponse.setGoodsNote7(goodsGroupDisplayEntity.getGoodsNote7());
        goodsGroupDisplayResponse.setGoodsNote8(goodsGroupDisplayEntity.getGoodsNote8());
        goodsGroupDisplayResponse.setGoodsNote9(goodsGroupDisplayEntity.getGoodsNote9());
        goodsGroupDisplayResponse.setGoodsNote10(goodsGroupDisplayEntity.getGoodsNote10());
        goodsGroupDisplayResponse.setOrderSetting1(goodsGroupDisplayEntity.getOrderSetting1());
        goodsGroupDisplayResponse.setOrderSetting2(goodsGroupDisplayEntity.getOrderSetting2());
        goodsGroupDisplayResponse.setOrderSetting3(goodsGroupDisplayEntity.getOrderSetting3());
        goodsGroupDisplayResponse.setOrderSetting4(goodsGroupDisplayEntity.getOrderSetting4());
        goodsGroupDisplayResponse.setOrderSetting5(goodsGroupDisplayEntity.getOrderSetting5());
        goodsGroupDisplayResponse.setOrderSetting6(goodsGroupDisplayEntity.getOrderSetting6());
        goodsGroupDisplayResponse.setOrderSetting7(goodsGroupDisplayEntity.getOrderSetting7());
        goodsGroupDisplayResponse.setOrderSetting8(goodsGroupDisplayEntity.getOrderSetting8());
        goodsGroupDisplayResponse.setOrderSetting9(goodsGroupDisplayEntity.getOrderSetting9());
        goodsGroupDisplayResponse.setOrderSetting10(goodsGroupDisplayEntity.getOrderSetting10());
        goodsGroupDisplayResponse.setPopularityCount(goodsGroupDisplayEntity.getPopularityCount());
        goodsGroupDisplayResponse.setRegistTime(goodsGroupDisplayEntity.getRegistTime());
        goodsGroupDisplayResponse.setUpdateTime(goodsGroupDisplayEntity.getUpdateTime());

        return goodsGroupDisplayResponse;
    }

    /**
     * 商品クラスに変換
     *
     * @param goodsSubRequest 商品クラス
     * @param shopSeq          ショップSEQ
     * @return 商品エンティティ
     */
    public GoodsEntity toGoodsEntity(GoodsSubRequest goodsSubRequest, Integer shopSeq) {
        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsSeq(goodsSubRequest.getGoodsSeq());
        goodsEntity.setGoodsGroupSeq(goodsSubRequest.getGoodsGroupSeq());
        goodsEntity.setGoodsCode(goodsSubRequest.getGoodsCode());
        goodsEntity.setJanCode(goodsSubRequest.getJanCode());
        goodsEntity.setSaleStatusPC(
                        EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class, goodsSubRequest.getSaleStatus()));
        goodsEntity.setSaleStartTimePC(dateUtility.convertDateToTimestamp(goodsSubRequest.getSaleStartTime()));
        goodsEntity.setSaleEndTimePC(dateUtility.convertDateToTimestamp(goodsSubRequest.getSaleEndTime()));
        goodsEntity.setIndividualDeliveryType(EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                            goodsSubRequest.getIndividualDeliveryType()
                                                                           ));
        goodsEntity.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                      goodsSubRequest.getFreeDeliveryFlag()
                                                                     ));
        goodsEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                        goodsSubRequest.getUnitManagementFlag()
                                                                       ));
        goodsEntity.setUnitValue1(goodsSubRequest.getUnitValue1());
        goodsEntity.setUnitValue2(goodsSubRequest.getUnitValue2());
        goodsEntity.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                         goodsSubRequest.getStockManagementFlag()
                                                                        ));
        goodsEntity.setPurchasedMax(goodsSubRequest.getPurchasedMax());
        goodsEntity.setOrderDisplay(goodsSubRequest.getOrderDisplay());
        goodsEntity.setVersionNo(goodsSubRequest.getVersionNo());
        goodsEntity.setShopSeq(shopSeq);
        goodsEntity.setRegistTime(dateUtility.convertDateToTimestamp(goodsSubRequest.getRegistTime()));
        goodsEntity.setUpdateTime(dateUtility.convertDateToTimestamp(goodsSubRequest.getUpdateTime()));

        return goodsEntity;
    }

    /**
     * 商品グループクラスに変換
     *
     * @param goodsGroupResponse 商品詳細レスポンスクラス
     * @param shopSeq            ショップSEQ
     * @return 商品グループクラス
     */
    public GoodsGroupEntity toGoodsGroupEntity(GoodsGroupRegistUpdateRequest goodsGroupResponse, Integer shopSeq) {
        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(goodsGroupResponse.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(goodsGroupResponse.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(goodsGroupResponse.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(goodsGroupResponse.getGoodsPrice());
        goodsGroupEntity.setWhatsnewDate(dateUtility.convertDateToTimestamp(goodsGroupResponse.getWhatsnewDate()));
        goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                            goodsGroupResponse.getGoodsOpenStatus()
                                                                           ));
        goodsGroupEntity.setOpenStartTimePC(dateUtility.convertDateToTimestamp(goodsGroupResponse.getOpenStartTime()));
        goodsGroupEntity.setOpenEndTimePC(dateUtility.convertDateToTimestamp(goodsGroupResponse.getOpenEndTime()));
        goodsGroupEntity.setGoodsTaxType(
                        EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class, goodsGroupResponse.getGoodsTaxType()));
        goodsGroupEntity.setTaxRate(goodsGroupResponse.getTaxRate());
        goodsGroupEntity.setAlcoholFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, goodsGroupResponse.getAlcoholFlag()));
        goodsGroupEntity.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                           goodsGroupResponse.getNoveltyGoodsType()
                                                                          ));
        goodsGroupEntity.setSnsLinkFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, goodsGroupResponse.getSnsLinkFlag()));
        goodsGroupEntity.setVersionNo(goodsGroupResponse.getVersionNo());
        goodsGroupEntity.setRegistTime(dateUtility.convertDateToTimestamp(goodsGroupResponse.getRegistTime()));
        goodsGroupEntity.setUpdateTime(dateUtility.convertDateToTimestamp(goodsGroupResponse.getUpdateTime()));
        goodsGroupEntity.setShopSeq(shopSeq);

        return goodsGroupEntity;
    }

    /**
     * 商品グループ画像に変換
     *
     * @param goodsGroupImageRequest 商品グループ画像クラス
     * @return 商品グループ画像
     */
    public GoodsGroupImageEntity toGoodsGroupImageEntity(GoodsGroupImageRequest goodsGroupImageRequest) {
        GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

        goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupImageRequest.getGoodsGroupSeq());
        goodsGroupImageEntity.setImageTypeVersionNo(goodsGroupImageRequest.getImageTypeVersionNo());
        goodsGroupImageEntity.setImageFileName(goodsGroupImageRequest.getImageFileName());
        goodsGroupImageEntity.setRegistTime(dateUtility.convertDateToTimestamp(goodsGroupImageRequest.getRegistTime()));
        goodsGroupImageEntity.setUpdateTime(dateUtility.convertDateToTimestamp(goodsGroupImageRequest.getUpdateTime()));

        return goodsGroupImageEntity;
    }

    /**
     * 商品グループ表示クラスに変換
     *
     * @param goodsGroupDisplayRequest 商品グループ検索条件
     * @return 商品グループ表示クラス
     */
    public GoodsGroupDisplayEntity toGoodsGroupDisplayEntity(GoodsGroupDisplayRequest goodsGroupDisplayRequest)
                    throws Exception {
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = new GoodsGroupDisplayEntity();

        goodsGroupDisplayEntity.setGoodsGroupSeq(goodsGroupDisplayRequest.getGoodsGroupSeq());
        goodsGroupDisplayEntity.setInformationIconPC(goodsGroupDisplayRequest.getInformationIcon());
        if (!ListUtils.isEmpty(goodsGroupDisplayRequest.getGoodsTag())) {
            goodsGroupDisplayEntity.setGoodsTag(
                            arrayFactoryUtility.createTextArrayFromList(goodsGroupDisplayRequest.getGoodsTag()));
        }
        goodsGroupDisplayEntity.setSearchKeyword(goodsGroupDisplayRequest.getSearchKeyword());
        goodsGroupDisplayEntity.setSearchKeywordEmUc(goodsGroupDisplayRequest.getSearchKeywordEm());
        goodsGroupDisplayEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                    goodsGroupDisplayRequest.getUnitManagementFlag()
                                                                                   ));
        goodsGroupDisplayEntity.setUnitTitle1(goodsGroupDisplayRequest.getUnitTitle1());
        goodsGroupDisplayEntity.setUnitTitle2(goodsGroupDisplayRequest.getUnitTitle2());
        goodsGroupDisplayEntity.setMetaDescription(goodsGroupDisplayRequest.getMetaDescription());
        goodsGroupDisplayEntity.setMetaKeyword(goodsGroupDisplayRequest.getMetaKeyword());
        goodsGroupDisplayEntity.setDeliveryType(goodsGroupDisplayRequest.getDeliveryType());
        goodsGroupDisplayEntity.setGoodsNote1(goodsGroupDisplayRequest.getGoodsNote1());
        goodsGroupDisplayEntity.setGoodsNote2(goodsGroupDisplayRequest.getGoodsNote2());
        goodsGroupDisplayEntity.setGoodsNote3(goodsGroupDisplayRequest.getGoodsNote3());
        goodsGroupDisplayEntity.setGoodsNote4(goodsGroupDisplayRequest.getGoodsNote4());
        goodsGroupDisplayEntity.setGoodsNote5(goodsGroupDisplayRequest.getGoodsNote5());
        goodsGroupDisplayEntity.setGoodsNote6(goodsGroupDisplayRequest.getGoodsNote6());
        goodsGroupDisplayEntity.setGoodsNote7(goodsGroupDisplayRequest.getGoodsNote7());
        goodsGroupDisplayEntity.setGoodsNote8(goodsGroupDisplayRequest.getGoodsNote8());
        goodsGroupDisplayEntity.setGoodsNote9(goodsGroupDisplayRequest.getGoodsNote9());
        goodsGroupDisplayEntity.setGoodsNote10(goodsGroupDisplayRequest.getGoodsNote10());
        goodsGroupDisplayEntity.setOrderSetting1(goodsGroupDisplayRequest.getOrderSetting1());
        goodsGroupDisplayEntity.setOrderSetting2(goodsGroupDisplayRequest.getOrderSetting2());
        goodsGroupDisplayEntity.setOrderSetting3(goodsGroupDisplayRequest.getOrderSetting3());
        goodsGroupDisplayEntity.setOrderSetting4(goodsGroupDisplayRequest.getOrderSetting4());
        goodsGroupDisplayEntity.setOrderSetting5(goodsGroupDisplayRequest.getOrderSetting5());
        goodsGroupDisplayEntity.setOrderSetting6(goodsGroupDisplayRequest.getOrderSetting6());
        goodsGroupDisplayEntity.setOrderSetting7(goodsGroupDisplayRequest.getOrderSetting7());
        goodsGroupDisplayEntity.setOrderSetting8(goodsGroupDisplayRequest.getOrderSetting8());
        goodsGroupDisplayEntity.setOrderSetting9(goodsGroupDisplayRequest.getOrderSetting9());
        goodsGroupDisplayEntity.setOrderSetting10(goodsGroupDisplayRequest.getOrderSetting10());
        goodsGroupDisplayEntity.setRegistTime(
                        dateUtility.convertDateToTimestamp(goodsGroupDisplayRequest.getRegistTime()));
        goodsGroupDisplayEntity.setUpdateTime(
                        dateUtility.convertDateToTimestamp(goodsGroupDisplayRequest.getUpdateTime()));

        return goodsGroupDisplayEntity;
    }

    /**
     * 在庫状態表示に変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 在庫状態表示
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayRequest stockStatusDisplayResponse) {
        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(stockStatusDisplayResponse.getGoodsGroupSeq());
        stockStatusDisplayEntity.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                stockStatusDisplayResponse.getStockStatus()
                                                                               ));
        stockStatusDisplayEntity.setRegistTime(
                        dateUtility.convertDateToTimestamp(stockStatusDisplayResponse.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(
                        dateUtility.convertDateToTimestamp(stockStatusDisplayResponse.getUpdateTime()));

        return stockStatusDisplayEntity;
    }

    /**
     * 検索条件の作成<br/>
     *
     * @param productItemListGetRequest 管理検索用商品一覧取得リクエスト
     * @return 商品Dao用検索条件Dto
     */
    public GoodsSearchForBackDaoConditionDto toGoodsSearchForBackDaoConditionDtoForSearch(ProductItemListGetRequest productItemListGetRequest) {

        GoodsSearchForBackDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsSearchForBackDaoConditionDto.class);

        /* 画面条件 */
        // キーワード
        if (productItemListGetRequest.getSearchRelationGoodsKeyword() != null) {
            String[] searchKeywordArray = productItemListGetRequest.getSearchRelationGoodsKeyword().split("[\\s|　]+");

            for (int i = 0; i < searchKeywordArray.length; i++) {
                switch (i) {
                    case 0:
                        conditionDto.setKeywordLikeCondition1(searchKeywordArray[i].trim());
                        break;
                    case 1:
                        conditionDto.setKeywordLikeCondition2(searchKeywordArray[i].trim());
                        break;
                    case 2:
                        conditionDto.setKeywordLikeCondition3(searchKeywordArray[i].trim());
                        break;
                    case 3:
                        conditionDto.setKeywordLikeCondition4(searchKeywordArray[i].trim());
                        break;
                    case 4:
                        conditionDto.setKeywordLikeCondition5(searchKeywordArray[i].trim());
                        break;
                    case 5:
                        conditionDto.setKeywordLikeCondition6(searchKeywordArray[i].trim());
                        break;
                    case 6:
                        conditionDto.setKeywordLikeCondition7(searchKeywordArray[i].trim());
                        break;
                    case 7:
                        conditionDto.setKeywordLikeCondition8(searchKeywordArray[i].trim());
                        break;
                    case 8:
                        conditionDto.setKeywordLikeCondition9(searchKeywordArray[i].trim());
                        break;
                    case 9:
                        conditionDto.setKeywordLikeCondition10(searchKeywordArray[i].trim());
                        break;
                    default:
                        break;
                }
            }
        }

        // カテゴリID 検索には使用しない
        conditionDto.setCategoryId(productItemListGetRequest.getSearchCategoryId());

        // 商品タグ
        if (productItemListGetRequest.getSearchGoodsTag() != null) {
            String[] searchGoodsTagArray = productItemListGetRequest.getSearchGoodsTag().split("[\\s|　]+");

            for (int i = 0; i < searchGoodsTagArray.length; i++) {
                switch (i) {
                    case 0:
                        conditionDto.setGoodsTagLikeCondition1(searchGoodsTagArray[i].trim());
                        break;
                    case 1:
                        conditionDto.setGoodsTagLikeCondition2(searchGoodsTagArray[i].trim());
                        break;
                    case 2:
                        conditionDto.setGoodsTagLikeCondition3(searchGoodsTagArray[i].trim());
                        break;
                    case 3:
                        conditionDto.setGoodsTagLikeCondition4(searchGoodsTagArray[i].trim());
                        break;
                    case 4:
                        conditionDto.setGoodsTagLikeCondition5(searchGoodsTagArray[i].trim());
                        break;
                    case 5:
                        conditionDto.setGoodsTagLikeCondition6(searchGoodsTagArray[i].trim());
                        break;
                    case 6:
                        conditionDto.setGoodsTagLikeCondition7(searchGoodsTagArray[i].trim());
                        break;
                    case 7:
                        conditionDto.setGoodsTagLikeCondition8(searchGoodsTagArray[i].trim());
                        break;
                    case 8:
                        conditionDto.setGoodsTagLikeCondition9(searchGoodsTagArray[i].trim());
                        break;
                    case 9:
                        conditionDto.setGoodsTagLikeCondition10(searchGoodsTagArray[i].trim());
                        break;
                    default:
                        break;
                }
            }
        }

        // 商品グループコード
        conditionDto.setGoodsGroupCode(productItemListGetRequest.getSearchGoodsGroupCode());

        // 商品コード
        conditionDto.setGoodsCode(productItemListGetRequest.getSearchGoodsCode());

        // JANコード
        conditionDto.setJanCode(productItemListGetRequest.getSearchJanCode());

        // 下限販売可能在庫数
        if (productItemListGetRequest.getSearchMinSalesPossibleStockCount() != null) {
            conditionDto.setMinSalesPossibleStock(
                            new BigDecimal(productItemListGetRequest.getSearchMinSalesPossibleStockCount()));
        } else {
            conditionDto.setMinSalesPossibleStock(null);
        }

        // 上限販売可能在庫数
        if (productItemListGetRequest.getSearchMaxSalesPossibleStockCount() != null) {
            conditionDto.setMaxSalesPossibleStock(
                            new BigDecimal(productItemListGetRequest.getSearchMaxSalesPossibleStockCount()));
        } else {
            conditionDto.setMaxSalesPossibleStock(null);
        }

        // 商品名
        conditionDto.setGoodsGroupName(productItemListGetRequest.getSearchGoodsGroupName());

        // サイト区分
        conditionDto.setSite(productItemListGetRequest.getSite());

        // 公開状態
        if (!ListUtils.isEmpty(productItemListGetRequest.getGoodsOpenStatusArray())) {
            conditionDto.setGoodsOpenStatusList(productItemListGetRequest.getGoodsOpenStatusArray());
        } else {
            conditionDto.setGoodsOpenStatusList(Arrays.asList(HTypeOpenDeleteStatus.OPEN.getValue(),
                                                              HTypeOpenDeleteStatus.NO_OPEN.getValue(),
                                                              HTypeOpenDeleteStatus.DELETED.getValue()
                                                             ));
        }

        // 販売状態
        if (!ListUtils.isEmpty(productItemListGetRequest.getGoodsSaleStatusArray())) {
            conditionDto.setSaleStatusList(productItemListGetRequest.getGoodsSaleStatusArray());
        } else {
            conditionDto.setSaleStatusList(
                            Arrays.asList(HTypeGoodsSaleStatus.SALE.getValue(), HTypeGoodsSaleStatus.NO_SALE.getValue(),
                                          HTypeGoodsSaleStatus.DELETED.getValue()
                                         ));
        }

        // 登録・更新日時
        String selectDate = productItemListGetRequest.getSelectRegistOrUpdate();
        if (selectDate != null) {
            if (selectDate.equals("0")) {
                conditionDto.setRegistTimeFrom(
                                conversionUtility.toTimeStamp(productItemListGetRequest.getSearchRegOrUpTimeFrom()));
                if (productItemListGetRequest.getSearchRegOrUpTimeTo() != null) {
                    conditionDto.setRegistTimeTo(dateUtility.getEndOfDate(
                                    conversionUtility.toTimeStamp(productItemListGetRequest.getSearchRegOrUpTimeTo())));
                }
            } else if (selectDate.equals("1")) {
                conditionDto.setUpdateTimeFrom(
                                conversionUtility.toTimeStamp(productItemListGetRequest.getSearchRegOrUpTimeFrom()));
                if (productItemListGetRequest.getSearchRegOrUpTimeTo() != null) {
                    conditionDto.setUpdateTimeTo(dateUtility.getEndOfDate(
                                    conversionUtility.toTimeStamp(productItemListGetRequest.getSearchRegOrUpTimeTo())));
                }
            }
        }
        conditionDto.setRelationGoodsSearchFlag(productItemListGetRequest.getRelationGoodsSearchFlag());

        // ノベルティ商品フラグ
        if (Boolean.TRUE.equals(productItemListGetRequest.getSearchNoveltyGoodsType())) {
            conditionDto.setNoveltyGoodsType(HTypeNoveltyGoodsType.NOVELTY_GOODS);
        } else {
            conditionDto.setNoveltyGoodsType(HTypeNoveltyGoodsType.NORMAL_GOODS);
        }

        // フロント表示
        if (!ListUtils.isEmpty(productItemListGetRequest.getFrontDisplayList())) {
            conditionDto.setFrontDisplayList(productItemListGetRequest.getFrontDisplayList());
        } else {
            conditionDto.setFrontDisplayList(Arrays.asList(HTypeFrontDisplayStatus.NO_OPEN.getValue(),
                                                           HTypeFrontDisplayStatus.OPEN.getValue()
                                                          ));
        }

        // フロント表示基準日時
        conditionDto.setFrontDisplayReferenceDate(
                        conversionUtility.toTimeStamp(productItemListGetRequest.getFrontDisplayReferenceDate()));

        // 検索用商品設定キーワード
        if (StringUtils.isNotEmpty(productItemListGetRequest.getSettingKeywords())) {
            ZenHanConversionUtility zenHanConversionUtility =
                            ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
            List<String> searchSettingKeywordList = new ArrayList<>();
            List<String> settingKeywordList = Arrays.asList(productItemListGetRequest.getSettingKeywords().split(" "));
            for (String searchSettingKeyword : settingKeywordList) {
                if (StringUtils.isNotEmpty(searchSettingKeyword)) {
                    searchSettingKeyword = zenHanConversionUtility.toZenkaku(searchSettingKeyword);
                    searchSettingKeyword = searchSettingKeyword.toUpperCase();
                    searchSettingKeywordList.add(searchSettingKeyword);
                }
            }
            conditionDto.setSearchSettingKeywordList(searchSettingKeywordList);
        }

        /** 複数番号検索 */
        if (CollectionUtils.isNotEmpty(productItemListGetRequest.getGoodsGroupCodeList())) {
            productItemListGetRequest.getGoodsGroupCodeList()
                                     .removeIf(goodsGroupCode -> StringUtils.isEmpty(goodsGroupCode));
            conditionDto.setGoodsGroupCodeList(productItemListGetRequest.getGoodsGroupCodeList());
        } else if (CollectionUtils.isNotEmpty(productItemListGetRequest.getGoodsCodeList())) {
            productItemListGetRequest.getGoodsCodeList().removeIf(goodsCode -> StringUtils.isEmpty(goodsCode));
            conditionDto.setGoodsCodeList(productItemListGetRequest.getGoodsCodeList());
        } else if (CollectionUtils.isNotEmpty(productItemListGetRequest.getJanCodeList())) {
            productItemListGetRequest.getJanCodeList().removeIf(janCode -> StringUtils.isEmpty(janCode));
            conditionDto.setJanCodeList(productItemListGetRequest.getJanCodeList());
        }

        /** カテゴリSEQリスト検索 */
        if (CollectionUtils.isNotEmpty(productItemListGetRequest.getCategoryIdList())) {
            conditionDto.setCategoryIdList(productItemListGetRequest.getCategoryIdList());
        }

        return conditionDto;
    }

    /**
     * 検索条件の作成<br/>
     *
     * @param productCsvDownloadGetRequest 商品CSVDLリクエスト
     * @return 商品Dao用検索条件Dto
     */
    public GoodsSearchForBackDaoConditionDto toGoodsSearchForBackDaoConditionDtoForSearch(ProductCsvDownloadGetRequest productCsvDownloadGetRequest) {

        GoodsSearchForBackDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsSearchForBackDaoConditionDto.class);

        /* 画面条件 */
        // キーワード
        if (productCsvDownloadGetRequest.getRelationGoodsKeyword() != null) {
            String[] searchKeywordArray = productCsvDownloadGetRequest.getRelationGoodsKeyword().split("[\\s|　]+");

            for (int i = 0; i < searchKeywordArray.length; i++) {
                switch (i) {
                    case 0:
                        conditionDto.setKeywordLikeCondition1(searchKeywordArray[i].trim());
                        break;
                    case 1:
                        conditionDto.setKeywordLikeCondition2(searchKeywordArray[i].trim());
                        break;
                    case 2:
                        conditionDto.setKeywordLikeCondition3(searchKeywordArray[i].trim());
                        break;
                    case 3:
                        conditionDto.setKeywordLikeCondition4(searchKeywordArray[i].trim());
                        break;
                    case 4:
                        conditionDto.setKeywordLikeCondition5(searchKeywordArray[i].trim());
                        break;
                    case 5:
                        conditionDto.setKeywordLikeCondition6(searchKeywordArray[i].trim());
                        break;
                    case 6:
                        conditionDto.setKeywordLikeCondition7(searchKeywordArray[i].trim());
                        break;
                    case 7:
                        conditionDto.setKeywordLikeCondition8(searchKeywordArray[i].trim());
                        break;
                    case 8:
                        conditionDto.setKeywordLikeCondition9(searchKeywordArray[i].trim());
                        break;
                    case 9:
                        conditionDto.setKeywordLikeCondition10(searchKeywordArray[i].trim());
                        break;
                    default:
                        break;
                }
            }
        }

        if (StringUtils.isNotEmpty(productCsvDownloadGetRequest.getGoodsTag())) {
            String[] searchGoodsTagArray = productCsvDownloadGetRequest.getGoodsTag().split("[\\s|　]+");

            for (int i = 0; i < searchGoodsTagArray.length; i++) {
                switch (i) {
                    case 0:
                        conditionDto.setGoodsTagLikeCondition1(searchGoodsTagArray[i].trim());
                        break;
                    case 1:
                        conditionDto.setGoodsTagLikeCondition2(searchGoodsTagArray[i].trim());
                        break;
                    case 2:
                        conditionDto.setGoodsTagLikeCondition3(searchGoodsTagArray[i].trim());
                        break;
                    case 3:
                        conditionDto.setGoodsTagLikeCondition4(searchGoodsTagArray[i].trim());
                        break;
                    case 4:
                        conditionDto.setGoodsTagLikeCondition5(searchGoodsTagArray[i].trim());
                        break;
                    case 5:
                        conditionDto.setGoodsTagLikeCondition6(searchGoodsTagArray[i].trim());
                        break;
                    case 6:
                        conditionDto.setGoodsTagLikeCondition7(searchGoodsTagArray[i].trim());
                        break;
                    case 7:
                        conditionDto.setGoodsTagLikeCondition8(searchGoodsTagArray[i].trim());
                        break;
                    case 8:
                        conditionDto.setGoodsTagLikeCondition9(searchGoodsTagArray[i].trim());
                        break;
                    case 9:
                        conditionDto.setGoodsTagLikeCondition10(searchGoodsTagArray[i].trim());
                        break;
                    default:
                        break;
                }
            }
        }

        // カテゴリID 検索には使用しない
        conditionDto.setCategoryId(productCsvDownloadGetRequest.getCategoryId());

        // 商品グループコード
        conditionDto.setGoodsGroupCode(productCsvDownloadGetRequest.getGoodsGroupCode());

        // 商品コード
        conditionDto.setGoodsCode(productCsvDownloadGetRequest.getGoodsCode());

        // JANコード
        conditionDto.setJanCode(productCsvDownloadGetRequest.getJanCode());

        // 下限販売可能在庫数
        if (productCsvDownloadGetRequest.getMinSalesPossibleStock() != null) {
            conditionDto.setMinSalesPossibleStock(
                            new BigDecimal(productCsvDownloadGetRequest.getMinSalesPossibleStock()));
        } else {
            conditionDto.setMinSalesPossibleStock(null);
        }

        // 上限販売可能在庫数
        if (productCsvDownloadGetRequest.getMaxSalesPossibleStock() != null) {
            conditionDto.setMaxSalesPossibleStock(
                            new BigDecimal(productCsvDownloadGetRequest.getMaxSalesPossibleStock()));
        } else {
            conditionDto.setMaxSalesPossibleStock(null);
        }

        // 商品名
        conditionDto.setGoodsGroupName(productCsvDownloadGetRequest.getGoodsGroupName());

        // サイト区分
        conditionDto.setSite(productCsvDownloadGetRequest.getSite());

        // 公開状態
        if (!ListUtils.isEmpty(productCsvDownloadGetRequest.getGoodsOpenStatusArray())) {
            conditionDto.setGoodsOpenStatusList(productCsvDownloadGetRequest.getGoodsOpenStatusArray());
        } else {
            conditionDto.setGoodsOpenStatusList(Arrays.asList(HTypeOpenDeleteStatus.OPEN.getValue(),
                                                              HTypeOpenDeleteStatus.NO_OPEN.getValue(),
                                                              HTypeOpenDeleteStatus.DELETED.getValue()
                                                             ));
        }

        // 販売状態
        if (!ListUtils.isEmpty(productCsvDownloadGetRequest.getGoodsSaleStatusArray())) {
            conditionDto.setSaleStatusList(productCsvDownloadGetRequest.getGoodsSaleStatusArray());
        } else {
            conditionDto.setSaleStatusList(
                            Arrays.asList(HTypeGoodsSaleStatus.SALE.getValue(), HTypeGoodsSaleStatus.NO_SALE.getValue(),
                                          HTypeGoodsSaleStatus.DELETED.getValue()
                                         ));
        }

        // 登録・更新日時
        String selectDate = productCsvDownloadGetRequest.getSelectRegistOrUpdate();
        if (selectDate != null) {
            if (selectDate.equals("0")) {
                if (productCsvDownloadGetRequest.getRegistTimeFrom() != null) {
                    conditionDto.setRegistTimeFrom(
                                    conversionUtility.toTimeStamp(productCsvDownloadGetRequest.getRegistTimeFrom()));
                }
                if (productCsvDownloadGetRequest.getRegistTimeTo() != null) {
                    conditionDto.setRegistTimeTo(
                                    conversionUtility.toTimeStamp(productCsvDownloadGetRequest.getRegistTimeTo()));
                }
            } else if (selectDate.equals("1")) {
                if (productCsvDownloadGetRequest.getUpdateTimeFrom() != null) {
                    conditionDto.setUpdateTimeFrom(
                                    conversionUtility.toTimeStamp(productCsvDownloadGetRequest.getUpdateTimeFrom()));
                }
                if (productCsvDownloadGetRequest.getUpdateTimeFrom() != null) {
                    conditionDto.setUpdateTimeTo(
                                    conversionUtility.toTimeStamp(productCsvDownloadGetRequest.getUpdateTimeTo()));
                }
            }
        }

        // ノベルティ商品フラグ
        conditionDto.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                       productCsvDownloadGetRequest.getNoveltyGoodsType()
                                                                      ));

        // フロント表示
        if (!ListUtils.isEmpty(productCsvDownloadGetRequest.getFrontDisplayList())) {
            conditionDto.setFrontDisplayList(productCsvDownloadGetRequest.getFrontDisplayList());
        } else {
            conditionDto.setFrontDisplayList(Arrays.asList(HTypeFrontDisplayStatus.NO_OPEN.getValue(),
                                                           HTypeFrontDisplayStatus.OPEN.getValue()
                                                          ));
        }

        // フロント表示基準日時
        conditionDto.setFrontDisplayReferenceDate(
                        conversionUtility.toTimeStamp(productCsvDownloadGetRequest.getFrontDisplayReferenceDate()));

        // 検索用商品設定キーワード
        if (StringUtils.isNotEmpty(productCsvDownloadGetRequest.getSettingKeywords())) {
            ZenHanConversionUtility zenHanConversionUtility =
                            ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
            List<String> searchSettingKeywordList = new ArrayList<>();
            List<String> settingKeywordList =
                            Arrays.asList(productCsvDownloadGetRequest.getSettingKeywords().split(" "));
            for (String searchSettingKeyword : settingKeywordList) {
                if (StringUtils.isNotEmpty(searchSettingKeyword)) {
                    searchSettingKeyword = zenHanConversionUtility.toZenkaku(searchSettingKeyword);
                    searchSettingKeyword = searchSettingKeyword.toUpperCase();
                    searchSettingKeywordList.add(searchSettingKeyword);
                }
            }
            conditionDto.setSearchSettingKeywordList(searchSettingKeywordList);
        }

        /** 複数番号検索 */
        if (CollectionUtils.isNotEmpty(productCsvDownloadGetRequest.getGoodsGroupCodeList())) {
            productCsvDownloadGetRequest.getGoodsGroupCodeList()
                                        .removeIf(goodsGroupCode -> StringUtils.isEmpty(goodsGroupCode));
            conditionDto.setGoodsGroupCodeList(productCsvDownloadGetRequest.getGoodsGroupCodeList());
        } else if (CollectionUtils.isNotEmpty(productCsvDownloadGetRequest.getGoodsCodeList())) {
            productCsvDownloadGetRequest.getGoodsCodeList().removeIf(goodsCode -> StringUtils.isEmpty(goodsCode));
            conditionDto.setGoodsCodeList(productCsvDownloadGetRequest.getGoodsCodeList());
        } else if (CollectionUtils.isNotEmpty(productCsvDownloadGetRequest.getJanCodeList())) {
            productCsvDownloadGetRequest.getJanCodeList().removeIf(janCode -> StringUtils.isEmpty(janCode));
            conditionDto.setJanCodeList(productCsvDownloadGetRequest.getJanCodeList());
        }

        /** カテゴリSEQリスト検索 */
        if (CollectionUtils.isNotEmpty(productCsvDownloadGetRequest.getCategoryIdList())) {
            conditionDto.setCategoryIdList(productCsvDownloadGetRequest.getCategoryIdList());
        }

        return conditionDto;
    }

    /**
     * 在庫Dtoクラスに変換
     *
     * @param stockRequest 商品グループ検索条件
     * @param shopSeq       ショップSEQ
     * @return 在庫Dtoクラス
     */
    public StockDto toStockDto(StockRequest stockRequest, Integer shopSeq) {
        StockDto stockDto = new StockDto();

        stockDto.setGoodsSeq(stockRequest.getGoodsSeq());
        stockDto.setShopSeq(shopSeq);
        stockDto.setSalesPossibleStock(stockRequest.getSalesPossibleStock());
        stockDto.setRealStock(stockRequest.getRealStock());
        stockDto.setOrderReserveStock(stockRequest.getOrderReserveStock());
        stockDto.setRemainderFewStock(stockRequest.getRemainderFewStock());
        stockDto.setSupplementCount(stockRequest.getSupplementCount());
        stockDto.setOrderPointStock(stockRequest.getOrderPointStock());
        stockDto.setSafetyStock(stockRequest.getSafetyStock());
        stockDto.setRegistTime(dateUtility.convertDateToTimestamp(stockRequest.getRegistTime()));
        stockDto.setUpdateTime(dateUtility.convertDateToTimestamp(stockRequest.getUpdateTime()));

        return stockDto;
    }

    /**
     * 商品グループDtoクラスに変換
     *
     * @param goodsGroupRequest 商品グループ一覧レスポンス
     * @param shopSeq           ショップSEQ
     * @return 商品グループ情報
     */
    public GoodsGroupDto toGoodsGroupDto(GoodsGroupRegistUpdateRequest goodsGroupRequest, Integer shopSeq)
                    throws Exception {
        GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

        GoodsGroupEntity goodsGroupEntity = toGoodsGroupEntity(goodsGroupRequest, shopSeq);
        StockStatusDisplayEntity batchUpdateStockStatusDisplayEntity = new StockStatusDisplayEntity();
        StockStatusDisplayEntity realTimeStockStatusDisplayEntity = new StockStatusDisplayEntity();
        if (ObjectUtils.isNotEmpty(goodsGroupRequest.getBatchUpdateStockStatus())) {
            batchUpdateStockStatusDisplayEntity =
                            toStockStatusDisplayEntity(goodsGroupRequest.getBatchUpdateStockStatus());
        } else {
            batchUpdateStockStatusDisplayEntity = null;
        }

        if (ObjectUtils.isNotEmpty(goodsGroupRequest.getRealTimeStockStatus())) {
            realTimeStockStatusDisplayEntity = toStockStatusDisplayEntity(goodsGroupRequest.getRealTimeStockStatus());
        } else {
            realTimeStockStatusDisplayEntity = null;
        }
        if (ObjectUtils.isNotEmpty(goodsGroupRequest.getGoodsGroupDisplay())) {
            GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                            toGoodsGroupDisplayEntity(goodsGroupRequest.getGoodsGroupDisplay());
            goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
        }
        List<GoodsGroupImageEntity> goodsGroupImageEntityList =
                        toGoodsGroupImageEntityList(goodsGroupRequest.getGoodsGroupImageRequestList());
        List<GoodsDto> goodsDtoList = toGoodsDtoList(goodsGroupRequest.getGoodsRequestList(),
                                                     goodsGroupRequest.getGoodsGroupDisplay(), shopSeq
                                                    );
        List<CategoryGoodsEntity> categoryGoodsEntityList =
                        toCategoryGoodsEntityList(goodsGroupRequest.getCategoryGoodsRequestList());
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = toGoodsInformationIconDetailsDtoList(
                        goodsGroupRequest.getGoodsInformationIconDetailsRequestList(), shopSeq);

        goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
        goodsGroupDto.setBatchUpdateStockStatus(batchUpdateStockStatusDisplayEntity);
        goodsGroupDto.setRealTimeStockStatus(realTimeStockStatusDisplayEntity);
        goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);
        goodsGroupDto.setGoodsDtoList(goodsDtoList);
        goodsGroupDto.setCategoryGoodsEntityList(categoryGoodsEntityList);
        goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);
        goodsGroupDto.setTaxRate(goodsGroupRequest.getTaxRate());

        return goodsGroupDto;
    }

    /**
     * 商品レスポンスクラスリストに変換
     *
     * @param goodsDtoList 商品DTOリスト
     * @return 商品レスポンスクラスリスト
     */
    public List<GoodsResponse> toGoodsResponse(List<GoodsDto> goodsDtoList) {
        List<GoodsResponse> goodsResponseList = new ArrayList<>();

        goodsDtoList.forEach(item -> {

            GoodsResponse goodsResponse = new GoodsResponse();
            if (item.getGoodsEntity() != null) {
                GoodsSubResponse goodsSubResponse = toGoodsSubResponse(item.getGoodsEntity());
                goodsResponse.setGoodsSub(goodsSubResponse);
            }
            if (item.getStockDto() != null) {
                StockResponse stockResponse = toStockResponse(item.getStockDto());
                goodsResponse.setStock(stockResponse);
            }
            goodsResponse.setDeleteFlg(item.getDeleteFlg());
            goodsResponse.setStockStatus(EnumTypeUtil.getValue(item.getStockStatusPc()));
            if (ObjectUtils.isNotEmpty(item.getFrontDisplayStockStatus())) {
                goodsResponse.setFrontDisplayStockStatus(EnumTypeUtil.getValue(item.getFrontDisplayStockStatus()));
            }

            goodsResponseList.add(goodsResponse);
        });

        return goodsResponseList;
    }

    /**
     * 関連商品クラスリストに変換
     *
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用Dtoクラスリスト
     * @return 関連商品クラスリスト
     */
    public List<GoodsGroupImageRegistUpdateRequest> toGoodsGroupImageRegistUpdateRequestList(List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList) {
        List<GoodsGroupImageRegistUpdateRequest> goodsGroupImageRegistRequestList = new ArrayList<>();

        for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : goodsGroupImageRegistUpdateDtoList) {
            GoodsGroupImageRegistUpdateRequest goodsGroupImageRegistUpdateRequest =
                            new GoodsGroupImageRegistUpdateRequest();

            goodsGroupImageRegistUpdateRequest.setGoodsGroupImageEntity(
                            toGoodsGroupImageRequest(goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()));
            goodsGroupImageRegistUpdateRequest.setImageFileName(goodsGroupImageRegistUpdateDto.getImageFileName());
            goodsGroupImageRegistUpdateRequest.setTmpImageFileName(
                            goodsGroupImageRegistUpdateDto.getTmpImageFileName());
            goodsGroupImageRegistUpdateRequest.setTmpImageFilePath(
                            goodsGroupImageRegistUpdateDto.getTmpImageFilePath());
            goodsGroupImageRegistUpdateRequest.setDeleteFlg(goodsGroupImageRegistUpdateDto.getDeleteFlg());

            goodsGroupImageRegistRequestList.add(goodsGroupImageRegistUpdateRequest);
        }

        return goodsGroupImageRegistRequestList;
    }

    /**
     * 商品レスポンスクラスリストに変換
     *
     * @param categoryGoodsEntityList カテゴリ登録商品クラスリスト
     * @return 商品レスポンスクラスリスト
     */
    public List<CategoryGoodsResponse> toCategoryGoodsResponseList(List<CategoryGoodsEntity> categoryGoodsEntityList) {
        List<CategoryGoodsResponse> categoryGoodsResponseList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(categoryGoodsEntityList)) {
            categoryGoodsEntityList.forEach(categoryGoodsEntity -> {
                CategoryGoodsResponse categoryGoodsResponse = new CategoryGoodsResponse();

                categoryGoodsResponse.setCategorySeq(categoryGoodsEntity.getCategorySeq());
                categoryGoodsResponse.setGoodsGroupSeq(categoryGoodsEntity.getGoodsGroupSeq());
                categoryGoodsResponse.setManualOrderDisplay(categoryGoodsEntity.getManualOrderDisplay());
                categoryGoodsResponse.setRegistTime(categoryGoodsEntity.getRegistTime());
                categoryGoodsResponse.setUpdateTime(categoryGoodsEntity.getUpdateTime());
                categoryGoodsResponse.setCategoryId(categoryGoodsEntity.getCategoryId());
                categoryGoodsResponse.setCategoryName(categoryGoodsEntity.getCategoryName());
                categoryGoodsResponse.setCategoryType(EnumTypeUtil.getValue(categoryGoodsEntity.getCategoryType()));
                categoryGoodsResponseList.add(categoryGoodsResponse);
            });
        }
        return categoryGoodsResponseList;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageEntityList 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    public List<GoodsGroupImageResponse> toGoodsGroupImageResponseList(List<GoodsGroupImageEntity> goodsGroupImageEntityList) {
        List<GoodsGroupImageResponse> goodsGroupImageResponseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsGroupImageEntityList)) {
            goodsGroupImageEntityList.forEach(item -> {
                GoodsGroupImageResponse goodsGroupImageResponse = new GoodsGroupImageResponse();

                goodsGroupImageResponse.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageResponse.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageResponse.setImageFileName(goodsUtility.getGoodsImagePath(item.getImageFileName()));
                goodsGroupImageResponse.setRegistTime(item.getRegistTime());
                goodsGroupImageResponse.setUpdateTime(item.getUpdateTime());

                goodsGroupImageResponseList.add(goodsGroupImageResponse);
            });
        }
        return goodsGroupImageResponseList;
    }

    /**
     * アイコン詳細レスポンスクラスリストに変換
     *
     * @param goodsInformationIconDetailsDtoList アイコン詳細DTOクラスリスト
     * @return アイコン詳細レスポンスクラスリスト
     */
    public List<GoodsInformationIconDetailsResponse> toGoodsInformationIconDetailsResponseList(List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList) {
        List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList = new ArrayList<>();

        for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsInformationIconDetailsDtoList) {
            GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse =
                            new GoodsInformationIconDetailsResponse();

            goodsInformationIconDetailsResponse.setGoodsGroupSeq(goodsInformationIconDetailsDto.getGoodsGroupSeq());
            goodsInformationIconDetailsResponse.setIconSeq(goodsInformationIconDetailsDto.getIconSeq());
            goodsInformationIconDetailsResponse.setIconName(goodsInformationIconDetailsDto.getIconName());
            goodsInformationIconDetailsResponse.setColorCode(goodsInformationIconDetailsDto.getColorCode());
            goodsInformationIconDetailsResponse.setOrderDisplay(goodsInformationIconDetailsDto.getOrderDisplay());

            goodsInformationIconDetailsResponseList.add(goodsInformationIconDetailsResponse);
        }

        return goodsInformationIconDetailsResponseList;
    }

    /**
     * カテゴリ登録商品クラスリストに変換
     *
     * @param categoryGoodsRequestList 商品レスポンスクラスリスト
     * @return カテゴリ登録商品クラスリスト
     */
    public List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsRequest> categoryGoodsRequestList) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        categoryGoodsRequestList.forEach(item -> {
            CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();
            categoryGoodsEntity.setCategorySeq(item.getCategorySeq());
            categoryGoodsEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            categoryGoodsEntity.setManualOrderDisplay(item.getManualOrderDisplay());
            categoryGoodsEntity.setRegistTime(dateUtility.convertDateToTimestamp(item.getRegistTime()));
            categoryGoodsEntity.setUpdateTime(dateUtility.convertDateToTimestamp(item.getUpdateTime()));
            categoryGoodsEntityList.add(categoryGoodsEntity);
        });

        return categoryGoodsEntityList;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageEntityList(List<GoodsGroupImageRequest> goodsGroupImageResponseList) {
        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(goodsGroupImageResponseList)) {
            goodsGroupImageResponseList.forEach(item -> {
                GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();
                goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageEntity.setImageFileName(item.getImageFileName());
                goodsGroupImageEntity.setRegistTime(dateUtility.convertDateToTimestamp(item.getRegistTime()));
                goodsGroupImageEntity.setUpdateTime(dateUtility.convertDateToTimestamp(item.getUpdateTime()));
                goodsGroupImageEntityList.add(goodsGroupImageEntity);
            });
        }

        return goodsGroupImageEntityList;
    }

    /**
     * アイコン詳細Dtoリストに変換
     *
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスクラスリスト
     * @param shopSeq                                 ショップSEQ
     * @return アイコン詳細Dtoリスト
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsRequest> goodsInformationIconDetailsResponseList,
                                                                                     Integer shopSeq) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(goodsInformationIconDetailsResponseList)) {
            goodsInformationIconDetailsResponseList.forEach(item -> {
                GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();
                goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
                goodsInformationIconDetailsDto.setIconName(item.getIconName());
                goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
                goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());
                goodsInformationIconDetailsDto.setShopSeq(shopSeq);
                goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
            });
        }

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品DTOリストに変換
     *
     * @param goodsRequestList 商品レスポンスクラスリスト
     * @param shopSeq           ショップSEQ
     * @return 商品DTOリスト
     */
    public List<GoodsDto> toGoodsDtoList(List<GoodsRequest> goodsRequestList,
                                         GoodsGroupDisplayRequest goodsGroupDisplayRequest,
                                         Integer shopSeq) {
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsRequestList.forEach(item -> {
            GoodsDto goodsDto = new GoodsDto();
            GoodsEntity goodsEntity = toGoodsEntity(item.getGoodsSubRequest(), shopSeq);
            StockDto stockDto = toStockDto(item.getStockRequest(), shopSeq);
            goodsDto.setGoodsEntity(goodsEntity);
            goodsDto.setStockDto(stockDto);
            goodsDto.setDeleteFlg(item.getDeleteFlg());
            goodsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, item.getStockStatus()));
            goodsDto.setGoodsTag(goodsGroupDisplayRequest.getGoodsTag());
            goodsDtoList.add(goodsDto);
        });

        return goodsDtoList;
    }

    /**
     * 商品グループ画像登録更新用Dtoクラスにリスト変換
     *
     * @param goodsGroupImageRegistUpdateRequestList 関連商品クラスリスト
     * @return 商品グループ画像登録更新用Dtoクラスリスト
     */
    public List<GoodsGroupImageRegistUpdateDto> toGoodsGroupImageRegistDtoList(List<GoodsGroupImageRegistUpdateRequest> goodsGroupImageRegistUpdateRequestList) {
        List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList = new ArrayList<>();

        for (GoodsGroupImageRegistUpdateRequest goodsGroupImageUpdateRegistRequest : goodsGroupImageRegistUpdateRequestList) {
            GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto = new GoodsGroupImageRegistUpdateDto();

            goodsGroupImageRegistUpdateDto.setGoodsGroupImageEntity(
                            toGoodsGroupImageEntity(goodsGroupImageUpdateRegistRequest.getGoodsGroupImageEntity()));
            goodsGroupImageRegistUpdateDto.setImageFileName(goodsGroupImageUpdateRegistRequest.getImageFileName());
            goodsGroupImageRegistUpdateDto.setTmpImageFileName(
                            goodsGroupImageUpdateRegistRequest.getTmpImageFileName());
            goodsGroupImageRegistUpdateDto.setTmpImageFilePath(
                            goodsGroupImageUpdateRegistRequest.getTmpImageFilePath());
            goodsGroupImageRegistUpdateDto.setDeleteFlg(goodsGroupImageUpdateRegistRequest.getDeleteFlg());

            goodsGroupImageRegistUpdateDtoList.add(goodsGroupImageRegistUpdateDto);
        }

        return goodsGroupImageRegistUpdateDtoList;
    }

    /**
     * 商品グループ画像登録更新用Dtoクラスにリスト変換
     *
     * @param goodsGroupImageUpdateRequestList 関連商品クラスリスト
     * @return 商品グループ画像登録更新用Dtoクラスリスト
     */
    public List<GoodsGroupImageRegistUpdateDto> toGoodsGroupImageUpdateDtoList(List<GoodsGroupImageRegistUpdateRequest> goodsGroupImageUpdateRequestList) {
        List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList = new ArrayList<>();

        for (GoodsGroupImageRegistUpdateRequest goodsGroupImageRegistUpdateRequest : goodsGroupImageUpdateRequestList) {
            GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto = new GoodsGroupImageRegistUpdateDto();

            goodsGroupImageRegistUpdateDto.setGoodsGroupImageEntity(
                            toGoodsGroupImageEntity(goodsGroupImageRegistUpdateRequest.getGoodsGroupImageEntity()));
            goodsGroupImageRegistUpdateDto.setImageFileName(goodsGroupImageRegistUpdateRequest.getImageFileName());
            goodsGroupImageRegistUpdateDto.setTmpImageFileName(
                            goodsGroupImageRegistUpdateRequest.getTmpImageFileName());
            goodsGroupImageRegistUpdateDto.setTmpImageFilePath(
                            goodsGroupImageRegistUpdateRequest.getTmpImageFilePath());
            goodsGroupImageRegistUpdateDto.setDeleteFlg(goodsGroupImageRegistUpdateRequest.getDeleteFlg());

            goodsGroupImageRegistUpdateDtoList.add(goodsGroupImageRegistUpdateDto);
        }

        return goodsGroupImageRegistUpdateDtoList;
    }

    /**
     * 商品グループ画像Mapに変換
     *
     * @param masterGoodsGroupImageRequestMap 商品グループ画像クラスMap
     * @return 商品グループ画像Map
     */
    public Map<Integer, GoodsGroupImageEntity> toMasterGoodsGroupImageEntityMap(Map<String, GoodsGroupImageRequest> masterGoodsGroupImageRequestMap) {
        Map<Integer, GoodsGroupImageEntity> map = new HashMap<>();

        if (!MapUtils.isEmpty(masterGoodsGroupImageRequestMap)) {
            masterGoodsGroupImageRequestMap.forEach((k, v) -> map.put(Integer.parseInt(k), toGoodsGroupImageEntity(v)));
        }

        return map;
    }

    /**
     * 関連商品クラスリストに変換
     *
     * @param goodsRelationRequestList 関連商品クラスリスト
     * @return 関連商品クラスリスト
     */
    public List<GoodsRelationEntity> toGoodRelationEntityList(List<GoodsRelationRequest> goodsRelationRequestList) {
        List<GoodsRelationEntity> goodsRelationEntityList = new ArrayList<>();

        for (GoodsRelationRequest goodsRelationRequest : goodsRelationRequestList) {
            GoodsRelationEntity goodsRelationEntity = new GoodsRelationEntity();

            goodsRelationEntity.setGoodsGroupSeq(goodsRelationRequest.getGoodsGroupSeq());
            goodsRelationEntity.setGoodsRelationGroupSeq(goodsRelationRequest.getGoodsRelationGroupSeq());
            goodsRelationEntity.setOrderDisplay(goodsRelationRequest.getOrderDisplay());
            goodsRelationEntity.setRegistTime(dateUtility.convertDateToTimestamp(goodsRelationRequest.getRegistTime()));
            goodsRelationEntity.setUpdateTime(dateUtility.convertDateToTimestamp(goodsRelationRequest.getUpdateTime()));
            goodsRelationEntity.setGoodsGroupCode(goodsRelationRequest.getGoodsGroupCode());
            goodsRelationEntity.setGoodsGroupName(goodsRelationRequest.getGoodsGroupName());
            goodsRelationEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   goodsRelationRequest.getGoodsOpenStatus()
                                                                                  ));

            goodsRelationEntityList.add(goodsRelationEntity);
        }

        return goodsRelationEntityList;
    }

    /**
     * 画像に関する登録更新情報を整理
     *
     * @param productRegistUpdateRequest 商品グループ登録更新リクエスト
     */
    public void toSetImageInfo(ProductRegistUpdateRequest productRegistUpdateRequest) {
        // 商品グループ画像
        setGoodsGroupImageRegistUpdateList(productRegistUpdateRequest);
    }

    /**
     * 登録更新情報を整理(商品グループ画像)<br/>
     *
     * @param productRegistUpdateRequest 商品グループ登録更新リクエスト
     */
    private void setGoodsGroupImageRegistUpdateList(ProductRegistUpdateRequest productRegistUpdateRequest) {
        List<GoodsGroupImageRegistUpdateDto> ruList = new ArrayList<>();

        // 登録更新内容取得
        Map<Integer, GoodsGroupImageRegistUpdateDto> registUpdateGgImageMap = new HashMap<>();
        if (productRegistUpdateRequest.getGoodsGroupImageUpdateRequest() != null) {
            for (GoodsGroupImageRegistUpdateDto ruDto : toGoodsGroupImageUpdateDtoList(
                            productRegistUpdateRequest.getGoodsGroupImageUpdateRequest())) {
                // 画像連番でMAPを作成
                registUpdateGgImageMap.put(ruDto.getGoodsGroupImageEntity().getImageTypeVersionNo(), ruDto);
            }
        }

        // 全画像連番＋全画像種別 の状態を確認
        String maxCount = PropertiesUtil.getSystemPropertiesValue("goodsimage.max.count");
        for (int versionNo = 0; versionNo <= Integer.parseInt(maxCount); versionNo++) {

            // 該当するマスタ情報取得
            GoodsGroupImageEntity masterEntity = toMasterGoodsGroupImageEntityMap(
                            productRegistUpdateRequest.getMasterGoodsGroupImageRequestMap()).get(versionNo);

            GoodsGroupImageRegistUpdateDto ruDto = registUpdateGgImageMap.get(versionNo);

            if (ruDto != null) {
                // 登録更新リストに含まれている場合、表示状態を上書き
                ruList.add(ruDto);

                // 登録更新リストに含まれていないが、表示状態を変更した場合はマスタ情報確認
            } else if (masterEntity != null) {
                // マスタに存在するため、上書き情報作成
                GoodsGroupImageRegistUpdateDto updateDto =
                                ApplicationContextUtility.getBean(GoodsGroupImageRegistUpdateDto.class);

                // 表示状態上書き+削除対象外
                updateDto.setGoodsGroupImageEntity(masterEntity);
                updateDto.setDeleteFlg(false);

                ruList.add(updateDto);
            }
        }
        List<GoodsGroupImageRegistUpdateRequest> goodsGroupImageUpdateRequestList =
                        toGoodsGroupImageRegistUpdateRequestList(ruList);

        productRegistUpdateRequest.setGoodsGroupImageUpdateRequest(goodsGroupImageUpdateRequestList);
    }

    /**
     * 商品グループレスポンスに変換
     *
     * @param goodsGroupDto 商品グループDtoクラス
     * @return 商品グループレスポンス
     */
    public ProductResponse setProductResponse(GoodsGroupDto goodsGroupDto) throws Exception {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setGoodsGroup(toGoodsGroupResponse(goodsGroupDto.getGoodsGroupEntity()));
        productResponse.setBatchUpdateStockStatus(
                        toStockStatusDisplayResponse(goodsGroupDto.getBatchUpdateStockStatus(), null));
        productResponse.setRealTimeStockStatus(toStockStatusDisplayResponse(goodsGroupDto.getRealTimeStockStatus(),
                                                                            goodsGroupDto.getFrontDisplayStockStatus()
                                                                           ));
        productResponse.setGoodsGroupDisplay(toGoodsGroupDisplayResponse(goodsGroupDto.getGoodsGroupDisplayEntity()));
        productResponse.setGoodsGroupImageResponseList(
                        toGoodsGroupImageResponseList(goodsGroupDto.getGoodsGroupImageEntityList()));
        productResponse.setGoodsResponseList(toGoodsResponse(goodsGroupDto.getGoodsDtoList()));
        productResponse.setCategoryGoodsResponseList(
                        toCategoryGoodsResponseList(goodsGroupDto.getCategoryGoodsEntityList()));
        productResponse.setGoodsInformationIconDetailsResponseList(toGoodsInformationIconDetailsResponseList(
                        goodsGroupDto.getGoodsInformationIconDetailsDtoList()));
        productResponse.setTaxRate(goodsGroupDto.getTaxRate());

        return productResponse;
    }

    /**
     * 画面表示用商品グループレスポンスに変換
     *
     * @param goodsGroupDto 商品グループDto
     * @return 画面表示用商品グループレスポンス
     */
    public ProductDisplayResponse setProductDisplayResponse(GoodsGroupDto goodsGroupDto) throws Exception {
        ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();

        productDisplayResponse.setGoodsGroup(toGoodsGroupResponse(goodsGroupDto.getGoodsGroupEntity()));
        productDisplayResponse.setBatchUpdateStockStatus(
                        toStockStatusDisplayResponse(goodsGroupDto.getBatchUpdateStockStatus(), null));
        productDisplayResponse.setRealTimeStockStatus(
                        toStockStatusDisplayResponse(goodsGroupDto.getRealTimeStockStatus(),
                                                     goodsGroupDto.getFrontDisplayStockStatus()
                                                    ));
        productDisplayResponse.setGoodsGroupDisplay(
                        toGoodsGroupDisplayResponse(goodsGroupDto.getGoodsGroupDisplayEntity()));
        productDisplayResponse.setGoodsGroupImageResponseList(
                        toGoodsGroupImageResponseList(goodsGroupDto.getGoodsGroupImageEntityList()));
        productDisplayResponse.setGoodsResponseList(toGoodsResponse(goodsGroupDto.getGoodsDtoList()));
        productDisplayResponse.setCategoryGoodsResponseList(
                        toCategoryGoodsResponseList(goodsGroupDto.getCategoryGoodsEntityList()));
        productDisplayResponse.setGoodsInformationIconDetailsResponseList(toGoodsInformationIconDetailsResponseList(
                        goodsGroupDto.getGoodsInformationIconDetailsDtoList()));
        productDisplayResponse.setFrontDisplay(ObjectUtils.isEmpty(goodsGroupDto.getFrontDisplay()) ?
                                                               null :
                                                               goodsGroupDto.getFrontDisplay().getValue());
        productDisplayResponse.setWarningMessage(goodsGroupDto.getWarningMessage());
        productDisplayResponse.setTaxRate(goodsGroupDto.getTaxRate());

        return productDisplayResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param goodsDetailsList 商品詳細Dtoリスト
     * @return 商品詳細一覧レスポンス
     */
    public ProductDetailListResponse toProductDetailListResponse(List<GoodsDetailsDto> goodsDetailsList)
                    throws Exception {

        ProductDetailListResponse productDetailListResponse = new ProductDetailListResponse();
        List<GoodsDetailsResponse> goodsDetailsResponses = new ArrayList<>();

        for (GoodsDetailsDto goodsDetailsDto : goodsDetailsList) {
            GoodsDetailsResponse goodsDetailsResponse = this.toGoodsDetailsResponseFromDto(goodsDetailsDto);
            goodsDetailsResponses.add(goodsDetailsResponse);
        }

        productDetailListResponse.setGoodsDetailsList(goodsDetailsResponses);

        return productDetailListResponse;
    }

    /**
     * 商品詳細レスポンスに変換
     *
     * @param goodsDetailsDto 商品詳細Dto
     * @return 商品詳細レスポンス
     */
    protected GoodsDetailsResponse toGoodsDetailsResponseFromDto(GoodsDetailsDto goodsDetailsDto) throws Exception {
        if (goodsDetailsDto == null) {
            return null;
        }

        GoodsDetailsResponse goodsDetailsResponse = new GoodsDetailsResponse();

        goodsDetailsResponse.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
        goodsDetailsResponse.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
        goodsDetailsResponse.setGoodsGroupSeq(goodsDetailsDto.getGoodsGroupSeq());
        goodsDetailsResponse.setVersionNo(goodsDetailsDto.getVersionNo());
        goodsDetailsResponse.setGoodsCode(goodsDetailsDto.getGoodsCode());
        goodsDetailsResponse.setGoodsTaxType(EnumTypeUtil.getValue(goodsDetailsDto.getGoodsTaxType()));
        goodsDetailsResponse.setTaxRate(goodsDetailsDto.getTaxRate());
        goodsDetailsResponse.setGoodsPriceInTax(
                        calculatePriceUtility.getTaxIncludedPrice(goodsDetailsDto.getGoodsPrice(),
                                                                  goodsDetailsDto.getTaxRate()
                                                                 ));
        goodsDetailsResponse.setGoodsPrice(goodsDetailsDto.getGoodsPrice());
        goodsDetailsResponse.setAlcoholFlag(EnumTypeUtil.getValue(goodsDetailsDto.getAlcoholFlag()));
        goodsDetailsResponse.setNoveltyGoodsType(EnumTypeUtil.getValue(goodsDetailsDto.getNoveltyGoodsType()));
        goodsDetailsResponse.setDeliveryType(goodsDetailsDto.getDeliveryType());
        if (ObjectUtils.isNotEmpty(goodsDetailsDto.getGoodsTag())) {
            goodsDetailsResponse.setGoodsTag(arrayFactoryUtility.arrayToList(goodsDetailsDto.getGoodsTag()));
        }
        goodsDetailsResponse.setSaleStatus(EnumTypeUtil.getValue(goodsDetailsDto.getSaleStatusPC()));
        goodsDetailsResponse.setSaleStartTime(goodsDetailsDto.getSaleStartTimePC());
        goodsDetailsResponse.setSaleEndTime(goodsDetailsDto.getSaleEndTimePC());
        goodsDetailsResponse.setUnitManagementFlag(EnumTypeUtil.getValue(goodsDetailsDto.getUnitManagementFlag()));
        goodsDetailsResponse.setStockManagementFlag(EnumTypeUtil.getValue(goodsDetailsDto.getStockManagementFlag()));
        goodsDetailsResponse.setIndividualDeliveryType(
                        EnumTypeUtil.getValue(goodsDetailsDto.getIndividualDeliveryType()));
        goodsDetailsResponse.setPurchasedMax(goodsDetailsDto.getPurchasedMax());
        goodsDetailsResponse.setFreeDeliveryFlag(EnumTypeUtil.getValue(goodsDetailsDto.getFreeDeliveryFlag()));
        goodsDetailsResponse.setOrderDisplay(goodsDetailsDto.getOrderDisplay());
        goodsDetailsResponse.setUnitValue1(goodsDetailsDto.getUnitValue1());
        goodsDetailsResponse.setUnitValue2(goodsDetailsDto.getUnitValue2());
        goodsDetailsResponse.setJanCode(goodsDetailsDto.getJanCode());
        goodsDetailsResponse.setSalesPossibleStock(goodsDetailsDto.getSalesPossibleStock());
        goodsDetailsResponse.setRealStock(goodsDetailsDto.getRealStock());
        goodsDetailsResponse.setOrderReserveStock(goodsDetailsDto.getOrderReserveStock());
        goodsDetailsResponse.setRemainderFewStock(goodsDetailsDto.getRemainderFewStock());
        goodsDetailsResponse.setOrderPointStock(goodsDetailsDto.getOrderPointStock());
        goodsDetailsResponse.setSafetyStock(goodsDetailsDto.getSafetyStock());
        goodsDetailsResponse.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
        goodsDetailsResponse.setWhatsnewDate(goodsDetailsDto.getWhatsnewDate());
        goodsDetailsResponse.setGoodsOpenStatus(EnumTypeUtil.getValue(goodsDetailsDto.getGoodsOpenStatusPC()));
        goodsDetailsResponse.setOpenStartTime(goodsDetailsDto.getOpenStartTimePC());
        goodsDetailsResponse.setOpenEndTime(goodsDetailsDto.getOpenEndTimePC());
        goodsDetailsResponse.setGoodsGroupName(goodsDetailsDto.getGoodsGroupName());
        goodsDetailsResponse.setUnitTitle1(goodsDetailsDto.getUnitTitle1());
        goodsDetailsResponse.setUnitTitle2(goodsDetailsDto.getUnitTitle2());
        if (CollectionUtil.isNotEmpty(goodsDetailsDto.getGoodsGroupImageEntityList())) {
            goodsDetailsResponse.setGoodsGroupImageList(
                            toGoodsGroupImageResponseList(goodsDetailsDto.getGoodsGroupImageEntityList()));
        }
        goodsDetailsResponse.setSnsLinkFlag(EnumTypeUtil.getValue(goodsDetailsDto.getSnsLinkFlag()));
        goodsDetailsResponse.setMetaDescription(goodsDetailsDto.getMetaDescription());
        goodsDetailsResponse.setStockStatus(EnumTypeUtil.getValue(goodsDetailsDto.getStockStatusPc()));
        if (CollectionUtil.isNotEmpty(goodsDetailsDto.getGoodsIconList())) {
            goodsDetailsResponse.setGoodsIconList(toGoodsGroupImageResponses(goodsDetailsDto.getGoodsIconList()));
        }
        goodsDetailsResponse.setGoodsNote1(goodsDetailsDto.getGoodsNote1());
        goodsDetailsResponse.setGoodsNote2(goodsDetailsDto.getGoodsNote2());
        goodsDetailsResponse.setGoodsNote3(goodsDetailsDto.getGoodsNote3());
        goodsDetailsResponse.setGoodsNote4(goodsDetailsDto.getGoodsNote4());
        goodsDetailsResponse.setGoodsNote5(goodsDetailsDto.getGoodsNote5());
        goodsDetailsResponse.setGoodsNote6(goodsDetailsDto.getGoodsNote6());
        goodsDetailsResponse.setGoodsNote7(goodsDetailsDto.getGoodsNote7());
        goodsDetailsResponse.setGoodsNote8(goodsDetailsDto.getGoodsNote8());
        goodsDetailsResponse.setGoodsNote9(goodsDetailsDto.getGoodsNote9());
        goodsDetailsResponse.setGoodsNote10(goodsDetailsDto.getGoodsNote10());
        goodsDetailsResponse.setOrderSetting1(goodsDetailsDto.getOrderSetting1());
        goodsDetailsResponse.setOrderSetting2(goodsDetailsDto.getOrderSetting2());
        goodsDetailsResponse.setOrderSetting3(goodsDetailsDto.getOrderSetting3());
        goodsDetailsResponse.setOrderSetting4(goodsDetailsDto.getOrderSetting4());
        goodsDetailsResponse.setOrderSetting5(goodsDetailsDto.getOrderSetting5());
        goodsDetailsResponse.setOrderSetting6(goodsDetailsDto.getOrderSetting6());
        goodsDetailsResponse.setOrderSetting7(goodsDetailsDto.getOrderSetting7());
        goodsDetailsResponse.setOrderSetting8(goodsDetailsDto.getOrderSetting8());
        goodsDetailsResponse.setOrderSetting9(goodsDetailsDto.getOrderSetting9());
        goodsDetailsResponse.setOrderSetting10(goodsDetailsDto.getOrderSetting10());

        return goodsDetailsResponse;
    }

    /**
     * 商品詳細レスポンスクラス変換
     *
     * @param goodsDetailsDto 商品詳細Dtoクラス
     * @return 商品詳細レスポンスクラス
     */
    protected ProductDetailsResponse toProductDetailsResponse(GoodsDetailsDto goodsDetailsDto) {

        if (goodsDetailsDto == null) {
            return null;
        }

        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();

        productDetailsResponse.setGoodsCode(goodsDetailsDto.getGoodsCode());
        productDetailsResponse.setUnitValue1(goodsDetailsDto.getUnitValue1());
        productDetailsResponse.setUnitValue2(goodsDetailsDto.getUnitValue2());
        productDetailsResponse.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
        productDetailsResponse.setUnitTitle1(goodsDetailsDto.getUnitTitle1());
        productDetailsResponse.setUnitTitle2(goodsDetailsDto.getUnitTitle2());

        return productDetailsResponse;
    }

    /**
     * アイコン詳細レスポンスに変換
     *
     * @param goodsIconList 商品アイコンリスト
     * @return アイコン詳細レスポンス
     */
    public List<GoodsInformationIconDetailsResponse> toGoodsGroupImageResponses(List<GoodsInformationIconDetailsDto> goodsIconList) {

        List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponses = new ArrayList<>();

        goodsIconList.forEach(item -> {

            GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse =
                            new GoodsInformationIconDetailsResponse();
            goodsInformationIconDetailsResponse.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsResponse.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsResponse.setIconName(item.getIconName());
            goodsInformationIconDetailsResponse.setColorCode(item.getColorCode());
            goodsInformationIconDetailsResponse.setOrderDisplay(item.getOrderDisplay());

            goodsInformationIconDetailsResponses.add(goodsInformationIconDetailsResponse);
        });

        return goodsInformationIconDetailsResponses;
    }

    /**
     * conditionDtoをセットする
     *
     * @param shopSeq               サイト種別
     * @param siteType              サイト
     * @param productListGetRequest 商品グループ検索条件リクエスト
     * @return 商品グループDao用検索条件Dtoクラス
     */
    public GoodsGroupSearchForDaoConditionDto setConditionDto(int shopSeq,
                                                              HTypeSiteType siteType,
                                                              ProductListGetRequest productListGetRequest) {

        GoodsGroupSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsGroupSearchForDaoConditionDto.class);
        // (3) 商品情報検索処理実行
        // ・商品グループDao用検索条件Dtoを作成する
        // ・商品グループDao用検索条件Dto
        // ‥ショップSEQ =共通情報.ショップSEQ
        // ‥サイト区分 =共通情報.サイト区分
        conditionDto.setShopSeq(shopSeq);
        conditionDto.setSiteType(siteType);

        // カテゴリIDをセット
        conditionDto.setCategoryId(productListGetRequest.getCategoryId());

        // 下限金額をセット
        if (productListGetRequest.getMinPrice() != null) {
            conditionDto.setMinPrice(productListGetRequest.getMinPrice());
        }
        // 上限金額をセット
        if (productListGetRequest.getMaxPrice() != null) {
            conditionDto.setMaxPrice(productListGetRequest.getMaxPrice());
        }
        // 公開状態「公開」をセット
        if (productListGetRequest.getOpenStatus() != null) {
            conditionDto.setOpenStatus(HTypeOpenDeleteStatus.of(productListGetRequest.getOpenStatus()));
        }

        // 在庫ありの指定がある場合
        if (productListGetRequest.getInStock() != null && productListGetRequest.getInStock()) {
            // 在庫状況「在庫あり」「残りわずか」「予約受付中」をセット
            conditionDto.setStcockExistStatus(new String[] {HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue(),
                            HTypeStockStatusType.STOCK_FEW.getValue(),
                            HTypeStockStatusType.ON_RESERVATIONS.getValue()});
        }

        // キーワードをセット
        if (productListGetRequest.getKeywordLikeCondition1() != null) {
            conditionDto.setKeywordLikeCondition1(productListGetRequest.getKeywordLikeCondition1());
        }
        if (productListGetRequest.getKeywordLikeCondition2() != null) {
            conditionDto.setKeywordLikeCondition2(productListGetRequest.getKeywordLikeCondition2());
        }
        if (productListGetRequest.getKeywordLikeCondition3() != null) {
            conditionDto.setKeywordLikeCondition3(productListGetRequest.getKeywordLikeCondition3());
        }
        if (productListGetRequest.getKeywordLikeCondition4() != null) {
            conditionDto.setKeywordLikeCondition4(productListGetRequest.getKeywordLikeCondition4());
        }
        if (productListGetRequest.getKeywordLikeCondition5() != null) {
            conditionDto.setKeywordLikeCondition5(productListGetRequest.getKeywordLikeCondition5());
        }
        if (productListGetRequest.getKeywordLikeCondition6() != null) {
            conditionDto.setKeywordLikeCondition6(productListGetRequest.getKeywordLikeCondition6());
        }
        if (productListGetRequest.getKeywordLikeCondition7() != null) {
            conditionDto.setKeywordLikeCondition7(productListGetRequest.getKeywordLikeCondition7());
        }
        if (productListGetRequest.getKeywordLikeCondition8() != null) {
            conditionDto.setKeywordLikeCondition8(productListGetRequest.getKeywordLikeCondition8());
        }
        if (productListGetRequest.getKeywordLikeCondition9() != null) {
            conditionDto.setKeywordLikeCondition9(productListGetRequest.getKeywordLikeCondition9());
        }
        if (productListGetRequest.getKeywordLikeCondition10() != null) {
            conditionDto.setKeywordLikeCondition10(productListGetRequest.getKeywordLikeCondition10());
        }

        if (CollectionUtils.isNotEmpty(productListGetRequest.getGoodsGroupSeqList())) {
            conditionDto.setGoodsGroupSeqList(productListGetRequest.getGoodsGroupSeqList());
        }

        conditionDto.setFrontDisplayReferenceDate(
                        this.dateUtility.convertDateToTimestamp(productListGetRequest.getFrontDisplayReferenceDate()));

        return conditionDto;
    }

    /**
     * 商品覧レスポンスに変換
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @return 商品覧レスポンス
     */
    public ProductListResponse toProductListResponse(List<GoodsGroupDto> goodsGroupDtoList) throws Exception {

        ProductListResponse productListResponse = new ProductListResponse();
        List<GoodsGroupResponse> goodsGroupResponseList = new ArrayList<>();

        for (GoodsGroupDto i : goodsGroupDtoList) {

            GoodsGroupResponse goodsGroupResponse = new GoodsGroupResponse();
            goodsGroupResponse.setTaxRate(i.getTaxRate());
            if (i.getGoodsGroupDisplayEntity() != null) {
                goodsGroupResponse.setGoodsGroupDisplay(toGoodsGroupDisplayResponse(i.getGoodsGroupDisplayEntity()));
            }
            goodsGroupResponse.setGoodsGroupSubResponse(toGoodsGroupResponse(i.getGoodsGroupEntity()));
            if (CollectionUtil.isNotEmpty(i.getGoodsDtoList())) {
                goodsGroupResponse.setGoodsResponseList(toGoodsResponse(i.getGoodsDtoList()));
            }
            if (CollectionUtil.isNotEmpty(i.getGoodsGroupImageEntityList())) {
                goodsGroupResponse.setGoodsGroupImageResponseList(
                                toGoodsGroupImageResponseList(i.getGoodsGroupImageEntityList()));
            }
            if (i.getBatchUpdateStockStatus() != null) {
                goodsGroupResponse.setBatchUpdateStockStatus(
                                toStockStatusDisplayResponse(i.getBatchUpdateStockStatus(), null));
            }
            if (CollectionUtil.isNotEmpty(i.getCategoryGoodsEntityList())) {
                goodsGroupResponse.setCategoryGoodsResponseList(
                                toCategoryGoodsResponseList(i.getCategoryGoodsEntityList()));
            }
            if (CollectionUtil.isNotEmpty(i.getGoodsInformationIconDetailsDtoList())) {
                goodsGroupResponse.setGoodsInformationIconDetailsResponseList(
                                toGoodsInformationIconDetailsResponseList(i.getGoodsInformationIconDetailsDtoList()));
            }
            if (i.getRealTimeStockStatus() != null) {
                goodsGroupResponse.setRealTimeStockStatus(toStockStatusDisplayResponse(i.getRealTimeStockStatus(),
                                                                                       i.getFrontDisplayStockStatus()
                                                                                      ));
            }
            if (ObjectUtils.isNotEmpty(i.getFrontDisplay())) {
                goodsGroupResponse.setFrontDisplay(EnumTypeUtil.getValue(i.getFrontDisplay()));
            }

            goodsGroupResponseList.add(goodsGroupResponse);
        }
        productListResponse.setGoodsGroupList(goodsGroupResponseList);
        return productListResponse;
    }

    /**
     * 入力情報チェック（削除処理用）<br/>
     * Validatorで対応できないもの
     *
     * @param goodsGroupDto 商品グループDtoクラス
     */
    public void checkDataForDelete(GoodsGroupDto goodsGroupDto) {
        clearErrorList();
        // 既に削除済みエラー
        if (HTypeOpenDeleteStatus.DELETED == goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC()) {
            addErrorMessage("AGG001101");
        }
        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * 商品グループDtoに削除フラグ設定<br/>
     *
     * @param goodsGroupDto 商品グループDtoクラス
     */
    public void setFlagForDelete(GoodsGroupDto goodsGroupDto) {
        // 商品グループエンティティ
        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
        goodsGroupEntity.setGoodsOpenStatusPC(HTypeOpenDeleteStatus.DELETED);
    }

    /**
     * Dtoへの変換処理。<br />
     * Pageの検索条件情報 ⇒ 商品Dao用検索条件Dto<br />
     *
     * @param productOrderItemListGetRequest 受注修正用商品一覧取得リクエスト
     * @return 商品Dao用検索条件Dto
     */
    public GoodsSearchForBackDaoConditionDto toGoodsSearchForBackDaoConditionDtoForGoodsSearch(
                    ProductOrderItemListGetRequest productOrderItemListGetRequest) {

        GoodsSearchForBackDaoConditionDto goodsConditionDto =
                        ApplicationContextUtility.getBean(GoodsSearchForBackDaoConditionDto.class);

        /* 画面条件 */
        // サイト
        goodsConditionDto.setSite(productOrderItemListGetRequest.getSite());

        // キーワード
        goodsConditionDto.setKeywordLikeCondition1(productOrderItemListGetRequest.getKeywordLikeCondition1());
        goodsConditionDto.setKeywordLikeCondition2(productOrderItemListGetRequest.getKeywordLikeCondition2());
        goodsConditionDto.setKeywordLikeCondition3(productOrderItemListGetRequest.getKeywordLikeCondition3());
        goodsConditionDto.setKeywordLikeCondition4(productOrderItemListGetRequest.getKeywordLikeCondition4());
        goodsConditionDto.setKeywordLikeCondition5(productOrderItemListGetRequest.getKeywordLikeCondition5());
        goodsConditionDto.setKeywordLikeCondition6(productOrderItemListGetRequest.getKeywordLikeCondition6());
        goodsConditionDto.setKeywordLikeCondition7(productOrderItemListGetRequest.getKeywordLikeCondition7());
        goodsConditionDto.setKeywordLikeCondition8(productOrderItemListGetRequest.getKeywordLikeCondition8());
        goodsConditionDto.setKeywordLikeCondition9(productOrderItemListGetRequest.getKeywordLikeCondition9());
        goodsConditionDto.setKeywordLikeCondition10(productOrderItemListGetRequest.getKeywordLikeCondition10());
        // 商品グループコード
        goodsConditionDto.setGoodsGroupCode(productOrderItemListGetRequest.getGoodsGroupCode());
        // 商品コード
        goodsConditionDto.setGoodsCode(productOrderItemListGetRequest.getGoodsCode());
        // JAN/カタログコード
        goodsConditionDto.setJanCode(productOrderItemListGetRequest.getJanCode());
        // 商品名
        goodsConditionDto.setGoodsGroupName(productOrderItemListGetRequest.getGoodsGroupName());

        return goodsConditionDto;
    }

    /**
     * レスポンスに変換
     *
     * @param goodsSearchResultDtoList 検索結果リスト
     * @return 受注用商品一覧レスポンス
     */
    public ProductOrderItemListResponse toGoodsSearchResultForOrderRegistResponse(List<GoodsSearchResultForOrderRegistDto> goodsSearchResultDtoList) {

        ProductOrderItemListResponse productOrderItemListResponse = new ProductOrderItemListResponse();
        List<GoodsSearchResultForOrderRegistResponse> goodsSearchResultForOrderRegistList = new ArrayList<>();

        for (GoodsSearchResultForOrderRegistDto goodsSearchResultForOrderRegistDto : goodsSearchResultDtoList) {
            if (goodsSearchResultForOrderRegistDto.getSalesPossibleStock().intValue() > 0
                || HTypeStockManagementFlag.OFF.equals(goodsSearchResultForOrderRegistDto.getStockManagementFlag())) {
                GoodsSearchResultForOrderRegistResponse goodsSearchResultForOrderRegistResponse =
                                new GoodsSearchResultForOrderRegistResponse();
                goodsSearchResultForOrderRegistResponse.setGoodsSeq(goodsSearchResultForOrderRegistDto.getGoodsSeq());
                goodsSearchResultForOrderRegistResponse.setGoodsCode(goodsSearchResultForOrderRegistDto.getGoodsCode());
                goodsSearchResultForOrderRegistResponse.setJanCode(goodsSearchResultForOrderRegistDto.getJanCode());
                goodsSearchResultForOrderRegistResponse.setGoodsGroupName(
                                goodsSearchResultForOrderRegistDto.getGoodsGroupName());
                goodsSearchResultForOrderRegistResponse.setTaxRate(goodsSearchResultForOrderRegistDto.getTaxRate());
                goodsSearchResultForOrderRegistResponse.setGoodsPrice(
                                goodsSearchResultForOrderRegistDto.getGoodsPrice());
                goodsSearchResultForOrderRegistResponse.setGoodsTaxType(
                                EnumTypeUtil.getValue(goodsSearchResultForOrderRegistDto.getGoodsTaxType()));
                goodsSearchResultForOrderRegistResponse.setFreeDeliveryFlag(
                                EnumTypeUtil.getValue(goodsSearchResultForOrderRegistDto.getFreeDeliveryFlag()));
                goodsSearchResultForOrderRegistResponse.setUnitValue1(
                                goodsSearchResultForOrderRegistDto.getUnitValue1());
                goodsSearchResultForOrderRegistResponse.setUnitValue2(
                                goodsSearchResultForOrderRegistDto.getUnitValue2());
                goodsSearchResultForOrderRegistResponse.setGoodsGroupCode(
                                goodsSearchResultForOrderRegistDto.getGoodsGroupCode());
                goodsSearchResultForOrderRegistResponse.setIndividualDeliveryType(
                                EnumTypeUtil.getValue(goodsSearchResultForOrderRegistDto.getIndividualDeliveryType()));
                goodsSearchResultForOrderRegistResponse.setStockManagementFlag(
                                EnumTypeUtil.getValue(goodsSearchResultForOrderRegistDto.getStockManagementFlag()));
                goodsSearchResultForOrderRegistResponse.setSalesPossibleStock(
                                goodsSearchResultForOrderRegistDto.getSalesPossibleStock());
                goodsSearchResultForOrderRegistResponse.setUnitManagementFlag(
                                EnumTypeUtil.getValue(goodsSearchResultForOrderRegistDto.getUnitManagementFlag()));
                goodsSearchResultForOrderRegistResponse.setDeliveryType(
                                goodsSearchResultForOrderRegistDto.getDeliveryType());
                goodsSearchResultForOrderRegistResponse.setOrderSetting1(
                                goodsSearchResultForOrderRegistDto.getOrderSetting1());
                goodsSearchResultForOrderRegistResponse.setOrderSetting2(
                                goodsSearchResultForOrderRegistDto.getOrderSetting2());
                goodsSearchResultForOrderRegistResponse.setOrderSetting3(
                                goodsSearchResultForOrderRegistDto.getOrderSetting3());
                goodsSearchResultForOrderRegistResponse.setOrderSetting4(
                                goodsSearchResultForOrderRegistDto.getOrderSetting4());
                goodsSearchResultForOrderRegistResponse.setOrderSetting5(
                                goodsSearchResultForOrderRegistDto.getOrderSetting5());
                goodsSearchResultForOrderRegistResponse.setOrderSetting6(
                                goodsSearchResultForOrderRegistDto.getOrderSetting6());
                goodsSearchResultForOrderRegistResponse.setOrderSetting7(
                                goodsSearchResultForOrderRegistDto.getOrderSetting7());
                goodsSearchResultForOrderRegistResponse.setOrderSetting8(
                                goodsSearchResultForOrderRegistDto.getOrderSetting8());
                goodsSearchResultForOrderRegistResponse.setOrderSetting9(
                                goodsSearchResultForOrderRegistDto.getOrderSetting9());
                goodsSearchResultForOrderRegistResponse.setOrderSetting10(
                                goodsSearchResultForOrderRegistDto.getOrderSetting10());

                goodsSearchResultForOrderRegistList.add(goodsSearchResultForOrderRegistResponse);
            }
        }

        productOrderItemListResponse.setGoodsSearchResultForOrderRegistList(goodsSearchResultForOrderRegistList);

        return productOrderItemListResponse;
    }

    /**
     * 在庫なしメッセージ付加<br/>
     * ※在庫切れの規格値に在庫なしの文言を付加する
     *
     * @param value 文言付加対象文字列
     * @return 文字列
     */
    public String addNoStockMessage(String value) {
        return value + NO_STOCK_MESSAGE;
    }

    /**
     * 商品在庫チェック<br/>
     *
     * @param goodsUnitDto 商品規格Dto
     * @return 在庫あり:true,在庫なし:false
     */
    protected boolean isGoodsStock(GoodsUnitDto goodsUnitDto) {
        // 在庫の確認
        if (HTypeStockManagementFlag.OFF.equals(goodsUnitDto.getStockManagementFlag())) {
            // 在庫管理なし
            return true;
        } else if (goodsUnitDto.getSalesPossibleStock() != null
                   && goodsUnitDto.getSalesPossibleStock().compareTo(goodsUnitDto.getRemainderFewStock()) > 0) {
            // 在庫あり
            return true;
        } else if (goodsUnitDto.getSalesPossibleStock().intValue() > 0) {
            // 在庫あり
            return true;
        }
        return false;
    }

    /**
     * 商品販売チェック<br/>
     *
     * @param goodsUnitDto 商品規格Dto
     * @param targetTime   指定日時（プレビュー日時）
     * @return 販売可能:true,販売不可:false
     */
    protected boolean isGoodsSale(GoodsUnitDto goodsUnitDto, Timestamp targetTime) {

        if (ObjectUtils.isEmpty(targetTime)) {
            if (isGoodsSales(goodsUnitDto.getSaleStatusPC(), goodsUnitDto.getSaleStartTimePC(),
                             goodsUnitDto.getSaleEndTimePC()
                            )) {
                return true;
            }
        } else {
            if (isGoodsSales(goodsUnitDto.getSaleStatusPC(), goodsUnitDto.getSaleStartTimePC(),
                             goodsUnitDto.getSaleEndTimePC(), targetTime
                            )) {
                return true;
            }
        }
        return false;
    }

    /**
     * 商品販売判定<br/>
     * ※現在日時
     *
     * @param goodsSaleStatus 商品販売状態
     * @param saleStartTime   販売開始日時
     * @param saleEndTime     販売終了日時
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSales(HTypeGoodsSaleStatus goodsSaleStatus, Timestamp saleStartTime, Timestamp saleEndTime) {

        // 現在日時
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();
        return isGoodsSales(goodsSaleStatus, saleStartTime, saleEndTime, currentTime);
    }

    /**
     * 商品販売判定<br/>
     *
     * @param goodsSaleStatus 商品販売状態
     * @param saleStartTime   販売開始日時
     * @param saleEndTime     販売終了日時
     * @param targetTime      比較時間
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSales(HTypeGoodsSaleStatus goodsSaleStatus,
                                Timestamp saleStartTime,
                                Timestamp saleEndTime,
                                Timestamp targetTime) {

        // 販売
        if (HTypeGoodsSaleStatus.SALE.equals(goodsSaleStatus)) {
            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
            return dateHelper.isOpen(saleStartTime, saleEndTime, targetTime);
        }
        return false;
    }

    /**
     * 商品コードリストレスポンスクラスに変換
     *
     * @param checkGoodsList 商品情報リスト取得（商品コード）
     * @return 商品コードリストレスポンスクラス
     */
    public GoodsCodeListResponse toGoodsCodeListResponse(List<String> checkGoodsList) {

        if (CollectionUtils.isEmpty(checkGoodsList)) {
            return null;
        }
        GoodsCodeListResponse goodsCodeListResponse = new GoodsCodeListResponse();
        goodsCodeListResponse.setCheckGoodsList(checkGoodsList);

        return goodsCodeListResponse;
    }

    /**
     * 商品検索CSVDLオプションリストレスポンスに変換
     *
     * @param dtoList 商品 CSVDL オプション DTO リスト
     * @return 商品検索CSVDLオプションリストレスポンス
     */
    public ProductCsvOptionListResponse toProductCsvOptionListResponse(List<ProductCsvDownloadOptionDto> dtoList) {

        if (org.springframework.util.ObjectUtils.isEmpty(dtoList)) {
            return null;
        }

        ProductCsvOptionListResponse response = new ProductCsvOptionListResponse();
        List<ProductCsvOptionResponse> optionList = new ArrayList<>();

        for (ProductCsvDownloadOptionDto dto : dtoList) {
            optionList.add(this.toProductCsvOptionResponse(dto));
        }
        response.setCsvDownloadOptionList(optionList);

        return response;
    }

    /**
     * DTO クラスをレスポンスに変換する
     *
     * @param dto 商品CSVDLオプションDTO
     * @return 商品検索CSVDLオプションレスポンス
     */
    public ProductCsvOptionResponse toProductCsvOptionResponse(ProductCsvDownloadOptionDto dto) {

        ProductCsvOptionResponse csvOption = new ProductCsvOptionResponse();
        csvOption.setOptionId(conversionUtility.toString(dto.getOptionId()));
        csvOption.setDefaultOptionName(dto.getDefaultOptionName());
        csvOption.setOptionName(dto.getOptionName());
        csvOption.setOutHeader(dto.isOutHeader());

        List<OptionContent> optionContentList = new ArrayList<>();
        for (OptionContentDto optionContentDto : dto.getOptionContent()) {
            OptionContent optionContent = new OptionContent();
            optionContent.setItemName(optionContentDto.getItemName());
            optionContent.setOrder(optionContentDto.getOrder());
            optionContent.setDefaultColumnLabel(optionContentDto.getDefaultColumnLabel());
            optionContent.setColumnLabel(optionContentDto.getColumnLabel());
            optionContent.setOutFlag(optionContentDto.isOutFlag());
            optionContent.setDefaultOrder(optionContentDto.getDefaultOrder());
            optionContentList.add(optionContent);
        }
        csvOption.setOptionContent(optionContentList);

        return csvOption;
    }

    /**
     * 更新のために商品 CSVDL オプション DTO に変換
     *
     * @param updateRequest 商品検索CSVDLオプションの更新リクエスト
     * @return 商品CSVDLオプションDTO
     */
    protected ProductCsvDownloadOptionDto toProductCSVDLUpdateDto(ProductCsvOptionUpdateRequest updateRequest) {
        ProductCsvDownloadOptionDto updateDto = new ProductCsvDownloadOptionDto();

        // クエリモデルに設定
        updateDto.setOptionId(conversionUtility.toInteger(updateRequest.getOptionId()));
        updateDto.setDefaultOptionName(updateRequest.getDefaultOptionName());
        updateDto.setOptionName(updateRequest.getOptionName());
        updateDto.setOutHeader(updateRequest.getOutHeader());

        if (CollectionUtils.isNotEmpty(updateRequest.getOptionContent())) {
            List<OptionContentDto> optionContentList = new ArrayList<>();
            for (OptionContent optionContentRequest : updateRequest.getOptionContent()) {

                OptionContentDto optionContent = new OptionContentDto();
                optionContent.setItemName(optionContentRequest.getItemName());
                optionContent.setDefaultColumnLabel(optionContentRequest.getDefaultColumnLabel());
                optionContent.setColumnLabel(optionContentRequest.getColumnLabel());
                optionContent.setOutFlag(optionContentRequest.getOutFlag());
                optionContent.setOrder(optionContentRequest.getOrder());
                optionContent.setDefaultOrder(optionContentRequest.getDefaultOrder());

                optionContentList.add(optionContent);
            }
            updateDto.setOptionContent(optionContentList);
        }
        return updateDto;
    }

    /**
     * 検索キーワード作成
     *
     * @param request 商品グループ検索条件リクエスト
     * @return 検索キー（必須）
     */
    public String createSearchKeyword(ProductListGetRequest request) {

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition1())) {
            sb.append(request.getKeywordLikeCondition1()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition2())) {
            sb.append(request.getKeywordLikeCondition2()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition3())) {
            sb.append(request.getKeywordLikeCondition3()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition4())) {
            sb.append(request.getKeywordLikeCondition4()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition5())) {
            sb.append(request.getKeywordLikeCondition5()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition6())) {
            sb.append(request.getKeywordLikeCondition6()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition7())) {
            sb.append(request.getKeywordLikeCondition7()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition8())) {
            sb.append(request.getKeywordLikeCondition8()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition9())) {
            sb.append(request.getKeywordLikeCondition9()).append(SPACE);
        }

        if (StringUtils.isNotEmpty(request.getKeywordLikeCondition10())) {
            sb.append(request.getKeywordLikeCondition10());
        }

        return sb.toString().strip();
    }

    /**
     * キーワード条件が null かをチェック
     *
     * @param request 商品グループ検索条件リクエスト
     * @return boolean
     */
    public boolean keywordLikeConditionIsNull(ProductListGetRequest request) {

        return StringUtil.isEmpty(request.getKeywordLikeCondition1()) && StringUtil.isEmpty(
                        request.getKeywordLikeCondition2()) && StringUtil.isEmpty(request.getKeywordLikeCondition3())
               && StringUtil.isEmpty(request.getKeywordLikeCondition4()) && StringUtil.isEmpty(
                        request.getKeywordLikeCondition5()) && StringUtil.isEmpty(request.getKeywordLikeCondition6())
               && StringUtil.isEmpty(request.getKeywordLikeCondition7()) && StringUtil.isEmpty(
                        request.getKeywordLikeCondition8()) && StringUtil.isEmpty(request.getKeywordLikeCondition9())
               && StringUtil.isEmpty(request.getKeywordLikeCondition10());

    }

    /**
     * CSVDL オプションのデフォルトのチームプレートを取得
     *
     * @return 商品CSVDLオプションDTO
     */
    public ProductCsvDownloadOptionDto getDefaultProductCsvOption() {
        List<OptionContentDto> contentDtoList = CsvOptionUtil.getListOptionContentDto(GoodsCsvDto.class);
        ProductCsvDownloadOptionDto defaultCsvDto = new ProductCsvDownloadOptionDto();
        defaultCsvDto.setOptionName(StringUtils.EMPTY);
        defaultCsvDto.setOutHeader(true);
        defaultCsvDto.setOptionContent(contentDtoList);

        return defaultCsvDto;
    }
}