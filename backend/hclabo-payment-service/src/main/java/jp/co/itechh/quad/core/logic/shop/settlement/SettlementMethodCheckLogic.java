/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

/**
 * 決済方法チェックロジック
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodCheckLogic {

    /** 「代金引換」の請求種別には、「後請求」を選択してください。 */
    public static final String MSGCD_RECEIPT_PAYMENT_PRE_CLAIM = "LST001002";

    /** 決済種別が「クレジット」で請求種別が「{0}」の決済方法は既に登録されています。 */
    public static final String MSGCD_CREDIT_SAME_BILL_TYPE = "LST001003";

    /** 決済方法名称「{0}」は既に登録されているため、利用できません。 */
    public static final String MSGCD_NAME_EXIST = "LST001005";

    /** 当決済方法を指定している商品があります。削除された商品を含め当決済方法の指定を外してください。 */
    public static final String MSGCD_STATUS_DELETE_GOODS_EXIST = "LST001006";

    /** 当決済方法を指定している商品に、販売状態{0}=「販売中」の商品があります。該当する商品を「非販売」にするか、当決済方法の設定を外してください。 */
    public static final String MSGCD_STATUS_NO_OPEN_GOODS_EXIST = "LST001007";

    /** 決済種別が「{0}」の請求種別には、「{1}」を選択してください。 */
    public static final String MSGCD_STATUS_NO_SELECT_CLAIM = "LST001008";

    /** 決済種別が「{0}」の決済方法は、既に登録されています。 */
    public static final String MSGCD_METHOD_EXIST = "LST001009";

    /**
     * 実行メソッド
     *
     * @param settlementMethodEntity 決済方法エンティティ
     */
    void execute(SettlementMethodEntity settlementMethodEntity);

    /**
     * 実行メソッド
     *
     * @param modified 変更後オブジェクト
     * @param original 変更前オブジェクト
     */
    void execute(SettlementMethodEntity modified, SettlementMethodEntity original);
}
