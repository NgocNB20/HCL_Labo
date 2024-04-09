/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.dia;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 配送方法設定：配送不可能エリア設定検索画面検索結果用Pageクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class DeliveryDiaModelItem implements Serializable {
    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 配送方法SEQ
     */
    private Integer deliveryMethodSeq;

    /**
     * 郵便番号
     */
    private String zipcode;

    /**
     * 住所一覧
     */
    private String addressList;

    /**
     * 選択チェックボックス
     */
    private boolean check;

    /**
     * 行番号
     */
    private int resultDataIndex;
}
