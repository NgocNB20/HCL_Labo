/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.entity;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliverySpecialChargeAreaDao;
import jp.co.itechh.quad.core.dto.shop.delivery.ReceiverDateDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodTypeCarriageListGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.ReceiverDateGetLogic;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.domain.inventory.proxy.InventoryProxyService;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.InventoryTargetItem;
import jp.co.itechh.quad.ddd.domain.inventory.valueobject.ItemCount;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.AssertException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 配送伝票ドメインサービス
 */
@Service
public class ShippingSlipEntityService {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 顧客アダプター */
    private final ICustomerAdapter customerAdapter;

    /** 注文アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送方法取得ロジック */
    private final DeliveryMethodGetLogic deliveryMethodGetLogic;

    /** 配送不可能エリア取得 */
    private final DeliveryImpossibleAreaGetLogic deliveryImpossibleAreaGetLogic;

    /** 特別配送料金エリアDao */
    private final DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao;

    /** 配送区分送料リスト取得ロジック */
    private final DeliveryMethodTypeCarriageListGetLogic deliveryMethodTypeCarriageListGetLogic;

    /** お届け希望日取得ロジック */
    private final ReceiverDateGetLogic receiverDateGetLogic;

    /** 在庫プロキシサービス */
    private final InventoryProxyService inventoryProxyService;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティクラス */
    private final DateUtility dateUtility;

    /** 会員業務ユーティリティクラス */
    private final MemberInfoUtility memberInfoUtility;

    /** コンストラクタ */
    @Autowired
    public ShippingSlipEntityService(IAddressBookRepository addressBookRepository,
                                     IProductAdapter productAdapter,
                                     ICustomerAdapter customerAdapter,
                                     IOrderSlipAdapter orderSlipAdapter,
                                     DeliveryMethodGetLogic deliveryMethodGetLogic,
                                     DeliveryImpossibleAreaGetLogic deliveryImpossibleAreaGetLogic,
                                     DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao,
                                     DeliveryMethodTypeCarriageListGetLogic deliveryMethodTypeCarriageListGetLogic,
                                     ReceiverDateGetLogic receiverDateGetLogic,
                                     InventoryProxyService inventoryProxyService,
                                     ConversionUtility conversionUtility,
                                     DateUtility dateUtility,
                                     MemberInfoUtility memberInfoUtility) {
        this.addressBookRepository = addressBookRepository;
        this.productAdapter = productAdapter;
        this.customerAdapter = customerAdapter;
        this.orderSlipAdapter = orderSlipAdapter;
        this.deliveryMethodGetLogic = deliveryMethodGetLogic;
        this.deliveryImpossibleAreaGetLogic = deliveryImpossibleAreaGetLogic;
        this.deliverySpecialChargeAreaDao = deliverySpecialChargeAreaDao;
        this.deliveryMethodTypeCarriageListGetLogic = deliveryMethodTypeCarriageListGetLogic;
        this.receiverDateGetLogic = receiverDateGetLogic;
        this.inventoryProxyService = inventoryProxyService;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.memberInfoUtility = memberInfoUtility;
    }

    /**
     * 配送方法を設定する
     *
     * @param shippingSlipEntity   対象配送伝票
     * @param shippingMethodId     配送方法ID
     * @param receiverDate         お届け日
     * @param receiverTimeZone     お届け希望時間帯
     * @param invoiceNecessaryFlag 納品書要否フラグ
     */
    public void settingShippingMethod(ShippingSlipEntity shippingSlipEntity,
                                      String shippingMethodId,
                                      Date receiverDate,
                                      String receiverTimeZone,
                                      boolean invoiceNecessaryFlag) {

        // 配送方法を取得する
        DeliveryMethodEntity deliveryMethodDbEntity =
                        this.deliveryMethodGetLogic.execute(this.conversionUtility.toInteger(shippingMethodId));

        // 配送方法が取得できない場合、不正な呼び出しとみなしエラー
        if (deliveryMethodDbEntity == null) {
            // 配送方法取得失敗
            throw new DomainException("LOGISTIC-DMDE0001-E", new String[] {shippingMethodId});
        }

        // お届け不可日チェック
        if (this.receiverDateGetLogic.checkDeliveryImpossibleDay(
                        receiverDate, deliveryMethodDbEntity.getDeliveryMethodSeq())) {
            throw new DomainException("HM34-4001-003-L-E");
        }

        // 配送方法を設定する
        shippingSlipEntity.settingShippingMethod(shippingMethodId, deliveryMethodDbEntity.getDeliveryMethodName());

        // 配送希望条件を設定する
        shippingSlipEntity.settingShippingRequirements(receiverDate, receiverTimeZone, invoiceNecessaryFlag);

    }

    /**
     * 在庫確保して配送商品に設定
     *
     * @param securedShippingItemList
     * @param shippingSlipEntity
     */
    public void secureInventoryForShippingItem(List<SecuredShippingItem> securedShippingItemList,
                                               ShippingSlipEntity shippingSlipEntity) {

        // チェック
        // 引数が存在しない場合エラー
        AssertChecker.assertNotEmpty("securedShippingItemList is empty", securedShippingItemList);
        // 下書き状態でないならエラー
        if (shippingSlipEntity.getShippingStatus() != ShippingStatus.DRAFT) {
            throw new DomainException("LOGISTIC-SHST0001-E");
        }
        // 配送先住所IDが未設定の場合はエラー
        AssertChecker.assertNotNull("shippingAddressId is null", shippingSlipEntity.getShippingAddressId());
        // 配送方法IDが未設定の場合はエラー
        AssertChecker.assertNotEmpty("shippingMethodId is null", shippingSlipEntity.getShippingMethodId());
        // 確保済み配送商品リストが既に設定されている場合はエラー
        if (!CollectionUtils.isEmpty(shippingSlipEntity.getSecuredShippingItemList())) {
            throw new AssertException("securedShippingItemList is not null");
        }
        // 在庫引き当て処理を実行
        int result = inventoryProxyService.secureInventory(toInventoryTargetItem(securedShippingItemList));
        if (result != securedShippingItemList.size()) {
            // 在庫引き当て失敗
            throw new DomainException("LOGISTIC-REDL0001-E");
        }

        // 確保済み配送商品へ登録
        shippingSlipEntity.setSecuredShippingItemList(securedShippingItemList);

        // 配送ステータス在庫確保
        shippingSlipEntity.setShippingStatus(ShippingStatus.SECURED_INVENTORY);
    }

    /**
     * 配送チェック
     *
     * @param shippingSlipEntity 対象配送伝票
     */
    public void checkShipping(ShippingSlipEntity shippingSlipEntity) {

        ApplicationException appException = new ApplicationException();

        // チェック
        AssertChecker.assertNotNull("shippingSlipEntity is null", shippingSlipEntity);

        // 配送方法チェック
        checkShippingMethod(shippingSlipEntity, appException);

        // エラーがあった場合
        if (appException.hasMessage()) {
            throw appException;
        }
    }

    /**
     * 配送商品リストから在庫対象商品リストへ変換
     *
     * @param shippingItemList 配送商品リスト
     * @return list 在庫対象商品リスト
     */
    private List<InventoryTargetItem> toInventoryTargetItem(List<SecuredShippingItem> shippingItemList) {

        // 引数のリストが存在しない場合はNullで返却
        if (CollectionUtils.isEmpty(shippingItemList)) {
            return null;
        }

        List<InventoryTargetItem> list = new ArrayList<>();

        for (int i = 0; i < shippingItemList.size(); i++) {
            list.add(new InventoryTargetItem(shippingItemList.get(i).getItemId(),
                                             new ItemCount(shippingItemList.get(i).getShippingCount().getValue())
            ));
        }

        return list;
    }

    /**
     * 配送方法情報を取得し、配送方法をチェック<br/>
     * 配送先住所ID、または配送方法IDが配送伝票に設定されていない場合は、未設定項目のメッセージをセットし、チェックを終了する<br/>
     * ※改訂用配送伝票でも使用するためパッケージプライベート
     *
     * @param shippingSlipEntity 配送伝票
     * @param errorList          エラーリスト
     */
    void checkShippingMethod(ShippingSlipEntity shippingSlipEntity, ApplicationException errorList) {

        // 配送先住所IDが設定されていない場合
        if (shippingSlipEntity.getShippingAddressId() == null) {
            errorList.addMessage("LOGISTIC-ENSA0001-E");
        }
        // 配送方法IDが設定されていない場合
        if (StringUtils.isBlank(shippingSlipEntity.getShippingMethodId())) {
            errorList.addMessage("LOGISTIC-ENSM0001-E");
        }

        if (shippingSlipEntity.getShippingAddressId() == null || StringUtils.isBlank(
                        shippingSlipEntity.getShippingMethodId())) {
            return;
        }

        // 配送方法を取得する
        DeliveryMethodEntity deliveryMethodDbEntity = this.deliveryMethodGetLogic.execute(
                        this.conversionUtility.toInteger(shippingSlipEntity.getShippingMethodId()));
        if (deliveryMethodDbEntity == null) {
            throw new DomainException("LOGISTIC-DMDE0001-E", new String[] {shippingSlipEntity.getShippingMethodId()});
        }

        // 住所録を取得する
        AddressBookEntity addressBookEntity = this.addressBookRepository.get(
                        new AddressId(shippingSlipEntity.getShippingAddressId().getValue()));
        if (addressBookEntity == null) {
            throw new DomainException(
                            "LOGISTIC-ADDB0001-E", new String[] {shippingSlipEntity.getShippingAddressId().getValue()});
        }

        // 配送方法の公開状態チェック
        checkOpen(deliveryMethodDbEntity.getOpenStatusPC(), errorList);

        // 配送不可能エリアチェック
        checkDeliveryImpossibleArea(shippingSlipEntity.getShippingMethodId(),
                                    addressBookEntity.getAddress().getZipCode(), errorList
                                   );

        // 都道府県送料設定チェック
        checkDeliveryRestriction(shippingSlipEntity.getShippingMethodId(), addressBookEntity.getAddress().getZipCode(),
                                 addressBookEntity.getAddress().getPrefecture(),
                                 deliveryMethodDbEntity.getDeliveryMethodType(), errorList
                                );

        // お届け希望日範囲内かチェック
        checkDeliveryDateRange(shippingSlipEntity.getShippingMethodId(), shippingSlipEntity.getReceiverDate(),
                               deliveryMethodDbEntity.getDeliveryLeadTime(),
                               deliveryMethodDbEntity.getPossibleSelectDays(), errorList
                              );

        // お届け希望時間帯範囲内かチェック
        checkReceiverTimeZone(shippingSlipEntity.getReceiverTimeZone(), deliveryMethodDbEntity, errorList);
    }

    /**
     * 配送方法の公開状態チェック<br/>
     *
     * @param openStatus 公開状態
     * @param errorList  エラーリスト
     */
    private void checkOpen(HTypeOpenDeleteStatus openStatus, ApplicationException errorList) {
        // 公開中以外の場合はエラー
        if (!HTypeOpenDeleteStatus.OPEN.equals(openStatus)) {
            errorList.addMessage("LOGISTIC-ERNO0001-E");
        }
    }

    /**
     * 配送方法不可能エリアチェック<br/>
     *
     * @param shippingMethodId 配送方法ID
     * @param zipCode          配送先郵便番号
     * @param errorList        エラーリスト
     */
    protected void checkDeliveryImpossibleArea(String shippingMethodId,
                                               String zipCode,
                                               ApplicationException errorList) {

        DeliveryImpossibleAreaEntity deliveryImpossibleAreaDbEntity = null;
        if (shippingMethodId != null && zipCode != null) {
            deliveryImpossibleAreaDbEntity = this.deliveryImpossibleAreaGetLogic.execute(
                            this.conversionUtility.toInteger(shippingMethodId), zipCode);
        }

        // 配送不可能エリアが存在する場合はエラー
        if (deliveryImpossibleAreaDbEntity != null) {
            errorList.addMessage("LOGISTIC-ERNI0001-E");
        }
    }

    /**
     * 配送種別ごとの制約チェック<br/>
     * ※HM3.6から金額別送料は未使用のため、チェックしていない
     *
     * @param shippingMethodId 配送方法ID
     * @param zipCode          配送先郵便番号
     * @param prefecture       配送先都道府県
     * @param methodType       配送種別
     * @param errorList        エラーリスト
     */
    protected void checkDeliveryRestriction(String shippingMethodId,
                                            String zipCode,
                                            String prefecture,
                                            HTypeDeliveryMethodType methodType,
                                            ApplicationException errorList) {

        // 配送種別に応じてチェックを行なう
        // 配送種別：都道府県別
        if (HTypeDeliveryMethodType.PREFECTURE.equals(methodType)) {

            // 配送特別料金エリアの取得
            DeliverySpecialChargeAreaEntity deliverySpecialChargeDbArea = this.deliverySpecialChargeAreaDao.getEntity(
                            this.conversionUtility.toInteger(shippingMethodId), zipCode);

            // 配送区分別送料の一覧を取得
            List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageDbEntityList =
                            this.deliveryMethodTypeCarriageListGetLogic.execute(
                                            this.conversionUtility.toInteger(shippingMethodId));

            // 配送区分送料リスト存在チェック
            boolean isExistDelivery = false;
            for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageDbEntity : deliveryMethodTypeCarriageDbEntityList) {
                if (deliveryMethodTypeCarriageDbEntity.getPrefectureType().getLabel().contains(prefecture)) {
                    isExistDelivery = true;
                    break;
                }
            }
            // 送料未設定の都道府県かつ特別料金エリアにも登録されていない場合はエラー
            if (!isExistDelivery && deliverySpecialChargeDbArea == null) {
                errorList.addMessage("LOGISTIC-ENDR0001-E");
            }
        }
    }

    /**
     * お届け希望時間帯チェック<br/>
     *
     * @param shippingMethodId 配送方法ID
     * @param receiverDate     お届け希望日
     * @param leadTime         リードタイム
     * @param selectDays       選択可能日数
     * @param errorList        エラーリスト
     */
    protected void checkDeliveryDateRange(String shippingMethodId,
                                          Date receiverDate,
                                          int leadTime,
                                          int selectDays,
                                          ApplicationException errorList) {

        // お届け希望日がない場合、チェック不要
        if (receiverDate == null) {
            return;
        }

        ReceiverDateDto receiverDateDto =
                        this.receiverDateGetLogic.checkCreateReceiverDateList(leadTime, selectDays, false,
                                                                              this.conversionUtility.toInteger(
                                                                                              shippingMethodId)
                                                                             );

        // お届け希望日をチェック用に変換
        String checkReceiverDate = this.dateUtility.formatYmd(this.conversionUtility.toTimestamp(receiverDate));

        // 選択されたお届け希望日が選択可能日付Mapに存在しない場合エラー（お届け希望日選択不可の配送方法に対して、お届け希望日を設定している場合も含む）
        if (receiverDateDto.getDateMap() == null || !receiverDateDto.getDateMap().containsKey(checkReceiverDate)) {
            errorList.addMessage("LOGISTIC-ENDD0001-E");
        }
    }

    /**
     * お届け希望時間帯範囲チェック<br/>
     *
     * @param receiverTimeZone       お届け希望時間帯
     * @param deliveryMethodDbEntity 配送方法エンティティ
     * @param errorList              エラーリスト
     */
    protected void checkReceiverTimeZone(String receiverTimeZone,
                                         DeliveryMethodEntity deliveryMethodDbEntity,
                                         ApplicationException errorList) {

        // お届け時間帯がない場合、チェック不要
        if (StringUtils.isBlank(receiverTimeZone)) {
            return;
        }

        // 変数名変換
        String tz1 = deliveryMethodDbEntity.getReceiverTimeZone1();
        String tz2 = deliveryMethodDbEntity.getReceiverTimeZone2();
        String tz3 = deliveryMethodDbEntity.getReceiverTimeZone3();
        String tz4 = deliveryMethodDbEntity.getReceiverTimeZone4();
        String tz5 = deliveryMethodDbEntity.getReceiverTimeZone5();
        String tz6 = deliveryMethodDbEntity.getReceiverTimeZone6();
        String tz7 = deliveryMethodDbEntity.getReceiverTimeZone7();
        String tz8 = deliveryMethodDbEntity.getReceiverTimeZone8();
        String tz9 = deliveryMethodDbEntity.getReceiverTimeZone9();
        String tz10 = deliveryMethodDbEntity.getReceiverTimeZone10();

        // 指定されているお届け希望時間が、配送方法のお届け希望時間帯に設定されていない場合
        if (!receiverTimeZone.equals(tz1) && !receiverTimeZone.equals(tz2) && !receiverTimeZone.equals(tz3)
            && !receiverTimeZone.equals(tz4) && !receiverTimeZone.equals(tz5) && !receiverTimeZone.equals(tz6)
            && !receiverTimeZone.equals(tz7) && !receiverTimeZone.equals(tz8) && !receiverTimeZone.equals(tz9)
            && !receiverTimeZone.equals(tz10)) {
            errorList.addMessage("LOGISTIC-ENRT0001-E");
        }
    }
}