/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.dto.cart.CartDto;
import jp.co.itechh.quad.front.dto.cart.ajax.CartResponseDto;
import jp.co.itechh.quad.front.dto.cart.ajax.CartResultDto;
import jp.co.itechh.quad.front.dto.common.CheckMessageDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カート追加（Ajax）Helperクラス
 * @author Pham Quang Dieu
 */
@Component
public class AjaxCartAddJSONHelper {

    /**
     * カート追加結果レスポンス作成
     *
     * @param validatorErrorList    バリデータエラーメッセージ
     * @param errorList             エラーメッセージ
     * @param cartDto               最新のカート情報
     * @param okSession             セッション情報がただしいか true:正しい / false:不正
     * @return カート追加結果
     */
    public CartResponseDto toCartIFResponse(List<CheckMessageDto> validatorErrorList,
                                            List<CheckMessageDto> errorList,
                                            CartDto cartDto,
                                            boolean okSession) {

        // カート追加結果の格納
        CartResultDto cartResultDto = ApplicationContextUtility.getBean(CartResultDto.class);
        cartResultDto.setOkSession(okSession);

        if (okSession && errorList.isEmpty() && validatorErrorList.isEmpty()) {
            // カート投入成功
            cartResultDto.setResult(true);
            cartResultDto.setValidatorErrorList(null);
            cartResultDto.setErrorList(null);
            cartResultDto.setCartInfo(cartDto);
        } else {
            // カート投入失敗
            cartResultDto.setResult(false);
            cartResultDto.setValidatorErrorList(validatorErrorList);
            cartResultDto.setErrorList(errorList);
            cartResultDto.setCartInfo(cartDto);
        }

        CartResponseDto cartResponseDto = ApplicationContextUtility.getBean(CartResponseDto.class);
        cartResponseDto.setCart(cartResultDto);

        return cartResponseDto;

    }
}