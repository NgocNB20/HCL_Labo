/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDeleteRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品管理：商品詳細ページHelper<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsDetailsHelper extends AbstractGoodsRegistUpdateHelper {

    private final int PROCESS_TYPE_FROM_SCREEN = 0;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     * @param dateUtility
     */
    @Autowired
    public GoodsDetailsHelper(ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsDetailsModel ページ
     */
    public void toPageForLoad(GoodsDetailsModel goodsDetailsModel) {

        // 商品グループ画像登録更新用DTOリスト（ページ内表示用）の作成
        initTmpGoodsGroupImageRegistUpdateDtoList(goodsDetailsModel);

        // 商品共通情報部分セット
        setGoodsIndex(goodsDetailsModel);

        // 商品詳細部分セット
        setGoodsDetails(goodsDetailsModel);

        // 商品詳細テキスト部分セット
        setGoodsDetailstext(goodsDetailsModel);

        // 商品画像部分セット
        setGoodsImage(goodsDetailsModel);

        // 商品規格部分セット
        setGoodsUnit(goodsDetailsModel);

        // 商品在庫部分セット
        setGoodsStock(goodsDetailsModel);

        // 関連商品部分セット
        setGoodsRelation(goodsDetailsModel);

        // 在庫表示
        setStockStatus(goodsDetailsModel);

        // プレビュー日時設定
        setPreviewForm(goodsDetailsModel);
    }

    /**
     * 商品基本情報入力部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 商品グループエンティティと共通商品エンティティから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsIndex(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 商品グループエンティティ
        GoodsGroupEntity goodsGroup = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupEntity();
        // 共通商品エンティティ
        GoodsEntity goods = goodsDetailsModel.getCommonGoodsEntity();

        // 商品グループコード
        goodsDetailsModel.setGoodsGroupCode(goodsGroup.getGoodsGroupCode());
        // 商品グループ名
        goodsDetailsModel.setGoodsGroupName(goodsGroup.getGoodsGroupName());
        // 登録日時
        goodsDetailsModel.setRegistTime(goodsGroup.getRegistTime());
        // 更新日時
        goodsDetailsModel.setUpdateTime(goodsGroup.getUpdateTime());
        // 新着日時
        if (goodsGroup.getWhatsnewDate() != null) {
            goodsDetailsModel.setWhatsnewDate(conversionUtility.toYmd(goodsGroup.getWhatsnewDate()));
        }
        // 価格(税抜)
        goodsDetailsModel.setGoodsPrice(goodsGroup.getGoodsPrice());
        // 税率
        goodsDetailsModel.setTaxRate(goodsGroup.getTaxRate());
        // 酒類フラグ
        goodsDetailsModel.setAlcoholFlag(EnumTypeUtil.getValue(goodsGroup.getAlcoholFlag()));
        // ノベルティ商品フラグ
        goodsDetailsModel.setNoveltyGoodsType(EnumTypeUtil.getValue(goodsGroup.getNoveltyGoodsType()));
        // SNS連携フラグ
        goodsDetailsModel.setSnsLinkFlag(EnumTypeUtil.getValue(goodsGroup.getSnsLinkFlag()));
        // 個別配送
        goodsDetailsModel.setIndividualDeliveryType(goods.getIndividualDeliveryType().getValue());
        // 無料配送
        goodsDetailsModel.setFreeDeliveryFlag(goods.getFreeDeliveryFlag().getValue());
        // 公開状態PC
        goodsDetailsModel.setGoodsOpenStatusPC(goodsGroup.getGoodsOpenStatusPC().getValue());
        // 公開開始日PC
        goodsDetailsModel.setOpenStartDatePC(goodsGroup.getOpenStartTimePC());
        // 公開開始時間PC
        goodsDetailsModel.setOpenStartTimePC(goodsGroup.getOpenStartTimePC());
        // 公開終了日PC
        goodsDetailsModel.setOpenEndDatePC(goodsGroup.getOpenEndTimePC());
        // 公開終了時間PC
        goodsDetailsModel.setOpenEndTimePC(goodsGroup.getOpenEndTimePC());
        // フロント表示状態
        goodsDetailsModel.setFrontDisplay(ObjectUtils.isEmpty(goodsDetailsModel.getGoodsGroupDto().getFrontDisplay()) ?
                                                          null :
                                                          goodsDetailsModel.getGoodsGroupDto()
                                                                           .getFrontDisplay()
                                                                           .getValue());
    }

    /**
     * 商品詳細設定部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 商品グループエンティティと商品グループ表示エンティティから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsDetails(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 検索キーワード
        goodsDetailsModel.setSearchKeyword(goodsGroupDisplay.getSearchKeyword());
        // 商品インフォメーションアイコン情報
        goodsDetailsModel.setGoodsInformationIconItems(makeInformationIconList(goodsDetailsModel));
        // metaDescription
        goodsDetailsModel.setMetaDescription(goodsGroupDisplay.getMetaDescription());
        // metaKeyword
        goodsDetailsModel.setMetaKeyword(goodsGroupDisplay.getMetaKeyword());
        // 商品タグ
        goodsDetailsModel.setGoodsTagList(goodsGroupDisplay.getGoodsTagList());
    }

    /**
     * 商品詳細テキスト部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 商品グループ表示エンティティから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsDetailstext(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品説明１
        goodsDetailsModel.setGoodsNote1(goodsGroupDisplay.getGoodsNote1());
        // 商品説明２
        goodsDetailsModel.setGoodsNote2(goodsGroupDisplay.getGoodsNote2());
        // 商品説明３
        goodsDetailsModel.setGoodsNote3(goodsGroupDisplay.getGoodsNote3());
        // 商品説明４
        goodsDetailsModel.setGoodsNote4(goodsGroupDisplay.getGoodsNote4());
        // 商品説明５
        goodsDetailsModel.setGoodsNote5(goodsGroupDisplay.getGoodsNote5());
        // 商品説明６
        goodsDetailsModel.setGoodsNote6(goodsGroupDisplay.getGoodsNote6());
        // 商品説明７
        goodsDetailsModel.setGoodsNote7(goodsGroupDisplay.getGoodsNote7());
        // 商品説明８
        goodsDetailsModel.setGoodsNote8(goodsGroupDisplay.getGoodsNote8());
        // 商品説明９
        goodsDetailsModel.setGoodsNote9(goodsGroupDisplay.getGoodsNote9());
        // 商品説明１０
        goodsDetailsModel.setGoodsNote10(goodsGroupDisplay.getGoodsNote10());

        // 受注連携設定１
        goodsDetailsModel.setOrderSetting1(goodsGroupDisplay.getOrderSetting1());
        // 受注連携設定２
        goodsDetailsModel.setOrderSetting2(goodsGroupDisplay.getOrderSetting2());
        // 受注連携設定３
        goodsDetailsModel.setOrderSetting3(goodsGroupDisplay.getOrderSetting3());
        // 受注連携設定４
        goodsDetailsModel.setOrderSetting4(goodsGroupDisplay.getOrderSetting4());
        // 受注連携設定５
        goodsDetailsModel.setOrderSetting5(goodsGroupDisplay.getOrderSetting5());
        // 受注連携設定６
        goodsDetailsModel.setOrderSetting6(goodsGroupDisplay.getOrderSetting6());
        // 受注連携設定７
        goodsDetailsModel.setOrderSetting7(goodsGroupDisplay.getOrderSetting7());
        // 受注連携設定８
        goodsDetailsModel.setOrderSetting8(goodsGroupDisplay.getOrderSetting8());
        // 受注連携設定９
        goodsDetailsModel.setOrderSetting9(goodsGroupDisplay.getOrderSetting9());
        // 受注連携設定１０
        goodsDetailsModel.setOrderSetting10(goodsGroupDisplay.getOrderSetting10());

        // 商品納期
        goodsDetailsModel.setDeliveryType(goodsGroupDisplay.getDeliveryType());
    }

    /**
     * 商品画像部分のセット<br/>
     *
     * <pre>
     * 商品グループ画像のパスを取得し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsImage(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 詳細画像に関する設定
        setDetailsImageInfo(goodsDetailsModel);

    }

    /**
     * 商品グループ詳細画像に関する設定<br/>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     */
    protected void setDetailsImageInfo(GoodsDetailsModel goodsDetailsModel) {

        String maxCount = PropertiesUtil.getSystemPropertiesValue("goodsimage.max.count");

        // 設定ファイルに指定数分ループ
        List<GoodsDetailsImageItem> items = new ArrayList<>();
        for (Integer i = 1; i <= Integer.valueOf(maxCount); i++) {
            GoodsDetailsImageItem item = ApplicationContextUtility.getBean(GoodsDetailsImageItem.class);

            item.setImageNo(i);

            // 商品画像をセット
            item.setImagePath(getGoodsImageFilepath(goodsDetailsModel, i));
            items.add(item);
        }
        goodsDetailsModel.setDetailsPageDetailsImageItems(items);
    }

    /**
     * 商品規格部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 商品グループエンティティと商品グループ表示エンティティと商品DTOリストから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsUnit(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsDetailsModel.getGoodsGroupDto().getGoodsDtoList();

        // 規格管理フラグ
        if (goodsGroupDisplay.getUnitManagementFlag() != null) {
            goodsDetailsModel.setUnitManagementFlag(goodsGroupDisplay.getUnitManagementFlag().getValue());
        }
        // 規格１表示名
        goodsDetailsModel.setUnitTitle1(goodsGroupDisplay.getUnitTitle1());
        // 規格２表示名
        goodsDetailsModel.setUnitTitle2(goodsGroupDisplay.getUnitTitle2());
        // 商品規格リスト情報
        // 商品規格情報リストのセット
        int index = 0;
        List<GoodsDetailsUnitItem> unitItemList = new ArrayList<>();
        // 条件を満たすことはないのでデッドコードになっている
        if (goodsDtoList == null || goodsDtoList.size() == 0) {
            return;
        }
        for (GoodsDto goodsDto : goodsDtoList) {
            if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()) {
                // ステータス削除の場合は飛ばす
                continue;
            }
            GoodsDetailsUnitItem unitItem = ApplicationContextUtility.getBean(GoodsDetailsUnitItem.class);
            index = index + 1;
            unitItem.setUnitDspNo(index);
            unitItem.setGoodsSeq(goodsDto.getGoodsEntity().getGoodsSeq());
            unitItem.setGoodsCode(goodsDto.getGoodsEntity().getGoodsCode());
            unitItem.setJanCode(goodsDto.getGoodsEntity().getJanCode());
            unitItem.setUnitValue1(goodsDto.getGoodsEntity().getUnitValue1());
            unitItem.setUnitValue2(goodsDto.getGoodsEntity().getUnitValue2());
            if (goodsDto.getGoodsEntity().getSaleStatusPC() != null) {
                unitItem.setGoodsSaleStatusPC(goodsDto.getGoodsEntity().getSaleStatusPC().getValue());
            }
            if (goodsDto.getGoodsEntity().getSaleStartTimePC() != null) {
                unitItem.setSaleStartDateTimePC(goodsDto.getGoodsEntity().getSaleStartTimePC());
            }
            if (goodsDto.getGoodsEntity().getSaleEndTimePC() != null) {
                unitItem.setSaleEndDateTimePC(goodsDto.getGoodsEntity().getSaleEndTimePC());
            }
            unitItem.setPurchasedMax(goodsDto.getGoodsEntity().getPurchasedMax());

            unitItemList.add(unitItem);
        }
        goodsDetailsModel.setUnitItems(unitItemList);
    }

    /**
     * 商品在庫部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 共通商品エンティティと商品DTOリストから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsStock(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 共通商品エンティティ
        GoodsEntity goods = goodsDetailsModel.getCommonGoodsEntity();
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsDetailsModel.getGoodsGroupDto().getGoodsDtoList();

        // 在庫管理フラグ
        if (goods.getStockManagementFlag() != null) {
            goodsDetailsModel.setStockManagementFlag(goods.getStockManagementFlag().getValue());
        }
        // 商品規格情報リストのセット
        int index = 0;
        List<GoodsDetailsStockItem> stockItemList = new ArrayList<>();
        // 条件を満たすことはないのでデッドコードになっている
        if (goodsDtoList == null || goodsDtoList.size() == 0) {
            return;
        }
        for (GoodsDto goodsDto : goodsDtoList) {
            if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()) {
                // ステータス削除の場合は飛ばす
                continue;
            }
            GoodsDetailsStockItem stockItem = ApplicationContextUtility.getBean(GoodsDetailsStockItem.class);
            index = index + 1;
            stockItem.setStockDspNo(index);
            stockItem.setGoodsSeq(goodsDto.getGoodsEntity().getGoodsSeq());
            stockItem.setGoodsCode(goodsDto.getGoodsEntity().getGoodsCode());
            stockItem.setJanCode(goodsDto.getGoodsEntity().getJanCode());
            stockItem.setUnitValue1(goodsDto.getGoodsEntity().getUnitValue1());
            stockItem.setUnitValue2(goodsDto.getGoodsEntity().getUnitValue2());
            if (goodsDto.getGoodsEntity().getSaleStatusPC() != null) {
                stockItem.setGoodsSaleStatusPC(goodsDto.getGoodsEntity().getSaleStatusPC().getValue());
            }
            // }
            if (goodsDto.getStockDto() != null) {
                stockItem.setRemainderFewStock(goodsDto.getStockDto().getRemainderFewStock());
                stockItem.setOrderPointStock(goodsDto.getStockDto().getOrderPointStock());
                stockItem.setSafetyStock(goodsDto.getStockDto().getSafetyStock());
                stockItem.setRealStock(goodsDto.getStockDto().getRealStock());
                stockItem.setSalesPossibleStock(goodsDto.getStockDto().getSalesPossibleStock());
                stockItem.setOrderReserveStock(goodsDto.getStockDto().getOrderReserveStock());
            }
            stockItemList.add(stockItem);
        }
        goodsDetailsModel.setStockItems(stockItemList);

    }

    /**
     * 商品関連商品部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品詳細ページが持つ、
     * 関連商品エンティティリストから必要な値を取出し、
     * 商品管理：商品詳細ページへセットする。
     * </pre>
     *
     * @param goodsDetailsModel 商品管理：商品詳細ページ
     * @param customParams      案件用引数
     */
    protected void setGoodsRelation(GoodsDetailsModel goodsDetailsModel, Object... customParams) {

        // 関連商品エンティティリスト
        List<GoodsRelationEntity> goodsRelationList = goodsDetailsModel.getGoodsRelationEntityList();
        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        // 関連商品情報リストのセット
        int index = 0;
        List<GoodsDetailsRelationItem> relationItems = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(goodsRelationList)) {
            for (GoodsRelationEntity goodsRelationEntity : goodsRelationList) {
                GoodsDetailsRelationItem relationItem =
                                ApplicationContextUtility.getBean(GoodsDetailsRelationItem.class);
                index = index + 1;
                relationItem.setRelationDspNo(index);
                relationItem.setRelationZenkakuNo(zenHanConversionUtility.toZenkaku(Integer.toString(index)));
                relationItem.setRelationGoodsGroupCode(goodsRelationEntity.getGoodsGroupCode());
                relationItem.setRelationGoodsGroupName(goodsRelationEntity.getGoodsGroupName());
                relationItems.add(relationItem);
            }
        }
        goodsDetailsModel.setRelationItems(relationItems);

        // ダミー用関連商品（空）を作成する
        List<GoodsDetailsRelationItem> dummyRelationItems = new ArrayList<>();
        for (int i = 0; i < goodsDetailsModel.getGoodsRelationAmount() - relationItems.size(); i++) {
            GoodsDetailsRelationItem relationItem = ApplicationContextUtility.getBean(GoodsDetailsRelationItem.class);
            relationItem.setRelationDspNo(0);
            relationItem.setRelationZenkakuNo("");
            relationItem.setRelationGoodsGroupCode("");
            relationItem.setRelationGoodsGroupName("");
            dummyRelationItems.add(relationItem);
        }
        goodsDetailsModel.setRelationNoItems(dummyRelationItems);
    }

    /**
     * 削除処理時のページ情報編集<br/>
     *
     * @param goodsDetailsModel ページ
     */
    public void toPageForDelete(GoodsDetailsModel goodsDetailsModel) {
        // 商品グループエンティティ
        GoodsGroupEntity goodsGroupEntity = goodsDetailsModel.getGoodsGroupDto().getGoodsGroupEntity();
        goodsDetailsModel.setOldGoodsOpenStatusPC(goodsGroupEntity.getGoodsOpenStatusPC());
        goodsGroupEntity.setGoodsOpenStatusPC(HTypeOpenDeleteStatus.DELETED);
    }

    /**
     * 商品インフォメーションアイコン情報リスト編集（画面表示用）<br/>
     *
     * @param goodsDetailsModel ページ
     * @return 商品インフォメーションアイコン情報リスト
     */
    public List<GoodsDetailsInformationIconItem> makeInformationIconList(GoodsDetailsModel goodsDetailsModel) {
        // 商品グループ表示
        GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                        goodsDetailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品インフォメーションアイコン情報を取得
        List<String> informationIconPC = new ArrayList<>();
        if (goodsGroupDisplayEntity.getInformationIconPC() != null || "".equals(
                        goodsGroupDisplayEntity.getInformationIconPC())) {
            informationIconPC = Arrays.asList(goodsDetailsModel.getGoodsGroupDto()
                                                               .getGoodsGroupDisplayEntity()
                                                               .getInformationIconPC()
                                                               .split("/"));
        }

        // ページ情報にセットされている商品インフォメーション情報のリスト（アイコンSEQ単位のリスト）から
        // 商品アイコン種別単位のリストを作成しています
        List<GoodsDetailsInformationIconItem> retList = new ArrayList<>();
        for (GoodsInformationIconDto iconDto : goodsDetailsModel.getIconList()) {
            GoodsDetailsInformationIconItem item =
                            ApplicationContextUtility.getBean(GoodsDetailsInformationIconItem.class);
            item.setIconSeq(iconDto.getGoodsInformationIconEntity().getIconSeq());
            item.setIconName(iconDto.getGoodsInformationIconEntity().getIconName());
            item.setColorCode(iconDto.getGoodsInformationIconEntity().getColorCode());
            if (informationIconPC.contains(item.getIconSeq().toString())) {
                // PCチェックボックスのチェック
                item.setCheckBoxPc(true);
            }
            retList.add(item);
        }
        return retList;
    }

    /**
     * 在庫状況表示を画面にセット
     *
     * @param goodsDetailsModel 商品詳細ページ
     */
    protected void setStockStatus(GoodsDetailsModel goodsDetailsModel) {

        GoodsGroupDto goodsGroup = goodsDetailsModel.getGoodsGroupDto();

        // 在庫状況更新バッチ実行時点の在庫状況を設定
        StockStatusDisplayEntity batchUpdateStockStatus = goodsGroup.getBatchUpdateStockStatus();
        // 商品グループ在庫状態PC
        goodsDetailsModel.setBatchUpdateStockStatusPc(EnumTypeUtil.getValue(batchUpdateStockStatus.getStockStatusPc()));

        // 現時点での在庫状況を設定
        StockStatusDisplayEntity realTimeStockStatus = goodsGroup.getRealTimeStockStatus();
        // 商品グループ在庫状態PC
        goodsDetailsModel.setRealTimeStockStatusPc(EnumTypeUtil.getValue(realTimeStockStatus.getStockStatusPc()));
    }

    /**
     * カテゴリエンティティリストに変換
     *
     * @param categoryGoodsResponse カテゴリ商品一覧レスポンス
     * @return カテゴリエンティティリスト
     */
    public List<CategoryEntity> toCategoryEntitiesFromResponse(List<CategoryGoodsResponse> categoryGoodsResponse) {

        List<CategoryEntity> categoryEntities = new ArrayList<>();

        for (CategoryGoodsResponse categoryGoodResponse : categoryGoodsResponse) {
            CategoryEntity categoryEntity = new CategoryEntity();

            categoryEntity.setCategorySeq(categoryGoodResponse.getCategorySeq());
            categoryEntity.setCategoryId(categoryGoodResponse.getCategoryId());
            categoryEntity.setCategoryName(categoryGoodResponse.getCategoryName());
            categoryEntity.setCategoryType(EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class,
                                                                         categoryGoodResponse.getCategoryType()
                                                                        ));
            categoryEntity.setRegistTime(conversionUtility.toTimestamp(categoryGoodResponse.getRegistTime()));
            categoryEntity.setUpdateTime(conversionUtility.toTimestamp(categoryGoodResponse.getUpdateTime()));

            categoryEntities.add(categoryEntity);
        }

        return categoryEntities;
    }

    /**
     * 商品グループ削除リクエストクラスに変換
     *
     * @return 商品グループ削除リクエスト
     */
    public ProductDeleteRequest toProductDeleteRequest() {

        ProductDeleteRequest productDeleteRequest = new ProductDeleteRequest();

        productDeleteRequest.setProcessType(PROCESS_TYPE_FROM_SCREEN);

        return productDeleteRequest;
    }

    /**
     * プレビュー日時が未設定の場合、現在日時を画面にセット
     *
     * @param goodsDetailsModel 商品詳細ページ
     */
    protected void setPreviewForm(GoodsDetailsModel goodsDetailsModel) {

        if (StringUtils.isBlank(goodsDetailsModel.getPreviewDate())) {
            goodsDetailsModel.setPreviewDate(this.dateUtility.getCurrentYmdWithSlash());
            goodsDetailsModel.setPreviewTime(this.dateUtility.getCurrentHMS());
        }
        // 日付は設定されているが、時刻未設定の場合
        else if (StringUtils.isNotBlank(goodsDetailsModel.getPreviewDate()) && StringUtils.isBlank(
                        goodsDetailsModel.getPreviewTime())) {
            goodsDetailsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }
    }
}