package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeAutoApprovalFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.admin.dto.goods.stock.StockDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsInformationIconEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.admin.entity.shop.ShopEntity;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.icon.presentation.api.param.IconResponse;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsRequest;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsImageItemRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsRelationRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.StockRequest;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayRequest;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsResponse;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import jp.co.itechh.quad.tax.presentation.api.param.TaxesResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品管理：商品登録更新（商品基本設定）ページHelper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsRegistUpdateHelper extends AbstractGoodsRegistUpdateHelper {

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     */
    @Autowired
    public GoodsRegistUpdateHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 更新時の初期表示データをページに反映<br/>
     *
     * @param goodsRegistUpdateModel     ページ
     */
    public void toPageForLoad(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        toPageForLoadCommon(goodsRegistUpdateModel);
        toPageForLoadDetails(goodsRegistUpdateModel);
        toPageForLoadDetailsText(goodsRegistUpdateModel);
        toPageForLoadUnit(goodsRegistUpdateModel);
        toPageForLoadImage(goodsRegistUpdateModel);
        toPageForLoadStock(goodsRegistUpdateModel);
        toPageForLoadRelation(goodsRegistUpdateModel);
    }

    /**
     * 入力情報をページにセット<br/>
     *
     * @param goodsRegistUpdateModel 商品管理：商品登録更新ページ
     */
    public void toPageForNext(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        toPageForNextCommon(goodsRegistUpdateModel);
        toPageForNextCategory(goodsRegistUpdateModel);
        toPageForNextDetails(goodsRegistUpdateModel);
        toPageForNextDetailsText(goodsRegistUpdateModel);
        toPageForNextUnit(goodsRegistUpdateModel);
        toPageForNextImage(goodsRegistUpdateModel);
        toPageForNextStock(goodsRegistUpdateModel);
        toPageForNextRelation(goodsRegistUpdateModel);
    }

    /***************************************************************************************************************************
     ** 商品基本設定
     ***************************************************************************************************************************/
    /**
     * 更新時の初期表示データをページに反映<br/>
     *
     * @param goodsRegistUpdateModel     ページ
     */
    private void toPageForLoadCommon(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループエンティティ
        GoodsGroupEntity goodsGroupEntity = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity();
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = goodsRegistUpdateModel.getCommonGoodsEntity();
        // 商品基本設定
        setToPageBasicSettings(goodsRegistUpdateModel, goodsGroupEntity);
        // 外部連携設定
        setToPageExternalCoordinationSettings(goodsRegistUpdateModel, goodsGroupEntity);
        // 価格設定
        setToPagePriceSettings(goodsRegistUpdateModel, goodsGroupEntity);
        // 配送設定
        setToPageDeliverySettings(goodsRegistUpdateModel, commonGoodsEntity);
        // 公開状態設定
        setToPageOpenSettings(goodsRegistUpdateModel, goodsGroupEntity);
    }

    /**
     * 商品基本設定項目をページに設定する<br/>
     *
     * @param goodsRegistUpdateModel ページクラス
     * @param goodsGroupEntity       商品グループEntity
     */
    private void setToPageBasicSettings(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                        GoodsGroupEntity goodsGroupEntity) {
        // 商品グループコード
        goodsRegistUpdateModel.setGoodsGroupCode(goodsGroupEntity.getGoodsGroupCode());
        // 商品名
        goodsRegistUpdateModel.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
        // 登録日時
        goodsRegistUpdateModel.setRegistTime(goodsGroupEntity.getRegistTime());
        // 更新日時
        goodsRegistUpdateModel.setUpdateTime(goodsGroupEntity.getUpdateTime());
        // 新着日時
        goodsRegistUpdateModel.setWhatsnewDate(conversionUtility.toYmd(goodsGroupEntity.getWhatsnewDate()));
        // 酒類フラグ
        goodsRegistUpdateModel.setAlcoholFlag(goodsGroupEntity.getAlcoholFlag().getValue());
        // ノベルティ商品フラグ
        goodsRegistUpdateModel.setNoveltyGoodsType(goodsGroupEntity.getNoveltyGoodsType().getValue());
    }

    /**
     * 外部連携設定項目をページに設定する<br/>
     *
     * @param goodsRegistUpdateModel ページクラス
     * @param goodsGroupEntity       商品グループEntity
     */
    private void setToPageExternalCoordinationSettings(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                                       GoodsGroupEntity goodsGroupEntity) {
        // SNS連携
        goodsRegistUpdateModel.setSnsLinkFlag(EnumTypeUtil.getValue(goodsGroupEntity.getSnsLinkFlag()));
    }

    /**
     * 価格設定項目をページに設定する<br/>
     *
     * @param goodsRegistUpdateModel ページクラス
     * @param goodsGroupEntity       商品グループEntity
     */
    private void setToPagePriceSettings(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                        GoodsGroupEntity goodsGroupEntity) {
        // 価格(税抜)
        goodsRegistUpdateModel.setGoodsPrice(String.valueOf(goodsGroupEntity.getGoodsPrice()));
        // 税率
        goodsRegistUpdateModel.setTaxRate(goodsGroupEntity.getTaxRate());
    }

    /**
     * 配送設定項目をページに設定する<br/>
     *
     * @param goodsRegistUpdateModel ページクラス
     * @param commonGoodsEntity      共通商品Entity
     */
    private void setToPageDeliverySettings(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                           GoodsEntity commonGoodsEntity) {

        // 個別配送
        goodsRegistUpdateModel.setIndividualDeliveryType(commonGoodsEntity.getIndividualDeliveryType().getValue());
        // 無料配送
        goodsRegistUpdateModel.setFreeDeliveryFlag(commonGoodsEntity.getFreeDeliveryFlag().getValue());
    }

    /**
     * 公開状態設定項目をページに設定する<br/>
     *
     * @param goodsRegistUpdateModel ページクラス
     * @param goodsGroupEntity       商品グループEntity
     */
    private void setToPageOpenSettings(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                       GoodsGroupEntity goodsGroupEntity) {
        // 公開状態PC
        if (goodsGroupEntity.getGoodsOpenStatusPC() != null) {
            goodsRegistUpdateModel.setGoodsOpenStatusPC(goodsGroupEntity.getGoodsOpenStatusPC().getValue());
        }
        // 公開開始日PC
        if (goodsGroupEntity.getOpenStartTimePC() != null) {
            goodsRegistUpdateModel.setGoodsOpenStartDatePC(
                            conversionUtility.toYmd(goodsGroupEntity.getOpenStartTimePC()));
            goodsRegistUpdateModel.setGoodsOpenStartTimePC(
                            conversionUtility.toHms(goodsGroupEntity.getOpenStartTimePC()));
        }
        // 公開終了日PC
        if (goodsGroupEntity.getOpenEndTimePC() != null) {
            goodsRegistUpdateModel.setGoodsOpenEndDatePC(conversionUtility.toYmd(goodsGroupEntity.getOpenEndTimePC()));
            goodsRegistUpdateModel.setGoodsOpenEndTimePC(conversionUtility.toHms(goodsGroupEntity.getOpenEndTimePC()));
        }
    }

    /**
     * 入力情報をページにセット<br/>
     *
     * @param goodsRegistUpdateModel 商品管理：商品登録更新（商品基本設定）ページ
     */
    private void toPageForNextCommon(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        // 商品グループエンティティ
        GoodsGroupEntity goodsGroupEntity = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity();
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = goodsRegistUpdateModel.getCommonGoodsEntity();

        // 商品基本設定
        setToEntityBasicSettings(goodsGroupEntity, commonGoodsEntity, goodsRegistUpdateModel);
        // 外部連携設定
        setToEntityExternalCoordinationSettings(goodsGroupEntity, goodsRegistUpdateModel);
        // 価格設定
        setToEntityPriceSettings(goodsGroupEntity, goodsRegistUpdateModel);
        // 配送設定
        setToEntityDeliverySettings(commonGoodsEntity, goodsRegistUpdateModel);
        // 公開状態設定
        setToEntityOpenSettings(goodsGroupEntity, goodsRegistUpdateModel);
    }

    /**
     * 商品基本設定項目をEntityにセットする<br/>
     *
     * @param goodsGroupEntity       商品グループEntity
     * @param commonGoodsEntity      共通商品Entity
     * @param goodsRegistUpdateModel ページクラス
     */
    private void setToEntityBasicSettings(GoodsGroupEntity goodsGroupEntity,
                                          GoodsEntity commonGoodsEntity,
                                          GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループコード
        goodsGroupEntity.setGoodsGroupCode(goodsRegistUpdateModel.getGoodsGroupCode());
        // 商品名
        goodsGroupEntity.setGoodsGroupName(goodsRegistUpdateModel.getGoodsGroupName());
        if (goodsRegistUpdateModel.isRegist() && StringUtil.isEmpty(goodsGroupEntity.getGoodsGroupName())) {
            // 新規登録かつ未設定時は商品名を設定する
            // 商品グループ名PC
            goodsGroupEntity.setGoodsGroupName(goodsRegistUpdateModel.getGoodsGroupName());
            // 新規登録かつ未設定時は商品名を半角変換して設定する
        }
        // 新着日時
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimeStamp(goodsRegistUpdateModel.getWhatsnewDate()));
        // 酒類フラグ
        goodsGroupEntity.setAlcoholFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, goodsRegistUpdateModel.getAlcoholFlag()));
        // ノベルティ商品フラグ
        goodsGroupEntity.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                           goodsRegistUpdateModel.getNoveltyGoodsType()
                                                                          ));
    }

    /**
     * 外部連携設定項目をEntityにセットする<br/>
     *
     * @param goodsGroupEntity       商品グループEntity
     * @param goodsRegistUpdateModel ページクラス
     */
    private void setToEntityExternalCoordinationSettings(GoodsGroupEntity goodsGroupEntity,
                                                         GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // SNS連携フラグ
        goodsGroupEntity.setSnsLinkFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, goodsRegistUpdateModel.getSnsLinkFlag()));
    }

    /**
     * 価格設定項目をEntityにセットする<br/>
     *
     * @param goodsGroupEntity       商品グループEntity
     * @param goodsRegistUpdateModel ページクラス
     */
    private void setToEntityPriceSettings(GoodsGroupEntity goodsGroupEntity,
                                          GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 価格(税抜)
        goodsGroupEntity.setGoodsPrice(conversionUtility.toBigDecimal(goodsRegistUpdateModel.getGoodsPrice()));
        // 税率
        goodsGroupEntity.setTaxRate(goodsRegistUpdateModel.getTaxRate());
    }

    /**
     * 配送設定項目をEntityにセットする<br/>
     *
     * @param commonGoodsEntity      共通商品Entity
     * @param goodsRegistUpdateModel ページクラス
     */
    private void setToEntityDeliverySettings(GoodsEntity commonGoodsEntity,
                                             GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 個別配送
        commonGoodsEntity.setIndividualDeliveryType(EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                                  goodsRegistUpdateModel.getIndividualDeliveryType()
                                                                                 ));
        // 無料配送
        commonGoodsEntity.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                            goodsRegistUpdateModel.getFreeDeliveryFlag()
                                                                           ));
    }

    /**
     * 公開状態設定項目をEntityにセットする<br/>
     *
     * @param goodsGroupEntity       商品グループEntity
     * @param goodsRegistUpdateModel ページクラス
     */
    private void setToEntityOpenSettings(GoodsGroupEntity goodsGroupEntity,
                                         GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 公開状態
        goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                            goodsRegistUpdateModel.getGoodsOpenStatusPC()
                                                                           ));
        // 公開開始日時
        goodsGroupEntity.setOpenStartTimePC(
                        conversionUtility.toTimeStampWithDefaultHms(goodsRegistUpdateModel.getGoodsOpenStartDatePC(),
                                                                    goodsRegistUpdateModel.getGoodsOpenStartTimePC(),
                                                                    ConversionUtility.DEFAULT_START_TIME
                                                                   ));
        // 公開終了日時
        goodsGroupEntity.setOpenEndTimePC(
                        conversionUtility.toTimeStampWithDefaultHms(goodsRegistUpdateModel.getGoodsOpenEndDatePC(),
                                                                    goodsRegistUpdateModel.getGoodsOpenEndTimePC(),
                                                                    ConversionUtility.DEFAULT_END_TIME
                                                                   ));
    }

    /**
     * 選択されたカテゴリSEQリストをカテゴリ商品登録情報リストに反映<br/>
     *
     * @param goodsRegistUpdateModel 商品登録更新（カテゴリ設定）ページ
     */
    private void toPageForNextCategory(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsRegistUpdateModel.getLinkedCategoryList())) {
            // カテゴリ登録商品取得（手動）
            for (CategoryEntity categoryEntity : goodsRegistUpdateModel.getLinkedCategoryList()) {
                // 新しくチェックされたカテゴリ(手動)
                CategoryGoodsEntity newCategoryGoodsEntity =
                                ApplicationContextUtility.getBean(CategoryGoodsEntity.class);
                newCategoryGoodsEntity.setCategorySeq(categoryEntity.getCategorySeq());
                newCategoryGoodsEntity.setCategoryName(categoryEntity.getCategoryName());
                newCategoryGoodsEntity.setGoodsGroupSeq(
                                goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq());
                newCategoryGoodsEntity.setCategoryType(categoryEntity.getCategoryType());
                categoryGoodsEntityList.add(newCategoryGoodsEntity);
            }
        }

        goodsRegistUpdateModel.getGoodsGroupDto().setCategoryGoodsEntityList(categoryGoodsEntityList);
    }

    /***************************************************************************************************************************
     ** 商品詳細設定
     ***************************************************************************************************************************/
    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param detailsModel ページ
     */
    private void toPageForLoadDetails(GoodsRegistUpdateModel detailsModel) {
        // 商品グループ画像登録更新用DTOリスト（ページ内編集用）の作成
        initTmpGoodsGroupImageRegistUpdateDtoList(detailsModel);

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = detailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品インフォメーションアイコン情報
        detailsModel.setGoodsInformationIconItems(makeInformationIconList(detailsModel));
        // metaDescription
        detailsModel.setMetaDescription(goodsGroupDisplayEntity.getMetaDescription());
        // metaKeyword
        detailsModel.setMetaKeyword(goodsGroupDisplayEntity.getMetaKeyword());
        // 検索キーワード
        detailsModel.setSearchKeyword(goodsGroupDisplayEntity.getSearchKeyword());
    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param detailsModel ページ
     */
    private void toPageForNextDetails(GoodsRegistUpdateModel detailsModel) {

        // (注意) 画像ファイル名が固定でない場合は、商品グループ内ファイル名の重複チェックを行う！

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = detailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // metaDescription
        goodsGroupDisplayEntity.setMetaDescription(detailsModel.getMetaDescription());
        // metaKeyword
        goodsGroupDisplayEntity.setMetaKeyword(detailsModel.getMetaKeyword());
        // 検索キーワード
        goodsGroupDisplayEntity.setSearchKeyword(detailsModel.getSearchKeyword());

        goodsGroupDisplayEntity.setGoodsTagList(detailsModel.getGoodsTagList());

        // 商品インフォメーションアイコン情報
        List<String> informationIconSeqListPc = new ArrayList<>();
        if (detailsModel.getGoodsInformationIconItems() != null) {
            for (GoodsRegistUpdateIconItem detailsPageItem : detailsModel.getGoodsInformationIconItems()) {
                if (detailsPageItem.isCheckBoxPc()) {
                    informationIconSeqListPc.add(detailsPageItem.getIconSeq().toString());
                }
            }
        }
        // アイコンSEQ順にソート
        Collections.sort(informationIconSeqListPc);
        goodsGroupDisplayEntity.setInformationIconPC(
                        joinSlashString(informationIconSeqListPc.toArray(new String[] {})));

        // 商品グループ画像登録更新用DTOリスト（ページ内編集用）をセッションにセット
        setTmpGoodsGroupImageRegistUpdateDtoList(detailsModel);
    }

    /**
     * 商品インフォメーションアイコン情報リスト編集（画面表示用）<br/>
     *
     * @param detailsModel ページ
     * @return 商品インフォメーションアイコン情報リスト
     */
    private List<GoodsRegistUpdateIconItem> makeInformationIconList(GoodsRegistUpdateModel detailsModel) {
        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = detailsModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品インフォメーションアイコン情報を取得
        List<String> informationIconPC = new ArrayList<>();
        if (goodsGroupDisplayEntity.getInformationIconPC() != null || "".equals(
                        goodsGroupDisplayEntity.getInformationIconPC())) {
            informationIconPC = Arrays.asList(detailsModel.getGoodsGroupDto()
                                                          .getGoodsGroupDisplayEntity()
                                                          .getInformationIconPC()
                                                          .split("/"));
        }

        // ページ情報にセットされている商品インフォメーション情報のリスト（アイコンSEQ単位のリスト）から
        // 商品アイコン種別単位のリストを作成しています
        List<GoodsRegistUpdateIconItem> retList = new ArrayList<>();
        for (GoodsInformationIconDto iconDto : detailsModel.getIconList()) {
            GoodsRegistUpdateIconItem item = ApplicationContextUtility.getBean(GoodsRegistUpdateIconItem.class);
            item.setIconSeq(iconDto.getGoodsInformationIconEntity().getIconSeq());
            item.setIconName(iconDto.getGoodsInformationIconEntity().getIconName());
            item.setColorCode(iconDto.getGoodsInformationIconEntity().getColorCode());
            if (informationIconPC.contains(item.getIconSeq().toString())) {
                // チェックボックスのチェック
                item.setCheckBoxPc(true);
            }
            retList.add(item);
        }
        return retList;
    }

    /***************************************************************************************************************************
     ** 商品詳細テキスト設定
     ***************************************************************************************************************************/
    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForLoadDetailsText(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品説明01
        goodsRegistUpdateModel.setGoodsNote1(goodsGroupDisplayEntity.getGoodsNote1());
        // 商品説明02
        goodsRegistUpdateModel.setGoodsNote2(goodsGroupDisplayEntity.getGoodsNote2());
        // 商品説明03
        goodsRegistUpdateModel.setGoodsNote3(goodsGroupDisplayEntity.getGoodsNote3());
        // 商品説明04
        goodsRegistUpdateModel.setGoodsNote4(goodsGroupDisplayEntity.getGoodsNote4());
        // 商品説明05
        goodsRegistUpdateModel.setGoodsNote5(goodsGroupDisplayEntity.getGoodsNote5());
        // 商品説明06
        goodsRegistUpdateModel.setGoodsNote6(goodsGroupDisplayEntity.getGoodsNote6());
        // 商品説明07
        goodsRegistUpdateModel.setGoodsNote7(goodsGroupDisplayEntity.getGoodsNote7());
        // 商品説明08
        goodsRegistUpdateModel.setGoodsNote8(goodsGroupDisplayEntity.getGoodsNote8());
        // 商品説明09
        goodsRegistUpdateModel.setGoodsNote9(goodsGroupDisplayEntity.getGoodsNote9());
        // 商品説明10
        goodsRegistUpdateModel.setGoodsNote10(goodsGroupDisplayEntity.getGoodsNote10());

        // 受注連携設定01
        goodsRegistUpdateModel.setOrderSetting1(goodsGroupDisplayEntity.getOrderSetting1());
        // 受注連携設定02
        goodsRegistUpdateModel.setOrderSetting2(goodsGroupDisplayEntity.getOrderSetting2());
        // 受注連携設定03
        goodsRegistUpdateModel.setOrderSetting3(goodsGroupDisplayEntity.getOrderSetting3());
        // 受注連携設定04
        goodsRegistUpdateModel.setOrderSetting4(goodsGroupDisplayEntity.getOrderSetting4());
        // 受注連携設定05
        goodsRegistUpdateModel.setOrderSetting5(goodsGroupDisplayEntity.getOrderSetting5());
        // 受注連携設定06
        goodsRegistUpdateModel.setOrderSetting6(goodsGroupDisplayEntity.getOrderSetting6());
        // 受注連携設定07
        goodsRegistUpdateModel.setOrderSetting7(goodsGroupDisplayEntity.getOrderSetting7());
        // 受注連携設定08
        goodsRegistUpdateModel.setOrderSetting8(goodsGroupDisplayEntity.getOrderSetting8());
        // 受注連携設定09
        goodsRegistUpdateModel.setOrderSetting9(goodsGroupDisplayEntity.getOrderSetting9());
        // 受注連携設定10
        goodsRegistUpdateModel.setOrderSetting10(goodsGroupDisplayEntity.getOrderSetting10());

        // 商品納期
        goodsRegistUpdateModel.setDeliveryType(goodsGroupDisplayEntity.getDeliveryType());

    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForNextDetailsText(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 入力項目をセット
        // 商品説明01
        goodsGroupDisplayEntity.setGoodsNote1(goodsRegistUpdateModel.getGoodsNote1());
        // 商品説明02
        goodsGroupDisplayEntity.setGoodsNote2(goodsRegistUpdateModel.getGoodsNote2());
        // 商品説明03
        goodsGroupDisplayEntity.setGoodsNote3(goodsRegistUpdateModel.getGoodsNote3());
        // 商品説明04
        goodsGroupDisplayEntity.setGoodsNote4(goodsRegistUpdateModel.getGoodsNote4());
        // 商品説明05
        goodsGroupDisplayEntity.setGoodsNote5(goodsRegistUpdateModel.getGoodsNote5());
        // 商品説明06
        goodsGroupDisplayEntity.setGoodsNote6(goodsRegistUpdateModel.getGoodsNote6());
        // 商品説明07
        goodsGroupDisplayEntity.setGoodsNote7(goodsRegistUpdateModel.getGoodsNote7());
        // 商品説明08
        goodsGroupDisplayEntity.setGoodsNote8(goodsRegistUpdateModel.getGoodsNote8());
        // 商品説明09
        goodsGroupDisplayEntity.setGoodsNote9(goodsRegistUpdateModel.getGoodsNote9());
        // 商品説明10
        goodsGroupDisplayEntity.setGoodsNote10(goodsRegistUpdateModel.getGoodsNote10());

        // 受注連携設定01
        goodsGroupDisplayEntity.setOrderSetting1(goodsRegistUpdateModel.getOrderSetting1());
        // 受注連携設定02
        goodsGroupDisplayEntity.setOrderSetting2(goodsRegistUpdateModel.getOrderSetting2());
        // 受注連携設定03
        goodsGroupDisplayEntity.setOrderSetting3(goodsRegistUpdateModel.getOrderSetting3());
        // 受注連携設定04
        goodsGroupDisplayEntity.setOrderSetting4(goodsRegistUpdateModel.getOrderSetting4());
        // 受注連携設定05
        goodsGroupDisplayEntity.setOrderSetting5(goodsRegistUpdateModel.getOrderSetting5());
        // 受注連携設定06
        goodsGroupDisplayEntity.setOrderSetting6(goodsRegistUpdateModel.getOrderSetting6());
        // 受注連携設定07
        goodsGroupDisplayEntity.setOrderSetting7(goodsRegistUpdateModel.getOrderSetting7());
        // 受注連携設定08
        goodsGroupDisplayEntity.setOrderSetting8(goodsRegistUpdateModel.getOrderSetting8());
        // 受注連携設定09
        goodsGroupDisplayEntity.setOrderSetting9(goodsRegistUpdateModel.getOrderSetting9());
        // 受注連携設定10
        goodsGroupDisplayEntity.setOrderSetting10(goodsRegistUpdateModel.getOrderSetting10());

        // 商品納期
        goodsGroupDisplayEntity.setDeliveryType(goodsRegistUpdateModel.getDeliveryType());

        // 商品タグ
        if (!ListUtils.isEmpty(goodsRegistUpdateModel.getGoodsTagList())) {
            goodsGroupDisplayEntity.setGoodsTagList(
                            goodsRegistUpdateModel.getGoodsTagList().stream().distinct().collect(Collectors.toList()));
        }

    }

    /***************************************************************************************************************************
     ** 規格設定
     ***************************************************************************************************************************/

    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param unitPage ページ
     */
    public void toPageForLoadUnit(GoodsRegistUpdateModel unitPage) {
        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = unitPage.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 規格管理フラグ
        if (goodsGroupDisplayEntity.getUnitManagementFlag() != null) {
            unitPage.setUnitManagementFlag(goodsGroupDisplayEntity.getUnitManagementFlag().getValue());
        }
        // 規格１表示名
        unitPage.setUnitTitle1(goodsGroupDisplayEntity.getUnitTitle1());
        // 規格２表示名
        unitPage.setUnitTitle2(goodsGroupDisplayEntity.getUnitTitle2());

        // 商品規格情報リストのセット
        toPageGoodsDataForLoad(unitPage);
        // 商品規格が0件の場合は1件追加する
        if (unitPage.getUnitCount() == 0) {
            toPageForAddGoods(unitPage);
            toPageGoodsDataForLoad(unitPage);
        }
    }

    /**
     * 商品規格情報をページにセット<br/>
     *
     * @param unitPage ページ
     */
    private void toPageGoodsDataForLoad(GoodsRegistUpdateModel unitPage) {

        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = unitPage.getGoodsGroupDto().getGoodsDtoList();

        int index = 0;
        List<GoodsRegistUpdateUnitItem> unitItemList = new ArrayList<>();
        List<GoodsRegistUpdateStockItem> stockItemList = new ArrayList<>();
        if (goodsDtoList != null && goodsDtoList.size() > 0) {
            for (GoodsDto goodsDto : goodsDtoList) {
                if (HTypeGoodsSaleStatus.DELETED.equals(goodsDto.getGoodsEntity().getSaleStatusPC())) {
                    // ステータス削除の場合は飛ばす
                    continue;
                }
                int indexUpdate = ++index;
                GoodsRegistUpdateUnitItem unitItem = createUnitItem(goodsDto);
                unitItem.setUnitDspNo(indexUpdate);
                unitItemList.add(unitItem);

                GoodsRegistUpdateStockItem stockItem = createStockItem(goodsDto);
                stockItem.setStockDspNo(indexUpdate);
                stockItemList.add(stockItem);
            }
            unitPage.setUnitItems(unitItemList);
            unitPage.setUnitCount(unitItemList.size());
            unitPage.setStockItems(stockItemList);
        }
    }

    /**
     * 商品Dtoから規格Itemを作成する<br/>
     *
     * @param goodsDto 商品Dto
     * @return UnitPageUnitItem
     */
    private GoodsRegistUpdateUnitItem createUnitItem(GoodsDto goodsDto) {
        GoodsRegistUpdateUnitItem unitItem = ApplicationContextUtility.getBean(GoodsRegistUpdateUnitItem.class);
        unitItem.setGoodsSeq(goodsDto.getGoodsEntity().getGoodsSeq());
        unitItem.setGoodsCode(goodsDto.getGoodsEntity().getGoodsCode());
        unitItem.setOrderDisplay(goodsDto.getGoodsEntity().getOrderDisplay());
        unitItem.setJanCode(goodsDto.getGoodsEntity().getJanCode());
        unitItem.setUnitValue1(goodsDto.getGoodsEntity().getUnitValue1());
        unitItem.setUnitValue2(goodsDto.getGoodsEntity().getUnitValue2());
        if (goodsDto.getGoodsEntity().getSaleStatusPC() != null) {
            unitItem.setGoodsSaleStatusPC(goodsDto.getGoodsEntity().getSaleStatusPC().getValue());
        }
        if (goodsDto.getGoodsEntity().getSaleStartTimePC() != null) {
            unitItem.setSaleStartDatePC(conversionUtility.toYmd(goodsDto.getGoodsEntity().getSaleStartTimePC()));
            unitItem.setSaleStartTimePC(conversionUtility.toHms(goodsDto.getGoodsEntity().getSaleStartTimePC()));
        }
        if (goodsDto.getGoodsEntity().getSaleEndTimePC() != null) {
            unitItem.setSaleEndDatePC(conversionUtility.toYmd(goodsDto.getGoodsEntity().getSaleEndTimePC()));
            unitItem.setSaleEndTimePC(conversionUtility.toHms(goodsDto.getGoodsEntity().getSaleEndTimePC()));
        }
        unitItem.setPurchasedMax(conversionUtility.toString(goodsDto.getGoodsEntity().getPurchasedMax()));

        return unitItem;
    }

    /**
     * 商品Dtoから商品在庫を設定する<br/>
     *
     * @param goodsDto 商品Dto
     * @return 商品在庫設定ページ情報
     */
    private GoodsRegistUpdateStockItem createStockItem(GoodsDto goodsDto) {
        GoodsRegistUpdateStockItem stockItem = ApplicationContextUtility.getBean(GoodsRegistUpdateStockItem.class);
        stockItem.setGoodsSeq(goodsDto.getGoodsEntity().getGoodsSeq());
        stockItem.setGoodsCode(goodsDto.getGoodsEntity().getGoodsCode());
        stockItem.setJanCode(goodsDto.getGoodsEntity().getJanCode());
        stockItem.setUnitValue1(goodsDto.getGoodsEntity().getUnitValue1());
        stockItem.setUnitValue2(goodsDto.getGoodsEntity().getUnitValue2());
        if (goodsDto.getGoodsEntity().getSaleStatusPC() != null) {
            stockItem.setGoodsSaleStatusPC(goodsDto.getGoodsEntity().getSaleStatusPC().getValue());
        }
        if (goodsDto.getStockDto().getSupplementCount() != null) {
            stockItem.setSupplementCount(String.valueOf(goodsDto.getStockDto().getSupplementCount()));
        }
        if (goodsDto.getStockDto().getSafetyStock() != null) {
            stockItem.setSafetyStock(String.valueOf(goodsDto.getStockDto().getSafetyStock()));
        }
        if (goodsDto.getStockDto().getRemainderFewStock() != null) {
            stockItem.setRemainderFewStock(String.valueOf(goodsDto.getStockDto().getRemainderFewStock()));
        }
        if (goodsDto.getStockDto().getOrderPointStock() != null) {
            stockItem.setOrderPointStock(String.valueOf(goodsDto.getStockDto().getOrderPointStock()));
        }
        return stockItem;
    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param unitPage ページ
     */
    private void toPageForNextUnit(GoodsRegistUpdateModel unitPage) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = unitPage.getGoodsGroupDto().getGoodsGroupDisplayEntity();
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = unitPage.getCommonGoodsEntity();

        // 規格管理フラグ
        goodsGroupDisplayEntity.setUnitManagementFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, unitPage.getUnitManagementFlag()));
        // 共通商品Dtoに規格管理フラグをセット
        commonGoodsEntity.setUnitManagementFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, unitPage.getUnitManagementFlag()));
        if (HTypeUnitManagementFlag.ON.getValue().equals(unitPage.getUnitManagementFlag())) {
            // 規格１表示名
            goodsGroupDisplayEntity.setUnitTitle1(unitPage.getUnitTitle1());
            // 規格２表示名
            goodsGroupDisplayEntity.setUnitTitle2(unitPage.getUnitTitle2());
        } else {
            // 規格１表示名
            goodsGroupDisplayEntity.setUnitTitle1(null);
            // 規格２表示名
            goodsGroupDisplayEntity.setUnitTitle2(null);
        }

        // 商品規格情報をページに反映
        toPageForGoodsInfoList(unitPage, false);
    }

    /**
     * 入力情報をページに反映(同一ページ内遷移)<br/>
     *
     * @param unitPage ページ
     */
    public void toPageForSame(GoodsRegistUpdateModel unitPage) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = unitPage.getGoodsGroupDto().getGoodsGroupDisplayEntity();
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = unitPage.getCommonGoodsEntity();

        // 規格管理フラグ
        goodsGroupDisplayEntity.setUnitManagementFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, unitPage.getUnitManagementFlag()));
        // 共通商品Dtoに規格管理フラグをセット
        commonGoodsEntity.setUnitManagementFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, unitPage.getUnitManagementFlag()));
        // 規格１表示名
        goodsGroupDisplayEntity.setUnitTitle1(unitPage.getUnitTitle1());
        // 規格２表示名
        goodsGroupDisplayEntity.setUnitTitle2(unitPage.getUnitTitle2());

        // 商品規格情報をページに反映
        toPageForGoodsInfoList(unitPage, true);
    }

    /**
     * 商品規格情報追加<br/>
     *
     * @param unitPage ページ
     */
    public void toPageForAddGoods(GoodsRegistUpdateModel unitPage) {
        // 商品DTO生成してリストにセット
        GoodsDto goodsDto = ApplicationContextUtility.getBean(GoodsDto.class);
        goodsDto.setGoodsEntity(ApplicationContextUtility.getBean(GoodsEntity.class));

        // 商品DTO共通情報をセット
        toGoodsDtoFromCommonGoods(unitPage, goodsDto.getGoodsEntity());
        goodsDto.getGoodsEntity()
                .setGoodsGroupSeq(unitPage.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq());
        goodsDto.getGoodsEntity().setShopSeq(unitPage.getGoodsGroupDto().getGoodsGroupEntity().getShopSeq());

        // 在庫DTOを生成
        goodsDto.setStockDto(ApplicationContextUtility.getBean(StockDto.class));
        goodsDto.getStockDto().setShopSeq(unitPage.getGoodsGroupDto().getGoodsGroupEntity().getShopSeq());
        goodsDto.getStockDto().setRealStock(new BigDecimal(0));
        goodsDto.getStockDto().setRemainderFewStock(new BigDecimal(0));
        goodsDto.getStockDto().setOrderPointStock(new BigDecimal(0));
        goodsDto.getStockDto().setSafetyStock(new BigDecimal(0));

        goodsDto.getGoodsEntity().setPurchasedMax(new BigDecimal(999));

        // 表示順を設定
        // 商品規格リスト最後尾の表示順＋１をセット
        if (unitPage.getGoodsGroupDto().getGoodsDtoList() == null) {
            unitPage.getGoodsGroupDto().setGoodsDtoList(new ArrayList<>());
        }
        if (unitPage.getUnitItems() != null && unitPage.getUnitItems().size() > 0) {
            GoodsRegistUpdateUnitItem lastUnitItem = unitPage.getUnitItems().get(unitPage.getUnitItems().size() - 1);
            goodsDto.getGoodsEntity().setOrderDisplay(lastUnitItem.getOrderDisplay().intValue() + 1);
        } else {
            goodsDto.getGoodsEntity().setOrderDisplay(1);
        }

        unitPage.getGoodsGroupDto().getGoodsDtoList().add(goodsDto);
    }

    /**
     * 商品規格情報削除<br/>
     *
     * @param unitPage ページ
     */
    public void toPageForDeleteGoods(GoodsRegistUpdateModel unitPage) {
        // 画面から選択商品コードを取得する
        Integer selectDspNo = unitPage.getSelectDspNo();

        // 該当する規格情報を取得する
        GoodsDto goodsDto = null;
        int count = 0;
        for (GoodsDto tmpGoodsDto : unitPage.getGoodsGroupDto().getGoodsDtoList()) {
            if (HTypeGoodsSaleStatus.DELETED.equals(tmpGoodsDto.getGoodsEntity().getSaleStatusPC())) {
                // ステータス削除の場合は飛ばす
                continue;
            } else {
                count++;
            }
            if (selectDspNo != null && selectDspNo.intValue() == count) {
                goodsDto = tmpGoodsDto;
            }
        }
        if (goodsDto == null) {
            return;
        }

        // 商品SEQがnullでない場合（＝既にDB登録済み）、販売状態＝"削除"をセットする
        if (goodsDto.getGoodsEntity().getGoodsSeq() != null) {
            goodsDto.getGoodsEntity().setSaleStatusPC(HTypeGoodsSaleStatus.DELETED);
        }

        // 上記以外の場合（DB未登録）、セッションの商品DTOリストから削除する
        else {
            unitPage.getGoodsGroupDto().getGoodsDtoList().remove(goodsDto);
        }
    }

    /**
     * 商品表示順変更(上)<br/>
     *
     * @param unitPage ページ
     */
    public void toPageForUpGoods(GoodsRegistUpdateModel unitPage) {
        orderDisplayChangeUnit(unitPage, true);
    }

    /**
     * 商品表示順変更(下)<br/>
     *
     * @param unitPage ページ
     */
    public void toPageForDownGoods(GoodsRegistUpdateModel unitPage) {
        orderDisplayChangeUnit(unitPage, false);
    }

    /**
     * 商品表示順変更<br/>
     *
     * @param unitPage ページ
     * @param upFlg    true: 上へ移動 false: 下へ移動
     */
    private void orderDisplayChangeUnit(GoodsRegistUpdateModel unitPage, boolean upFlg) {
        // 画面から選択商品コードを取得する
        if (unitPage.getSelectDspNo() == null) {
            return;
        }
        int selectDspNo1 = unitPage.getSelectDspNo().intValue();

        // 該当する規格情報を取得する
        GoodsDto goodsDto1 = null;
        int count = 0;
        for (GoodsDto tmpGoodsDto : unitPage.getGoodsGroupDto().getGoodsDtoList()) {
            if (HTypeGoodsSaleStatus.DELETED == tmpGoodsDto.getGoodsEntity().getSaleStatusPC()) {
                // ステータス削除の場合は飛ばす
                continue;
            } else {
                count++;
            }
            if (selectDspNo1 == count) {
                goodsDto1 = tmpGoodsDto;
            }
        }

        // 移動先表示順規格情報を取得する
        int selectDspNo2 = 0;
        if (upFlg) {
            // 上へ移動の場合
            selectDspNo2 = selectDspNo1 - 1;
        } else {
            // 下へ移動の場合
            selectDspNo2 = selectDspNo1 + 1;
        }
        GoodsDto goodsDto2 = null;
        count = 0;
        for (GoodsDto tmpGoodsDto : unitPage.getGoodsGroupDto().getGoodsDtoList()) {
            if (HTypeGoodsSaleStatus.DELETED.equals(tmpGoodsDto.getGoodsEntity().getSaleStatusPC())) {
                // ステータス削除の場合は飛ばす
                continue;
            } else {
                count++;
            }
            if (selectDspNo2 == count) {
                goodsDto2 = tmpGoodsDto;
            }
        }
        if (goodsDto1 == null || goodsDto2 == null) {
            // 1件目規格を上へなどの場合にもここに来る
            return;
        }

        // 規格表示順を入れ替える
        Integer tmpOrderDisplay = goodsDto1.getGoodsEntity().getOrderDisplay();
        goodsDto1.getGoodsEntity().setOrderDisplay(goodsDto2.getGoodsEntity().getOrderDisplay());
        goodsDto2.getGoodsEntity().setOrderDisplay(tmpOrderDisplay);
        // リスト内の順番を入れ替える
        int index2 = unitPage.getGoodsGroupDto().getGoodsDtoList().indexOf(goodsDto2);
        unitPage.getGoodsGroupDto()
                .getGoodsDtoList()
                .set(unitPage.getGoodsGroupDto().getGoodsDtoList().indexOf(goodsDto1), goodsDto2);
        unitPage.getGoodsGroupDto().getGoodsDtoList().set(index2, goodsDto1);
    }

    /**
     * 商品規格情報のページ反映<br/>
     *
     * @param unitPage      ページ
     * @param toSamePageFlg 同一ページ内遷移フラグ
     */
    private void toPageForGoodsInfoList(GoodsRegistUpdateModel unitPage, boolean toSamePageFlg) {
        if (unitPage.getUnitItems() != null) {
            for (GoodsRegistUpdateUnitItem unitPageItem : unitPage.getUnitItems()) {
                GoodsDto goodsDto = null;
                // 該当する規格情報を取得する
                int count = 0;
                for (GoodsDto tmpGoodsDto : unitPage.getGoodsGroupDto().getGoodsDtoList()) {
                    if (HTypeGoodsSaleStatus.DELETED.equals(tmpGoodsDto.getGoodsEntity().getSaleStatusPC())) {
                        // ステータス削除の場合は飛ばす
                        continue;
                    } else {
                        count++;
                    }
                    if (unitPageItem.getUnitDspNo() != null && unitPageItem.getUnitDspNo() == count) {
                        goodsDto = tmpGoodsDto;
                    }
                }
                if (goodsDto == null) {
                    return;
                }
                boolean unitValueSetFlag = toSamePageFlg || HTypeUnitManagementFlag.ON.getValue()
                                                                                      .equals(unitPage.getUnitManagementFlag());
                setToGoodsDto(goodsDto, unitPageItem, unitValueSetFlag);
            }
        }
    }

    /**
     * ページ入力値を商品Dtoにセットする<br/>
     *
     * @param goodsDto         商品Dto
     * @param unitPageItem     規格Item
     * @param unitValueSetFlag 規格値セットフラグ
     */
    private void setToGoodsDto(GoodsDto goodsDto, GoodsRegistUpdateUnitItem unitPageItem, boolean unitValueSetFlag) {
        goodsDto.getGoodsEntity().setGoodsCode(unitPageItem.getGoodsCode());
        goodsDto.getGoodsEntity().setOrderDisplay(unitPageItem.getOrderDisplay());
        goodsDto.getGoodsEntity().setJanCode(unitPageItem.getJanCode());
        if (unitValueSetFlag) {
            goodsDto.getGoodsEntity().setUnitValue1(unitPageItem.getUnitValue1());
            goodsDto.getGoodsEntity().setUnitValue2(unitPageItem.getUnitValue2());
        } else {
            goodsDto.getGoodsEntity().setUnitValue1(null);
            goodsDto.getGoodsEntity().setUnitValue2(null);
        }
        goodsDto.getGoodsEntity()
                .setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                               unitPageItem.getGoodsSaleStatusPC()
                                                              ));
        // 販売開始日時
        goodsDto.getGoodsEntity()
                .setSaleStartTimePC(conversionUtility.toTimeStampWithDefaultHms(unitPageItem.getSaleStartDatePC(),
                                                                                unitPageItem.getSaleStartTimePC(),
                                                                                ConversionUtility.DEFAULT_START_TIME
                                                                               ));
        // 販売終了日時
        goodsDto.getGoodsEntity()
                .setSaleEndTimePC(conversionUtility.toTimeStampWithDefaultHms(unitPageItem.getSaleEndDatePC(),
                                                                              unitPageItem.getSaleEndTimePC(),
                                                                              ConversionUtility.DEFAULT_END_TIME
                                                                             ));
        goodsDto.getGoodsEntity().setPurchasedMax(conversionUtility.toBigDecimal(unitPageItem.getPurchasedMax()));
    }

    /***************************************************************************************************************************
     ** 商品画像設定
     ***************************************************************************************************************************/
    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForLoadImage(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        // 商品グループ画像登録更新用DTOリスト（ページ内編集用）の作成
        initTmpGoodsGroupImageRegistUpdateDtoList(goodsRegistUpdateModel);

        // 商品画像に関する設定
        setDetailsImageInfo(goodsRegistUpdateModel);
    }

    /**
     * 商品画像情報設定<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void setDetailsImageInfo(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        // 商品グループ詳細画像アイテムリスト作成
        createGroupDetailsImageItems(goodsRegistUpdateModel);

    }

    /**
     * 商品グループ詳細画像アイテム作成<br/>
     *
     * @param goodsRegistUpdateModel     ページ
     */
    private void createGroupDetailsImageItems(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        String maxCount = PropertiesUtil.getSystemPropertiesValue("goodsimage.max.count");

        // 設定ファイルに指定数分ループ
        List<GoodsRegistUpdateImageItem> items = new ArrayList<>();
        for (Integer i = 1; i <= Integer.parseInt(maxCount); i++) {
            GoodsRegistUpdateImageItem item = ApplicationContextUtility.getBean(GoodsRegistUpdateImageItem.class);

            item.setImageNo(i);

            // 詳細画像をセット
            item.setImagePath(getGoodsImageFilepath(goodsRegistUpdateModel, i));
            items.add(item);
        }
        goodsRegistUpdateModel.setGoodsImageItems(items);
    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForNextImage(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        // 商品画像登録更新用DTOリスト（ページ内編集用）をセッションにセット
        setTmpGoodsGroupImageRegistUpdateDtoList(goodsRegistUpdateModel);
    }

    /***************************************************************************************************************************
     ** 在庫設定
     ***************************************************************************************************************************/
    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForLoadStock(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループエンティティ
        GoodsGroupEntity goodsGroupEntity = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity();
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList();
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = goodsRegistUpdateModel.getCommonGoodsEntity();

        // 商品名
        goodsRegistUpdateModel.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
        // 公開状態PC
        if (goodsGroupEntity.getGoodsOpenStatusPC() != null) {
            goodsRegistUpdateModel.setGoodsOpenStatusPC(goodsGroupEntity.getGoodsOpenStatusPC().getValue());
        }
        // 在庫管理フラグ
        if (commonGoodsEntity.getStockManagementFlag() != null) {
            goodsRegistUpdateModel.setStockManagementFlag(commonGoodsEntity.getStockManagementFlag().getValue());
        }

        // 商品規格情報リストのセット
        int index = 0;
        List<GoodsRegistUpdateStockItem> stockItemList = new ArrayList<>();
        if (goodsDtoList != null && goodsDtoList.size() > 0) {
            for (GoodsDto goodsDto : goodsDtoList) {
                if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()) {
                    // ステータス削除の場合は飛ばす
                    continue;
                }
                GoodsRegistUpdateStockItem stockItem =
                                ApplicationContextUtility.getBean(GoodsRegistUpdateStockItem.class);
                stockItem.setStockDspNo(++index);
                stockItem.setGoodsSeq(goodsDto.getGoodsEntity().getGoodsSeq());
                stockItem.setGoodsCode(goodsDto.getGoodsEntity().getGoodsCode());
                stockItem.setJanCode(goodsDto.getGoodsEntity().getJanCode());
                stockItem.setUnitValue1(goodsDto.getGoodsEntity().getUnitValue1());
                stockItem.setUnitValue2(goodsDto.getGoodsEntity().getUnitValue2());
                if (goodsDto.getGoodsEntity().getSaleStatusPC() != null) {
                    stockItem.setGoodsSaleStatusPC(goodsDto.getGoodsEntity().getSaleStatusPC().getValue());
                }
                if (goodsDto.getStockDto() != null) {

                    // 変換Helper取得
                    ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

                    stockItem.setRemainderFewStock(
                                    conversionUtility.toString(goodsDto.getStockDto().getRemainderFewStock()));
                    stockItem.setOrderPointStock(
                                    conversionUtility.toString(goodsDto.getStockDto().getOrderPointStock()));
                    stockItem.setSafetyStock(conversionUtility.toString(goodsDto.getStockDto().getSafetyStock()));
                    stockItem.setRealStock(goodsDto.getStockDto().getRealStock());
                }
                stockItemList.add(stockItem);
            }
            goodsRegistUpdateModel.setStockItems(stockItemList);
        }
        goodsRegistUpdateModel.setUnitCount(stockItemList.size());

    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void toPageForNextStock(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 共通商品エンティティ
        GoodsEntity commonGoodsEntity = goodsRegistUpdateModel.getCommonGoodsEntity();

        // 入力項目をセット
        // 在庫管理フラグ
        commonGoodsEntity.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                               goodsRegistUpdateModel.getStockManagementFlag()
                                                                              ));

        // 商品在庫情報をページに反映
        toPageForStockInfoList(goodsRegistUpdateModel);
    }

    /**
     * 商品在庫情報のページ反映<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void toPageForStockInfoList(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        if (goodsRegistUpdateModel.getStockItems() != null) {
            for (GoodsRegistUpdateStockItem stockPageItem : goodsRegistUpdateModel.getStockItems()) {
                GoodsDto goodsDto = null;
                // 該当する規格情報を取得する
                int count = 0;
                for (GoodsDto tmpGoodsDto : goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList()) {
                    if (HTypeGoodsSaleStatus.DELETED == tmpGoodsDto.getGoodsEntity().getSaleStatusPC()) {
                        // ステータス削除の場合は飛ばす
                        continue;
                    } else {
                        count++;
                    }
                    if (stockPageItem.getStockDspNo() == count) {
                        goodsDto = tmpGoodsDto;
                    }
                }
                if (goodsDto == null) {
                    return;
                }

                if (HTypeStockManagementFlag.ON == EnumTypeUtil.getEnumFromValue(
                                HTypeStockManagementFlag.class, goodsRegistUpdateModel.getStockManagementFlag())) {

                    // 変換Helper取得
                    ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

                    if (!StringUtils.isEmpty(stockPageItem.getSupplementCount())) {
                        goodsDto.getStockDto()
                                .setSupplementCount(conversionUtility.toBigDecimal(stockPageItem.getSupplementCount()));
                    }
                    goodsDto.getStockDto()
                            .setRemainderFewStock(conversionUtility.toBigDecimal(stockPageItem.getRemainderFewStock()));
                    goodsDto.getStockDto()
                            .setOrderPointStock(conversionUtility.toBigDecimal(stockPageItem.getOrderPointStock()));
                    goodsDto.getStockDto()
                            .setSafetyStock(conversionUtility.toBigDecimal(stockPageItem.getSafetyStock()));
                } else {
                    // 在庫管理なしの時は、残少表示在庫数・発注点在庫数・安全在庫数に0をセット
                    goodsDto.getStockDto().setSupplementCount(new BigDecimal(0));
                    goodsDto.getStockDto().setRemainderFewStock(new BigDecimal(0));
                    goodsDto.getStockDto().setOrderPointStock(new BigDecimal(0));
                    goodsDto.getStockDto().setSafetyStock(new BigDecimal(0));
                }
            }
        }
    }

    /***************************************************************************************************************************
     ** 関連商品設定
     ***************************************************************************************************************************/
    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsRegistUpdateModel ページ
     */
    public void toPageForLoadRelation(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        if (goodsRegistUpdateModel.getTmpGoodsRelationEntityList() == null
            && goodsRegistUpdateModel.getRedirectGoodsRelationEntityList() != null) {
            goodsRegistUpdateModel.setTmpGoodsRelationEntityList(
                            goodsRegistUpdateModel.getRedirectGoodsRelationEntityList());
        }

        List<GoodsRelationEntity> tmpGoodsRelationEntityList;
        if (goodsRegistUpdateModel.getTmpGoodsRelationEntityList() == null
            && goodsRegistUpdateModel.getGoodsRelationEntityList() != null) {
            // 関連商品エンティティリストをセッションからページへディープコピーする
            tmpGoodsRelationEntityList = new ArrayList<>(goodsRegistUpdateModel.getGoodsRelationEntityList());
            goodsRegistUpdateModel.setTmpGoodsRelationEntityList(tmpGoodsRelationEntityList);
        } else {
            // 同一ページ内遷移時はページ情報を取得
            tmpGoodsRelationEntityList = goodsRegistUpdateModel.getTmpGoodsRelationEntityList();
        }
        if (tmpGoodsRelationEntityList == null) {
            tmpGoodsRelationEntityList = new ArrayList<>();
            goodsRegistUpdateModel.setTmpGoodsRelationEntityList(new ArrayList<>());
        }

        // 関連商品情報リストのセット
        int index = 0;
        List<GoodsRegistUpdateRelationItem> relationItems = new ArrayList<>();
        for (GoodsRelationEntity goodsRelationEntity : tmpGoodsRelationEntityList) {
            GoodsRegistUpdateRelationItem relationItem =
                            ApplicationContextUtility.getBean(GoodsRegistUpdateRelationItem.class);
            relationItem.setRelationDspNo(++index);
            relationItem.setRelationGoodsGroupCode(goodsRelationEntity.getGoodsGroupCode());
            relationItem.setRelationGoodsGroupName(goodsRelationEntity.getGoodsGroupName());
            relationItems.add(relationItem);
        }
        goodsRegistUpdateModel.setRelationItems(relationItems);

        // ダミー用関連商品（空）を作成する
        List<GoodsRegistUpdateRelationItem> dummyRelationItems = new ArrayList<>();
        for (int i = 0; i < goodsRegistUpdateModel.getGoodsRelationAmount() - relationItems.size(); i++) {
            GoodsRegistUpdateRelationItem relationItem =
                            ApplicationContextUtility.getBean(GoodsRegistUpdateRelationItem.class);
            relationItem.setRelationDspNo(0);
            relationItem.setRelationGoodsGroupCode("");
            relationItem.setRelationGoodsGroupName("");
            dummyRelationItems.add(relationItem);
        }
        goodsRegistUpdateModel.setRelationNoItems(dummyRelationItems);
    }

    /**
     * 入力情報をページに反映<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    private void toPageForNextRelation(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        if (goodsRegistUpdateModel.getTmpGoodsRelationEntityList() != null) {
            // tmp関連商品エンティティリストをセッションにセット
            goodsRegistUpdateModel.setGoodsRelationEntityList(goodsRegistUpdateModel.getTmpGoodsRelationEntityList());
        }
    }

    /**
     * 関連商品情報削除<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    public void doDeleteRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 画面から選択商品コードを取得する
        String goodsGroupCode = goodsRegistUpdateModel.getSelectRelationGoodsGroupCode();

        if (goodsGroupCode == null || goodsRegistUpdateModel.getTmpGoodsRelationEntityList() == null) {
            return;
        }

        // 該当する関連商品情報を取得する
        GoodsRelationEntity goodsRelationEntity = null;
        for (GoodsRelationEntity tmpGoodsRelationEntity : goodsRegistUpdateModel.getTmpGoodsRelationEntityList()) {
            if (goodsGroupCode.equals(tmpGoodsRelationEntity.getGoodsGroupCode())) {
                goodsRelationEntity = tmpGoodsRelationEntity;
                break;
            }
        }
        if (goodsRelationEntity == null) {
            return;
        }

        // tmp関連商品リストから削除する
        goodsRegistUpdateModel.getTmpGoodsRelationEntityList().remove(goodsRelationEntity);

        // 表示順を再設定する
        int orderIndex = 0;
        for (GoodsRelationEntity tmpGoodsRelationEntity : goodsRegistUpdateModel.getTmpGoodsRelationEntityList()) {
            tmpGoodsRelationEntity.setOrderDisplay(++orderIndex);
        }
    }

    /**
     * 関連商品表示順変更(上)<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    public void toPageForUpRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        orderDisplayChangeRelation(goodsRegistUpdateModel, true);
    }

    /**
     * 関連商品表示順変更(下)<br/>
     *
     * @param goodsRegistUpdateModel ページ
     */
    public void toPageForDownRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        orderDisplayChangeRelation(goodsRegistUpdateModel, false);
    }

    /**
     * 商品表示順変更<br/>
     *
     * @param goodsRegistUpdateModel ページ
     * @param upFlg                  true: 上へ移動 false: 下へ移動
     */
    private void orderDisplayChangeRelation(GoodsRegistUpdateModel goodsRegistUpdateModel, boolean upFlg) {
        // 画面から選択商品コードを取得する
        if (goodsRegistUpdateModel.getSelectRelationGoodsGroupCode() == null) {
            return;
        }
        String goodsGroupCode = goodsRegistUpdateModel.getSelectRelationGoodsGroupCode();

        // 該当する関連商品情報を取得する
        GoodsRelationEntity goodsRelationEntity1 = null;
        for (GoodsRelationEntity tmpGoodsRelationEntity : goodsRegistUpdateModel.getTmpGoodsRelationEntityList()) {
            if (goodsGroupCode.equals(tmpGoodsRelationEntity.getGoodsGroupCode())) {
                goodsRelationEntity1 = tmpGoodsRelationEntity;
                break;
            }
        }

        // 移動先表示順商品情報を取得する
        GoodsRelationEntity goodsRelationEntity2 = null;
        if (upFlg && goodsRegistUpdateModel.getTmpGoodsRelationEntityList().indexOf(goodsRelationEntity1) > 0) {
            // 上へ移動の場合
            int targetIndex = goodsRegistUpdateModel.getTmpGoodsRelationEntityList().indexOf(goodsRelationEntity1) - 1;
            goodsRelationEntity2 = goodsRegistUpdateModel.getTmpGoodsRelationEntityList().get(targetIndex);
        } else if (!upFlg && goodsRegistUpdateModel.getTmpGoodsRelationEntityList().indexOf(goodsRelationEntity1)
                             < goodsRegistUpdateModel.getTmpGoodsRelationEntityList().size() - 1) {
            // 下へ移動の場合
            int targetIndex = goodsRegistUpdateModel.getTmpGoodsRelationEntityList().indexOf(goodsRelationEntity1) + 1;
            goodsRelationEntity2 = goodsRegistUpdateModel.getTmpGoodsRelationEntityList().get(targetIndex);
        }
        if (goodsRelationEntity1 == null || goodsRelationEntity2 == null) {
            // 1件目を上へなどの場合にもここへくる
            return;
        }

        // 表示順を入れ替える
        Integer tmpOrderDisplay = goodsRelationEntity1.getOrderDisplay();
        goodsRelationEntity1.setOrderDisplay(goodsRelationEntity2.getOrderDisplay());
        goodsRelationEntity2.setOrderDisplay(tmpOrderDisplay);
        // リスト内の順番を入れ替える
        int index2 = goodsRegistUpdateModel.getTmpGoodsRelationEntityList().indexOf(goodsRelationEntity2);
        goodsRegistUpdateModel.getTmpGoodsRelationEntityList()
                              .set(goodsRegistUpdateModel.getTmpGoodsRelationEntityList()
                                                         .indexOf(goodsRelationEntity1), goodsRelationEntity2);
        goodsRegistUpdateModel.getTmpGoodsRelationEntityList().set(index2, goodsRelationEntity1);
    }

    /**
     * 商品グループ詳細画像アイテムリストの最後の画像のインデックスを取得
     *
     * @param imageItems
     * @return 最後の画像のインデックス
     */
    public int getLastImageIndex(List<GoodsRegistUpdateImageItem> imageItems) {
        int index = 1;
        for (GoodsRegistUpdateImageItem item : imageItems) {
            if (!item.isExistImage()) {
                return index;
            }
            index++;
        }
        return index;
    }

    /**
     * カテゴリエンティティに変換
     *
     * @param categoryGoodsResponses      カテゴリー商品レスポンスクラスリスト
     * @param shopSeq           ショップSEQ
     * @return カテゴリエンティティリスト
     */
    public List<CategoryEntity> toCategoryEntityFromResponse(List<CategoryGoodsResponse> categoryGoodsResponses,
                                                             Integer shopSeq) {

        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        categoryGoodsResponses.forEach(item -> {
            CategoryEntity categoryEntity = new CategoryEntity();

            categoryEntity.setCategorySeq(item.getCategorySeq());
            categoryEntity.setShopSeq(shopSeq);
            categoryEntity.setCategoryId(item.getCategoryId());
            categoryEntity.setCategoryName(item.getCategoryName());
            categoryEntity.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, item.getCategoryType()));
            categoryEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            categoryEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
            categoryEntityList.add(categoryEntity);
        });
        return categoryEntityList;
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
        categoryList.forEach(item -> {
            CategoryEntity categoryEntity = new CategoryEntity();

            categoryEntity.setCategorySeq(item.getCategorySeq());
            categoryEntity.setShopSeq(shopSeq);
            categoryEntity.setCategoryId(item.getCategoryId());
            categoryEntity.setCategoryName(item.getCategoryName());
            categoryEntity.setCategoryOpenStatusPC(
                            EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, item.getCategoryOpenStatus()));
            categoryEntity.setCategoryOpenStartTimePC(conversionUtility.toTimestamp(item.getCategoryOpenStartTime()));
            categoryEntity.setCategoryOpenEndTimePC(conversionUtility.toTimestamp(item.getCategoryOpenEndTime()));
            categoryEntity.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, item.getCategoryType()));
            categoryEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            categoryEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
            if (categorySelectedList == null || categorySelectedList.size() == 0) {
                categoryEntity.setRegistCategoryCheck(false);
            } else {
                categoryEntity.setRegistCategoryCheck(categorySelectedList.stream()
                                                                          .anyMatch(el -> el.getCategorySeq()
                                                                                            .equals(item.getCategorySeq())));
            }

            categoryEntityList.add(categoryEntity);
        });
        return categoryEntityList;
    }

    /**
     * カテゴリエンティティに変換
     *
     * @param categoryGoodsEntityList      カテゴリ登録商品クラス
     * @param shopSeq           ショップSEQ
     * @return カテゴリエンティティリスト
     */
    public List<CategoryEntity> toCategoryEntity(List<CategoryGoodsEntity> categoryGoodsEntityList, Integer shopSeq) {

        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        categoryGoodsEntityList.forEach(item -> {
            CategoryEntity categoryEntity = new CategoryEntity();

            categoryEntity.setCategorySeq(item.getCategorySeq());
            categoryEntity.setCategoryName(item.getCategoryName());
            categoryEntity.setCategoryType(item.getCategoryType());
            categoryEntity.setShopSeq(shopSeq);
            categoryEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            categoryEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
            categoryEntityList.add(categoryEntity);
        });
        return categoryEntityList;
    }

    /**
     * 登録カテゴリエンティティに変換
     *
     * @param categoryEntityNormalLinked      通常のカテゴリー登録商品
     * @param categoryAutoLinked              自動カテゴリー登録商品
     * @return カテゴリエンティティリスト
     */
    public List<CategoryEntity> toCategoryRegistList(List<CategoryEntity> categoryEntityNormalLinked,
                                                     List<CategoryEntity> categoryAutoLinked) {
        List<CategoryEntity> categoryEntityList = new ArrayList<>();

        // カテゴリ手動リスト取得
        if (CollectionUtil.isNotEmpty(categoryEntityNormalLinked)) {
            categoryEntityNormalLinked.removeIf(item -> item.getCategoryType() == HTypeCategoryType.AUTO);
            categoryEntityList.addAll(categoryEntityNormalLinked);
        }
        // カテゴリ自動リスト取得
        if (CollectionUtil.isNotEmpty(categoryAutoLinked)) {
            categoryAutoLinked.removeIf(item -> item.getCategoryType() == HTypeCategoryType.NORMAL);
            categoryEntityList.addAll(categoryAutoLinked);
        }
        return categoryEntityList;
    }

    /**
     * 商品グループ一覧レスポンスに変換
     *
     * @param goodsGroupDto 商品グループDtoクラス
     * @return 商品グループ一覧レスポンス
     */
    public GoodsGroupRegistUpdateRequest toGoodsGroupRegistUpdateRequest(GoodsGroupDto goodsGroupDto) {
        GoodsGroupRegistUpdateRequest request = new GoodsGroupRegistUpdateRequest();

        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();

        request.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
        request.setGoodsGroupCode(goodsGroupEntity.getGoodsGroupCode());
        request.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
        request.setGoodsPrice(goodsGroupEntity.getGoodsPrice());
        request.setWhatsnewDate(goodsGroupEntity.getWhatsnewDate());
        request.setGoodsOpenStatus(EnumTypeUtil.getValue(goodsGroupEntity.getGoodsOpenStatusPC()));
        request.setOpenStartTime(goodsGroupEntity.getOpenStartTimePC());
        request.setOpenEndTime(goodsGroupEntity.getOpenEndTimePC());
        request.setGoodsTaxType(EnumTypeUtil.getValue(goodsGroupEntity.getGoodsTaxType()));
        request.setAlcoholFlag(EnumTypeUtil.getValue(goodsGroupEntity.getAlcoholFlag()));
        request.setNoveltyGoodsType(EnumTypeUtil.getValue(goodsGroupEntity.getNoveltyGoodsType()));
        request.setSnsLinkFlag(EnumTypeUtil.getValue(goodsGroupEntity.getSnsLinkFlag()));
        request.setVersionNo(goodsGroupEntity.getVersionNo());
        request.setRegistTime(goodsGroupEntity.getRegistTime());
        request.setUpdateTime(goodsGroupEntity.getUpdateTime());

        request.setGoodsRequestList(toGoodsRequestList(goodsGroupDto.getGoodsDtoList()));

        if (ObjectUtils.isNotEmpty(request.getBatchUpdateStockStatus())) {
            request.setBatchUpdateStockStatus(toStockStatusDisplayRequest(goodsGroupDto.getBatchUpdateStockStatus()));
        } else {
            request.setBatchUpdateStockStatus(null);
        }

        if (ObjectUtils.isNotEmpty(request.getBatchUpdateStockStatus())) {
            request.setRealTimeStockStatus(toStockStatusDisplayRequest(goodsGroupDto.getRealTimeStockStatus()));
        } else {
            request.setRealTimeStockStatus(null);
        }

        request.setGoodsGroupDisplay(toGoodsGroupDisplayRequest(goodsGroupDto.getGoodsGroupDisplayEntity()));
        request.setGoodsGroupImageRequestList(
                        toGoodsGroupImageRequestList(goodsGroupDto.getGoodsGroupImageEntityList()));
        request.setCategoryGoodsRequestList(toCategoryGoodsRequestList(goodsGroupDto.getCategoryGoodsEntityList()));
        request.setGoodsInformationIconDetailsRequestList(toGoodsInformationIconDetailsRequestList(
                        goodsGroupDto.getGoodsInformationIconDetailsDtoList()));
        request.setTaxRate(goodsGroupEntity.getTaxRate());

        return request;
    }

    /**
     * 商品レスポンスクラスリストに変換
     *
     * @param goodsDtoList 商品DTOリスト
     * @return 商品レスポンスクラスリスト
     */
    public List<GoodsRequest> toGoodsRequestList(List<GoodsDto> goodsDtoList) {
        List<GoodsRequest> goodsRequestList = new ArrayList<>();

        goodsDtoList.forEach(item -> {
            GoodsRequest goodsRequest = new GoodsRequest();

            goodsRequest.setGoodsSubRequest(toGoodsSubRequest(item.getGoodsEntity()));
            goodsRequest.setDeleteFlg(item.getDeleteFlg());
            goodsRequest.setStockRequest(toStockRequest(item.getStockDto()));
            goodsRequest.setStockStatus(EnumTypeUtil.getValue(item.getStockStatusPc()));

            goodsRequestList.add(goodsRequest);
        });

        return goodsRequestList;
    }

    /**
     * 商品クラスリクエストに変換
     *
     * @param goodsEntity 商品エンティティ
     * @return 商品クラスリクエスト
     */
    public GoodsSubRequest toGoodsSubRequest(GoodsEntity goodsEntity) {
        GoodsSubRequest goodsSubRequest = new GoodsSubRequest();

        goodsSubRequest.setGoodsCode(goodsEntity.getGoodsCode());
        goodsSubRequest.setGoodsSeq(goodsEntity.getGoodsSeq());
        goodsSubRequest.setGoodsGroupSeq(goodsEntity.getGoodsGroupSeq());
        goodsSubRequest.setGoodsCode(goodsEntity.getGoodsCode());
        goodsSubRequest.setJanCode(goodsEntity.getJanCode());
        goodsSubRequest.setSaleStatus(EnumTypeUtil.getValue(goodsEntity.getSaleStatusPC()));
        goodsSubRequest.setSaleStartTime(goodsEntity.getSaleStartTimePC());
        goodsSubRequest.setSaleEndTime(goodsEntity.getSaleEndTimePC());
        goodsSubRequest.setIndividualDeliveryType(EnumTypeUtil.getValue(goodsEntity.getIndividualDeliveryType()));
        goodsSubRequest.setFreeDeliveryFlag(EnumTypeUtil.getValue(goodsEntity.getFreeDeliveryFlag()));
        goodsSubRequest.setUnitManagementFlag(EnumTypeUtil.getValue(goodsEntity.getUnitManagementFlag()));
        goodsSubRequest.setUnitValue1(goodsEntity.getUnitValue1());
        goodsSubRequest.setUnitValue2(goodsEntity.getUnitValue2());
        goodsSubRequest.setStockManagementFlag(EnumTypeUtil.getValue(goodsEntity.getStockManagementFlag()));
        goodsSubRequest.setPurchasedMax(goodsEntity.getPurchasedMax());
        goodsSubRequest.setOrderDisplay(goodsEntity.getOrderDisplay());
        goodsSubRequest.setVersionNo(goodsEntity.getVersionNo());
        goodsSubRequest.setRegistTime(goodsEntity.getRegistTime());
        goodsSubRequest.setUpdateTime(goodsEntity.getUpdateTime());

        return goodsSubRequest;
    }

    /**
     * 商品グループ検索条件リクエストに変換
     *
     * @param stockDto 在庫Dtoクラス
     * @return 商品グループ検索条件リクエスト
     */
    public StockRequest toStockRequest(StockDto stockDto) {
        StockRequest stockRequest = new StockRequest();

        stockRequest.setGoodsSeq(stockDto.getGoodsSeq());
        stockRequest.setSalesPossibleStock(stockDto.getSalesPossibleStock());
        stockRequest.setRealStock(stockDto.getRealStock());
        stockRequest.setOrderReserveStock(stockDto.getOrderReserveStock());
        stockRequest.setRemainderFewStock(stockDto.getRemainderFewStock());
        stockRequest.setSupplementCount(stockDto.getSupplementCount());
        stockRequest.setOrderPointStock(stockDto.getOrderPointStock());
        stockRequest.setSafetyStock(stockDto.getSafetyStock());
        stockRequest.setRegistTime(stockDto.getRegistTime());
        stockRequest.setUpdateTime(stockDto.getUpdateTime());

        return stockRequest;
    }

    /**
     * 商品グループ在庫表示クラスリクエストに変換
     *
     * @param entity       商品グループ在庫表示クラス
     * @return 商品グループ在庫表示クラスリクエスト
     */
    public StockStatusDisplayRequest toStockStatusDisplayRequest(StockStatusDisplayEntity entity) {
        StockStatusDisplayRequest stockStatusDisplayRequest = new StockStatusDisplayRequest();

        stockStatusDisplayRequest.setGoodsGroupSeq(entity.getGoodsGroupSeq());
        stockStatusDisplayRequest.setStockStatus(EnumTypeUtil.getValue(entity.getStockStatusPc()));
        stockStatusDisplayRequest.setRegistTime(entity.getRegistTime());
        stockStatusDisplayRequest.setUpdateTime(entity.getUpdateTime());

        return stockStatusDisplayRequest;
    }

    /**
     * 商品グループ検索条件リクエストに変換
     *
     * @param entity 商品グループ表示クラス
     * @return 商品グループ検索条件リクエスト
     */
    public GoodsGroupDisplayRequest toGoodsGroupDisplayRequest(GoodsGroupDisplayEntity entity) {
        GoodsGroupDisplayRequest goodsGroupDisplayRequest = new GoodsGroupDisplayRequest();

        goodsGroupDisplayRequest.setGoodsGroupSeq(entity.getGoodsGroupSeq());
        goodsGroupDisplayRequest.setInformationIcon(entity.getInformationIconPC());
        goodsGroupDisplayRequest.setSearchKeyword(entity.getSearchKeyword());
        goodsGroupDisplayRequest.setSearchKeywordEm(entity.getSearchKeywordEm());
        goodsGroupDisplayRequest.setUnitManagementFlag(EnumTypeUtil.getValue(entity.getUnitManagementFlag()));
        goodsGroupDisplayRequest.setUnitTitle1(entity.getUnitTitle1());
        goodsGroupDisplayRequest.setUnitTitle2(entity.getUnitTitle2());
        goodsGroupDisplayRequest.setMetaDescription(entity.getMetaDescription());
        goodsGroupDisplayRequest.setMetaKeyword(entity.getMetaKeyword());
        goodsGroupDisplayRequest.setGoodsTag(entity.getGoodsTagList());
        goodsGroupDisplayRequest.setDeliveryType(entity.getDeliveryType());
        goodsGroupDisplayRequest.setGoodsNote1(entity.getGoodsNote1());
        goodsGroupDisplayRequest.setGoodsNote2(entity.getGoodsNote2());
        goodsGroupDisplayRequest.setGoodsNote3(entity.getGoodsNote3());
        goodsGroupDisplayRequest.setGoodsNote4(entity.getGoodsNote4());
        goodsGroupDisplayRequest.setGoodsNote5(entity.getGoodsNote5());
        goodsGroupDisplayRequest.setGoodsNote6(entity.getGoodsNote6());
        goodsGroupDisplayRequest.setGoodsNote7(entity.getGoodsNote7());
        goodsGroupDisplayRequest.setGoodsNote8(entity.getGoodsNote8());
        goodsGroupDisplayRequest.setGoodsNote9(entity.getGoodsNote9());
        goodsGroupDisplayRequest.setGoodsNote10(entity.getGoodsNote10());
        goodsGroupDisplayRequest.setOrderSetting1(entity.getOrderSetting1());
        goodsGroupDisplayRequest.setOrderSetting2(entity.getOrderSetting2());
        goodsGroupDisplayRequest.setOrderSetting3(entity.getOrderSetting3());
        goodsGroupDisplayRequest.setOrderSetting4(entity.getOrderSetting4());
        goodsGroupDisplayRequest.setOrderSetting5(entity.getOrderSetting5());
        goodsGroupDisplayRequest.setOrderSetting6(entity.getOrderSetting6());
        goodsGroupDisplayRequest.setOrderSetting7(entity.getOrderSetting7());
        goodsGroupDisplayRequest.setOrderSetting8(entity.getOrderSetting8());
        goodsGroupDisplayRequest.setOrderSetting9(entity.getOrderSetting9());
        goodsGroupDisplayRequest.setOrderSetting10(entity.getOrderSetting10());
        goodsGroupDisplayRequest.setRegistTime(entity.getRegistTime());
        goodsGroupDisplayRequest.setUpdateTime(entity.getUpdateTime());
        goodsGroupDisplayRequest.setGoodsTag(entity.getGoodsTagList());

        return goodsGroupDisplayRequest;
    }

    /**
     * 関連商品クラスリストに変換
     *
     * @param entityList エンティティリスト
     * @return 関連商品クラスリスト
     */
    public List<GoodsGroupImageRegistUpdateRequest> toGoodsGroupImageRegistUpdateRequest(List<GoodsGroupImageRegistUpdateDto> entityList) {
        List<GoodsGroupImageRegistUpdateRequest> goodsGroupImageRegistUpdateRequestList = new ArrayList<>();

        entityList.forEach(item -> {
            GoodsGroupImageRegistUpdateRequest goodsGroupImageRegistUpdateRequest =
                            new GoodsGroupImageRegistUpdateRequest();

            goodsGroupImageRegistUpdateRequest.setGoodsGroupImageEntity(
                            toGoodsGroupImageRequest(item.getGoodsGroupImageEntity()));
            goodsGroupImageRegistUpdateRequest.setImageFileName(item.getImageFileName());
            goodsGroupImageRegistUpdateRequest.setTmpImageFileName(item.getTmpImageFileName());
            goodsGroupImageRegistUpdateRequest.setDeleteFlg(item.getDeleteFlg());
            goodsGroupImageRegistUpdateRequest.setTmpImageFilePath(item.getTmpImageFilePath());

            goodsGroupImageRegistUpdateRequestList.add(goodsGroupImageRegistUpdateRequest);
        });

        return goodsGroupImageRegistUpdateRequestList;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param entityList 商品グループ画像エンティティリスト
     * @return 商品グループ画像クラスリスト
     */
    public List<GoodsGroupImageRequest> toGoodsGroupImageRequestList(List<GoodsGroupImageEntity> entityList) {
        List<GoodsGroupImageRequest> goodsGroupImageRequestList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(entityList)) {
            entityList.forEach(item -> {
                GoodsGroupImageRequest goodsGroupImageRequest = new GoodsGroupImageRequest();

                goodsGroupImageRequest.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageRequest.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageRequest.setImageFileName(item.getImageFileName());
                goodsGroupImageRequest.setRegistTime(item.getRegistTime());
                goodsGroupImageRequest.setUpdateTime(item.getUpdateTime());

                goodsGroupImageRequestList.add(goodsGroupImageRequest);
            });
        }

        return goodsGroupImageRequestList;
    }

    /**
     * 商品レスポンスクラスリストに変換
     *
     * @param entityList 商品レスポンスクラスリスト
     * @return 商品レスポンスクラスリスト
     */
    public List<CategoryGoodsRequest> toCategoryGoodsRequestList(List<CategoryGoodsEntity> entityList) {
        List<CategoryGoodsRequest> categoryGoodsRequestList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(entityList)) {
            for (CategoryGoodsEntity entity : entityList) {
                CategoryGoodsRequest categoryGoodsRequest = new CategoryGoodsRequest();
                categoryGoodsRequest.setCategorySeq(entity.getCategorySeq());
                categoryGoodsRequest.setGoodsGroupSeq(entity.getGoodsGroupSeq());
                categoryGoodsRequest.setManualOrderDisplay(entity.getOrderDisplay());
                categoryGoodsRequest.setRegistTime(entity.getRegistTime());
                categoryGoodsRequest.setUpdateTime(entity.getUpdateTime());
                categoryGoodsRequestList.add(categoryGoodsRequest);
            }
        }

        return categoryGoodsRequestList;
    }

    /**
     * アイコン詳細リクエストクラスリストに変換
     *
     * @param entityList エンティティリスト
     * @return アイコン詳細リクエストクラスリスト
     */
    public List<GoodsInformationIconDetailsRequest> toGoodsInformationIconDetailsRequestList(List<GoodsInformationIconDetailsDto> entityList) {
        List<GoodsInformationIconDetailsRequest> goodsInformationIconDetailsRequestList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(entityList)) {
            entityList.forEach(item -> {
                GoodsInformationIconDetailsRequest goodsInformationIconDetailsRequest =
                                new GoodsInformationIconDetailsRequest();

                goodsInformationIconDetailsRequest.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsInformationIconDetailsRequest.setIconSeq(item.getIconSeq());
                goodsInformationIconDetailsRequest.setIconName(item.getIconName());
                goodsInformationIconDetailsRequest.setColorCode(item.getColorCode());
                goodsInformationIconDetailsRequest.setOrderDisplay(item.getOrderDisplay());

                goodsInformationIconDetailsRequestList.add(goodsInformationIconDetailsRequest);
            });
        }

        return goodsInformationIconDetailsRequestList;
    }

    /**
     * 関連商品クラスリストに変換
     *
     * @param entityList エンティティリスト
     * @return 関連商品クラスリスト
     */
    public List<GoodsRelationRequest> toGoodsRelationRequest(List<GoodsRelationEntity> entityList) {
        List<GoodsRelationRequest> goodsRelationRequestList = new ArrayList<>();

        if (CollectionUtil.isEmpty(entityList)) {
            return goodsRelationRequestList;
        }

        entityList.forEach(item -> {
            GoodsRelationRequest goodsRelationRequest = new GoodsRelationRequest();

            goodsRelationRequest.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsRelationRequest.setGoodsRelationGroupSeq(item.getGoodsRelationGroupSeq());
            goodsRelationRequest.setOrderDisplay(item.getOrderDisplay());
            goodsRelationRequest.setRegistTime(item.getRegistTime());
            goodsRelationRequest.setUpdateTime(item.getUpdateTime());
            goodsRelationRequest.setGoodsGroupCode(item.getGoodsGroupCode());
            goodsRelationRequest.setGoodsGroupName(item.getGoodsGroupName());
            goodsRelationRequest.setGoodsOpenStatus(EnumTypeUtil.getValue(item.getGoodsOpenStatusPC()));

            goodsRelationRequestList.add(goodsRelationRequest);
        });

        return goodsRelationRequestList;
    }

    /**
     * 店舗内の全アイコン詳細Dtoに変換
     *
     * @param iconResponseList   アイコン一覧レスポンス
     * @param shopSeq            ショップSEQ
     * @return 店舗内の全アイコン詳細Dto
     */
    public List<GoodsInformationIconDto> toGoodsInformationIconDtoList(List<IconResponse> iconResponseList,
                                                                       Integer shopSeq) {
        List<GoodsInformationIconDto> goodsInformationIconDtoList = new ArrayList<>();

        iconResponseList.forEach(item -> {
            GoodsInformationIconDto goodsInformationIconDto = new GoodsInformationIconDto();

            GoodsInformationIconEntity goodsInformationIconEntity = new GoodsInformationIconEntity();
            goodsInformationIconEntity.setIconSeq(item.getIconSeq());
            goodsInformationIconEntity.setShopSeq(shopSeq);
            goodsInformationIconEntity.setIconName(item.getIconName());
            goodsInformationIconEntity.setColorCode(item.getColorCode());
            goodsInformationIconEntity.setOrderDisplay(item.getOrderDisplay());
            goodsInformationIconEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsInformationIconEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsInformationIconDto.setGoodsInformationIconEntity(goodsInformationIconEntity);
            goodsInformationIconDtoList.add(goodsInformationIconDto);
        });

        return goodsInformationIconDtoList;
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
        goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupImageRequest.getRegistTime()));
        goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupImageRequest.getUpdateTime()));

        return goodsGroupImageEntity;
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
        goodsGroupImageRequest.setRegistTime(conversionUtility.toTimestamp(goodsGroupImageEntity.getRegistTime()));
        goodsGroupImageRequest.setUpdateTime(conversionUtility.toTimestamp(goodsGroupImageEntity.getUpdateTime()));

        return goodsGroupImageRequest;
    }

    /**
     * 商品グループ表示クラスに変換
     *
     * @param goodsGroupDisplayResponse 商品グループ検索条件
     * @return 商品グループ表示クラス
     */
    public GoodsGroupDisplayEntity toGoodsGroupDisplayEntity(GoodsGroupDisplayResponse goodsGroupDisplayResponse) {
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
        goodsGroupDisplayEntity.setGoodsTagList(goodsGroupDisplayResponse.getGoodsTag());
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
     * 在庫状態表示に変換
     *
     * @param stockStatusDisplayResponse 商品グループ在庫表示クラス
     * @return 在庫状態表示
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayResponse stockStatusDisplayResponse) {
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
     * 商品グループ画像レスポンスに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像レスポンス
     * @return 商品グループ画像レスポンス
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
     * @param goodsResponseList 商品レスポンスレスポンス
     * @param shopSeq           ショップSEQ
     * @return 商品DTOリスト
     */
    public List<GoodsDto> toGoodsDtoList(List<GoodsResponse> goodsResponseList, Integer shopSeq) {
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsResponseList.forEach(item -> {
            GoodsDto goodsDto = new GoodsDto();
            GoodsEntity goodsEntity = toGoodsEntity(Objects.requireNonNull(item.getGoodsSub()), shopSeq);
            StockDto stockDto = toStockDto(Objects.requireNonNull(item.getStock()), shopSeq);
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
     * @param shopSeq          ショップSEQ
     * @return 商品エンティティ
     */
    public GoodsEntity toGoodsEntity(GoodsSubResponse goodsSubResponse, Integer shopSeq) {
        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsSeq(goodsSubResponse.getGoodsSeq());
        goodsEntity.setGoodsGroupSeq(goodsSubResponse.getGoodsGroupSeq());
        goodsEntity.setGoodsCode(goodsSubResponse.getGoodsCode());
        goodsEntity.setJanCode(goodsSubResponse.getJanCode());
        goodsEntity.setSaleStatusPC(
                        EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class, goodsSubResponse.getSaleStatus()));
        goodsEntity.setSaleStartTimePC(conversionUtility.toTimestamp(goodsSubResponse.getSaleStartTime()));
        goodsEntity.setSaleEndTimePC(conversionUtility.toTimestamp(goodsSubResponse.getSaleEndTime()));
        goodsEntity.setIndividualDeliveryType(EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                            goodsSubResponse.getIndividualDeliveryType()
                                                                           ));
        goodsEntity.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                      goodsSubResponse.getFreeDeliveryFlag()
                                                                     ));
        goodsEntity.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                        goodsSubResponse.getUnitManagementFlag()
                                                                       ));
        goodsEntity.setUnitValue1(goodsSubResponse.getUnitValue1());
        goodsEntity.setUnitValue2(goodsSubResponse.getUnitValue2());
        goodsEntity.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                         goodsSubResponse.getStockManagementFlag()
                                                                        ));
        goodsEntity.setPurchasedMax(goodsSubResponse.getPurchasedMax());
        goodsEntity.setOrderDisplay(goodsSubResponse.getOrderDisplay());
        goodsEntity.setVersionNo(goodsSubResponse.getVersionNo());
        goodsEntity.setShopSeq(shopSeq);
        goodsEntity.setRegistTime(conversionUtility.toTimestamp(goodsSubResponse.getRegistTime()));
        goodsEntity.setUpdateTime(conversionUtility.toTimestamp(goodsSubResponse.getUpdateTime()));

        return goodsEntity;
    }

    /**
     * 在庫Dtoクラスに変換
     *
     * @param stockResponse 商品グループ検索条件
     * @param shopSeq       ショップSEQ
     * @return 在庫Dtoクラス
     */
    public StockDto toStockDto(StockResponse stockResponse, Integer shopSeq) {
        StockDto stockDto = new StockDto();

        stockDto.setGoodsSeq(stockResponse.getGoodsSeq());
        stockDto.setShopSeq(shopSeq);
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
     * カテゴリ登録商品エンティティリストに変換
     *
     * @param categoryGoodsResponseList 商品レスポンスクラスリスト
     * @return カテゴリ登録商品エンティティリスト
     */
    public List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsResponse> categoryGoodsResponseList) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        categoryGoodsResponseList.forEach(item -> {
            CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();
            categoryGoodsEntity.setCategorySeq(item.getCategorySeq());
            categoryGoodsEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            categoryGoodsEntity.setCategoryName(item.getCategoryName());
            categoryGoodsEntity.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, item.getCategoryType()));
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
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスレスポンス
     * @param shopSeq                                 ショップSEQ
     * @return アイコン詳細Dtoリスト
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailsDtoList(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList,
                                                                                     Integer shopSeq) {
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();

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

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 関連商品レスポンスに変換
     *
     * @param relationGoodsListResponse 関連商品リストレスポンス
     * @return 関連商品レスポンス
     */
    public List<GoodsRelationEntity> toGoodsRelationEntityList(RelationGoodsListResponse relationGoodsListResponse) {
        if (ObjectUtils.isEmpty(relationGoodsListResponse) || CollectionUtils.isEmpty(
                        relationGoodsListResponse.getRelationGoodsList())) {
            return null;
        }
        List<GoodsRelationEntity> goodsRelationEntityList = new ArrayList<>();

        for (RelationGoodsResponse goodsRelationResponse : relationGoodsListResponse.getRelationGoodsList()) {
            GoodsRelationEntity goodsRelationEntity = new GoodsRelationEntity();

            goodsRelationEntity.setGoodsGroupSeq(goodsRelationResponse.getGoodsGroupSeq());
            goodsRelationEntity.setGoodsRelationGroupSeq(goodsRelationResponse.getGoodsRelationGroupSeq());
            goodsRelationEntity.setOrderDisplay(goodsRelationResponse.getOrderDisplay());
            goodsRelationEntity.setRegistTime(conversionUtility.toTimestamp(goodsRelationResponse.getRegistTime()));
            goodsRelationEntity.setUpdateTime(conversionUtility.toTimestamp(goodsRelationResponse.getUpdateTime()));
            goodsRelationEntity.setGoodsGroupCode(goodsRelationResponse.getGoodsGroupCode());
            goodsRelationEntity.setGoodsGroupName(goodsRelationResponse.getGoodsGroupName());
            goodsRelationEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   goodsRelationResponse.getGoodsOpenStatus()
                                                                                  ));

            goodsRelationEntityList.add(goodsRelationEntity);
        }

        return goodsRelationEntityList;
    }

    /**
     * ショップエンティティに変換
     *
     * @param shopResponse   ショップ情報リクエスト
     * @param shopSeq ショップSEQ
     * @return ショップエンティティ
     */
    public ShopEntity toShopEntity(ShopResponse shopResponse, Integer shopSeq) {

        ShopEntity shopEntity = new ShopEntity();

        shopEntity.setShopSeq(shopSeq);
        shopEntity.setShopId(shopResponse.getShopId());
        shopEntity.setShopNamePC(shopResponse.getShopNamePC());
        shopEntity.setUrlPC(shopResponse.getUrlPC());
        shopEntity.setMetaDescription(shopResponse.getMetaDescription());
        shopEntity.setMetaKeyword(shopResponse.getMetaKeyword());
        shopEntity.setVersionNo(shopResponse.getVersionNo());
        shopEntity.setAutoApprovalFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeAutoApprovalFlag.class, shopResponse.getAutoApprovalFlag()));

        return shopEntity;
    }

    /**
     * To big decimal key of map map.
     *
     * @param taxesResponse 税レスポンス
     * @return map
     */
    public Map<BigDecimal, String> toBigDecimalKeyOfMap(TaxesResponse taxesResponse) {
        Map<BigDecimal, String> returnMap = new HashMap<>();
        Map<String, String> mapResponse = taxesResponse.getTaxRateMapList();

        Objects.requireNonNull(mapResponse).forEach((k, v) -> {
            if (!StringUtil.isEmpty(mapResponse.get(k))) {
                returnMap.put(BigDecimal.valueOf(IntegerConversionUtil.toInteger(k)), v);
            }
        });

        return returnMap;
    }

    /**
     * List<GoodsImageItemRequest>に変換
     *
     * @param goodsImageItems 商品管理：商品登録更新（商品画像設定） 商品グループ詳細画像アイテム
     * @return List<GoodsImageItemRequest>
     */
    public List<GoodsImageItemRequest> toGoodsImageItemRequestList(List<GoodsRegistUpdateImageItem> goodsImageItems) {
        List<GoodsImageItemRequest> goodsImageItemRequestList = new ArrayList<>();

        goodsImageItems.forEach(item -> {
            GoodsImageItemRequest goodsImageItemRequest = new GoodsImageItemRequest();

            goodsImageItemRequest.setImageNo(item.getImageNo());
            if (StringUtils.isNotEmpty(item.getImagePath())) {
                String contextPath = PropertiesUtil.getSystemPropertiesValue("server.servlet.context-path");
                goodsImageItemRequest.setImagePath(item.getImagePath().replaceAll(contextPath, ""));
            }

            goodsImageItemRequestList.add(goodsImageItemRequest);
        });

        return goodsImageItemRequestList;
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param goodsRegistUpdateModel
     */
    public void setDefaultValueForLoad(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 税率プルダウンのデフォルト値を設定
        if (ObjectUtils.isEmpty(goodsRegistUpdateModel.getTaxRate())) {
            goodsRegistUpdateModel.setTaxRate(new BigDecimal(10));
        }
        // 公開状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getGoodsOpenStatusPC())) {
            goodsRegistUpdateModel.setGoodsOpenStatusPC(HTypeOpenDeleteStatus.NO_OPEN.getValue());
        }
        // 商品価格のデフォルト値を設定
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getGoodsPrice())) {
            goodsRegistUpdateModel.setGoodsPrice("0");
        }
        // 商品規格ごとのデフォルト値はtoPageForLoadUnit#toPageForAddGoodsで設定するため、ここでは対応しない
    }

    /**
     * 画面表示用商品グループ取得リクエストに変換
     *
     * @param goodGroupCode 商品管理商品グループコード
     * @return 画面表示用商品グループ取得リクエスト
     */
    public ProductDisplayGetRequest toProductDisplayGetRequest(String goodGroupCode) {
        ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();

        productDisplayGetRequest.setGoodsGroupCode(goodGroupCode);
        productDisplayGetRequest.setGoodCode(null);
        productDisplayGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));
        productDisplayGetRequest.setSiteType(EnumTypeUtil.getValue(HTypeSiteType.BACK));

        return productDisplayGetRequest;
    }

}