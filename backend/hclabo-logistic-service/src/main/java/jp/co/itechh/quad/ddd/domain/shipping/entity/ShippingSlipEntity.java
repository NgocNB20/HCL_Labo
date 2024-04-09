/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.entity;

import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShipmentStatusConfirmCode;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingItemSeqFactory;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 配送伝票 エンティティ
 */
@Getter
public class ShippingSlipEntity {

    /** 配送伝票ID */
    protected ShippingSlipId shippingSlipId;

    /** 配送ステータス */
    protected ShippingStatus shippingStatus;

    /** 配送状況確認番号 */
    protected ShipmentStatusConfirmCode shipmentStatusConfirmCode;

    /** 配送商品 */
    protected List<SecuredShippingItem> securedShippingItemList;

    /** 配送方法名 */
    protected String shippingMethodName;

    /** お届け希望日 */
    protected Date receiverDate;

    /** お届け希望時間帯 */
    protected String receiverTimeZone;

    /** 納品書要否フラグ */
    protected boolean invoiceNecessaryFlag;

    /** 出荷完了日時 */
    protected Date completeShipmentDate;

    /** 登録日時 */
    protected Date registDate;

    /** 取引ID */
    protected String transactionId;

    /** 配送方法ID */
    protected String shippingMethodId;

    /** 配送先住所ID */
    protected ShippingAddressId shippingAddressId;

    /**
     * コンストラクタ<br/>
     * 配送伝票発行
     *
     * @param transactionId 取引ID
     * @param registDate    登録日時
     */
    public ShippingSlipEntity(String transactionId, Date registDate) {

        // チェック
        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 設定
        this.shippingSlipId = new ShippingSlipId();
        // 配送ステータス下書き
        this.shippingStatus = ShippingStatus.DRAFT;
        // 納品書の選択は不要とする
        this.invoiceNecessaryFlag = false;
        this.transactionId = transactionId;
        this.securedShippingItemList = new ArrayList<>();
        this.registDate = registDate;
    }

    /**
     * コンストラクタ<br/>
     * 配送伝票改訂(改訂用配送伝票から生成)
     *
     * @param shippingSlipForRevisionEntity
     * @param registDate
     */
    public ShippingSlipEntity(ShippingSlipForRevisionEntity shippingSlipForRevisionEntity, Date registDate) {

        // チェック
        AssertChecker.assertNotNull("shippingSlipForRevisionEntity is null", shippingSlipForRevisionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 改訂用配送伝票をコピー
        copyProperties(shippingSlipForRevisionEntity);
        // 改訂用配送伝票IDを引き継いで、配送伝票IDに設定
        this.shippingSlipId = new ShippingSlipId(shippingSlipForRevisionEntity.getShippingSlipRevisionId().getValue());
        // 改訂用取引IDを引き継いで、取引IDに設定
        this.transactionId = shippingSlipForRevisionEntity.getTransactionRevisionId();
        this.registDate = registDate;
    }

    /**
     * 配送先設定
     *
     * @param shippingAddressId 配送先住所ID
     */
    public void settingShippingAddress(ShippingAddressId shippingAddressId) {

        // チェック
        // 下書き状態、在庫確保状態でないならエラー
        if (this.shippingStatus != ShippingStatus.DRAFT && this.shippingStatus != ShippingStatus.SECURED_INVENTORY) {
            throw new DomainException("LOGISTIC-SHST0001-E");
        }

        // 設定
        this.shippingAddressId = shippingAddressId;
    }

    /**
     * 配送方法設定
     *
     * @param shippingMethodId   配送方法ID
     * @param shippingMethodName 配送方法名
     */
    public void settingShippingMethod(String shippingMethodId, String shippingMethodName) {

        // チェック
        AssertChecker.assertNotEmpty("shippingMethodId is empty", shippingMethodId);
        AssertChecker.assertNotEmpty("shippingMethodName is empty", shippingMethodName);

        // 下書き状態、在庫確保状態でないならエラー
        if (this.shippingStatus != ShippingStatus.DRAFT && this.shippingStatus != ShippingStatus.SECURED_INVENTORY) {
            throw new DomainException("LOGISTIC-SHST0001-E");
        }

        // 設定
        this.shippingMethodId = shippingMethodId;
        this.shippingMethodName = shippingMethodName;
    }

    /**
     * 配送希望条件設定<br/>
     * ※パッケージプライベートメソッド
     *
     * @param receiverDate         お届け希望日
     * @param receiverTimeZone     お届け希望時間帯
     * @param invoiceNecessaryFlag 納品書要否フラグ
     */
    void settingShippingRequirements(Date receiverDate, String receiverTimeZone, boolean invoiceNecessaryFlag) {

        // チェック
        // 配送方法IDがない場合はエラー
        AssertChecker.assertNotEmpty("shippingMethodId is empty", shippingMethodId);
        // 下書き状態、在庫確保状態でないならエラー
        if (this.shippingStatus != ShippingStatus.DRAFT && this.shippingStatus != ShippingStatus.SECURED_INVENTORY) {
            throw new DomainException("LOGISTIC-SHST0001-E");
        }

        // 設定
        this.receiverDate = receiverDate;
        this.receiverTimeZone = receiverTimeZone;
        this.invoiceNecessaryFlag = invoiceNecessaryFlag;
    }

    /**
     * 配送希望時間帯デフォルト設定<br/>
     *
     * @param selectAbleDeliveryTimeList 選択可能お届け希望時間帯リスト（配送方法マスタ情報）
     */
    public void settingDefaultDeliveryTime(List<String> selectAbleDeliveryTimeList) {

        // チェック
        // 配送方法IDがない場合はエラー
        AssertChecker.assertNotEmpty("shippingMethodId is empty", this.shippingMethodId);
        // 下書き状態でないならエラー
        if (this.shippingStatus != ShippingStatus.DRAFT) {
            throw new DomainException("LOGISTIC-SHST0001-E");
        }
        // 選択可能お届け時間帯リストが存在する場合のみ、デフォルト値を設定
        if (!CollectionUtils.isEmpty(selectAbleDeliveryTimeList)) {
            this.receiverTimeZone = selectAbleDeliveryTimeList.get(0);
        }
    }

    /**
     * 配送伝票トランザクションデータ最新化
     *
     * @param shippingMethodName 配送方法名
     * @param shippingItemList   配送商品リスト
     */
    public void modernizeShippingSlipTranData(String shippingMethodName, List<SecuredShippingItem> shippingItemList) {
        // 設定
        this.shippingMethodName = shippingMethodName;
        this.securedShippingItemList = shippingItemList;
    }

    /**
     * 下書き戻し
     */
    public void revertDraft() {
        // 在庫確保状態でないならエラー
        if (this.shippingStatus != ShippingStatus.SECURED_INVENTORY) {
            throw new DomainException("LOGISTIC-SECI0001-E");
        }

        // 新しい安全な配送品目リストを作成する
        this.securedShippingItemList = new ArrayList<>();

        // 配送ステータス下書き
        this.shippingStatus = ShippingStatus.DRAFT;
    }

    /**
     * 伝票確定
     */
    public void openSlip() {

        // 在庫確保状態でないならエラー
        if (this.shippingStatus != ShippingStatus.SECURED_INVENTORY) {
            throw new DomainException("LOGISTIC-SECI0001-E");
        }

        // 配送ステータス確定
        this.shippingStatus = ShippingStatus.OPEN;
    }

    /**
     * 配送商品設定
     * ※ドメインサービス用パッケージプライベート
     *
     * @param securedShippingItemList
     */
    void setSecuredShippingItemList(List<SecuredShippingItem> securedShippingItemList) {
        this.securedShippingItemList = securedShippingItemList;
    }

    /**
     * ステータス設定
     * ※ドメインサービス用パッケージプライベート
     *
     * @param securedInventory
     */
    void setShippingStatus(ShippingStatus securedInventory) {
        this.shippingStatus = securedInventory;
    }

    /**
     *  商品リストを個数単位で分割し、数量をすべて1にする
     */
    public void itemListDivision() {
        List<SecuredShippingItem> itemListDivision = new ArrayList<>();
        SecuredShippingItem securedShippingItemNew;

        // 分割前は商品連番順にソートされている想定だ
        this.getSecuredShippingItemList().sort(Comparator.comparing(SecuredShippingItem::getShippingItemSeq));

        for (SecuredShippingItem securedShippingItem : this.getSecuredShippingItemList()) {

            int count = securedShippingItem.getShippingCount().getValue();
            int shippingItemSeqCur = ShippingItemSeqFactory.constructShippingItemSeq(itemListDivision);

            // 数量が1の場合は、変更後商品リストの末尾に追加する
            if (count == 1) {
                securedShippingItemNew = new SecuredShippingItem(shippingItemSeqCur, securedShippingItem.getItemId(),
                                                                 securedShippingItem.getItemName(),
                                                                 securedShippingItem.getUnitTitle1(),
                                                                 securedShippingItem.getUnitValue1(),
                                                                 securedShippingItem.getUnitTitle2(),
                                                                 securedShippingItem.getUnitValue2(),
                                                                 new ShippingCount(1)
                );
                itemListDivision.add(securedShippingItemNew);
            }
            // 数量が2以上の場合は、注文数量が1つずつになるように変更後商品リストの末尾に追加する
            else if (count > 1) {
                for (int i = 0; i < count; i++) {
                    securedShippingItemNew =
                                    new SecuredShippingItem(shippingItemSeqCur++, securedShippingItem.getItemId(),
                                                            securedShippingItem.getItemName(),
                                                            securedShippingItem.getUnitTitle1(),
                                                            securedShippingItem.getUnitValue1(),
                                                            securedShippingItem.getUnitTitle2(),
                                                            securedShippingItem.getUnitValue2(), new ShippingCount(1)
                                    );
                    itemListDivision.add(securedShippingItemNew);
                }
            }

        }

        this.setSecuredShippingItemList(itemListDivision);
    }

    /**
     * コンストラクタ<br/>
     * ※改訂用
     */
    ShippingSlipEntity() {
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public ShippingSlipEntity(ShippingSlipId shippingSlipId,
                              ShippingStatus shippingStatus,
                              ShipmentStatusConfirmCode shipmentStatusConfirmCode,
                              String shippingMethodId,
                              String shippingMethodName,
                              List<SecuredShippingItem> shippingItemList,
                              ShippingAddressId shippingAddressId,
                              String transactionId,
                              boolean invoiceNecessaryFlag,
                              Date receiverDate,
                              String receiverTimeZone,
                              Date completeShipmentDate,
                              Date registDate) {
        this.shippingSlipId = shippingSlipId;
        this.shippingStatus = shippingStatus;
        this.shipmentStatusConfirmCode = shipmentStatusConfirmCode;
        this.shippingMethodId = shippingMethodId;
        this.shippingMethodName = shippingMethodName;
        this.securedShippingItemList = shippingItemList;
        this.shippingAddressId = shippingAddressId;
        this.transactionId = transactionId;
        this.invoiceNecessaryFlag = invoiceNecessaryFlag;
        this.receiverDate = receiverDate;
        this.receiverTimeZone = receiverTimeZone;
        this.completeShipmentDate = completeShipmentDate;
        this.registDate = registDate;
    }

    /**
     * プロパティコピー<br/>
     * ※改訂取引用
     *
     * @param shippingSlipEntity
     */
    protected void copyProperties(ShippingSlipEntity shippingSlipEntity) {

        this.shippingSlipId = shippingSlipEntity.getShippingSlipId();
        this.shippingStatus = shippingSlipEntity.getShippingStatus();
        this.shipmentStatusConfirmCode = shippingSlipEntity.getShipmentStatusConfirmCode();
        this.securedShippingItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shippingSlipEntity.getSecuredShippingItemList())) {
            for (SecuredShippingItem originSecuredShippingItem : shippingSlipEntity.getSecuredShippingItemList()) {
                this.securedShippingItemList.add(originSecuredShippingItem);
            }
        }
        this.shippingMethodName = shippingSlipEntity.getShippingMethodName();
        this.receiverDate = shippingSlipEntity.getReceiverDate();
        this.receiverTimeZone = shippingSlipEntity.getReceiverTimeZone();
        this.invoiceNecessaryFlag = shippingSlipEntity.isInvoiceNecessaryFlag();
        this.completeShipmentDate = shippingSlipEntity.getCompleteShipmentDate();
        this.registDate = shippingSlipEntity.getRegistDate();
        this.transactionId = shippingSlipEntity.getTransactionId();
        this.shippingMethodId = shippingSlipEntity.getShippingMethodId();
        this.shippingAddressId = shippingSlipEntity.getShippingAddressId();
    }
}
