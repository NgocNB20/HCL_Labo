/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleAreaDao;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodDao;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliverySpecialChargeAreaDao;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodSelectListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType.AMOUNT;
import static jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType.FLAT;
import static jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType.PREFECTURE;

/**
 * 配送方法別送料リスト取得ロジック実装クラス
 *
 * @author negishi
 */
@Component
public class DeliveryMethodSelectListGetLogicImpl extends AbstractShopLogic
                implements DeliveryMethodSelectListGetLogic {

    /** 配送方法Dao */
    private final DeliveryMethodDao deliveryMethodDao;

    /** 配送不可能エリアDao */
    private final DeliveryImpossibleAreaDao deliveryImpossibleAreaDao;

    /** 配送特別料金Dao */
    private final DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao;

    @Autowired
    public DeliveryMethodSelectListGetLogicImpl(DeliveryMethodDao deliveryMethodDao,
                                                DeliveryImpossibleAreaDao deliveryImpossibleAreaDao,
                                                DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao) {
        this.deliveryMethodDao = deliveryMethodDao;
        this.deliveryImpossibleAreaDao = deliveryImpossibleAreaDao;
        this.deliverySpecialChargeAreaDao = deliverySpecialChargeAreaDao;
    }

    /**
     * 実行メソッド
     *
     * @param conditionDto          配送方法Dao用検索条件DTO
     * @param deliveryMethodSeqList 配送方法SEQリスト
     * @param freeDeliveryFlag      無料配送フラグ
     * @return 配送方法Dtoリスト
     */
    @Override
    public List<DeliveryDto> execute(DeliverySearchForDaoConditionDto conditionDto,
                                     List<Integer> deliveryMethodSeqList,
                                     HTypeFreeDeliveryFlag freeDeliveryFlag) {
        return execute(conditionDto, deliveryMethodSeqList, freeDeliveryFlag, null);
    }

    /**
     * 実行メソッド
     *
     * @param conditionDto          配送方法Dao用検索条件DTO
     * @param deliveryMethodSeqList 配送方法SEQリスト
     * @param freeDeliveryFlag      無料配送フラグ
     * @param available             利用可能、不可能どちらかを指定。null..全部 / true..利用可能のみ / false..利用不可能のみ
     * @return 配送方法Dtoリスト
     */
    @Override
    public List<DeliveryDto> execute(DeliverySearchForDaoConditionDto conditionDto,
                                     List<Integer> deliveryMethodSeqList,
                                     HTypeFreeDeliveryFlag freeDeliveryFlag,
                                     Boolean available) {

        // パラメータチェック
        checkParameter(conditionDto, deliveryMethodSeqList);

        // 戻り値用、配送Dtoリスト作成
        List<DeliveryDto> deliveryDtoList = new ArrayList<>();
        // 配送方法詳細DTOリスト取得処理を実行。
        List<DeliveryDetailsDto> deliveryDetailsDtoList = getSearchDeliveryDetailsList(conditionDto);

        List<Integer> tmpDeliverySeqList = new ArrayList<>();
        if (deliveryMethodSeqList == null) {
            // 重複を省くため java.util.Set を使用。
            Set<Integer> tmpDeliverySeqSet = new LinkedHashSet<>();
            for (DeliveryDetailsDto deliveryDetailsDto : deliveryDetailsDtoList) {
                tmpDeliverySeqSet.add(deliveryDetailsDto.getDeliveryMethodSeq());
            }
            tmpDeliverySeqList.addAll(tmpDeliverySeqSet);

        } else {
            tmpDeliverySeqList = deliveryMethodSeqList;
        }

        // 配送方法がない
        if (tmpDeliverySeqList.isEmpty()) {
            return deliveryDtoList;
        }

        List<DeliveryImpossibleAreaEntity> deliveryImpossibleAreaEntityList = null;
        if (!conditionDto.isIgnoreImpossibleAreaFlag()) {
            // 配送不可能エリアエンティティリスト取得処理実行
            deliveryImpossibleAreaEntityList = getDeliveryImpossibleAreaList(tmpDeliverySeqList, conditionDto);
        }
        // 配送特別料金エリアエンティティリスト取得処理実行
        List<DeliverySpecialChargeAreaEntity> deliverySpecialChargeAreaEntityList =
                        getDeliverySpecialChargeAreaList(tmpDeliverySeqList, conditionDto);

        // 配送Dtoリスト設定
        setDeliveryDtoList(conditionDto, deliveryMethodSeqList, available, deliveryDtoList, deliveryDetailsDtoList,
                           deliveryImpossibleAreaEntityList, deliverySpecialChargeAreaEntityList
                          );

        // 無料配送の商品が含まれているかどうか。含まれている場合、全配送方法送料無料。
        setFreeDeliveryCarriage(freeDeliveryFlag, deliveryDtoList);

        return deliveryDtoList;
    }

    /**
     * 配送Dtoリスト設定<br/>
     *
     * @param conditionDto                        配送方法Dao用検索条件DTO
     * @param deliveryMethodSeqList               配送方法SEQリスト
     * @param available                           利用可能、不可能どちらかを指定。null..全部 / true..利用可能のみ / false..利用不可能のみ
     * @param deliveryDtoList                     配送方法DTOリスト
     * @param deliveryDetailsDtoList              配送方法詳細DTOリスト
     * @param deliveryImpossibleAreaEntityList    配送不可能エリアエンティティリスト
     * @param deliverySpecialChargeAreaEntityList 配送特別料金エリアエンティティリスト
     * @param customParams                        案件用引数
     */
    protected void setDeliveryDtoList(DeliverySearchForDaoConditionDto conditionDto,
                                      List<Integer> deliveryMethodSeqList,
                                      Boolean available,
                                      List<DeliveryDto> deliveryDtoList,
                                      List<DeliveryDetailsDto> deliveryDetailsDtoList,
                                      List<DeliveryImpossibleAreaEntity> deliveryImpossibleAreaEntityList,
                                      List<DeliverySpecialChargeAreaEntity> deliverySpecialChargeAreaEntityList,
                                      Object... customParams) {
        // 配送方法SEQが同じかどうかを判断するために使用する変数
        Integer tmpDeliveryMethodSeq = 0;

        // 配送方法詳細Dtoリストの件数分ループ
        // ラベル
        deliveryDetailsDtoListLoop:
        for (DeliveryDetailsDto deliveryDetailsDto : deliveryDetailsDtoList) {
            // ① 同一配送方法SEQのうち、上限金額が最小またはnullの配送方法詳細Dtoのみ使用する
            if (tmpDeliveryMethodSeq.equals(deliveryDetailsDto.getDeliveryMethodSeq())) {
                continue;

            } else {
                tmpDeliveryMethodSeq = deliveryDetailsDto.getDeliveryMethodSeq();
            }

            // ② 配送DTOの生成
            DeliveryDto deliveryDto = ApplicationContextUtility.getBean(DeliveryDto.class);
            // 配送Dtoに配送方法詳細Dtoを設定
            deliveryDto.setDeliveryDetailsDto(deliveryDetailsDto);

            // ③ 送料・選択区分の設定
            // ③－１ 選択可能配送方法対象チェック
            if (deliveryMethodSeqList != null && !deliveryMethodSeqList.contains(
                            deliveryDetailsDto.getDeliveryMethodSeq())) {
                // 送料を０に設定
                deliveryDto.setCarriage(BigDecimal.ZERO);
                // 選択区分を「不可」に設定
                deliveryDto.setSelectClass(false);
                if (available == null || !available) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;
            }

            // ③－２ 選択済み配送方法の対象チェック
            if (deliveryImpossibleAreaEntityList != null && !deliveryImpossibleAreaEntityList.isEmpty()) {
                // 配送不可能エリアかどうか
                for (DeliveryImpossibleAreaEntity deliveryImpossibleAreaEntity : deliveryImpossibleAreaEntityList) {
                    if (deliveryDetailsDto.getDeliveryMethodSeq()
                                          .equals(deliveryImpossibleAreaEntity.getDeliveryMethodSeq())) {
                        // 送料を０に設定
                        deliveryDto.setCarriage(BigDecimal.ZERO);
                        // 選択区分を「不可」に設定
                        deliveryDto.setSelectClass(false);
                        if (available == null || !available) {
                            deliveryDtoList.add(deliveryDto);
                        }
                        continue deliveryDetailsDtoListLoop;
                    }
                }
            }

            // ③－３ 配送特別料金エリア対象チェック
            if (deliverySpecialChargeAreaEntityList != null && !deliverySpecialChargeAreaEntityList.isEmpty()) {
                for (DeliverySpecialChargeAreaEntity deliverySpecialChargeAreaEntity : deliverySpecialChargeAreaEntityList) {
                    if (deliveryDetailsDto.getDeliveryMethodSeq()
                                          .equals(deliverySpecialChargeAreaEntity.getDeliveryMethodSeq())) {
                        // 送料を設定
                        deliveryDto.setCarriage(deliverySpecialChargeAreaEntity.getCarriage());
                        // フラグをセット
                        deliveryDto.setSpecialChargeAreaFlag(true);
                        // 選択区分を「可」に設定
                        deliveryDto.setSelectClass(true);
                        if (available == null || Boolean.TRUE.equals(available)) {
                            deliveryDtoList.add(deliveryDto);
                        }
                        continue deliveryDetailsDtoListLoop;
                    }
                }
            }

            // ③－４ 配送区分別送料の対象チェック
            if (deliveryDetailsDto.getMaxPrice() == null && AMOUNT.equals(deliveryDetailsDto.getDeliveryMethodType())) {
                // ※金額別送料は現在未使用
                // 送料を０に設定
                deliveryDto.setCarriage(BigDecimal.ZERO);
                // 選択区分を「不可」に設定
                deliveryDto.setSelectClass(false);
                if (available == null || !available) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;

            } else if (deliveryDetailsDto.getPrefectureType() == null && PREFECTURE.equals(
                            deliveryDetailsDto.getDeliveryMethodType())) {
                // 送料を０に設定
                deliveryDto.setCarriage(BigDecimal.ZERO);
                // 選択区分を「不可」に設定
                deliveryDto.setSelectClass(false);
                if (available == null || !available) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;
            }

            // ③－５ 高額割引適用チェック
            BigDecimal largeAmountDiscountPrice = deliveryDetailsDto.getLargeAmountDiscountPrice();
            if (largeAmountDiscountPrice != null && largeAmountDiscountPrice.intValue() > 0 && (
                            conditionDto.getTotalGoodsPrice() != null
                            && conditionDto.getTotalGoodsPrice().compareTo(largeAmountDiscountPrice) >= 0)) {
                // 送料を設定
                deliveryDto.setCarriage(deliveryDetailsDto.getLargeAmountDiscountCarriage());
                // 選択区分を「可」に設定
                deliveryDto.setSelectClass(true);
                if (available == null || Boolean.TRUE.equals(available)) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;
            }

            // ③－６ 区分別送料のセット
            // 配送方法区分が「全国一律」の場合
            if (FLAT.equals(deliveryDetailsDto.getDeliveryMethodType())) {
                // 送料を設定
                deliveryDto.setCarriage(deliveryDetailsDto.getEqualsCarriage());
                // 選択区分を「可」に設定
                deliveryDto.setSelectClass(true);
                if (available == null || Boolean.TRUE.equals(available)) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;

                // 配送方法区分が「金額別」「都道府県別」の場合
            } else {
                // 送料を設定
                deliveryDto.setCarriage(deliveryDetailsDto.getCarriage());
                // 選択区分を「可」に設定
                deliveryDto.setSelectClass(true);
                if (available == null || Boolean.TRUE.equals(available)) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;
            }
        }
    }

    /**
     * 無料配送設定<br/>
     * 無料配送の商品が含まれているかどうか。含まれている場合、全配送方法送料無料。<br/>
     *
     * @param freeDeliveryFlag 無料配送フラグ
     * @param deliveryDtoList  配送Dtoリスト
     * @param customParams     案件用引数
     */
    protected void setFreeDeliveryCarriage(HTypeFreeDeliveryFlag freeDeliveryFlag,
                                           List<DeliveryDto> deliveryDtoList,
                                           Object... customParams) {
        if (HTypeFreeDeliveryFlag.ON.equals(freeDeliveryFlag)) {
            BigDecimal zeroCarriage = new BigDecimal(0);
            for (int i = 0; i < deliveryDtoList.size(); i++) {
                deliveryDtoList.get(i).setCarriage(zeroCarriage);
            }
        }
    }

    /**
     * パラメータのチェックを行います。
     *
     * @param conditionDto          配送方法Dao用検索条件DTO
     * @param deliveryMethodSeqList 配送方法SEQリスト
     */
    protected void checkParameter(DeliverySearchForDaoConditionDto conditionDto, List<Integer> deliveryMethodSeqList) {
        // 配送方法Dao用検索条件DTO
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);
        if (deliveryMethodSeqList != null && deliveryMethodSeqList.isEmpty()) {
            throwMessage(MSGCD_NO_DELIVERY_METHOD);
        }
    }

    /**
     * 配送方法詳細DTOリスト取得
     *
     * @param conditionDto 配送方法Dao用検索条件DTO
     * @return 配送方法詳細Dtoリスト
     */
    protected List<DeliveryDetailsDto> getSearchDeliveryDetailsList(DeliverySearchForDaoConditionDto conditionDto) {
        return deliveryMethodDao.getSearchDeliveryDetailsList(conditionDto);
    }

    /**
     * 配送不可能エリアエンティティリスト取得
     *
     * @param deliverySeqList 配送方法SEQリスト
     * @param conditionDto    配送方法Dao用検索条件Dto
     * @return 配送不可能エリアエンティティリスト
     */
    protected List<DeliveryImpossibleAreaEntity> getDeliveryImpossibleAreaList(List<Integer> deliverySeqList,
                                                                               DeliverySearchForDaoConditionDto conditionDto) {
        return deliveryImpossibleAreaDao.getDeliveryImpossibleAreaList(deliverySeqList, conditionDto.getZipcode());
    }

    /**
     * 配送特別料金エリアエンティティリスト取得
     *
     * @param deliveryMethodSeqList 配送方法SEQリスト
     * @param conditionDto          配送方法Dao用検索条件Dto
     * @return 配送特別料金エリアエンティティリスト
     */
    protected List<DeliverySpecialChargeAreaEntity> getDeliverySpecialChargeAreaList(List<Integer> deliveryMethodSeqList,
                                                                                     DeliverySearchForDaoConditionDto conditionDto) {
        return deliverySpecialChargeAreaDao.getDeliverySpecialChargeAreaList(
                        deliveryMethodSeqList, conditionDto.getZipcode());
    }

}