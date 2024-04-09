/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.category.presentation.consumer.processor;

import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.SyncUpdateCategoryProductQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.core.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailListGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDetailsGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsRemoveLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsModifyService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CategoryGoodsRegistUpdateErrorRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * カテゴリ商品更新 Processor.
 * @author Pham Quang Dieu (VJP)
 */
@Component
@Scope("prototype")
public class SyncUpdateCategoryProductProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SyncUpdateCategoryProductProcessor.class);

    /** カテゴリ情報DTO取得 */
    private final CategoryDetailsGetLogic categoryDetailsGetLogic;

    /** カテゴリ登録商品マップ取得 */
    private final CategoryGoodsMapGetLogic categoryGoodsMapGetLogic;

    /** カテゴリ条件取得 */
    private final CategoryConditionGetLogic categoryConditionGetLogic;

    /** カテゴリ条件詳細リスト取得 */
    private final CategoryConditionDetailListGetLogic categoryConditionDetailListGetLogic;

    /** カテゴリ登録商品情報削除 */
    private final CategoryGoodsRemoveLogic categoryGoodsRemoveLogic;

    /** 商品グループリスト取得 */
    private final CategoryGoodsModifyService categoryGoodsModifyService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 通知サブAPI */
    private final NotificationSubApi notificationSubApi;

    /** 商品グループリスト取得 */
    private final GoodsGroupListGetLogic goodsGroupListGetLogic;

    /** 商品アイコン表示情報リストを取得する */
    private final GoodsInformationIconGetLogic goodsInformationIconGetLogic;

    /**
     * コンストラクタ
     *
     * @param categoryDetailsGetLogic             カテゴリ情報DTO取得
     * @param categoryGoodsMapGetLogic            カテゴリ登録商品マップ取得
     * @param categoryConditionGetLogic           カテゴリ条件取得
     * @param categoryConditionDetailListGetLogic カテゴリ条件詳細リスト取得
     * @param categoryGoodsRemoveLogic            カテゴリ登録商品情報削除
     * @param categoryGoodsModifyService          商品グループリスト取得
     * @param asyncService                        非同期処理サービス
     * @param notificationSubApi                  通知サブAPI
     * @param goodsGroupListGetLogic              商品グループリスト取得
     */
    @Autowired
    public SyncUpdateCategoryProductProcessor(CategoryDetailsGetLogic categoryDetailsGetLogic,
                                              CategoryGoodsMapGetLogic categoryGoodsMapGetLogic,
                                              CategoryConditionGetLogic categoryConditionGetLogic,
                                              CategoryConditionDetailListGetLogic categoryConditionDetailListGetLogic,
                                              CategoryGoodsRemoveLogic categoryGoodsRemoveLogic,
                                              CategoryGoodsModifyService categoryGoodsModifyService,
                                              AsyncService asyncService,
                                              NotificationSubApi notificationSubApi,
                                              GoodsGroupListGetLogic goodsGroupListGetLogic,
                                              GoodsInformationIconGetLogic goodsInformationIconGetLogic) {
        this.categoryDetailsGetLogic = categoryDetailsGetLogic;
        this.categoryGoodsMapGetLogic = categoryGoodsMapGetLogic;
        this.categoryConditionGetLogic = categoryConditionGetLogic;
        this.categoryConditionDetailListGetLogic = categoryConditionDetailListGetLogic;
        this.categoryGoodsRemoveLogic = categoryGoodsRemoveLogic;
        this.categoryGoodsModifyService = categoryGoodsModifyService;
        this.asyncService = asyncService;
        this.notificationSubApi = notificationSubApi;
        this.goodsGroupListGetLogic = goodsGroupListGetLogic;
        this.goodsInformationIconGetLogic = goodsInformationIconGetLogic;
    }

    /**
     * Processor
     *
     * @param syncUpdateCategoryProductQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(SyncUpdateCategoryProductQueueMessage syncUpdateCategoryProductQueueMessage) throws Exception {

        LOGGER.info("カテゴリ商品更新バッチ");

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("CATEGORY_GOODS_REGISTER_UPDATE_BATCH");
        batchLogging.setBatchName("カテゴリ商品更新バッチ");
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("カテゴリ商品アップデート開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {

            List<Integer> categorySeqList = new ArrayList<>();

            // パラメータ「カテゴリーSEQ」がない場合
            if (syncUpdateCategoryProductQueueMessage.getCategorySeq() == null) {
                // 自動カテゴリーをDB検索
                categorySeqList = categoryDetailsGetLogic.getCategorySeqListByCategoryType(HTypeCategoryType.AUTO);
                // パラメータ「カテゴリーSEQ」がある場合、カテゴリーSEQリストを新規生成し
            } else {
                categorySeqList.add(syncUpdateCategoryProductQueueMessage.getCategorySeq());
            }

            // 処理対象カテゴリーSEQリストの件数分
            for (Integer categorySeq : categorySeqList) {
                // カテゴリ登録商品のリストを取得する
                List<CategoryGoodsEntity> categoryGoodsEntityList =
                                categoryGoodsMapGetLogic.getCategoryGoodsListByCategorySeq(categorySeq,
                                                                                           syncUpdateCategoryProductQueueMessage.getGoodsGroupSeqList()
                                                                                          );

                // カテゴリ条件取得
                CategoryConditionEntity categoryConditionEntity = categoryConditionGetLogic.execute(categorySeq);

                // カテゴリ検索条件詳細を取得する
                List<CategoryConditionDetailEntity> categoryConditionDetailEntityList =
                                categoryConditionDetailListGetLogic.execute(categorySeq);
                List<CategoryConditionDetailEntity> categoryConditionDetailEntities = new ArrayList<>();

                // アイコン名からアイコンSeqに処理
                handleIconMatch(categoryConditionDetailEntityList, categoryConditionDetailEntities);

                // 商品グループSEQのリストを取得する
                List<Integer> goodsGroupList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(syncUpdateCategoryProductQueueMessage.getGoodsGroupSeqList())) {
                    syncUpdateCategoryProductQueueMessage.getGoodsGroupSeqList().forEach(goodsGroupSeq -> {
                        goodsGroupList.addAll(goodsGroupListGetLogic.execute(goodsGroupSeq, categoryConditionEntity,
                                                                             categoryConditionDetailEntities
                                                                            ));
                    });
                } else {
                    goodsGroupList.addAll(goodsGroupListGetLogic.execute(null, categoryConditionEntity,
                                                                         categoryConditionDetailEntities
                                                                        ));
                }

                // MQから対象商品グループSEQが指定されている場合、カテゴリー条件だけで判定した商品グループSEQリストを取得し、商品登録更新による自動カテゴリー紐づけ解除対象の商品グループSEQリストを取得
                if (CollectionUtils.isNotEmpty(syncUpdateCategoryProductQueueMessage.getGoodsGroupSeqList())) {
                    List<Integer> goodsGroupOldList = goodsGroupListGetLogic.execute(null, categoryConditionEntity,
                                                                                     categoryConditionDetailEntities
                                                                                    );
                    goodsGroupList.addAll(goodsGroupOldList);
                }

                // カテゴリ登録商品情報を削除
                int deleteCnt = 0;
                if (categoryGoodsEntityList != null) {
                    // 重複を削除
                    categoryGoodsEntityList.removeIf(categoryGoodsEntity -> goodsGroupList.contains(
                                    categoryGoodsEntity.getGoodsGroupSeq()) && (categoryGoodsEntity.getCategorySeq()
                                                                                != null
                                                                                && categoryGoodsEntity.getCategorySeq()
                                                                                                      .equals(categorySeq)));

                    // カテゴリ登録商品情報を削除
                    for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
                        deleteCnt += categoryGoodsRemoveLogic.execute(categoryGoodsEntity);
                    }
                }

                reportString.append(categorySeq).append(": 削除件数[").append(deleteCnt).append("]で処理が終了しました。\n");

                // カテゴリ商品の修正
                int updateCnt = categoryGoodsModifyService.execute(categorySeq, goodsGroupList);

                reportString.append(categorySeq).append(": 更新件数[").append(updateCnt).append("]で処理が終了しました。\n");
                batchLogging.setProcessCount(updateCnt);
            }

            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {

            // エラーメッセージを成形する
            String errorResultMsg = null;
            CategoryGoodsRegistUpdateErrorRequest categoryGoodsRegistUpdateErrorRequest =
                            new CategoryGoodsRegistUpdateErrorRequest();
            if (StringUtils.isNotBlank(e.getMessage())) {
                errorResultMsg = e.getMessage();
            } else {
                errorResultMsg = e.getClass().getName() + "が発生";
            }
            categoryGoodsRegistUpdateErrorRequest.setErrorResultMsg(errorResultMsg.trim());

            // メール送信
            Object[] args = new Object[] {categoryGoodsRegistUpdateErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {CategoryGoodsRegistUpdateErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "categoryGoodsRegistUpdateError", args, argsClass);

            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            batchLogging.setProcessCount(null);

            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            batchLogging.setReport(reportString);
            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);
            // バッチ異常終了
            LOGGER.error("ロールバックします。");

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");
            LOGGER.info(reportString.toString());
            LOGGER.info("カテゴリ商品更新終了");
        }
    }

    /**
     * アイコン名からアイコンSeqに処理
     *
     * @param categoryConditionDetailEntityList カテゴリ検索条件詳細を
     * @param categoryConditionDetailEntities カテゴリ条件詳細エンティティクラスのリスト
     */
    private void handleIconMatch(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList,
                                 List<CategoryConditionDetailEntity> categoryConditionDetailEntities) {
        Map<String, List<String>> iconsMap = new HashMap<>();
        List<String> icons = new ArrayList<>();

        for (CategoryConditionDetailEntity categoryConditionDetailEntity : categoryConditionDetailEntityList) {
            if ((HTypeConditionColumnType.ICON.getValue()).equals(categoryConditionDetailEntity.getConditionColumn())
                && (HTypeConditionOperatorType.MATCH.getValue()).equals(
                            categoryConditionDetailEntity.getConditionOperator()) && ObjectUtils.isNotEmpty(
                            categoryConditionDetailEntity.getConditionValue()) && !icons.contains(
                            categoryConditionDetailEntity.getConditionValue())) {

                icons.add(categoryConditionDetailEntity.getConditionValue());
            }
        }

        // アイコン名のリストからエンティティのリストを取得
        List<GoodsInformationIconEntity> goodsInformationIconEntities =
                        goodsInformationIconGetLogic.getEntityListByNameList(icons);

        for (GoodsInformationIconEntity informationIconEntity : goodsInformationIconEntities) {
            List<String> iconSeqs = new ArrayList<>();

            if (iconsMap.containsKey(informationIconEntity.getIconName())) {
                iconSeqs = iconsMap.get(informationIconEntity.getIconName());
            }
            iconSeqs.add(informationIconEntity.getIconSeq().toString());
            iconsMap.put(informationIconEntity.getIconName(), iconSeqs);
        }

        for (CategoryConditionDetailEntity categoryConditionDetailEntity : categoryConditionDetailEntityList) {
            CategoryConditionDetailEntity conditionDetailEntity = new CategoryConditionDetailEntity();
            BeanUtils.copyProperties(categoryConditionDetailEntity, conditionDetailEntity);

            if ((HTypeConditionColumnType.ICON.getValue()).equals(conditionDetailEntity.getConditionColumn())
                && (HTypeConditionOperatorType.MATCH.getValue()).equals(conditionDetailEntity.getConditionOperator())
                && ObjectUtils.isNotEmpty(conditionDetailEntity.getConditionValue())) {
                if (iconsMap.containsKey(conditionDetailEntity.getConditionValue())) {
                    conditionDetailEntity.setConditionValues(iconsMap.get(conditionDetailEntity.getConditionValue()));
                }
            }
            categoryConditionDetailEntities.add(conditionDetailEntity);
        }

    }
}