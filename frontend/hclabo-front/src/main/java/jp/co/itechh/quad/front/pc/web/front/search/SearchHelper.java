/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.search;

import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.BeanUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.stock.StockDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsGroupItem;
import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsIconItem;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品検索画面 Helper
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Component
public class SearchHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchHelper.class);

    /** 検索キーワードの最大文字数（超過部分は切り捨てます） */
    private static final int KEYWORD_MAX_LENGTH = 100;

    /** 商品系Helper取得 */
    private final GoodsUtility goodsUtility;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param goodsUtility 商品系Helper取得
     * @param conversionUtility 変換Utility
     */
    @Autowired
    public SearchHelper(GoodsUtility goodsUtility, ConversionUtility conversionUtility) {
        this.goodsUtility = goodsUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件の設定
     * @param searchModel 商品検索Model
     * @param fromView 遷移元画面
     */
    public void toPageForLoad(SearchModel searchModel, String fromView) {
        // クリア除外対象フィールドリスト
        List<String> clearExcludedFieldsList = new ArrayList<>();

        // **************************************************************************
        // 検索条件Modelをクリアする
        // ⇒fromView（遷移元パラメータ）によって、以下３パターン処理する
        //  【１】自画面（検索画面）からの遷移（fromView=search）
        //     ⇒クリアを行わない（何もしない）
        //
        //  【２】共通ヘッダからの検索（fromView=header）
        //     ⇒検索キーワードだけ残し、他項目をクリアする
        //
        //  【３】上記以外（fromView=その他）
        //     ⇒Getパラメータのみ活かし、他項目クリアする
        // **************************************************************************
        // 【１】自画面（検索画面）からの遷移
        if (StringUtils.equals(SearchModel.FROM_VIEW_SEARCH_KEY, fromView)) {
            // 何もせず処理を抜ける
            // ★Modelクリアを行わない★
            return;
        }

        // 【２】共通ヘッダからの検索
        // 【３】上記以外
        // リクエストパラメータ項目をクリア除外対象項目に登録
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            clearExcludedFieldsList.add(paramNames.nextElement());
        }

        // Modelクリアを実行
        BeanUtility beanUtility = ApplicationContextUtility.getBean(BeanUtility.class);
        beanUtility.clearBean(SearchModel.class, searchModel, clearExcludedFieldsList.toArray(new String[] {}));

        // keyword<--->q の調整
        if (StringUtils.isNotEmpty(searchModel.getKeyword())) {
            // keywordがパラメータに含まれている場合は、keywordをencodeした文字列をqに入れる
            try {
                String encodedKeyword = URLEncoder.encode(searchModel.getKeyword(), "UTF-8");
                searchModel.setQ(encodedKeyword);
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("文字エンコーディングサポート例外：", e);
            }

        } else if (StringUtils.isNotEmpty(searchModel.getQ())) {
            // qががパラメータに含まれている場合は、qをdecodeencodeした文字列をkewwordに入れる
            try {
                String decodedKeyword = URLDecoder.decode(searchModel.getQ(), "UTF-8");
                searchModel.setKeyword(decodedKeyword);
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("文字エンコーディングサポート例外：", e);
            }
        }
    }

    /**
     *
     * 画面表示・再表示<br/>
     *
     * @param goodsGroupDtoList 商品グループDTOリスト
     * @param searchModel 商品検索Model
     */
    public void toPageForLoad(List<GoodsGroupDto> goodsGroupDtoList, SearchModel searchModel) {

        if (goodsGroupDtoList == null) {
            goodsGroupDtoList = new ArrayList<>();
        }
        // 商品グループリスト設定
        List<GoodsGroupItem> itemsList = setGoodsGroupListItems(goodsGroupDtoList, searchModel);

        // 商品グループサムネイルリスト設定
        setGoodsGroupThumbnailItemsItems(searchModel, itemsList);

        int count = 0;
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            GoodsGroupItem goodsGroupItem = itemsList.get(count);

            // 商品検索結果には在庫状況更新バッチ実行時点の在庫状況を表示する
            HTypeStockStatusType status = goodsGroupDto.getBatchUpdateStockStatus().getStockStatusPc();
            goodsGroupItem.setStockStatusPc(EnumTypeUtil.getValue(status));
            goodsGroupItem.setStockStatusDisplay(false);

            // 商品グループ在庫の表示判定
            GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
            if (goodsUtility.isGoodsGroupStock(status)) {
                goodsGroupItem.setStockStatusDisplay(true);
            }

            itemsList.set(count, goodsGroupItem);
            count++;
        }

        // リスト
        searchModel.setGoodsGroupListItems(itemsList);

    }

    /**
     * 商品グループリスト設定<br/>
     *
     * @param goodsGroupDtoList 商品リストDTO
     * @param searchModel 商品検索Model
     * @param customParams 案件用引数
     * @return 商品グループリスト
     */
    protected List<GoodsGroupItem> setGoodsGroupListItems(List<GoodsGroupDto> goodsGroupDtoList,
                                                          SearchModel searchModel,
                                                          Object... customParams) {
        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);

        List<GoodsGroupItem> itemsList = new ArrayList<>();
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            GoodsGroupItem goodsGroupItem = ApplicationContextUtility.getBean(GoodsGroupItem.class);
            // 商品グループ情報の設定
            setGoodsGroupData(goodsGroupDto, goodsGroupItem);

            // 商品画像情報の設定
            setGoodsGroupImage(goodsUtility, goodsGroupDto, goodsGroupItem);

            // アイコン情報の設定
            setGoodsIconItems(goodsUtility, goodsGroupDto, goodsGroupItem);

            if (!ObjectUtils.isEmpty(goodsGroupDto.getGoodsGroupEntity())
                && goodsGroupDto.getGoodsGroupEntity().getWhatsnewDate() != null) {
                // 新着画像の表示期間を取得
                Timestamp whatsnewDate =
                                goodsUtility.getRealWhatsNewDate(goodsGroupDto.getGoodsGroupEntity().getWhatsnewDate());
                goodsGroupItem.setWhatsnewDate(whatsnewDate);
            }

            // カテゴリ情報の設定
            goodsGroupItem.setCid(searchModel.getCondCid());
            goodsGroupItem.setHsn(searchModel.getHsn());

            itemsList.add(goodsGroupItem);
        }
        // 商品グループリスト設定
        searchModel.setGoodsGroupListItems(itemsList);
        return itemsList;
    }

    /**
     * 商品グループ情報設定<br/>
     *
     * @param goodsGroupDto 商品グループDTO
     * @param goodsGroupItem 商品検索画面ページアイテム
     * @param customParams 案件用引数
     */
    protected void setGoodsGroupData(GoodsGroupDto goodsGroupDto,
                                     GoodsGroupItem goodsGroupItem,
                                     Object... customParams) {
        if (goodsGroupDto != null) {
            GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
            goodsGroupItem.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
            goodsGroupItem.setGgcd(goodsGroupEntity.getGoodsGroupCode());
            goodsGroupItem.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());

            GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();
            goodsGroupItem.setGoodsNote1(goodsGroupDisplayEntity.getGoodsNote1());
            goodsGroupItem.setGoodsNote2(goodsGroupDisplayEntity.getGoodsNote2());
            goodsGroupItem.setGoodsNote3(goodsGroupDisplayEntity.getGoodsNote3());
            goodsGroupItem.setGoodsNote4(goodsGroupDisplayEntity.getGoodsNote4());
            goodsGroupItem.setGoodsNote5(goodsGroupDisplayEntity.getGoodsNote5());
            goodsGroupItem.setGoodsNote6(goodsGroupDisplayEntity.getGoodsNote6());
            goodsGroupItem.setGoodsNote7(goodsGroupDisplayEntity.getGoodsNote7());
            goodsGroupItem.setGoodsNote8(goodsGroupDisplayEntity.getGoodsNote8());
            goodsGroupItem.setGoodsNote9(goodsGroupDisplayEntity.getGoodsNote9());
            goodsGroupItem.setGoodsNote10(goodsGroupDisplayEntity.getGoodsNote10());

            // 税率
            BigDecimal taxRate = goodsGroupEntity.getTaxRate();
            goodsGroupItem.setTaxRate(taxRate);

            // 通常価格 - 税込計算
            BigDecimal goodsPrice = goodsGroupEntity.getGoodsPrice();
            goodsGroupItem.setGoodsPrice(goodsPrice);
            goodsGroupItem.setGoodsPriceInTax(goodsGroupEntity.getGoodsPriceInTax());
        }
    }

    /**
     * 商品画像情報設定<br/>
     *
     * @param goodsUtility 商品系Helper
     * @param goodsGroupDto 商品グループDTO
     * @param goodsGroupItem 商品検索画面ページアイテム
     * @param customParams 案件用引数
     */
    protected void setGoodsGroupImage(GoodsUtility goodsUtility,
                                      GoodsGroupDto goodsGroupDto,
                                      GoodsGroupItem goodsGroupItem,
                                      Object... customParams) {
        if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsGroupImageEntityList())) {

            // 画像の取得
            List<String> goodsImageList = new ArrayList<>();

            for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
                goodsImageList.add(goodsGroupImageEntity.getImageFileName());
            }

            goodsGroupItem.setGoodsImageItems(goodsImageList);
        }
    }

    /**
     * アイコン情報設定<br/>
     *
     * @param goodsUtility 商品系Helper
     * @param goodsGroupDto 商品グループDTO
     * @param goodsGroupItem 商品検索画面ページアイテム
     * @param customParams 案件用引数
     */
    protected void setGoodsIconItems(GoodsUtility goodsUtility,
                                     GoodsGroupDto goodsGroupDto,
                                     GoodsGroupItem goodsGroupItem,
                                     Object... customParams) {
        List<GoodsIconItem> goodsIconList = new ArrayList<>();
        if (goodsGroupDto.getGoodsInformationIconDetailsDtoList() != null) {
            setGoodsIconList(goodsUtility, goodsGroupDto, goodsIconList);
        }
        // アイコン情報の設定
        goodsGroupItem.setGoodsIconItems(goodsIconList);
    }

    /**
     * 商品アイコンリスト設定<br/>
     *
     * @param goodsUtility 商品系Helper
     * @param goodsGroupDto 商品グループDTO
     * @param goodsIconList 商品アイコンリスト
     * @param customParams 案件用引数
     */
    protected void setGoodsIconList(GoodsUtility goodsUtility,
                                    GoodsGroupDto goodsGroupDto,
                                    List<GoodsIconItem> goodsIconList,
                                    Object... customParams) {
        for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsGroupDto.getGoodsInformationIconDetailsDtoList()) {
            GoodsIconItem iconPageItem = ApplicationContextUtility.getBean(GoodsIconItem.class);

            iconPageItem.setIconName(goodsInformationIconDetailsDto.getIconName());
            iconPageItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

            goodsIconList.add(iconPageItem);
        }
    }

    /**
     * 商品グループサムネイルリスト設定<br/>
     *
     * @param searchModel 商品検索Model
     * @param itemsList 商品グループリスト
     * @param customParams 案件用引数
     */
    protected void setGoodsGroupThumbnailItemsItems(SearchModel searchModel,
                                                    List<GoodsGroupItem> itemsList,
                                                    Object... customParams) {

        // 縦リスト
        List<List<GoodsGroupItem>> listPageItemsItems = new ArrayList<>();
        // 横リスト
        List<GoodsGroupItem> listPageItems = new ArrayList<>();

        int col = searchModel.getCol();
        if (itemsList != null) {
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
            searchModel.setGoodsGroupThumbnailItemsItems(listPageItemsItems);
        }
    }

    /**
     *
     * 画面表示・再表示<br/>
     * カテゴリ情報をページクラスにセット<br />
     *
     * @param categoryTreeDto カテゴリー木構造Dtoクラス
     * @param searchModel 商品検索Model
     */
    public void toPageForLoad(CategoryTreeDto categoryTreeDto, SearchModel searchModel) {

        // 検索条件プルダウンに、項目をセット
        // 何階層目まで表示するかは、プロパティファイルにて設定
        List<Map<String, String>> categoryList = new ArrayList<>();

        if (categoryTreeDto.getCategoryTreeDtoList() != null) {
            categoryList = makeList(categoryTreeDto, categoryList);
        }

        searchModel.setCondCidItems(categoryList);
    }

    /**
     *
     * 子カテゴリを再帰的に呼び、カテゴリ情報リストを作成<br/>
     *
     * @param categoryTreeDto カテゴリー木構造Dtoクラス
     * @param categoryList カテゴリ情報リスト
     * @return カテゴリ情報リスト
     */
    protected List<Map<String, String>> makeList(CategoryTreeDto categoryTreeDto,
                                                 List<Map<String, String>> categoryList) {
        if (categoryTreeDto != null) {
            for (int i = 0; i < categoryTreeDto.getCategoryTreeDtoList().size(); i++) {
                CategoryTreeDto childCategoryTreeDto = categoryTreeDto.getCategoryTreeDtoList().get(i);

                Map<String, String> map = new HashMap<>();

                // カテゴリーレベルごとの、プレフィックスラベルを取得
                map.put("label", childCategoryTreeDto.getDisplayName());
                map.put("value", childCategoryTreeDto.getCategoryId());
                map.put("hsn", childCategoryTreeDto.getHierarchicalSerialNumber());
                Integer level = childCategoryTreeDto.getHierarchicalSerialNumber().split("-").length;
                map.put("level", String.valueOf(level));

                categoryList.add(map);

                if (childCategoryTreeDto.getCategoryTreeDtoList() != null) {
                    makeList(childCategoryTreeDto, categoryList);
                }
            }
        }
        return categoryList;
    }

    /**
     * Modelの内容を元にproductListGetRequestをセットする
     *
     * @param searchModel
     * @return productListGetRequest
     */
    protected ProductListGetRequest toSearchProductListGetRequest(SearchModel searchModel) {
        ProductListGetRequest productListGetRequest = new ProductListGetRequest();
        // 下限金額をセット
        if (searchModel.getLl() != null) {
            productListGetRequest.setMinPrice(BigDecimalConversionUtil.toBigDecimal(searchModel.getLl()));
        }
        // 上限金額をセット
        if (searchModel.getUl() != null) {
            productListGetRequest.setMaxPrice(BigDecimalConversionUtil.toBigDecimal(searchModel.getUl()));
        }
        // カテゴリIDをセット
        productListGetRequest.setCategoryId(searchModel.getCondCid());
        // 公開状態「公開」をセット
        productListGetRequest.setOpenStatus(HTypeOpenDeleteStatus.OPEN.getValue());
        // 在庫ありの指定がある場合
        if (searchModel.isSt()) {
            List<String> StcockExistStatusList = new ArrayList<>();
            StcockExistStatusList.add(HTypeStockStatusType.STOCK_POSSIBLE_SALES.getValue());
            StcockExistStatusList.add(HTypeStockStatusType.STOCK_FEW.getValue());
            StcockExistStatusList.add(HTypeStockStatusType.ON_RESERVATIONS.getValue());
            productListGetRequest.setStcockExistStatus(StcockExistStatusList);
        }

        productListGetRequest.setInStock(searchModel.isSt());

        // キーワードをセット
        String[] keyword = new String[0];
        if (searchModel.getKeyword() != null) {

            // 全角に変換
            // 全角コンバータ
            ZenHanConversionUtility zenHanConversionUtility =
                            ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
            // 全角変換
            String keywordString = (String) zenHanConversionUtility.toZenkaku(searchModel.getKeyword());
            // 大文字変換
            keywordString = StringUtils.upperCase(keywordString);

            // 先頭と末尾のtrim
            keywordString = keywordString.replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", "");
            // 101文字以降を切り捨て
            if (keywordString.length() > KEYWORD_MAX_LENGTH) {
                keywordString = keywordString.substring(0, KEYWORD_MAX_LENGTH);
            }
            keyword = keywordString.split("[\\s　]+");
        }

        if (keyword.length > 0) {
            productListGetRequest.setKeywordLikeCondition1(keyword[0]);
        }
        if (keyword.length > 1) {
            productListGetRequest.setKeywordLikeCondition2(keyword[1]);
        }
        if (keyword.length > 2) {
            productListGetRequest.setKeywordLikeCondition3(keyword[2]);
        }
        if (keyword.length > 3) {
            productListGetRequest.setKeywordLikeCondition4(keyword[3]);
        }
        if (keyword.length > 4) {
            productListGetRequest.setKeywordLikeCondition5(keyword[4]);
        }
        if (keyword.length > 5) {
            productListGetRequest.setKeywordLikeCondition6(keyword[5]);
        }
        if (keyword.length > 6) {
            productListGetRequest.setKeywordLikeCondition7(keyword[6]);
        }
        if (keyword.length > 7) {
            productListGetRequest.setKeywordLikeCondition8(keyword[7]);
        }
        if (keyword.length > 8) {
            productListGetRequest.setKeywordLikeCondition9(keyword[8]);
        }
        if (keyword.length > 9) {
            productListGetRequest.setKeywordLikeCondition10(keyword[9]);
        }

        return productListGetRequest;
    }

    public GoodsGroupDisplayEntity toGoodsGroupDisplayEntity(GoodsGroupDisplayResponse goodsGroupDisplayResponse) {
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
     * 商品グループクラススに変換
     *
     * @param goodsGroupSubResponse 商品詳細レスポンスクラス
     * @return 商品グループクラス
     */
    public GoodsGroupEntity toGoodsGroupEntity(GoodsGroupSubResponse goodsGroupSubResponse) {
        // 処理前は存在しないためnullを返す
        if (goodsGroupSubResponse == null) {
            return null;
        }

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
        if (goodsGroupEntity.getAlcoholFlag() != null) {
            goodsGroupEntity.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                          goodsGroupSubResponse.getAlcoholFlag()
                                                                         ));
        }
        if (goodsGroupEntity.getSnsLinkFlag() != null) {
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
     * 商品クラススに変換
     *
     * @param goodsSubResponse 商品クラス
     * @return 商品クラス
     */
    public GoodsEntity toGoodsEntity(GoodsSubResponse goodsSubResponse) {
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
     * 在庫Dtoクラススに変換
     *
     * @param stockResponse 商品グループ検索条件
     * @return 在庫DTO
     */
    public StockDto toStockDto(StockResponse stockResponse) {
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
     * 商品DTOリストクラスリストに変換
     *
     * @param goodsResponseList 商品レスポンスクラスリスト
     * @return 商品DTOリスト
     */
    public List<GoodsDto> toGoodsDtoList(List<GoodsResponse> goodsResponseList) {
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsResponseList.forEach(item -> {

            GoodsDto goodsDto = new GoodsDto();
            if (item.getGoodsSub() != null) {
                GoodsEntity goodsEntity = toGoodsEntity(item.getGoodsSub());
                goodsDto.setGoodsEntity(goodsEntity);
                goodsDtoList.add(goodsDto);
            }
            if (item.getStock() != null) {
                StockDto stockDto = toStockDto(item.getStock());
                goodsDto.setStockDto(stockDto);
            }
            goodsDto.setDeleteFlg(item.getDeleteFlg());

            if (item.getStockStatus() != null) {
                goodsDto.setStockStatusPc(
                                EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, item.getStockStatus()));
            }

            goodsDtoList.add(goodsDto);
        });

        return goodsDtoList;
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
     * カテゴリ登録商品クラスリストに変換
     *
     * @param CategoryGoodsResponseList 商品レスポンスクラスリスト
     * @return カテゴリ登録商品クラスリスト
     */
    public List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsResponse> CategoryGoodsResponseList) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        CategoryGoodsResponseList.forEach(categoryGoodsResponse -> {
            CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();

            categoryGoodsEntity.setCategorySeq(categoryGoodsResponse.getCategorySeq());
            categoryGoodsEntity.setGoodsGroupSeq(categoryGoodsResponse.getGoodsGroupSeq());
            categoryGoodsEntity.setOrderDisplay(categoryGoodsResponse.getManualOrderDisplay());
            categoryGoodsEntity.setRegistTime(conversionUtility.toTimestamp(categoryGoodsResponse.getRegistTime()));
            categoryGoodsEntity.setUpdateTime(conversionUtility.toTimestamp(categoryGoodsResponse.getUpdateTime()));

            categoryGoodsEntityList.add(categoryGoodsEntity);
        });

        return categoryGoodsEntityList;
    }

    /**
     * アイコン詳細DTOクラススクラスリストに変換
     *
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスクラスリスト
     * @return アイコン詳細DTOクラスリスト
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();

        for (GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse : goodsInformationIconDetailsResponseList) {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(goodsInformationIconDetailsResponse.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(goodsInformationIconDetailsResponse.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(goodsInformationIconDetailsResponse.getIconName());
            goodsInformationIconDetailsDto.setColorCode(goodsInformationIconDetailsResponse.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(goodsInformationIconDetailsResponse.getOrderDisplay());

            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        }

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品グループ在庫表示クラススに変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 在庫状態表示
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayResponse stockStatusDisplayResponse) {
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
     * 商品グループDtoクラススに変換
     *
     * @param productListResponse 商品覧レスポンス
     * @return 商品グループ一覧情報DTO
     */
    protected List<GoodsGroupDto> toGoodsGroupDtoList(ProductListResponse productListResponse) {
        // 処理前は存在しないためnullを返す
        if (productListResponse == null) {
            return null;
        }

        List<GoodsGroupDto> goodsGroupDtoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(productListResponse.getGoodsGroupList())) {
            for (GoodsGroupResponse i : productListResponse.getGoodsGroupList()) {
                GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
                goodsGroupDto.setTaxRate(i.getTaxRate());
                if (i.getGoodsGroupDisplay() != null) {
                    goodsGroupDto.setGoodsGroupDisplayEntity(toGoodsGroupDisplayEntity(i.getGoodsGroupDisplay()));
                }
                goodsGroupDto.setGoodsGroupEntity(toGoodsGroupEntity(i.getGoodsGroupSubResponse()));
                if (CollectionUtil.isNotEmpty(i.getGoodsResponseList())) {
                    goodsGroupDto.setGoodsDtoList(toGoodsDtoList(i.getGoodsResponseList()));
                }
                if (CollectionUtil.isNotEmpty(i.getGoodsGroupImageResponseList())) {
                    goodsGroupDto.setGoodsGroupImageEntityList(
                                    toGoodsGroupImageEntityList(i.getGoodsGroupImageResponseList()));
                }
                if (i.getBatchUpdateStockStatus() != null) {
                    goodsGroupDto.setBatchUpdateStockStatus(toStockStatusDisplayEntity(i.getBatchUpdateStockStatus()));
                }
                if (CollectionUtil.isNotEmpty(i.getCategoryGoodsResponseList())) {
                    goodsGroupDto.setCategoryGoodsEntityList(
                                    toCategoryGoodsEntityList(i.getCategoryGoodsResponseList()));
                }
                if (CollectionUtil.isNotEmpty(i.getGoodsInformationIconDetailsResponseList())) {
                    goodsGroupDto.setGoodsInformationIconDetailsDtoList(toGoodsInformationIconDetailsDtoList(
                                    i.getGoodsInformationIconDetailsResponseList()));
                }
                if (i.getRealTimeStockStatus() != null) {
                    goodsGroupDto.setRealTimeStockStatus(toStockStatusDisplayEntity(i.getRealTimeStockStatus()));
                }

                goodsGroupDtoList.add(goodsGroupDto);
            }
        }
        return goodsGroupDtoList;
    }

    /**
     * カテゴリー木構造Dtoに変換
     *
     * @param categoryTreeDto カテゴリー木構造Dtoクラス
     * @param categoryTreeResponse カテゴリ木構造レスポンス
     */
    public void toCategoryTreeDto(CategoryTreeDto categoryTreeDto, CategoryTreeResponse categoryTreeResponse) {

        convertCategoryTreeResponse(categoryTreeDto, categoryTreeResponse);
        List<CategoryTreeDto> categoryTreeDtoList = new ArrayList<>();

        if (categoryTreeResponse.getCategoryTreeResponse() != null) {
            categoryTreeResponse.getCategoryTreeResponse().forEach(response -> {

                CategoryTreeDto dto = new CategoryTreeDto();
                convertCategoryTreeResponse(dto, response);
                categoryTreeDtoList.add(dto);
                categoryTreeDto.setCategoryTreeDtoList(categoryTreeDtoList);

                toCategoryTreeDto(dto, response);
            });
        }
    }

    /**
     * カテゴリー木構造Dtoに変換
     *
     * @param categoryTreeDto          カテゴリー木構造Dtoクラス
     * @param categoryTreeResponse カテゴリ木構造レスポンス
     */
    private void convertCategoryTreeResponse(CategoryTreeDto categoryTreeDto,
                                             CategoryTreeResponse categoryTreeResponse) {
        if (!ObjectUtils.isEmpty(categoryTreeResponse)) {
            categoryTreeDto.setCategoryId(categoryTreeResponse.getCategoryId());
            categoryTreeDto.setDisplayName(categoryTreeResponse.getDisplayName());
            categoryTreeDto.setHierarchicalSerialNumber(categoryTreeResponse.getHierarchicalSerialNumber());
        }
    }

    /**
     * カテゴリー木構造取得リクエストを設定
     *
     * @return カテゴリー木構造取得リクエスト
     */
    protected CategoryTreeGetRequest setupCategoryTree() {

        // 最大表示回数
        String viewLevel = PropertiesUtil.getSystemPropertiesValue("goods.search.category.view.level");

        CategoryTreeGetRequest categoryTreeGetRequest = new CategoryTreeGetRequest();
        categoryTreeGetRequest.setOpenStatus(HTypeOpenStatus.OPEN.getValue());
        categoryTreeGetRequest.setMaxHierarchical(Integer.valueOf(viewLevel));

        return categoryTreeGetRequest;
    }

}
