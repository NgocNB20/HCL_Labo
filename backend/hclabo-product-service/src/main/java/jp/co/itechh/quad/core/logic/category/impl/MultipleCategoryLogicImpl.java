/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.NumberUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.dto.multipleCategory.ajax.MultipleCategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.category.MultipleCategoryLogic;
import jp.co.itechh.quad.core.service.goods.group.OpenGoodsGroupSearchService;
import jp.co.itechh.quad.core.thymeleaf.ImageConverterViewUtil;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.CalculatePriceUtility;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * To fetch special category list
 *
 * @author Shalaka kale
 *
 */

@Component
public class MultipleCategoryLogicImpl extends AbstractShopLogic implements MultipleCategoryLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleCategoryLogicImpl.class);

    /** 公開展示商品情報リスト取得サービスクラス */
    private final OpenGoodsGroupSearchService openGoodsGroupSearchService;

    /** 表示形式：サムネイル表示 キー*/
    public static final String VIEW_TYPE_THUMBNAIL_KEY = "thumbs";

    /** 表示形式：リスト表示 キー*/
    public static final String VIEW_TYPE_LIST_KEY = "list";

    /** 数値関連ヘルパー */
    private final NumberUtility numberUtility;

    /** 商品系Helper取得*/
    private final GoodsUtility goodsUtility;

    /** 税込計算Utility */
    private final CalculatePriceUtility calculatePriceUtility;

    @Autowired
    public MultipleCategoryLogicImpl(OpenGoodsGroupSearchService openGoodsGroupSearchService,
                                     NumberUtility numberUtility,
                                     GoodsUtility goodsUtility,
                                     CalculatePriceUtility calculatePriceUtility) {
        this.openGoodsGroupSearchService = openGoodsGroupSearchService;
        this.numberUtility = numberUtility;
        this.goodsUtility = goodsUtility;
        this.calculatePriceUtility = calculatePriceUtility;
    }

    /**
     *
     * To fetch special category list
     *
     * @param categoryString comma separated categoryId
     * @param seqString      comma separated category sequences
     * @param limitString    comma separated category limit
     * @param priceFrom      priceFrom
     * @param priceTo        priceTo
     * @param stock          stock
     * @param viewType       viewType
     * @return Map as categoryId as key and list of GoodsGroupDto as values
     */
    @Override
    public Map<String, List<MultipleCategoryGoodsDetailsDto>> getCategoryMap(HTypeSiteType siteType,
                                                                             String categoryString,
                                                                             String seqString,
                                                                             String limitString,
                                                                             String priceFrom,
                                                                             String priceTo,
                                                                             String stock,
                                                                             String viewType) {
        Map<String, List<MultipleCategoryGoodsDetailsDto>> resultMap = new LinkedHashMap<>();

        if (StringUtil.isEmpty(categoryString)) {
            categoryString = "";
        }

        String[] categoryArray = categoryString.split(",");

        int index = 0;
        for (String cc : categoryArray) {
            String seqStr = getString(seqString, index);
            String limitStr = getString(limitString, index);
            String priceFromStr = getString(priceFrom, index);
            String priceToStr = getString(priceTo, index);
            String stockStr = getString(stock, index);
            String viewTypeStr = getString(viewType, index);

            // 引数の作成
            String order = null;
            boolean asc = false;
            if (MultipleCategoryLogic.SORT_TYPE_GOODSPRICE_KEY.equals(seqStr)) {
                // 価格順
                order = "goodsGroupMinPrice";
                asc = true;
            } else if (MultipleCategoryLogic.SORT_TYPE_SALECOUNT_KEY.equals(seqStr)) {
                // 売上個数順
                order = "popularityCount";
                asc = false;
            } else if (MultipleCategoryLogic.SORT_TYPE_REGISTDATE_KEY.equals(seqStr)) {
                // 新着順
                order = "whatsnewdate";
                asc = false;
            } else {
                // 標準
                order = "normal";
                asc = true;
            }

            int limit = 0;
            try {
                limit = Integer.parseInt(limitStr);
            } catch (NumberFormatException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 変換失敗は無視
            }
            List<GoodsGroupDto> specialDtoList =
                            getCategoryGoodsList(siteType, cc, limit, order, asc, priceFromStr, priceToStr, stockStr);

            if (specialDtoList.isEmpty()) {
                continue;
            }
            // Set page items
            List<MultipleCategoryGoodsDetailsDto> pageItemList =
                            this.makeIndexPageItemList(specialDtoList, viewTypeStr);
            resultMap.put(cc, pageItemList);
            index++;
        }

        return resultMap;
    }

    /**
     *
     * This method will accept String array and index, and returns the value from the array at the given index
     * It also handles ArrayIndexOutOfBoundsException and return blank string
     *
     *
     * @param valueToConvert string array
     * @param index          index to get the value
     * @return value at given index from given array
     */
    protected String getString(String valueToConvert, int index) {
        String value = StringUtils.EMPTY;
        if (StringUtils.isEmpty(valueToConvert)) {
            return valueToConvert;
        }
        String[] stringArray = valueToConvert.split(",");
        try {
            if (StringUtils.isNotEmpty(stringArray[index])) {
                value = stringArray[index];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 取得失敗は無視
        }
        return value;
    }

    /**
     * 指定したカテゴリに属する商品グループ情報を取得する。
     *
     * @param siteType  the site type
     * @param categoryId   カテゴリID
     * @param limit        limit
     * @param order        order
     * @param asc          asc
     * @param priceFromStr priceFrom
     * @param priceToStr   priceTo
     * @param stockStr     stock
     * @return 商品グループ情報
     */
    public List<GoodsGroupDto> getCategoryGoodsList(HTypeSiteType siteType,
                                                    String categoryId,
                                                    int limit,
                                                    String order,
                                                    boolean asc,
                                                    String priceFromStr,
                                                    String priceToStr,
                                                    String stockStr) {

        GoodsGroupSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsGroupSearchForDaoConditionDto.class);

        conditionDto.setCategoryId(categoryId);
        if (StringUtil.isNotEmpty(priceFromStr) && numberUtility.isNumber(priceFromStr)) {
            conditionDto.setMinPrice(new BigDecimal(priceFromStr));
        }
        if (StringUtil.isNotEmpty(priceToStr) && numberUtility.isNumber(priceToStr)) {
            conditionDto.setMaxPrice(new BigDecimal(priceToStr));
        }

        conditionDto.setOpenStatus(HTypeOpenDeleteStatus.OPEN);

        PageInfoModule PageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        PageInfoModule.setupPageInfoForSkipCount(conditionDto, limit, order, asc);

        if (StringUtil.isNotEmpty(stockStr) && Boolean.valueOf(stockStr)) {
            // 在庫ありの指定がある場合は、在庫状況が「在庫あり」「残りわずか」「予約受付中」のものが対象
            conditionDto.setStcockExistStatus(new String[] {HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue(),
                            HTypeStockStatusType.STOCK_FEW.getValue()});
        }
        return openGoodsGroupSearchService.execute(siteType, conditionDto);
    }

    /**
     * DTO一覧をitemクラスの一覧に変換する
     *
     * @param goodsGroupDtoList 商品グループ一覧情報DTO
     * @param viewType          viewType
     * @return カテゴリページアイテム情報一覧
     */
    protected List<MultipleCategoryGoodsDetailsDto> makeIndexPageItemList(List<GoodsGroupDto> goodsGroupDtoList,
                                                                          String viewType) {

        List<MultipleCategoryGoodsDetailsDto> itemsList = new ArrayList<>();

        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {

            GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
            MultipleCategoryGoodsDetailsDto multipleCategoryGoodsDetails =
                            ApplicationContextUtility.getBean(MultipleCategoryGoodsDetailsDto.class);
            multipleCategoryGoodsDetails.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
            multipleCategoryGoodsDetails.setGgcd(goodsGroupEntity.getGoodsGroupCode());
            multipleCategoryGoodsDetails.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
            multipleCategoryGoodsDetails.setGoodsTaxType(goodsGroupEntity.getGoodsTaxType());
            multipleCategoryGoodsDetails.setTaxRate(goodsGroupEntity.getTaxRate());

            BigDecimal taxRate = multipleCategoryGoodsDetails.getTaxRate();

            // 通常価格 - 税込計算
            multipleCategoryGoodsDetails.setGoodsPrice(goodsGroupEntity.getGoodsPrice());
            multipleCategoryGoodsDetails.setGoodsPriceInTax(
                            calculatePriceUtility.getTaxIncludedPrice(multipleCategoryGoodsDetails.getGoodsPrice(),
                                                                      taxRate
                                                                     ));

            // 商品消費税種別
            multipleCategoryGoodsDetails.setGoodsTaxType(goodsGroupEntity.getGoodsTaxType());

            // 新着日付
            multipleCategoryGoodsDetails.setWhatsnewDate(goodsGroupEntity.getWhatsnewDate());

            String goodsImage = null;
            if (goodsGroupDto.getGoodsGroupImageEntityList() != null) {
                // 画像ファイルを取得
                goodsImage = goodsUtility.getImageFileName(goodsGroupDto);
                multipleCategoryGoodsDetails.setGoodsImageItem(getGoodsImageItem(goodsGroupDto));
            }
            multipleCategoryGoodsDetails.setGoodsGroupImageThumbnail(goodsUtility.getGoodsImagePath(goodsImage));

            // アイコン情報の取得
            List<MultipleCategoryGoodsDetailsDto> goodsIconList = new ArrayList<>();
            if (goodsGroupDto.getGoodsInformationIconDetailsDtoList() != null) {
                for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsGroupDto.getGoodsInformationIconDetailsDtoList()) {
                    MultipleCategoryGoodsDetailsDto iconPageItem = new MultipleCategoryGoodsDetailsDto();
                    iconPageItem.setIconName(goodsInformationIconDetailsDto.getIconName());
                    iconPageItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

                    goodsIconList.add(iconPageItem);
                }
            }
            multipleCategoryGoodsDetails.setGoodsIconItems(goodsIconList);

            multipleCategoryGoodsDetails.setGoodsGroupImage(
                            isGoodsGroupImage(multipleCategoryGoodsDetails.getGoodsGroupImageThumbnail()));

            String stockStatusPc = EnumTypeUtil.getValue(goodsGroupDto.getBatchUpdateStockStatus().getStockStatusPc());
            multipleCategoryGoodsDetails.setStockStatusPc(stockStatusPc);
            multipleCategoryGoodsDetails.setStockStatusDisplay(false);

            // 商品グループ在庫の表示判定
            if (goodsUtility.isGoodsGroupStock(goodsGroupDto.getBatchUpdateStockStatus().getStockStatusPc())) {
                multipleCategoryGoodsDetails.setStockStatusDisplay(true);
            }
            multipleCategoryGoodsDetails.setStockPossibleSalesIconDisp(isStockPossibleSalesIconDisp(stockStatusPc));
            multipleCategoryGoodsDetails.setStockFewIconDisp(isStockFewIconDisp(stockStatusPc));
            multipleCategoryGoodsDetails.setStockSoldOutIconDisp(isStockSoldOutIconDisp(stockStatusPc));
            multipleCategoryGoodsDetails.setStockBeforeSaleIconDisp(isStockBeforeSaleIconDisp(stockStatusPc));
            multipleCategoryGoodsDetails.setStockNoStockIconDisp(isStockNoStockIconDisp(stockStatusPc));
            multipleCategoryGoodsDetails.setStockNoSaleDisp(isStockNoSaleDisp(stockStatusPc));

            GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();
            multipleCategoryGoodsDetails.setGoodsNote1(goodsGroupDisplayEntity.getGoodsNote1());

            itemsList.add(multipleCategoryGoodsDetails);
        }
        return itemsList;
    }

    /**
     * TOP表示用画像を取得
     * ①：Entityから画像リストを取得
     * ②：thymeleafユーティリティオブジェクトで、TOP表示用画像ファイル取得
     *
     * @param goodsGroupDto 商品グループDTO
     * @param customParams  案件用引数
     * @return TOP表示用画像ファイル
     */
    protected String getGoodsImageItem(GoodsGroupDto goodsGroupDto, Object... customParams) {

        List<String> imageList = new ArrayList<String>();
        for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
            imageList.add(goodsGroupImageEntity.getImageFileName());
        }

        // 画面表示用のthymeleafユーティリティオブジェクト
        ImageConverterViewUtil util = new ImageConverterViewUtil();
        // 画像リスト、画像番号、画像種別を渡す
        // thymeleafユーティリティオブジェクトからは、指定された画像を返却
        return util.convert(imageList, 0, null);
    }

    /**
     * 新着日付が現在の時刻を過ぎていないか判断
     *
     * @param whatsnewDate whatsnewDate
     * @return true:新着日付、false:新着日付を過ぎている
     */
    public boolean isNewDate(Timestamp whatsnewDate) {
        if (whatsnewDate == null) {
            return false;
        }
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        return whatsnewDate.compareTo(dateUtility.getCurrentDate()) >= 0;
    }

    /**
     * サムネイル画像有無チェック
     *
     * @param goodsGroupImageThumbnail goodsGroupImageThumbnail
     * @return 画像あり
     */
    public boolean isGoodsGroupImage(String goodsGroupImageThumbnail) {
        return StringUtil.isNotEmpty(goodsGroupImageThumbnail);
    }

    /**
     * 在庫状態:在庫あり
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：在庫あり
     */
    public boolean isStockPossibleSalesIconDisp(String stockStatusPc) {
        return HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:残りわずか
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：残りわずか
     */
    public boolean isStockFewIconDisp(String stockStatusPc) {
        return HTypeStockStatusType.STOCK_FEW.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:販売期間終了
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：販売期間終了
     */
    public boolean isStockSoldOutIconDisp(String stockStatusPc) {
        return HTypeStockStatusType.SOLDOUT.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:販売前
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：販売前
     */
    public boolean isStockBeforeSaleIconDisp(String stockStatusPc) {
        return HTypeStockStatusType.BEFORE_SALE.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:在庫なし
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：在庫なし
     */
    public boolean isStockNoStockIconDisp(String stockStatusPc) {
        return HTypeStockStatusType.STOCK_NOSTOCK.getValue().equals(stockStatusPc);
    }

    /**
     * 在庫状態:非販売
     * （アイコン表示用）
     *
     * @param stockStatusPc stockStatusPc
     * @return true：非販売
     */
    public boolean isStockNoSaleDisp(String stockStatusPc) {
        return HTypeStockStatusType.NO_SALE.getValue().equals(stockStatusPc);
    }
}
