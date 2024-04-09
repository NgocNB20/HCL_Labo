/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods.impl;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.util.common.DiffUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadError;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUploadType;
import jp.co.itechh.quad.core.dto.csv.CsvReaderOptionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupMapGetByCodeLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsCsvCodeCheckService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsCsvUpLoadAsynchronousService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupCorrelationDataCheckService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupGetService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupRegistUpdateService;
import jp.co.itechh.quad.core.service.goods.relation.GoodsRelationListGetForBackService;
import jp.co.itechh.quad.core.utility.CheckMessageUtility;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品一括アップロード
 * 商品CSVファイルの一括アップロードを行う。
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Service
public class GoodsCsvUpLoadAsynchronousServiceImpl extends AbstractShopService
                implements GoodsCsvUpLoadAsynchronousService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCsvUpLoadAsynchronousServiceImpl.class);

    /** 商品グループ取得サービス */
    private final GoodsGroupGetService goodsGroupGetService;

    /** 関連商品リスト取得サービス（バック用） */
    private final GoodsRelationListGetForBackService goodsRelationListGetForBackService;

    /** 商品グループDto相関チェックサービス */
    private final GoodsGroupCorrelationDataCheckService goodsGroupCorrelationDataCheckService;

    /** 商品グループ登録更新サービス */
    private final GoodsGroupRegistUpdateService goodsGroupRegistUpdateService;

    /** 商品CSVアップロードコードチェックサービス */
    private final GoodsCsvCodeCheckService goodsCsvCodeCheckService;

    /** 商品グループマップ取得(商品グループコード)ロジック */
    private final GoodsGroupMapGetByCodeLogic goodsGroupMapGetByCodeLogic;

    /** カテゴリ情報取得ロジック */
    private final CategoryGetLogic categoryGetLogic;

    /** カテゴリ登録商品マップ取得ロジック */
    private final CategoryGoodsMapGetLogic categoryGoodsMapGetLogic;

    /** CheckMessageユーティリティ */
    private final CheckMessageUtility checkMessageUtility;

    /** 配列ファクトリユーティリティ */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /** CSV読み込みのユーティル */
    private final CsvReaderModule csvReaderModule;

    /** コンストラクタ */
    @Autowired
    public GoodsCsvUpLoadAsynchronousServiceImpl(GoodsGroupGetService goodsGroupGetService,
                                                 GoodsRelationListGetForBackService goodsRelationListGetForBackService,
                                                 GoodsGroupCorrelationDataCheckService goodsGroupCorrelationDataCheckService,
                                                 GoodsGroupRegistUpdateService goodsGroupRegistUpdateService,
                                                 GoodsCsvCodeCheckService goodsCsvCodeCheckService,
                                                 GoodsGroupMapGetByCodeLogic goodsGroupMapGetByCodeLogic,
                                                 CategoryGetLogic categoryGetLogic,
                                                 CategoryGoodsMapGetLogic categoryGoodsMapGetLogic,
                                                 CheckMessageUtility checkMessageUtility,
                                                 ArrayFactoryUtility arrayFactoryUtility,
                                                 CsvReaderModule csvReaderModule) {
        this.goodsGroupGetService = goodsGroupGetService;
        this.goodsRelationListGetForBackService = goodsRelationListGetForBackService;
        this.goodsGroupCorrelationDataCheckService = goodsGroupCorrelationDataCheckService;
        this.goodsGroupRegistUpdateService = goodsGroupRegistUpdateService;
        this.goodsCsvCodeCheckService = goodsCsvCodeCheckService;
        this.goodsGroupMapGetByCodeLogic = goodsGroupMapGetByCodeLogic;
        this.categoryGetLogic = categoryGetLogic;
        this.categoryGoodsMapGetLogic = categoryGoodsMapGetLogic;
        this.checkMessageUtility = checkMessageUtility;
        this.arrayFactoryUtility = arrayFactoryUtility;
        this.csvReaderModule = csvReaderModule;
    }

    /**
     * 商品一括アップロード処理実行
     *
     * @param administratorSeq         管理者SEQ
     * @param uploadedCsvFile          商品CSVデータアップロードファイル
     * @param uploadType               アップロード種別
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     * @return CsvUploadResult CSVアップロード結果Dto
     */
    @Override
    public CsvUploadResult execute(Integer administratorSeq,
                                   File uploadedCsvFile,
                                   HTypeUploadType uploadType,
                                   Integer shopSeq,
                                   List<Integer> goodsGroupSeqSuccessList) throws Exception {

        // エラーリスト
        clearErrorList();
        // Csvアップロード結果Dto作成
        CsvUploadResult csvUploadResult = new CsvUploadResult();

        // バリデーションチェック エラーの場合終了
        if (validate(csvUploadResult, uploadedCsvFile, uploadType)) {
            return csvUploadResult;
        }

        // CSVアップロード登録処理
        registProcess(administratorSeq, uploadType, csvUploadResult, uploadedCsvFile, shopSeq,
                      goodsGroupSeqSuccessList
                     );
        return csvUploadResult;
    }

    /**
     * 登録処理
     *
     * @param administratorSeq         管理者SEQ
     * @param uploadType               アップロード種別
     * @param csvUploadResult          Csvアップロード結果
     * @param uploadedCsvFile          商品CSVデータアップロードファイル
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     */
    protected void registProcess(Integer administratorSeq,
                                 HTypeUploadType uploadType,
                                 CsvUploadResult csvUploadResult,
                                 File uploadedCsvFile,
                                 Integer shopSeq,
                                 List<Integer> goodsGroupSeqSuccessList) throws Exception {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(-1);

        // Csvファイルを読み込み
        try (CSVParser csvParser = csvReaderModule.readCsv(
                        uploadedCsvFile.getPath(), GoodsCsvDto.class, csvReaderOptionDto)) {

            // CSVファイル処理行番号
            Integer recodeCount = 1;

            // アップロードされたカラムの取得
            List<String> uploadColumn = getUploadColumn(uploadedCsvFile);

            // CSVアップロード共通情報
            Map<String, Object> commonCsvInfoMap =
                            createCommonCsvInfoMap(csvParser, uploadType, csvUploadResult, recodeCount, uploadColumn);

            // １行前の商品グループコード
            String lastGoodsGroupCode = null;
            // 処理中の商品グループDto
            GoodsGroupDto goodsGroupDto = null;
            // 処理中の関連商品エンティティ
            List<GoodsRelationEntity> goodsRelationEntityList = null;
            // 規格共通情報（同一商品グループ内の全規格で同じ値が入る項目を管理するオブジェクト）
            GoodsEntity commonGoodsInfo = getComponent(GoodsEntity.class);

            // エラーとなった商品管理番号の保存用リスト
            List<String> errorGgcdList = new ArrayList<>();

            goodsCsvCodeCheckService.init();

            // 成功件数
            int successCount = 0;
            // 処理中の商品管理番号単位のレコード数
            int recodeCountSpan = 0;
            // エラー対象の商品管理番号の一時保存用変数
            String tmpSaveGgcd;

            while (csvParser.iterator().hasNext()) {

                // エラー対象の商品管理番号の一時保存用変数を初期化
                tmpSaveGgcd = null;

                /** レコード（商品グループ）単位でコミットし、throw発生時は、レコード（商品グループ）単位でロールバック */
                try {

                    GoodsCsvDto csvLine = csvReaderModule.convertCsv2Dto(csvParser.iterator().next(), GoodsCsvDto.class);

                    /** 読み込んだCSVの1つ前までの情報を商品グループ単位で登録更新処理実施 */
                    // 対象商品グループがエラー未発生 かつ 前回商品グループと、処理中のCsvレコードの商品グループが異なる場合に実行
                    if (goodsGroupDto != null && !errorGgcdList.contains(
                                    goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode())
                        && lastGoodsGroupCode != null && ObjectUtils.isNotEmpty(csvLine) && !lastGoodsGroupCode.equals(
                                    csvLine.getGoodsGroupCode())) {
                        if (goodsGroupRegistUpdateProcess(administratorSeq, commonCsvInfoMap, goodsGroupDto,
                                                          goodsRelationEntityList, commonGoodsInfo,
                                                          goodsGroupSeqSuccessList
                                                         )) {
                            successCount++;
                        } else {
                            // エラーとなった商品管理番号をリストに保存
                            errorGgcdList.add(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode());
                        }
                        // 処理中の商品管理番号単位のレコード数を初期化
                        recodeCountSpan = 0;
                    }

                    /** 読み込んだCSVの行単位の処理実施 */

                    // 処理行インクリメント
                    recodeCount++;
                    commonCsvInfoMap.put("recodeCount", recodeCount);
                    // 処理中の商品管理番号単位のレコード数をインクリメント
                    recodeCountSpan++;
                    commonCsvInfoMap.put("recodeCountSpan", recodeCountSpan);

                    if (ObjectUtils.isEmpty(csvLine)) {
                        throwMessage(CSVUPLOAD002E);
                    }

                    // 読み込んだCsvDtoの商品管理番号を一時保存
                    tmpSaveGgcd = csvLine.getGoodsGroupCode();

                    // 読み込んだCsvレコードの商品管理番号が既にエラー発生していないかチェック
                    if (errorGgcdList.contains(csvLine.getGoodsGroupCode())) {
                        throwMessage(CSVUPLOAD_DUPLICATE_ERROR, new Object[] {csvLine.getGoodsGroupCode()});
                    }

                    // 商品管理番号・商品番号のチェック
                    boolean saveGgCode = goodsCsvCodeCheckService.canSaveGoodsGroupCode(csvLine.getGoodsGroupCode());
                    boolean saveGCode = goodsCsvCodeCheckService.canSaveGoodsCode(csvLine.getGoodsCode());
                    if (!saveGgCode || !saveGCode) {
                        if (!saveGgCode) {
                            addErrorMessage(SGP001086,
                                            new Object[] {csvLine.getGoodsGroupCode(), csvLine.getGoodsCode()}
                                           );
                        }
                        if (!saveGCode) {
                            addErrorMessage(SGP001087, new Object[] {csvLine.getGoodsCode()});
                        }
                        if (hasErrorMessage()) {
                            // エラー発生時は、エラー発生行数（recodeCount）をメッセージに表示させるようにするため、goodsCodeAndRecodeCountをクリア
                            commonCsvInfoMap.remove("goodsGroupInfoRecordCount");
                            throwMessage();
                        }
                    }

                    // 商品グループの処理 ※データ１件目やエラーレコードの次レコード分を処理、または前回商品グループと、処理中のCsvレコードの商品グループが異なる場合に実行
                    if (lastGoodsGroupCode == null || !lastGoodsGroupCode.equals(csvLine.getGoodsGroupCode())) {
                        // 商品グループ情報取得処理
                        goodsGroupDto = goodsGroupInfoGetProcess(csvLine.getGoodsGroupCode());

                        // 関連商品情報リスト取得処理
                        goodsRelationEntityList = goodsRelationInfoGetProcess(commonCsvInfoMap,
                                                                              goodsGroupDto.getGoodsGroupEntity()
                                                                                           .getGoodsGroupSeq()
                                                                             );

                        // 商品グループ情報編集処理
                        goodsGroupInfoEditProcess(
                                        commonCsvInfoMap, csvLine, goodsGroupDto, goodsRelationEntityList, shopSeq);
                    }

                    // 商品情報編集処理
                    goodsInfoEditProcess(commonCsvInfoMap, csvLine, goodsGroupDto, shopSeq);

                    // 商品グループ情報から規格共通情報を編集 ※データ１件目やエラーレコードの次レコード分を処理、または前回商品グループと、処理中のCsvレコードの商品グループが異なる場合に実行
                    if (lastGoodsGroupCode == null || !lastGoodsGroupCode.equals(csvLine.getGoodsGroupCode())) {
                        // 規格共通情報を編集する
                        commonGoodsInfo = editCommonGoodsInfo(csvLine.getGoodsCode(), goodsGroupDto);
                    }

                    // 1行単位での処理後処理
                    // １行前の商品グループコードに処理した商品グループコードを設定
                    lastGoodsGroupCode = csvLine.getGoodsGroupCode();

                    // 処理件数をインクリメント
                    csvUploadResult.addMergeCount();
                } catch (AppLevelListException ape) {
                    LOGGER.error("例外処理が発生しました", ape);
                    createCsvUploadErrorList(commonCsvInfoMap, ape, recodeCountSpan);

                    // エラーとなった商品管理番号をリストに保存
                    errorGgcdList.add(tmpSaveGgcd);

                    // 処理中商品グループを初期化
                    lastGoodsGroupCode = null;
                    // 処理中の商品管理番号単位のレコード数を初期化
                    recodeCountSpan = 0;
                    // 処理件数をインクリメント
                    csvUploadResult.addMergeCount();

                    // 次のレコードを処理
                    continue;
                }
            }

            // CSVデータ行終了時の商品グループ登録更新処理
            if (lastGoodsGroupCode != null) {
                if (goodsGroupRegistUpdateProcess(administratorSeq, commonCsvInfoMap, goodsGroupDto,
                                                  goodsRelationEntityList, commonGoodsInfo, goodsGroupSeqSuccessList
                                                 )) {
                    successCount++;
                }
            }

            // 処理結果設定
            csvUploadResult.setSuccessCount(successCount);
            csvUploadResult.setErrorCount(csvReaderModule.getErrorCount());

        } catch (Exception e) {
            // CSV読み込みで有り得ない例外が発生した場合
            csvReaderModule.createUnexpectedExceptionMsg(csvUploadResult);
        }
    }

    /**
     * CSVアップロード処理共通情報生成
     * <pre>
     * Key : helper = CsvアップロードHelper
     * uploadType = アップロード種別
     * csvUploadResult = Csvアップロード結果
     * recodeCount = CSVファイル処理行番号
     * goodsGroupSeq = 処理中データの商品グループSEQ（商品グループ新規登録時はnull）
     * goodsCodeAndRecodeCountMap = 商品コードとCSV行の対応マップ（エラー行表示時に使用する）
     * goodsGroupInfoRecordCount = 商品グループ情報のCSV行（エラー行表示時に使用する）
     * </pre>
     *
     * @param uploadType      アップロード種別
     * @param csvUploadResult Csvアップロード結果
     * @param recodeCount     CSVファイルの処理行
     * @param uploadColumn    アップロードされたカラム
     * @return CSVアップロード処理中の共通情報
     */
    protected Map<String, Object> createCommonCsvInfoMap(CSVParser csvParser,
                                                         HTypeUploadType uploadType,
                                                         CsvUploadResult csvUploadResult,
                                                         Integer recodeCount,
                                                         List<String> uploadColumn) {
        Map<String, Object> commonCsvInfoMap = new HashMap<>();
        commonCsvInfoMap.put("csvParser", csvParser);
        commonCsvInfoMap.put("uploadType", uploadType);
        commonCsvInfoMap.put("csvUploadResult", csvUploadResult);
        commonCsvInfoMap.put("recodeCount", recodeCount);
        commonCsvInfoMap.put("updateColumn", uploadColumn);
        commonCsvInfoMap.put("goodsGroupSeq", null);
        commonCsvInfoMap.put("goodsCodeAndRecodeCountMap", new HashMap<String, Integer>());
        return commonCsvInfoMap;
    }

    /**
     * 商品登録更新処理
     *
     * @param administratorSeq         管理者SEQ
     * @param commonCsvInfoMap         CSVアップロード共通情報
     * @param goodsGroupDto            商品グループDto
     * @param goodsRelationEntityList  関連商品エンティティリスト
     * @param commonGoodsInfo          規格共通情報
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     * @return true ... エラー無し / false ... エラーあり
     */
    protected boolean goodsGroupRegistUpdateProcess(Integer administratorSeq,
                                                    Map<String, Object> commonCsvInfoMap,
                                                    GoodsGroupDto goodsGroupDto,
                                                    List<GoodsRelationEntity> goodsRelationEntityList,
                                                    GoodsEntity commonGoodsInfo,
                                                    List<Integer> goodsGroupSeqSuccessList) throws Exception {

        // 商品グループDtoの各規格情報に規格共通情報を設定する
        setupCommonGoodsInfo(goodsGroupDto, commonGoodsInfo);

        try {
            // 商品グループDto相関チェック
            goodsGroupCorrelationDataCheckService.execute(goodsGroupDto, goodsRelationEntityList,
                                                          GoodsGroupCorrelationDataCheckService.PROCESS_TYPE_FROM_CSV,
                                                          commonCsvInfoMap
                                                         );

            // 商品グループ登録更新処理
            goodsGroupRegistUpdateService.executeForCsv(administratorSeq, goodsGroupDto, goodsRelationEntityList,
                                                        GoodsGroupRegistUpdateService.PROCESS_TYPE_FROM_CSV,
                                                        goodsGroupSeqSuccessList
                                                       );
        } catch (AppLevelListException ape) {
            LOGGER.error("例外処理が発生しました", ape);
            // 商品管理番号単位の処理中CSV読込レコード数を取得
            int unitProcessSize = (int) commonCsvInfoMap.get("recodeCountSpan");

            createCsvUploadErrorList(commonCsvInfoMap, ape, unitProcessSize);
            return false;
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            createCsvUploadError(commonCsvInfoMap, goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode());
            return false;
        }
        return true;
    }

    /**
     * 商品グループ情報取得処理
     * 商品グループコード → 商品グループDto
     *
     * @param goodsGroupCode 商品グループコード
     * @return 商品グループDto
     */
    protected GoodsGroupDto goodsGroupInfoGetProcess(String goodsGroupCode) {

        Integer shopSeq = 1001;
        HTypeSiteType siteType = HTypeSiteType.BACK;

        // 商品グループコードをもとに商品グループDtoを取得する
        GoodsGroupDto goodsGroupDto = goodsGroupGetService.execute(goodsGroupCode, shopSeq, siteType);
        if (goodsGroupDto == null) {
            goodsGroupDto = ApplicationContextUtility.getBean(GoodsGroupDto.class);
        }
        if (goodsGroupDto.getGoodsGroupEntity() == null) {
            goodsGroupDto.setGoodsGroupEntity(getComponent(GoodsGroupEntity.class));
        }
        if (goodsGroupDto.getGoodsGroupDisplayEntity() == null) {
            goodsGroupDto.setGoodsGroupDisplayEntity(getComponent(GoodsGroupDisplayEntity.class));
        }
        if (goodsGroupDto.getGoodsGroupImageEntityList() == null) {
            goodsGroupDto.setGoodsGroupImageEntityList(new ArrayList<>());
        }
        if (goodsGroupDto.getGoodsDtoList() == null) {
            goodsGroupDto.setGoodsDtoList(new ArrayList<>());
        }
        if (goodsGroupDto.getCategoryGoodsEntityList() == null) {
            goodsGroupDto.setCategoryGoodsEntityList(new ArrayList<>());
        }

        // DBから取得した商品の入庫数にデフォルト「0」を設定する
        if (ObjectUtils.isNotEmpty(goodsGroupDto.getGoodsDtoList())) {
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                if (HTypeStockManagementFlag.ON == goodsDto.getGoodsEntity().getStockManagementFlag()) {
                    goodsDto.getStockDto().setSupplementCount(new BigDecimal(0));
                }
            }
        }

        return goodsGroupDto;
    }

    /**
     * 関連商品情報リスト取得処理
     * 商品グループSEQ → 関連商品Dtoリスト
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param goodsGroupSeq    商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    protected List<GoodsRelationEntity> goodsRelationInfoGetProcess(Map<String, Object> commonCsvInfoMap,
                                                                    Integer goodsGroupSeq) {

        List<GoodsRelationEntity> goodsRelationEntityList = new ArrayList<>();
        // 商品グループSEQをもとに関連商品エンティティリストを取得する。関連商品がなければ空リストを設定する。
        if (goodsGroupSeq != null) {
            goodsRelationEntityList = goodsRelationListGetForBackService.execute(goodsGroupSeq);
        }

        // 商品グループ単位で更新するCSVアップロード共通情報の編集
        // CSVアップロード共通情報の商品グループSEQを更新する
        commonCsvInfoMap.put("goodsGroupSeq", goodsGroupSeq);
        // CSVアップロード共通情報の商品コードとCSV行の対応マップをクリアする
        commonCsvInfoMap.put("goodsCodeAndRecodeCountMap", new HashMap<String, Integer>());
        // 商品グループ情報CSV行に現在行を設定する
        commonCsvInfoMap.put("goodsGroupInfoRecordCount", commonCsvInfoMap.get("recodeCount"));
        return goodsRelationEntityList;
    }

    /**
     * 商品グループDtoの各規格情報に規格共通情報を設定
     * 規格共通情報オブジェクト → goodsGroupDto
     *
     * @param goodsGroupDto   商品グループDto
     * @param commonGoodsInfo 規格共通情報
     */
    protected void setupCommonGoodsInfo(GoodsGroupDto goodsGroupDto, GoodsEntity commonGoodsInfo) {

        // 規格共通情報を全規格に設定する
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
            goodsEntity.setFreeDeliveryFlag(commonGoodsInfo.getFreeDeliveryFlag());
            goodsEntity.setIndividualDeliveryType(commonGoodsInfo.getIndividualDeliveryType());
            goodsEntity.setUnitManagementFlag(commonGoodsInfo.getUnitManagementFlag());
            goodsEntity.setStockManagementFlag(commonGoodsInfo.getStockManagementFlag());
        }
    }

    /**
     * 商品グループ内の各規格共通オブジェクトを編集
     * goodsGroupDto → 規格共通情報オブジェクト
     *
     * @param goodsCode     商品コード
     * @param goodsGroupDto 商品グループDto
     * @return 規格共通情報
     */
    protected GoodsEntity editCommonGoodsInfo(String goodsCode, GoodsGroupDto goodsGroupDto) {

        // 規格共通情報
        GoodsEntity commonGoodsInfo = getComponent(GoodsEntity.class);
        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            if (goodsCode.equals(goodsDto.getGoodsEntity().getGoodsCode())) {
                // 規格共通情報の設定
                commonGoodsInfo.setFreeDeliveryFlag(goodsDto.getGoodsEntity().getFreeDeliveryFlag());
                commonGoodsInfo.setIndividualDeliveryType(goodsDto.getGoodsEntity().getIndividualDeliveryType());
                commonGoodsInfo.setUnitManagementFlag(goodsDto.getGoodsEntity().getUnitManagementFlag());
                commonGoodsInfo.setStockManagementFlag(goodsDto.getGoodsEntity().getStockManagementFlag());
                break;
            }
        }
        return commonGoodsInfo;
    }

    /**
     * 商品グループ情報を編集
     *
     * @param commonCsvInfoMap        CSVアップロード共通情報
     * @param goodsCsvDto             商品CSVDto
     * @param goodsGroupDto           商品グループDto
     * @param goodsRelationEntityList 関連商品エンティティリスト
     */
    protected void goodsGroupInfoEditProcess(Map<String, Object> commonCsvInfoMap,
                                             GoodsCsvDto goodsCsvDto,
                                             GoodsGroupDto goodsGroupDto,
                                             List<GoodsRelationEntity> goodsRelationEntityList,
                                             Integer shopSeq) {

        // アップロード種別を取得する
        HTypeUploadType uploadType = (HTypeUploadType) commonCsvInfoMap.get("uploadType");
        // 更新対象項目リストを取得する
        @SuppressWarnings("unchecked")
        List<String> updateColumn = (List<String>) commonCsvInfoMap.get("updateColumn");

        // CSVDtoのうち、更新対象項目の商品グループ情報を商品グループDtoに反映する
        setupGoodsGroupDto(commonCsvInfoMap, goodsCsvDto, uploadType, updateColumn, goodsGroupDto, shopSeq);

        // CSVDtoの関連商品設定を関連商品Dtoリストに反映する
        setupGoodsRelationDtoList(
                        commonCsvInfoMap, goodsCsvDto, uploadType, updateColumn, goodsRelationEntityList, shopSeq);
    }

    /**
     * 商品グループ情報設定
     * (編集前)商品グループDto + 商品CSVDto + 更新対象項目リスト ⇒ 商品グループDto
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param goodsCsvDto      商品CSVDto
     * @param uploadType       アップロード種別
     * @param updateColumn     更新対象項目リスト
     * @param goodsGroupDto    商品グループDto
     */
    protected void setupGoodsGroupDto(Map<String, Object> commonCsvInfoMap,
                                      GoodsCsvDto goodsCsvDto,
                                      HTypeUploadType uploadType,
                                      List<String> updateColumn,
                                      GoodsGroupDto goodsGroupDto,
                                      Integer shopSeq) {

        // CSVDtoのうち、更新対象項目データを商品グループDtoの商品グループ部に反映する

        // -------------------------------
        // 商品グループEntityの設定
        // -------------------------------
        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();

        // ショップSEQを取得し設定（無条件で設定）
        goodsGroupEntity.setShopSeq(shopSeq);

        // 商品管理番号
        if (HTypeUploadType.REGIST.equals(uploadType)) {
            goodsGroupEntity.setGoodsGroupCode(goodsCsvDto.getGoodsGroupCode());
        }
        // 商品名
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsGroupName") && goodsCsvDto.getGoodsGroupName() != null) {
            goodsGroupEntity.setGoodsGroupName(goodsCsvDto.getGoodsGroupName());
        }
        // 公開状態、公開開始日時、公開終了日時
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsOpenStatusPC") && goodsCsvDto.getGoodsOpenStatusPC() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(goodsCsvDto.getGoodsOpenStatusPC());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("openStartTimePC") && goodsCsvDto.getOpenStartTimePC() != null) {
            goodsGroupEntity.setOpenStartTimePC(goodsCsvDto.getOpenStartTimePC());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("openEndTimePC") && goodsCsvDto.getOpenEndTimePC() != null) {
            goodsGroupEntity.setOpenEndTimePC(goodsCsvDto.getOpenEndTimePC());
        }
        // 新着日付
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("whatsnewDate") && goodsCsvDto.getWhatsnewDate() != null) {
            goodsGroupEntity.setWhatsnewDate(goodsCsvDto.getWhatsnewDate());
        }
        // 酒類フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("alcoholFlag") && goodsCsvDto.getAlcoholFlag() != null) {
            goodsGroupEntity.setAlcoholFlag(goodsCsvDto.getAlcoholFlag());
        }
        // ノベルティ商品フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("noveltyGoodsType") && goodsCsvDto.getNoveltyGoodsType() != null) {
            goodsGroupEntity.setNoveltyGoodsType(goodsCsvDto.getNoveltyGoodsType());
        }
        // SNS連携フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("snsLinkFlag") && goodsCsvDto.getSnsLinkFlag() != null) {
            goodsGroupEntity.setSnsLinkFlag(goodsCsvDto.getSnsLinkFlag());
        }
        // 価格（税抜）
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsPrice") && goodsCsvDto.getGoodsPrice() != null) {
            goodsGroupEntity.setGoodsPrice(goodsCsvDto.getGoodsPrice());
        }
        // 税率
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("taxRate") && goodsCsvDto.getTaxRate() != null) {
            goodsGroupEntity.setTaxRate(goodsCsvDto.getTaxRate());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsTaxType")) {
            // 商品消費税種別・・・外税固定
            goodsGroupEntity.setGoodsTaxType(HTypeGoodsTaxType.OUT_TAX);
        }

        // -------------------------------
        // 商品グループ表示Entityの設定
        // -------------------------------
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();
        // 商品納期
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("deliveryType") && goodsCsvDto.getDeliveryType() != null) {
            goodsGroupDisplayEntity.setDeliveryType(goodsCsvDto.getDeliveryType());
        }
        // 商品説明01～10
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote1") && goodsCsvDto.getGoodsNote1() != null) {
            goodsGroupDisplayEntity.setGoodsNote1(goodsCsvDto.getGoodsNote1());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote2") && goodsCsvDto.getGoodsNote2() != null) {
            goodsGroupDisplayEntity.setGoodsNote2(goodsCsvDto.getGoodsNote2());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote3") && goodsCsvDto.getGoodsNote3() != null) {
            goodsGroupDisplayEntity.setGoodsNote3(goodsCsvDto.getGoodsNote3());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote4") && goodsCsvDto.getGoodsNote4() != null) {
            goodsGroupDisplayEntity.setGoodsNote4(goodsCsvDto.getGoodsNote4());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote5") && goodsCsvDto.getGoodsNote5() != null) {
            goodsGroupDisplayEntity.setGoodsNote5(goodsCsvDto.getGoodsNote5());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote6") && goodsCsvDto.getGoodsNote6() != null) {
            goodsGroupDisplayEntity.setGoodsNote6(goodsCsvDto.getGoodsNote6());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote7") && goodsCsvDto.getGoodsNote7() != null) {
            goodsGroupDisplayEntity.setGoodsNote7(goodsCsvDto.getGoodsNote7());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote8") && goodsCsvDto.getGoodsNote8() != null) {
            goodsGroupDisplayEntity.setGoodsNote8(goodsCsvDto.getGoodsNote8());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote9") && goodsCsvDto.getGoodsNote9() != null) {
            goodsGroupDisplayEntity.setGoodsNote9(goodsCsvDto.getGoodsNote9());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsNote10") && goodsCsvDto.getGoodsNote10() != null) {
            goodsGroupDisplayEntity.setGoodsNote10(goodsCsvDto.getGoodsNote10());
        }
        // アイコン設定
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("informationIconPC") && goodsCsvDto.getInformationIconPC() != null) {
            goodsGroupDisplayEntity.setInformationIconPC(goodsCsvDto.getInformationIconPC());
        }
        // 商品タグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("goodsTagSetting")) {
            if (ObjectUtils.isNotEmpty(goodsCsvDto.getGoodsTagSetting())) {
                try {
                    goodsGroupDisplayEntity.setGoodsTag(
                                    arrayFactoryUtility.createTextArray(goodsCsvDto.getGoodsTagSetting().split("/")));
                } catch (Exception e) {
                    LOGGER.error("例外処理が発生しました", e);
                    throwMessage("SGC001104");
                }
            }
        }
        // 規格管理フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitManagementFlag") && goodsCsvDto.getUnitManagementFlag() != null) {
            goodsGroupDisplayEntity.setUnitManagementFlag(goodsCsvDto.getUnitManagementFlag());
        }
        // 規格1・2表示名
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitTitle1") && goodsCsvDto.getUnitTitle1() != null) {
            goodsGroupDisplayEntity.setUnitTitle1(goodsCsvDto.getUnitTitle1());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitTitle2") && goodsCsvDto.getUnitTitle2() != null) {
            goodsGroupDisplayEntity.setUnitTitle2(goodsCsvDto.getUnitTitle2());
        }
        // 受注連携設定01～10
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting1") && goodsCsvDto.getOrderSetting1() != null) {
            goodsGroupDisplayEntity.setOrderSetting1(goodsCsvDto.getOrderSetting1());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting2") && goodsCsvDto.getOrderSetting2() != null) {
            goodsGroupDisplayEntity.setOrderSetting2(goodsCsvDto.getOrderSetting2());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting3") && goodsCsvDto.getOrderSetting3() != null) {
            goodsGroupDisplayEntity.setOrderSetting3(goodsCsvDto.getOrderSetting3());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting4") && goodsCsvDto.getOrderSetting4() != null) {
            goodsGroupDisplayEntity.setOrderSetting4(goodsCsvDto.getOrderSetting4());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting5") && goodsCsvDto.getOrderSetting5() != null) {
            goodsGroupDisplayEntity.setOrderSetting5(goodsCsvDto.getOrderSetting5());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting6") && goodsCsvDto.getOrderSetting6() != null) {
            goodsGroupDisplayEntity.setOrderSetting6(goodsCsvDto.getOrderSetting6());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting7") && goodsCsvDto.getOrderSetting7() != null) {
            goodsGroupDisplayEntity.setOrderSetting7(goodsCsvDto.getOrderSetting7());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting8") && goodsCsvDto.getOrderSetting8() != null) {
            goodsGroupDisplayEntity.setOrderSetting8(goodsCsvDto.getOrderSetting8());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting9") && goodsCsvDto.getOrderSetting9() != null) {
            goodsGroupDisplayEntity.setOrderSetting9(goodsCsvDto.getOrderSetting9());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderSetting10") && goodsCsvDto.getOrderSetting10() != null) {
            goodsGroupDisplayEntity.setOrderSetting10(goodsCsvDto.getOrderSetting10());
        }
        // 検索キーワード
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("searchKeyword") && goodsCsvDto.getSearchKeyword() != null) {
            goodsGroupDisplayEntity.setSearchKeyword(goodsCsvDto.getSearchKeyword());
        }
        // メタ説明文、キーワード
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("metaDescription") && goodsCsvDto.getMetaDescription() != null) {
            goodsGroupDisplayEntity.setMetaDescription(goodsCsvDto.getMetaDescription());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("metaKeyword") && goodsCsvDto.getMetaKeyword() != null) {
            goodsGroupDisplayEntity.setMetaKeyword(goodsCsvDto.getMetaKeyword());
        }

        // -------------------------------
        // カテゴリ登録商品Entityリストの設定
        // -------------------------------
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("categoryGoodsSetting")) {
            setupCategoryGoodsEntityList(
                            commonCsvInfoMap, goodsCsvDto, goodsGroupDto.getCategoryGoodsEntityList(), shopSeq);
        }
    }

    /**
     * 関連商品情報設定
     * (編集前)関連商品Dtoリスト + 商品CSVDto + 更新対象項目リスト ⇒ 関連商品Dtoリスト
     *
     * @param commonCsvInfoMap        CSVアップロード共通情報
     * @param goodsCsvDto             商品CSVDto
     * @param uploadType              アップロード種別
     * @param updateColumn            更新対象項目リスト
     * @param goodsRelationEntityList 関連商品エンティティリスト
     */
    protected void setupGoodsRelationDtoList(Map<String, Object> commonCsvInfoMap,
                                             GoodsCsvDto goodsCsvDto,
                                             HTypeUploadType uploadType,
                                             List<String> updateColumn,
                                             List<GoodsRelationEntity> goodsRelationEntityList,
                                             Integer shopSeq) {

        // 関連商品DtoリストのcopyObjectへディープコピーするを行う
        List<GoodsRelationEntity> copyGoodsRelationDtoList = new ArrayList<>();
        for (int i = 0; goodsRelationEntityList != null && i < goodsRelationEntityList.size(); i++) {
            copyGoodsRelationDtoList.add(CopyUtil.deepCopy(goodsRelationEntityList.get(i)));
        }

        // 商品グループSEQを取得
        Integer goodsGroupSeq = (Integer) commonCsvInfoMap.get("goodsGroupSeq");

        // 以下の場合は関連商品Dtoリストを編集しない
        // 関連商品設定が更新対象項目のデータに含まれていない場合
        if (HTypeUploadType.REGIST != uploadType && !updateColumn.contains("goodsRelationGoodsGroupCode")) {
            return;
        }
        // 関連商品Dtoリストをクリアする
        goodsRelationEntityList.clear();

        // 商品グループ新規登録かつ、関連商品登録なしの場合
        if (goodsGroupSeq == null && StringUtils.isEmpty(goodsCsvDto.getGoodsRelationGoodsGroupCode())) {
            return;
        }

        // CSVから取得した関連商品グループコードをリスト化する
        String[] goodsRelationGoodsGroupCodeList = new String[] {};
        if (StringUtils.isNotEmpty(goodsCsvDto.getGoodsRelationGoodsGroupCode())) {
            goodsRelationGoodsGroupCodeList = goodsCsvDto.getGoodsRelationGoodsGroupCode().split("/");
            // 関連商品設定で0件を指定したとき
            if (goodsRelationGoodsGroupCodeList.length == 0) {
                // 空のリストを関連商品Dtoに設定して終了
                return;
            }

            // 関連商品Dtoリストを編集する
            List<String> strGoodsRelationGoodsGroupCodeList = Arrays.asList(goodsRelationGoodsGroupCodeList);
            List<String> repetitionRelationGoodsList = new ArrayList<>();
            // 自身の商品が複数関連商品に登録されているときに自商品エラーが表示されるようにする
            // 自商品が重複しているかを確認するための商品グループコードリスト
            List<String> sameGoodsList = new ArrayList<>();

            // CSVの関連商品グループコードリストをもとに、商品グループマップを取得
            Map<String, GoodsGroupEntity> goodsGroupEntityMap = new HashMap<>();
            if (goodsRelationGoodsGroupCodeList.length != 0) {
                goodsGroupEntityMap = goodsGroupMapGetByCodeLogic.execute(shopSeq,
                                                                          Arrays.asList(goodsRelationGoodsGroupCodeList)
                                                                         );
            }
            // CSVの関連商品_商品管理番号IDリストごとにDtoを編集する
            for (int i = 0;
                 goodsRelationGoodsGroupCodeList != null && i < goodsRelationGoodsGroupCodeList.length; i++) {
                String goodsRelationGoodsGroupCode = goodsRelationGoodsGroupCodeList[i];

                if (strGoodsRelationGoodsGroupCodeList.indexOf(goodsRelationGoodsGroupCode)
                    != strGoodsRelationGoodsGroupCodeList.lastIndexOf(goodsRelationGoodsGroupCode)
                    && !repetitionRelationGoodsList.contains(goodsRelationGoodsGroupCode)) {
                    // 登録関連商品重複エラー
                    addErrorMessage(RELATIONGOODS_REPETITION_FAIL,
                                    new Object[] {goodsRelationGoodsGroupCode, null, null}
                                   );
                    repetitionRelationGoodsList.add(goodsRelationGoodsGroupCode);
                    continue;
                }
                if (goodsRelationGoodsGroupCode.equals(goodsCsvDto.getGoodsGroupCode())) {
                    // 複数個登録されていた場合一つのエラーとして表示する
                    if (!sameGoodsList.contains(goodsRelationGoodsGroupCode)) {
                        // 自身を関連商品に登録エラー
                        addErrorMessage(RELATIONGOODS_MYSELF_FAIL,
                                        new Object[] {goodsRelationGoodsGroupCode, null, null}
                                       );
                        sameGoodsList.add(goodsRelationGoodsGroupCode);
                    }
                    continue;
                }

                // 関連商品Dtoの編集
                if (i < copyGoodsRelationDtoList.size() && goodsRelationGoodsGroupCode.equals(
                                copyGoodsRelationDtoList.get(i).getGoodsGroupCode())) {
                    // 商品コードと表示順が一致した場合は戻り値用リストにそのまま追加
                    goodsRelationEntityList.add(copyGoodsRelationDtoList.get(i));
                } else {
                    // 商品コードまたは表示順が異なる場合
                    GoodsRelationEntity newGoodsRelationEntity =
                                    ApplicationContextUtility.getBean(GoodsRelationEntity.class);
                    if (goodsGroupEntityMap.get(goodsRelationGoodsGroupCode) == null
                        || HTypeOpenDeleteStatus.DELETED == goodsGroupEntityMap.get(goodsRelationGoodsGroupCode)
                                                                               .getGoodsOpenStatusPC()) {
                        // 存在しない関連商品グループコードエラー(PCが削除ならMBも削除だから、片方だけみる）
                        addErrorMessage(RELATIONGOODS_NONE_FAIL,
                                        new Object[] {goodsRelationGoodsGroupCodeList[i], null, null}
                                       );
                        continue;
                    }
                    newGoodsRelationEntity.setGoodsGroupCode(
                                    goodsGroupEntityMap.get(goodsRelationGoodsGroupCode).getGoodsGroupCode());
                    newGoodsRelationEntity.setGoodsGroupName(
                                    goodsGroupEntityMap.get(goodsRelationGoodsGroupCode).getGoodsGroupName());
                    newGoodsRelationEntity.setGoodsOpenStatusPC(
                                    goodsGroupEntityMap.get(goodsRelationGoodsGroupCode).getGoodsOpenStatusPC());
                    newGoodsRelationEntity.setGoodsGroupSeq(goodsGroupSeq);
                    newGoodsRelationEntity.setGoodsRelationGroupSeq(
                                    goodsGroupEntityMap.get(goodsRelationGoodsGroupCode).getGoodsGroupSeq());
                    newGoodsRelationEntity.setOrderDisplay(i + 1);
                    if (i < copyGoodsRelationDtoList.size()) {
                        newGoodsRelationEntity.setRegistTime(copyGoodsRelationDtoList.get(i).getRegistTime());
                    }
                    goodsRelationEntityList.add(newGoodsRelationEntity);
                }
            }
            if (goodsRelationGoodsGroupCodeList.length > PropertiesUtil.getSystemPropertiesValueToInt(
                            "goods.relation.amount")) {
                // 関連商品登録数超過エラー
                addErrorMessage(RELATIONGOODS_OVER_FAIL,
                                new Object[] {PropertiesUtil.getSystemPropertiesValue("goods.relation.amount"), null,
                                                null}
                               );
            }
        }

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * 商品情報を編集
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param goodsCsvDto      商品CSVDto
     * @param goodsGroupDto    商品グループDto
     */
    @SuppressWarnings("unchecked")
    protected void goodsInfoEditProcess(Map<String, Object> commonCsvInfoMap,
                                        GoodsCsvDto goodsCsvDto,
                                        GoodsGroupDto goodsGroupDto,
                                        Integer shopSeq) {

        // アップロード種別を取得する
        HTypeUploadType uploadType = (HTypeUploadType) commonCsvInfoMap.get("uploadType");
        // 更新対象項目リストを取得する
        List<String> updateColumn = (List<String>) commonCsvInfoMap.get("updateColumn");
        // 商品コードとCSV行の対応マップに追加
        Map<String, Integer> goodsCodeAndRecodeCountMap =
                        (Map<String, Integer>) commonCsvInfoMap.get("goodsCodeAndRecodeCountMap");
        goodsCodeAndRecodeCountMap.put(goodsCsvDto.getGoodsCode(), (Integer) commonCsvInfoMap.get("recodeCount"));

        // CSVDtoのうち、更新対象項目の商品情報を商品Dtoに反映する
        setupGoodsDto(goodsCsvDto, uploadType, updateColumn, goodsGroupDto, shopSeq);
    }

    /**
     * 商品情報設定
     * (編集前)商品グループDto + 商品CSVDto + 更新対象項目リスト ⇒ 商品グループDto
     *
     * @param goodsCsvDto   商品CSVDto
     * @param uploadType    アップロード種別
     * @param updateColumn  更新対象項目リスト
     * @param goodsGroupDto 商品グループDto
     */
    protected void setupGoodsDto(GoodsCsvDto goodsCsvDto,
                                 HTypeUploadType uploadType,
                                 List<String> updateColumn,
                                 GoodsGroupDto goodsGroupDto,
                                 Integer shopSeq) {

        // CSVDtoのうち、更新対象項目データを商品グループDtoの規格情報に反映する

        // （規格更新時）処理行の規格に対応する商品Entity取得
        GoodsDto goodsDto = null;
        for (GoodsDto tmpDto : goodsGroupDto.getGoodsDtoList()) {
            if (goodsCsvDto.getGoodsCode().equals(tmpDto.getGoodsEntity().getGoodsCode()) && shopSeq.equals(
                            tmpDto.getGoodsEntity().getShopSeq())) {
                goodsDto = tmpDto;
            }
        }

        // 更新対象規格なしエラー
        if (goodsDto == null && HTypeUploadType.MODIFY.equals(uploadType)) {
            addErrorMessage(UPDATEGOODS_NONE_FAIL,
                            new Object[] {goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode(),
                                            goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode()}
                           );
        } else if (goodsDto != null && HTypeUploadType.REGIST.equals(uploadType)) {
            addErrorMessage(REGISTGOODS_EXIST_FAIL,
                            new Object[] {goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode(),
                                            goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode()}
                           );
        } else if (HTypeUploadType.MODIFY.equals(uploadType) && HTypeGoodsSaleStatus.DELETED.equals(
                        goodsDto.getGoodsEntity().getSaleStatusPC())) {
            addErrorMessage(UPDATEGOODS_DELETED_FAIL,
                            new Object[] {goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode(),
                                            goodsCsvDto.getGoodsGroupCode(), goodsCsvDto.getGoodsCode()}
                           );
        }
        // 規格新規登録時
        if (goodsDto == null) {
            goodsDto = ApplicationContextUtility.getBean(GoodsDto.class);
            goodsDto.setGoodsEntity(getComponent(GoodsEntity.class));
            goodsDto.setStockDto(ApplicationContextUtility.getBean(StockDto.class));
            if (goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq() != null) {
                goodsDto.getGoodsEntity().setGoodsGroupSeq(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
            }
            goodsGroupDto.getGoodsDtoList().add(goodsDto);
        }

        // -------------------------------
        // 商品Entityの設定
        // -------------------------------
        GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
        // ショップSEQ
        goodsEntity.setShopSeq(shopSeq);
        // 規格表示順
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderDisplay") && goodsCsvDto.getOrderDisplay() != null) {
            // 新規登録の際にまだ必須チェックされていないため、nullの可能性あり
            if (goodsCsvDto.getOrderDisplay() != null) {
               goodsEntity.setOrderDisplay(goodsCsvDto.getOrderDisplay().intValue());
           }
        }
        // 商品番号
        if (HTypeUploadType.REGIST.equals(uploadType)) {
            goodsEntity.setGoodsCode(goodsCsvDto.getGoodsCode());
        }
        // 規格管理フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitManagementFlag") && goodsCsvDto.getUnitManagementFlag() != null) {
            goodsEntity.setUnitManagementFlag(goodsCsvDto.getUnitManagementFlag());
        }
        // 規格1、規格2
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitValue1") && goodsCsvDto.getUnitValue1() != null) {
            goodsEntity.setUnitValue1(goodsCsvDto.getUnitValue1());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("unitValue2") && goodsCsvDto.getUnitValue2() != null) {
            goodsEntity.setUnitValue2(goodsCsvDto.getUnitValue2());
        }
        // 注文上限
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("purchasedMax") && goodsCsvDto.getPurchasedMax() != null) {
            goodsEntity.setPurchasedMax(goodsCsvDto.getPurchasedMax());
        }
        // 販売状態
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("saleStatusPC") && goodsCsvDto.getSaleStatusPC() != null) {
            goodsEntity.setSaleStatusPC(goodsCsvDto.getSaleStatusPC());
        }
        // JANコード
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("janCode") && goodsCsvDto.getJanCode() != null) {
            goodsEntity.setJanCode(goodsCsvDto.getJanCode());
        }
        // 販売開始日時、販売終了日時
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("saleStartTimePC") && goodsCsvDto.getSaleStartTimePC() != null) {
            goodsEntity.setSaleStartTimePC(goodsCsvDto.getSaleStartTimePC());
        }
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("saleEndTimePC") && goodsCsvDto.getSaleEndTimePC() != null) {
            goodsEntity.setSaleEndTimePC(goodsCsvDto.getSaleEndTimePC());
        }
        // 個別配送
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("individualDeliveryType") && goodsCsvDto.getIndividualDeliveryType() != null) {
            goodsEntity.setIndividualDeliveryType(goodsCsvDto.getIndividualDeliveryType());
        }
        // 送料込／送料別
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("freeDeliveryFlag") && goodsCsvDto.getFreeDeliveryFlag() != null) {
            goodsEntity.setFreeDeliveryFlag(goodsCsvDto.getFreeDeliveryFlag());
        }
        // 在庫管理フラグ
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("stockManagementFlag") && goodsCsvDto.getStockManagementFlag() != null) {
            goodsEntity.setStockManagementFlag(goodsCsvDto.getStockManagementFlag());
        }

        // -------------------------------
        // 在庫Dtoの設定
        // -------------------------------
        StockDto stockDto = goodsDto.getStockDto();
        // 入庫数
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("supplementCount")) {
            if (HTypeStockManagementFlag.OFF == goodsEntity.getStockManagementFlag()
                && goodsCsvDto.getSupplementCount() == null) {
                // 在庫設定なし、且つ入庫数がnullの場合、「0」を補完する
                stockDto.setSupplementCount(BigDecimal.ZERO);
            } else {
                stockDto.setSupplementCount(goodsCsvDto.getSupplementCount());
            }
        }
        // 安全在庫数
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("safetyStock")) {
            if (HTypeStockManagementFlag.OFF == goodsEntity.getStockManagementFlag()
                && goodsCsvDto.getSafetyStock() == null) {
                // 在庫設定なし、且つ安全在庫数がnullの場合、「0」を補完する
                stockDto.setSafetyStock(BigDecimal.ZERO);
            } else {
                stockDto.setSafetyStock(goodsCsvDto.getSafetyStock());
            }
        }
        // 残少表示在庫数
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("remainderFewStock")) {
            if (HTypeStockManagementFlag.OFF == goodsEntity.getStockManagementFlag()
                && goodsCsvDto.getRemainderFewStock() == null) {
                // 在庫設定なし、且つ残少表示在庫数がnullの場合、「0」を補完する
                stockDto.setRemainderFewStock(BigDecimal.ZERO);
            } else {
                stockDto.setRemainderFewStock(goodsCsvDto.getRemainderFewStock());
            }
        }

        // 発注点在庫数
        if (HTypeUploadType.REGIST.equals(uploadType) || updateColumn.contains("orderPointStock")) {
            if (HTypeStockManagementFlag.OFF == goodsEntity.getStockManagementFlag()
                && goodsCsvDto.getOrderPointStock() == null) {
                // 在庫設定なし、且つ発注点在庫数がnullの場合、「0」を補完する
                stockDto.setOrderPointStock(BigDecimal.ZERO);
            } else {
                stockDto.setOrderPointStock(goodsCsvDto.getOrderPointStock());
            }
        }

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * カテゴリエンティティリスト設定
     *
     * @param commonCsvInfoMap        CSVアップロード共通情報
     * @param goodsCsvDto             商品CSVDto
     * @param categoryGoodsEntityList カテゴリ登録商品Entityリスト
     */
    protected void setupCategoryGoodsEntityList(Map<String, Object> commonCsvInfoMap,
                                                GoodsCsvDto goodsCsvDto,
                                                List<CategoryGoodsEntity> categoryGoodsEntityList,
                                                Integer shopSeq) {

        // 商品グループSEQを取得
        Integer goodsGroupSeq = (Integer) commonCsvInfoMap.get("goodsGroupSeq");

        // カテゴリ登録商品リストをクリア
        categoryGoodsEntityList.clear();

        // カテゴリ設定リストがない場合は、空のリストのまま返す
        if (goodsCsvDto.getCategoryGoodsSetting() == null || "".equals(goodsCsvDto.getCategoryGoodsSetting())) {
            return;
        }

        // CSVから取得したカテゴリ設定をリスト化する
        String[] categoryIdList = goodsCsvDto.getCategoryGoodsSetting().split("/");

        // DBに登録されているカテゴリ登録商品マップ(KEY:カテゴリSEQ)を取得する
        Map<Integer, CategoryGoodsEntity> categoryGoodsEntityMap = new HashMap<>();
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        if (goodsGroupSeq != null) {
            goodsGroupSeqList.add(goodsGroupSeq);
        }
        if (goodsGroupSeqList.size() > 0) {
            Map<Integer, List<CategoryGoodsEntity>> categoryGoodsEntityListMap =
                            categoryGoodsMapGetLogic.execute(goodsGroupSeqList);
            List<CategoryGoodsEntity> masterCategoryGoodsEntityList = categoryGoodsEntityListMap.get(goodsGroupSeq);
            if (masterCategoryGoodsEntityList != null) {
                for (CategoryGoodsEntity masterCategoryGoodsEntity : masterCategoryGoodsEntityList) {
                    categoryGoodsEntityMap.put(masterCategoryGoodsEntity.getCategorySeq(), masterCategoryGoodsEntity);
                }
            }
        }

        // 重複チェック用リストの作成
        List<String> strCategoryIdList = Arrays.asList(categoryIdList);
        // 登録カテゴリ重複エラーのセット
        List<String> repetitionedCategory = new ArrayList<>();
        // 存在しないカテゴリーが指定され、同一のカテゴリーであった場合はメッセージは１行に集約する
        // 存在しないカテゴリーのセット
        List<String> noneFailCategory = new ArrayList<>();
        for (String categoryId : categoryIdList) {
            if (strCategoryIdList.indexOf(categoryId) != strCategoryIdList.lastIndexOf(categoryId)
                && !repetitionedCategory.contains(categoryId)) {
                // 登録カテゴリ重複エラー
                addErrorMessage(CATEGORY_REPETITION_FAIL, new Object[] {categoryId, null, null});
                repetitionedCategory.add(categoryId);
                continue;
            }
            CategoryEntity categoryEntity = categoryGetLogic.execute(shopSeq, categoryId);
            // カテゴリーが存在しない
            if (categoryEntity == null) {
                // １度もエラーとしてメッセージをセットしていない場合
                if (!noneFailCategory.contains(categoryId)) {
                    // カテゴリなしエラー
                    addErrorMessage(CATEGORY_NONE_FAIL, new Object[] {categoryId, null, null});
                    noneFailCategory.add(categoryId);
                }
                continue;
            }
            CategoryGoodsEntity dto = categoryGoodsEntityMap.get(categoryEntity.getCategorySeq());
            if (dto != null) {
                // カテゴリ登録商品が既に登録されている場合
                // 戻り値用カテゴリ登録商品Dtoリストにそのままセット
                categoryGoodsEntityList.add(dto);
            } else {
                // カテゴリ登録商品が既に登録されていない場合
                // 戻り値用カテゴリ登録商品リストに登録データを作成してセット
                CategoryGoodsEntity newDto = ApplicationContextUtility.getBean(CategoryGoodsEntity.class);
                newDto.setCategorySeq(categoryEntity.getCategorySeq());
                newDto.setGoodsGroupSeq(goodsGroupSeq);
                categoryGoodsEntityList.add(newDto);
            }
        }

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /** **** 以降、結果用メッセージ関連のメソッド **** */

    /**
     * エラーメッセージ生成
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param appe             アプリケーションExceptionオブジェクトリスト
     * @param unitSize         商品規格数（レコード数）
     */
    protected void createCsvUploadErrorList(Map<String, Object> commonCsvInfoMap,
                                            AppLevelListException appe,
                                            int unitSize) {

        // エラー情報が無い場合は処理を抜ける
        if (appe.getErrorList() == null || appe.getErrorList().isEmpty()) {
            return;
        }

        // 行番号取得
        Integer recordCount = (Integer) commonCsvInfoMap.get("recodeCount");

        // CSVアップロード結果を取得する
        CsvUploadResult csvUploadResult = (CsvUploadResult) commonCsvInfoMap.get("csvUploadResult");

        // エラーメッセージを作成・セット
        List<CsvUploadError> csvUploadErrorList = new ArrayList<>();

        for (AppLevelException ae : appe.getErrorList()) {
            csvUploadErrorList.add(createCsvUploadError(recordCount, unitSize, ae.getMessageCode(), ae.getArgs()));
        }

        if (CollectionUtils.isEmpty(csvUploadResult.getCsvUploadErrorlList())) {
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
        } else {
            csvUploadResult.getCsvUploadErrorlList().addAll(csvUploadErrorList);
        }

        // エラーリストをクリア
        clearErrorList();
    }

    /**
     * エラーメッセージ生成
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param goodsGroupCode   商品グループコード
     */
    protected void createCsvUploadError(Map<String, Object> commonCsvInfoMap, String goodsGroupCode) {

        // 行番号取得
        Integer recordCount = (Integer) commonCsvInfoMap.get("recodeCount");

        // 商品管理番号単位の処理中CSV読込レコード数を取得
        int unitProcessSize = (int) commonCsvInfoMap.get("recodeCountSpan");

        // CSVアップロード結果を取得する
        CsvUploadResult csvUploadResult = (CsvUploadResult) commonCsvInfoMap.get("csvUploadResult");

        // エラーメッセージを作成・セット
        List<CsvUploadError> csvUploadErrorList =
                        Arrays.asList(createCsvUploadError(recordCount, unitProcessSize, "CSV-UPLOAD-006-",
                                                           new Object[] {goodsGroupCode}
                                                          ));

        if (CollectionUtils.isEmpty(csvUploadResult.getCsvUploadErrorlList())) {
            csvUploadResult.setCsvUploadErrorlList(csvUploadErrorList);
        } else {
            csvUploadResult.getCsvUploadErrorlList().addAll(csvUploadErrorList);
        }

        // エラーリストをクリア
        clearErrorList();
    }

    /**
     * CsvアップロードエラーDto作成
     *
     * @param row         行番号
     * @param messageCode メッセージコード
     * @param args        引数
     * @param unitSize    商品規格数（レコードの行数）
     * @return CsvアップロードエラーDto
     */
    protected CsvUploadError createCsvUploadError(Integer row, int unitSize, String messageCode, Object[] args) {
        CsvUploadError csvUploadError = new CsvUploadError();
        csvUploadError.setRow(row);
        csvUploadError.setRowSpan(unitSize);
        csvUploadError.setMessageCode(messageCode);
        csvUploadError.setArgs(args);
        csvUploadError.setMessage(checkMessageUtility.getMessage(messageCode, args));
        return csvUploadError;
    }

    /**
     * Csvアップロード内容のバリデーションチェック
     *
     * @param csvUploadResult Csvアップロード結果
     * @param uploadedCsvFile 商品CSVデータアップロードファイル
     * @param uploadType
     * @return バリデーション結果Dto
     */
    protected boolean validate(CsvUploadResult csvUploadResult, File uploadedCsvFile, HTypeUploadType uploadType) {

        // CSV読み込みオプションを設定する
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        csvReaderOptionDto.setValidLimit(-1);

        List<String> uploadColumnListEng = this.getUploadColumn(uploadedCsvFile);

        List<CsvUploadError> csvUploadValidateErrorList = new ArrayList<>();

        // ヘッダーチェック
        if (!isHeaderValid(uploadedCsvFile, uploadType)) {
            CsvUploadError csvUploadError = new CsvUploadError();
            csvUploadError.setRow(1);
            csvUploadError.setMessage(AppLevelFacesMessageUtil.getAllMessage(CSVUPLOAD_HEADER_VALIDATOR_MSGCD, null).getMessage());

            csvUploadValidateErrorList.add(csvUploadError);
            csvUploadResult.setCsvUploadErrorlList(csvUploadValidateErrorList);
            return true;
        }


        /* Csvファイルを読み込み */
        try (CSVParser csvParser = csvReaderModule.readCsv(
                        uploadedCsvFile.getPath(), GoodsCsvDto.class, csvReaderOptionDto)) {

            if (csvParser == null) {
                // CSV読み込みで有り得ない例外が発生した場合
                csvReaderModule.createUnexpectedExceptionMsg(csvUploadResult);
                return true;
            }

            // バリデータエラーカウンタ
            int errorCounter = 0;

            // csvデータ行数(ヘッダー行を除く)
            int csvRowCount = 0;

            // CSV のレコードを DTO に変換する
            while (csvParser.iterator().hasNext()) {
                csvRowCount++;
                // CSV を DTO に変換する。バリデーションがNGの場合は null が返される
                GoodsCsvDto csvDto = csvReaderModule.convertCsv2Dto(csvParser.iterator().next(), GoodsCsvDto.class);

                if (csvDto == null) {
                    errorCounter++;
                }

                if (HTypeUploadType.MODIFY.equals(uploadType) && csvDto != null) {

                    checkRequiredWhenHeaderPresent(uploadColumnListEng, csvDto, csvRowCount, csvUploadValidateErrorList);

                    checkRequiredUnitManagement(csvDto, csvRowCount, csvUploadValidateErrorList);

                    checkRequiredUnit1(csvDto, csvRowCount, csvUploadValidateErrorList);

                    checkRequiredUnit2(csvDto, csvRowCount, csvUploadValidateErrorList);

                    checkRequiredStockManagement(csvDto, csvRowCount, csvUploadValidateErrorList);
                }
            }
            csvUploadResult.setCsvRowCount(csvRowCount);

            if (CollectionUtils.isNotEmpty(csvUploadValidateErrorList)) {
                csvUploadResult.setCsvUploadErrorlList(csvUploadValidateErrorList);

                // ヘッダー行の1をセット
                csvUploadResult.setRecordCount(csvReaderModule.getRecordCount());

                csvUploadResult.setSuccessCount(0);
                return true;
            }

            // エラーあり
            if (errorCounter > 0) {
                csvUploadResult.setCsvValidationResult(csvReaderModule.getCsvValidationResult());

                // ヘッダー行の1をセット
                csvUploadResult.setRecordCount(csvReaderModule.getRecordCount());

                csvUploadResult.setSuccessCount(0);
                return true;
            }
        } catch (Exception e) {
            // CSV読み込みで有り得ない例外が発生した場合
            csvReaderModule.createUnexpectedExceptionMsg(csvUploadResult);
            return true;
        }

        // エラーなし
        return false;
    }

    /**
     * アップロードされたカラムの取得
     *
     * @param uploadedCsvFile
     * @return アップロードされたカラム
     */
    protected List<String> getUploadColumn(File uploadedCsvFile) {
        CsvReaderOptionDto csvReaderOptionDto = new CsvReaderOptionDto();
        return csvReaderModule.getUploadedCsvHeaderEng(GoodsCsvDto.class, uploadedCsvFile, csvReaderOptionDto);
    }

    /**
     * ヘッダーチェック
     * @param uploadedCsvFile
     * @param uploadType
     * @return false - ヘッダーが無効です
     */
    private boolean isHeaderValid(File uploadedCsvFile, HTypeUploadType uploadType) {
        List<String> expectedCsvHeaderJp = csvReaderModule.getCsvHeader(GoodsCsvDto.class);
        List<String> uploadedCsvHeaderJp = csvReaderModule.getUploadedCsvHeader(uploadedCsvFile, new CsvReaderOptionDto());

        List<String> filteredCsvHeaderJp = csvReaderModule.filterHeaderByOnlyDownload(new ArrayList<>(uploadedCsvHeaderJp), GoodsCsvDto.class);

        if (HTypeUploadType.REGIST == uploadType) {
            return validateRestricted(expectedCsvHeaderJp, filteredCsvHeaderJp);
        }

        return validateUnrestricted(expectedCsvHeaderJp, filteredCsvHeaderJp);
    }

    /**
     * 部分アップロードを禁止する場合のヘッダー検証
     *
     * @param expectedCsvHeader 完全なヘッダーリスト
     * @param uploadedCsvHeader アップロードされたCSVのヘッダーリスト
     * @return false - ヘッダが一致しない場合
     */
    private boolean validateRestricted(List<String> expectedCsvHeader, List<String> uploadedCsvHeader) {

        List<String> diff = DiffUtil.diff(expectedCsvHeader, uploadedCsvHeader);

        if (!diff.isEmpty()) {

            for (String d : diff) {
                LOGGER.debug("HEADER-Csv Header diferrence:" + d);
            }

            return false;
        }

        return true;
    }

    /**
     * 部分アップロードを禁止しない場合のヘッダー検証
     *
     * @param expectedCsvHeader 完全なヘッダーリスト
     * @param uploadedCsvHeader アップロードされたCSVのヘッダーリスト
     * @return false - アップロード可能なヘッダが一つも含まれていない
     */
    private boolean validateUnrestricted(List<String> expectedCsvHeader, List<String> uploadedCsvHeader) {

        boolean found = false;

        for (String shouldExist : uploadedCsvHeader) {
            if (expectedCsvHeader.contains(shouldExist)) {
                found = true;
                break;
            }
        }

        return found;
    }


    /**
     * ヘッダーが存在する場合は必須チェック
     * @param uploadedCsvHeaderEng
     * @param csvDto
     * @param rowCount
     * @param csvUploadErrorList
     */
    private void checkRequiredWhenHeaderPresent(List<String> uploadedCsvHeaderEng, GoodsCsvDto csvDto, int rowCount, List<CsvUploadError> csvUploadErrorList) {

        if (uploadedCsvHeaderEng.contains("goodsGroupName") && csvDto.getGoodsGroupName() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"商品名"});
        }
        if (uploadedCsvHeaderEng.contains("goodsOpenStatusPC") && csvDto.getGoodsOpenStatusPC() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"公開状態"});
        }
        if (uploadedCsvHeaderEng.contains("whatsnewDate") && csvDto.getWhatsnewDate() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"新着日付"});
        }
        if (uploadedCsvHeaderEng.contains("deliveryType") && csvDto.getDeliveryType() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"商品納期"});
        }
        if (uploadedCsvHeaderEng.contains("noveltyGoodsType") && csvDto.getNoveltyGoodsType() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"ノベルティ商品フラグ"});
        }
        if (uploadedCsvHeaderEng.contains("alcoholFlag") && csvDto.getAlcoholFlag() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"酒類フラグ"});
        }
        if (uploadedCsvHeaderEng.contains("snsLinkFlag") && csvDto.getSnsLinkFlag() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"SNS連携フラグ"});
        }
        if (uploadedCsvHeaderEng.contains("goodsPrice") && (csvDto.getGoodsPrice() == null || csvDto.getGoodsPrice().compareTo(BigDecimal.ZERO) <= 0)) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"価格_税抜"});
        }
        if (uploadedCsvHeaderEng.contains("taxRate") && csvDto.getTaxRate() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"税率"});
        }
        if (uploadedCsvHeaderEng.contains("unitManagementFlag") && csvDto.getUnitManagementFlag() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"規格表示"});
        }
        if (uploadedCsvHeaderEng.contains("purchasedMax") && (csvDto.getPurchasedMax() == null || csvDto.getPurchasedMax().compareTo(BigDecimal.ZERO) <= 0)) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"注文上限"});
        }
        if (uploadedCsvHeaderEng.contains("saleStatusPC") && csvDto.getSaleStatusPC() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"販売状態"});
        }
        if (uploadedCsvHeaderEng.contains("individualDeliveryType") && csvDto.getIndividualDeliveryType() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"個別配送"});
        }
        if (uploadedCsvHeaderEng.contains("freeDeliveryFlag") && csvDto.getFreeDeliveryFlag() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"送料区分"});
        }
        if (uploadedCsvHeaderEng.contains("stockManagementFlag") && csvDto.getStockManagementFlag() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_VALIDATE_REQUIRED, new Object[]{"在庫設定"});
        }

    }

    /**
     * 標準管理の利用可能チェック
     * @param csvDto
     * @param rowCount
     * @param csvUploadErrorList
     */
    private void checkRequiredUnitManagement(GoodsCsvDto csvDto, int rowCount, List<CsvUploadError> csvUploadErrorList) {
        if (HTypeUnitManagementFlag.ON == csvDto.getUnitManagementFlag() && csvDto.getUnitTitle1() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"規格表示", "規格1表示名"});
        }
    }

    /**
     * 商品グループの規格1表示名が設定チェック
     * @param csvDto
     * @param rowCount
     * @param csvUploadErrorList
     */
    private void checkRequiredUnit1(GoodsCsvDto csvDto, int rowCount, List<CsvUploadError> csvUploadErrorList) {
        if (csvDto.getUnitTitle1() != null && csvDto.getUnitValue1() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"規格1表示名", "規格1"});
        }
    }

    /**
     * 商品グループの規格2表示名が設定チェック
     * @param csvDto
     * @param rowCount
     * @param csvUploadErrorList
     */
    private void checkRequiredUnit2(GoodsCsvDto csvDto, int rowCount, List<CsvUploadError> csvUploadErrorList) {
        if (csvDto.getUnitTitle2() != null && csvDto.getUnitValue2() == null) {
            addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"規格2表示名", "規格2"});
        }
    }

    /**
     * 在庫管理設定チェック
     * @param csvDto
     * @param rowCount
     * @param csvUploadErrorList
     */
    private void checkRequiredStockManagement(GoodsCsvDto csvDto, int rowCount, List<CsvUploadError> csvUploadErrorList) {
        if (HTypeStockManagementFlag.ON == csvDto.getStockManagementFlag()) {

            if (csvDto.getSafetyStock() == null || csvDto.getSafetyStock().compareTo(BigDecimal.ZERO) <= 0) {
                addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"在庫設定", "安全在庫数"});

            }

            if (csvDto.getRemainderFewStock() == null || csvDto.getRemainderFewStock().compareTo(BigDecimal.ZERO) <= 0) {
                addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"在庫設定", "残少表示在庫数"});

            }

            if (csvDto.getOrderPointStock() == null || csvDto.getOrderPointStock().compareTo(BigDecimal.ZERO) <= 0) {
                addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"在庫設定", "発注点在庫数"});

            }

            if (csvDto.getSupplementCount() == null || csvDto.getSupplementCount().compareTo(BigDecimal.ZERO) <= 0) {
                addValidationMessage(csvUploadErrorList, rowCount, CSVUPLOAD_FIELD_COMBO_VALIDATE_REQUIRED, new Object[]{"在庫設定", "入庫数"});

            }
        }
    }

    /**
     * エラーリスト検証設定
     * @param csvUploadErrorList
     * @param row
     * @param messageId
     * @param args
     */
    private void addValidationMessage(List<CsvUploadError> csvUploadErrorList, int row, String messageId, Object[] args) {
        CsvUploadError csvUploadError = new CsvUploadError();
        csvUploadError.setRow(row+1);
        csvUploadError.setMessage(AppLevelFacesMessageUtil.getAllMessage(messageId, args).getMessage());

        csvUploadErrorList.add(csvUploadError);
    }

}