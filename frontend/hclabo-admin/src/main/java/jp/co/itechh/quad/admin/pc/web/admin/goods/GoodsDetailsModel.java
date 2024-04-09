// CHECKSTYLE:OFF
// ファイルサイズオーバーを許容：単純に項目が多いため。
// CHECKSTYLE:ON
/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.GoodsCategoryTreeItem;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品管理：商品詳細ページ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsDetailsModel extends AbstractGoodsRegistUpdateModel {

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public GoodsDetailsModel() {
        super();
    }

    /** 再検索フラグ */
    private String md;

    /************************************
     ** 削除処理時の退避用
     ************************************/
    /**
     * 公開状態PC<br/>
     */
    private HTypeOpenDeleteStatus oldGoodsOpenStatusPC;

    /************************************
     ** 表示項目（商品基本設定部分）
     ************************************/
    /**
     * 商品グループ名<br/>
     */
    private String goodsGroupName;

    /**
     * 単価＝商品単価（税抜）<br/>
     */
    private BigDecimal goodsPrice;

    /** 税率 */
    private BigDecimal taxRate;

    /**
     * 酒類フラグ<br/>
     */
    private String alcoholFlag;

    /** ノベルティ商品フラグ */
    private String noveltyGoodsType;

    /** SNS連携フラグ */
    private String snsLinkFlag;

    /**
     * 登録日時<br/>
     */
    private Timestamp registTime;

    /**
     * 更新日時<br/>
     */
    private Timestamp updateTime;

    /**
     * 新着日時<br/>
     */
    private String whatsnewDate;

    /**
     * 個別配送<br/>
     */
    private String individualDeliveryType;

    /**
     * 無料配送<br/>
     */
    private String freeDeliveryFlag;

    /**
     * 公開状態PC<br/>
     */
    private String goodsOpenStatusPC;

    /**
     * 公開開始日PC<br/>
     */
    private Timestamp openStartDatePC;

    /**
     * 公開終了日PC<br/>
     */
    private Timestamp openEndDatePC;

    /**
     * 公開開始時間PC<br/>
     */
    private Timestamp openStartTimePC;

    /**
     * 公開終了時間PC<br/>
     */
    private Timestamp openEndTimePC;

    /************************************
     ** 表示項目(商品グループ在庫状態)
     ************************************/

    /**
     * 商品グループ在庫状態PC（在庫状況更新バッチ実行時点）<br/>
     */
    private String batchUpdateStockStatusPc;

    /**
     * 商品グループ在庫状態PC（リアルタイム）<br/>
     */
    private String realTimeStockStatusPc;

    /************************************
     ** 表示項目（カテゴリ設定部分）
     ************************************/
    /**
     * 登録カテゴリリスト<br/>
     */
    private List<CategoryEntity> registCategoryEntityList;

    /**
     * カテゴリリスト<br/>
     */
    private List<CategoryEntity> categoryEntityList;

    /**
     * 登録カテゴリリスト（画面表示用 改行付き）<br/>
     */
    private String registCategory;

    /**
     * 検索キーワード<br/>
     */
    public String searchKeyword;

    /**
     * metaDescription<br/>
     */
    private String metaDescription;

    /**
     * metaKeyword<br/>
     */
    private String metaKeyword;

    /************************************
     ** 全アイコン情報リスト（商品詳細設定部分）
     ************************************/
    /**
     * インフォメーションアイコン情報リスト<br/>
     * （ショップに登録された全アイコンリスト）
     */
    private List<GoodsInformationIconDto> iconList;

    /**
     * インフォメーションアイコン情報リスト<br/>
     * （現在登録更新中の商品に設定している情報を保持しているリスト）
     */
    private List<GoodsDetailsInformationIconItem> goodsInformationIconItems;

    /************************************
     ** 表示項目（商品詳細テキスト設定）
     ************************************/
    /**
     * 商品納期<br/>
     */
    private String deliveryType;

    /**
     * 商品説明01<br/>
     */
    private String goodsNote1;

    /**
     * 商品説明02<br/>
     */
    private String goodsNote2;

    /**
     * 商品説明03<br/>
     */
    private String goodsNote3;

    /**
     * 商品説明04<br/>
     */
    private String goodsNote4;

    /**
     * 商品説明05<br/>
     */
    private String goodsNote5;

    /**
     * 商品説明06<br/>
     */
    private String goodsNote6;

    /**
     * 商品説明07<br/>
     */
    private String goodsNote7;

    /**
     * 商品説明08<br/>
     */
    private String goodsNote8;

    /**
     * 商品説明09<br/>
     */
    private String goodsNote9;

    /**
     * 商品説明10<br/>
     */
    private String goodsNote10;

    /************************************
     ** 表示項目（受注連携設定）
     ************************************/
    /**
     * 受注連携設定01<br/>
     */
    private String orderSetting1;

    /**
     * 受注連携設定02<br/>
     */
    private String orderSetting2;

    /**
     * 受注連携設定03<br/>
     */
    private String orderSetting3;

    /**
     * 受注連携設定04<br/>
     */
    private String orderSetting4;

    /**
     * 受注連携設定05<br/>
     */
    private String orderSetting5;

    /**
     * 受注連携設定06<br/>
     */
    private String orderSetting6;

    /**
     * 受注連携設定07<br/>
     */
    private String orderSetting7;

    /**
     * 受注連携設定08<br/>
     */
    private String orderSetting8;

    /**
     * 受注連携設定09<br/>
     */
    private String orderSetting9;

    /**
     * 受注連携設定10<br/>
     */
    private String orderSetting10;

    /************************************
     ** 表示項目（商品規格部分）
     ************************************/
    /**
     * 規格管理フラグ<br/>
     */
    private String unitManagementFlag;

    /**
     * 規格１表示名<br/>
     */
    private String unitTitle1;

    /**
     * 規格２表示名<br/>
     */
    private String unitTitle2;

    /************************************
     ** リスト項目（商品規格設定）
     ************************************/
    /**
     * 商品規格リスト<br/>
     */
    private List<GoodsDetailsUnitItem> unitItems;

    /************************************
     ** 表示項目（商品在庫部分）
     ************************************/
    /**
     * 在庫管理フラグ<br/>
     */
    private String stockManagementFlag;

    /************************************
     ** リスト項目（商品在庫部分）
     ************************************/
    /**
     * 商品規格リスト<br/>
     */
    private List<GoodsDetailsStockItem> stockItems;

    /************************************
     ** リスト項目（関連商品部分）
     ************************************/
    /**
     * 関連商品リスト<br/>
     */
    private List<GoodsDetailsRelationItem> relationItems;

    /**
     * 関連商品リスト（空）<br/>
     */
    private List<GoodsDetailsRelationItem> relationNoItems;

    /**
     * 登録商品タグリリスト<br/>
     */
    private List<String> goodsTagList;

    /**
     * 商品ステータス判定<br/>
     * 公開状態が削除の場合は、<br/>
     * 「削除」ボタンを非表示にする<br/>
     *
     * @return false=ステータス「削除」
     */
    public boolean isDeleteGoods() {
        // PC公開状態が「削除」の場合
        if (this.goodsOpenStatusPC.equals(HTypeOpenDeleteStatus.DELETED.getValue())) {
            return false;
        }
        return true;
    }

    /************************************
     ** 商品画像項目 ここから
     ************************************/
    /**
     * 商品画像アイテムリスト
     */
    private List<GoodsDetailsImageItem> detailsPageDetailsImageItems;

    /**
     * 選択した商品Seq
     */
    private Integer selectGoodsSeq;

    /**
     * 商品カテゴリーアイテムリスト
     */
    private List<GoodsCategoryTreeItem> categoryTrees;

    /************************************
     ** フロント表示 / プレビュー用項目 ここから
     ************************************/

    /** フロント表示 */
    private String frontDisplay;

    /** プレビュー日付 */
    @NotEmpty(message = "{HSeparateDateTimeValidator.NOT_DATE_detail}", groups = {PreviewGroup.class})
    @HCDate
    @HVDate(groups = {PreviewGroup.class})
    private String previewDate;

    /** プレビュー時間 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", message = HDateValidator.NOT_DATE_TIME_MESSAGE_ID, groups = {PreviewGroup.class})
    private String previewTime;

    /** プレビューアクセスキー */
    private String preKey;

    /** プレビュー日時 */
    private String preTime;
}
