/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import org.apache.commons.lang3.ObjectUtils;
import org.seasar.doma.boot.autoconfigure.DomaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループリスト取得
 *
 * @author ozaki
 */
@Component
public class GoodsGroupListGetLogicImpl extends AbstractShopLogic implements GoodsGroupListGetLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    /** 商品グループ画像取得ロジッククラス */
    private final GoodsGroupImageGetLogic goodsGroupImageGetLogic;

    /** 商品インフォメーションアイコン情報取得ロジッククラス */
    private final GoodsInformationIconGetLogic goodsInformationIconGetLogic;

    /** 商品グループSEQリスト */
    protected List<Integer> goodsGroupSeqList;

    /** 商品アイコンSEQリスト */
    protected List<Integer> iconSeqList;

    /** 配列工場ユーティリティクラス */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /** doma-spring-boot で使用される実装。 */
    private final DomaConfig config;

    @Autowired
    public GoodsGroupListGetLogicImpl(GoodsGroupDao goodsGroupDao,
                                      GoodsGroupImageGetLogic goodsGroupImageGetLogic,
                                      GoodsInformationIconGetLogic goodsInformationIconGetLogic,
                                      ArrayFactoryUtility arrayFactoryUtility,
                                      DomaConfig config) {
        this.goodsGroupDao = goodsGroupDao;
        this.goodsGroupImageGetLogic = goodsGroupImageGetLogic;
        this.goodsInformationIconGetLogic = goodsInformationIconGetLogic;
        this.arrayFactoryUtility = arrayFactoryUtility;
        this.config = config;
    }

    /**
     * 商品グループリスト取得
     * 検索条件をもとに商品グループ情報リストを取得する。
     *
     * @param conditionDto 商品グループDao用検索条件DTO
     * @return 商品グループリスト
     */
    @Override
    public List<GoodsGroupDto> execute(GoodsGroupSearchForDaoConditionDto conditionDto) {

        // (1) 初期値設定
        initValue();

        // (2) パラメータチェック
        // 商品情報Dao用検索条件DTOが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // (3) 商品情報取得
        List<GoodsGroupDetailsDto> goodsGroupDetailsDtoList = getSearchGoodsGroupList(conditionDto);

        // (4) 商品グループSEQリスト、商品アイコンSEQリストの生成
        makeGoodsSeqList(conditionDto, goodsGroupDetailsDtoList);

        // (5) 商品グループ画像情報取得
        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageEntityMap = getGoodsGroupImageEntityMap();

        // (6) 商品アイコンMAP取得
        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap = getGoodsInformationIconDetailsDtoMap();

        // (7) (3)、(5)、(6)で取得した情報を元に、戻り値を作成
        List<GoodsGroupDto> goodsGroupDtoList =
                        makeGoodsGroupDtoList(conditionDto, goodsGroupDetailsDtoList, goodsGroupImageEntityMap,
                                              iconDetailsDtoMap
                                             );

        // (8) 商品グループDTOリストを返す。
        return goodsGroupDtoList;
    }

    /**
     * カテゴリ商品一覧 or セール商品一覧切り替え
     * categoryIDがnull の場合はセール商品一覧
     *
     * @param conditionDto 商品グループDao用検索条件DTO
     * @return 商品グループ詳細DTO
     */
    protected List<GoodsGroupDetailsDto> getSearchGoodsGroupList(GoodsGroupSearchForDaoConditionDto conditionDto) {
        return goodsGroupDao.getSearchGoodsGroupDetailsList(
                        conditionDto, conditionDto.getPageInfo().getSelectOptions());
    }

    /**
     * 初期値設定処理
     *
     * @param customParams 案件用引数
     */
    protected void initValue(Object... customParams) {

        // 商品グループSEQリスト
        goodsGroupSeqList = new ArrayList<>();
        // 商品アイコンSEQリスト
        iconSeqList = new ArrayList<>();
        // エラーリスト
        clearErrorList();
    }

    /**
     * 商品グループSEQリスト、商品アイコンSEQリストの生成
     *
     * @param conditionDto 商品グループ検索条件DTO
     * @param goodsGroupDetailsDtoList 商品グループ詳細DTOリスト
     * @param customParams 案件用引数
     */
    protected void makeGoodsSeqList(GoodsGroupSearchForDaoConditionDto conditionDto,
                                    List<GoodsGroupDetailsDto> goodsGroupDetailsDtoList,
                                    Object... customParams) {

        Map<String, String> iconSeqMap = new HashMap<>();

        for (GoodsGroupDetailsDto goodsGroupDetailsDto : goodsGroupDetailsDtoList) {

            // 商品詳細DTOリストから商品グループSEQのリストを作成する。
            goodsGroupSeqList.add(goodsGroupDetailsDto.getGoodsGroupSeq());

            // 商品詳細DTOリストから、商品インフォメーションアイコンSEQのリストを作成する。
            String[] informationIconSeqArray = null;
            if ((conditionDto.getSiteType() == HTypeSiteType.FRONT_PC)
                && goodsGroupDetailsDto.getInformationIconPC() != null) {
                informationIconSeqArray = goodsGroupDetailsDto.getInformationIconPC().split("/");
            }

            if (informationIconSeqArray == null) {
                continue;
            }

            for (String informationIconSeq : informationIconSeqArray) {

                if (!informationIconSeq.equals("") && !iconSeqMap.containsKey(informationIconSeq)) {
                    iconSeqMap.put(informationIconSeq, informationIconSeq);
                    iconSeqList.add(Integer.parseInt(informationIconSeq));
                }
            }
        }
    }

    /**
     * 商品グループ画像エンティティマップを取得
     *
     * @param customParams 案件用引数
     * @return 商品グループ画像エンティティマップ
     */
    protected Map<Integer, List<GoodsGroupImageEntity>> getGoodsGroupImageEntityMap(Object... customParams) {

        // 作成した商品グループSEQリストをもとに、商品グループ画像取得Logicを利用して、商品グループ画像マップを取得する
        if (goodsGroupSeqList.isEmpty()) {
            return null;
        }
        return goodsGroupImageGetLogic.execute(goodsGroupSeqList);
    }

    /**
     * 商品アイコン詳細DTOマップを取得
     *
     * @param customParams 案件用引数
     * @return 商品アイコン詳細DTOマップ
     */
    protected Map<String, GoodsInformationIconDetailsDto> getGoodsInformationIconDetailsDtoMap(Object... customParams) {

        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap = new LinkedHashMap<>();
        if (iconSeqList.isEmpty()) {
            return iconDetailsDtoMap;
        }

        List<GoodsInformationIconDetailsDto> iconDetailsDtoList = goodsInformationIconGetLogic.execute(iconSeqList);

        for (GoodsInformationIconDetailsDto iconDetailsDto : iconDetailsDtoList) {
            iconDetailsDtoMap.put(iconDetailsDto.getIconSeq().toString(), iconDetailsDto);
        }
        return iconDetailsDtoMap;
    }

    /**
     * 商品グループDTOリストを生成
     *
     * @param conditionDto 商品グループ検索条件DTO
     * @param goodsGroupDetailsDtoList 商品グループ詳細DTOリスト
     * @param goodsGroupImageEntityMap 商品グループ画像エンティティマップ
     * @param iconDetailsDtoMap 商品アイコン詳細DTOマップ
     * @param customParams 案件用引数
     * @return 商品グループDTOリスト
     */
    protected List<GoodsGroupDto> makeGoodsGroupDtoList(GoodsGroupSearchForDaoConditionDto conditionDto,
                                                        List<GoodsGroupDetailsDto> goodsGroupDetailsDtoList,
                                                        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageEntityMap,
                                                        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap,
                                                        Object... customParams) {

        List<GoodsGroupDto> goodsGroupDtoList = new ArrayList<>();
        for (GoodsGroupDetailsDto goodsGroupDetailsDto : goodsGroupDetailsDtoList) {

            // 商品グループエンティティの作成
            GoodsGroupEntity goodsGroupEntity = getGoodsGroupEntity(goodsGroupDetailsDto);

            // 商品グループ表示エンティティの作成
            GoodsGroupDisplayEntity goodsGroupDisplayEntity = getGoodsGroupDisplayEntity(goodsGroupDetailsDto);

            // 商品グループ在庫表示エンティティの作成
            StockStatusDisplayEntity batchUpdateStockStatus = getStockStatusDisplay(goodsGroupDetailsDto);

            // 商品リストの作成
            List<GoodsDto> goodsDtoList = getGoodsDtoList();

            // アイコンの作成
            List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList =
                            getGoodsInformationIconDetailsDtoList(goodsGroupDetailsDto, iconDetailsDtoMap,
                                                                  conditionDto.getSiteType()
                                                                 );

            GoodsGroupDto goodsGroupDto = ApplicationContextUtility.getBean(GoodsGroupDto.class);
            goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
            goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
            // 商品グループ一覧データ取得処理では在庫状況更新バッチ実行時点の在庫状況を取得している
            goodsGroupDto.setBatchUpdateStockStatus(batchUpdateStockStatus);
            goodsGroupDto.setGoodsDtoList(goodsDtoList);
            goodsGroupDto.setGoodsGroupImageEntityList(
                            goodsGroupImageEntityMap.get(goodsGroupDetailsDto.getGoodsGroupSeq()));
            goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);

            // フロント表示状態を判定（公開状態と公開期間から判断）
            goodsGroupDto.setFrontDisplay(goodsGroupDetailsDto.getFrontDisplay());
            // 公開以外の場合はワーニングメッセージを設定
            if (HTypeFrontDisplayStatus.NO_OPEN.equals(goodsGroupDto.getFrontDisplay())) {
                // 公開状態であり期間外の場合
                if (HTypeOpenDeleteStatus.OPEN.equals(goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC())
                    && ObjectUtils.isNotEmpty(goodsGroupDetailsDto.getOpenStartTimePC()) || ObjectUtils.isNotEmpty(
                                goodsGroupDetailsDto.getOpenEndTimePC())) {
                    goodsGroupDto.setWarningMessage(AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_OUT_OF_TERM,
                                                                                           new Object[] {ObjectUtils.isEmpty(
                                                                                                           goodsGroupDetailsDto.getOpenStartTimePC()) ?
                                                                                                           "" :
                                                                                                           goodsGroupDetailsDto.getOpenStartTimePC(),
                                                                                                           ObjectUtils.isEmpty(
                                                                                                                           goodsGroupDetailsDto.getOpenEndTimePC()) ?
                                                                                                                           "" :
                                                                                                                           goodsGroupDetailsDto.getOpenEndTimePC()}
                                                                                          ).getMessage());
                } else {
                    goodsGroupDto.setWarningMessage(
                                    AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_NO_OPEN, null)
                                                            .getMessage());
                }
            }

            goodsGroupDtoList.add(goodsGroupDto);
        }
        return goodsGroupDtoList;
    }

    /**
     *
     * 商品グループエンティティ作成
     * 商品グループ詳細DTOより、商品グループエンティティを作成する
     *
     * @param goodsGroupDetailsDto 商品グループ詳細DTO
     * @return 商品グループエンティティ
     */
    protected GoodsGroupEntity getGoodsGroupEntity(GoodsGroupDetailsDto goodsGroupDetailsDto) {

        GoodsGroupEntity goodsGroupEntity = getComponent(GoodsGroupEntity.class);

        goodsGroupEntity.setGoodsGroupSeq(goodsGroupDetailsDto.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(goodsGroupDetailsDto.getGoodsGroupCode());
        goodsGroupEntity.setWhatsnewDate(goodsGroupDetailsDto.getWhatsnewDate());
        goodsGroupEntity.setGoodsOpenStatusPC(goodsGroupDetailsDto.getGoodsOpenStatusPC());
        goodsGroupEntity.setOpenStartTimePC(goodsGroupDetailsDto.getOpenStartTimePC());
        goodsGroupEntity.setOpenEndTimePC(goodsGroupDetailsDto.getOpenEndTimePC());
        goodsGroupEntity.setShopSeq(goodsGroupDetailsDto.getShopSeq());
        goodsGroupEntity.setGoodsGroupName(goodsGroupDetailsDto.getGoodsGroupName());
        goodsGroupEntity.setVersionNo(goodsGroupDetailsDto.getVersionNo());

        // 消費税対応追加分
        goodsGroupEntity.setTaxRate(goodsGroupDetailsDto.getTaxRate());
        goodsGroupEntity.setGoodsTaxType(goodsGroupDetailsDto.getGoodsTaxType());
        goodsGroupEntity.setGoodsPrice(goodsGroupDetailsDto.getGoodsPrice());

        return goodsGroupEntity;
    }

    /**
     *
     * 商品グループ表示エンティティ作成
     * 商品グループ詳細DTOより、商品グループ表示エンティティを作成する
     *
     * @param goodsGroupDetailsDto 商品グループ詳細DTO
     * @return 商品グループ表示エンティティ
     */
    protected GoodsGroupDisplayEntity getGoodsGroupDisplayEntity(GoodsGroupDetailsDto goodsGroupDetailsDto) {

        GoodsGroupDisplayEntity goodsGroupDisplayEntity = getComponent(GoodsGroupDisplayEntity.class);

        goodsGroupDisplayEntity.setGoodsGroupSeq(goodsGroupDetailsDto.getGoodsGroupSeq());
        goodsGroupDisplayEntity.setInformationIconPC(goodsGroupDetailsDto.getInformationIconPC());
        goodsGroupDisplayEntity.setSearchKeyword(goodsGroupDetailsDto.getSearchKeyword());
        goodsGroupDisplayEntity.setSearchKeywordEmUc(goodsGroupDetailsDto.getSearchKeywordEm());
        goodsGroupDisplayEntity.setUnitManagementFlag(goodsGroupDetailsDto.getUnitManagementFlag());
        goodsGroupDisplayEntity.setUnitTitle1(goodsGroupDetailsDto.getUnitTitle1());
        goodsGroupDisplayEntity.setUnitTitle2(goodsGroupDetailsDto.getUnitTitle2());
        goodsGroupDisplayEntity.setMetaDescription(goodsGroupDetailsDto.getMetaDescription());
        goodsGroupDisplayEntity.setMetaKeyword(goodsGroupDetailsDto.getMetaKeyword());
        goodsGroupDisplayEntity.setDeliveryType(goodsGroupDetailsDto.getDeliverytype());
        goodsGroupDisplayEntity.setGoodsTag(goodsGroupDetailsDto.getGoodsTag());
        // 商品説明１～１０
        goodsGroupDisplayEntity.setGoodsNote1(goodsGroupDetailsDto.getGoodsNote1());
        goodsGroupDisplayEntity.setGoodsNote2(goodsGroupDetailsDto.getGoodsNote2());
        goodsGroupDisplayEntity.setGoodsNote3(goodsGroupDetailsDto.getGoodsNote3());
        goodsGroupDisplayEntity.setGoodsNote4(goodsGroupDetailsDto.getGoodsNote4());
        goodsGroupDisplayEntity.setGoodsNote5(goodsGroupDetailsDto.getGoodsNote5());
        goodsGroupDisplayEntity.setGoodsNote6(goodsGroupDetailsDto.getGoodsNote6());
        goodsGroupDisplayEntity.setGoodsNote7(goodsGroupDetailsDto.getGoodsNote7());
        goodsGroupDisplayEntity.setGoodsNote8(goodsGroupDetailsDto.getGoodsNote8());
        goodsGroupDisplayEntity.setGoodsNote9(goodsGroupDetailsDto.getGoodsNote9());
        goodsGroupDisplayEntity.setGoodsNote10(goodsGroupDetailsDto.getGoodsNote10());
        // 受注連携設定１～１０
        goodsGroupDisplayEntity.setOrderSetting1(goodsGroupDetailsDto.getOrderSetting1());
        goodsGroupDisplayEntity.setOrderSetting2(goodsGroupDetailsDto.getOrderSetting2());
        goodsGroupDisplayEntity.setOrderSetting3(goodsGroupDetailsDto.getOrderSetting3());
        goodsGroupDisplayEntity.setOrderSetting4(goodsGroupDetailsDto.getOrderSetting4());
        goodsGroupDisplayEntity.setOrderSetting5(goodsGroupDetailsDto.getOrderSetting5());
        goodsGroupDisplayEntity.setOrderSetting6(goodsGroupDetailsDto.getOrderSetting6());
        goodsGroupDisplayEntity.setOrderSetting7(goodsGroupDetailsDto.getOrderSetting7());
        goodsGroupDisplayEntity.setOrderSetting8(goodsGroupDetailsDto.getOrderSetting8());
        goodsGroupDisplayEntity.setOrderSetting9(goodsGroupDetailsDto.getOrderSetting9());
        goodsGroupDisplayEntity.setOrderSetting10(goodsGroupDetailsDto.getOrderSetting10());
        goodsGroupDisplayEntity.setPopularityCount(goodsGroupDetailsDto.getPopularityCount());

        // goodsGroupDisplayEntity.setRegistTime();
        // goodsGroupDisplayEntity.setUpdateTime();

        return goodsGroupDisplayEntity;
    }

    /**
     *
     * 商品グループ在庫表示エンティティDTO作成
     * 商品グループ詳細DTOより、商品グループ在庫表示エンティティDTOを作成する
     *
     * @param goodsGroupDetailsDto 商品グループ詳細DTO
     * @return 商品グループ在庫表示エンティティDTO
     */
    protected StockStatusDisplayEntity getStockStatusDisplay(GoodsGroupDetailsDto goodsGroupDetailsDto) {
        StockStatusDisplayEntity stockStatusDisplay = getComponent(StockStatusDisplayEntity.class);

        stockStatusDisplay.setStockStatusPc(goodsGroupDetailsDto.getStockstatusPC());
        return stockStatusDisplay;
    }

    /**
     *
     * 商品DTOリスト作成
     * 商品グループ詳細DTOより、商品DTOリストを作成する
     *
     * @return 商品DTOリスト
     */
    protected List<GoodsDto> getGoodsDtoList() {

        GoodsEntity goodsEntity = getComponent(GoodsEntity.class);
        GoodsDto goodsDto = getComponent(GoodsDto.class);
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsDto.setGoodsEntity(goodsEntity);

        goodsDtoList.add(goodsDto);

        return goodsDtoList;
    }

    /**
     *
     * 商品インフォメーションアイコンDTOリスト作成
     * 商品グループ詳細DTOより、対象の商品インフォメーションアイコンDTOリストを作成する
     *
     * @param goodsGroupDetailsDto 商品グループ詳細DTO
     * @param iconDetailsDtoMap 商品インフォメーションアイコン詳細MAP
     * @param siteType サイト区分
     * @return 商品インフォメーションアイコンDTOリスト
     */
    protected List<GoodsInformationIconDetailsDto> getGoodsInformationIconDetailsDtoList(GoodsGroupDetailsDto goodsGroupDetailsDto,
                                                                                         Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap,
                                                                                         HTypeSiteType siteType) {

        // 対象となるアイコンSEQの抽出
        String[] informationIconSeqArray = null;
        if ((siteType == HTypeSiteType.FRONT_PC) && goodsGroupDetailsDto.getInformationIconPC() != null) {
            informationIconSeqArray = goodsGroupDetailsDto.getInformationIconPC().split("/");
        }

        // アイコンSEQより、商品インフォメーションアイコンリストの作成
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
        if (informationIconSeqArray != null) {

            for (Iterator<String> it = iconDetailsDtoMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();

                for (int i = 0; i < informationIconSeqArray.length; i++) {
                    if (key.equals(informationIconSeqArray[i])) {
                        goodsInformationIconDetailsDtoList.add(iconDetailsDtoMap.get(informationIconSeqArray[i]));
                    }
                }
            }
        }

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * カテゴリの修正
     *
     * @param goodGroupSeq                      商品グループSEQ
     * @param categoryConditionEntity           カテゴリ条件エンティティクラス
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティクラスのリスト
     * @return 件数
     */
    @Override
    public List<Integer> execute(Integer goodGroupSeq,
                                 CategoryConditionEntity categoryConditionEntity,
                                 List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {

        AssertionUtil.assertNotNull("categoryConditionEntity", categoryConditionEntity);
        AssertionUtil.assertNotNull("categoryConditionDetailEntityList", categoryConditionDetailEntityList);

        List<Integer> responseList = goodsGroupDao.getGoodsGroupSeqListByCondition(goodGroupSeq,
                                                                                   categoryConditionEntity.getConditionType(),
                                                                                   categoryConditionDetailEntityList
                                                                                  );

        return responseList;
    }
}