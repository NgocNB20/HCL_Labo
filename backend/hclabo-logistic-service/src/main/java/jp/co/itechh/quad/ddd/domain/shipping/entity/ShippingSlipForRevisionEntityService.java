/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.entity;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.constant.type.HTypeShipedGoodsStockReturn;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.ddd.domain.inventory.repository.IInventoryRepository;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderItem;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingCount;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingStatus;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.shippingslip.presentation.api.param.WarningContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.co.itechh.quad.ddd.exception.BaseException.GLOBAL_MESSAGE_FIELD_NAME;

/**
 * 改訂用配送伝票ドメインサービス
 */
@Service
public class ShippingSlipForRevisionEntityService {

    /** 配送伝票ドメインサービス */
    private final ShippingSlipEntityService shippingSlipEntityService;

    /** 注文アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 在庫リポジトリ */
    private final IInventoryRepository inventoryRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /**
     * 処理件数マップ　ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    public static final String WARNING_MESSAGE = "WarningMessage";

    /** お届け希望日のチェックのエラーメッセージコード */
    public static final String MSGCD_SELECTED_DELIVERY_DATE = "LOGISTIC-ENDD0001-E";

    /** お届け希望日のチェックの警告メッセージコード */
    public static final String WCD_SELECTED_DELIVERY_DATE = "LOGISTIC-ENDD0001-W";

    /** コンストラクタ */
    @Autowired
    public ShippingSlipForRevisionEntityService(ShippingSlipEntityService shippingSlipEntityService,
                                                IOrderSlipAdapter orderSlipAdapter,
                                                IInventoryRepository inventoryRepository,
                                                IProductAdapter productAdapter) {
        this.shippingSlipEntityService = shippingSlipEntityService;
        this.orderSlipAdapter = orderSlipAdapter;
        this.inventoryRepository = inventoryRepository;
        this.productAdapter = productAdapter;
    }

    /**
     * 改訂用配送伝票チェック
     *
     * @param shippingSlipForRevisionEntity 改訂用配送伝票
     * @return 警告メッセージマップ
     */
    public Map<String, List<WarningContent>> checkShippingSlipForRevision(ShippingSlipForRevisionEntity shippingSlipForRevisionEntity) {

        // チェック
        AssertChecker.assertNotNull("shippingSlipForRevisionEntity is null", shippingSlipForRevisionEntity);

        ApplicationException appException = new ApplicationException();

        // 改訂用配送伝票チェック
        shippingSlipEntityService.checkShippingMethod(shippingSlipForRevisionEntity, appException);

        Map<String, List<WarningContent>> warningMapForResponse = new HashMap<>();
        if (appException.hasMessage()) {
            List<ExceptionContent> listError = appException.getMessageMap().get(GLOBAL_MESSAGE_FIELD_NAME);

            for (ExceptionContent error : listError) {
                if (MSGCD_SELECTED_DELIVERY_DATE.equals(error.getCode())) {
                    listError.remove(error);
                    break;
                }
            }

            if (listError.size() > 0) {
                throw appException;
            } else {
                warningMapForResponse = settingWarningMap(WCD_SELECTED_DELIVERY_DATE);
            }
        }
        return warningMapForResponse;
    }

    /**
     * 警告メッセージの設定
     *
     * @param warningCode 警告番号
     * @return 警告メッセージマップ
     */
    public Map<String, List<WarningContent>> settingWarningMap(String warningCode) {

        Map<String, List<WarningContent>> warningMapForResponse = new HashMap<>();

        WarningContent warningContent = new WarningContent();
        warningContent.setCode(warningCode);
        warningContent.setMessage(AppLevelFacesMessageUtil.getAllMessage(warningCode, null).getMessage());

        warningMapForResponse.put(WARNING_MESSAGE, Arrays.asList(warningContent));

        return warningMapForResponse;
    }

    /**
     * 在庫調整して配送商品を更新
     *
     * @param shippingSlipForRevisionEntity
     */
    public void adjustInventoryForShippingItem(ShippingSlipForRevisionEntity shippingSlipForRevisionEntity) {

        // チェック
        if (shippingSlipForRevisionEntity.getShippingStatus() != ShippingStatus.OPEN
            && shippingSlipForRevisionEntity.getShippingStatus() != ShippingStatus.SHIPMENT_COMPLETED) {
            throw new DomainException("LOGISTIC-SHST0002-E");
        }

        /* 改訂後注文商品と現時点での確保済み配送商品の差分を計算して、在庫調整する */

        // 改訂用注文票取得
        OrderSlip orderSlip = orderSlipAdapter.getOrderSlipForRevision(
                        shippingSlipForRevisionEntity.getTransactionRevisionId());
        if (orderSlip == null) {
            throw new DomainException("LOGISTIC-ORDS0001-E",
                                      new String[] {shippingSlipForRevisionEntity.getTransactionRevisionId()}
            );
        }

        // 改訂後注文商品のループ
        for (OrderItem orderItem : orderSlip.getRevisionOrderItemList()) {

            // 配送商品に既に存在する場合の配列インデックス
            int indexOfExistShippingItem = -1;
            // 商品差分数量（注文数量をデフォルトに設定）
            int needSecureCount = orderItem.getOrderCount();

            // 改訂用配送伝票を走査し、差分を計算する
            int i = 0;
            for (SecuredShippingItem securedShippingItem : shippingSlipForRevisionEntity.getSecuredShippingItemList()) {

                // 注文商品連番が一致するかで、配送商品に既に存在するか判定
                if (orderItem.getOrderItemSeq() == securedShippingItem.getShippingItemSeq()) {
                    needSecureCount = orderItem.getOrderCount() - securedShippingItem.getShippingCount().getValue();
                    indexOfExistShippingItem = i;
                    break;
                }
                i++;
            }

            // 出荷前かで分岐して在庫調整を行う
            if (shippingSlipForRevisionEntity.getShippingStatus() == ShippingStatus.OPEN) {
                // 出荷前の場合

                if (needSecureCount > 0) {
                    // 数量増または新規商品追加の場合は、在庫引当を行う

                    // 在庫引当
                    int resCount = inventoryRepository.secureStock(orderItem.getItemId(), needSecureCount);
                    if (resCount != 1) {
                        throw new DomainException("LOGISTIC-SEST0001-E");
                    }

                } else if (needSecureCount < 0) {
                    // 数量減の場合は、在庫引当戻しを行う

                    // 在庫引当戻し
                    int resCount = inventoryRepository.updateSecuredStockRollback(orderItem.getItemId(),
                                                                                  needSecureCount
                                                                                 );
                    if (resCount != 1) {
                        throw new DomainException("LOGISTIC-USTR0001-E");
                    }
                }

            } else {
                // 出荷後の場合

                if (needSecureCount > 0) {
                    // 数量増または新規商品追加の場合は、在庫引当+在庫出荷処理を行う

                    // 在庫引当
                    int resCount = inventoryRepository.secureStock(orderItem.getItemId(), needSecureCount);
                    if (resCount != 1) {
                        throw new DomainException("LOGISTIC-SEST0001-E");
                    }

                    // 在庫出荷
                    int resShipCount = inventoryRepository.updateStockShipping(orderItem.getItemId(), needSecureCount);
                    if (resShipCount != 1) {
                        throw new DomainException("LOGISTIC-SHIC0001-E");
                    }

                } else if (needSecureCount < 0) {
                    // 数量を減らした場合(返品)は、在庫引当戻し+出荷後在庫戻しを行う

                    // 出荷後実在庫戻し
                    if (HTypeShipedGoodsStockReturn.ON.equals(
                                    ShippingSlipForRevisionEntity.SHIPPED_ITEM_STOCK_RETURN)) {
                        // needSecureCountがマイナス値なので実在庫戻しためネガティブに変換する必要
                        inventoryRepository.updateStockShippingRollback(orderItem.getItemId(), needSecureCount * -1);
                    }
                }
            }

            // 商品差分数量がある場合、確保済み配送商品の調整を行う
            if (needSecureCount != 0) {

                if (indexOfExistShippingItem >= 0) {
                    // 既に存在する配送商品の場合、確保済み配送商品数量を注文商品数量に更新する

                    if (orderItem.getOrderCount() > 0) {

                        SecuredShippingItem oldSecuredShippingItem =
                                        shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                                     .get(indexOfExistShippingItem);
                        SecuredShippingItem newSecuredShippingItem =
                                        new SecuredShippingItem(oldSecuredShippingItem.getShippingItemSeq(),
                                                                oldSecuredShippingItem.getItemId(),
                                                                oldSecuredShippingItem.getItemName(),
                                                                oldSecuredShippingItem.getUnitTitle1(),
                                                                oldSecuredShippingItem.getUnitValue1(),
                                                                oldSecuredShippingItem.getUnitTitle2(),
                                                                oldSecuredShippingItem.getUnitValue2(),
                                                                new ShippingCount(orderItem.getOrderCount())
                                        );

                        // 数量を更新した値オブジェクトの入れ替え
                        shippingSlipForRevisionEntity.getSecuredShippingItemList().remove(indexOfExistShippingItem);
                        shippingSlipForRevisionEntity.getSecuredShippingItemList()
                                                     .add(indexOfExistShippingItem, newSecuredShippingItem);

                    } else if (orderItem.getOrderCount() == 0) {

                        shippingSlipForRevisionEntity.getSecuredShippingItemList().remove(indexOfExistShippingItem);
                    }

                } else {
                    // 新規に追加された商品の場合

                    // 商品詳細情報取得
                    GoodsDetailsDto goodsDetailsDto =
                                    productAdapter.getGoodsDetailsDto(Integer.parseInt(orderItem.getItemId()));
                    if (goodsDetailsDto == null) {
                        throw new DomainException("LOGISTIC-GOOD0001-E");
                    }

                    // 確保済み配送商品追加
                    SecuredShippingItem securedShippingItem =
                                    new SecuredShippingItem(orderItem.getOrderItemSeq(), orderItem.getItemId(),
                                                            goodsDetailsDto.getGoodsGroupName(),
                                                            goodsDetailsDto.getUnitTitle1(),
                                                            goodsDetailsDto.getUnitValue1(),
                                                            goodsDetailsDto.getUnitTitle2(),
                                                            goodsDetailsDto.getUnitValue2(),
                                                            new ShippingCount(orderItem.getOrderCount())
                                    );
                    shippingSlipForRevisionEntity.getSecuredShippingItemList().add(securedShippingItem);
                }
            }
        }
    }
}