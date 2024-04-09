/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathResponse;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.front.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.front.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.front.dto.goods.stock.StockDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.front.pc.web.front.goods.common.CategoryItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsIconItem;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.CategoryUtility;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品一覧画面 Helper
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
public class GoodsListHelper {

    /** カテゴリUtility */
    private final CategoryUtility categoryUtility;

    /** 商品系Helper */
    private final GoodsUtility goodsUtility;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /** パンくずDUMMY */
    private static final String BREADCRUMBDUMMY = "dummy";

    /**
     * コンストラクタ
     *
     * @param categoryUtility   カテゴリUtility
     * @param goodsUtility      商品系Helper
     * @param conversionUtility 変換Helper
     * @param dateUtility
     */
    @Autowired
    public GoodsListHelper(CategoryUtility categoryUtility,
                           GoodsUtility goodsUtility,
                           ConversionUtility conversionUtility,
                           DateUtility dateUtility) {
        this.categoryUtility = categoryUtility;
        this.goodsUtility = goodsUtility;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     *
     * 画面表示・再表示<br/>
     * カテゴリ情報をページクラスにセット<br />
     *
     * @param categoryDetailsDto カテゴリ詳細DTO
     * @param goodsListModel
     */
    public void toPageForLoad(CategoryDetailsDto categoryDetailsDto, GoodsListModel goodsListModel) {

        // カテゴリが取得できない場合は、終了
        goodsListModel.setCid(null);
        if (categoryDetailsDto == null) {
            return;
        }

        // 表示カテゴリ情報をページクラスにセット
        goodsListModel.setCid(categoryDetailsDto.getCategoryId());
        goodsListModel.setCategoryImagePC(null);

        if (categoryDetailsDto.getCategoryImagePC() != null) {
            String contextPath = PropertiesUtil.getSystemPropertiesValue("server.contextPath");
            goodsListModel.setCategoryImagePC(contextPath + categoryDetailsDto.getCategoryImagePC());
        }
        goodsListModel.setMetaDescription(categoryDetailsDto.getMetaDescription());
        goodsListModel.setManualDisplayFlag(categoryDetailsDto.getManualDisplayFlag());
        goodsListModel.setFreeTextPC(categoryDetailsDto.getFreeTextPC());

        // ブラウザタイトル用に利用
        goodsListModel.setCategoryName(categoryDetailsDto.getCategoryNamePC());

        // プレビュー用の制御項目を設定
        goodsListModel.setFrontDisplay(EnumTypeUtil.getValue(categoryDetailsDto.getFrontDisplay()));

        goodsListModel.setWarningMessage(categoryDetailsDto.getWarningMessage());
    }

    /**
     *
     * 画面表示・再表示<br/>
     * パンくず情報をページクラスにセット<br />
     *
     * @param topicPathListResponse パンくずリストレスポンス
     * @param goodsListModel
     */
    public void toPageForLoadForTopicPath(TopicPathListResponse topicPathListResponse, GoodsListModel goodsListModel) {
        // 処理前は存在しないためnullを返す
        if (topicPathListResponse == null || topicPathListResponse.getTopicPathList() == null) {
            return;
        }

        List<TopicPathResponse> topicPathList = topicPathListResponse.getTopicPathList();
        List<CategoryItem> itemsList = new ArrayList<>();
        for (int i = 0; i < topicPathList.size(); i++) {
            TopicPathResponse topicPathResponse = topicPathList.get(i);

            if (!BREADCRUMBDUMMY.equals(topicPathResponse.getCategoryId())) {// dummyは省く
                CategoryItem categoryItem = ApplicationContextUtility.getBean(CategoryItem.class);
                categoryItem.setCid(topicPathResponse.getCategoryId());
                categoryItem.setCategoryName(topicPathResponse.getDisplayName());
                categoryItem.setHierarchicalSerialNumber(topicPathResponse.getHierarchicalSerialNumber());
                itemsList.add(categoryItem);
            }
        }

        goodsListModel.setCategoryPassItems(itemsList);
    }

    /**
     *
     * 画面表示・再表示<br/>
     * 現在のカテゴリに属する商品一覧情報をページクラスに設定
     *
     * @param goodsGroupDtoList 商品グループリストDTO
     * @param col サムネイル横表示商品数
     * @param goodsListModel
     */
    public void toPageForLoadCurrentCategoryGoods(List<GoodsGroupDto> goodsGroupDtoList,
                                                  int col,
                                                  GoodsListModel goodsListModel) {

        if (CollectionUtils.isEmpty(goodsGroupDtoList)) {
            return;
        }
        List<GoodsGroupItem> itemsList = makeListPageItemList(goodsGroupDtoList, goodsListModel);

        // リスト
        goodsListModel.setGoodsGroupListItems(itemsList);

        // 縦リスト
        List<List<GoodsGroupItem>> listPageItemsItems = new ArrayList<>();

        // 横リスト
        List<GoodsGroupItem> listPageItems = new ArrayList<>();

        for (int i = 0; i < itemsList.size(); i++) {
            // 横表示毎にリストを作成
            if (i % col == 0) {
                listPageItems = new ArrayList<>();
            }

            // リストに追加
            listPageItems.add(itemsList.get(i));

            // 次のインデックスが横表示 or ラストインデックスの場合 縦リストに追加
            if ((i + 1) % col == 0 || i == (itemsList.size() - 1)) {
                listPageItemsItems.add(listPageItems);
            }

        }

        // サムネイルループリストにセット
        goodsListModel.setGoodsGroupThumbnailItemsItems(listPageItemsItems);
    }

    /**
     *
     * DTO一覧をitemクラスの一覧に変換する<br/>
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @param goodsListModel
     * @return カテゴリページアイテム情報一覧
     */
    protected List<GoodsGroupItem> makeListPageItemList(List<GoodsGroupDto> goodsGroupDtoList,
                                                        GoodsListModel goodsListModel) {

        List<GoodsGroupItem> itemsList = new ArrayList<>();
        String currentCategoryId = goodsListModel.getCid();
        String currentCategoryHsn = goodsListModel.getHsn();
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();

            GoodsGroupItem listPageItem = ApplicationContextUtility.getBean(GoodsGroupItem.class);
            listPageItem.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
            listPageItem.setGgcd(goodsGroupEntity.getGoodsGroupCode());
            listPageItem.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());

            // 税率
            BigDecimal taxRate = goodsGroupEntity.getTaxRate();
            listPageItem.setTaxRate(taxRate);

            // 商品説明1~10
            GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();
            if (!ObjectUtils.isEmpty(goodsGroupDisplayEntity)) {
                listPageItem.setGoodsNote1(goodsGroupDisplayEntity.getGoodsNote1());
                listPageItem.setGoodsNote2(goodsGroupDisplayEntity.getGoodsNote2());
                listPageItem.setGoodsNote3(goodsGroupDisplayEntity.getGoodsNote3());
                listPageItem.setGoodsNote4(goodsGroupDisplayEntity.getGoodsNote4());
                listPageItem.setGoodsNote5(goodsGroupDisplayEntity.getGoodsNote5());
                listPageItem.setGoodsNote6(goodsGroupDisplayEntity.getGoodsNote6());
                listPageItem.setGoodsNote7(goodsGroupDisplayEntity.getGoodsNote7());
                listPageItem.setGoodsNote8(goodsGroupDisplayEntity.getGoodsNote8());
                listPageItem.setGoodsNote9(goodsGroupDisplayEntity.getGoodsNote9());
                listPageItem.setGoodsNote10(goodsGroupDisplayEntity.getGoodsNote10());
            }

            // 通常価格 - 税込計算
            BigDecimal goodsPrice = goodsGroupEntity.getGoodsPrice();
            listPageItem.setGoodsPrice(goodsPrice);
            listPageItem.setGoodsPriceInTax(goodsGroupEntity.getGoodsPriceInTax());

            if (currentCategoryId != null && !"".equals(currentCategoryId)) {
                listPageItem.setCid(currentCategoryId);
            }

            if (currentCategoryHsn != null && !"".equals(currentCategoryHsn)) {
                listPageItem.setHsn(currentCategoryHsn);
            }

            // 画像の取得
            List<String> goodsImageList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsGroupImageEntityList())) {
                for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
                    goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                }
            }
            listPageItem.setGoodsImageItems(goodsImageList);

            // アイコン情報の取得
            List<GoodsIconItem> goodsIconList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsInformationIconDetailsDtoList())) {
                for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsGroupDto.getGoodsInformationIconDetailsDtoList()) {

                    GoodsIconItem iconPageItem = ApplicationContextUtility.getBean(GoodsIconItem.class);
                    iconPageItem.setIconName(goodsInformationIconDetailsDto.getIconName());
                    iconPageItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

                    goodsIconList.add(iconPageItem);
                }
            }
            listPageItem.setGoodsIconItems(goodsIconList);

            if (goodsGroupEntity.getWhatsnewDate() != null) {
                // 新着画像の表示期間を取得
                Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsGroupEntity.getWhatsnewDate());
                listPageItem.setWhatsnewDate(whatsnewDate);
            }

            listPageItem.setStockStatusDisplay(false);
            if (!ObjectUtils.isEmpty(goodsGroupDto.getBatchUpdateStockStatus())
                && goodsGroupDto.getBatchUpdateStockStatus().getStockStatusPc() != null) {
                // 商品検索結果には在庫状況更新バッチ実行時点の在庫状況を表示する
                HTypeStockStatusType status = goodsGroupDto.getBatchUpdateStockStatus().getStockStatusPc();
                listPageItem.setStockStatusPc(EnumTypeUtil.getValue(status));

                // 商品グループ在庫の表示判定
                if (goodsUtility.isGoodsGroupStock(status)) {
                    listPageItem.setStockStatusDisplay(true);
                }
            }

            // プレビュー用の制御項目を設定
            if (StringUtils.isNotBlank(goodsListModel.getPreKey())) {
                listPageItem.setFrontDisplay(EnumTypeUtil.getValue(goodsGroupDto.getFrontDisplay()));
                listPageItem.setFrontDisplayReferenceDate(this.dateUtility.toTimestampValue(goodsListModel.getPreTime(),
                                                                                            this.dateUtility.YMD_HMS
                                                                                           ));
            }

            itemsList.add(listPageItem);
        }

        return itemsList;
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

                if (!ObjectUtils.isEmpty(goodsGroupResponse.getFrontDisplay())) {
                    goodsGroupDto.setFrontDisplay(EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class,
                                                                                goodsGroupResponse.getFrontDisplay()
                                                                               ));
                }

                goodsGroupDtoList.add(goodsGroupDto);
            }
        }

        return goodsGroupDtoList;
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

        if (!ObjectUtils.isEmpty(stockStatusDisplayResponse.getStockStatus())) {
            stockStatusDisplayEntity.setFrontDisplayStockStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                          stockStatusDisplayResponse.getStockStatus()
                                                         ));
        }

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
        goodsGroupDisplayEntity.setGoodsTag(goodsGroupDisplayResponse.getGoodsTag());
        goodsGroupDisplayEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupDisplayResponse.getRegistTime()));
        goodsGroupDisplayEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupDisplayResponse.getUpdateTime()));

        return goodsGroupDisplayEntity;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageEntityList(List<GoodsGroupImageResponse> goodsGroupImageResponseList) {
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
     * アイコン詳細Dtoリストに変換
     *
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスクラスリスト
     * @return アイコン詳細Dtoリスト
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();

        goodsInformationIconDetailsResponseList.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();
            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());
            goodsInformationIconDetailsDto.setShopSeq(1001);
            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * カテゴリ詳細Dtoクラスに変換
     *
     * @param categoryResponse カテゴリレスポンス
     * @return カテゴリ詳細Dto
     */
    protected CategoryDetailsDto toCategoryDetailsDto(CategoryResponse categoryResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(categoryResponse)) {
            return null;
        }

        CategoryDetailsDto categoryDetailsDto = new CategoryDetailsDto();

        categoryDetailsDto.setCategorySeq(categoryResponse.getCategorySeq());
        categoryDetailsDto.setCategoryId(categoryResponse.getCategoryId());
        categoryDetailsDto.setCategoryNamePC(categoryResponse.getCategoryName());
        if (categoryResponse.getCategoryType() != null) {
            categoryDetailsDto.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, categoryResponse.getCategoryType()));
        }

        if (categoryResponse.getCategoryOpenStatus() != null) {
            categoryDetailsDto.setCategoryOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class,
                                                                                     categoryResponse.getCategoryOpenStatus()
                                                                                    ));
        }
        categoryDetailsDto.setCategoryOpenStartTimePC(
                        conversionUtility.toTimestamp(categoryResponse.getCategoryOpenStartTime()));
        categoryDetailsDto.setCategoryOpenEndTimePC(
                        conversionUtility.toTimestamp(categoryResponse.getCategoryOpenEndTime()));
        if (categoryResponse.getCategoryType() != null) {
            categoryDetailsDto.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, categoryResponse.getCategoryType()));
        }
        categoryDetailsDto.setCategoryImagePC(categoryResponse.getCategoryImage());
        categoryDetailsDto.setMetaDescription(categoryResponse.getMetaDescription());
        categoryDetailsDto.setFreeTextPC(categoryResponse.getFreeText());
        categoryDetailsDto.setVersionNo(categoryResponse.getVersionNo());
        categoryDetailsDto.setRegistTime(conversionUtility.toTimestamp(categoryResponse.getRegistTime()));
        categoryDetailsDto.setUpdateTime(conversionUtility.toTimestamp(categoryResponse.getUpdateTime()));
        categoryDetailsDto.setGoodsSortColumn(categoryResponse.getGoodsSortColumn());
        if (categoryResponse.getGoodsSortOrder() != null) {
            categoryDetailsDto.setGoodsSortOrder(categoryResponse.getGoodsSortOrder());
        }
        if (!ObjectUtils.isEmpty(categoryResponse.getFrontDisplay())) {
            categoryDetailsDto.setFrontDisplay(EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class,
                                                                             categoryResponse.getFrontDisplay()
                                                                            ));
        }
        categoryDetailsDto.setWarningMessage(categoryResponse.getWarningMessage());

        return categoryDetailsDto;
    }

    /**
     * 商品グループ検索条件リクエストクラスに変換
     *
     * @param goodsGroupSearchForDaoConditionDto 商品グループDao用検索条件Dto
     * @param goodsListModel
     * @return 商品グループ検索条件リクエスト
     */
    protected ProductListGetRequest toProductListGetRequest(GoodsGroupSearchForDaoConditionDto goodsGroupSearchForDaoConditionDto,
                                                            GoodsListModel goodsListModel) {
        ProductListGetRequest productListGetRequest = new ProductListGetRequest();

        if (goodsGroupSearchForDaoConditionDto != null) {
            productListGetRequest.setCategoryId(goodsGroupSearchForDaoConditionDto.getCategoryId());
            productListGetRequest.setMinPrice(goodsGroupSearchForDaoConditionDto.getMinPrice());
            productListGetRequest.setMaxPrice(goodsGroupSearchForDaoConditionDto.getMaxPrice());
            productListGetRequest.setOpenStatus(goodsGroupSearchForDaoConditionDto.getOpenStatus().getValue());
        }
        if (StringUtils.isNotBlank(goodsListModel.getPreKey())) {
            productListGetRequest.setFrontDisplayReferenceDate(
                            this.dateUtility.toTimestampValue(goodsListModel.getPreTime(), this.dateUtility.YMD_HMS));
        }
        return productListGetRequest;
    }

}
