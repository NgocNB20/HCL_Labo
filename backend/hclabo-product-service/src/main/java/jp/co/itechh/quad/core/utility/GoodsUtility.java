/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 商品系ヘルパークラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsUtility {

    /** 規格の最大値 */
    protected static final Integer UNIT_LENGTH = 2;

    /** 画像なし画像名 */
    public static final String NO_IMAGE_FILENAME = "noimage";

    /** 区切り文字：スラッシュ */
    public static final String SEPARATOR_SLASH = "/";

    /** 画像なし画像拡張子 */
    protected static final String GIF = ".gif";

    /** 在庫切れメッセージ */
    protected static final String NO_STOCK_MESSAGE = " [在庫なし]";

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    @Autowired
    public GoodsUtility(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 商品画像パスの取得
     * ※商品画像パスがない場合は、画像なし画像を返す
     *
     * @param goodsImagePath 商品画像パス(null許可)
     * @return 実際の商品画像パス
     */
    public String getGoodsImagePath(String goodsImagePath) {

        // 画像がないまたは、非表示の場合は、画像なし画像
        String imagePath = null;
        if (StringUtil.isEmpty(goodsImagePath)) {
            imagePath = NO_IMAGE_FILENAME + GIF;
        } else {
            imagePath = goodsImagePath;
        }

        // システム設定パス取得
        String path = PropertiesUtil.getSystemPropertiesValue("images.path.goods");
        if (path == null) {
            return imagePath;
        }
        return path + SEPARATOR_SLASH + imagePath;
    }

    /**
     * 画像名の取得
     * 画像が[存在しない or 非表示]の場合nullを返す
     *
     * @param goodsGroupDto 商品グループ
     * @return 商品画像名
     */
    public String getImageFileName(GoodsGroupDto goodsGroupDto) {
        return getImageFileName(goodsGroupDto.getGoodsGroupImageEntityList());
    }

    /**
     * 画像名の取得
     * 画像が[存在しない or 非表示]の場合nullを返す
     *
     * @param goodsGroupImageEntityList 商品画像Entityリスト
     * @return 商品画像名
     */
    private String getImageFileName(List<GoodsGroupImageEntity> goodsGroupImageEntityList) {
        // 以下の条件より、対象のグループ画像を取得する
        // ・商品画像区分 = 引数に指定した値
        // ・画像表示状態 = 表示
        if (goodsGroupImageEntityList != null && !goodsGroupImageEntityList.isEmpty()) {
            return goodsGroupImageEntityList.get(0).getImageFileName();
        }
        return null;
    }

    /**
     * 商品公開判定
     * ※現在日時
     *
     * @param goodsOpenStatus 商品公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime 公開終了日時
     * @return true=公開、false=公開でない
     */
    public boolean isGoodsOpen(HTypeOpenDeleteStatus goodsOpenStatus, Timestamp openStartTime, Timestamp openEndTime) {

        // 日付関連Helper取得
        DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
        // 現在日時
        Timestamp currentTime = dateHelper.getCurrentTime();
        return isGoodsOpen(goodsOpenStatus, openStartTime, openEndTime, currentTime);
    }

    /**
     * 商品公開判定
     * ※現在日時
     *
     * @param goodsOpenStatus 商品公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime 公開終了日時
     * @param targetTime 比較時間
     * @return true=公開、false=公開でない
     */
    public boolean isGoodsOpen(HTypeOpenDeleteStatus goodsOpenStatus,
                               Timestamp openStartTime,
                               Timestamp openEndTime,
                               Timestamp targetTime) {

        // 公開
        if (goodsOpenStatus.equals(HTypeOpenDeleteStatus.OPEN)) {
            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
            return dateHelper.isOpen(openStartTime, openEndTime, targetTime);
        }
        return false;
    }

    /**
     * PC用商品販売判定
     * ※現在日時
     *
     * @param entity 商品
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSalesPc(GoodsEntity entity) {
        return isGoodsSales(entity.getSaleStatusPC(), entity.getSaleStartTimePC(), entity.getSaleEndTimePC());
    }

    /**
     * 商品販売判定
     * ※現在日時
     *
     * @param goodsSaleStatus 商品販売状態
     * @param saleStartTime 販売開始日時
     * @param saleEndTime 販売終了日時
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSales(HTypeGoodsSaleStatus goodsSaleStatus, Timestamp saleStartTime, Timestamp saleEndTime) {

        // 現在日時
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();
        return isGoodsSales(goodsSaleStatus, saleStartTime, saleEndTime, currentTime);
    }

    /**
     * 商品販売判定
     *
     * @param goodsSaleStatus 商品販売状態
     * @param saleStartTime 販売開始日時
     * @param saleEndTime 販売終了日時
     * @param targetTime 比較時間
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSales(HTypeGoodsSaleStatus goodsSaleStatus,
                                Timestamp saleStartTime,
                                Timestamp saleEndTime,
                                Timestamp targetTime) {

        // 販売
        if (HTypeGoodsSaleStatus.SALE.equals(goodsSaleStatus)) {
            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
            return dateHelper.isOpen(saleStartTime, saleEndTime, targetTime);
        }
        return false;
    }

    /**
     * 商品グループ在庫の表示判定
     *
     * @param status 在庫状況
     * @return true..表示 false..非表示
     */
    public boolean isGoodsGroupStock(HTypeStockStatusType status) {
        if (status == null) {
            return false;
        }
        return status.isDisplay();
    }

    /**
     * 商品の規格１～２の値を連携する
     *
     * @param goodsEntity 商品情報
     * @return /で結合した規格値　※規格なしの場合は、空文字
     */
    public String createUnitValue(GoodsEntity goodsEntity) {

        // 規格なしの場合は、空文字
        String unitValue = "";
        if (HTypeUnitManagementFlag.OFF == goodsEntity.getUnitManagementFlag()) {
            return unitValue;
        }

        // 規格ありの場合は、/区切り
        for (int i = 1; i <= UNIT_LENGTH; i++) {
            String value = "";
            if (1 == i) {
                value = StringUtils.isNotBlank(goodsEntity.getUnitValue1()) ? goodsEntity.getUnitValue1() : "";
            } else if (2 == i) {
                value = StringUtils.isNotBlank(goodsEntity.getUnitValue2()) ? goodsEntity.getUnitValue2() : "";
            }
            if (unitValue.isEmpty()) {
                unitValue = value;
            } else {
                unitValue = unitValue + "/" + value;
            }
        }
        return unitValue;
    }

    /**
     * 指定のサイトごとにインフォメーションアイコンSEQリストを作成します。
     *
     * @param siteType サイト種別
     * @param iconPc インフォメーションアイコンPC(※複数の場合、スラッシュ区切りでないとダメ)
     * @return インフォメーションアイコンSEQリスト
     */
    public List<Integer> createIconSeqList(HTypeSiteType siteType, String iconPc) {

        // サイトごとにアイコンSEQリストを作成
        String[] iconSeqArray = null;
        if ((siteType.isFrontPC()) && StringUtil.isNotEmpty(iconPc)) {
            iconSeqArray = iconPc.split(SEPARATOR_SLASH);
        } else if (siteType.isBack()) {
            // TreeSet
            Set<String> treeSet = new TreeSet<>();
            if (StringUtil.isNotEmpty(iconPc)) {
                treeSet.addAll(Arrays.asList(iconPc.split(SEPARATOR_SLASH)));
            }
            iconSeqArray = treeSet.toArray(new String[] {});
        }

        // Integerに変換して詰め替え
        List<Integer> iconSeqList = new ArrayList<>();
        if (iconSeqArray == null) {
            return iconSeqList;
        }
        for (String iconSeq : iconSeqArray) {
            if (StringUtil.isNotEmpty(iconSeq)) {
                iconSeqList.add(conversionUtility.toInteger(iconSeq));
            }
        }
        return iconSeqList;
    }

    /**
     * 規格単位の在庫状況を商品グループ単位に変換
     *
     * @param goodsDtoList 商品DTOリスト
     * @param goodsStockStatusMap 規格単位の在庫ステータスMAP＜商品SEQ、在庫状況＞
     * @return 在庫状況
     */
    public HTypeStockStatusType convertGoodsGroupStockStatus(List<GoodsDto> goodsDtoList,
                                                             Map<Integer, HTypeStockStatusType> goodsStockStatusMap) {
        HTypeStockStatusType status = null;
        for (GoodsDto goodsDto : goodsDtoList) {
            HTypeStockStatusType currentStatus = goodsStockStatusMap.get(goodsDto.getGoodsEntity().getGoodsSeq());
            // より大きい優先度の在庫状態を採用する
            status = getPriorityStatus(currentStatus, status);
        }
        return status;
    }

    /**
     * 優先度の高い在庫状況を返却
     *
     * @param currentStatus 現在処理中の在庫状況
     * @param status 前回までの在庫状況
     * @return 優先度の高い在庫状況
     */
    public HTypeStockStatusType getPriorityStatus(HTypeStockStatusType currentStatus, HTypeStockStatusType status) {
        if (status == null) {
            return currentStatus;
        }
        // if (currentStatus.getOrdinal() > status.getOrdinal()) {
        // return currentStatus;
        // }
        return status;
    }

}