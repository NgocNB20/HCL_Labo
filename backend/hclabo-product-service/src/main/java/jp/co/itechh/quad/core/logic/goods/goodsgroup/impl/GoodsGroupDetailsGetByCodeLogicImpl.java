/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.stock.StockStatusDisplayDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockStatusGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDetailsGetByCodeLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 商品グループ詳細取得
 *
 * @author ozaki
 */
@Component
public class GoodsGroupDetailsGetByCodeLogicImpl extends AbstractShopLogic implements GoodsGroupDetailsGetByCodeLogic {

    /** 商品グループ在庫表示DAO */
    private final StockStatusDisplayDao stockStatusDisplayDao;

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    /** 商品グループ画像取得ロジッククラス */
    private final GoodsGroupImageGetLogic goodsGroupImageGetLogic;

    /** 商品グループ表示取得ロジッククラス */
    private final GoodsGroupDisplayGetLogic goodsGroupDisplayGetLogic;

    /** 商品一覧取得ロジッククラス */
    private final GoodsListGetLogic goodsListGetLogic;

    /** 商品インフォメーションアイコンリスト取得ロジック */
    private final GoodsInformationIconGetLogic goodsInformationIconGetLogic;

    /** 商品の在庫状態取得ロジック */
    private final GoodsStockStatusGetLogic goodsStockStatusGetLogic;

    /** DateUtility */
    private final DateUtility dateUtility;

    /** 商品Utility */
    private final GoodsUtility goodsUtility;

    @Autowired
    public GoodsGroupDetailsGetByCodeLogicImpl(StockStatusDisplayDao stockStatusDisplayDao,
                                               GoodsGroupDao goodsGroupDao,
                                               GoodsGroupImageGetLogic goodsGroupImageGetLogic,
                                               GoodsGroupDisplayGetLogic goodsGroupDisplayGetLogic,
                                               GoodsListGetLogic goodsListGetLogic,
                                               GoodsInformationIconGetLogic goodsInformationIconGetLogic,
                                               GoodsStockStatusGetLogic goodsStockStatusGetLogic,
                                               DateUtility dateUtility,
                                               GoodsUtility goodsUtility) {
        this.stockStatusDisplayDao = stockStatusDisplayDao;
        this.goodsGroupDao = goodsGroupDao;
        this.goodsGroupImageGetLogic = goodsGroupImageGetLogic;
        this.goodsGroupDisplayGetLogic = goodsGroupDisplayGetLogic;
        this.goodsListGetLogic = goodsListGetLogic;
        this.goodsInformationIconGetLogic = goodsInformationIconGetLogic;
        this.goodsStockStatusGetLogic = goodsStockStatusGetLogic;
        this.dateUtility = dateUtility;
        this.goodsUtility = goodsUtility;
    }

    /**
     * 商品グループ詳細取得
     * 商品グループコードから、商品グループ詳細DTOを取得する。
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode 商品コード
     * @param siteType サイト区分
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループ情報DTO
     */
    @Override
    public GoodsGroupDto execute(Integer shopSeq,
                                 String goodsGroupCode,
                                 String goodsCode,
                                 HTypeSiteType siteType,
                                 HTypeOpenDeleteStatus openStatus,
                                 Timestamp frontDisplayReferenceDate) {
        return execute(shopSeq, goodsGroupCode, goodsCode, siteType, openStatus, frontDisplayReferenceDate, null);
    }

    /**
     * 商品グループ詳細取得
     * 商品グループコードから、商品グループ詳細DTOを取得する。
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode 商品コード
     * @param siteType サイト区分
     * @param openStatus 公開状態
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループ情報DTO
     */
    @Override
    public GoodsGroupDto execute(Integer shopSeq,
                                 String goodsGroupCode,
                                 String goodsCode,
                                 HTypeSiteType siteType,
                                 HTypeOpenDeleteStatus openStatus,
                                 Timestamp frontDisplayReferenceDate,
                                 Boolean imageGetFlag) {
        String goodsInfo = null;
        if (StringUtils.isNotEmpty(goodsGroupCode)) {
            goodsInfo = goodsGroupCode;
        }
        if (StringUtils.isNotEmpty(goodsCode)) {
            goodsInfo = goodsCode;
        }

        // パラメータチェック
        checkParameter(shopSeq, goodsInfo);

        // 商品グループ情報取得
        GoodsGroupEntity goodsGroupEntity = getGoodsGroup(shopSeq, goodsGroupCode, goodsCode, siteType, openStatus,
                                                          frontDisplayReferenceDate
                                                         );
        if (goodsGroupEntity == null) {
            return null;
        }

        // 商品グループDTO作成
        GoodsGroupDto goodsGroupDto = makeGoodsGroupDto(goodsGroupEntity, siteType, frontDisplayReferenceDate);

        // 商品グループDTOに在庫状況情報を追加
        addStockStatusInfo(goodsGroupDto, shopSeq, frontDisplayReferenceDate);

        // 戻り値
        return goodsGroupDto;
    }

    /**
     * 商品グループDTOに在庫状況を追加
     *
     * @param goodsGroupDto             商品グループDTO
     * @param shopSeq                   ショップSEQ
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     */
    protected void addStockStatusInfo(GoodsGroupDto goodsGroupDto,
                                      Integer shopSeq,
                                      Timestamp frontDisplayReferenceDate) {

        // 在庫状態更新バッチ実行時点での商品グループ在庫状態を取得して設定
        StockStatusDisplayEntity batchUpdateStockStatus =
                        stockStatusDisplayDao.getEntity(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
        if (batchUpdateStockStatus == null) {
            // 前回の在庫状況更新バッチ実行後に登録された商品の在庫状況はまだ存在しない
            goodsGroupDto.setBatchUpdateStockStatus(getComponent(StockStatusDisplayEntity.class));
        } else {
            goodsGroupDto.setBatchUpdateStockStatus(batchUpdateStockStatus);
        }

        List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList();

        // 在庫状態テーブルから取得した在庫状態表示をリアルタイムの在庫状態表示で上書きします。
        // 在庫管理しない商品でも在庫及び在庫設定は存在する為、リアルタイム在庫状況が null ということはあり得ない。
        StockStatusDisplayEntity realTimeStockStatus = getComponent(StockStatusDisplayEntity.class);

        // 規格単位のリアルタイム在庫状況を取得
        Map<Integer, HTypeStockStatusType> stockStatusPcMap = goodsStockStatusGetLogic.execute(goodsDtoList, shopSeq);

        // 商品規格単位（商品DTO）にリアルタイム在庫状況を設定
        setGoodsRealTimeStockStatus(goodsDtoList, stockStatusPcMap, frontDisplayReferenceDate);

        // 商品グループ単位に変換しセット
        realTimeStockStatus.setStockStatusPc(goodsUtility.convertGoodsGroupStockStatus(goodsDtoList, stockStatusPcMap));

        // 商品グループのリアルタイム在庫状況を設定
        goodsGroupDto.setRealTimeStockStatus(realTimeStockStatus);

        // 商品グループのフロント表示在庫状況にフロント表示基準日時で判定したものを設定
        goodsGroupDto.setFrontDisplayStockStatus(goodsDtoList.get(0).getFrontDisplayStockStatus());
    }

    /**
     * 商品規格単位（商品DTO）に在庫状況を設定する
     *
     * @param goodsDtoList              商品DTOリスト
     * @param stockStatusPcMap          PC在庫状況MAP＜商品SEQ、在庫状況＞
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     */
    protected void setGoodsRealTimeStockStatus(List<GoodsDto> goodsDtoList,
                                               Map<Integer, HTypeStockStatusType> stockStatusPcMap,
                                               Timestamp frontDisplayReferenceDate) {
        for (GoodsDto dto : goodsDtoList) {
            if (stockStatusPcMap.containsKey(dto.getGoodsEntity().getGoodsSeq())) {
                dto.setStockStatusPc(stockStatusPcMap.get(dto.getGoodsEntity().getGoodsSeq()));

                // フロント表示基準日時が設定されている場合
                if (ObjectUtils.isNotEmpty(frontDisplayReferenceDate)) {
                    // フロント表示基準日時で判定したフロント表示在庫状況を設定（プレビュー画面表示用）
                    dto.setFrontDisplayStockStatus(judgeFrontDisplayStockStatusForPreview(dto.getStockDto(),
                                                                                          dto.getGoodsEntity()
                                                                                             .getStockManagementFlag(),
                                                                                          dto.getGoodsEntity()
                                                                                             .getSaleStatusPC(),
                                                                                          dto.getGoodsEntity()
                                                                                             .getSaleStartTimePC(),
                                                                                          dto.getGoodsEntity()
                                                                                             .getSaleEndTimePC(),
                                                                                          frontDisplayReferenceDate
                                                                                         ));
                }
            }
        }
    }

    /**
     * 対象日時と販売期間から在庫状態を判定し、対象商品のフロント表示在庫状況を返却
     *
     * @param stockDto            在庫Dto
     * @param stockManagementFlag 在庫管理フラグ
     * @param saleStatus          販売状態
     * @param saleStartTime       販売開始日時
     * @param saleEndTime         販売終了日時
     * @param targetTime          対象日時
     * @return 販売在庫状況
     */
    protected HTypeStockStatusType judgeFrontDisplayStockStatusForPreview(StockDto stockDto,
                                                                          HTypeStockManagementFlag stockManagementFlag,
                                                                          HTypeGoodsSaleStatus saleStatus,
                                                                          Timestamp saleStartTime,
                                                                          Timestamp saleEndTime,
                                                                          Timestamp targetTime) {
        HTypeStockStatusType currentStatus = HTypeStockStatusType.NO_SALE;
        if (HTypeGoodsSaleStatus.SALE.equals(saleStatus)) {
            // 販売中の場合、「残りわずか」「在庫あり」「在庫なし」「販売前」「販売期間終了」の判定を行う
            if (saleEndTime != null && targetTime.compareTo(saleEndTime) > 0) {
                // 販売期間終了の場合、「販売期間終了」
                currentStatus = HTypeStockStatusType.SOLDOUT;
            } else if (saleStartTime != null && targetTime.compareTo(saleStartTime) < 0) {
                // 販売開始前の場合、「販売前」
                currentStatus = HTypeStockStatusType.BEFORE_SALE;
            } else if (HTypeStockManagementFlag.ON.equals(stockManagementFlag)
                       && stockDto.getSalesPossibleStock() != null
                       && stockDto.getSalesPossibleStock().compareTo(BigDecimal.ZERO) <= 0) {
                // 在庫管理する、かつ販売可能在庫数 <= 0 の場合、「在庫なし」
                currentStatus = HTypeStockStatusType.STOCK_NOSTOCK;
            } else if (HTypeStockManagementFlag.OFF.equals(stockManagementFlag) || (
                            stockDto.getSalesPossibleStock() != null
                            && stockDto.getSalesPossibleStock().compareTo(stockDto.getRemainderFewStock()) > 0)) {
                // 在庫管理しない、または、販売可能在庫数 > 残少表示在庫数の場合、「在庫あり」
                currentStatus = HTypeStockStatusType.STOCK_POSSIBLE_SALES;
            } else if (stockDto.getSalesPossibleStock() != null
                       && stockDto.getSalesPossibleStock().compareTo(stockDto.getRemainderFewStock()) <= 0
                       && stockDto.getSalesPossibleStock().compareTo(BigDecimal.ZERO) > 0) {
                // 販売可能在庫数<= 残少表示在庫数で販売可能在庫数 > 0の場合、「残りわずか」
                currentStatus = HTypeStockStatusType.STOCK_FEW;
            } else {
                // 販売可能在庫数 <= 0 の場合、「在庫なし」
                currentStatus = HTypeStockStatusType.STOCK_NOSTOCK;
            }
        } else if (HTypeGoodsSaleStatus.NO_SALE.equals(saleStatus)) {
            // 非販売の場合、「非販売」
            currentStatus = HTypeStockStatusType.NO_SALE;
        } else {
            // 削除の場合、「非公開」
            currentStatus = HTypeStockStatusType.NO_OPEN;
        }
        return currentStatus;
    }

    /**
     * 商品グループDTO作成
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @param siteType サイトタイプ
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループDTO
     */
    protected GoodsGroupDto makeGoodsGroupDto(GoodsGroupEntity goodsGroupEntity,
                                              HTypeSiteType siteType,
                                              Timestamp frontDisplayReferenceDate) {

        Integer goodsGroupSeq = goodsGroupEntity.getGoodsGroupSeq();

        // 商品グループ画像情報取得
        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageListMap = getGoodsGroupImageListMap(goodsGroupSeq);

        // 商品グループ表示情報取得
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = getGoodsGroupDisplay(goodsGroupSeq);

        // 商品インフォメーションアイコンリスト取得
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList =
                        getGoodsInformationIconDetailsDtoList(siteType, goodsGroupDisplayEntity);

        // 商品情報リスト取得
        List<GoodsDto> goodsDtoList = getGoodsDtoList(siteType, goodsGroupSeq);

        // 商品グループDTOの編集
        GoodsGroupDto goodsGroupDto = createGoodsGroupDto(goodsGroupEntity, goodsGroupSeq, goodsGroupImageListMap,
                                                          goodsGroupDisplayEntity, goodsInformationIconDetailsDtoList,
                                                          goodsDtoList, frontDisplayReferenceDate
                                                         );

        return goodsGroupDto;
    }

    /**
     * 販売中かを判定
     * 販売以外または販売期間外は対象外
     *
     * @param goodsDto 商品DTO
     * @param siteType サイト種別
     * @return true：対象外
     */
    protected boolean judgNoSale(GoodsDto goodsDto, HTypeSiteType siteType) {

        // サイト種別で判定用項目を決定
        HTypeGoodsSaleStatus saleStatus = null;
        Timestamp startTime = null;
        Timestamp endTime = null;
        saleStatus = goodsDto.getGoodsEntity().getSaleStatusPC();
        startTime = goodsDto.getGoodsEntity().getSaleStartTimePC();
        endTime = goodsDto.getGoodsEntity().getSaleEndTimePC();

        // 販売以外は対象外
        if (!HTypeGoodsSaleStatus.SALE.equals(saleStatus)) {
            return true;
        }

        // 販売期間外は対象外
        if (!dateUtility.isOpen(startTime, endTime)) {
            return true;
        }
        return false;

    }

    /**
     * 入力パラメータチェック
     *
     * @param shopSeq ショップSEQ
     * @param goodsInfo 商品グループコード or 商品コード
     * @param customParams 案件用引数
     */
    protected void checkParameter(Integer shopSeq, String goodsInfo, Object... customParams) {
        // 商品グループコード or 商品コードが null でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("goodsGroupCode", goodsInfo);
    }

    /**
     * 商品グループ情報取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode 商品コード
     * @param siteType サイト区分
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時
     * @return 商品グループエンティティ
     */
    protected GoodsGroupEntity getGoodsGroup(Integer shopSeq,
                                             String goodsGroupCode,
                                             String goodsCode,
                                             HTypeSiteType siteType,
                                             HTypeOpenDeleteStatus openStatus,
                                             Timestamp frontDisplayReferenceDate) {
        // （パラメータ）ショップSEQ、（パラメータ）商品グループコードをもとに、商品グループエンティティを取得する
        // 商品グループDaoの商品グループ取得処理を実行する。
        return goodsGroupDao.getGoodsGroupByCode(
                        shopSeq, goodsGroupCode, goodsCode, siteType, openStatus, frontDisplayReferenceDate);
    }

    /**
     * 商品グループ画像情報取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品グループ画像マップ
     */
    protected Map<Integer, List<GoodsGroupImageEntity>> getGoodsGroupImageListMap(Integer goodsGroupSeq,
                                                                                  Object... customParams) {
        // (2)で取得した商品グループエンティティの商品グループエンティティ．商品グループSEQをもとに商品グループSEQリスト（1件）を作成する。
        // 作成した商品グループSEQリストをもとに、商品グループ画像取得Logicを利用して、商品グループ画像マップを取得する
        // Logic GoodsGroupImageGetLogic
        // パラメータ 商品グループSEQリスト
        // 戻り値 商品グループ画像マップ
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        goodsGroupSeqList.add(goodsGroupSeq);
        return goodsGroupImageGetLogic.execute(goodsGroupSeqList);
    }

    /**
     * 商品グループ表示情報取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品グループ表示エンティティ
     */
    protected GoodsGroupDisplayEntity getGoodsGroupDisplay(Integer goodsGroupSeq, Object... customParams) {
        // 商品グループ表示取得Logicを利用して、商品グループ表示情報を取得する
        // Logic GoodsGroupDisplayGetLogic
        // パラメータ 商品グループSEQ
        // 戻り値 商品グループ表示エンティティ
        return goodsGroupDisplayGetLogic.execute(goodsGroupSeq);
    }

    /**
     * 商品インフォメーションアイコンリスト取得
     *
     * @param siteType サイト区分
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param customParams 案件用引数
     * @return 商品インフォメーションアイコンリスト
     */
    public List<GoodsInformationIconDetailsDto> getGoodsInformationIconDetailsDtoList(HTypeSiteType siteType,
                                                                                      GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                                                                      Object... customParams) {
        // 商品グループ表示情報の商品インフォメーションアイコンを元に、商品インフォメーションアイコン取得Logicを利用して、
        // 商品インフォメーションアイコンエンティティリストを取得する。
        // Logic GoodsInformationIconGetLogic
        // パラメータ 商品グループ表示．インフォメーションアイコン
        // 戻り値 商品インフォメーションアイコンマップ
        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
        if (!"".equals(goodsGroupDisplayEntity.getInformationIconPC())) {
            String[] informationIconSeqArray = null;
            if ((siteType == HTypeSiteType.FRONT_PC) && goodsGroupDisplayEntity.getInformationIconPC() != null) {
                informationIconSeqArray = goodsGroupDisplayEntity.getInformationIconPC().split("/");
            } else if (siteType == HTypeSiteType.BACK) {
                // TreeSet
                Set<String> treeSet = new TreeSet<>();
                if (goodsGroupDisplayEntity.getInformationIconPC() != null && !"".equals(
                                goodsGroupDisplayEntity.getInformationIconPC())) {
                    treeSet.addAll(Arrays.asList(goodsGroupDisplayEntity.getInformationIconPC().split("/")));
                }
                informationIconSeqArray = treeSet.toArray(new String[] {});
            }
            if (informationIconSeqArray != null) {
                List<Integer> informationIconSeqList = new ArrayList<>();
                for (int i = 0; i < informationIconSeqArray.length; i++) {
                    if (!informationIconSeqArray[i].equals("")) {
                        informationIconSeqList.add(Integer.parseInt(informationIconSeqArray[i]));
                    }
                }
                if (informationIconSeqList.size() > 0) {
                    goodsInformationIconDetailsDtoList = goodsInformationIconGetLogic.execute(informationIconSeqList);

                }
            }
        }
        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品情報リスト取得
     *
     * @param siteType サイト区分
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品情報リスト
     */
    protected List<GoodsDto> getGoodsDtoList(HTypeSiteType siteType, Integer goodsGroupSeq, Object... customParams) {
        // 商品Dao用検索条件DTOを生成して、以下の項目を設定する
        // 項目名 設定値
        // 商品グループSEQ （(2)で取得した商品グループエンティティ）商品グループエンティティ．商品グループSEQ
        // 並び順 " order by goods.orderdisplay asc " （商品テーブル．並び順 の昇順）
        GoodsSearchForDaoConditionDto goodsSearchForDaoConditionDto = getComponent(GoodsSearchForDaoConditionDto.class);
        goodsSearchForDaoConditionDto.setGoodsGroupSeq(goodsGroupSeq);
        goodsSearchForDaoConditionDto.setOrderField("orderdisplay");
        goodsSearchForDaoConditionDto.setOrderAsc(true);
        if (!siteType.isBack()) {
            goodsSearchForDaoConditionDto.setSaleStatus(HTypeGoodsSaleStatus.SALE);
        }

        // 商品Dao用検索条件DTOをもとに、商品リスト取得Logicを利用して、商品DTOリストを取得する
        // Logic GoodsListGetLogic
        // パラメータ 商品Dao用検索条件DTO
        // 戻り値 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsListGetLogic.execute(goodsSearchForDaoConditionDto);
        return goodsDtoList;
    }

    /**
     * 商品グループDTOの編集
     *
     * @param goodsGroupEntity                   商品グループエンティティ
     * @param goodsGroupSeq                      商品グループSEQ
     * @param goodsGroupImageListMap             商品グループ画像マップ
     * @param goodsGroupDisplayEntity            商品グループ表示エンティティ
     * @param goodsInformationIconDetailsDtoList 商品インフォメーションアイコンリスト
     * @param goodsDtoList                       商品DTOリスト
     * @param frontDisplayReferenceDate          フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループDTO
     */
    protected GoodsGroupDto createGoodsGroupDto(GoodsGroupEntity goodsGroupEntity,
                                                Integer goodsGroupSeq,
                                                Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageListMap,
                                                GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                                List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList,
                                                List<GoodsDto> goodsDtoList,
                                                Timestamp frontDisplayReferenceDate) {
        GoodsGroupDto goodsGroupDto = getComponent(GoodsGroupDto.class);
        goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
        goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageListMap.get(goodsGroupSeq));
        goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
        goodsGroupDto.setGoodsDtoList(goodsDtoList);
        goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);
        // フロント表示状態を判定（公開状態と公開期間から判断）
        Timestamp openStartTime = goodsGroupDto.getGoodsGroupEntity().getOpenStartTimePC();
        Timestamp openEndTime = goodsGroupDto.getGoodsGroupEntity().getOpenEndTimePC();
        // フロント表示基準日時が未設定の場合、区分値の判定処理で現在日時に変換
        goodsGroupDto.setFrontDisplay(
                        HTypeFrontDisplayStatus.isDisplay(goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC(),
                                                          openStartTime, openEndTime, frontDisplayReferenceDate
                                                         ) ?
                                        HTypeFrontDisplayStatus.OPEN :
                                        HTypeFrontDisplayStatus.NO_OPEN);
        // 公開以外の場合はワーニングメッセージを設定
        if (HTypeFrontDisplayStatus.NO_OPEN.equals(goodsGroupDto.getFrontDisplay())) {
            // 公開状態であり期間外の場合
            if (HTypeOpenDeleteStatus.OPEN.equals(goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC())
                && ObjectUtils.isNotEmpty(openStartTime) || ObjectUtils.isNotEmpty(openEndTime)) {
                goodsGroupDto.setWarningMessage(AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_OUT_OF_TERM,
                                                                                       new Object[] {ObjectUtils.isEmpty(
                                                                                                       openStartTime) ?
                                                                                                       "" :
                                                                                                       openStartTime,
                                                                                                       ObjectUtils.isEmpty(
                                                                                                                       openEndTime) ?
                                                                                                                       "" :
                                                                                                                       openEndTime}
                                                                                      ).getMessage());
            } else {
                goodsGroupDto.setWarningMessage(
                                AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_NO_OPEN, null).getMessage());
            }
        }
        return goodsGroupDto;
    }

    /**
     * 非販売商品を除外
     *
     * @param siteType サイト種別
     * @param goodsGroupDto 商品グループDTO
     */
    protected void excludeNoSaleGoods(HTypeSiteType siteType, GoodsGroupDto goodsGroupDto) {
        Iterator<GoodsDto> it = goodsGroupDto.getGoodsDtoList().iterator();
        while (it.hasNext()) {
            GoodsDto goodsDto = it.next();
            if (HTypeSiteType.FRONT_PC.equals(siteType)) {
                if (!goodsUtility.isGoodsSalesPc(goodsDto.getGoodsEntity())) {
                    it.remove();
                }
            }
        }
    }

}