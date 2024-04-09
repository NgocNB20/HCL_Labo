/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 注文メッセージDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.5 $
 */
@Data
@Component
@Scope("prototype")
public class OrderMessageDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注商品詳細メッセージマップ：Key=注文連番, 子マップKey=商品SEQ */
    private Map<Integer, Map<Integer, List<CheckMessageDto>>> orderGoodsMessageMapMap;

    /** 受注商品詳細メッセージマップ：マップKey=商品SEQ */
    private Map<Integer, List<CheckMessageDto>> orderGoodsMessageMap;

    /** 注文メッセージリスト */
    private List<CheckMessageDto> orderMessageList;

    /**
     * @return エラー系のメッセージの有無
     */
    public boolean hasErrorMessage() {

        if (orderMessageList != null) {
            for (CheckMessageDto dto : orderMessageList) {
                if (dto.isError()) {
                    return true;
                }
            }
        }

        return hasGoodsErrorMessage();
    }

    /**
     * @return 商品系のエラーメッセージの有無
     */
    public boolean hasGoodsErrorMessage() {
        if (orderGoodsMessageMapMap != null) {
            Collection<Map<Integer, List<CheckMessageDto>>> values = orderGoodsMessageMapMap.values();
            for (Map<Integer, List<CheckMessageDto>> consecutivelist : values) {
                for (List<CheckMessageDto> dtoList : consecutivelist.values()) {
                    for (CheckMessageDto dto : dtoList) {
                        if (dto.isError()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}