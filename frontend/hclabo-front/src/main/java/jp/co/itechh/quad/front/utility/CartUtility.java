/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.utility;

import jp.co.itechh.quad.front.dto.cart.CartGoodsDto;
import jp.co.itechh.quad.front.entity.cart.CartGoodsEntity;
import jp.co.itechh.quad.front.pc.web.front.cart.CartModel;
import jp.co.itechh.quad.orderslip.presentation.api.param.ErrorContent;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * カート業務Utility
 *
 * @author kaneda
 */
@Component
public class CartUtility {

    /**
     * コンストラクタ<br/>
     */
    public CartUtility() {
        // nop
    }

    /**
     * カート商品チェックサービスの結果から、処理続行を許可する内容か否かを判断します。
     * <pre>
     * そのチェック結果と、カート商品チェックの結果より、処理続行の判断を行う。
     * </pre>
     *
     * @param errorMap メッセージマップ
     * @param cartGoodsDtoList カート商品DTOリスト
     * @return true..許可しない / false..許可する
     */
    public boolean isErrorAbortProcessing(Map<String, List<ErrorContent>> errorMap,
                                          List<CartGoodsDto> cartGoodsDtoList) {

        // カート商品チェックOKの場合は空のマップ情報が設定されるためfalseを設定し処理を終了する
        if (errorMap == null || errorMap.isEmpty()) {
            return false;
        }

        // 個別配送商品の場合、以下の追加チェックが必要
        String messageId = null;
        Integer goodsSeq = null;
        for (CartGoodsDto cartGoodsDto : cartGoodsDtoList) {
            // カート商品エンティティを取得
            CartGoodsEntity cartGoodsEntity = cartGoodsDto.getCartGoodsEntity();
            // 商品SEQ取得
            goodsSeq = cartGoodsEntity.getGoodsSeq();

            // メッセージマップからカートSEQに該当するメッセージリストを取得
            List<ErrorContent> checkMessageDtoList = errorMap.get(goodsSeq.toString());
            if (checkMessageDtoList != null) {
                for (ErrorContent errorContent : checkMessageDtoList) {
                    messageId = errorContent.getCode();

                    // エラーチェック（個別配送商品エラーでない）
                    if (!StringUtils.isEmpty(messageId) && !messageId.equals(CartModel.MSGCD_INDIVIDUAL_DELIVERY)) {
                        // 個別配送商品以外のエラーがあった時点で処理中断
                        return true;
                    }
                    // 個別配送商品と別の商品は同時購入はできない
                    if (CartModel.MSGCD_INDIVIDUAL_DELIVERY.equals(messageId)) {
                        for (CartGoodsDto cartGoodsDto2 : cartGoodsDtoList) {
                            if (ObjectUtils.isNotEmpty(cartGoodsDto2.getCartGoodsEntity())
                                && cartGoodsDto2.getCartGoodsEntity().getGoodsSeq() != null
                                && !cartGoodsDto2.getCartGoodsEntity().getGoodsSeq().equals(goodsSeq)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}