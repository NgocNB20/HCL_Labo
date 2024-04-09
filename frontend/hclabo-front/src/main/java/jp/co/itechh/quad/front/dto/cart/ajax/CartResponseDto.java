/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.dto.cart.ajax;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * カート追加レスポンス用Dtoクラス<br/>
 *
 * @author kaneda
 */
@Data
@Component
@Scope("prototype")
public class CartResponseDto {

    /** カート追加結果 */
    private CartResultDto cart;

}