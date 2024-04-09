/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group.impl;

import jp.co.itechh.quad.core.base.constant.ValidatorConstants;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ArrayFactoryUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeTaxRateType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUploadType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.entity.shop.tax.TaxRateEntity;
import jp.co.itechh.quad.core.logic.shop.tax.TaxGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsCsvUpLoadAsynchronousService;
import jp.co.itechh.quad.core.service.goods.group.GoodsGroupCorrelationDataCheckService;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * 商品グループDto相関チェックサービス
 */
@Service
public class GoodsGroupCorrelationDataCheckServiceImpl extends AbstractShopService
                implements GoodsGroupCorrelationDataCheckService {

    /**
     * 商品-最小金額
     */
    public static final BigDecimal GOODS_PRICE_MIN = BigDecimal.ZERO;

    /**
     * 商品-最大金額
     */
    public static final BigDecimal GOODS_PRICE_MAX = new BigDecimal(99999999);

    /**
     * 全角スペース
     */
    public static final String FULL_WIDTH_SPACE_CHARACTER = "\\u3000";

    /** 制御文字 0x80 - 0x9F \p{Cntrl}には含まれない為独自に定義する */
    public static final String CONTROL_CHARCTER_0X80_0X9F = "\\x80\\x81\\x82\\x83\\x84\\x85\\x86"
                                                            + "\\x87\\x88\\x89\\x8a\\x8b\\x8c\\x8d\\x8e\\x8f\\x90\\x91\\x92\\x93\\x94"
                                                            + "\\x95\\x96\\x97\\x98\\x99\\x9a\\x9b\\x9c\\x9d\\x9e\\x9f";

    /** 特殊文字が含まれない正規表現 */
    public static final String NO_SPECIAL_CHARACTER_REGEX =
                    "[^\\p{Cntrl}\\p{Punct}\\p{Space}" + FULL_WIDTH_SPACE_CHARACTER + CONTROL_CHARCTER_0X80_0X9F + "]*";

    /**
     * 商品Utility
     */
    private final GoodsUtility goodsUtility;

    /**
     * 配列工場ユーティリティクラス
     */
    private final ArrayFactoryUtility arrayFactoryUtility;

    /**
     * 税率情報取得
     */
    private final TaxGetLogic taxGetLogic;

    @Autowired
    public GoodsGroupCorrelationDataCheckServiceImpl(GoodsUtility goodsUtility,
                                                     ArrayFactoryUtility arrayFactoryUtility,
                                                     TaxGetLogic taxGetLogic) {

        this.goodsUtility = goodsUtility;
        this.arrayFactoryUtility = arrayFactoryUtility;
        this.taxGetLogic = taxGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @param goodsGroupDto           商品グループDto
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報
     */
    @Override
    public void execute(GoodsGroupDto goodsGroupDto,
                        List<GoodsRelationEntity> goodsRelationEntityList,
                        int processType,
                        Map<String, Object> commonCsvInfoMap) throws Exception {

        clearErrorList();

        /************************************
         **  入力データ有無チェック
         ************************************/
        initInputDataCheck(goodsGroupDto, processType, commonCsvInfoMap);

        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
        List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList();
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();

        /************************************
         **  キー項目チェック
         ************************************/
        String goodsGroupCode = checkKeyData(processType, commonCsvInfoMap, goodsGroupEntity);

        /************************************
         **  登録カテゴリチェック
         ************************************/
        checkCategoryGoods(goodsGroupDto, processType, commonCsvInfoMap, goodsGroupCode);

        /************************************
         **  関連商品情報チェック
         ************************************/
        checkGoodsRelation(goodsRelationEntityList, processType, commonCsvInfoMap, goodsGroupEntity, goodsGroupCode);

        /************************************
         **  商品グループ情報チェック
         ************************************/
        checkGoodsGroup(processType, commonCsvInfoMap, goodsGroupEntity, goodsGroupCode);

        /************************************
         **  ノベルティ商品チェック
         ************************************/
        checkNovelty(goodsGroupDto);

        /************************************
         **  商品グループ表示情報チェック
         ************************************/
        checkGoodsGroupDisplay(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);

        /************************************
         **  商品規格情報チェック
         ************************************/
        checkGoodsDetail(processType, commonCsvInfoMap, goodsGroupEntity, goodsDtoList, goodsGroupDisplayEntity,
                         goodsGroupCode
                        );

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * 入力データ有無チェック
     *
     * @param goodsGroupDto    商品グループDTO
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param customParams     案件用引数
     */
    protected void initInputDataCheck(GoodsGroupDto goodsGroupDto,
                                      int processType,
                                      Map<String, Object> commonCsvInfoMap,
                                      Object... customParams) {
        // 商品グループDtoなし
        if (ObjectUtils.isEmpty(goodsGroupDto)) {
            throwMessage(processType, commonCsvInfoMap, null, null, "SGP001001");
        }
        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
        List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList();
        // CSVからの場合は、商品規格をCSV処理行順にソートして処理する
        if (processType == PROCESS_TYPE_FROM_CSV) {
            goodsDtoList = sortCsvRecordOrder(commonCsvInfoMap, goodsGroupDto.getGoodsDtoList());
        }
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDto.getGoodsGroupDisplayEntity();
        // 商品グループエンティティなし、商品グループ表示エンティティなし
        if (goodsGroupEntity == null || goodsGroupDisplayEntity == null) {
            throwMessage(processType, commonCsvInfoMap, null, null, "SGP001001");
        }
        // 商品Dtoリストなし
        if (goodsDtoList == null || goodsDtoList.size() == 0) {
            throwMessage(processType, commonCsvInfoMap, null, null, "SGP001002");
        }
    }

    /**
     * キー項目チェック
     *
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupEntity 商品グループエンティティ
     * @param customParams     案件用引数
     * @return 商品グループコード
     */
    protected String checkKeyData(int processType,
                                  Map<String, Object> commonCsvInfoMap,
                                  GoodsGroupEntity goodsGroupEntity,
                                  Object... customParams) {
        // 商品管理番号
        if (goodsGroupEntity.getGoodsGroupCode() == null || goodsGroupEntity.getGoodsGroupCode().length() > 20
            || !Pattern.matches(regPatternForCode, goodsGroupEntity.getGoodsGroupCode())) {
            addErrorMessage(processType, commonCsvInfoMap, null, null, "SGP001003");
        }
        String goodsGroupCode = goodsGroupEntity.getGoodsGroupCode();
        // 商品名
        if (goodsGroupEntity.getGoodsGroupName() == null || goodsGroupEntity.getGoodsGroupName().length() > 120) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001004");
        }
        return goodsGroupCode;
    }

    /**
     * 登録カテゴリチェック
     *
     * @param goodsGroupDto    商品グループDTO
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupCode   商品グループコード
     * @param customParams     案件用引数
     */
    protected void checkCategoryGoods(GoodsGroupDto goodsGroupDto,
                                      int processType,
                                      Map<String, Object> commonCsvInfoMap,
                                      String goodsGroupCode,
                                      Object... customParams) {
        // 登録カテゴリ重複チェック
        List<CategoryGoodsEntity> categoryGoodsEntityList = goodsGroupDto.getCategoryGoodsEntityList();
        // 重複チェック用カテゴリSEQリスト
        List<Integer> categorySeqList = new ArrayList<>();
        if (categoryGoodsEntityList != null) {
            for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
                // 登録カテゴリSEQ重複チェック
                if (categorySeqList.contains(categoryGoodsEntity.getCategorySeq())) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001065",
                                    new Object[] {categoryGoodsEntity.getCategorySeq().toString()}
                                   );
                }
                categorySeqList.add(categoryGoodsEntity.getCategorySeq());
            }
        }
    }

    /**
     * 関連商品チェック
     *
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupEntity        商品グループエンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkGoodsRelation(List<GoodsRelationEntity> goodsRelationEntityList,
                                      int processType,
                                      Map<String, Object> commonCsvInfoMap,
                                      GoodsGroupEntity goodsGroupEntity,
                                      String goodsGroupCode,
                                      Object... customParams) {
        // 重複チェック用商品グループSEQリスト
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        int maxGoodsRelationAmount = Integer.parseInt(PropertiesUtil.getSystemPropertiesValue("goods.relation.amount"));
        // 関連商品登録数上限チェック
        if (goodsRelationEntityList.size() > maxGoodsRelationAmount) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001066",
                            new Object[] {PropertiesUtil.getSystemPropertiesValue("goods.relation.amount")}
                           );
        }
        for (GoodsRelationEntity goodsRelationEntity : goodsRelationEntityList) {
            // 更新時、自身の商品グループでないことを確認する
            if (goodsGroupEntity.getGoodsGroupSeq() != null && goodsGroupEntity.getGoodsGroupSeq()
                                                                               .equals(goodsRelationEntity.getGoodsRelationGroupSeq())) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001067");
            }
            // 関連商品_商品グループSEQ重複チェック
            if (goodsGroupSeqList.contains(goodsRelationEntity.getGoodsRelationGroupSeq())) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001068",
                                new Object[] {goodsRelationEntity.getGoodsGroupCode()}
                               );
            }
            goodsGroupSeqList.add(goodsRelationEntity.getGoodsRelationGroupSeq());
        }
    }

    /**
     * 商品グループチェック
     *
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupEntity 商品グループエンティティ
     * @param goodsGroupCode   商品グループコード
     * @param customParams     案件用引数
     */
    protected void checkGoodsGroup(int processType,
                                   Map<String, Object> commonCsvInfoMap,
                                   GoodsGroupEntity goodsGroupEntity,
                                   String goodsGroupCode,
                                   Object... customParams) {

        // 単価
        if (goodsGroupEntity.getGoodsPrice() == null || goodsGroupEntity.getGoodsPrice().compareTo(GOODS_PRICE_MIN) < 0
            || goodsGroupEntity.getGoodsPrice().compareTo(GOODS_PRICE_MAX) > 0) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, MSGCD_PRICE_LIMIT_OUT,
                            new Object[] {goodsGroupCode}
                           );
        }
        // 商品表示名PC
        if (goodsGroupEntity.getGoodsGroupName() == null || goodsGroupEntity.getGoodsGroupName().length() > 120) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001005");
        }
        // 商品公開状態
        if (goodsGroupEntity.getGoodsOpenStatusPC() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001084");
        }
        // 商品公開開始日時≦商品公開終了日時チェック
        if (goodsGroupEntity.getOpenStartTimePC() != null && goodsGroupEntity.getOpenEndTimePC() != null
            && goodsGroupEntity.getOpenStartTimePC().getTime() > goodsGroupEntity.getOpenEndTimePC().getTime()) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001069");
        }

        // 追加モードでアップロード時、PC,モバイルの公開状態が削除の場合、エラー
        if (processType == PROCESS_TYPE_FROM_CSV) {
            if (HTypeOpenDeleteStatus.DELETED == goodsGroupEntity.getGoodsOpenStatusPC()
                && commonCsvInfoMap.get("uploadType") == HTypeUploadType.REGIST) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001105",
                                new Object[] {goodsGroupEntity.getGoodsGroupCode()}
                               );
            }
        }

        // SNS連携フラグ
        if (goodsGroupEntity.getSnsLinkFlag() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001103");
        }
        // 酒類フラグ
        if (goodsGroupEntity.getAlcoholFlag() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "PKG-4113-003-L-E");
        }
        // ノベルティ商品フラグ
        if (goodsGroupEntity.getNoveltyGoodsType() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "PKG-4113-007-L-E");
        }

        // 税率チェック
        if (goodsGroupEntity.getTaxRate() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, MSGCD_NOSET_TAX_RATE,
                            messageArgsGenerator(MSG_ARGS_TAX_RATE)
                           );
        } else {

            // 税率情報を取得
            Map<HTypeTaxRateType, TaxRateEntity> taxRateMap = taxGetLogic.getEffectiveTaxRateMap();

            boolean taxRateErrorFlg = true;
            for (TaxRateEntity val : taxRateMap.values()) {
                // 施工中の税率に該当する場合、抜ける
                if (val.getRate().compareTo(goodsGroupEntity.getTaxRate()) == 0) {
                    taxRateErrorFlg = false;
                    break;
                }

            }
            // 現在施工中の税率がヒットしない場合エラー
            if (taxRateErrorFlg) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, MSGCD_TAX_RATE_OUT);
            }
        }

    }

    /**
     * ノベルティ商品チェック.
     *
     * @param goodsGroupDto 商品グループDtoクラス
     */
    protected void checkNovelty(GoodsGroupDto goodsGroupDto) {
        GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();

        // ノベルティ商品区分がノベルティ商品の場合は以下チェック
        if (HTypeNoveltyGoodsType.NOVELTY_GOODS.equals(goodsGroupEntity.getNoveltyGoodsType())) {

            // 公開状態「公開」の場合はエラー
            if (HTypeOpenDeleteStatus.OPEN.equals(goodsGroupEntity.getGoodsOpenStatusPC())) {
                addErrorMessage("ITC-0252-012-A-");
            }

            // 公開期間が設定されている場合はエラー
            if (goodsGroupEntity.getOpenStartTimePC() != null || goodsGroupEntity.getOpenEndTimePC() != null) {
                addErrorMessage("ITC-0252-014-A-");
            }

            if (goodsGroupEntity.getGoodsPrice().compareTo(BigDecimal.ZERO) != 0) {
                addErrorMessage("ITC-0252-018-A-");
            }

            List<GoodsDto> goodsDtoList = goodsGroupDto.getGoodsDtoList() != null ?
                            goodsGroupDto.getGoodsDtoList() :
                            new ArrayList<>();
            boolean errorSaleStatusFlag = false;
            boolean errorSaleDateTime = false;
            if (goodsDtoList != null && goodsDtoList.size() > 0) {
                for (GoodsDto goodsDto : goodsDtoList) {
                    GoodsEntity dto = goodsDto.getGoodsEntity();
                    if (HTypeGoodsSaleStatus.DELETED == dto.getSaleStatusPC()) {
                        continue;
                    }
                    if (HTypeStockManagementFlag.OFF == goodsDto.getGoodsEntity().getStockManagementFlag()) {
                        addErrorMessage("ITC-0252-016-A-");
                    }
                    // 販売状態「販売」の場合はエラー
                    if (!errorSaleStatusFlag) {
                        if (HTypeGoodsSaleStatus.SALE == dto.getSaleStatusPC()) {
                            errorSaleStatusFlag = true;
                            addErrorMessage("ITC-0252-013-A-");
                        }
                    }
                    // 販売期間が設定されている場合はエラー
                    if (!errorSaleDateTime) {
                        if (dto.getSaleStartTimePC() != null || dto.getSaleEndTimePC() != null) {
                            errorSaleDateTime = true;
                            addErrorMessage("ITC-0252-015-A-");
                        }
                    }
                    if (HTypeFreeDeliveryFlag.ON.equals(dto.getFreeDeliveryFlag())) {
                        addErrorMessage("ITC-0252-017-A-");
                    }
                    if (HTypeIndividualDeliveryType.ON.equals(dto.getIndividualDeliveryType())) {
                        addErrorMessage("ITC-0252-019-A-");
                    }
                }
            }
        }
    }

    /**
     * 商品グループ表示チェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkGoodsGroupDisplay(int processType,
                                          Map<String, Object> commonCsvInfoMap,
                                          GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                          String goodsGroupCode,
                                          Object... customParams) throws Exception {
        // インフォメーションアイコンチェック
        checkInformationIcon(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
        // 商品タグチェック
        checkProductTag(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
        // キーワードチェック（商品検索キーワード＆metaDescription＆metaKeyword）
        checkKeyword(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
        // 商品情報チェック（商品説明）
        checkGoodsNote(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
        // 商品情報チェック（受注連携設定）
        checkOrderSetting(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
        // 商品納期チェック
        checkDeliveryType(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);

        // 規格情報チェック
        checkUnitInfo(processType, commonCsvInfoMap, goodsGroupDisplayEntity, goodsGroupCode);
    }

    /**
     * インフォメーションアイコンチェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkInformationIcon(int processType,
                                        Map<String, Object> commonCsvInfoMap,
                                        GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                        String goodsGroupCode,
                                        Object... customParams) {
        // インフォメーションアイコンPC
        if (goodsGroupDisplayEntity.getInformationIconPC() != null
            && goodsGroupDisplayEntity.getInformationIconPC().length() > 150) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001072");
        }
    }

    /**
     * 商品タグチェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkProductTag(int processType,
                                   Map<String, Object> commonCsvInfoMap,
                                   GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                   String goodsGroupCode,
                                   Object... customParams) throws Exception {

        if (ObjectUtils.isEmpty(goodsGroupDisplayEntity.getGoodsTag())) {
            return;
        }

        List<String> goodsTagList = arrayFactoryUtility.arrayToList(goodsGroupDisplayEntity.getGoodsTag());

        if (!ListUtils.isEmpty(goodsTagList)) {
            // １商品あたりのタグひもづけ件数が、５０件超過時はエラー
            if (goodsTagList.size() > 50) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001107");
            }

            // 商品タグ１つあたりの、桁数・文字種チェック
            for (String goodsTag : goodsTagList) {
                // １００桁を超えている場合はエラー
                if (goodsTag.length() > 100) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001108");
                }
                // 記号を含んでいた場合エラー
                if (!Pattern.matches(NO_SPECIAL_CHARACTER_REGEX, goodsTag)) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001109");
                }
            }
        }
    }

    /**
     * キーワードチェック（商品検索キーワード＆metaDescription＆metaKeyword）
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkKeyword(int processType,
                                Map<String, Object> commonCsvInfoMap,
                                GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                String goodsGroupCode,
                                Object... customParams) {
        // 商品検索キーワード
        if (goodsGroupDisplayEntity.getSearchKeyword() != null
            && goodsGroupDisplayEntity.getSearchKeyword().length() > 1000) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001011");
        }
        // metaDescription
        if (goodsGroupDisplayEntity.getMetaDescription() != null
            && goodsGroupDisplayEntity.getMetaDescription().length() > 400) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001012");
        }
        // metaKeyword
        if (goodsGroupDisplayEntity.getMetaKeyword() != null
            && goodsGroupDisplayEntity.getMetaKeyword().length() > 200) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001013");
        }
    }

    /**
     * 商品説明チェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkGoodsNote(int processType,
                                  Map<String, Object> commonCsvInfoMap,
                                  GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                  String goodsGroupCode,
                                  Object... customParams) {
        final int MAXIMUM = ValidatorConstants.LENGTH_GOODS_NOTE_MAXIMUM;
        // 商品説明１
        if (goodsGroupDisplayEntity.getGoodsNote1() != null
            && goodsGroupDisplayEntity.getGoodsNote1().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明１", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明２
        if (goodsGroupDisplayEntity.getGoodsNote2() != null
            && goodsGroupDisplayEntity.getGoodsNote2().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明２", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明３
        if (goodsGroupDisplayEntity.getGoodsNote3() != null
            && goodsGroupDisplayEntity.getGoodsNote3().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明３", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明４
        if (goodsGroupDisplayEntity.getGoodsNote4() != null
            && goodsGroupDisplayEntity.getGoodsNote4().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明４", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明５
        if (goodsGroupDisplayEntity.getGoodsNote5() != null
            && goodsGroupDisplayEntity.getGoodsNote5().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明５", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明６
        if (goodsGroupDisplayEntity.getGoodsNote6() != null
            && goodsGroupDisplayEntity.getGoodsNote6().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明６", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明７
        if (goodsGroupDisplayEntity.getGoodsNote7() != null
            && goodsGroupDisplayEntity.getGoodsNote7().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明７", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明８
        if (goodsGroupDisplayEntity.getGoodsNote8() != null
            && goodsGroupDisplayEntity.getGoodsNote8().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明８", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明９
        if (goodsGroupDisplayEntity.getGoodsNote9() != null
            && goodsGroupDisplayEntity.getGoodsNote9().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明９", String.valueOf(MAXIMUM)}
                           );
        }
        // 商品説明１０
        if (goodsGroupDisplayEntity.getGoodsNote10() != null
            && goodsGroupDisplayEntity.getGoodsNote10().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"商品説明１０", String.valueOf(MAXIMUM)}
                           );
        }
    }

    /**
     * 受注連携設定チェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkOrderSetting(int processType,
                                     Map<String, Object> commonCsvInfoMap,
                                     GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                     String goodsGroupCode,
                                     Object... customParams) {
        final int MAXIMUM = ValidatorConstants.LENGTH_GOODS_ORDERSETTING_MAXIMUM;
        // 受注連携設定１
        if (goodsGroupDisplayEntity.getOrderSetting1() != null
            && goodsGroupDisplayEntity.getOrderSetting1().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定１", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定２
        if (goodsGroupDisplayEntity.getOrderSetting2() != null
            && goodsGroupDisplayEntity.getOrderSetting2().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定２", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定３
        if (goodsGroupDisplayEntity.getOrderSetting3() != null
            && goodsGroupDisplayEntity.getOrderSetting3().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定３", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定４
        if (goodsGroupDisplayEntity.getOrderSetting4() != null
            && goodsGroupDisplayEntity.getOrderSetting4().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定４", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定５
        if (goodsGroupDisplayEntity.getOrderSetting5() != null
            && goodsGroupDisplayEntity.getOrderSetting5().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定５", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定６
        if (goodsGroupDisplayEntity.getOrderSetting6() != null
            && goodsGroupDisplayEntity.getOrderSetting6().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定６", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定７
        if (goodsGroupDisplayEntity.getOrderSetting7() != null
            && goodsGroupDisplayEntity.getOrderSetting7().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定７", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定８
        if (goodsGroupDisplayEntity.getOrderSetting8() != null
            && goodsGroupDisplayEntity.getOrderSetting8().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定８", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定９
        if (goodsGroupDisplayEntity.getOrderSetting9() != null
            && goodsGroupDisplayEntity.getOrderSetting9().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定９", String.valueOf(MAXIMUM)}
                           );
        }
        // 受注連携設定１０
        if (goodsGroupDisplayEntity.getOrderSetting10() != null
            && goodsGroupDisplayEntity.getOrderSetting10().length() > MAXIMUM) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001014",
                            new String[] {"受注連携設定１０", String.valueOf(MAXIMUM)}
                           );
        }
    }

    /**
     * 商品納期チェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkDeliveryType(int processType,
                                     Map<String, Object> commonCsvInfoMap,
                                     GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                     String goodsGroupCode,
                                     Object... customParams) {
        // 商品納期
        if (goodsGroupDisplayEntity.getDeliveryType() != null
            && goodsGroupDisplayEntity.getDeliveryType().length() > 50) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001083");
        }
    }

    /**
     * 規格情報チェック
     *
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkUnitInfo(int processType,
                                 Map<String, Object> commonCsvInfoMap,
                                 GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                 String goodsGroupCode,
                                 Object... customParams) {
        // 規格管理フラグ
        if (goodsGroupDisplayEntity.getUnitManagementFlag() == null) {
            addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001031");
        }
        // 規格管理フラグ
        if (HTypeUnitManagementFlag.ON == goodsGroupDisplayEntity.getUnitManagementFlag()) {
            // 規格１表示名
            if (goodsGroupDisplayEntity.getUnitTitle1() == null
                || goodsGroupDisplayEntity.getUnitTitle1().length() > 100) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001032");
            }
            // 規格２表示名
            if (goodsGroupDisplayEntity.getUnitTitle2() != null
                && goodsGroupDisplayEntity.getUnitTitle2().length() > 100) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001033");
            }
        } else if (HTypeUnitManagementFlag.OFF == goodsGroupDisplayEntity.getUnitManagementFlag()) {
            // 規格１表示名
            if (goodsGroupDisplayEntity.getUnitTitle1() != null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001034");
            }
            // 規格２表示名
            if (goodsGroupDisplayEntity.getUnitTitle2() != null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001035");
            }
        }
    }

    /**
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupEntity        商品グループエンティティ
     * @param goodsDtoList            商品DTOリスト
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param goodsGroupCode          商品グループコード
     * @param customParams            案件用引数
     */
    protected void checkGoodsDetail(int processType,
                                    Map<String, Object> commonCsvInfoMap,
                                    GoodsGroupEntity goodsGroupEntity,
                                    List<GoodsDto> goodsDtoList,
                                    GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                    String goodsGroupCode,
                                    Object... customParams) {
        Set<String> goodsCodeSet = new HashSet<>();
        boolean existUnit = false;
        boolean existUnitFail = false;
        boolean stockManagementFlagFail = false;
        boolean individualDeliveryTypeFail = false;
        boolean freeDeliveryFlagFail = false;

        // 規格 / 表示順重複チェック用
        Set<String> unitvalues = new HashSet<>();
        Set<Integer> goodsGroupCodeOrderDisplays = new HashSet<>();

        for (int i = 0; i < goodsDtoList.size(); i++) {
            GoodsDto goodsDto = goodsDtoList.get(i);
            GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
            // 規格が存在しない場合は、以下のチェックをスキップ
            if (goodsEntity == null) {
                continue;
            }

            String goodsCode = goodsEntity.getGoodsCode();
            // 商品規格情報不正
            if (goodsEntity.getGoodsCode() == null) {
                addErrorMessage(processType, commonCsvInfoMap, null, null, "SGP001077",
                                new Object[] {Integer.valueOf(i + 1).toString()}
                               );
                continue;
            }

            // 追加モードでPC,モバイルの販売状態が削除の場合、エラー
            if (processType == PROCESS_TYPE_FROM_CSV) {
                if (HTypeGoodsSaleStatus.DELETED == goodsEntity.getSaleStatusPC()
                    && commonCsvInfoMap.get("uploadType") == HTypeUploadType.REGIST) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001106",
                                    new Object[] {goodsCode}
                                   );
                }
            }

            // 削除状態の場合は、以下のチェックをスキップ
            if (HTypeGoodsSaleStatus.DELETED == goodsEntity.getSaleStatusPC()) {
                continue;
            }
            if (!existUnitFail && existUnit
                && HTypeUnitManagementFlag.OFF == goodsGroupDisplayEntity.getUnitManagementFlag()) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001036");
                existUnitFail = true;
            } else {
                existUnit = true;
            }
            // 商品番号重複チェック
            if (goodsEntity != null) {
                if (goodsCodeSet.contains(goodsEntity.getGoodsCode())) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001037",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                } else {
                    goodsCodeSet.add(goodsEntity.getGoodsCode());
                }
            }
            // 個別配送
            if (!individualDeliveryTypeFail && goodsEntity.getIndividualDeliveryType() == null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001041",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
                individualDeliveryTypeFail = true;
            }
            // 送料
            if (!freeDeliveryFlagFail && goodsEntity.getFreeDeliveryFlag() == null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001042",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
                freeDeliveryFlagFail = true;
            }
            // 商品番号
            if (goodsEntity.getGoodsCode() == null || goodsEntity.getGoodsCode().length() > 20 || !Pattern.matches(
                            regPatternForCode, goodsEntity.getGoodsCode())) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001043",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
            }
            // 規格表示順
            if (goodsEntity.getOrderDisplay() == null || goodsEntity.getOrderDisplay().compareTo(Integer.valueOf(1)) < 0
                || goodsEntity.getOrderDisplay().compareTo(Integer.valueOf(9999)) > 0) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001099",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
            }
            // JANコード
            if (goodsEntity.getJanCode() != null && (goodsEntity.getJanCode().length() > 16 || !Pattern.matches(
                            regPatternForJanCode, goodsEntity.getJanCode()))) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001044",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
            }
            // 規格情報
            if (HTypeUnitManagementFlag.ON == goodsGroupDisplayEntity.getUnitManagementFlag()) {
                if (goodsEntity.getUnitValue1() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001046",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                }
                if (goodsGroupDisplayEntity.getUnitTitle2() != null && goodsEntity.getUnitValue2() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001047",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                }
                if (goodsGroupDisplayEntity.getUnitTitle2() == null && goodsEntity.getUnitValue2() != null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001048",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                }
            } else if (HTypeUnitManagementFlag.OFF == goodsGroupDisplayEntity.getUnitManagementFlag()) {
                if (goodsEntity.getUnitValue1() != null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001049",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                }
                if (goodsEntity.getUnitValue2() != null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001050",
                                    new Object[] {goodsEntity.getGoodsCode()}
                                   );
                }
            }

            // 規格削除でない場合 規格名称重複 / 表示順 重複チェックを実施
            if (goodsEntity.getSaleStatusPC() != HTypeGoodsSaleStatus.DELETED) {

                // 規格重複チェック
                String unitValue = goodsUtility.createUnitValue(goodsEntity);
                if (unitvalues.contains(unitValue)) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001102",
                                    new Object[] {goodsCode}
                                   );
                } else {
                    unitvalues.add(unitValue);
                }

                // 規格表示順の重複チェック
                // 商品販売状態=削除の場合以外、重複チェック対象
                if (goodsEntity.getOrderDisplay() != null) {
                    if (goodsGroupCodeOrderDisplays.contains(goodsEntity.getOrderDisplay())) {
                        addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode,
                                        GoodsCsvUpLoadAsynchronousService.OREDERDISPLAY_DUPLICATE_FAIL,
                                        new Object[] {goodsGroupEntity.getGoodsGroupCode(),
                                                        goodsEntity.getOrderDisplay()}
                                       );
                    } else {
                        goodsGroupCodeOrderDisplays.add(goodsEntity.getOrderDisplay());
                    }
                }

            }

            // 販売状態PC
            if (goodsEntity.getSaleStatusPC() == null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001051",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
            }
            // 販売開始日時PC≦販売終了日時PCチェック
            if (goodsEntity.getSaleStartTimePC() != null && goodsEntity.getSaleEndTimePC() != null
                && goodsEntity.getSaleStartTimePC().getTime() > goodsEntity.getSaleEndTimePC().getTime()) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001070");
            }

            // 最大購入可能数
            if (goodsEntity.getPurchasedMax() == null || goodsEntity.getPurchasedMax().compareTo(new BigDecimal(1)) < 0
                || goodsEntity.getPurchasedMax().compareTo(new BigDecimal(9999)) > 0) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001057",
                                new Object[] {goodsEntity.getGoodsCode()}
                               );
            }

            // 在庫管理フラグ
            if (!stockManagementFlagFail && goodsDto.getGoodsEntity().getStockManagementFlag() == null) {
                addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, null, "SGP001058");
                stockManagementFlagFail = true;
            }

            // 商品在庫
            StockDto stockDto = goodsDto.getStockDto();
            if (HTypeStockManagementFlag.ON == goodsDto.getGoodsEntity().getStockManagementFlag()) {
                // 残少表示在庫数
                if (stockDto.getRemainderFewStock() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001059",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 発注点在庫数
                if (stockDto.getOrderPointStock() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001060",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 安全在庫数
                if (stockDto.getSafetyStock() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001061",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 入庫数
                if (stockDto.getSupplementCount() == null) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "GOODS-CSV-STOCK-001-",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
            } else if (HTypeStockManagementFlag.OFF == goodsDto.getGoodsEntity().getStockManagementFlag()) {
                // 残少表示在庫数
                if (stockDto.getRemainderFewStock() != null && !stockDto.getRemainderFewStock()
                                                                        .equals(new BigDecimal(0))) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001062",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 発注点在庫数
                if (stockDto.getOrderPointStock() != null && !stockDto.getOrderPointStock().equals(new BigDecimal(0))) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001063",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 安全在庫数
                if (stockDto.getSafetyStock() != null && !stockDto.getSafetyStock().equals(new BigDecimal(0))) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "SGP001064",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
                // 入庫数
                if (stockDto.getSupplementCount() != null && !stockDto.getSupplementCount().equals(new BigDecimal(0))) {
                    addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, "GOODS-CSV-STOCK-002-",
                                    new Object[] {goodsDto.getGoodsEntity().getGoodsCode()}
                                   );
                }
            }
        }
    }

    /**
     * 処理種別=CSV用の規格情報ソート
     * CSV処理行の順に並べる。
     *
     * @param commonCsvInfoMap CSVアップロード共通情報
     * @param goodsDtoList     商品Dtoリスト
     * @return CSV処理行順に並んだ商品Dto(CSVにない規格は商品 ． 表示順の昇順)
     */
    protected List<GoodsDto> sortCsvRecordOrder(Map<String, Object> commonCsvInfoMap, List<GoodsDto> goodsDtoList) {

        // 商品コード：CSV処理行の対応マップ
        @SuppressWarnings("unchecked")
        Map<String, Integer> goodsCodeAndRecodeCountMap =
                        (Map<String, Integer>) commonCsvInfoMap.get("goodsCodeAndRecodeCountMap");

        // ソート後の商品Dto
        List<GoodsDto> sortedGoodsDtoList = new ArrayList<>();
        // Key=CSV処理行, Value=商品DtoのTreeMap
        TreeMap<Integer, GoodsDto> tmap = new TreeMap<>();
        // CSV処理対象外の商品Dtoリスト
        List<GoodsDto> noUpdateGoodsDtoList = new ArrayList<>();

        for (GoodsDto goodsDto : goodsDtoList) {
            if (goodsCodeAndRecodeCountMap.get(goodsDto.getGoodsEntity().getGoodsCode()) != null) {
                tmap.put(goodsCodeAndRecodeCountMap.get(goodsDto.getGoodsEntity().getGoodsCode()), goodsDto);
            } else {
                noUpdateGoodsDtoList.add(goodsDto);
            }
        }

        for (Iterator<Map.Entry<Integer, GoodsDto>> iterator = tmap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<Integer, GoodsDto> entry = iterator.next();
            sortedGoodsDtoList.add(entry.getValue());
        }
        sortedGoodsDtoList.addAll(noUpdateGoodsDtoList);
        return sortedGoodsDtoList;
    }

    /**
     * エラーメッセージスロー処理
     * 画面からの場合は、そのままメッセージをスロー。
     * CSVからの場合は、商品管理番号・商品番号・行番号をエラーパラメータの末尾に追加してメッセージをスローする。
     *
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupCode   商品管理番号（処理種別=画面の場合null）
     * @param goodsCode        商品番号（処理種別=画面の場合null）
     * @param errorCode        エラーコード
     */
    protected void throwMessage(int processType,
                                Map<String, Object> commonCsvInfoMap,
                                String goodsGroupCode,
                                String goodsCode,
                                String errorCode) {

        // エラーパラメータ
        Object[] args = null;

        // CSVからの場合、行番号を取得する
        if (processType == PROCESS_TYPE_FROM_CSV) {

            // エラーパラメータ末尾に追加
            args = new Object[] {goodsGroupCode, goodsCode};
        }

        super.throwMessage(errorCode, args);
    }

    /**
     * エラーメッセージ追加処理
     * 画面からの場合は、そのままメッセージを設定。
     * CSVからの場合は、商品管理番号・商品番号をエラーパラメータの末尾に追加してメッセージを設定する。
     *
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupCode   商品管理番号（処理種別=画面の場合null）
     * @param goodsCode        商品番号（処理種別=画面の場合null）
     * @param errorCode        エラーコード
     * @param args             エラーパラメータ
     */
    protected void addErrorMessage(int processType,
                                   Map<String, Object> commonCsvInfoMap,
                                   String goodsGroupCode,
                                   String goodsCode,
                                   String errorCode,
                                   Object[] args) {

        // CSVからの場合、行番号を取得する
        if (processType == PROCESS_TYPE_FROM_CSV) {

            // エラーパラメータ末尾に追加
            if (args == null) {
                args = new Object[] {goodsGroupCode, goodsCode};
            } else {
                Object[] args2 = new Object[args.length + 2];
                for (int i = 0; i < args.length; i++) {
                    args2[i] = args[i];
                }
                args2[args.length] = goodsGroupCode;
                args2[args.length + 1] = goodsCode;
                args = args2;
            }
        }
        super.addErrorMessage(errorCode, args);
    }

    /**
     * エラーメッセージ追加処理
     * 画面からの場合は、そのままメッセージを設定。
     * CSVからの場合は、商品管理番号・商品番号をパラメータに追加してメッセージを設定する。
     *
     * @param processType      処理種別（画面/CSV）
     * @param commonCsvInfoMap CSVアップロード共通情報（処理種別=画面の場合null）
     * @param goodsGroupCode   商品管理番号（処理種別=画面の場合null）
     * @param goodsCode        商品番号（処理種別=画面の場合null）
     * @param errorCode        エラーコード
     */
    protected void addErrorMessage(int processType,
                                   Map<String, Object> commonCsvInfoMap,
                                   String goodsGroupCode,
                                   String goodsCode,
                                   String errorCode) {

        this.addErrorMessage(processType, commonCsvInfoMap, goodsGroupCode, goodsCode, errorCode, null);
    }

    /**
     * メッセージの引数を生成する
     *
     * @param args メッセージ引数
     * @return 生成された引数
     */
    protected Object[] messageArgsGenerator(String... args) {
        if (args == null || args.length == 0) {
            return new Object[] {};
        }

        List<Object> objectList = new ArrayList<>();
        for (String arg : args) {
            objectList.add(arg);
        }

        return objectList.toArray();
    }
}