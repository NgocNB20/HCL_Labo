/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.method.proxy;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleAreaDao;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodDao;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliverySpecialChargeAreaDao;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.ReceiverDateGetLogic;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDetailsGetService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType.PREFECTURE;

/**
 * 配送方法プロキシサービス
 */
@Service
public class ShippingMethodProxyService {

    /** お届け希望日リスト取得ロジック */
    private final ReceiverDateGetLogic receiverDateGetLogic;

    /** 配送方法Dao */
    private final DeliveryMethodDao deliveryMethodDao;

    /** 配送不可能エリアDao */
    private final DeliveryImpossibleAreaDao deliveryImpossibleAreaDao;

    /** 配送方法詳細取得サービス */
    private final DeliveryMethodDetailsGetService deliveryMethodDetailsGetService;

    /** 配送特別料金Dao */
    private final DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao;

    /** コンストラクタ */
    @Autowired
    public ShippingMethodProxyService(ReceiverDateGetLogic receiverDateGetLogic,
                                      DeliveryMethodDao deliveryMethodDao,
                                      DeliveryImpossibleAreaDao deliveryImpossibleAreaDao,
                                      DeliveryMethodDetailsGetService deliveryMethodDetailsGetService,
                                      DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao) {
        this.receiverDateGetLogic = receiverDateGetLogic;
        this.deliveryMethodDao = deliveryMethodDao;
        this.deliveryImpossibleAreaDao = deliveryImpossibleAreaDao;
        this.deliveryMethodDetailsGetService = deliveryMethodDetailsGetService;
        this.deliverySpecialChargeAreaDao = deliverySpecialChargeAreaDao;
    }

    /**
     * デフォルト配送方法を取得する
     *
     * @param zipcode 郵便番号
     * @param prefecture 都道府県
     * @return DeliveryDto デフォルト配送方法
     */
    public DeliveryDto getDefaultShippingMethod(String zipcode, String prefecture) {
        // 選択可能配送方法を取得する
        List<DeliveryDto> defaultShippingMethodList = this.getSelectableShippingMethodList(zipcode, prefecture, true);
        // デフォルト配送方法リストが存在する場合
        if (!CollectionUtils.isEmpty(defaultShippingMethodList)) {
            // デフォルト配送方法リストの一つ目を返却する
            return defaultShippingMethodList.get(0);
        }
        // 配送方法が存在しない場合はnull返却
        return null;
    }

    /**
     * 選択可能配送方法を取得する
     *
     * @param zipcode    郵便番号
     * @param prefecture 都道府県
     * @return DeliveryDtoList 選択可能配送方法リスト
     */
    public List<DeliveryDto> getSelectableShippingMethodList(String zipcode, String prefecture) {
        return getSelectableShippingMethodList(zipcode, prefecture, false);
    }

    /**
     * 選択可能配送方法を取得する
     *
     * @param zipcode               郵便番号
     * @param prefecture            都道府県
     * @param exclusionReceiverDate お届け希望日設定除外フラグ
     * @return DeliveryDtoList 選択可能配送方法リスト
     */
    public List<DeliveryDto> getSelectableShippingMethodList(String zipcode,
                                                             String prefecture,
                                                             boolean exclusionReceiverDate) {

        DeliverySearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(DeliverySearchForDaoConditionDto.class);

        // conditionDtoの設定　※フェーズ１に合わせて、必須項目を設定
        conditionDto.setShopSeq(1001);

        // 常に公開固定
        conditionDto.setOpenStatusPC(HTypeOpenDeleteStatus.OPEN);

        // 常に配送不可エリア判定無効化フラグをfalse固定
        conditionDto.setIgnoreImpossibleAreaFlag(false);

        // お届け先都道府県から、都道府県種別に変換
        if (StringUtils.isNotBlank(prefecture)) {
            HTypePrefectureType prefectureType = EnumTypeUtil.getEnumFromLabel(HTypePrefectureType.class, prefecture);
            conditionDto.setPrefectureType(prefectureType);
        }

        // 郵便番号を設定
        conditionDto.setZipcode(zipcode);

        // 配送方法詳細リストを取得　※フェーズ１Daoを流用
        List<DeliveryDetailsDto> deliveryDetailsDtoList =
                        this.deliveryMethodDao.getSearchDeliveryDetailsList(conditionDto);

        // 配送方法詳細リストが存在しな場合は処理をスキップ
        if (CollectionUtils.isEmpty(deliveryDetailsDtoList)) {
            return null;
        }

        // DeliveryDetailsDtoリストから配送方法Seqリストを作成
        List<Integer> deliverySeqList = new ArrayList<>();
        Set<Integer> deliverySeqSet = new LinkedHashSet<>();
        for (DeliveryDetailsDto deliveryDetailsDto : deliveryDetailsDtoList) {
            deliverySeqSet.add(deliveryDetailsDto.getDeliveryMethodSeq());
        }
        deliverySeqList.addAll(deliverySeqSet);

        // 配送方法がない
        if (deliverySeqList.isEmpty()) {
            return null;
        }

        List<DeliveryImpossibleAreaEntity> deliveryImpossibleAreaDbEntityList = null;
        if (!conditionDto.isIgnoreImpossibleAreaFlag() && conditionDto.getZipcode() != null) {
            // 配送不可能エリアエンティティリスト取得処理実行
            deliveryImpossibleAreaDbEntityList =
                            this.deliveryImpossibleAreaDao.getDeliveryImpossibleAreaList(deliverySeqList,
                                                                                         conditionDto.getZipcode()
                                                                                        );
        }

        List<DeliveryDto> deliveryDtoList = new ArrayList<>();
        // DeliveryDetailsDtoからDeliveryDtoのリストに変換　※フェーズ１のDeliveryMethodSelectListGetLogicImpl#setDeliveryDtoListを一部流用
        // 配送特別料金エリアエンティティリスト取得処理実行
        List<DeliverySpecialChargeAreaEntity> deliverySpecialChargeAreaEntityList =
                        deliverySpecialChargeAreaDao.getDeliverySpecialChargeAreaList(deliverySeqList,
                                                                                      conditionDto.getZipcode()
                                                                                     );

        // 第1引数は常に利用可能に固定
        setDeliveryDtoList(true, deliveryDtoList, deliveryDetailsDtoList, deliveryImpossibleAreaDbEntityList,
                           deliverySpecialChargeAreaEntityList
                          );

        if (!exclusionReceiverDate) {
            // お届け日取得　※フェーズ１のReceiverDateGetLogicImpl#createReceiverDateListをそのまま流用
            this.receiverDateGetLogic.createReceiverDateList(deliveryDtoList, true);
        }

        return deliveryDtoList;
    }

    /**
     * 配送方法取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法詳細DTO
     */
    public DeliveryMethodDetailsDto getByDeliveryMethodSeq(Integer deliveryMethodSeq) {
        return this.deliveryMethodDetailsGetService.execute(deliveryMethodSeq);
    }

    /**
     * 配送Dtoリスト設定<br/>
     *
     *
     * @param available                          利用可能、不可能どちらかを指定。null..全部 / true..利用可能のみ / false..利用不可能のみ
     * @param deliveryDtoList                    配送方法DTOリスト
     * @param deliveryDetailsDtoList             配送方法詳細DTOリスト
     * @param deliveryImpossibleAreaDbEntityList 配送不可能エリアエンティティリスト
     */
    protected void setDeliveryDtoList(Boolean available,
                                      List<DeliveryDto> deliveryDtoList,
                                      List<DeliveryDetailsDto> deliveryDetailsDtoList,
                                      List<DeliveryImpossibleAreaEntity> deliveryImpossibleAreaDbEntityList,
                                      List<DeliverySpecialChargeAreaEntity> deliverySpecialChargeAreaEntityList) {
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

            // ③ 選択区分の設定

            // ③－１ 配送不可能エリアチェック
            if (deliveryImpossibleAreaDbEntityList != null && !deliveryImpossibleAreaDbEntityList.isEmpty()) {
                for (DeliveryImpossibleAreaEntity deliveryImpossibleAreaDbEntity : deliveryImpossibleAreaDbEntityList) {
                    if (deliveryDetailsDto.getDeliveryMethodSeq()
                                          .equals(deliveryImpossibleAreaDbEntity.getDeliveryMethodSeq())) {
                        // 選択区分を「不可」に設定
                        deliveryDto.setSelectClass(false);
                        if (available == null || !available) {
                            deliveryDtoList.add(deliveryDto);
                        }
                        continue deliveryDetailsDtoListLoop;
                    }
                }
            }

            // 配送特別料金エリア対象チェック
            if (!CollectionUtils.isEmpty(deliverySpecialChargeAreaEntityList)) {
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

            // ③－2 配送方法種別「都道府県別」のチェック
            if (deliveryDetailsDto.getPrefectureType() == null && PREFECTURE.equals(
                            deliveryDetailsDto.getDeliveryMethodType())) {
                // 選択区分を「不可」に設定
                deliveryDto.setSelectClass(false);
                if (available == null || !available) {
                    deliveryDtoList.add(deliveryDto);
                }
                continue;
            }

            // ③－3 選択区分のセット
            // 選択区分を「可」に設定
            deliveryDto.setSelectClass(true);
            if (available == null || Boolean.TRUE.equals(available)) {
                deliveryDtoList.add(deliveryDto);
            }
        }
    }

}