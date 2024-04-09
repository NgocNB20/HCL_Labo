package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import jp.co.itechh.quad.ddd.usecase.query.AbstractQueryCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 受注検索条件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@Scope("prototype")
public class OrderSearchQueryCondition extends AbstractQueryCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 受注番号
     */
    private String orderCode;

    /**
     * 受注状態
     */
    private String orderStatus;

    /**
     * 異常フラグ
     */
    private String emergencyFlag;

    /**
     * キャンセルフラグ
     */
    private String cancelFlag;

    /**
     * 期間
     * <pre>
     * ・受注日    ："1"=期間From～期間To、"2"=期間From～、"3"=～期間To
     * ・出荷登録日："4"=期間From～期間To、"5"=期間From～、"6"=～期間To
     * ・入金日    ："7"=期間From～期間To、"8"=期間From～、"9"=～期間To
     * ・更新日    ："13"=期間From～期間To、"14"=期間From～、"15"=～期間To
     * ・支払期限日："16"=期間From～期間To、"17"=期間From～、"18"=～期間To
     * ・お届け希望日："19"=期間From～期間To、"20"=期間From～、"21"=～期間To
     * ・キャンセル日時："22"=期間From～期間To、"23"=期間From～、"24"=～期間To
     * </pre>
     */
    private String timeType;

    /**
     * 期間From
     */
    private Timestamp timeFrom;

    /**
     * 期間To
     */
    private Timestamp timeTo;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 氏名
     */
    private String searchNameEmUc;

    /**
     * 電話番号
     */
    private String searchTelEn;

    /**
     * お客様メールアドレス
     */
    private String customerMail;

    /**
     * 商品グループコード
     */
    private String goodsGroupCode;

    /**
     * 商品コード
     */
    private String goodsCode;

    /**
     * 商品名
     */
    private String goodsGroupName;

    /**
     * JANコード
     */
    private String janCode;

    /**
     * 出荷状態
     */
    private List<String> shipmentStatus;

    /**
     * 配送方法
     */
    private String deliveryMethod;

    /**
     * 伝票番号
     */
    private String deliveryCode;

    /**
     * 決済方法
     */
    private String settlememntMethod;

    /**
     * 受注番号（複数番号検索用）
     */
    private List<String> orderCodeList;

    /**
     * ノベルティ判定状態
     */
    private String noveltyPresentJudgmentStatus;

    /**
     * ページ番号
     */
    private Integer page;

    /**
     * 表示最大件数
     */
    private Integer limit;

    /**
     * ソート項目
     */
    private String orderBy;

    /**
     * ソート
     */
    private Boolean sort = true;

    /**
     * 受注金額From
     */
    private String orderPriceFrom;

    /**
     * 受注金額To
     */
    private String orderPriceTo;

    /**
     * 検査キット番号
     */
    private List<String> examKitCodeList;

    /**
     * 検体番号
     */
    private String specimenCode;

    /**
     * 検査状態
     */
    private String examStatus;

    /**
     * 受注商品単位で絞り込むフラグ
     */
    private Boolean filterOrderedProductFlag;

}
