/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsInformationIconDao;
import jp.co.itechh.quad.core.dto.common.FileRegistDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupRegistUpdateResultDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupPopularityEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.stock.StockEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsRegistLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryLockLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryTableLockLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsOrderDisplayCheckLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsRegistLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplaySyncLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagRegistUpdateLogic;
import jp.co.itechh.quad.core.logic.goods.goods.NewGoodsSeqGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDataCheckLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayRegistLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayUpdateLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageRegistDataOnlyLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageRegistLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupLockLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupPopularityRegistLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupRegistLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupTableLockLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupUpdateLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.NewGoodsGroupSeqGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.impl.GoodsGroupDetailsGetByCodeLogicImpl;
import jp.co.itechh.quad.core.logic.goods.relation.GoodsRelationRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupRegistUpdateService;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.GoodsStockInfo;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import jp.co.itechh.quad.stock.presentation.api.param.StockRegistUpdateRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.MapUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 商品グループ登録更新サービス実装クラス
 *
 * @author kimura
 */
@Service
public class GoodsGroupRegistUpdateServiceImpl extends AbstractShopService implements GoodsGroupRegistUpdateService {

    /**
     * 全角スペース
     * <code>EM_SPACE</code>
     */
    protected static final String EM_SPACE = "　";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsGroupRegistUpdateServiceImpl.class);

    /** 商品グループデータチェックLogic */
    private final GoodsGroupDataCheckLogic goodsGroupDataCheckLogic;

    /** 商品グループテーブルロックLogic */
    private final GoodsGroupTableLockLogic goodsGroupTableLockLogic;

    /** カテゴリテーブルロックLogic */
    private final CategoryTableLockLogic categoryTableLockLogic;

    /** 商品グループレコードロックLogic */
    private final GoodsGroupLockLogic goodsGroupLockLogic;

    /** カテゴリレコードロックLogic */
    private final CategoryLockLogic categoryLockLogic;

    /** 商品グループSEQ採番Logic */
    private final NewGoodsGroupSeqGetLogic newGoodsGroupSeqGetLogic;

    /** 商品グループ登録Logic */
    private final GoodsGroupRegistLogic goodsGroupRegistLogic;

    /** 商品グループ表示登録Logic */
    private final GoodsGroupDisplayRegistLogic goodsGroupDisplayRegistLogic;

    /** 商品グループ人気登録Logic */
    private final GoodsGroupPopularityRegistLogic goodsGroupPopularityRegistLogic;

    /** 商品グループ更新Logic */
    private final GoodsGroupUpdateLogic goodsGroupUpdateLogic;

    /** 商品グループ表示更新Logic */
    private final GoodsGroupDisplayUpdateLogic goodsGroupDisplayUpdateLogic;

    /** カテゴリ登録商品登録Logic */
    private final CategoryGoodsRegistLogic categoryGoodsRegistLogic;

    /** 関連商品登録Logic */
    private final GoodsRelationRegistLogic goodsRelationRegistLogic;

    /** 商品グループ画像登録Logic */
    private final GoodsGroupImageRegistLogic goodsGroupImageRegistLogic;

    /** 商品グループ画像登録(データのみ、画像ファイル操作は行わない CSV用)Logic */
    private final GoodsGroupImageRegistDataOnlyLogic goodsGroupImageRegistDataOnlyLogic;

    /** 商品SEQ採番Logic */
    private final NewGoodsSeqGetLogic newGoodsSeqGetLogic;

    /** 商品登録Logic */
    private final GoodsRegistLogic goodsRegistLogic;

    /*** 商品タグ登録Logic */
    private final GoodsTagRegistUpdateLogic goodsTagRegistUpdateLogic;

    /** 商品グループ詳細取得Logic */
    private final GoodsGroupDetailsGetByCodeLogicImpl goodsGroupDetailsGetByCodeLogic;

    /** 商品規格表示順チェックロジック */
    private final GoodsOrderDisplayCheckLogic goodsOrderDisplayCheckLogic;

    /** FileOperationUtility */
    private final FileOperationUtility fileOperationUtility;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 商品インフォメーションアイコンDao */
    private final GoodsInformationIconDao goodsInformationIconDao;

    /** 在庫API */
    private final StockApi stockApi;

    /** 商品在庫表示非同期ロジック */
    private final GoodsStockDisplaySyncLogic goodsStockDisplaySyncLogic;

    /** コンストラクタ */
    @Autowired
    public GoodsGroupRegistUpdateServiceImpl(GoodsGroupDataCheckLogic goodsGroupDataCheckLogic,
                                             GoodsGroupTableLockLogic goodsGroupTableLockLogic,
                                             CategoryTableLockLogic categoryTableLockLogic,
                                             GoodsGroupLockLogic goodsGroupLockLogic,
                                             CategoryLockLogic categoryLockLogic,
                                             NewGoodsGroupSeqGetLogic newGoodsGroupSeqGetLogic,
                                             GoodsGroupRegistLogic goodsGroupRegistLogic,
                                             GoodsGroupDisplayRegistLogic goodsGroupDisplayRegistLogic,
                                             GoodsGroupPopularityRegistLogic goodsGroupPopularityRegistLogic,
                                             GoodsGroupUpdateLogic goodsGroupUpdateLogic,
                                             GoodsGroupDisplayUpdateLogic goodsGroupDisplayUpdateLogic,
                                             CategoryGoodsRegistLogic categoryGoodsRegistLogic,
                                             GoodsRelationRegistLogic goodsRelationRegistLogic,
                                             GoodsGroupImageRegistLogic goodsGroupImageRegistLogic,
                                             GoodsGroupImageRegistDataOnlyLogic goodsGroupImageRegistDataOnlyLogic,
                                             NewGoodsSeqGetLogic newGoodsSeqGetLogic,
                                             GoodsRegistLogic goodsRegistLogic,
                                             GoodsTagRegistUpdateLogic goodsTagRegistUpdateLogic,
                                             GoodsGroupDetailsGetByCodeLogicImpl goodsGroupDetailsGetByCodeLogic,
                                             GoodsOrderDisplayCheckLogic goodsOrderDisplayCheckLogic,
                                             FileOperationUtility fileOperationUtility,
                                             DateUtility dateUtility,
                                             ConversionUtility conversionUtility,
                                             HeaderParamsUtility headerParamsUtil,
                                             GoodsInformationIconDao goodsInformationIconDao,
                                             StockApi stockApi,
                                             GoodsStockDisplaySyncLogic goodsStockDisplaySyncLogic) {
        this.goodsGroupDataCheckLogic = goodsGroupDataCheckLogic;
        this.goodsGroupTableLockLogic = goodsGroupTableLockLogic;
        this.categoryTableLockLogic = categoryTableLockLogic;
        this.goodsGroupLockLogic = goodsGroupLockLogic;
        this.categoryLockLogic = categoryLockLogic;
        this.newGoodsGroupSeqGetLogic = newGoodsGroupSeqGetLogic;
        this.goodsGroupRegistLogic = goodsGroupRegistLogic;
        this.goodsGroupDisplayRegistLogic = goodsGroupDisplayRegistLogic;
        this.goodsGroupPopularityRegistLogic = goodsGroupPopularityRegistLogic;
        this.goodsGroupUpdateLogic = goodsGroupUpdateLogic;
        this.goodsGroupDisplayUpdateLogic = goodsGroupDisplayUpdateLogic;
        this.categoryGoodsRegistLogic = categoryGoodsRegistLogic;
        this.goodsRelationRegistLogic = goodsRelationRegistLogic;
        this.goodsGroupImageRegistLogic = goodsGroupImageRegistLogic;
        this.goodsGroupImageRegistDataOnlyLogic = goodsGroupImageRegistDataOnlyLogic;
        this.newGoodsSeqGetLogic = newGoodsSeqGetLogic;
        this.goodsRegistLogic = goodsRegistLogic;
        this.goodsTagRegistUpdateLogic = goodsTagRegistUpdateLogic;
        this.goodsGroupDetailsGetByCodeLogic = goodsGroupDetailsGetByCodeLogic;
        this.goodsOrderDisplayCheckLogic = goodsOrderDisplayCheckLogic;
        this.fileOperationUtility = fileOperationUtility;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
        this.headerParamsUtil = headerParamsUtil;
        this.goodsInformationIconDao = goodsInformationIconDao;
        this.stockApi = stockApi;
        this.goodsStockDisplaySyncLogic = goodsStockDisplaySyncLogic;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        this.headerParamsUtil.setHeader(this.stockApi.getApiClient());
    }

    /**
     * CSVアップロード用の実行メソッド（トランザクション指定）
     *
     * @param administratorSeq        管理者SEQ
     * @param goodsGroupDto           商品グループDto
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param processType             処理種別（画面/CSV）
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void executeForCsv(Integer administratorSeq,
                              GoodsGroupDto goodsGroupDto,
                              List<? extends GoodsRelationEntity> goodsRelationEntityList,
                              int processType,
                              List<Integer> goodsGroupSeqSuccessList) throws Exception {
        // エラーリストをクリア（呼び出し元のループ処理で本サービスのエラーリストからメッセージを組み立てる際に、前のレコードのエラー情報を保持させないため）
        clearErrorList();
        Map<String, Object> retMap = execute(administratorSeq, goodsGroupDto, goodsRelationEntityList, null,
                                             GoodsGroupRegistUpdateService.PROCESS_TYPE_FROM_CSV
                                            );

        if (!MapUtils.isEmpty(retMap) && (retMap.get(GoodsGroupRegistUpdateService.WARNING_MESSAGE) == null
                                          || "".equals(retMap.get(GoodsGroupRegistUpdateService.WARNING_MESSAGE)))) {
            goodsGroupSeqSuccessList.add((Integer) retMap.get(GOODS_GROUP_SEQ));
        }

    }

    /**
     * 実行メソッド
     *
     * @param administratorSeq                        管理者SEQ
     * @param inputGoodsGroupDto                      商品グループDto
     * @param inputGoodsRelationEntityList            関連商品エンティティリスト
     * @param inputGoodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト（処理種別=CSV時はnull）
     * @param processType                             処理種別（画面/CSV）
     * @return 処理件数マップ
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> execute(Integer administratorSeq,
                                       GoodsGroupDto inputGoodsGroupDto,
                                       List<? extends GoodsRelationEntity> inputGoodsRelationEntityList,
                                       List<GoodsGroupImageRegistUpdateDto> inputGoodsGroupImageRegistUpdateDtoList,
                                       int processType) throws Exception {

        Integer shopSeq = 1001;

        // 使用するパラメータをディープコピーする ※エラーで戻ったときのために入力状態を保持する
        GoodsGroupDto goodsGroupDto = new GoodsGroupDto();
        BeanUtils.copyProperties(goodsGroupDto, inputGoodsGroupDto);

        // リストのディープコピーを行う
        List<GoodsRelationEntity> goodsRelationEntityList = new ArrayList<>();
        for (int i = 0; inputGoodsRelationEntityList != null && inputGoodsRelationEntityList.size() > i; i++) {
            goodsRelationEntityList.add(CopyUtil.deepCopy(inputGoodsRelationEntityList.get(i)));
        }
        List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList = new ArrayList<>();
        for (int i = 0; inputGoodsGroupImageRegistUpdateDtoList != null
                        && inputGoodsGroupImageRegistUpdateDtoList.size() > i; i++) {
            goodsGroupImageRegistUpdateDtoList.add(CopyUtil.deepCopy(inputGoodsGroupImageRegistUpdateDtoList.get(i)));
        }

        /** 共通処理結果マップ領域 */
        Map<String, Integer> resultMap = null;
        // 処理件数保管Dto
        GoodsGroupRegistUpdateResultDto resultDto =
                        ApplicationContextUtility.getBean(GoodsGroupRegistUpdateResultDto.class);

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupDto", goodsGroupDto);

        // テーブル＆レコードロック処理
        executeTableLock(goodsGroupDto);

        // 商品グループ＋商品データチェックLogic処理
        goodsGroupDataCheckLogic.execute(goodsGroupDto, goodsRelationEntityList, shopSeq);

        // 商品グループ検索キーワード全角の編集
        resultDto.setWarningMessage(editSearchKeywordEm(goodsGroupDto, resultDto.getWarningMessage()));

        // 商品検索設定キーワード全角大文字
        resultDto.setWarningMessage(editSearchSettingKeywordsEmUc(goodsGroupDto, resultDto.getWarningMessage()));

        boolean isRegist = goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq() == null;

        if (isRegist) {
            /********************************************************
             **  商品グループ登録用処理
             ********************************************************/
            // 商品グループSEQ採番＋ショップSEQ設定Logic処理
            GoodsGroupPopularityEntity goodsGroupPopularityEntity =
                            createRegistGoodsGroupData(processType, goodsGroupDto, goodsRelationEntityList,
                                                       goodsGroupImageRegistUpdateDtoList, shopSeq
                                                      );

            // 商品グループ登録Logic処理
            resultDto.setResultGoodsGroupRegist(goodsGroupRegistLogic.execute(goodsGroupDto.getGoodsGroupEntity()));

            // 商品グループ表示登録Logic処理
            resultDto.setResultGoodsGroupDisplayRegist(
                            goodsGroupDisplayRegistLogic.execute(goodsGroupDto.getGoodsGroupDisplayEntity()));

            // 商品グループ人気登録Logic処理
            resultDto.setResultGoodsGroupPopularityRegist(
                            goodsGroupPopularityRegistLogic.execute(goodsGroupPopularityEntity));

            // 商品タグ更新Logic処理
            goodsTagRegistUpdateLogic.execute(goodsGroupDto.getGoodsGroupDisplayEntity());

        } else {
            /********************************************************
             **  商品グループ更新用処理
             ********************************************************/
            createUpdateGoodsGroupData(processType, goodsGroupDto, goodsGroupImageRegistUpdateDtoList);

            // 商品グループ更新Logic処理
            resultDto.setResultGoodsGroupUpdate(goodsGroupUpdateLogic.execute(goodsGroupDto.getGoodsGroupEntity()));

            // 商品タグ更新Logic処理
            goodsTagRegistUpdateLogic.execute(goodsGroupDto.getGoodsGroupDisplayEntity());

            // 商品グループ表示更新Logic処理
            resultDto.setResultGoodsGroupDisplayUpdate(
                            goodsGroupDisplayUpdateLogic.execute(goodsGroupDto.getGoodsGroupDisplayEntity()));
        }

        // カテゴリ登録商品登録Logic処理
        if (goodsGroupDto.getCategoryGoodsEntityList() == null) {
            goodsGroupDto.setCategoryGoodsEntityList(new ArrayList<>());
        }
        resultMap = categoryGoodsRegistLogic.execute(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(),
                                                     goodsGroupDto.getCategoryGoodsEntityList()
                                                    );
        resultDto.setResultCategoryGoodsRegist(resultMap.get(CategoryGoodsRegistLogic.CATEGORY_GOODS_REGIST));
        resultDto.setResultCategoryGoodsDelete(resultMap.get(CategoryGoodsRegistLogic.CATEGORY_GOODS_DELETE));

        // 関連商品登録Logic処理
        resultMap = goodsRelationRegistLogic.execute(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(),
                                                     goodsRelationEntityList
                                                    );
        resultDto.setResultGoodsRelationRegist(resultMap.get(GoodsRelationRegistLogic.GOODS_RELATION_REGIST));
        resultDto.setResultGoodsRelationUpdate(resultMap.get(GoodsRelationRegistLogic.GOODS_RELATION_UPDATE));
        resultDto.setResultGoodsRelationDelete(resultMap.get(GoodsRelationRegistLogic.GOODS_RELATION_DELETE));

        // 商品グループ画像登録Logic処理
        if (processType == PROCESS_TYPE_FROM_SCREEN) {
            // 画面からの場合（画像ファイルも含めて登録更新用Logic）
            Map<String, Object> resultMap2 =
                            goodsGroupImageRegistLogic.execute(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(),
                                                               goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode(),
                                                               goodsGroupImageRegistUpdateDtoList
                                                              );
            resultDto.setResultGoodsGroupImageRegist(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_REGIST));
            resultDto.setResultGoodsGroupImageUpdate(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_UPDATE));
            resultDto.setResultGoodsGroupImageDelete(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_DELETE));
            resultDto.setDeleteImageFilePathList(
                            (List<String>) resultMap2.get(GoodsGroupImageRegistLogic.DELETE_IMAGE_FILE_PATH_LIST));
            resultDto.setRegistImageFilePathList((List<FileRegistDto>) resultMap2.get(
                            GoodsGroupImageRegistLogic.REGIST_IMAGE_FILE_PATH_LIST));
        } else if (processType == PROCESS_TYPE_FROM_CSV) {
            // 画面からの場合（画像ファイルを含めない登録更新用Logic）
            Map<String, Object> resultMap2 = goodsGroupImageRegistDataOnlyLogic.execute(
                            goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(),
                            goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode(),
                            goodsGroupDto.getGoodsGroupImageEntityList()
                                                                                       );
            resultDto.setResultGoodsGroupImageRegist(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_REGIST));
            resultDto.setResultGoodsGroupImageUpdate(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_UPDATE));
            resultDto.setResultGoodsGroupImageDelete(
                            (Integer) resultMap2.get(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_DELETE));
            resultDto.setDeleteImageFilePathList(
                            (List<String>) resultMap2.get(GoodsGroupImageRegistLogic.DELETE_IMAGE_FILE_PATH_LIST));
        }

        // 商品Dtoリストから(登録更新用)商品エンティティリストを作成
        List<GoodsEntity> goodsEntityList = new ArrayList<>();
        // 商品Dtoリストから(登録用)在庫エンティティリストを作成
        List<StockEntity> stockEntityList = new ArrayList<>();
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            // 商品・在庫設定・在庫登録更新用リスト編集
            createRegistGoodsData(shopSeq, goodsEntityList, stockEntityList, goodsDto);
        }

        // 商品SEQ採番Logic処理
        if (goodsEntityList.size() > 0) {
            resultMap = goodsRegistLogic.execute(administratorSeq,
                                                 goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(), goodsEntityList
                                                );
            resultDto.setResultGoodsRegist(resultMap.get(GoodsRegistLogic.GOODS_REGIST));
            resultDto.setResultGoodsUpdate(resultMap.get(GoodsRegistLogic.GOODS_UPDATE));
        }

        // 画像ファイル削除
        String errorDeleteFilePath = "";
        for (String deleteFilePath : resultDto.getDeleteImageFilePathList()) {
            errorDeleteFilePath = deleteFilePath;
            boolean ret = false;
            try {
                ret = fileOperationUtility.remove(deleteFilePath);
            } catch (IOException e) {
                LOGGER.warn("商品画像の削除に失敗しました。" + errorDeleteFilePath, e);
            }
            if (ret) {
                resultDto.setResultGoodsGroupImageFileDelete(resultDto.getResultGoodsGroupImageFileDelete() + 1);
            }
        }

        // 画像ファイル登録（一時領域からフロント公開領域へ）
        if (processType == PROCESS_TYPE_FROM_SCREEN) {
            resultDto.setResultGoodsGroupImageFileRegist(
                            registGoodsGroupImageFile(resultDto.getRegistImageFilePathList(),
                                                      resultDto.getResultGoodsGroupImageFileRegist()
                                                     ));
        }

        // 商品規格表示順チェック ※「処理種別=CSV」の場合のみ実行
        if (processType == PROCESS_TYPE_FROM_CSV) {
            goodsOrderDisplayCheckLogic.execute(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq(),
                                                goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode()
                                               );
        }

        // 物流サービスAPIを呼び出し、在庫情報を登録更新
        callStockRegistUpdate(goodsGroupDto, processType);

        //  処理件数マップの作成
        return createReturnMap(resultDto, goodsGroupDto);
    }

    /**
     * 各Logicの処理件数マップの作成<br/>
     *
     * @param resultDto     結果Dto
     * @param goodsGroupDto 商品グループDto
     * @return Map&lt;String, Object&gt;
     */
    protected Map<String, Object> createReturnMap(GoodsGroupRegistUpdateResultDto resultDto,
                                                  GoodsGroupDto goodsGroupDto) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put(GOODS_GROUP_SEQ, goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
        returnMap.put(GOODS_GROUP_REGIST, resultDto.getResultGoodsGroupRegist());
        returnMap.put(GOODS_GROUP_DISPLAY_REGIST, resultDto.getResultGoodsGroupDisplayRegist());
        returnMap.put(GOODS_GROUP_POPULARITY_REGIST, resultDto.getResultGoodsGroupPopularityRegist());
        returnMap.put(GOODS_GROUP_UPDATE, resultDto.getResultGoodsGroupUpdate());
        returnMap.put(GOODS_GROUP_DISPLAY_UPDATE, resultDto.getResultGoodsGroupDisplayUpdate());
        returnMap.put(CategoryGoodsRegistLogic.CATEGORY_GOODS_REGIST, resultDto.getResultCategoryGoodsRegist());
        returnMap.put(CategoryGoodsRegistLogic.CATEGORY_GOODS_DELETE, resultDto.getResultCategoryGoodsDelete());
        returnMap.put(GoodsRelationRegistLogic.GOODS_RELATION_REGIST, resultDto.getResultGoodsRelationRegist());
        returnMap.put(GoodsRelationRegistLogic.GOODS_RELATION_UPDATE, resultDto.getResultGoodsRelationUpdate());
        returnMap.put(GoodsRelationRegistLogic.GOODS_RELATION_DELETE, resultDto.getResultGoodsRelationDelete());
        returnMap.put(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_REGIST, resultDto.getResultGoodsGroupImageRegist());
        returnMap.put(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_UPDATE, resultDto.getResultGoodsGroupImageUpdate());
        returnMap.put(GoodsGroupImageRegistLogic.GOODS_GROUP_IMAGE_DELETE, resultDto.getResultGoodsGroupImageDelete());
        returnMap.put(GoodsRegistLogic.GOODS_REGIST, resultDto.getResultGoodsRegist());
        returnMap.put(GoodsRegistLogic.GOODS_UPDATE, resultDto.getResultGoodsUpdate());
        returnMap.put(GOODS_GROUP_IMAGE_FILE_REGIST, resultDto.getResultGoodsGroupImageFileRegist());
        returnMap.put(GOODS_GROUP_IMAGE_FILE_DELETE, resultDto.getResultGoodsGroupImageFileDelete());
        returnMap.put(WARNING_MESSAGE, resultDto.getWarningMessage());

        return returnMap;
    }

    /**
     * テーブル＆レコードロック処理
     *
     * @param goodsGroupDto 商品グループDTO
     * @param customParams  案件用引数
     */
    protected void executeTableLock(GoodsGroupDto goodsGroupDto, Object... customParams) {
        // 商品グループテーブルロックLogic処理
        goodsGroupTableLockLogic.execute();

        // カテゴリテーブルロックLogic処理
        categoryTableLockLogic.execute();

        // 商品グループレコードロックLogic処理
        if (goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq() != null) {
            List<Integer> goodsGroupSeqList = new ArrayList<>();
            goodsGroupSeqList.add(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
            goodsGroupLockLogic.execute(goodsGroupSeqList, goodsGroupDto.getGoodsGroupEntity().getVersionNo());
        }

        // カテゴリレコードロックLogic処理
        if (goodsGroupDto.getCategoryGoodsEntityList() != null
            && goodsGroupDto.getCategoryGoodsEntityList().size() > 0) {
            List<Integer> categorySeqList = new ArrayList<>();
            for (CategoryGoodsEntity categoryGoodsEntity : goodsGroupDto.getCategoryGoodsEntityList()) {
                categorySeqList.add(categoryGoodsEntity.getCategorySeq());
            }
            categoryLockLogic.execute(categorySeqList);
        }
    }

    /**
     * 商品グループ検索キーワード全角の編集
     *
     * @param goodsGroupDto  商品グループDTO
     * @param warningMessage 警告メッセージ
     * @param customParams   案件用引数
     * @return 警告メッセージ
     */
    protected String editSearchKeywordEm(GoodsGroupDto goodsGroupDto, String warningMessage, Object... customParams) {
        String searchKeywordEm = "";

        // 商品検索キーワード
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getSearchKeyword() != null) {
            searchKeywordEm += goodsGroupDto.getGoodsGroupDisplayEntity().getSearchKeyword();
        }

        // 商品名
        if (goodsGroupDto.getGoodsGroupEntity().getGoodsGroupName() != null) {
            if (StringUtils.isNotEmpty(searchKeywordEm)) {
                searchKeywordEm += EM_SPACE;
            }
            searchKeywordEm += goodsGroupDto.getGoodsGroupEntity().getGoodsGroupName();
        }

        // 商品管理番号
        if (goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode() != null) {
            if (StringUtils.isNotEmpty(searchKeywordEm)) {
                searchKeywordEm += EM_SPACE;
            }
            searchKeywordEm += goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode();
        }

        // 規格１表示名・規格２表示名
        if (HTypeUnitManagementFlag.ON.equals(goodsGroupDto.getGoodsGroupDisplayEntity().getUnitManagementFlag())) {
            // 規格値１、規格値２の重複を省略して設定
            Set<String> unitValueSet = new TreeSet<>();
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                // 販売状態が「販売中」なら規格を追加する
                if (!HTypeGoodsSaleStatus.SALE.equals(goodsDto.getGoodsEntity().getSaleStatusPC())) {
                    continue;
                }
                if (goodsDto.getGoodsEntity().getUnitValue1() != null) {
                    unitValueSet.add(goodsDto.getGoodsEntity().getUnitValue1());
                }
                if (goodsDto.getGoodsEntity().getUnitValue2() != null) {
                    unitValueSet.add(goodsDto.getGoodsEntity().getUnitValue2());
                }
            }
            String[] unitValueList = unitValueSet.toArray(new String[] {});
            for (String unitValue : unitValueList) {
                if (StringUtils.isNotEmpty(searchKeywordEm)) {
                    searchKeywordEm += EM_SPACE;
                }
                searchKeywordEm += unitValue;
            }
        }

        // 商品タグ
        if (goodsGroupDto.getGoodsDtoList() != null) {
            Set<String> tagValueSet = new TreeSet<>();
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                // 販売状態が「販売中」なら商品タグを追加する
                if (!HTypeGoodsSaleStatus.SALE.equals(goodsDto.getGoodsEntity().getSaleStatusPC())) {
                    continue;
                }
                if (!CollectionUtils.isEmpty(goodsDto.getGoodsTag())) {
                    for (String str : goodsDto.getGoodsTag()) {
                        tagValueSet.add(str);
                    }
                }
            }
            String[] tagValueList = tagValueSet.toArray(new String[] {});
            for (String tagValue : tagValueList) {
                if (StringUtils.isNotEmpty(searchKeywordEm)) {
                    searchKeywordEm += EM_SPACE;
                }
                searchKeywordEm += tagValue;
            }
        }

        // アイコン名
        if (goodsGroupDto.getGoodsGroupDisplayEntity() != null
            && goodsGroupDto.getGoodsGroupDisplayEntity().getInformationIconPC() != null) {
            String[] informationIconPCArray =
                            goodsGroupDto.getGoodsGroupDisplayEntity().getInformationIconPC().split("/");
            List<Integer> informationIconPCList = new ArrayList<>();

            for (int i = 0; i < informationIconPCArray.length; i++) {
                informationIconPCList.add(Integer.valueOf(informationIconPCArray[i]));
            }

            List<GoodsInformationIconEntity> goodsInformationIconEntities =
                            goodsInformationIconDao.getEntityListBySeqList(informationIconPCList);

            if (!ListUtils.isEmpty(goodsInformationIconEntities)) {
                for (GoodsInformationIconEntity goodsInformationIconEntity : goodsInformationIconEntities) {
                    if (StringUtils.isNotEmpty(searchKeywordEm)) {
                        searchKeywordEm += EM_SPACE;
                    }
                    searchKeywordEm += goodsInformationIconEntity.getIconName();
                }
            }
        }

        // 全角変換
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        searchKeywordEm = zenHanConversionUtility.toZenkaku(searchKeywordEm);
        // 大文字変換
        searchKeywordEm = StringUtils.upperCase(searchKeywordEm);
        if (searchKeywordEm.length() > 81710) {
            searchKeywordEm = searchKeywordEm.substring(0, 81710);
            // ワーニングメッセージを追加
            if (StringUtils.isNotEmpty(warningMessage)) {
                warningMessage += ",";
            }
            warningMessage += "AGG001002";
            LOGGER.warn("商品検索キーワード全角が81710文字を超えたため81710文字で切り捨てて登録しました。");
        }
        goodsGroupDto.getGoodsGroupDisplayEntity().setSearchKeywordEmUc(searchKeywordEm);
        return warningMessage;
    }

    /**
     * 検索用商品設定キーワード全角の編集
     *
     * @param goodsGroupDto  商品グループDTO
     * @param warningMessage 警告メッセージ
     * @param customParams   案件用引数
     * @return 警告メッセージ
     */
    protected String editSearchSettingKeywordsEmUc(GoodsGroupDto goodsGroupDto,
                                                   String warningMessage,
                                                   Object... customParams) {
        String searchSettingKeywordsEmUc = "";

        // 商品納期
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getDeliveryType() != null) {
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getDeliveryType();
        }

        // 商品説明01
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote1() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote1();
        }

        // 商品説明02
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote2() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote2();
        }

        // 商品説明03
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote3() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote3();
        }

        // 商品説明04
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote4() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote4();
        }

        // 商品説明05
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote5() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote5();
        }

        // 商品説明06
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote6() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote6();
        }

        // 商品説明07
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote7() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote7();
        }

        // 商品説明08
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote8() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote8();
        }

        // 商品説明09
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote9() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote9();
        }

        // 商品説明10
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote10() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getGoodsNote10();
        }

        // 受注連携設定01
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting1() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting1();
        }

        // 受注連携設定02
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting2() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting2();
        }

        // 受注連携設定03
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting3() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting3();
        }

        // 受注連携設定04
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting4() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting4();
        }

        // 受注連携設定05
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting5() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting5();
        }

        // 受注連携設定06
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting6() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting6();
        }

        // 受注連携設定07
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting7() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting7();
        }

        // 受注連携設定08
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting8() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting8();
        }

        // 受注連携設定09
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting9() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting9();
        }

        // 受注連携設定10
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting10() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getOrderSetting10();
        }

        // 商品検索キーワード
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getSearchKeyword() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getSearchKeyword();
        }

        // メタ説明文
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getMetaDescription() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getMetaDescription();
        }

        // メタキーワード
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getMetaKeyword() != null) {
            if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                searchSettingKeywordsEmUc += EM_SPACE;
            }
            searchSettingKeywordsEmUc += goodsGroupDto.getGoodsGroupDisplayEntity().getMetaKeyword();
        }

        // アイコン名
        if (goodsGroupDto.getGoodsGroupDisplayEntity().getInformationIconPC() != null) {
            if (CollectionUtils.isNotEmpty(goodsGroupDto.getGoodsInformationIconDetailsDtoList())) {
                for (GoodsInformationIconDetailsDto iconDetailsDto : goodsGroupDto.getGoodsInformationIconDetailsDtoList()) {
                    if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                        searchSettingKeywordsEmUc += EM_SPACE;
                    }
                    searchSettingKeywordsEmUc += iconDetailsDto.getIconName();
                }
            } else {
                List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList =
                                goodsGroupDetailsGetByCodeLogic.getGoodsInformationIconDetailsDtoList(
                                                HTypeSiteType.BACK, goodsGroupDto.getGoodsGroupDisplayEntity());
                for (GoodsInformationIconDetailsDto iconDetailsDto : goodsInformationIconDetailsDtoList) {
                    if (StringUtils.isNotEmpty(searchSettingKeywordsEmUc)) {
                        searchSettingKeywordsEmUc += EM_SPACE;
                    }
                    searchSettingKeywordsEmUc += iconDetailsDto.getIconName();
                }
            }
        }

        // 全角変換
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        searchSettingKeywordsEmUc = zenHanConversionUtility.toZenkaku(searchSettingKeywordsEmUc);
        // 大文字変換
        searchSettingKeywordsEmUc = StringUtils.upperCase(searchSettingKeywordsEmUc);
        if (searchSettingKeywordsEmUc.length() > 100000) {
            searchSettingKeywordsEmUc = searchSettingKeywordsEmUc.substring(0, 100000);
            // ワーニングメッセージを追加
            if (StringUtils.isNotEmpty(warningMessage)) {
                warningMessage += ",";
            }
            warningMessage += "AGG001002";
            LOGGER.warn("検索用商品設定キーワード全角が100000文字を超えたため100000文字で切り捨てて登録しました。");
        }
        goodsGroupDto.getGoodsGroupDisplayEntity().setSearchSettingKeywordsEmUc(searchSettingKeywordsEmUc);
        return warningMessage;
    }

    /**
     * 商品グループ登録データ作成
     *
     * @param processType                        処理種別（画面/CSV）
     * @param goodsGroupDto                      商品グループDTO
     * @param goodsRelationEntityList            関連商品エンティティリスト
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト
     * @param shopSeq                            ショップSEQ
     * @param customParams                       案件用引数
     * @return 商品グループ人気エンティティ
     */
    protected GoodsGroupPopularityEntity createRegistGoodsGroupData(int processType,
                                                                    GoodsGroupDto goodsGroupDto,
                                                                    List<GoodsRelationEntity> goodsRelationEntityList,
                                                                    List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList,
                                                                    Integer shopSeq,
                                                                    Object... customParams) {
        // 商品グループSEQ採番Logic処理
        Integer goodsGroupSeq = newGoodsGroupSeqGetLogic.execute();
        // 採番した商品グループSEQを各データにセット
        // 商品グループ
        goodsGroupDto.getGoodsGroupEntity().setGoodsGroupSeq(goodsGroupSeq);
        // 商品グループ表示
        goodsGroupDto.getGoodsGroupDisplayEntity().setGoodsGroupSeq(goodsGroupSeq);
        // カテゴリ登録商品
        if (goodsGroupDto.getCategoryGoodsEntityList() != null) {
            for (CategoryGoodsEntity categoryGoodsEntity : goodsGroupDto.getCategoryGoodsEntityList()) {
                categoryGoodsEntity.setGoodsGroupSeq(goodsGroupSeq);
            }
        } else {
            goodsGroupDto.setCategoryGoodsEntityList(new ArrayList<>());
        }
        // 商品グループ画像
        if (processType == PROCESS_TYPE_FROM_SCREEN) {
            for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : goodsGroupImageRegistUpdateDtoList) {
                goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity().setGoodsGroupSeq(goodsGroupSeq);
                // ファイル名に商品管理番号のディレクトリ名が付いていない場合に付ける(登録時にはつけることになる)
                if (goodsGroupImageRegistUpdateDto.getImageFileName() != null
                    && goodsGroupImageRegistUpdateDto.getImageFileName().indexOf("/") < 0) {
                    goodsGroupImageRegistUpdateDto.setImageFileName(
                                    goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode() + "/"
                                    + goodsGroupImageRegistUpdateDto.getImageFileName());
                }
            }
        } else if (processType == PROCESS_TYPE_FROM_CSV) {
            for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
                goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupSeq);
                // ファイル名に商品管理番号のディレクトリ名が付いていない場合に付ける(登録時にはつけることになる)
                if (goodsGroupImageEntity.getImageFileName() != null
                    && goodsGroupImageEntity.getImageFileName().indexOf("/") < 0) {
                    goodsGroupImageEntity.setImageFileName(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode() + "/"
                                                           + goodsGroupImageEntity.getImageFileName());
                }
            }
        }

        // 関連商品
        for (GoodsRelationEntity goodsRelationEntity : goodsRelationEntityList) {
            goodsRelationEntity.setGoodsGroupSeq(goodsGroupSeq);
        }
        // 商品
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            goodsDto.getGoodsEntity().setGoodsGroupSeq(goodsGroupSeq);
        }
        // 商品グループ人気（初期データ生成）
        GoodsGroupPopularityEntity goodsGroupPopularityEntity =
                        ApplicationContextUtility.getBean(GoodsGroupPopularityEntity.class);
        goodsGroupPopularityEntity.setGoodsGroupSeq(goodsGroupSeq);
        goodsGroupPopularityEntity.setPopularityCount(0);

        // 商品グループ登録Logic処理
        goodsGroupDto.getGoodsGroupEntity().setShopSeq(shopSeq);
        return goodsGroupPopularityEntity;
    }

    /**
     * 商品グループ更新データ作成
     *
     * @param processType                        処理種別（画面/CSV）
     * @param goodsGroupDto                      商品グループDTO
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト
     * @param customParams                       案件用引数
     */
    protected void createUpdateGoodsGroupData(int processType,
                                              GoodsGroupDto goodsGroupDto,
                                              List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList,
                                              Object... customParams) {
        // 商品グループ画像
        if (processType == PROCESS_TYPE_FROM_SCREEN) {
            for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : goodsGroupImageRegistUpdateDtoList) {
                // ファイル名に商品管理番号のディレクトリ名が付いていない場合に付ける(登録時にはつけることになる)
                if (goodsGroupImageRegistUpdateDto.getImageFileName() != null
                    && goodsGroupImageRegistUpdateDto.getImageFileName().indexOf("/") < 0) {
                    goodsGroupImageRegistUpdateDto.setImageFileName(
                                    goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode() + "/"
                                    + goodsGroupImageRegistUpdateDto.getImageFileName());
                }
            }
        } else if (processType == PROCESS_TYPE_FROM_CSV) {
            for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
                goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
                // ファイル名に商品管理番号のディレクトリ名が付いていない場合に付ける(登録時にはつけることになる)
                if (goodsGroupImageEntity.getImageFileName() != null
                    && goodsGroupImageEntity.getImageFileName().indexOf("/") < 0) {
                    goodsGroupImageEntity.setImageFileName(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode() + "/"
                                                           + goodsGroupImageEntity.getImageFileName());
                }
            }
        }
        // 公開状態が削除の場合は商品タグをすべてクリアする
        if (goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC().equals(HTypeOpenDeleteStatus.DELETED)) {
            goodsGroupDto.getGoodsGroupDisplayEntity().setGoodsTag(null);
        }
    }

    /**
     * 商品登録データ作成
     *
     * @param shopSeq         ショップSEQ
     * @param goodsEntityList 商品エンティティリスト
     * @param stockEntityList 在庫エンティティリスト
     * @param goodsDto        商品DTO
     */
    protected void createRegistGoodsData(Integer shopSeq,
                                         List<GoodsEntity> goodsEntityList,
                                         List<StockEntity> stockEntityList,
                                         GoodsDto goodsDto) {
        if (goodsDto.getGoodsEntity().getGoodsSeq() == null) {
            // 新規登録商品の場合は商品エンティティと在庫Dtoに商品SEQの採番とショップSEQの設定を行う
            setGoodsSeqForGoodsDto(shopSeq, stockEntityList, goodsDto);
        }
        // 商品登録更新用リストに追加
        goodsEntityList.add(goodsDto.getGoodsEntity());
    }

    /**
     * 商品Dto配下のEntityに商品SEQをセットする
     *
     * @param shopSeq         ショップSEQ
     * @param stockEntityList 在庫エンティティリスト
     * @param goodsDto        商品DTO
     */
    protected void setGoodsSeqForGoodsDto(Integer shopSeq, List<StockEntity> stockEntityList, GoodsDto goodsDto) {
        // 商品SEQ採番Logic処理
        Integer goodsSeq = newGoodsSeqGetLogic.execute();
        goodsDto.getGoodsEntity().setShopSeq(shopSeq);
        goodsDto.getGoodsEntity().setGoodsSeq(goodsSeq);
        goodsDto.getStockDto().setShopSeq(shopSeq);
        goodsDto.getStockDto().setGoodsSeq(goodsSeq);

        // 在庫エンティティを初期化して登録用リストに追加
        StockEntity stockEntity = ApplicationContextUtility.getBean(StockEntity.class);
        stockEntity.setShopSeq(shopSeq);
        stockEntity.setGoodsSeq(goodsSeq);
        // 商品新規登録時の入荷数を実在庫にセット
        if (goodsDto.getStockDto().getSupplementCount() != null) {
            stockEntity.setRealStock(goodsDto.getStockDto().getSupplementCount());
        } else {
            stockEntity.setRealStock(BigDecimal.ZERO);
        }
        stockEntity.setOrderReserveStock(BigDecimal.ZERO);
        stockEntityList.add(stockEntity);
    }

    /**
     * 画像ファイル登録（一時領域からフロント公開領域へ）
     *
     * @param registImageFilePathList         登録用画像ファイルパスリスト
     * @param resultGoodsGroupImageFileRegist 画像ファイル登録件数
     * @param customParams                    案件用引数
     * @return 画像ファイル登録件数
     */
    protected int registGoodsGroupImageFile(List<FileRegistDto> registImageFilePathList,
                                            int resultGoodsGroupImageFileRegist,
                                            Object... customParams) {
        for (FileRegistDto registFilePath : registImageFilePathList) {
            String strImgDir = registFilePath.getToFilePath()
                                             .substring(0, registFilePath.getToFilePath().lastIndexOf("/"));
            if (!new File(strImgDir).exists()) {
                new File(strImgDir).mkdir();
            }
            try {
                fileOperationUtility.put(registFilePath.getFromFilePath(), registFilePath.getToFilePath(), true);
            } catch (IOException e) {
                LOGGER.warn("移動元画像の削除に失敗しました。", e);
            }
            resultGoodsGroupImageFileRegist++;
        }
        return resultGoodsGroupImageFileRegist;
    }

    /**
     * 物流サービスAPIを呼び出し、在庫情報を登録更新（商品サービス内のデータ登録更新用処理の最後に呼び出すこと）
     *
     * @param goodsGroupDto 商品グループDto
     * @param processType   処理種別（画面/CSV）
     */
    protected void callStockRegistUpdate(GoodsGroupDto goodsGroupDto, int processType) {

        StockRegistUpdateRequest stockRegistUpdateRequest = new StockRegistUpdateRequest();
        List<GoodsStockInfo> goodsStockInfoList = new ArrayList<>();

        goodsGroupDto.getGoodsDtoList().forEach(item -> {
            GoodsStockInfo goodsStockInfo = new GoodsStockInfo();
            goodsStockInfo.setGoodsSeq(item.getGoodsEntity().getGoodsSeq());
            goodsStockInfo.setRemainderFewStock(item.getStockDto().getRemainderFewStock());
            goodsStockInfo.setOrderPointStock(item.getStockDto().getOrderPointStock());
            goodsStockInfo.setSafetyStock(item.getStockDto().getSafetyStock());
            // 在庫管理ON かつ 入庫数が0以外の場合に、入庫日時を設定
            if (HTypeStockManagementFlag.ON.getValue().equals(item.getGoodsEntity().getStockManagementFlag().getValue())
                && item.getStockDto().getSupplementCount().compareTo(BigDecimal.ZERO) != 0) {
                goodsStockInfo.setSupplementTime(this.conversionUtility.toDate(this.dateUtility.getCurrentTime()));
            }
            goodsStockInfo.setSupplementCount(item.getStockDto().getSupplementCount());
            goodsStockInfo.setStockManagementFlag(item.getGoodsEntity().getStockManagementFlag().getValue());
            goodsStockInfoList.add(goodsStockInfo);
        });

        stockRegistUpdateRequest.setGoodsStockInfoList(goodsStockInfoList);

        List<GoodsStockDisplayEntity> goodsStockDisplayEntityList = new ArrayList<>();
        try {
            StockDetailListResponse stockDetailListResponse = stockApi.registUpdate(stockRegistUpdateRequest);
            goodsStockDisplayEntityList = toGoodsStockDisplayEntityList(stockDetailListResponse);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            if (processType == PROCESS_TYPE_FROM_SCREEN) {
                LOGGER.error("例外処理が発生しました", e);
                // 画面からの商品登録更新の場合、APIレスポンスをそのままthrow
                throw e;
            } else {
                LOGGER.error("例外処理が発生しました", e);
                throwMessage("CSV-UPLOAD-004-", new Object[] {goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode()});
            }
        }

        goodsStockDisplaySyncLogic.syncUpsertStock(goodsStockDisplayEntityList);
    }

    /**
     * 商品在庫表示エンティティリストに変換
     *
     * @param stockDetailListResponse 在庫詳細一覧レスポンス
     * @return List<GoodsStockDisplayEntity>
     */
    private List<GoodsStockDisplayEntity> toGoodsStockDisplayEntityList(StockDetailListResponse stockDetailListResponse) {

        if (ObjectUtils.isEmpty(stockDetailListResponse) || CollectionUtils.isEmpty(
                        stockDetailListResponse.getStockDetailsList())) {
            return null;
        }

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        List<GoodsStockDisplayEntity> list = new ArrayList<>();
        stockDetailListResponse.getStockDetailsList().forEach(item -> {
            GoodsStockDisplayEntity entity = new GoodsStockDisplayEntity();
            entity.setGoodsSeq(item.getGoodsSeq());
            entity.setRemainderFewStock(item.getRemainderFewStock());
            entity.setOrderPointStock(item.getOrderPointStock());
            entity.setSafetyStock(item.getSafetyStock());
            entity.setRealStock(item.getRealStock());
            entity.setOrderReserveStock(item.getOrderReserveStock());
            entity.setRegistTime(currentTime);
            entity.setUpdateTime(currentTime);
            list.add(entity);
        });
        return list;
    }
}