/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

/**
 * HBothSidesSpaceValidatorの制御に用いる区分値<br/>
 * HVBothSidesSpaceの設定時に利用する<br/>
 *
 * @author matsumoto
 * @version $Revision: 1.1 $
 *
 */
public enum SpaceValidateMode {
    /** 半角スペースを許可しない */
    DENY_HALFWIDTH_SPACE(1),

    /** 全角スペースを許可しない */
    DENY_FULLWIDTH_SPACE(2),

    /** 半角スペース、全角スペースの両方を許可しない */
    DENY_SPACE(3),

    /** 半角スペース、全角スペースの両方を許可する */
    ALLOW_SPACE(4);

    /** 区分値のID */
    private int id;

    /**
     * 非公開のコンストラクタ<br/>
     *
     * @param id 区分値のID
     */
    private SpaceValidateMode(int id) {
        this.id = id;
    }

    /**
     * 区分値のIDを返す<br/>
     *
     * @return 区分値のID
     */
    public int getId() {
        return this.id;
    }
}