/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.dto.cart.ajax;

import jp.co.itechh.quad.front.dto.cart.CartDto;
import jp.co.itechh.quad.front.dto.common.CheckMessageDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カート追加結果Dtoクラス<br/>
 *
 * @author kaneda
 */
@Data
@Component
@Scope("prototype")
public class CartResultDto {
    /**
     * カート追加結果（true/false）
     */
    private boolean result;

    /**
     * バリデータエラーメッセージ
     */
    private List<CheckMessageDto> validatorErrorList;

    /**
     * カート追加エラーメッセージ
     */
    private List<CheckMessageDto> errorList;

    /**
     * 最新のカート情報
     */
    private CartDto cartInfo;

    /** セッション情報が正しいか true:正しい/false:不正 */
    private boolean okSession;

}