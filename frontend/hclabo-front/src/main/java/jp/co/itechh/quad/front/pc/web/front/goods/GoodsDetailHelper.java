/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListGetRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryRegistRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsInformationIconDetailResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathResponse;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.util.seasar.StringUtil;
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
import jp.co.itechh.quad.front.constant.type.HTypeSiteType;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.front.dto.goods.stock.StockDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.front.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.front.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.front.pc.web.front.goods.common.CategoryItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsIconItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsItem;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.front.utility.SnsUtility;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListGetRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品検索画面 Helper
 *
 * @author kn23834
 * @version $Revision : 1.0 $
 */
@Component
public class GoodsDetailHelper {

    /** パンくずDUMMY */
    private static final String BREADCRUMBDUMMY = "dummy";

    /**
     * 商品系ヘルパークラス
     */
    private final GoodsUtility goodsUtility;

    /**
     * SNS連携のUtil
     */
    private final SnsUtility snsUtility;

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * 日付関連Utilityクラス
     */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *  @param goodsUtility          商品系ヘルパークラス
     * @param snsUtility            SNS連携のUtil
     * @param conversionUtility     変換Utility取得
     * @param dateUtility
     */
    @Autowired
    public GoodsDetailHelper(GoodsUtility goodsUtility,
                             SnsUtility snsUtility,
                             ConversionUtility conversionUtility,
                             DateUtility dateUtility) {
        this.goodsUtility = goodsUtility;
        this.snsUtility = snsUtility;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * カテゴリ情報をページクラスにセット<br />
     *
     * @param categoryDetailsDto カテゴリ詳細DTO
     * @param goodsDetailModel   商品詳細画面 Model
     */
    public void toPageForLoadCategoryDto(CategoryDetailsDto categoryDetailsDto, GoodsDetailModel goodsDetailModel) {
        if (categoryDetailsDto == null) {
            goodsDetailModel.setCategoryName(null);
            goodsDetailModel.setCategoryImagePC(null);
        } else {
            goodsDetailModel.setCategoryName(categoryDetailsDto.getCategoryNamePC());
            if (categoryDetailsDto.getCategoryImagePC() != null) {
                String contexPath = PropertiesUtil.getSystemPropertiesValue("server.contextPath");
                goodsDetailModel.setCategoryImagePC(contexPath + categoryDetailsDto.getCategoryImagePC());
            }
        }
    }

    /**
     * パンくず情報をページクラスにセット<br />
     *
     * @param categoryDetailsDtoList 公開カテゴリパスリスト
     * @param goodsDetailModel       商品詳細画面 Model
     */
    public void toPageForLoadForTopicPath(List<CategoryDetailsDto> categoryDetailsDtoList,
                                          GoodsDetailModel goodsDetailModel) {
        List<CategoryItem> categoryItemList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(categoryDetailsDtoList)) {
            for (CategoryDetailsDto categoryDetailsDto : categoryDetailsDtoList) {
                CategoryItem categoryItem = ApplicationContextUtility.getBean(CategoryItem.class);
                categoryItem.setCid(categoryDetailsDto.getCategoryId());
                categoryItem.setCategoryName(categoryDetailsDto.getCategoryName());
                categoryItem.setFrontDisplayStatus(EnumTypeUtil.getValue(categoryDetailsDto.getFrontDisplay()));

                categoryItemList.add(categoryItem);
            }
        }
        goodsDetailModel.setCategoryPassItems(categoryItemList);
    }

    /**
     * 画面表示・再表示<br/>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     */
    public void toPageForLoad(GoodsGroupDto goodsGroupDto, GoodsDetailModel goodsDetailModel) {
        // 初期値の設定
        goodsDetailModel.setGoodsGroupSeq(null);
        // 存在チェック
        // 条件を満たすことはないのでデッドコードになっている
        if (goodsGroupDto == null) {
            return;
        }

        // 商品グループ情報の設定
        setGoodsGroup(goodsGroupDto, goodsDetailModel);

        // 商品グループ表示情報の設定
        setGoodsGroupDisplay(goodsGroupDto, goodsDetailModel);

        // 商品在庫情報の設定
        setGoodsStock(goodsGroupDto, goodsDetailModel);

        // 商品画像の振り分け
        setGoodsImage(goodsGroupDto, goodsDetailModel);

        // 規格選択プルダウンの作成
        setGoodsUnit(goodsGroupDto, goodsDetailModel);

        // 在庫状況表示の設定
        setGoodsGroupStock(goodsGroupDto, goodsDetailModel);

        // SNS連携情報の設定
        setSnsInfo(goodsGroupDto, goodsDetailModel);

        // プレビュー画面の設定
        if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
            setPreview(goodsGroupDto, goodsDetailModel);
        }
    }

    /**
     * 商品グループ情報の設定<br/>
     * <pre>
     * 商品グループDTOから必要な値を取出し、
     * 商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setGoodsGroup(GoodsGroupDto goodsGroupDto,
                                 GoodsDetailModel goodsDetailModel,
                                 Object... customParams) {
        // 商品グループエンティティ
        GoodsGroupEntity entity = goodsGroupDto.getGoodsGroupEntity();

        // 商品グループ情報の設定
        // 条件を満たすことはないのでデッドコードになっている
        if (ObjectUtils.isEmpty(entity)) {
            return;
        }
        goodsDetailModel.setGoodsGroupSeq(entity.getGoodsGroupSeq());
        goodsDetailModel.setGgcd(entity.getGoodsGroupCode());

        if (entity.getWhatsnewDate() != null) {
            // 新着画像の表示期間を取得
            Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(entity.getWhatsnewDate());
            goodsDetailModel.setWhatsnewDate(whatsnewDate);
        }

        goodsDetailModel.setGoodsGroupName(entity.getGoodsGroupName());

        if (goodsDetailModel.getUnitSelect1() == null && goodsDetailModel.getGcd() != null) {
            // 通常商品
            goodsDetailModel.setUnitSelect1(goodsDetailModel.getGcd());
        }

        // 商品表示単価 設定
        goodsDetailModel.setGoodsPrice(entity.getGoodsPrice());
        goodsDetailModel.setGoodsPriceInTax(entity.getGoodsPriceInTax());

    }

    /**
     * 商品グループ表示情報の設定<br/>
     * <pre>
     * 商品グループDTOから必要な値を取出し、
     * 商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setGoodsGroupDisplay(GoodsGroupDto goodsGroupDto,
                                        GoodsDetailModel goodsDetailModel,
                                        Object... customParams) {
        // 商品グループエンティティ
        GoodsGroupDisplayEntity entity = goodsGroupDto.getGoodsGroupDisplayEntity();

        // 商品グループ表示情報の設定
        // 条件を満たすことはないのでデッドコードになっている
        if (ObjectUtils.isEmpty(entity)) {
            return;
        }
        // 商品説明１～１０
        goodsDetailModel.setGoodsNote1(entity.getGoodsNote1());
        goodsDetailModel.setGoodsNote2(entity.getGoodsNote2());
        goodsDetailModel.setGoodsNote3(entity.getGoodsNote3());
        goodsDetailModel.setGoodsNote4(entity.getGoodsNote4());
        goodsDetailModel.setGoodsNote5(entity.getGoodsNote5());
        goodsDetailModel.setGoodsNote6(entity.getGoodsNote6());
        goodsDetailModel.setGoodsNote7(entity.getGoodsNote7());
        goodsDetailModel.setGoodsNote8(entity.getGoodsNote8());
        goodsDetailModel.setGoodsNote9(entity.getGoodsNote9());
        goodsDetailModel.setGoodsNote10(entity.getGoodsNote10());

        // 受注連携設定１～１０を一応セットしておく（使うかどうかは案件判断）
        goodsDetailModel.setOrderSetting1(entity.getOrderSetting1());
        goodsDetailModel.setOrderSetting2(entity.getOrderSetting2());
        goodsDetailModel.setOrderSetting3(entity.getOrderSetting3());
        goodsDetailModel.setOrderSetting4(entity.getOrderSetting4());
        goodsDetailModel.setOrderSetting5(entity.getOrderSetting5());
        goodsDetailModel.setOrderSetting6(entity.getOrderSetting6());
        goodsDetailModel.setOrderSetting7(entity.getOrderSetting7());
        goodsDetailModel.setOrderSetting8(entity.getOrderSetting8());
        goodsDetailModel.setOrderSetting9(entity.getOrderSetting9());
        goodsDetailModel.setOrderSetting10(entity.getOrderSetting10());

        goodsDetailModel.setUnitManagementFlag(entity.getUnitManagementFlag().getValue());
        goodsDetailModel.setUnitTitle1(entity.getUnitTitle1());
        goodsDetailModel.setUnitTitle2(entity.getUnitTitle2());
        goodsDetailModel.setErrorUnitTitle1(entity.getUnitTitle1());
        goodsDetailModel.setErrorUnitTitle2(entity.getUnitTitle2());
        goodsDetailModel.setMetaDescription(entity.getMetaDescription());
        goodsDetailModel.setMetaKeyword(entity.getMetaKeyword());
        // 商品納期
        // 商品DTOリストより取得していたが、商品グループ表示エンティティに同値を保持しているため、
        // 商品グループ表示エンティティより取得する。（フロントMBと同じ挙動とする）
        goodsDetailModel.setDeliveryType(entity.getDeliveryType());

        if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsInformationIconDetailsDtoList())) {
            // インフォメーションアイコン情報の設定
            List<GoodsIconItem> informationIconList = new ArrayList<>();
            for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsGroupDto.getGoodsInformationIconDetailsDtoList()) {
                GoodsIconItem goodsIconItem = ApplicationContextUtility.getBean(GoodsIconItem.class);
                goodsIconItem.setIconName(goodsInformationIconDetailsDto.getIconName());
                goodsIconItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

                informationIconList.add(goodsIconItem);
            }
            goodsDetailModel.setInformationIconItems(informationIconList);
        }
    }

    /**
     * 商品在庫情報の設定<br/>
     * <pre>
     * 商品グループDTOから必要な値を取出し、
     * 商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setGoodsStock(GoodsGroupDto goodsGroupDto,
                                 GoodsDetailModel goodsDetailModel,
                                 Object... customParams) {
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList();
        // 在庫表示用リスト
        List<GoodsStockItem> goodsStockItems = new ArrayList<>();

        goodsDetailModel.setExistsSaleStatusGoods(false);

        if (!CollectionUtils.isEmpty(goodsDtoList)) {
            for (GoodsDto goodsDto : goodsDtoList) {
                // 商品エンティティ
                GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
                // 在庫DTO
                StockDto stockDto = goodsDto.getStockDto();
                // 販売状態チェック
                if (goodsUtility.isGoodsSales(goodsEntity.getSaleStatusPC(), goodsEntity.getSaleStartTimePC(),
                                              goodsDto.getGoodsEntity().getSaleEndTimePC()
                                             )) {
                    goodsDetailModel.setExistsSaleStatusGoods(true);
                }

                // ページアイテムクラスに、商品情報をセット
                GoodsStockItem goodsStockItem = ApplicationContextUtility.getBean(GoodsStockItem.class);
                goodsStockItem.setGoodsSeq(goodsEntity.getGoodsSeq());
                goodsStockItem.setGcd(goodsEntity.getGoodsCode());
                goodsStockItem.setGoodsPrice(goodsGroupDto.getGoodsGroupEntity().getGoodsPrice());
                goodsStockItem.setGoodsPriceInTax(goodsGroupDto.getGoodsGroupEntity().getGoodsPriceInTax());
                goodsStockItem.setTaxRate(goodsGroupDto.getGoodsGroupEntity().getTaxRate());
                goodsStockItem.setStockManagementFlag(goodsEntity.getStockManagementFlag());
                goodsStockItem.setIndividualDeliveryType(goodsEntity.getIndividualDeliveryType());
                if (!ObjectUtils.isEmpty(goodsGroupDto.getGoodsGroupDisplayEntity())) {
                    goodsStockItem.setUnitTitle1(goodsGroupDto.getGoodsGroupDisplayEntity().getUnitTitle1());
                    goodsStockItem.setUnitTitle2(goodsGroupDto.getGoodsGroupDisplayEntity().getUnitTitle2());
                }
                goodsStockItem.setUnitValue1(goodsEntity.getUnitValue1());
                goodsStockItem.setUnitValue2(goodsEntity.getUnitValue2());
                if (goodsEntity.getStockManagementFlag() == HTypeStockManagementFlag.OFF) {
                    goodsStockItem.setSalesPossibleStock(null);
                } else {
                    goodsStockItem.setSalesPossibleStock(stockDto.getSalesPossibleStock());
                }
                // プレビュー表示用の在庫状態文字列を設定
                if (StringUtils.isNotBlank(goodsDetailModel.getPreKey()) && !ObjectUtils.isEmpty(
                                goodsDto.getFrontDisplayStockStatus())) {
                    goodsStockItem.setStockTextType(EnumTypeUtil.getValue(goodsDto.getFrontDisplayStockStatus()));
                }
                // 通常表示用の在庫状態文字列を設定
                else if (goodsDto.getStockStatusPc() != null) {
                    goodsStockItem.setStockTextType(EnumTypeUtil.getValue(goodsDto.getStockStatusPc()));
                }

                goodsStockItems.add(goodsStockItem);

            }
        }
        goodsDetailModel.setGoodsStockItems(goodsStockItems);
    }

    /**
     * 商品画像の設定<br/>
     * <pre>
     * 商品グループDTOから必要な値を取出し、
     * 商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setGoodsImage(GoodsGroupDto goodsGroupDto,
                                 GoodsDetailModel goodsDetailModel,
                                 Object... customParams) {

        // 商品グループ画像の取得
        List<String> groupImageItems = createGroupImageItems(goodsGroupDto);

        // 取得した画像をページにセット
        goodsDetailModel.setGoodsImageItems(groupImageItems);
    }

    /**
     * 商品グループ画像の取得<br/>
     *
     * @param goodsGroupDto 商品グループDTO
     * @return 商品グループ画像リスト
     */
    protected List<String> createGroupImageItems(GoodsGroupDto goodsGroupDto) {

        List<String> groupImageList = new ArrayList<>();

        if (CollectionUtils.isEmpty(goodsGroupDto.getGoodsGroupImageEntityList())) {
            return groupImageList;
        }

        for (GoodsGroupImageEntity entity : goodsGroupDto.getGoodsGroupImageEntityList()) {
            // 商品画像パスを取得
            String imagePath = entity.getImageFileName();
            groupImageList.add(imagePath);
        }

        return groupImageList;
    }

    /**
     * 商品規格の設定<br/>
     * <pre>
     * 商品グループDTOから必要な値を取出し、
     * 商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setGoodsUnit(GoodsGroupDto goodsGroupDto,
                                GoodsDetailModel goodsDetailModel,
                                Object... customParams) {
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList();

        // プルダウンを初期化
        goodsDetailModel.setUnitSelect1Items(null);
        goodsDetailModel.setUnitSelect2Items(null);

        // 規格管理なしの場合、商品コードをセットして終了
        if (StringUtils.isNotEmpty(goodsDetailModel.getUnitManagementFlag()) && !HTypeUnitManagementFlag.ON.getValue()
                                                                                                           .equals(goodsDetailModel.getUnitManagementFlag())) {
            if (!CollectionUtils.isEmpty(goodsDtoList) && !ObjectUtils.isEmpty(goodsDtoList.get(0).getGoodsEntity())) {
                goodsDetailModel.setGcd(((goodsDtoList.get(0)).getGoodsEntity().getGoodsCode()));
            }
            return;
        }

        // 商品コードMAP(key=商品コード、value=規格配列[規格１、規格２])
        Map<String, String[]> gcdMap = new HashMap<>();
        // 規格１MAP(key=規格１、value=規格２配列[商品コード、規格２])
        Map<String, List<String[]>> unit1Map = new LinkedHashMap<>();

        if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsDtoList())) {
            Timestamp previewDate = null;
            if (StringUtils.isNotBlank(goodsDetailModel.getPreviewDate()) && StringUtils.isNotBlank(
                            goodsDetailModel.getPreviewTime())) {
                previewDate = this.dateUtility.toTimestampValue(
                                goodsDetailModel.getPreviewDate() + goodsDetailModel.getPreviewTime(),
                                this.dateUtility.YMD_SLASH + this.dateUtility.HMS
                                                               );
            }
            goodsUtility.createAllUnitMap(goodsGroupDto.getGoodsDtoList(), gcdMap, unit1Map, previewDate);

            // プルダウン作成
            this.createUnitList(goodsDetailModel, gcdMap, unit1Map);
        }
    }

    /**
     * 規格プルダウン作成<br/>
     *
     * @param goodsDetailModel 商品詳細画面 Model
     * @param gcdMap           商品コードMAP(key=商品コード、value=規格配列[規格１、規格２])
     * @param unit1Map         規格１MAP(key=規格１、value=規格２配列[商品コード、規格２])
     */
    protected void createUnitList(GoodsDetailModel goodsDetailModel,
                                  Map<String, String[]> gcdMap,
                                  Map<String, List<String[]>> unit1Map) {
        Map<String, String> unitValue1Map = new LinkedHashMap<>();
        Map<String, String> unitValue2Map = new LinkedHashMap<>();

        // パラメータの商品コード有無により処理を分岐
        if (StringUtils.isEmpty(goodsDetailModel.getGcd())) {
            // 商品コードなし(商品グループコードあり)の場合
            for (Map.Entry<String, List<String[]>> unit1MapEntry : unit1Map.entrySet()) {
                // 規格１表示用のリストを作成
                String unit1Label;
                if (goodsDetailModel.isUseUnit2()) {
                    unit1Label = unit1MapEntry.getKey();
                } else {
                    // 規格１のみの場合、在庫なしメッセージを付加する
                    if (Objects.equals(unit1MapEntry.getValue().get(0)[2], "true")) {
                        unit1Label = unit1MapEntry.getKey();
                    } else {
                        unit1Label = goodsUtility.addNoStockMessage(unit1MapEntry.getKey());
                    }
                }
                unitValue1Map.put(unit1MapEntry.getValue().get(0)[0], unit1Label);
            }
        } else {
            // 商品コードありの場合
            String[] tmpUnit1Array = gcdMap.get(goodsDetailModel.getGcd());
            String tmpUnit1 = "";
            if (tmpUnit1Array != null) {
                // 該当の規格１を商品コードMAPから抽出
                tmpUnit1 = tmpUnit1Array[0];
            }
            // 規格１と該当商品に存在する規格２を出力する
            for (Map.Entry<String, List<String[]>> unit1MapEntry : unit1Map.entrySet()) {
                // 規格１表示用のリストを作成
                String unit1Label;
                if (goodsDetailModel.isUseUnit2()) {
                    unit1Label = unit1MapEntry.getKey();
                } else {
                    // 規格１のみの場合、在庫なしメッセージを付加する
                    if (Objects.equals(unit1MapEntry.getValue().get(0)[2], "true")) {
                        unit1Label = unit1MapEntry.getKey();
                    } else {
                        unit1Label = goodsUtility.addNoStockMessage(unit1MapEntry.getKey());
                    }
                }
                unitValue1Map.put(unit1MapEntry.getValue().get(0)[0], unit1Label);
                // 該当商品の規格１かどうか確認
                if (unit1MapEntry.getKey().equals(tmpUnit1)) {
                    // 規格１選択値を規格１MAPから選択しなおす
                    // ※正しいvalue値は該当する規格１MAP規格２配列の一番上の商品コードとなるため
                    goodsDetailModel.setUnitSelect1(unit1MapEntry.getValue().get(0)[0]);
                    // 規格２管理を行う商品かどうか確認
                    if (goodsDetailModel.isUseUnit2()) {
                        for (String[] tmpArray : unit1MapEntry.getValue()) {
                            // 規格２表示用のリストを作成
                            String unit2Label;
                            if (Objects.equals(tmpArray[2], "true")) {
                                unit2Label = tmpArray[1];
                            } else {
                                unit2Label = goodsUtility.addNoStockMessage(tmpArray[1]);
                            }
                            unitValue2Map.put(tmpArray[0], unit2Label);
                        }
                        // 規格２をセット
                        goodsDetailModel.setUnitSelect2(goodsDetailModel.getGcd());
                    }
                }
            }
        }
        // ページの規格１プルダウンをセット
        goodsDetailModel.setUnitSelect1Items(unitValue1Map);
        // ページの規格２プルダウンをセット
        goodsDetailModel.setUnitSelect2Items(unitValue2Map);
    }

    /**
     * 在庫状況を設定
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     */
    protected void setGoodsGroupStock(GoodsGroupDto goodsGroupDto, GoodsDetailModel goodsDetailModel) {
        goodsDetailModel.setStockStatusDisplay(false);

        if (!ObjectUtils.isEmpty(goodsGroupDto.getRealTimeStockStatus())
            && goodsGroupDto.getRealTimeStockStatus().getStockStatusPc() != null) {
            // 商品詳細画面には現時点での在庫状況を表示する
            HTypeStockStatusType status = goodsGroupDto.getRealTimeStockStatus().getStockStatusPc();
            goodsDetailModel.setStockStatusPc(EnumTypeUtil.getValue(status));

            // 商品グループ在庫の表示判定
            if (goodsUtility.isGoodsGroupStock(status)) {
                goodsDetailModel.setStockStatusDisplay(true);
            }
        }
    }

    /**
     * SNS連携用の各種設定を行う
     *
     * @param goodsGroupDto    商品グループDto
     * @param goodsDetailModel 商品詳細画面 Model
     * @param customParams     案件用引数
     */
    protected void setSnsInfo(GoodsGroupDto goodsGroupDto, GoodsDetailModel goodsDetailModel, Object... customParams) {
        GoodsGroupEntity goodsGroup = goodsGroupDto.getGoodsGroupEntity();
        // 商品マスタのSNS連携フラグを設定
        goodsDetailModel.setSnsLinkDisplay((HTypeSnsLinkFlag.ON == goodsGroup.getSnsLinkFlag()));

        // SNSごとの利用フラグを設定
        goodsDetailModel.setUseFacebook(snsUtility.isUseFacebook());
        goodsDetailModel.setUseTwitter(snsUtility.isUseTwitter());
        if (goodsDetailModel.isUseTwitter()) {
            // Twitter用メンションを取得
            goodsDetailModel.setTwitterVia(snsUtility.getTwitterVia());
        }
        goodsDetailModel.setUseLine(snsUtility.isUseLine());

        // SNSが全て利用しない場合、SNS連携ボタンの表示フィールドごと非表示
        if (!goodsDetailModel.isUseFacebook() && !goodsDetailModel.isUseTwitter() && !goodsDetailModel.isUseLine()) {
            goodsDetailModel.setSnsLinkDisplay(false);
        }
    }

    /**
     * プレビュー画面用の設定を行う
     *
     * @param goodsGroupDto    商品グループDto
     * @param goodsDetailModel 商品詳細画面 Model
     */
    protected void setPreview(GoodsGroupDto goodsGroupDto, GoodsDetailModel goodsDetailModel) {
        goodsDetailModel.setFrontDisplay(ObjectUtils.isEmpty(goodsGroupDto.getFrontDisplay()) ?
                                                         null :
                                                         goodsGroupDto.getFrontDisplay().getValue());
        goodsDetailModel.setFrontDisplayStockStatus(
                        ObjectUtils.isEmpty(goodsGroupDto.getRealTimeStockStatus()) || ObjectUtils.isEmpty(
                                        goodsGroupDto.getRealTimeStockStatus().getFrontDisplayStockStatus()) ?
                                        null :
                                        goodsGroupDto.getRealTimeStockStatus().getFrontDisplayStockStatus().getValue());
        goodsDetailModel.setWarningMessage(goodsGroupDto.getWarningMessage());
    }

    /**
     * 画面表示・再表示(あしあと情報)<br/>
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @param goodsDetailModel  商品詳細画面 Model
     */
    public void toPageForLoadBrowsingHistory(List<GoodsGroupDto> goodsGroupDtoList, GoodsDetailModel goodsDetailModel) {
        goodsDetailModel.setBrowsingHistoryGoodsItems(getGoodsItemsForGoodsListDto(goodsGroupDtoList));
    }

    /**
     * 商品一覧DTOより、ページアイテムリストを作成する。<br/>
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @return ページアイテムリスト
     */
    protected List<GoodsGroupItem> getGoodsItemsForGoodsListDto(List<GoodsGroupDto> goodsGroupDtoList) {
        List<GoodsGroupItem> goodsItems = new ArrayList<>();
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            GoodsGroupItem goodsGroupItem = ApplicationContextUtility.getBean(GoodsGroupItem.class);

            GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
            List<GoodsGroupImageEntity> goodsGroupImageEntityList = goodsGroupDto.getGoodsGroupImageEntityList();

            if (goodsGroupEntity != null) {
                goodsGroupItem.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
                goodsGroupItem.setGgcd(goodsGroupEntity.getGoodsGroupCode());
                goodsGroupItem.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
                goodsGroupItem.setGoodsOpenStatus(goodsGroupEntity.getGoodsOpenStatusPC());
                goodsGroupItem.setOpenStartTime(goodsGroupEntity.getOpenStartTimePC());
                goodsGroupItem.setOpenEndTime(goodsGroupEntity.getOpenEndTimePC());
                goodsGroupItem.setGoodsTaxType(goodsGroupEntity.getGoodsTaxType());

                // 税率
                BigDecimal taxRate = goodsGroupEntity.getTaxRate();
                goodsGroupItem.setTaxRate(taxRate);

                // 通常価格 - 税込計算
                BigDecimal goodsPrice = goodsGroupEntity.getGoodsPrice();
                goodsGroupItem.setGoodsPrice(goodsPrice);
                goodsGroupItem.setGoodsPriceInTax(goodsGroupEntity.getGoodsPriceInTax());

                if (goodsGroupEntity.getWhatsnewDate() != null) {
                    // 新着画像の表示期間を取得
                    Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsGroupEntity.getWhatsnewDate());
                    goodsGroupItem.setWhatsnewDate(whatsnewDate);
                }

            }

            List<String> goodsImageList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(goodsGroupImageEntityList)) {
                // 商品画像リストを取り出す。
                for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
                    goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                }
            }
            goodsGroupItem.setGoodsImageItems(goodsImageList);

            // 在庫状況表示
            StockStatusDisplayEntity stockStatusDisplayEntity = goodsGroupDto.getBatchUpdateStockStatus();
            if (stockStatusDisplayEntity != null) {
                goodsGroupItem.setStockStatusPc(EnumTypeUtil.getValue(stockStatusDisplayEntity.getStockStatusPc()));
            }

            // アイコン情報の取得
            goodsGroupItem.setGoodsIconItems(
                            createGoodsIconList(goodsGroupDto.getGoodsInformationIconDetailsDtoList()));

            goodsItems.add(goodsGroupItem);

        }

        return goodsItems;
    }

    /**
     * お気に入り商品検索条件Dtoの作成
     *
     * @param wishlistGoodsLimit お気に入り商品件数
     * @param goodsDetailModel   商品詳細画面 Model
     * @return お気に入り検索条件Dto
     */
    public WishlistSearchForDaoConditionDto toWishlistConditionDtoForSearchWishlistList(int wishlistGoodsLimit,
                                                                                        GoodsDetailModel goodsDetailModel) {
        // お気に入り検索条件Dtoの作成 公開状態＝指定なし
        WishlistSearchForDaoConditionDto wishlistConditionDto =
                        ApplicationContextUtility.getBean(WishlistSearchForDaoConditionDto.class);
        wishlistConditionDto.setMemberInfoSeq(goodsDetailModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq());
        // PageInfoModule取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // ページングセットアップ
        wishlistConditionDto =
                        pageInfoModule.setupPageInfoForSkipCount(wishlistConditionDto, wishlistGoodsLimit, "updateTime",
                                                                 false
                                                                );
        return wishlistConditionDto;
    }

    /**
     * 画面表示・再表示(お気に入り情報)<br/>
     *
     * @param wishlistDtoList  お気に入りDTOリスト
     * @param goodsDetailModel 商品詳細画面 Model
     */
    public void toPageForLoadWishlist(List<WishlistDto> wishlistDtoList, GoodsDetailModel goodsDetailModel) {
        List<GoodsItem> goodsItems = new ArrayList<>();
        for (WishlistDto wishlistDto : wishlistDtoList) {
            // お気に入り商品情報取得
            GoodsDetailsDto goodsDetailsDto = wishlistDto.getGoodsDetailsDto();

            // ページアイテムクラスにセット
            GoodsItem goodsItem = ApplicationContextUtility.getBean(GoodsItem.class);
            if (goodsDetailsDto != null) {
                goodsItem.setGoodsGroupSeq(goodsDetailsDto.getGoodsGroupSeq());
                goodsItem.setGcd(goodsDetailsDto.getGoodsCode());
                goodsItem.setGoodsGroupName(goodsDetailsDto.getGoodsGroupName());
                goodsItem.setUnitTitle1(goodsDetailsDto.getUnitTitle1());
                goodsItem.setUnitValue1(goodsDetailsDto.getUnitValue1());
                goodsItem.setUnitTitle2(goodsDetailsDto.getUnitTitle2());
                goodsItem.setUnitValue2(goodsDetailsDto.getUnitValue2());
                goodsItem.setGoodsPrice(goodsDetailsDto.getGoodsPrice());

                // 税率、税種別の変換
                goodsItem.setTaxRate(goodsDetailsDto.getTaxRate());
                goodsItem.setGoodsTaxType(goodsDetailsDto.getGoodsTaxType());

                // 税込価格の計算
                goodsItem.setGoodsPriceInTax(goodsDetailsDto.getGoodsPriceInTax());

                if (goodsDetailsDto.getWhatsnewDate() != null) {
                    // 新着画像の表示期間を取得
                    Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsDetailsDto.getWhatsnewDate());
                    goodsItem.setWhatsnewDate(whatsnewDate);
                }

                goodsItem.setGoodsOpenStatus(goodsDetailsDto.getGoodsOpenStatusPC());
                goodsItem.setOpenStartTime(goodsDetailsDto.getOpenStartTimePC());
                goodsItem.setOpenEndTime(goodsDetailsDto.getOpenEndTimePC());

                goodsItem.setSaleStatus(goodsDetailsDto.getSaleStatusPC());
                goodsItem.setSaleStartTime(goodsDetailsDto.getSaleStartTimePC());
                goodsItem.setSaleEndTime(goodsDetailsDto.getSaleEndTimePC());

                goodsItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());

                // 商品画像リストを取り出す。
                List<String> goodsImageList = new ArrayList<>();
                if (goodsDetailsDto.getGoodsGroupImageEntityList() != null) {
                    for (GoodsGroupImageEntity goodsGroupImageEntity : goodsDetailsDto.getGoodsGroupImageEntityList()) {
                        goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                    }
                }
                goodsItem.setGoodsImageItems(goodsImageList);

            }

            // 在庫状況表示
            goodsItem.setStockStatusPc(wishlistDto.getStockStatus());
            // アイコン情報の取得
            goodsItem.setGoodsIconItems(createGoodsIconList(wishlistDto.getGoodsInformationIconDetailsDtoList()));

            goodsItems.add(goodsItem);

        }

        goodsDetailModel.setWishlistGoodsItems(goodsItems);
    }

    /**
     * お気に入り表示設定
     *
     * @param wishlistList     お気に入りリスト
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     */
    public void setWishlistView(List<WishlistEntity> wishlistList,
                                GoodsGroupDto goodsGroupDto,
                                GoodsDetailModel goodsDetailModel) {
        if (goodsDetailModel.getWishlistGoodsItems() != null && !CollectionUtils.isEmpty(wishlistList)) {
            StringBuilder wishlistGoodsCodeList = new StringBuilder();
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                for (WishlistEntity wishlistEntity : wishlistList) {
                    if (!ObjectUtils.isEmpty(goodsDto.getGoodsEntity())
                        && goodsDto.getGoodsEntity().getGoodsSeq() != null && goodsDto.getGoodsEntity()
                                                                                      .getGoodsSeq()
                                                                                      .equals(wishlistEntity.getGoodsSeq())) {
                        wishlistGoodsCodeList.append(goodsDto.getGoodsEntity().getGoodsCode()).append(",");
                    }
                }
            }
            goodsDetailModel.setWishlistGoodsCodeList(wishlistGoodsCodeList.toString());
        }
    }

    /**
     * アイコン情報を設定
     *
     * @param goodsInformationIconDetailsDtoList 登録されているアイコン情報
     * @return 画面表示用に作成したアイコンリスト
     */
    protected List<GoodsIconItem> createGoodsIconList(List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsInformationIconDetailsDtoList)) {
            return null;
        }
        List<GoodsIconItem> goodsIconList = new ArrayList<>();

        for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsInformationIconDetailsDtoList) {
            GoodsIconItem goodsIconItem = ApplicationContextUtility.getBean(GoodsIconItem.class);
            goodsIconItem.setIconName(goodsInformationIconDetailsDto.getIconName());
            goodsIconItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

            goodsIconList.add(goodsIconItem);
        }
        return goodsIconList;
    }

    /**
     * 画面表示・再表示(関連商品情報)<br/>
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @param goodsDetailModel  商品詳細画面 Model
     */
    public void toPageForLoadRelated(List<GoodsGroupDto> goodsGroupDtoList, GoodsDetailModel goodsDetailModel) {

        List<GoodsGroupItem> goodsItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsGroupDtoList)) {
            for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {

                GoodsGroupItem goodsGroupItem = ApplicationContextUtility.getBean(GoodsGroupItem.class);

                GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
                List<GoodsGroupImageEntity> goodsGroupImageEntityList = goodsGroupDto.getGoodsGroupImageEntityList();

                if (goodsGroupEntity != null) {
                    goodsGroupItem.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
                    goodsGroupItem.setGgcd(goodsGroupEntity.getGoodsGroupCode());
                    goodsGroupItem.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
                    // 税率
                    BigDecimal taxRate = goodsGroupEntity.getTaxRate();
                    goodsGroupItem.setTaxRate(taxRate);

                    // 通常価格 - 税込計算
                    BigDecimal goodsPrice = goodsGroupEntity.getGoodsPrice();
                    goodsGroupItem.setGoodsPrice(goodsPrice);
                    goodsGroupItem.setGoodsPriceInTax(goodsGroupEntity.getGoodsPriceInTax());

                    if (goodsGroupEntity.getWhatsnewDate() != null) {
                        // 新着画像の表示期間を取得
                        Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsGroupEntity.getWhatsnewDate());
                        goodsGroupItem.setWhatsnewDate(whatsnewDate);
                    }
                }

                List<String> goodsImageList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(goodsGroupImageEntityList)) {
                    // 商品画像リストを取り出す。
                    for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
                        goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                    }
                }
                goodsGroupItem.setGoodsImageItems(goodsImageList);

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
                goodsGroupItem.setGoodsIconItems(goodsIconList);

                // 在庫状況表示
                StockStatusDisplayEntity stockStatusDisplayEntity = goodsGroupDto.getBatchUpdateStockStatus();
                if (!ObjectUtils.isEmpty(stockStatusDisplayEntity)
                    && stockStatusDisplayEntity.getStockStatusPc() != null) {
                    goodsGroupItem.setStockStatusPc(EnumTypeUtil.getValue(stockStatusDisplayEntity.getStockStatusPc()));
                }

                // プレビュー用の制御項目を設定
                if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
                    goodsGroupItem.setFrontDisplayReferenceDate(
                                    this.dateUtility.toTimestampValue(goodsDetailModel.getPreTime(),
                                                                      this.dateUtility.YMD_HMS
                                                                     ));
                }

                goodsItems.add(goodsGroupItem);
            }
        }

        goodsDetailModel.setRelatedGoodsItems(goodsItems);
    }

    /**
     * 商品グループ取得リクエストに変換
     *
     * @param goodsDetailModel 商品詳細画面 Model
     * @return 商品グループ取得リクエスト
     */
    public ProductDisplayGetRequest toProductGetRequest(GoodsDetailModel goodsDetailModel) {
        ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();

        productDisplayGetRequest.setGoodCode(goodsDetailModel.getGcd());
        productDisplayGetRequest.setOpenStatus(HTypeOpenDeleteStatus.OPEN.getValue());
        productDisplayGetRequest.setGoodsGroupCode(goodsDetailModel.getGgcd());
        productDisplayGetRequest.setSiteType(HTypeSiteType.FRONT_PC.getValue());
        if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
            productDisplayGetRequest.setFrontDisplayReferenceDate(
                            this.dateUtility.toTimestampValue(goodsDetailModel.getPreTime(), this.dateUtility.YMD_HMS));
        }
        return productDisplayGetRequest;
    }

    /**
     * 商品グループDtoクラスに変換
     *
     * @param productDisplayResponse 商品グループレスポンス
     * @return 商品グループDtoクラス
     */
    public GoodsGroupDto toGoodsGroupDto(ProductDisplayResponse productDisplayResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(productDisplayResponse)) {
            return null;
        }
        GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

        if (!ObjectUtils.isEmpty(productDisplayResponse.getGoodsGroup())) {
            goodsGroupDto.setGoodsGroupEntity(toGoodsGroupEntity(productDisplayResponse.getGoodsGroup()));
        }
        if (!ObjectUtils.isEmpty(productDisplayResponse.getBatchUpdateStockStatus())) {
            goodsGroupDto.setBatchUpdateStockStatus(
                            toStockStatusDisplayEntity(productDisplayResponse.getBatchUpdateStockStatus()));
        }
        if (!ObjectUtils.isEmpty(productDisplayResponse.getRealTimeStockStatus())) {
            goodsGroupDto.setRealTimeStockStatus(
                            toStockStatusDisplayEntity(productDisplayResponse.getRealTimeStockStatus()));
        }
        if (!ObjectUtils.isEmpty(productDisplayResponse.getGoodsGroupDisplay())) {
            goodsGroupDto.setGoodsGroupDisplayEntity(
                            toGoodsGroupDisplayEntity(productDisplayResponse.getGoodsGroupDisplay()));
        }
        if (!CollectionUtils.isEmpty(productDisplayResponse.getGoodsGroupImageResponseList())) {
            goodsGroupDto.setGoodsGroupImageEntityList(
                            toGoodsGroupImageEntityList(productDisplayResponse.getGoodsGroupImageResponseList()));
        }
        if (!CollectionUtils.isEmpty(productDisplayResponse.getGoodsResponseList())) {
            goodsGroupDto.setGoodsDtoList(toGoodDtoList(productDisplayResponse.getGoodsResponseList()));
        }
        if (!CollectionUtils.isEmpty(productDisplayResponse.getCategoryGoodsResponseList())) {
            goodsGroupDto.setCategoryGoodsEntityList(
                            toCategoryGoodsEntityList(productDisplayResponse.getCategoryGoodsResponseList()));
        }
        if (!CollectionUtils.isEmpty(productDisplayResponse.getGoodsInformationIconDetailsResponseList())) {
            goodsGroupDto.setGoodsInformationIconDetailsDtoList(toGoodsInformationIconDetailsDtoList(
                            productDisplayResponse.getGoodsInformationIconDetailsResponseList()));
        }
        goodsGroupDto.setTaxRate(productDisplayResponse.getTaxRate());
        // プレビュー用の制御項目を設定
        if (StringUtils.isNotBlank(productDisplayResponse.getFrontDisplay())) {
            goodsGroupDto.setFrontDisplay(EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class,
                                                                        productDisplayResponse.getFrontDisplay()
                                                                       ));
        }
        goodsGroupDto.setWarningMessage(productDisplayResponse.getWarningMessage());

        return goodsGroupDto;
    }

    /**
     * 商品グループクラスに変換
     *
     * @param goodsGroupSubResponse 商品詳細レスポンスクラス
     * @return 商品グループクラス
     */
    private GoodsGroupEntity toGoodsGroupEntity(GoodsGroupSubResponse goodsGroupSubResponse) {
        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(goodsGroupSubResponse.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(goodsGroupSubResponse.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(goodsGroupSubResponse.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(goodsGroupSubResponse.getGoodsPrice());
        goodsGroupEntity.setGoodsPriceInTax(goodsGroupSubResponse.getGoodsPriceInTax());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(goodsGroupSubResponse.getWhatsnewDate()));
        if (goodsGroupSubResponse.getGoodsOpenStatus() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                goodsGroupSubResponse.getGoodsOpenStatus()
                                                                               ));
        }
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenStartTime()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenEndTime()));
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
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupSubResponse.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupSubResponse.getUpdateTime()));

        return goodsGroupEntity;
    }

    /**
     * 商品グループクラスに変換
     *
     * @param goodsGroupResponse あしあと商品
     * @return 商品グループクラス
     */
    private GoodsGroupEntity toGoodsGroupEntity(GoodsGroupResponse goodsGroupResponse) {
        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(goodsGroupResponse.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(goodsGroupResponse.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(goodsGroupResponse.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(goodsGroupResponse.getGoodsPrice());
        goodsGroupEntity.setGoodsPriceInTax(goodsGroupResponse.getGoodsPriceInTax());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(goodsGroupResponse.getWhatsnewDate()));
        if (goodsGroupResponse.getGoodsOpenStatusPC() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                goodsGroupResponse.getGoodsOpenStatusPC()
                                                                               ));
        }
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(goodsGroupResponse.getOpenStartTimePC()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(goodsGroupResponse.getOpenEndTimePC()));
        if (goodsGroupResponse.getGoodsTaxType() != null) {
            goodsGroupEntity.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                           goodsGroupResponse.getGoodsTaxType()
                                                                          ));
        }
        goodsGroupEntity.setTaxRate(goodsGroupResponse.getTaxRate());
        if (goodsGroupResponse.getAlcoholFlag() != null) {
            goodsGroupEntity.setAlcoholFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, goodsGroupResponse.getAlcoholFlag()));
        }
        if (goodsGroupResponse.getSnsLinkFlag() != null) {
            goodsGroupEntity.setSnsLinkFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, goodsGroupResponse.getSnsLinkFlag()));
        }
        goodsGroupEntity.setVersionNo(goodsGroupResponse.getVersionNo());
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupResponse.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupResponse.getUpdateTime()));

        return goodsGroupEntity;
    }

    /**
     * 商品グループクラスに変換
     *
     * @param relationGoodsResponse 関連商品レスポンス
     * @return 商品グループクラス
     */
    private GoodsGroupEntity toGoodsGroupEntity(RelationGoodsResponse relationGoodsResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(relationGoodsResponse)) {
            return null;
        }

        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(relationGoodsResponse.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(relationGoodsResponse.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(relationGoodsResponse.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(relationGoodsResponse.getGoodsPrice());
        goodsGroupEntity.setGoodsPriceInTax(relationGoodsResponse.getGoodsPriceInTax());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(relationGoodsResponse.getWhatsnewDate()));
        if (relationGoodsResponse.getGoodsOpenStatus() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                relationGoodsResponse.getGoodsOpenStatus()
                                                                               ));
        }
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(relationGoodsResponse.getOpenStartTime()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(relationGoodsResponse.getOpenEndTime()));
        goodsGroupEntity.setTaxRate(relationGoodsResponse.getTaxRate());
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(relationGoodsResponse.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(relationGoodsResponse.getUpdateTime()));

        return goodsGroupEntity;
    }

    /**
     * 商品グループ在庫表示クラスに変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 商品グループ在庫表示クラス
     */
    private StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayResponse stockStatusDisplayResponse) {
        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(stockStatusDisplayResponse.getGoodsGroupSeq());
        if (stockStatusDisplayResponse.getStockStatus() != null) {
            stockStatusDisplayEntity.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                    stockStatusDisplayResponse.getStockStatus()
                                                                                   ));
        }
        stockStatusDisplayEntity.setRegistTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getUpdateTime()));
        if (stockStatusDisplayResponse.getFrontDisplayStockStatus() != null) {
            // プレビュー用の制御項目を設定
            stockStatusDisplayEntity.setFrontDisplayStockStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                          stockStatusDisplayResponse.getFrontDisplayStockStatus()
                                                         ));
        }

        return stockStatusDisplayEntity;
    }

    /**
     * 商品グループ在庫表示クラスに変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 商品グループ在庫表示クラス
     */
    private StockStatusDisplayEntity toStockStatusDisplayEntity1(jp.co.itechh.quad.relation.presentation.api.param.StockStatusDisplayResponse stockStatusDisplayResponse) {
        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(stockStatusDisplayResponse.getGoodsGroupSeq());
        if (stockStatusDisplayResponse.getStockStatus() != null) {
            stockStatusDisplayEntity.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                    stockStatusDisplayResponse.getStockStatus()
                                                                                   ));
        }
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
    private GoodsGroupDisplayEntity toGoodsGroupDisplayEntity(GoodsGroupDisplayResponse goodsGroupDisplayResponse) {
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = new GoodsGroupDisplayEntity();
        goodsGroupDisplayEntity.setGoodsGroupSeq(goodsGroupDisplayResponse.getGoodsGroupSeq());
        goodsGroupDisplayEntity.setInformationIconPC(goodsGroupDisplayResponse.getInformationIcon());
        goodsGroupDisplayEntity.setSearchKeyword(goodsGroupDisplayResponse.getSearchKeyword());
        goodsGroupDisplayEntity.setSearchKeywordEm(goodsGroupDisplayResponse.getSearchKeywordEmUc());
        if (goodsGroupDisplayResponse.getUnitManagementFlag() != null) {
            goodsGroupDisplayEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                        goodsGroupDisplayResponse.getUnitManagementFlag()
                                                                                       ));
        }

        // 規格タイトル
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

        // 受注連携設定
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
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponses 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    private List<GoodsGroupImageEntity> toGoodsGroupImageEntityList(List<GoodsGroupImageResponse> goodsGroupImageResponses) {
        List<GoodsGroupImageEntity> goodsGroupImageEntities = new ArrayList<>();

        for (GoodsGroupImageResponse goodsGroupImageResponse : goodsGroupImageResponses) {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupImageResponse.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageFileName(goodsGroupImageResponse.getImageFileName());
            goodsGroupImageEntity.setImageTypeVersionNo(goodsGroupImageResponse.getImageTypeVersionNo());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupImageResponse.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupImageResponse.getUpdateTime()));

            goodsGroupImageEntities.add(goodsGroupImageEntity);
        }

        return goodsGroupImageEntities;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponses 商品グループ画像クラス
     * @return 商品グループ画像クラスリスト
     */
    private List<GoodsGroupImageEntity> toGoodsGroupImageEntityList1(List<jp.co.itechh.quad.relation.presentation.api.param.GoodsGroupImageResponse> goodsGroupImageResponses) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsGroupImageResponses)) {
            return null;
        }

        List<GoodsGroupImageEntity> goodsGroupImageEntities = new ArrayList<>();

        goodsGroupImageResponses.forEach(item -> {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageFileName(item.getImageFileName());
            goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsGroupImageEntities.add(goodsGroupImageEntity);
        });

        return goodsGroupImageEntities;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponses 商品グループ画像クラス
     * @return 商品グループ画像クラスリスト
     */
    private List<GoodsGroupImageEntity> toGoodsGroupImageEntityList2(List<jp.co.itechh.quad.wishlist.presentation.api.param.GoodsGroupImageResponse> goodsGroupImageResponses) {
        List<GoodsGroupImageEntity> goodsGroupImageEntities = new ArrayList<>();

        goodsGroupImageResponses.forEach(item -> {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageFileName(item.getImageFileName());
            goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsGroupImageEntities.add(goodsGroupImageEntity);
        });

        return goodsGroupImageEntities;
    }

    /**
     * 商品Dtoクラスリストに変換
     *
     * @param goodsResponses 商品レスポンスクラスリスト
     * @return 商品Dtoクラスリスト
     */
    private List<GoodsDto> toGoodDtoList(List<GoodsResponse> goodsResponses) {
        List<GoodsDto> goodsDtos = new ArrayList<>();

        for (GoodsResponse goodsResponse : goodsResponses) {
            GoodsDto goodsDto = new GoodsDto();

            if (goodsResponse.getGoodsSub() != null) {
                goodsDto.setGoodsEntity(toGoodsEntity(goodsResponse.getGoodsSub()));
            }
            if (goodsResponse.getStock() != null) {
                goodsDto.setStockDto(toStockDto(goodsResponse.getStock()));
            }
            if (goodsResponse.getStockStatus() != null) {
                goodsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                        goodsResponse.getStockStatus()
                                                                       ));
            }
            if (goodsResponse.getFrontDisplayStockStatus() != null) {
                goodsDto.setFrontDisplayStockStatus(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                  goodsResponse.getFrontDisplayStockStatus()
                                                                                 ));
            }
            goodsDto.setDeleteFlg(goodsResponse.getDeleteFlg());

            goodsDtos.add(goodsDto);
        }
        return goodsDtos;
    }

    /**
     * 商品クラスに変換
     *
     * @param goodsSubResponse 商品クラス
     * @return 商品クラス
     */
    private GoodsEntity toGoodsEntity(GoodsSubResponse goodsSubResponse) {
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
    private StockDto toStockDto(StockResponse stockResponse) {
        StockDto stockDto = new StockDto();

        stockDto.setGoodsSeq(stockResponse.getGoodsSeq());
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
     * @param categoryGoodsResponses 商品レスポンスクラスc
     * @return カテゴリ登録商品クラスリスト
     */
    private List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsResponse> categoryGoodsResponses) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        categoryGoodsResponses.forEach(item -> {
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
     * アイコン詳細DTOクラスリストに変換
     *
     * @param goodsInformationIconDetailsResponses アイコン詳細レスポンスクラスリスト
     * @return アイコン詳細DTOクラスリスト
     */
    private List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponses) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtos = new ArrayList<>();

        goodsInformationIconDetailsResponses.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());

            goodsInformationIconDetailsDtos.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtos;
    }

    /**
     * アイコン詳細DTOクラスリストに変換
     *
     * @param goodsInformationIconDetailsResponses 商品レスポンスクラス
     * @return アイコン詳細DTOクラスリスト
     */
    private List<GoodsInformationIconDetailsDto> toRelationGoodsInformationIconDetailsDtoList(List<jp.co.itechh.quad.relation.presentation.api.param.GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponses) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtos = new ArrayList<>();

        goodsInformationIconDetailsResponses.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());

            goodsInformationIconDetailsDtos.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtos;
    }

    /**
     * アイコン詳細DTOクラスリストに変換
     *
     * @param goodsInformationIconDetailsResponses アイコン詳細DTOクラス
     * @return アイコン詳細DTOクラスリスト
     */
    private List<GoodsInformationIconDetailsDto> toWishlistGoodsInformationIconDetailsDtoList(List<jp.co.itechh.quad.wishlist.presentation.api.param.GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponses) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtos = new ArrayList<>();

        goodsInformationIconDetailsResponses.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());

            goodsInformationIconDetailsDtos.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtos;
    }

    /**
     * あしあと商品情報リスト取得リクエストに変換
     *
     * @param limit               取得件数
     * @param exceptGoodsGroupSeq 取得対象外の商品
     * @return あしあと商品情報リスト取得リクエスト browsingHistory list get request
     */
    public BrowsingHistoryListGetRequest toBrowsingHistoryListGetRequest(Integer limit, Integer exceptGoodsGroupSeq) {
        BrowsingHistoryListGetRequest browsingHistoryListGetRequest = new BrowsingHistoryListGetRequest();

        browsingHistoryListGetRequest.setBrowsingHistoryGoodsLimit(limit);
        browsingHistoryListGetRequest.setExceptGoodsGroupSeq(exceptGoodsGroupSeq);

        return browsingHistoryListGetRequest;
    }

    /**
     * 商品グループDtoクラスに変換
     *
     * @param browsingHistoryListResponse あしあと商品情報一覧レスポンス
     * @return 商品グループDtoクラスリスト
     */
    public List<GoodsGroupDto> toGoodsGroupDtoList(BrowsingHistoryListResponse browsingHistoryListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(browsingHistoryListResponse) || CollectionUtils.isEmpty(
                        browsingHistoryListResponse.getBrowsingHistoryResponse())) {
            return null;
        }

        List<GoodsGroupDto> goodsGroupDtos = new ArrayList<>();

        browsingHistoryListResponse.getBrowsingHistoryResponse().forEach(item -> {
            GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

            if (!ObjectUtils.isEmpty(item.getGoodsGroupResponse())) {
                goodsGroupDto.setGoodsGroupEntity(toGoodsGroupEntity(item.getGoodsGroupResponse()));
            }

            goodsGroupDto.setGoodsGroupImageEntityList(
                            toBrowsingHistoryGoodsGroupImageEntityList(item.getGoodsGroupImageResponseList()));

            goodsGroupDto.setBatchUpdateStockStatus(toBatchUpdateStockStatus(item.getStockStatusDisplayResponse()));

            goodsGroupDto.setGoodsInformationIconDetailsDtoList(toBrowsingHistoryGoodsInformationIconDetailsDtoList(
                            item.getGoodsInformationIconDetailResponse()));

            goodsGroupDtos.add(goodsGroupDto);
        });

        return goodsGroupDtos;
    }

    /**
     * 商品グループ画像クラスのリストに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像レスポンスリストのリスト
     * @return 商品グループ画像クラス
     */
    public List<GoodsGroupImageEntity> toBrowsingHistoryGoodsGroupImageEntityList(List<jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupImageResponse> goodsGroupImageResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsGroupImageResponseList)) {
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
     * アイコン詳細DTOクラスのリストに変換
     *
     * @param goodsInformationIconDetailResponseList アイコン詳細レスポンスのリスト
     * @return アイコン詳細DTOクラス
     */
    public List<GoodsInformationIconDetailsDto> toBrowsingHistoryGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailResponse> goodsInformationIconDetailResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsInformationIconDetailResponseList)) {
            return null;
        }

        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();

        Integer shopSeq = 1001;

        goodsInformationIconDetailResponseList.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setShopSeq(shopSeq);
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());

            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品グループDtoクラスリストに変換
     *
     * @param relationGoodsListResponse 関連商品一覧レスポンス
     * @return 商品グループDtoクラスリスト
     */
    public List<GoodsGroupDto> toGoodsGroupDtoList(RelationGoodsListResponse relationGoodsListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(relationGoodsListResponse) || CollectionUtils.isEmpty(
                        relationGoodsListResponse.getRelationGoodsList())) {
            return null;
        }

        List<GoodsGroupDto> goodsGroupDtos = new ArrayList<>();

        for (RelationGoodsResponse relationGoodsResponse : relationGoodsListResponse.getRelationGoodsList()) {
            GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
            goodsGroupDto.setGoodsGroupImageEntityList(
                            toGoodsGroupImageEntityList1(relationGoodsResponse.getGoodsGroupImageList()));
            goodsGroupDto.setGoodsGroupEntity(toGoodsGroupEntity(relationGoodsResponse));
            if (!ObjectUtils.isEmpty(relationGoodsResponse.getBatchUpdateStockStatus())) {
                goodsGroupDto.setBatchUpdateStockStatus(
                                toStockStatusDisplayEntity1(relationGoodsResponse.getBatchUpdateStockStatus()));
            }
            if (!CollectionUtils.isEmpty(relationGoodsResponse.getGoodsInformationIconDetailsList())) {
                goodsGroupDto.setGoodsInformationIconDetailsDtoList(toRelationGoodsInformationIconDetailsDtoList(
                                relationGoodsResponse.getGoodsInformationIconDetailsList()));
            }

            goodsGroupDtos.add(goodsGroupDto);
        }

        return goodsGroupDtos;
    }

    /**
     * お気に入りDtoクラスに変換
     *
     * @param wishlistListResponse お気に入り情報一覧レスポンス
     * @return お気に入りDtoクラスリスト
     */
    public List<WishlistDto> toWishlistDtoList(WishlistListResponse wishlistListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(wishlistListResponse) || CollectionUtils.isEmpty(
                        wishlistListResponse.getWishListList())) {
            return null;
        }

        List<WishlistDto> wishlistDtoList = new ArrayList<>();

        for (WishlistResponse wishlistResponse : wishlistListResponse.getWishListList()) {
            WishlistDto wishlistDto = new WishlistDto();
            if (wishlistResponse != null) {
                wishlistDto.setWishlistEntity(toWishlistEntity(wishlistResponse));
                wishlistDto.setGoodsDetailsDto(toGoodsDetailsDto(wishlistResponse));
                if (!CollectionUtils.isEmpty(wishlistResponse.getGoodsGroupImageResponseList())) {
                    wishlistDto.setGoodsGroupImageEntityList(
                                    toGoodsGroupImageEntityList2(wishlistResponse.getGoodsGroupImageResponseList()));
                }
                if (wishlistResponse.getGoodsInformationIconDetailsResponseList() != null) {
                    wishlistDto.setGoodsInformationIconDetailsDtoList(toWishlistGoodsInformationIconDetailsDtoList(
                                    wishlistResponse.getGoodsInformationIconDetailsResponseList()));
                }
                if (StringUtil.isNotEmpty(wishlistResponse.getStockStatusPc())) {
                    wishlistDto.setStockStatus(wishlistResponse.getStockStatusPc());
                }
            }

            wishlistDtoList.add(wishlistDto);
        }
        return wishlistDtoList;
    }

    /**
     * お気に入りクラスに変換
     *
     * @param wishlistResponse お気に入り情報レスポンス
     * @return お気に入りクラス
     */
    private WishlistEntity toWishlistEntity(WishlistResponse wishlistResponse) {
        WishlistEntity wishlistEntity = new WishlistEntity();

        wishlistEntity.setGoodsSeq(wishlistResponse.getGoodsSeq());
        wishlistEntity.setMemberInfoSeq(wishlistResponse.getMemberInfoSeq());
        wishlistEntity.setRegistTime(conversionUtility.toTimestamp(wishlistResponse.getRegistTime()));
        wishlistEntity.setUpdateTime(conversionUtility.toTimestamp(wishlistResponse.getUpdateTime()));

        return wishlistEntity;
    }

    /**
     * 商品詳細Dtoクラスに変換
     *
     * @param wishlistResponse お気に入り情報レスポンス
     * @return 商品詳細Dtoクラス
     */
    private GoodsDetailsDto toGoodsDetailsDto(WishlistResponse wishlistResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(wishlistResponse)) {
            return null;
        }

        GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

        goodsDetailsDto.setGoodsSeq(wishlistResponse.getGoodsSeq());
        goodsDetailsDto.setGoodsGroupSeq(wishlistResponse.getGoodsGroupSeq());
        goodsDetailsDto.setVersionNo(wishlistResponse.getVersionNo());
        goodsDetailsDto.setRegistTime(conversionUtility.toTimestamp(wishlistResponse.getRegistTime()));
        goodsDetailsDto.setUpdateTime(conversionUtility.toTimestamp(wishlistResponse.getUpdateTime()));
        goodsDetailsDto.setGoodsCode(wishlistResponse.getGoodsCode());
        if (wishlistResponse.getGoodsTaxType() != null) {
            goodsDetailsDto.setGoodsTaxType(
                            EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class, wishlistResponse.getGoodsTaxType()));
        }
        goodsDetailsDto.setTaxRate(wishlistResponse.getTaxRate());
        if (wishlistResponse.getAlcoholFlag() != null) {
            goodsDetailsDto.setAlcoholFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, wishlistResponse.getAlcoholFlag()));
        }
        goodsDetailsDto.setGoodsPriceInTax(wishlistResponse.getGoodsPriceInTax());
        goodsDetailsDto.setGoodsPrice(wishlistResponse.getGoodsPrice());
        goodsDetailsDto.setDeliveryType(wishlistResponse.getDeliveryType());
        if (wishlistResponse.getSaleStatusPC() != null) {
            goodsDetailsDto.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                          wishlistResponse.getSaleStatusPC()
                                                                         ));
        }
        goodsDetailsDto.setSaleStartTimePC(conversionUtility.toTimestamp(wishlistResponse.getSaleStartTimePC()));
        goodsDetailsDto.setSaleEndTimePC(conversionUtility.toTimestamp(wishlistResponse.getSaleEndTimePC()));
        if (wishlistResponse.getStockManagementFlag() != null) {
            goodsDetailsDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                 wishlistResponse.getStockManagementFlag()
                                                                                ));
        }
        if (wishlistResponse.getIndividualDeliveryType() != null) {
            goodsDetailsDto.setIndividualDeliveryType(EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                                    wishlistResponse.getIndividualDeliveryType()
                                                                                   ));
        }
        goodsDetailsDto.setPurchasedMax(wishlistResponse.getPurchasedMax());
        if (wishlistResponse.getFreeDeliveryFlag() != null) {
            goodsDetailsDto.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                              wishlistResponse.getFreeDeliveryFlag()
                                                                             ));
        }
        goodsDetailsDto.setOrderDisplay(wishlistResponse.getOrderDisplay());
        goodsDetailsDto.setUnitValue1(wishlistResponse.getUnitValue1());
        goodsDetailsDto.setUnitValue2(wishlistResponse.getUnitValue2());
        goodsDetailsDto.setJanCode(wishlistResponse.getJanCode());
        goodsDetailsDto.setSalesPossibleStock(wishlistResponse.getSalesPossibleStock());
        goodsDetailsDto.setRealStock(wishlistResponse.getRealStock());
        goodsDetailsDto.setOrderReserveStock(wishlistResponse.getOrderReserveStock());
        goodsDetailsDto.setRemainderFewStock(wishlistResponse.getRemainderFewStock());
        goodsDetailsDto.setOrderPointStock(wishlistResponse.getOrderPointStock());
        goodsDetailsDto.setSafetyStock(wishlistResponse.getSafetyStock());
        goodsDetailsDto.setGoodsGroupCode(wishlistResponse.getGoodsGroupCode());
        goodsDetailsDto.setWhatsnewDate(conversionUtility.toTimestamp(wishlistResponse.getWhatsnewDate()));
        goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                           wishlistResponse.getGoodsOpenStatusPC()
                                                                          ));
        goodsDetailsDto.setOpenStartTimePC(conversionUtility.toTimestamp(wishlistResponse.getOpenStartTimePC()));
        goodsDetailsDto.setOpenEndTimePC(conversionUtility.toTimestamp(wishlistResponse.getOpenEndTimePC()));
        goodsDetailsDto.setGoodsGroupName(wishlistResponse.getGoodsGroupName());
        goodsDetailsDto.setUnitTitle1(wishlistResponse.getUnitTitle1());
        goodsDetailsDto.setUnitTitle2(wishlistResponse.getUnitTitle2());
        if (!CollectionUtils.isEmpty(wishlistResponse.getGoodsGroupImageResponseList())) {
            goodsDetailsDto.setGoodsGroupImageEntityList(
                            toGoodsGroupImageEntityList2(wishlistResponse.getGoodsGroupImageResponseList()));
        }
        if (wishlistResponse.getSnsLinkFlag() != null) {
            goodsDetailsDto.setSnsLinkFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, wishlistResponse.getSnsLinkFlag()));
        }
        goodsDetailsDto.setMetaDescription(wishlistResponse.getMetaDescription());
        if (wishlistResponse.getStockStatusPc() != null) {
            goodsDetailsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                           wishlistResponse.getStockStatusPc()
                                                                          ));
        }
        if (wishlistResponse.getGoodsInformationIconDetailsResponseList() != null) {
            goodsDetailsDto.setGoodsIconList(toWishlistGoodsInformationIconDetailsDtoList(
                            wishlistResponse.getGoodsInformationIconDetailsResponseList()));
        }

        // 商品説明１～１０
        goodsDetailsDto.setGoodsNote1(wishlistResponse.getGoodsNote1());
        goodsDetailsDto.setGoodsNote2(wishlistResponse.getGoodsNote2());
        goodsDetailsDto.setGoodsNote3(wishlistResponse.getGoodsNote3());
        goodsDetailsDto.setGoodsNote4(wishlistResponse.getGoodsNote4());
        goodsDetailsDto.setGoodsNote5(wishlistResponse.getGoodsNote5());
        goodsDetailsDto.setGoodsNote6(wishlistResponse.getGoodsNote6());
        goodsDetailsDto.setGoodsNote7(wishlistResponse.getGoodsNote7());
        goodsDetailsDto.setGoodsNote8(wishlistResponse.getGoodsNote8());
        goodsDetailsDto.setGoodsNote9(wishlistResponse.getGoodsNote9());
        goodsDetailsDto.setGoodsNote10(wishlistResponse.getGoodsNote10());

        // 受注連携設定１～１０を一応セットしておく（使うかどうかは案件判断）
        goodsDetailsDto.setOrderSetting1(wishlistResponse.getOrderSetting1());
        goodsDetailsDto.setOrderSetting2(wishlistResponse.getOrderSetting2());
        goodsDetailsDto.setOrderSetting3(wishlistResponse.getOrderSetting3());
        goodsDetailsDto.setOrderSetting4(wishlistResponse.getOrderSetting4());
        goodsDetailsDto.setOrderSetting5(wishlistResponse.getOrderSetting5());
        goodsDetailsDto.setOrderSetting6(wishlistResponse.getOrderSetting6());
        goodsDetailsDto.setOrderSetting7(wishlistResponse.getOrderSetting7());
        goodsDetailsDto.setOrderSetting8(wishlistResponse.getOrderSetting8());
        goodsDetailsDto.setOrderSetting9(wishlistResponse.getOrderSetting9());
        goodsDetailsDto.setOrderSetting10(wishlistResponse.getOrderSetting10());

        return goodsDetailsDto;
    }

    /**
     * お気に入りクラスリストに変換
     *
     * @param wishlistListResponse お気に入り情報一覧レスポンス
     * @return お気に入りクラスリスト
     */
    public List<WishlistEntity> toWishlistEntityList(WishlistListResponse wishlistListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(wishlistListResponse) || CollectionUtils.isEmpty(
                        wishlistListResponse.getWishListList())) {
            return null;
        }

        List<WishlistEntity> wishlistEntities = new ArrayList<>();

        wishlistListResponse.getWishListList().forEach(item -> {
            WishlistEntity wishlistEntity = new WishlistEntity();

            wishlistEntity.setMemberInfoSeq(item.getMemberInfoSeq());
            wishlistEntity.setGoodsSeq(item.getGoodsSeq());
            wishlistEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            wishlistEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            wishlistEntities.add(wishlistEntity);

        });

        return wishlistEntities;
    }

    /**
     * カテゴリ詳細Dtoクラスに変換
     *
     * @param categoryResponse カテゴリレスポンス
     * @return カテゴリ詳細Dtoクラス
     */
    public CategoryDetailsDto toCategoryDetailsDto(CategoryResponse categoryResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(categoryResponse)) {
            return null;
        }

        CategoryDetailsDto categoryDetailsDto = new CategoryDetailsDto();

        categoryDetailsDto.setCategoryId(categoryResponse.getCategoryId());
        categoryDetailsDto.setCategoryNamePC(categoryResponse.getCategoryName());
        categoryDetailsDto.setCategoryNotePC(categoryResponse.getCategoryNote());
        categoryDetailsDto.setFreeTextPC(categoryResponse.getFreeText());
        categoryDetailsDto.setMetaDescription(categoryResponse.getMetaDescription());
        categoryDetailsDto.setCategoryImagePC(categoryResponse.getCategoryImage());
        categoryDetailsDto.setCategorySeq(categoryResponse.getCategorySeq());
        categoryDetailsDto.setCategoryName(categoryResponse.getCategoryName());
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

        categoryDetailsDto.setVersionNo(categoryResponse.getVersionNo());
        categoryDetailsDto.setRegistTime(conversionUtility.toTimestamp(categoryResponse.getRegistTime()));
        categoryDetailsDto.setUpdateTime(conversionUtility.toTimestamp(categoryResponse.getUpdateTime()));
        categoryDetailsDto.setDisplayRegistTime(conversionUtility.toTimestamp(categoryResponse.getDisplayRegistTime()));
        categoryDetailsDto.setDisplayUpdateTime(conversionUtility.toTimestamp(categoryResponse.getDisplayUpdateTime()));

        return categoryDetailsDto;
    }

    /**
     * カテゴリ詳細Dtoクラスリストに変換
     *
     * @param topicPathListResponse パンくずリストレスポンス
     * @return カテゴリ詳細Dtoクラスリスト
     */
    public List<CategoryDetailsDto> toCategoryDetailsDtoList(TopicPathListResponse topicPathListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(topicPathListResponse) || CollectionUtils.isEmpty(
                        topicPathListResponse.getTopicPathList())) {
            return null;
        }

        List<CategoryDetailsDto> categoryDetailsDtos = new ArrayList<>();
        List<TopicPathResponse> topicPathList = topicPathListResponse.getTopicPathList();
        for (int i = 0; i < topicPathList.size(); i++) {
            TopicPathResponse topicPathResponse = topicPathList.get(i);

            if (!BREADCRUMBDUMMY.equals(topicPathResponse.getCategoryId())) {// dummyは省く
                CategoryDetailsDto categoryDetailsDto = new CategoryDetailsDto();
                categoryDetailsDto.setCategoryId(topicPathResponse.getCategoryId());
                categoryDetailsDto.setCategoryNamePC(topicPathResponse.getDisplayName());
                categoryDetailsDto.setCategoryName(topicPathResponse.getDisplayName());
                categoryDetailsDtos.add(categoryDetailsDto);
            }
        }

        return categoryDetailsDtos;
    }

    /**
     * 関連商品一覧取得リクエストに変換
     *
     * @param goodsRelationSearchForDaoConditionDto 関連商品Dao用検索条件Dtoクラス
     * @param goodsDetailModel                      商品詳細画面 Model
     * @return 関連商品一覧取得リクエスト
     */
    public RelationGoodsListGetRequest toRelationGoodsListGetRequest(GoodsRelationSearchForDaoConditionDto goodsRelationSearchForDaoConditionDto,
                                                                     GoodsDetailModel goodsDetailModel) {

        RelationGoodsListGetRequest relationGoodsListGetRequest = new RelationGoodsListGetRequest();

        if (goodsRelationSearchForDaoConditionDto.getOpenStatus() != null) {
            relationGoodsListGetRequest.setOpenStatus(goodsRelationSearchForDaoConditionDto.getOpenStatus().getValue());
        }
        if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
            relationGoodsListGetRequest.setFrontDisplayReferenceDate(
                            this.dateUtility.toTimestampValue(goodsDetailModel.getPreTime(), this.dateUtility.YMD_HMS));
        }

        return relationGoodsListGetRequest;
    }

    /**
     * 在庫状況表示取得リクエストに変換
     *
     * @param wishlistDto お気に入りDtoクラス
     * @return 在庫状況表示取得リクエスト
     */
    public InventoryStatusDisplayGetRequest toInventoryStatusDisplayGetRequest(WishlistDto wishlistDto) {
        InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest = new InventoryStatusDisplayGetRequest();

        if (wishlistDto != null && !ObjectUtils.isEmpty(wishlistDto.getGoodsDetailsDto())) {
            if (wishlistDto.getGoodsDetailsDto().getSaleStatusPC() != null) {
                inventoryStatusDisplayGetRequest.setSaleStatus(
                                wishlistDto.getGoodsDetailsDto().getSaleStatusPC().getValue());
            }
            inventoryStatusDisplayGetRequest.setSaleStartTime(wishlistDto.getGoodsDetailsDto().getSaleStartTimePC());
            inventoryStatusDisplayGetRequest.setSaleEndTime(wishlistDto.getGoodsDetailsDto().getSaleEndTimePC());
            inventoryStatusDisplayGetRequest.setSalesPossibleStock(
                            wishlistDto.getGoodsDetailsDto().getSalesPossibleStock());
            inventoryStatusDisplayGetRequest.setRemainderFewStock(
                            wishlistDto.getGoodsDetailsDto().getRemainderFewStock());
            if (wishlistDto.getGoodsDetailsDto().getStockManagementFlag() != null) {
                inventoryStatusDisplayGetRequest.setStockManagementFlag(
                                wishlistDto.getGoodsDetailsDto().getStockManagementFlag().getValue());
            }
            if (wishlistDto.getGoodsDetailsDto().getGoodsOpenStatusPC() != null) {
                inventoryStatusDisplayGetRequest.setOpenStatus(
                                wishlistDto.getGoodsDetailsDto().getGoodsOpenStatusPC().getValue());
            }
            inventoryStatusDisplayGetRequest.setOpenStartTime(wishlistDto.getGoodsDetailsDto().getOpenStartTimePC());
            inventoryStatusDisplayGetRequest.setOpenEndTime(wishlistDto.getGoodsDetailsDto().getOpenEndTimePC());
        }

        return inventoryStatusDisplayGetRequest;
    }

    /**
     * ページ情報リクエストに変換
     *
     * @param conditionDto 関連商品Dao用検索条件Dtoクラス
     * @return ページ情報リクエスト
     */
    public jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest toPageInfoRelationRequest(
                    GoodsRelationSearchForDaoConditionDto conditionDto) {
        jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest pageInfoRelationRequest =
                        new PageInfoRequest();

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageRequest(pageInfoRelationRequest, conditionDto.getPageInfo().getPnum(),
                                        conditionDto.getPageInfo().getLimit(),
                                        conditionDto.getPageInfo().getOrderField(),
                                        conditionDto.getPageInfo().isOrderAsc()
                                       );

        return pageInfoRelationRequest;
    }

    /**
     * ページ情報リクエスト（ページネーションのため）に変換
     *
     * @param wishlistConditionDto お気に入りDao用検索条件Dtoクラス
     * @return ページ情報リクエスト （ページネーションのため）
     */
    public jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest toPageInfoWishlistRequest(
                    WishlistSearchForDaoConditionDto wishlistConditionDto) {
        jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest pageInfoWishlistRequest =
                        new jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest();

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageRequest(pageInfoWishlistRequest, wishlistConditionDto.getPageInfo().getPnum(),
                                        wishlistConditionDto.getPageInfo().getLimit(),
                                        wishlistConditionDto.getPageInfo().getOrderField(),
                                        wishlistConditionDto.getPageInfo().isOrderAsc()
                                       );

        return pageInfoWishlistRequest;
    }

    /**
     * あしあと商品情報登録リクエストに変換
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return あしあと商品情報登録リクエスト
     */
    public BrowsingHistoryRegistRequest toBrowsingHistoryRegistRequest(Integer goodsGroupSeq) {
        BrowsingHistoryRegistRequest browsinghistoryRegistRequest = new BrowsingHistoryRegistRequest();

        browsinghistoryRegistRequest.setGoodsGroupSeq(goodsGroupSeq);

        return browsinghistoryRegistRequest;
    }

    /**
     * 商品グループ在庫表示クラスに変換
     *
     * @param stockStatusDisplayResponse あしあと商品
     * @return 商品グループ在庫表示クラス
     */
    public StockStatusDisplayEntity toBatchUpdateStockStatus(jp.co.itechh.quad.browsinghistory.presentation.api.param.StockStatusDisplayResponse stockStatusDisplayResponse) {

        // 商品グループ在庫表示更新バッチの処理前は存在しないためnullを返す
        if (stockStatusDisplayResponse == null) {
            return null;
        }

        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(stockStatusDisplayResponse.getGoodsGroupSeq());
        if (stockStatusDisplayResponse.getStockStatusPc() != null) {
            stockStatusDisplayEntity.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                    stockStatusDisplayResponse.getStockStatusPc()
                                                                                   ));
        }
        stockStatusDisplayEntity.setRegistTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(
                        conversionUtility.toTimestamp(stockStatusDisplayResponse.getUpdateTime()));

        return stockStatusDisplayEntity;
    }
}