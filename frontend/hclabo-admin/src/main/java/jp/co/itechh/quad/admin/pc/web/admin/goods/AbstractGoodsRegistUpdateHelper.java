package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.admin.dto.goods.stock.StockDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.product.presentation.api.param.CategoryGoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsSubResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockResponse;
import jp.co.itechh.quad.product.presentation.api.param.StockStatusDisplayResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品管理：商品登録更新抽象ページHelper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AbstractGoodsRegistUpdateHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     */
    public AbstractGoodsRegistUpdateHelper() {
        this.conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
    }

    /**
     * 更新または再利用登録時の商品グループ初期化
     *
     * @param abstractModel ページ
     * @param goodsGroupDto 商品グループDTO
     */
    public void initGoodsGroupDto(AbstractGoodsRegistUpdateModel abstractModel, GoodsGroupDto goodsGroupDto) {
        // 商品グループDTOをセットする
        abstractModel.setGoodsGroupDto(goodsGroupDto);

        // 商品グループ内から販売状態＝「削除」でない商品を１件取得
        GoodsEntity tmpGoodsEntity = null;
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            if (HTypeGoodsSaleStatus.DELETED != goodsDto.getGoodsEntity().getSaleStatusPC()) {
                tmpGoodsEntity = goodsDto.getGoodsEntity();
                break;
            }
        }

        // 共通商品情報を初期化する
        GoodsEntity commonGoodsEntity = ApplicationContextUtility.getBean(GoodsEntity.class);
        if (tmpGoodsEntity == null) {
            // 商品グループ内の全商品が販売状態＝「削除」だった場合
            // 共通商品情報を初期化する
            commonGoodsEntity.setIndividualDeliveryType(HTypeIndividualDeliveryType.OFF);
            commonGoodsEntity.setFreeDeliveryFlag(HTypeFreeDeliveryFlag.OFF);
            // 規格設定画面項目
            commonGoodsEntity.setUnitManagementFlag(abstractModel.getCommonGoodsEntity().getUnitManagementFlag());
            // 在庫管理フラグ
            commonGoodsEntity.setStockManagementFlag(HTypeStockManagementFlag.ON);
        } else {
            // 取得した商品の情報を共通商品情報にセット
            commonGoodsEntity.setGoodsSeq(tmpGoodsEntity.getGoodsSeq());
            commonGoodsEntity.setIndividualDeliveryType(tmpGoodsEntity.getIndividualDeliveryType());
            commonGoodsEntity.setFreeDeliveryFlag(tmpGoodsEntity.getFreeDeliveryFlag());
            // 規格設定画面項目
            commonGoodsEntity.setUnitManagementFlag(tmpGoodsEntity.getUnitManagementFlag());
            // 在庫管理フラグ
            commonGoodsEntity.setStockManagementFlag(tmpGoodsEntity.getStockManagementFlag());
        }
        abstractModel.setCommonGoodsEntity(commonGoodsEntity);
    }

    /**
     * 商品登録時の商品グループ初期化
     *
     * @param abstractModel ページ
     */
    public void initGoodsGroupDto(AbstractGoodsRegistUpdateModel abstractModel) {
        // 商品グループ情報を初期化する
        GoodsGroupEntity goodsGroupEntity = ApplicationContextUtility.getBean(GoodsGroupEntity.class);
        abstractModel.getGoodsGroupDto().setGoodsGroupEntity(goodsGroupEntity);
        // 商品グループ表示を初期化する
        GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                        ApplicationContextUtility.getBean(GoodsGroupDisplayEntity.class);
        abstractModel.getGoodsGroupDto().setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);

        // 共通商品情報を初期化する
        abstractModel.setCommonGoodsEntity(ApplicationContextUtility.getBean(GoodsEntity.class));
        abstractModel.getCommonGoodsEntity().setIndividualDeliveryType(HTypeIndividualDeliveryType.OFF);
        abstractModel.getCommonGoodsEntity().setFreeDeliveryFlag(HTypeFreeDeliveryFlag.OFF);
        // 規格設定画面項目
        abstractModel.getCommonGoodsEntity()
                     .setUnitManagementFlag(abstractModel.getCommonGoodsEntity().getUnitManagementFlag());
        // 在庫管理フラグ
        abstractModel.getCommonGoodsEntity().setStockManagementFlag(HTypeStockManagementFlag.ON);

    }

    /************************************
     ** 共通処理
     ************************************/
    /**
     * 共通商品情報から個別商品情報リストへのデータ反映
     *
     * @param abstractModel ページ
     */
    public void toGoodsListFromCommonGoods(AbstractGoodsRegistUpdateModel abstractModel) {
        if (abstractModel.getGoodsGroupDto() == null || abstractModel.getGoodsGroupDto().getGoodsDtoList() == null) {
            return;
        }
        for (GoodsDto goodsDto : abstractModel.getGoodsGroupDto().getGoodsDtoList()) {
            toGoodsDtoFromCommonGoods(abstractModel, goodsDto.getGoodsEntity());
        }
    }

    /**
     * 共通商品情報から個別商品情報へのデータ反映
     *
     * @param abstractModel ページ
     * @param goodsEntity   商品エンティティ
     */
    public void toGoodsDtoFromCommonGoods(AbstractGoodsRegistUpdateModel abstractModel, GoodsEntity goodsEntity) {
        // 商品基本情報設定
        goodsEntity.setIndividualDeliveryType(abstractModel.getCommonGoodsEntity().getIndividualDeliveryType());
        goodsEntity.setFreeDeliveryFlag(abstractModel.getCommonGoodsEntity().getFreeDeliveryFlag());
        // 規格設定画面項目
        goodsEntity.setUnitManagementFlag(abstractModel.getCommonGoodsEntity().getUnitManagementFlag());
        // 在庫管理フラグ
        goodsEntity.setStockManagementFlag(abstractModel.getCommonGoodsEntity().getStockManagementFlag());
    }

    /************************************
     ** 商品グループ画像関連処理
     ************************************/
    /**
     * 商品グループ画像登録更新用DTOリスト（ページ内編集用）の作成
     * 画像編集用画面のページ初期化時に呼ぶ。
     *
     * @param abstractModel ページ
     */
    public void initTmpGoodsGroupImageRegistUpdateDtoList(AbstractGoodsRegistUpdateModel abstractModel) {
        if (abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList() == null) {
            if (abstractModel.getGoodsGroupImageRegistUpdateDtoList() != null) {
                // 商品グループ画像登録更新用DTOリストをセッションからページへディープコピーする
                abstractModel.setTmpGoodsGroupImageRegistUpdateDtoList(
                                abstractModel.getGoodsGroupImageRegistUpdateDtoList());
            } else {
                abstractModel.setTmpGoodsGroupImageRegistUpdateDtoList(new ArrayList<>());
            }
        }
    }

    /**
     * 商品グループ画像登録更新用DTOリスト（ページ内編集用）のセッション反映
     * 画像編集用画面での編集を終了して別画面へ遷移するときに呼ぶ。
     *
     * @param abstractModel ページ
     */
    public void setTmpGoodsGroupImageRegistUpdateDtoList(AbstractGoodsRegistUpdateModel abstractModel) {
        abstractModel.setGoodsGroupImageRegistUpdateDtoList(abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList());
    }

    /**
     * 商品グループ画像パスの取得
     *
     * @param abstractModel      ページ
     * @param imageTypeVersionNo 画像種別内連番
     * @return 商品グループ画像ファイルパス
     */
    public String getGoodsImageFilepath(AbstractGoodsRegistUpdateModel abstractModel, Integer imageTypeVersionNo) {
        if (abstractModel.getGoodsGroupDto() == null
            || abstractModel.getGoodsGroupDto().getGoodsGroupImageEntityList() == null) {
            return null;
        }
        // 商品グループ画像登録更新用DTOリストを参照してファイルパスを取得
        if (abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList() != null) {
            for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList()) {
                if (goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()
                                                  .getImageTypeVersionNo()
                                                  .equals(imageTypeVersionNo)) {
                    if (goodsGroupImageRegistUpdateDto.getDeleteFlg()) {
                        // 削除フラグがtrueの場合
                        return null;
                    }
                    // 画像操作Utility取得
                    String tmpImageDirpath = PropertiesUtil.getSystemPropertiesValue("server.servlet.context-path")
                                             + PropertiesUtil.getSystemPropertiesValue("tmp.path");
                    return tmpImageDirpath + "/" + goodsGroupImageRegistUpdateDto.getTmpImageFileName();
                }
            }
        }
        // 商品グループエンティティを参照してファイルパスを取得
        for (GoodsGroupImageEntity goodsGroupImageEntity : abstractModel.getGoodsGroupDto()
                                                                        .getGoodsGroupImageEntityList()) {
            if (goodsGroupImageEntity.getImageTypeVersionNo().equals(imageTypeVersionNo)) {
                String goodsGroupImagepath = PropertiesUtil.getSystemPropertiesValue("images.path.goods");
                return goodsGroupImagepath + goodsGroupImageEntity.getImageFileName();
            }
        }
        return null;
    }

    /**
     * 商品グループ画像登録更新情報作成
     *
     * @param abstractModel      ページ
     * @param imageTypeVersionNo 画像種別内連番
     * @param tmpImageFilePath   一時画像ファイルパス
     * @param tmpImageFileName   一時画像ファイル名
     * @param imageFileName      登録先画像ファイル名
     * @param deleteFlg          削除フラグ
     */
    public void makeGoodsGroupImageRegistUpdateInfo(AbstractGoodsRegistUpdateModel abstractModel,
                                                    Integer imageTypeVersionNo,
                                                    String tmpImageFilePath,
                                                    String tmpImageFileName,
                                                    String imageFileName,
                                                    boolean deleteFlg) {
        // 商品グループ画像登録更新情報がnullの場合は作成する
        if (abstractModel.getGoodsGroupImageRegistUpdateDtoList() == null) {
            abstractModel.setGoodsGroupImageRegistUpdateDtoList(new ArrayList<>());
        }
        if (abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList() == null) {
            abstractModel.setTmpGoodsGroupImageRegistUpdateDtoList(new ArrayList<>());
        }

        GoodsGroupImageEntity goodsGroupImageEntity = null;
        // 商品グループDTOを参照して該当する商品グループ画像情報があれば取得する
        if (abstractModel.getGoodsGroupDto().getGoodsGroupImageEntityList() != null) {
            for (GoodsGroupImageEntity tmpGoodsGroupImageEntity : abstractModel.getGoodsGroupDto()
                                                                               .getGoodsGroupImageEntityList()) {
                if (tmpGoodsGroupImageEntity.getImageTypeVersionNo().equals(imageTypeVersionNo)) {
                    goodsGroupImageEntity = tmpGoodsGroupImageEntity;
                    break;
                }
            }
        }

        // 商品グループ画像登録更新用DTOリストに該当画像の情報があれば取得して更新する
        for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList()) {
            if (goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()
                                              .getImageTypeVersionNo()
                                              .equals(imageTypeVersionNo)) {
                // "削除"かつ商品グループ画像情報がない場合は、商品グループ画像登録更新用DTOリストから削除するのみ
                if (deleteFlg && goodsGroupImageEntity == null) {
                    abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList().remove(goodsGroupImageRegistUpdateDto);
                }
                if (goodsGroupImageEntity != null) {
                    goodsGroupImageRegistUpdateDto.setGoodsGroupImageEntity(goodsGroupImageEntity);
                }
                goodsGroupImageRegistUpdateDto.setTmpImageFilePath(tmpImageFilePath);
                goodsGroupImageRegistUpdateDto.setTmpImageFileName(tmpImageFileName);
                goodsGroupImageRegistUpdateDto.setImageFileName(imageFileName);
                goodsGroupImageRegistUpdateDto.setDeleteFlg(deleteFlg);
                return;
            }
        }

        // 商品グループ画像エンティティが存在しない場合は
        // 商品グループ画像エンティティを作成して商品グループDTOに追加する
        if (goodsGroupImageEntity == null) {
            if (deleteFlg) {
                // 商品グループ画像エンティティが存在しない、かつ削除はありえないが、処理なしで戻す
                return;
            }
            goodsGroupImageEntity = ApplicationContextUtility.getBean(GoodsGroupImageEntity.class);
            if (abstractModel.getGoodsGroupDto().getGoodsGroupEntity() != null) {
                goodsGroupImageEntity.setGoodsGroupSeq(
                                abstractModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq());
            }
            goodsGroupImageEntity.setImageTypeVersionNo(imageTypeVersionNo);
            if (abstractModel.getGoodsGroupDto().getGoodsGroupImageEntityList() == null) {
                abstractModel.getGoodsGroupDto().setGoodsGroupImageEntityList(new ArrayList<>());
            }
        }

        // 商品グループ画像登録更新用DTOリストに存在しない場合は
        // 商品グループ画像エンティティと商品グループ画像登録更新用DTOを作成してリストに追加する
        GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto =
                        ApplicationContextUtility.getBean(GoodsGroupImageRegistUpdateDto.class);
        goodsGroupImageRegistUpdateDto.setGoodsGroupImageEntity(goodsGroupImageEntity);
        goodsGroupImageRegistUpdateDto.setTmpImageFilePath(tmpImageFilePath);
        goodsGroupImageRegistUpdateDto.setTmpImageFileName(tmpImageFileName);
        goodsGroupImageRegistUpdateDto.setImageFileName(imageFileName);
        goodsGroupImageRegistUpdateDto.setDeleteFlg(deleteFlg);
        abstractModel.getTmpGoodsGroupImageRegistUpdateDtoList().add(goodsGroupImageRegistUpdateDto);
    }

    /**
     * 文字列配列⇒スラッシュ区切り変換
     *
     * @param param 文字の配列
     * @return スラッシュ区切り後の文字列
     */
    public String joinSlashString(String[] param) {
        if (param == null || param.length == 0) {
            return null;
        }

        String retString = "";
        for (String str : param) {
            if (!"".equals(retString)) {
                retString += "/";
            }
            retString += str;
        }
        return retString;
    }

    /**
     * 商品グループDTOに変換
     *
     * @param productDisplayResponse 画面表示用商品グループレスポンス
     * @return 商品グループDTO
     */
    public GoodsGroupDto toGoodsDtoFromProductDisplayResponse(ProductDisplayResponse productDisplayResponse) {
        Integer shopSeq = 1001;
        GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

        GoodsGroupEntity goodsGroupEntity =
                        toGoodsGroupEntity(Objects.requireNonNull(productDisplayResponse.getGoodsGroup()), shopSeq);
        StockStatusDisplayEntity batchUpdateStockStatusDisplayEntity = toStockStatusDisplayEntity(
                        Objects.requireNonNull(productDisplayResponse.getBatchUpdateStockStatus()));
        StockStatusDisplayEntity realTimeStockStatusDisplayEntity = toStockStatusDisplayEntity(
                        Objects.requireNonNull(productDisplayResponse.getRealTimeStockStatus()));
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = toGoodsGroupDisplayEntity(
                        Objects.requireNonNull(productDisplayResponse.getGoodsGroupDisplay()));
        List<GoodsGroupImageEntity> goodsGroupImageEntityList = toGoodsGroupImageEntityList(
                        Objects.requireNonNull(productDisplayResponse.getGoodsGroupImageResponseList()));
        List<GoodsDto> goodsDtoList =
                        toGoodsDtoList(Objects.requireNonNull(productDisplayResponse.getGoodsResponseList()), shopSeq);
        List<CategoryGoodsEntity> categoryGoodsEntityList = toCategoryGoodsEntityList(
                        Objects.requireNonNull(productDisplayResponse.getCategoryGoodsResponseList()));
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = toGoodsInformationIconDetailsDtoList(
                        Objects.requireNonNull(productDisplayResponse.getGoodsInformationIconDetailsResponseList()),
                        shopSeq
                                                                                                                      );

        goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
        goodsGroupDto.setBatchUpdateStockStatus(batchUpdateStockStatusDisplayEntity);
        goodsGroupDto.setRealTimeStockStatus(realTimeStockStatusDisplayEntity);
        goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
        goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);
        goodsGroupDto.setGoodsDtoList(goodsDtoList);
        goodsGroupDto.setCategoryGoodsEntityList(categoryGoodsEntityList);
        goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);
        goodsGroupDto.setFrontDisplay(EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class,
                                                                    productDisplayResponse.getFrontDisplay()
                                                                   ));
        goodsGroupDto.setTaxRate(productDisplayResponse.getTaxRate());

        return goodsGroupDto;
    }

    /**
     * 商品グループクラスに変換
     *
     * @param goodsGroupSubResponse 商品詳細レスポンスクラス
     * @param shopSeq               ショップSEQ
     * @return 商品グループクラス
     */
    public GoodsGroupEntity toGoodsGroupEntity(GoodsGroupSubResponse goodsGroupSubResponse, Integer shopSeq) {

        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(goodsGroupSubResponse.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(goodsGroupSubResponse.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(goodsGroupSubResponse.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(goodsGroupSubResponse.getGoodsPrice());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(goodsGroupSubResponse.getWhatsnewDate()));
        goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                            goodsGroupSubResponse.getGoodsOpenStatus()
                                                                           ));
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenStartTime()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(goodsGroupSubResponse.getOpenEndTime()));
        goodsGroupEntity.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                       goodsGroupSubResponse.getGoodsTaxType()
                                                                      ));
        goodsGroupEntity.setTaxRate(goodsGroupSubResponse.getTaxRate());
        goodsGroupEntity.setAlcoholFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, goodsGroupSubResponse.getAlcoholFlag()));
        goodsGroupEntity.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class,
                                                                           goodsGroupSubResponse.getNoveltyGoodsType()
                                                                          ));
        goodsGroupEntity.setSnsLinkFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, goodsGroupSubResponse.getSnsLinkFlag()));
        goodsGroupEntity.setVersionNo(goodsGroupSubResponse.getVersionNo());
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(goodsGroupSubResponse.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(goodsGroupSubResponse.getUpdateTime()));
        goodsGroupEntity.setShopSeq(shopSeq);

        return goodsGroupEntity;
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
     * @return 在庫状態表示 stock status display entity
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
     * @return 商品グループ画像レスポンス list
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
     * @return 商品エンティティ goods entity
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
     * @return 在庫Dtoクラス stock dto
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
     * カテゴリ登録商品レスポンスに変換
     *
     * @param categoryGoodsResponseList 商品レスポンスレスポンス
     * @return カテゴリ登録商品レスポンス list
     */
    public List<CategoryGoodsEntity> toCategoryGoodsEntityList(List<CategoryGoodsResponse> categoryGoodsResponseList) {
        List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();

        categoryGoodsResponseList.forEach(item -> {
            CategoryGoodsEntity categoryGoodsEntity = new CategoryGoodsEntity();
            categoryGoodsEntity.setCategorySeq(item.getCategorySeq());
            categoryGoodsEntity.setCategoryName(item.getCategoryName());
            categoryGoodsEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            categoryGoodsEntity.setOrderDisplay(item.getManualOrderDisplay());
            categoryGoodsEntity.setCategoryType(
                            EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, item.getCategoryType()));
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
     * @return アイコン詳細Dtoリスト list
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
     * @return 関連商品レスポンス list
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
     * カテゴリレスポンスマップに変換
     *
     * @param categoryResponseList カテゴリレスポンスのリスト
     * @return map
     */
    public Map<String, String> convertCategoryMapStr(List<CategoryResponse> categoryResponseList) {
        Map<String, String> categoryMap = new HashMap<>();

        categoryResponseList.forEach(item -> {
            categoryMap.put(item.getCategoryId(), item.getCategoryName());
        });

        return categoryMap;
    }
}