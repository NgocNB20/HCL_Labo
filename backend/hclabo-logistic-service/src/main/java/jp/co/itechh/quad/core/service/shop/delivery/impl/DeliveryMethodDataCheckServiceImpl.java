/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDataCheckService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodGetService;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus.NO_OPEN;
import static jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus.OPEN;

/**
 * 配送方法データチェックサービス実装クラス<br/>
 * サービスコード：SSD0013
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Service
public class DeliveryMethodDataCheckServiceImpl extends AbstractShopService implements DeliveryMethodDataCheckService {

    /** メッセージコード：配送方法名が重複 */
    protected static final String MSGCD_DUPLICATIVE_NAME = "SSD001301";

    /** メッセージコード：公開状態PCを決済方法が原因で公開から非公開にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_NO_OPEN_PC_BY_SETTLEMENT = "SSD001302";

    /** メッセージコード：公開状態モバイルを決済方法が原因で公開から非公開にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_NO_OPEN_MB_BY_SETTLEMEN = "SSD001303";

    /** メッセージコード：公開状態PCを商品が原因で公開から非公開にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_NO_OPEN_PC_BY_GOODS = "SSD001304";

    /** メッセージコード：公開状態モバイルを商品が原因で公開から非公開にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_NO_OPEN_MB_BY_GOODS = "SSD001305";

    /** メッセージコード：公開状態PCを決済方法が原因で削除にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_DELETE_BY_SETTLEMENT = "SSD001306";

    /** メッセージコード：公開状態モバイルを商品が原因で削除にできない時 */
    protected static final String MSGCD_IMPOSSIBLE_DELETE_BY_GOODS = "SSD001307";

    /** 配送方法取得サービス */
    private final DeliveryMethodGetService deliveryMethodGetService;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** 配送方法データチェックサービス実装クラス Helper */
    private final DeliveryMethodDataCheckServiceHelper deliveryMethodDataCheckServiceHelper;

    /**
     * コンストラクター
     *
     * @param deliveryMethodGetService              配送方法取得サービス
     * @param settlementMethodApi                   決済方法API
     * @param deliveryMethodDataCheckServiceHelper  配送方法データチェックサービス実装クラス Helper
     */
    @Autowired
    public DeliveryMethodDataCheckServiceImpl(DeliveryMethodGetService deliveryMethodGetService,
                                              SettlementMethodApi settlementMethodApi,
                                              DeliveryMethodDataCheckServiceHelper deliveryMethodDataCheckServiceHelper) {
        this.deliveryMethodGetService = deliveryMethodGetService;
        this.settlementMethodApi = settlementMethodApi;
        this.deliveryMethodDataCheckServiceHelper = deliveryMethodDataCheckServiceHelper;
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     */
    @Override
    public void execute(DeliveryMethodEntity deliveryMethodEntity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodEntity", deliveryMethodEntity);

        // 名称重複チェック
        DeliveryMethodEntity deliveryMethodEntityDB = checkDuplicativeName(deliveryMethodEntity);
        // 公開状態チェック
        checkOpenStatus(deliveryMethodEntity, deliveryMethodEntityDB);

        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * 配送方法名の重複チェック
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return DBから取得した配送方法エンティティ
     */
    protected DeliveryMethodEntity checkDuplicativeName(DeliveryMethodEntity deliveryMethodEntity) {

        DeliveryMethodEntity deliveryMethodEntityBySeq = null;
        // 配送方法取得ロジック実行（配送方法名で取得）
        DeliveryMethodEntity deliveryMethodEntityByName =
                        deliveryMethodGetService.execute(deliveryMethodEntity.getDeliveryMethodName());

        // 登録時
        if (deliveryMethodEntity.getDeliveryMethodSeq() == null) {
            if (deliveryMethodEntityByName != null) {
                addErrorMessage(MSGCD_DUPLICATIVE_NAME);
            }

            // 更新時
        } else {
            // 配送方法取得ロジック実行（SEQで取得）
            deliveryMethodEntityBySeq = deliveryMethodGetService.execute(deliveryMethodEntity.getDeliveryMethodSeq());
            if (StringUtils.isNotEmpty(deliveryMethodEntity.getDeliveryMethodName())
                && !deliveryMethodEntity.getDeliveryMethodName()
                                        .equals(deliveryMethodEntityBySeq.getDeliveryMethodName())) {
                if (deliveryMethodEntityByName != null) {
                    addErrorMessage(MSGCD_DUPLICATIVE_NAME);
                }
            }
        }

        return deliveryMethodEntityBySeq;
    }

    /**
     * 公開状態チェック
     *
     * @param deliveryMethodEntity 画面の入力情報で更新された配送方法エンティティ
     * @param deliveryMethodEntityDB DBから取得した配送方法エンティティ
     */
    protected void checkOpenStatus(DeliveryMethodEntity deliveryMethodEntity,
                                   DeliveryMethodEntity deliveryMethodEntityDB) {
        // 更新処理の時は、公開状態のチェックを行う
        if (deliveryMethodEntity.getDeliveryMethodSeq() != null) {
            // 公開状態PC 公開 ⇒ 非公開
            if (OPEN.equals(deliveryMethodEntityDB.getOpenStatusPC()) && NO_OPEN.equals(
                            deliveryMethodEntity.getOpenStatusPC())) {
                // 決済方法取得サービス実行
                PaymentMethodListResponse paymentMethodListResponse = settlementMethodApi.get();
                List<SettlementMethodEntity> settlementMethodEntityList =
                                deliveryMethodDataCheckServiceHelper.toSettlementMethodEntityList(
                                                paymentMethodListResponse);
                for (SettlementMethodEntity settlementMethodEntity : settlementMethodEntityList) {
                    // この配送方法に紐づいている決済方法かどうか
                    if (settlementMethodEntity.getDeliveryMethodSeq() != null
                        && settlementMethodEntity.getDeliveryMethodSeq()
                                                 .equals(deliveryMethodEntity.getDeliveryMethodSeq())) {
                        // 公開中かどうか。
                        if (OPEN.equals(settlementMethodEntity.getOpenStatusPC())) {
                            addErrorMessage(MSGCD_IMPOSSIBLE_NO_OPEN_PC_BY_SETTLEMENT);
                            break;
                        }
                    }
                }
            }
        }
    }
}