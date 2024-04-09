/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.admin.dto.goods.stock.StockDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理：商品登録更新確認ページDxo<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsRegistUpdateConfirmHelper extends AbstractGoodsRegistUpdateHelper {

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility            変換Utility
     */
    @Autowired
    public GoodsRegistUpdateConfirmHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面表示・再表示<br/>
     * 初期表示情報をページクラスにセット<br />
     *
     * @param goodsRegistUpdateModel
     */
    public void toPageForLoad(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループ画像登録更新用DTOリスト（ページ内表示用）の作成
        initTmpGoodsGroupImageRegistUpdateDtoList(goodsRegistUpdateModel);

        // 商品共通情報部分セット
        setGoodsIndex(goodsRegistUpdateModel);

        // 商品詳細部分セット
        setGoodsDetails(goodsRegistUpdateModel);

        // 商品詳細テキスト部分セット
        setGoodsDetailstext(goodsRegistUpdateModel);

        // 商品画像部分セット
        setGoodsImage(goodsRegistUpdateModel);

        // 商品規格部分セット
        setGoodsUnit(goodsRegistUpdateModel);

        // 商品在庫部分セット
        setGoodsStock(goodsRegistUpdateModel);

        // 関連商品部分セット
        setGoodsRelation(goodsRegistUpdateModel);
    }

    /**
     * 商品基本情報入力部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 商品グループエンティティと共通商品エンティティから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams
     */
    protected void setGoodsIndex(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 商品グループエンティティ
        GoodsGroupEntity goodsGroup = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity();

        // 共通商品エンティティ
        GoodsEntity goods = goodsRegistUpdateModel.getCommonGoodsEntity();

        // 商品グループコード
        goodsRegistUpdateModel.setGoodsGroupCode(goodsGroup.getGoodsGroupCode());

        // 商品グループ名
        goodsRegistUpdateModel.setGoodsGroupName(goodsGroup.getGoodsGroupName());

        // 登録日時
        goodsRegistUpdateModel.setRegistTime(goodsGroup.getRegistTime());

        // 更新日時
        goodsRegistUpdateModel.setUpdateTime(goodsGroup.getUpdateTime());

        // 新着日時
        if (goodsGroup.getWhatsnewDate() != null) {
            goodsRegistUpdateModel.setWhatsnewDate(conversionUtility.toYmd(goodsGroup.getWhatsnewDate()));
        }
        // 価格(税抜)
        goodsRegistUpdateModel.setGoodsPrice(String.valueOf(goodsGroup.getGoodsPrice()));
        // 税率
        goodsRegistUpdateModel.setTaxRate(goodsGroup.getTaxRate());

        // 酒類フラグ
        goodsRegistUpdateModel.setAlcoholFlag(EnumTypeUtil.getValue(goodsGroup.getAlcoholFlag()));

        // ノベルティ商品フラグ
        goodsRegistUpdateModel.setNoveltyGoodsType(EnumTypeUtil.getValue(goodsGroup.getNoveltyGoodsType()));

        // SNS連携フラグ
        goodsRegistUpdateModel.setSnsLinkFlag(EnumTypeUtil.getValue(goodsGroup.getSnsLinkFlag()));

        // 個別配送
        goodsRegistUpdateModel.setIndividualDeliveryType(goods.getIndividualDeliveryType().getValue());

        // 無料配送
        goodsRegistUpdateModel.setFreeDeliveryFlag(goods.getFreeDeliveryFlag().getValue());

        // 公開状態PC
        goodsRegistUpdateModel.setGoodsOpenStatusPC(goodsGroup.getGoodsOpenStatusPC().getValue());

        // 公開開始日PC
        goodsRegistUpdateModel.setOpenStartDatePC(goodsGroup.getOpenStartTimePC());

        // 公開開始時間PC
        goodsRegistUpdateModel.setOpenStartTimePC(goodsGroup.getOpenStartTimePC());

        // 公開終了日PC
        goodsRegistUpdateModel.setOpenEndDatePC(goodsGroup.getOpenEndTimePC());

        // 公開終了時間PC
        goodsRegistUpdateModel.setOpenEndTimePC(goodsGroup.getOpenEndTimePC());
    }

    /**
     * 商品詳細設定部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 商品グループエンティティと商品グループ表示エンティティから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsDetails(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 検索キーワード
        goodsRegistUpdateModel.setSearchKeyword(goodsGroupDisplay.getSearchKeyword());

        // 商品インフォメーションアイコン情報
        goodsRegistUpdateModel.setGoodsInformationIconItems(makeInformationIconList(goodsRegistUpdateModel));
        // metaDescription
        goodsRegistUpdateModel.setMetaDescription(goodsGroupDisplay.getMetaDescription());

        // metaKeyword
        goodsRegistUpdateModel.setMetaKeyword(goodsGroupDisplay.getMetaKeyword());
    }

    /**
     * 商品詳細テキスト部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 商品グループ表示エンティティから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsDetailstext(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品説明１
        goodsRegistUpdateModel.setGoodsNote1(goodsGroupDisplay.getGoodsNote1());
        // 商品説明２
        goodsRegistUpdateModel.setGoodsNote2(goodsGroupDisplay.getGoodsNote2());
        // 商品説明３
        goodsRegistUpdateModel.setGoodsNote3(goodsGroupDisplay.getGoodsNote3());
        // 商品説明４
        goodsRegistUpdateModel.setGoodsNote4(goodsGroupDisplay.getGoodsNote4());
        // 商品説明５
        goodsRegistUpdateModel.setGoodsNote5(goodsGroupDisplay.getGoodsNote5());
        // 商品説明６
        goodsRegistUpdateModel.setGoodsNote6(goodsGroupDisplay.getGoodsNote6());
        // 商品説明７
        goodsRegistUpdateModel.setGoodsNote7(goodsGroupDisplay.getGoodsNote7());
        // 商品説明８
        goodsRegistUpdateModel.setGoodsNote8(goodsGroupDisplay.getGoodsNote8());
        // 商品説明９
        goodsRegistUpdateModel.setGoodsNote9(goodsGroupDisplay.getGoodsNote9());
        // 商品説明１０
        goodsRegistUpdateModel.setGoodsNote10(goodsGroupDisplay.getGoodsNote10());
        // 受注連携設定１
        goodsRegistUpdateModel.setOrderSetting1(goodsGroupDisplay.getOrderSetting1());
        // 受注連携設定２
        goodsRegistUpdateModel.setOrderSetting2(goodsGroupDisplay.getOrderSetting2());
        // 受注連携設定３
        goodsRegistUpdateModel.setOrderSetting3(goodsGroupDisplay.getOrderSetting3());
        // 受注連携設定４
        goodsRegistUpdateModel.setOrderSetting4(goodsGroupDisplay.getOrderSetting4());
        // 受注連携設定５
        goodsRegistUpdateModel.setOrderSetting5(goodsGroupDisplay.getOrderSetting5());
        // 受注連携設定６
        goodsRegistUpdateModel.setOrderSetting6(goodsGroupDisplay.getOrderSetting6());
        // 受注連携設定７
        goodsRegistUpdateModel.setOrderSetting7(goodsGroupDisplay.getOrderSetting7());
        // 受注連携設定８
        goodsRegistUpdateModel.setOrderSetting8(goodsGroupDisplay.getOrderSetting8());
        // 受注連携設定９
        goodsRegistUpdateModel.setOrderSetting9(goodsGroupDisplay.getOrderSetting9());
        // 受注連携設定１０
        goodsRegistUpdateModel.setOrderSetting10(goodsGroupDisplay.getOrderSetting10());

        // 商品納期
        goodsRegistUpdateModel.setDeliveryType(goodsGroupDisplay.getDeliveryType());
    }

    /**
     * 商品画像部分のセット<br/>
     *
     * <pre>
     * 商品グループ画像のパスを取得し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsImage(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {
        // 詳細画像に関する設定
        setDetailsImageInfo(goodsRegistUpdateModel);
    }

    /**
     * 商品規格部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 商品グループエンティティと商品グループ表示エンティティと商品DTOリストから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsUnit(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplay =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList();

        // 規格管理フラグ
        if (goodsGroupDisplay.getUnitManagementFlag() != null) {
            goodsRegistUpdateModel.setUnitManagementFlag(goodsGroupDisplay.getUnitManagementFlag().getValue());
        }

        // 規格１表示名
        goodsRegistUpdateModel.setUnitTitle1(goodsGroupDisplay.getUnitTitle1());

        // 規格２表示名
        goodsRegistUpdateModel.setUnitTitle2(goodsGroupDisplay.getUnitTitle2());

        // 商品規格リスト情報
        // 商品規格情報リストのセット
        int index = 0;
        List<GoodsRegistUpdateUnitItem> unitItemList = new ArrayList<>();
        // 条件を満たすことはないのでデッドコードになっている
        if (goodsDtoList == null || goodsDtoList.size() == 0) {
            return;
        }
        for (GoodsDto goodsDto : goodsDtoList) {
            if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()
                || goodsDto.getGoodsEntity().getGoodsCode() == null) {
                // ステータス削除の場合は飛ばす(商品コードがnullの場合も)
                continue;
            }
            GoodsRegistUpdateUnitItem unitItem = ApplicationContextUtility.getBean(GoodsRegistUpdateUnitItem.class);
            unitItem.setUnitDspNo(++index);
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
            unitItem.setPurchasedMax(String.valueOf(goodsDto.getGoodsEntity().getPurchasedMax()));
            unitItemList.add(unitItem);
        }
        goodsRegistUpdateModel.setConfirmUnitItems(unitItemList);
    }

    /**
     * 商品在庫部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 共通商品エンティティと商品DTOリストから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsStock(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 共通商品エンティティ
        GoodsEntity goods = goodsRegistUpdateModel.getCommonGoodsEntity();
        // 商品DTOリスト
        List<GoodsDto> goodsDtoList = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList();

        // 在庫管理フラグ
        if (goods.getStockManagementFlag() != null) {
            goodsRegistUpdateModel.setStockManagementFlag(goods.getStockManagementFlag().getValue());
        }
        // 商品規格情報リストのセット
        int index = 0;
        List<GoodsRegistUpdateStockItem> stockItemList = new ArrayList<>();
        // 条件を満たすことはないのでデッドコードになっている
        if (goodsDtoList == null || goodsDtoList.size() == 0) {
            return;
        }
        for (GoodsDto goodsDto : goodsDtoList) {
            if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()
                || goodsDto.getGoodsEntity().getGoodsCode() == null) {
                // ステータス削除の場合は飛ばす(商品番号がnullの場合も)
                continue;
            }
            GoodsRegistUpdateStockItem stockItem = ApplicationContextUtility.getBean(GoodsRegistUpdateStockItem.class);
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
                if (goodsDto.getStockDto().getSupplementCount() != null) {
                    stockItem.setSupplementCount(String.valueOf(goodsDto.getStockDto().getSupplementCount()));
                }
                stockItem.setRemainderFewStock(String.valueOf(goodsDto.getStockDto().getRemainderFewStock()));
                stockItem.setOrderPointStock(String.valueOf(goodsDto.getStockDto().getOrderPointStock()));
                stockItem.setSafetyStock(String.valueOf(goodsDto.getStockDto().getSafetyStock()));
                stockItem.setRealStock(goodsDto.getStockDto().getRealStock());
            }
            stockItemList.add(stockItem);
        }
        goodsRegistUpdateModel.setStockItems(stockItemList);
    }

    /**
     * 商品関連商品部分のセット<br/>
     *
     * <pre>
     * 商品管理：商品登録更新確認ページが持つ、
     * 関連商品エンティティリストから必要な値を取出し、
     * 商品管理・商品登録更新確認ページへセットする。
     * </pre>
     *
     * @param goodsRegistUpdateModel
     * @param customParams           案件用引数
     */
    protected void setGoodsRelation(GoodsRegistUpdateModel goodsRegistUpdateModel, Object... customParams) {

        // 関連商品エンティティリスト
        List<GoodsRelationEntity> goodsRelationList = goodsRegistUpdateModel.getGoodsRelationEntityList();
        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        // 関連商品情報リストのセット
        int index = 0;
        List<GoodsRegistUpdateRelationItem> relationItems = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(goodsRelationList)) {
            for (GoodsRelationEntity goodsRelationEntity : goodsRelationList) {
                GoodsRegistUpdateRelationItem relationItem =
                                ApplicationContextUtility.getBean(GoodsRegistUpdateRelationItem.class);
                relationItem.setRelationDspNo(++index);
                relationItem.setRelationZenkakuNo(zenHanConversionUtility.toZenkaku(Integer.toString(index)));
                relationItem.setRelationGoodsGroupCode(goodsRelationEntity.getGoodsGroupCode());
                relationItem.setRelationGoodsGroupName(goodsRelationEntity.getGoodsGroupName());
                relationItems.add(relationItem);
            }
        }
        goodsRegistUpdateModel.setRelationItems(relationItems);

        // ダミー用関連商品（空）を作成する
        List<GoodsRegistUpdateRelationItem> dummyRelationItems = new ArrayList<>();
        for (int i = ++index; i <= goodsRegistUpdateModel.getGoodsRelationAmount(); i++) {
            GoodsRegistUpdateRelationItem relationItem =
                            ApplicationContextUtility.getBean(GoodsRegistUpdateRelationItem.class);
            relationItem.setRelationDspNo(i);
            dummyRelationItems.add(relationItem);
        }
        goodsRegistUpdateModel.setRelationNoItems(dummyRelationItems);
    }

    /**
     * 商品インフォメーションアイコン情報リスト編集（画面表示用）<br/>
     *
     * @param goodsRegistUpdateModel
     * @return 商品インフォメーションアイコン情報リスト
     */
    public List<GoodsRegistUpdateIconItem> makeInformationIconList(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループ表示エンティティ
        GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                        goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupDisplayEntity();

        // 商品インフォメーションアイコン情報を取得
        List<String> informationIconPC = new ArrayList<>();
        if (goodsGroupDisplayEntity.getInformationIconPC() != null || "".equals(
                        goodsGroupDisplayEntity.getInformationIconPC())) {
            informationIconPC = Arrays.asList(goodsRegistUpdateModel.getGoodsGroupDto()
                                                                    .getGoodsGroupDisplayEntity()
                                                                    .getInformationIconPC()
                                                                    .split("/"));
        }

        // ページ情報にセットされている商品インフォメーション情報のリスト（アイコンSEQ単位のリスト）から
        // 商品アイコン種別単位のリストを作成しています
        List<GoodsRegistUpdateIconItem> retList = new ArrayList<>();
        for (GoodsInformationIconDto iconDto : goodsRegistUpdateModel.getIconList()) {
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

    /**
     * 画像に関する登録更新情報を整理
     *
     * @param goodsRegistUpdateModel
     */
    public void toSetImageInfo(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品グループ画像
        setGoodsGroupImageRegistUpdateList(goodsRegistUpdateModel);
    }

    /**
     * 登録更新情報を整理(商品グループ画像)<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void setGoodsGroupImageRegistUpdateList(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        List<GoodsGroupImageRegistUpdateDto> ruList = new ArrayList<>();

        // 登録更新内容取得
        Map<Integer, GoodsGroupImageRegistUpdateDto> registUpdateGgImageMap = new HashMap<>();
        if (goodsRegistUpdateModel.getGoodsGroupImageRegistUpdateDtoList() != null) {
            for (GoodsGroupImageRegistUpdateDto ruDto : goodsRegistUpdateModel.getGoodsGroupImageRegistUpdateDtoList()) {
                // 画像連番でMAPを作成
                registUpdateGgImageMap.put(ruDto.getGoodsGroupImageEntity().getImageTypeVersionNo(), ruDto);
            }
        }

        // 全画像連番＋全画像種別 の状態を確認
        String maxCount = PropertiesUtil.getSystemPropertiesValue("goodsimage.max.count");
        for (int versionNo = 0; versionNo <= Integer.valueOf(maxCount); versionNo++) {

            // 該当するマスタ情報取得
            GoodsGroupImageEntity masterEntity =
                            goodsRegistUpdateModel.getMasterGoodsGroupImageEntityMap().get(versionNo);

            GoodsGroupImageRegistUpdateDto ruDto = registUpdateGgImageMap.get(versionNo);

            if (ruDto != null) {
                // 登録更新リストに含まれている場合、表示状態を上書き
                ruList.add(ruDto);

                // 登録更新リストに含まれていないが、表示状態を変更した場合はマスタ情報確認
            } else if (masterEntity != null) {
                // マスタに存在するため、上書き情報作成
                GoodsGroupImageRegistUpdateDto updateDto =
                                ApplicationContextUtility.getBean(GoodsGroupImageRegistUpdateDto.class);

                // 表示状態上書き+削除対象外
                updateDto.setGoodsGroupImageEntity(masterEntity);
                updateDto.setDeleteFlg(false);

                ruList.add(updateDto);
            }
        }

        goodsRegistUpdateModel.setGoodsGroupImageRegistUpdateDtoList(ruList);
    }

    /**
     * 商品画像設定<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void setDetailsImageInfo(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        // 商品グループ詳細画像アイテムリスト作成
        createDetailsImageItems(goodsRegistUpdateModel);

    }

    /**
     * 商品グループ詳細画像アイテム作成<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void createDetailsImageItems(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品画像は登録更新画面から引き渡す
        goodsRegistUpdateModel.setConfirmGoodsImageItems(goodsRegistUpdateModel.getGoodsImageItems());
    }

    /**
     * 入庫登録<br/>
     *
     * @param goodsRegistUpdateStockItem
     * @return stockResultEntity　入庫実績エンティティ
     */
    public StockResultEntity toStockResultEntityforStockSupplementIns(GoodsRegistUpdateStockItem goodsRegistUpdateStockItem) {
        StockResultEntity stockResultEntity = ApplicationContextUtility.getBean(StockResultEntity.class);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // リストより、選択された商品に対して、入庫実績エンティティへ設定する。
        stockResultEntity.setGoodsSeq(goodsRegistUpdateStockItem.getGoodsSeq());
        stockResultEntity.setSupplementCount(
                        conversionUtility.toBigDecimal(goodsRegistUpdateStockItem.getSupplementCount()));
        return stockResultEntity;

    }

    /**
     * 変更箇所の表示スタイル設定処理<br/>
     *
     * @param goodsRegistUpdateModel Model
     * @param original              　修正前GoodsGroupDto
     * @param modified              　修正後GoodsGroupDto
     */
    protected void setDiff(GoodsRegistUpdateModel goodsRegistUpdateModel,
                           GoodsGroupDto original,
                           GoodsGroupDto modified) {

        // 商品グループエンティティの差分
        goodsRegistUpdateModel.setModifiedGoodsGroupList(
                        DiffUtil.diff(original.getGoodsGroupEntity(), modified.getGoodsGroupEntity()));

        // 商品グループ表示エンティティの差分
        goodsRegistUpdateModel.setModifiedGoodsGroupDspList(
                        DiffUtil.diff(original.getGoodsGroupDisplayEntity(), modified.getGoodsGroupDisplayEntity()));

        // 規格毎の差分
        setGoodsUnitDiff(goodsRegistUpdateModel, original.getGoodsDtoList(), modified.getGoodsDtoList());

        // 関連商品エンティティの差分
        setGoodsRelationDiff(goodsRegistUpdateModel, goodsRegistUpdateModel.getMasterGoodsRelationEntityList(),
                             goodsRegistUpdateModel.getGoodsRelationEntityList()
                            );

        // カテゴリ登録商品の変更箇所の表示スタイル設定処理
        setModifiedCategoryGoodsEntityList(goodsRegistUpdateModel, original.getCategoryGoodsEntityList(),
                                           modified.getCategoryGoodsEntityList()
                                          );
    }

    /**
     * カテゴリ登録商品エンティティリスト
     *
     * @param categoryGoodsEntityList カテゴリ登録商品エンティティリスト
     */
    private List<CategoryGoodsEntity> collectManualList(List<CategoryGoodsEntity> categoryGoodsEntityList) {
        List<CategoryGoodsEntity> categoryGoodsEntities = new ArrayList<>();

        for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
            if (HTypeCategoryType.AUTO != categoryGoodsEntity.getCategoryType()) {

                categoryGoodsEntities.add(categoryGoodsEntity);
            }
        }

        return categoryGoodsEntities;
    }

    /**
     * カテゴリ登録商品の変更箇所の表示スタイル設定処理
     *
     * @param goodsRegistUpdateModel Model
     * @param original              　修正前 カテゴリ登録商品エンティティリスト
     * @param modified              　修正後 カテゴリ登録商品エンティティリスト
     */
    protected void setModifiedCategoryGoodsEntityList(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                                      List<CategoryGoodsEntity> original,
                                                      List<CategoryGoodsEntity> modified) {

        List<CategoryGoodsEntity> originalList = new ArrayList<>();
        List<CategoryGoodsEntity> modifiedList = new ArrayList<>();

        if (CollectionUtil.isEmpty(original) && CollectionUtil.isEmpty(modified)) {
            // 修正前後で設定されていないので、差分なし
            return;
        }

        if (!CollectionUtil.isEmpty(original)) {
            originalList = collectManualList(original);
        }

        if (!CollectionUtil.isEmpty(modified)) {
            modifiedList = collectManualList(modified);
        }

        goodsRegistUpdateModel.setModifiedCategoryGoodsEntityList(new ArrayList<>());

        if (CollectionUtil.isEmpty(originalList)) {
            // 変更前の情報がない場合は新規追加のため、追加分の差分リストを作成
            for (int i = 0; i < modifiedList.size(); i++) {
                goodsRegistUpdateModel.getModifiedCategoryGoodsEntityList()
                                      .add(DiffUtil.diff(new CategoryGoodsEntity(), modifiedList.get(i)));
            }
        } else {

            if (originalList.size() < modifiedList.size()) {
                for (int i = 0; i < originalList.size(); i++) {
                    goodsRegistUpdateModel.getModifiedCategoryGoodsEntityList()
                                          .add(DiffUtil.diff(originalList.get(i).getCategorySeq(),
                                                             modifiedList.get(i).getCategorySeq()
                                                            ));
                }
                // 追加分
                for (int i = originalList.size(); i < modifiedList.size(); i++) {
                    goodsRegistUpdateModel.getModifiedCategoryGoodsEntityList()
                                          .add(DiffUtil.diff(new CategoryGoodsEntity(), modifiedList.get(i)));
                }

            } else if (originalList.size() == modifiedList.size()) {
                for (int i = 0; i < originalList.size(); i++) {
                    if (!ListUtils.isEmpty(DiffUtil.diff(originalList.get(i).getCategorySeq(),
                                                         modifiedList.get(i).getCategorySeq()
                                                        ))) {
                        goodsRegistUpdateModel.getModifiedCategoryGoodsEntityList()
                                              .add(DiffUtil.diff(originalList.get(i).getCategorySeq(),
                                                                 modifiedList.get(i).getCategorySeq()
                                                                ));
                    }
                }

            } else {
                for (int i = 0; i < original.size(); i++) {
                    goodsRegistUpdateModel.getModifiedCategoryGoodsEntityList()
                                          .add(DiffUtil.diff(original.get(i), new CategoryGoodsEntity()));
                }
            }
        }
    }

    /**
     * 規格毎の変更箇所の表示スタイル設定処理<br/>
     *
     * @param goodsRegistUpdateModel Model
     * @param original              　修正前GoodsDtoリスト
     * @param modified              　修正後GoodsDtoリスト
     */
    protected void setGoodsUnitDiff(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                    List<GoodsDto> original,
                                    List<GoodsDto> modified) {

        // 対象Entity名
        String goodsEntity = goodsRegistUpdateModel.diffObjectNameGoods + DiffUtil.SEPARATOR;

        // 商品Dtoの差分
        List<String> tmpGoodsList = new ArrayList<>();
        goodsRegistUpdateModel.setModifiedGoodsList(new ArrayList<>());
        goodsRegistUpdateModel.setModifiedStockList(new ArrayList<>());

        // 修正後のほうが商品規格数が多い場合
        if (original.size() < modified.size()) {
            for (int i = 0; i < original.size(); i++) {
                // 削除された規格は除外
                if (HTypeGoodsSaleStatus.DELETED != modified.get(i).getGoodsEntity().getSaleStatusPC()) {
                    goodsRegistUpdateModel.getModifiedGoodsList()
                                          .add(DiffUtil.diff(original.get(i).getGoodsEntity(),
                                                             modified.get(i).getGoodsEntity()
                                                            ));
                    goodsRegistUpdateModel.getModifiedStockList()
                                          .add(DiffUtil.diff(original.get(i).getStockDto(),
                                                             modified.get(i).getStockDto()
                                                            ));
                }
            }
            // 追加分の規格
            for (int i = original.size(); i < modified.size(); i++) {
                tmpGoodsList = DiffUtil.diff(new GoodsEntity(), modified.get(i).getGoodsEntity());
                if (HTypeGoodsSaleStatus.NO_SALE == modified.get(i).getGoodsEntity().getSaleStatusPC()) {
                    // 非販売で追加した場合、差分がでないので、追加
                    tmpGoodsList.add(goodsEntity + "saleStatusPC");
                }
                goodsRegistUpdateModel.getModifiedGoodsList().add(tmpGoodsList);
                goodsRegistUpdateModel.getModifiedStockList()
                                      .add(DiffUtil.diff(new StockDto(), modified.get(i).getStockDto()));
            }
        }
        // 修正前後で同じ規格数の場合 ※修正後に削除されていても、配列数は減少しないので、修正前 > 修正後になることはない
        else {
            for (int i = 0; i < original.size(); i++) {
                // 削除された規格は除外
                if (HTypeGoodsSaleStatus.DELETED != modified.get(i).getGoodsEntity().getSaleStatusPC()) {
                    goodsRegistUpdateModel.getModifiedGoodsList()
                                          .add(DiffUtil.diff(original.get(i).getGoodsEntity(),
                                                             modified.get(i).getGoodsEntity()
                                                            ));
                    goodsRegistUpdateModel.getModifiedStockList()
                                          .add(DiffUtil.diff(original.get(i).getStockDto(),
                                                             modified.get(i).getStockDto()
                                                            ));
                }
            }
        }
    }

    /**
     * 関連商品の変更箇所の表示スタイル設定処理<br/>
     *
     * @param goodsRegistUpdateModel Model
     * @param original              　修正前 関連商品エンティティリスト
     * @param modified              　修正後 関連商品エンティティリスト
     */
    protected void setGoodsRelationDiff(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                        List<GoodsRelationEntity> original,
                                        List<GoodsRelationEntity> modified) {

        if (CollectionUtil.isEmpty(original) && CollectionUtil.isEmpty(modified)) {
            // 修正前後で設定されていないので、差分なし
            return;
        }

        goodsRegistUpdateModel.setModifiedGoodsRelationList(new ArrayList<>());

        if (CollectionUtil.isEmpty(original)) {
            // 変更前の情報がない場合は新規追加のため、追加分の差分リストを作成
            for (int i = 0; i < modified.size(); i++) {
                goodsRegistUpdateModel.getModifiedGoodsRelationList()
                                      .add(DiffUtil.diff(new GoodsRelationEntity(), modified.get(i)));
            }
        } else {

            // 修正後のほうが関連商品数が多い場合
            if (original.size() < modified.size()) {
                for (int i = 0; i < original.size(); i++) {
                    goodsRegistUpdateModel.getModifiedGoodsRelationList()
                                          .add(DiffUtil.diff(original.get(i), modified.get(i)));
                }
                // 追加分
                for (int i = original.size(); i < modified.size(); i++) {
                    goodsRegistUpdateModel.getModifiedGoodsRelationList()
                                          .add(DiffUtil.diff(new GoodsRelationEntity(), modified.get(i)));
                }
            }
            // 修正前のほうが関連商品数が多い、または同数の場合
            else {
                for (int i = 0; i < modified.size(); i++) {
                    goodsRegistUpdateModel.getModifiedGoodsRelationList()
                                          .add(DiffUtil.diff(original.get(i), modified.get(i)));
                }
            }
        }
    }

}