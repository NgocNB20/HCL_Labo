/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.utility;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 商品系ヘルパークラス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsUtility {

    /**
     * 商品表示価格最高値<br/>
     * 商品最安値算出時に使用(DBの桁数に合わせて8ケタ)<br/>
     */
    public static final BigDecimal GOODS_DISPLAY_MAX_PRICE = new BigDecimal("99999999");

    /** 規格の最大値<br/> */
    protected static final Integer UNIT_LENGTH = 2;

    /** 画像なし画像名<br/> */
    public static final String NO_IMAGE_FILENAME = "noimage";

    /** 区切り文字：スラッシュ<br/> */
    public static final String SEPARATOR_SLASH = "/";

    /** 画像なし画像拡張子<br/> */
    // protected static final String JPEG = ".jpg";
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
     * 商品グループ画像ファイル名作成<br/>
     *
     * @param goodsGroupCode 商品管理番号
     * @param versionNo 画像連番
     * @return ファイル名
     */
    public String createImageFileName(String goodsGroupCode, Integer versionNo, String imageExt) {

        ImageUtility imageUtility = ApplicationContextUtility.getBean(ImageUtility.class);

        String separator = PropertiesUtil.getSystemPropertiesValue("goodsimage.name.separator");

        // プロパティに該当情報が存在しない場合
        if (separator == null) {
            return null;
        }

        // 画像ファイル名を作成
        // (商品管理番号_画像種別内連番_画像種別)
        StringBuilder sb = new StringBuilder();
        sb.append(goodsGroupCode);
        sb.append(separator);
        sb.append(String.format("%02d", versionNo));

        // 拡張子を付加
        String fileName = null;
        if (imageExt != null) {
            fileName = imageUtility.createImageFileNameExtension(imageExt, sb.toString());
        } else {
            fileName = sb.toString();
        }

        return fileName;
    }

    /**
     * 商品画像パスの取得<br/>
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
     * 画像名の取得<br/>
     * 画像が[存在しない or 非表示]の場合nullを返す
     *
     * @param goodsGroupDto 商品グループ
     * @return 商品画像名
     */
    public String getImageFileName(GoodsGroupDto goodsGroupDto) {
        return getImageFileName(goodsGroupDto.getGoodsGroupImageEntityList());
    }

    /**
     * 画像名の取得<br/>
     * 画像が[存在しない or 非表示]の場合nullを返す
     *
     * @param goodsDetailsDto 商品詳細
     * @return 商品画像名
     */
    public String getImageFileName(GoodsDetailsDto goodsDetailsDto) {
        return getImageFileName(goodsDetailsDto.getGoodsGroupImageEntityList());
    }

    /**
     * 画像名の取得<br/>
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
            // 1件目を返却
            return goodsGroupImageEntityList.get(0).getImageFileName();
        }
        return null;
    }

    /**
     * 商品公開判定<br/>
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
     * 商品公開判定<br/>
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
        if (HTypeOpenDeleteStatus.OPEN.equals(goodsOpenStatus)) {
            // 日付関連Helper取得
            DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
            return dateHelper.isOpen(openStartTime, openEndTime, targetTime);
        }
        return false;
    }

    /**
     * PC用商品販売判定<br/>
     * ※現在日時
     *
     * @param entity 商品
     * @return true=販売中、false=販売中でない
     */
    public boolean isGoodsSalesPc(GoodsEntity entity) {
        return isGoodsSales(entity.getSaleStatusPC(), entity.getSaleStartTimePC(), entity.getSaleEndTimePC());
    }

    /**
     * 商品販売判定<br/>
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
     * 商品販売判定<br/>
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
     * system.properties から新着商品画像の表示日数（whatsnew.view.days）を取得する
     *
     * @return 新着商品画像の表示期間
     */
    public int getWhatsNewViewDays() {
        String whatsnewViewDays = PropertiesUtil.getSystemPropertiesValue("whatsnew.view.days");
        int addDay = 0;
        if (StringUtil.isNotEmpty(whatsnewViewDays)) {
            addDay = Integer.parseInt(whatsnewViewDays);
        }

        return addDay;
    }

    /**
     * 新着商品をいつまで表示するかの、「いつまで」の日付を取得<br/>
     * この日付まで新着商品として表示される
     *
     * @param whatsNewDate 商品データに登録されている新着日付を想定
     * @return 指定された日付に、新着商品画像の表示期間を足した日付
     */
    public Timestamp getRealWhatsNewDate(Timestamp whatsNewDate) {
        // 日付関連Helper取得
        DateUtility dateHelper = ApplicationContextUtility.getBean(DateUtility.class);
        return dateHelper.getAmountDayTimestamp(getWhatsNewViewDays(), true, whatsNewDate);
    }

    /**
     * 在庫チェック<br/>
     *
     * @param stockManagementFlag 在庫管理フラグ
     * @param salesPossibleStock 販売可能在庫数
     * @return true..在庫あり
     */
    public boolean isGoodsStock(HTypeStockManagementFlag stockManagementFlag, BigDecimal salesPossibleStock) {
        if (HTypeStockManagementFlag.OFF.equals(stockManagementFlag)) {
            // 在庫管理なし
            return true;
        } else if (salesPossibleStock.intValue() > 0) {
            // 在庫あり
            return true;
        }
        return false;
    }

    /**
     * 在庫なしメッセージ付加<br/>
     * ※在庫切れの規格値に在庫なしの文言を付加する
     *
     * @param stockManagementFlag 在庫管理フラグ
     * @param salesPossibleStock 販売可能在庫数
     * @param value 文言付加対象文字列
     * @return 文字列
     */
    public String addNoStockMessage(HTypeStockManagementFlag stockManagementFlag,
                                    BigDecimal salesPossibleStock,
                                    String value) {
        if (this.isGoodsStock(stockManagementFlag, salesPossibleStock)) {
            return value;
        }
        return this.addNoStockMessage(value);
    }

    /**
     * 在庫なしメッセージ付加<br/>
     * ※在庫切れの規格値に在庫なしの文言を付加する
     *
     * @param value 文言付加対象文字列
     * @return 文字列
     */
    public String addNoStockMessage(String value) {
        return value + NO_STOCK_MESSAGE;
    }

    /**
     * 商品グループ在庫の表示判定<br/>
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
     * 規格の全組み合わせを作成する(規格プルダウン表示用)<br/>
     * <pre>
     * 商品コードと規格画像コードの全組合わせを文字列で返却する
     * 【例】
     * [商品情報]
     * 商品コード/規格名1/規格名2/規格画像コード/
     * goods1/赤/S/RED
     * goods2/青/S/BLUE
     * goods3/緑/S/GREEN
     * goods4/赤/M/RED
     * [戻り値]
     * goods1,RED,goods2,BLUE,goods3,GREEN,goods4,RED
     * </pre>
     *
     * @param goodsDtoList 商品DTOリスト
     * @param gcdMap 商品コードMAP(key=商品コード、value=規格配列[規格１、規格２])
     * @param unit1Map 規格１MAP(key=規格１、value=規格２配列[商品コード、規格２、在庫フラグ])
     */
    public void createAllUnitMap(List<GoodsDto> goodsDtoList,
                                 Map<String, String[]> gcdMap,
                                 Map<String, List<String[]>> unit1Map) {

        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);

        // プルダウン作成のために各MAP(全規格の組み合わせ)を作成
        for (GoodsDto goodsDto : goodsDtoList) {

            GoodsEntity entity = goodsDto.getGoodsEntity();

            // 販売チェック
            // 購入可能なもののみ、プルダウンにセット
            if (!goodsUtility.isGoodsSales(
                            entity.getSaleStatusPC(), entity.getSaleStartTimePC(), entity.getSaleEndTimePC())) {
                continue;
            }

            // 規格情報を取得
            String unitGcd = entity.getGoodsCode();
            String unitValue1 = entity.getUnitValue1();
            String unitValue2 = entity.getUnitValue2();
            Boolean isStock = goodsUtility.isGoodsStock(goodsDto.getGoodsEntity().getStockManagementFlag(),
                                                        goodsDto.getStockDto().getSalesPossibleStock()
                                                       );

            // 規格２配列リスト
            List<String[]> tmpUnit1MapArrayList = new ArrayList<>();
            if (unit1Map.get(unitValue1) != null) {
                // 規格情報MAPに現在の規格１があれば、規格２配列リストへ追加
                tmpUnit1MapArrayList = unit1Map.get(unitValue1);
            }

            // MAPに追加する配列を生成
            String[] tmpGcdMapArray = {unitValue1, unitValue2};
            String[] tmpUnit1MapArray = {unitGcd, unitValue2, isStock.toString()};
            tmpUnit1MapArrayList.add(tmpUnit1MapArray);

            // MAPに格納
            gcdMap.put(unitGcd, tmpGcdMapArray);
            unit1Map.put(unitValue1, tmpUnit1MapArrayList);
        }
    }

    /**
     * 商品の規格１～２の値を連携する<br/>
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
                value = goodsEntity.getUnitValue1();
            } else if (2 == i) {
                value = goodsEntity.getUnitValue2();
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
     * 商品DTOリストを編集
     *
     * @param goodsDtoList 商品DTOリスト
     * @return goodsDtoList 商品DTOリスト
     */
    public List<GoodsDto> editGoodsDtoList(List<GoodsDto> goodsDtoList) {

        List<GoodsDto> editGoodsDtoList = new ArrayList<>();

        for (int i = 0; i < goodsDtoList.size(); i++) {
            GoodsDto goodsDto = goodsDtoList.get(i);
            // 規格削除の場合はGoodsDtoListから除外する。
            if (!goodsDto.getGoodsEntity().getSaleStatusPC().equals(HTypeGoodsSaleStatus.DELETED)) {
                editGoodsDtoList.add(goodsDto);
            }
        }
        return editGoodsDtoList;
    }

    /**
     * 指定のサイトごとにインフォメーションアイコンSEQリストを作成します。<br/>
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
     * 規格単位の在庫状況を商品グループ単位に変換<br/>
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
     * 優先度の高い在庫状況を返却<br/>
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

    /**
     * 商品名と規格名をつなげた文字列を返却する<br/>
     *
     * @param goodsDetailsDto 商品詳細DTO
     * @return 商品名（規格名）
     */
    public String createGoodsNameAndUnitValue(GoodsDetailsDto goodsDetailsDto) {
        String goodsName = goodsDetailsDto.getGoodsGroupName();

        if (StringUtil.isNotEmpty(goodsDetailsDto.getUnitValue1())) {
            // 規格2は入力無しのケース有のため、nullの場合は空文字として扱う
            String unitValue2 = "";
            if (StringUtil.isNotEmpty(goodsDetailsDto.getUnitValue2())) {
                unitValue2 = GoodsUtility.SEPARATOR_SLASH + goodsDetailsDto.getUnitValue2();
            }
            String unitvalue = goodsDetailsDto.getUnitValue1() + unitValue2;
            goodsName += "(" + unitvalue + ")";
        }

        return goodsName;
    }
}