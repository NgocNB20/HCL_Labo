package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.OptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.GoodsRegistUpdateModel;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.OptionContent;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvDownloadGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 変換Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class GoodsHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /** 商品グループコード */
    private final static String GOODS_GROUP_CODE_RADIO = "0";

    /** 商品番号 */
    private final static String GOODS_CODE_RADIO = "1";

    /** JANコード */
    private final static String JAN_CODE_RADIO = "2";

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public GoodsHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件の作成<br/>
     *
     * @param goodsModel 商品モデル
     * @return 商品一覧取得リクエスト
     */
    public ProductItemListGetRequest toProductItemListGetRequest(GoodsModel goodsModel) {

        ProductItemListGetRequest productItemListGetRequest = new ProductItemListGetRequest();

        // カテゴリID 検索には使用しない
        productItemListGetRequest.setSearchCategoryId(goodsModel.getSearchCategoryId());

        // 商品グループコード
        productItemListGetRequest.setSearchGoodsGroupCode(goodsModel.getSearchGoodsGroupCode());

        // 商品コード
        productItemListGetRequest.setSearchGoodsCode(goodsModel.getSearchGoodsCode());

        // JANコード
        productItemListGetRequest.setSearchJanCode(goodsModel.getSearchJanCode());

        // 下限販売可能在庫数
        if (goodsModel.getSearchMinSalesPossibleStockCount() != null) {
            productItemListGetRequest.setSearchMinSalesPossibleStockCount(
                            goodsModel.getSearchMinSalesPossibleStockCount());
        }

        // 上限販売可能在庫数
        if (goodsModel.getSearchMaxSalesPossibleStockCount() != null) {
            productItemListGetRequest.setSearchMaxSalesPossibleStockCount(
                            goodsModel.getSearchMaxSalesPossibleStockCount());
        }

        // 商品名
        productItemListGetRequest.setSearchGoodsGroupName(goodsModel.getSearchGoodsGroupName());

        // 商品タグ
        productItemListGetRequest.setSearchGoodsTag(goodsModel.getSearchGoodsTag());

        // サイト区分
        productItemListGetRequest.setSite(goodsModel.getSite());

        // 公開状態
        if (goodsModel.getGoodsOpenStatusArray() != null && goodsModel.getGoodsOpenStatusArray().length > 0) {
            productItemListGetRequest.setGoodsOpenStatusArray(Arrays.asList(goodsModel.getGoodsOpenStatusArray()));
        } else {
            productItemListGetRequest.setGoodsOpenStatusArray(Arrays.asList(HTypeOpenDeleteStatus.OPEN.getValue(),
                                                                            HTypeOpenDeleteStatus.NO_OPEN.getValue(),
                                                                            HTypeOpenDeleteStatus.DELETED.getValue()
                                                                           ));
        }

        // 販売状態
        if (goodsModel.getGoodsSaleStatusArray() != null && goodsModel.getGoodsSaleStatusArray().length > 0) {
            productItemListGetRequest.setGoodsSaleStatusArray(Arrays.asList(goodsModel.getGoodsSaleStatusArray()));
        } else {
            productItemListGetRequest.setGoodsSaleStatusArray(
                            Arrays.asList(HTypeGoodsSaleStatus.SALE.getValue(), HTypeGoodsSaleStatus.NO_SALE.getValue(),
                                          HTypeGoodsSaleStatus.DELETED.getValue()
                                         ));
        }

        // 登録・更新日時
        String selectDate = goodsModel.getSelectRegistOrUpdate();
        productItemListGetRequest.setSelectRegistOrUpdate(selectDate);
        if (selectDate != null) {
            if (selectDate.equals("0") || selectDate.equals("1")) {
                productItemListGetRequest.setSearchRegOrUpTimeFrom(goodsModel.getSearchRegOrUpTimeFrom());
                if (goodsModel.getSearchRegOrUpTimeTo() != null) {
                    productItemListGetRequest.setSearchRegOrUpTimeTo(goodsModel.getSearchRegOrUpTimeTo());
                }
            }
        }

        // ノベルティ商品フラグ
        productItemListGetRequest.setSearchNoveltyGoodsType(goodsModel.isSearchNoveltyGoodsType());

        // 商品検索設定キーワード全角大文字
        if (!StringUtils.isEmpty(goodsModel.getSettingKeywords())) {
            ZenHanConversionUtility zenHanConversionUtility = new ZenHanConversionUtility();
            String settingKeyword = zenHanConversionUtility.toZenkaku(goodsModel.getSettingKeywords().trim())
                                                           .replaceAll("　", " ");
            productItemListGetRequest.settingKeywords(settingKeyword);
        }

        if (!StringUtils.isEmpty(goodsModel.getMultiCodeList()) && !StringUtils.isEmpty(
                        goodsModel.getMultiCodeRadio())) {
            List<String> searchCode = Arrays.asList(goodsModel.getMultiCodeList()
                                                              .trim()
                                                              .replaceAll("[ 　\t\\x0B\f\r]", "")
                                                              .split("\n"));

            //商品管理番号リスト
            if (goodsModel.getMultiCodeRadio().equals(GOODS_GROUP_CODE_RADIO)) {
                productItemListGetRequest.setGoodsGroupCodeList(searchCode);
                //商品番号リスト
            } else if (goodsModel.getMultiCodeRadio().equals(GOODS_CODE_RADIO)) {
                productItemListGetRequest.setGoodsCodeList(searchCode);
                //JANコードリスト
            } else if (goodsModel.getMultiCodeRadio().equals(JAN_CODE_RADIO)) {
                productItemListGetRequest.setJanCodeList(searchCode);
            }
        }

        if (!CollectionUtils.isEmpty(goodsModel.getCategoryList())) {
            List<String> categoryIdList = new ArrayList<>();
            for (CategoryEntity categoryEntity : goodsModel.getCategoryList()) {
                categoryIdList.add(categoryEntity.getCategoryId());
            }
            productItemListGetRequest.setCategoryIdList(categoryIdList);
        }

        // フロント表示状態
        if (goodsModel.getFrontDisplayArray() != null && goodsModel.getFrontDisplayArray().length > 0) {
            productItemListGetRequest.setFrontDisplayList(Arrays.asList(goodsModel.getFrontDisplayArray()));
        } else {
            productItemListGetRequest.setFrontDisplayList(Arrays.asList(HTypeFrontDisplayStatus.NO_OPEN.getValue(),
                                                                        HTypeFrontDisplayStatus.OPEN.getValue()
                                                                       ));
        }

        // フロント表示基準日時（プレビュー日時欄の入力条件）
        if (StringUtils.isNotBlank(goodsModel.getPreviewDate())) {
            if (StringUtils.isBlank(goodsModel.getPreviewTime())) {
                //　日付のみ入力の場合、時間を設定
                goodsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
            }
            productItemListGetRequest.setFrontDisplayReferenceDate(this.conversionUtility.toDate(
                            this.conversionUtility.toTimeStamp(goodsModel.getPreviewDate(),
                                                               goodsModel.getPreviewTime()
                                                              )));
        }

        return productItemListGetRequest;
    }

    /**
     * 検索結果をページに反映<br/>
     *
     * @param productItemListResponse 商品一覧レスポンス
     * @param goodsModel              商品モデル
     */
    public void toPageForSearch(ProductItemListResponse productItemListResponse, GoodsModel goodsModel) {

        // オフセット + 1をNoにセット
        int index = ((productItemListResponse.getPageInfo().getPage() - 1) * productItemListResponse.getPageInfo()
                                                                                                    .getLimit()) + 1;

        List<GoodsResultItem> resultItemList = new ArrayList<>();
        for (GoodsDetailsResponse goodsItemResponse : productItemListResponse.getGoodsDetailsList()) {
            GoodsResultItem goodsResultItem = ApplicationContextUtility.getBean(GoodsResultItem.class);
            goodsResultItem.setResultNo(index++);
            goodsResultItem.setResultGoodsSeq(goodsItemResponse.getGoodsSeq());
            goodsResultItem.setGoodsGroupCode(goodsItemResponse.getGoodsGroupCode());
            goodsResultItem.setResultGoodsCode(goodsItemResponse.getGoodsCode());
            goodsResultItem.setResultGoodsGroupName(goodsItemResponse.getGoodsGroupName());
            goodsResultItem.setResultUnitValue1(goodsItemResponse.getUnitValue1());
            goodsResultItem.setResultUnitValue2(goodsItemResponse.getUnitValue2());
            goodsResultItem.setResultGoodsOpenStatusPC(goodsItemResponse.getGoodsOpenStatus());
            goodsResultItem.setResultGoodsOpenStartTimePC(
                            conversionUtility.toTimestamp(goodsItemResponse.getOpenStartTime()));
            goodsResultItem.setResultGoodsOpenEndTimePC(
                            conversionUtility.toTimestamp(goodsItemResponse.getOpenEndTime()));
            goodsResultItem.setResultSaleStatusPC(goodsItemResponse.getSaleStatus());
            goodsResultItem.setResultSaleStartTimePC(
                            conversionUtility.toTimestamp(goodsItemResponse.getSaleStartTime()));
            goodsResultItem.setResultSaleEndTimePC(conversionUtility.toTimestamp(goodsItemResponse.getSaleEndTime()));
            goodsResultItem.setResultGoodsPrice(goodsItemResponse.getGoodsPrice());
            goodsResultItem.setResultStockManagementFlag(goodsItemResponse.getStockManagementFlag());
            goodsResultItem.setResultSalesPossibleStock(goodsItemResponse.getSalesPossibleStock());
            goodsResultItem.setResultRealStock(goodsItemResponse.getRealStock());
            goodsResultItem.setResultIndividualDeliveryType(goodsItemResponse.getIndividualDeliveryType());
            goodsResultItem.setResultFrontDisplay(goodsItemResponse.getFrontDisplay());
            if (StringUtils.isNotBlank(goodsModel.getPreviewDate())) {
                goodsResultItem.setResultFrontDisplayReferenceDate(conversionUtility.toTimestamp(
                                this.conversionUtility.toDate(
                                                this.conversionUtility.toTimeStamp(goodsModel.getPreviewDate(),
                                                                                   goodsModel.getPreviewTime()
                                                                                  ))));
            }

            resultItemList.add(goodsResultItem);
        }
        goodsModel.setResultItems(resultItemList);

        // 件数セット
        goodsModel.setTotalCount(resultItemList.size());
    }

    /**
     * 商品CSVDLリクエスト<br/>
     *
     * @param goodsModel 商品モデル
     * @return 商品CSVDLリクエスト
     */
    public ProductCsvDownloadGetRequest toProductCsvDownloadGetRequest(GoodsModel goodsModel) {

        ProductCsvDownloadGetRequest productCsvDownloadGetRequest = new ProductCsvDownloadGetRequest();

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // カテゴリID 検索には使用しない
        productCsvDownloadGetRequest.setCategoryId(goodsModel.getSearchCategoryId());

        // 商品管理番号
        productCsvDownloadGetRequest.setGoodsGroupCode(goodsModel.getSearchGoodsGroupCode());

        // 商品コード
        productCsvDownloadGetRequest.setGoodsCode(goodsModel.getSearchGoodsCode());

        // JANコード
        productCsvDownloadGetRequest.setJanCode(goodsModel.getSearchJanCode());

        // 下限販売可能在庫数
        if (goodsModel.getSearchMinSalesPossibleStockCount() != null) {
            productCsvDownloadGetRequest.setMinSalesPossibleStock(goodsModel.getSearchMinSalesPossibleStockCount());
        } else {
            productCsvDownloadGetRequest.setMinSalesPossibleStock(null);
        }

        // 上限販売可能在庫数
        if (goodsModel.getSearchMaxSalesPossibleStockCount() != null) {
            productCsvDownloadGetRequest.setMaxSalesPossibleStock(goodsModel.getSearchMaxSalesPossibleStockCount());
        } else {
            productCsvDownloadGetRequest.setMaxSalesPossibleStock(null);
        }

        // 商品名
        productCsvDownloadGetRequest.setGoodsGroupName(goodsModel.getSearchGoodsGroupName());

        // ノベルティ商品フラグ
        if (goodsModel.isSearchNoveltyGoodsType()) {
            productCsvDownloadGetRequest.setNoveltyGoodsType(HTypeNoveltyGoodsType.NOVELTY_GOODS.getValue());
        }

        // サイト区分
        productCsvDownloadGetRequest.setSite(goodsModel.getSite());

        // 公開状態
        if (goodsModel.getGoodsOpenStatusArray() != null && goodsModel.getGoodsOpenStatusArray().length > 0) {
            productCsvDownloadGetRequest.setGoodsOpenStatusArray(Arrays.asList(goodsModel.getGoodsOpenStatusArray()));
        } else {
            productCsvDownloadGetRequest.setGoodsOpenStatusArray(Arrays.asList(HTypeOpenDeleteStatus.OPEN.getValue(),
                                                                               HTypeOpenDeleteStatus.NO_OPEN.getValue(),
                                                                               HTypeOpenDeleteStatus.DELETED.getValue()
                                                                              ));
        }

        // 販売状態
        if (goodsModel.getGoodsSaleStatusArray() != null && goodsModel.getGoodsSaleStatusArray().length > 0) {
            productCsvDownloadGetRequest.setGoodsSaleStatusArray(Arrays.asList(goodsModel.getGoodsSaleStatusArray()));
        } else {
            productCsvDownloadGetRequest.setGoodsSaleStatusArray(
                            Arrays.asList(HTypeGoodsSaleStatus.SALE.getValue(), HTypeGoodsSaleStatus.NO_SALE.getValue(),
                                          HTypeGoodsSaleStatus.DELETED.getValue()
                                         ));
        }

        // 登録・更新日時
        String selectDate = goodsModel.getSelectRegistOrUpdate();
        if (selectDate != null) {
            productCsvDownloadGetRequest.setSelectRegistOrUpdate(selectDate);
            if (selectDate.equals("0")) {
                if (goodsModel.getSearchRegOrUpTimeFrom() != null) {
                    productCsvDownloadGetRequest.setRegistTimeFrom(
                                    conversionUtility.toTimeStamp(goodsModel.getSearchRegOrUpTimeFrom()));
                }
                if (goodsModel.getSearchRegOrUpTimeTo() != null) {
                    productCsvDownloadGetRequest.setRegistTimeTo(dateUtility.getEndOfDate(
                                    conversionUtility.toTimeStamp(goodsModel.getSearchRegOrUpTimeTo())));
                }
            } else if (selectDate.equals("1")) {
                if (goodsModel.getSearchRegOrUpTimeFrom() != null) {
                    productCsvDownloadGetRequest.setUpdateTimeFrom(
                                    conversionUtility.toTimeStamp(goodsModel.getSearchRegOrUpTimeFrom()));
                }
                if (goodsModel.getSearchRegOrUpTimeTo() != null) {
                    productCsvDownloadGetRequest.setUpdateTimeTo(dateUtility.getEndOfDate(
                                    conversionUtility.toTimeStamp(goodsModel.getSearchRegOrUpTimeTo())));
                }
            }
        }

        // フロント表示状態
        if (goodsModel.getFrontDisplayArray() != null && goodsModel.getFrontDisplayArray().length > 0) {
            productCsvDownloadGetRequest.setFrontDisplayList(Arrays.asList(goodsModel.getFrontDisplayArray()));
        } else {
            productCsvDownloadGetRequest.setFrontDisplayList(Arrays.asList(HTypeFrontDisplayStatus.NO_OPEN.getValue(),
                                                                           HTypeFrontDisplayStatus.OPEN.getValue()
                                                                          ));
        }
        // 商品タグ
        if (goodsModel.getSearchGoodsTag() != null) {
            productCsvDownloadGetRequest.setGoodsTag(goodsModel.getSearchGoodsTag());
        }

        // フロント表示基準日時（プレビュー日時欄の入力条件）
        if (StringUtils.isNotBlank(goodsModel.getPreviewDate())) {
            if (StringUtils.isBlank(goodsModel.getPreviewTime())) {
                //　日付のみ入力の場合、時間を設定
                goodsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
            }
            productCsvDownloadGetRequest.setFrontDisplayReferenceDate(this.conversionUtility.toDate(
                            this.conversionUtility.toTimeStamp(goodsModel.getPreviewDate(),
                                                               goodsModel.getPreviewTime()
                                                              )));
        }
        // 商品検索設定キーワード全角大文字
        if (!StringUtils.isEmpty(goodsModel.getSettingKeywords())) {
            ZenHanConversionUtility zenHanConversionUtility = new ZenHanConversionUtility();
            String settingKeyword = zenHanConversionUtility.toZenkaku(goodsModel.getSettingKeywords().trim())
                                                           .replaceAll("　", " ");
            productCsvDownloadGetRequest.settingKeywords(settingKeyword);
        }

        if (!StringUtils.isEmpty(goodsModel.getMultiCodeList()) && !StringUtils.isEmpty(
                        goodsModel.getMultiCodeRadio())) {
            List<String> searchCode = Arrays.asList(goodsModel.getMultiCodeList()
                                                              .trim()
                                                              .replaceAll("[ 　\t\\x0B\f\r]", "")
                                                              .split("\n"));

            //商品管理番号リスト
            if (goodsModel.getMultiCodeRadio().equals(GOODS_GROUP_CODE_RADIO)) {
                productCsvDownloadGetRequest.setGoodsGroupCodeList(searchCode);
                //商品番号リスト
            } else if (goodsModel.getMultiCodeRadio().equals(GOODS_CODE_RADIO)) {
                productCsvDownloadGetRequest.setGoodsCodeList(searchCode);
                //JANコードリスト
            } else if (goodsModel.getMultiCodeRadio().equals(JAN_CODE_RADIO)) {
                productCsvDownloadGetRequest.setJanCodeList(searchCode);
            }
        }

        if (!CollectionUtils.isEmpty(goodsModel.getCategoryList())) {
            List<String> categoryIdList = new ArrayList<>();
            for (CategoryEntity categoryEntity : goodsModel.getCategoryList()) {
                categoryIdList.add(categoryEntity.getCategoryId());
            }
            productCsvDownloadGetRequest.setCategoryIdList(categoryIdList);
        }

        return productCsvDownloadGetRequest;
    }

    /**
     * @param categoryListResponse カテゴリ一覧レスポンス
     */
    public Map<String, String> toCategoryMap(CategoryListResponse categoryListResponse) {
        Map<String, String> categoryMap = new HashMap<>();
        List<CategoryResponse> categoryList = categoryListResponse.getCategoryList();

        categoryList.forEach(categoryResponse -> {
            categoryMap.put(categoryResponse.getCategoryId(), categoryResponse.getCategoryName());
        });
        return categoryMap;
    }

    /**
     * チェックされた商品SEQリストを作成<br/>
     *
     * @param goodsModel 商品検索ページ
     * @return 選択済みの商品SEQリスト
     */
    public List<Integer> toGoodsSeqList(GoodsModel goodsModel) {

        List<Integer> goodsSeqList = new ArrayList<>();

        for (Iterator<GoodsResultItem> it = goodsModel.getResultItems().iterator(); it.hasNext(); ) {
            GoodsResultItem goodsResultItem = it.next();
            if (goodsResultItem.isResultGoodsCheck()) {
                goodsSeqList.add(goodsResultItem.getResultGoodsSeq());
            }
        }
        return goodsSeqList;
    }

    /**
     * CSVDL オプション リストに変換します
     *
     * @param responseList 商品検索CSVDLオプションリストレスポンス
     * @return CSVDL オプション リスト
     */
    public List<CsvDownloadOptionDto> toCsvDownloadOptionDtoList(ProductCsvOptionListResponse responseList) {
        List<CsvDownloadOptionDto> csvDownloadOptionDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(responseList.getCsvDownloadOptionList())) {
            for (ProductCsvOptionResponse response : responseList.getCsvDownloadOptionList()) {
                CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();

                csvDownloadOptionDto.setOptionId(response.getOptionId());
                csvDownloadOptionDto.setDefaultOptionName(response.getDefaultOptionName());
                csvDownloadOptionDto.setOptionName(response.getOptionName());
                csvDownloadOptionDto.setOutHeader(response.getOutHeader());
                List<OptionDto> optionDtoList = new ArrayList<>();
                for (OptionContent optionContent : Objects.requireNonNull(response.getOptionContent())) {
                    OptionDto optionDto = new OptionDto();
                    optionDto.setItemName(optionContent.getItemName());
                    optionDto.setDefaultColumnLabel(optionContent.getDefaultColumnLabel());
                    optionDto.setColumnLabel(optionContent.getColumnLabel());
                    optionDto.setDefaultOrder(optionContent.getDefaultOrder());
                    optionDto.setOrder(optionContent.getOrder());
                    optionDto.setOutFlag(optionContent.getOutFlag());
                    optionDtoList.add(optionDto);
                }
                csvDownloadOptionDto.setOptionContent(optionDtoList);

                csvDownloadOptionDtoList.add(csvDownloadOptionDto);
            }
        }
        return csvDownloadOptionDtoList;
    }

    /**
     * カテゴリエンティティに変換
     *
     * @param categoryListResponse      カテゴリ一覧レスポンス
     * @param shopSeq           ショップSEQ
     * @return カテゴリエンティティリスト
     */
    public List<CategoryEntity> toCategoryEntityFromResponse(CategoryListResponse categoryListResponse,
                                                             GoodsRegistUpdateModel goodsRegistUpdateModel,
                                                             Integer shopSeq) {

        // 選択されたカテゴリリスト
        List<CategoryEntity> categorySelectedList = goodsRegistUpdateModel.getLinkedCategoryList();

        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        List<CategoryResponse> categoryList = categoryListResponse.getCategoryList();
        if (!ListUtils.isEmpty(categoryList)) {
            categoryList.forEach(item -> {
                CategoryEntity categoryEntity = new CategoryEntity();

                categoryEntity.setCategorySeq(item.getCategorySeq());
                categoryEntity.setShopSeq(shopSeq);
                categoryEntity.setCategoryId(item.getCategoryId());
                categoryEntity.setCategoryName(item.getCategoryName());
                categoryEntity.setCategoryOpenStatusPC(
                                EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, item.getCategoryOpenStatus()));
                categoryEntity.setCategoryOpenStartTimePC(
                                conversionUtility.toTimestamp(item.getCategoryOpenStartTime()));
                categoryEntity.setCategoryOpenEndTimePC(conversionUtility.toTimestamp(item.getCategoryOpenEndTime()));
                categoryEntity.setCategoryType(
                                EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, item.getCategoryType()));
                categoryEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
                categoryEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
                if (ListUtils.isEmpty(categorySelectedList)) {
                    categoryEntity.setRegistCategoryCheck(false);
                } else {
                    categoryEntity.setRegistCategoryCheck(categorySelectedList.stream()
                                                                              .anyMatch(el -> el.getCategorySeq()
                                                                                                .equals(item.getCategorySeq())));
                }

                categoryEntityList.add(categoryEntity);
            });
        }
        return categoryEntityList;
    }

    /**
     * カテゴリ登録リクエストに変換
     *
     * @param goodsRegistUpdateModel 商品登録更新ページ
     * @return カテゴリ登録リクエスト
     */
    public CategoryListGetRequest toCategoryListGetRequest(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();

        if (goodsRegistUpdateModel.getCategorySearch() == null) {
            categoryListGetRequest.categorySearchKeyword("");
        } else {
            categoryListGetRequest.categorySearchKeyword(goodsRegistUpdateModel.getCategorySearch());
        }
        return categoryListGetRequest;
    }

    /**
     * 商品検索CSVDLオプションの更新リクエストに変換します
     *
     * @param csvDownloadOptionDto CSVDLオプショDto
     * @return productCsvOptionUpdateRequest 商品検索CSVDLオプションの更新リクエスト
     */
    public ProductCsvOptionUpdateRequest toProductCsvOptionUpdateRequest(CsvDownloadOptionDto csvDownloadOptionDto) {
        ProductCsvOptionUpdateRequest productCsvOptionUpdateRequest = new ProductCsvOptionUpdateRequest();

        if (!ObjectUtils.isEmpty(csvDownloadOptionDto)) {
            productCsvOptionUpdateRequest.setOptionId(csvDownloadOptionDto.getOptionId());
            productCsvOptionUpdateRequest.setDefaultOptionName(csvDownloadOptionDto.getDefaultOptionName());
            productCsvOptionUpdateRequest.setOptionName(csvDownloadOptionDto.getOptionName());
            productCsvOptionUpdateRequest.setOutHeader(csvDownloadOptionDto.getOutHeader());

            List<jp.co.itechh.quad.product.presentation.api.param.OptionContent> optionContentList = new ArrayList<>();
            for (OptionDto optionDto : Objects.requireNonNull(csvDownloadOptionDto.getOptionContent())) {
                OptionContent optionContent = new OptionContent();
                optionContent.setItemName(optionDto.getItemName());
                optionContent.setDefaultColumnLabel(optionDto.getDefaultColumnLabel());
                optionContent.setColumnLabel(optionDto.getColumnLabel());
                optionContent.setDefaultOrder(optionDto.getDefaultOrder());
                optionContent.setOrder(optionDto.getOrder());
                optionContent.setOutFlag(optionDto.getOutFlag());
                optionContentList.add(optionContent);
            }

            productCsvOptionUpdateRequest.setOptionContent(optionContentList);
        }

        return productCsvOptionUpdateRequest;
    }

}